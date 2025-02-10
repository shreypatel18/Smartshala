package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Result;
import SmartShala.SmartShala.Entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface ResultRepository extends JpaRepository<Result, Integer> {

    Optional<Result> findByStudentIdAndTest(int studentId, Test test);

}
