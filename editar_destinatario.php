<?php
require_once 'conexion.php';

$ruc = $_GET['ruc'] ?? null;
if (!$ruc) {
    die("RUC no especificado");
}

$errores = [];

try {
    $stmt = $pdo->prepare("SELECT * FROM destinatario WHERE ruc = ?");
    $stmt->execute([$ruc]);
    $destinatario = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$destinatario) {
        die("Destinatario no encontrado");
    }

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $nombre        = $_POST['nombre'] ?? '';
        $telefono      = $_POST['telefono'] ?? '';
        $direccion     = $_POST['direccion'] ?? '';
        $codigo_ubigeo = $_POST['codigo_ubigeo'] ?? '';
        $gmail         = $_POST['gmail'] ?? '';

        $stmt2 = $pdo->prepare("CALL sp_actualizar_destinatario(?,?,?,?,?,?)");
        $stmt2->execute([$ruc, $nombre, $telefono, $direccion, $codigo_ubigeo, $gmail]);
        $stmt2->closeCursor();

        header('Location: listar_destinatarios.php');
        exit;
    }
} catch (PDOException $e) {
    $errores[] = $e->getMessage();
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar destinatario</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        form { background:#fff; padding:20px; border-radius:8px; max-width:450px; }
        label { display:block; margin-top:10px; }
        input { width:100%; padding:6px; margin-top:4px; box-sizing:border-box; }
        button, a.btn { margin-top:15px; padding:8px 12px; border:none; border-radius:4px; text-decoration:none; }
        button { background:#3498db; color:#fff; }
        a.btn { background:#7f8c8d; color:#fff; }
        .error { color:#e74c3c; }
    </style>
</head>
<body>
<h1>Editar destinatario</h1>
<a href="listar_destinatarios.php" class="btn">← Volver</a>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err) echo "<p>Error: ".htmlspecialchars($err)."</p>"; ?>
    </div>
<?php endif; ?>

<form method="post">
    <p><strong>RUC:</strong> <?= htmlspecialchars($destinatario['ruc']) ?></p>

    <label>Nombre / Razón social:
        <input type="text" name="nombre" required
               value="<?= htmlspecialchars($destinatario['nombre']) ?>">
    </label>

    <label>Teléfono:
        <input type="text" name="telefono" pattern="\d*"
               value="<?= htmlspecialchars($destinatario['numero_telefono']) ?>">
    </label>

    <label>Dirección:
        <input type="text" name="direccion" required
               value="<?= htmlspecialchars($destinatario['calle_direccion']) ?>">
    </label>

    <label>Código Ubigeo:
        <input type="text" name="codigo_ubigeo" required pattern="\d{6}" maxlength="6"
               value="<?= htmlspecialchars($destinatario['codigo_ubigeo']) ?>">
    </label>

    <label>Correo electrónico:
        <input type="email" name="gmail"
               value="<?= htmlspecialchars($destinatario['gmail']) ?>">
    </label>

    <button type="submit">Actualizar</button>
</form>
</body>
</html>
