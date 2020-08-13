<@com.html title="归档信息查询" import="ace,public">
<style>
    .tab-content {
        overflow-y: auto;
        height: auto;
    }

</style>
<script type="text/javascript">
    var serverUrl = "${serverUrl!}";
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
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'PROID'},
            colNames: ['序号', '收件编号', '证号','坐落',  '权利人', '档案号','归档人员','归档日期', 'proid'],
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
                {name: 'CQZH', index: 'CQZH', width: '18%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                {name: 'DAH', index: 'DAH', width: '10%', sortable: false},
                {name: 'GDRY', index: 'GDRY', width: '10%', sortable: false},
                {name: 'GDRQ', index: 'GDRQ', width: '10%', sortable: false},
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
                aRowids.forEach(function (e) {
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
                    asyncGetRestOfGdxx(data.PROID, $(grid_selector), rowIds[index]);
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
        var zh = $("#zh").val();
        var gdlx = $("#gdlx").val();
        clearProidArray();
        slbhs = [];
        slbhs.push(slbh);
        var url = serverUrl + "/bdcGdByProid/getGdxxPagesJson";
        tableReload("grid-table",url,{zh:zh,slbhs:slbh,gdlx:gdlx});
    }
    function asyncGetRestOfGdxx(proid,table,rowid) {
        debugger;
        $.ajax({
            type: "post",
            url: serverUrl+"/bdcGdByProid/asyncGetRestOfGdxx?proid=" + proid,
            success: function (result) {
                if (result.qlrMc != null && result.qlrMc != ''){
                    table.setCell(rowid, "QLR", result.qlrMc);
                }
                if (result.bh !=null && result.bh != ''){
                    table.setCell(rowid, "BH", result.bh);
                }
                if (result.zl !=null && result.zl != ''){
                    table.setCell(rowid, "ZL", result.zl);
                }
                if (result.cqzh != null && result.cqzh != ''){
                    table.setCell(rowid, "CQZH", result.cqzh);
                }
                if (result.dah != null && result.dah != ''){
                    table.setCell(rowid, "DAH", result.dah);
                }
                if (result.gdr != null && result.gdr != ''){
                    table.setCell(rowid, "GDRY", result.gdr);
                }
                if (result.gdrq != null && result.gdrq != ''){
                    table.setCell(rowid, "GDRQ", result.gdrq);
                }
            }
        });
    }

    function zjSlbh() {
        var slbh = $("#slbh").val();
        slbhs.push(slbh);
        var slbhsStr = slbhs.join(",");
        var url = serverUrl + "/bdcGdByProid/getGdxxPagesJson";
        tableReload("grid-table",url,{slbhs:slbhsStr});
    }

    function gdxxClear() {
        slbhs = [];
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
        var url = serverUrl + "/bdcGdByProid/getGdxxPagesJson";
        tableReload("grid-table",url,{slbhs:slbhsStr});
    }

    function bdcGd() {
        var proids = $selectedInput.join(",");
        var gdlx = $("#gdlx").val();
        if(proids == null || proids == ''){
            tipInfo("请至少选择一条数据进行归档！");
        }
        if (gdlx == null || gdlx == '' || gdlx == undefined){
            tipInfo("请选择归档类型!");
        }else {
            validateDahExist(proids,gdlx);
        }
    }
    function validateDahExist(proids,gdlx) {
        if (proids != null && proids != null){
            $.ajax({
                type:"get",
                url: serverUrl+"/bdcGdByProid/validateDahExist?xmids=" + proids,
                success:function (data) {
                    if(data.result != null && data.result != null){
                        if (data.result == "true"){
                            var msg = "已归档，是否重新生成档案号?";
                            showConfirmDialog("提示信息", msg, "reBdcArchive", "'"+ proids+ "','"+ gdlx +"'","", "");
                        }else{
                            bdcArchives(proids,gdlx);
                        }
                    }else{
                        tipInfo("验证档案号是否重复失败！");
                    }
                },
                error:function () {
                    tipInfo("验证档案号是否重复失败！");
                }
            });
        }
    }
    function bdcArchives(proids,gdlx) {
        $.ajax({
            type:"get",
            url: serverUrl+"/bdcGdByProid/bdcGd?proids=" + proids +"&gdlx="+gdlx +"&userid=" + userid,
            success:function (result) {
                if(result != null && result !=''){
                    tipInfo(result);
                    $("#grid-table").trigger("reloadGrid");
                    clearProidArray();
                }else {
                    tipInfo("归档失败");
                }
            },
            error:function () {
                tipInfo("归档失败");
            }
        });
    }
    function reBdcArchive(proids,gdlx) {
        $.ajax({
            type:"get",
            url: serverUrl+"/bdcGdByProid/reBdcArchive?xmids=" + proids +"&gdlx="+gdlx +"&userid=" + userid,
            success:function (data) {
                if(data.result != null && data.result !=''){
                    tipInfo(data.result);
                    $("#grid-table").trigger("reloadGrid");
                    clearProidArray();
                }else {
                    tipInfo("归档失败");
                }
            },
            error:function () {
                tipInfo("归档失败");
            }
        });
    }
    function clearProidArray() {
        $selectedInput = [];
        $mulRowid = [];
        $selectedSlbh = [];
    }
</script>
<div class="main-container" id ="main-container" >
    <div class="page-content" style="float:left; margin-left: 0;">
        <div class="simpleSearch" style="width:1300px; height: 90px;margin-top: 10px;">
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
                        <label class="col-xs-1 control-label " style="width:100px">证号：</label>
                    </td>
                    <td>
                        <input type="text" id="zh" style="border:none;">
                    </td>
                </tr>
            </table>
            <table style="margin-top: 20px">
                <tr>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="gdxxSearch" name="gdxxSearch" onclick="gdxxSearch()">查询</button>
                    </td>
                    <td style="border: 0px">&nbsp;&nbsp;</td>
                    <td style="border: 0px">&nbsp;</td>
                    <td style="border: 0px">
                        <button type="button" class="btn btn-info save" id="bdcGd" name="bdcGd" onclick="bdcGd()">归档</button>
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