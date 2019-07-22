//Validate form
$(function()
{	
	var userName= $("#user");
	var password= $("#pass");
	var passwordConfirm= $("#pass-conf");
	var alertPasswordMatch= $("#passwordNotMatch");
	var alertPassword= $("#passwordSize");
	var alertUsername= $("#usernameSize");
	var submit = $("#submit");
	

	userName.focusout(function(){
		value = $(this).val();
		if(value.length>=5 && value.length<=15){
			alertUsername.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alertUsername.slideDown();
			submit.prop("disabled",true);
		}
	});
	
	password.focusout(function(){
		value = $(this).val();
		if(value.length>=5 && value.length<=8){
			alertPassword.slideUp();
			submit.prop("disabled",false); 
		}
		else{
			alertPassword.slideDown();
			submit.prop("disabled",true);
		}
	});
	
	passwordConfirm.focusout(function(){
		value = $(this).val();
		if(value == password.val()){
			alertPasswordMatch.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alertPasswordMatch.slideDown();
			submit.prop("disabled",true);
		}
	});
	
})