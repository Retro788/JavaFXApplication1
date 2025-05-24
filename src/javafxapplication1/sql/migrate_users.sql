-- Script para migrar datos de la tabla 'login' a la nueva tabla 'users'

-- Primero, asegurarse de que la tabla 'users' existe
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Procedimiento para migrar los datos
DELIMITER //
CREATE PROCEDURE migrate_users()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_username VARCHAR(50);
    DECLARE v_password VARCHAR(255);
    DECLARE v_type VARCHAR(20);
    
    -- Cursor para recorrer los datos de la tabla 'login'
    DECLARE cur CURSOR FOR 
        SELECT username, password, type FROM login;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- Abrir cursor
    OPEN cur;
    
    -- Recorrer datos
    read_loop: LOOP
        FETCH cur INTO v_username, v_password, v_type;
        
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Insertar en la nueva tabla
        -- Nota: Aquí se inserta la contraseña sin hash, se debe actualizar manualmente después
        INSERT IGNORE INTO users (username, password_hash, role)
        VALUES (v_username, v_password, v_type);
    END LOOP;
    
    -- Cerrar cursor
    CLOSE cur;
    
    SELECT 'Migración completada. Recuerde actualizar las contraseñas con hash BCrypt.' AS message;
END //
DELIMITER ;

-- Ejecutar el procedimiento
CALL migrate_users();

-- Eliminar el procedimiento
DROP PROCEDURE IF EXISTS migrate_users;

-- Nota importante: Este script inserta las contraseñas sin hash.
-- Después de la migración, se deben actualizar las contraseñas con hash BCrypt
-- utilizando la aplicación o un script adicional.