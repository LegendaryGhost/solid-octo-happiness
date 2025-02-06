package mg.itu.cryptomonnaie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;
import org.springframework.transaction.annotation.Transactional;

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
            coursCrypto.setDateHeure(LocalDateTime.now());

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

    public Double analyse(
        String typeAnalyse,
        List<Long> idCryptos,
        LocalDateTime dateHeureMin,
        LocalDateTime dateHeureMax
    ) {
        List<CoursCrypto> coursCryptos = coursCryptoRepository.findParCryptomonnaieEtDate(idCryptos, dateHeureMin, dateHeureMax);
        if (coursCryptos.isEmpty()) return null;

        return switch (typeAnalyse.toLowerCase()) {
            case "1er-quartile" -> calculerPremierQuartile(coursCryptos);
            case "max" -> calculerMax(coursCryptos);
            case "min" -> calculerMin(coursCryptos);
            case "moyenne" -> calculerMoyenne(coursCryptos);
            case "ecart-type" -> calculerEcartType(coursCryptos);
            default -> throw new IllegalArgumentException("Type d'analyse inconnu: " + typeAnalyse);
        };
    }

    private Double calculerPremierQuartile(List<CoursCrypto> coursCryptos) {
        List<Double> coursValues = coursCryptos.stream()
            .map(CoursCrypto::getCoursActuel)
            .sorted()
            .toList();

        return coursValues.get(
            (int) (Math.floor(0.25 * (coursValues.size() + 1)) - 1)
        );
    }

    private Double calculerMax(List<CoursCrypto> coursCryptos) {
        return coursCryptos.stream()
            .map(CoursCrypto::getCoursActuel)
            .max(Double::compare)
            .orElseThrow(() -> new IllegalArgumentException("Aucun cours trouvé"));
    }

    private Double calculerMin(List<CoursCrypto> coursCryptos) {
        return coursCryptos.stream()
            .map(CoursCrypto::getCoursActuel)
            .min(Double::compare)
            .orElseThrow(() -> new IllegalArgumentException("Aucun cours trouvé"));
    }

    private Double calculerMoyenne(List<CoursCrypto> coursCryptos) {
        return coursCryptos.stream()
            .mapToDouble(CoursCrypto::getCoursActuel)
            .average()
            .orElseThrow(() -> new IllegalArgumentException("Aucun cours trouvé"));
    }

    private Double calculerEcartType(List<CoursCrypto> coursCryptos) {
        return Math.sqrt(
            coursCryptos.stream()
                .mapToDouble(c -> Math.pow(c.getCoursActuel() - calculerMoyenne(coursCryptos), 2))
                .average()
                .orElseThrow(() -> new IllegalArgumentException("Aucun cours trouvé"))
        );
    }
}
