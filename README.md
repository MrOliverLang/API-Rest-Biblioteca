# API de Gestión de Biblioteca

## Descripción

API REST desarrollada con Spring Boot para la gestión de libros, usuarios y préstamos de la Biblioteca.  Permite realizar operaciones CRUD básicas sobre estas entidades.

## Ejecución

Puedes ejecutar la API de dos maneras:

**1. Ejecución por Línea de Comandos (Maven CLI):**

Si tienes Maven CLI instalado, sigue estos pasos:

1.  **Construir el Proyecto con Maven:**
    Abre una terminal en el directorio del proyecto y ejecuta:
    ```bash
    mvn clean install
    ```

2.  **Ejecutar la Aplicación Spring Boot:**
    En la raíz del proyecto, ejecuta:
    ```bash
    mvn spring-boot:run
    ```

**2. Ejecución con IntelliJ IDEA (Botones Maven):**

Si prefieres usar los botones de Maven integrados en IntelliJ IDEA (sin necesidad de CLI de Maven instalado), sigue estos pasos:

1.  **Abrir la ventana de herramientas "Maven":**
    En IntelliJ IDEA, localiza el panel de herramientas lateral derecho.  Deberías ver una pestaña llamada "Maven" (generalmente tiene un icono con una 'M'). Haz clic en esta pestaña para abrir la ventana de herramientas de Maven.

    [Textual description of UI element:  Maven Tool Window Tab in IntelliJ IDEA Side Panel]

2.  **Limpiar el proyecto (Maven Clean):**
    Dentro de la ventana de Maven, deberías ver un árbol con tu proyecto (`gestion-biblioteca`) y debajo las diferentes fases del ciclo de vida de Maven ("Lifecycle", "Plugins", "Dependencies", etc.).

    Navega hasta la sección **"Lifecycle"**.  Haz doble clic en **"clean"**.

    [Textual description of UI element: Maven Tool Window - Lifecycle Section - Clean Option]

    IntelliJ IDEA ejecutará la fase `clean` de Maven, limpiando el proyecto.

3.  **Construir el proyecto (Maven Install o Package):**
    Dentro de la sección "Lifecycle" de Maven, busca y haz doble clic en **"install"** (o **"package"** si solo quieres empaquetar sin instalar en el repositorio local).

    [Textual description of UI element: Maven Tool Window - Lifecycle Section - Install Option]

    [Textual description of UI element: Maven Tool Window - Lifecycle Section - Package Option]

    IntelliJ IDEA ejecutará la fase `install` (o `package`) de Maven, construyendo tu proyecto, compilando el código, y descargando dependencias si es necesario.

4.  **Ejecutar la Aplicación Spring Boot (spring-boot:run):**
    Ahora, dentro de la ventana de Maven, expande la sección **"Plugins"**.
    Busca y expande el plugin **"spring-boot"** (generalmente dentro de la lista de plugins).
    Dentro del plugin "spring-boot", busca y haz doble clic en el goal **"spring-boot:run"**.

    [Textual description of UI element: Maven Tool Window - Plugins Section - spring-boot Plugin - spring-boot:run Goal]

    IntelliJ IDEA ejecutará el goal `spring-boot:run` del plugin de Spring Boot, que arrancará tu aplicación Spring Boot.

5.  **Verificar la Ejecución y Acceder a la Documentación:**
    Observa la ventana de "Run" (o "Ejecutar") en IntelliJ IDEA. Deberías ver los logs de inicio de Spring Boot.  Cuando veas mensajes como "Started Application in X seconds" y la indicación del puerto (generalmente 8080), la aplicación estará en marcha.

    Luego, abre tu navegador y accede a la documentación de Swagger UI en:
    ```
    http://localhost:8080/swagger-ui.html
    ```

## Más Detalles

*   **Base de Datos:**  Configurada con H2 en memoria para desarrollo (los datos se pierden al detener la aplicación).
*   **Configuración:**  La configuración principal se encuentra en `application.properties`.

## Autor

[Oliver Dlouhý] 
