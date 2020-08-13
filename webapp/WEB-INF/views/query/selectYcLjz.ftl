<@com.html title="" import="ace,public">
<style>
    a{
        color: #428bca;
    }
    .tab-content {
        overflow-y: auto;
        height: auto;
    }
        /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
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
        /*移动modal样式*/
    #tipPop .modal-dialog {
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
    .alert {
        font-size: 12px;
        border-radius: 4px;
        padding: 5px;
        margin-bottom: 5px;
    }
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


        /*去掉表格横向滚动条*/
        /*.ui-jqgrid-bdiv{
            overflow-x: hidden!important;
        }*/

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
    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }
    label {
        font-weight: bold;
    }
    #ywsjMulXxGwc .button:hover
    {
        background: url(static/img/Cart_Add.png) repeat 0px -44px;
    }
</style>
<script type="text/javascript">
    var bdcdjUrl = "${serverUrl!}";
    $mulData=new Array();
    $mulRowid=new Array();
    var mulSelectJson={};
    if($.cookie('lpb_cookie')!=null){
        mulSelectJson=JSON.parse($.cookie('lpb_cookie'));
//        alert($.cookie('lpb_cookie'));
    }
//    mulSelectJson.ljzJson.push(ljzJson);
//    mulSelectJson.ljz.name = "ljz1";
//    mulSelectJson.value="111";
//    mulSelectJson.type="item";
//    alert(mulSelectJson);
$(function () {
    /*   文字水印  */
    $(".watermarkText").watermark();

    Array.prototype.remove = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    //搜索事件
    $("#djsj_search_btn").click(function(){
        var djsjUrl = "${bdcdjUrl}/lpb/getYcLpbPagesJson?" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});
    })


    /*高级按钮点击事件 begin*/
    $("#djsjShow").click(function(){
        $("#djsjSearchPop").show();
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function(){
        var djsjUrl = "${bdcdjUrl}/lpb/getYcLpbPagesJson?" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table",djsjUrl,{dcxc:""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    })

    $("#djsjHide,#tipHide,#tipCloseBtn").click(function(){
        if(this.id=="djsjHide"){
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        }else if(this.id=="tipHide" || this.id=="tipCloseBtn"){
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        }
    });
    $(".djsjSearchPop-modal").draggable({opacity: 0.7,handle: 'div.modal-header'});
    //默认初始化表格
    djsjInitTable('');
});
//地籍数据
function djsjInitTable(tableKey){
    var grid_selector='';
    var pager_selector='';
    if(tableKey==''){
        grid_selector = "#djsj-grid-table";
        pager_selector = "#djsj-grid-pager";
    }else {
         grid_selector = "#"+tableKey+"-grid-table";
         pager_selector = "#"+tableKey+"-grid-pager";
    }

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
    });
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
        }
    });
    jQuery(grid_selector).jqGrid({
        datatype:"local",
        height:'auto',
        jsonReader:{id:'FW_DCB_INDEX'},
        colNames:[ '宗地号','小区名称','坐落','操作','dcb',"fwmc"],
        colModel:[
            {name:'LSZD', index:'LSZD', width:'25%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                    if(cellvalue!=null && cellvalue!='')
                        cell= '<a href="javascript:djsjEditXm(\'\',\'\',\'' + rowObject.FW_DCB_INDEX + '\')" title="'+cellvalue+'">'+cellvalue+'</a>';

                    else
                        cell='';
                    return cell;
                }},
            {name:'FWMC', index:'FWMC', width:'30%', sortable:false},
            {name:'ZLDZ', index:'ZLDZ', width:'40%', sortable:false},
            {name:'CZ', index:'', width:'5%', sortable:false,formatter:function (cellvalue, options, rowObject) {
                return '<div style="margin-left:8px;"><div title="查看楼盘表" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="LookLpb(\''+rowObject.FW_DCB_INDEX+'\',\''+rowObject.LSZD+'\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');">'+'<span class="ace-icon fa fa-search  bigger-120 blue"></span></div></div>';
            }},
            {name:'FW_DCB_INDEX', index:'FW_DCB_INDEX', width:'10%', sortable:false,hidden:true},
            {name:'FWMC', index:'FWMC', width:'10%', sortable:false,hidden:true}
        ],
        viewrecords:true,
        rowNum:7,
        rowList:[7, 10, 20, 30],
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:false,
        multiselect:true,
        onSelectAll: function(aRowids,status){
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids,function(i,e){
                var cm = $myGrid.jqGrid('getRowData',e);
                if(cm.FW_DCB_INDEX==e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        $mulData.push(cm);
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            })
            //赋值数量
//            $("#ywsjMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
        },
        onSelectRow:function(rowid,status){
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData',rowid);
            if(cm.FW_DCB_INDEX==rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                var ljzName=cm.FWMC+"("+ cm.LSZD+")";
                addSelectJson(mulSelectJson,ljzName,cm.FW_DCB_INDEX,'item',"true","");
                //赋值数量
//                $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if($(grid_selector).jqGrid("getRowData").length==7){
                $(grid_selector).jqGrid("setGridHeight","auto");
            }else{
                $(grid_selector).jqGrid("setGridHeight","275px");
            }
            $(grid_selector).jqGrid('setGridWidth', $(".tableHeader").width());
            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true,
        beforeSelectRow: function (rowid, e) {

            var $myGrid = $(this),
            i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
            cm = $myGrid.jqGrid('getGridParam', 'colModel');
            if(cm[i].name == 'CZ'){
                return false;
            }else{
                return true;
            }
        }
    });
}

    function djsjSureFun(id) {
        $.cookie('lpb_cookie', '', { expires: -1 });
        mulSelectJson={};
        if($.cookie('lpb_cookie')!=null){
            createXmxx();
        }
        else{
            if($mulData.length==0){
                alert("请至少选择一条数据！");
                return;
            }
            var proid="";
            if ($("#proid").val() != '') {
                proid = $("#proid").val();
            }
            var bdcXmRelList="";
            var yxmids="";
            var dcbIndexs=new Array();
            for(var i=0;i<$mulData.length;i++){
                bdcXmRelList+="&bdcXmRelList["+i+"].proid="+proid;
                if (id == "djsjSure" || id == "djsjMulSureBtn") {
                    bdcXmRelList+="&bdcXmRelList["+i+"].qjid="+$mulData[i].LSZD;
                    //dcbIndexs+="$dcbIndexs["+i+"]="+$mulData[i].FW_DCB_INDEX;
                    dcbIndexs.push($mulData[i].FW_DCB_INDEX);
                }else{
                    yxmids+="&yxmids["+i+"]="+$mulData[i].PROID;
                    bdcXmRelList+="&bdcXmRelList["+i+"].yproid="+$mulData[i].PROID+"&bdcXmRelList["+i+"].ydjxmly=1";
                }
            }
            if (id == "djsjSure"||id == "djsjMulSureBtn") {
                djsjEditXm('',bdcXmRelList,dcbIndexs);
            }
        }
    }
