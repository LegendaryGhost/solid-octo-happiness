package mg.itu.cryptomonnaie.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;

import jakarta.persistence.EntityManager;
import mg.itu.cryptomonnaie.exceptions.UnparsableDocumentSnapshotIdToEntityIdTypeException;

import java.util.Map;

import static mg.itu.cryptomonnaie.utils.FirestoreUtils.*;

public interface FirestoreSynchronisableEntity {
    String getCollectionName();

    String getDocumentId();

    Map<String, Object> toMap();

    static <T extends FirestoreSynchronisableEntity> T createFromDocumentSnapshot(
        final DocumentSnapshot documentSnapshot,
        final Class<T> entityClass,
        final ObjectMapper objectMapper,
        final EntityManager entityManager
    ) {
        final Map<String, Object> data = documentSnapshot.getData();

        final String collectionName = documentSnapshotCollectionName(documentSnapshot);
        final String id = documentSnapshot.getId();
        if (data == null) throw new RuntimeException(String.format(
            "Les données du document avec l'identifiant \"%s\" dans la collection \"%s\" sont \"null\"", id, collectionName));

        // Nettoyage des données
        data.put("id", convertId(id, collectionName, entityClass, entityManager));
        data.forEach((k, v) -> {
            if (k.equalsIgnoreCase("id") || !(v instanceof Timestamp timestamp)) return;
            data.put(k, convertGoogleCloudTimestampToLocalDateTime(timestamp));
        });

        return objectMapper.convertValue(data, entityClass);
    }

    static Object convertId(
        final String id,
        final String collectionName,
        final Class<?> entityClass,
        final EntityManager entityManager
    ) {
        final Class<?> idType = getEntityIdType(entityClass, entityManager);
        String idTypeSimpleName = idType.getSimpleName();
        try {
            if (idType == String.class) return id;
            if (idType == Long.class    || idType == long.class)   return Long.parseLong(id);
            if (idType == Integer.class || idType == int.class)    return Integer.parseInt(id);
            if (idType == Double.class  || idType == double.class) return Double.parseDouble(id);
            if (idType == Float.class   || idType == float.class)  return Float.parseFloat(id);
            throw new IllegalArgumentException("Type d'identifiant non supporté pour la conversion : " + idTypeSimpleName);
        } catch (NumberFormatException e) {
            throw new UnparsableDocumentSnapshotIdToEntityIdTypeException(id, collectionName, entityClass, idTypeSimpleName);
        }
    }
}
