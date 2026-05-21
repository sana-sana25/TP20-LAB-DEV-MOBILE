<?php

header('Content-Type: application/json');

error_reporting(0);

require_once __DIR__ . '/../service/ContactService.php';

$response = array(
    "success" => false,
    "message" => ""
);

try {

    $rawData = file_get_contents("php://input");

    $data = json_decode($rawData, true);

    if ($data == null) {

        $response["message"] = "JSON invalide";

        echo json_encode($response);
        exit;
    }

    $name = isset($data['name']) ? $data['name'] : '';
    $phone = isset($data['phone']) ? $data['phone'] : '';

    if ($name == '' || $phone == '') {

        $response["message"] = "Champs manquants";

        echo json_encode($response);
        exit;
    }

    $service = new ContactService();

    $insert = $service->insert(
        $name,
        $phone,
        "mobile"
    );

    if ($insert) {

        $response["success"] = true;
        $response["message"] = "Insertion réussie";

    } else {

        $response["message"] = "Erreur insertion";
    }

} catch (Exception $e) {

    $response["message"] = $e->getMessage();
}

echo json_encode($response);

exit;

?>