package mg.itu.cryptomonnaie.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.HistoriqueTransaction;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.HistoriqueTransactionRepository;

@RequiredArgsConstructor
@Service
public class HistoriqueCryptoService {
    private final HistoriqueTransactionRepository historiqueTransactionRepository;
    private final CoursCryptoRepository coursCryptoRepository;

    public List<HistoriqueCryptoDTO> portefeuilleClientActuel(final Utilisateur utilisateur) {
        List<HistoriqueCryptoDTO> portefeuilles = new ArrayList<>();

        List<HistoriqueTransaction> historiqueTransactions = historiqueTransactionRepository.findAllByProfil(utilisateur.getId());
        for (HistoriqueTransaction historiqueTransaction : historiqueTransactions) {
            HistoriqueCryptoDTO portefeuille = new HistoriqueCryptoDTO();
            portefeuille.setProfil(utilisateur);
            portefeuille.setCryptomonnaie(historiqueTransaction.getCryptomonnaie());
            portefeuille.setTypeOperation(historiqueTransaction.getTypeAction());
            portefeuille.setDateAction(historiqueTransaction.getDateHeure());
            portefeuille.setQuantite(historiqueTransaction.getQuantite());
            CoursCrypto coursCryptoActuel = coursCryptoRepository
                    .findFirstByCryptomonnaieIdOrderByDateHeureDesc(portefeuille.getCryptomonnaie().getId());
            portefeuille.setPrixActuel(coursCryptoActuel.getCoursActuel());
            portefeuille.setPrixAchatU(historiqueTransaction.getCours());
            Double variation = variationCours(portefeuille.getPrixActuel(), portefeuille.getPrixAchatU());
            portefeuille.setVariation(variation);
            Double valActuel = valeurActuelle(historiqueTransaction.getQuantite(), portefeuille.getPrixActuel());
            portefeuille.setValeurActuelle(valActuel);
            Double profitouPerte = profitOuPerte(valActuel, historiqueTransaction.getQuantite(),
                    historiqueTransaction.getCours());
            portefeuille.setProfitOuPerte(profitouPerte);

            portefeuilles.add(portefeuille);
        }
        return portefeuilles;
    }

    public Double variationCours(Double actuel, Double achat) {
        return ((actuel - achat) / achat) * 100;
    }

    public Double valeurActuelle(Double quantite, Double prixActuel) {
        return quantite * prixActuel;
    }

    public Double profitOuPerte(Double valActue, Double qtt, Double pAchat) {
        return valActue - (qtt * pAchat);
    }

    public List<HistoriqueTransaction> historiqueUtilisateur(Utilisateur utilisateur) {
        return historiqueTransactionRepository.findAllByProfil(utilisateur.getId());
    }

    public List<HistoriqueTransaction> historiqueGlobale() {
        return historiqueTransactionRepository.findAllByOrderByDateHeureDesc();
    }
}
