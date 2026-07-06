package duoc.catalogo.service;

import duoc.catalogo.dto.CategoriaRequestDTO;
import duoc.catalogo.model.Categoria;
import duoc.catalogo.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria guardarCategoria(CategoriaRequestDTO dto) {
        log.info("Creando nueva categoría: {}", dto.getNombre());

        if (categoriaRepository.existsByNombre(dto.getNombre())) {
            log.warn("Categoría duplicada: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarLasCategorias() {
        log.info("Consultando la lista completa de categorías");
        return categoriaRepository.findAll();
    }

    public Categoria buscarCategoriasPorId(Long id) {
        log.info("Buscando categoría con ID: {}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
    }
}