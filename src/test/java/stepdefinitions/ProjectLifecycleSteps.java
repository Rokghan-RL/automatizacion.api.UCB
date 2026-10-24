package stepdefinitions;

import api.AuthenticationClient;
import api.ProjectClient;
import api.UserClient;
import context.TestContext;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.restassured.response.Response;
import org.testng.Assert;
import utils.DataGenerator;

/**
 * ProjectLifecycleSteps: Contiene la definición de los pasos del escenario Gherkin.
 *
 * Principios aplicados:
 * - CERO lógica RestAssured o construcción de peticiones HTTP aquí.
 * - Toda la comunicación con Todo.ly se delega a las clases cliente (UserClient, AuthenticationClient, ProjectClient).
 * - Se comparte el estado entre pasos mediante TestContext inyectado por Cucumber.
 * - Validación rigurosa de respuestas mediante aserciones de TestNG.
 */
public class ProjectLifecycleSteps {

    private final UserClient userClient;
    private final AuthenticationClient authClient;
    private final ProjectClient projectClient;
    private final TestContext context;

    /**
     * Constructor con inyección de dependencias para TestContext (gestionado por Cucumber PicoContainer).
     *
     * @param context Instancia compartida del contexto de prueba para el escenario en curso.
     */
    public ProjectLifecycleSteps(TestContext context) {
        this.context = context;
        this.userClient = new UserClient();
        this.authClient = new AuthenticationClient();
        this.projectClient = new ProjectClient();
    }

    /**
     * Constructor por defecto para fallback.
     */
    public ProjectLifecycleSteps() {
        this(new TestContext());
    }

    // ========================================================
    // 1. PASOS DE REGISTRO DE USUARIO
    // ========================================================

    @Dado("que se generan credenciales dinámicas para un nuevo usuario")
    public void queSeGeneranCredencialesDinamicasParaUnNuevoUsuario() {
        context.setUserEmail(DataGenerator.generateUniqueEmail());
        context.setUserPassword(DataGenerator.generatePassword());
        context.setUserFullName(DataGenerator.generateFullName());
    }

    @Cuando("se envía la solicitud para registrar al nuevo usuario")
    public void seEnviaLaSolicitudParaRegistrarAlNuevoUsuario() {
        Response response = userClient.createUser(
                context.getUserEmail(),
                context.getUserPassword(),
                context.getUserFullName()
        );
        context.setLastResponse(response);
    }

    @Y("el cuerpo de la respuesta debe contener un ID de usuario válido y el correo registrado")
    public void elCuerpoDeLaRespuestaDebeContenerUnIdDeUsuarioValidoYElCorreoRegistrado() {
        Response response = context.getLastResponse();
        Integer userId = response.jsonPath().getInt("Id");
        String returnedEmail = response.jsonPath().getString("Email");

        Assert.assertNotNull(userId, "El campo 'Id' del usuario no debe ser nulo.");
        Assert.assertTrue(userId > 0, "El ID de usuario debe ser mayor a cero.");
        Assert.assertEquals(returnedEmail, context.getUserEmail(), "El email retornado no coincide con el enviado.");
    }

    // ========================================================
    // 2. PASOS DE AUTENTICACIÓN Y TOKEN
    // ========================================================

    @Cuando("se solicita un token de autenticación mediante Basic Auth con las credenciales creadas")
    public void seSolicitaUnTokenDeAutenticacionMedianteBasicAuthConLasCredencialesCreadas() {
        Response response = authClient.getAuthenticationToken(
                context.getUserEmail(),
                context.getUserPassword()
        );
        context.setLastResponse(response);
    }

    @Y("se debe obtener y almacenar un TokenString válido")
    public void seDebeObtenerYAlmacenarUnTokenStringValido() {
        String token = authClient.extractToken(context.getLastResponse());

        Assert.assertNotNull(token, "El 'TokenString' retornado no debe ser nulo.");
        Assert.assertFalse(token.isBlank(), "El 'TokenString' retornado no debe estar vacío.");

        context.setTokenString(token);
    }

