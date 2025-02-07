package mg.itu.cryptomonnaie.projections;

public interface ResumeHistoriqueTransactionUtilisateur {
    Integer getIdUtilisateur();
    String  getEmailUtilisateur();
    Double  getTotalAchats();
    Double  getTotalVentes();
    Double  getValeurPortefeuille();
}
