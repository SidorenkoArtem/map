<%@page import="com.mapsapp.domain.BelEnergoCompany"%>
<%@page import="com.mapsapp.controller.HelloContoller"%>
<%@page import="com.mapsapp.service.database.DatabaseServiceImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ru" lang="ru">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Language" content="ru" />

<title>ЭЛЕКТРОСЕТИ</title>
<link href="lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/my_bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="css/my_navbar.css" ><!-- оформление кнопочек Линии и Авто -->
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/hidden_element.js"></script><!-- делает элементы невидимыми -->
<script type="text/javascript" src="js/markerclusterer.js"></script>
<script type="text/javascript" src="js/calendar.js"></script><!-- календарь  -->
<script type="text/javascript" src="js/date_today.js"></script><!-- сегодняшняя дата в input -->

<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#map_canvas {
	width: 99%;
	height: 100%;
	border: 1px solid;
	margin: 0 auto;
}

.panel-body label {
	margin: 0px;
	font-size: 0.85em;
	padding-left: 0;
}

.glyphicon-erase:hover {
	color: #dd7700;
}
</style>

</head>

<body onload1="initialize()" style="">



	<div class="row" style="padding-bottom: 2px; height: 100%;">
		<div class="col-md-2" style="padding-left: 1%">

			<ul class="nav navbar-nav">
				<li><a href="yandex/index.html">Яндекс карта</a></li>
				<li><a href="osmap/osmap.html">OSM карта</a></li>
			</ul>

			<br>
			<br>

			<h4 onclick="removeAllVehiclesObjects()" style1="text-align: center;">
				&nbsp; Карта электросетей <span
					style="float: right; cursor: pointer;"
					class="glyphicon glyphicon-erase" title="Очистить карту"></span>
			<br>
			<br>
				
			<ul id="navbar_lin_avt">	
				<a href="javascript:void(0);" onClick="openLayer(div1); ">Линии</a>
				<a href="javascript:void(0);" onClick="openLayer(div2);">Авто</a>
        	</ul>	
        	
        	 <div id="distance_way">
        		<div>
					<input id="way" type="button" onclick="get_way()" value="Путь в период"  style="width:200px;height:25px;">
				</div> 
				<br>
			<div>
			<form  style="z-index:9;" action="">
				<p style="font-size:14px;">
				с <input style="width: 80px;"   type="data" name="calend" id="today1" onfocus="this.select();_Calendar.lcs(this)"
    			onclick="event.cancelBubble=true;this.select();_Calendar.lcs(this)">
				по <input style="width: 80px;" type="data" name="calend" id="today2" onfocus="this.select();_Calendar.lcs(this)"
    			onclick="event.cancelBubble=true;this.select();_Calendar.lcs(this)">
				</p>
			</form>
			</div>
			</div>
		
        	<div id="hidden_element"> <!-- from my_navbar.css -->	
			</h4>
			<div id="div1">  <!--from hidden_element.js-->
			<select id="bel_energo_companies" class="form-control input-sm"
				style="margin: 1px;">
				<option value="bel_energo_companies">- ЭС -</option>
			</select>
			<h5></h5>
			<select id="sectors" class="form-control input-sm"
				style="margin: 1px;">
				<option value="">- УЧАСТКИ -</option>
			</select>
			<h5></h5>
			<!--select id="air_lines1" class="form-control input-sm">
			<option value="" selected> </option>
		</select-->
			<div class="panel">
				<div class="panel-heading">Магистральные линии</div>
				<div class="panel-body checkbox" id="air_lines"></div>
			</div>
			<h5></h5>
			<div class="panel">
				<div class="panel-heading">ВЛ 10кВ</div>
				<div class="panel-body checkbox" id="air_lines10"></div>
			</div></div></div>
			<!--select id="air_lines10" class="form-control input-sm">
			<option value="" selected> </option>
		</select-->
			<div id="hidden_element">
			<div  id="div2"> <!--from hidden_element.js-->
			<h5></h5>
			<select id="bel_energo_companies2" class="form-control input-sm"
				style="margin: 1px;">
				<option value="">-Предприятия электросетей-</option>
			</select><br> 
			<select id="vehicles" class="form-control input-sm"
				style="margin: 1px;">
				<option value="">-Машины-</option>
			</select>
			<div class="small">
				<div id="marks" style=""></div>
				<div id="lines" style=""></div>
				<div id="zoom" style=""></div>
			</div></div>
			<div>
				<input id="way" type="button" onclick="get_way()" value="Way"
					style="width: 200px; height: 25px">
			</div>
		</div>
		<div class="col-md-10" style="height: 100%; padding-top: 5px;">
			<div id="map_canvas"></div>
		</div>
		<script type="text/javascript"
			src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAdlipia47Yw1mk-pv_dq_bZkpXdzIAlbY&sensor=false&libraries=visualization&dummy=.js"></script>


		<script type="text/javascript">

