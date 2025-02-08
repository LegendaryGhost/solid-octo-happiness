package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.repository.CryptomonnaieRepository;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoursCryptoService {
    private final CoursCryptoRepository   coursCryptoRepository;
    private final CryptomonnaieRepository cryptomonnaieRepository;
    private Random random;

    public List<CoursCrypto> getByCryptomonnaie(final Integer idCryptomonnaie) {
        return coursCryptoRepository.findByCryptomonnaieId(idCryptomonnaie);
    }

    @Transactional
    public CoursCrypto getCoursCryptoActuelByCryptomonnaie(final Integer idCryptomonnaie) {
        return coursCryptoRepository.findFirstByCryptomonnaieIdOrderByDateHeureDesc(idCryptomonnaie);
    }

    // @Scheduled(fixedRate = 10000)
    @Transactional
    public void generateRandomCours() {
        cryptomonnaieRepository.findAll().forEach(cryptomonnaie -> {
            CoursCrypto coursCrypto = new CoursCrypto();
            coursCrypto.setCours(generateRandomCoursValue(
                getCoursCryptoActuelByCryptomonnaie(cryptomonnaie.getId()).getCours()
            ));
            coursCrypto.setCryptomonnaie(cryptomonnaie);
            coursCryptoRepository.save(coursCrypto);

            log.debug("Cours généré pour la cryptomonnaie \"{}\" : {}", cryptomonnaie.getDesignation(), coursCrypto.getCours());
        });
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

    private Double generateRandomCoursValue(Double dernierCours) {
        Double facteur = (random.nextDouble() * 0.2) - 0.1;
        return dernierCours + (dernierCours * facteur);
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
