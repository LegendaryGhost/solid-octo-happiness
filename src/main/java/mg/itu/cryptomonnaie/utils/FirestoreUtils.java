package mg.itu.cryptomonnaie.utils;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import mg.itu.cryptomonnaie.entity.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class FirestoreUtils {
    private FirestoreUtils() { }

    public static final CollectionRegister COLLECTION_REGISTER = new CollectionRegister()
        .addCollection(CoursCrypto.class)
        .addCollection(Cryptomonnaie.class)
        .addCollection(Operation.class)
        .addCollection(Portefeuille.class)
        .addCollection(Transaction.class);

    public static LocalDateTime convertGoogleCloudTimestampToLocalDateTime(final Timestamp timestamp) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
            ZoneId.systemDefault());
    }

    public static Timestamp convertLocalDateTimeToGoogleCloudTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.ofTimeSecondsAndNanos(localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond(), localDateTime.getNano());
    }

    public static String documentSnapshotCollectionName(final DocumentSnapshot documentSnapshot) {
        return documentSnapshot.getReference().getParent().getId();
    }

    public static String getCollectionName(final Class<? extends FirestoreSynchronisableEntity> c) {
        return COLLECTION_REGISTER.findByCollectionClass(c).getCollectionName();
    }

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class CollectionMetaDataHolder {
        private final Class<? extends FirestoreSynchronisableEntity> clazz;
        private String collectionName;

        private CollectionMetaDataHolder(Class<? extends FirestoreSynchronisableEntity> clazz) {
            Assert.notNull(clazz, "La classe ne peut pas être \"null\"");
            this.clazz = clazz;

            setCollectionName();
        }

        private void setCollectionName() {
            final String lowerCasedClassName = clazz.getSimpleName().toLowerCase();

            if (!clazz.isAnnotationPresent(Collection.class)) collectionName = lowerCasedClassName;
            Collection collection = clazz.getAnnotation(Collection.class);

            final String collectionValue = collection.value();
            if (!StringUtils.hasText(collectionValue)) collectionName = lowerCasedClassName;
            else collectionName = collectionValue;
        }
    }

    @Getter
    public static class CollectionRegister {
        private final List<CollectionMetaDataHolder> collectionMetaDataHolders;

        public CollectionRegister() {
            collectionMetaDataHolders = new ArrayList<>();
        }

        public CollectionMetaDataHolder findByCollectionClass(final Class<? extends FirestoreSynchronisableEntity> c) {
            return collectionMetaDataHolders.stream()
                .filter(holder -> holder.getClazz().equals(c))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    String.format("Aucune classe trouvée pour \"%s\"", c.getName())
                ));
        }

        CollectionRegister addCollection(final Class<? extends FirestoreSynchronisableEntity> c) {
            final CollectionMetaDataHolder collectionMetaDataHolder = new CollectionMetaDataHolder(c);

            final boolean isDuplicate = collectionMetaDataHolders.stream()
                .anyMatch(holder -> holder.equals(collectionMetaDataHolder));

            if (isDuplicate) throw new IllegalArgumentException(String.format(
                "Une collectionMetaDataHolder avec le nom \"%s\" et la classe \"%s\" existe déjà",
                collectionMetaDataHolder.getCollectionName(), collectionMetaDataHolder.getClazz()));

            collectionMetaDataHolders.add(collectionMetaDataHolder);
            return this;
        }
    }
}
