<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FernanPop - Error en la aplicación</title>
    <link rel="stylesheet" href="style.css?v=2.0">
    <style>
        .error-page-container {
            min-height: 70vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 2rem;
        }

        .error-card-wrapper {
            background-color: var(--bg-card);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 3rem 2rem;
            max-width: 500px;
            width: 100%;
            box-shadow: var(--shadow-md);
        }

        .error-icon {
            font-size: 3.5rem;
            margin-bottom: 1rem;
            line-height: 1;
        }

        .error-title {
            font-size: 1.8rem;
            font-weight: 800;
            color: var(--text-main);
            margin-bottom: 0.75rem;
        }

        .error-message {
            color: var(--text-muted);
            font-size: 1rem;
            line-height: 1.5;
            margin-bottom: 2rem;
        }

        .error-actions-btn {
            display: inline-block;
            width: 100%;
        }
    </style>
</head>
<body>

<div class="main-container">
    <div class="error-page-container">
        <div class="error-card-wrapper">
            <div class="error-icon">⚠️️</div>
            <h1 class="error-title">¡Ups! Algo salió mal</h1>
            <p class="error-message">
                Ha ocurrido un problema inesperado al procesar tu solicitud. No te preocupes, tus datos no se han perdido.
            </p>

            <div class="error-actions-btn">
                <a href="inicio.jsp" class="btn-primary">Volver al Inicio</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
