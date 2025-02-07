package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO;
import mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionUtilisateurDTO;
import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByUtilisateurIdOrderByDateHeureDesc(Integer idUtilisateur);

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
        LEFT JOIN CoursCrypto cc ON cc.cryptomonnaie.id = t.cryptomonnaie.id
        AND cc.dateHeure = (
            SELECT MAX(cc2.dateHeure)
            FROM CoursCrypto cc2
            WHERE cc2.cryptomonnaie.id = t.cryptomonnaie.id
        ) 
        ORDER BY t.dateHeure DESC
    """)
    List<HistoriqueTransactionDTO> findHistoriqueGlobale();

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionUtilisateurDTO(
            u.id,
            u.email,
            CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.quantite * ht.cours ELSE 0 END) AS Double),
            CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.quantite * ht.cours ELSE 0 END) AS Double),
            u.fondsActuel
        )
        FROM Transaction ht
            JOIN ht.utilisateur u
        GROUP BY u.id
    """)
    List<ResumeHistoriqueTransactionUtilisateurDTO> findResumesHistoriquesTransactionGroupByUtilisateur();

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO(
            c.designation,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.montantCommission ELSE 0 END) AS Double)
            END,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.montantCommission ELSE 0 END) AS Double)
                 ELSE CAST(AVG(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.montantCommission ELSE 0 END) AS Double)
            END
        )
        FROM Transaction ht
            JOIN ht.cryptomonnaie c
        WHERE (:idCryptomonnaie IS NULL OR c.id = :idCryptomonnaie)
            AND (:dateHeureMin  IS NULL OR ht.dateHeure >= :dateHeureMin)
            AND (:dateHeureMax  IS NULL OR ht.dateHeure <= :dateHeureMax)
        GROUP BY c.id
    """)
    ResultatAnalyseCommissionDTO analyserCommission(
        TypeAnalyseCommission typeAnalyse,
        @Nullable Integer idCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);
}
