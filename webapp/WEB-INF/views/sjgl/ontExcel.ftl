<@com.html title="导入" import="ace,public">
<style>
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
</style>
<script type="text/javascript" src="../static/thirdControl/jquery/plugins/jquery.form.min.js"></script>
<script type="text/javascript">
    $(function(){
        ontInitTable();
        var id = createUUID();
        $("#selectExcel").click(function(){
            $("#impFile").click();
        });
        $("#uploadForm").ajaxForm({
            url:"${bdcdjUrl}/ont/readExcel?type=PL&id=" + id,
            success:function(result){
                tableReload("ont-grid-table","${bdcdjUrl}/ont/getOntXmlData?id=" + id,{});
            }
        });

        $("#export").click(function () {
            var title = "项目创建信息";
            getXlsFromTbl("ont-grid-table", "ontDiv", title, true);
        });
    });

    function tableReload(table,Url,data){
        var jqgrid = $("#"+table);
        jqgrid.setGridParam({url:Url, datatype:'xml',page:1,postData:data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function UUIDCreatePart(length) {
        var uuidPart = "";
        for (var i = 0; i < length; i++) {
            var uuidChar = parseInt((Math.random() * 256), 10).toString(16);
            if (uuidChar.length == 1) {
                uuidChar = "0" + uuidChar;
            }
            uuidPart += uuidChar;
        }
        return uuidPart;
    }

    function createUUID() {
        return UUIDCreatePart(4) + '-' +
                UUIDCreatePart(2) + '-' +
                UUIDCreatePart(2) + '-' +
                UUIDCreatePart(2) + '-' +
                UUIDCreatePart(6);
    }

    function ontInitTable() {
        var grid_selector = "#ont-grid-table";
        var pager_selector = "#ont-grid-pager";

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
            url:"",
            datatype:"xml",
            height:'auto',
            jsonReader:{id:'ID'},
            colNames:["收件号", '申请登记类型','不动产单元号','状态','工作流定义ID','ID','saveFileDir'],
            colModel:[
                {name:'SJH', index:'SJH', width:'5%', sortable:false},
                {name:'SQDJLX', index:'SQDJLX', width:'11%', sortable:false},
                {name:'BDCDYH', index:'BDCDYH', width:'11%', sortable:false},
                {name:'STATE', index:'STATE', width:'20%', sortable:false},
                {name:'WORKFLOWDEFINEID', index:'WORKFLOWDEFINEID', width:'0%', sortable:false,hidden:true},
                {name:'SAVEFILEDIR', index:'SAVEFILEDIR', width:'0%', sortable:false,hidden:true},
                {name:'ID', index:'ID', width:'0%', sortable:false,hidden:true}
            ],
            viewrecords:true,
            rowNum:7,
            rowList:[7, 15, 20],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:false,
            multiselect:false,
            rownumbers:true,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
                var jqData = $("#ont-grid-table").jqGrid("getRowData");
                if(isNotBlank(jqData)){
                    $.each(jqData,function(index,data){
                        createTask(data.ID,data.WORKFLOWDEFINEID,data.SJH,data.BDCDYH,$("#ont-grid-table"),data.SAVEFILEDIR);
                    });
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }

    function createTask(rowid,wdid,sjh,bdcdyh,table,saveFileDir){
        if(isNotBlank(wdid)){
            $.ajax({
                url:"${portalUrl}/taskCenter/createTask?wdid="+wdid,
                type:"POST",
                dataType:"json",
                success:function(result){
                    createProjectEvent(rowid,wdid,result.userid,result.proid,result.wfid,sjh,bdcdyh,table,result.taskId,saveFileDir);
                },
                error:function(){
                    table.setCell(rowid,"STATE","创建项目失败!原因:无法创建此流程!");
                }
            });
        }else{
            table.setCell(rowid,"STATE","创建项目失败!原因:系统不存在此登记流程!");
        }
    }

    function createProjectEvent(rowid,wdid,userid,proid,wfid,sjh,bdcdyh,table,taskId,saveFileDir){
        $.ajax({
            url:"${bdcdjUrl}/wfProject/creatProjectEvent",
            type:"GET",
            data:{wdid:wdid,userid:userid,proid:proid,wfid:wfid},
            success:function(result){
                getFileFromDir(rowid,proid,sjh,bdcdyh,table,taskId,saveFileDir);
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:无法创建此项目!");
            }
        });
    }

    function getFileFromDir(rowid,proid,sjh,bdcdyh,table,taskId,saveFileDir){
        $.ajax({
            url: "${bdcdjUrl}/ont/getFileFromDir",
            type:"GET",
            data:{proid:proid,sjh:sjh,bdcdyh:bdcdyh,saveFileDir:saveFileDir},
            success: function (result) {
                if(isNotBlank(result)){
                    delTask(taskId);
                    table.setCell(rowid,"STATE",result);
                }else{
                    getBdcdyInfo(rowid,proid,sjh,table,taskId);
                }
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:无法获取到附件!");
            }
        });
    }

    function getBdcdyInfo(rowid,proid,sjh,table,taskId){
        $.ajax({
            url:"${bdcdjUrl}/ont/getBdcdyInfo",
            data:{proid:proid,sjh:sjh},
            success:function(result){
                if(result.bdcdyly == 0 && isNotBlank(result.bdcdyhs)) {
                    getDjsjBdcdyInfo(rowid, proid, result.bdcdyhs, result.zdtzm, result.qlxzdm, result.bdclxdm, table,taskId);
                }else if(result.bdcdyly == 1 && isNotBlank(result.bdcdyhs)){
                    getBdcZsInfo(proid,rowid,result.bdcdyhs,result.zdtzm,result.qlxzdm,result.dyfs,result.yqllxdm,result.ysqlxdm,result.bdclxdm,table,'',taskId);
                }else if(result.bdcdyly == 2 && isNotBlank(result.bdcdyhs)){
                    getBdcZsInfo(proid,rowid,result.bdcdyhs,result.zdtzm,result.qlxzdm,result.dyfs,result.yqllxdm,result.ysqlxdm,result.bdclxdm,table,'2',taskId);
                }else{
                    delTask(taskId);
                    table.setCell(rowid,"STATE","创建项目失败!原因:此不动产单元不支持此登记!");
                }
                if(isNotBlank(result.msg)){
                    delTask(taskId);
                    table.setCell(rowid,"STATE","创建项目失败!原因:"+result.msg+"!");
                }
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:获取不动产单元信息失败!");
            }
        });
    }

    function getDjsjBdcdyInfo(rowid,proid,bdcdyhs,zdtzm,qlxzdm,bdclxdm,table,taskId){
        $.ajax({
            url:"${bdcdjUrl}/ont/getDjsjBdcdyInfo",
            data:{bdcdyhs:bdcdyhs,zdtzm:zdtzm,qlxzdm:qlxzdm,bdclxdm:bdclxdm},
            success:function(result){
                if(isNotBlank(result)){
                    $.each(result,function(index,data){
                        djsjEditXm(proid,data.ID,data.BDCLX,data.BDCDYH,rowid,table,taskId);
                    });
                }else{
                    delTask(taskId);
                    table.setCell(rowid,"STATE","创建项目失败!原因:查无此不动产单元!");
                }
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:无法获取不动产单元信息!");
            }
        });
    }

    function getBdcZsInfo(proid,rowid,bdcdyhs,zdtzm,qlxzdm,dyfs,yqllxdm,ysqlxdm,bdclxdm,table,bdcdyly,taskId){
        $.ajax({
            url:"${bdcdjUrl}/ont/getBdcZsInfo",
            data:{bdcdyhs:bdcdyhs,zdtzm:zdtzm,qlxzdm:qlxzdm,dyfs:dyfs,yqllxdm:yqllxdm,ysqlxdm:ysqlxdm,bdclxdm:bdclxdm},
            success:function(result){
                if(isNotBlank(result)){
                    $.each(result,function(index,data){
                        ywsjEditXm(proid,data.PROID, data.BDCDYH, data.BDCDYID,data.BDCQZH,data.BDCLX,rowid,table,taskId);
                    });
                }else{
                    if(bdcdyly == '2'){
                        getDjsjBdcdyInfo(rowid, proid, bdcdyhs, zdtzm, qlxzdm, bdclxdm, table,taskId);
                    }else{
                        delTask(taskId);
                        table.setCell(rowid,"STATE","创建项目失败!原因:查无此不动产证书!");
                    }
                }
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:无法获取证书信息!");
            }
        });
    }

    function djsjEditXm(proid,djid, bdclx, bdcdyh,rowid,table,taskId) {
        $.ajax({
            url:'${bdcdjUrl}/wfProject/checkBdcXm',
            type:'post',
            dataType:'json',
            data:{proid:proid, bdcdyh:bdcdyh, djId:djid},
            success:function (result) {
                var checkMsg = "创建项目成功!";
                if(result.length == 0){
                    djsjInitVoFromOldData(proid, djid, bdclx, bdcdyh,taskId);
                }else{
                    delTask(taskId);
                    checkMsg = "创建项目失败!原因:";
                    $.each(result,function(index,data){
                        checkMsg += data.checkMsg;
                    });
                }
                table.setCell(rowid,"STATE",checkMsg);
            },
            error:function(){
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:项目验证有误!");
            }
        });
    }

    function djsjInitVoFromOldData(proid, djid, bdclx, bdcdyh,taskId) {
        $.ajax({
            type:'post',
            url:'${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + djid + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh,
            success:function (data) {
                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        async: true,
                        url: '${bdcdjUrl}/wfProject/updateWorkFlow?proid=' + proid,
                        success: function (data) {

                        },
                        error:function(){
                            delTask(taskId);
                        }
                    });
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                delTask(taskId);
                if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }
                setTimeout($.unblockUI, 10);
            }
        });
    }

    function ywsjEditXm(proid,id, bdcdyh, bdcdyid,bdcqzh,bdclx,rowid,table,taskId) {
        $.ajax({
            url:'${bdcdjUrl}/wfProject/checkBdcXm',
            type:'post',
            dataType:'json',
            data:{proid:proid, yxmid:id, bdcdyh:bdcdyh},
            success:function (result) {
                var checkMsg = "创建项目成功!";
                if(result.length == 0){
                    ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid,bdcqzh,bdclx,taskId);
                }else{
                    delTask(taskId);
                    checkMsg = "创建项目失败!原因:";
                    $.each(result,function(index,data){
                        checkMsg += data.checkMsg;
                    });
                }
                table.setCell(rowid,"STATE",checkMsg);
            },
            error:function (data) {
                delTask(taskId);
                table.setCell(rowid,"STATE","创建项目失败!原因:项目验证有误!");
            }
        });
    }

    function ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx,taskId) {
        $.ajax({
            type: 'post',
            url: '${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx,
            success: function (data) {
                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        url: '${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                        success: function (data) {

                        },
                        error:function(){
                            delTask(taskId);
                        }
                    });
                } else {
                    delTask(taskId);
                    alert(data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                delTask(taskId);
                if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }
            }
        });
    }

    function delTask(taskId){
        var platformUrl = '${platformUrl!}';
        var msgs = '';
        if(isNotBlank(platformUrl)){
            $.ajax({
                type:"POST",
                url:platformUrl+"/task!del.action?taskid=" + taskId,
                async: false,
                success:function(msg){
                    msg = msg.replace(/(^\s*)|(\s*$)/g,"");
                    if (msg == 'true' || msg=='1'){
                        msgs += "任务删除成功";
                    }else{
                        msgs += "任务删除失败，请联系管理员！错误码："  + "[" + msg + "]";
                    }
                },
                error:function(msg){
                    if (msg){
                        msgs += "任务删除失败!";
                    }
                }
            });
        }
    }

    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140',
            'ui-icon ui-icon-plus': 'ace-icon fa fa-plus bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
    }

    function getXlsFromTbl(inTblId, inTblContainerId, title, rownumbers) {
        try {
            var allStr = "";
            var curStr = "";
            //alert("getXlsFromTbl");
            if (inTblId != null && inTblId != "" && inTblId != "null") {
                curStr = getTblData($('#' + inTblId), $('#' + inTblContainerId), rownumbers);
            }
            if (curStr != null) {
                allStr += curStr;
            }
            else {
                alert("你要导出的表不存在！");
                return;
            }
            var fileName = getExcelFileName(title);
            doFileExport(fileName, allStr);
        }
        catch (e) {
            alert("导出发生异常:" + e.name + "->" + e.description + "!");
        }
    }
    function getTblData(curTbl, curTblContainer, rownumbers) {
        var outStr = "";
        if (curTbl != null) {
            var rowdata = curTbl.getRowData();
            var Lenr = 1;

            for (i = 0; i < Lenr; i++) {
                //var Lenc = curTbl.rows(i).cells.length;
                var th;
                if (rownumbers == true) {
                    th = curTblContainer.find('TH:not(:first-child)');
                }
                else {
                    th = curTblContainer.find('TH');
                }
                th.each(function(index, element) {
                    //alert($(element).text());
                    //取得每行的列数
                    if(index < 4){
                        var j = index + 1;
                        var content = $(element).text();
                        //alert(j + "|" + content);
                        outStr += content + "\t";
                    }
                    //赋值
                });
                outStr += "\r\n";
            }
            var tmp = "";
            for (i = 0; i < rowdata.length; i++) {
                rowdata[i].ID = '';
                rowdata[i].WORKFLOWDEFINEID = '';
                var row = eval(rowdata[i]);
                for (each in row) {
                    outStr += row[each] + "\t";
                }
                outStr += "\r\n";
            }
        }
        else {
            outStr = null;
            alert(inTbl + "不存在!");
        }
        return outStr;
    }
    function getExcelFileName(title) {
        var d = new Date();
        var curYear = d.getYear();
        var curMonth = "" + (d.getMonth() + 1);
        var curDate = "" + d.getDate();
        var curHour = "" + d.getHours();
        var curMinute = "" + d.getMinutes();
        var curSecond = "" + d.getSeconds();
        if (curMonth.length == 1) {
            curMonth = "0" + curMonth;
        }
        if (curDate.length == 1) {
            curDate = "0" + curDate;
        }
        if (curHour.length == 1) {
            curHour = "0" + curHour;
        }
        if (curMinute.length == 1) {
            curMinute = "0" + curMinute;
        }
        if (curSecond.length == 1) {
            curSecond = "0" + curSecond;
        }
        var fileName = title + "_" + curYear + curMonth + curDate + "_"
                + curHour + curMinute + curSecond + ".csv";
        //alert(fileName);
        return fileName;
    }
    function doFileExport(inName, inStr) {
        var xlsWin = null;
        if (!!document.all("HideFrm")) {
            xlsWin = document.getElementById('HideFrm').contentWindow.document ;
        }
        else {
            var width = 6;
            var height = 4;
            var openPara = "left=" + (window.screen.width / 2 - width / 2)
                    + ",top=" + (window.screen.height / 2 - height / 2)
                    + ",scrollbars=no,width=" + width + ",height=" + height;
            xlsWin = window.open("", "_blank", openPara);
        }
        xlsWin.write(inStr);
        xlsWin.execCommand('Saveas', true, inName);
        xlsWin.close();
    }
</script>
<div>
    <div class="space-10"></div>
    <div class="main-container">
        <div class="page-content">
            <div class="row">
                <div class="tabbable">
                    <div class="tab-content">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <form method="post" enctype="multipart/form-data" id="uploadForm">
                                            <input type="file" id="impFile" name="multipartFile"  style="float: left"/>
                                            <input type="submit" class="btn01 AdvancedButton" value="读取压缩包" style="float: left">
                                        </form>
                                    </td>
                                    <td style="border: 0">&nbsp;&nbsp;</td>
                                    <td>
                                        <button type="button" class="btn01 AdvancedButton" id="export">导出</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div id="ontDiv">
                            <table id="ont-grid-table"></table>
                            <div id="ont-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<iframe id="HideFrm" style="display: none"></iframe>
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>