<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

function obtenerConexion()
{
    $host    = 'localhost';
    $dbname  = 'guia_remision';

    $usuario = 'root';     
    $clave   = 'angel';         

    try {
        $dsn = "mysql:host=$host;dbname=$dbname;charset=utf8";
        $pdo = new PDO($dsn, $usuario, $clave);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $pdo;
    } catch (PDOException $e) {
        echo "Error de conexión: " . $e->getMessage();
        exit;
    }
}
