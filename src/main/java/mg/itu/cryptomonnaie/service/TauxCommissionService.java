package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.TauxCommission;
import mg.itu.cryptomonnaie.repository.TauxCommissionRepository;
import mg.itu.cryptomonnaie.request.TauxCommissionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TauxCommissionService {
    private final TauxCommissionRepository tauxCommissionRepository;

    @Transactional
    public void save(final TauxCommissionRequest request) {
        TauxCommission tauxCommission = tauxCommissionRepository.findFirstByOrderById()
            .orElseThrow(() -> new RuntimeException("Configuration du taux de commission non mise en place"));

        Double tauxCommissionValeurAchat = tauxCommission.getValeurAchat();
        Double tauxCommissionValeurVente = tauxCommission.getValeurVente();
        Double requestValeurAchat = request.getValeurAchat();
        Double requestValeurVente = request.getValeurVente();

        if (!tauxCommissionValeurAchat.equals(requestValeurAchat)) tauxCommission.setValeurAchat(requestValeurAchat);
        if (!tauxCommissionValeurVente.equals(requestValeurVente)) tauxCommission.setValeurVente(requestValeurVente);

        tauxCommissionRepository.save(tauxCommission);
    }
}
