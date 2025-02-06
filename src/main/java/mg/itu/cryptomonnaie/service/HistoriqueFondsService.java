package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFonds;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.HistoriqueFondsRepository;
import mg.itu.cryptomonnaie.request.HistoriqueFondsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@RequiredArgsConstructor
@Service
public class HistoriqueFondsService {
    public static final String HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY = "_historiques_fonds_temp";

    private final HistoriqueFondsRepository historiqueFondsRepository;
    private final UtilisateurService utilisateurService;
    private final EmailService emailService;

    public List<HistoriqueFonds> transactionProfil(final Utilisateur utilisateur) {
        return historiqueFondsRepository.findTransactionsProfil(Long.valueOf(utilisateur.getId()));
    }

    public void creerHistoriqueFondsTemporaire(
        final HistoriqueFondsRequest request, final Utilisateur utilisateur
    ) throws MessagingException {
        HistoriqueFonds historiqueFonds = new HistoriqueFonds();
        historiqueFonds.setNumCarteBancaire(request.getNumCarteBancaire());
        historiqueFonds.setMontant(request.getMontant());
        historiqueFonds.setUtilisateur(utilisateur);
        historiqueFonds.setTypeOperation(request.getTypeOperation());

        final String token = generateToken(20);
        System.out.println("Token : " + token);

        safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY).put(token, historiqueFonds);

        emailService.envoyerEmailValidationHistoFonds(utilisateur.getEmail(), token);
    }

    public void confirmerOperation(final Utilisateur utilisateur, final String token) {
        HistoriqueFonds historiqueFonds = safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY).get(token, HistoriqueFonds.class);
        if (historiqueFonds != null) {
            Double fondsActuel = utilisateur.getFondsActuel();
            Double montant = historiqueFonds.getMontant();

            utilisateur.setFondsActuel(switch (historiqueFonds.getTypeOperation()) {
                case DEPOT   -> fondsActuel + montant;
                case RETRAIT -> fondsActuel - montant;
            });
            utilisateurService.save(utilisateur);

            historiqueFondsRepository.save(historiqueFonds);
        }
    }
}
