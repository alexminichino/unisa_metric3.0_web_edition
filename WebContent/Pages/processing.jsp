<%@page import="it.unisa.metric.web.WebConstants"%>

  

<div  class="text-center">
	<textarea class="loadArea" id="echoText"> </textarea> 
</div>  
 

<div class="text-center" id="overlay" style="display:none"> 
	<img class="loader" src="${pageContext.request.contextPath}/Assets/images/loading.gif";/>
	<!-- <div class="loader" id="loaderButton"></div> --> 
	<span class="loader" id="opInprogressNotice"></span>
</div> 

<a href="Dispatcher?page=resultPage"  id="resultButton" style="display:none"><button class="btn btn-default buttons">Show result</button></a>


<!-- Socket -->
<script type="text/javascript">
var SOCKET_URL= '<%= WebConstants.SOCKET_URL+"/"+ request.getParameter("files") %>';
</script>
<script src="${pageContext.request.contextPath}/Assets/JS/webSocket.js"></script>
