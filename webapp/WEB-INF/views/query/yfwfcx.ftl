<@com.html title="不动产单元查询" import="ace,public">
<style>
    .modal-dialog {
        width: 1000px;
    }

        /*高级搜索样式添加 begin*/
        /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 810px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

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
	#sszy { 
		width: 100px; 
		height:100px; 
		background: #fff 
		url(static/img/cf.gif)  
		no-repeat 100% 0; 
		border:0px; cursor:pointer;
		} 
    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
        margin: 0px 5px 0px 0px;
    }
    
       form input[type='button'] {
        border-radius: 4px !important;
        margin: 0px 5px 0px 0px;
        padding-left:8px;
    }

    .form .row {
        margin: 10px 0px 10px 0px;
    }
    .form .row .col-xs-1 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
        width :50px;
    }
    .form .row .col-xs-2 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
        width :85px;
    }
	.form .row .col-xs-3 {
        padding-left: 0px;
        padding-right: 4px;
        text-align: right;
        width :180px;
    }
    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }
        /*重写下拉列表高度*/
    .chosen-container>.chosen-single, [class*="chosen-container"]>.chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script src="${bdcdjUrl}/static/js/readCard.js"></script>
<script type="text/javascript">
var zj='';
$(function () {
  var k='';
    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject) {
        if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
            $(window).resize(function () {
                $.each($(".moveModel > .modal-dialog"), function () {
                    $(this).css("left", ($(window).width() - $(this).width()) / 2);
                    $(this).css("top", "40px");
                })
            })
        }
    }
    $("#print").click(function(){ 
   	var xms='',sfzhs='', zjh='',
    zjhs='';
    var qlrsfzh ='',qlrmc='';
    var qlr=encodeURI(encodeURI($.trim(qlrs)));
    var qlrzjh=encodeURI(encodeURI($.trim(qlrzjhs)));
    var jtrels = encodeURI(encodeURI($.trim(gxrels)));
    sqr = encodeURI(encodeURI($("#search_sqr").val()));
    sqyy = encodeURI(encodeURI($("#search_sqyy").val()));
    if( qlr!='' || qlrzjh!=''){
            Url="${reportUrl}/ReportServer?reportlet=com.fr.function.AnthorCpt"+"&cpturl=/edit/bdc_fwdjxxzm&op=write&cptName=bdc_fwdjxxzm&qlr="+qlr+"&qlrzjh="+qlrzjh+"&jtrels="+jtrels+"&qlrsfzh="+qlrsfzh+"&sqr="+sqr+"&sqyy="+sqyy+"&zj="+zj+'&sj='+new Date().getTime();
            openWin (Url);
     }
     });
     
	//open函数
	function openWin(url, name) {
	var w_width = screen.availWidth - 10;
	var w_height = screen.availHeight - 32;
	window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}
    //项目表高级查询的搜索按钮事件
    $("#gjSearchBtn").click(function (){
       var xms='',sfzhs='', zjh='';
       zjhs='',keys='',gxrels='';
      $("#homeSearchForm input[name=home_xm]").each(function(){
      if($(this).val()==''){
      		tipInfo("姓名不能为空!");
      		 qlrs="";
       		return false;
		  }else if($(this).val()!=''){
       	   xms+=$.trim($(this).val())+",";
       	}
        qlrs=xms;
       })
      $("#homeSearchForm input[name=home_sfzh]").each(function(){
      if($(this).val()==''){
      		tipInfo("证件号不能为空!");
      		 qlrzjhs="";
       		return false;
		  }else if($(this).val()!=''){
       	  zjh=$.trim($(this).val());
          sfzhs+=$.trim($(this).val())+",";
          if(zjh.length==18){
          zjh=zjh.substring(0,6)+zjh.substring(8,17);
          zjhs+="\'"+zjh+"\'"+",";
       	  keys+="\'"+$.trim($(this).val())+"\'"+",";
       	  }
         qlrzjhs=sfzhs;
        } 
       })
      $("#homeSearchForm input[name=home_rel]").each(function(){
      if($(this).val()==''){
      		tipInfo("关系不能为空!");
      		gxrels="";
       		return false;
		  }else if($(this).val()!=''){
       	   gxrels+=$.trim($(this).val())+",";
       	}
       })
      	keys=keys.substring(1,keys.length-2);
      	zjhs=zjhs.substring(1,zjhs.length-2);
        var Url = "${bdcdjUrl}/lsfccx/getFwxxByPagesJson";
        tableReload("grid-table", Url, {xms:xms,sfzhs:sfzhs});
    });
    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "default");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});

    //项目高级搜索关闭事件
    $("#proHide").click(function () {
     	document.getElementById('homeSearchForm').reset();
    	$('#homeSearchForm').html($("#homeSearchForm :first"));
        $("#gjSearchPop").hide();
        $(".chosen-select").trigger('chosen:updated');
    });
    //项目高级查询按钮点击事件
    $("#show").click(function () {
        $("#gjSearchPop").show();
        $(window).trigger('resize.chosen');
        $(".modal-dialog").css({"_margin-left":"25%"});
    });
});
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
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
}

