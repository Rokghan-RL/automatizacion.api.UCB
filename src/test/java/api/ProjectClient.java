package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * ProjectClient: Encapsula el ciclo de vida CRUD para el recurso Projects en Todo.ly.
 * Utiliza autenticación basada en header 'Token: [TokenString]'.
 */
public class ProjectClient extends BaseApiClient {

    /**
     * Crea un nuevo proyecto en Todo.ly mediante POST a /projects.json.
     *
     * @param token          Token de sesión del usuario.
     * @param projectPayload Cuerpo de la petición en JSON (ej. Content, Icon).
     * @return Response de RestAssured.
     */
    public Response createProject(String token, Map<String, Object> projectPayload) {
        return RestAssured.given()
                .spec(getAuthenticatedRequestSpec(token))
                .body(projectPayload)
                .when()
                .post(ApiEndpoints.PROJECTS);
    }

    /**
     * Método de conveniencia para crear un proyecto especificando su nombre.
     *
     * @param token       Token de sesión del usuario.
     * @param projectName Nombre o título del proyecto.
     * @return Response de RestAssured.
     */
    public Response createProject(String token, String projectName) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("Content", projectName);
        return createProject(token, payload);
    }

    /**
     * Actualiza las propiedades de un proyecto existente mediante PUT a /projects/[Id].json.
     *
     * @param token         Token de sesión del usuario.
     * @param projectId     Identificador numérico del proyecto.
     * @param updatePayload Mapa con los campos a modificar.
     * @return Response de RestAssured.
     */
    public Response updateProject(String token, int projectId, Map<String, Object> updatePayload) {
        return RestAssured.given()
                .spec(getAuthenticatedRequestSpec(token))
                .body(updatePayload)
                .when()
                .put(ApiEndpoints.getProjectByIdEndpoint(projectId));
    }

    /**
     * Método de conveniencia para renombrar un proyecto.
     *
     * @param token     Token de sesión del usuario.
     * @param projectId Identificador del proyecto.
     * @param newName   Nuevo nombre para el proyecto.
     * @return Response de RestAssured.
     */
    public Response updateProject(String token, int projectId, String newName) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("Content", newName);
        return updateProject(token, projectId, payload);
    }

    /**
     * Elimina un proyecto mediante DELETE a /projects/[Id].json.
     *
     * @param token     Token de sesión del usuario.
     * @param projectId Identificador del proyecto a remover.
     * @return Response de RestAssured con confirmación de borrado (Deleted: true).
     */
    public Response deleteProject(String token, int projectId) {
        return RestAssured.given()
                .spec(getAuthenticatedRequestSpec(token))
                .when()
                .delete(ApiEndpoints.getProjectByIdEndpoint(projectId));
    }

    /**
     * Obtiene los detalles de un proyecto específico mediante GET a /projects/[Id].json.
     *
     * @param token     Token de sesión del usuario.
     * @param projectId Identificador del proyecto.
     * @return Response de RestAssured.
     */
    public Response getProjectById(String token, int projectId) {
        return RestAssured.given()
                .spec(getAuthenticatedRequestSpec(token))
                .when()
                .get(ApiEndpoints.getProjectByIdEndpoint(projectId));
    }

    /**
     * Extrae el ID numérico del proyecto desde la respuesta JSON.
     *
     * @param response Objeto Response retornado por el API.
     * @return ID del proyecto.
     */
    public int extractProjectId(Response response) {
        return response.jsonPath().getInt("Id");
    }
}
