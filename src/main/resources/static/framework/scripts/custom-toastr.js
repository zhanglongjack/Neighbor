$(function() {
	toastr.options = {  
	    closeButton: false,  
	    debug: false,  
	    progressBar: false,  
	    positionClass: "toast-top-full-width",  
	    onclick: null,  
	    showDuration: "300",  
	    hideDuration: "1000",  
	    timeOut: "2000",  
	    extendedTimeOut: "1000",  
	    showEasing: "swing",  
	    hideEasing: "linear",  
	    showMethod: "fadeIn",  
	    hideMethod: "fadeOut"
	};
	
	var system = {
	    win: false,
	    mac: false,
	    xll: false,
	    ipad:false
	};
	
	//检测平台
	var p = navigator.platform;
	
	system.win = p.indexOf("Win") == 0;
	system.mac = p.indexOf("Mac") == 0;
	system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
	system.ipad = (navigator.userAgent.match(/iPad/i) != null)?true:false;
	if (system.win || system.mac || system.xll ||system.ipad) {
		toastr.options.isYd=false;
		toastr.options.positionClass = "toast-top-right";
	} else {
		toastr.options.isYd=true;
		toastr.options.positionClass = "toast-top-full-width";
	}
	
});

