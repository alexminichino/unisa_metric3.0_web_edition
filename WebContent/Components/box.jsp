<div class="boxInfo  border">
	<p class="boxTitle" style="font-size:15px;"> ${param['boxTitle']}</p>  
	<jsp:include page="/Components/${param['componentToLoad']}.jsp" flush="true">
        <jsp:param name="url" value="${param['url']}"/>
        <jsp:param name="textPlain" value="${param['textPlain']}"/>
   	</jsp:include>
</div>
    
    
   