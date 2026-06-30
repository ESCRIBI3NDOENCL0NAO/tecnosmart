package duoc.pagos.repository;

import duoc.pagos.model.ModelPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryPago extends JpaRepository<ModelPago, Long> {
}   // Spring Boot nos regala el CRUD completo al heredar de aquí
