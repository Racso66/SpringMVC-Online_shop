$(function(){
	var url = '/o2o/frontend/listfrontpageinfo';
	//from frontpage info, retrieve all head line and first level category list
	$.getJSON(url, function(data){
		if(data.success){
			var headLineList = data.headLineList;
			var swiperHtml = '';
			//traverse head line list and concatinate slide show
			headLineList.map(function(item, index){
				swiperHtml += '<div class = "swiper-slide img-wrap">' + '<a href = "'
					+ item.lineLink + '" external><img class = "banner-img" src = "'
					+ item.lineImg + '" alt = "' + item.lineName + '"></a>' + '</div>'
			});
			//apply slide show to front-end html control
			$('.swiper-wrapper').html(swiperHtml);
			//set slide show image swap time to 3 seconds
			$('.swiper-container').swiper({
				autoplay : 3000,
				//whether or not stop autoplay when user operates on slide show
				autoplayDisableOnInteraction : false
			});
			var shopCategoryList = data.shopCategoryList;
			var categoryHtml = '';
			//traverse category list and concat 2 row into one line (each holds 50% of screen width)
			shopCategoryList.map(function(item, index){
				categoryHtml += '<div class="col-50 shop-classify" data-category='
						+ item.shopCategoryId + '>' + '<div class="word">'
						+ '<p class="shop-title">' + item.shopCategoryName + '</p>' 
						+ '<p class="shop-desc">' + item.shopCategoryDesc + '</p>' 
						+ '</div>' + '<div class="shop-classify-img-warp">'
						+ '<img class="shop-img" src="' + item.shopCategoryImg + '">' 
						+ '</div>' + '</div>';
			});
			$('.row').html(categoryHtml);
		}
	});
	$('#me').click(function(){
		$.openPanel('#panel-right-demo');
	});
	//When one of the row is clicked on, the category clicked on becomes parent and shows shop under it.
	$('.row').on('click','.shop-classify', function(e){
		var shopCategoryId = e.currentTarget.dataset.category;
		var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
		window.location.href = newUrl;
	});
});
