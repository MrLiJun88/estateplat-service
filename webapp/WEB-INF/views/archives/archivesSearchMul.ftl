<@com.html title="归档信息查询" import="ace,public">
<style>
    .tab-content {
        overflow-y: auto;
        height: auto;
    }

</style>
<script type="text/javascript">
    var serverUrl = "${serverUrl!}";
    var reportUrl = "${reportUrl!}";
    $selectedInput = new Array();
    $mulRowid = new Array();
    $selectedSlbh = new Array();
    slbhs = new Array();
    var userid = "${userid!}";
    $(function () {
        Array.prototype.remove = function (index) {
            if (index > -1) {
                this.splice(index, 1);
            }
        };
        $("#bdcGd").click(function () {
            $.blockUI({message: "请稍等……"});
            var proids = $selectedInput.join(",");
            var gdlx = $("#gdlx").val();
            if(proids == null || proids == ''){
                setTimeout($.unblockUI, 10);
                alert("请至少选择一条数据进行归档！");
                return false;
            }
            if (gdlx == null || gdlx == '' || gdlx == undefined){
                setTimeout($.unblockUI, 10);
                alert("请选择归档类型!");
                return false;
            }else {
                $.ajax({
                    type:"get",
                    url: serverUrl+"/bdcArchivesMul/bdcGdMul?proids=" + proids +"&gdlx="+gdlx +"&userid=" + userid,
                    success:function (result) {
                        if(result != null && result !=''){
                            setTimeout($.unblockUI, 10);
                            alert(result);
                            $("#grid-table").trigger("reloadGrid");
                            clearProidArray();
                        }else {
                            setTimeout($.unblockUI, 10);
                            alert("归档失败");
                        }
                    },
                    error:function () {
                        setTimeout($.unblockUI, 10);
                        alert("归档异常！");
                    }
                });
            }
        });
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'PROID'},
            colNames: ['序号', '收件编号', '坐落', '不动产单元号', '产权证号', '流程名称', '权利人', '档案号', '盒号', 'proid'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '3%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '</span>'
                    }
                },
                {name: 'BH', index: 'BH', width: '10%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                {name: 'BDCDYH', index: 'BDCDYH', width: '10%', sortable: false},
                {name: 'CQZH', index: 'CQZH', width: '10%', sortable: false},
                {name: 'LCMC', index: 'LCMC', width: '10%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                {name: 'DAH', index: 'DAH', width: '10%', sortable: false},
                {name: 'HH', index: 'HH', width: '10%', sortable: false},
                {name: 'PROID', index: 'PROID', width: '5%', hidden: true}
            ],
            viewrecords: true,
            rowNum: 10,
            rowList: [10, 20, 30],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            rownumbers: false,
            onSelectAll: function (aRowids, status) {
                var $myGrid = $(this);
                // aRowids.forEach(function (e) {
                $.each(aRowids, function (i, e) {
                    var cm = $myGrid.jqGrid('getRowData', e);
                    var index = $.inArray(cm.XL, $mulRowid);
                    if (status && index < 0) {
                        $selectedInput.push(cm.PROID);
                        $selectedSlbh.push(cm.SLBH);
                        $mulRowid.push(cm.XL);
                    } else if (!status && index >= 0) {
                        $selectedInput.remove(index);
                        $selectedSlbh.remove(index);
                        $mulRowid.remove(index);
                    }
                })
            },
            onSelectRow: function (rowid, status) {
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
                var index = $.inArray(cm.XL, $mulRowid);
                if (status && index < 0) {
                    $selectedInput.push(cm.PROID);
                    $selectedSlbh.push(cm.SLBH);
                    $mulRowid.push(cm.XL);
                } else if (!status && index >= 0) {
                    $selectedInput.remove(index);
                    $selectedSlbh.remove(index);
                    $mulRowid.remove(index);
                }
            },
            loadComplete: function () {
                var table = this;
                var jqData = $(grid_selector).jqGrid("getRowData");
                var rowIds = $(grid_selector).jqGrid("getDataIDs");
                $.each(jqData, function (index, data) {
                    asyncGetRestOfGdxxMul(data.PROID, $(grid_selector), rowIds[index]);

                    //翻页时保持复选框选中状态
                    if ($selectedInput.indexOf(data.PROID) != -1) {
                        $(grid_selector).setSelection(rowIds[index]);
                    }
                });
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果10条设置宽度为auto,如果少于10条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 10) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "390px");
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
        //绑定回车键
        $(document).keydown(function (event) {
            if (event.keyCode == 13) {
                $("#zjSlbh").click();
            }
        });
    });
    function tableReload(table, url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: url, datatype: 'json', page: 1, postData: data});
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
                    'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
                };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        })
    }
    function gdxxSearch() {
        var slbh = $("#slbh").val();
        var dyzmh = $("#dyzmh").val();
        var cqzh = $("#cqzh").val();
        var gdlx = $("#gdlx").val();
        var dah = $("#dah").val();
        var cxlx = $("#cxlx").val();
        clearProidArray();
        slbhs = [];
        slbhs.push(slbh);
        var url = serverUrl + "/bdcArchivesMul/getGdxxMulPagesJson";
        tableReload("grid-table",url,{dyzmh:dyzmh,cqzh:cqzh,dah:dah,slbhs:slbh,gdlx:gdlx,cxlx:cxlx});
    }
    function asyncGetRestOfGdxxMul(proid,table,rowid) {
        $.ajax({
            type: "post",
            url: serverUrl+"/bdcArchivesMul/asyncGetRestOfGdxxMul?proid=" + proid,
            success: function (result) {
                if (result.qlrMc != null && result.qlrMc != ''){
                    table.setCell(rowid, "QLR", result.qlrMc);
                }
                if (result.bh !=null && result.bh != ''){
                    table.setCell(rowid, "BH", result.bh);
                }
                if (result.bdcdyh !=null && result.bdcdyh != ''){
                    table.setCell(rowid, "BDCDYH", result.bdcdyh);
                }
                if (result.zl !=null && result.zl != ''){
                    table.setCell(rowid, "ZL", result.zl);
                }
                if (result.lcmc != null && result.lcmc != ''){
                    table.setCell(rowid, "LCMC", result.lcmc);
                }
                if (result.cqzh != null && result.cqzh != ''){
                    table.setCell(rowid, "CQZH", result.cqzh);
                }
                //组装可编辑档案号和盒号
                if(result.dah != null && result.dah != ''){
                    var dahCell = '<input type="text" id="gdxxdah_'+ proid +'" name="dah" value="'+result.dah+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "DAH", dahCell);
                }
                if(result.hh != null && result.hh != ''){
                    var hhCell = '<input type="text" id="gdxxhh_'+ proid +'" name="hh" value="'+result.hh+'"style="border: none;height: 25px;width: 150px"/>';
                    table.setCell(rowid, "HH", hhCell);
                }
            }
        });
    }

    function zjSlbh() {
        var slbh = $("#slbh").val();
        var cxlx = $("#cxlx").val();
        slbhs.push(slbh);
        var slbhsStr = slbhs.join(",");
        var url = serverUrl + "/bdcArchivesMul/getGdxxMulPagesJson";
        tableReload("grid-table",url,{slbhs:slbhsStr,cxlx:cxlx});
    }

    function gdxxClear() {
        slbhs = [];
        $selectedInput = [];
        $mulRowid = [];
        $selectedSlbh = [];
        $("#grid-table").jqGrid("clearGridData");
    }

    function gdxxDel() {
        $.each($selectedSlbh,function (index,element) {
            var index = $.inArray(element,slbhs);
            if(index >= -1){
                slbhs.splice(index,1);
            }
        });
        clearProidArray();
        var slbhsStr = slbhs.join(",");
        var url = serverUrl + "/bdcArchivesMul/getGdxxMulPagesJson";
        tableReload("grid-table",url,{slbhs:slbhsStr});
    }
    function saveGdxx() {
        //遍历所有选中记录，更新档案号和盒号
        $.each($selectedInput, function (index, element){
            var proid = element;
            var dah = $("#gdxxdah_"+proid).val();
            var hh = $("#gdxxhh_"+proid).val();
            $.ajax({
                type: "post",
                url: serverUrl + "/bdcArchivesMul/saveGdxxDahAndHh",
                data:{proid:proid,dah:dah,hh:hh, yzDah:"false"},
                success: function (result) {
                    if (result == 'success') {
                        alert("保存成功");
                        clearProidArray();
                        $("#grid-table").trigger("reloadGrid");
                    }else if(result != null && result != ''){
                        alert(result);
                    }
                },
                error:function () {
                    alert("保存失败");
                }
            });
        });
    }
    function exportCqDyQd() {
        var proids = $selectedInput.join(",");
        var gdlx = $("#gdlx").val();
        if (gdlx == null || gdlx ==''){
            alert(" 请选择归档类型！");
        }else if(gdlx == "0"){
            var url = serverUrl + "/bdcArchivesMul/exportCqGdQd?proids="+proids;
            window.open(url);
        }else if(gdlx == "1"){
            var url = serverUrl + "/bdcArchivesMul/exportDyGdQd?proids="+proids;
            window.open(url);
        }else {
            alert("请输入正确的归档类型!");
        }
    }
    function printCqDyQdShow() {
        var proids = $selectedInput.join(",");
        if (proids == '' ||proids == null){
            alert("请选择至少一条数据!");
        }
        var gdlx = $("#gdlx").val();
        if (gdlx == "0"){
            window.open(reportUrl + "/ReportServer?reportlet=print%2FbdcCqGdQdByProids.cpt&op=write&proids=" + proids +  "&gdlx="+gdlx+"&serverUrl="+serverUrl.replace("http://",""), "产权归档清单", "left=50,top=50,height=" + 600 + ",width=" + 1200);
        }else if(gdlx == "1"){
            window.open(reportUrl + "/ReportServer?reportlet=print%2FbdcDyGdQdByProids.cpt&op=write&proids=" + proids + "&gdlx="+gdlx+"&serverUrl="+serverUrl.replace("http://",""), "抵押归档清单", "left=50,top=50,height=" + 600 + ",width=" + 1200);
        }else{
            alert("请输入正确的归档类型！");
        }
    }

    function clearProidArray() {
        $selectedInput = [];
        $mulRowid = [];
        $selectedSlbh = [];
    }
