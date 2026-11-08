package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Producto;
import models.Trato;
import models.Usuario;
import utils.Conexiones;

@WebServlet("/GestionOfertaServlet")
public class GestionOfertaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Control de Sesión: Comprobar autenticación
        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Obtener parámetros del formulario
        String idProductoParam = request.getParameter("idProducto");
        String accion = request.getParameter("accion");
        String precioParam = request.getParameter("precio");
        String emailComprador = request.getParameter("emailComprador");

        if (idProductoParam != null && accion != null) {
            try {
                long idProducto = Long.parseLong(idProductoParam);
                GestionAPP gestionAPP = new GestionAPP();

                // --- ACCIÓN: ACEPTAR OFERTA ---
                if ("aceptar".equalsIgnoreCase(accion)) {

                    if (emailComprador != null && precioParam != null && !precioParam.isEmpty()) {
                        double precio = Double.parseDouble(precioParam.replace(",", "."));

                        // Aceptar oferta (elimina o actualiza ofertas)
                        boolean ofertaAceptada = gestionAPP.aceptarOferta(idProducto);

                        if (ofertaAceptada) {
                            // Crear el Trato principal
                            int idTrato = gestionAPP.addTrato(usuarioLogueado, idProducto, emailComprador, precio);

                            if (idTrato > 0) {
                                Trato trato = gestionAPP.buscaTratoId(idTrato);

                                if (trato != null) {
                                    // Registrar Venta (Vendedor) y Compra (Comprador)
                                    gestionAPP.addTratoVenta(usuarioLogueado, trato.getId(), idProducto, emailComprador, precio);

                                    Usuario comprador = gestionAPP.buscaMail(emailComprador);
                                    if (comprador != null) {
                                        gestionAPP.addTratoCompra(comprador, trato);
                                        gestionAPP.addValoracionPendiente(comprador, idTrato);
                                        Producto producto = gestionAPP.buscaProductoId(idProducto);
                                        String tituloProducto = (producto != null) ? producto.getTitulo() : "Producto #" + idProducto;

// 2. Preparar el correo para el COMPRADOR
                                        // 2. Preparar el correo para el COMPRADOR
                                        String asuntoComprador = "¡Oferta aceptada! Compra confirmada en FernanPop";
                                        String mensajeComprador = "<html><head><style>"
                                                + "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; }"
                                                + "  .card { max-width: 550px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); overflow: hidden; font-size: 15px; color: #333333; }"
                                                + "  .header { background: linear-gradient(135deg, #10b981 0%, #059669 100%); padding: 24px; text-align: center; color: #ffffff; }"
                                                + "  .header h2 { margin: 0; font-size: 22px; font-weight: 600; }"
                                                + "  .content { padding: 28px 24px; line-height: 1.6; }"
                                                + "  .details-box { background-color: #f8fafc; border-left: 4px solid #10b981; border-radius: 6px; padding: 16px; margin: 20px 0; }"
                                                + "  .details-box p { margin: 6px 0; }"
                                                + "  .highlight { color: #059669; font-weight: 600; }"
                                                + "  .footer { background-color: #f8fafc; padding: 16px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #e2e8f0; }"
                                                + "</style></head><body>"
                                                + "<div class='card'>"
                                                + "  <div class='header'><h2>¡Enhorabuena! Tu oferta ha sido aceptada</h2></div>"
                                                + "  <div class='content'>"
                                                + "    <p>¡Buenas noticias! Se ha confirmado tu compra.</p>"
                                                + "    <div class='details-box'>"
                                                + "      <p><strong>Producto:</strong> " + tituloProducto + "</p>"
                                                + "      <p><strong>Precio acordado:</strong> <span class='highlight'>" + String.format("%.2f", precio) + " €</span></p>"
                                                + "    </div>"
                                                + "    <p>Ponte en contacto con el vendedor (<a href='mailto:" + usuarioLogueado.getMail() + "' style='color:#059669; text-decoration:none; font-weight:bold;'>" + usuarioLogueado.getMail() + "</a>) para acordar la entrega.</p>"
                                                + "  </div>"
                                                + "  <div class='footer'>Gracias por confiar en <strong>FernanPop</strong>.</div>"
                                                + "</div></body></html>";

// 3. Preparar el correo para el VENDEDOR
                                        String asuntoVendedor = "¡Has vendido un producto en FernanPop!";
                                        String mensajeVendedor = "<html><head><style>"
                                                + "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; }"
                                                + "  .card { max-width: 550px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); overflow: hidden; font-size: 15px; color: #333333; }"
                                                + "  .header { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); padding: 24px; text-align: center; color: #ffffff; }"
                                                + "  .header h2 { margin: 0; font-size: 22px; font-weight: 600; }"
                                                + "  .content { padding: 28px 24px; line-height: 1.6; }"
                                                + "  .details-box { background-color: #f8fafc; border-left: 4px solid #3b82f6; border-radius: 6px; padding: 16px; margin: 20px 0; }"
                                                + "  .details-box p { margin: 6px 0; }"
                                                + "  .highlight { color: #2563eb; font-weight: 600; }"
                                                + "  .footer { background-color: #f8fafc; padding: 16px; text-align: center; font-size: 13px; color: #64748b; border-top: 1px solid #e2e8f0; }"
                                                + "</style></head><body>"
                                                + "<div class='card'>"
                                                + "  <div class='header'><h2>¡Felicidades! Has aceptado una oferta</h2></div>"
                                                + "  <div class='content'>"
                                                + "    <p>¡Has cerrado una venta con éxito!</p>"
                                                + "    <div class='details-box'>"
                                                + "      <p><strong>Producto vendido:</strong> " + tituloProducto + "</p>"
                                                + "      <p><strong>Precio acordado:</strong> <span class='highlight'>" + String.format("%.2f", precio) + " €</span></p>"
                                                + "    </div>"
                                                + "    <p>Ponte en contacto con el comprador (<a href='mailto:" + emailComprador + "' style='color:#2563eb; text-decoration:none; font-weight:bold;'>" + emailComprador + "</a>) para coordinar la entrega y el pago.</p>"
                                                + "  </div>"
                                                + "  <div class='footer'>Gracias por confiar en <strong>FernanPop</strong>.</div>"
                                                + "</div></body></html>";
                                        Conexiones.enviarCorreo(emailComprador, asuntoComprador, mensajeComprador);
                                        Conexiones.enviarCorreo(usuarioLogueado.getMail(), asuntoVendedor, mensajeVendedor);

                                    }


                                    response.sendRedirect("ofertas.jsp?msg=aceptada");
                                    return;
                                }
                            }
                        }
                    }

                    // Si falla algún paso dentro del aceptar
                    response.sendRedirect("ofertas.jsp?msg=error");
                    return;

                    // --- ACCIÓN: RECHAZAR OFERTA ---
                } else if ("rechazar".equalsIgnoreCase(accion) && precioParam != null) {
                    float precioFloat = Float.parseFloat(precioParam.replace(",", "."));

                    if (gestionAPP.rechazarOferta(idProducto, usuarioLogueado.getId(), precioFloat)) {
                        response.sendRedirect("ofertas.jsp?msg=rechazada");
                        return;
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirección por defecto si los parámetros no eran válidos
        response.sendRedirect("ofertas.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir peticiones GET no permitidas de vuelta a ofertas
        response.sendRedirect("ofertas.jsp");
    }
}