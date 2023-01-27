$(function(){
	//whether or not page is loading, preventss repetetive loading
	var loading = false;
	//maximum shops allowed to return, exceeding will block access
	var maxItems = 999;
	var pageSize = 3;
	var listUrl = '/o2o/frontend/listshops';
	var searchDivUrl = '/o2o/frontend/listshopspageinfo';
	var pageNum = 1;
	var parentId = getQueryString('parentId');
	var areaId = '';
	var shopCategoryId = '';
	var shopName = '';
	//render shop category list and area list for searching
	getSearchDivData();
	//load initial shop information
	addItems(pageSize, pageNum);
	
	function getSearchDivData(){
		var url = searchDivUrl + '?' + 'parentId=' + parentId;
		$.getJSON(url, function(data){
			if(data.success){
				var shopCategoryList = data.shopCategoryList;
				var html = '';
				html += '<a href = "#" class = "button" data-category-id = "">All categories</a>';
				//traverse shop category list and concatinate into <a> tags
				shopCategoryList.map(function(item, index){
					html += '<a href = "#" class = "button" data-category-id ='
						+ item.shopCategoryId + '>' + item.shopCategoryName
						+ '</a>';
				});
				$('#shoplist-search-div').html(html);
				var selectOption = '<option value = "">All areas</option>';
				//get area info list
				var areaList = data.areaList;
				//traverse area list and concatinate <option> tags
				areaList.map(function(item, index){
					selectOption += '<option value = "' + item.areaId
					+ '">' + item.areaName + '</option>';
				});
				$('#area-search').html(selectOption);
			}
		});
	}
	
	function addItems(pageSize, pageIndex){
		//concat url for searching
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize=' + pageSize
			+ '&parentId=' + parentId + '&areaId=' + areaId + '&shopCategoryId='
			+ shopCategoryId + '&shopName=' + shopName;
		//prevent from reloading if backend is still loading data
		loading = true;
		//visit backend to retrieve shop list according to search condition above
		$.getJSON(url, function(data){
			if(data.success){
				//retrieve shop count under the search condition
				maxItems = data.count;
				var html = '';
				data.shopList.map(function(item, index){
					html += '<div class="card" data-shop-id="' + item.shopId 
							+ '">' + '<div class="card-header">'
							+ item.shopName + '</div>'
							+ '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'
							+ item.shopImg + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.shopDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEdited).Format("yyyy-MM-dd")
							+ 'Updated on</p>' + '<span>click for more</span>' 
							+ '</div>' + '</div>';
				});
				$('.list-div').append(html);
				//retrieve total number of cards (shop display)
				var total = $('.list-div .card').length;
				//if cards reach maximum, stop infinite-scroll
				if(total >= maxItems) {
					$.detachInfiniteScroll($('.infinite-scroll'));
					//delete preloader
					$('.infinite-scroll-preloader').remove();
				}
				//if not, page number ++, load new shops
				pageNum += 1;
				//end loading
				loading = false;
				//refresh page and display newly loaded shops
				$.refreshScroller();
			}
		});
	}
	//automate event search and load when scrolled to X px away from bottom
	$(document).on('infinite', '.infinite-scroll-bottom', function(){
		if(loading){
			return;
		}
		addItems(pageSize, pageNum);
	});
	//click event for cards
	$('.shop-list').on('click', '.card', function(e){
		var shopId = e.currentTarget.dataset.shopId;
		window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
	});
	//when new category is selected: reset page number, clear showcased shop list
	$('#shoplist-search-div').on('click','.button',function(e){
		if(parentId){//selected category is a child category
			shopCategoryId = e.target.dataset.categoryId;
			//clear originally selected category and select newly selected
			if($(e.target).hasClass('button-fill')){
				$(e.target).removeClass('button-fill');
				shopCategoryId = '';
			} else{
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			//clear shop list before search due to change in search condition
			$('.list-div').empty();
			//reset page number
			pageNum = 1;
			addItems(pageSize, pageNum);
		} else{
			parentId = e.target.dataset.categoryId;
			if ($(e.target).hasClass('button-fill')) {
				$(e.target).removeClass('button-fill');
				parentId = '';
			} else{
				$(e.target).addClass('button-fill').siblings().removeClass('button-fill');
			}
			//clear shop list before search due to change in search condition
			$('.list-div').empty();
			//reset page number
			pageNum = 1;
			addItems(pageSize, pageNum);
			parentId = '';
		}
	});
	//when shop name condition changes, reset page number, clear shoplist
	$('#search').on('input', function(e){
		shopName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	//when area condition changes, reset page number, clear shoplist
	$('#area-search').on('change', function(){
		areaId = $('#area-search').val();
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
