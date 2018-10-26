var pageQueryParams = {
	index: 1,
	pageSize: 10,
	total:0,
	pageUrl:"",
	fromQueryData:null
}
var buildParams = function(){
	if(pageQueryParams.fromQueryData || pageQueryParams.fromQueryData!=null){
		params = $.param(pageQueryParams) + "&" + pageQueryParams.fromQueryData;
	}else{
		params = $.param(pageQueryParams);
	}
	
	return params;
}

//获取二级分类
var getCategorySecondData = function(callback) {
	var params = buildParams();
	$.ajax({
		url: pageQueryParams.pageUrl,
		type: 'POST',
		data: params,
		//dataType: 'json',
		success: function(response) {
			callback && callback(response);
		}
	});
}

var render = function(pageData) {
	pageQueryParams = pageData;
	//setPaginator(currPage, Math.ceil(currTotal / currPageSize), render);
	getCategorySecondData(function(data) {
		$('#main-content').empty();
		$('#main-content').html(data);
		setPaginator(Math.ceil(pageQueryParams.total / pageQueryParams.pageSize), render);
	});
}

var setPaginator = function(pageSum,callback) {
	
	var currPage = pageQueryParams.index;
	var v_pageNum = toastr.options.isYd?3:10;
	if(pageQueryParams.total==0){
		return;
	}
	var options = { // 这个方法调用时，自动在.pagination添加分页li
		/*当前使用的是3版本的bootstrap*/
		bootstrapMajorVersion: 3,
		/*配置的字体大小是小号*/
		size: 'normal',
		/*当前页*/
		currentPage: currPage,
		/*一共多少页*/
		totalPages: pageSum,
		/*页面上最多显示几个含数字的分页按钮*/
		numberOfPages: v_pageNum,
		/*设置显示的样式，默认是箭头	*/
		itemTexts: function(type, page, current) {
			switch(type) {
				case "first": // type值固定
					return '首页';
				case "prev":
					return '上一页';
				case "next":
					return '下一页';
				case "last":
					return '尾页';
				case "page":
					return page;
			}
		},tooltipTitles: function (type, page, current) {

            switch (type) {
            case "first":
                return "跳到首页";
            case "prev":
                return "跳到上一页";
            case "next":
                return "跳到下一页";
            case "last":
                return "跳到最后一页";
            case "page":
                return (page === current) ? "当前是第" + page+"页" : "跳到第" + page+"页";
            }
        },
		onPageClicked: function(event, originalEvent, type, page) {
			//currPage = page; // 注意currPage的作用域
			pageQueryParams.index = page;
			callback && callback(pageQueryParams);
			//window.location.href="/custInfoView?index="+page+"&pageSize="+currPageSize;
			//render(page,pageSize,total);  // 渲染页面函数
			
		}
	}
	$('.pagination').bootstrapPaginator(options);
	
	$.fn.bootstrapPaginator.Constructor.prototype.getPages = function() {

	            var totalPages = this.totalPages;// get or calculate the total pages via the total records
	            var offsetNum =Math.round((this.numberOfPages+ 1) / 2-1);
	            var pageStart = this.currentPage-offsetNum;
	            var output = [], i = 0, counter = 0;
	            pageStart = this.currentPage > totalPages - offsetNum?pageStart-(this.currentPage - (totalPages - offsetNum)):pageStart;
	            pageStart = pageStart < 1 ? 1 : pageStart;//check the range of the page start to see if its less than 1.
	            
	            for (i = pageStart, counter = 0; counter < this.numberOfPages && i <= totalPages; i = i + 1, counter = counter + 1) {//fill the pages
	                output.push(i);
	            }

	            output.first = 1;//add the first when the current page leaves the 1st page.

	            if (this.currentPage > 1) {// add the previous when the current page leaves the 1st page
	                output.prev = this.currentPage - 1;
	            } else {
	                output.prev = 1;
	            }

	            if (this.currentPage < totalPages) {// add the next page when the current page doesn't reach the last page
	                output.next = this.currentPage + 1;
	            } else {
	                output.next = totalPages;
	            }

	            output.last = totalPages;// add the last page when the current page doesn't reach the last page

	            output.current = this.currentPage;//mark the current page.

	            output.total = totalPages;

	            output.numberOfPages = this.options.numberOfPages;

	            return output;

	        }
	
	
}