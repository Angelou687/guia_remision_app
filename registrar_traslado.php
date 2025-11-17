<?php
require_once 'conexion.php';

$errores = [];
$ok = false;

// Cargar combos: guías emitidas, vehículos, conductores
try {
    $guias = $pdo->query("
        SELECT codigo_guia
        FROM cabecera_guia
        WHERE estado_guia = 'emitida'
        ORDER BY fecha_emision DESC, codigo_guia
    ")->fetchAll(PDO::FETCH_ASSOC);

    $vehiculos = $pdo->query("
        SELECT placa, tipo_vehiculo, marca, modelo
        FROM vehiculo
        ORDER BY placa
    ")->fetchAll(PDO::FETCH_ASSOC);

    $conductores = $pdo->query("
        SELECT licencia, nombre
        FROM conductor
        ORDER BY nombre
    ")->fetchAll(PDO::FETCH_ASSOC);

} catch (PDOException $e) {
    die("Error cargando datos: " . $e->getMessage());
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $codigo_traslado = $_POST['codigo_traslado'] ?? '';
    $codigo_guia     = $_POST['codigo_guia'] ?? '';
    $placa           = $_POST['placa'] ?? '';
    $licencia        = $_POST['licencia'] ?? '';
    $fecha_inicio    = $_POST['fecha_inicio'] ?? '';
    $fecha_fin       = $_POST['fecha_fin'] ?? '';
    $estado          = $_POST['estado_traslado'] ?? 'en tránsito';
    $observaciones   = $_POST['observaciones'] ?? '';

    try {
        $stmt = $pdo->prepare("CALL sp_registrar_traslado(?,?,?,?,?,?,?,?)");
        $stmt->execute([
            $codigo_traslado,
            $codigo_guia,
            $placa,
            $licencia,
            $fecha_inicio,
            $fecha_fin,
            $estado,
            $observaciones
        ]);
        $stmt->closeCursor();
        $ok = true;
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar traslado</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        h1 { color:#2c3e50; }
        form { background:#fff; padding:20px; border-radius:8px; max-width:700px; }
        label { display:block; margin-top:8px; }
        input, select, textarea { width:100%; padding:6px; margin-top:3px; box-sizing:border-box; }
        .row { display:flex; gap:10px; }
        .row > div { flex:1; }
        button, a.btn { margin-top:15px; padding:8px 12px; border:none; border-radius:4px; text-decoration:none; }
        button { background:#3498db; color:#fff; }
        a.btn { background:#7f8c8d; color:#fff; }
        .ok { color:#27ae60; margin-top:10px; }
        .error { color:#e74c3c; margin-top:10px; }
    </style>
</head>
<body>
<h1>Registrar traslado</h1>
<a href="index.php" class="btn">← Volver al menú</a>

<?php if ($ok): ?>
    <p class="ok">Traslado registrado correctamente.</p>
<?php endif; ?>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err): ?>
            <p>Error: <?= htmlspecialchars($err) ?></p>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<form method="post">
    <div class="row">
        <div>
            <label>Código de traslado:
                <input type="text" name="codigo_traslado" required maxlength="15">
            </label>
        </div>
        <div>
            <label>Guía asociada:
                <select name="codigo_guia" required>
                    <option value="">-- Seleccione guía emitida --</option>
                    <?php foreach ($guias as $g): ?>
                        <option value="<?= htmlspecialchars($g['codigo_guia']) ?>">
                            <?= htmlspecialchars($g['codigo_guia']) ?>
                        </option>
                    <?php endforeach; ?>
                </select>
            </label>
        </div>
    </div>

    <div class="row">
        <div>
            <label>Vehículo (placa):
                <select name="placa" required>
                    <option value="">-- Seleccione vehículo --</option>
                    <?php foreach ($vehiculos as $v): ?>
                        <option value="<?= htmlspecialchars($v['placa']) ?>">
                            <?= htmlspecialchars($v['placa']) ?> - <?= htmlspecialchars($v['tipo_vehiculo']) ?>
                            (<?= htmlspecialchars($v['marca'].' '.$v['modelo']) ?>)
                        </option>
                    <?php endforeach; ?>
                </select>
            </label>
        </div>
        <div>
            <label>Conductor (licencia):
                <select name="licencia" required>
                    <option value="">-- Seleccione conductor --</option>
                    <?php foreach ($conductores as $c): ?>
                        <option value="<?= htmlspecialchars($c['licencia']) ?>">
                            <?= htmlspecialchars($c['nombre']) ?> (<?= htmlspecialchars($c['licencia']) ?>)
                        </option>
                    <?php endforeach; ?>
                </select>
            </label>
        </div>
    </div>

    <div class="row">
        <div>
            <label>Fecha y hora de inicio:
                <input type="datetime-local" name="fecha_inicio" required>
            </label>
        </div>
        <div>
            <label>Fecha y hora de fin (opcional):
                <input type="datetime-local" name="fecha_fin">
            </label>
        </div>
    </div>

    <label>Estado del traslado:
        <select name="estado_traslado">
            <option value="en tránsito">En tránsito</option>
            <option value="programado">Programado</option>
        </select>
    </label>

    <label>Observaciones:
        <textarea name="observaciones" rows="3"></textarea>
    </label>

    <button type="submit">Guardar traslado</button>
</form>
</body>
</html>
