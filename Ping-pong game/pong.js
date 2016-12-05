

//initializing global variables
var x=0;
var y=-50;
var xoffset=5;
var yoffset=5;
var max_x=1060;
var max_y=500;
var time;
var timer=30;
var score=0;

var flag;

function initialize()
{
  var position=0;
}


function move()
{
  clearTimeout(time);
  animateBall();  
}

// function to reset score
function resetCounter()
{
    score=0;
    document.getElementById("message").innerHTML=score;
       
}

//function for padle movement
function movePaddle(event)
{
	
    //move paddle up and down.. following mouse
	if (event.clientY <= 50)
	{
		position = 0;
	}
	else if (event.clientY >= 500)
	{
		position = 500;
	}
	else
	{
		position = event.clientY -50;
	}
	document.getElementById("paddle").style.top = position +'px';
}

//function startGame(event) called when the mouse is clicked on the court
function startGame(event)
{
   flag=0;
   animateBall();
}

//function to animate the ball
function animateBall()
{
    
    if (flag==0)
    {
    x = x + xoffset;
	y = y + yoffset;
    //alert(x);
    
	document.getElementById("ball").style.top = y+'px';
	document.getElementById("ball").style.left = x+'px';
    var ballElement=document.getElementById("ball");
    var paddleElement=document.getElementById("paddle");
    var ballpos= ballElement.getBoundingClientRect();
    var paddlepos= paddleElement.getBoundingClientRect();
    
    if(ballpos.right>1060)
    {
        //paddle hit condition
        if ((ballpos.top>=paddlepos.top) && (ballpos.top<=paddlepos.bottom))
        {
          xoffset =-1*xoffset; 
        }
         
    }
    
    //to bounce back on court borders- x-axis 
    if ((x < 0)||(x > max_x)) 
    {
        //before left border and after right border
        //alert("inside max xxxx");
       if(x > max_x)
        {
            //alert("inside max");  
           score++;
           document.getElementById("message").innerHTML=score;
           startAgain();
             
        }
       xoffset =-1*xoffset; 
    }
    
    //to bounce back on court borders- y-axis
	if ((y > max_y) || (y < -50))
    {
	   yoffset =-1*yoffset;
	}
	
    time=setTimeout("move()",timer);
	//alert(time);
    }
}

//function to set the speed values to slow,medium and fast.
function setSpeed(value)
{    
   if(value==0) //slow
   {
       timer=30;
   }
   if(value==1) //medium
    {
        timer=20;
    }
   if(value==2) //fast
    {
        timer=10;
    } 
    
}

function startAgain()
{
     
     flag=1;
     //alert("Score  :" +score);
     x=0;
     y=(Math.random())*400; 
     setSpeed(0);
     document.getElementById("slow").checked=true;
     document.getElementById("ball").style.top = y+'px';
     document.getElementById("ball").style.left = x+'px';         
     clearTimeout(time);
             
}
