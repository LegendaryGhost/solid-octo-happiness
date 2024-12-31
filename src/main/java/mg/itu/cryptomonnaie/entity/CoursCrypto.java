package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cours_crypto")
public class CoursCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cours_actuel", precision = 15, scale = 2, nullable = false)
    private BigDecimal coursActuel;

    @Column(name = "date_cours", nullable = false)
    private LocalDateTime dateCours;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;
}
