<?php
require_once 'conexion.php';
$pdo = obtenerConexion();

$row = $pdo->query("SELECT COUNT(*) AS total FROM destinatario")->fetch(PDO::FETCH_ASSOC);

echo "Total de destinatarios: " . $row['total'] . PHP_EOL;
