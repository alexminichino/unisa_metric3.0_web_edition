<br>
<h1>Nuova Analisi</h1>
<div class="row">
	<div class="col-md-10 offset-md-1">
		<div class="border" style="margin-top: 50px; padding: 10px;" id="form">

			<form id="uploadForm" action="<%=request.getContextPath()%>/Upload"
				method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="name">Nome:</label> <input type="text"
						class="form-control" id="name" name="project_name" required>
				</div>
				<div class="alert alert-danger" role="alert" id="invalidInput"
					style="display: none">
					<strong>Incorrect input!</strong> The name can only contain
					letters, numbers and underscores.
				</div>

				<div class="form-group">
					<label for="file">File</label>
					<div class="custom-file" id="customFile" lang="es">
						<input required style="visibility: hidden;" type="file"
							class="custom-file-input form-control" id="InputFile"
							aria-describedby="fileHelp" name="file"> <label
							class="custom-file-label" for="InputFile"> </label>
					</div>
					<script>
						$('#InputFile').on('change', function() {
							//get the file name
							var fileName = $(this).val();
							//replace the "Choose a file" label
							$(this).next('.custom-file-label').html(fileName);
						})
					</script>
				</div>
				<div class="text-center">

					<button id="submit" type="submit" class="btn btn-default buttons"
						style="margin-top: 10px;" value="processing.jsp">Submit
					</button>

				</div>
			</form>
		</div>
		<div id="progressBar"></div>

		<jsp:include page="/Components/modal.jsp" flush="true">
			<jsp:param name="modalId" value="modalSuccess" />
			<jsp:param name="modalTitle" value="Great" />
			<jsp:param name="modalBody" value="Upload completed" />
			<jsp:param name="preventClose" value="true" />
			<jsp:param name="actionContent"
				value="
	        <a id='destination' class='btn btn-secondary buttons' >Start analysis</a> 
	    " />
		</jsp:include>

		<jsp:include page="/Components/modal.jsp" flush="true">
			<jsp:param name="modalId" value="modalError" />
			<jsp:param name="modalTitle" value="Error" />
			<jsp:param name="modalBody" value="Please try again!" />
			<jsp:param name="preventClose" value="false" />
			<jsp:param name="dismissText" value="Close" />
		</jsp:include>
		<script src="${pageContext.request.contextPath}/Assets/JS/upload.js"></script>
	</div>
</div>