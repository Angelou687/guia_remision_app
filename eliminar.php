<?php
require_once 'conexion.php';
$pdo = obtenerConexion();

if (!isset($_GET['ruc'])) {
    die("RUC no proporcionado");
}

$ruc = $_GET['ruc'];

try {
    $stmt = $pdo->prepare("CALL sp_eliminar_logico_destinatario(:ruc)");
    $stmt->execute([':ruc' => $ruc]);
    $stmt->closeCursor();
    header('Location: listar.php');
    exit;
} catch (PDOException $e) {
    die("Error al eliminar lógicamente: " . $e->getMessage());
}
