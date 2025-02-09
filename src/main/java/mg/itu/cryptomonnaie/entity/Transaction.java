package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.utils.Collection;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import mg.itu.cryptomonnaie.utils.FirestoreUtils;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Table(name = "_transaction")
@DynamicInsert
@Collection
public class Transaction implements FirestoreSynchronisableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private Float quantite;

    @Setter
    @Column(nullable = false)
    private Double cours;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;

    @Setter
    @Column(nullable = false)
    private Double tauxCommission;

    @Column(nullable = false)
    private Double montantCommission;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    public void calculerMontantCommission() {
        montantCommission = quantite != null && cours != null && tauxCommission != null ?
            quantite * cours * (tauxCommission / 100) : 0;
    }

    @Override
    public String getDocumentId() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("quantite", quantite);
        map.put("cours", cours);
        map.put("dateHeure", FirestoreUtils.convertLocalDateTimeToGoogleCloudTimestamp(dateHeure));
        map.put("typeTransaction", typeTransaction);
        map.put("cryptomonnaie", cryptomonnaie != null ? cryptomonnaie.getDesignation() : null);
        map.put("idUtilisateur", utilisateur != null ? utilisateur.getId() : null);

        return map;
    }
}
