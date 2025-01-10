package mg.itu.cryptomonnaie.request;

import lombok.Data;

@Data
public class HistoriqueFondRequest {

    private Double montant;
    private String numCarteBancaire;
    private Integer idTypeTransaction;

}
