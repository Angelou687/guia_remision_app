<?php
require_once 'conexion.php';
$pdo = obtenerConexion();
$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $ruc       = $_POST['ruc'] ?? '';
    $nombre    = $_POST['nombre'] ?? '';
    $telefono  = $_POST['telefono'] ?? '';
    $direccion = $_POST['direccion'] ?? '';
    $ubigeo    = $_POST['ubigeo'] ?? '';
    $gmail     = $_POST['gmail'] ?? '';

    try {
        $stmt = $pdo->prepare("CALL sp_insertar_destinatario(:ruc, :nombre, :tel, :dir, :ubi, :mail)");
        $stmt->execute([
            ':ruc'   => $ruc,
            ':nombre'=> $nombre,
            ':tel'   => $telefono,
            ':dir'   => $direccion,
            ':ubi'   => $ubigeo,
            ':mail'  => $gmail
        ]);
        $stmt->closeCursor();
        header('Location: listar.php');
        exit;
    } catch (PDOException $e) {
        $error = "Error al insertar: " . $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Insertar destinatario</title>
</head>
<body>
    <h1>Insertar nuevo destinatario</h1>
    <p><a href="listar.php">← Volver a la lista</a></p>

    <?php if (!empty($error)): ?>
        <p style="color:red;"><?= htmlspecialchars($error) ?></p>
    <?php endif; ?>

    <form method="post">
        <label>RUC:
            <input type="text" name="ruc" maxlength="11" required>
        </label><br><br>

        <label>Nombre:
            <input type="text" name="nombre" required>
        </label><br><br>

        <label>Teléfono:
            <input type="text" name="telefono">
        </label><br><br>

        <label>Dirección:
            <input type="text" name="direccion" required>
        </label><br><br>

        <label>Código Ubigeo:
            <input type="text" name="ubigeo" maxlength="6" required>
        </label><br><br>

        <label>Correo (gmail):
            <input type="email" name="gmail">
        </label><br><br>

        <button type="submit">Guardar</button>
    </form>
</body>
</html>
