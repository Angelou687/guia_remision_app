<?php
require_once "conexion.php";
$stmt = $pdo->query("CALL sp_listar_destinatarios()");
$destinatarios = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Destinatarios</title>
    <link rel="stylesheet" href="estilos.css">
</head>

<body>
<div class="container">
    <h2>Lista de Destinatarios</h2>

    <a href="insertar_destinatario.php" class="btn">Agregar Destinatario</a>

    <table>
        <tr>
            <th>RUC</th>
            <th>Nombre</th>
            <th>Teléfono</th>
            <th>Correo</th>
            <th>Acciones</th>
        </tr>

        <?php foreach ($destinatarios as $d): ?>
        <tr>
            <td><?= $d['ruc'] ?></td>
            <td><?= $d['nombre'] ?></td>
            <td><?= $d['telefono'] ?></td>
            <td><?= $d['gmail'] ?></td>
            <td>
                <a href="editar_destinatario.php?ruc=<?= $d['ruc'] ?>">Editar</a> |
                <a href="eliminar_destinatario.php?ruc=<?= $d['ruc'] ?>">Eliminar</a>
            </td>
        </tr>
        <?php endforeach; ?>

    </table>

    <p><a href="index.php">Volver</a></p>
</div>
</body>
</html>
