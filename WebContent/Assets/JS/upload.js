//Validate form
$(function()
{	
	var projectName= $("#name");
	var alert= $("#invalidInput");
	var submit = $("#submit");
	var regex= /^[a-zA-Z0-9_]*$/;

	projectName.keyup(function(){
		value = $(this).val();
		if(value.match(regex)){
			alert.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alert.slideDown();
			submit.prop("disabled",true);
		}
	});
})

$(function()
{	
	var pbar = $('#progressBar'), currentProgress = 0;
	function trackUploadProgress(e)
	{
		if(e.lengthComputable)
		{
			currentProgress = (e.loaded / e.total) * 100; // Amount uploaded in percent
			$(pbar).width(currentProgress+'%');
			
			if( currentProgress == 100 )
				console.log('Progress : 100%');
		}
	}

	function uploadFile()
	{
		var formdata = new FormData($('form')[0]);
		$.ajax(
		{
			url:'/ProgettoIGES/Upload',
			type:'post',
			data:formdata,
			xhr: function()
			{
				// Custom XMLHttpRequest
				var appXhr = $.ajaxSettings.xhr();

				// Check if upload property exists, if "yes" then upload progress can be tracked otherwise "not"
				if(appXhr.upload)
				{
					// Attach a function to handle the progress of the upload
					appXhr.upload.addEventListener('progress',trackUploadProgress, false);
				}
				return appXhr;
			},
			success:function(data){ 
				$("#uploadForm").find("input").prop("disabled",true);
				$("#uploadForm").find("button").prop("disabled",true);
				$("#destination").attr("href",data)
				$("#modalSuccess").modal({
		    		backdrop: 'static',
		    		keyboard: false
				});
				 
			},
			error:function(data){ 
				$("#modalError").modal({
		    		backdrop: 'static',
		    		keyboard: false
				});
			},

			// Tell jQuery "Hey! don't worry about content-type and don't process the data"
			// These two settings are essential for the application
			contentType:false,
			processData: false 
		})
	}

	$('form').submit(function(e)
	{
		e.preventDefault();
		$(pbar).width(0).addClass('active');
		uploadFile();
	});
})