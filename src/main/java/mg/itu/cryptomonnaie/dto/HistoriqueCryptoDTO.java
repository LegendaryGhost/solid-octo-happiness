package mg.itu.cryptomonnaie.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.entity.TypeAction;
import mg.itu.cryptomonnaie.entity.Utilisateur;

@Data
public class HistoriqueCryptoDTO {
    private Utilisateur utilisateur;
    private Cryptomonnaie cryptomonnaie;
    private TypeAction typeAction;
    private LocalDateTime dateAction;
    private Double quantite;
    private Double prixAchatU;
    private Double prixActuel;
    private Double variation;
    private Double valeurActuelle;
    private Double profitOuPerte;
}
