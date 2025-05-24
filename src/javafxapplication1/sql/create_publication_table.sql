-- Script para crear la tabla de publicaciones

CREATE TABLE IF NOT EXISTS publication (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    fee DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento de las consultas
CREATE INDEX IF NOT EXISTS idx_publication_title ON publication(title);
CREATE INDEX IF NOT EXISTS idx_publication_fee ON publication(fee);
CREATE INDEX IF NOT EXISTS idx_publication_created_at ON publication(created_at);