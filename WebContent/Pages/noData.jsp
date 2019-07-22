
<%
	String linkNewAnalisys = request.getContextPath() + "/Dispatcher?page=newAnalysis";
%>
<h1>Nessuna analisi effettuata</h1>

<div class="row" style="margin-top: 50px;"> 
    <div class="col-md-10 offset-md-1">  
        <div class="border"  style="padding: 10px 10px 10px 10px;">
           <h2>Nessuna analisi presente in archivio 
	           <a href ="<%=linkNewAnalisys%>"> 
	          		effettua una nuova analisi
	           </a>
           </h2> 
        </div>
        
    </div>
</div>