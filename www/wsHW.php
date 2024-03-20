<?php

header("Content-Type: application/json");
$method = strtolower($_SERVER["REQUEST_METHOD"]);

$data = json_decode(file_get_contents("php://input"));

if ($method == "get") {
    $response["stockTicker"] = "APPL";
    $response["stockPrice"] = 174.64;
    $response["numShares"] = 10;
    $response["dateAccessed"] = date("F j, Y, g:i a");
    http_response_code(200);
}
else if ($method == "post") {
    $response["stockTicker"] = $data->stockTicker;
    $response["stockPrice"] = $data->stockPrice;
    $response["numShares"] = $data->numShares;
    $response["dateCreated"] = time();
    $response["originServer"] = gethostname();
    http_response_code(200);
}
else {
    http_response_code(405);
    die();
}
echo json_encode($response);