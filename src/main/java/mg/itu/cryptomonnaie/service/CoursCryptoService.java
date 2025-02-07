package mg.itu.cryptomonnaie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
    private Random random = new Random();;

    @Scheduled(fixedRate = 10000)
    public void generateRandomCours() {
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieRepository.findAll();

        for (Cryptomonnaie crypto : cryptomonnaies) {
            CoursCrypto coursActuel = coursCryptoRepository
                    .findFirstByCryptomonnaieIdOrderByDateCoursDesc(crypto.getId());
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

    @Nullable
    public Double analyser(final AnalyseCoursCryptoRequest request) {
        List<Double> coursCrypto = coursCryptoRepository.findAllCoursActuelInIdsCryptomonnaieForPeriode(
            request.getIdsCryptomonnaie(), request.getDateHeureMin(), request.getDateHeureMax());
        if (coursCrypto.isEmpty()) return null;

        return switch (request.getTypeAnalyse()) {
            case PREMIER_QUARTILE -> premierQuartile(coursCrypto);
            case MAX -> coursCrypto.stream().max(Double::compare).orElse(0.0);
            case MIN -> coursCrypto.stream().min(Double::compare).orElse(0.0);
            case MOYENNE    -> moyenne(coursCrypto);
            case ECART_TYPE -> ecartType(coursCrypto);
        };
    }

    private static Double premierQuartile(List<Double> coursCrypto) {
        coursCrypto.sort(null);
        return coursCrypto.get((int) Math.ceil(0.25 * coursCrypto.size()) - 1);
    }

    private static Double moyenne(List<Double> coursCrypto) {
        return coursCrypto.stream()
            .mapToDouble(aDouble -> aDouble)
            .average().orElse(0.0);
    }

    private static Double ecartType(List<Double> coursCrypto) {
        Double moyenne = moyenne(coursCrypto);
        return Math.sqrt(coursCrypto.stream()
            .mapToDouble(val -> Math.pow(val - moyenne, 2))
            .average().orElse(0.0));
    }
}
