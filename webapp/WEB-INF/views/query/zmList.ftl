<@com.html title="不动产登记业务管理系统" import="ace">
<style>
    /*日期表单样式*/
    .input-icon {
        width: 100%;
    }
    .chosen-container-single .chosen-single {
        height: 35px;
    }
</style>
<script type="text/javascript">
    //修改权限
    var hasEdit=false;
    //删除权限
    var hasDel=false;
    //获取参数
    function getUrlVars()  {

        var vars = [], hash;
        var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
        for(var i = 0; i < hashes.length; i++)
        {
            hash = hashes[i].split('=');
            vars.push(hash[0]);
            vars[hash[0]] = hash[1];
        }
        return vars;
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
            'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }
    $(function () {
        //证明按钮权限控制
        $.each(${authorList!},function(index,data){
            var elementName = data.elementName;
            $("button[name='"+elementName+"']").parent().removeClass("hidden");
            if(!hasDel && elementName=='del'){
                hasDel=true;
            }
            if(!hasEdit && elementName=='edit'){
                hasEdit=true;
            }
        })
        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
        //水印文字
        $(".watermarkText").watermark();
        //证明搜索事件
        $("#search").click(function () {
            var zmid = $("#search_zmmc").val();
            $("#gjSearchForm")[0].reset();
            var Url = "${bdcdjUrl}/zm/getZmPagesJson?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {zmid:zmid});
        });

        //证明高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/zm/getZmPagesJson?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {zmid:""});
        });
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});
        //证明高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
        });
        //证明高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
        });
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/zm/getZmPagesJson",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'ZMID'},
            colNames:['证明编号','证明类型','证明受理时间','证明受理人','证明申请人','操作'],
            colModel:[
                {name:'ZMID', index:'ZMID', width:'20%', sortable:false, hidden:true},
                {name:'MC', index:'MC', width:'15%', sortable:false},
                {name:'ZMSLSJ', index:'ZMSLSJ', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (!cellvalue) {
                        return"";
                    }
                    var value = cellvalue;
                    var data = new Date(value).Format("yyyy-MM-dd");
                    return data;
                }},
                {name:'ZMSLR', index:'ZMSLR', width:'15%', sortable:false},
                {name:'ZMSQR', index:'ZMSQR', width:'15%', sortable:false},
                {name:'mydy', index:'', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    var str='<div style="margin-left:25px;"> ';
                    if(hasEdit){
                        str+='<div title="编辑所选记录" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="EditZm(\'' + rowObject.ZMID + '\',\'' + rowObject.DM + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ui-icon-pencil" name="edit"></span></div>';
                    }
                    if(hasDel){
                        str+='<div title="删除所选记录" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="DelZm(\'' + rowObject.ZMID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ui-icon-trash" name="del"></span></div>';
                    }
                    str+= ' <div title="打印所选记录" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="printZm(\'' + rowObject.ZMID + '\',\'' + rowObject.DM + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ui-icon ace-icon fa fa-print blue"></span></div></div>';
                    return str;
                }
                }
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
                }, 0);
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
        $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
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
    });
    //打开窗口
    function openWin(url){
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, "", "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    //删除项目
    function DelZm(zmid) {
        $.blockUI({ message:"请稍等……" });
        bootbox.dialog({
            message:"是否删除？",
            title:"",
            closeButton:false,
            buttons:{
                success:{
                    label:"确定",
                    className:"btn-success",
                    callback:function () {
                        $.getJSON("${bdcdjUrl!}/zm/delBdcZm?id=" + zmid, {}, function (jsonData) {
                            setTimeout($.unblockUI, 10);
                            alert(jsonData.result);
                            $("#grid-table").trigger("reloadGrid");//重新加载JqGrid
                        })
                    }
                },
                main:{
                    label:"取消",
                    className:"btn-primary",
                    callback:function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    }
    //打印证明
    function printZm(zmid,zmlx) {
        if(zmid!=""&&zmlx!=""){
            if(zmlx=="wfzm"){
                var url="${reportUrl}/ReportServer?reportlet=print%2Fbdc_zm_"+zmlx+".cpt&op=form&zmid="+zmid;
            }
            else{
                var url="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_zm_"+zmlx+".cpt&op=form&zmid="+zmid;}
            openWin(url);
        }
    }
    //编辑证明
    function EditZm(zmid,zmlx) {
        if(zmid!=""&&zmlx!=""){
            var url="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_zm_"+zmlx+".cpt&op=write&zmid="+zmid;
            openWin(url);
        }
    }
    //新建证明
    function creatZm(zmlx) {
        $.getJSON("${bdcdjUrl!}/zm/creatBdcZm?zmlx="+zmlx, {}, function (jsonData) {
            if(zmlx!=""){
                var url="${reportUrl}/ReportServer?reportlet=edit%2Fbdc_zm_"+zmlx+".cpt&op=write&zmid="+jsonData.zmid;
                openWin(url);
            }
        })
    }
    //搜索
    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_zmmc" data-watermark="请输入证明编号">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td style="border: 0px">&nbsp;</td>
                    <td>
                        <button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>
                    </td>
                </tr>
            </table>
        </div>
        <div class="tableHeader">
            <ul>
                <#list zmmcList as zmmc>
                    <li class="hidden">
                        <button type="button" onclick="creatZm('${zmmc.DM}')" name="${zmmc.DM}" >
                            <i class="ace-icon fa fa-pencil-square-o"></i>
                            <span>${zmmc.MC}</span>
                        </button>
                    </li>
                </#list>
            </ul>
        </div>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="gjSearchPop">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级搜索</h4>
                <button type="button" id="proHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="gjSearchForm">
                    <div class="row">
                        <div class="rowLabel col-xs-2">
                            <label>证明受理人：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <input type="text" name="zmslr" class="form-control">
                        </div>
                        <div class="rowLabel col-xs-2">
                            <label>证明类型：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <select name="zmlx" class="form-control">
                                <option></option>
                                <#list zmmcList as zmmc>
                                    <option value="${zmmc.DM}">${zmmc.MC}</option>
                                </#list>
                            </select>

                        </div>
                    </div>
                    <div class="row">
                        <div class="rowLabel col-xs-2">
                            <label>起始日期：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="zmqsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                        </div>
                        <div class="rowLabel col-xs-2">
                            <label>结束日期：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                        <span class="input-icon">
                             <input type="text" class="date-picker form-control" name="zmjsrq"
                                    data-date-format="yyyy-mm-dd">
                            <i class="ace-icon fa fa-calendar"></i>
                        </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="rowLabel col-xs-2">
                            <label>证明申请人：</label>
                        </div>
                        <div class="rowContent col-xs-4">
                            <input type="text" name="zmsqr" class="form-control">
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
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
