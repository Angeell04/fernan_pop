package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GuardarResenaServlet")
public class GuardarResenaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTratoStr = request.getParameter("idTrato");
        String puntuacionStr = request.getParameter("puntuacion");
        String comentario = request.getParameter("comentario");

        if (idTratoStr != null && puntuacionStr != null && comentario != null) {
            try {
                int idTrato = Integer.parseInt(idTratoStr);
                int puntuacion = Integer.parseInt(puntuacionStr);

                GestionAPP gestionAPP = new GestionAPP();
                gestionAPP.valoraCompra(idTrato, comentario, puntuacion);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirige de nuevo al historial de tratos
        response.sendRedirect("tratos.jsp");
    }
}