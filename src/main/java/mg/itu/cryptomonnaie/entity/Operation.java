package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.enums.TypeOperation;
import mg.itu.cryptomonnaie.utils.Collection;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Collection
public class Operation implements FirestoreSynchronisableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(length = 50, nullable = false)
    private String numCarteBancaire;

    @Setter
    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOperation typeOperation;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @Override
    public String getDocumentId() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("numCarteBancaire", numCarteBancaire);
        map.put("montant", montant);
        map.put("dateHeure", dateHeure);
        map.put("typeOperation", typeOperation);
        map.put("idUtilisateur", utilisateur != null ? utilisateur.getId() : null);

        return map;
    }
}
