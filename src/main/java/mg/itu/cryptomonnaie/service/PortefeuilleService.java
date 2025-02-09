package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.dto.SituationPortefeuilleDTO;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.entity.Portefeuille;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.projections.PortefeuilleAvecCours;
import mg.itu.cryptomonnaie.repository.PortefeuilleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Service
public class PortefeuilleService {
    private final PortefeuilleRepository portefeuilleRepository;
    private final TransactionService transactionService;
    private final FirestoreService firestoreService;

    @Transactional
    public Portefeuille getByUtilisateurAndCryptomonnaieOrCreate(
        final Utilisateur utilisateur, final Cryptomonnaie cryptomonnaie
    ) {
        Portefeuille portefeuille = portefeuilleRepository.findByUtilisateurIdAndCryptomonnaieId(utilisateur.getId(), cryptomonnaie.getId());
        if (portefeuille != null) return portefeuille;

        portefeuille = new Portefeuille();
        portefeuille.setUtilisateur(utilisateur);
        portefeuille.setCryptomonnaie(cryptomonnaie);

        save(portefeuille);
        firestoreService.synchronizeLocalDbToFirestore(portefeuille);

        return portefeuille;
    }

    @Transactional
    public void save(final Portefeuille portefeuille) {
        portefeuilleRepository.save(portefeuille);
    }

    public SituationPortefeuilleDTO getSituationPortefeuilleActuelle(final Utilisateur utilisateur) {
        final Integer idUtilisateur = utilisateur.getId();

        // Récupération de la liste des cryptos dans le portefeuille avec leur cours actuel
        List<PortefeuilleAvecCours> portefeuilleAvecCoursDTOList = portefeuilleRepository.findAvecCoursActuelByUtilisateur(idUtilisateur);
        Map<Integer, Double> coursCryptoActuelMap = portefeuilleAvecCoursDTOList.stream()
            .collect(Collectors.toMap(
                PortefeuilleAvecCours::getIdCryptomonnaie,
                PortefeuilleAvecCours::getCoursActuel,
                (existing, replacement) -> existing
            ));

        // Récupération de l'historique des transactions
        List<HistoriqueTransactionDTO> historiquesTransactionDTO = new ArrayList<>();
        transactionService.getAllByUtilisateurIdOrderByDateHeureDesc(idUtilisateur)
            .forEach(historiqueTransaction -> {
                Cryptomonnaie cryptomonnaie = historiqueTransaction.getCryptomonnaie();
                Integer idCryptomonnaie = cryptomonnaie.getId();

                historiquesTransactionDTO.add(new HistoriqueTransactionDTO(
                    idUtilisateur,
                    utilisateur.getEmail(),
                    idCryptomonnaie,
                    cryptomonnaie.getDesignation(),
                    historiqueTransaction.getTypeTransaction(),
                    historiqueTransaction.getQuantite(),
                    historiqueTransaction.getDateHeure(),
                    historiqueTransaction.getCours(),
                    coursCryptoActuelMap.get(idCryptomonnaie)
                ));
            });

        return new SituationPortefeuilleDTO(
            idUtilisateur, utilisateur.getFondsActuel(), portefeuilleAvecCoursDTOList, historiquesTransactionDTO);
    }
}
