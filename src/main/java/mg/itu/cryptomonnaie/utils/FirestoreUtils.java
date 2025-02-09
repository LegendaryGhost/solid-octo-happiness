package mg.itu.cryptomonnaie.utils;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class FirestoreUtils {
    private FirestoreUtils() { }

    public static LocalDateTime convertGoogleCloudTimestampToLocalDateTime(final Timestamp timestamp) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
            ZoneId.systemDefault());
    }

    public static Timestamp convertLocalDateTimeToGoogleCloudTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.ofTimeSecondsAndNanos(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(), localDateTime.getNano());
    }

    public static String getCollectionName(final DocumentSnapshot documentSnapshot) {
        return documentSnapshot.getReference().getParent().getId();
    }

    public static Class<?> getEntityIdType(final Class<?> entityClass, final EntityManager entityManager) {
        return entityManager.getMetamodel()
            .entity(entityClass)
            .getId(entityClass)
            .getJavaType();
    }
}