//	function initialize() {
		var APP_data = {lines:{},substations:{}};
		var lines = 0;
		var marks = 0;
		var cur_line = 0;  // тек. пролет
		var cur_line_obj;
		var cur_idLine = -1;
		var cur_latLng;
		var painted = false;
		var markerList = [];
		var polylineList = [];
		
		var markerVehiclesList = [];
		var coordinatesAllWay = [];
		var pointsOnWay = [];
		var flightPath;
		var arrayForRoad = [];
        var markersBounds;
        var id_vehicles;
        
        
        const is_zoom = [[8, 102400], [9, 51200], [10, 25600], [11, 12800], [12, 6400], 
                         [13,3200], [14, 1600], [15, 800], [16, 400], [17, 200], [18, 100], 
                         [19, 50], [20, 25]];
		
        const isZoomForCircle =[[13, 45],[14, 35],[15, 20],[16, 10],[17, 6],[18, 4],[19, 3],[20, 3]];
		const Spans = [152,246,247, 3,248,249, 150,250,251, 151,252,253, 153,254,255  ,149,244];
		const Pillars = [86,85,236,235,220];
		const Pillar10 = 220;
		const Substations = [108,148];
		const Lines = [1,216,217,218,219,230,231,230];

		const selected =  '#0033E5'; // '#FF7700';
		const def_color = "#ee3300";
		const main_zoom = 13;		

		function get_bel_energo_companiess() {
			$.ajax({url: "bel_energo_companies2", method: "GET", dataType: "json"})
					.done(function (data) {
						if (data) {
							$.each(data, function (i, item) { $('#bel_energo_companies2').append($('<option>', {value: item.id, text: item.name})); });
							$('#bel_energo_companies2 option').eq(1)[0].selected = true;
							$('#bel_energo_companies2').change();
						}
					});
		}
		get_bel_energo_companiess();

		$('#bel_energo_companies2').on("change", function () {
			var codes = $('#bel_energo_companies2').val();
			if (codes) {
				get_vehicles(codes);
			}
		  }
		);

	    function latlng2distance(lat1, long1, lat2, long2) {
	        //радиус Земли
	        var R = 6372795;
	        //перевод коордитат в радианы
	        lat1 *= Math.PI / 180;
	        lat2 *= Math.PI / 180;
	        long1 *= Math.PI / 180;
	        long2 *= Math.PI / 180;
	        //вычисление косинусов и синусов широт и разницы долгот
	        var cl1 = Math.cos(lat1);
	        var cl2 = Math.cos(lat2);
	        var sl1 = Math.sin(lat1);
	        var sl2 = Math.sin(lat2);
	        var delta = long2 - long1;
	        var cdelta = Math.cos(delta);
	        var sdelta = Math.sin(delta);
	        //вычисления длины большого круга
	        var y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
	        var x = sl1 * sl2 + cl1 * cl2 * cdelta;
	        var ad = Math.atan2(y, x);
	        var dist = ad * R; //расстояние между двумя координатами в метрах
	        return dist
	    }
		
		function get_way(){
			mode=0;
			drawWay();
		}
		
		function drawWay(){
			var prevLat, prevLng;
			var lengthWay = 0;
			removePointsOnWay();
			removeVehicles();
			removeWayVehicle();
			markersBounds = new google.maps.LatLngBounds();
			coordinatesAllWay = [];
			var rad;
			var zoom = map.getZoom();
			for(i=0; i<isZoomForCircle.length; i++){
				if (zoom == isZoomForCircle[i][0]){
					rad = isZoomForCircle[i][1];
					break;
				}
			}
			var codes = $('#vehicles').val();
			var date = document.getElementById('today1').value;
			var lastDate = document.getElementById('today2').value;
			if (codes) {
				$.ajax({
					url: "coordinatesVehicles",
					method: "POST",
					data: {code:  codes, date: date, lastDate: lastDate}
				})
				.done(function (data){
					$.each(data, function (i, item){
						if (item.stat == "DISCONNECT"){
							var flightPathLocation = new google.maps.Polyline({
								  path: coordinatesAllWay,
								  geodesic: true,
								  strokeColor: '#FF0000',
								  strokeOpacity: 1.0,
								  strokeWeight: 2,
								  icons: masArrows,
								  map: map
							});
							arrayForRoad.push(flightPathLocation);
							var flightPathLocation1 = new google.maps.Polyline({
								  path: [coordinatesAllWay[coordinatesAllWay.length-1], {lat: item.latitude, lng: item.longitude}],
								  geodesic: true,
								  strokeColor: '#FFFF00',
								  strokeOpacity: 1.0,
								  strokeWeight: 3,
								  icons: masArrows,
								  map: map
							});
							arrayForRoad.push(flightPathLocation1);
							coordinatesAllWay = [];
						}
						coordinatesAllWay.push({lat: item.latitude, lng: item.longitude});
						if (item.stat == "WAIT" && map.getZoom() > 14){	
							var marker = new google.maps.Marker({
					            position: {lat: item.latitude, lng: item.longitude} ,
					            map: map,
					            icon :  {url: "img/Wait.png",
					            	scaledSize: new google.maps.Size(20, 30)}
					          });
							var infoWindow = new google.maps.InfoWindow({
								content : "<br>Дата и время : " + item.date + "/" + item.time +
								          "<br>Продолжительность остановки : "+ item.waitTime 
								});
					    	marker.addListener('click', function(){
					    		infoWindow.open(map, marker);});
							pointsOnWay.push(marker);
						}
						if (item.stat == "SHOWPOINT" && map.getZoom() >= 13){
					    	var circle = new google.maps.Circle({
					    	      strokeColor: '#602020',
					    	      strokeOpacity: 0.8,
					    	      strokeWeight: 2,
					    	      fillColor: '#602020',
					    	      fillOpacity: 0.35,
					    	      center: {lat: item.latitude, lng: item.longitude},
					    		  map : map,
					    		  radius: rad
					   });
					    	pointsOnWay.push(circle);
							var infoWindow = new google.maps.InfoWindow({
								content : "<br>Дата и время : "+ item.date + "/" + item.time +
//										  "<br>Расстояние до предыдущей точки : " + item.distance + "м." +
										  "<br>Средняя скорость : ~" +item.srSpeed + "км/ч"
								});
					    	circle.addListener('click', function(){
					    		infoWindow.setPosition(circle.getCenter());
					    		infoWindow.open(map, circle);});
						}
					    if (item.stat == "FINISHPOINT"){
							var infoWindow1 = new google.maps.InfoWindow({
								content : "<br> Последняя точка : " +
										  "<br>Дата : " + item.date
							});
							var marker = new google.maps.Marker({
					            position: {lat: item.latitude, lng: item.longitude},
					            map: map,
					            icon :  {url: "img/car.png",
					            	scaledSize: new google.maps.Size(35, 50)}
					          });
							marker.addListener('click', function(){
					    		infoWindow1.open(map, marker);});
							pointsOnWay.push(marker);
					    	
					    }
					    if (prevLat && prevLng)	{
						    lengthWay = lengthWay + latlng2distance(item.latitude, 
								          item.longitude, prevLat, prevLng);
						    prevLat = item.latitude;
						    prevLng = item.longitude;
					    }else{
						    prevLat = item.latitude;
						    prevLng = item.longitude;
					    }
						var markerPosition = new google.maps.LatLng(item.latitude, item.longitude);
						markersBounds.extend(markerPosition);
					});
					
					$.ajax({
						url : "blablabla",
						method : "POST",
						data : {idVehicle : codes} 
					}).done(function(data){
					var infoWindow1 = new google.maps.InfoWindow({
						content : "<br> Модель : " + data.model +
								  "<br> Рег. номер : " + data.regNumber +
								  "<br> Водитель : " + data.driverName +
								  "<br> Номер телефона : " + data.phoneNumber
					});
					var marker = new google.maps.Marker({
			            position: coordinatesAllWay[coordinatesAllWay.length-1],
			            map: map,
			            icon :  {url: "img/car.png",
			            	scaledSize: new google.maps.Size(35, 50)}
			          });
					marker.addListener('click', function(){
			    		infoWindow1.open(map, marker);});
					pointsOnWay.push(marker);
					});
					
					if (coordinatesAllWay.length != 0){
						if (mode!=1){
						  map.setCenter(markersBounds.getCenter(), map.fitBounds(markersBounds));
						  removePointsOnWay();
						}
					    var zoom = map.getZoom();
					    var val;
					    for(i=0; i<is_zoom.length; i++){
						    if (zoom == is_zoom[i][0]){
						    	val = is_zoom[i][1];
						    	break;
						    }
					    }
					    var masArrows = [];//
		                for(i=0; i<lengthWay/val; i++){
		            	  var s = {icon: {
			                              path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW
	                                     },
			                              offset: (100/(lengthWay/val)) * i + '%'};
		            	  masArrows.push(s);
		                }
						flightPath = new google.maps.Polyline({
							  path: coordinatesAllWay,
							  geodesic: true,
							  strokeColor: '#FF0000',
							  strokeOpacity: 1.0,
							  strokeWeight: 2,
							  icons: masArrows,
							  map: map
						});
						arrayForRoad.push(flightPath)
						mode = 1;
					}
				});
			}
		}
		
		function removeAllVehiclesObjects(){
			removeWayVehicle();
			removeVehicles();
			removePointsOnWay();
		}
		
	    function removeWayVehicle(){
			if (flightPath)
			    flightPath.setMap(null);
			for (i in arrayForRoad)
				if (arrayForRoad[i])
					arrayForRoad[i].setMap(null);
	    }
	    
	    function removeVehicles(){
			if (markerVehiclesList) 
	              for (i in markerVehiclesList)
	            	  markerVehiclesList[i].setMap(null);
	    }
	    
	    function removePointsOnWay(){
	    	if (pointsOnWay)
	    		for (i in pointsOnWay)
	    			pointsOnWay[i].setMap(null);
	    }
		//
		//Средство передвижения
		//
		function get_vehicles(codes){
			var markers = [];
			mode = 0;
			removePointsOnWay();
			removeWayVehicle();
			removeVehicles();
			markersBounds = new google.maps.LatLngBounds();
			$.ajax({url: "vehicles", method: "POST", dataType: "json", contentType: 'application/json', mimeType: 'application/json', data: "[" + codes + "]"})
			.done(function (data){
				if (data) {
					$('#vehicles').empty().append($('<option>', {value: "", text: " - Машины - "}))
					$.each(data, function (i, item){
						$('#vehicles').append($('<option>', {value: item.id, text: item.name + '('+ item.regNumber + ')'}));
						var infoWindow = new google.maps.InfoWindow({
							content : "<br>Марка : " + item.name +
									  "<br>Рег. номер : " + item.regNumber +
							          "<br>Водитель : " + item.driverName +
							          "<br>Номер телефона : " + item.phoneNumber
							});
						var uluru = {lat: item.lastPositionY, lng: item.lastPositionX};
						
						var markerPosition = new google.maps.LatLng(item.lastPositionY, item.lastPositionX);
						markersBounds.extend(markerPosition);
				        var marker = new google.maps.Marker({
				            position : uluru,
				            map : map,
				            icon :  {url : "img/car.png",
				            	scaledSize: new google.maps.Size(35, 50)}
				        });
				        markers.push(marker);
				        marker.addListener('click', function(){infoWindow.open(map, marker);});
				        markerVehiclesList.push(marker);
					});
					}
				map.setCenter(markersBounds.getCenter(), map.fitBounds(markersBounds)); 
			});
		}
		
		function get_bel_energo_companies() {  // ФЭС
			$.ajax({url: "bel_energo_companies", method: "GET", dataType: "json"})
					.done(function (data) {
						if (data) { $.each(data, function (i, item) { $('#bel_energo_companies').append($('<option>', {value: item.id, text: item.name})); });
						$('#bel_energo_companies option').eq(1)[0].selected = true;
						 $('#bel_energo_companies').change();
						}
					});
		}
		get_bel_energo_companies();
		$('#bel_energo_companies').on("change", function () {
			var codes = $('#bel_energo_companies').val();
			if (codes) {
				get_spr_data("air_lines", codes);
				get_sectors(codes);
			} 
		});
		//показать машину
		$('#vehicles').on("change", function(){
			var code = $('#vehicles').val();
			if (code){
				getVehicle(code);
			}
		});
		
		function getVehicle(code){
			mode = 0;
			removePointsOnWay();
			removeWayVehicle();
			removeVehicles();
			markersBounds = new google.maps.LatLngBounds();
			$.ajax({url:"getVehicle", method: "POST", dataType:"json", contentType: 'application/json', mimeType: 'applicarion/json', data: "[" + code + "]"})
					.done(function (data){
						if (data){
							$.each(data, function (i, item){
								var infoWindow = new google.maps.InfoWindow({
									content : "<br>Марка : " + item.name +
											  "<br>Рег. номер : " + item.regNumber +
									          "<br>Водитель : " + item.driverName +
									          "<br>Номер телефона : " + item.phoneNumber
									});
								var marker = new google.maps.Marker({
						            position: {lat: item.lastPositionY, lng: item.lastPositionX},
						            map: map,
						            icon :  {url: "img/car.png",
						            	scaledSize: new google.maps.Size(35, 50)}
						        });
						        marker.addListener('click', function(){infoWindow.open(map, marker);});
								markerVehiclesList.push(marker);
								var markerPosition = new google.maps.LatLng(item.lastPositionY, item.lastPositionX);
								markersBounds.extend(markerPosition);
								
							});
							map.setCenter(markersBounds.getCenter(), map.fitBounds(markersBounds)); 
						}
					});
		}
		
		// Участки
		function get_sectors(codes) {
			$.ajax({url: "sectors", method: "POST", dataType: "json", contentType: 'application/json', mimeType: 'application/json', data: "[" + codes + "]"})
					.done(function (data) {
						if (data) {
							$('#sectors').empty().append($('<option>', {value: "", text: " - УЧАСТКИ - "}));
							$.each(data, function (i, item) {
								$('#sectors').append($('<option>', {value: item.id, text: item.name}));
							});
						}
					});
		}

		$('#sectors').on("change", function () {
			var codes = $('#sectors').val();
			if (codes) {
				get_spr_data("air_lines10", codes);
			}
		});

		function get_spr_data(ob , top_spr_value) {

			$.ajax({
				url: ob,
				type: "POST",    //"GET", "POST"
				dataType: "json",
				data: "[" + top_spr_value + "]",
				contentType: 'application/json',
				mimeType: 'application/json',
				error: function (xhr, status, err) {
					ajax(err);
					console.log(err);
				},
				success: onSprReceived
			});

			function onSprReceived(series) {  // получение результа ajax

				var options = ""
				if($("#"+ob)[0].tagName.toLowerCase() == "select")
						options = "<option value=''>- Л И Н И И -</option>\n";

				//var sel = $("#"+ob);
				//var a = Date.now();
			//	$.each(series, function (i, item) {   //   Толин вариант красивый, но работает медленно !
			//		$("#"+ob).append($('<option>', {value: item.id, text: item.name}));
			//	});
			//	var b = Date.now();

				series.forEach(function (element) {
					if($("#"+ob)[0].tagName.toLowerCase() == "select")
						options += "<option value='" + element.id + "'>" + element.name + "</option>\n";
					else
						options += "<label class='fetchSeries' value='" + element.id + "'>" + element.name + "</label><br>\n";

				});
				$("#"+ob).html(options);
			//	var c = Date.now();
			//	console.log("Толин - " + (b-a));
			//	console.log("мой - " + (c-b));
			}
		}



		var bounds  = new google.maps.LatLngBounds();
		var latlng = new google.maps.LatLng(52.413354, 31.006106), // Координаты центра

				myOptions = {
					zoom:      12, // Масштаб
					maxZoom:   20,
					minZoom:   8,
					center:    latlng, // Координаты центра

					zoomControl: true,
					streetViewControl: false,
					scaleControl: true,
					rotateControl: true,
					fullscreenControl:true,
					navigationControlOptions: {
						style: google.maps.NavigationControlStyle.ZOOM_PAN //SMALL
					},

					mapTypeId: google.maps.MapTypeId.ROADMAP,  // Тип карты

					mapTypeControlOptions: {
						mapTypeIds: [

							google.maps.MapTypeId.ROADMAP,
							google.maps.MapTypeId.SATELLITE,
							google.maps.MapTypeId.HYBRID,
							google.maps.MapTypeId.TERRAIN,
							"OSM",
							"Yandex",
							"Yandex2"
						],
						style: google.maps.MapTypeControlStyle.DEFAULT   //DROPDOWN_MENU
					}
					//----------- стили ----------
					,styles: [
						{
							"featureType": "water",
							"stylers": [
								{ "color": "#80C8E5" },
								{ "saturation": 0 }
							]
						} /*,
						{
							"featureType": "road.arterial",
							"elementType": "geometry",
							"stylers": [
								{ "color": "#cc9999" }
							]
						}*/
					]
					//------------конец --------------
				};

		var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);



