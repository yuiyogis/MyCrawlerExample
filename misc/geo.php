<?php
/**
 * 取得GeoJson 的矩形邊界  
 * 
 * @param $json GeoJSON 資料
 * @return [左上lat, 左上 lon, 右下lat, 右下lon]
 *
 *     *       *****
 *    * *      *   *
 *   *   * =>  *   *
 *    * *      *   *
 *     *       *****
 */
function geojson_boundary($json){
  $geometries = loadGeometry_noHole($json);

  $lats=array();
  $lons=array();

  foreach( $geometries as $geometry ) {
    for($i=0;$i<count($geometry->exterior);$i++){
      for($j=0;$j<count($geometry->exterior[$i]);$j++){
        $lats[]=$geometry->exterior[$i][$j][1];
        $lons[]=$geometry->exterior[$i][$j][0];
      }
    }
  }

  return array(max($lats),min($lons),min($lats),max($lons),$geometries);
}

/**
 * 設定座標位移角度($brng) 及距離($dist) 
 * 
 *         0
 *         |
 *  270 ---+--- 90
 *         |
 *        180
 */
function place_len_to($lat, $long, $brng, $dist) {
    $a = 6378137;
    $b = 6356752.3142;
    $f = 1/298.257223563;
    $alpha1 = deg2rad($brng);
    $sinAlpha1 = sin($alpha1);
    $cosAlpha1 = cos($alpha1);
    $tanU1 = (1 - $f) * tan(deg2rad($lat));
    $cosU1 = 1 / sqrt((1 + $tanU1*$tanU1));
    $sinU1 = $tanU1 * $cosU1;
    $sigma1 = atan2($tanU1, $cosAlpha1);
    $sinAlpha = $cosU1 * $sinAlpha1;
    $cosSqAlpha = 1 - $sinAlpha * $sinAlpha;
    $uSq = $cosSqAlpha * ($a * $a - $b * $b) / ($b * $b);
    $A = 1 + $uSq / 16384 * (4096 + $uSq * (-768 + $uSq * (320 - 175 * $uSq)));
    $B = $uSq / 1024 * (256 + $uSq * (-128 + $uSq * (74 - 47 * $uSq)));
    $sigma = $dist / ($b * $A);
    $pi = pi();
    $sigmaP = 2 * $pi;
    while( abs($sigma - $sigmaP) > 0.000000000001 ) {
        $cos2SigmaM = cos(2 * $sigma1 + $sigma);
        $sinSigma = sin($sigma);
        $cosSigma = cos($sigma);
        $deltaSigma = $B * $sinSigma * ($cos2SigmaM + $B / 4 * ($cosSigma * (-1 + 2 * $cos2SigmaM * $cos2SigmaM) - $B / 6 * $cos2SigmaM * (-3 + 4 * $sinSigma * $sinSigma) * (-3 + 4 * $cos2SigmaM * $cos2SigmaM)));
        $sigmaP = $sigma;
        $sigma = $dist / ($b * $A) + $deltaSigma;
    }
    $tmp = $sinU1 * $sinSigma - $cosU1 * $cosSigma * $cosAlpha1;
    $lat_end = atan2($sinU1 * $cosSigma + $cosU1 * $sinSigma * $cosAlpha1, (1 - $f) * sqrt($sinAlpha * $sinAlpha + $tmp * $tmp));
    $lambda = atan2($sinSigma * $sinAlpha1, $cosU1 * $cosSigma - $sinU1 * $sinSigma * $cosAlpha1);
    $C = $f / 16 * $cosSqAlpha * (4 + $f * (4 - 3 * $cosSqAlpha));
    $L = $lambda - (1 - $C) * $f * $sinAlpha * ($sigma + $C * $sinSigma * ($cos2SigmaM + $C * $cosSigma * (-1 + 2 * $cos2SigmaM * $cos2SigmaM)));
    $long_end = fmod((deg2rad($long) + $L + 3 * $pi), 2 * $pi) - $pi;
    return array(rad2deg($lat_end), rad2deg($long_end));
}


function loadGeometry($features){
  $results = array();

  foreach( $features->features as $feature ){
    $type = $feature->geometry->type;
    if ($type=="GeometryCollection"){
      foreach($feature->geometry->geometries as $geometry){
        if ( $geometry->type != 'Polygon' ) continue;

        $geo = new stdClass();
        $geo->exterior = $geometry->coordinates[0];
        $geo->hole = array();
        for($i=1;$i<count($geometry->coordinates);$i++)
          $geo->hole[] = $geometry->coordinates[$i];

        $results[] = $geo;
      }
    }
    else if ( $type == 'Polygon' ) {
      $geometry = $feature->geometry;
      $geo = new stdClass();
      $geo->exterior = $geometry->coordinates[0];
      $geo->hole = array();
      for($i=1;$i<count($geometry->coordinates);$i++)
        $geo->hole[] = $geometry->coordinates[$i];

        $results[] = $geo;
    }
  }
  return $results;
}

/**
 * 判斷兩點是否存在 $x,$y 範圍內
 *
 * @param $x X軸座標陣列
 * @param $y Y軸座標陣列
 */
function isPointInsidePolygon ($x, $y, $pointX, $pointY){
  $isInside = false;
  $nPoints = count($x);

  $j = 0;
  for ($i = 0; $i < $nPoints; $i++) {
      $j++;
      if ($j == $nPoints) $j = 0;
      if ($y[$i] < $pointY && $y[$j] >= $pointY || $y[$j] < $pointY && $y[$i] >= $pointY) {
        if ($x[$i] + ($pointY - $y[$i]) / ($y[$j] - $y[$i]) * ($x[$j] - $x[$i]) < $pointX) {
          $isInside = !$isInside;
      }
    }
  }

  return $isInside;
}

function loadGeometry_noHole($features){
  $results = array();

  foreach( $features->features as $feature ){
    $type = $feature->geometry->type;
    if ($type=="GeometryCollection"){
      foreach($feature->geometry->geometries as $geometry){
        if ( $geometry->type != 'Polygon' ) continue;

        $geo = new stdClass();
        $geo->exterior = $geometry->coordinates;
        $geo->hole = array();
        //for($i=1;$i<count($geometry->coordinates);$i++)
        //  $geo->hole[] = $geometry->coordinates[$i];

        $results[] = $geo;
      }
    }
    else if ( $type == 'Polygon' ) {
      $geometry = $feature->geometry;
      $geo = new stdClass();
      $geo->exterior = $geometry->coordinates;
      $geo->hole = array();
      //for($i=1;$i<count($geometry->coordinates);$i++)
      //  $geo->hole[] = $geometry->coordinates[$i];

      $results[] = $geo;
    }

    else if ( $type == 'MultiPolygon' ) {
      $geometry = $feature->geometry;
      for($i=0; $i<count($geometry->coordinates); $i++){
        $geo = new stdClass();
        $geo->exterior = $geometry->coordinates[$i];
        $geo->hole = array();
        $results[] = $geo;
      }
    }

  }
  return $results;
}
