package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Answer;
import SmartShala.SmartShala.Entities.Classroom;
import SmartShala.SmartShala.Entities.Student;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepo extends JpaRepository<Classroom, Integer> {
    Optional<Classroom> findByName(String name);
}
