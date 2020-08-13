<@com.html title="导入" import="ace,public">
<style type="text/css">
    .simpleSearch .SSinput {
        border: 0px;
        height: 34px;
        min-width: 120px;
        font-size: 12px;
        text-indent: 10px;
    }

    /*表单样式重写*/
    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width: 95% !important;
        margin: 0px 5px 0px 5px;
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

    .SSinput {
        min-width: 330px !important;
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

</style>
<script type="text/javascript" src="../static/thirdControl/jquery/plugins/jquery.form.min.js"></script>
<script type="text/javascript">
    $(function () {
        $("#djsjTab,#ywsjTab").click(function () {
            if (this.id == "djsjTab") {
                $("#djsj").addClass("active");
                $("#ywsjHide").click();
                var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
                djsjInitTable();
                tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});
            } else if (this.id == "ywsjTab") {
                $("#ywsj").addClass("active");
                $("#djsjHide").click();
                var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
                ywsjInitTable();
                tableReload("ywsj-grid-table", ywsjUrl, {dcxc:$("#ywsj_search").val()});
            }
        });

        $("#djsjHide,#ywsjHide,#tipHide,#tipCloseBtn").click(function () {
            if (this.id == "djsjHide") {
                $("#djsjSearchPop").hide();
                $("#djsjSearchForm")[0].reset();
            } else if (this.id == "ywsjHide") {
                $("#ywsjSearchPop").hide();
                $("#ywsjSearchForm")[0].reset();
            } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
                $("#modal-backdrop").hide();
                $("#tipPop").hide();
            }
        });
        $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});


        $("#djsj_search_btn").click(function () {
            var bdcdyhs = "${bdcdyhs!}";
            if(isBlank(bdcdyhs)){
                bdcdyhs = "###";
            }
            var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
            tableReload("djsj-grid-table", djsjUrl,{bdcdyhs:bdcdyhs});
        });

        $("#ywsj_search_btn").click(function () {
            var bdcdyhs = "${bdcdyhs!}";
            if(isBlank(bdcdyhs)){
                bdcdyhs = "###";
            }
            var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
            tableReload("ywsj-grid-table", ywsjUrl,{bdcdyhs:bdcdyhs});

        });

        $("#searchCondition").keydown(function (event) {
            if (event.keyCode == 13) {//绑定回车
                $("#search").click();
            }
        });

        $("#impHref").click(function(){
            $("#impFile").click();
        });

        /*   文字水印  */
        $(".watermarkText").watermark();

        //时间控件
        $('.date-picker').datepicker({
            autoclose: true,
            todayHighlight: true,
            language: 'zh-CN'
        }).next().on(ace.click_event, function () {
            $(this).prev().focus();
        });

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
        if ("${bdcdyly!}" == '0') {
            djsjInitTable();
        } else if ("${bdcdyly!}" == '1') {
            ywsjInitTable();
        }

        $("#djUploadForm").ajaxForm({
            url:"${bdcdjUrl}/ont/uncompressZip",
            success:function(){
                alert("确定后,等待页面刷新,点击搜索,查看不动产单元");
                window.location.reload();
            }
        });

        $("#ywUploadForm").ajaxForm({
            url:"${bdcdjUrl}/ont/uncompressZip",
            success:function(){
                alert("确定后,等待页面刷新,点击搜索,查看不动产单元");
                window.location.reload();
            }
        });
    });

    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function enableTooltips(table) {
        $('.navtable .ui-pg-button').tooltip({container: 'body'});
        $(table).find('.ui-pg-div').tooltip({container: 'body'});
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

    function ywsjInitTable() {
        var grid_selector = "#ywsj-grid-table";
        var pager_selector = "#ywsj-grid-pager";
        $('#ywsj_search').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#ywsj_search_btn").click();
            }
        });
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
            datatype:"local",
            height:'auto',
            jsonReader:{id:'PROID'},
            colNames:["不动产单元号", '不动产权证号', '坐落', '权利人','不动产类型'],
            colModel:[
                {name:'BDCDYH', index:'BDCDYH', width:'12%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'' + rowObject.BDCQZH + '\',\'' + rowObject.BDCLX + '\',\'\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }},
                {name:'BDCQZH', index:'BDCQZH', width:'11%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'' + rowObject.BDCQZH + '\',\'' + rowObject.BDCLX + '\',\'\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }},
                {name:'ZL', index:'ZL', width:'10%', sortable:false},
                {name:'QLR', index:'QLR', width:'6%', sortable:false},
                {name:'BDCLX', index:'BDCLX', width:'0%', sortable:false,hidden:true}
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
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }

    function djsjInitTable() {
        var grid_selector = "#djsj-grid-table";
        var pager_selector = "#djsj-grid-pager";

        //绑定回车键
        $('#djsj_search').keydown(function (event) {
            if (event.keyCode == 13) {
                $("#djsj_search_btn").click();
            }
        });
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
            datatype:"local",
            height:'auto',
            jsonReader:{id:'ID'},
            colNames:["地籍号", '不动产单元号', '坐落', '权利人', '不动产类型',"ID"],
            colModel:[
                {name:'DJH', index:'DJH', width:'18%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }},
                {name:'BDCDYH', index:'BDCDYH', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    }
                    else
                        cell = '';
                    return cell;
                }},
                {name:'TDZL', index:'TDZL', width:'20%', sortable:false},
                {name:'QLR', index:'QLR', width:'10%', sortable:false},
                {name:'BDCLX', index:'BDCLX', width:'15%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    var value = "";
                    if (cellvalue != null && cellvalue != "") {
                        if (cellvalue.indexOf('TD') > -1) {
                            if (cellvalue.indexOf('FW') > -1) {
                                value = "土地、房屋等建筑物";
                            } else if (cellvalue.indexOf('GZW') > -1)
                                value = "土地、构筑物";
                            else if (cellvalue.indexOf('SL') > -1)
                                value = "土地、森林、林木";
                            else if (cellvalue.indexOf('QT') > -1)
                                value = "土地、其他";
                            else
                                value = "土地";
                        } else if (cellvalue.indexOf('HY') > -1) {

                            if (cellvalue.indexOf('FW') > -1) {
                                value = "海域、房屋等建筑物";
                            } else if (cellvalue.indexOf('GZW') > -1)
                                value = "海域、构筑物";
                            else if (cellvalue.indexOf('WJM') > -1)
                                value = "海域、无居民海岛";
                            else if (cellvalue.indexOf('SL') > -1)
                                value = "海域、森林、林木";
                            else if (cellvalue.indexOf('QT') > -1)
                                value = "海域、其他";
                            else
                                value = "海域";
                        } else if (cellvalue.indexOf('QT') > -1) {
                            value = "其他";
                        }
                    }
                    return value;
                }},
                {name:'ID', index:'ID', width:'10%', sortable:false, hidden:true}
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
                    //resize
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });
    }

    function djsjEditXm(id, bdclx, bdcdyh) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        $.blockUI({ message:"请稍等……" });
        var options = {
            url:'${bdcdjUrl}/wfProject/checkBdcXm',
            type:'post',
            dataType:'json',
            data:{proid:proid, bdcdyh:bdcdyh, djId:id},
            success:function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                if (data.length > 0) {
                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                    $.each(data, function (i, item) {
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                        if (item.checkModel == "confirm") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning">' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                        }
                    })
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
                if (alertSize == 0 && confirmSize == 0) {
                    djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                } else if (alertSize == 0 && confirmSize > 0) {
                    $("span[name='hlBtn']").click(function () {
                        $(this).parent().remove();
                        if ($("#csdjConfirmInfo > div").size() == 0) {
                            djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                        }
                    })
                }

            },
            error:function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }

    function djsjInitVoFromOldData(proid, id, bdclx, bdcdyh) {
        var initurl='${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh;
        $.ajax({
            type:'post',
            url:initurl,
            success:function (data) {

                if (data == '成功') {
                    $.ajax({
                        type:'get',
                        async:true,
                        url:'${bdcdjUrl}/wfProject/updateWorkFlow?proid=' + proid,
                        success:function (data) {

                        }
                    });
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                } else {
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    alert(data);
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
    }

    function ywsjEditXm(id, bdcdyh, bdcdyid,bdcqzh,bdclx,sqlx) {
        var proid = '';
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var options = {
            url:'${bdcdjUrl}/wfProject/checkBdcXm',
            type:'post',
            dataType:'json',
            data:{proid:proid, yxmid:id, bdcdyh:bdcdyh},
            success:function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                if (data.length > 0 && sqlx!='CF') {
                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                    var alertCount = 0;
                    $.each(data, function (i, item) {
                        if (item.checkModel == "alert") {
                            alertCount++;
                        }
                    });
                    $.each(data, function (i, item) {
                        if (item.checkModel == "confirm") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids+ '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "confirmAndCreate") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + id + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if(item.checkCode=='199'){
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            }else{
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    });
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
                if (alertSize == 0 && confirmSize == 0) {
                    ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid,bdcqzh,bdclx);
                } else if (alertSize == 0 && confirmSize > 0) {
                    $("span[name='hlBtn']").click(function () {
                        $(this).parent().remove();
                        if ($("#csdjConfirmInfo > div").size() == 0) {
                            ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid,bdcqzh,bdclx);
                        }
                    })
                }
            },
            error:function (data) {

            }
        };
        $.ajax(options);
    }

    function ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx) {
        var initurl = "";
        initurl = '${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx;
        $.ajax({
            type: 'post',
            url: initurl,
            success: function (data) {
                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        async: true,
                        url: '${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                        success: function (data) {

                        }
                    });
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                } else {
                    alert(data);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }
            }
        });
    }
