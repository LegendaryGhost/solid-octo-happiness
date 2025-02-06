package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_utilisateur", "id_cryptomonnaie"}))
public class Portefeuille {
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
}
