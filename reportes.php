<?php
require_once 'conexion.php';

$reporte = $_GET['rep'] ?? null;
$resultados = [];
$errores = [];
$titulo_reporte = "";
$columnas = [];
$requiere_form = false;
$tipo_form = ''; // 'orden', 'dias'

if ($reporte === '1') {
    $titulo_reporte = "Detalle de una orden con sus productos";
    $requiere_form = true;
    $tipo_form = 'orden';

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $codigo_orden = $_POST['codigo_orden'] ?? '';
        try {
            $stmt = $pdo->prepare("CALL sp_reporte_detalle_orden(:cod)");
            $stmt->bindParam(':cod', $codigo_orden);
            $stmt->execute();
            $resultados = $stmt->fetchAll();
            $stmt->closeCursor();
            $columnas = ['codigo_orden','codigo_producto','nombre_producto','cantidad','precio_unitario','subtotal'];
        } catch (PDOException $e) {
            $errores[] = $e->getMessage();
        }
    }
}
elseif ($reporte === '2') {
    $titulo_reporte = "Guías emitidas por fecha y estado";
    try {
        $stmt = $pdo->query("CALL sp_reporte_guias_por_fecha_estado()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['fecha_emision','estado_guia','guias'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '3') {
    $titulo_reporte = "Productos más vendidos por cantidad";
    try {
        $stmt = $pdo->query("CALL sp_reporte_productos_mas_vendidos()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['codigo_producto','nombre_producto','cantidad_vendida'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '4') {
    $titulo_reporte = "Utilización de vehículos (viajes por placa)";
    try {
        $stmt = $pdo->query("CALL sp_reporte_utilizacion_vehiculos()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['placa','marca','modelo','viajes'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '5') {
    $titulo_reporte = "Licencias por vencer en N días";
    $requiere_form = true;
    $tipo_form = 'dias';

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $dias = intval($_POST['dias'] ?? 180);
        try {
            $stmt = $pdo->prepare("CALL sp_reporte_licencias_por_vencer(?)");
            $stmt->execute([$dias]);
            $resultados = $stmt->fetchAll();
            $stmt->closeCursor();
            $columnas = ['licencia','nombre','telefono','fecha_vencimiento_licencia'];
        } catch (PDOException $e) {
            $errores[] = $e->getMessage();
        }
    }
}
elseif ($reporte === '6') {
    $titulo_reporte = "Guías sin traslado asignado";
    try {
        $stmt = $pdo->query("CALL sp_reporte_guias_sin_traslado()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['codigo_guia','fecha_emision','estado_guia'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '7') {
    $titulo_reporte = "Órdenes de pago por cliente";
    try {
        $stmt = $pdo->query("CALL sp_reporte_ordenes_por_cliente()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['ruc','nombre','total_ordenes'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '8') {
    $titulo_reporte = "Bultos entregados por cliente (últimos 90 días)";
    try {
        $stmt = $pdo->query("CALL sp_reporte_bultos_por_cliente_90d()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['cliente','bultos_90d'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '9') {
    $titulo_reporte = "KPI diario de estado de guías";
    try {
        $stmt = $pdo->query("CALL sp_reporte_kpi_guias_diario()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['fecha_emision','emitidas','en_transito','entregadas'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
elseif ($reporte === '10') {
    $titulo_reporte = "Ingresos por fecha (subtotal de órdenes)";
    try {
        $stmt = $pdo->query("CALL sp_reporte_ingresos_por_fecha()");
        $resultados = $stmt->fetchAll();
        $stmt->closeCursor();
        $columnas = ['fecha','total_dia'];
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reportes</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        h1 { color:#2c3e50; }
        .btn { display:inline-block; padding:6px 10px; background:#3498db; color:#fff; text-decoration:none; border-radius:4px; font-size:14px; margin:2px; }
        .btn-secondary { background:#7f8c8d; }
        table { border-collapse: collapse; width: 100%; background:#fff; margin-top:15px; }
        th, td { border:1px solid #ddd; padding:8px; font-size:13px; }
        th { background:#2c3e50; color:#fff; }
        tr:nth-child(even){ background:#f9f9f9; }
        .error { color:#e74c3c; }
        form.inline-form { margin-top:10px; background:#fff; padding:10px; border-radius:6px; display:inline-block; }
        label { margin-right:5px; }
    </style>
</head>
<body>
<h1>Reportes SQL</h1>
<a href="index.php" class="btn btn-secondary">← Volver al menú</a>

<h3>Seleccione un reporte:</h3>
<div>
    <a class="btn" href="reportes.php?rep=1">1) Detalle de orden</a>
    <a class="btn" href="reportes.php?rep=2">2) Guías por fecha y estado</a>
    <a class="btn" href="reportes.php?rep=3">3) Productos más vendidos</a>
    <a class="btn" href="reportes.php?rep=4">4) Uso de vehículos</a>
    <a class="btn" href="reportes.php?rep=5">5) Licencias por vencer</a>
    <a class="btn" href="reportes.php?rep=6">6) Guías sin traslado</a>
    <a class="btn" href="reportes.php?rep=7">7) Órdenes por cliente</a>
    <a class="btn" href="reportes.php?rep=8">8) Bultos por cliente (90d)</a>
    <a class="btn" href="reportes.php?rep=9">9) KPI guías diario</a>
    <a class="btn" href="reportes.php?rep=10">10) Ingresos por fecha</a>
</div>

<?php if ($reporte && $titulo_reporte): ?>
    <h2><?= htmlspecialchars($titulo_reporte) ?></h2>
<?php endif; ?>

<?php if ($requiere_form && $tipo_form === 'orden'): ?>
    <form method="post" class="inline-form">
        <label>Código de orden:
            <input type="text" name="codigo_orden" required>
        </label>
        <button type="submit" class="btn">Ver detalle</button>
    </form>
<?php endif; ?>

<?php if ($requiere_form && $tipo_form === 'dias'): ?>
    <form method="post" class="inline-form">
        <label>Días hasta vencimiento:
            <input type="number" name="dias" min="1" value="180">
        </label>
        <button type="submit" class="btn">Ver licencias</button>
    </form>
<?php endif; ?>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err) echo "<p>Error ejecutando reporte: ".htmlspecialchars($err)."</p>"; ?>
    </div>
<?php endif; ?>

<?php if ($resultados && $columnas): ?>
    <table>
        <tr>
            <?php foreach ($columnas as $col): ?>
                <th><?= htmlspecialchars($col) ?></th>
            <?php endforeach; ?>
        </tr>
        <?php foreach ($resultados as $fila): ?>
            <tr>
                <?php foreach ($columnas as $col): ?>
                    <td><?= htmlspecialchars($fila[$col] ?? '') ?></td>
                <?php endforeach; ?>
            </tr>
        <?php endforeach; ?>
    </table>
<?php elseif ($reporte && !$requiere_form && !$errores): ?>
    <p>No se encontraron datos para este reporte.</p>
<?php endif; ?>
</body>
</html>
