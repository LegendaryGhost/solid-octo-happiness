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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_historique_fonds", "statut"}))
public class StatutHistoriqueFonds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutOperation statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeure;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_historique_fonds")
    private HistoriqueFonds historiqueFonds;
}
