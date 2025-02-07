package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Operation;
import mg.itu.cryptomonnaie.entity.SuiviOperation;
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

    public List<Operation> transactionProfil(final Utilisateur utilisateur) {
        return historiqueFondsRepository.findTransactionsProfil(Long.valueOf(utilisateur.getId()));
    }

    public List<Operation> getHistoriqueGlobale(final @Nullable LocalDateTime dateHeure) {
        return historiqueFondsRepository.findAllByDateHeureEquals(dateHeure);
    }

    @Transactional
    public void creerHistoriqueFondsEnAttente(
        final HistoriqueFondsRequest request, final Utilisateur utilisateur
    ) {
        Operation operation = new Operation();
        operation.setNumCarteBancaire(request.getNumCarteBancaire());
        operation.setMontant(request.getMontant());
        operation.setTypeOperation(request.getTypeOperation());
        operation.setUtilisateur(utilisateur);

        SuiviOperation suiviOperation = new SuiviOperation();
        suiviOperation.setStatut(StatutOperation.defaultValue());
        suiviOperation.setOperation(operation);

        statutHistoriqueFondsService.save(suiviOperation);
        historiqueFondsRepository.save(operation);
    }

    public void accepterOperation(final Integer idHistoriqueFonds) {
        SuiviOperation suiviOperation = statutHistoriqueFondsService.getByHistoriqueFondsOrderByDateHeureDesc(idHistoriqueFonds);

        
    }

    public void refuserOperation(final Integer idHistoriqueFonds) {

    }

    @Deprecated
    public void creerHistoriqueFondsTemporaire(
        final HistoriqueFondsRequest request, final Utilisateur utilisateur
    ) throws MessagingException {
        Operation operation = new Operation();
        operation.setNumCarteBancaire(request.getNumCarteBancaire());
        operation.setMontant(request.getMontant());
        operation.setUtilisateur(utilisateur);
        operation.setTypeOperation(request.getTypeOperation());

        final String token = generateToken(20);
        System.out.println("Token : " + token);

        safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY).put(token, operation);
        emailService.envoyerEmailValidationHistoFonds(utilisateur.getEmail(), token);
    }

    public void confirmerOperation(final Utilisateur utilisateur, final String token) {
        Operation operation = safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY).get(token, Operation.class);
        if (operation != null) {
            Double fondsActuel = utilisateur.getFondsActuel();
            Double montant = operation.getMontant();

            utilisateur.setFondsActuel(switch (operation.getTypeOperation()) {
                case DEPOT   -> fondsActuel + montant;
                case RETRAIT -> fondsActuel - montant;
            });
            utilisateurService.save(utilisateur);

            historiqueFondsRepository.save(operation);
        }
    }
}
