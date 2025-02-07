package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.StatutHistoriqueFonds;
import mg.itu.cryptomonnaie.repository.StatutHistoriqueFondsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StatutHistoriqueFondsService {
    private final StatutHistoriqueFondsRepository statutHistoriqueFondsRepository;

    @Transactional
    public StatutHistoriqueFonds getByHistoriqueFondsOrderByDateHeureDesc(final Integer idHistoriqueFonds) {
        return statutHistoriqueFondsRepository.findByHistoriqueFondsIdOrderByDateHeureDesc(idHistoriqueFonds)
            .orElseThrow(() -> new RuntimeException("Statut de l'opération introuvable à partir l'identifiant donné : " + idHistoriqueFonds));
    }

    @Transactional
    public void save(final StatutHistoriqueFonds statutHistoriqueFonds) {
        statutHistoriqueFondsRepository.save(statutHistoriqueFonds);
    }
}
