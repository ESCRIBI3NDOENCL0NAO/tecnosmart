package duoc.notificaciones.repository;

import duoc.notificaciones.model.ModelNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryNotificacion extends JpaRepository<ModelNotificacion, Long> {
}   // Spring Boot nos regala el CRUD completo al heredar de aquí