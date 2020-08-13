<@com.html title="" import="ace,public">
    <@script name="static/js/public.js"></@script>
<style>
    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
    }

    .ace-settings-box.open {
        max-width: 1000px;
        padding: 0 0px;
    }

    .modal-dialog {
        width: 600px;
        margin: 30px auto;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>
<script type="text/javascript">
    window.onbeforeunload = function () {
        window.opener.updateGdFwAndDyhSel();
    }
    //全局数组
    $selectArray = new Array();
    $mulDataFw = new Array();
    $mulRowidFw = new Array();
    $selectedInput = new Array();
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '300px';
    //全局的不动产类型
    $bdclx = 'TDFW';
    //定义公用的基础colModel
    fwColModel = [
//        {
//            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
//            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.FWID + '\',\'' + rowObject.DAH + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.PPZT + '\',\'' + rowObject.QLID + '\')"/>'
//        }
//        },
        {name: 'XL', index: 'XL', width: '10%', sortable: false},
        {name: 'FWZL', index: 'FWZL', width: '40%', sortable: false},
        {name: 'SZC', index: 'SZC', width: '10%', sortable: false},
        {name: 'ZCS', index: 'ZCS', width: '10%', sortable: false},
        {name: 'JZMJ', index: 'JZMJ', width: '15%', sortable: false},
        {name: 'PPZT', index: 'PPZT', width: '15%', sortable: false},
        {name: 'FWID', index: 'FWID', width: '0%', sortable: false, hidden: true}
    ];

    dyhColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>'
        }
        },
        {name: 'DJH', index: 'DJH', width: '25%', sortable: false},
        {
            name: 'BDCDYH',
            index: 'BDCDYH',
            width: '16%',
            sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return "";
                }
                var value = cellvalue.substr(19);
                return value;
            }
        },
        {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
        {name: 'TDZL', index: 'TDZL', width: '34%', sortable: false},
        {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true}
    ];
    dyhLoadComplete = function () {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        //清空
        $("#bdcdyh").val("");
        //如果10条设置宽度为auto,如果少于7条就设置固定高度
        if ($(table).jqGrid("getRowData").length == $rownum) {
            $(table).jqGrid("setGridHeight", "auto");
        } else {
            $(table).jqGrid("setGridHeight", $pageHight);
        }
        //去掉遮罩
        setTimeout($.unblockUI, 10);
    };
    $(function () {
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据"});
        var width = $(window).width() / 2;
        if (width < 795) {
            width = 795;
        }
        var height = $(window).height() - 20;
        $("#ace-settings-box").css("width", width).css("height", height);
        $(window).resize(function () {
            var width = $(window).width() / 2;
            if (width < 795) {
                width = 795;
            }
            var height = $(window).height() - 20;
            $("#ace-settings-box").css("width", width).css("height", height);
        })
        //默认初始化表格
        dyhTableInit();
        fwTdTableInit();

//    fwTableInit();
        //左边房屋林权草权土地
        $("#fwTab").click(function () {
            var url;
            //清空查询内容
            $("#dyh_search_qlr").val("");
            $("#dyh_search_qlr").next().show();
            if (this.id == "dyhTab") {
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                tableReload("dyh-grid-table", dyhUrl, {
                    hhSearch: '',
                    bdcdyh: '',
                    bdclx: $bdclx
                }, dyhColModel, dyhLoadComplete);
            } else {
                //清空隐藏表单数据
                $("#lqid").val("");
                $("#cqid").val("");
                $("#fwid").val("");
                $("#tdid").val("");
                $("#dah").val("");
                $("#xmmc").val("");
                //清空刷新不动产单元表
                $("#dyh-grid-table").setGridParam({datatype: 'local', page: 1});
                $("#dyh-grid-table").trigger("reloadGrid");
                if (this.id == "fwTab") {
                    $bdclx = 'TDFW';
                    $("#fw").addClass("active");

                    var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdFwJson?gdproid=${gdproid!}";
                    fwTableInit();
                    tableReload("fw-grid-table", fwUrl, {
                        hhSearch: '',
                        fczh: '',
                        dah: '',
                        filterFwPpzt: "${filterFwPpzt!}"
                    }, '', '');
                    $("#dyhTab").click();
                }

            }

        })


        //右边不动产单元，房屋土地
        $("#dyhTab,#fwTdTab").click(function () {
            if (this.id == "fwTdTab") {
                $bdclx = 'TDFW';
                $("#fwTd").addClass("active");
                var grid_selector = "#fwTd-grid-table";
                var parent_column = $(grid_selector).closest('[class*="col-"]');
                $(grid_selector).jqGrid("setGridWidth", parent_column.width());
            <#--var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";-->
//            fwTdTableInit();
//            tableReload("fwTd-grid-table", tdUrl, {hhSearch:'',tdzh:''},'','');
            } else {
                $("#file").addClass("active");
            <#--var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";-->
            <#--tableReload("dyh-grid-table", dyhUrl, {hhSearch:'',bdcdyh:'',bdclx:$bdclx},'','');-->
            }
        })
        $("#fwTab").click();
        /*   文字水印  */
        $(".watermarkText").watermark();

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdFwJson?gdproid=${gdproid!}";
            tableReload("fw-grid-table", fwUrl, {hhSearch: hhSearch, fczh: '', dah: ''}, '', '');
        })
        $("#dyh_search").click(function () {
            var hhSearch = $("#dyh_search_qlr").val();
            var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdclx: $bdclx}, dyhColModel, '');
        })

        $("#fwTd_search").click(function () {
            var hhSearch = $("#fwTd_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson";
            $bdclx = "TDFW";
            tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
        })

        $("#clean").click(function () {
            $("#gdproids").val("");
            $("#qlids").val("");
            $mulDataFw.splice(0,$mulDataFw.length);
            $mulRowidFw.splice(0,$mulRowidFw.length);
            $selectedInput.splice(0,$selectedInput.length);
            $selectArray.splice(0,$selectArray.length);
            $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
            $("#fw-grid-table").trigger("reloadGrid");
            tipInfo("清空成功");
        })

        //匹配
        $("#match").click(function () {
            var fwid = $("#fwid").val();
            var fwqlid = $("#qlid").val();
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var bdcdyh = $("#bdcdyh").val();
            var tdzh = $("#tdzh").val();
            var ppzt = "1";
            var dyid = $("#dyid").val();
            var gdproid = $("#gdproid").val();
            var djId = $("#djId").val();
            var tdqlid = $("#tdqlid").val();
            if (dyid == null || dyid == "undefined")
                dyid = "";
            var ygid = $("#ygid").val();
            if (ygid == null || ygid == "undefined")
                ygid = "";
            var cfid = $("#cfid").val();
            if (cfid == null || cfid == "undefined")
                cfid = "";
            var yyid = $("#yyid").val();
            if (yyid == null || yyid == "undefined")
                yyid = "";
            if (gdproid == null || gdproid == "undefined")
                gdproid = "";
            if (djId == null || djId == "undefined")
                djId = "";
            var fwids = "";
            if ($mulDataFw!=null&&$mulDataFw.length > 0 ) {
                var fwidArr = new Array();
                for (var i = 0; i < $mulDataFw.length; i++) {
                    fwidArr.push($mulDataFw[i].FWID)
                }
                fwids = fwidArr.join(",");
            }
            if (bdcdyh == '') {
                tipInfo("请选择不动产单元");
                return false;
            } else if (fwid == ''&&fwids=='' && $("#fw").hasClass("active")) {
                tipInfo("请选择房产证");
                return false;
            }
            var sqlxdm = "${sqlxdm!}";
            var wfids = "${wfids!}";
            if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                //当不等于商品房转移登记的需要匹配土地证
                if ($("#fw").hasClass("active") && ($("#tdid").val() == null || $("#tdid").val() == '')) {
                    var msg = "没有匹配房屋土地证，是否匹配！";
                    showConfirmDialog("提示信息", msg, "dyhPic", "'" + gdproid + "','" + fwid + "','" + tdid + "','" + bdcdyh + "','" + djId + "'", "", "");
                } else {
                    dyhPic(gdproid, fwid, tdid, bdcdyh, djId,tdqlid);
                }

            } else {
                tdid = "";
                dyhPic(gdproid, fwid, tdid, bdcdyh, djId,tdqlid);
            }
        })

        //匹配
        $("#matchAll").click(function () {
            var gdproid = "${gdproid!}";
            $.ajax({
                url: '${bdcdjUrl}/bdcJgSjgl/matchDataAll',
                type: 'post',
                dataType: 'json',
                data: {gdproid: gdproid,gdid:recordId,bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid,djId: djId,qlid: fwqlid,tdqlid:tdqlid,fwids: fwids},
                success: function (data) {
                    tipInfo(data.result);
                    if(fwids!=null&&fwids!=""){
                        for(var i = 0; i < $mulDataFw.length; i++){
                            changePpzt($(grid_selector), $mulDataFw[i].FWID);
                        }
                    }else{
                        changePpzt($(grid_selector), recordId);
                    }
                    changePpzt($(grid_selector), recordId);
                },
                error: function (data) {
                }
            });
        });

        //取消匹配
        $("#dismatch").click(function (){
            var gdid = $("#fwid").val();
            if (isBlank(gdid)) {
                alert("请选择房屋！")
                return false;
            }
            var ppzt = $("#ppzt").val();
            if (ppzt.indexOf("未匹配") > -1) {
                alert("所选房屋未匹配！")
                return false;
            }
            $.ajax({
                url: '${bdcdjUrl!}/bdcSjgl/dismatch',
                type: 'post',
                datatype: 'json',
                data: {fwid: gdid, gdtab: 'fw'},
                success: function (data) {
                    if (data == "fail") {
                        alert("删除失败！");
                    } else if (data == "error") {
                        alert("项目id不能为空！");
                    } else {
                        alert("删除成功！");
                        window.location.reload();
                    }
                },
                error: function (data) {
                    alert("删除失败！");
                }
            });
        });

        //定位
        $("#ace-settings-btn").click(function () {
            if ($("#ace-settings-box").hasClass("open")) {
                $("#iframe").attr("src", $("#iframeSrcUrl").val())
            }
        })

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var contentWidth = $(".tab-content").width();
            $("#fw-grid-table,#lq-grid-table,#dyh-grid-table,#cq-grid-table,#td-grid-table,#fwTd-grid-table").jqGrid('setGridWidth', contentWidth);
        });
    });
    var dyhPic = function (gdproid, fwid,tdid, bdcdyh, djId,tdqlid) {
        var grid_selector = "#fw-grid-table";
        var recordId = $("#fwid").val();
        var fwids = "";
        if ($mulDataFw!=null&&$mulDataFw.length > 0 ) {
            var fwidArr = new Array();
            for (var i = 0; i < $mulDataFw.length; i++) {
                fwidArr.push($mulDataFw[i].FWID)
            }
            fwids = fwidArr.join(",");
        }
        var fwqlid=$("#qlid").val();
        var ppzt = $("#ppzt").val();
        $.ajax({
            url: '${bdcdjUrl}/bdcJgSjgl/matchData',
            type: 'post',
            dataType: 'json',
            data: {gdproid: gdproid,gdid:recordId,bdcdyh: bdcdyh, fwid: fwid, bdclx: $bdclx, tdid: tdid,djId: djId,qlid: fwqlid,tdqlid:tdqlid,fwids: fwids,ppzt:ppzt},
            success: function (data) {
                if(data.is_show_pphzd == "true"){
                    <#--var url = "${bdcdjUrl}/bdcJgSjgl/showPphzdxx?bdcdyh=" + bdcdyh + '&qlid' + recordId;-->
                    <#--openWin(url, "查看匹配关系确认单");-->
                    ////苏州新要求直接跳转到打印预览页面
                    var url = "${reportUrl!}/ReportServer?reportlet=print%2Fbdc_pphzd.cpt&op=write&bdcdyh=" + bdcdyh + "&qlid=" + recordId + "&qlids=" + recordId;
                    openWin(url);
                }else{
                    tipInfo(data.result);
                }
                if(fwids!=null&&fwids!=""){
                    for(var i = 0; i < $mulDataFw.length; i++){
                        changePpzt($(grid_selector), $mulDataFw[i].FWID);
                    }
                }else{
                    changePpzt($(grid_selector), recordId);
                }
                changePpzt($(grid_selector), recordId);
                //清空
                $("#gdproids").val("");
                $("#qlids").val("");
                $mulDataFw.splice(0,$mulDataFw.length);
                $mulRowidFw.splice(0,$mulRowidFw.length);
                $selectedInput.splice(0,$selectedInput.length);
                $selectArray.splice(0,$selectArray.length);
                $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
                $("#fw-grid-table").trigger("reloadGrid");
            },
            error: function (data) {
            }
        });
    }
    function dyhTableInit() {
        var grid_selector = "#dyh-grid-table";
        var pager_selector = "#dyh-grid-pager";
        //resize to fit page size
        /* $(window).on('resize.jqGrid', function () {
             $(grid_selector).jqGrid('setGridWidth',$(".page-content").width());
         });*/
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'ID'},
            colNames: ['序列', '地籍号', '不动产单元号', '权利人', "坐落", '不动产类型'],
            colModel: dyhColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false, /*
            multiboxonly:true,
            multiselect:true,*/
            loadComplete: dyhLoadComplete,
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

    //房屋土地证初始化
    function fwTdTableInit() {
        var grid_selector = "#fwTd-grid-table";
        var pager_selector = "#fwTd-grid-pager";

        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var gridRowNum = $rownum;
        var index=0;
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '土地证号', "坐落", '状态', 'ID','qlid'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
//                        index++;
//                        if(index==1){
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl"  onclick="fwTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\',\'' + rowObject.QLID + '\')"/>';
//                        }else{
//                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" onclick="fwTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\')"/>';
//                        }
                    }
                },
                {name: 'TDZH', index: 'TDZH', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '35%', sortable: false},

                {name: 'STATUS', index: '', width: '20%', sortable: false},
                {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: gridRowNum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            gridComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }

                $(table).jqGrid("setGridWidth", parent_column.width());
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getDyYgCfStatus(data.QLID, data.TDID,$(grid_selector));
                });
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


    function fwTableInit() {
        var grid_selector = "#fw-grid-table";
        var pager_selector = "#fw-grid-pager";
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'FWID'},
            colNames: ['序列', '房屋坐落', '所在层', "总层数", '建筑面积', '匹配状态', 'FWID','DAH'],
            colModel: [
                {name: 'XL', index: 'XL', width: '6%', sortable: false},
                {name: 'FWZL', index: 'FWZL', width: '40%', sortable: false},
                {name: 'SZC', index: 'SZC', width: '10%', sortable: false},
                {name: 'ZCS', index: 'ZCS', width: '10%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '15%', sortable: false},
                {name: 'PPZT', index: 'PPZT', width: '15%', sortable: false},
                {name: 'FWID', index: 'FWID', width: '0%', sortable: false, hidden: true},
                {name: 'DAH', index: 'DAH', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                $("#fwid").val("");
                $("#dah").val("");
                $("#xmmc").val("");
                $("#dyid").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    //获取匹配状态：根据gd_dyh_rel中是否有对应的数据处理匹配状态
                    getPpzt(data.PPZT, $(grid_selector), data.FWID);
                })
            },
            onSelectAll: function(aRowids,status){
                var $myGrid = $(this);
                aRowids.forEach(function(e){
                    var cm = $myGrid.jqGrid('getRowData', e);
                    var index = $.inArray(e, $mulRowidFw);
                    if (status && index < 0) {
                        $mulDataFw.push(cm);
                        $mulRowidFw.push(e);
                    } else if (!status && index >= 0) {
                        $mulDataFw.remove(index);
                        $mulRowidFw.remove(index);
                    }
                })
                //赋值数量
                $("#gdfwMulXx").html("<span>已选择("+$mulRowidFw.length+")</span>");
            },
            onSelectRow: function (rowid, status) {
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData', rowid);
                //判断是已选择界面还是原界面s
                var index=$.inArray(rowid,$mulRowidFw);
                if(status && index<0){
                    $mulDataFw.push(cm);
                    $mulRowidFw.push(rowid);
                    $selectedInput.push(rowid);
                }else if(!status && index>=0){
                    $mulDataFw.remove(index);
                    $mulRowidFw.remove(index);
                    $selectedInput.remove(rowid);
                }
                if (status) {
                    fwSel(cm.FWID, cm.DAH, cm.FWZL, cm.PPZT, cm.QLID)
                }
                //赋值数量
                $("#gdfwMulXx").html("<span>已选择(" + $mulRowidFw.length + ")</span>");
            },
            gridComplete:function(){
                $.each($mulDataFw,function(index){
                    $selectedInput.push($mulDataFw[index].QLID);
                });
                $.each($selectedInput,function(index){
                    $('#'+$selectedInput[index]+'').click();
                });
                $selectedInput.splice(0,$selectedInput.length);
            },
            beforeSelectRow: function (rowid, e) {
                var $myGrid = $(this);
                try {
                    var i = $.jgrid.getCellIndex($(e.target).closest('td')[0]);
                }catch (error){
                    return;
                }
                var cm = $myGrid.jqGrid('getGridParam', 'colModel');
                return (cm[i].name === 'cb');
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    /**
     * 根据key获取是否加载数据
     * @param key
     * @returns {boolean}
     */
    function isLoadGrid(key) {
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        var gdTabLoadData = "${gdTabLoadData!}";
        var gdTabLoadDataArray = new Array();
        gdTabLoadDataArray = gdTabLoadData.split(",");
        if (gdTabOrderArray.length == gdTabLoadDataArray.length) {
            for (var i = 0; i < gdTabOrderArray.length; i++) {
                if (gdTabOrderArray[i] == key) {
                    var loadGrid = gdTabLoadDataArray[i];
                    if (loadGrid == 1) {
                        return true;
                        break;
                    } else {
                        return false;
                        break;
                    }
                }
            }
            return false;
        } else
            return false;
    }
    //改变匹配状态
    function changePpzt(table, rowid) {
        var ppzt = '';
        getPpzt(ppzt, table, rowid);
        var ppzts = $("span[name='ppzt']");
        var ppztArray = '';
        $.each(ppzts, function (index, data) {
            ppztArray += $(data).attr("value");
            ppztArray += ',';
        });
        $.ajax({
            url: "${bdcdjUrl}/bdcSjgl/changeGdFwPpzt?gdproid=${gdproid!}",
            type: "POST",
            data: {ppzt: ppztArray},
            dataType: "JSON",
            success: function () {
            },
            error: function () {
            }
        });
    }
    //获取匹配状态
    function getPpzt(ppzt, table, rowid) {
        $.ajax({
            url: "${bdcdjUrl}/bdcSjgl/judgeIsPp",
            type: "POST",
            data: {gdId: rowid},
            dataType: "JSON",
            async: false,
            success: function (result) {
                if (result.isHavePp) {
                    ppzt = '<span class="label label-success" value="1" name="ppzt">已匹配</span>';
                } else {
                    ppzt = '<span class="label label-danger" value="0" name="ppzt">待匹配</span>';
                }
                table.setCell(rowid, "PPZT", ppzt);
            },
            error: function (data) {
            }
        });
    }

    function tableReload(table, Url, data, colModel, loadComplete) {
        var index = 0;
        var jqgrid = $("#" + table);
        if (colModel == '' && loadComplete == '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        } else if (loadComplete == '' && colModel != '') {
            jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data, colModel: colModel});
        } else if (loadComplete != '' && colModel != '') {
            jqgrid.setGridParam({
                url: Url,
                datatype: 'json',
                page: 1,
                postData: data,
                colModel: colModel,
                loadComplete: loadComplete
            });
        }
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    function fwSel(fwid, dah, fwzl, ppzt,qlid) {
        //遮罩
        $.blockUI({message: "请稍等……"});
        //赋值
        if (fwzl && fwzl != 'undefined')
            $("#xmmc").val(fwzl);
        else
            fwzl = "";
        if (ppzt && ppzt != 'undefined')
            $("#ppzt").val(ppzt);
        else {
            $("#ppzt").val("");
            ppzt = "";
        }


        if (fwid && fwid != 'undefined')
            $("#fwid").val(fwid);
        else
            $("#fwid").val("");

        $("#tdid").val('');
        var bdcid = $("#gdproid").val();

        if (qlid && qlid != 'undefined')
            $("#qlid").val(qlid);
        else
            $("#qlid").val("");
        picDyh(bdcid, fwid, dah, fwzl, ppzt,qlid);


    }
    //匹配不动产单元
    var picDyh = function (bdcid, fwid, dah, qlrmc, ppzt,qlid) {
        //通过fczh获取hs_index
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/queryBdcdyhByDahAndFwid?gdid=" + bdcid + "&dah=" + dah + "&fwid=" + fwid,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
                        bdcdyh: '',
                        bdclx: $bdclx
                    }, dyhColModel, dyhLoadComplete);
                } else {
                    //清空查询内容
                    $("#dyh_search_qlr").val("");
                    $("#dyh_search_qlr").next().show();
                    $.each(result, function (index, data) {
                        if (index == 0) {
                            if (data.hasOwnProperty("bdcdyh")) {
                                if (data.bdcdyh && data.bdcdyh != 'undefined') {
                                    selDyhByDyh(data.bdcdyh);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                    tableReload("dyh-grid-table", dyhUrl, {
                                        hhSearch: qlrmc,
                                        bdcdyh: '',
                                        bdclx: $bdclx
                                    }, dyhColModel, dyhLoadComplete);
                                }
                            } else {
                                if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                    selDyhByDyh(data.BDCDYH);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    var dyhUrl = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
                                    tableReload("dyh-grid-table", dyhUrl, {
                                        hhSearch: qlrmc,
                                        bdcdyh: '',
                                        bdclx: $bdclx
                                    }, dyhColModel, dyhLoadComplete);
                                }

                            }
                        }
                    })
                }
            },
            error: function (data) {
            }
        });

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/queryTdByFwid?fwid=" + fwid + "&dah=" + dah + "&gdid=" + bdcid,
            dataType: "json",
            success: function (result) {

                if (result == '' || result == null) {
                    $("#fwTd_search_qlr").next().hide();
                    $("#fwTd_search_qlr").val(qlrmc);
                    $("#tdzh").val('');

                    $("#tdid").val('');
                    var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                    tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                } else {
                    //清空查询内容
                    $("#fwTd_search_qlr").val("");
                    $("#fwTd_search_qlr").next().show();
                    $.each(result, function (index, data) {
                        if (index == 0) {
                            if (data.tdid == null || data.tdid == '') {
                                $("#fwTd_search_qlr").next().hide();
                                $("#fwTd_search_qlr").val(qlrmc);
                                $("#tdzh").val('');
                                $("#tdid").val('');
                                var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
                                fwTdTableInit();
                                tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                            } else {
                                selFwTdByFw(data.tdid);
                                $("#tdid").val(data.tdid);
                            }

                        }
                    })
                }
            },
            error: function (data) {
            }
        });
        $("#fwTdTab").show();

    }

    //通过房产档案号级联不动产单元
    function selDyhByDah(fcdah) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
        var data = {hhSearch: '', dah: fcdah, bdclx: $bdclx};
        var jqgrid = $("#dyh-grid-table");
        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        index++;
                        if (index == 1) {
                            $("#bdcdyh").val(rowObject.BDCDYH);

                            $("#djId").val(rowObject.ID);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" checked="true" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>';
                        }
                    }
                },
                {name: 'DJH', index: 'DJH', width: '25%', sortable: false},
                {
                    name: 'BDCDYH',
                    index: 'BDCDYH',
                    width: '25%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue.substr(19);
                        return value;
                    }
                },
                {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
                {name: 'TDZL', index: 'TDZL', width: '25%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果10条设置宽度为auto,如果少于7条就设置固定高度
                if ($(table).jqGrid("getRowData").length == $rownum) {
                    $(table).jqGrid("setGridHeight", "auto");
                } else {
                    $(table).jqGrid("setGridHeight", $pageHight);
                }
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
    //通过不动产单元级联不动产单元
    function selDyhByDyh(bdcdyh) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcSjgl/getBdcDyhPagesJson";
        var data = {hhSearch: '', bdcdyh: bdcdyh, bdclx: $bdclx};
        var jqgrid = $("#dyh-grid-table");
        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        index++;
                        if (index == 1) {
                            $("#bdcdyh").val(rowObject.BDCDYH);

                            $("#djId").val(rowObject.ID);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" checked="true" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="dyhXl" onclick="dyhSel(\'' + rowObject.BDCDYH + '\',\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.QLR + '\',\'' + rowObject.DAH + '\')"/>';
                        }
                    }
                },
                {name: 'DJH', index: 'DJH', width: '25%', sortable: false},
                {
                    name: 'BDCDYH',
                    index: 'BDCDYH',
                    width: '25%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue.substr(19);
                        return value;
                    }
                },
                {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
                {name: 'TDZL', index: 'TDZL', width: '25%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果10条设置宽度为auto,如果少于7条就设置固定高度
                if ($(table).jqGrid("getRowData").length == $rownum) {
                    $(table).jqGrid("setGridHeight", "auto");
                } else {
                    $(table).jqGrid("setGridHeight", $pageHight);
                }
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function fwTdSel(tdzh, tdid,qlid) {
        $("#tdzh").val(tdzh);
        $("#tdid").val(tdid);
        $("#tdqlid").val(qlid);
    }
    //通过房产证号级联不动产单元
    function selFwTdByFw(tdid) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcSjgl/getGdTdJson";
        var data = {hhSearch: '', tdid: tdid};
        var jqgrid = $("#fwTd-grid-table");
        var pager_selector = "#fwTd-grid-pager";
        var parent_column = $(jqgrid).closest('[class*="col-"]');

        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        index++;
                        if (index == 1) {
                            $("#tdid").val(rowObject.TDID);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" checked="true" onclick="fwTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\',\'' + rowObject.QLID + '\')"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl" onclick="fwTdSel(\'' + rowObject.TDZH + '\',\'' + rowObject.TDID + '\',\'' + rowObject.QLID + '\')"/>';
                        }
                    }
                },
                {name: 'TDZH', index: 'TDZH', width: '20%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '35%', sortable: false},

                {name: 'STATUS', index: '', width: '20%', sortable: false},
                {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //如果10条设置宽度为auto,如果少于7条就设置固定高度
                if ($(table).jqGrid("getRowData").length == $rownum) {
                    $(table).jqGrid("setGridHeight", "auto");
                } else {
                    $(table).jqGrid("setGridHeight", $pageHight);
                }
                $(table).jqGrid("setGridWidth", parent_column.width());
                var jqData = $(jqgrid).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                })
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    //选择不动产单元号的赋值操作
    function dyhSel(dyh, id, bdclx, xmmc, dah) {
        var zdzhh;
        if (dyh && dyh != 'undefined') {
            zdzhh = dyh.substr(0, 19);
            $("#bdcdyh").val(dyh);
            //定位
//        bdzDyMap(dyh);
            //$("#zdzhh").val(zdzhh);
        } else {
            $("#bdcdyh").val("");
            //$("#zdzhh").val("");
        }
        if (id && id != 'undefined') {
            $("#djId").val(id);
        } else {
            $("#djId").val("");
        }

    }

    //获取 抵押 查封 预告 状态
    function getDyYgCfStatus(qlid,tdid, table) {
        if (tdid == null || tdid == 'undefined') {
            tdid = "";
        }
        $.ajax({
            type: "GET",
            //url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdQlztByBdcid?tdid=" + tdid + "&bdclx=TD",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdTdxxByQlid?qlid=" + qlid + "&bdclx=TD",
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                if (result.qlzts == null || result.qlzts == "") {
                    cellVal = '<span class="label label-success">正常</span>';
                } else {
                    var qlzts = result.qlzts.split(",");
                    for (var i = 0; i < qlzts.length; i++) {
                        if (qlzts[i] == "DY") {
                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        } else if (qlzts[i] == "CF") {
                            cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        } else if (qlzts[i] == "YG") {
                            cellVal += '<span class="label label-info">预告</span><span> </span>';
                        } else if (qlzts[i] == "YY") {
                            cellVal += '<span class="label label-info">异议</span><span> </span>';
                        } else if (qlzts[i] == "ZX") {
                            cellVal += '<span class="label label-danger">注销</span><span> </span>';
                        } else if (qlzts[i] == "DI") {
                            cellVal += '<span class="label label-danger">地役</span><span> </span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }
                if (result.zls != null && result.zls != "" && result.qlrs != "null")
                    table.setCell(tdid, "ZL", result.zls);
                table.setCell(tdid, "STATUS", cellVal);
            }
        });
    }
</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
    <#--图形-->
        <div class="ace-settings-container" id="ace-settings-container" style="display: none;">
            <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
                <i class="ace-icon fa fa-globe blue bigger-200"></i>
            </div>

            <div class="ace-settings-box clearfix " id="ace-settings-box">
                <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
            </div>
            <!-- /.ace-settings-box -->
        </div>
        <div class="row" style="border-bottom: 1px solid #ddd;">
            <div class="col-xs-6"><h2>多个房屋匹配</h2></div>
            <div class="col-xs-6" style="text-align: right;">
                <#if readOnly?? &&  readOnly!="true">
                    <button type="button" class="btn btn-sm btn-primary" id="match"
                            style="margin-top: 20px; margin-bottom: 10px;">匹配
                    </button>
                    <#--<button type="button" class="btn btn-sm btn-primary" id="matchAll"
                            style="margin-top: 20px; margin-bottom: 10px;">一键匹配
                    </button>-->
                    <button type="button" class="btn btn-sm btn-primary" id="dismatch"
                            style="margin-top: 20px; margin-bottom: 10px;">取消匹配
                    </button>
                </#if>

            </div>
        </div>
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li>
                            <a data-toggle="tab" id="fwTab" href="#fw">
                                房屋信息
                            </a>
                        </li>

                    </ul>
                    <div class="tab-content">
                        <div id="fw" class="tab-pane ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                                   data-watermark="请输入档案号/坐落">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="fw_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="tableHeader">
                                <ul>
                                    <li>
                                        <button type="button" id="gdfwMulXx">
                                            <span>已选择</span>
                                        </button>
                                    </li>
                                    <li>
                                        <button type="button" id="clean">
                                            <i class="ace-icon fa fa-mail-forward"></i>
                                            <span>清空</span>
                                        </button>
                                    </li>
                                </ul>
                            </div>
                            <table id="fw-grid-table"></table>
                            <div id="fw-grid-pager"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active">
                            <a data-toggle="tab" id="dyhTab" href="#file">
                                不动产单元
                            </a>
                        </li>
                        <#if sqlxdm?? && wfids?? && wfids?index_of(sqlxdm)==-1>
                            <li>
                                <a data-toggle="tab" id="fwTdTab" href="#fwTd">
                                    房屋土地证
                                </a>
                            </li>
                        </#if>
                    </ul>
                    <div class="tab-content">
                        <div id="file" class="tab-pane in active ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="dyh_search_qlr"
                                                   data-watermark="请输入权利人/坐落/不动产单元号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="dyh_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <table id="dyh-grid-table"></table>
                            <div id="dyh-grid-pager"></div>
                        </div>

                        <#if sqlxdm?? && wfids?? && wfids?index_of(sqlxdm)==-1>
                            <div id="fwTd" class="tab-pane">
                                <div class="simpleSearch">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td>
                                                <input type="text" class="SSinput watermarkText" id="fwTd_search_qlr"
                                                       data-watermark="请输入坐落/土地证号">
                                            </td>
                                            <td class="Search">
                                                <a href="#" id="fwTd_search">
                                                    搜索
                                                    <i class="ace-icon fa fa-search bigger-130"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <table id="fwTd-grid-table" style="width: 800px"></table>
                                <div id="fwTd-grid-pager" style="width: 800px"></div>
                            </div>
                        </#if>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<form id="form" hidden="hidden">
    <input type="hidden" id="djlx" name="djlx">
    <input type="hidden" id="fwid" name="fwid">
    <input type="hidden" id="tdid" name="tdid">
    <input type="hidden" id="dah" name="dah">
    <input type="hidden" id="lqid" name="lqid">
    <input type="hidden" id="cqid" name="cqid">
    <input type="hidden" id="djId" name="djId">
    <input type="hidden" id="bdcdyh" name="bdcdyh">
    <input type="hidden" id="workFlowDefId" name="workFlowDefId">
    <input type="hidden" id="sqlx" name="sqlxMc">
    <input type="hidden" id="xmmc" name="xmmc">
    <input type="hidden" id="tdzh" name="tdzh"/>
    <input type="hidden" id="ppzt" name="ppzt"/>
    <input type="hidden" id="dyid" name="dyid"/>
    <input type="hidden" id="ygid" name="ygid"/>
    <input type="hidden" id="cfid" name="cfid"/>
    <input type="hidden" id="yyid" name="yyid"/>
    <input type="hidden" id="qlid" name="qlid" value="${qlid!}"/>
    <input type="hidden" id="gdproid" name="gdproid" value="${gdproid!}"/>
    <input type="hidden" id="mulGdfw" name="mulGdfw"/>
    <input type="hidden" id="tdqlid" name="tdqlid"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
