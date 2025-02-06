package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.TypeOperation;
import mg.itu.cryptomonnaie.repository.TypeOperationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TypeOperationService {
    private TypeOperationRepository typeOperationRepository;

    public List<TypeOperation> getAll() {
        return typeOperationRepository.findAll();
    }

    public Optional<TypeOperation> getById(final Integer id) {
        return typeOperationRepository.findById(id);
    }
}
