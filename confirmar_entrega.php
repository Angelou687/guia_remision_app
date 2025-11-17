<?php
require_once "conexion.php";
$mensaje = "";

if ($_POST) {
    try {
        $stmt = $pdo->prepare("CALL sp_confirmar_entrega(?)");
        $stmt->execute([$_POST['codigo_guia']]);
        $mensaje = "Entrega confirmada correctamente";
    } catch (Exception $e) {
        $mensaje = "Error: " . $e->getMessage();
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmar Entrega</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Confirmar Entrega</h2>

    <?php if ($mensaje): ?><p><strong><?= $mensaje ?></strong></p><?php endif; ?>

    <form method="POST">

        <label>Código Guía:</label>
        <input type="text" name="codigo_guia" required>

        <input type="submit" value="Confirmar Entrega">
    </form>

    <p><a href="index.php">Volver</a></p>
</div>
</body>
</html>
