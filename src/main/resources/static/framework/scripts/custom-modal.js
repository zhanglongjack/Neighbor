$(function() {
	modifyModal();
});

function modifyModal(){
	console.log("初始化模态框")
	$("#ModifyModal").on("shown.bs.modal", function() {  
		$("#ModifyModalSubmit").click(function(){
			var bootstrapValidator = $("#modifyDataFrom").data("bootstrapValidator");
			$('#modifyDataFrom').data('bootstrapValidator').resetForm();
			bootstrapValidator.validate();
			if(bootstrapValidator.isValid()){
				$(this).attr("disabled","true");
				var targetUrl = pageQueryParams.preUrl+"/"+$("#modifyDataFrom #modifyModel").val();
				var id = $("#modifyDataFrom #modifyId").val();
				var dataParams = $("#modifyDataFrom").serialize();     
				$.ajax({ 
					type:'post',  
					url:targetUrl, 
					cache: false,
					data:dataParams,  
					dataType:'json', 
					success:function(data){  
						if(data.errorCode==0){
							toastr.success(id ? "更新成功!":"添加成功!"); 
							render(pageQueryParams);  // 渲染页面函数
							$('#ModifyModal').modal('toggle')
						}else{
							toastr.error(id ? "更新失败!":"添加失败!"); 
						}
						//window.location.href="/custInfoView?index="+currPage+"&pageSize="+currPageSize;
					},
					error:function(data){ 
						if(data.responseJSON){
							toastr.error(dataParams+"添加失败:"+data.responseJSON.message); 
						}
						
					}
				});
				
				$(this).removeAttr("disabled");
			}else{
				//错误消息提示，默认背景为浅红色 
				toastr.warning("请填写完整资料再提交");  
			}
		});
	}); 

}


var initTimePicker =function(){
	$('[datepicker-moth-id]').datepicker({
		format: "yyyymm",
		language: "zh-CN",
		minViewMode: 1,
		maxViewMode: 1,
		autoclose: true
	});
	
	$('#datepicker').datepicker({
	    format: "yyyy-mm-dd",
	    language: "zh-CN",
	    maxViewMode: 2,
	    autoclose: true,
	    todayBtn: "linked",
	    todayHighlight: true
	});
	
	$('[datepicker-flag]').datepicker({
		format: "yyyymmdd",
		language: "zh-CN",
		maxViewMode: 2,
		autoclose: true,
		todayBtn: "linked",
		todayHighlight: true
	});
	
	$('#datetimepicker').datetimepicker({
		format: 'yyyy-mm-dd hh:ii',
		startDate: "2018-01-01 00:00",
	    language: "zh-CN",
	    autoclose: true,
	    todayHighlight: true,
	    pickerPosition: "bottom-left"
	});
}
