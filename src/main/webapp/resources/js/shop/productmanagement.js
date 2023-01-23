$(function(){
	var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	var statusUrl = '/o2o/shopadmin/modifyproduct'; //for deactivating product
	getList();

	function getList(){
		$.getJSON(listUrl, function(data){
			if(data.success){
				var productList = data.productList;
				var tempHtml = '';
				/* 
				* Traverse through all shop info and concatenate to a single row for display
				* List info includes: product name, priority, modify product *include id* (deactivate/activate)
				* edit *include id*, preview *include id*
				*/
				productList.map(function(item, index){
					var textOp = "Deactive";
					var contraryStatus = 0;
					//if enable status is already 0, change button name to activate
					if(item.enableStatus == 0){
						textOp = "Activate";
						contraryStatus = 1;
					}
					//concatenate every product's row info
					tempHtml += '<div class = "row row-product">'
						+ '<div class = "col-33">' + item.productName
						+ '</div>' + '<div class = "col-20">' + item.priority
						+ '</div>' + '<div class = "col-40">'
						+ '<a href = "#" class = "edit" data-id ="'
						+ item.productId + '" data-status ="' + item.enableStatus
						+ '">Edit</a>'
						+ '<a href = "#" class = "modify" data-id ="'
						+ item.productId + '" data-status ="' + contraryStatus
						+ '">' + textOp + '</a>'
						+ '<a href = "#" class = "preview" data-id ="'
						+ item.productId + '" data-status ="' + item.enableStatus
						+ '">Preview</a>' + '</div>' + '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}
	$('.product-wrap').on('click','a',function(e){//give a tag in class product-wrap click event function
		var target = $(e.currentTarget);
		if(target.hasClass('edit')){
			window.location.href = '/o2o/shopadmin/productoperation?productId=' + e.currentTarget.dataset.id;
		} else if(target.hasClass('modify')){
			changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status);
		} else if(target.hasClass('preview')){//front-end showcase system to be implemented
			window.location.href = '/o2o/frontend/productdetail?productId=' + e.currentTarget.dataset.id;
		}
	});
	//for modifying product status (activate or deactivate) using back-end operations
	function changeItemStatus(id, enableStatus){
		//declare product as JSON target and add in productId and status
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('Are you sure?', function(){
			//modify status of related product
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data){
					if(data.success){
						$.toast('Successful operation');
						getList();
					} else{
						$.toast('Operation failure');
					}
				}
			});
		});
	}
});