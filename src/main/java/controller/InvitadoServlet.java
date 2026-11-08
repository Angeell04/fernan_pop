package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/InvitadoServlet")
public class InvitadoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // 1. Marcamos en la sesión que el usuario entra en modo invitado
        session.setAttribute("esInvitado", true);

        // 2. Limpiamos cualquier usuario logueado por seguridad
        session.removeAttribute("usuario");

        // 3. Redirigimos a la vista principal en la sección 'inicio'
        response.sendRedirect("index.jsp?seccion=inicio");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}