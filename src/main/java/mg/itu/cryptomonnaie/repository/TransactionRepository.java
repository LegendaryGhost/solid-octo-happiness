package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;
import mg.itu.cryptomonnaie.projections.ResultatAnalyseCommission;
import mg.itu.cryptomonnaie.projections.ResumeHistoriqueTransactionUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByUtilisateurIdOrderByDateHeureDesc(String idUtilisateur);

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO(
            t.utilisateur.id,
            t.utilisateur.email,
            t.cryptomonnaie.id,
            t.cryptomonnaie.designation,
            t.typeTransaction,
            t.quantite,
            t.dateHeure,
            t.cours,
            cc.cours
        )
        FROM Transaction t
                LEFT JOIN 
            CoursCrypto cc ON cc.cryptomonnaie.id = t.cryptomonnaie.id AND 
            cc.dateHeure = (
                SELECT MAX(cc2.dateHeure)
                FROM CoursCrypto cc2
                WHERE cc2.cryptomonnaie.id = t.cryptomonnaie.id
            )
        WHERE (:idCryptomonnaie IS NULL OR t.cryptomonnaie.id = :idCryptomonnaie) AND 
              (:idUtilisateur   IS NULL OR t.utilisateur.id = :idUtilisateur)
        ORDER BY t.dateHeure DESC
    """)
    List<HistoriqueTransactionDTO> findHistoriqueGlobale(
        @Nullable Integer idCryptomonnaie,
        @Nullable String idUtilisateur);

    @Query("""
        SELECT
            c.designation AS cryptomonnaie,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN t.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN t.montantCommission ELSE 0 END) AS Double)
            END AS valeurPourAchat,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN t.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN t.montantCommission ELSE 0 END) AS Double)
            END AS valeurPourVente
        FROM Transaction t
            JOIN t.cryptomonnaie c
        WHERE (:idCryptomonnaie IS NULL OR c.id = :idCryptomonnaie)
            AND (COALESCE(:dateHeureMin, t.dateHeure) >= :dateHeureMin)
            AND (COALESCE(:dateHeureMax, t.dateHeure) <= :dateHeureMax)
        GROUP BY c.id
    """)
    ResultatAnalyseCommission analyserCommission(
        TypeAnalyseCommission typeAnalyse,
        @Nullable Integer idCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);


    @Query("""
        SELECT u.id,
               u.email,
               CAST(SUM(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN t.quantite * t.cours ELSE 0 END) AS Double),
               CAST(SUM(CASE WHEN t.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN t.quantite * t.cours ELSE 0 END) AS Double),
               u.fondsActuel
        FROM Transaction t
            JOIN t.utilisateur u
        GROUP BY u.id
    """)
    List<ResumeHistoriqueTransactionUtilisateur> findResumesHistoriquesTransactionGroupByUtilisateur();
}
