package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * UserClient: Encapsula todas las operaciones HTTP relativas al recurso User en Todo.ly.
 * Aisla la lógica de RestAssured fuera de los Step Definitions.
 */
public class UserClient extends BaseApiClient {

    /**
     * Registra un nuevo usuario en Todo.ly enviando un POST a /user.json.
     *
     * @param userPayload Mapa con los campos Email, Password y FullName.
     * @return Objeto Response de RestAssured con la respuesta del servidor.
     */
    public Response createUser(Map<String, Object> userPayload) {
        return RestAssured.given()
                .spec(getBaseRequestSpec())
                .body(userPayload)
                .when()
                .post(ApiEndpoints.USER);
    }

    /**
     * Método de conveniencia para registrar un usuario a partir de credenciales directas.
     *
     * @param email    Correo electrónico único.
     * @param password Contraseña en texto plano.
     * @param fullName Nombre completo del usuario.
     * @return Objeto Response de RestAssured.
     */
    public Response createUser(String email, String password, String fullName) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("Email", email);
        payload.put("Password", password);
        payload.put("FullName", fullName);
        return createUser(payload);
    }
}
