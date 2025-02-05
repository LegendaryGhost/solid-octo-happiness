package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
public class HistoriqueTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private Double cours;

    @Setter
    @Column(nullable = false)
    private Float quantite;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_transaction")
    private TypeTransaction typeTransaction;
}