$(function () {
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
//帆软中文乱码
function cjkEncode(text) {                                                                            

           if (text == null) {         

             return "";         

        }         

        var newText = "";         

        for (var i = 0; i < text.length; i++) {         

          var code = text.charCodeAt (i);          

         if (code >= 128 || code == 91 || code == 93) {         

            newText += "[" + code.toString(16) + "]";         

         } else {         

           newText += text.charAt(i);         

         }         

        }         

       return newText;         

      } 
      //弹出模态框
      $('#homeSearch').click(function(){
      zj=''
      $('#gjSearchPop').show();
        count=1,tr='';
      })
      jQuery(grid_selector).jqGrid({
        datatype:"json",
        height:'auto',
        jsonReader:{id:'BDCDYH'},
        colNames:["权利人",'权利人证件号','坐落','用途','房屋结构','建筑面积','分摊面积','BDCDYID'],
        colModel:[
			{name:'QLR', index:'QLR', width:'20%', sortable:false},
			{name:'QLRZJH', index:'QLRZJH', width:'20%', sortable:false},
			{name:'ZL', index:'ZL', width:'20%', sortable:false},
			{name:'YT', index:'YT', width:'10%', sortable:false},
			{name:'FWJG', index:'FWJG', width:'10%', sortable:false},
			{name:'MJ',inde:'JZMJ',width:'10%',sortable:false},
			{name:'FTJZMJ',inde:'FTJZMJ',width:'10%',sortable:false},
			{name:'BDCDYID',inde:'BDCDYID',width:'10%',sortable:false,hidden:true}
		],
        viewrecords:true,
        rowNum:10,
        rowList:[10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
            }, 0);
            var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                   zj+="\'"+data.QLRZJH+"\'"+","
                });
                zj=zj.substring(1,zj.length-2); 
             },
        editurl:"", //nothing is saved
      	caption:"",
        autowidth:true
    })
     })
     
     
//身份证读卡器
function readCardRel(div){
debugger;
    var sfzh_radom="";
    var name_radom="";
	$(div.parent().parent().children()).children().each(function(){
	 if($(this).attr('name')=="home_sfzh"){
	  sfzh_radom=new Date().getTime(); 
	 $(this).attr('id',"sfzh"+sfzh_radom);
	  }
	if($(this).attr('name')=="home_xm"){
	  name_radom=new Date().getTime(); 
	$(this).attr('id',"name"+name_radom);
	  }
	});
   ReadIDCardNew($("#"+"name"+name_radom),$("#"+"sfzh"+sfzh_radom));
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}
	var count=1;
	var tr='',qlrs="", qlrzjhs="",  keys='', zjhs='',gxrels='';
	function add_tr(obj) {
      //增加一行
       	var sum = ++count;
       	
       	if(sum>20){
       	count--;
       	tipInfo("最多只能输入20行!");
       	return false;
       	}else{
            tr = $(addGroup);
            tr.parent().append($("#addGroup").clone(""));
   		var i =1;
   		$("#homeSearchForm input[type=text]").each(function(i){
   		  	 i++; 
   		  	 if(i==(sum*3-1)||i==(sum*3)||i==(sum*3-2)){
   		  	 $(this).val("");
   		  	 }
   		})
  }
  }
  function del_tr(obj) {
  var sum=--count;
  if(sum>=1){ 
  $(obj).parent().parent().remove();
  }else{
  	count++;
    tipInfo("删除失败!");
	return false;
  }
  }
</script>
<div class="space-10"></div>
<div class="main-container">
	<div class="page-content">
		<div class="simpleSearch">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td style="border: 0px">
						<label class="ace-icon " style="width:100px">申请人名称：</label>
					</td>
					<td>
					<input type="text" class="input-icon" id="search_sqr" style="height: 35px;border:0px solid;line-height:35px"
						   data-watermark="请输入姓名">
					</input>	   
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td style="border: 0px">
						<label class="ace-icon " style="width:70px">查询原因：</label>
					</td>
					<td>
						<select id="search_sqyy" style="height:35px;border:0px solid;width:130px">
								<option value="1">单位分房</option>
								<option value="2">房屋拆迁</option>
								<option value="3">公检法核实</option>
								<option value="4">购房核税</option>
								<option value="5">律师核实</option>
								<option value="6">迁户口</option>
								<option value="7">诉讼</option>
								<option value="8">小孩入学</option>
								<option value="9">银行贷款</option>
								<option value="10">住房补贴</option>
								<option value="11">其他</option>
						</select>		
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td class="Search">
						<a href="#" class="search" id="homeSearch">
							新增证件信息
						</a>
					</td>
					<td style="border: 0px">&nbsp;&nbsp;&nbsp;</td>
					<td>
                        <button type="button" class="btn01 AdvancedButton" id="print">打印</button>
                    </td>
				</tr>
			</table>
		</div>
		<!-- 搜索结果 -->
		<table id="grid-table"></table>
		<div id="grid-pager"></div>
	</div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal ">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-105"></i>家庭查询</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa; height:270px; overflow:auto">
                <form class="form advancedSearchTable" id="homeSearchForm">
                    <div class="row" id='addGroup'>
                        <div class="col-xs-2">
                            <label>姓名：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="home_xm" ondblclick="javascript:readCardRel($(this))" id="qlrRel" class="form-control"></input>
                        </div>
                        <div class="col-xs-2">
                            <label>身份证号：</label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="home_sfzh" ondblclick="javascript:readCardRel($(this))" id="sfzhRel" class="form-control"></input>
                        </div>
                        <div class="col-xs-1">
                            <label>关系：</label>
                        </div>
                        <div class="col-xs-2">
						    <input type="text" name="home_rel" id="gxRel" class="form-control"></input>
                        </div>
                        <div class="col-xs-1">
                          <input type="button"   class=" form-control"   style="width:46px;height:35px;background:blue;color:white;margin: 0px 10px 0px 0px;" value="增加" onclick="add_tr(this)" id="addTable" />
                          </div>
                          <div class="col-xs-1">
                          <input type="button"  class=" form-control"  style="width:46px;height:35px;background:red;color:white;margin: 0px 0px 0px 0px" value="删除" id="deleteTable" onclick="del_tr(this)" />
                        </div>
                     </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="gjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="frXms" />
<input type="hidden" id="frSfzhs" />
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
