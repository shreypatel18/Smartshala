package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Subject;
import SmartShala.SmartShala.Entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findBySubject(Subject subject);

}
