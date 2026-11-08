<%--
  Created by IntelliJ IDEA.
  User: miran
  Date: 10/09/2026
  Time: 19:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="error.jsp" %>
<%@ page import="models.Usuario" %>
<%@ page import="controller.GestionAPP" %>

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

    if (usuarioLogueado == null){
        response.sendRedirect("login.jsp");
    }

    String accion = request.getParameter("accion");
    if ("aceptar".equalsIgnoreCase(accion)){
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String movil = request.getParameter("movil");
        GestionAPP gestionAPP = new GestionAPP();

        if (!nombre.isEmpty()) gestionAPP.cambiaNombre(usuarioLogueado, nombre);
        if (!apellido.isEmpty()) gestionAPP.cambiaApellidos(usuarioLogueado, apellido);
        if (!movil.isEmpty()) gestionAPP.cambiaMovil(usuarioLogueado, Integer.parseInt(movil));
    }

%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Publicar</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>


    <h2>Ajustes del perfil</h2>
    <br>

    <div style="max-width: 600px;">
        <form action="" method="POST" class="app-form">
            <label>Nombre</label>
            <input type="text" name="nombre" value="<%= usuarioLogueado != null ? usuarioLogueado.getNombre() : "" %>" required>

            <label>Apellido</label>
            <input type="text" name="apellido" value="<%= usuarioLogueado != null ? usuarioLogueado.getApel() : "" %>" required>

            <label>Teléfono móvil</label>
            <input type="tel" name="movil" value="<%= usuarioLogueado != null ? usuarioLogueado.getMovil() : "" %>" required>

            <label>Clave</label>
            <input type="password" name="clave" value="******************" required>
            <input type="hidden" name="accion" value="aceptar">
            <button type="submit" class="btn-primary">Guardar Cambios</button>
        </form>

        <div style="margin-top: 30px;">
            <form action="LogoutServlet" method="POST">
                <button type="submit" style="background-color: var(--border); color: var(--text-main); border: none; padding: 12px; border-radius: 8px; cursor: pointer; width: 100%; font-weight: bold;">
                    Cerrar Sesión
                </button>
            </form>
        </div>
    </div>
</body>
