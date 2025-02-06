package mg.itu.cryptomonnaie.service;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueTransactionDTO;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.HistoriqueTransaction;
import mg.itu.cryptomonnaie.repository.HistoriqueTransactionRepository;

@RequiredArgsConstructor
@Service
public class HistoriqueTransactionService {
    private final HistoriqueTransactionRepository historiqueTransactionRepository;

    public List<HistoriqueTransactionDTO> getHistoriqueGlobale() {
        return historiqueTransactionRepository.findHistoriqueGlobale();
    }

    @Transactional
    public List<HistoriqueTransaction> getAllByUtilisateurIdOrderByDateHeureDesc(final Integer idUtilisateur) {
        return historiqueTransactionRepository.findAllByUtilisateurIdOrderByDateHeureDesc(idUtilisateur);
    }
}
