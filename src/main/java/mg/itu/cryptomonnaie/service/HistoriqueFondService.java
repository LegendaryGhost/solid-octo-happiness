package mg.itu.cryptomonnaie.service;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.mail.MessagingException;
import mg.itu.cryptomonnaie.repository.ProfilRepository;
import mg.itu.cryptomonnaie.utils.Utils;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.entity.EtatFond;
import mg.itu.cryptomonnaie.entity.HistoriqueFond;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.entity.TypeTransaction;
import mg.itu.cryptomonnaie.repository.EtatFondRepository;
import mg.itu.cryptomonnaie.repository.HistoriqueFondRepository;
import mg.itu.cryptomonnaie.repository.TypeTransactionRepository;
import mg.itu.cryptomonnaie.request.HistoriqueFondRequest;
import mg.itu.cryptomonnaie.utils.SecureTokenGenerator;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@AllArgsConstructor
@Service
public class HistoriqueFondService {

    private final TypeTransactionRepository typeTransactionRepository;
    private final CacheManager cacheManager;
    private final HistoriqueFondRepository historiqueFondRepository;
    private final EmailService emailService;
    private final EtatFondRepository etatFondRepository;

    public List<HistoriqueFond> transactionProfil(HttpSession session) {
        Profil profil = Utils.getUser(session);
        assert profil != null;
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

    public HistoriqueFond creerHistoriqueFondEnAttente(HistoriqueFondRequest historique, Profil profil) {
        HistoriqueFond historiqueFond = new HistoriqueFond();
        historiqueFond.setMontant(historique.getMontant());
        historiqueFond.setNumCarteBancaire(historique.getNumCarteBancaire());
        historiqueFond.setProfil(profil);

        TypeTransaction typeTransaction = typeTransactionRepository.findById(
                Long.valueOf(historique.getIdTypeTransaction()))
                .orElseThrow(() -> new RuntimeException("Type de transaction introuvable"));
        historiqueFond.setTypeTransaction(typeTransaction);

        // Tokony asiana champ ana date amle form depot-retrait
        historiqueFond.setDateTransaction(LocalDateTime.now());

        // Statut de transaction de fond En attente id = 3
        EtatFond etatFond = etatFondRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Etat de transaction de fond introuvable"));
        historiqueFond.setEtatFond(etatFond);

        return historiqueFondRepository.save(historiqueFond);
    }

    public List<HistoriqueFond> listeTransactionFondEnAttente() {
        List<HistoriqueFond> historiques = historiqueFondRepository.findByEtat("En attente");

        if (historiques == null || historiques.isEmpty()) {
            throw new RuntimeException("Aucune transaction en attente disponible");
        }

        return historiques;
    }

    public HistoriqueFond validerTransactionFond(Long id) {
        HistoriqueFond historiqueFond = historiqueFondRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction de fond introuvable pour cet id"));

        // Statut de transaction de fond Refusée id = 2
        EtatFond etatFond = etatFondRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Etat de transaction de fond introuvable"));

        historiqueFond.setEtatFond(etatFond);

        return historiqueFondRepository.save(historiqueFond);
    }

    public HistoriqueFond refuserTransactionFond(Long id) {
        HistoriqueFond historiqueFond = historiqueFondRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction de fond introuvable pour cet id"));

        // Statut de transaction de fond Refusée id = 2
        EtatFond etatFond = etatFondRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Etat de transaction de fond introuvable"));

        historiqueFond.setEtatFond(etatFond);

        return historiqueFondRepository.save(historiqueFond);
    }
}