</script>
<div class="main-container" id ="main-container" >
    <div class="page-content" style="float:left; margin-left: 0;">
        <div class="simpleSearch" style="width:100%; height: 90px;margin-top: 10px;">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td style="border: 0px">
                        <label class="col-xs-1 control-label " style="width:100px">受理编号：</label>
                    </td>
                    <td>
                        <input type="text" id="slbh" style="border:none;">
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <label class="col-xs-1 control-label " style="width:100px">归档类型：</label>
                    </td>
                    <td>
                        <@f.select id="gdlx" name="gdlx"  showFieldName="mc" valueFieldName="dm" source="gdlxList" defaultValue="" style="width:200px;">
                        </@f.select>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <label class="col-xs-1 control-label " style="width:100px">产权证号：</label>
                    </td>
                    <td>
                        <input type="text" id="cqzh" style="border:none;">
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <label class="col-xs-2 control-label " style="width:110px">抵押证明号：</label>
                    </td>
                    <td>
                        <input type="text" id="dyzmh" style="border:none;">
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <label class="col-xs-1 control-label " style="width:80px">档案号：</label>
                    </td>
                    <td>
                        <input type="text" id="dah" style="border:none;">
                    </td>
                </tr>
            </table>
            <table style="margin-top: 20px">
                <tr>
                    <td style="border: 0px">
                        <label class="col-xs-1 control-label " style="width:100px">查询类型：</label>
                    </td>
                    <td>
                        <select id="cxlx" name="cxlx" class="form-control" style="width:170px;">
                            <option value="" selected>请选择</option>
                            <option value="产权">产权</option>
                            <option value="抵押权">抵押权</option>
                        </select>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="gdxxSearch" name="gdxxSearch" onclick="gdxxSearch()">查询</button>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="printCqDyQdShow" name="printCqDyQdShow" onclick="printCqDyQdShow()">打印预览</button>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="exportCqDyQd" name="exportCqDyQd" onclick="exportCqDyQd()">导出</button>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="bdcGd"  onclick="">归档</button>
                    </td>
                </tr>
            </table>

        </div>
        <div class="row">
            <div class="tabbable">
                <div class="tab-content">
                    <div class="tableHeader">
                        <ul>
                            <li>
                                <button type="button" id="gdxxClear" onclick="gdxxClear()">
                                    <span>清空</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="saveGdxx" onclick="saveGdxx()">
                                    <span>保存</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" id="gdxxDel" onclick="gdxxDel()">
                                    <span>删除</span>
                                </button>
                            </li>
                        </ul>
                    </div>
                    <table id="grid-table"></table>
                    <div id="grid-pager"></div>
                </div>
            </div>
        </div>
        <div class="simpleSearch" style="width:100%; height: 50px;margin-top: 10px;">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="zjSlbh" name="zjSlbh" onclick="zjSlbh()" style="display: none">追加</button>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</@com.html>