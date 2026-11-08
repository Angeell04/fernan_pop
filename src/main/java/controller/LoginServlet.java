package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Usuario;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String clave = request.getParameter("clave");

        GestionAPP gestionAPP = new GestionAPP();
        Usuario usuario = gestionAPP.login(email, clave);

        if (usuario != null) {
            HttpSession session = request.getSession();

            // 1. Guardar el usuario logueado
            session.setAttribute("usuario", usuario);

            // 2. Desactivar explícitamente el modo invitado
            session.setAttribute("esInvitado", false);
            // O bien: session.removeAttribute("esInvitado");

            // 3. Redirigir a través de index.jsp para que cargue la navbar y el contenedor
            response.sendRedirect("index.jsp?seccion=inicio");

        } else {
            response.sendRedirect("index.jsp?vistaLogin=login&error=1");
        }
    }
}
