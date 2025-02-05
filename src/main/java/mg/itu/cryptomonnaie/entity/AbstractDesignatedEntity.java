package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@MappedSuperclass
public abstract class AbstractDesignatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Setter
    @Column(length = 50, nullable = false, unique = true)
    protected String designation;
}
