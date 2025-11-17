<?php
require_once "conexion.php";
$mensaje = "";

$ruc = $_GET['ruc'];

$stmt = $pdo->query("SELECT * FROM destinatario WHERE ruc='$ruc'");
$data = $stmt->fetch(PDO::FETCH_ASSOC);

if ($_POST) {
    $pdo->prepare("CALL sp_editar_destinatario(?,?,?,?,?,?)")
        ->execute([
            $ruc,
            $_POST['nombre'],
            $_POST['telefono'],
            $_POST['direccion'],
            $_POST['ubigeo'],
            $_POST['gmail']
        ]);

    $mensaje = "Datos actualizados correctamente";
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Destinatario</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Editar Destinatario</h2>

    <?php if ($mensaje): ?><p><strong><?= $mensaje ?></strong></p><?php endif; ?>

    <form method="POST">
        <label>Nombre:</label>
        <input type="text" name="nombre" value="<?= $data['nombre'] ?>">

        <label>Teléfono:</label>
        <input type="text" name="telefono" value="<?= $data['numero_telefono'] ?>">

        <label>Dirección:</label>
        <input type="text" name="direccion" value="<?= $data['calle_direccion'] ?>">

        <label>Ubigeo:</label>
        <input type="text" name="ubigeo" value="<?= $data['codigo_ubigeo'] ?>">

        <label>Correo:</label>
        <input type="text" name="gmail" value="<?= $data['gmail'] ?>">

        <input type="submit" value="Guardar cambios">
    </form>

    <p><a href="listar_destinatarios.php">Volver</a></p>
</div>
</body>
</html>