map.mapTypes.set("OSM", new google.maps.ImageMapType({
	getTileUrl: function(coord, zoom) {
		return "http://tile.openstreetmap.org/" + zoom + "/" + coord.x + "/" + coord.y + ".png";
	},
	tileSize: new google.maps.Size(256, 256),
	name: "OpenStreetMap",
	maxZoom: 18
}));



var yandexMapType = new google.maps.ImageMapType(
		{getTileUrl: function(coord, zoom) {
			return "http://vec0"+((coord.x+coord.y)%4+1)+".maps.yandex.net/tiles?l=map&v=2.16.0&x=" +
					coord.x + "&y=" + coord.y + "&z=" + zoom + "";
		},
			tileSize: new google.maps.Size(256, 256),
			isPng: true,
			alt: "Yandex",
			name: "Яндекс карта",
			maxZoom: 18});
// projection is ignored if passed to MapTypeOptions
yandexMapType.projection = new YandexProjection();
map.mapTypes.set("Yandex", yandexMapType);

var yandexMapType2 = new google.maps.ImageMapType(
		{getTileUrl: function(coord, zoom) {
			return "http://sat0"+((coord.x+coord.y)%4+1)+".maps.yandex.net/tiles?l=sat&v=3.313.0&x=" +
					coord.x + "&y=" + coord.y + "&z=" + zoom + "";
		},
			tileSize: new google.maps.Size(256, 256),
			isPng: true,
			alt: "Yandex",
			name: "Яндекс спутник",
			maxZoom: 19});
