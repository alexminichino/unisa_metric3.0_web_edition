<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="it.unisa.metric.web.HistoryItem"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.lang.reflect.Type"%>
<%
	Type type = new TypeToken<List<HistoryItem>>() {
	}.getType();
	Gson g = new Gson();
	ArrayList<HistoryItem> totalResults = g.fromJson(request.getParameter("historyItems"), type);
	String analysisUrl = request.getContextPath() + "/Results?analysisId=";
	String historyPage = request.getContextPath() + "/History?page=history";
	
%>
<h1>Overview</h1>
<!--1 row-->
<div class="row" style="margin-top: 50px;">
	<div class="col-md-6">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="textContentBox" />
			<jsp:param name="boxTitle" value="Total Analysis" />
			<jsp:param name="textValue" value="<%=totalResults.size()%>" />
		</jsp:include>
	</div>
	<div class="col-md-6">
		<jsp:include page="/Components/box.jsp" flush="true">
			<jsp:param name="componentToLoad" value="textContentBox" />
			<jsp:param name="boxTitle" value="Last Analysis" />
			<jsp:param name="textValue"
				value="<%=totalResults.get(0).getName()%>" />

		</jsp:include>
	</div>
</div>
<!--2 row-->
<div class="row" style="margin-top: 200px;">
	<div class="col-md-5" style="padding-left: 3px;">
		<div class="border boxInfo" style="padding: 5px 5px 5px 5px;">
			<p align="left">Analysis History</p>
			<div class="border" style="padding: 5px 5px 5px 5px;">
				<jsp:include page="/Components/table.jsp" flush="true">
					<jsp:param name="analysisId"
						value="<%=totalResults.get(0).getanalysisId()%>" />
				</jsp:include>

			</div>
			<a href="<%=historyPage%>"><p align="left">
					<u>Show more</u>
				</p></a>
		</div>
	</div>
	<div class="col-md-5">

		<div class="border boxInfo">
			<p align="left">Last Tree</p>
			<p class="boxTitle" style="font-size: 15px;">
				${param['boxTitle']}</p>
			<jsp:include page="/Components/commentTree.jsp" flush="true">
				<jsp:param name="analysisId"
					value="<%=totalResults.get(0).getanalysisId()%>" />
			</jsp:include>
			<a href="<%=analysisUrl+totalResults.get(0).getanalysisId()%>"><p align="left">
					<u>Show more</u>
				</p></a>
		</div>

	</div>
</div>