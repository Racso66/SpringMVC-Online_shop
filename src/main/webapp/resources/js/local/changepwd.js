$(function(){
	var pwdurl = '/o2o/local/changelocalpwd';
	var usertype = getQueryString('usertype');
	
	$('#submit').click(function(){
		var username = $('#username').val();
		var password = $('#thepassword').val();
		var newPassword = $('#newPassword').val();
		var confirmPassword = $('#confirmPassword').val();
		// ensure re-entered password matches new password
		if(newPassword != confirmPassword){
			$.toast('Ensure re-entered password matches new password');
			return;
		}
		// new form data
		var formData = new FormData();
		formData.append('username', username);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		//get kaptcha code
		var verifyCodeActual = $('#j_kaptcha').val();
		if(!verifyCodeActual){
			$.toast('please enter the code');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		// Post values to backend for password change
		$.ajax({
			url : pwdurl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data){
				if(data.success){
					$.toast('Successful submission');
					if(usertype == 1){
						window.location.href = '/o2o/frontend/index';
					} else {
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('Failed to submit' + data.errMsg);
					$('#Kaptcha_img').click();//change code
				}
			}
		});
	});
	
	$('#back').click(function(){
		if(usertype == 1){
			window.location.href = '/o2o/frontend/index';
		} else {
			window.location.href = '/o2o/shopadmin/shoplist';
		}
	});
});