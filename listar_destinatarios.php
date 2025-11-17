<?php
require_once 'conexion.php';

try {
    $stmt = $pdo->query("CALL sp_listar_destinatarios()");
    $destinatarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
} catch (PDOException $e) {
    die("Error al listar destinatarios: " . $e->getMessage());
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Destinatarios</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4f6f9; margin: 30px; }
        h1 { color: #2c3e50; }
        a.btn { display:inline-block; padding:6px 10px; background:#3498db; color:#fff; text-decoration:none; border-radius:4px; font-size:14px; }
        table { border-collapse: collapse; width: 100%; background:#fff; margin-top:15px; }
        th, td { border:1px solid #ddd; padding:8px; font-size:14px; }
        th { background:#2c3e50; color:#fff; }
        tr:nth-child(even){ background:#f9f9f9; }
    </style>
</head>
<body>
    <h1>Destinatarios</h1>
    <a href="index.php" class="btn">← Volver al menú</a>
    <a href="crear_destinatario.php" class="btn" style="background:#2ecc71;">➕ Nuevo destinatario</a>

    <table>
        <tr>
            <th>RUC</th>
            <th>Nombre</th>
            <th>Teléfono</th>
            <th>Dirección</th>
            <th>Ubigeo</th>
            <th>Email</th>
            <th>Acciones</th>
        </tr>
        <?php foreach ($destinatarios as $d): ?>
            <tr>
                <td><?= htmlspecialchars($d['ruc']) ?></td>
                <td><?= htmlspecialchars($d['nombre']) ?></td>
                <td><?= htmlspecialchars($d['numero_telefono']) ?></td>
                <td><?= htmlspecialchars($d['calle_direccion']) ?></td>
                <td><?= htmlspecialchars($d['codigo_ubigeo']) ?></td>
                <td><?= htmlspecialchars($d['gmail']) ?></td>
                <td>
                    <a class="btn" style="background:#f39c12;"
                       href="editar_destinatario.php?ruc=<?= urlencode($d['ruc']) ?>">Editar</a>
                    <a class="btn" style="background:#e74c3c;"
                       href="eliminar_destinatario.php?ruc=<?= urlencode($d['ruc']) ?>"
                       onclick="return confirm('¿Seguro que deseas eliminar este destinatario?')">
                       Eliminar
                    </a>
                </td>
            </tr>
        <?php endforeach; ?>
    </table>
</body>
</html>
