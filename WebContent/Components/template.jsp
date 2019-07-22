<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="it.unisa.metric.*, org.eclipse.jdt.core.dom.*, java.lang.*" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ page import="it.unisa.metric.web.*" %>

<!doctype html>
<html lang="en">  
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	  <!-- Bootstrap CSS -->
	  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
    <!--Material Icon-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">  
    <!--Navbar Css-->
    <link href="${pageContext.request.contextPath}/Assets/CSS/sidebar_style.css" rel="stylesheet"> 
    <!--Dashboard Css-->
    <link href="${pageContext.request.contextPath}/Assets/CSS/templateStyle.css" rel="stylesheet">
    <!-- Upload style-->
    <link href="${pageContext.request.contextPath}/Assets/CSS/upload.css" rel="stylesheet">
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
    <!--Bootstrap-->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
    <!--JQuery UI-->
    <script  src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"  integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
    <!-- JSTREE -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css" />
    <!--Dynamic page title-->
    <title>${param["pageTitle"]}</title>
  </head>
  <body>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2">
			    <!-- Navbar -->
			    <jsp:include page="sidebar.jsp" flush="true">
			        <jsp:param name="currentPage" value="newInstance.com"/>
			    </jsp:include>
			    <!-- navbar end -->
			</div>
			
			<div class="col-md-10">
			    <!--Main div-->

			  
			    <div id="main" class="">
			     <jsp:include page="/Pages/${param['page']}.jsp" flush="true">
			         <jsp:param name="" value=""/>
			    </jsp:include>
			    
			    
			    </div> 
		    <!--Main div End-->
		    </div>
	    </div>
    </div>
  </body>
</html>