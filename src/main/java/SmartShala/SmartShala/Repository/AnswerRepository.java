package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Optional<Answer> findByStudentIdAndTest_Id(int studentId, int testId);
}
