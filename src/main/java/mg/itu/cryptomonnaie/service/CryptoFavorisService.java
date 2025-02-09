package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.repository.CryptoFavorisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CryptoFavorisService {
    private final CryptoFavorisRepository cryptoFavorisRepository;

    @Transactional
    public boolean isCryptomonnaieInFavoris(final Integer idUtilisateur, final Integer idCryptomonnaie) {
        return cryptoFavorisRepository.existsByUtilisateurIdAndCryptomonnaieId(idUtilisateur, idCryptomonnaie);
    }
}
