<%@page import="java.util.List"%>
<%@page import="java.lang.reflect.Type"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="it.unisa.metric.web.MetricResultsMean"%>
<%@page import="it.unisa.metric.web.MetricResult"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.unisa.metric.web.socket.SocketResponse"%>
<%@page import="com.google.gson.Gson"%>
<h1>Project info</h1>

<!--row 1-->
<div class="row" style="margin-top: 20px;">
	<div class="col-md-6">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="textContentBox" />
			<jsp:param name="boxTitle" value="Project name" />
			<jsp:param name="textValue" value="${param['name']}" />
		</jsp:include>
	</div>
	<div class="col-md-6">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="textContentBox" /> 
			<jsp:param name="boxTitle" value="Analysis date" />  
			<jsp:param name="textValue" value="${param['date']}" />
		</jsp:include>
	</div>
</div>
<!--row 2-->
<h1 style="margin-top: 100px;">Result Tree</h1>
<div class="row" style="margin-top: 20px;">

	<div class="col-md-8">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="commentTree" />
			<jsp:param name="boxTitle" value="Tree" />
			<jsp:param name="analysisId" value="${param['analysisId']}" />
		</jsp:include>
	</div>
</div>
<h1 style="margin-top: 100px;">Metrics Averages</h1>

<%
Type type = new TypeToken<List<MetricResultsMean>>() {}.getType(); 
Gson g = new Gson();
ArrayList<MetricResultsMean> totalResults = g.fromJson(request.getParameter("avarages"), type); 
totalResults.sort((MetricResultsMean o, MetricResultsMean o2) -> o.getMetricLevel().ordinal() - o2.getMetricLevel().ordinal() );
int col=0;
String lastLevel="";
for (MetricResultsMean m: totalResults){
	String currentLevel=m.getMetricLevel().toString().toLowerCase();
	currentLevel= currentLevel.substring(0,1).toUpperCase()+currentLevel.substring(1);
	if(!lastLevel.equals(currentLevel)){
		if(col>0){
			col=0;%>
			</div>
		<%}
		lastLevel=  currentLevel;%>
		<h2 style="margin-top: 20px;"><%= currentLevel %></h2>
		<div class="row" style="margin-top: 20px;">
	<%}
	if(col%4==0 && col>0){%> 
		</div>
		<div class="row" style="margin-top: 20px;">
	<% } %>
	<div class="col-md-3" style="margin-top: 20px;">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="textContentBox" />
			<jsp:param name="boxTitle" value="<%= m.getMetricName()%>" />
			<jsp:param name="textValue" value="<%= m.getMean()%>" />
		</jsp:include>
	</div>
<%col++;}%>
</div> 

<h1 style="margin-top: 100px;">Log File</h1>
<div class="row" style="margin-top: 20px;">

	<a href="<%=request.getContextPath()+"/Download?logfile="+request.getParameter("logfile") %>" class="btn btn-default" id="logButton"
		style="background-color: #43425D; color: white; margin-left: 20px; margin-bottom: 30px;">
		Download</a>
</div>