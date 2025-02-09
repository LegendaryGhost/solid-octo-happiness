package mg.itu.cryptomonnaie.service;

import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.entity.Admin;
import mg.itu.cryptomonnaie.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminAuthenticationService {

    private final AdminRepository adminRepository;

    public boolean authentifier(Admin credentials) {
	Optional<Admin> adminOptional = adminRepository.findByEmail(credentials.getEmail());

	if (adminOptional.isEmpty()) {
	    return false;
	}

	Admin admin = adminOptional.get();
	return Objects.equals(admin.getMotDePasse(), credentials.getMotDePasse());
    }

}
