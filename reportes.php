<?php
require_once 'conexion.php';
$pdo = obtenerConexion();

$reporte = $_GET['rep'] ?? null;
$tituloReporte = '';
$resultados = [];

function ejecutarSP($pdo, $sql, $params = [])
{
    $stmt = $pdo->prepare($sql);
    $stmt->execute($params);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
    $stmt->closeCursor();
    return $rows;
}

if ($reporte === '1') {
    $tituloReporte = '1) Detalle de una orden con sus productos';
    // Podrías pedir el código_orden vía formulario; aquí dejo uno de ejemplo:
    $codigoOrden = 'ORD0001';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_detalle_orden(:cod)", [
        ':cod' => $codigoOrden
    ]);
}
elseif ($reporte === '2') {
    $tituloReporte = '2) Guías emitidas por fecha y estado';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_guias_por_fecha_estado()");
}
elseif ($reporte === '3') {
    $tituloReporte = '3) Productos más vendidos por cantidad';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_productos_mas_vendidos()");
}
elseif ($reporte === '4') {
    $tituloReporte = '4) Utilización de vehículos (viajes por placa)';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_utilizacion_vehiculos()");
}
elseif ($reporte === '5') {
    $tituloReporte = '5) Licencias por vencer en ≤ 180 días';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_licencias_por_vencer(:dias)", [
        ':dias' => 180
    ]);
}
elseif ($reporte === '6') {
    $tituloReporte = '6) Guías sin traslado asignado';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_guias_sin_traslado()");
}
elseif ($reporte === '7') {
    $tituloReporte = '7) Capacidad vs. carga por guía (alerta de sobrecarga)';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_capacidad_vs_carga()");
}
elseif ($reporte === '8') {
    $tituloReporte = '8) Bultos entregados por cliente (últimos 90 días)';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_bultos_por_cliente_90d()");
}
elseif ($reporte === '9') {
    $tituloReporte = '9) KPI diario de estado de guías';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_kpi_guias_diario()");
}
elseif ($reporte === '10') {
    $tituloReporte = '10) Clientes sin compras en los últimos 60 días';
    $resultados = ejecutarSP($pdo, "CALL sp_reporte_clientes_sin_compras_60d()");
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reportes</title>
</head>
<body>
    <h1>Reportes sobre la base de datos guia_remision</h1>
    <p><a href="index.php">← Volver al menú</a></p>

    <h2>Seleccione un reporte</h2>
    <ol>
        <li><a href="reportes.php?rep=1">Detalle de una orden con sus productos</a></li>
        <li><a href="reportes.php?rep=2">Guías emitidas por fecha y estado</a></li>
        <li><a href="reportes.php?rep=3">Productos más vendidos por cantidad</a></li>
        <li><a href="reportes.php?rep=4">Utilización de vehículos (viajes por placa)</a></li>
        <li><a href="reportes.php?rep=5">Licencias por vencer en ≤ 180 días</a></li>
        <li><a href="reportes.php?rep=6">Guías sin traslado asignado</a></li>
        <li><a href="reportes.php?rep=7">Capacidad vs. carga por guía</a></li>
        <li><a href="reportes.php?rep=8">Bultos entregados por cliente (últimos 90 días)</a></li>
        <li><a href="reportes.php?rep=9">KPI diario de estado de guías</a></li>
        <li><a href="reportes.php?rep=10">Clientes sin compras en los últimos 60 días</a></li>
    </ol>

    <?php if ($reporte && $tituloReporte): ?>
        <h2><?= htmlspecialchars($tituloReporte) ?></h2>

        <?php if (empty($resultados)): ?>
            <p>No se encontraron registros para este reporte.</p>
        <?php else: ?>
            <table border="1" cellpadding="5" cellspacing="0">
                <tr>
                    <?php foreach (array_keys($resultados[0]) as $col): ?>
                        <th><?= htmlspecialchars($col) ?></th>
                    <?php endforeach; ?>
                </tr>
                <?php foreach ($resultados as $fila): ?>
                    <tr>
                        <?php foreach ($fila as $valor): ?>
                            <td><?= htmlspecialchars($valor) ?></td>
                        <?php endforeach; ?>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php endif; ?>
    <?php endif; ?>
</body>
</html>
