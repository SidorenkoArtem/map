 	// ФЭС
	//
	function get_bel_energo_companies() {
		$.ajax({url: "../bel_energo_companies", method: "GET", dataType: "json"})
			.done(function (data) {
				if (data) { $.each(data, function (i, item) { $('#bel_energo_companies').append($('<option>', {value: item.id, text: item.name})); }); }
			});
	}
	//
	get_bel_energo_companies();
	//
	$('#bel_energo_companies').on("change", function () {
		var codes = $('#bel_energo_companies').val();
		if (codes) {
			get_air_lines(codes);
			get_sectors(codes);
		}
	});
	//
	// Магистральные линии
	//
	function get_air_lines(codes) {
		$.ajax({url: "../air_lines", method: "POST", dataType: "json", contentType: 'application/json', mimeType: 'application/json', data: "[" + codes + "]"})
			.done(function (data) {
				if (data) {
					$('#air_lines').empty().append($('<option>', {value: "", text: " - ЛИНИИ - "}));
					//
					$.each(data, function (i, item) {
						$('#air_lines').append($('<option>', {value: item.id, text: item.name}));
					});
				}
			});
	}
	//
	$('#air_lines').on("change", function(){show_air_lines();});
	//
	function show_air_lines(showall) {

		var codes = "";
		if (showall) {
			console.log("show_air_lines ALL");
			$("#air_lines").find("> option").each(function () {if (this.value) {codes += "," + this.value}});
			codes = codes.substring(1);
		}
		else {
			console.log("show_air_lines ONE");
			codes = $('#air_lines').val()
		}
		//
		if (codes) {get_coords_trunklines("air",codes);}
		else {alert("Пожалуйста выберите чего-нибудь ...");}
	}
	//
	//
	// Воздушные линии
	//
	function get_sectors(codes) {
		$.ajax({url: "../sectors", method: "POST", dataType: "json", contentType: 'application/json', mimeType: 'application/json', data: "[" + codes + "]"})
			.done(function (data) {
				if (data) {
					$('#sectors').empty().append($('<option>', {value: "", text: " - УЧАСТКИ - "}));
					//
					$.each(data, function (i, item) {
						$('#sectors').append($('<option>', {value: item.id, text: item.name}));
					});
				}
			});
	}
	//
	$('#sectors').on("change", function () {
		var codes = $('#sectors').val();
		if (codes) {
			get_air_lines10(codes);
		}
	});
	//
	function get_air_lines10(codes) {
		$.ajax({url: "../air_lines10", method: "POST", dataType: "json", contentType: 'application/json', mimeType: 'application/json', data: "[" + codes + "]"})
			.done(function (data) {
				if (data) {
					$('#air_lines10').empty().append($('<option>', {value: "", text: " - ЛИНИИ - "}));
					//
					$.each(data, function (i, item) {
						$('#air_lines10').append($('<option>', {value: item.id, text: item.name}));
					});
				}
			});
	}

	$('#air_lines10').on("change", function () {
		var codes = $('#air_lines10').val();
		if (codes) {get_coords_trunklines("air10",codes);}
		else {alert("Пожалуйста выберите чего-нибудь ...");}
	});

	var rad =function(x)
	{return x *Math.PI /180;};
	

	var getDistance = function (p1, p2) {
		var R = 6378137;	// Earth’s mean radius in metervar
		var dLat = rad(p2[0] - p1[0]);
		var dLong = rad(p2[1] - p1[1]);
		var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(p1[0])) * Math.cos(rad(p2[0])) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		var d = R * c;
		return d.toFixed(2);// returns the distance in meter
	};
	
