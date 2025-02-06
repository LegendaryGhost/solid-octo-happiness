package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO;
import mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.entity.*;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface HistoriqueTransactionRepository extends JpaRepository<HistoriqueTransaction, Integer> {

    List<HistoriqueTransaction> findAllByUtilisateurIdOrderByDateHeureDesc(Integer idUtilisateur);

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO(
            ht.utilisateur.id,
            ht.utilisateur.email,
            ht.cryptomonnaie.id,
            ht.cryptomonnaie.designation,
            ht.typeTransaction,
            ht.quantite,
            ht.dateHeure,
            ht.cours,
            cc.coursActuel
        )
        FROM HistoriqueTransaction ht
        LEFT JOIN CoursCrypto cc ON cc.cryptomonnaie.id = ht.cryptomonnaie.id
        AND cc.dateHeure = (
            SELECT MAX(cc2.dateHeure)
            FROM CoursCrypto cc2
            WHERE cc2.cryptomonnaie.id = ht.cryptomonnaie.id
        ) 
        ORDER BY ht.dateHeure DESC
    """)
    List<HistoriqueTransactionDTO> findHistoriqueGlobale();

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionDTO(
            u.id,
            u.email,
            CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.quantite * ht.cours ELSE 0 END) AS Double),
            CAST(SUM(CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.quantite * ht.cours ELSE 0 END) AS Double),
            u.fondsActuel
        )
        FROM HistoriqueTransaction ht
            JOIN ht.utilisateur u
        GROUP BY u.id
    """)
    List<ResumeHistoriqueTransactionDTO> findResumesHistoriquesTransactionGroupByUtilisateur();

    @Query("""
        SELECT NEW mg.itu.cryptomonnaie.dto.ResultatAnalyseCommissionDTO(
            c.designation,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(ht.quantite * ht.cours * CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.tauxCommissionAchat ELSE ht.tauxCommissionVente END) AS Double)
                 ELSE CAST(AVG(ht.quantite * ht.cours * CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.ACHAT THEN ht.tauxCommissionAchat ELSE ht.tauxCommissionVente END) AS Double)
            END,
            CASE WHEN :typeAnalyse = mg.itu.cryptomonnaie.enums.TypeAnalyseCommission.SOMME 
                 THEN CAST(SUM(ht.quantite * ht.cours * CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.tauxCommissionVente ELSE ht.tauxCommissionAchat END) AS Double)
                 ELSE CAST(AVG(ht.quantite * ht.cours * CASE WHEN ht.typeTransaction = mg.itu.cryptomonnaie.enums.TypeTransaction.VENTE THEN ht.tauxCommissionVente ELSE ht.tauxCommissionAchat END) AS Double)
            END
        )
        FROM HistoriqueTransaction ht
            JOIN ht.cryptomonnaie c
        WHERE (:idCryptomonnaie IS NULL OR c.id = :idCryptomonnaie)
            AND (:dateHeureMin IS NULL OR ht.dateHeure >= :dateHeureMin)
            AND (:dateHeureMax IS NULL OR ht.dateHeure <= :dateHeureMax)
        GROUP BY c.id
    """)
    ResultatAnalyseCommissionDTO analyserCommission(
        TypeAnalyseCommission typeAnalyse,
        @Nullable Integer idCryptomonnaie,
        @Nullable LocalDateTime dateHeureMin,
        @Nullable LocalDateTime dateHeureMax);
}
