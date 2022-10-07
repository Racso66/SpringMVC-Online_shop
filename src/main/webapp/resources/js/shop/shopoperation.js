/**
 * 1.retrieve shop category, area.... information from backend and outputs to frontent html file
 * 2.retrieve shop information and send to backend for registering shop
 * 3.Verify correctness of shop information input
 */

$(function() {
	var initUrl = '/o2o/shopadmin/getshopinitinfo';
	var registerShopUrl = '/o2o/shopadmin/registershop';
	//alert(initUrl);
	getShopInitInfo();
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
		//2.
		$('#submit').click(function() {
			var shop = {};
			shop.shopName = $('#shop-name').val();
			shop.shopAddr = $('#shop-addr').val();
			shop.phone = $('#shop-phone').val();
			shop.shopDesc = $('#shop-desc').val();
			shop.shopCategory = {
				shopCategoryId: $('#shop-category').find('option').not(function() {
					return !this.selected;
				}).data('id')
			};
			shop.area = {
				areaId: $('#area').find('option').not(function() {
					return !this.selected;
				}).data('id')
			};
			var shopImg = $('#shop-img')[0].files[0];
			var formData = new FormData();
			formData.append('shopImg', shopImg);
			formData.append('shopStr', JSON.stringify(shop));
			var verifyCodeActual = $('#j_kaptcha').val();
			if (!verifyCodeActual){
				$.toast('Please enter veryfication code !');
				return;
			}
			formData.append('verifyCodeActual', verifyCodeActual);
			$.ajax({
				url: registerShopUrl,
				type: 'POST',
				data: formData,
				contentType: false,
				processData: false,
				cache: false,
				success: function(data) {
					if (data.success) {
						$.toast('Sumbition successful!');
					} else {
						$.toast('Sumbition failed' + data.errMsg);
					}
					$('#Kaptcha_img').click();
				}
			});
		});
	}
})