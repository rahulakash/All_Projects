//Author: RahulAkash
//email: rahulakash.nethaji@mavs.uta.edu
var map = "";
var markers = [];
var addr;
var geocoder;
var mymarker=[];


function initialize() {
  geocoder= new google.maps.Geocoder();
  var mapOptions = {
    zoom: 14
  };
  map = new google.maps.Map(document.getElementById('Gmap'),
      mapOptions);

  // Try HTML5 geolocation
  if(navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
    var pos = new google.maps.LatLng(position.coords.latitude,
                                       position.coords.longitude);
    map.setCenter(pos);
    //alert(pos);
    geocoder.geocode({'latLng': pos}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        if(results[0]) {
          add = results[0].formatted_address;
          document.getElementById('location').innerHTML = "<center><b>Your Location: </b><i>"+add+"</i></center><br/>";
          marker = new google.maps.Marker({
              map: map,
              position:pos,
              animation: google.maps.Animation.DROP,
              title: add,
              icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=U|FF0000|000000'
          });
        mymarker.push(marker);
        }
      }
    });
    },function() {
      handleNoGeolocation(true);
    });
  } 
  else {
    // Browser doesn't support Geolocation
    handleNoGeolocation(false);
  }
  google.maps.event.addListener(map,'bounds_changed',function(){
    //sendRequest();
   });
}

function loadScript(){
  var script = document.createElement('script');
  script.type = 'text/javascript';
  script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&' +
'callback=initialize';
  document.body.appendChild(script);
  
}


// Function to display the list of restaurants within the map boundry.
function sendRequest () {
  var shift="";
   var detail = "<h1><center>TOP 10 RESTAURANTS</center></h1>";
   var addresses = [];
   var name = [];
   var mapBounds = map.getBounds(); 
   var sw = mapBounds.getSouthWest();
   var ne = mapBounds.getNorthEast();
   var location = sw.lat()+","+sw.lng()+"|"+ne.lat()+","+ne.lng();
   var xhr = new XMLHttpRequest();
   var searchTerm = document.getElementById('form-input');
  
   // Remove all the markers from map.
   for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
   markers = [];
   
   xhr.open("GET", "proxy.php?term="+searchTerm.value+"&bounds="+location+"&limit=10");
   xhr.setRequestHeader("Accept","application/json");
   xhr.onreadystatechange = function () {
       if (this.readyState == 4) {
          var json = JSON.parse(this.responseText);
          var str = JSON.stringify(json,undefined,2);
	
	if(json.businesses.length == 0)
	{
	document.getElementById('output').innerHTML =	"<h2>No "+searchTerm.value+" food place in this region. Please try a different search or drag the map to a different region</h2>";
	}
	else{
	  for (var i = 0 ; i< json.businesses.length; i++){
    var address ="";
    address = json.businesses[i].location.address+", "+json.businesses[i].location.city+", "+json.businesses[i].location.state_code+" "+json.businesses[i].location.postal_code;
    addresses.push(address);
    name.push(json.businesses[i].name);
    var k = i+1;
    var n=json.businesses[i].name.replace(/'/g,'');
     var e=n.replace(/&/g,'and');
    var z=addresses[i].replace(/'/g,'');
    var y=z.replace(/&/g,'and');
    var c=add.replace(/&/g,'and');
    var w=c.replace(/&/g,'');

    detail+= "<table><tr class='restaurant'><td><b>"+k+".</b></td><td><img style='float:left; margin-top:10px; width:100px;' src="+json.businesses[i].image_url+" alt='IMAGE NOT FOUND' /></td>";
    detail+= "<td title='Click on "+e+" to proceed...'><div><br/><b>Name: </b><a href="+json.businesses[i].url+">"+json.businesses[i].name+"</a><br>";
    detail+= "<br/><b>Rating: </b><img src="+json.businesses[i].rating_img_url+" />";
    detail+= "<br/><b>Customer Review: </b>"+json.businesses[i].snippet_text+"</div></td></tr></table>";
	  }
    document.getElementById("output").innerHTML = detail;
	  // Add markers to the map. 
	  addMarker(addresses,name);
       }
}
   };
   xhr.send(null);
}

// Function to add markers to the map.
function addMarker(addresses,name){
    var j = 0;
    var geocoder = new google.maps.Geocoder();
	// Convert addresses to latlng.
	for( var i = 0; i< addresses.length; i++){
		geocoder.geocode( {'address': addresses[i]}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                var k= j+1;
		// Add markers with corresponding addresses. 
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location,
			        animation: google.maps.Animation.DROP,
			        title: k+". "+ name[j]+"@ "+addresses[j],
			        icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld='+k+'|FAC741|000000'
                 });
		         j++;
		      markers.push(marker);
             }
            else{
                       // alert("Geocode was not successful for the following reason: " + status);
             }
        });
	}
	
	for (var j = 0; j < markers.length; j++) {	
   		 markers[j].setMap(map);
  	}
}


$(document).keypress(function(e) {
    if(e.which == 13) { 
        document.getElementById("find").click();
        e.preventDefault();
    }
});

function searchsetting()
{
   var search= document.getElementById("form-input");
   if(search.value=="")
   {
        search.placeholder= "  Enter your food type here ...";
   }
        search.size= 30;

}

