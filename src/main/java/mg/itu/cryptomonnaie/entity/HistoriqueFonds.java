package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.enums.TypeOperation;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
public class HistoriqueFonds {
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
    private LocalDateTime dateHeure;

    @Setter
    @Enumerated(EnumType.STRING)
    private TypeOperation typeOperation;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
}
