package context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * TestContext: Almacena y comparte el estado en tiempo de ejecución entre los diferentes
 * pasos (Step Definitions) de un escenario de Cucumber.
 *
 * Implementa un mecanismo de Dependency Injection liviano para evitar variables estáticas
 * globales frágiles y permitir ejecuciones aisladas y concurrentes.
 */
public class TestContext {

    // Credenciales de usuario
    private String userEmail;
    private String userPassword;
    private String userFullName;

    // Tokens y referencias de entidades
    private String tokenString;
    private Integer projectId;
    private String currentProjectName;

    // Última respuesta HTTP recibida para aserciones posteriores
    private Response lastResponse;

    // Almacén genérico clave-valor para datos adicionales
    private final Map<String, Object> storage = new HashMap<>();

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getCurrentProjectName() {
        return currentProjectName;
    }

    public void setCurrentProjectName(String currentProjectName) {
        this.currentProjectName = currentProjectName;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public void set(String key, Object value) {
        storage.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) storage.get(key);
    }

    /**
     * Limpia el contexto para reiniciar el estado en caso de reutilización.
     */
    public void reset() {
        userEmail = null;
        userPassword = null;
        userFullName = null;
        tokenString = null;
        projectId = null;
        currentProjectName = null;
        lastResponse = null;
        storage.clear();
    }
}
