package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
