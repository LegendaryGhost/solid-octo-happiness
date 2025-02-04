package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFond;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.entity.TypeTransaction;
import mg.itu.cryptomonnaie.repository.HistoriqueFondRepository;
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
    private final HistoriqueFondRepository historiqueFondRepository;
    private final EmailService emailService;

    public List<HistoriqueFond> transactionProfil(final Profil profil) {
        return historiqueFondRepository.findTransactionsProfil(profil.getId());
    }

    public void creerHistoriqueFondTemporaire(HistoriqueFondRequest request, Profil profil) throws MessagingException {
        HistoriqueFond historiqueFond = new HistoriqueFond();
        historiqueFond.setMontant(request.getMontant());
        historiqueFond.setNumCarteBancaire(request.getNumCarteBancaire());

        historiqueFond.setProfil(profil);

        TypeTransaction typeTransaction = typeTransactionRepository.findById(
                Long.valueOf(request.getIdTypeTransaction()))
            .orElseThrow(() -> new RuntimeException("Type de transaction introuvable"));
        historiqueFond.setTypeTransaction(typeTransaction);

        String token = SecureTokenGenerator.generateToken(20);
        System.out.println("Token: " + token);
        // Stocker l'objet dans le cache
        Objects.requireNonNull(cacheManager.getCache("historiqueFondCache")).put(token, historiqueFond);

        // Envoyer email de validation
        emailService.envoyerValidationHistoFondEmail(profil.getEmail(), token);
    }

    public HistoriqueFond getCachedHistoriqueFond(String token) {
        return Objects.requireNonNull(cacheManager.getCache("historiqueFondCache")).get(token, HistoriqueFond.class);
    }

    public void confirmerTransaction(String token) {
        HistoriqueFond historiqueFond = getCachedHistoriqueFond(token);
        if (historiqueFond != null) {
            historiqueFondRepository.save(historiqueFond);
        }
    }

    public List<HistoriqueFond> listeTransactions() {
        return historiqueFondRepository.findAll();
    }
}
