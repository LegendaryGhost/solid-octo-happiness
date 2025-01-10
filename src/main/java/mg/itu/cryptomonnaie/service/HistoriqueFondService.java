package mg.itu.cryptomonnaie.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mg.itu.cryptomonnaie.entity.HistoriqueFond;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.repository.HistoriqueFondRepository;

@Service
public class HistoriqueFondService {
    @Autowired
    HistoriqueFondRepository historiqueFondRepository;

    public List<HistoriqueFond> transactionProfil(Profil profil) {
        List<HistoriqueFond> historiqueFonds = historiqueFondRepository.findTransactionsProfil(profil.getId());
        return historiqueFonds;
    }

    public List<HistoriqueFond> listeTransactions() {
        List<HistoriqueFond> historiqueFonds = historiqueFondRepository.findAll();
        return historiqueFonds;
    }
}
