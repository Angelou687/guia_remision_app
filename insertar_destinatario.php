<?php
require_once "conexion.php";
$mensaje = "";

if ($_POST) {
    $stmt = $pdo->prepare("CALL sp_insertar_destinatario(?,?,?,?,?,?)");
    $stmt->execute([
        $_POST['ruc'],
        $_POST['nombre'],
        $_POST['telefono'],
        $_POST['direccion'],
        $_POST['ubigeo'],
        $_POST['gmail']
    ]);
    $mensaje = "Destinatario registrado correctamente";
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nuevo Destinatario</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Registrar Destinatario</h2>

    <?php if ($mensaje): ?><p><strong><?= $mensaje ?></strong></p><?php endif; ?>

    <form method="POST">
        <label>RUC:</label>
        <input type="text" name="ruc" required>

        <label>Nombre:</label>
        <input type="text" name="nombre" required>

        <label>Teléfono:</label>
        <input type="text" name="telefono">

        <label>Dirección:</label>
        <input type="text" name="direccion">

        <label>Ubigeo:</label>
        <input type="text" name="ubigeo">

        <label>Correo:</label>
        <input type="text" name="gmail">

        <input type="submit" value="Guardar">
    </form>

    <p><a href="listar_destinatarios.php">Volver</a></p>
</div>
</body>
</html>
