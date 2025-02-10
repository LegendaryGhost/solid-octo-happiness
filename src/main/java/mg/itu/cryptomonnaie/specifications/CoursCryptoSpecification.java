package mg.itu.cryptomonnaie.specifications;

import jakarta.persistence.criteria.Predicate;
import mg.itu.cryptomonnaie.entity.CoursCrypto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class CoursCryptoSpecification {

    public static Specification<CoursCrypto> filterByIdsAndDateRange(
            List<Integer> idsCryptomonnaie,
            LocalDateTime dateHeureMin,
            LocalDateTime dateHeureMax
    ) {
        
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (idsCryptomonnaie != null && !idsCryptomonnaie.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    root.get("cryptomonnaie").get("id").in(idsCryptomonnaie));
            }

            if (dateHeureMin != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateHeure"), dateHeureMin));
            }

            if (dateHeureMax != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.lessThanOrEqualTo(root.get("dateHeure"), dateHeureMax));
            }

            return predicate;
        };
    }
}
