package mg.itu.cryptomonnaie.utils;

import java.util.Map;

public interface FirestoreSynchronisableEntity {
    String getDocumentId();

    Map<String, Object> toMap();
}
