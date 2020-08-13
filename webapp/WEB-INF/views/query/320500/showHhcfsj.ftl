<@com.html import="ace,public">
<style>
    #bdcdyTipPop .modal-dialog {
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
</style>
<script type="text/javascript">
    var $mulData = [];
    var $mulRowid = [];
    var valite = [];
    var bdcXmRelList = "";
    $(function () {
        Array.prototype.remove = function (index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };
        initGrid();
        showData();
        //展示购物车中数据
        $("#show").click(function () {
            jQuery("#djsj-grid-table").jqGrid("clearGridData");
            showData();
        });


        $("#clear").click(function () {
            $("#sure").html("<span>创建项目</span>");
            jQuery("#djsj-grid-table").jqGrid("clearGridData");
            window.localStorage.removeItem("hhcfData_${proid!}");
            alert("清空成功");
        });
        $("#clearSelect").click(function () {
            $("#sure").html("<span>创建项目</span>");
            var selectedRowIds = $("#djsj-grid-table").jqGrid("getGridParam","selarrrow");
            var len = selectedRowIds.length;
            for(var i = 0;i < len ;i ++) {
                $("#djsj-grid-table").jqGrid("delRowData", selectedRowIds[0]);
            }
            $mulData = [];
            $mulRowid = [];
            alert("清空成功");
        });

        //生成数据
        $("#sure").click(function () {

            if ($mulData.length > 0) {
                checkXm();
            } else {
                alert("请选择数据！");
            }
        });
        $("#search").click(function () {
            var url = "${bdcdjUrl}/selectBdcdy/selectHhcf?proid=${proid!}";
            window.openWin(url);
        });

        $("#bdcdyTipHide,#bdcdyTipCloseBtn").click(function () {
            $("#bdcdyTipPop").hide();
            valite.length = 0;
            bdcXmRelList = "";
            setTimeout($.unblockUI, 10);
        });

        $("#tipHide,#tipCloseBtn").click(function () {
            $("#tipPop").hide();
            setTimeout($.unblockUI, 10);
        });

        $("#bdcdyTipBackBtn").click(function () {
            $(this).hide();
            $("#bdcdyTipHide,#bdcdyTipCloseBtn").show();
            mulDisplayTip();
        });
    })
    function initGrid() {
        var grid_selector = "#djsj-grid-table";
        var pager_selector = "#djsj-grid-pager";
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
            datatype: "local",
            height: 'auto',
            colNames: ["来源", "不动产单元号", "其他（证书或者地籍号）", "坐落", "权利人", "ID", 'TYPE', 'GDPROID', 'QLID', 'DJID'],
            colModel: [
                {
                    name: 'TYPE', index: 'TYPE', width: '20%', sortable: false, formatter: function (cellvalue) {
                    var cell = "";
                    if (cellvalue == "bdcdy")
                        cell = "不动产单元";
                    else if (cellvalue == "bdcqz")
                        cell = "不动产权证";
                    else if (cellvalue == "fcz")
                        cell = "房产证";
                    else if (cellvalue == "tdz")
                        cell = "土地证";
                    return cell;
                }
                },
                {name: 'BDCDYH', index: 'BDCDYH', width: '20%', sortable: false},
                {name: 'QT', index: 'QT', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '40%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '20%', sortable: false},
                {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true},
                {name: 'TYPE', index: 'TYPE', width: '0%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'GDPROID', width: '0%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
                {name: 'DJID', index: 'DJID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 5,
            rowList: [5, 10, 20],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            onSelectAll: function (aRowids, status) {
                var $myGrid = $(this);
                aRowids.forEach(function(e){
                var cm = $myGrid.jqGrid('getRowData', e);
                var index = $.inArray(e, $mulRowid);
                if (status && index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(e);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                });
                $("#sure").html("<span>创建项目(" + $mulRowid.length + ")</span>");
            },
            onSelectRow: function (rowid, status) {
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
                if (cm.ID == rowid) {
                    var index = $.inArray(rowid, $mulRowid);
                    if (status && index < 0) {
                        $mulData.push(cm);
                        $mulRowid.push(rowid);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
                $("#sure").html("<span>创建项目(" + $mulRowid.length + ")</span>");
            },
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果5条设置宽度为auto,如果少于7条就设置固定高度
                $(grid_selector).jqGrid("setGridHeight", "320px");
                $("#djsj-grid-pager_left").empty();
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
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
                    'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
                };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }

    function editData(bdcXmRelList) {
        var proid = "${proid!}";
        $.ajax({
            url: "${bdcdjUrl}/wfProject/initVoFromOldData?proid=" + proid + "&hhcf=true" + bdcXmRelList,
            type: "get",
            success: function (data) {
                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        async: true,
                        url: '${bdcdjUrl}/wfProject/updateWorkFlow?proid=' + proid,
                        success: function (data) {
                        }
                    });
                    bdcXmRelList = "";
                    //清除本地缓存
                    <#--window.localStorage.removeItem("hhcfData_${proid!}");-->
                    window.parent.parent.hideModel();
                    window.parent.parent.resourceRefresh();
                } else {
                    tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
                    bdcXmRelList = "";
                    window.localStorage.removeItem("hhcfData_${proid!}");
                    //window.parent.parent.hideModel();
                    //window.parent.parent.resourceRefresh();
                    setTimeout($.unblockUI, 10);
                }
            },
            error: function (data) {
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
                setTimeout($.unblockUI, 10);
            }
        });
    }

    function checkXm() {
        var proid = "${proid!}";
        var bdcdyhs = "";
        var djIds = "";
        var yxmids = "";
        var gdData = [];
        var gdproids = [];
        var qlids = [];
        var i = 0;
        $.each($mulData, function (index, data) {
            if (data.TYPE == "bdcdy") {
                bdcdyhs += "&bdcdyhs[" + i + "]=" + data.BDCDYH;
                djIds += "&djIds[" + i + "]=" + data.ID;
                bdcXmRelList += "&bdcXmRelList[" + i + "].qjid=" + data.ID + "&bdcXmRelList[" + i + "].ydjxmly=1";
                i++;
            } else if (data.TYPE == "bdcqz") {
                yxmids += "&yxmids[" + i + "]=" + data.PROID;
                bdcdyhs += "&bdcdyhs[" + i + "]=" + data.BDCDYH;
                bdcXmRelList += "&bdcXmRelList[" + i + "].yproid=" + data.ID + "&bdcXmRelList[" + i + "].ydjxmly=1";
                i++;
            } else {
                gdData.push(data);
                gdproids.push(data.GDPROID);
                qlids.push(data.QLID);
            }
        });

        var check = [];
        //验证不动产
        if (bdcXmRelList && bdcXmRelList != 'undefined') {
            check = joinArray(check, checkBdcData(yxmids, bdcdyhs, bdcXmRelList, proid));
        }
        if (gdproids && gdproids.length > 0) {
            check = joinArray(check, checkGdData(gdproids.join(","), qlids.join(",")));
            bdcXmRelList = addGdXmRel(gdData, bdcXmRelList);
        }

        if (check && check.length > 0) {
            hadleTipData(check);
            if (valite.length == 0)
                djsjDisplayTip(check, djIds, proid, bdcXmRelList, bdcdyhs);
            else {
                $("#bdcdyTipPop").show();
                mulDisplayTip();
            }
        } else {
            editData(bdcXmRelList);
        }
    }

    function djsjDisplayTip(data, djIds, proid, bdcXmRelList, bdcdyhs) {
        var alertSize = 0;
        var confirmSize = 0;
        if (data.length > 0) {
            $("#csdjAlertInfo,#csdjConfirmInfo").html("");
            $.each(data, function (i, item) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (item.checkModel == "confirm") {
                    confirmSize++;
                    $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                } else if (item.checkModel == "alert") {
                    alertSize++;
                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                }
            })
            $("#tipPop").show();
            $("#modal-backdrop").show();
        }
        if (alertSize == 0 && confirmSize == 0) {
            djsjInitVoFromOldData(djIds, proid, bdcXmRelList, bdcdyhs);
        } else if (alertSize == 0 && confirmSize > 0) {
            $("span[name='hlBtn']").click(function () {
                $(this).parent().remove();
                if ($("#csdjConfirmInfo > div").size() == 0) {
                    djsjInitVoFromOldData(djIds, proid, bdcXmRelList, bdcdyhs);
                }
            })
        }
    }

    function addGdXmRel(gdData, bdcXmRelList) {
        //验证过度数据
        if (gdData.length > 0) {
            //组织过度数据
            $.each(gdData, function (index, data) {
                var i = $mulData.length - gdData.length + index;
                var xmly = "";
                if (data.TYPE == "fcz") {
                    xmly = "3";
                } else
                    xmly = "2";
                bdcXmRelList += "&bdcXmRelList[" + i + "].yqlid=" + data.QLID + "&bdcXmRelList[" + i + "].yproid=" + data.GDPROID + "&bdcXmRelList[" + i + "].ydjxmly=" + xmly + "&bdcXmRelList[" + i + "].qjid=" + data.DJID;
            });
        }
        return bdcXmRelList;
    }

    function checkBdcData(yxmids, bdcdyhs, bdcXmRelList, proid) {
        var bdcData = null;
        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/wfProject/checkMulBdcXm?proid=' + proid + bdcdyhs + yxmids + bdcXmRelList,
            type: 'post',
            dataType: 'json',
            async: false,
            success: function (data) {
                if (data && data != 'undefined')
                    bdcData = data;
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
        return bdcData;
    }

    function checkGdData(gdproids, qlids) {
        var result = null;
        var sqlxMc = "${sqlxMc!}";
        var options = {
            url: '${bdcdjUrl}/bdcJgSjgl/checkMulXmFw',
            type: 'post',
            dataType: 'json',
            async: false,
            data: {gdproids: gdproids, qlids: qlids, sqlxMc: sqlxMc},
            success: function (data) {
                if (data && data != 'undefined')
                    result = data;
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
        return result;
    }

    function showData() {
        var sessionData = window.localStorage.getItem("hhcfData_${proid!}");
        if (sessionData && sessionData != 'undefined') {
            var jsonData = JSON.parse(sessionData);
            if (jsonData.length > 0) {
                $.each(jsonData, function () {
                    var arrayData = this.data;
                    if (arrayData && arrayData.length > 0) {
                        $.each(arrayData, function (index, data) {
                            jQuery("#djsj-grid-table").jqGrid('addRowData', data.ID, data);
                        });
                    }
                })
            }
        }
    }

    function joinArray(resource, target) {
        if (target != null && target.length > 0) {
            $.each(target, function () {
                resource.push(this);
            })
        }
        return resource;
    }

    function hadleTipData(data) {
        var bdcdyh = [];
        var alterInfo;
        var confirmInfo;
        var sigBdcdyh;
        $.each(data, function (i, item) {
            if (item.bdcdyh && item.bdcdyh != 'undefined') {
                var i = bdcdyh.indexOf(item.bdcdyh)
                if (i > -1) {
                    alterInfo = valite[i].alertInfo;
                    confirmInfo = valite[i].confirmInfo;
                } else {
                    alterInfo = [];
                    confirmInfo = [];
                    sigBdcdyh = [];
                    bdcdyh.push(item.bdcdyh);
                }

                //根据不动产单元号生成对应的json,并放入全局对象valite中
                var json = [];
                json["msg"] = item.checkMsg;
                json["info"] = item.info;

                if (item.checkModel == "alert")
                    alterInfo.push(json);
                else if (item.checkModel == "confirm" || item.checkModel == "confirmAndCreate")
                    confirmInfo.push(json);

                if (i == -1) {
                    sigBdcdyh["bdcdyh"] = item.bdcdyh;
                    sigBdcdyh["alertInfo"] = alterInfo;
                    sigBdcdyh["confirmInfo"] = confirmInfo;
                    valite.push(sigBdcdyh);
                }
            } else {
                //清空获取的情况
                valite.length = 0;
                //跳出循环
                return false;
            }
        });
    }

    function mulDisplayTip() {
        $("#bdcdyInfo").html("");
        $("#bdcdyAlertInfo,#bdcdyConfirmInfo").html("");
        var altercount = 0;
        var confirmcount = 0;
        var i = valite.length;
        while (i--) {
            var item = valite[i];
            var content = item.bdcdyh;
            if (item.alertInfo.length != 0 || item.confirmInfo.length != 0) {
                if (item.alertInfo.length != 0) {
                    content += "存在" + item.alertInfo.length + "个警告验证";
                    altercount += item.alertInfo.length;
                }
                if (item.confirmInfo.length != 0) {
                    content += "存在" + item.confirmInfo.length + "个确认验证";
                    confirmcount += item.confirmInfo.length;
                    if ($("button[name='hlBtn']").length == 0) {
                        $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                    }
                    $("button[name='hlBtn']").click(function () {
                        if($("#footer").data("altercount") == 0){
                            $("#bdcdyTipPop").hide();
                            editData(bdcXmRelList);
                        }
                    });
                }
                $("#bdcdyInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="viewBdcdyTip(\'' + i + '\',\'' + altercount + '\')">查看</span>' + content + '</div>');
            } else {
                //没有值的清空
                valite.splice(i, 1);
            }
        }
        $("#footer").data("altercount",altercount);
        if (altercount == 0 && confirmcount == 0) {
            editData(bdcXmRelList);
        }
    }

    function viewBdcdyTip(i, altercount) {
        $("#bdcdyTipHide,#bdcdyTipCloseBtn").hide();
        $("#bdcdyTipBackBtn").show();
        $("#bdcdyInfo").html("");
        $("#bdcdyAlertInfo,#bdcdyConfirmInfo").html("");
        var alterInfo = valite[i].alertInfo;
        var confirmInfo = valite[i].confirmInfo;
        if (alterInfo && alterInfo != 'undefined' && alterInfo.length > 0)
            $.each(alterInfo, function (i, item) {
                $("#bdcdyAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.msg + '</div>');
            });
        if (confirmInfo && confirmInfo != 'undefined' && confirmInfo.length > 0) {
            var content = "";
            if (altercount == 0) {
                $.each(confirmInfo, function (i, item) {
                    $("#bdcdyConfirmInfo").append('<div class="alert alert-warning" value=' + i + '><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="mulhlBtn">忽略</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.msg + '</div>');
                });
            } else {
                $.each(confirmInfo, function (i, item) {
                    $("#bdcdyConfirmInfo").append('<div class="alert alert-warning" value=' + i + '><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.msg + '</div>');
                });
            }
        }


        if (altercount == 0) {
            $("span[name='mulhlBtn']").click(function () {
                var index = $(this).parent()[0].getAttribute("value");
                $(this).parent().remove();
                if (index && index != 'undefinde' && index > -1) {
                    confirmInfo.remove(index);
                }
                if (confirmInfo.length == 0)
                    $("#bdcdyTipBackBtn").click();
            });
        }
    }
    function openProjectInfo(proid) {
        if (proid && proid != undefined) {
            $.ajax({
                url: "${bdcdjUrl}/qllxResource/getViewUrl?proid=" + proid,
                type: 'post',
                success: function (data) {
                    if (data && data != undefined) {
                        openWin(data);
                    } else {
                        openWin('${bdcdjUrl!}/bdcJsxx?bdcdyh=' + proid);
                    }
                },
                error: function (data) {
                    tipInfo("查看失败！");
                }
            });
        }
    }
    function openWin(url) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
</script>
<div class="main-container">
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="show">
                    <span>查看选择数据</span>
                </button>
            </li>
            <li>
                <button type="button" id="clear">
                    <span>清空数据</span>
                </button>
            </li>
            <li>
                <button type="button" id="clearSelect">
                    <span>清空选择数据</span>
                </button>
            </li>
            <li>
                <button type="button" id="sure">
                    <span>创建项目</span>
                </button>
            </li>
        </ul>
    </div>
    <table id="djsj-grid-table"></table>
    <div id="djsj-grid-pager"></div>
</div>

<div class="Pop-upBox moveModel" style="display: none;" id="bdcdyTipPop">
    <div class="modal-dialog tipPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><#--<i class="ace-icon fa fa-search bigger-110"></i>-->不动产单元提示信息</h4>
                <button type="button" id="bdcdyTipHide" class="proHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <div id="bdcdyInfo"></div>
                <div id="bdcdyAlertInfo"></div>
                <div id="bdcdyConfirmInfo"></div>
            </div>
            <div id="footer" class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="bdcdyTipCloseBtn">关闭</button>
                <button type="button" class="btn btn-sm btn-primary" id="bdcdyTipBackBtn" style="display: none;">返回
                </button>
            </div>
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
</@com.html>