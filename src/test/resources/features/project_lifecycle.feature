# language: es
Característica: Gestión del ciclo de vida de proyectos en la API de Todo.ly
  Como QA Automation Engineer
  Quiero validar el flujo de negocio de registro, autenticación y CRUD de proyectos
  Para asegurar la estabilidad y correcta integración de la API REST de Todo.ly

  @Smoke @Regression @E2E
  Escenario: Crear un usuario, autenticar mediante token y gestionar el ciclo de vida completo de un proyecto
    Dado que se generan credenciales dinámicas para un nuevo usuario
    Cuando se envía la solicitud para registrar al nuevo usuario
    Entonces el código de respuesta debe ser 200
    Y el cuerpo de la respuesta debe contener un ID de usuario válido y el correo registrado
    
    Cuando se solicita un token de autenticación mediante Basic Auth con las credenciales creadas
    Entonces el código de respuesta debe ser 200
    Y se debe obtener y almacenar un TokenString válido
    
    Cuando se crea un nuevo proyecto con el nombre "Proyecto Automatizado UCB"
    Entonces el código de respuesta debe ser 200
    Y el proyecto creado debe tener el nombre "Proyecto Automatizado UCB" y un ID válido
    
    Cuando se actualiza el nombre del proyecto creado a "Proyecto Automatizado UCB - Actualizado"
    Entonces el código de respuesta debe ser 200
    Y el nombre del proyecto en la respuesta debe ser "Proyecto Automatizado UCB - Actualizado"
    
    Cuando se envía la solicitud para eliminar el proyecto creado
    Entonces el código de respuesta debe ser 200
    Y la respuesta debe confirmar que el proyecto fue eliminado con el campo Deleted en true
