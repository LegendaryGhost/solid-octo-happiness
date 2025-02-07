package mg.itu.cryptomonnaie.dto;

public record ResumeHistoriqueTransactionUtilisateurDTO(
    Integer idUtilisateur,
    String  emailUtilisateur,
    Double  totalAchats,
    Double  totalVentes,
    Double  valeurPortefeuille
) { }
