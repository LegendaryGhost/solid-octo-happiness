package mg.itu.cryptomonnaie.service;

import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.entity.TypeTransaction;
import mg.itu.cryptomonnaie.repository.TypeTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TypeTransactionService {
    private final TypeTransactionRepository typeTransactionRepository;

    public List<TypeTransaction> liste() {
        return typeTransactionRepository.findAll();
    }
}
