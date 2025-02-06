package mg.itu.cryptomonnaie.dto;

public record ResumeHistoriqueTransactionUtilisateurDTO(
    Integer idUtilisateur,
    String  emailUtilisateur,
    Double  sommeAchats,
    Double  sommeVentes,
    Double  valeurPortefeuille
) { }
