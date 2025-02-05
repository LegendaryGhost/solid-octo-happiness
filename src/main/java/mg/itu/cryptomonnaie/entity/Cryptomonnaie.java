package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, doNotUseGetters = true)
@Entity
public class Cryptomonnaie extends AbstractDesignatedEntity {
    @Column(length = 150, nullable = false, unique = true)
    private String designation;
}
