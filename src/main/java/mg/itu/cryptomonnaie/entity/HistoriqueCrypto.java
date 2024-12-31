package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "historique_crypto")
public class HistoriqueCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal cours;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal montant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_profil")
    private Profil profil;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_action")
    private TypeAction typeAction;
}
