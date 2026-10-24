package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;

/**
 * AuthenticationClient: Encapsula la obtención de tokens de sesión en Todo.ly
 * a través de Basic Authentication contra el endpoint /authentication/token.json.
 */
public class AuthenticationClient extends BaseApiClient {

    /**
     * Solicita un token de autenticación para las credenciales provistas mediante HTTP Basic Auth.
     *
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Response de RestAssured conteniendo el TokenString.
     */
    public Response getAuthenticationToken(String email, String password) {
        return RestAssured.given()
                .spec(getBaseRequestSpec())
                .auth()
                .preemptive()
                .basic(email, password)
                .when()
                .get(ApiEndpoints.AUTHENTICATION_TOKEN);
    }

    /**
     * Extrae el valor del campo 'TokenString' desde la respuesta de autenticación.
     *
     * @param response Objeto Response obtenido tras llamar a getAuthenticationToken.
     * @return Cadena con el token de sesión (TokenString) o null si no existe.
     */
    public String extractToken(Response response) {
        return response.jsonPath().getString("TokenString");
    }
}
