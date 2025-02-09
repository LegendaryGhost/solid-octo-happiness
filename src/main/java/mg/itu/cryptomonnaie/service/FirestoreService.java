package mg.itu.cryptomonnaie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import mg.itu.cryptomonnaie.utils.FirestoreUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirestoreService {
    private final Firestore firestore;
    @PersistenceContext
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(
        final T entity, final boolean delete
    ) {
        final String collectionName = entity.getCollectionName();
        final DocumentReference documentReference = firestore.collection(collectionName).document(entity.getId());
        try {
            if (delete) documentReference.delete().get();
            else documentReference.set(entity.toMap()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(String.format("Erreur lors de la %s vers Firestore pour l'entité : %s",
                delete ? "suppression" : "synchronisation", entity.getClass().getName()
            ), e);
        }
    }

    public void synchronizeFirestoreToLocalDb() {

    }

    private <T extends FirestoreSynchronisableEntity> void synchronizeFirestoreToLocalDb(
        final String collectionName,
        final Class<T> entityClass,
        final JpaRepository<T, Object> repository
    ) {
        firestore.collection(collectionName).addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                log.error("Erreur lors de l'écoute des changements pour l'entité : \"{}\"", entityClass.getName(), e);
                return;
            }
            if (snapshots == null) return;

            snapshots.getDocumentChanges().forEach(documentChange -> {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                switch (documentChange.getType()) {
                    case ADDED, MODIFIED ->
                        repository.save(FirestoreSynchronisableEntity.createFromDocumentSnapshot(documentSnapshot, entityClass, objectMapper, entityManager));
                    case REMOVED -> repository.deleteById(FirestoreSynchronisableEntity.convertId(
                        documentSnapshot.getId(), FirestoreUtils.getCollectionName(documentSnapshot), entityClass, entityManager));
                }
            });
        });
    }
}
