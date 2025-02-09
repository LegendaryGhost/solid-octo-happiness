package mg.itu.cryptomonnaie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@DynamicInsert
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(length = 75, nullable = false, unique = true)
    private String email;

    @Setter
    private String pdp;

    @Setter
    @Column(nullable = false)
    private Double fondsActuel;

    @JsonIgnore
    @Setter
    private String token;

    @JsonIgnore
    @Column(nullable = false)
    private LocalDateTime dateHeureMaj;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeureCreation;
}
