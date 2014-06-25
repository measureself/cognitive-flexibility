package rage.ts.repository;

import rage.ts.domain.AuthenticationInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationInformationRepository extends JpaRepository<AuthenticationInformation, Long> {
}
