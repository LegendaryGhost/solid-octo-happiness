package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.SuiviOperation;
import mg.itu.cryptomonnaie.repository.SuiviOperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SuiviOperationService {
    private final SuiviOperationRepository suiviOperationRepository;

    @Transactional
    public SuiviOperation getByOperationOrderByDateHeureDesc(final Integer idOperation) {
        return suiviOperationRepository.findByOperationIdOrderByDateHeureDesc(idOperation)
            .orElseThrow(() -> new RuntimeException("Suivi d'opération introuvable à partir l'identifiant donné : " + idOperation));
    }

    @Transactional
    public void save(final SuiviOperation suiviOperation) {
        suiviOperationRepository.save(suiviOperation);
    }
}
