package duoc.favoritos.repository;

import duoc.favoritos.model.ModelFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryFavorito extends JpaRepository<ModelFavorito, Long> {
}   // Spring Boot nos regala el CRUD completo al heredar de aquí
