<h1>Login</h1>

		<div class="col-md-6 offset-md-2 ">
			<div class="border" style="margin-top: 15%; padding: 10px;" id="formLogin">
				<form action="LoginServlet" method="post">
					<div class="form-group">
						<label for="user">Username:</label>
						<input class="form-control" type="text" name="userName">
					</div>
					<div class="form-group">
						<label for="pass">Password:</label>
						<input class="form-control" type="password" name="passWord">
					</div>
					<div class="text-center">
						<button type="submit" class="btn btn-default buttons " style=" margin-top: 10px;" value="" >Login </button>
					</div>
			</form>
				<div class="text-center">
					<a href="Dispatcher?page=newAccount" style="margin-top: 10px;">Register a new Account</a>
				</div>
					<% if (request.getAttribute("errorLogin")!=null){ %>
						<jsp:include page="/Components/alert.jsp"></jsp:include>
					<% 	}%>
				
			</div>
		</div>
	
<script>
	$(document).ready(function(){
		$("#sideBar").hide();
		$("#hamburgerButton").hide();
	});
	
</script>