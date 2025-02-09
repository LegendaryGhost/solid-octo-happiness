package mg.itu.cryptomonnaie.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String email;

    @Column
    private String motDePasse;

    public Admin(String email, String motDePasse) {
	this.email = email;
	this.motDePasse = motDePasse;
    }
}
