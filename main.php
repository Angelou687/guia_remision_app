<?php
require_once 'conexion.php'; // tu getConnection()

function menu() {
    while (true) {
        echo "\n=== MENU PRINCIPAL ===\n";
        echo "1. Listar destinatarios\n";
        echo "2. Insertar destinatario\n";
        echo "3. Actualizar destinatario\n";
        echo "4. Eliminar lógico destinatario\n";
        echo "0. Salir\n";
        echo "Opción: ";

        $op = trim(fgets(STDIN));

        switch ($op) {
            case '1': listarDestinatarios(); break;
            case '2': insertarDestinatario(); break;
            case '3': actualizarDestinatario(); break;
            case '4': eliminarDestinatario(); break;
            case '0': exit("Saliendo...\n");
            default: echo "Opción no válida.\n";
        }
    }
}

function listarDestinatarios() {
    $pdo = getConnection();
    $stmt = $pdo->query("CALL sp_listar_destinatarios()");
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo "\nDESTINATARIOS ACTIVOS:\n";
    foreach ($rows as $r) {
        echo $r['ruc']." | ".$r['nombre']." | ".$r['numero_telefono']." | ".$r['gmail'].PHP_EOL;
    }
}

function insertarDestinatario() {
    $pdo = getConnection();

    echo "RUC: ";      $ruc = trim(fgets(STDIN));
    echo "Nombre: ";   $nombre = trim(fgets(STDIN));
    echo "Teléfono: "; $tel = trim(fgets(STDIN));
    echo "Dirección: ";$dir = trim(fgets(STDIN));
    echo "Ubigeo: ";   $ubi = trim(fgets(STDIN));
    echo "Gmail: ";    $mail = trim(fgets(STDIN));

    $stmt = $pdo->prepare("CALL sp_insertar_destinatario(?,?,?,?,?,?)");
    $stmt->execute([$ruc, $nombre, $tel, $dir, $ubi, $mail]);

    echo "Destinatario insertado.\n";
}

// funciones actualizarDestinatario() y eliminarDestinatario() serían similares,
// llamando a sp_actualizar_destinatario y sp_eliminar_logico_destinatario.

menu();
