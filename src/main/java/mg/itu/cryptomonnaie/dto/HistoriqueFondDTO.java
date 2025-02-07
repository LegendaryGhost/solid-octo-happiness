package mg.itu.cryptomonnaie.dto;

import java.time.LocalDateTime;

import lombok.Data;

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
