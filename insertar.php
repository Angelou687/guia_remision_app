<?php
require_once 'conexion.php';

$errores = [];
$mensaje = "";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $ruc       = $_POST['ruc'] ?? '';
    $nombre    = $_POST['nombre'] ?? '';
    $telefono  = $_POST['numero_telefono'] ?? '';
    $direccion = $_POST['calle_direccion'] ?? '';
    $ubigeo    = $_POST['codigo_ubigeo'] ?? '';
    $gmail     = $_POST['gmail'] ?? '';

    if ($ruc === '' || strlen($ruc) != 11) {
        $errores[] = "El RUC es obligatorio y debe tener 11 caracteres.";
    }
    if ($nombre === '') {
        $errores[] = "El nombre es obligatorio.";
    }
    if ($direccion === '') {
        $errores[] = "La dirección es obligatoria.";
    }
    if ($ubigeo === '') {
        $errores[] = "El código de ubigeo es obligatorio.";
    }

    if (empty($errores)) {
        try {
            $stmt = $pdo->prepare("CALL sp_insertar_destinatario(?, ?, ?, ?, ?, ?)");
            $stmt->execute([$ruc, $nombre, $telefono, $direccion, $ubigeo, $gmail]);
            header("Location: listar.php");
            exit;
        } catch (PDOException $e) {
            $errores[] = "Error al insertar: " . $e->getMessage();
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Nuevo Destinatario</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:0; }
        header { background:#2c3e50; color:#fff; padding:1rem 2rem; }
        main {
            max-width:700px;
            margin:2rem auto;
            background:#fff;
            padding:2rem;
            border-radius:8px;
            box-shadow:0 2px 8px rgba(0,0,0,0.1);
        }
        label { display:block; margin-top:0.75rem; }
        input[type="text"] {
            width:100%;
            padding:0.5rem;
            margin-top:0.25rem;
            box-sizing:border-box;
        }
        .btn { margin-top:1rem; padding:0.5rem 1rem; border:none; border-radius:4px; cursor:pointer; }
        .btn-primary { background:#3498db; color:#fff; }
        .btn-secondary { background:#7f8c8d; color:#fff; text-decoration:none; padding:0.5rem 1rem; }
        .errores { background:#e74c3c; color:#fff; padding:0.5rem; margin-bottom:1rem; border-radius:4px; }
    </style>
</head>
<body>
<header>
    <h1>Nuevo destinatario</h1>
</header>
<main>
    <a href="listar.php" class="btn btn-secondary">← Volver</a>

    <?php if (!empty($errores)): ?>
        <div class="errores">
            <ul>
            <?php foreach ($errores as $err): ?>
                <li><?= htmlspecialchars($err) ?></li>
            <?php endforeach; ?>
            </ul>
        </div>
    <?php endif; ?>

    <form method="post">
        <label>RUC:
            <input type="text" name="ruc" maxlength="11" value="<?= htmlspecialchars($_POST['ruc'] ?? '') ?>">
        </label>
        <label>Nombre:
            <input type="text" name="nombre" value="<?= htmlspecialchars($_POST['nombre'] ?? '') ?>">
        </label>
        <label>Teléfono:
            <input type="text" name="numero_telefono" value="<?= htmlspecialchars($_POST['numero_telefono'] ?? '') ?>">
        </label>
        <label>Dirección:
            <input type="text" name="calle_direccion" value="<?= htmlspecialchars($_POST['calle_direccion'] ?? '') ?>">
        </label>
        <label>Código Ubigeo:
            <input type="text" name="codigo_ubigeo" maxlength="6" value="<?= htmlspecialchars($_POST['codigo_ubigeo'] ?? '') ?>">
        </label>
        <label>Correo:
            <input type="text" name="gmail" value="<?= htmlspecialchars($_POST['gmail'] ?? '') ?>">
        </label>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</main>
</body>
</html>