//打开批量信息
   function djsjMulXxFunc(id) {
        $("#modal-backdrop-mul").show();
        var table="";
        if (id == "ywsjMulXx") {
            $("#djsjMulTable").show();
            djsjInitTable("djsj-mul");
            table="djsj-mul-grid-table";
        }
        $("#"+table).jqGrid("clearGridData");
        for(var i=0;i<=$mulData.length;i++){
            $("#"+table).jqGrid('addRowData',i+1,$mulData[i]);
        }
    }
//关闭
    function closeDivMulDjsj(){
        $("#djsj-mul-grid-table").trigger("reloadGrid");
        $("#modal-backdrop-mul").hide();
        $("#djsjMulTable").hide();
    }
    //删除批量
    function deleteMulDjsj(id){
        var ids ;
        var table;
        if (id == "djsjDel") {
            table="#djsj-mul-grid-table";
        }
        ids = $(table).jqGrid('getGridParam','selarrrow');
        for(var i = ids.length-1;i>-1 ;i--) {
            var cm = $(table).jqGrid('getRowData',ids[i]);
            var index;
            if(id == "djsjDel"){
                index=$.inArray(cm.LSZD,$mulRowid);
            }
            $mulData.pop(index);
            $mulRowid.pop(index);
            $(table).jqGrid("delRowData", ids[i]);
        }

    }

function LookLpb(id,lszd){
    var proid="${proid!}";
    <#--location.href ="${bdcdjUrl}/lpb/ycLpb?dcbId="+id+"&proid="+proid+"&mulSelect=${mulSelect!}&showQl=false&type=${type!}&isNotWf=${isNotWf!}&zjgcdyFw=${zjgcdyFw!}";-->
    var url="${bdcdjUrl}/lpb/ycLpb?dcbId="+id+"&lszd="+lszd+"&proid="+proid+"&mulSelect=${mulSelect!}&showQl=false&type=${type!}&isNotWf=${isNotWf!}&zjgcdyFw=${zjgcdyFw!}&isFrist=true";
    openPostPage(url,"","","winLocation");
}

