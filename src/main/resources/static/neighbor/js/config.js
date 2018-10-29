
//滚动条
mui('.mui-scroll-wrapper').scroll()
mui('.mui-content .mui-switch').each(function() { //循环所有toggle
				//toggle.classList.contains('mui-active') 可识别该toggle的开关状态
				this.parentNode.querySelector('span').innerText = '状态：' + (this.classList.contains('mui-active') ? 'true' : 'false');
				/**
				 * toggle 事件监听
				 */
				this.addEventListener('toggle', function(event) {
					//event.detail.isActive 可直接获取当前状态
					this.parentNode.querySelector('span').innerText = '状态：' + (event.detail.isActive ? 'true' : 'false');
				});
});

//绑定跳转事件
mui(document).on('tap','[data-href]',function(){
  var $url=this.getAttribute("data-href")
   mui.openWindow({
    id:$url,
    url:$url,
    waiting:{
      autoShow:true,//自动显示等待框，默认为true
      title:'正在加载...'
    }
  });
})
//js触发网页跳转
function gotoUrl($url) {
   mui.openWindow({
    id:$url,
    url:$url
  });
}
//建设中
mui(document).on('tap','.btn-future',function(){
  mui.toast("建设中")
})
//文字提示
function future (txt) {
	if (!txt) {
		txt="建设中";
	}
	mui.toast(txt)
}

mui(document).on('tap','[close]',function(){
  mui(".mui-popover").popover('hide')
})