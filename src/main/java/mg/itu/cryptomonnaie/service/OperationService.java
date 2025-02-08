package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.Operation;
import mg.itu.cryptomonnaie.entity.SuiviOperation;
import mg.itu.cryptomonnaie.entity.Utilisateur;
import mg.itu.cryptomonnaie.enums.StatutOperation;
import mg.itu.cryptomonnaie.repository.OperationRepository;
import mg.itu.cryptomonnaie.request.OperationRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final SuiviOperationService suiviOperationService;
    private final UtilisateurService utilisateurService;

    public List<Operation> getHistoriqueGlobale(final @Nullable LocalDateTime dateHeure) {
        return operationRepository.findAllBySuiviOperationRecentAndStatut(dateHeure, StatutOperation.VALIDEE);
    }

    public List<Operation> getAllEnAttente() {
        return operationRepository.findAllBySuiviOperationRecentAndStatut(null, StatutOperation.EN_ATTENTE);
    }

    public List<Operation> getAllByUtilisateur(final Integer idUtilisateur) {
        return operationRepository.findAllByUtilisateurId(idUtilisateur);
    }

    @Transactional
    public void creerOperationEnAttente(
        final OperationRequest request, final Utilisateur utilisateur
    ) {
        Operation operation = new Operation();
        operation.setNumCarteBancaire(request.getNumCarteBancaire());
        operation.setMontant(request.getMontant());
        operation.setTypeOperation(request.getTypeOperation());
        operation.setUtilisateur(utilisateur);

        operationRepository.save(operation);

        SuiviOperation suiviOperation = new SuiviOperation();
        suiviOperation.setStatut(StatutOperation.defaultValue());
        suiviOperation.setDateHeure(operation.getDateHeure());
        suiviOperation.setOperation(operation);

        suiviOperationService.save(suiviOperation);
    }

    @Transactional
    public void accepter(final Integer idOperation) {
        SuiviOperation suiviOperation = suiviOperationService.getByOperationOrderByDateHeureDesc(idOperation);
        ensureOperationIsPending(suiviOperation);

        Operation operation = suiviOperation.getOperation();
        SuiviOperation s = new SuiviOperation();
        s.setStatut(StatutOperation.VALIDEE);
        s.setDateHeure(LocalDateTime.now());
        s.setOperation(operation);

        suiviOperationService.save(s);

        // Mise à jour du fonds
        Utilisateur utilisateur = operation.getUtilisateur();
        Double fondsActuel = utilisateur.getFondsActuel();
        Double montant     = operation.getMontant();
        utilisateur.setFondsActuel(switch (operation.getTypeOperation()) {
            case DEPOT   -> fondsActuel + montant;
            case RETRAIT -> fondsActuel - montant;
        });

        utilisateurService.save(utilisateur);
    }

    public void refuser(final Integer idOperation) {
        SuiviOperation suiviOperation = suiviOperationService.getByOperationOrderByDateHeureDesc(idOperation);
        ensureOperationIsPending(suiviOperation);

        SuiviOperation s = new SuiviOperation();
        s.setStatut(StatutOperation.REJETEE);
        s.setDateHeure(LocalDateTime.now());
        s.setOperation(suiviOperation.getOperation());

        suiviOperationService.save(s);
    }

    private static void ensureOperationIsPending(final SuiviOperation suiviOperation) {
        if (suiviOperation.getStatut() != StatutOperation.EN_ATTENTE)
            throw new RuntimeException("L'opération n'est plus en attente");
    }
}
