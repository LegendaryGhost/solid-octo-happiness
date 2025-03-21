package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mg.itu.cryptomonnaie.enums.StatutOperation;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_operation", "statut"}))
public class SuiviOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private StatutOperation statut;

    @Setter
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeure;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_operation")
    private Operation operation;
}
