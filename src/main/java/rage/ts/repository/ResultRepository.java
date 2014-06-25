package rage.ts.repository;

import java.util.List;
import rage.ts.domain.Participant;
import rage.ts.domain.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<TestResult, Long> {

    List<TestResult> findByParticipantAndTestTypeAndInfo(Participant participant, String testType, String info);
}
