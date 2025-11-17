<?php
require_once 'conexion.php';
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Guía de Remisión - RED LIPA</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background: #f4f6f9; }
        h1 { color: #2c3e50; }
        .menu-container { display: flex; flex-wrap: wrap; gap: 20px; margin-top: 20px; }
        .card {
            background: #fff;
            border-radius: 8px;
            padding: 20px;
            min-width: 250px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .card h2 { margin-top: 0; font-size: 18px; }
        .card a {
            display: inline-block;
            margin-top: 8px;
            text-decoration: none;
            color: #fff;
            background: #3498db;
            padding: 6px 10px;
            border-radius: 4px;
            font-size: 14px;
        }
        .card a.secondary {
            background: #2ecc71;
        }
    </style>
</head>
<body>
    <h1>Sistema de gestión de guías de remisión</h1>
    <p>Aplicativo cliente para la base de datos <strong>guia_remision</strong> de RED LIPA ECOLÓGICA S.A.C.</p>

    <div class="menu-container">

    <div class="card">
    <h2>Traslados</h2>
    <p>Registro y seguimiento de los traslados asociados a las guías.</p>
    <a href="registrar_traslado.php">Registrar traslado</a>
    <a href="listar_traslados.php" class="secondary">Ver traslados</a>
    <a href="confirmar_entrega.php" class="secondary">Confirmar entrega de guía</a>
    </div>

        <div class="card">
            <h2>Destinatarios</h2>
            <p>Mantenimiento de clientes / destinatarios de las guías.</p>
            <a href="listar_destinatarios.php">Gestionar destinatarios</a>
        </div>
        <div class="card">
    <h2>Guías de remisión</h2>
    <p>Emisión y consulta de guías de remisión electrónicas.</p>
    <a href="emitir_guia.php">Emitir guía</a>
    <a href="listar_guias.php" class="secondary">Ver guías emitidas</a>
    </div>
        <div class="card">
            <h2>Traslados</h2>
            <p>Registro de vehículos, conductores y traslados asociados a guías.</p>
            <a href="registrar_traslado.php">Registrar traslado</a>
        </div>

        <div class="card">
            <h2>Reportes</h2>
            <p>Consultas analíticas sobre órdenes, guías, vehículos y clientes.</p>
            <a href="reportes.php" class="secondary">Ver reportes</a>
        </div>
    </div>
</body>
</html>
