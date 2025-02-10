package mg.itu.cryptomonnaie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.*;
import mg.itu.cryptomonnaie.utils.Facade;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static mg.itu.cryptomonnaie.utils.FirestoreUtils.*;
import static mg.itu.cryptomonnaie.utils.FirestoreUtils.convertGoogleCloudTimestampToLocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirestoreService {
    private final Firestore firestore;
    @PersistenceContext
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    private List<ListenerRegistration> listenerRegistrations;

    @EventListener(ApplicationReadyEvent.class)
    public void initListenerRegistrations() {
        log.info("Initialisation des listeners Firestore");

        listenerRegistrations = new ArrayList<>();
        this.addRegistrationListener(Operation.class)
            .addRegistrationListener(CryptoFavoris.class);

        log.info("Listeners Firestore initialisés avec succès");
    }

    @EventListener(ContextClosedEvent.class)
    public void destroyListenerRegistrations() {
        log.info("Destruction des listeners Firestore");

        listenerRegistrations.forEach(ListenerRegistration::remove);
        listenerRegistrations.clear();

        log.info("Listeners Firestore correctement détruits");
    }

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(final List<T> entities) {
        synchronizeLocalDbToFirestore(entities, false);
    }

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(
        final List<T> entities, final boolean delete
    ) {
        entities.forEach(entity -> synchronizeLocalDbToFirestore(entity, delete));
    }

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(final T entity) {
        synchronizeLocalDbToFirestore(entity, false);
    }

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(
        final T entity, final boolean delete
    ) {
        final Class<? extends FirestoreSynchronisableEntity> entityClass = entity.getClass();
        final String collectionName  = getCollectionName(entityClass);
        final String entityClassName = entityClass.getName();

        final DocumentReference documentReference = firestore.collection(collectionName).document(entity.getDocumentId());
        try {
            if (delete) documentReference.delete().get();
            else {
                Map<String, Object> map = entity.toMap();
                if (documentReference.get().get().exists())
                     documentReference.update(map).get();
                else documentReference.set(map).get();
            }

            log.debug("Synchronisation de la base de données locale vers Firestore pour l'entité : \"{}\"", entityClassName);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Erreur lors de la {} vers Firestore pour l'entité : {}",
                delete ? "suppression" : "synchronisation", entityClassName, e);
        }
    }

    /*
        Petite remarque sur cette méthode :

        La synchronisation de firestore → Base de données locale ne nécessite pas d'implémenter "FirestoreSynchronisableEntity".
        Il suffit que la classe soit une entité et une collection.
     */
    @SuppressWarnings("unchecked")
    private <T, ID> FirestoreService addRegistrationListener(final Class<T> entityClass) {
        final String collectionName = getCollectionName(entityClass);
        listenerRegistrations.add(firestore.collection(collectionName).addSnapshotListener((snapshots, e) -> {
            final String entityClassName = entityClass.getName();
            if (e != null) {
                log.error("Erreur lors de l'écoute des changements pour l'entité : \"{}\"", entityClassName, e);
                return;
            }
            if (snapshots == null) return;

            final JpaRepository<T, ID> repository = Facade.getRepositoryFor(entityClass);
            snapshots.getDocumentChanges().forEach(documentChange -> {
                try {
                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
                    switch (documentChange.getType()) {
                        case ADDED, MODIFIED -> {
                            T t = createEntityFromDocumentSnapshot(documentSnapshot, entityClass, objectMapper, entityManager);
                            if (t != null) repository.save(t);
                        }
                        case REMOVED -> {
                            ID id = (ID) convertId(
                                documentSnapshot.getId(), collectionName, entityClass, entityManager);
                            if (id != null) repository.deleteById(id);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Erreur lors du process des changements de document pour la collection : \"{}\"", collectionName, ex);
                }
            });
        }));

        return this;
    }

    /*
        On assume que le format des documents ressemblent au format de l'entité.
        C'est la condition sine qua non pour faire fonctionner la méthode
     */
    @Nullable
    private static <T> T createEntityFromDocumentSnapshot(
        final DocumentSnapshot documentSnapshot,
        final Class<T> entityClass,
        final ObjectMapper objectMapper,
        final EntityManager entityManager
    ) {
        final Map<String, Object> data = documentSnapshot.getData();

        final String collectionName = documentSnapshot.getReference().getParent().getId();
        final String id = documentSnapshot.getId();
        if (data == null) {
            log.warn("Les données du document avec l'identifiant \"{}\" dans la collection \"{}\" sont \"null\"", id, collectionName);
            return null;
        }

        // Nettoyage des données
        data.put("id", convertId(id, collectionName, entityClass, entityManager));
        data.forEach((k, v) -> {
            if (k.equalsIgnoreCase("id") || !(v instanceof Timestamp timestamp)) return;
            data.put(k, convertGoogleCloudTimestampToLocalDateTime(timestamp));
        });

        return objectMapper.convertValue(data, entityClass);
    }

    @Nullable
    private static Object convertId(
        final String id,
        final String collectionName,
        final Class<?> entityClass,
        final EntityManager entityManager
    ) {
        final Class<?> idType = entityManager.getMetamodel()
            .entity(entityClass)
            .getIdType()
            .getJavaType();
        String idTypeSimpleName = idType.getSimpleName();
        try {
            if (idType == String.class) return id;
            if (idType == Long.class    || idType == long.class)   return Long.parseLong(id);
            if (idType == Integer.class || idType == int.class)    return Integer.parseInt(id);
            if (idType == Double.class  || idType == double.class) return Double.parseDouble(id);
            if (idType == Float.class   || idType == float.class)  return Float.parseFloat(id);

            log.error("Type d'identifiant non supporté pour la conversion : {}", idTypeSimpleName);
            return null;
        } catch (NumberFormatException e) {
            log.error("Impossible de convertir l'identifiant \"{}\" de la collection \"{}\" en type \"{}\"",
                id, collectionName, idTypeSimpleName);
            return null;
        }
    }
}
