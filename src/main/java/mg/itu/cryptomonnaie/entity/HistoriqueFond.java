package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "historique_fond")
public class HistoriqueFond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime dateTransaction = LocalDateTime.now();

    @Column(nullable = false)
    private Double montant;

    @Column(name = "num_carte_bancaire", nullable = false)
    private String numCarteBancaire;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profil")
    private Profil profil;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_transaction")
    private TypeTransaction typeTransaction;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etat")
    private EtatFond etatFond;

    // @JsonIgnore // Ajout de @JsonIgnore
    // public Profil getProfil() {
    // return profil;
    // }

    // @JsonIgnore
    // public TypeTransaction getTypeTransaction() {
    // return typeTransaction;
    // }

    // // @JsonIgnore
    // // public EtatFond getEtatFond() {
    // // return etatFond;
    // // }
}
