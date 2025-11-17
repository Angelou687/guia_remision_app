<?php
require_once 'conexion.php';

$errores = [];
$ok = false;

// Cargar combos: órdenes, remitentes, destinatarios, ubigeos
try {
    $ordenes = $pdo->query("
        SELECT codigo_orden, ruc_cliente
        FROM orden_de_pago
        ORDER BY codigo_orden
    ")->fetchAll();

    $remitentes = $pdo->query("
        SELECT ruc, nombre_empresa
        FROM remitente
        ORDER BY nombre_empresa
    ")->fetchAll();

    $destinatarios = $pdo->query("
        SELECT ruc, nombre
        FROM destinatario
        ORDER BY nombre
    ")->fetchAll();

    $ubigeos = $pdo->query("
        SELECT codigo_ubigeo,
               CONCAT(departamento, ' - ', provincia, ' - ', distrito) AS descripcion
        FROM ubigeo
        ORDER BY departamento, provincia, distrito
    ")->fetchAll();

} catch (PDOException $e) {
    die("Error cargando datos: " . $e->getMessage());
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $codigo_guia      = $_POST['codigo_guia'] ?? '';
    $serie            = $_POST['serie'] ?? '';
    $numero           = $_POST['numero'] ?? '';
    $cod_orden        = $_POST['cod_orden'] ?? '';
    $ruc_remitente    = $_POST['ruc_remitente'] ?? '';
    $ruc_destinatario = $_POST['ruc_destinatario'] ?? '';
    $dir_partida      = $_POST['dir_partida'] ?? '';
    $dir_llegada      = $_POST['dir_llegada'] ?? '';
    $ubigeo_origen    = $_POST['ubigeo_origen'] ?? '';
    $ubigeo_destino   = $_POST['ubigeo_destino'] ?? '';
    $motivo           = $_POST['motivo'] ?? '';
    $modalidad        = $_POST['modalidad'] ?? '';
    $peso_total       = $_POST['peso_total'] ?? '';
    $numero_bultos    = $_POST['numero_bultos'] ?? '';

    try {
        $stmt = $pdo->prepare("
            CALL sp_emitir_guia(?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        ");

        $stmt->execute([
            $codigo_guia,
            $serie,
            $numero,
            $cod_orden,
            $ruc_remitente,
            $ruc_destinatario,
            $dir_partida,
            $dir_llegada,
            $ubigeo_origen,
            $ubigeo_destino,
            $motivo,
            $modalidad,
            $peso_total,
            $numero_bultos
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
    <title>Emitir guía de remisión</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        h1 { color:#2c3e50; }
        form { background:#fff; padding:20px; border-radius:8px; max-width:700px; }
        fieldset { border:1px solid #ccc; margin-top:10px; padding:10px; }
        legend { font-weight:bold; }
        label { display:block; margin-top:8px; }
        input, select, textarea { width:100%; padding:6px; margin-top:3px; box-sizing:border-box; }
        .row { display:flex; gap:10px; }
        .row > div { flex:1; }
        button, a.btn { margin-top:15px; padding:8px 12px; border:none; border-radius:4px; text-decoration:none; }
        button { background:#2ecc71; color:#fff; }
        a.btn { background:#7f8c8d; color:#fff; }
        .ok { color:#27ae60; margin-top:10px; }
        .error { color:#e74c3c; margin-top:10px; }
    </style>
</head>
<body>
<h1>Emitir guía de remisión</h1>
<a href="index.php" class="btn">← Volver al menú</a>

<?php if ($ok): ?>
    <p class="ok">La guía de remisión se emitió correctamente.</p>
<?php endif; ?>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err): ?>
            <p>Error: <?= htmlspecialchars($err) ?></p>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<form method="post">
    <fieldset>
        <legend>Datos de la guía</legend>

        <div class="row">
            <div>
                <label>Código de guía:
                    <input type="text" name="codigo_guia" required maxlength="15">
                </label>
            </div>
            <div>
                <label>Serie:
                    <input type="text" name="serie" required maxlength="10" value="A001">
                </label>
            </div>
            <div>
                <label>Número:
                    <input type="text" name="numero" required maxlength="10">
                </label>
            </div>
        </div>

        <div class="row">
            <div>
                <label>Orden de pago:
                    <select name="cod_orden" required>
                        <option value="">-- Seleccione orden --</option>
                        <?php foreach ($ordenes as $o): ?>
                            <option value="<?= htmlspecialchars($o['codigo_orden']) ?>">
                                <?= htmlspecialchars($o['codigo_orden']) ?> (RUC cliente: <?= htmlspecialchars($o['ruc_cliente']) ?>)
                            </option>
                        <?php endforeach; ?>
                    </select>
                </label>
            </div>

            <div>
                <label>Remitente:
                    <select name="ruc_remitente" required>
                        <?php foreach ($remitentes as $r): ?>
                            <option value="<?= htmlspecialchars($r['ruc']) ?>">
                                <?= htmlspecialchars($r['nombre_empresa']) ?> (<?= htmlspecialchars($r['ruc']) ?>)
                            </option>
                        <?php endforeach; ?>
                    </select>
                </label>
            </div>
        </div>
    </fieldset>

    <fieldset>
        <legend>Datos del destinatario y direcciones</legend>

        <label>Destinatario:
            <select name="ruc_destinatario" required>
                <option value="">-- Seleccione destinatario --</option>
                <?php foreach ($destinatarios as $d): ?>
                    <option value="<?= htmlspecialchars($d['ruc']) ?>">
                        <?= htmlspecialchars($d['nombre']) ?> (<?= htmlspecialchars($d['ruc']) ?>)
                    </option>
                <?php endforeach; ?>
            </select>
        </label>

        <label>Dirección de partida:
            <input type="text" name="dir_partida" required maxlength="120">
        </label>

        <label>Dirección de llegada:
            <input type="text" name="dir_llegada" required maxlength="120">
        </label>

        <div class="row">
            <div>
                <label>Ubigeo origen:
                    <select name="ubigeo_origen" required>
                        <option value="">-- Seleccione origen --</option>
                        <?php foreach ($ubigeos as $u): ?>
                            <option value="<?= htmlspecialchars($u['codigo_ubigeo']) ?>">
                                <?= htmlspecialchars($u['codigo_ubigeo']) ?> - <?= htmlspecialchars($u['descripcion']) ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </label>
            </div>

            <div>
                <label>Ubigeo destino:
                    <select name="ubigeo_destino" required>
                        <option value="">-- Seleccione destino --</option>
                        <?php foreach ($ubigeos as $u): ?>
                            <option value="<?= htmlspecialchars($u['codigo_ubigeo']) ?>">
                                <?= htmlspecialchars($u['codigo_ubigeo']) ?> - <?= htmlspecialchars($u['descripcion']) ?>
                            </option>
                        <?php endforeach; ?>
                    </select>
                </label>
            </div>
        </div>
    </fieldset>

    <fieldset>
        <legend>Datos del traslado</legend>

        <label>Motivo de traslado:
            <input type="text" name="motivo" required maxlength="100">
        </label>

        <label>Modalidad de transporte:
            <input type="text" name="modalidad" required maxlength="50" value="terrestre">
        </label>

        <div class="row">
            <div>
                <label>Peso total (kg):
                    <input type="number" name="peso_total" required min="0" step="0.01">
                </label>
            </div>
            <div>
                <label>Número de bultos:
                    <input type="number" name="numero_bultos" required min="0" step="1">
                </label>
            </div>
        </div>
    </fieldset>

    <button type="submit">Emitir guía</button>
</form>
</body>
</html>
