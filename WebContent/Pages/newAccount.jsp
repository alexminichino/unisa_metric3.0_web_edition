<h1>Register New Account</h1>
<div class="col-md-6 offset-md-2 ">
	<div class="border" style="margin-top: 15%; padding: 10px;"
		id="formNewAccount">
		<form action="RegisterAccountServlet" method="post">
			<div class="form-group">
				<label for="name">Name:</label> <input type="text"
					class="form-control" name="nameRegister" id="name" required>
			</div>
			<div class="form-group">
				<label for="surname">Surname:</label> <input type="text"
					class="form-control" name="surnameRegister" id="surname" required>
			</div>
			<div class="form-group">
				<labe for="comp">Company </labe>
				<input type="text" class="form-control" name="companyRegister"
					id="comp">
			</div>
			<div class="form-group">
				<label for="user">Username: </label> <input type="text"
					class="form-control" name="userRegister" id="user" required>
			</div>
			<div class="alert alert-danger" role="alert" id="usernameSize"
				style="display: none">
				<strong>Incorrect input!</strong>  Your username must be between 5 and 15 characters.
			</div>
			<div class="form-group">
				<label for="pass">Password:</label> <input type="password"
					class="form-control" name="passwordRegister" id="pass" required>
			</div>
			<div class="alert alert-danger" role="alert" id="passwordSize"
				style="display: none">
				<strong>Incorrect input!</strong>  Your password must be between 5 and 8 characters.
			</div>
			<div class="form-group">
				<label for="pass-conf">Confirm Password:</label> <input
					type="password" class="form-control" id="pass-conf" required>
			</div>
			<div class="alert alert-danger" role="alert" id="passwordNotMatch"
				style="display: none">
				<strong>Incorrect input!</strong> Your password and confirmation password do not match.
			</div>

			<div class="text-center">
				<button type="submit" class="btn btn-default buttons "
					style="margin-top: 10px;" value="" id="submit">Register</button>
			</div>

		</form>
	</div>
</div>

<script src="${pageContext.request.contextPath}/Assets/JS/register.js"></script>
<script>
	$(document).ready(function(){
		$("#sideBar").hide();
		$("#hamburgerButton").hide();
	});
	
</script>