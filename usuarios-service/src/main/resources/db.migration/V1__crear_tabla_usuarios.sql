CREATE TABLE usuarios (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        rut VARCHAR(50) NOT NULL,
        nombre VARCHAR(255) NOT NULL,
        email VARCHAR(255) NOT NULL,
        telefono VARCHAR(50) NOT NULL
);