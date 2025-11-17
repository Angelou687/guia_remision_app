<?php
require_once 'conexion.php';

$ruc = $_GET['ruc'] ?? null;
if (!$ruc) {
    die("RUC no especificado");
}

try {
    $stmt = $pdo->prepare("CALL sp_eliminar_destinatario(?)");
    $stmt->execute([$ruc]);
    $stmt->closeCursor();
    header('Location: listar_destinatarios.php');
    exit;
} catch (PDOException $e) {
    die("Error al eliminar destinatario: " . $e->getMessage());
}
