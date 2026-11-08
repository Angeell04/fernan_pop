<%@ page import="models.*" %>
<%@ page import="controller.GestionAPP" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>

<%
  GestionAPP gestionAPP = new GestionAPP();
  Usuario usuarioLogin = (Usuario) session.getAttribute("usuario");

  if (usuarioLogin == null){
    response.sendRedirect("login.jsp");
    return;
  }

  ArrayList<Oferta> ofertasUser = gestionAPP.showAllOfertByUser(usuarioLogin);
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>FernanPop - Ofertas</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>


<div class="main-container">
  <h2>Gestión de Ofertas</h2>
  <p style="color: var(--text-muted); margin-top: 6px;">
    Consulta las ofertas de compra que has enviado o recibido por tus productos.
  </p>

  <% if (ofertasUser == null || ofertasUser.isEmpty()) { %>
  <div class="offer-empty">
    <p>🤝 No tienes ofertas pendientes en este momento.</p>
  </div>
  <% } else { %>
  <div class="offers-list">
    <% for (Oferta oferta : ofertasUser) { %>
    <div class="offer-card">
      <div class="offer-info">
        <p class="offer-user">
          Oferta de: <span><%= oferta.getComprador().getNombre() %></span>
        </p>
        <h3 class="offer-title"><%= oferta.getProducto().getTitulo() %></h3>
      </div>

      <div class="offer-price-tag">
        <span class="amount"><%= oferta.getPrecio() %> €</span>
      </div>

      <!-- Botones preparados para Aceptar/Rechazar cuando los conectemos -->
      <div class="offer-actions">
        <form action="GestionOfertaServlet" method="POST" style="display:inline;">
          <input type="hidden" name="idProducto" value="<%= oferta.getProducto().getId() %>">
          <input type="hidden" name="emailComprador" value="<%= oferta.getComprador().getMail() %>">
          <input type="hidden" name="precio" value="<%= oferta.getPrecio() %>">
          <input type="hidden" name="accion" value="aceptar">
          <button type="submit" class="btn-accept">Aceptar</button>
        </form>
        <form action="GestionOfertaServlet" method="POST" style="display:inline;">
          <input type="hidden" name="idProducto" value="<%= oferta.getProducto().getId() %>">
          <input type="hidden" name="precio" value="<%= oferta.getPrecio() %>">
          <input type="hidden" name="accion" value="rechazar">
          <button type="submit" class="btn-reject">Rechazar</button>
        </form>
      </div>
    </div>
    <% } %>
  </div>
  <% } %>
</div>

</body>
</html>
