package mg.itu.cryptomonnaie.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import mg.itu.cryptomonnaie.entity.EtatFond;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.entity.TypeTransaction;

@Data
public class HistoriqueFondDTO {
    private Long id;
    private LocalDateTime dateTransaction;
    private Double montant;
    private String numCarteBancaire;
    private String email;
    private double fondActuel;
    private String typeTransaction;
    private String etatFond;
}
