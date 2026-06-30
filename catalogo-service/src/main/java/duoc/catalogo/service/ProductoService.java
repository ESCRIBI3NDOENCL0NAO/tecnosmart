package duoc.catalogo.service;

import duoc.catalogo.model.entity.Categoria;
import duoc.catalogo.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import duoc.catalogo.model.dto.ProductoRequestDTO;
import duoc.catalogo.model.entity.Producto;
import duoc.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Producto guardarProducto(ProductoRequestDTO dto) {
        log.info("Creando nuevo producto: {}", dto.getNombre());

        // Validamos que la categoría enviada realmente exista en la base de datos
        Categoria cat = validarCategoria(dto.getCategoriaId());

        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setCategoria(cat);

        return productoRepository.save(p);
    }

    public List<Producto> listarLosProductos() {
        log.info("Consultando todos los productos del catálogo");
        return productoRepository.findAll();
    }

    public Producto buscarProductoPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public Producto actualizarProducto(Long id, ProductoRequestDTO dto) {
        log.info("Actualizando producto ID: {}", id);

        Producto p = buscarProductoPorId(id);
        Categoria cat = validarCategoria(dto.getCategoriaId());

        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setCategoria(cat);

        return productoRepository.save(p);
    }

    public void eliminarProducto(Long id) {
        log.warn("Eliminando producto ID: {}", id);
        Producto p = buscarProductoPorId(id);
        productoRepository.delete(p);
    }

    // metodo de validación para las categorías
    private Categoria validarCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> {
                    log.error("Error: La categoría con ID {} no existe", categoriaId);
                    return new RuntimeException("Categoría no encontrada");
                });
    }
}

/* Explicación: válida que no creemos un producto sin una categoria, y se encarga de transformar
el 'categoriaId' en una categoria real. */
