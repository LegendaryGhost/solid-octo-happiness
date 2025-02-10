package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.repository.CoursCryptoRepository;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import mg.itu.cryptomonnaie.specifications.CoursCryptoSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoursCryptoService {
    private static final double POURCENTAGE_MAX_VARIATION = 5.0;
    private static final double VALEUR_COURS_MAX = 9.99999999999999E12;

    private final CoursCryptoRepository coursCryptoRepository;
    private final CryptomonnaieService  cryptomonnaieService;
    private final FirestoreService firestoreService;

    @Transactional
    public List<CoursCrypto> getByCryptomonnaie(final Integer idCryptomonnaie) {
        return coursCryptoRepository.findByCryptomonnaieId(idCryptomonnaie);
    }

    @Transactional
    public CoursCrypto getCoursCryptoActuelByCryptomonnaie(final Integer idCryptomonnaie) {
        return coursCryptoRepository.findFirstByCryptomonnaieIdOrderByDateHeureDesc(idCryptomonnaie);
    }

    @Scheduled(fixedRateString = "${courscrypto.fixedrate}")
    @Transactional
    public void genererCoursCryptosAleatoirement() {
        List<CoursCrypto> coursCryptosAleatoires = new ArrayList<>();
        cryptomonnaieService.getAll().forEach(cryptomonnaie -> {
            CoursCrypto dernierCours = getCoursCryptoActuelByCryptomonnaie(cryptomonnaie.getId());

            CoursCrypto coursCrypto = new CoursCrypto();
            coursCrypto.setCours(generateRandomCoursValue(
                dernierCours == null ? 1000.0 : dernierCours.getCours()
            ));
            coursCrypto.setCryptomonnaie(cryptomonnaie);
            coursCryptosAleatoires.add(coursCrypto);

            log.trace("Cours généré pour la cryptomonnaie \"{}\" : {}", cryptomonnaie.getDesignation(), coursCrypto.getCours());
        });

        coursCryptoRepository.saveAll(coursCryptosAleatoires);
        firestoreService.synchronizeLocalDbToFirestore(coursCryptosAleatoires);
    }

    @Nullable
    public Double analyser(final AnalyseCoursCryptoRequest request) {
        Specification<CoursCrypto> spec = CoursCryptoSpecification.filterByIdsAndDateRange(request.getIdsCryptomonnaie(), request.getDateHeureMin(), request.getDateHeureMax());
        // Exécutez la requête
        List<CoursCrypto> coursCryptoList = coursCryptoRepository.findAll(spec);


        List<Double> coursCrypto = coursCryptoList.stream()
            .map(CoursCrypto::getCours)
            .collect(Collectors.toList());;
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
        double pourcentageVariationAleatoire = -POURCENTAGE_MAX_VARIATION + (Math.random() * (POURCENTAGE_MAX_VARIATION * 2));

        // S'assure que la valeur ne dépasse pas les limites de la base de données (10^13)
        return Math.min(dernierCours * (1 + (pourcentageVariationAleatoire / 100.0)), VALEUR_COURS_MAX);
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
