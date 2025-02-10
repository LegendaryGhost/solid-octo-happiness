package mg.itu.cryptomonnaie.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CryptomonnaieService {
    private final CryptomonnaieRepository cryptomonnaieRepository;

    @Transactional
    public List<Cryptomonnaie> getAll() {
        return cryptomonnaieRepository.findAll();
    }

    @Transactional
    public Cryptomonnaie getById(final Integer id) {
        return cryptomonnaieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aucune cryptomonnaie trouvée avec l'identifiant : " + id));
    }

    @Transactional
    public Cryptomonnaie getByIdOrGetTopest(@Nullable final Integer id) {
        return cryptomonnaieRepository.findByIdOrFindTopest(id)
            .orElseThrow(() -> new RuntimeException("Aucune cryptomonnaie dans la base de données"));
    }
}
