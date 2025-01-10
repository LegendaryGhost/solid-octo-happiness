package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "type_transaction")
public class TypeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String designation;
}
