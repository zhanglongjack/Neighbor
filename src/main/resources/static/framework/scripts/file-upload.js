    function ajaxUploadImage(v_url,succesCallBack) {

        var formValue="uploadImgForm1";

        var options = {
            url: v_url,
            type: "POST",
            dataType: "json",
            success: function (result) {
            	succesCallBack&&succesCallBack(result);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	toastr.error(msg);
            }
        };
        $("#"+formValue).ajaxSubmit(options);
    }

    
var fileUploadInit = function(url){
	$("#ModifyModal").on("hidden.bs.modal", function() {  
	    $(this).removeData("bs.modal");  
	}); 
	

    $("#input-imprt").fileinput({
        uploadUrl: url,
    	language: 'zh',
    	enctype:'multipart/form-data',
        browseClass: "btn btn-primary btn-block",
        uploadAsync: true, //默认异步上传
        showPreview :true, //是否显示预览
        showCaption: true,
        showRemove: true,//显示移除按钮
        showUpload: true,//是否显示上传按钮
        maxFileCount:1,
        allowedFileExtensions:["xlsx","xls"],
        maxFilePreviewSize: 20240,
        elErrorContainer: "#errorBlock",
    	previewFileIconSettings: {
            'docx': '<i ass="fa fa-file-word-o text-primary"></i>',
            'xlsx': '<i class="fa fa-file-excel-o text-success"></i>',
            'xls': '<i class="fa fa-file-excel-o text-success"></i>',
            'pptx': '<i class="fa fa-file-powerpoint-o text-danger"></i>',
            'jpg': '<i class="fa fa-file-photo-o text-warning"></i>',
            'pdf': '<i class="fa fa-file-archive-o text-muted"></i>',
            'zip': '<i class="fa fa-file-archive-o text-muted"></i>',
        }
    });
    $("#input-imprt").on("fileuploaded", function (event, data, previewId, index) {
		toastr.success("文件上传成功:"+data.message);
		render(pageQueryParams);  // 渲染页面函数
		$('#ModifyModal').modal('toggle');
    }); 
    $('#input-imprt').on('fileerror', function(event, data, msg) {
    	toastr.error(msg);
    });
    
	
}