package mg.itu.cryptomonnaie.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirestoreService {
    private final Firestore firestore;

    public <T extends FirestoreSynchronisableEntity> void synchronizeLocalDbToFirestore(T entity) {
        try {
            firestore.collection(entity.getCollectionName())
                .document(entity.getId())
                .set(entity.toMap())
                .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Erreur lors de la synchronisation vers Firestore pour l'entité : " + entity.getCollectionName(), e);
        }
    }

    public void synchronizeFirestoreToLocalDb() {
        
    }

    private <T extends FirestoreSynchronisableEntity> void synchronizeFirestoreToLocalDb(T entity) {
        String collectionName = entity.getCollectionName();
        firestore.collection(collectionName).addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                log.error("Erreur lors de l'écoute des changements pour l'entité : \"{}\"", collectionName, e);
                return;
            }
            if (snapshots == null) return;

            snapshots.getDocumentChanges().forEach(documentChange -> {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                switch (documentChange.getType()) {
                    case ADDED:
                    case MODIFIED:
                        entity.fromDocumentSnapshot(documentSnapshot);
                        break;
                    case REMOVED:
                        // Do nothing
                        break;
                }
            });
        });
    }


}
