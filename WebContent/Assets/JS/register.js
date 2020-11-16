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
		checkUsername();
	});
	
	password.focusout(function(){
		checkPassword();
	});
	
	passwordConfirm.focusout(function(){
		checkPasswordConfirm();
	});

	function checkUsername(ev){
		value = userName.val();
		if(value.length>=5 && value.length<=15){
			alertUsername.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alertUsername.slideDown();
			submit.prop("disabled",true);
			stopEv(ev);
		}
	}

	function checkPassword(ev){
		value = password.val();
		if(value.length>=5 && value.length<=8){
			alertPassword.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alertPassword.slideDown(ev);
			submit.prop("disabled",true);
			stopEv(ev);
		}
	}

	function checkPasswordConfirm(ev){
		value = passwordConfirm.val();
		if(value == password.val()){
			alertPasswordMatch.slideUp();
			submit.prop("disabled",false);
		}
		else{
			alertPasswordMatch.slideDown();
			submit.prop("disabled",true);
			stopEv(ev);
		}
	}

	function stopEv(ev){
		if(ev != undefined){
			ev.stopPropagation();
			ev.preventDefault();
		}
	}

	submit.click(function(ev){
		checkUsername(ev);
		checkPassword(ev);
		checkPasswordConfirm(ev);
	});
	
})