package mg.itu.cryptomonnaie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;

@Service
public class CoursCryptoService {
    @Autowired
    private CoursCryptoRepository coursCryptoRepository;

    @Autowired
    private CryptomonnaieRepository cryptomonnaieRepository;
    private Random random;

    @Scheduled(fixedRate = 10000)
    public void generateRandomCours() {
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieRepository.findAll();

        for (Cryptomonnaie crypto : cryptomonnaies) {
            CoursCrypto coursActuel = coursCryptoRepository.findCoursActuel(crypto.getId());
            Double dernierCours = coursActuel.getCoursActuel();
            CoursCrypto coursCrypto = new CoursCrypto();
            coursCrypto.setCryptomonnaie(crypto);
            coursCrypto.setCoursActuel(generateRandomCoursValue(dernierCours));
            coursCrypto.setDateCours(LocalDateTime.now());

            coursCryptoRepository.save(coursCrypto);
            System.out.println("Cours généré pour " + crypto.getDesignation() + ": " + coursCrypto.getCoursActuel());
        }
    }

    private Double generateRandomCoursValue(Double dernierCours) {
        Double facteur = (random.nextDouble() * 0.2) - 0.1;
        return dernierCours + (dernierCours * facteur);
    }

    public List<CoursCrypto> listeCoursCrypto() {
        List<CoursCrypto> coursCryptos = coursCryptoRepository.findAll();
        return coursCryptos;
    }

    public List<CoursCrypto> listeCoursParCryptomonnaie(Cryptomonnaie cryptomonnaie) {
        List<CoursCrypto> coursCryptos = coursCryptoRepository.findCoursParCryptomonnaie(cryptomonnaie.getId());
        return coursCryptos;
    }
}
