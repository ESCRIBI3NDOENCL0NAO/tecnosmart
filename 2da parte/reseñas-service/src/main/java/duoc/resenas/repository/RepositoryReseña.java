package duoc.resenas.repository;

import duoc.resenas.model.ModelReseña;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryReseña extends JpaRepository<ModelReseña, Long> {
} // Spring Boot nos regala el CRUD completo al heredar de aquí