package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
