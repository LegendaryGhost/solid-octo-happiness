package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.repository.TauxCommissionRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TauxCommissionService {
    private final TauxCommissionRepository tauxCommissionRepository;

}
