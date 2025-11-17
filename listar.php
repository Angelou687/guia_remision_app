<?php
require_once 'conexion.php';

// Obtenemos solo activos (estado = 1)
$stmt = $pdo->query("
    SELECT ruc, nombre, numero_telefono, gmail
    FROM destinatario
    WHERE estado = 1
    ORDER BY nombre
");
$destinatarios = $stmt->fetchAll();
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Destinatarios - Listado</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:0; }
        header { background:#2c3e50; color:#fff; padding:1rem 2rem; }
        main {
            max-width:1000px;
            margin:2rem auto;
            background:#fff;
            padding:2rem;
            border-radius:8px;
            box-shadow:0 2px 8px rgba(0,0,0,0.1);
        }
        table { width:100%; border-collapse:collapse; margin-top:1rem; }
        th, td { border:1px solid #ddd; padding:0.5rem; text-align:left; }
        th { background:#ecf0f1; }
        a.btn { padding:0.35rem 0.6rem; border-radius:4px; text-decoration:none; font-size:0.9rem; }
        .btn-primary { background:#3498db; color:#fff; }
        .btn-warning { background:#f1c40f; color:#fff; }
        .btn-danger  { background:#e74c3c; color:#fff; }
        .btn-secondary { background:#7f8c8d; color:#fff; }
        .top-actions { display:flex; justify-content:space-between; align-items:center; }
    </style>
</head>
<body>
<header>
    <h1>Destinatarios</h1>
</header>
<main>
    <div class="top-actions">
        <a href="index.php" class="btn btn-secondary">← Volver al menú</a>
        <a href="insertar.php" class="btn btn-primary">+ Nuevo destinatario</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>RUC</th>
                <th>Nombre</th>
                <th>Teléfono</th>
                <th>Correo</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <?php if (count($destinatarios) === 0): ?>
            <tr><td colspan="5">No hay destinatarios registrados.</td></tr>
        <?php else: ?>
            <?php foreach ($destinatarios as $d): ?>
                <tr>
                    <td><?= htmlspecialchars($d['ruc']) ?></td>
                    <td><?= htmlspecialchars($d['nombre']) ?></td>
                    <td><?= htmlspecialchars($d['numero_telefono'] ?? '') ?></td>
                    <td><?= htmlspecialchars($d['gmail'] ?? '') ?></td>
                    <td>
                        <a class="btn btn-warning" href="editar.php?ruc=<?= urlencode($d['ruc']) ?>">Editar</a>
                        <a class="btn btn-danger" href="eliminar.php?ruc=<?= urlencode($d['ruc']) ?>"
                           onclick="return confirm('¿Seguro que deseas eliminar este destinatario?');">
                           Eliminar
                        </a>
                    </td>
                </tr>
            <?php endforeach; ?>
        <?php endif; ?>
        </tbody>
    </table>
</main>
</body>
</html>
