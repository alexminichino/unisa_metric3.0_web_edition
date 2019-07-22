

<h1>Help Center</h1>

<p>If you want to report a bug or submit a question, compile the
	form and our team contact you.</p>
<div class="row">
	<div class="col-md-10 offset-md-1" style="margin-top: 50px;"> 
		<div class="border" style="padding: 10px;">

			<form action="SendEmailServlet" method="post">
				<div class="form-group">
					<label for="email">Email:</label> 
					<input type="text" class="form-control" id="email" name="emailFrom">
					<div class="alert alert-danger" id="alert" ><strong>Email non valida!</strong></div>
				</div>
				<div class="form-group">
					<label for="subject">Subject:</label> <input type="text"
						class="form-control" id="subj" name="subject">
				</div>
				<div class="form-group" style="margin-top: 5px;">
					<label for="textArea">Text:</label>
					<textarea id="text" class="form-control" name="content"></textarea>
					<div class="alert alert-danger" id="alertT" ><strong>Corpo della email vuoto!</strong></div>
				</div>

				<div class="text-center">
					<button  id="sub" type="submit" class="btn btn-default"
						style="margin-top: 10px; background-color: #43425D; color: white;">Submit</button>
				</div>
		</div>
		</form>
	</div>
</div>
<script>
$('#email').on('change', function(){
	var email= $(this).val();
	var myRegEx = /^[A-z0-9\.\+_-]+@[A-z0-9\._-]+\.[A-z]{2,6}$/;
	if (!(myRegEx.test(email)))
		$("#alert").show();
	else
		$("#alert").hide();
})
</script>
<script>
$(document).ready(function() {
    $("#alert").hide();
    $("#alertT").hide();
});
</script>
<script>
$("textarea").focusout(function(){
    if ($(this).val() == '')
    	$("#alertT").show();
  });
</script>