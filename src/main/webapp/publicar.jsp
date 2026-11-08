<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="models.Usuario" %>
<%@ page import="controller.GestionAPP" %>
<%@ page import="models.Producto" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    if (usuarioLogueado == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Inicializamos como null en lugar de cadena vacía
    String mensaje = null;

    if ("POST".equalsIgnoreCase(request.getMethod())){
        try{
            GestionAPP gestionAPP = new GestionAPP();
            String name = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            float precio = Float.parseFloat(request.getParameter("precio"));
            String estado = request.getParameter("estado");

            Producto productoTmp = new Producto(descripcion, estado, gestionAPP.generaIdProducto(), precio, name, 0);

            if (gestionAPP.addProducto(usuarioLogueado, productoTmp)){
                response.sendRedirect("inicio.jsp");
                return;
            } else {
                mensaje = "No se pudo publicar el producto. Inténtalo de nuevo.";
            }
        } catch (Exception e){
            mensaje = "Ocurrió un error al procesar los datos introducidos.";
        }
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Publicar Producto</title>
    <!-- Agregamos versión al CSS para evitar problemas de caché -->
    <link rel="stylesheet" href="style.css?v=2.0">
</head>
<body>


<div class="main-container">
    <div class="auth-card">
        <h2>Publicar Producto</h2>

        <%-- Solo renderiza el div si existe un mensaje real --%>
        <% if (mensaje != null && !mensaje.isEmpty()) { %>
        <div class="alert-error">
            <%= mensaje %>
        </div>
        <% } %>

        <form action="" method="POST" class="app-form">
            <div class="form-group">
                <label for="titulo">Título</label>
                <input type="text" id="titulo" name="titulo" placeholder="Ej. Mandos PS5, Bicicleta..." required>
            </div>

            <div class="form-group">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" placeholder="Describe los detalles del producto..." rows="3" required></textarea>
            </div>

            <div class="form-group">
                <label for="precio">Precio (€)</label>
                <input type="number" id="precio" step="0.01" min="0" name="precio" placeholder="0.00" required>
            </div>

            <div class="form-group">
                <label for="estado">Estado del producto</label>
                <select id="estado" name="estado" required>
                    <option value="" disabled selected>Selecciona un estado</option>
                    <option value="Nuevo">Nuevo</option>
                    <option value="Como nuevo">Como nuevo</option>
                    <option value="Usado">Usado</option>
                </select>
            </div>

            <button type="submit" class="btn-primary">Publicar Producto</button>
        </form>
    </div>
</div>

</body>
</html>