package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@Entity
public class CoursCrypto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private Double coursActuel;

    @Column(nullable = false)
    private LocalDateTime dateHeure;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cryptomonnaie")
    private Cryptomonnaie cryptomonnaie;
}
