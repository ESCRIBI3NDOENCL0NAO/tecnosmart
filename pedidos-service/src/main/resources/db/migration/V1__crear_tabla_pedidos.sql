CREATE TABLE pedidos (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     cliente_id BIGINT NOT NULL,
     producto_id BIGINT NOT NULL,
     cantidad INT NOT NULL,
     total_estimado INT NOT NULL,
     estado VARCHAR(50) NOT NULL,
     fecha_creacion TIMESTAMP NOT NULL
);