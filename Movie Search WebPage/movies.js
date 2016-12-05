function initialize () {
    sendRequest();
}

function sendRequest () {
   var xhr = new XMLHttpRequest();
   var query = encodeURI(document.getElementById("form-input").value);
   xhr.open("GET", "proxy.php?method=/3/search/movie&query=" + query);
   xhr.setRequestHeader("Accept","application/json");
   xhr.onreadystatechange = function () {
       if (this.readyState == 4) {
          var json = JSON.parse(this.responseText);
          var str = JSON.stringify(json,undefined,2);
          var movie="";
          //getting the id,movie title and release-date.
          for( var i=0;i<json.results.length;i++)
          {
           var id=json.results[i].id;
           var name= json.results[i].title+"--"+ json.results[i].release_date.split('-')[0];
           movie+="<a  href='#' style='text-decoration:none' onclick='getMovieInfo("+id+");'>" + name + "</a><br/>";
          }
          //displaying the movie list on the window
          document.getElementById("output").innerHTML =movie;
          document.getElementById("output").style.display="block";
         
       }
   };
   xhr.send(null);
}

function getMovieInfo(id)
{
	var id=id;
	//alert(id);
	var hr=new XMLHttpRequest();
	hr.open("GET", "proxy.php?method=/3/movie/"+id);
   	hr.setRequestHeader("Accept","application/json");
   	hr.onreadystatechange = function () {
       	if (this.readyState == 4)
       {
           var data = JSON.parse(hr.responseText);
           var str = JSON.stringify(data,undefined,2);
           var title=data.title;
           image="http://image.tmdb.org/t/p/w185"+data.poster_path;
           var overview=data.overview;
           var genre="";
	   for( var i=0;i<data.genres.length;i++)
             {
                var gen=data.genres[i].name;
                genre+= gen+",";
            
             }
	   var crew="";
           var xhr=new XMLHttpRequest();
           xhr.open("GET", "proxy.php?method=/3/movie/"+id+"/credits");
           xhr.setRequestHeader("Accept","application/json");
           xhr.onreadystatechange = function () {
            if (this.readyState == 4)
             {
                        var res1 = JSON.parse(this.responseText);
	             		for(var j=0;j<res1.cast.length;j++)
                               { 
                                
                                crew = crew + res1.cast[j].name;
                                crew = crew + ",";
                                if(j==4)
                                {
                                   break;
                                }
                               }
                   
                       }
            var fullist="<img style=border-radius:8px src="+ image + " height='350' width='300'/><br/><li><b>Title:</b>"+title+"</li><li><b>Genre:</b>"+ genre +"</li><li><b>Summary:</b>"+overview+"</li><li><b>Cast:</b>"+crew+"</li>";
            // displaying the movie details   
           document.getElementById("displaywin").innerHTML =fullist;
           document.getElementById("displaywin").style.display="block";
     };
   xhr.send(null);
   }
 };
hr.send(null);                                       
}
