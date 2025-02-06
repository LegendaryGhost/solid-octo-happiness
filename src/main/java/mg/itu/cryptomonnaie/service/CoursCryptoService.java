package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class CoursCryptoService {
    private final CoursCryptoRepository coursCryptoRepository;
    private final CryptomonnaieRepository cryptomonnaieRepository;
    private Random random;

    @Transactional
    public CoursCrypto getCoursCryptoActuelByCryptomonnaie(final Integer idCryptomonnaie) {
        return coursCryptoRepository.findFirstByCryptomonnaieIdOrderByDateHeureDesc(idCryptomonnaie);
    }

    public List<CoursCrypto> getByCryptomonnaie(final Cryptomonnaie cryptomonnaie) {
        return coursCryptoRepository.findByCryptomonnaieId(cryptomonnaie.getId());
    }

    // @Scheduled(fixedRate = 10000)
    public void generateRandomCours() {
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieRepository.findAll();

        for (Cryptomonnaie crypto : cryptomonnaies) {
            CoursCrypto coursActuel = coursCryptoRepository
                .findFirstByCryptomonnaieIdOrderByDateHeureDesc(crypto.getId());
            Double dernierCours = coursActuel.getCoursActuel();
            CoursCrypto coursCrypto = new CoursCrypto();
            coursCrypto.setCryptomonnaie(crypto);
            coursCrypto.setCoursActuel(generateRandomCoursValue(dernierCours));

            coursCryptoRepository.save(coursCrypto);
            System.out.println("Cours généré pour " + crypto.getDesignation() + ": " + coursCrypto.getCoursActuel());
        }
    }

    private Double generateRandomCoursValue(Double dernierCours) {
        Double facteur = (random.nextDouble() * 0.2) - 0.1;
        return dernierCours + (dernierCours * facteur);
    }

    @Nullable
    public Double analyser(final AnalyseCoursCryptoRequest request) {
        List<CoursCrypto> coursCrypto = coursCryptoRepository.findAllByCryptomonnaieIdInAndDateHeureBetweenOrderByDateHeureDesc(
            request.getIdsCryptomonnaie(), request.getDateHeureMin(), request.getDateHeureMax());
        if (coursCrypto.isEmpty()) return null;

        List<Double> valeursCours = coursCrypto.stream().map(CoursCrypto::getCoursActuel).toList();
        return switch (request.getTypeAnalyse()) {
            case PREMIER_QUARTILE -> premierQuartile(valeursCours);
            case MAX -> valeursCours.stream().max(Double::compare).orElse(0.0);
            case MIN -> valeursCours.stream().min(Double::compare).orElse(0.0);
            case MOYENNE    -> moyenne(valeursCours);
            case ECART_TYPE -> ecartType(valeursCours);
        };
    }

    private static Double premierQuartile(List<Double> valeursCours) {
        valeursCours.sort(null);
        return valeursCours.get((int) Math.ceil(0.25 * valeursCours.size()) - 1);
    }

    private static Double moyenne(List<Double> valeursCours) {
        return valeursCours.stream()
            .mapToDouble(aDouble -> aDouble)
            .average().orElse(0.0);
    }

    private static Double ecartType(List<Double> valeursCours) {
        Double moyenne = moyenne(valeursCours);
        return Math.sqrt(valeursCours.stream()
            .mapToDouble(val -> Math.pow(val - moyenne, 2))
            .average().orElse(0.0));
    }
}
