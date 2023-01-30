$(function(){
	var productId = getQueryString('productId');
	var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;
	
	//access back-end to retrieve product info and render to page
	$.getJSON(productUrl, function(data) {
		if(data.success){
			var product = data.product;
			$('#product-thumbnail').attr('src',product.imgAddr);
			$('#product-update-time').text("Updated on " + new Date(product.lastEdited).Format("yyyy-MM-dd"));
			$('#product-name').text(product.productName);
			$('#product-desc').text(product.productDesc);
			//product price. if both prices are not given, don't show price
			if(product.normalPrice != undefined && product.salePrice != undefined){
				//both not null, cross out normal price and show both prices
				$('#price').show();
				$('#normalPrice').html('<del>' + '$' + product.normalPrice
					+ '</del>');
				$('#salePrice').text('$' + product.salePrice);
			} else if(product.normalPrice != undefined && product.normalPrice == undefined){
				//no sale price. show normal price normally
				$('#price').show();
				$('#normalPrice').html('$' + product.normalPrice);
			} else if(product.nromalPrice == undefined && product.salePrice != undefined){
				//no normal price. show sale price normally
				$('#salePrice').text('$' + product.salePrice);
			}
			var imgListHtml = '';
			//traverse product specific image list, generate <img> tags up to 6
			product.productImgList.map(function(item, index){
				imgListHtml += '<div> <img src="' + item.imgAddr
					+ '" width = "100%" /></div>';
			});
			$('#imgList').html(imgListHtml);
		}
	});
	$('#me').click(function(){
		$.openPanel('#panel-right-demo');
	});
	$.init();
});