</script>
<div class="space-10"></div>
<div class="main-container">
<div class="page-content">
<div class="row">
<div class="tabbable">
    <ul class="nav nav-tabs">
        <#if bdcdyly==0>
            <li class="active">
                <a data-toggle="tab" id="djsjTab" href="#djsj">
                    不动产单元
                </a>
            </li>
        </#if>
        <#if bdcdyly==1>
            <li class="active">
                <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                    产权证
                </a>
            </li>
        </#if>
    </ul>
<div class="tab-content">
    <#if bdcdyly==0>
    <div id="djsj" class="tab-pane in active">
    </#if>
    <#if bdcdyly==1>
    <div id="djsj" class="tab-pane">
    </#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="searchCondition"
                           data-watermark="地籍号/不动产单元号">
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td class="Search">
                    <a href="#" id="djsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <form method="post" enctype="multipart/form-data" id="djUploadForm">
                    <input type="text" name="proid" value="${proid!}" hidden/>
                    <input type="text" name="wiid" value="${wiid!}" hidden/>
                    <td class="Search">
                        <input type="file" id="impFile" name="multipartFile"/>
                    </td>
                    <td>
                        <input type="submit" value="读取压缩包">
                    </td>
                </form>
            </tr>
        </table>
    </div>
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>
    <#if bdcdyly==0>
    <div id="ywsj" class="tab-pane">
    </#if>
    <#if bdcdyly==1>
    <div id="ywsj" class="tab-pane in active">
    </#if>
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="ywsj_search"
                           data-watermark="请输入不动产权证号/权利人/坐落/不动产单元号">
                </td>
                <td class="Search">
                    <a href="#" id="ywsj_search_btn">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <form method="post" enctype="multipart/form-data" id="ywUploadForm">
                    <input type="text" name="proid" value="${proid!}" hidden/>
                    <input type="text" name="wiid" value="${wiid!}" hidden/>
                    <td class="Search">
                        <input type="file" id="impFile" name="multipartFile"/>
                    </td>
                    <td>
                        <input class="btn01 AdvancedButton" type="submit" value="读取压缩包">
                    </td>
                </form>
            </tr>
        </table>
    </div>
    <table id="ywsj-grid-table"></table>
    <div id="ywsj-grid-pager"></div>
</div>
</div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="tipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->提示信息</h4>
                <button type="button" id="tipHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="csdjAlertInfo"></div>
                <div id="csdjConfirmInfo"></div>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
            </div>
        </div>
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<form>
    <input id="proid" value="${proid!}" hidden/>
</form>
</@com.html>
