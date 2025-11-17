<?php
require_once "conexion.php";
$resultado = [];
$reporte_nombre = "";
$mensaje = "";

if ($_POST) {
    $opc = $_POST["reporte"];

    try {
        $stmt = $pdo->query("CALL $opc()");
        $resultado = $stmt->fetchAll(PDO::FETCH_ASSOC);
        $reporte_nombre = $opc;
    } catch (Exception $e) {
        $mensaje = "Error ejecutando reporte: " . $e->getMessage();
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reportes</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Reportes de Base de Datos</h2>

    <form method="POST">
        <label>Seleccione un reporte:</label>
        <select name="reporte">
            <option value="sp_reporte_detalle_orden">Detalle de orden</option>
            <option value="sp_reporte_guias_por_fecha_estado">Guías por fecha y estado</option>
            <option value="sp_reporte_productos_mas_vendidos">Productos más vendidos</option>
            <option value="sp_reporte_utilizacion_vehiculos">Uso de vehículos</option>
            <option value="sp_reporte_licencias_por_vencer">Licencias por vencer</option>
            <option value="sp_reporte_guias_sin_traslado">Guías sin traslado</option>
            <option value="sp_reporte_bultos_por_cliente_90d">Bultos por cliente (90 días)</option>
            <option value="sp_reporte_kpi_guias_diario">KPI diario</option>
            <option value="sp_reporte_ordenes_por_dia">Órdenes por día</option>
            <option value="sp_reporte_entregas_tiempo_real">Entregas tiempo real</option>
        </select>

        <input type="submit" value="Ver reporte">
    </form>

    <?php if ($resultado): ?>
        <h3>Resultado del reporte</h3>
        <table>
            <tr>
                <?php foreach (array_keys($resultado[0]) as $col): ?>
                    <th><?= $col ?></th>
                <?php endforeach; ?>
            </tr>

            <?php foreach ($resultado as $fila): ?>
            <tr>
                <?php foreach ($fila as $col): ?>
                    <td><?= $col ?></td>
                <?php endforeach; ?>
            </tr>
            <?php endforeach; ?>

        </table>
    <?php endif; ?>

    <?php if ($mensaje): ?>
        <p><strong><?= $mensaje ?></strong></p>
    <?php endif; ?>

    <p><a href="index.php">Volver</a></p>
</div>
</body>
</html>
