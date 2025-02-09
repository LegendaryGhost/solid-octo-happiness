package mg.itu.cryptomonnaie.exceptions;

import lombok.Getter;

@Getter
public class UnparsableDocumentSnapshotIdToEntityIdTypeException extends RuntimeException {
    private final String documentSnapshotId;
    private final String documentSnapshotCollectionName;
    private final Class<?> entityClass;
    private final String entityIdTypeSimpleName;

    public UnparsableDocumentSnapshotIdToEntityIdTypeException(
        final String documentSnapshotId,
        final String documentSnapshotCollectionName,
        final Class<?> entityClass,
        final String entityIdTypeSimpleName
    ) {
        super(String.format("Impossible de convertir l'identifiant \"%s\" du document de la collection \"%s\" " +
                "l'identifiant de l'entité \"%s\" de type \"%s\"",
            documentSnapshotId, documentSnapshotCollectionName, entityClass.getName(), entityIdTypeSimpleName
        ));

        this.documentSnapshotId = documentSnapshotId;
        this.documentSnapshotCollectionName = documentSnapshotCollectionName;
        this.entityClass = entityClass;
        this.entityIdTypeSimpleName = entityIdTypeSimpleName;
    }
}
