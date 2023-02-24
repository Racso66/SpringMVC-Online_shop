$(function() {
	var listUrl = '/o2o/shopadmin/getproductcategorylist';
	var addUrl = '/o2o/shopadmin/addproductcategories';
	var deleteUrl = '/o2o/shopadmin/removeproductcategory';

	getList();
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var dataList = data.data;
				$('.category-wrap').html('');
				var tempHtml = '';
				dataList.map(function(item, index) {
					tempHtml += ''
						+ '<div class = "row row-product-category now">'// now tag means already exist
						+ '<div class = "col-33 product-category-name">'
						+ item.productCategoryName
						+ '</div>'
						+ '<div class = "col-33">'
						+ item.priority
						+ '</div>'
						+ '<div class = "col-33"><a href = "#" class = "button delete" data-id = "'
						+ item.productCategoryId
						+ '">Delete</a></div>'
						+ '</div>'
				});
				$('.category-wrap').append(tempHtml);
			}
		});
	}
	$('#create').click(function() { //create button
		var tempHtml = '<div class = "row row-product-category temp">'//temp tag means to be added upon submition
			+ '<div class = "col-33"><input class = "category-input category" type = "text" placeholder = "Category Name"></div>'
			+ '<div class = "col-33"><input class = "category-input priority" type = "number" placeholder = "Priority"></div>'
			+ '<div class = "col-33"><a href = "#" class = "button delete">Delete</a></div>'
			+ '</div>'
		$('.category-wrap').append(tempHtml); //each click on create button will allow entries to be appened to .category-wrap
	});
	$('#submit').click(function() { //submit button
		var tempArr = $('.temp'); //acquire new entries by searching for temp tag
		var productCategoryList = [];
		tempArr.map(function(index, item) {
			var tempObj = {};
			tempObj.productCategoryName = $(item).find('.category').val(); //finds from each item containing temp tag instead of the whole list
			tempObj.priority = $(item).find('.priority').val();
			if (tempObj.productCategoryName && tempObj.priority) {
				productCategoryList.push(tempObj);//no need for shopId, already acquired from session in controller
			}
		});
		$.ajax({
			url: addUrl,
			type: 'POST',
			data: JSON.stringify(productCategoryList),
			contentType: 'application/json',
			success: function(data) {
				if (data.success) {
					$.toast('Submission success!');
					getList();//call getList() function to immediatly update list and sort by priority upon succesful submit
				} else {
					$.toast('Submission failure!');
				}
			}
		});
	});
	$('.category-wrap').on('click', '.row-product-category.temp .delete', function(e){//delete newly created product category before submission
		console.log($(this).parent().parent());
		$(this).parent().parent().remove();//access starting <div></div>
	});
	
	$('.category-wrap').on('click', '.row-product-category.now .delete', function(e){//delete from categories that are already stored
		var target = e.currentTarget;
		$.confirm('Are you sure?', function(){
			$.ajax({
				url: deleteUrl,
				type: 'POST',
				data: {productCategoryId: target.dataset.id},
				dataType: 'json',
				success: function(data) {
					if (data.success) {
						$.toast('Deleted!');
						getList();//call getList() function to immediatly update list and sort by priority upon succesful submit
					} else {
						$.toast('Deletion failed!');
					}
				}
			});
		});
	});
});