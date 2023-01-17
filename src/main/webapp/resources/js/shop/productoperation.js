$(function(){
	var productId = getQueryString('productId'); //from Url
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	var isEdit = false;//determine operation add or modify (both operation on same page)
	if(productId) {	//modify operation
		getInfo(productId);
		isEdit = true;
	} else { //add product does not require productId specified
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct'
	}
	function getInfo(id){//retrieve product info to be edited, assign to form
		$.getJSON(infoUrl, function(data){
			if(data.success){
				//info retrieved from JSON
				var product = data.product;
				$('#product-name').val(product.productName);
				$('#product-desc').val(product.productDesc);
				$('#priority').val(product.priority);
				$('#normal-price').val(product.normalPrice);
				$('#sale-price').val(product.salePrice);
				//retrieve original product category and all categories of the current shop
				var optionHtml = '';
				var optionArr = data.productCategoryList;
				var optionSelected = product.productCategory.productCategoryId; //original
				//generate HTML of shop category list in front-end and by default select the shop
				//category before the edit, (ie. select juice by default if the category was juice)
				optionArr.map(function(item, index){
					var isSelect = optionSelected === item.productCategoryId?'selected':'';
					optionHtml += '<option data-value="'
						+ item.productCategoryId +'"'
						+ isSelect + '>'
						+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}
	function getCategory(){ //provide all categories existing in the shop for add product operation
		$.getJSON(categoryUrl, function(data){
			if(data.success){
				var productCategoryList = data.data;
				var optionHtml = '';
				productCategoryList.map(function(item, index){
					optionHtml += '<option data-value="'
						+ item.productCategoryId + '">'
						+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}
	//submit responds differently with add product and modify product
	$('#submit').click(function(){
		//create product JSON target, retrieve value from form
		var product = {};
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.priority = $('#priority').val();
		product.normalPrice = $('#normal-price').val();
		product.salePrice = $('#sale-price').val();
		product.productCategory = {
			productCategoryId: $('#category').find('option').not(
				function(){
					return !this.selected;
				}).data('value')
		};
		product.productId = productId;
		var thumbnail = $('#thumbnail-img')[0].files[0];
		//generate form target to receive values for back-end
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		$('.detailed-img').map(
			function(index, item){
				if($('.detailed-img')[index].files.length > 0) {
					//assign i-th files stream as key into productImg i
					formData.append('productImg' + index, $('.detailed-img')[index].files[0]);
				}
			}
		);
		formData.append('productStr', JSON.stringify(product));
		//get verify code
		var verifyCodeActual = $('#j_kaptcha').val();
		if(!verifyCodeActual){
			$.toast('Please enter verify code !');
			return;
		}
		formData.append("verifyCodeActual",verifyCodeActual);
		$.ajax({
			url : productPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data){
				if(data.success){
					$.toast('Submission success');
					$('#kaptcha_img').click(); //change code
				} else {
					$.toast('Submission fail');
					$('#kaptcha_img').click();
				}
			}
		});
	});
	//only add upload button if the upload is child/last upload, add up to 6th
	//managing previous will not create new button
	$('.detailed-img-div').on('change','.detailed-img:last-child', function(){ //latest upload
		if($('.detailed-img').length < 6) { //limit to 6
			$('#detailed-img').append('<input type = "file" class = "detailed-img">');
		}
	});
});