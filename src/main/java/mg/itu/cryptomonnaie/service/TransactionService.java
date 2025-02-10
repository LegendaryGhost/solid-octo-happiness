package mg.itu.cryptomonnaie.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.entity.Portefeuille;
import mg.itu.cryptomonnaie.entity.Transaction;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.projections.ResultatAnalyseCommission;
import mg.itu.cryptomonnaie.projections.ResumeHistoriqueTransactionUtilisateur;
import mg.itu.cryptomonnaie.repository.TransactionRepository;
import mg.itu.cryptomonnaie.request.AnalyseCommissionRequest;
import mg.itu.cryptomonnaie.request.TransactionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CryptomonnaieService cryptomonnaieService;
    private final CoursCryptoService   coursCryptoService;
    private final PortefeuilleService  portefeuilleService;
    private final UtilisateurService   utilisateurService;
    private final FirestoreService     firestoreService;
    private final CryptoFavorisService cryptoFavorisService;
    private final NotificationService  notificationService;

    @Transactional
    public List<HistoriqueTransactionDTO> getHistoriqueGlobale(
        final Integer idCryptomonnaie, final String idUtilisateur
    ) {
        return transactionRepository.findHistoriqueGlobale(idCryptomonnaie, idUtilisateur);
    }

    @Transactional
    public List<Transaction> getAllByUtilisateurIdOrderByDateHeureDesc(final String idUtilisateur) {
        return transactionRepository.findAllByUtilisateurIdOrderByDateHeureDesc(idUtilisateur);
    }

    @Transactional
    public void save(final TransactionRequest request, final Utilisateur utilisateur) {
        final Integer idCryptomonnaie = request.getIdCryptomonnaie();
        Cryptomonnaie cryptomonnaie = cryptomonnaieService.getById(idCryptomonnaie);

        TypeTransaction typeTransaction = request.getTypeTransaction();
        Float requestQuantite = request.getQuantite();

        Transaction transaction = new Transaction();
        transaction.setQuantite(requestQuantite);
        transaction.setCours(coursCryptoService
            .getCoursCryptoActuelByCryptomonnaie(idCryptomonnaie).getCours());
        transaction.setTypeTransaction(typeTransaction);
        transaction.calculerMontantCommission();
        transaction.setCryptomonnaie(cryptomonnaie);
        transaction.setUtilisateur(utilisateur);

        transactionRepository.save(transaction);
        firestoreService.synchronizeLocalDbToFirestore(transaction);

        final Double cours = transaction.getCours();

        // Envoi de notification si la crypto fait partie des favoris de l'utilisateur
        if (cryptoFavorisService.isCryptomonnaieInFavoris(utilisateur.getId(), idCryptomonnaie))
            notificationService.envoyerNotificationPush(utilisateur.getExpoPushToken(),
                "Transaction effectuée",
                String.format("Vous avez %s %s %s au cours de %s",
                    typeTransaction == TypeTransaction.ACHAT ? "acheté" : "vendu",
                    requestQuantite, cryptomonnaie.getDesignation(), cours
                ));

        // Mise à jour du portefeuille
        Portefeuille portefeuille  = portefeuilleService.getByUtilisateurAndCryptomonnaieOrCreate(utilisateur, cryptomonnaie);
        Float portefeuilleQuantite = portefeuille.getQuantite();

        // Pour la création du portefeuille la première fois, ceci sera null
        portefeuilleQuantite = portefeuilleQuantite == null ? 0 : portefeuilleQuantite;

        // Mise à jour du fonds de l'utilisateur
        final Double fondsActuel = utilisateur.getFondsActuel();
        final Double montant     = requestQuantite * cours;
        switch (typeTransaction) {
            case ACHAT -> {
                portefeuille.setQuantite(portefeuilleQuantite + requestQuantite);
                utilisateur.setFondsActuel(fondsActuel - montant);
            }
            case VENTE -> {
                portefeuille.setQuantite(portefeuilleQuantite - requestQuantite);
                utilisateur.setFondsActuel(fondsActuel + montant);
            }
        }
        utilisateurService.save(utilisateur);
        firestoreService.synchronizeLocalDbToFirestore(utilisateur);

        portefeuilleService.save(portefeuille);
    }

    public ResultatAnalyseCommission analyserCommission(final AnalyseCommissionRequest request) {
        return transactionRepository.analyserCommission(
            request.getTypeAnalyse(), request.getIdCryptomonnaie(), request.getDateHeureMin(), request.getDateHeureMax());
    }

    public List<ResumeHistoriqueTransactionUtilisateur> getResumesHistoriquesTransactionGroupByUtilisateur() {
        return transactionRepository.findResumesHistoriquesTransactionGroupByUtilisateur();
    }
}
