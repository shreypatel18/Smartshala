package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface TestRepository extends JpaRepository<Test, Integer> {
}
