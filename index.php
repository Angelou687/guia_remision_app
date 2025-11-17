<?php require_once 'conexion.php'; ?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Guía de Remisión - RED LIPA</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background:#f4f6f9;
            margin:0;
            padding:0;
        }
        header {
            background:#2c3e50;
            color:#fff;
            padding:1rem 2rem;
        }
        h1 { margin:0; }
        main {
            max-width:900px;
            margin:2rem auto;
            background:#fff;
            padding:2rem;
            border-radius:8px;
            box-shadow:0 2px 8px rgba(0,0,0,0.1);
        }
        .menu {
            display:flex;
            flex-direction:column;
            gap:1rem;
        }
        a.btn {
            display:inline-block;
            padding:0.75rem 1rem;
            border-radius:6px;
            text-decoration:none;
            background:#3498db;
            color:#fff;
            text-align:center;
        }
        a.btn:hover { background:#2980b9; }
    </style>
</head>
<body>
<header>
    <h1>Guía de Remisión - RED LIPA ECOLÓGICA S.A.C.</h1>
</header>
<main>
    <h2>Menú principal</h2>
    <div class="menu">
        <a class="btn" href="listar.php">Gestión de destinatarios (CRUD)</a>
        <a class="btn" href="reportes.php">Reportes y consultas</a>
    </div>
</main>
</body>
</html>
