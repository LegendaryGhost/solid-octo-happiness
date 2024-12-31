package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "type_action")
public class TypeAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String designation;
}