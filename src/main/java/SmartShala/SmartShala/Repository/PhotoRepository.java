package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
public interface PhotoRepository extends JpaRepository<Photo , Integer>{

}
