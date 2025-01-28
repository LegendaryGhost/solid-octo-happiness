package mg.itu.cryptomonnaie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;

@Service
public class CryptomonnaieService {
    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;

    public List<Cryptomonnaie> listeCryptomonnaie() {
        return cryptomonnaieRepository.findAll();
    }

    public Cryptomonnaie getById(Long idCrypto) {
        return cryptomonnaieRepository.findById(idCrypto).orElse(null);
    }
}
