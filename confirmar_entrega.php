<?php
require_once 'conexion.php';

$errores = [];
$ok = false;

// Cargar guías que aún no están entregadas
try {
    $guias = $pdo->query("
        SELECT DISTINCT g.codigo_guia,
               g.fecha_emision,
               g.estado_guia,
               COALESCE(d.nombre, 'Sin destinatario') AS destinatario
        FROM cabecera_guia g
        LEFT JOIN cuerpo_guia cg ON cg.codigo_guia = g.codigo_guia
        LEFT JOIN destinatario d ON d.ruc = cg.ruc_destinatario
        LEFT JOIN traslado t     ON t.codigo_guia = g.codigo_guia
        WHERE g.estado_guia <> 'entregada'
        ORDER BY g.fecha_emision DESC, g.codigo_guia
    ")->fetchAll(PDO::FETCH_ASSOC);
} catch (PDOException $e) {
    die("Error cargando guías: " . $e->getMessage());
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $codigo_guia = $_POST['codigo_guia'] ?? '';

    if ($codigo_guia === '') {
        $errores[] = "Debe seleccionar una guía.";
    } else {
        try {
            $stmt = $pdo->prepare("CALL sp_confirmar_entrega(?)");
            $stmt->execute([$codigo_guia]);
            $stmt->closeCursor();
            $ok = true;
        } catch (PDOException $e) {
            $errores[] = $e->getMessage();
        }
    }
}
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Confirmar entrega de guía</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:30px; }
        h1 { color:#2c3e50; }
        form { background:#fff; padding:20px; border-radius:8px; max-width:600px; }
        label { display:block; margin-top:8px; }
        select { width:100%; padding:6px; margin-top:3px; box-sizing:border-box; }
        button, a.btn { margin-top:15px; padding:8px 12px; border:none; border-radius:4px; text-decoration:none; }
        button { background:#27ae60; color:#fff; }
        a.btn { background:#7f8c8d; color:#fff; }
        .ok { color:#27ae60; margin-top:10px; }
        .error { color:#e74c3c; margin-top:10px; }
    </style>
</head>
<body>
<h1>Confirmar entrega de guía</h1>
<a href="index.php" class="btn">← Volver al menú</a>

<?php if ($ok): ?>
    <p class="ok">La guía fue marcada como entregada correctamente.</p>
<?php endif; ?>

<?php if ($errores): ?>
    <div class="error">
        <?php foreach ($errores as $err): ?>
            <p>Error: <?= htmlspecialchars($err) ?></p>
        <?php endforeach; ?>
    </div>
<?php endif; ?>

<form method="post">
    <label>Seleccione la guía a confirmar:
        <select name="codigo_guia" required>
            <option value="">-- Seleccione guía --</option>
            <?php foreach ($guias as $g): ?>
                <option value="<?= htmlspecialchars($g['codigo_guia']) ?>">
                    <?= htmlspecialchars($g['codigo_guia']) ?>
                    — Fecha: <?= htmlspecialchars($g['fecha_emision']) ?>
                    — Estado: <?= htmlspecialchars($g['estado_guia']) ?>
                    — Destinatario: <?= htmlspecialchars($g['destinatario']) ?>
                </option>
            <?php endforeach; ?>
        </select>
    </label>

    <button type="submit">Confirmar entrega</button>
</form>
</body>
</html>
