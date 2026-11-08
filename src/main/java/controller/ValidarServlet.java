package controller;

import controller.GestionAPP;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ValidarServlet")
public class ValidarServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token != null && !token.trim().isEmpty()) {
            GestionAPP gestionAPP = new GestionAPP();

            // Método en tu lógica de negocio que busca el token, activa al usuario y borra/invalida el token
            boolean activado = gestionAPP.validarUsuarioPorToken(token);

            if (activado) {
                // Redirigir al login informando de que la cuenta ya está activa
                response.sendRedirect("login.jsp?cuentaValidada=true");
                return;
            }
        }

        // Si el token no existe, está caducado o falló la activación
        response.sendRedirect("login.jsp?errorToken=true");
    }
}