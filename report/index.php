<?php
$servername = "localhost";
$username = "root";
$password = "root";
$dbname = "bigdata";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
$arrayPoids = array();
$arrayTweetId = array();

$sql = "SELECT DISTINCT * FROM poids_data";
$result = mysqli_query($conn, $sql);


if (mysqli_num_rows($result) > 0) {
    // output data of each row
    $i = 0;
    while($row = mysqli_fetch_assoc($result)) {
        $arrayPoids[$i] = $row["poids"];
        $arrayTweetId[$i] = $row["tweet_id"];
        $i++;
        // echo "id_tweet: " . $row["tweet_id"]. " - poids: " . $row["poids"]."<br>";
    }
} else {
    echo "0 results";
}

$positive = 0;
$veryPositve = 0;
$negative = 0;
$veryNegative = 0;
$neutral = 0;

for($j= 0; $j < $i ; $j++) {

    if(strcmp($arrayPoids[$j], "positive") == 0) {
        $positive++;
    }

    if(strcmp($arrayPoids[$j], "very positive") == 0) {
        $veryPositve++;
    }

    if(strcmp($arrayPoids[$j], "negative") == 0) {
        $negative++;
    }

    if(strcmp($arrayPoids[$j], "very negative") == 0) {
        $veryNegative++;
    }

    if(strcmp($arrayPoids[$j], "neutral") == 0) {
        $neutral++;
    }

}

mysqli_close($conn);
?>

<!DOCTYPE html>
<html>
<head>

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">


    <!-- CHART JS IMPORT-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.bundle.js" integrity="sha256-o8aByMEvaNTcBsw94EfRLbBrJBI+c3mjna/j4LrfyJ8=" crossorigin="anonymous"></script>

    <title>BigData_init</title>
</head>
<body>
    <div class="container-fluid">
        <br>
        <h1 class="d-flex justify-content-center">
            ETNA PROJECT : Study on yellow vests
        </h1>
        <h3 class="d-flex justify-content-center">
            Total person picked <?php echo $i; ?>
        </h3>
        <h4 class="d-flex justify-content-center">Representation of public opinion</h4>
        <br>
        <div class = "row">
            <canvas id="myChart" width="400" height="100" aria-label="Hello ARIA World" role="img"></canvas>
        </div>

        <br><br>
        <h4 class="d-flex justify-content-center">Percentage of people</h4>
        <div class="row">
            <canvas id="myChart1" width="400" height="100" aria-label="Hello ARIA World" role="img"></canvas>
        </div>
    </div>

    <script>
    var ctx = document.getElementById("myChart1").getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'polarArea',
        data: {
            labels: ["Very positive", "positive", "neutral","negative","Very negative"],
            datasets: [{
                label:'',
                data: [<?php echo (100 * $veryPositve) / $i ?>,
                    <?php echo (100 * $positive) / $i ?>,
                    <?php echo (100 * $neutral) / $i ?>,
                    <?php echo (100 * $negative) / $i ?>,
                    <?php echo (100 * $veryNegative) / $i ?>
                ],
                backgroundColor: [
                    'rgb(21, 255, 0)',
                    'rgb(0, 255, 85)',
                    'rgb(255, 248, 71)',
                    'rgb(255, 71, 71)',
                    'rgb(255, 20, 20)'
                ],
                borderColor: [
                    'rgb(21, 255, 0)',
                    'rgb(0, 255, 85)',
                    'rgb(255, 248, 71)',
                    'rgb(255, 71, 71)',
                    'rgb(255, 20, 20)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });
</script>

<script>
var ctx = document.getElementById("myChart").getContext('2d');
var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ["Very positive", "positive", "neutral","negative","Very negative"],
        datasets: [{
            label:'',
            data: [<?php echo $veryPositve; ?>,
                <?php echo $positive; ?>,
                <?php echo $neutral; ?>,
                <?php echo $veryNegative; ?>,
                <?php echo $negative; ?>
            ],
            backgroundColor: [
                'rgb(21, 255, 0)',
                'rgb(0, 255, 85)',
                'rgb(255, 248, 71)',
                'rgb(255, 71, 71)',
                'rgb(255, 20, 20)'
            ],
            borderColor: [
                'rgb(21, 255, 0)',
                'rgb(0, 255, 85)',
                'rgb(255, 248, 71)',
                'rgb(255, 71, 71)',
                'rgb(255, 20, 20)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero:true
                }
            }]
        }
    }
});
</script>

<!-- BOOTSTRAP JS IMPORT -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
