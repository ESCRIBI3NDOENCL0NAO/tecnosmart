package duoc.envios.repository;

import duoc.envios.model.ModelEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Se encarga de hablar con MySQL
public interface RepositoryEnvio extends JpaRepository<ModelEnvio, Long> {
}  // Spring Boot nos regala el CRUD completo al heredar de aquí
