<?php
require_once 'conexion.php';
$pdo = obtenerConexion();

$stmt = $pdo->query("CALL sp_listar_destinatarios()");
$destinatarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
$stmt->closeCursor(); // buena práctica tras usar CALL
?>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Listar destinatarios</title>
</head>
<body>
    <h1>Destinatarios</h1>
    <p><a href="index.php">← Volver al menú</a></p>
    <p><a href="insertar.php">+ Nuevo destinatario</a></p>

    <table border="1" cellpadding="5" cellspacing="0">
        <tr>
            <th>RUC</th>
            <th>Nombre</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        <?php foreach ($destinatarios as $d): ?>
            <tr>
                <td><?= htmlspecialchars($d['ruc']) ?></td>
                <td><?= htmlspecialchars($d['nombre']) ?></td>
                <td><?= htmlspecialchars($d['numero_telefono']) ?></td>
                <td><?= htmlspecialchars($d['gmail']) ?></td>
                <td><?= $d['estado'] == 1 ? 'Activo' : 'Inactivo' ?></td>
                <td>
                    <a href="editar.php?ruc=<?= urlencode($d['ruc']) ?>">Editar</a> |
                    <a href="eliminar.php?ruc=<?= urlencode($d['ruc']) ?>"
                       onclick="return confirm('¿Seguro que deseas eliminar lógicamente este destinatario?');">
                        Eliminar lógico
                    </a>
                </td>
            </tr>
        <?php endforeach; ?>
    </table>
</body>
</html>
