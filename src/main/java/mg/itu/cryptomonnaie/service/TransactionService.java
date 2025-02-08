package mg.itu.cryptomonnaie.service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.entity.*;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.projections.ResultatAnalyseCommission;
import mg.itu.cryptomonnaie.projections.ResumeHistoriqueTransactionUtilisateur;
import mg.itu.cryptomonnaie.request.AnalyseCommissionRequest;
import mg.itu.cryptomonnaie.request.TransactionRequest;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.repository.TransactionRepository;

@RequiredArgsConstructor
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CryptomonnaieService cryptomonnaieService;
    private final CoursCryptoService   coursCryptoService;
    private final PortefeuilleService  portefeuilleService;
    private final UtilisateurService   utilisateurService;

    public List<HistoriqueTransactionDTO> getHistoriqueGlobale(
        final Integer idCryptomonnaie, final Integer idUtilisateur
    ) {
        return transactionRepository.findHistoriqueGlobale(idCryptomonnaie, idUtilisateur);
    }

    @Transactional
    public List<Transaction> getAllByUtilisateurIdOrderByDateHeureDesc(final Integer idUtilisateur) {
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

        // Mise à jour du portefeuille
        Portefeuille portefeuille  = portefeuilleService.getByUtilisateurAndCryptomonnaieOrCreate(utilisateur, cryptomonnaie);
        Float portefeuilleQuantite = portefeuille.getQuantite();

        // Pour la création du portefeuille la première fois, ceci sera null
        portefeuilleQuantite = portefeuilleQuantite == null ? 0 : portefeuilleQuantite;

        // Mise à jour du fonds de l'utilisateur
        Double fondsActuel = utilisateur.getFondsActuel();
        Double montant     = requestQuantite * transaction.getCours();
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
