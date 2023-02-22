$(function(){
	var loginUrl = '/o2o/local/logincheck';
	// usertype 1 for customers, 2 for shop owners
	var usertype = getQueryString('usertype');
	// if login fails 3 times, prompt kaptcha verify code
	var loginCount = 0;
	
	$('#submit').click(function(){
		var username = $('#username').val();
		var password = $('#thepassword').val();
		var verifyCodeActual = $('#j_kaptcha').val();
		var needVerify = false;
		//failed login 3 times
		if(loginCount >= 3){
			// check code
			if(!verifyCodeActual) {
				$.toast('Please enter the code');
				return;
			} else {
				needVerify = true;
			}
		}
		// visit backend to verify login
		$.ajax({
			url : loginUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				username : username,
				password : password,
				verifyCodeActual : verifyCodeActual,
				// check if verification code is needed
				needVerify : needVerify
			},
			success : function(data){
				if(data.success){
					$.toast('Login success');
					if(usertype == 1){
						window.location.href = '/o2o/frontend/index';
					} else if(usertype == 2){
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else{
					$.toast('Login failed' + data.errMsg);
					loginCount++;
					if(loginCount >= 3){
						$('#verify').show();
					}
				}
			}
		});
	});
});
