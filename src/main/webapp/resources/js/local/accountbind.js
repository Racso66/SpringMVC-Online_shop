$(function(){
	var bindUrl = '/o2o/local/bindlocalauth';
	// get user type from url, type 1 front end , type 2 shop end
	var usertype = getQueryString('usertype');
	$('#submit').click(function(){
		var username = $('#username').val();
		var password = $('#thepassword').val();
		var verifyCodeActual = $('#j_kaptcha').val();
		var needVerify = false;
		if(!verifyCodeActual) {
			$.toast('please enter the code');
			return;
		}
		// access backend and bind the account (LocalAuthController)
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				username : username,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data){
				if(data.success){
					$.toast('Account binded');
					if(usertype == 1){
						// front end
						window.location.href = '/o2o/frontend/index';
					} else if(usertype == 2){
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else{
					$.toast('Failed to submit ' + data.errMsg);
					$('#Kaptcha_img').click();
				}
			}
		});
	});
});
