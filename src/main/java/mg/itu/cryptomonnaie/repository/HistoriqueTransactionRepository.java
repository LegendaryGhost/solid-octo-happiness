package mg.itu.cryptomonnaie.repository;

import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionUtilisateurDTO;
import mg.itu.cryptomonnaie.entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
        SELECT NEW mg.itu.cryptomonnaie.dto.ResumeHistoriqueTransactionUtilisateurDTO(
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
    List<ResumeHistoriqueTransactionUtilisateurDTO> findResumeTransactionGroupByUtilisateur();
}
