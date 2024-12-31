package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "historique_fond")
public class HistoriqueFond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime dateTransaction;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal montant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_profil")
    private Profil profil;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_transaction")
    private TypeTransaction typeTransaction;
}
