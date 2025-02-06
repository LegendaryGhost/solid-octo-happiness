package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.PortefeuilleAvecCoursDTO;
import mg.itu.cryptomonnaie.repository.PortefeuilleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortefeuilleService {
    private final PortefeuilleRepository portefeuilleRepository;

    public List<PortefeuilleAvecCoursDTO> getSituationPortefeuilleActuelle(Integer idUtilisateur) {
        return portefeuilleRepository.findSituationPortefeuilleActuelle(idUtilisateur);
    }
}
