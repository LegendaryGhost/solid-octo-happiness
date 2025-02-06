package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.dto.PortefeuilleAvecCoursDTO;
import mg.itu.cryptomonnaie.dto.SituationPortefeuilleDTO;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.repository.PortefeuilleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PortefeuilleService {
    private final PortefeuilleRepository portefeuilleRepository;
    private final HistoriqueTransactionService historiqueTransactionService;

    public SituationPortefeuilleDTO getSituationPortefeuilleActuelle(final Utilisateur utilisateur) {
        final Integer idUtilisateur = utilisateur.getId();

        List<PortefeuilleAvecCoursDTO> portefeuilleAvecCoursDTOList = portefeuilleRepository.findAvecCoursActuel(idUtilisateur);
        Map<Integer, Double> coursCryptoActuelMap = portefeuilleAvecCoursDTOList.stream()
            .collect(Collectors.toMap(
                PortefeuilleAvecCoursDTO::idCryptomonnaie,
                PortefeuilleAvecCoursDTO::coursActuel,
                (existing, replacement) -> existing
            ));

        List<HistoriqueTransactionDTO> historiquesTransactionDTO = new ArrayList<>();
        historiqueTransactionService.getAllByUtilisateurIdOrderByDateHeureDesc(idUtilisateur)
            .forEach(historiqueTransaction -> {
                Cryptomonnaie cryptomonnaie = historiqueTransaction.getCryptomonnaie();
                Integer idCryptomonnaie = cryptomonnaie.getId();

                historiquesTransactionDTO.add(new HistoriqueTransactionDTO(
                    idUtilisateur,
                    utilisateur.getEmail(),
                    idCryptomonnaie,
                    cryptomonnaie.getDesignation(),
                    historiqueTransaction.getTypeTransaction().getDesignation(),
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
