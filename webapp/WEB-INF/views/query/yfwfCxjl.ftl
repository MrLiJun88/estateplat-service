<@com.html title="查封登记查询" import="ace,public">
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
	width: 750px;
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
form input[type='text'],select,textarea {
	border-radius: 4px !important;
	width: 200px !important;
	margin: 0px 5px 0px 5px;
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

.form .row .col-xs-4 {
	padding-left: 0px;
	padding-right: 0px;
}

label {
	font-weight: bold;
}

/*日期表单样式*/
.dropdown-menu {
	z-index: 10000 !important;
}

.input-icon {
	width: 150px;
}
</style>
<script type="text/javascript">
    $(function () {
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
        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/bdcCf/queryCfdjList?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc:""});
        })
        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
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
            url:"${bdcdjUrl}/yfwfcxjl/queryCxjl",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'JLID'},
            colNames:["查询人姓名",'查询时间','被查询人姓名','被查询人身份证号','查询结果','jlid'],
            colModel:[
                {name:'QLR', index:'QLR', width:'7%', sortable:false},
                {name:'CZSJ', index:'CZSJ', width:'8%', sortable:false},
                {name:'CXRMC', index:'CXRMC', width:'10%', sortable:false},
                {name:'CXRZJH', index:'CXRZJH', width:'10%', sortable:false},
                {name:'XXNR', index:'CXJG', width:'10%', sortable:false},
                {name:'JLID', index:'JLID', width:'10%', sortable:false,hidden :true}
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
                }, 0);
            },
            onCellSelect:function (rowid) {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });
    Date.prototype.Format = function (fmt) {
        var o = {
            "M+":this.getMonth() + 1, //月份
            "d+":this.getDate(), //日
            "h+":this.getHours(), //小时
            "m+":this.getMinutes(), //分
            "s+":this.getSeconds(), //秒
            "q+":Math.floor((this.getMonth() + 3) / 3), //季度
            "S":this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    function openWin(url,name){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, name, "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    /* 调用子页面方法  */
    function showModal(){

        var frame = window.parent;
        while(frame != frame.parent) {
            frameframe=frame.parent;
        }
        frame.postMessage("childCall", "*");
    }

    function serch(){
        var cxjssj = $("#cxjssj").val();
        var cxqssj = $("#cxqssj").val();
        var Url = "${bdcdjUrl}/yfwfcxjl/queryCxjl";
        tableReload("grid-table", Url, {cxqssj:cxqssj,cxjssj:cxjssj});
    }

    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
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
		<div class="simpleSearch" style ="height:50px ;width:800px">
			 <div class="bootbox-body" style="background: #fafafa;">
					<div class="col-xs-2">
						<label>查询时间(起)：</label>
					</div>
					<div class="col-xs-3">
						<span class="input-icon"> <input type="text"
							class="date-picker form-control" name="cxqssj" id = "cxqssj"
							data-date-format="yyyy-mm-dd"> <i
							class="ace-icon fa fa-calendar"></i>
						</span>
					</div>
					<div class="col-xs-2">
							<label>查询时间(至)：</label>
					</div>
					<div class="col-xs-3">
							<span class="input-icon"> <input type="text"
								class="date-picker form-control" name="cxjssj" id = "cxjssj"
								data-date-format="yyyy-mm-dd"> <i
								class="ace-icon fa fa-calendar"></i>
							</span>
					</div>
					<div class="col-xs-2">
						<button type="button" class="btn btn-sm btn-primary" id="search"  onclick="serch()"><i
                            class="ace-icon fa fa-search bigger-130"></i>搜索
                    	</button>
					</div>
				   </div>	
		</div>
		<!--end  高级搜索 -->
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
	</div>
</div>
<!--高级搜索-->
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
