var isRedirect = false;
function handleAjaxError(xhr, textStatus, errorThrown) {
	if (xhr.status == "0") {
		return;
	}
	
	isRedirect = false;
	var msgTxt = "系统错误，请联系系统管理员。";
	if (textStatus == "parsererror") {
		msgTxt = "登录超时或重复登录，请重新登录。";
		isRedirect = true;
	} else if (textStatus == "timeout") {
		msgTxt = "请求超时，请稍后再试。";
	}
	
	var modal = document.getElementById('myModal');
	if (!modal) {
		$("body").append("<div class='modal fade' id='myModal' tabindex='-9999' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>" + 
			"<h4 class='modal-title' id='myModalLabel'>错误</h4></div><div class='modal-body' id='modalbody'></div><div class='modal-footer'><button type='button' class='btn btn-default' data-dismiss='modal'>关闭</button></div></div></div></div>");
	}
	$('#modalbody').html(msgTxt);
	$('#myModal').modal('show');
	$('#myModal').on('hide.bs.modal', function () {
		if (isRedirect) {
			window.location.href = "/jeets/login.jsp";
		}
	});
	return false;
}

function resetJqGridPgInput(maxPage){
	var maxPageAttr = maxPage.split(",");
	if(maxPageAttr.length>1){
		maxPage = "";
		for(var i=0;i<maxPageAttr.length;i++){
			maxPage += maxPageAttr[i];
		}
	}

	var pgInput = parseInt($('.ui-pg-input').val());
	var maxPageN = parseInt(maxPage);
	if(isNaN(pgInput) || isNaN(maxPageN)){
		$('.ui-pg-input').val(1);
	}else{
		if(pgInput > maxPageN){
			$('.ui-pg-input').val(maxPageN > 0 ? maxPageN : 1);
		}else if(pgInput <= 0){
			$('.ui-pg-input').val(1);
		}else{
			$('.ui-pg-input').val(pgInput);
		}
	}

	return;
}

function refreshJqGridFirst(maxPage){
	if( maxPage <=0 && $('.ui-pg-input').val() !=1 ){
		$('.ui-pg-input').val(1);
		$("#searchBtn").trigger("click");
	}
	return;
}

//保存查询表单参数到Cookie
function saveSearchParam(searchFormName){
	var params;
	$.cookie.json = true;
	if( searchFormName == "pxyDiagFormParams" ){
		params = {
			customerCodeCondition:$("#customerCodeCondition").val(),
			customerNameCondition:$("#customerNameCondition").val()
		};
	}else{
		params = $("#searchForm").serializeObject();
	}

	$.cookie(searchFormName, params);
	return params;
}

//从Cookie恢复查询表单参数
function restoreSearchParam(searchFormName){
	$.cookie.json = true;
	var params = $.cookie(searchFormName) || {};

	$.each(params, function(name, value){
		$("#"+name).val(value);
	});

	return params;
}

function showWebWait(){
	var wnd =$(window),doc =$(document);
	if(wnd.height()>doc.height()){ //当高度少于一屏
		wHeight = wnd.height();
	}else{//当高度大于一屏
		wHeight = doc.height(); 
	}
/*
	//创建遮罩背景
	$("body").append(
		"<div id=\"showWebWait\"><div style=\"left:50%;top:50%;position: absolute; z-index: 10002;\"><i class=\"fa fa-spinner fa-spin fa-3x fa-fw yellow\"><\/i></div></div>");

	$("body").find("#showWebWait").width(wnd.width()).height(wHeight).css(
		//{position:"absolute",top:"0px",left:"0px",background:"#333333",filter:"Alpha(opacity=30);",
		{position:"absolute",top:"0px",left:"0px",background:"none",filter:"Alpha(opacity=30);",opacity:"0.8",zIndex:"10001",display:"block"});
*/

	//创建遮罩背景
	$("body").append(
		"<div id=\"showWebWait1\"><div style=\"left:50%;top:50%;position: absolute; z-index: 10002;\">" +
			//"<img src=\"" + getContextPath() + "/static/assets/images/wait.gif\" />" +
			"<i class=\"fa fa-spinner fa-spin fa-3x fa-fw yellow\"><\/i></div></div>" +
		"<div id=\"showWebWait\"><div style=\"left:50%;top:50%;position: absolute; z-index: 10002;\">&nbsp;</div></div>");

	$("body").find("#showWebWait1").width(wnd.width()).height(wHeight).css(
		{position:"absolute",top:"0px",left:"0px",background:"none",filter:"Alpha(opacity=30);",opacity:"0.8",zIndex:"10001",display:"block"});
	$("body").find("#showWebWait").width(wnd.width()).height(wHeight).css(
		{position:"absolute",top:"0px",left:"0px",background:"#333333",filter:"Alpha(opacity=0);",opacity:"0",zIndex:"10001",display:"block"});

	return;
}

function removeWebWait(){
	$("div[id='showWebWait1']").each(function(){
		$(this).remove();
	});
	$("div[id='showWebWait']").each(function(){
		$(this).remove();
	});

/*
	$("#showWebWait").remove();
	if($.trim(parent.$("#showWebWait")) != "") {
		parent.$("#showWebWait").remove();
	}
*/
	return;
}

$(function() {
	$.ajaxSetup({
		error:function(xhr, textStatus, errorThrown) {
			if (xhr.status != "404") {
				handleAjaxError(xhr, textStatus, errorThrown);
			}
		}
	});

	// TODO searchBtn在form内，而且是submit类型的情况，按回车键可能检索两次。
	document.onkeydown = function(event) {
		var e = event || window.event;

		if (e && e.keyCode==13) { //enter键
			var visibility;
			var btnNameBase = "searchBtn";
			var btnName = btnNameBase;
			var i = 0;
			var searBtn;

			do {
				visibility = true;
				searBtn = document.getElementById(btnName);
				if (!searBtn) {
					//alert(btnName+" none");
					visibility = false;
				} else {
					if ($("#"+btnName).css("display") == "none") {
						//alert(btnName + " display none");
						visibility = false;
					} else {
						$("#"+btnName).parents().each(function() {
							if ($(this).css("display") == "none") {
								//alert(btnName + " parents display none");
								visibility = false;
							}
						});
					}
				}

				if (visibility) {
					//alert(btnName+" visibility");
					searBtn.click();
					break;
				}

				// Find next search button
				i = i+1;
				btnName = btnNameBase + i;
				//alert("btnName="+btnName);

			} while(i<15); // Max count to 15(TBD)

		}
		return;
	};

});