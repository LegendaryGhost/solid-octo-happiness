package mg.itu.cryptomonnaie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import mg.itu.cryptomonnaie.utils.Utils;
import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.HistoriqueCrypto;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.HistoriqueCryptoRepository;

@Service
public class HistoriqueCryptoService {
    @Autowired
    private HistoriqueCryptoRepository historiqueCryptoRepository;

    @Autowired
    private CoursCryptoRepository coursCryptoRepository;

    @Autowired
    private ProfilService profilService;

    public List<HistoriqueCryptoDTO> portefeuilleClientActuel(HttpSession session) {
        List<HistoriqueCryptoDTO> portefeuilles = new ArrayList<>();
        Profil profil = Utils.getUser(session);

        List<HistoriqueCrypto> historiqueCryptos = historiqueCryptoRepository.findAllByProfil(profil.getId());
        for (HistoriqueCrypto historiqueCrypto : historiqueCryptos) {
            HistoriqueCryptoDTO portefeuille = new HistoriqueCryptoDTO();
            portefeuille.setProfil(profil);
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

    public List<HistoriqueCrypto> historiqueUtilisateur(Profil profil) {
        return historiqueCryptoRepository.findAllByProfil(profil.getId());
    }

    public List<HistoriqueCrypto> historiqueGlobale() {
        return historiqueCryptoRepository.findAllByOrderByDateActionDesc();
    }

}
