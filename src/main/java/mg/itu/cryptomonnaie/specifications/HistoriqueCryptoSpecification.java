package mg.itu.cryptomonnaie.specifications;

import mg.itu.cryptomonnaie.entity.HistoriqueCrypto;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueCryptoSpecification {
    public static Specification<HistoriqueCrypto> filtrerHistorique(
            LocalDateTime dateHeureMin,
            LocalDateTime dateHeureMax,
            Long idCrypto,
            Long idProfil) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateHeureMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateAction"), dateHeureMin));
            }
            if (dateHeureMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateAction"), dateHeureMax));
            }
            if (idCrypto != null) {
                predicates.add(criteriaBuilder.equal(root.get("cryptomonnaie").get("id"), idCrypto));
            }
            if (idProfil != null) {
                predicates.add(criteriaBuilder.equal(root.get("profil").get("id"), idProfil));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
