<?php
require_once 'conexion.php';

$errores = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $ruc           = $_POST['ruc'] ?? '';
    $nombre        = $_POST['nombre'] ?? '';
    $telefono      = $_POST['telefono'] ?? '';
    $direccion     = $_POST['direccion'] ?? '';
    $codigo_ubigeo = $_POST['codigo_ubigeo'] ?? '';
    $gmail         = $_POST['gmail'] ?? '';

    try {
        $stmt = $pdo->prepare("CALL sp_insertar_destinatario(?,?,?,?,?,?)");
        $stmt->execute([$ruc, $nombre, $telefono, $direccion, $codigo_ubigeo, $gmail]);
        $stmt->closeCursor();
        header('Location: listar_destinatarios.php');
        exit;
    } catch (PDOException $e) {
        $errores[] = $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nuevo destinatario</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        form { background:#fff; padding:20px; border-radius:8px; max-width:450px; }
        label { display:block; margin-top:10px; }
        input { width:100%; padding:6px; margin-top:4px; box-sizing:border-box; }
        button, a.btn { margin-top:15px; padding:8px 12px; border:none; border-radius:4px; text-decoration:none; }
        button { background:#2ecc71; color:#fff; }
        a.btn { background:#7f8c8d; color:#fff; }
        .error { color:#e74c3c; }
    </style>
</head>
<body>
<h1>Registrar destinatario</h1>
<a href="listar_destinatarios.php" class="btn">← Volver</a>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err) echo "<p>Error: ".htmlspecialchars($err)."</p>"; ?>
    </div>
<?php endif; ?>

<form method="post">
    <label>RUC (11 dígitos):
        <input type="text" name="ruc" required pattern="\d{11}" maxlength="11">
    </label>

    <label>Nombre / Razón social:
        <input type="text" name="nombre" required>
    </label>

    <label>Teléfono:
        <input type="text" name="telefono" pattern="\d+" maxlength="20">
    </label>

    <label>Dirección:
        <input type="text" name="direccion" required>
    </label>

    <label>Código Ubigeo (6 dígitos):
        <input type="text" name="codigo_ubigeo" required pattern="\d{6}" maxlength="6">
    </label>

    <label>Correo electrónico:
        <input type="email" name="gmail">
    </label>

    <button type="submit">Guardar</button>
</form>
</body>
</html>
