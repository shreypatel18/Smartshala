package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
public interface ClassroomRepository extends JpaRepository<Classroom,Integer> {
}
