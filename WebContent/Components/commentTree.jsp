	<div id="jstree"></div>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
	
	
<!-- Socket --> 
<script type="text/javascript">
var ANALYSIS_URL= '<%= request.getContextPath()+"/Results?analysisId="+ request.getParameter("analysisId") %>';
var BASE_URL= '<%= request.getContextPath()%>'; 
</script>
<script src="${pageContext.request.contextPath}/Assets/JS/results.js"></script>
 