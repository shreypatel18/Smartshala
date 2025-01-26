package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface SubjectRepository extends JpaRepository<Subject,Integer> {
}
