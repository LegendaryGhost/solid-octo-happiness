package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(length = 75, nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private Double fondsActuel;

    @Setter
    private String token;

    @Column(nullable = false)
    private LocalDateTime dateHeureMaj;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeureCreation;
}
