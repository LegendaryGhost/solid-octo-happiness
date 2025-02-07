package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFonds;
import mg.itu.cryptomonnaie.entity.StatutHistoriqueFonds;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.enums.StatutOperation;
import mg.itu.cryptomonnaie.repository.HistoriqueFondsRepository;
import mg.itu.cryptomonnaie.request.HistoriqueFondsRequest;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@RequiredArgsConstructor
@Service
public class HistoriqueFondsService {
    public static final String HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY = "_historiques_fonds_temp";

    private final HistoriqueFondsRepository historiqueFondsRepository;
    private final UtilisateurService utilisateurService;
    private final StatutHistoriqueFondsService statutHistoriqueFondsService;
    private final EmailService emailService;
    private final SimpleAsyncTaskExecutor applicationTaskExecutor;

    public List<HistoriqueFonds> transactionProfil(final Utilisateur utilisateur) {
        return historiqueFondsRepository.findTransactionsProfil(Long.valueOf(utilisateur.getId()));
    }

    public List<HistoriqueFonds> getHistoriqueGlobale(final @Nullable LocalDateTime dateHeure) {
        return historiqueFondsRepository.findAllByDateHeureEquals(dateHeure);
    }

    @Transactional
    public void creerHistoriqueFondsEnAttente(
        final HistoriqueFondsRequest request, final Utilisateur utilisateur
    ) {
        HistoriqueFonds historiqueFonds = new HistoriqueFonds();
        historiqueFonds.setNumCarteBancaire(request.getNumCarteBancaire());
        historiqueFonds.setMontant(request.getMontant());
        historiqueFonds.setTypeOperation(request.getTypeOperation());
        historiqueFonds.setUtilisateur(utilisateur);

        StatutHistoriqueFonds statutHistoriqueFonds = new StatutHistoriqueFonds();
        statutHistoriqueFonds.setStatut(StatutOperation.defaultValue());
        statutHistoriqueFonds.setHistoriqueFonds(historiqueFonds);

        statutHistoriqueFondsService.save(statutHistoriqueFonds);
        historiqueFondsRepository.save(historiqueFonds);
    }

    public void accepterOperation(final Integer idHistoriqueFonds) {
        StatutHistoriqueFonds statutHistoriqueFonds = statutHistoriqueFondsService.getByHistoriqueFondsOrderByDateHeureDesc(idHistoriqueFonds);

        
    }

    public void refuserOperation(final Integer idHistoriqueFonds) {

    }

    @Deprecated
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
