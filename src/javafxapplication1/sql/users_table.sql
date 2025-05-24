CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(60) NOT NULL,
  role ENUM('Practicante','Paciente','Docente','Operator','Dentist') NOT NULL
);