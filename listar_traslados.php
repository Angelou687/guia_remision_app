<?php
require_once 'conexion.php';

$errores = [];
$traslados = [];

try {
    $stmt = $pdo->query("CALL sp_listar_traslados()");
    $traslados = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
} catch (PDOException $e) {
    $errores[] = $e->getMessage();
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registro de traslados</title>
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
<h1>Registro de traslados</h1>
<a href="index.php" class="btn">← Volver al menú</a>
<a href="registrar_traslado.php" class="btn" style="background:#2ecc71;">➕ Registrar nuevo traslado</a>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err): ?>
            <p>Error: <?= htmlspecialchars($err) ?></p>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<?php if ($traslados): ?>
    <table>
        <tr>
            <th>Cód. traslado</th>
            <th>Cód. guía</th>
            <th>Fecha emisión guía</th>
            <th>Placa</th>
            <th>Tipo vehículo</th>
            <th>Licencia</th>
            <th>Conductor</th>
            <th>Inicio</th>
            <th>Fin</th>
            <th>Estado</th>
            <th>Observaciones</th>
        </tr>
        <?php foreach ($traslados as $t): ?>
            <tr>
                <td><?= htmlspecialchars($t['codigo_traslado']) ?></td>
                <td><?= htmlspecialchars($t['codigo_guia']) ?></td>
                <td><?= htmlspecialchars($t['fecha_emision']) ?></td>
                <td><?= htmlspecialchars($t['placa']) ?></td>
                <td><?= htmlspecialchars($t['tipo_vehiculo']) ?></td>
                <td><?= htmlspecialchars($t['licencia']) ?></td>
                <td><?= htmlspecialchars($t['conductor']) ?></td>
                <td><?= htmlspecialchars($t['fecha_inicio']) ?></td>
                <td><?= htmlspecialchars($t['fecha_fin']) ?></td>
                <td><?= htmlspecialchars($t['estado_traslado']) ?></td>
                <td><?= htmlspecialchars($t['observaciones']) ?></td>
            </tr>
        <?php endforeach; ?>
    </table>
<?php else: ?>
    <p>No hay traslados registrados.</p>
<?php endif; ?>
</body>
</html>
