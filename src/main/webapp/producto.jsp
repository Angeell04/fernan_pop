<%@ page import="models.Usuario" %>
<%@ page import="models.Producto" %>
<%@ page import="controller.GestionAPP" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>

<%
    GestionAPP gestionAPP = new GestionAPP();
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

    if (usuarioLogueado == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String idParam = request.getParameter("id");
    Producto producto = null;
    long id = 0;

    if (idParam != null && !idParam.isEmpty()) {
        try {
            id = Long.parseLong(idParam);
            producto = gestionAPP.buscaProductoId(id);
        } catch (NumberFormatException e) {
            producto = null;
        }
    }

    // Redirección corregida a inicio.jsp
    if (producto == null) {
        response.sendRedirect("inicio.jsp");
        return;
    }

    // Guardamos el ID del vendedor para no hacer la consulta varias veces
    long idVendedor = gestionAPP.buscaIdUsuarioProducto(id);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - <%= producto.getTitulo() %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>


<div class="main-container">
    <a href="inicio.jsp" class="btn-back">← Volver al inicio</a>

    <div class="product-detail-card">

        <div class="product-detail-header">
            <span class="badge"><%= producto.getEstado() != null ? producto.getEstado() : "En venta" %></span>
        </div>

        <div class="product-detail-info">
            <h1><%= producto.getTitulo() %></h1>
            <div class="detail-price"><%= producto.getPrecio() %> €</div>

            <hr class="divider">

            <div class="description-section">
                <h3>Descripción del producto</h3>
                <p class="detail-description"><%= producto.getDescripcion() %></p>
            </div>
        </div>

        <div class="action-buttons">
            <% if (idVendedor != usuarioLogueado.getId()) { %>
                <div class="offer-box">
                    <label>¿Cuánto quieres ofrecer por este producto?</label>
                    <form action="HacerOfertaServlet" method="POST">
                        <input type="hidden" name="idProducto" value="<%= producto.getId() %>">
                        <input type="hidden" name="idVendedor" value="<%= idVendedor %>">

                        <div class="offer-form-group">
                            <input type="number" step="0.01" name="precioOferta"
                                   value="<%= producto.getPrecio() %>"
                                   min="1" required>
                            <button type="submit" class="btn-primary">Enviar Oferta</button>
                        </div>
                    </form>

                    <% if (request.getParameter("ofertaEnviada") != null) { %>
                    <div class="alert-success">
                        ¡Oferta enviada con éxito al vendedor!
                    </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="alert-owner">
                    📦 Este producto lo has publicado tú.
                </div>
            <% } %>
        </div>

    </div>
</div>

</body>
</html>
