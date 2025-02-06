package mg.itu.cryptomonnaie.dto;

public record ResumeHistoriqueTransactionDTO(
    Integer idUtilisateur,
    String  emailUtilisateur,
    Double  totalAchats,
    Double  totalVentes,
    Double  valeurPortefeuille
) { }
