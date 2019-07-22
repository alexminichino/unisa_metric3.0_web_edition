<div id="mobileBar">
	<button  id="hamburgerButton" class="openbtn" onclick="openNav(event)">&#9776;</button>  
</div>
<div id="sideBar" class="sidebar">
  <a href="javascript:void(0)" class="closebtn" onclick="closeNav(event)">&times;</a>
  

<% if (session.getAttribute("user") == null) { %>
    	
	<% } else {%>
		<p style="color: white; font-size:16px; vertical-align:middle; padding: 8px 8px 8px 15px;"><i class="material-icons viola md-24">face</i>
	    	<%=session.getAttribute("user")%></p>
	<% } %>
	
	
		<a onclick="checked()" class="nav-link li_space" id="target" href="History?page=dashboard"><i class="material-icons viola md-24" >home</i>Home </a>
    <a onclick="checked()" class="nav-link li_space" id="target" href="Dispatcher?page=newAnalysis"><i class="material-icons viola md-24" >insert_chart_outlined</i>New Analysis</a>
    <a onclick="checked()" class="nav-link li_space" id="target" href="History?page=history"><i class="material-icons viola md-24" >history</i>History</a>
    <a onclick="checked()" class="nav-link li_space" id="target" href="Dispatcher?page=help"><i class="material-icons viola md-24" >help</i>Help Center</a>
   
    <% if (session.getAttribute("user") == null) { %>
     <a  class="nav-link li_space" id="target" href="Dispatcher?page=login"><i class="material-icons viola md-24" >account_box</i>Login</a>
    
    <% } else { %>
     <a class="nav-link li_space" id="target" href="LogoutServlet"><i class="material-icons viola md-24" >account_box</i>Logout</a>
    <%} %>
	
	
	
	
</div>
<script>
function openNav(event) {
  $("#sideBar").show("slide", {direction:"left", mode:"slow"},1000);
  //$("#hamburgerButton").hide();
  event.stopPropagation();
}

function closeNav(event) {
  $("#sideBar").hide("slide", {direction:"left", mode:"slow"},1000);
  $("#hamburgerButton").show();
  event.stopPropagation();
}

function checked(){
  
  var ck= document.getElementById("target");
  var x = location.pathname;
  var url_array = x.split('/');
  var last_segment = url_array[url_array.length - 1];
  
  if (document.getElementById("target").getAttribute("href")==last_segment)
    document.getElementById("target").style.color="#1E90FF";

  }

  $(document).ready(function(){
    $("#main").click(function(event){
      if( $("#sideBar").is(":visible") && $(window).width() <= 1200 ){
        closeNav(event);
        event.stopPropagation();
      }
    });
    
    
    $("#mobileBar").click(function(event){
       alert()
      });
    
  });

</script>


