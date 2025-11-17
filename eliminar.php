<?php
require_once 'conexion.php';

if (!isset($_GET['ruc'])) {
    header("Location: listar.php");
    exit;
}

$ruc = $_GET['ruc'];

try {
    $stmt = $pdo->prepare("CALL sp_eliminar_destinatario(?)");
    $stmt->execute([$ruc]);
} catch (PDOException $e) {
    die("Error al eliminar: " . $e->getMessage());
}

header("Location: listar.php");
exit;
