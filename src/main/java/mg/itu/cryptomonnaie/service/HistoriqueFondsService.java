package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFonds;
import mg.itu.cryptomonnaie.entity.TypeOperation;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.HistoriqueFondsRepository;
import mg.itu.cryptomonnaie.request.HistoriqueFondsRequest;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@RequiredArgsConstructor
@Service
public class HistoriqueFondsService {
    public static final String HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY = "_historiques_fonds_temp";

    private final HistoriqueFondsRepository historiqueFondsRepository;
    private final CacheManager cacheManager;
    private final TypeOperationService typeOperationService;
    private final EmailService emailService;

    public List<HistoriqueFonds> transactionProfil(final Utilisateur utilisateur) {
        return historiqueFondsRepository.findTransactionsProfil(Long.valueOf(utilisateur.getId()));
    }

    public void creerHistoriqueFondsTemporaire(
        final HistoriqueFondsRequest request, final Utilisateur utilisateur
    ) throws MessagingException {
        final Integer idTypeOperation = request.getIdTypeOperation();
        TypeOperation typeOperation = typeOperationService.getById(idTypeOperation)
            .orElseThrow(() -> new RuntimeException("Aucun type d'opération trouvé avec l'identifiant : " + idTypeOperation));

        HistoriqueFonds historiqueFonds = new HistoriqueFonds();
        historiqueFonds.setNumCarteBancaire(request.getNumCarteBancaire());
        historiqueFonds.setMontant(request.getMontant());
        historiqueFonds.setUtilisateur(utilisateur);
        historiqueFonds.setTypeOperation(typeOperation);

        final String token = generateToken(20);
        System.out.println("Token : " + token);

        safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY, cacheManager).put(token, historiqueFonds);

        emailService.envoyerEmailValidationHistoFonds(utilisateur.getEmail(), token);
    }

    public void confirmerOperation(final String token) {
        HistoriqueFonds historiqueFonds = safelyGetCache(HISTORIQUES_FONDS_TEMPORAIRES_CACHE_KEY, cacheManager).get(token, HistoriqueFonds.class);
        if (historiqueFonds != null) historiqueFondsRepository.save(historiqueFonds);
    }
}