    // ========================================================
    // 3. PASOS DE CREACIÓN DE PROYECTO
    // ========================================================

    @Cuando("se crea un nuevo proyecto con el nombre {string}")
    public void seCreaUnNuevoProyectoConElNombre(String projectName) {
        context.setCurrentProjectName(projectName);
        Response response = projectClient.createProject(context.getTokenString(), projectName);
        context.setLastResponse(response);
    }

    @Y("el proyecto creado debe tener el nombre {string} y un ID válido")
    public void elProyectoCreadoDebeTenerElNombreYUnIdValido(String expectedName) {
        Response response = context.getLastResponse();
        int projectId = projectClient.extractProjectId(response);
        String actualName = response.jsonPath().getString("Content");
        Boolean isDeleted = response.jsonPath().getBoolean("Deleted");

        Assert.assertTrue(projectId > 0, "El ID del proyecto creado debe ser mayor a cero.");
        Assert.assertEquals(actualName, expectedName, "El nombre del proyecto creado no coincide con el esperado.");
        Assert.assertFalse(isDeleted, "Un proyecto recién creado no debe tener 'Deleted' en true.");

        context.setProjectId(projectId);
    }

    // ========================================================
    // 4. PASOS DE ACTUALIZACIÓN DE PROYECTO
    // ========================================================

    @Cuando("se actualiza el nombre del proyecto creado a {string}")
    public void seActualizaElNombreDelProyectoCreadoA(String newProjectName) {
        Response response = projectClient.updateProject(
                context.getTokenString(),
                context.getProjectId(),
                newProjectName
        );
        context.setCurrentProjectName(newProjectName);
        context.setLastResponse(response);
    }

    @Y("el nombre del proyecto en la respuesta debe ser {string}")
    public void elNombreDelProyectoEnLaRespuestaDebeSer(String expectedProjectName) {
        Response response = context.getLastResponse();
        String actualProjectName = response.jsonPath().getString("Content");
        int returnedId = response.jsonPath().getInt("Id");

        Assert.assertEquals(actualProjectName, expectedProjectName, "El nombre actualizado del proyecto no coincide.");
        Assert.assertEquals(returnedId, context.getProjectId().intValue(), "El ID del proyecto actualizado cambió inesperadamente.");
    }

    // ========================================================
    // 5. PASOS DE ELIMINACIÓN DE PROYECTO
    // ========================================================

    @Cuando("se envía la solicitud para eliminar el proyecto creado")
    public void seEnviaLaSolicitudParaEliminarElProyectoCreado() {
        Response response = projectClient.deleteProject(
                context.getTokenString(),
                context.getProjectId()
        );
        context.setLastResponse(response);
    }

    @Y("la respuesta debe confirmar que el proyecto fue eliminado con el campo Deleted en true")
    public void laRespuestaDebeConfirmarQueElProyectoFueEliminadoConElCampoDeletedEnTrue() {
        Response response = context.getLastResponse();
        Boolean isDeleted = response.jsonPath().getBoolean("Deleted");
        int deletedId = response.jsonPath().getInt("Id");

        Assert.assertNotNull(isDeleted, "El campo 'Deleted' no debe ser nulo en la respuesta.");
        Assert.assertTrue(isDeleted, "El campo 'Deleted' debe ser true tras la eliminación.");
        Assert.assertEquals(deletedId, context.getProjectId().intValue(), "El ID del proyecto eliminado no coincide con el ID original.");
    }

    // ========================================================
    // ASERCIÓN GENÉRICA DE CÓDIGO DE ESTADO HTTP
    // ========================================================

    @Entonces("el código de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int expectedStatusCode) {
        Response response = context.getLastResponse();
        Assert.assertNotNull(response, "No existe una respuesta HTTP registrada en el contexto de prueba.");
        Assert.assertEquals(
                response.getStatusCode(),
                expectedStatusCode,
                String.format("Código de estado HTTP incorrecto. Esperado: %d, Recibido: %d. Respuesta: %s",
                        expectedStatusCode,
                        response.getStatusCode(),
                        response.asString()
                )
        );
    }
}
