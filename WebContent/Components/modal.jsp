<!-- The Modal -->
  <div class="modal fade" id="${param['modalId']}">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">${param['modalTitle']}</h4>
          <% if(!request.getParameter("preventClose").equals("true")){ %>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <% } %>
        </div>
        
        <!-- Modal body -->
        <div class="modal-body">
          ${param['modalBody']}
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer text-center" style="display:block">
           <% if(request.getParameter("actionContent") == null){ %>
          	<button type="button" class="btn btn-secondary" data-dismiss="modal">${param['dismissText']}</button>
          <%} else{ %>
          	${param['actionContent']}
          <%} %>
        </div>
        
      </div>
    </div>
  </div>