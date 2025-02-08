package mg.itu.cryptomonnaie.utils;

import com.google.cloud.firestore.DocumentSnapshot;

import java.util.Map;

public interface FirestoreSynchronisableEntity {
    String getCollectionName();

    String getId();

    Map<String, Object> toMap();

    void fromDocumentSnapshot(final DocumentSnapshot documentSnapshot);
}
