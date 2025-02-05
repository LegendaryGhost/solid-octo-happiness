package mg.itu.cryptomonnaie.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.HistoriqueCrypto;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.HistoriqueCryptoRepository;

@RequiredArgsConstructor
@Service
public class HistoriqueCryptoService {
    private final HistoriqueCryptoRepository historiqueCryptoRepository;
    private final CoursCryptoRepository coursCryptoRepository;

    public List<HistoriqueCryptoDTO> portefeuilleClientActuel(final Utilisateur utilisateur) {
        List<HistoriqueCryptoDTO> portefeuilles = new ArrayList<>();

        List<HistoriqueCrypto> historiqueCryptos = historiqueCryptoRepository.findAllByProfil(utilisateur.getId());
        for (HistoriqueCrypto historiqueCrypto : historiqueCryptos) {
            HistoriqueCryptoDTO portefeuille = new HistoriqueCryptoDTO();
            portefeuille.setProfil(utilisateur);
            portefeuille.setCryptomonnaie(historiqueCrypto.getCryptomonnaie());
            portefeuille.setTypeAction(historiqueCrypto.getTypeAction());
            portefeuille.setDateAction(historiqueCrypto.getDateAction());
            portefeuille.setQuantite(historiqueCrypto.getQuantite());
            CoursCrypto coursCryptoActuel = coursCryptoRepository
                    .findFirstByCryptomonnaieIdOrderByDateCoursDesc(portefeuille.getCryptomonnaie().getId());
            portefeuille.setPrixActuel(coursCryptoActuel.getCoursActuel());
            portefeuille.setPrixAchatU(historiqueCrypto.getCours());
            Double variation = variationCours(portefeuille.getPrixActuel(), portefeuille.getPrixAchatU());
            portefeuille.setVariation(variation);
            Double valActuel = valeurActuelle(historiqueCrypto.getQuantite(), portefeuille.getPrixActuel());
            portefeuille.setValeurActuelle(valActuel);
            Double profitouPerte = profitOuPerte(valActuel, historiqueCrypto.getQuantite(),
                    historiqueCrypto.getCours());
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

    public List<HistoriqueCrypto> historiqueUtilisateur(Utilisateur utilisateur) {
        return historiqueCryptoRepository.findAllByProfil(utilisateur.getId());
    }

    public List<HistoriqueCrypto> historiqueGlobale() {
        return historiqueCryptoRepository.findAllByOrderByDateActionDesc();
    }
}
