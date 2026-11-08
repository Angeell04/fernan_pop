package controller;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Usuario;
import utils.Conexiones;

@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Capturar los datos enviados desde registro.jsp
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            int movil = Integer.parseInt(request.getParameter("movil"));
            String email = request.getParameter("email");
            String clave = request.getParameter("clave");

            // 2. Generar token único para la validación por URL
            String token = UUID.randomUUID().toString();

            // 3. Instanciar usuario y registrar en la base de datos (con estado inactivo/token)
            Usuario nuevoUsuario = new Usuario(clave, apellido, movil, nombre, email);
            GestionAPP gestionAPP = new GestionAPP();

            // Pasa el token al método de registro de tu lógica de negocio
            boolean registrado = gestionAPP.registro(
                    nuevoUsuario.getNombre(),
                    nuevoUsuario.getClave(),
                    nuevoUsuario.getApel(),
                    nuevoUsuario.getMovil(),
                    nuevoUsuario.getMail(),
                    token
            );

            // 4. Evaluar el resultado del registro
            if (registrado) {
                // Construir la URL de validación dinámica (compatible con localhost y ngrok)
                String scheme = request.getScheme();             // http o https
                String serverName = request.getServerName();     // localhost o xxxx.ngrok-free.app
                int serverPort = request.getServerPort();         // 8080 o 80/443
                String contextPath = request.getContextPath();   // /FernanPop

                String puertoStr = (serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort;
                String urlValidacion = scheme + "://" + serverName + puertoStr + contextPath + "/ValidarServlet?token=" + token;

                // Preparar el cuerpo del correo en HTML
                String asunto = "Activa tu cuenta en FernanPop";
                String mensajeEmail = "<h2>¡Bienvenido a FernanPop, " + nombre + "!</h2>"
                        + "<p>Gracias por registrarte. Haz clic en el siguiente enlace para activar tu cuenta:</p>"
                        + "<p style='margin: 20px 0;'>"
                        + "  <a href='" + urlValidacion + "' "
                        + "     style='background-color: #4f46e5; color: white; padding: 12px 20px; "
                        + "            text-decoration: none; border-radius: 8px; font-weight: bold; "
                        + "            display: inline-block;'>"
                        + "     Validar mi cuenta"
                        + "  </a>"
                        + "</p>"
                        + "<p style='color: #64748b; font-size: 0.85rem;'>"
                        + "  Si el botón no funciona, copia y pega esta URL en tu navegador:<br>"
                        + "  <a href='" + urlValidacion + "'>" + urlValidacion + "</a>"
                        + "</p>";

                // Enviar el correo electrónico mediante tu clase utilitaria de email
                Conexiones.enviarCorreo(email, "Activa tu cuenta en FernanPop", mensajeEmail);

                // Redirigir al registro mostrando el aviso de confirmación enviada (ya no loguea directamente)
                response.sendRedirect("registro.jsp?exito=1");
            } else {
                response.sendRedirect("registro.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("registro.jsp?error=1");
        }
    }
}
