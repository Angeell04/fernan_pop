<%@ page import="models.Usuario" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Trato" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="controller.GestionAPP" %>
<%@ page import="utils.Utils" %>
<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.Trato" %>
<%@ page import="models.Usuario" %>
<%@ page import="controller.GestionAPP" %>
<%@ page import="utils.Utils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>

<%
    GestionAPP gestionAPP = new GestionAPP();
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

    if (usuarioLogueado == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Trato> compras = gestionAPP.historialCompra(usuarioLogueado);
    List<Trato> ventas = gestionAPP.historialVentas(usuarioLogueado);

    // Clasificación de compras según tengan o no valoración/puntuación
    List<Trato> comprasValoradas = new ArrayList<>();
    List<Trato> comprasPendientes = new ArrayList<>();

    if (compras != null) {
        for (Trato t : compras) {
            if (t.getPuntuacion() > 0 || (t.getComentario() != null && !t.getComentario().isBlank())) {
                comprasValoradas.add(t);
            } else {
                comprasPendientes.add(t);
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Historial de Tratos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>

<main class="tratos-main">
    <header class="section-header" style="margin-bottom: 25px;">
        <h2>Historial de Tratos</h2>
        <p style="color: var(--text-muted); margin-top: 6px;">
            Aquí se muestran las transacciones cerradas de tus compras y ventas.
        </p>
    </header>

    <% if ((compras == null || compras.isEmpty()) && (ventas == null || ventas.isEmpty())) { %>

    <div class="offer-empty">
        <p>🤝 Aún no has completado ningún trato.</p>
    </div>

    <% } else { %>

    <div class="tratos-container">

        <!-- 1. SECCIÓN COMPRAS PENDIENTES DE VALORAR -->
        <section class="tratos-section">
            <h3 class="tratos-section-title">
                ⏳ Compras Pendientes de Valorar (<%= comprasPendientes.size() %>)
            </h3>

            <% if (comprasPendientes.isEmpty()) { %>
            <p style="color: var(--text-muted);">No tienes compras pendientes de valorar.</p>
            <% } else { %>
            <div class="tratos-grid">
                <% for (Trato trato : comprasPendientes) {
                    Usuario vendedor = gestionAPP.buscaMail(trato.getEmailOtroUser());
                    String nombreVendedor = (vendedor != null) ? vendedor.getNombre() : trato.getEmailOtroUser();
                %>
                <article class="trato-card" style="border-color: rgba(255, 193, 7, 0.4);">
                    <h4>
                        <%= (trato.getProducto() != null) ? trato.getProducto().getTitulo() : "Producto no disponible" %>
                    </h4>

                    <ul class="trato-details">
                        <li><strong>Vendedor:</strong> <%= nombreVendedor %></li>
                        <li><strong>Precio Final:</strong> <span class="trato-price"><%= trato.getPrecio() %> €</span></li>
                        <li><strong>Fecha:</strong> <%= (trato.getFecha() != null) ? Utils.calendarToString(trato.getFecha()) : "N/D" %></li>
                    </ul>

                    <!-- Formulario de valoración rápida -->
                    <form action="GuardarResenaServlet" method="POST" class="form-resena" style="margin-top: 15px; border-top: 1px solid var(--border); padding-top: 12px;">
                        <input type="hidden" name="idTrato" value="<%= trato.getId() %>">

                        <div style="margin-bottom: 8px;">
                            <label style="font-size: 0.85rem; color: var(--text-muted); display: block; margin-bottom: 4px;">Puntuación:</label>
                            <select name="puntuacion" required style="width: 100%; padding: 6px; border-radius: 6px; background: var(--bg-card); color: var(--text-main); border: 1px solid var(--border);">
                                <option value="5">(5/5)</option>
                                <option value="4">(4/5)</option>
                                <option value="3">(3/5)</option>
                                <option value="2">(2/5)</option>
                                <option value="1">(1/5)</option>
                            </select>
                        </div>

                        <div style="margin-bottom: 10px;">
                            <label style="font-size: 0.85rem; color: var(--text-muted); display: block; margin-bottom: 4px;">Comentario:</label>
                            <textarea name="comentario" rows="2" placeholder="¿Qué tal la experiencia con el vendedor?" required style="width: 100%; padding: 6px; border-radius: 6px; background: var(--bg-card); color: var(--text-main); border: 1px solid var(--border); resize: none;"></textarea>
                        </div>

                        <button type="submit" class="btn-accept" style="width: 100%; text-align: center;">Enviar Valoración</button>
                    </form>
                </article>
                <% } %>
            </div>
            <% } %>
        </section>

        <!-- 2. SECCIÓN COMPRAS VALORADAS -->
        <section class="tratos-section">
            <h3 class="tratos-section-title">
                🛒 Mis Compras Valoradas (<%= comprasValoradas.size() %>)
            </h3>

            <% if (comprasValoradas.isEmpty()) { %>
            <p style="color: var(--text-muted);">No has valorado ninguna compra aún.</p>
            <% } else { %>
            <div class="tratos-grid">
                <% for (Trato trato : comprasValoradas) {
                    Usuario vendedor = gestionAPP.buscaMail(trato.getEmailOtroUser());
                    String nombreVendedor = (vendedor != null) ? vendedor.getNombre() : trato.getEmailOtroUser();
                %>
                <article class="trato-card">
                    <h4>
                        <%= (trato.getProducto() != null) ? trato.getProducto().getTitulo() : "Producto no disponible" %>
                    </h4>

                    <ul class="trato-details">
                        <li><strong>Vendedor:</strong> <%= nombreVendedor %></li>
                        <li><strong>Precio Final:</strong> <span class="trato-price"><%= trato.getPrecio() %> €</span></li>
                        <li><strong>Fecha:</strong> <%= (trato.getFecha() != null) ? Utils.calendarToString(trato.getFecha()) : "N/D" %></li>
                        <li><strong>Puntuación:</strong> <%= trato.getPuntuacion() %> / 5 ⭐</li>
                        <% if (trato.getComentario() != null && !trato.getComentario().isBlank()) { %>
                        <li class="trato-comment">
                            "<%= trato.getComentario() %>"
                        </li>
                        <% } %>
                    </ul>
                </article>
                <% } %>
            </div>
            <% } %>
        </section>

        <!-- 3. SECCIÓN VENTAS -->
        <section class="tratos-section">
            <h3 class="tratos-section-title">
                🏷️ Mis Ventas (<%= (ventas != null) ? ventas.size() : 0 %>)
            </h3>

            <% if (ventas == null || ventas.isEmpty()) { %>
            <p style="color: var(--text-muted);">No has vendido ningún producto todavía.</p>
            <% } else { %>
            <div class="tratos-grid">
                <% for (Trato trato : ventas) {
                    Usuario comprador = gestionAPP.buscaMail(trato.getEmailOtroUser());
                    String nombreComprador = (comprador != null) ? comprador.getNombre() : trato.getEmailOtroUser();
                %>
                <article class="trato-card">
                    <h4>
                        <%= (trato.getProducto() != null) ? trato.getProducto().getTitulo() : "Producto no disponible" %>
                    </h4>

                    <ul class="trato-details">
                        <li><strong>Comprador:</strong> <%= nombreComprador %></li>
                        <li><strong>Precio Final:</strong> <span class="trato-price"><%= trato.getPrecio() %> €</span></li>
                        <li><strong>Fecha:</strong> <%= (trato.getFecha() != null) ? Utils.calendarToString(trato.getFecha()) : "N/D" %></li>
                        <li><strong>Puntuación recibida:</strong> <%= trato.getPuntuacion() %> / 5 ⭐</li>
                        <% if (trato.getComentario() != null && !trato.getComentario().isBlank()) { %>
                        <li class="trato-comment">
                            "<%= trato.getComentario() %>"
                        </li>
                        <% } %>
                    </ul>
                </article>
                <% } %>
            </div>
            <% } %>
        </section>

    </div>

    <% } %>
</main>
</body>
</html>