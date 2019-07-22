<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="it.unisa.metric.web.HistoryItem"%>
<%@page import="com.google.gson.reflect.TypeToken"%>
<%@page import="java.lang.reflect.Type"%>
<%
Type type = new TypeToken<List<HistoryItem>>() {}.getType(); 
Gson g = new Gson();
ArrayList<HistoryItem> totalResults = g.fromJson(request.getParameter("historyItems"), type); 

String analysisUrl = request.getContextPath()+"/Results?analysisId=";


%>
<table class="table table-hover">
    <thead>
        <th> Date</th> 
        <th>Name</th>
        <th>Link</th>
    </thead>
    <tbody>
        <% for (HistoryItem i: totalResults){ %>
        	<tr>
        		<td>
        			<%=i.getDate() %>
        		</td>
        		<td>
        			<%=i.getName() %>
        		</td>
        		<td>
        			<a href="<%=analysisUrl+i.getanalysisId()%>">Show more</a>
        		</td>
        	</tr>
        <%} %>
    </tbody>
</table>