function tableReload(table,Url,data){
    var jqgrid = $("#"+table);
    jqgrid.setGridParam({url:Url, datatype:'json',page:1,postData:data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

//修改项目信息的函数
function djsjEditXm(bdclx,bdcXmRelList,dcbIndexs) {
    var proid='';
    if($("#proid").val()!=''){
        proid=$("#proid").val();
    }
    $.ajax({
        url:'${bdcdjUrl}/wfProject/checkLjz?sfyc=1&proid='+proid+"&dcbIndexs="+dcbIndexs,
        type:'post',
        dataType:'json',
        cache:false,
//        data:{proid:proid,dcbIndexs:dcbIndexs},
        success:function (data) {
            var alertSize=0;
            var confirmSize=0;
            if(data.length>0){
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                $.each(data,function(i,item){
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\''+item.info[0]+'\')" >查看</span>' + item.checkMsg + '</div>');
                    }
                })
                $("#tipPop").show();
                $("#modal-backdrop").show();
            }
            if(alertSize==0 && confirmSize==0){
                djsjInitVoFromOldData(proid,bdclx,bdcXmRelList,dcbIndexs);
            }else if(alertSize== 0 && confirmSize>0){
                $("span[name='hlBtn']").click(function(){
                    $(this).parent().remove();
                    if($("#csdjConfirmInfo > div").size()==0){
                        djsjInitVoFromOldData(proid,bdclx,bdcXmRelList,dcbIndexs);
                    }
                })
            }
        },
        error:function (data) {
            setTimeout($.unblockUI, 10);
        }
    });
}
function djsjInitVoFromOldData(proid,bdclx,bdcXmRelList,dcbIndexs){
    var zjgcdyFw = "${zjgcdyFw!}";
    $.blockUI({message: "请稍等......"});
    //                   独幢或者是多幢时直接创建 返回的是不动产单元号
    if(zjgcdyFw!=null && zjgcdyFw!="" && zjgcdyFw=="2"){
        createZjjzwxx(proid,dcbIndexs,"","");
    }
    else{
        $.ajax({
            type:'get',
            async:false,
            datetype:"String",
            url:'${bdcdjUrl!}/wfProject/initVoFromOldData?proid=' + proid  + "&bdclx=TDFW&dcbIndexs="+dcbIndexs+"&sfyc=1",
            success:function (data) {
                if (data == '成功') {
                    if(zjgcdyFw!=null && zjgcdyFw!="" && zjgcdyFw=="1"){
                        createZjjzwxx(proid,dcbIndexs,"","");
                    }
                    refresh();
                    setTimeout($.unblockUI, 10);

                } else {
                    setTimeout($.unblockUI, 10);
                    tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
                }
            },
            error:function (_ex) {
                setTimeout($.unblockUI, 10);
                //alert("保存失败!失败原因:" + _ex);
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        });
    }
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
//修改项目信息的函数
<#--function selectZd(id,zdzhh) {-->
    <#--var proid="${proid!}";-->
    <#--var zjgcdyFw = "${zjgcdyFw!}";-->
    <#--if(id!=""){-->
        <#--//遮罩-->
        <#--$.blockUI({ message:"请稍等……" });-->
        <#--if(zjgcdyFw!=null && zjgcdyFw!="" && zjgcdyFw=="true"){-->
            <#--$.ajax({-->
                <#--type:"get",-->
                <#--url:"${bdcdjUrl!}/wfProject/initVoFromOldData?proid=" + proid + "&bdclx=TDFW&zdzhh="+zdzhh+"&dcbIndex="+id+"&sfyc=1",-->
                <#--success:function (data) {-->
<#--//                    alert(data);-->

                    <#--//去掉遮罩-->
                    <#--setTimeout($.unblockUI, 10);-->
                    <#--if (data == '成功') {-->
                        <#--window.parent.hideModel();-->
                        <#--window.parent.resourceRefresh();-->
                    <#--}-->
                <#--},-->
                <#--error:function (XMLHttpRequest, textStatus, errorThrown) {-->
                    <#--if(XMLHttpRequest.readyState==4){-->
                        <#--setTimeout($.unblockUI, 10);-->
                        <#--alert("选择失败!");-->
                    <#--}-->
                <#--}-->
            <#--});-->
        <#--}else{-->
            <#--createZjjzwxx(id,zdzhh,proid);-->
        <#--}-->
    <#--}-->
<#--}-->


    function djsjMulXxGwcFunc(id){
        var url="${bdcdjUrl}/lpb/selectMulLjzGwc?proid=${proid!}"+"&mulSelect=${mulSelect!}&showQl=false&type=${type!}&isNotWf=${isNotWf!}&zjgcdyFw=${zjgcdyFw!}&isYc=1";
        var iWidth = 1000;
        var iHeight = 600;
        var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
        var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
        var win = window.open(url, "批量购物车", "width=" + iWidth + ", height=" + iHeight + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no,alwaysRaised=yes,depended=yes");


//        window.showModalDialog(url,"","dialogWidth=1000px;dialogHeight=600px") ;

//        window.parent.parent.showModel(url,"gwc","1000","550",false);
    }


    function refresh(){
        window.parent.hideModel();
        window.parent.resourceRefresh();
    }

</script>
<@script name="static/js/plGwc.js"></@script>
<div class="main-container">
    <div class="space-6"></div>
    <input type="hidden" id="proid" value="${proid!}">
    <div class="page-content">
        <div class="row">
            <div class="simpleSearch">
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <input type="text" class="SSinput watermarkText" id="djsj_search" data-watermark="请输入宗地号/小区名称/坐落">
                        </td>
                        <td class="Search">
                            <a href="#" id="djsj_search_btn">
                                搜索
                                <i class="ace-icon fa fa-search bigger-130"></i>
                            </a>
                        </td>
                        <td style="border: 0px">&nbsp;</td>
                        <td>
                            <button class="btn01 AdvancedButton" id="djsjShow">高级搜索</button>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="tableHeader">
                <ul>
                    <li>
                        <button type="button" id="djsjSure" onclick="djsjSureFun(this.id)">
                            <i class="ace-icon fa fa-file-o"></i>
                            <span>确定</span>
                        </button>
                    </li>
                    <li>
                        <#--<button type="button" id="ywsjMulXx" onclick="djsjMulXxFunc(this.id)">-->
                            <#--<span>已选择</span>-->
                        <#--</button>-->
                    </li>
                    <li >
                        <button type="button" id="ywsjMulXxGwc" onclick="djsjMulXxGwcFunc(this.id)">
                            <i  class="glyphicon glyphicon-shopping-cart"></i>
                            <span>查看已选</span>
                        </button>
                    </li>
                    <li>
                        <button type="button" id="clearGwc" onclick="clearLpbCookie()">
                            <i class="glyphicon glyphicon-trash"></i>
                            <span>清空已选</span>
                        </button>
                    </li>
                </ul>
            </div>
            <table id="djsj-grid-table"></table>
            <div id="djsj-grid-pager"></div>
        </div>
    </div>
</div>

<!--地籍高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="djsjSearchPop">
    <div class="modal-dialog djsjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>地籍高级查询</h4>
                <button type="button" id="djsjHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="djsjSearchForm">
                    <input id="zdtzm" name="zdtzm" type="hidden" value="${zdtzm!}"/>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>小区名称：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="xmjc" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>宗地号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zdh" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="djsjGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--错误提示-->
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo" ></div>
                <div id="csdjConfirmInfo" ></div>
            </div>
            <div class="modelFooter">
                <button class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>

<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-mul"></div>
<div class="Pop-upBox moveModel" style="display: none;" id="djsjMulTable">
    <div class="modal-dialog tipPop-modal mulSelectPop">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">已选择的逻辑幢</h4>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div class="tableHeader">
                    <ul>
                        <li>
                            <button type="button" id="djsjDel" onclick="deleteMulDjsj(this.id)">
                                <i class="ace-icon fa fa-file-o"></i>
                                <span>删除</span>
                            </button>
                        </li>
                    </ul>
                </div>
                <table id="djsj-mul-grid-table"></table>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="djsjMulSureBtn">确定</button>
                <button type="button" class="btn btn-sm btn-primary" id="djsjMulCloseBtn" onclick="closeDivMulDjsj()">关闭</button>
            </div>
        </div>
    </div>
</div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>