<?php
require_once "conexion.php";

$ruc = $_GET['ruc'];

$pdo->prepare("CALL sp_eliminar_destinatario(?)")->execute([$ruc]);

header("Location: listar_destinatarios.php");
exit;
