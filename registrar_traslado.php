<?php
require_once "conexion.php";
$mensaje = "";

if ($_POST) {
    try {
        $stmt = $pdo->prepare("CALL sp_registrar_traslado(?,?,?,?,?,?,?)");
        $stmt->execute([
            $_POST['codigo_traslado'],
            $_POST['codigo_guia'],
            $_POST['placa'],
            $_POST['licencia'],
            $_POST['inicio'],
            $_POST['fin'],
            $_POST['estado']
        ]);

        $mensaje = "Traslado registrado correctamente";
    } catch (Exception $e) {
        $mensaje = "Error: " . $e->getMessage();
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Traslado</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Registrar Traslado</h2>

    <?php if ($mensaje): ?><p><strong><?= $mensaje ?></strong></p><?php endif; ?>

    <form method="POST">

        <label>Código de Traslado:</label>
        <input type="text" name="codigo_traslado" required>

        <label>Código Guía:</label>
        <input type="text" name="codigo_guia" required>

        <label>Placa:</label>
        <input type="text" name="placa" required>

        <label>Licencia Conductor:</label>
        <input type="text" name="licencia" required>

        <label>Fecha/Hora Inicio:</label>
        <input type="datetime-local" name="inicio" required>

        <label>Fecha/Hora Fin:</label>
        <input type="datetime-local" name="fin" required>

        <label>Estado:</label>
        <select name="estado">
            <option value="en tránsito">En tránsito</option>
            <option value="entregado">Entregado</option>
        </select>

        <input type="submit" value="Registrar Traslado">
    </form>

    <p><a href="index.php">Volver</a></p>
</div>
</body>
</html>
