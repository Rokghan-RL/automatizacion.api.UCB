package api;

/**
 * ApiEndpoints: Centraliza la Base URL y las rutas de los recursos de la API REST de Todo.ly.
 * Facilita el mantenimiento y previene el uso de cadenas "hardcodeadas" en los clientes.
 */
public final class ApiEndpoints {

    private ApiEndpoints() {
        // Constructor privado para prevenir instanciación de clase de constantes
    }

    public static final String BASE_URL = "https://todo.ly/api/";

    // Endpoints de Usuario
    public static final String USER = "user.json";

    // Endpoints de Autenticación
    public static final String AUTHENTICATION_TOKEN = "authentication/token.json";

    // Endpoints de Proyectos
    public static final String PROJECTS = "projects.json";
    public static final String PROJECT_BY_ID = "projects/%d.json";

    /**
     * Construye dinámicamente la ruta de un proyecto específico según su identificador.
     *
     * @param projectId Identificador numérico del proyecto.
     * @return Ruta formateada (ej. "projects/12345.json").
     */
    public static String getProjectByIdEndpoint(int projectId) {
        return String.format(PROJECT_BY_ID, projectId);
    }
}
