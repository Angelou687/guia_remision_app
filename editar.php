<?php
require_once 'conexion.php';
$pdo = obtenerConexion();
$error = '';

if (!isset($_GET['ruc'])) {
    die("RUC no proporcionado");
}

$ruc = $_GET['ruc'];

// Obtener destinatario actual
$stmt = $pdo->prepare("CALL sp_obtener_destinatario(:ruc)");
$stmt->execute([':ruc' => $ruc]);
$destinatario = $stmt->fetch(PDO::FETCH_ASSOC);
$stmt->closeCursor();

if (!$destinatario) {
    die("Destinatario no encontrado");
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $nombre    = $_POST['nombre'] ?? '';
    $telefono  = $_POST['telefono'] ?? '';
    $direccion = $_POST['direccion'] ?? '';
    $ubigeo    = $_POST['ubigeo'] ?? '';
    $gmail     = $_POST['gmail'] ?? '';

    try {
        $stmtUpd = $pdo->prepare("
            CALL sp_actualizar_destinatario(
                :ruc, :nombre, :tel, :dir, :ubi, :mail
            )
        ");
        $stmtUpd->execute([
            ':ruc'    => $ruc,
            ':nombre' => $nombre,
            ':tel'    => $telefono,
            ':dir'    => $direccion,
            ':ubi'    => $ubigeo,
            ':mail'   => $gmail
        ]);
        $stmtUpd->closeCursor();
        header('Location: listar.php');
        exit;
    } catch (PDOException $e) {
        $error = "Error al actualizar: " . $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar destinatario</title>
</head>
<body>
    <h1>Editar destinatario</h1>
    <p><a href="listar.php">← Volver a la lista</a></p>

    <?php if (!empty($error)): ?>
        <p style="color:red;"><?= htmlspecialchars($error) ?></p>
    <?php endif; ?>

    <form method="post">
        <p><strong>RUC: <?= htmlspecialchars($destinatario['ruc']) ?></strong></p>

        <label>Nombre:
            <input type="text" name="nombre" value="<?= htmlspecialchars($destinatario['nombre']) ?>" required>
        </label><br><br>

        <label>Teléfono:
            <input type="text" name="telefono" value="<?= htmlspecialchars($destinatario['numero_telefono']) ?>">
        </label><br><br>

        <label>Dirección:
            <input type="text" name="direccion" value="<?= htmlspecialchars($destinatario['calle_direccion']) ?>" required>
        </label><br><br>

        <label>Código Ubigeo:
            <input type="text" name="ubigeo" value="<?= htmlspecialchars($destinatario['codigo_ubigeo']) ?>" maxlength="6" required>
        </label><br><br>

        <label>Correo (gmail):
            <input type="email" name="gmail" value="<?= htmlspecialchars($destinatario['gmail']) ?>">
        </label><br><br>

        <button type="submit">Actualizar</button>
    </form>
</body>
</html>
