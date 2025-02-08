package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString(doNotUseGetters = true)
@Entity
public class Cryptomonnaie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false, unique = true)
    private String designation;

    @Column(length = 10, nullable = false, unique = true)
    private String symbole;
}
