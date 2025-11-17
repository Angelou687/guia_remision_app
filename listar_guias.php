<?php
require_once 'conexion.php';

$errores = [];
$guias = [];

try {
    $stmt = $pdo->query("CALL sp_listar_guias()");
    $guias = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
} catch (PDOException $e) {
    $errores[] = $e->getMessage();
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Guías de remisión emitidas</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        h1 { color:#2c3e50; }
        .btn { display:inline-block; padding:6px 10px; background:#3498db; color:#fff; text-decoration:none; border-radius:4px; font-size:14px; margin-bottom:10px; }
        table { border-collapse: collapse; width: 100%; background:#fff; margin-top:10px; }
        th, td { border:1px solid #ddd; padding:8px; font-size:13px; }
        th { background:#2c3e50; color:#fff; }
        tr:nth-child(even){ background:#f9f9f9; }
        .error { color:#e74c3c; }
    </style>
</head>
<body>
<h1>Guías de remisión emitidas</h1>
<a href="index.php" class="btn">← Volver al menú</a>
<a href="emitir_guia.php" class="btn" style="background:#2ecc71;">➕ Emitir nueva guía</a>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err): ?>
            <p>Error: <?= htmlspecialchars($err) ?></p>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<?php if ($guias): ?>
    <table>
        <tr>
            <th>Código guía</th>
            <th>Serie</th>
            <th>Número</th>
            <th>Orden</th>
            <th>Fecha emisión</th>
            <th>Estado</th>
            <th>Destinatario</th>
            <th>Dirección llegada</th>
        </tr>
        <?php foreach ($guias as $g): ?>
            <tr>
                <td><?= htmlspecialchars($g['codigo_guia']) ?></td>
                <td><?= htmlspecialchars($g['serie']) ?></td>
                <td><?= htmlspecialchars($g['numero']) ?></td>
                <td><?= htmlspecialchars($g['cod_orden']) ?></td>
                <td><?= htmlspecialchars($g['fecha_emision']) ?></td>
                <td><?= htmlspecialchars($g['estado_guia']) ?></td>
                <td><?= htmlspecialchars($g['destinatario'] ?? '') ?></td>
                <td><?= htmlspecialchars($g['direccion_llegada'] ?? '') ?></td>
            </tr>
        <?php endforeach; ?>
    </table>
<?php else: ?>
    <p>No hay guías emitidas registradas.</p>
<?php endif; ?>
</body>
</html>
