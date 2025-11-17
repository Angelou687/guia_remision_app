<?php
require_once 'conexion.php';

$reporte = $_POST['reporte'] ?? '';
$resultados = [];
$columnas = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && $reporte !== '') {
    try {
        switch ($reporte) {
            case 'detalle_orden':
                $codigo = $_POST['codigo_orden'] ?? 'ORD0001';
                $stmt = $pdo->prepare("CALL sp_reporte_detalle_orden(?)");
                $stmt->execute([$codigo]);
                break;

            case 'guias_fecha_estado':
                $stmt = $pdo->prepare("CALL sp_reporte_guias_por_fecha_estado()");
                $stmt->execute();
                break;

            case 'productos_mas_vendidos':
                $stmt = $pdo->prepare("CALL sp_reporte_productos_mas_vendidos()");
                $stmt->execute();
                break;

            case 'utilizacion_vehiculos':
                $stmt = $pdo->prepare("CALL sp_reporte_utilizacion_vehiculos()");
                $stmt->execute();
                break;

            case 'licencias_por_vencer':
                $stmt = $pdo->prepare("CALL sp_reporte_licencias_por_vencer(?)");
                $stmt->execute([180]);
                break;

            case 'guias_sin_traslado':
                $stmt = $pdo->prepare("CALL sp_reporte_guias_sin_traslado()");
                $stmt->execute();
                break;

            case 'top_clientes_guias':
                $stmt = $pdo->prepare("CALL sp_reporte_top_clientes_por_guias()");
                $stmt->execute();
                break;

            case 'bultos_90d':
                $stmt = $pdo->prepare("CALL sp_reporte_bultos_por_cliente_90d()");
                $stmt->execute();
                break;

            case 'kpi_guias':
                $stmt = $pdo->prepare("CALL sp_reporte_kpi_guias_diario()");
                $stmt->execute();
                break;

            case 'historial_traslados':
                $stmt = $pdo->prepare("CALL sp_reporte_historial_estados_traslado()");
                $stmt->execute();
                break;

            default:
                $stmt = null;
        }

        if (isset($stmt)) {
            $resultados = $stmt->fetchAll();
            if (!empty($resultados)) {
                $columnas = array_keys($resultados[0]);
            }
            // Consumir resultados restantes de procedimientos (por seguridad con MySQL)
            while ($stmt->nextRowset()) { /* nothing */ }
        }
    } catch (PDOException $e) {
        $error = "Error al ejecutar el reporte: " . $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reportes</title>
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
        select, input[type="text"] { padding:0.4rem; margin-right:0.5rem; }
        table { width:100%; border-collapse:collapse; margin-top:1rem; }
        th, td { border:1px solid #ddd; padding:0.5rem; text-align:left; }
        th { background:#ecf0f1; }
        .btn { padding:0.4rem 0.8rem; border:none; border-radius:4px; background:#3498db; color:#fff; cursor:pointer; }
        .btn-secondary { background:#7f8c8d; color:#fff; text-decoration:none; padding:0.4rem 0.8rem; border-radius:4px; }
        .error { background:#e74c3c; color:#fff; padding:0.5rem; border-radius:4px; margin-top:1rem; }
    </style>
</head>
<body>
<header>
    <h1>Reportes de la base de datos</h1>
</header>
<main>
    <a href="index.php" class="btn-secondary">← Volver al menú</a>

    <h2>Seleccionar reporte</h2>
    <form method="post">
        <select name="reporte" required>
            <option value="">-- Seleccione un reporte --</option>
            <option value="detalle_orden"        <?= $reporte==='detalle_orden'?'selected':'' ?>>1) Detalle de una orden</option>
            <option value="guias_fecha_estado"   <?= $reporte==='guias_fecha_estado'?'selected':'' ?>>2) Guías por fecha y estado</option>
            <option value="productos_mas_vendidos" <?= $reporte==='productos_mas_vendidos'?'selected':'' ?>>3) Productos más vendidos</option>
            <option value="utilizacion_vehiculos"  <?= $reporte==='utilizacion_vehiculos'?'selected':'' ?>>4) Utilización de vehículos</option>
            <option value="licencias_por_vencer"   <?= $reporte==='licencias_por_vencer'?'selected':'' ?>>5) Licencias por vencer (180 días)</option>
            <option value="guias_sin_traslado"     <?= $reporte==='guias_sin_traslado'?'selected':'' ?>>6) Guías sin traslado</option>
            <option value="top_clientes_guias"     <?= $reporte==='top_clientes_guias'?'selected':'' ?>>7) Top clientes por guías</option>
            <option value="bultos_90d"             <?= $reporte==='bultos_90d'?'selected':'' ?>>8) Bultos por cliente (90 días)</option>
            <option value="kpi_guias"              <?= $reporte==='kpi_guias'?'selected':'' ?>>9) KPI diario de guías</option>
            <option value="historial_traslados"    <?= $reporte==='historial_traslados'?'selected':'' ?>>10) Historial de estados de traslado</option>
        </select>

        <!-- Campo extra solo para detalle de orden -->
        <?php if ($reporte === 'detalle_orden'): ?>
            Código de orden:
            <input type="text" name="codigo_orden" value="<?= htmlspecialchars($_POST['codigo_orden'] ?? 'ORD0001') ?>">
        <?php endif; ?>

        <button type="submit" class="btn">Ejecutar</button>
    </form>

    <?php if (isset($error)): ?>
        <div class="error"><?= htmlspecialchars($error) ?></div>
    <?php endif; ?>

    <?php if (!empty($resultados)): ?>
        <h3>Resultado</h3>
        <table>
            <thead>
                <tr>
                    <?php foreach ($columnas as $col): ?>
                        <th><?= htmlspecialchars($col) ?></th>
                    <?php endforeach; ?>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($resultados as $fila): ?>
                    <tr>
                        <?php foreach ($columnas as $col): ?>
                            <td><?= htmlspecialchars($fila[$col]) ?></td>
                        <?php endforeach; ?>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php elseif ($_SERVER['REQUEST_METHOD'] === 'POST' && empty($error)): ?>
        <p>No se encontraron resultados para este reporte.</p>
    <?php endif; ?>
</main>
</body>
</html>
