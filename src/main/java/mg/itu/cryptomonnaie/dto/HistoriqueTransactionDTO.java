package mg.itu.cryptomonnaie.dto;

import lombok.Data;
import mg.itu.cryptomonnaie.enums.TypeTransaction;

import java.time.LocalDateTime;

@Data
public final class HistoriqueTransactionDTO {
    private final String idUtilisateur;
    private final String  emailUtilisateur;
    private final Integer idCryptomonnaie;
    private final String  designationCryptomonnaie;
    private final TypeTransaction typeTransaction;
    private final Float quantite;
    private final LocalDateTime dateHeure;
    private final Double cours;
    private final Double coursActuel;
    private final Double valeurActuelle;
    private final Double variation;
    private final Double profitOuPerte;

    public HistoriqueTransactionDTO(
        final String  idUtilisateur,
        final String  emailUtilisateur,
        final Integer idCryptomonnaie,
        final String  designationCryptomonnaie,
        final TypeTransaction typeTransaction,
        final Float quantite,
        final LocalDateTime dateHeure,
        final Double cours,
        final Double coursActuel
    ) {
        this.idUtilisateur    = idUtilisateur;
        this.emailUtilisateur = emailUtilisateur;
        this.idCryptomonnaie  = idCryptomonnaie;
        this.designationCryptomonnaie = designationCryptomonnaie;
        this.typeTransaction = typeTransaction;
        this.quantite  = quantite;
        this.dateHeure = dateHeure;
        this.cours = cours;
        this.coursActuel = coursActuel;

        valeurActuelle = quantite * coursActuel;
        variation      = ((coursActuel - cours) / cours) * 100;
        profitOuPerte  = valeurActuelle - (quantite * cours);
    }
}
