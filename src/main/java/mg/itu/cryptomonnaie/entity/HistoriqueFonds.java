package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
public class HistoriqueFonds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String numCarteBancaire;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_operation")
    private TypeOperation typeOperation;
}
