package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
