package SmartShala.SmartShala.Repository;

import SmartShala.SmartShala.Entities.Admin;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
}
