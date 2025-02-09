package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.utils.Collection;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import mg.itu.cryptomonnaie.utils.FirestoreUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Collection("coursCrypto")
public class CoursCrypto implements FirestoreSynchronisableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private Double cours;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeure = LocalDateTime.now();

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
        map.put("cours", cours);
        map.put("dateHeure", FirestoreUtils.convertLocalDateTimeToGoogleCloudTimestamp(dateHeure));
        map.put("idCryptomonnaie", cryptomonnaie != null ? cryptomonnaie.getId() : null);

        return map;
    }
}
