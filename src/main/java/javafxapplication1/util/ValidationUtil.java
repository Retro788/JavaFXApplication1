package javafxapplication1.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Utilidad para validación de datos
 */
public class ValidationUtil {
    
    /**
     * Verifica si una cadena es numérica
     * @param s Cadena a verificar
     * @return true si es numérica, false en caso contrario
     */
    public static boolean isNumeric(String s) {
        return s != null && s.matches(NUMERIC_PATTERN);
    }
    
    /**
     * Verifica si una cadena es un número decimal
     * @param s Cadena a verificar
     * @return true si es un número decimal, false en caso contrario
     */
    public static boolean isDecimal(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(s);
            return s.matches(DECIMAL_PATTERN);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Verifica si una cadena es una fecha válida en formato ISO (yyyy-MM-dd)
     * @param s Cadena a verificar
     * @return true si es una fecha válida, false en caso contrario
     */
    public static boolean isDate(String s) {
        try { 
            LocalDate.parse(s); 
            return true; 
        } catch(DateTimeParseException e) { 
            return false; 
        }
    }
    
    /**
     * Verifica si una cadena es un correo electrónico válido
     * @param s Cadena a verificar
     * @return true si es un correo electrónico válido, false en caso contrario
     */
    public static boolean isEmail(String s) {
        return s != null && s.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    /**
     * Verifica si una cadena es un número de teléfono válido
     * @param s Cadena a verificar
     * @return true si es un número de teléfono válido, false en caso contrario
     */
    public static boolean isPhoneNumber(String s) {
        return s != null && s.matches(PHONE_PATTERN);
    }
    
    /**
     * Verifica si una cadena no está vacía
     * @param s Cadena a verificar
     * @return true si no está vacía, false en caso contrario
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }
    
    /**
     * Verifica si una cadena tiene una longitud mínima
     * @param s Cadena a verificar
     * @param minLength Longitud mínima
     * @return true si tiene la longitud mínima, false en caso contrario
     */
    public static boolean hasMinLength(String s, int minLength) {
        return s != null && s.length() >= minLength;
    }
    
    /**
     * Verifica si una cadena tiene una longitud máxima
     * @param s Cadena a verificar
     * @param maxLength Longitud máxima
     * @return true si tiene la longitud máxima, false en caso contrario
     */
    public static boolean hasMaxLength(String s, int maxLength) {
        return s != null && s.length() <= maxLength;
    }
}