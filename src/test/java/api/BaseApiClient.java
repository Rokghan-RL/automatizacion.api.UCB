package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * BaseApiClient: Clase base para todos los clientes HTTP de la API de Todo.ly.
 * Configura la RequestSpecification común con la Base URL, Content-Type JSON
 * y configuraciones globales de RestAssured.
 */
public abstract class BaseApiClient {

    /**
     * Construye la especificación base para peticiones HTTP estándar.
     *
     * @return RequestSpecification configurada con Base URL y JSON Content-Type.
     */
    protected RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * Construye la especificación para peticiones que requieren autenticación por Token de Todo.ly.
     *
     * @param token Cadena del token de autenticación (TokenString).
     * @return RequestSpecification con el header 'Token' inyectado.
     */
    protected RequestSpecification getAuthenticatedRequestSpec(String token) {
        return getBaseRequestSpec()
                .header("Token", token);
    }
}
