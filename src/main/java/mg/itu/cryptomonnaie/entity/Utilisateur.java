package mg.itu.cryptomonnaie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.utils.Collection;
import mg.itu.cryptomonnaie.utils.FirestoreSynchronisableEntity;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@DynamicInsert
@Collection("profil")
public class Utilisateur implements FirestoreSynchronisableEntity {
    @Setter
    @Id
    private String id;

    @Setter
    @Column(length = 75, nullable = false, unique = true)
    private String email;

    @Setter
    @Column(length = 75, nullable = false)
    private String nom;

    @Setter
    @Column(length = 75, nullable = false)
    private String prenom;

    @Setter
    @Column(nullable = false)
    private LocalDate dateNaissance;

    @Setter
    private String pdp;

    @Setter
    @Column(nullable = false)
    private Double fondsActuel;

    @JsonIgnore
    @Setter
    @Column(name = "identityflow_token", columnDefinition = "TEXT")
    private String identityFlowToken;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String expoPushToken;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeureCreation;

    @Override
    public String getDocumentId() {
        return id;
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("fondsActuel", fondsActuel);
    }
}
