function check() {
	$.ajax({
		url: "TestController",
		type: "GET",
		data: { value: "41232" },
		dataType: "json",
	}).done((responseJson) => {
		 $.each(responseJson, function(key, value) { 
            alert(key + " = " + value);
        }); 
	}).fail((xhr, status, errorThrown) => {
		alert(xhr + "\n" + status + "\n" + errorThrown);
	});
}