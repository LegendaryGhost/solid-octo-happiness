package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.utils.Collection;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/*
    ###### PETIT SOUCIS MAIS ACCEPTABLE ######
    ⇒ Cette table doit être une relation "many to many" normalement pas une entité à part entière.
 */

@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_utilisateur", "id_cryptomonnaie"}))
@Collection("favoris")
public class CryptoFavoris {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;

//    @Version
//    private Integer version;
}
