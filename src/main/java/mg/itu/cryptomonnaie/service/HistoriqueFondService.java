package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFonds;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.entity.TypeTransaction;
import mg.itu.cryptomonnaie.repository.HistoriqueFondsRepository;
import mg.itu.cryptomonnaie.repository.TypeTransactionRepository;
import mg.itu.cryptomonnaie.request.HistoriqueFondRequest;
import mg.itu.cryptomonnaie.utils.SecureTokenGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class HistoriqueFondService {
    private final TypeTransactionRepository typeTransactionRepository;
    private final CacheManager cacheManager;
    private final HistoriqueFondsRepository historiqueFondsRepository;
    private final EmailService emailService;

    public List<HistoriqueFonds> transactionProfil(final Utilisateur utilisateur) {
        return historiqueFondsRepository.findTransactionsProfil(utilisateur.getId());
    }

    public void creerHistoriqueFondTemporaire(HistoriqueFondRequest request, Utilisateur utilisateur) throws MessagingException {
        HistoriqueFonds historiqueFonds = new HistoriqueFonds();
        historiqueFonds.setMontant(request.getMontant());
        historiqueFonds.setNumCarteBancaire(request.getNumCarteBancaire());

        historiqueFonds.setUtilisateur(utilisateur);

        TypeTransaction typeTransaction = typeTransactionRepository.findById(
                Long.valueOf(request.getIdTypeTransaction()))
            .orElseThrow(() -> new RuntimeException("Type de transaction introuvable"));
        historiqueFonds.setTypeTransaction(typeTransaction);

        String token = SecureTokenGenerator.generateToken(20);
        System.out.println("Token: " + token);
        // Stocker l'objet dans le cache
        Objects.requireNonNull(cacheManager.getCache("historiqueFondCache")).put(token, historiqueFonds);

        // Envoyer email de validation
        emailService.envoyerValidationHistoFondEmail(utilisateur.getEmail(), token);
    }

    public HistoriqueFonds getCachedHistoriqueFond(String token) {
        return Objects.requireNonNull(cacheManager.getCache("historiqueFondCache")).get(token, HistoriqueFonds.class);
    }

    public void confirmerTransaction(String token) {
        HistoriqueFonds historiqueFonds = getCachedHistoriqueFond(token);
        if (historiqueFonds != null) {
            historiqueFondsRepository.save(historiqueFonds);
        }
    }

    public List<HistoriqueFonds> listeTransactions() {
        return historiqueFondsRepository.findAll();
    }
}