// projection is ignored if passed to MapTypeOptions
yandexMapType2.projection = new YandexProjection();
map.mapTypes.set("Yandex2", yandexMapType2);

//map.setOptions({mapTypeControlOptions: {mapTypeIds: [google.maps.MapTypeId.ROADMAP, "Yandex"]} });

function YandexProjection() {
	this.pixelOrigin_ = new google.maps.Point(128,128);
	var MERCATOR_RANGE = 256;
	this.pixelsPerLonDegree_ = MERCATOR_RANGE / 360;
	this.pixelsPerLonRadian_ = MERCATOR_RANGE / (2 * Math.PI);

	this.fromLatLngToPoint = function(latLng) {
		function atanh(x) {
			return 0.5*Math.log((1+x)/(1-x));
		}
		function degreesToRadians(deg) {
			return deg * (Math.PI / 180);
		}
		function bound(value, opt_min, opt_max) {
			if (opt_min != null) value = Math.max(value, opt_min);
			if (opt_max != null) value = Math.min(value, opt_max);
			return value;
		}

		var origin = this.pixelOrigin_;
		var exct = 0.0818197;
		var z = Math.sin(latLng.lat()/180*Math.PI);
		return new google.maps.Point(origin.x + latLng.lng() *this.pixelsPerLonDegree_,
				Math.abs(origin.y - this.pixelsPerLonRadian_*(atanh(z)-exct*atanh(exct*z))));
	};

	this.fromPointToLatLng = function(point) {
		var origin = this.pixelOrigin_;
		var lng = (point.x - origin.x) / this.pixelsPerLonDegree_;
		var latRadians = (point.y - origin.y) / -this.pixelsPerLonRadian_;
		var lat = Math.abs((2*Math.atan(Math.exp(latRadians))-Math.PI/2)*180/Math.PI);
		var Zu = lat/(180/Math.PI);
		var Zum1 = Zu+1;
		var exct = 0.0818197;
		var yy = -Math.abs(((point.y)-128));
		while (Math.abs(Zum1-Zu)>0.0000001){
			Zum1 = Zu;
			Zu = Math.asin(1-((1+Math.sin(Zum1))*Math.pow(1-exct*Math.sin(Zum1),exct))
			/ (Math.exp((2*yy)/-(256/(2*Math.PI)))*Math.pow(1+exct*Math.sin(Zum1),exct)));
		}
		if (point.y>256/2) {
			lat=-Zu*180/Math.PI;
		} else {
			lat=Zu*180/Math.PI;
		}
		return new google.maps.LatLng(lat, lng);
	};

	return this;
}



		function get_media_urls(id){   	//id = 84279;   //81059;

			$.ajax({
				url: "pillar/check_data",
				type: "POST",    //"GET", "POST"
				dataType: "json",
				data: "[" + id + "]",
				contentType: 'application/json',
				mimeType: 'application/json',
				error: function (xhr, status, err) {
					alert(err);
					console.log(err);
				},
				success: onReceived
			});

			function onReceived(series) {// получение результа ajax
				if( series.length == 0)return;
				var json_ob = series[0].dataExistence;
				if( json_ob == undefined)return;
				var rez="";
				for (var key in json_ob) {
					if (json_ob[key] != "" )
						switch (key.substr(0,5)) {
							case "voice":
								rez += 	"<audio  controls><source src='" + json_ob[key].substr(1) + ".wav' type=\"audio/wav\"></audio><br><br>";
								break;

							case "photo":
								rez += 	"<img src=\"" + json_ob[key].substr(1)+ ".jpeg\" height=\"200\">";
							//	rez += 	"<video controls  width=\"300\" height=\"200\">	<source src='https://www.w3schools.com/html/mov_bbb.mp4' type='video/mp4' /></video><br><br>";
								break;
							case "image":
								rez += 	"<img src=\"" + json_ob[key].substr(1)+ ".jpeg\" height=\"200\">";
								//	rez += 	"<video controls  width=\"300\" height=\"200\">	<source src='https://www.w3schools.com/html/mov_bbb.mp4' type='video/mp4' /></video><br><br>";
								break;

							case "video":
								rez += 	"<video controls  width=\"300\" height=\"200\">	<source src='" + json_ob[key].substr(1) + ".avi' type='video/avi' /></video><br><br>";

							case "schem":
								rez += 	"<br><a href='"+ json_ob[key].substr(1) + ".vsd'>Схема </a>";
						}
				}
				$("#media").html(rez);
			}
		};

		var infowindow = new google.maps.InfoWindow( );


		$("#air_lines").change(function () {

			var maps_nom = $(this).val();
			create_line(maps_nom, "coords_trunklines");
		})

		$("#air_lines10").change(function () {

			var maps_nom = $(this).val();
			create_line(maps_nom, "coords_airlines10");
		})

		function create_line(maps_nom,serv_url){
			if(APP_data.lines[maps_nom] != undefined){
				select_big_line(maps_nom);
				big_zoom(maps_nom);
				return;
			}

				if(maps_nom != "")
					$.ajax({
						url: serv_url,
						type: "POST",    //"GET", "POST"
						dataType: "json",
						data: "[" + maps_nom + "]",
							contentType: 'application/json',
							mimeType: 'application/json',
						error: function (xhr, status, err) {
							alert(err);
							console.log(err);
						},
						success: onDataReceived
					});

			//    =====    О т р и с о в к а       л и н и и    ========

				function onDataReceived(series) {  // получение результа ajax  это все по линии

					var old_line = false;
					series.forEach( function( element ) {

						if(old_line)return;

						cur_idLine = element.idLine;

						if(Lines.indexOf(element.code) != -1) {

							if(set_LEP(element) == false) {
								big_zoom(element.idLine );
								old_line = true;
								return; // добавить лэп
							}
						}
						else
						if(Spans.indexOf(element.code) != -1) {
							set_polylines(element); // добавить пролет
						}
						else
						if(Pillars.indexOf(element.code) != -1) {
							set_pillars(element);	// добавить опору
						}
						else
						if(Substations.indexOf(element.code) != -1) {
							set_substations(element);	// добавить опору
						}
					})


					if(old_line)return;

					cur_line_obj["polylineList"] = polylineList;
					cur_line_obj.distance = get_line_length(polylineList);

					cur_line_obj["markerList"] = markerList;

					select_big_line(APP_data.lines[cur_idLine].idLineParent);

					$("#lines").html("Всего пролетов:" + lines);
					$("#marks").html("Всего опор:" + marks);

					map.setCenter(bounds.getCenter());
					map.fitBounds(bounds);
					map.panToBounds(bounds);
		  };
		};


	function set_substations(element) {   //    =====    П о д с т а н ц и и    ========
		var id = element.id;
		if (APP_data.substations[id] == undefined) {
			APP_data.substations[id] = {name: element.name, id: element.id, idLine: element.idLine};

			//var bounds = [[item.latitude1, item.longitude1], [item.latitude2, item.longitude2]];

			var rectangle = new google.maps.Rectangle({
				strokeColor: '#00FF00',
				strokeOpacity: 0.8,
				strokeWeight: 2,
				fillColor: '#00FF00',
				fillOpacity: 0.15,
				map: map,
				bounds: {
					north: element.latitude1,
					south: element.latitude2,
					west:  element.longitude1,
					east:  element.longitude2
				}
			})
			rectangle.addListener('click', function() {
				infowindow.setContent('<h4>Подстанция ' + element.name + '</h4>' +
				'<br> координаты : ' + element.latitude1 + " x " + element.longitude1 + '<hr><div id="media">'
				+'</div><hr><a href="javascript:small_zoom();">Показать все линии </a>');
				get_media_urls(element.id);

				var Center = rectangle.getBounds().getCenter();
				infowindow.setPosition(Center);
				infowindow.open(map);
			});
			APP_data.substations[id].rectangle = rectangle;
		};
	}

	function set_LEP(element){   //    =====    Л и н и и   ========
		var id = element.idLine;

		if(APP_data.lines[id] == undefined) {

			if(polylineList.length > 0) {
				cur_line_obj["polylineList"] = polylineList;
				cur_line_obj.distance = get_line_length(polylineList);
				polylineList = [];
			}
			if(markerList.length > 0) {
				cur_line_obj["markerList"] = markerList;
				markerList = [];
			}

			APP_data.lines[id] = {name: element.name, id: element.id, idLine:element.idLine,
				idLineParent: element.idLineParent,selected: false};
			cur_line_obj = APP_data.lines[id];

			var point = new google.maps.LatLng(element.latitude1, element.longitude1);
			cur_line_obj["bounds_point1"] = point;
			bounds.extend(point);

			point = new google.maps.LatLng(element.latitude2, element.longitude2);
			cur_line_obj["bounds_point2"] = point;
			bounds.extend(point);

			return true;
		}
		else
			return false;

	};

	function set_pillars(element) {  			//    =====    О п о р ы    ========
			marks ++;
			var marker = new google.maps.Marker({
				position: new google.maps.LatLng(element.latitude1, element.longitude1),
				//	map: map,
				title: "Опора ЛЭП " + element.name,
				icon: {
					url: (element.code == Pillar10 ? "img/lep10.png" :"img/lep.jpg"),
					//	anchor: new google.maps.Point(10,10),
					scaledSize: (element.code == Pillar10 ? new google.maps.Size(7, 11) : new google.maps.Size(10, 15))
				}
			});

		  //  marker.addListener('click', marker_click.bind(null, element, this));
			marker.addListener('click', function() {
				infowindow.setContent('<h5> ЛЭП ' + APP_data.lines[element.idLine].name + '</h5><h5>Опора ЛЭП ' + element.name + '</h5> ' +
				'<br> координаты опоры: ' + element.latitude1 + " x " + element.longitude1 + '<hr><div id="media">'
					//	 +'<img src="img/lep2.jpg" title="Опора ЛЭП" style="border: 0px none;" height="1" border="0" >'
				+'</div><hr><a href="javascript:small_zoom();">Показать все линии </a>');
				get_media_urls(element.id);
				infowindow.open(map, marker);
			});
			markerList.push( marker);
	}


	function set_polylines(element) {  //  ====   П р о л е т ы   ========

			lines++;
			var polylineCoords = [];

			var point = new google.maps.LatLng(element.latitude1, element.longitude1);
			polylineCoords.push(point);
			var point1 = new google.maps.LatLng(element.latitude2, element.longitude2);
			polylineCoords.push(point1);

			var polyline = new google.maps.Polyline({
				path: polylineCoords, // Координаты
				strokeColor: def_color, // Цвет
				strokeOpacity: 0.8, // Прозрачность
				//title: "Пролет ЛЭП",
				strokeWeight: 3 // Ширина
			});
			polyline.idLine = element.idLine;
			polyline.distance =  getDistance(point, point1);
			polyline.setMap(map);

			google.maps.event.addListener(polyline, 'click', function (event) {
				closeclick();
				if(map.getZoom() < main_zoom +1) {

					infowindow.setContent('<h5> ЛЭП ' + APP_data.lines[this.idLine].name + '</h5>' +
					'  Длина линии: ' + APP_data.lines[this.idLine].distance + ' км.' +
					' <hr><br><a href="javascript:big_zoom(' + this.idLine +  ');">Показать только эту линию</a>' +
					' <br><br><a href="javascript:big_zoom2(' + this.idLine + ');">Показать опоры</a>');

					select_current_line(element.idLine);

				//	APP_data.lines[this.idLine].selected = APP_data.lines[this.idLine].selected == true ? false : true;
				//	select_all_line(APP_data.lines[this.idLine].polylineList, APP_data.lines[this.idLine].selected);

				}else {
						infowindow.setContent('<h5> ЛЭП ' + APP_data.lines[this.idLine].name + '</h5>' +
						' Длина линии: ' + APP_data.lines[this.idLine].distance + ' км.' +
						'<h5>пролет ЛЭП </h5> '  +
						' Координаты пролета: <br><div style="padding-left: 30px;">' + element.latitude1 + ' x ' + element.longitude1 +
						' <br>' + element.latitude2 + ' x ' + element.longitude2 +
						'</div><br> Длина пролета  : ' + this.distance + ' м.' +
						'<hr><a href="javascript:small_zoom();">Показать все линии </a>');

				//		cur_line = this;
				//	if(cur_line.strokeColor ==  def_color)
				//		cur_line.setOptions({strokeColor: selected});
				//	else
				//		cur_line.setOptions({strokeColor: def_color});

					select_span(this);
				}
				cur_latLng = event.latLng;
				infowindow.setPosition(event.latLng);
				infowindow.open(map);
			});

			polylineList.push(polyline);
	}

		function select_span(span){
			var lineList =  APP_data.lines[span.idLine].polylineList;
			select_all_line(lineList,APP_data.lines[span.idLine].selected);

			cur_line = span;

			if(APP_data.lines[span.idLine].selected == false)
				span.setOptions({strokeColor: selected});
			else
				span.setOptions({strokeColor: def_color});
		}

		function select_current_line(idLine) {    				 //подсветить выбранную линию по клику
			var current_line_in_APP= APP_data.lines[idLine];
			$.each(APP_data.lines, function (i, item) {
				if (item.selected){
					APP_data.lines[item.idLine].selected = APP_data.lines[item.idLine].selected = false;
					select_all_line(APP_data.lines[item.idLine].polylineList,false);
				}
			});
			APP_data.lines[idLine].selected = APP_data.lines[idLine].selected = true;
			select_all_line(APP_data.lines[idLine].polylineList,true);
		}

		function select_big_line(idLine) {    					 //подсветить линию и отпайки
			var current_line_in_APP= APP_data.lines[idLine];
			$.each(APP_data.lines, function (i, item) {
				if (item.selected){
					APP_data.lines[item.idLine].selected = false;
					select_all_line(APP_data.lines[item.idLine].polylineList,false);
				}
				if(APP_data.lines[item.idLine].idLineParent == idLine){
					APP_data.lines[item.idLine].selected = true;
					select_all_line(APP_data.lines[item.idLine].polylineList,true);
				}
			});
		}


		function select_all_line(lineList,select) {  			   //подсветить  все поли-линии
			if(lineList == undefined) return;
			lineList.forEach(function (element) {
				if (select)
					element.setOptions({strokeColor: selected});
				else
					element.setOptions({strokeColor: def_color});
			})
		}

		var big_zoom = function(idLine ){		//     увеличить только эту линию
			var line_in_APP= APP_data.lines[idLine];
			var bound = new google.maps.LatLngBounds();
			bound.extend(line_in_APP.bounds_point1);
			bound.extend(line_in_APP.bounds_point2);

			infowindow.close();
			map.setCenter(bound.getCenter());
			map.fitBounds(bound);
			map.panToBounds(bound);
		}

		var big_zoom2 =  function(idLine){//  показать опоры
			infowindow.close();
			map.setCenter(cur_latLng);
			map.setZoom(main_zoom + 1);

		}


		var small_zoom = function(){		//    показать все обьекты
		if($.isEmptyObject(APP_data.lines) ){
			return;
		}
			var bound = new google.maps.LatLngBounds();
			for (var idLine in APP_data.lines) {

				var line_in_APP= APP_data.lines[idLine];

				bound.extend(line_in_APP.bounds_point1);
				bound.extend(line_in_APP.bounds_point2);
			}
			infowindow.close();
			map.setCenter(bound.getCenter());
			map.fitBounds(bound);
			map.panToBounds(bound);
		}

		google.maps.event.addListener(map, 'zoom_changed', function() {   // Z o o m

			document.getElementById("zoom").innerHTML = "Увеличение: " + map.getZoom();
			if (mode == 1){
				drawWay();
			}
			if( map.getZoom() > main_zoom && !painted){

				painted = true;
				for (var idLine in APP_data.lines) {
					var line_in_APP= APP_data.lines[idLine];
					line_in_APP.markerList.forEach( function( element ){
						element.setMap(map);
					})
				}
			}
			else
			if( map.getZoom() <= main_zoom && painted){
				painted = false;
				for (var idLine in APP_data.lines) {
					var line_in_APP = APP_data.lines[idLine];
					line_in_APP.markerList.forEach(function (element) {
						element.setMap(null);
					})
				}
			}
		});

		function delete_line(idLine) {
			var line_in_APP = APP_data.lines[idLine];
			line_in_APP.markerList.forEach(function (element) {
				element.setMap(null);
			})
			//delete line_in_APP.markerList ;

			line_in_APP.polylineList.forEach(function (element) {
					element.setMap(null);
			})
			//delete line_in_APP.polylineList;
			delete APP_data.lines[idLine];
		}

		function clear_map() {
			for (var idLine in APP_data.lines) {
				delete_line(idLine);
			}
			for (var subst in APP_data.substations) {
				APP_data.substations[subst].rectangle.setMap(null);
				delete APP_data.substations[subst];
			}
			$(".sel").removeClass("sel");
			bounds  = new google.maps.LatLngBounds();
		}
		$(".glyphicon-erase").click( function() {
			clear_map();
		})

		google.maps.event.addListener(infowindow,'closeclick',closeclick); // закрыть infowindow

		function closeclick() {
			if (cur_line)
			if(cur_line.strokeColor ==  def_color)
				cur_line.setOptions({strokeColor: selected});
			else
				cur_line.setOptions({strokeColor: def_color});
		}

		google.maps.event.addListener(map, "rightclick",function(event){small_zoom()});

		google.maps.event.addListener(map, 'tilesloaded', function(event) {
		//	$(".gmnoprint").not(':eq(2)').html("");
		//	$(".gm-style-cc").eq(1).html("");
		//	$('div').filter(function() {return $(this).css('z-index') == '1000000'; }).html("Alex_Ant КБСП карты");
			$(".gm-style-mtc").eq(0).children().eq(0).html("Google карта");
			$(".gm-style-mtc").eq(1).children().eq(0).html("Google спутник");
		});

		$( "#air_lines" ).on( "click", "label", function(e) {

				var maps_nom = $(this).attr("value");
				create_line(maps_nom, "coords_trunklines");
				$(this).addClass("sel");
		});

		$( "#air_lines10" ).on( "click", "label", function(e) {

			var maps_nom = $(this).attr("value");
			create_line(maps_nom, "coords_airlines10");
			$(this).addClass("sel");
		});


function get_line_length(polylineList){
    var len=0;
	polylineList.forEach(function (element) {
		if(element.distance != undefined)
			len = len * 1 + element.distance * 1;
	})
	return (len/1000).toFixed(3);
}


var rad =function(x)
{return x *Math.PI /180;};

var getDistance =function(p1, p2){
		var R =6378137;	// Earth’s mean radius in metervar
	 dLat = rad(p2.lat()- p1.lat());
	 var dLong = rad(p2.lng()- p1.lng());
	 var a =Math.sin(dLat /2)*Math.sin(dLat /2)+Math.cos(rad(p1.lat()))*Math.cos(rad(p2.lat()))*Math.sin(dLong /2)*Math.sin(dLong /2);
	 var c =2*Math.atan2(Math.sqrt(a),Math.sqrt(1- a));
	 var d = R * c;
	 return d.toFixed(2);// returns the distance in meter
	};




//}

</script>
</body>
</html>
