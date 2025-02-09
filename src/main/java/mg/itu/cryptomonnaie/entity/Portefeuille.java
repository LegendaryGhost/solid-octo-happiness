package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.utils.Collection;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import org.hibernate.annotations.DynamicInsert;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_utilisateur", "id_cryptomonnaie"}))
@DynamicInsert
@Collection
public class Portefeuille implements FirestoreSynchronisableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private Float quantite;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;

    @Override
    public String getDocumentId() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("quantite", quantite);
        map.put("idUtilisateur", utilisateur != null ? utilisateur.getId() : null);
        map.put("idCryptomonnaie", cryptomonnaie != null ? cryptomonnaie.getId() : null);
        
        return map;
    }
}
