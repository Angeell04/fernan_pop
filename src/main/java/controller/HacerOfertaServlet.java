package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Usuario;

@WebServlet("/HacerOfertaServlet")
public class HacerOfertaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario comprador = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (comprador == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));
            int idVendedor = Integer.parseInt(request.getParameter("idVendedor"));
            float precioOferta = Float.parseFloat(request.getParameter("precioOferta"));

            // Llamamos a la lógica de la app para registrar la oferta en la BD o lista
            GestionAPP gestionAPP = new GestionAPP();
            gestionAPP.hacerOferta(comprador.getId(), idVendedor, precioOferta, idProducto);

            // Redirigimos de vuelta confirmando el envío
            response.sendRedirect("producto.jsp?id=" + idProducto + "&ofertaEnviada=1");

        } catch (NumberFormatException e) {
            response.sendRedirect("inicio.jsp");
        }
    }
}
