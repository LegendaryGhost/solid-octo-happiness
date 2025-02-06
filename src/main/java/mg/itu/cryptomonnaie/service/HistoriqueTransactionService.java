package mg.itu.cryptomonnaie.service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.entity.*;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.request.HistoriqueTransactionRequest;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.repository.HistoriqueTransactionRepository;

@RequiredArgsConstructor
@Service
public class HistoriqueTransactionService {
    private final HistoriqueTransactionRepository historiqueTransactionRepository;
    private final CryptomonnaieService cryptomonnaieService;
    private final CoursCryptoService   coursCryptoService;
    private final PortefeuilleService  portefeuilleService;
    private final UtilisateurService   utilisateurService;

    public List<HistoriqueTransactionDTO> getHistoriqueGlobale() {
        return historiqueTransactionRepository.findHistoriqueGlobale();
    }

    @Transactional
    public List<HistoriqueTransaction> getAllByUtilisateurIdOrderByDateHeureDesc(final Integer idUtilisateur) {
        return historiqueTransactionRepository.findAllByUtilisateurIdOrderByDateHeureDesc(idUtilisateur);
    }

    public void save(final HistoriqueTransactionRequest request, final Utilisateur utilisateur) {
        final Integer idCryptomonnaie = request.getIdCryptomonnaie();
        Cryptomonnaie cryptomonnaie = cryptomonnaieService.getById(idCryptomonnaie);

        TypeTransaction typeTransaction = request.getTypeTransaction();
        Float requestQuantite = request.getQuantite();

        HistoriqueTransaction historiqueTransaction = new HistoriqueTransaction();
        historiqueTransaction.setQuantite(requestQuantite);
        historiqueTransaction.setCours(coursCryptoService
            .getCoursCryptoActuelByCryptomonnaie(idCryptomonnaie).getCoursActuel());
        historiqueTransaction.setTypeTransaction(typeTransaction);
        historiqueTransaction.setUtilisateur(utilisateur);
        historiqueTransaction.setCryptomonnaie(cryptomonnaie);

        // Mise à jour du portefeuille
        Portefeuille portefeuille  = portefeuilleService.getByUtilisateurAndCryptomonnaieOrCreate(utilisateur, cryptomonnaie);
        Float portefeuilleQuantite = portefeuille.getQuantite();
        switch (typeTransaction) {
            case ACHAT -> portefeuille.setQuantite(portefeuilleQuantite + requestQuantite);
            case VENTE -> {
                portefeuille.setQuantite(portefeuilleQuantite - requestQuantite);

                // La Vente se transforme en fonds
                utilisateur.setFondsActuel(utilisateur.getFondsActuel() + requestQuantite * historiqueTransaction.getCours());
                utilisateurService.save(utilisateur);
            }
        }

        portefeuilleService.save(portefeuille);
        historiqueTransactionRepository.save(historiqueTransaction);
    }
}
