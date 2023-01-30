$(function(){
	var loading = false;
	//maximum items allowed changes according to shop
	var maxItems = 20;
	var pageSize = 3;
	var listUrl = '/o2o/frontend/listproductsbyshop';
	var pageNum = 1;
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';
	var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;
	//render shop information and product category list for searching
	getSearchDivData();
	//preload initial product information
	addItems(pageSize,pageNum);
	
	function getSearchDivData() {
		var url = searchDivUrl;
		$.getJSON(url, function(data) {
			if (data.success) {
				// get shopCategoryList from back-end
				var shop = data.shop;

				$('#shop-cover-pic').attr('src', shop.shopImg);
				$('#shop-update-time').html(new Date(shop.lastEdited).Format("yyyy-MM-dd"));
				$('#shop-name').html(shop.shopName);
				$('#shop-desc').html(shop.shopDesc);
				$('#shop-addr').html(shop.shopAddr);
				$('#shop-phone').html(shop.phone);
				//retrieve product category of this shop from back-end
				var productCategoryList = data.productCategoryList;
				var html = '';
				//traverse product list and concat <a>
				productCategoryList.map(function(item, index) {
					html += '<a href="#" class="button" data-product-search-id='
						+ item.productCategoryId + '>' + item.productCategoryName
						+ '</a>';
				});
				$('#shopdetail-button-div').html(html);
			}
		});
	}
	
	function addItems(pageSize, pageIndex){
		//concat search url
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize=' + pageSize
			+ '&productCategoryId=' + productCategoryId + '&productName=' + productName
			+ '&shopId=' + shopId;
		//set loading, still retrieving data, prevent reload
		loading = true;
		//access back-end, retrieve product list based on search condition
		$.getJSON(url, function(data){
			if(data.success){
				maxItems = data.count;
				var html = '';
				//traverse product list, concat card set
				data.productList.map(function(item, index) {
					html += '<div class="card" data-product-id =' + item.productId + '>'
						+ '<div class="card-header">' + item.productName  + '</div>'
						+ '<div class="card-content">'
						+ '<div class="list-block media-list">' + '<ul>'
						+ '<li class="item-content">' + '<div class="item-media">' 
						+ '<img src="' + item.imgAddr + '" width="44">' + '</div>'
						+ '<div class="item-inner">' + '<div class="item-subtitle">'
						+ item.productDesc + '</div>' + '</div>' + '</li>' + '</ul>'
						+ '</div>' + '</div>' + '<div class="card-footer">'
						+ '<p class = "color-gray">Updated on '
						+ new Date(item.lastEdited).Format("yyyy-MM-dd") + '</p>'
						+ '<span>Click to view</span>' + '</div>' + '</div>';
				});
				$('.list-div').append(html);
				//get the number of cards displayed so far, including previously loaded
				var total = $('.list-div .card').length;
				//if cards reach maximum, stop infinite-scroll
				if(total >= maxItems) {
					$.detachInfiniteScroll($('.infinite-scroll'));
					//delete preloader
					$('.infinite-scroll-preloader').remove();
				}
				//if not, page number ++, load new shops
				pageNum += 1;
				//end loading, allow reload
				loading = false;
				//refresh page and display newly loaded shops
				$.refreshScroller();
			}
		});
	}
	
	$(document).on('infinite', '.infinite-scroll-bottom', function(){
		if(loading){
			return;
		}
		addItems(pageSize, pageNum);
	});
	
	//click event for cards, go into product detail
	$('.list-div').on('click', '.card', function(e){
		var productId = e.currentTarget.dataset.productId;
		window.location.href = '/o2o/frontend/productdetail?productId=' + productId;
	});
	
	//if select new product category, reset page number, clear original product list
	$('#shopdetail-button-div').on('click', '.button', function(e){
		productCategoryId = e.target.dataset.productSearchId;
		if(productCategoryId){
			//remove previously selected category(if any), and select newly seleced
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				productCategoryId = '';
			} else {
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			//clear originally displayed product list
			$('.list-div').empty();
			//reset page
			pageNum = 1;
			addItems(pageSize, pageNum);
		}
	});
	
	//when product name condition changes, reset page number, clear shoplist
	$('#search').on('input', function(e){
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	$('#me').click(function(){
		$.openPanel('#panel-right-demo');
	});
	//initialize page
	$.init();
});