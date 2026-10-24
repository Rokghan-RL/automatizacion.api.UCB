package utils;

import java.util.UUID;

/**
 * DataGenerator: Utilidad para la generación de datos de prueba dinámicos y únicos.
 * Previene colisiones de cuentas de usuario duplicadas en la API pública de Todo.ly.
 */
public final class DataGenerator {

    private DataGenerator() {
        // Prevenir instanciación de clase de utilidades
    }

    /**
     * Genera un correo electrónico garantizado como único usando timestamp y un sufijo aleatorio.
     *
     * @return Dirección de correo (ej. "qa_test_1711234567890_a1b2@testmail.com").
     */
    public static String generateUniqueEmail() {
        long timestamp = System.currentTimeMillis();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4);
        return String.format("qa_test_%d_%s@testmail.com", timestamp, randomSuffix);
    }

    /**
     * Genera una contraseña estándar segura válida para Todo.ly.
     *
     * @return Cadena con contraseña robusta.
     */
    public static String generatePassword() {
        return "Password123!";
    }

    /**
     * Genera un nombre completo representativo para el usuario.
     *
     * @return Nombre completo dinámico.
     */
    public static String generateFullName() {
        long timestamp = System.currentTimeMillis() % 100000;
        return "QA Engineer " + timestamp;
    }

    /**
     * Genera un nombre de proyecto dinámico con prefijo configurable.
     *
     * @param prefix Prefijo deseado para el nombre.
     * @return Nombre de proyecto formateado con timestamp.
     */
    public static String generateProjectName(String prefix) {
        return prefix + " " + System.currentTimeMillis();
    }
}
