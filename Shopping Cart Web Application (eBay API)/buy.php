<?php
session_start();
?>
<!--Author:RahulAkash 
Email: rahulakash.nethaji@mavs.uta.edu-->
<html>
<head><title>Buy Products</title></head>
<body bgcolor="#303030">
<p style="color:#FFFFFF">
Shopping Basket:"
</p>
<?php
$sum=0.0;
session_start();
if(isset($_SESSION['viewxml'])){
   $id =array();
   $cost=$_SESSION['price'];
   $newxml= new SimpleXMLElement($_SESSION['viewxml']);
   $buy=$_GET['buy'];?>
   <table border-width="500px" cellspacing="0" border="1" bgcolor="#C0C0C0" border-style="groove" border-color="black">
 <?php foreach($newxml->categories->category as $category){
      foreach($category->items->product as $product){
        if($product['id']==$buy){
        $tableval= '<tr><td><a href="'.$product->productOffersURL.'"><img src="'.$product->images->image->sourceURL.'"/></a></td><td>'.$product->name->asXML().'</td><td>'.$product->minPrice->asXML().'$</td><td><a href="buy.php?delete='.$buy.'">delete</a></td></tr>';
           $_SESSION['cart']["$buy"]=$tableval;
            $min=$product->minPrice;
            $cost[$buy]=(string)$min;
            $_SESSION['price']=$cost;
          }
       	}
   }
 
    $price= $_SESSION['price'];
    foreach($price as $k=>$v)
	{
	 $sum=$sum+$v;
	}
     $_SESSION['sum']=$sum;
     
}
   

if((isset($_GET['delete']))&&(isset($_SESSION['viewxml'])))
{
$delete=$_GET['delete'];  
foreach($_SESSION['cart'] as $c=>$d)
  if($c==$delete)
    unset($_SESSION['cart']["$delete"]);
    $price=$_SESSION['price'];
    unset($_SESSION['price']["$delete"]);
    $val = $price[$delete];
    $sum=$sum-$val;
    $_SESSION['sum']=$sum;
  
}


if(isset($_GET['clear']))
{
  unset($_SESSION['cart']);
  unset($_SESSION['price']);
  $_SESSION['sum']=0;
}


foreach($_SESSION['cart'] as $a=>$b)
{
   echo $b;
}

?></table>
<p style="color:#FFFFFF">
Total:<?php echo $_SESSION['sum'] ?>$</p>
<p></p>
<form action='buy.php' method="GET">
<input type="hidden" name="clear" value="1"/>
<input type="submit" name="submit1" value="EmptyBasket"/></form>
<form method="GET" action='buy.php' style="margin-top:20px">
<fieldset><legend style="color:#FFFFFF" > Find Products:</legend><br/>
<input type="text" name="text1"/>
<input type="submit" value="Search"/></fieldset>
</form>

<?php
if(isset($_GET['text1'])){
error_reporting(E_ALL);
ini_set('display_errors','On');
$text=urlencode($_GET['text1']);
$xmlstr = file_get_contents('http://sandbox.api.ebaycommercenetwork.com/publisher/3.0/rest/GeneralSearch?apiKey=78b0db8a-0ee1-4939-a2f9-d3cd95ec0fcc&trackingId=7000610&keyword='.$text);

$xml = new SimpleXMLElement($xmlstr);
$_SESSION['viewxml']=$xmlstr;
if($xml->categories->intActualCategoryCount==0)
{
	echo "<p style='color:#FFFFFF'>Enter a proper product name</p>";
}
else
{
echo '<table border="1" style="width:500px" cellspacing="0" bgcolor="#C0C0C0" border-style="groove" border-width="medium" >';
foreach($xml->categories->category as $category)
      foreach($category->items->product as $product)
        echo  nl2br('<tr id='.$product['id'].'><td><a href="buy.php?buy='.$product["id"].'"><img src="'.$product->images->image->sourceURL.'"/></a></td><td>'.$product->name->asXML().'</td><td>'.$product->minPrice->asXML().'$</td></tr>');
echo '</table>';
}}

?>
</body>
</html>
