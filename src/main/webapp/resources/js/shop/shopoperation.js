/** getShopInitInfo()
 * 1.retrieve shop category, area.... information from backend and outputs to frontent html file
 * 2.retrieve shop information and send to backend for registering shop
 * 3.Verify correctness of shop information input
 */
/**
 * input shopId to retreive shop info
 */

$(function() {
	var shopId = getQueryString('shopId');
	var isEdit = shopId?true:flase;//true if not empty, used to determine register or modify
	var initUrl = '/o2o/shopadmin/getshopinitinfo';
	var registerShopUrl = '/o2o/shopadmin/registershop';
	var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
	var editShopUrl = '/o2o/shopadmin/modifyshop';
	
	if(!isEdit){
		//alert(initUrl);
		getShopInitInfo();
	} else {
		getShopInfo(shopId);
	}
	
	
	function getShopInfo(shopId){
		$.getJSON(shopInfoUrl, function(data){
			if(data.success){
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="' + shop.shopCategory.shopCategoryId + '" selected>' 
					+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml ='';
				data.areaList.map(function(item,index){
					tempAreaHtml += '<option data-id ="' + item.areaId + '">' + item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				$('#shop-category').attr('disabled','disabled');//cannot be modified by users
				$('#area').html(tempAreaHtml);
				$("#area option[data-id ='" + shop.area.areaId + "']").attr("selected","selected");
			}
		});
	}
	
	//1.
	function getShopInitInfo() {
		$.getJSON(initUrl, function(data) {
			if (data.success) {
				var tempHtml = '';
				var tempAreaHtml = '';
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId + '">' + item.shopCategoryName + '</option>';
				});
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id = "' + item.areaId + '">' + item.areaName + '</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
	}
	//2.
	$('#submit').click(function() {
		var shop = {};
		if(isEdit) {
			shop.shopId = shopId;
		}
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		shop.shopCategory = {
			shopCategoryId : $('#shop-category').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		shop.area = {
			areaId : $('#area').find('option').not(function() {
				return !this.selected;
			}).data('id')
		};
		var shopImg = $('#shop-img')[0].files[0];
		var formData = new FormData();
		formData.append('shopImg', shopImg);
		formData.append('shopStr', JSON.stringify(shop));

		var verifyCodeActual = $('#j_kaptcha').val();
		if (!verifyCodeActual) {
			$.toast('please enter verify code!');
			return;
		}
		formData.append('verifyCodeActual', verifyCodeActual);
		$.ajax({
			url : (isEdit ? editShopUrl : registerShopUrl),
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('Submition Success!');
				} else {
					$.toast('Submition Failed!' + data.errMsg);
				}
				$('#Kaptcha_img').click();
			}
		});
	});

});
