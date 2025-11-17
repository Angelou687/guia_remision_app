<?php
require_once "conexion.php";
$mensaje = "";

if ($_POST) {
    try {
        $stmt = $pdo->prepare("CALL sp_generar_guia(?,?,?,?,?,?,?,?,?,?)");
        $stmt->execute([
            $_POST['codigo_guia'],
            $_POST['serie'],
            $_POST['numero'],
            $_POST['cod_orden'],
            $_POST['ruc_remitente'],
            $_POST['ruc_destinatario'],
            $_POST['origen'],
            $_POST['destino'],
            $_POST['motivo'],
            $_POST['bultos']
        ]);

        $mensaje = "Guía generada correctamente";
    } catch (Exception $e) {
        $mensaje = "Error: " . $e->getMessage();
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Generar Guía</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Emitir Guía de Remisión</h2>

    <?php if ($mensaje): ?><p><strong><?= $mensaje ?></strong></p><?php endif; ?>

    <form method="POST">

        <fieldset>
            <legend>Datos generales</legend>

            <label>Código Guía:</label>
            <input type="text" name="codigo_guia" required>

            <label>Serie:</label>
            <input type="text" name="serie" required>

            <label>Número:</label>
            <input type="text" name="numero" required>

            <label>Código Orden:</label>
            <input type="text" name="cod_orden" required>
        </fieldset>

        <fieldset>
            <legend>Remitente y destinatario</legend>

            <label>RUC Remitente:</label>
            <input type="text" name="ruc_remitente" required>

            <label>RUC Destinatario:</label>
            <input type="text" name="ruc_destinatario" required>
        </fieldset>

        <fieldset>
            <legend>Información del envío</legend>

            <label>Ubigeo Origen:</label>
            <input type="text" name="origen" required>

            <label>Ubigeo Destino:</label>
            <input type="text" name="destino" required>

            <label>Motivo:</label>
            <input type="text" name="motivo" required>

            <label>Número de bultos:</label>
            <input type="number" name="bultos" required>
        </fieldset>

        <input type="submit" value="Generar Guía">
    </form>

    <p><a href="index.php">Volver</a></p>
</div>
</body>
</html>
