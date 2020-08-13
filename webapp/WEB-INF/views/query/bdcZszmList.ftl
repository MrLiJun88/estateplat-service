<@com.html title="证书证明列表" import="ace,public">
<style xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
    /*移动modal样式*/
    #logSearchPop .modal-dialog {
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

    /*
     zwq 解决帆软的样式和页面上的jqgrid的下标的冲突
    */
    .ui-state-disabled {
        width: 10%;
        opacity:1;
        color: #BBB;
    }

    .print {
        border: 0px !important;
        padding-left: 10px;
    }

    .save {
        border: 0px !important;
        padding-left: 10px;
    }

    pre, input[type="text"] {
        padding: 0px !important;
    }

    .distance {
        width: 10px;
        border: 0px !important;
    }

    .zsbh {

        background-color: #f2f2f2;
        border: 1px solid #aaa !important;
    }

    /*去掉表格横向滚动条*/
    /*.ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }*/

    /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
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

    .form .row .col-xs-4, .col-xs-10 {
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
        width: 100%;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .ui-jqgrid tr.jqgrow td{
        white-space: pre-wrap;
        word-wrap:break-word;
        /*text-overflow : clip;*/
        padding-top:;0;
    }
</style>
<script src="${bdcdjUrl}/static/js/readCard.js"></script>
<script type="text/javascript">
    $(function(){
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });
        // 集成身份证读卡器qlrmc
        $("input[name='zslzr']").on("dblclick", function () {
            var readidarry = [];
            var index = $(this).attr('id');
            var msgid = ReadIDCardNew(index);
            if (msgid != null && msgid != undefined) {
                readidarry.push(msgid);
            }
            if (readidarry != null && readidarry != undefined && readidarry.length > 0) {
                alert(readidarry[0]);
            }
        });
        //查询搜索事件
        $("#search").click(function () {
            var searchInfo = $("#searchInfo").val();
            var logUrl = "${bdcdjUrl}/bdcqz/getBdcZsListByPage";
            tableReload("log-grid-table", logUrl, {sjbh:searchInfo});
        })
        var grid_selector = "#log-grid-table";
        var pager_selector = "#log-grid-pager";
        $(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcqz/getBdcZsListByPage",
            datatype:"json",
            height:'415px',
            jsonReader:{id:'ZSID'},
            colNames:['产权证号', '不动产单元号', '坐落', "权利人", "领证日期",'', 'zsid'],
            colModel:[
                {name:'BDCQZH', index:'BDCQZH', width:'16%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'13%', sortable:false},
                {name:'ZL', index:'ZL', width:'13%', sortable:false},
                {name:'QLRMC', index:'QLRMC', width:'10%', sortable:false},
                {name:'LZRQ', index:'LZRQ', width:'7%', sortable:false,formatoptions: {newformat: 'Y-m-d'}},
                {name: 'rqbl',
                    index: '',
                    width: '6%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<a class="btn btn-link" class="dropdown-toggle" style="padding：0" onclick="editZs(\'' + rowObject.ZSID + '\')">日期补录</a>'
                    }
                },
                {name:'ZSID', index:'ZSID', width:'0%', sortable:false, hidden:true}
            ],
            viewrecords:true,
            cellEdit:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:false,
            rownumWidth:50,
            multiboxonly:false,
            multiselect:false,
            loadComplete:function () {

            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    });
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function editZs(zsid){
        $.ajax({
            type:"GET",
            async:false,
            url:"${bdcdjUrl}/bdcqz/getFzrq?zsid="+zsid,
            dataType:"json",
            success: function (data) {
                if(data && data!="undefined"){
                    $('#zslzrq').val(data.zslzrq);
                    $('#zslzr').val(data.zslzr);
                }
            }
        });
        if(zsid == "undefined"){
            zsid = "";
        }
        $('#zsid').val(zsid);
        $('#sfSearchPop').show();
    }
    function saveFzrqDetail(){
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/bdcqz/saveFzrqInfo",
            data:$("#sfSearchForm").serialize(),
            dataType:"json",
            success: function (data) {
                if(data && data!="undefined") {
                    alert(data.msg);
//                    $('#sfSearchForm').reset();
                }
            }
        })
    }
    function closeSearchDialog(){
        $('#sfSearchPop').hide();
        $('#zslzrq').val('');
        $('#zslzr').val('');
    }
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="searchInfo" data-watermark="请输入收件编号">
                    </td>
                    <td class="Search">
                        <a href="#" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <td class="distance"></td>
                </tr>
            </table>
        </div>
        <table id="log-grid-table"></table>
        <div id="log-grid-pager"></div>
    </div>
</div>
<input type="hidden" id="iframeSrcUrl">
<div class="Pop-upBox moveModel" style="display: none;" id="sfSearchPop">
    <div class="modal-dialog ywsjSearchPop-modal">
        <div class="modal-content" style="width: 80%">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>证书信息补录</h4>
                <button type="button" id="searchHide"  onclick="closeSearchDialog()" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="sfSearchForm">
                    <div class="row" >
                        <div class="col-xs-2">
                            <label>领证人: </label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" name="zslzr" id ="zslzr" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>领证日期: </label>
                        </div>
                        <div class="col-xs-3">
                            <input type="text" class="date-picker form-control" name="zslzrq" id="zslzrq" data-date-format="yyyy-mm-dd" style="width: 150px;">
                        </div>
                        <div class="col-xs-3" style="display: none">
                            <input type="text" name="zsid" id ="zsid" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="save" onclick="saveFzrqDetail()">保存</button>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
