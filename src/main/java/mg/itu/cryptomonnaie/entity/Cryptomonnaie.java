package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
public class Cryptomonnaie implements FirestoreSynchronisableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false, unique = true)
    private String designation;

    @Column(length = 10, nullable = false, unique = true)
    private String symbole;

    @Override
    public String getCollectionName() {
        return "cryptomonnaie";
    }

    @Override
    public String getDocumentId() {
        return String.valueOf(id);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("designation", designation);
        map.put("symbole", symbole);

        return map;
    }
}
