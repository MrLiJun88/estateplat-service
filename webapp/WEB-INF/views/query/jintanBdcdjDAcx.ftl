<@com.html title="不动产登记业务管理系统" import="ace,public">
<style type="text/css">
        /*高级搜索的样式修改*/
    .btn01:hover {
        background-color: #c7c7c7;
        text-decoration: none;
        color: #333;
    }
    .btn01 {
        display: inline-block;
        padding: 4px 12px;
        margin-bottom: 0;
        font-size: 14px;
        color: #333333;
        text-align: center;
        vertical-align: middle;
        cursor: pointer;
        background-color: #f2f2f2;
        border: 1px solid #aaa;
        webkit-border-radius: 0px !important;
        -moz-border-radius: 0px !important;
        border-radius: 0px !important;
    }
        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 650px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }
        /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;100% !important;
    }
    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }
    .form .row {
        margin: 10px 0px 10px 0px;
    }
    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
    }
    .form .row .col-xs-3 {
        padding-left: 0px;
        padding-right: 10px;
    }
    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }
    label {
        font-weight: bold;
    }
    .wdinput{
    	width:100px;
    	heigth:43px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
	var yg ='yes';
	var canuse=false;
    $(function () {
        try{
            Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
            content=$("#search_xmmc");
        }catch(err){
            alert("请安装Active X控件CaptureVideo.cab");
        }
        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject){
            if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
                $(window).resize(function(){
                    $.each($(".moveModel > .modal-dialog"),function(){
                        $(this).css("left",($(window).width()-$(this).width())/2);
                        $(this).css("top","40px");
                    })
                })
            }
        }
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //项目表高级查询的搜索按钮事件
        <#--$("#gjSearchBtn").click(function () {
        	yg='yes';
        	canuse=false;
        	$("#wdqlrzjh").val(null);
        	$("#wdqlr").val(null);
        	$("#search_xmmc").val(null);
            var Url = "${bdcdjUrl}/wuhubdcdjdacx/queryWuhuDAxxList?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url_3,{dcxc:"",yg:yg});
        })-->

        $('#search_xmmc').keydown(function (event) {
            if (event.keyCode == 13) {
               serch();
            }
        });

        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        });
        /*   文字水印  */
        $(".watermarkText").watermark();

        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });

        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/wuhubdcdjdacx/queryWuhuDAxxListJintan?time="+new Date(),
            datatype:"local",
            height:'auto',
            jsonReader:{id:'BDCDYID'},
            colNames:["收件单号", '申请类型名称', '权利人', '权利人证件号','不动产权证号/预告登记证明号','不动产单元号','房屋坐落','登记时间','查封\抵押状态','查看','ly','zsid'],
            colModel:[
                {name:'SJDH', index:'SJDH', width:'10%', sortable:true},
                {name:'SQLXMC', index:'SQLXMC', width:'10%', sortable:true},
                {name:'QLRMC', index:'QLRMC', width:'5%', sortable:true},
                {name:'QLRZJH', index:'QLRZJH', width:'10%', sortable:true},
                {name:'BDCQZH', index:'BDCQZH', width:'20%', sortable:true},
                {name:'BDCDYH', index:'BDCDYH', width:'20%', sortable:true,formatter:function (cellvalue, options, rowObject) {
                	var value ='';
                	if( cellvalue != null){
                	 value= cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);}
                	return value;
            	}},
                {name:'FWZL', index:'FWZL', width:'15%', sortable:true},
                {name:'DJSJ', index:'DJSJ', width:'10%', sortable:true},
                //{name:'QSZT', index:'QSZT', width:'10%', sortable:false},
                {name:'DYACF', index:'DYACF', width:'10%', sortable:false},
                {name:'ck', index:'', width:'10%', sortable:true, formatter:function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:8px;"> <div title="查看" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="openDetails(\'' + rowObject.BDCDYID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-search  bigger-120 blue"></span></div>' +
                            '</div>'
                }
                },
                {name:'LY', index:'LY', width:'0', sortable:false,hidden:true},
                {name:'BDCDYID', index:'BDCDYID', width:'20%', sortable:false,hidden:true}
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:true,
            rownumWidth:50,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    var replacement =
                    {
                        'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
                    };
                    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                        var icon = $(this);
                        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                    })
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
                 var jqData = $(grid_selector).jqGrid("getRowData");
           		$.each(jqData, function (index, data) {
                  getBdcGDxxQlxx(data.BDCDYID, $(grid_selector), data.BDCDYID);
                  getZszt(data.BDCDYID, $(grid_selector), data.BDCDYID,data.QSZT);
            })
            },
            <#--ondblClickRow:function (rowid) {
           		 var rowData = $('#grid-table').jqGrid('getRowData',rowid);
                 EditXm(rowData.BDCQZH,rowData.LY);
            },-->
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });
    function openWin(url,name){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, name, "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    function openDetails(bdcdyid){
    	var url='${reportUrl!}/ReportServer?reportlet=edit%2Fbdc_djxxcxjg.cpt&op=write&bdcdyid='+bdcdyid+'&time='+new Date();
        openWin(encodeURI(url));
    }
  //跳转到第二个ftl中
 <#-- function EditXm(bdcqzh,ly){
      <#--  window.open('${bdcdjUrl}/bwuhubdcdjdacxdcDjb/fccxDt?proid='+${proid!}+"&bdcdyid"+);
       window.open('${bdcdjUrl}/wuhubdcdjdacx/fccxDt?bdcqzh='+bdcqzh+'&ly='+ly);
    }-->  
    
    //Grid加载完成后第二次请求权利人等其他信息
	function getBdcGDxxQlxx(bdcdyid,table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/wuhubdcdjdacx/getBdcGDxx" ,
        data:{bdcdyid:bdcdyid,yg:yg},
        dataType:"json",
        success:function (result) {
           		table.setCell(rowid, "SJDH", result.SJDH);
				table.setCell(rowid, "SQLXMC", result.SQLXMC);
				table.setCell(rowid, "QLRMC", result.QLRMC);
				table.setCell(rowid, "QLRZJH", result.QLRZJH);
				table.setCell(rowid, "FWZL", result.FWZL);
				table.setCell(rowid, "DJSJ", result.DJSJ);
				table.setCell(rowid, "BDCQZH", result.BDCQZH);
				<#--var value = "";
                if(result.QSZT =='3'){
                	value = '<span class="label label-danger">终止</span>';
                }else 
                if( result.QSZT =='0' ){
                	value = '<span class="label label-success">临时</span>';
                }else
                 if(result.QSZT =='2'){
                	value = '<span class="label label-warning">历史</span>';
                }else{
                	value = '<span class="label label-success">现势</span>';
                }
				table.setCell(rowid, "QSZT", value);-->
				table.setCell(rowid, "BDCDYH", result.BDCDYH);
        }
    });
}
	//证书状态
  	function getZszt(bdcdyid,table, rowid,qszt) {
  	var cellVal = "";
		    $.ajax({
		        type:"GET",
		        url:"${bdcdjUrl}/wuhubdcdjdacx/getBdcqzzt?time="+new Date(),
		        data:{bdcdyid:bdcdyid},
		        dataType:"json",
		        success:function (result) {
		            cellVal = "";
		            if (!result.cf) {
		            cellVal += '<span class="label label-danger">查封</span>';
		            }
		            if (!result.dya) {
		                cellVal += '<span class="label label-warning">抵押</span>';
		            }
		            
		            if (result.cf && result.dya) {
		                cellVal += '<span class="label label-success">正常</span>';
		            }
		              table.setCell(rowid, "DYACF", cellVal);
		        }
	        });
	   
    
}
    /* 调用子页面方法  */
    function showModal(){

        var frame = window.parent;
        while(frame != frame.parent) {
            frameframe=frame.parent;
        }
        frame.postMessage("childCall", "*");
    }

    function search_data(flag){
    	var Url = "${bdcdjUrl}/wuhubdcdjdacx/queryWuhuDAxxListJintan?time="+new Date();
    	if(flag=='search'){
	    	yg='yes';
	    	canuse=false;
	        var dcxc=$("#search_xmmc").val();
	        $("#gjSearchForm")[0].reset();
	        $("#wdqlrzjh").val('');
	        $("#wdqlr").val('');
	        //第一次请求  返回BDCQZH
	        tableReload("grid-table", Url, {dcxc:dcxc,yg:yg});
	    }else if(flag=='wdsearch'){
	    	yg='no';
		 	$("#search_xmmc").val(null);
		 	$("#gjSearchForm")[0].reset();
	        var wdqlrzjh = $("#wdqlrzjh").val();
	        var wdqlr = $("#wdqlr").val();
	        var reg=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        	if(!wdqlrzjh.match(reg)){
        		tipInfo("输入的证件号不正确");
        	}else{
		        if(wdqlrzjh != null && wdqlrzjh != ''  && wdqlr != null && wdqlr != ''){
			        //无档证明查询请求  返回BDCQZH
			        tableReload("grid-table", Url,{wdqlr:wdqlr,wdqlrzjh:wdqlrzjh,yg:yg,dcxc:''});
			        canuse=true;
		       	}else{
		        	tipInfo("请同时输入无档证明权利人名称及其证件号");
		        }
		    }
	    } else if(flag=='gjSearchBtn'){
	    	yg='yes';
        	canuse=false;
        	$("#wdqlrzjh").val('');
        	$("#wdqlr").val('');
        	$("#search_xmmc").val('');
            Url += "&" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url,{dcxc:'',yg:yg,wdqlr:'',wdqlrzjh:''});
	    }
    }

	function wdserch(){
	alert();
	 	yg='no';
	 	$("#search_xmmc").val(null);
	 	$("#gjSearchForm")[0].reset();
        var wdqlrzjh = $("#wdqlrzjh").val();
        var wdqlr = $("#wdqlr").val();
        var reg='/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/';
        if(!reg.test(wdqlrzjh)){
        	tipInfo("输入的证件号不正确");
        }else{
	        if(wdqlrzjh != null && wdqlrzjh != ''  && wdqlr != null && wdqlr != ''){
		        //无档证明查询请求  返回BDCQZH
		        var Url_2 = "${bdcdjUrl}/wuhubdcdjdacx/queryWuhuDAxxListJintan?time="+new Date();
		        tableReload("grid-table", Url_2,{wdqlr:wdqlr,wdqlrzjh:wdqlrzjh,yg:yg});
		        canuse=true;
	        }else{
	        	tipInfo("请同时输入无档证明权利人名称及其证件号");
	        }
        }
        
    }
    function printZm(){
    	if(!canuse){
    		tipInfo("请先进行无档证明搜索操作！");
    	}else{
    		var num=$("#grid-table").jqGrid('getGridParam','records');  
    		if(num !=null && num == 0){
    			var wdqlrzjh = $("#wdqlrzjh").val();
        		var wdqlr = $("#wdqlr").val();
        		var Url="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_dacxzm.cpt&op=write&qlrzjh="+encodeURI(encodeURI(wdqlrzjh))+"&qlr="+encodeURI(encodeURI(wdqlr));
    			openWin(Url);
    		}else if(num !=null && num > 0){
    			tipInfo("查询结果有房，无法进行该操作！");
    		}
    	}
    }
    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
        Url =null;
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container:'body'});
        $(table).find('.ui-pg-div').tooltip({container:'body'});
    }
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }
</script>
<div class="space-10"></div>
<div class="main-container">
    <div class="page-content">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc" data-watermark="请输入模糊搜索条件：权利人名称/权利人证件号码/产权证号/房屋坐落">
                    </td>
                    <td class="Search">
                        <a href="#" id="search" onclick="search_data(this.id)">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px;width:200px">
                        <button type="button"  id="show" style="width:100px">高级搜索</button>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 1px solid;border-color: gray;">
                        <input type="text" style="border: none;" class="wdinput watermarkText" id="wdqlr" name="wdqlr" data-watermark="权利人名称">
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 1px solid;border-color: gray;">
                        <input type="text" style="border: none;" class="wdinput watermarkText" id="wdqlrzjh" name="wdqlrzjh" data-watermark="权利人证件号码">
                    </td >
                    <td style="border: 0px;width:30px;">&nbsp;</td>
                    <td class="Search" style="border:none;">
                        <a href="#" id="wdsearch" onclick="search_data(this.id)">
                            无档证明搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px;width:30px;">&nbsp;</td>
                     <td style="border: 0px;width:200px">
                        <button type="button"  id="printZM" style="width:120px" onclick="printZm()">打印无档证明</button>
                    </td>
                </tr>
            </table>
        </div>
        <!--end  高级搜索 -->
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                	<div class="row">
                        <div class="col-xs-2">
                            <label>权利人：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="qlr" class="form-control">
                        </div>
                        <div class="col-xs-3">
                            <label>权利人证件号：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="qlrzjh" class="form-control">
                        </div>
                        
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>房屋坐落：</label>
                        </div>
                        <div class="col-xs-3">
                        	<input type="text" name="fwzl" class="form-control">
                        </div>
                        <div class="col-xs-3">
                            <label>产权证号：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="cqzh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>申请类型：</label>
                        </div>
                        <div class="col-xs-3">
                            <select name="sqlx" class="form-control" >
                                <option></option>
                                <#list sqlxlist as sqlx>
                                    <option value="${sqlx.DM}">${sqlx.MC}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn" onclick="search_data(this.id)">搜索</button>
            </div>
        </div>
    </div>
</div>
<object id="Capture" style ="width: 100%;height: 100%;border: 5 gray solid;display: none" classid="clsid:9A73DB73-2CA3-478D-9A3F-7E9D6A8D327C" codebase="CaptureVideo.cab#version=1,1,1,9">
    <embed></embed>
</object>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
