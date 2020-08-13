<@com.html title="" import="ace,public">
<style>
    .filesub {
        display: block;
        width: 90px;
        line-height: 34px;
        height: 34px;
        text-align: center;
        background: #155e96;
        color: #fff;
        word-spacing: 10px;
    }

    a:visited {
        color: #FFF;
    }

    a:hover {
        color: #FFF;
    }

    .col-xs-11 {
        width: 100%
    }

    .tab-content {
        overflow: hidden;
        height: auto;
        width: auto;
    }

    .tableHeader {
        width: 100%;
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

    .SSinput {
        min-width: 100px !important;
    }

    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
        width: 1175px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput .modal-dialog {
        width: 500px;
        position: fixed;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        z-index: 1050;
        -webkit-overflow-scrolling: touch;
        outline: 0;
    }

    #fileInput1 .modal-dialog {
        width: 620px;
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

    .title {
        font-size: 18pt;
        text-align: center;
        padding-left: 2px;
        position: absolute;
        margin-left: 40px;
        margin-top: 5px;
    }

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

    if (!Array.prototype.forEach) {
        Array.prototype.forEach = function(callback, thisArg) {
            var T, k;
            if (this == null) {
                throw new TypeError(" this is null or not defined");
            }
            var O = Object(this);
            var len = O.length >>> 0; // Hack to convert O.length to a UInt32
            if ({}.toString.call(callback) != "[object Function]") {
                throw new TypeError(callback + " is not a function");
            }
            if (thisArg) {
                T = thisArg;
            }
            k = 0;
            while (k < len) {
                var kValue;
                if (k in O) {
                    kValue = O[k];
                    callback.call(T, kValue, k, O);
                }
                k++;
            }
        };
    }

    Array.prototype.remove = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    //全局数组
    $selectArray = new Array();
    //table每页行数
    $mulData=new Array();
    $mulRowid=new Array();
    $selectedInput=new Array();
    //table每页行数
    $rownum = 8;
    //table 每页高度
    $pageHight = '300px';
    //全局的不动产类型
    $bdclx = 'TDFW';
    var djIds = [];
    //定义公用的基础colModel
    fwColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span>'
        }
        },
        {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
        {name: 'ZSLX', index: 'ZSLX', width: '15%', sortable: false},
        {name: 'FCZH', index: 'FCZH', width: '18%', sortable: false},
        {name: 'FWZL', index: 'FWZL', width: '26%', sortable: false},
        {name: 'PPZT', index: '', width: '13%', sortable: false},
        {name: 'STATUS', index: '', width: '18%', sortable: false},
        {name: 'QLZT', index: 'QLZT', width: '0%', sortable: false, hidden: true},
        {name: 'PROID', index: 'PROID', width: '0%', sortable: false, hidden: true},
        {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true}
    ];
    dyhColModel = [
        {
            name: 'XL', index: '', width: '10%', sortable: false, formatter: function (cellvalue, options, rowObject) {
            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>'
        }
        },
        {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
        {name: 'TDZL', index: 'TDZL', width: '30%', sortable: false},
        {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
        {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
        {
            name: 'BDCDYH',
            index: 'BDCDYH',
            width: '15%',
            sortable: false,
            formatter: function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return "";
                }
                var value = cellvalue.substr(19);
                return value;
            }
        },
        {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
        {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
    ];
    dyhLoadComplete = function () {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        oldDjhForTable("#dyh-grid-table");
//        addYdjhForTable("#dyh-grid-table");
        qlrForTable("#dyh-grid-table");

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
    fwLoadComplete = function () {
        //zwq防止有勾选的情况下,搜索勾选另一个，从而读取2个房产证号
        $selectArray.length = 0;
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        qlrForFwTable("#fw-grid-table");
        //如果10条设置宽度为auto,如果少于7条就设置固定高度
        if ($(table).jqGrid("getRowData").length == $rownum) {
            $(table).jqGrid("setGridHeight", "auto");
        } else {
            $(table).jqGrid("setGridHeight", $pageHight);
        }
        //去掉遮罩
        setTimeout($.unblockUI, 10);
    };

    fwtdLoadComplete = function () {
        var table = this;
        setTimeout(function () {
            updatePagerIcons(table);
            enableTooltips(table);
        }, 0);
        var grid_selector = "#fwTd-grid-table";
        //如果7条设置宽度为auto,如果少于7条就设置固定高度
        if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
            $(grid_selector).jqGrid("setGridHeight", "auto");
        } else {
            $(grid_selector).jqGrid("setGridHeight", $pageHight);
        }
        var jqData = $(grid_selector).jqGrid("getRowData");
        $.each(jqData, function (index, data) {
            getDyYgCfStatus($(grid_selector), data.TDID);
        })
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(table).jqGrid("setGridWidth", parent_column.width());
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
        if ('${userName!}' != "系统管理员") {
            $("#gdFwJcZx,#gdTdJcZx,#gdFwZx,#gdTdZx").hide();
        }
        //默认初始化表格
        dyhTableInit();
        fwTdTableInit();
        //左边房屋林权草权土地
        $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
            var url;
            //清空查询内容
            $("#dyh_search_qlr").val("");
            $("#dyh_search_qlr").next().show();
            if (this.id == "dyhTab") {
                $("#file").addClass("active");
                var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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
                    $("#dyhTab").click();
                    var fwUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwJson";
                    fwTableInit();
                    if (isLoadGrid("fw"))
                        tableReload("fw-grid-table", fwUrl, {
                            hhSearch: '',
                            fczh: '',
                            dah: '',
                            filterFwPpzt: "${filterFwPpzt!}"
                        }, "", fwLoadComplete);

                } else if (this.id == "lqTab") {
                    $bdclx = 'TDSL';
                    $("#lq").addClass("active");
                    $("#dyhTab").click();
                    var lqUrl = "${bdcdjUrl}/bdcJgSjgl/getGdLqJson";
                    lqTableInit();
                    if (isLoadGrid("lq"))
                        tableReload("lq-grid-table", lqUrl, {hhSearch: '', lqzh: ''}, '', '');
                } else if (this.id == "cqTab") {

                    $bdclx = 'TDQT';
                    $("#cq").addClass("active");
                    $("#dyhTab").click();
                    var cqUrl = "${bdcdjUrl}/bdcJgSjgl/getGdCqJson";
                    cqTableInit();
                    if (isLoadGrid("cq"))
                        tableReload("cq-grid-table", cqUrl, {hhSearch: '', cqzh: ''}, '', '');
                } else if (this.id == "tdTab") {
                    $bdclx = 'TD';
                    $("#td").addClass("active");
                    $("#dyhTab").click();
                    var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdTdJson";
                    tdTableInit();
                    if (isLoadGrid("td"))
                        tableReload("td-grid-table", tdUrl, {hhSearch: ''}, '', '');
                }
                getSqlxByDjlxAndBdclx('', '');
            }
            $("#fwTdTab").hide();

            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcJgSjgl/getDjlxByBdclx",
                data: {bdclx: $bdclx},
                dataType: "json",
                success: function (result) {
                    //清空
                    $("#djlxSelect").html("");
                    if (result != null && result != '') {
                        $.each(result, function (index, data) {
                            $("#djlxSelect").append('<option value="' + data.DM + '" >' + data.MC + '</option>');
                        })
                    }
                    djlx = $("#djlxSelect  option:selected").text();
                    getSqlxByDjlxAndBdclx(djlx, "");
                    $("#djlxSelect").trigger("chosen:updated");

                },
                error: function (data) {
                }
            });
        })

        //通过受理编号或权利人搜索
        $("#bdcxm_search").click(function () {
            var bdcxmUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcXmJson";
            var hhSearch = $("#bdcxm_search_qlr").val();
            bdcxmTableInit();
            tableReload("bdcxm-grid-table1", bdcxmUrl, {hhSearch: hhSearch}, '', '');
        })
        function bdcxmTableInit() {
            var grid_selector = "#bdcxm-grid-table1";
            var pager_selector = "#bdcxm-grid-pager1";
            var parent_column = $(grid_selector).closest('[class*="col-"]');
            $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
                if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                    $(grid_selector).jqGrid('setGridWidth', 580);
                }
            });
            var gridRowNum = $rownum;
            jQuery(grid_selector).jqGrid({
                url: "",
                datatype: "local",
                height: $pageHight,
                jsonReader: {id: 'PROID'},
                colNames: ['受理编号', '权利人', '流程名称', '关联项目'],
                colModel: [
                    {name: 'BH', index: 'BH', width: '20%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '25%', sortable: false},
                    {name: 'SQLX', index: 'SQLX', width: '25%', sortable: false},
                    {
                        name: 'mydy',
                        index: '',
                        width: '10%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            return '<div style="margin-left:20px;"> <div title="关联项目" style="float:left;cursor:pointer;" class="ui-pg-div ui-inline-edit" id="" onclick="glxm(\'' + rowObject.PROID + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg blue"></span></div>'
                        }
                    }
                ],
                viewrecords: true,
                rowNum: $rownum,
                pager: pager_selector,
                pagerpos: "left",
                altRows: false,
                multiboxonly: false,
                multiselect: false,
                loadComplete: function () {
                    var table = this;
                    setTimeout(function () {
                        updatePagerIcons(table);
                        enableTooltips(table);
                    }, 0);
                    //如果7条设置宽度为auto,如果少于7条就设置固定高度
                    if ($(grid_selector).jqGrid("getRowData").length < $rownum) {
                        $(grid_selector).jqGrid("setGridHeight", $pageHight);
                        $(grid_selector).jqGrid('setGridWidth', 580);
                    } else {
                        $(grid_selector).jqGrid('setGridWidth', 580);
                        $(grid_selector).jqGrid("setGridHeight", $pageHight);
                    }
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    $.each(jqData, function (index, data) {
                        getPpzt(data.PPZT, $(grid_selector), data.FWID);
                    })
                },
                editurl: "", //nothing is saved
                caption: "",
                autowidth: true
            });
        }

        //右边不动产单元，房屋土地
        $("#dyhTab,#fwTdTab").click(function () {
            if (this.id == "fwTdTab") {
                $bdclx = 'TDFW';
                $("#fwTd").addClass("active");
                var grid_selector = "#fwTd-grid-table";
                var parent_column = $(grid_selector).closest('[class*="col-"]');
                $(grid_selector).jqGrid("setGridWidth", parent_column.width());
            } else {
                $("#file").addClass("active");
                var grid_selectorcheckGdfwNum = "#dyh-grid-table";
                var parent_column = $(grid_selector).closest('[class*="col-"]');
                $(grid_selector).jqGrid("setGridWidth", parent_column.width());

            }
        })
        var gdTabOrder = "${gdTabOrder!}";
        var gdTabOrderArray = new Array();
        gdTabOrderArray = gdTabOrder.split(",");
        if (gdTabOrderArray != null && gdTabOrderArray.length > 0) {
            if (gdTabOrderArray[0] == 'td')
                $("#tdTab").click();
            else if (gdTabOrderArray[0] == 'lq')
                $("#lqTab").click();
            else if (gdTabOrderArray[0] == 'cq')
                $("#cqTab").click();
            else
                $("#fwTab").click();
        }
        /*   文字水印  */
        $(".watermarkText").watermark();

        //查询按钮点击事件
        $("#fw_search").click(function () {
            var hhSearch = $("#fw_search_qlr").val();
            var fwUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwJson";
            tableReload("fw-grid-table", fwUrl, {
                hhSearch: hhSearch,
                fczh: '',
                dah: '',
                filterFwPpzt: '${filterFwPpzt!}'
            }, "", fwLoadComplete);
        })
        $("#dyh_search").click(function () {
            resetBdcdyhs();
            var hhSearch = $("#dyh_search_qlr").val();
            var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {hhSearch: hhSearch, bdcdyh: '', bdclx: $bdclx}, dyhColModel, '');
        })
        $("#lq_search").click(function () {
            var hhSearch = $("#lq_search_qlr").val();
            var lqUrl = "${bdcdjUrl}/bdcJgSjgl/getGdLqJson";
            tableReload("lq-grid-table", lqUrl, {hhSearch: hhSearch, lqzh: ''}, '', '');
        })
        $("#cq_search").click(function () {
            var hhSearch = $("#cq_search_qlr").val();
            var cqUrl = "${bdcdjUrl}/bdcJgSjgl/getGdCqJson";
            tableReload("cq-grid-table", cqUrl, {hhSearch: hhSearch, cqzh: ''}, '', '');
        })
        $("#td_search").click(function () {
            var hhSearch = $("#td_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdTdJson";
            tableReload("td-grid-table", tdUrl, {
                hhSearch: hhSearch,
                tdzh: '',
                filterFwPpzt: '${filterFwPpzt!}'
            }, '', '');
        })
        $("#fwTd_search").click(function () {
            resetFwtdids();
            var hhSearch = $("#fwTd_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
            $bdclx = "TDFW";
            tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: ''}, '', '');
        })


        //登记类型变换事件
        $("#djlxSelect").change(function () {
            getSqlxByDjlxAndBdclx('', '');
        })
        $("#addSel").click(function () {
            var ids=$('#fw-grid-table').jqGrid('getGridParam','selarrrow');
            ids.forEach(function(e){
                var cm = $("#fw-grid-table").jqGrid("getRowData",e);
                var index = $.inArray(e, $mulRowid);
                if (index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(e);
                } else if (index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
            })
            $("#gdfwMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        })
        $("#gdfwMulXx").click(function () {
            var msg = "";
            if($mulData.length==0){
                msg = "请选择房产证";
                tipInfo(msg);
                return;
            }
            var table="";
            if (this.id == "gdfwMulXx") {
                var qlids='';
                var qlidArr=new Array();
                for(var i=0;i<$mulData.length;i++){
                    qlidArr.push($mulData[i].QLID)
                }
                var qlids = qlidArr.join(",");
                var windowWidth = window.screen.width;
//                if(windowWidth<1280){
                url= "${bdcdjUrl}/bdcJgSjgl/qurrySelectGdfw?qlids="+qlids;
                showIndexModel(url,"列表详情","1000","650",false);
                return;
//                }
            }
        })
        //保存事件
        $("#save").click(function () {
            var bdcqzh = $selectArray.join("、");
            $("#ybdcqzh").val(bdcqzh);
            var qlzt = $("#qlzt").val();
            if (qlzt == '1') {
                tipInfo("该权利已经注销，无法创建项目！")
                return false;
            }
            $("#workFlowDefId").val($("#sqlxSelect  option:selected").val());
            $("#sqlx").val($("#sqlxSelect  option:selected").text());
            $("#djlx").val($("#djlxSelect  option:selected").val());
            var fwid = $("#fwid").val();
            var gdid;
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var bdcdyh = $("#bdcdyh").val();
            var ppzt = $("#ppzt").val();
            var dyid = $("#dyid").val();
            var ygid = $("#ygid").val();
            var cfid = $("#cfid").val();
            var yyid = $("#yyid").val();
            var gdproid = $("#gdproid").val();
            var qlid = $("#qlid").val();

            var mulGdfw = $("#mulGdfw").val();
            if (dyid == null || dyid == "undefined")
                dyid = "";
            if (ygid == null || ygid == "undefined")
                ygid = "";
            if (cfid == null || cfid == "undefined")
                cfid = "";
            if (yyid == null || yyid == "undefined")
                yyid = "";
            if (gdproid == null || gdproid == "undefined")
                gdproid = "";
            if (mulGdfw == null || mulGdfw == "undefined")
                mulGdfw = "";

            var bdcdyhs = getBdcdyhs();
            var fwtdids = getFwtdids();
            var bdcdyDjIds = getBdcdyDjIds();
            var djlx = $("#djlx").val();
            if (bdcdyhs == null || bdcdyhs == '' && (djlx!=null && djlx!="")) {
                tipInfo("请选择不动产单元");
                return false;
            } else if (gdproid == '' && $("#fw").hasClass("active") && $mulData.length==0) {
                tipInfo("请选择房产证");
                return false;
            } else if (tdid == '' && $("#td").hasClass("active")) {
                tipInfo("请选择土地证");
                return false;
            } else if (lqid == '' && $("#lq").hasClass("active")) {
                tipInfo("请选择林权证");
                return false;
            } else if (cqid == '' && $("#cq").hasClass("active")) {
                tipInfo("请选择草原证");
                return false;
            }
            if ($("#fw").hasClass("active")) {
                $("#tdid").val("");
                $("#cqid").val("");
                $("#lqid").val("");
            } else if ($("#td").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#lqid").val("");
                $("#cqid").val("");
            } else if ($("#lq").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#tdid").val("");
                $("#cqid").val("");
            } else if ($("#cq").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#tdid").val("");
                $("#cqid").val("");
            }
            var sqlxMc =$("#sqlx").val();
            //选择单个房产证或者项目内多幢的情况
            if (($mulData.length==1 && bdcdyhs.length==1) || ($mulData.length==0 && $("#td") || ($mulData.length==1 && bdcdyhs.length==0))) {
                if(sqlxMc!=null && sqlxMc!="" && sqlxMc.indexOf("批量")>-1){
                    tipInfo("含有批量的申请类型需要选择2个及以上房产登记记录！");
                    return false;
                }
                if(mulGdfw=='true'){
                    if (bdcdyDjIds != null && bdcdyDjIds != "")
                        $("#djIds").val(bdcdyDjIds.join("$"));
                    if (bdcdyhs != null && bdcdyhs != "")
                        $("#bdcdyhs").val(bdcdyhs.join("$"));
                    bdcdyh = bdcdyhs[0];
                    $("#bdcdyh").val(bdcdyh);
                    var djIds = $("#djIds").val();
                    $("#djId").val(djIds);
                }
                //存在单个房屋进行登记
                if (fwid == '' && $("#fw").hasClass("active")) {
                    tipInfo("该项目没有房屋！");
                    return false;
                }
                if (bdcdyhs.length > 1) {
                    tipInfo("请选择一个不动产单元号！");
                    return false;
                }
                bdcdyh = bdcdyhs[0];
                $("#bdcdyh").val(bdcdyh);
                if (bdcdyDjIds != null && bdcdyDjIds.length > 0) {
                    $("#djId").val(bdcdyDjIds[0]);
                }else
                    $("#djId").val("");
                if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                    if (fwtdids.length > 1) {
                        tipInfo("请选择一个房屋土地证！");
                        return false;
                    }
                    tdid = fwtdids[0];
                    $("#tdid").val(tdid);
                }
                //针对bug单修改此处校验，申明，此处校验没有问题，为所选房产证进行前台校验，并非校验后台配置的不动产单元是否已处于抵押状态！为满足测试单体验需求，故更改校验提示！
//                if ($("#fw").hasClass("active")) {
//                    var rowData = $("#fw-grid-table").jqGrid("getRowData", qlid);
//                    var status = rowData.STATUS;
//                    if (status.indexOf("抵押") > -1) {
//                        tipInfo("所选的证书已抵押，请重新选择证书!");
//                        return false;
//                    }
//                }
                var djlxdm = $("#djlxSelect  option:selected").val();
                var msg = "所选房屋土地证已被";
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdQlztByBdcid?tdid=" + tdid + "&bdclx=" + $bdclx,
                    dataType: "json",
                    async:false,
                    success: function (result) {
                        if (isNotBlank(result.qlzts)) {
                            var qlzts = result.qlzts.split(",");
                            for (var i = 0; i < qlzts.length; i++) {
                                if (qlzts[i] == "DY") {
                                    msg += "抵押,";
                                }
                                if (qlzts[i] == "ZX") {
                                    msg += "注销,";
                                }
                                if (qlzts[i] == "CF") {
                                    msg += "查封,";
                                }
                            }
                        }
                    }
                });
                msg = msg.substring(0,msg.length-1);
                if(djlxdm == "800" || djlxdm == "1000"){
                    if(msg.indexOf("注销")>-1){
                        tipInfo(msg);
                        return false;
                    }
                }else {
                    if (msg.indexOf("抵押") >-1|| msg.indexOf("注销") >-1|| msg.indexOf("查封")>-1) {
                        tipInfo(msg);
                        return false;
                    }
                }
                var sqlxdm = $("#sqlxSelect  option:selected").val();
                var wfids = "${wfids!}";
                var djId = $("#djId").val();
                if (djId == null || djId == "undefined")
                    djId = "";
                if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                    if (bdcdyhs == null || bdcdyhs == '') {
                        var msg = "没有匹配不动产单元，是否创建项目!";
                        showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproids + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + djId + "','" + lqid + "','" + cqid + "','" + djId + "','" + lqid + "','" + cqid+ "'", "", "");
                    } else{
                        if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && "${matchTdzh!}" == "true") {
                            var msg = "没有匹配房屋土地证，是否创建项目！";
                            showConfirmDialog("提示信息", msg, "checkXm", "'" + gdproids + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + djId + "','" + lqid + "','" + cqid + "','" + djId + "','" + lqid + "','" + cqid + "'", "", "");
                        } else {
                            checkXm(gdproids, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
                        }
                    }
                } else {
                    checkXm(gdproid, bdcdyh, '', fwid, tdid, qlid, ppzt, djId, lqid, cqid);
                }
            }else {
                //存在多个房屋进行登记
                //项目内多幢
                var gdproids="";
                var qlids = "";
                var gdproidArr = new Array();
                var qlidArr = new Array();
                for (var i = 0; i < $mulData.length; i++) {
                    gdproidArr.push($mulData[i].PROID)
                    qlidArr.push($mulData[i].QLID);
                }
                gdproids = gdproidArr.join(",");
                $("#gdproids").val(gdproids);
                qlids = qlidArr.join(",");
                $("#qlids").val(qlids);

                if ($("#fw").hasClass("active")) {
                    $("#tdid").val("");
                    $("#cqid").val("");
                    $("#lqid").val("");
                    $("#bdcdyh").val("");
                    $("#djId").val("");
                    $("#gdproid").val("");
                    $("#qlid").val("");
                }

                if(sqlxMc!=null && sqlxMc!="" && sqlxMc.indexOf("批量")<0 && $("#gdproids").val()!=null && $("#gdproids").val()!=""){
                    tipInfo("请选择批量的申请类型进行处理！");
                    return false;
                }
                if ($("#fw").hasClass("active")) {
                    //选一条权利，但是匹配不同的不动产单元，属于一证多房的情况,而不是项目内多幢
//                    if(bdcdyhs.length>1 || bdcdyhs==null || bdcdyhs==""){
//                        if(bdcdyhs.length>1 || djlx=='800'){
                    if(djlx=='800'){
                        createMulXm(gdproids,qlids);
                    }else{
                        checkMulXmFw(gdproids,qlids);
                    }
                    //       }
                    //  }
                }
            }
        })
        var checkMulXmFw = function(gdproids,qlids){
            var bdclx = $bdclx;
            var options = {
                url: '${bdcdjUrl}/bdcJgSjgl/checkMulXmFw',
                type: 'post',
                dataType: 'json',
                data: $("#form").serialize(),
                success: function (data) {
                    if(data!=null && (data.msg=="" || data.msg==null)){
                        createMulXm(gdproids,qlids);
                    }else{
                        tipInfo(data.msg);
                        return false;
                    }
                },
                error: function (data) {
                    setTimeout($.unblockUI, 10);
                }
            };
            $.ajax(options);
        }
        $("#fz").click(function () {
            var fwid = $("#fwid").val();
            var djlx = $("#djlxSelect  option:selected").text();
            var gdid;
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var bdcdyh = $("#bdcdyh").val();
            var bdclx = "";
            var sqlxSelect = $("#sqlxSelect").val();
            if (bdcdyh == '') {
                tipInfo("请先匹配");
                return false;
            } else if (fwid == '' && $("#fw").hasClass("active")) {
                tipInfo("请先匹配");
                return false;
            } else if (tdid == '' && $("#td").hasClass("active")) {
                tipInfo("请先匹配");
                return false;
            } else if (lqid == '' && $("#lq").hasClass("active")) {
                tipInfo("请先匹配");
                return false;
            } else if (cqid == '' && $("#cq").hasClass("active")) {
                tipInfo("请先匹配");
                return false;
            }
            if ($("#fw").hasClass("active")) {
                gdid = $("#dah").val();
                bdclx = "TDFW";
            } else if ($("#td").hasClass("active")) {
                gdid = tdid;
                bdclx = "TD";
            } else if ($("#lq").hasClass("active")) {
                gdid = lqid;
                bdclx = "TDSL";
            } else if ($("#cq").hasClass("active")) {
                gdid = cqid;
                bdclx = "TDQT";
            }
            var options = {
                url: '${bdcdjUrl}/bdcJgSjgl/zs',
                type: 'post',
                dataType: 'json',
                data: {gdid: gdid, bdcdyh: bdcdyh, sqlxSelect: sqlxSelect, djlx: djlx, bdclx: bdclx},
                success: function (data) {
                    hideModal(data);
                },
                error: function (data) {
                }
            };
            $.ajax(options);
        });
        //匹配
        $("#match").click(function () {
            var qlzt = $("#qlzt").val();
            if (qlzt == '1') {
                tipInfo("该权利已经注销，无法匹配！");
                return false;
            }
            var fwid = $("#fwid").val();
            var gdid;
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var bdcdyh = $("#bdcdyh").val();
            var tdzh = $("#tdzh").val();
            var ppzt = $("#ppzt").val();
            var mulGdfw = $("#mulGdfw").val();
            var gdproid = $("#gdproid").val();
            var qlid = $("#qlid").val();

            if (gdproid == null || gdproid == "undefined")
                gdproid = "";
            if (mulGdfw == null || mulGdfw == "undefined")
                mulGdfw = "";
            var bdcdyhs = getBdcdyhs();
            var fwtdids = getFwtdids();
            var bdcdyDjIds = getBdcdyDjIds();
            if (bdcdyhs == null || bdcdyhs == '') {
                tipInfo("请选择不动产单元");
                return false;
            } else if (qlid == '' && $("#fw").hasClass("active")&& $mulData.length==0) {
                tipInfo("请选择房产证");
                return false;
            } else if (tdid == '' && $("#td").hasClass("active")) {
                tipInfo("请选择土地证");
                return false;
            } else if (lqid == '' && $("#lq").hasClass("active")) {
                tipInfo("请选择林权证");
                return false;
            } else if (cqid == '' && $("#cq").hasClass("active")) {
                tipInfo("请选择草原证");
                return false;
            }
            if ($("#fw").hasClass("active")) {
                $("#tdid").val("");
                $("#cqid").val("");
                $("#lqid").val("");
            } else if ($("#td").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#lqid").val("");
                $("#cqid").val("");
            } else if ($("#lq").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#tdid").val("");
                $("#cqid").val("");
            } else if ($("#cq").hasClass("active")) {
                //$("#gdproid").val("");
                $("#fwid").val("");
                $("#tdid").val("");
                $("#cqid").val("");
            }
            if (mulGdfw != "true") {
                if (fwid == '' && $("#fw").hasClass("active")) {
                    tipInfo("该项目没有房屋！");
                    return false;
                }
//                if (bdcdyhs.length > 1) {
//                   tipInfo("请选择一个不动产单元号！");
//                    return false;
//                }
                bdcdyh = bdcdyhs[0];
                $("#bdcdyh").val(bdcdyh);
                if (bdcdyDjIds != null && bdcdyDjIds.length > 0) {
                    $("#djId").val(bdcdyDjIds[0]);
                }else
                    $("#djId").val("");
                if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
                    if (fwtdids.length > 1) {
                        tipInfo("请选择一个房屋土地证！");
                        return false;
                    }
                    tdid = fwtdids[0];
                    $("#tdid").val(tdid);
                }

                var djId = $("#djId").val();
                if (djId == null || djId == "undefined")
                    djId = "";
                var sqlxdm = $("#sqlxSelect  option:selected").val();
                var wfids = "${wfids!}";
                if (wfids != null && wfids != "" && wfids.indexOf(sqlxdm) < 0) {
                    //当不等于商品房转移登记的需要匹配土地证
                    if ($("#fw").hasClass("active") && (fwtdids == null || fwtdids == '') && "${matchTdzh!}" == "true") {
                        var msg = "没有匹配房屋土地证，是否匹配！";
                        showConfirmDialog("提示信息", msg, "dyhPic", "'" + gdproid + "','" + bdcdyh + "','" + tdzh + "','" + fwid + "','" + tdid + "','" + ppzt + "','" + djId + "','" + qlid + "','" + lqid + "','" + cqid + "'", "", "");
                    } else {
                        dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, ppzt, djId, qlid, lqid, cqid);
                    }
                } else {
                    dyhPic(gdproid, bdcdyh, tdzh, fwid, tdid, ppzt, djId, qlid, lqid, cqid);
                }
            } else {
                tipInfo("该项目存在多个房屋，请到多个房屋匹配页面进行匹配！");
            }

        });

        //取消匹配
        $("#dismatch").click(function () {
            var qlid = $("#qlid").val();
            var fwid = $("#fwid").val();
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var bdcdyh = $("#bdcdyh").val();
            var ppzt = $("#ppzt").val();
            var gdproid = $("#gdproid").val();
            if (gdproid == null || gdproid == "undefined")
                gdproid = "";
            var bdcdyhs = getBdcdyhs();
            var fwtdids = getFwtdids();
            //未选择项目
            if (gdproid == '' && $("#fw").hasClass("active")) {
                tipInfo("请选择房产证");
                return false;
            } else if (tdid == '' && $("#td").hasClass("active")) {
                tipInfo("请选择土地证");
                return false;
            } else if (lqid == '' && $("#lq").hasClass("active")) {
                tipInfo("请选择林权证");
                return false;
            } else if (cqid == '' && $("#cq").hasClass("active")) {
                tipInfo("请选择草权证");
                return false;
            } else if (ppzt == '' || ppzt == '0') {
                //多个房屋匹配页面获得ppzt和数据库中不一致
                if (gdproid != '' && $("#fw").hasClass("active")) {
                    $.ajax({
                        url: '${bdcdjUrl}/bdcSjgl/getPpzt?gdproid=' + gdproid,
                        datatype: 'GET',
                        success: function (data) {
                            if (data == '0' || data == '') {
                                tipInfo("该项目未匹配");
                                return false;
                            } else if (data == '1' || data == '2') {
                                showConfirmDialog("提示信息", "是否解除匹配", "disPic", "'" + gdproid + "','" + tdid + "','" + lqid + "','" + cqid + "','" + qlid + "'", "", "");
                            } else {
                                tipInfo("已匹配项目未删除！");
                            }
                        }
                    });

                } else {
                    tipInfo("该项目未匹配");
                    return false;
                }
            }
            else if (ppzt == '1' || ppzt == '2') {
                showConfirmDialog("提示信息", "是否解除匹配", "disPic", "'" + gdproid + "','" + tdid + "','" + lqid + "','" + cqid + "','" + qlid + "'", "", "");
            } else {

                tipInfo("已匹配项目未删除！");
            }


        });

        //关联项目
        $("#ppxm").click(function () {
            var gdproid = $("#gdproid").val();
            if (gdproid == '' && $("#fw").hasClass("active")&& $mulData.length==0) {
                tipInfo("请选择房产项目");
                return false;
            }
            var bdcxmUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcXmJson";
            var hhSearch = '$$$';
//           bdcxmTableInit();
            tableReload("bdcxm-grid-table1", bdcxmUrl, {hhSearch: hhSearch}, '', '');
            bdcxmTableInit();
            $("#fileInput1").show();

        });
        //删除关联项目
        $("#deletexmgl").click(function () {
            var gdproid = $("#gdproid").val();
            var tdid = $("#tdid").val();
            var lqid = $("#lqid").val();
            var cqid = $("#cqid").val();
            var ppzt = $("#ppzt").val();
            if (gdproid == '' && $("#fw").hasClass("active")) {
                tipInfo("请选择房产项目");
                return false;
            } else if (tdid == '' && $("#td").hasClass("active")) {
                tipInfo("请选择土地证");
                return false;
            } else if (lqid == '' && $("#lq").hasClass("active")) {
                tipInfo("请选择林权证");
                return false;
            } else if (cqid == '' && $("#cq").hasClass("active")) {
                tipInfo("请选择草权证");
                return false;
            } else if (ppzt == '' || ppzt == '0') {
                tipInfo("该项目没匹配不动产单元");
                return false;
            }
            var gdproid = $("#gdproid").val();
            if (gdproid == "") {
                tipInfo("请选择一条要删除的数据!");
                return false;
            }
            var msg = "确认删除";
            showConfirmDialog("提示信息", msg, "deletexmgl", "'" + gdproid + "'", "", "");
        })

        $("#fileHide1").click(function () {
            $("#fileInput1").hide();
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


//新增按钮点击事件
        $("#gdFwAdd,#gdLqAdd,#gdTdAdd,#gdCqAdd").click(function () {
            var bdclxId = this.id;
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/gdXxLr/getUUid",
                success: function (result) {
                    if (result != null && result != "") {
                        if (bdclxId == "gdFwAdd") {
                            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid=" + result + "&iscp=true");
                        } else if (bdclxId == "gdLqAdd") {
                            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write");
                        } else if (bdclxId == "gdTdAdd") {
                            addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid=" + result + "&iscp=true&tdid=" + result);
                        } else if (bdclxId == "gdCqAdd") {
                            addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                        }
                    }
                }
            });
        })


        $("#fileHide").click(function () {
            $("#fileInput").hide();
        });

        $("#fileSub").click(function () {
            //遮罩
            $.blockUI({message: "请稍等……"});
        });

//修改按钮点击事件
        $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {
            if (this.id == "gdFwUpdate") {
                var gdproid = $("#gdproid").val();
                if (gdproid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }

                addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid=" + gdproid + "&iscp=true");
            } else if (this.id == "gdLqUpdate") {
                var lqid = $("#lqid").val();
                if (lqid == null || lqid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=" + lqid + "&iscp=true");
            } else if (this.id == "gdTdUpdate") {
                var tdid = "";
                if ($("#fwTd").hasClass("active")) {
                    var tdids = getFwtdids();
                    if (tdids == null && tdids == "") {
                        tipInfo("请选择一条要修改的数据!");
                        return false;
                    }
                    else if (tdids.length > 1) {
                        tipInfo("请选择一条要修改的数据!");
                        return false;
                    } else if (tdids.length == 0) {
                        tipInfo("请选择一条要修改的数据!");
                        return false;
                    }
                    else
                        tdid = getFwtdids();
                } else {
                    tdid = $("#tdid").val();
                    if (tdid == null || tdid == "") {
                        tipInfo("请选择一条要修改的数据!");
                        return false;
                    }
                }

                var gdproid = $("#gdproid").val();
                if (gdproid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }

                addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid=" + gdproid + "&iscp=true&tdid=" + tdid);
            } else if (this.id == "gdCqUpdate") {
                var cqid = $("#cqid").val();
                if (cqid == null || cqid == "") {
                    tipInfo("请选择一条要修改的数据!");
                    return false;
                }
                addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=" + cqid);
            }
        })

        //注销按钮点击事件
        $("#gdFwZx,#gdTdZx").click(function () {
            var qlid = $("#qlid").val();
            var qlzt = $("#qlzt").val();
            if (qlid == "") {
                tipInfo("请选择一条要注销的数据!");
                return false;
            }

            var url = "";
            if (qlzt == '0' || qlzt == "") {
                if (this.id == "gdFwZx") {
                    url = "${bdcdjUrl}/bdcJgSjgl/zxQl?qlid=" + qlid + "&lx=gdFw";
                    $.ajax({
                        type: "GET",
                        url: url,
                        dataType: "json",
                        success: function (jsonData) {
                            if (jsonData.result == "success") {
                                // var qlzt = '1';
                                //var rowid = $("#gdproid").val();
                                //var table = "#fw-grid-table";
                                //setQlzt($(table),qlzt,rowid);
                                //刷新
                                $("#fw_search").click();
                                $("#qlzt").val(qlzt);
                            }
                        },
                        error: function (data) {

                        }
                    })
                } else if (this.id == "gdTdZx") {
                    url = "${bdcdjUrl}/bdcJgSjgl/zxQl?qlid=" + qlid + "&lx=gdTd";
                    $.ajax({
                        type: "GET",
                        url: url,
                        dataType: "json",
                        success: function (jsonData) {
                            if (jsonData.result == "success") {
                                //刷新
                                $("#td_search").click();
                                $("#qlzt").val(qlzt);
                            }
                        },
                        error: function (data) {

                        }
                    })
                }

            } else {
                tipInfo("该权利已经注销");
            }


        })

        //解除注销按钮点击事件
        $("#gdFwJcZx,#gdTdJcZx").click(function () {
            var qlid = $("#qlid").val();
            var qlzt = $("#qlzt").val();
            if (qlid == "") {
                tipInfo("请选择一条要解除注销的数据!");
                return false;
            }

            if (qlzt == '1' || qlzt == "") {
                if (this.id == "gdFwJcZx") {
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/bdcJgSjgl/jczxQl?qlid=" + qlid + "&lx=gdFw",
                        dataType: "json",
                        success: function (jsonData) {
                            if (jsonData.result == "success") {

                                //var qlzt = '0';
                                //var rowid = $("#gdproid").val();
                                //var table = "#fw-grid-table";
                                //setQlzt($(table),qlzt,rowid);
                                //刷新
                                $("#fw_search").click();
                                $("#qlzt").val(qlzt);
                            }
                        },
                        error: function (data) {
                            alert("失败");
                        }
                    })
                } else if (this.id == "gdTdJcZx") {
                    $.ajax({
                        type: "GET",

                        url: "${bdcdjUrl}/bdcJgSjgl/jczxQl?qlid=" + qlid + "&lx=gdTd",
                        dataType: "json",
                        success: function (jsonData) {
                            if (jsonData.result == "success") {
                                $("#td_search").click();
                                $("#qlzt").val(qlzt);
                            }
                        },
                        error: function (data) {

                        }
                    })
                }

            } else {
                tipInfo("该权利已经是正常状态");
            }
        })
        $("#pptzs").click(function () {
//            if ($mulData.length != 1) {
//                tipInfo("请选择一个房屋");
//                return false;
//            }
            var tdid="";
            var fwtdids = getFwtdids();
            if (fwtdids != null && fwtdids != "" && $bdclx == "TDFW") {
//                if (fwtdids.length > 1) {
//                    tipInfo("请选择一个房屋土地证！");
//                    return false;
//                }
                tdid = fwtdids[0];
                $("#tdid").val(tdid);
            }
            var qlid =$("#qlid").val();

            var ppzt = $("#ppzt").val();
//            if (ppzt != '2') {
//                tipInfo("请匹配！");
//                return;
//            }
            var bdcdyh = $("#bdcdyh").val();
            if (bdcdyh == '') {
                var bdcdyhArr = $("#bdcdyhs").val().split(',');
                bdcdyhArr = unique(bdcdyhArr);
                bdcdyh = bdcdyhArr.join(',')
            }
            var url = "${reportUrl!}/ReportServer?reportlet=print%2Fgd_pptzs.cpt&op=write&qlid=" + qlid + "&bdcdyh=" + bdcdyh + "&tdqlid=" + tdid;
            window.open(url);

        })
        $("#queryFw").click(function () {
            var qlids = $("#qlids").val();
            if (qlids = null || qlids == "" || qlids == "undefined") {
                tipInfo("请选择房屋！");
                return;
            }
            var url = "${bdcdjUrl}/bdcJgSjgl/openMulGdFw";
            window.open(url);

        })
        $("#clean").click(function () {
            $("#gdproids").val("");
            $("#qlids").val("");
            $mulData.splice(0,$mulData.length);
            $mulRowid.splice(0,$mulRowid.length);
            $selectedInput.splice(0,$selectedInput.length);
            $selectArray.splice(0,$selectArray.length);
            $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            $("#fw-grid-table").trigger("reloadGrid");
            tipInfo("清空成功");
        })
        $("#addFw").click(function () {
            var proids = $("#gdproids").val();
            var qlids = $("#qlids").val();
            if (proids == null || proids == "" || proids == "undefined")
                proids = "";
            if (qlids == null || qlids == "" || qlids == "undefined")
                qlids = "";
            var fwXl = $('input[name="fwXl"]:checked');
            if (fwXl.length == 0) {
                tipInfo("请选择房屋");
                return false;
            }
            $.each(fwXl, function (index, obj) {
                var proid = $(obj).parent().find('input[name="fwxlTemp"]').val();
                var qlid = $(obj).parent().find('input[name="fwQlid"]').val();

                if (proids.indexOf("proid") < 0) {
                    if (index == 0 && proids == "") {
                        proids = proid;
                    } else if ((proids + ",").indexOf(proid + ",") < 0) {
                        proids = proids + "," + proid;
                    }
                }
                if (qlids.indexOf("qlid") < 0) {
                    if (index == 0 && qlids == "") {
                        qlids = qlid;
                    } else if ((qlids + ",").indexOf(qlid + ",") < 0) {
                        qlids = qlids + "," + qlid;
                    }
                }
            })

            if (proids != "") {
                $("#gdproids").val(proids);
                $("#qlids").val(qlids);
                tipInfo("添加成功");
            }
        })
    })

    function unique(arr) {
        var result = [], hash = {};
        for (var i = 0, elem; (elem = arr[i]) != null; i++) {
            if (!hash[elem]) {
                result.push(elem);
                hash[elem] = true;
            }
        }
        return result;
    }

    var disPic = function (gdproid, tdid, lqid, cqid,qlid) {
        var recordId = "";
        var grid_selector = "";
        var fwid = $("#fwid").val();
        var gdid = '';
        var gdtab = '';
        if ($("#fw").hasClass("active")) {
            recordId = gdproid;
            grid_selector = "#fw-grid-table";
            gdid = gdproid;
            gdtab = "fw";
        } else if ($("#td").hasClass("active")) {
            gdid = tdid;
            gdtab = "td";
            recordId = qlid;
            grid_selector = "#td-grid-table";
        } else if ($("#lq").hasClass("active")) {
            gdid = lqid;
            gdtab = "lq";
            recordId = lqid;
            grid_selector = "#lq-grid-table";
        } else if ($("#cq").hasClass("active")) {
            gdid = cqid;
            gdtab = "cq";
            recordId = cqid;
            grid_selector = "#cq-grid-table";
        }
        $.ajax({
            url: '${bdcdjUrl}/bdcSjgl/dismatch',
            type: 'post',
            datatype: 'json',
            data: {gdid: gdid, gdproid: gdproid, tdid: tdid, lqid: lqid, cqid: cqid, gdtab: gdtab, fwid: fwid},
            success: function (data) {
                if (data == "fail") {
                    alert("删除失败！");
                } else if (data == "error") {
                    alert("项目id不能为空！");
                } else {
                    alert("删除成功！");
                    $("#dyh_search").click()
                    if ($("#fw").hasClass("active")) {
                        changePpzt("0", $(grid_selector), recordId);
                        $("#fw_search").click();
                        $("#fwTd_search").click();
                    } else if ($("#td").hasClass("active")) {
                        changeQtPpzt("0", $(grid_selector), recordId);
                        $("td_search").click();
                    } else if ($("#lq").hasClass("active")) {
                        $("#lq_search").click();
                    } else if ($("#cq").hasClass("active")) {
                        $("#cq_search").click();
                    }

                }
            },
            error: function (data) {
                alert("删除失败！");
            }
        });

    }

    var deletexmgl = function (gdproid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/deletexmgl?gdproid=" + gdproid,
            dataType: "json",
            success: function (jsonData) {
                alert(jsonData.result);
                $("#fw_search").click();
            },
            error: function (data) {
            }
        })
    }

    function glxm(proid) {
        // alert(proid);
        var ppzt = $("#ppzt").val();

        if (ppzt == 0 || ppzt == '') {
            tipInfo("请先匹配，再关联项目");
            return false;
        }
        if ($("#fw").hasClass("active") && ppzt == 3) {
            tipInfo("该证书正在发证，不能再次发证！");
            return false;
        } else if ($("#fw").hasClass("active") && ppzt == 4) {
            tipInfo("该证书已经发证，不能再次发证！");
            return false;
        }

        var yz = yzGlxmxx(proid);
        if (yz) {
            createGlxm(proid);
        } else {
            var msg = "中间库的坐落与收件单的不相符，是否继续创建？";
            showConfirmDialog("提示信息", msg, "createGlxm", "'" + proid + "'", "", "");
        }
    }

    function yzGlxmxx(proid) {
        //目前只支持房产项目，土地项目以后考虑
        var zl = $("#xmmc").val();
        var pd = false;
        $.ajax({
            url: "${bdcdjUrl!}/bdcJgSjgl/yzGlxmxx?proid=" + proid + "&zl=" + encodeURI(encodeURI(zl)),
            type: "GET",
            async: false,
            success: function (data) {
                pd = data;
            }
        });

        return pd;

    }

    function createGlxm(proid) {
        $.ajax({
            url: '${bdcdjUrl}/bdcJgSjgl/isCancel?bdclx=' + $bdclx,
            type: 'post',
            dataType: 'json',
            data: $("#form").serialize(),
            success: function (data) {
                if (data.hasOwnProperty("result")) {
                    if (data.result) {
                        createGlxmFun(proid);
                    } else if (!data.result && data.msg != null && data.msg != "") {
                        setTimeout($.unblockUI, 10);
                        if (data.checkModel == "ALERT")
                            alert(data.msg);
                        else if (data.checkModel == "CONFIRM") {
                            showConfirmDialog("提示信息", data.msg, "createGlxmFun", "'" + gdproid + "'", "", "");
                        }

                    } else {
                        setTimeout($.unblockUI, 10);
                        alert("创建项目失败！")
                    }
                } else {
                    setTimeout($.unblockUI, 10);
                    var str = "";
                    $.each(data.resultList, function (index, obj) {
                        str += obj + "\n";
                    })
                    window.open("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=" + $("#fwid").val());
                }

            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                alert("创建项目失败！")
            }
        });

    }
    var createGlxmFun = function (proid) {
        var bdcqzh = $selectArray.join("、");
        $("#ybdcqzh").val(bdcqzh);
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcJgSjgl/glxm?proid=" + proid + "&bdclx=" + $bdclx,
            dataType: "json",
            data: $("#form").serialize(),
            success: function (data) {
                if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                    setTimeout($.unblockUI, 10);
                    openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid, '_task');
                    $("#fileInput").hide();
                    $("#fw_search").click();
                } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                    setTimeout($.unblockUI, 10);
                    alert("创建项目成功!");

                } else {
                    setTimeout($.unblockUI, 10);
                    alert(data.msg);
                }

            },
            error: function (data) {
            }
        });
    }

    //设置权利状态
    function setQlzt(table, qlzt, rowid) {
        var cellVal = "";
        if (qlzt == '0') {
            cellVal = '<span class="label label-success">正常</span>';
        } else if (qlzt == '1') {
            cellVal = '<span class="label label-gray">注销</span><span> </span>';
        }
        table.setCell(rowid, "STATUS", cellVal);
    }

    function getSqlxByDjlxAndBdclx(djlx, wfid) {
        if (djlx == null || djlx == "")
            djlx = $("#djlxSelect  option:selected").text();
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getSqlxByDjlx",
            data: {djlx: djlx, bdclx: $bdclx},
            dataType: "json",
            success: function (result) {
                //清空
                $("#sqlxSelect").html("");
                if (result != null && result != '') {
                    $.each(result, function (index, data) {
                        if (data.DM == "24D768DE8B8F4CD59F70E621C2CAB2E2" || data.DM == "19D3CDE088174478943341B32EF3238C" || data.DM == wfid)
                            $("#sqlxSelect").append('<option value="' + data.DM + '" selected="selected">' + data.MC + '</option>');
                        else
                            $("#sqlxSelect").append('<option value="' + data.DM + '" >' + data.MC + '</option>');
                    })
                }
                $("#sqlxSelect").trigger("chosen:updated");
                var sqlx = $("#sqlxSelect").val();
                var wfids = "${wfids!}";
                if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1)
                    hideFwtdGrid();
            },
            error: function (data) {
            }
        });
    }
    function hideModal(proid) {
        if (proid && proid != undefined && proid != "undefined") {
            openWin('${bdcdjUrl}/bdcJgSjgl/formTab?proid=' + proid);
        }
    }
    function openWin(url) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        var openner = window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
        if (window.opener && !window.opener.closed) {
            alert(openner);
        }
    }


    //选择草原证
    function cqSel(cqzh, cqid, qlrmc, ppzt) {
        //赋值
        if (cqid && cqid != 'undefined') {
            $("#cqid").val(cqid);
        } else {
            $("#cqid").val("");
        }
        if (qlrmc && qlrmc != 'undefined') {
            $("#xmmc").val(qlrmc);
        } else
            qlrmc = "";
        if (ppzt && ppzt != 'undefined')
            $("#ppzt").val(ppzt);
        else
            $("#ppzt").val("");
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByGdid?gdid=" + cqid + "&bdclx=" + $bdclx,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {

                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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
                                    selDyhByFw(data.bdcdyh, "");
                                    //定位
//                                bdzDyMap(data.bdcdyh);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                                    tableReload("dyh-grid-table", dyhUrl, {
                                        hhSearch: qlrmc,
                                        bdcdyh: '',
                                        bdclx: $bdclx
                                    }, dyhColModel, dyhLoadComplete);
                                }

                            } else {
                                if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                    selDyhByFw(data.BDCDYH, "");
                                    //定位
//                                bdzDyMap(data.BDCDYH);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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
    }
    //选择林权证
    function lqSel(lqzh, lqid, qlrmc, ppzt) {

        //赋值
        if (lqid && lqid != 'undefined') {
            $("#lqid").val(lqid);
        } else {
            $("#lqid").val("");
        }
        if (qlrmc && qlrmc != 'undefined') {
            $("#xmmc").val(qlrmc);
        } else
            qlrmc = "";
        if (ppzt && ppzt != 'undefined')
            $("#ppzt").val(ppzt);
        else
            $("#ppzt").val("");
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByGdid?gdid=" + lqid + "&bdclx=" + $bdclx,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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
                                    selDyhByFw(data.bdcdyh, "");
                                    //定位
//                                bdzDyMap(data.bdcdyh);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                                    tableReload("dyh-grid-table", dyhUrl, {
                                        hhSearch: qlrmc,
                                        bdcdyh: '',
                                        bdclx: $bdclx
                                    }, dyhColModel, dyhLoadComplete);
                                }
                            } else {
                                if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                    selDyhByFw(data.BDCDYH, "");
                                    //定位
//                                bdzDyMap(data.BDCDYH);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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

    }
    //选择土地证号

    function tdSel(proid, qlid, tdzh, qlzt) {
        //删除的原证号
        if ($selectArray.length == 1) {
            $selectArray.remove(0);
        } else {
            for (var i = 0; i != $selectArray.length; i++) {
                if ($selectArray[0] == tdzh) {
                    $selectArray.splice(i, 1);
                }
            }
        }
        $selectArray.push(tdzh);
        var rowData = $("#td-grid-table").jqGrid("getRowData", qlid);
        var ppzt = $(rowData.PPZT).attr("value");
        if (qlid && qlid != 'undefined') {
            $("#qlid").val(qlid);
        } else {
            $("#qlid").val("");
        }
        if (proid && proid != 'undefined') {
            $("#gdproid").val(proid);
        } else {
            $("#gdproid").val("");
        }
        if (ppzt && ppzt != 'undefined')
            $("#ppzt").val(ppzt);
        else
            $("#ppzt").val("");

        if (qlzt && qlzt != 'undefined')
            $("#qlzt").val(qlzt);
        else
            qlzt = "";
        var qlrmc = $("#td-grid-table").getCell(qlid, "ZL");
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByTdidDjh?qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
                    $("#file").addClass("active");
                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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
                            if (data.tdid && data.tdid != 'undefined')
                                $("#tdid").val(data.tdid);
                            if (data.gdid && data.gdid != 'undefined')
                                $("#tdid").val(data.gdid);
                            if (data.hasOwnProperty("bdcdyh")) {
                                if (data.bdcdyh && data.bdcdyh != 'undefined') {
                                    selDyhByFw(data.bdcdyh, "");
                                    //定位
//                                bdzDyMap(data.bdcdyh);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                                    tableReload("dyh-grid-table", dyhUrl, {
                                        hhSearch: qlrmc,
                                        bdcdyh: '',
                                        bdclx: $bdclx
                                    }, dyhColModel, dyhLoadComplete);
                                }
                            } else {

                                if (data.BDCDYH && data.BDCDYH != 'undefined') {
                                    selDyhByFw(data.BDCDYH, "");
                                    //定位
//                                bdzDyMap(data.BDCDYH);
                                } else {
                                    $("#dyh_search_qlr").next().hide();
                                    $("#dyh_search_qlr").val(qlrmc);
                                    //无匹配数据 不刷新
                                    $("#file").addClass("active");
                                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
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

    }


    function fwSel(proid, qlrmc, ppzt, djlx, qlid, qlzt, fczh) {
//        if (!obj.checked) {
//            //删除的原证号
//            if ($selectArray.length == 1) {
//                $selectArray.remove(0);
//            } else {
//                for (var i = 0; i != $selectArray.length; i++) {
//                    if ($selectArray[0] == fczh) {
//                        $selectArray.splice(i, 1);
//                    }
//                }
//            }
//            return false;
//        }
        //遮罩
        $.blockUI({message: "请稍等……"});

        //赋值
        if (qlrmc && qlrmc != 'undefined')
            $("#xmmc").val(qlrmc);
        else
            qlrmc = "";
        var ppzt = $("#fw-grid-table").getCell(qlid, "PPZT");
        ppzt = getPpztVal(ppzt);
        if (ppzt && ppzt != 'undefined')
            $("#ppzt").val(ppzt);
        else {
            $("#ppzt").val("");
            ppzt = "";
        }
        if (qlid && qlid != 'undefined')
            $("#qlid").val(qlid);
        else
            qlid = "";
        if (qlzt && qlzt != 'undefined')
            $("#qlzt").val(qlzt);
        else
            qlzt = "";
        if (proid && proid != 'undefined')
            $("#gdproid").val(proid);
        else
            $("#gdproid").val("");

        if (djlx && djlx == 'undefined')
            djlx = "";
        if (qlrmc && qlrmc != 'undefined')
            $("#qlr").val(qlrmc);
        else
            $("#qlr").val("");

        proid = $("#gdproid").val();
        $("#dyhTab").click();
        $("#tdid").val('');
        resetBdcdyhs();
        resetFwtdids();

        //    判断是否是多个房屋
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/checkGdfwNum?gdproid=" + proid + "&bdclx=" + $bdclx + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                $("#mulGdfw").val(result.mulGdfw);
                var fwzl = $("#fw-grid-table").getCell(qlid, "FWZL");
                $("#fwid").val(result.fwid);
                $("#xmmc").val(fwzl);
                $("#qlr").val(fwzl);
                $.ajax({
                    type: "GET",
                    url: "${bdcdjUrl}/bdcJgSjgl/getGdFcDjlxToSqlxWdid?djlx=" + djlx,
                    dataType: "json",
                    success: function (sqlxSesult) {
                        //获取过渡房产数据对应的不动产登记类型
                        if (sqlxSesult != null && sqlxSesult != "" && sqlxSesult.busiName != "") {
                            $("#djlxSelect option").each(function () {
                                if ($(this).text() == sqlxSesult.busiName) {
                                    $(this).attr('selected', 'selected');
                                }
                                $("#djlxSelect").trigger("chosen:updated");
                            });
                            getSqlxByDjlxAndBdclx(sqlxSesult.busiName, sqlxSesult.wfid);
                        } else {
                            var busiName = "首次登记";
                            $("#djlxSelect option").each(function () {
                                if ($(this).text() == busiName) {
                                    $(this).attr('selected', 'selected');
                                }
                                $("#djlxSelect").trigger("chosen:updated");
                            });

                            getSqlxByDjlxAndBdclx(busiName, "");
                        }
                        if (result != null && result != "" && result.mulGdfw != "false") {
                            var msg = "";
                            var readOnly = "false";
                            if (ppzt == "4") {
                                msg = "该项目存在多个房屋并且已经进行过匹配，是否继续查看匹配情况！";
                                readOnly = "true";
                            } else if (ppzt == "3") {
                                msg = "该项目存在多个房屋并且已经正在发证，是否继续查看匹配情况！";
                                readOnly = "true";
                            }
                            else if (ppzt == "1" || ppzt == "2")
                                msg = "该项目存在多个房屋并且已经进行过匹配，是否继续匹配！";
                            else
                                msg = "该项目存在多个房屋，是否继续匹配！";
                            //                showConfirmDialog("提示信息",msg,"showMulGdFwPic","'"+bdcid+"','" +qlrmc+"','"+ppzt+"','"+djlx+"'");
                            disableDyhTabDiv("file");
                            clearTabDiv();
                            showConfirmDialog("提示信息", msg, "showMulGdFwPic", "'" + proid + "','" + qlid + "','" + readOnly + "'", "updateGdFwAndDyhSel", "'" + djlx + "'");
                            //去掉遮罩
                            setTimeout($.unblockUI, 10);
                        } else {
                            visableDyhTabDiv("file");
                            $("#fwid").val(result.fwid);


                            qlrmc = fwzl;
                            picDyh(proid, result.fwid, qlrmc, ppzt, djlx, qlid);
                            setTimeout($.unblockUI, 10);
                        }
                    },
                    error: function (data) {
                    }
                });
            }
        });


    }

    //匹配不动产单元
    var picDyh = function (proid, fwid, qlrmc, ppzt, djlx, qlid) {
        //通过fczh获取hs_index
        var bdcdyhs = getBdcdyhs();
        var fwtdids = getFwtdids();

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByQlid?proid=" + proid + "&bdclx=" + $bdclx + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);
                    //无匹配数据 不刷新
//                $("#file").addClass("active");
                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
                        bdcdyh: '',
                        bdclx: $bdclx
                    }, dyhColModel, dyhLoadComplete);
                } else {
                    //清空查询内容
                    $("#dyh_search_qlr").val("");
                    $("#dyh_search_qlr").next().show();
                    if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length > 1) {
                        selDyhByFw("", bdcdyhs);
                        $("#bdcdyhs").val(bdcdyhs);
                    } else if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length == 1) {
                        selDyhByFw(bdcdyhs.join(""), "");
                        $("#bdcdyhs").val("");
                        $("#bdcdyh").val(bdcdyhs.join(""));
                    } else if (result[0].bdcdyh != null && result[0].bdcdyh != "" && result[0].bdcdyh != "undefined") {
                        selDyhByFw(result[0].bdcdyh, "");
                        $("#bdcdyhs").val("");
                        $("#bdcdyh").val(result[0].bdcdyh);
                    } else if (result[0].BDCDYH != null && result[0].BDCDYH != "" && result[0].BDCDYH != "undefined") {
                        selDyhByFw(result[0].BDCDYH, "");
                        $("#bdcdyhs").val("");
                        $("#bdcdyh").val(result[0].BDCDYH);
                    }  else if (result[0] != null && result[0] != "" && result[0] != "undefined") {
                        selDyhByFw(result[0], "");
                        $("#bdcdyhs").val("");
                        $("#dyh_search_qlr").next().hide();
                        $("#dyh_search_qlr").val(result[0]);
                        $("#bdcdyh").val(result[0].BDCDYH);
                    } else {
                        $("#dyh_search_qlr").next().hide();
                        $("#dyh_search_qlr").val(qlrmc);
                        //无匹配数据 不刷新
                        $("#file").addClass("active");
                        var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                        tableReload("dyh-grid-table", dyhUrl, {
                            hhSearch: qlrmc,
                            bdcdyh: '',
                            bdclx: $bdclx
                        }, dyhColModel, dyhLoadComplete);
                    }
                }
            },
            error: function (data) {
            }
        });

        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getGdFcDjlxToSqlxWdid?djlx=" + djlx,
            dataType: "json",
            success: function (result) {
                //获取过渡房产数据对应的不动产登记类型
                if (result != null && result != "" && result.busiName != "") {
                    $("#djlxSelect option").each(function () {
                        if ($(this).text() == result.busiName) {
                            $(this).attr('selected', 'selected');
                        }
                        $("#djlxSelect").trigger("chosen:updated");
                    });
                    getSqlxByDjlxAndBdclx(result.busiName, result.wfid);
                } else {
                    var busiName = "首次登记";
                    $("#djlxSelect option").each(function () {
                        if ($(this).text() == busiName) {
                            $(this).attr('selected', 'selected');
                        }
                        $("#djlxSelect").trigger("chosen:updated");
                    });

                    getSqlxByDjlxAndBdclx(busiName, "");
                }
                var wfids = "${wfids!}";
                if ("${matchTdzh!}" == "true" && result != null && (result.wfid == null || result.wfid == "" || wfids != null && wfids != "" && wfids.indexOf(result.wfid) < 0)) {
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/bdcJgSjgl/queryTdByQlid?qlid=" + qlid,
                        dataType: "json",
                        success: function (result) {
                            if (result == '' || result == null) {
                                $("#fwTd_search_qlr").next().hide();
                                $("#fwTd_search_qlr").val(qlrmc);
                                $("#tdzh").val('');

                                $("#tdid").val('');
                                var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
                                tableReload("fwTd-grid-table", tdUrl, {
                                    hhSearch: qlrmc,
                                    tdzh: ''
                                }, '', fwtdLoadComplete);
                            } else {
                                //清空查询内容
                                $("#fwTd_search_qlr").val("");
                                $("#fwTd_search_qlr").next().show();
                                if (fwtdids != null && fwtdids != "" && fwtdids.length > 1) {
                                    selFwTdByFw("", fwtdids,"");
                                    $("#tdids").val(fwtdids);
                                } else if (fwtdids != null && fwtdids != "" && fwtdids.length == 1) {
                                    selFwTdByFw(fwtdids.join(""), "","");
                                    $("#tdids").val("");
                                    $("#tdid").val(fwtdids.join(""));
                                } else if (result[0].tdid != null && result[0].tdid != "" && result[0].tdid != "undefined") {
                                    selFwTdByFw(result[0].tdid, "","");
                                    $("#tdids").val("");
                                    $("#tdid").val(result[0].tdid);
                                } else if (result[0].TDID != null && result[0].TDID != "" && result[0].TDID != "undefined") {
                                    selFwTdByFw(result[0].TDID, "","");
                                    $("#tdids").val("");
                                    $("#tdid").val(result[0].TDID);
                                } else if (result[0].tdqlid != null && result[0].tdqlid != "" && result[0].tdid != "undefined") {
                                    selFwTdByFw("", "",result[0].tdqlid);
                                    $("#tdids").val("");
                                    $("#tdid").val(result[0].tdqlid);
                                }else {
                                    $("#fwTd_search_qlr").next().hide();
                                    $("#fwTd_search_qlr").val(qlrmc);
                                    var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson";
                                    tableReload("fwTd-grid-table", tdUrl, {
                                        hhSearch: qlrmc,
                                        tdzh: ''
                                    }, '', fwtdLoadComplete);
                                }
                            }

                        },
                        error: function (data) {
                        }
                    });
                    $("#fwTdTab").show();

                }
            },
            error: function (data) {
            }
        });
    }
    var checkXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, djId, lqid, cqid) {
//        if ($("#fw").hasClass("active") && ppzt == 3) {
//            tipInfo("该证书正在发证，不能再次发证！");
//            return false;
//        } else if ($("#fw").hasClass("active") && ppzt == 4) {
//            tipInfo("该证书已经发证，不能再次发证！");
//            return false;
//        }
        var grid_selector;
        var recordId;
        if ($("#fw").hasClass("active")) {
            gdid = fwid;
            recordId = qlid;
            grid_selector = "#fw-grid-table";
        } else if ($("#td").hasClass("active")) {
            gdid = tdid;
            recordId = tdid;
            grid_selector = "#td-grid-table";
        } else if ($("#lq").hasClass("active")) {
            gdid = lqid;
            recordId = lqid;
            grid_selector = "#lq-grid-table";
        } else if ($("#cq").hasClass("active")) {
            gdid = cqid;
            recordId = cqid;
            grid_selector = "#cq-grid-table";
        }
        if ($bdclx == "TD")
            tdid = '';
        //遮罩
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: '${bdcdjUrl}/bdcJgSjgl/isCancel?bdclx=' + $bdclx,
            type: 'post',
            dataType: 'json',
            data: $("#form").serialize(),
            success: function (data) {
                if (data.hasOwnProperty("result")) {
                    if (data.result) {
                        createXm(gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId);
                    } else if (!data.result && data.msg != null && data.msg != "") {
                        setTimeout($.unblockUI, 10);
                        if (data.checkModel == "ALERT")
                            alert(data.msg);
                        else if (data.checkModel == "CONFIRM") {
                            // 未改动前
                            //  showConfirmDialog("提示信息", data.msg, "createXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + dyid + "','" + ygid + "','" + cfid + "','" + yyid + "','" + ppzt + "'", "", ",'" + gdid + "','" + grid_selector + "','" + djId + "'", "", "");
                            //  fushilu 改动后
                            showConfirmDialog("提示信息", data.msg, "createXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + gdid + "','" + grid_selector + "','" + djId + "'", "", "");
                        }

                    } else {
                        setTimeout($.unblockUI, 10);
                        alert("创建项目失败！")
                    }
                } else {
                    setTimeout($.unblockUI, 10);
                    var str = "";
                    $.each(data.resultList, function (index, obj) {
                        str += obj + "\n";
                    })
                    window.open("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=" + $("#fwid").val());
                }

            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                alert("创建项目失败！")
            }
        });
    }
    var dyhPic = function (gdproid, bdcdyh, tdzh, fwid, tdid, ppzt, djId, qlid, lqid, cqid) {
        //清空
        $("#gdproids").val("");
        $("#qlids").val("");
        $mulData.splice(0,$mulData.length);
        $mulRowid.splice(0,$mulRowid.length);
        $selectedInput.splice(0,$selectedInput.length);
        $selectArray.splice(0,$selectArray.length);
        $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
        $("#fw-grid-table").trigger("reloadGrid");
//        $("#fw-grid-table").trigger("reloadGrid");
//        if (ppzt == 3) {
//            tipInfo("该证书已进行过匹配、并且正在发证，不能重新匹配！");
//            return false;
//        } else if (ppzt == 4) {
//            tipInfo("该证书已进行过匹配、并且已经发证，不能重新匹配！");
//            return false;
//        }
        var grid_selector;
        var recordId;
        if ($("#fw").hasClass("active")) {
            gdid = fwid;
            recordId = qlid;
            grid_selector = "#fw-grid-table";
        } else if ($("#td").hasClass("active")) {
            gdid = tdid;
            recordId = qlid;
            grid_selector = "#td-grid-table";
        } else if ($("#lq").hasClass("active")) {
            gdid = lqid;
            recordId = lqid;
            grid_selector = "#lq-grid-table";
        } else if ($("#cq").hasClass("active")) {
            gdid = cqid;
            recordId = cqid;
            grid_selector = "#cq-grid-table";
        }

        if ($bdclx == "TD")
            tdid = '';
        var djlx = $("#djlxSelect  option:selected").val();
        $.ajax({
            url: '${bdcdjUrl!}/bdcJgSjgl/matchData',
            type: 'POST',
            dataType: 'json',
            data: {gdid: gdid, bdcdyh: bdcdyh,tdqlid:tdid,djId: djId,bdclx: $bdclx,qlid:recordId},
            success: function (data) {
                tipInfo(data.result);
//            if ($("#fw").hasClass("active"))
//                changePpzt("2", $(grid_selector), recordId);
//            else
                changeQtPpzt("2", $(grid_selector), recordId);
            },
            error: function (data) {
            }
        });

    }
    var createXm = function (gdproid, bdcdyh, tdzh, fwid, tdid, qlid, ppzt, gdid, grid_selector, djId) {

        if (bdcdyh === null || bdcdyh == "undefined" || bdcdyh == "") {
            ppzt = "6";
        } else {
            ppzt = "3";
        }
        var options = {
            url: '${bdcdjUrl}/bdcJgSjgl/matchData',
            type: 'post',
            dataType: 'json',
            data: {
                gdid: gdid,
                bdcdyh: bdcdyh,
                fwid: fwid,
                bdclx: $bdclx,
                tdid: tdid,
                ppzt: ppzt,
                qlid: qlid,
                gdproid: gdproid,
                djId: djId
            },
            success: function (matchData) {
                //                                        refreshStore();
//            if ($("#fw").hasClass("active"))
//                changePpzt("3", $(grid_selector), gdid);
//            else
                changeQtPpzt(ppzt, $(grid_selector), qlid);
                $.ajax({
                    url: '${bdcdjUrl}/bdcJgSjgl/createCsdj?bdclx=' + $bdclx,
                    type: 'post',
                    dataType: 'json',
                    data: $("#form").serialize(),
                    success: function (data) {
                        if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                            setTimeout($.unblockUI, 10);
                            openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid, '_task');
                        } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                            setTimeout($.unblockUI, 10);
                            alert("创建项目成功!");

                        } else {
                            setTimeout($.unblockUI, 10);
                            alert(data.msg);
                        }
                        //清空列表
                        $("#gdproids").val("");
                        $("#qlids").val("");
                        $mulData.splice(0,$mulData.length);
                        $mulRowid.splice(0,$mulRowid.length);
                        $selectedInput.splice(0,$selectedInput.length);
                        $selectArray.splice(0,$selectArray.length);
                        $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
                        $("#fw-grid-table").trigger("reloadGrid");

                    },
                    error: function (data) {
                        $.ajax({
                            url: '${bdcdjUrl}/bdcJgSjgl/matchData',
                            type: 'post',
                            dataType: 'json',
                            data: {
                                gdid: gdid,
                                bdcdyh: bdcdyh,
                                fwid: fwid,
                                bdclx: $bdclx,
                                tdid: tdid,
                                ppzt: '2',
                                qlid: qlid,
                                gdproid: gdproid
                            },
                            success: function (data) {
//                            if ($("#fw").hasClass("active"))
//                                changePpzt("2", $(grid_selector), gdid);
//                            else
                                changeQtPpzt("2", $(grid_selector), qlid);
                                setTimeout($.unblockUI, 10);
                                alert("创建项目失败！");
                            },
                            error: function (data) {
                                setTimeout($.unblockUI, 10);
                            }
                        });
                    }
                });
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);

    }
    var createMulXm = function (gdproids,qlids) {
//        if ($("#fw").hasClass("active") && ppzt == 3) {
//            tipInfo("该证书正在发证，不能再次发证！");
//            return false;
//        } else if ($("#fw").hasClass("active") && ppzt == 4) {
//            tipInfo("该证书已经发证，不能再次发证！");
//            return false;
//        }
        var djlx = $("#djlx").val();
        if ($("#fw").hasClass("active")) {
            for (var i = 0; i < $mulData.length; i++) {
                if(djlx!="800"){
                    if ($mulData[i].PPZT.indexOf("已部分匹配未发证")>-1 || $mulData[i].PPZT == 0 || $mulData[i].PPZT == '') {
                        tipInfo("所选证书有未匹配，请先匹配！");
                        return false;
                        break;
                    }
                    if ($mulData[i].PPZT.indexOf("已匹配正在发证")>-1) {
                        tipInfo("所选证书正在发证，不能再次发证！");
                        return false;
                        break;
                    }
                    if ($mulData[i].PPZT.indexOf("已匹配已发证")>-1) {
                        tipInfo("所选证书已经发证，不能再次发证！");
                        return false;
                        break;
                    }
                }
                if ($mulData[i].PPZT.indexOf("已受理")>-1) {
                    tipInfo("所选证书已经受理，不能再次受理！");
                    return false;
                    break;
                }
            }
        }
        $.blockUI({message: "请稍等……"});
        var options = {
            url: '${bdcdjUrl}/bdcSjgl/updateGdPpzt',
            type: 'post',
            dataType: 'json',
            data: {gdproids:gdproids,ppzt:3,bdclx:$bdclx},
            success: function (matchData) {
                for (var i = 0; i < $mulData.length; i++) {
                    if(djlx=800) {
                        changePpzt("6", $("#fw-grid-table"), $mulData[i].QLID);
                    }else{
                        changePpzt("3", $("#fw-grid-table"), $mulData[i].QLID);
                    }
                    //changeQtPpzt("3", $(grid_selector), gdid);
                }
                $.ajax({
                    url: '${bdcdjUrl}/bdcJgSjgl/createCsdj?lx=' + $bdclx + "&gdFwWay=cg",
                    type: 'post',
                    dataType: 'json',
                    data: $("#form").serialize(),
                    success: function (data) {
                        if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                            setTimeout($.unblockUI, 10);
                            openWin('${portalUrl!}/taskHandle?taskid=' + data.taskid, '_task');
                        } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                            setTimeout($.unblockUI, 10);
                            alert("创建项目成功!");

                        } else {
                            setTimeout($.unblockUI, 10);
                            alert(data.msg);
                        }
                        //清空列表
                        $("#gdproids").val("");
                        $("#qlids").val("");
                        $mulData.splice(0,$mulData.length);
                        $mulRowid.splice(0,$mulRowid.length);
                        $selectedInput.splice(0,$selectedInput.length);
                        $selectArray.splice(0,$selectArray.length);
                        $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
                        $("#fw-grid-table").trigger("reloadGrid");
                    },
                    error: function (data) {
                        $.ajax({
                            url: '${bdcdjUrl}/bdcSjgl/updateGdPpzt',
                            type: 'post',
                            dataType: 'json',
                            data: {gdproids:gdproids,ppzt:2,bdclx:$bdclx},
                            success: function (data) {
                                for (var i = 0; i < $mulData.length; i++) {
                                    changePpzt("2", $("#fw-grid-table"), $mulData[i].QLID);
                                    //changeQtPpzt("3", $(grid_selector), gdid);
                                }
                                setTimeout($.unblockUI, 10);
                                alert("创建项目失败！");
                                $.ajax(options);
                            },
                            error: function (data) {
                                setTimeout($.unblockUI, 10);
                            }
                        });
                    }
                });
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
            }
        };
        $.ajax(options);
    }
    var showMulGdFwPic = function (gdproid, qlid,readOnly) {

        var sqlx = $("#sqlxSelect").val();
        var url = "${bdcdjUrl!}/bdcSjgl/openMulGdFwPic?gdproid=" + gdproid + "&sqlxdm=" + sqlx + "&readOnly=" + readOnly;
        openWin(url, "房屋匹配");
    }
    //通过房产证号级联不动产单元
    function selDyhByFw(bdcdyh, bdcdyhs) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
        if (bdcdyhs != null && bdcdyhs != "")
            bdcdyhs = bdcdyhs.join(",");
        var data = {hhSearch: '', bdcdyh: bdcdyh, bdcdyhs: bdcdyhs, bdclx: $bdclx};
        var jqgrid = $("#dyh-grid-table");
        var grid_selector = "#dyh-grid-table";

        jqgrid.setGridParam({
            url: Url,
            datatype: 'json',
            page: 1,
            postData: data,
            colModel: [
                {
                    name: 'XL',
                    index: 'XL',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        index++;
                        if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.indexOf(rowObject.BDCDYH) > -1) {
                            $("#bdcdyh").val("");
                            $("#djId").val("");
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" checked="true"  value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                        } else if (index == 1) {
                            $("#bdcdyh").val(rowObject.BDCDYH);
                            $("#djId").val(rowObject.ID);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" checked="true" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="dyhXl" value="' + rowObject.BDCDYH + '" djId="' + rowObject.ID + '"/>';
                        }
                    }
                },
                {name: 'YDJH', index: 'YDJH', width: '15%', sortable: false},
                {name: 'TDZL', index: 'TDZL', width: '25%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '15%', sortable: false},
                {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
                {
                    name: 'BDCDYH',
                    index: 'BDCDYH',
                    width: '22%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (!cellvalue) {
                            return "";
                        }
                        var value = cellvalue.substr(19);
                        return value;
                    }
                },
                {name: 'BDCLX', index: 'BDCLX', width: '0%', sortable: false, hidden: true},
                {name: 'ID', index: 'ID', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);

                    addYdjhForTable(grid_selector);

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
        if (djIds != null && djIds != "") {
            $("#djIds").val();
        }

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

    //通过不动产单元关联房产证号
    function selFczhByBdcdyh(dah) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcJgSjgl/getGdXmFwJsonByPage";
        var data = {dah: dah};
        var jqgrid = $("#fw-grid-table");
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
                            $("#xmmc").val(rowObject.RF1DWMC);
                            $("#fwid").val(rowObject.FWID);
                            $("#dah").val(rowObject.DAH);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" checked="true" onclick="fwSel(\'' + rowObject.FCZH + '\',\'' + rowObject.FWID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.FWID + '\',\'' + rowObject.DYID + '\',\'' + rowObject.YGID + '\',\'' + rowObject.CFID + '\',\'' + rowObject.YYID + '\',\'' + rowObject.BDCID + '\')"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="fwXl" onclick="fwSel(\'' + rowObject.FCZH + '\',\'' + rowObject.FWID + '\',\'' + rowObject.FWZL + '\',\'' + rowObject.FWID + '\',\'' + rowObject.DYID + '\',\'' + rowObject.YGID + '\',\'' + rowObject.CFID + '\',\'' + rowObject.YYID + '\',\'' + rowObject.BDCID + '\')"/>';
                        }
                    }
                },
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '15%', sortable: false},
                {name: 'FCZH', index: 'FCZH', width: '20%', sortable: false},
                {name: 'FWZL', index: 'FWZL', width: '55%', sortable: false}
            ]
        });
        jqgrid.trigger("reloadGrid");//重新加载JqGrid

    }

    //通过不动产单元获取项目id，定位
    function bdzDyMap(bdcdyh) {
        if (bdcdyh != '' && bdcdyh != undefined && bdcdyh != "undefined") {
            $("#iframeSrcUrl").val("${bdcdjUrl}/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
            if ($("#ace-settings-box").hasClass("open")) {
                $("#iframe").attr("src", "${bdcdjUrl}/bdzDyMap?bdcdyh=" + bdcdyh + "&bdclxdm=" + $bdclx);
            }
        }
    }

    function tableReload(table, Url, data, colModel, loadComplete) {
        var index = 0;
        var jqgrid = $("#" + table);
        if (colModel == '' && loadComplete == '') {
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
        } else if (loadComplete == '' && colModel != '') {
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
        } else if (loadComplete != '' && colModel != '') {
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel, loadComplete:loadComplete});
        } else if (loadComplete != '' && colModel == '') {
            jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, loadComplete:loadComplete});
        }

        jqgrid.trigger("reloadGrid");//重新加载JqGrid
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
            url: "${bdcdjUrl}/bdcJgSjgl/getGdFwJson?filterFwPpzt=${filterFwPpzt!}",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'QLID'},
            colNames: ['序列', '权利人', '证书类型', "房产证号", '坐落', '匹配状态', '权利状态', 'QLZT', 'PROID', 'QLID'],
            colModel: fwColModel,
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: true,
            loadComplete: function () {
                //zwq防止有勾选的情况下,搜索勾选另一个，从而读取2个房产证号
                //               $selectArray.length = 0;
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
                for(var i=0;i<=$mulRowid.length;i++){
                    $(grid_selector).jqGrid('setSelection',$mulRowid[i]);
                }
                //赋值数量
                $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
                $.each(jqData, function (index, data) {
                    asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
                    //    getQtPpzt(data.PPZT, $(grid_selector), data.PROID);
                })
            },
            onSelectAll: function(aRowids,status){
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
                })
                //赋值数量

                $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            onSelectRow:function(rowid,status){
                var $myGrid = $(this);
                var cm = $myGrid.jqGrid('getRowData',rowid);
                //判断是已选择界面还是原界面s
//                var index=$.inArray(rowid,$mulRowid);
//                if(status && index<0){
//                    $mulData.push(cm);
//                    $mulRowid.push(rowid);
//                    $selectedInput.push(rowid);
//                }else if(!status && index>=0){
//                    $mulData.remove(index);
//                    $mulRowid.remove(index);
//                    $selectedInput.remove(rowid);
//                }
                if (status) {
                    fwSel(cm.PROID, cm.FWZL, cm.PPZT, cm.DJLX, cm.QLID, cm.QLZT,cm.FCZH)
                }
                //赋值数量
                $("#gdfwMulXx").html("<span>已选择("+$mulRowid.length+")</span>");
            },
            gridComplete:function(){
                $.each($mulData, function (index) {
                    $selectedInput.push($mulData[index].QLID);
                });
                $.each($selectedInput, function (index) {
                    $('#' + $selectedInput[index] + '').click();
                });
                $selectedInput.splice(0, $selectedInput.length);
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

    //为表格添加地籍号列数据
    function addDjhForTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            getDjhByYdjh(data.DJH, $(grid_selector), rowIds[index]);
        })
    }

    //为表格添加原地籍号列数据
    function addYdjhForTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            getYdjhBydjh(data.DJH, $(grid_selector), rowIds[index]);
        })
    }

    //为表格添加权利人列数据
    function qlrForTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            getQlrByDjid(data.ID, $(grid_selector), rowIds[index]);
        })
    }
    function oldDjhForTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            getOldDjhByDjh(data.DJH, $(grid_selector), rowIds[index]);
        })
    }
    function qlrForFwTable(grid_selector) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
//            getQtPpzt(data.PPZT, $(grid_selector), data.QLID);
            asyncGetGdFwxx(data.PROID, $(grid_selector), data.PROID, data.QLZT, data.QLID);
        })
    }


    //获取原地籍号
    function getYdjhBydjh(djh, table, rowid) {
        if (djh == null || djh == "undefined")
            djh = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getYdjhBydjh?djh=" + djh,
            success: function (result) {
                var ydjh = result.ydjh;
                if (ydjh == null || ydjh == "undefined")
                    ydjh = "";
                var cellVal = "";
                cellVal += '<span>' + ydjh + '</span>';

                table.setCell(rowid, "YDJH", cellVal);
            }
        });
    }
    //获取老地籍号
    function getOldDjhByDjh(djh, table, rowid) {
        if (djh == null || djh == "undefined")
            djh = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getOldDjhByDjh?djh=" + djh,
            success: function (result) {
                var oldDjh = result.oldDjh;
                if (oldDjh == null || oldDjh == "undefined")
                    oldDjh = "";
                var cellVal = "";
                cellVal += '<span>' + oldDjh + '</span>';
                table.setCell(rowid, "YDJH", cellVal);
            }
        });
    }
    //获取权利人
    function getQlrByDjid(djid, table, rowid) {
        if (djid == null || djid == "undefined")
            djid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + $bdclx,
            success: function (result) {
                var qlr = result.qlr;
                if (qlr == null || qlr == "undefined")
                    qlr = "";
                var cellVal = "";
                cellVal += '<span>' + qlr + '</span>';
                table.setCell(rowid, "QLR", cellVal);
            }
        });
    }

    //获取过渡房屋权利人
    function getfwQlrByQlid(djid, table, rowid) {
        if (djid == null || djid == "undefined")
            djid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getFwQlrByQlid?djid=" + djid,
            success: function (result) {
                var qlr = result.qlr;
                if (qlr == null || qlr == "undefined")
                    qlr = "";
                var cellVal = "";
                cellVal += '<span>' + qlr + '</span>';
                table.setCell(rowid, "RF1DWMC", cellVal);
            }
        });
    }

    //获取过渡房屋坐落
    function getfwZlByQlid(djid, table, rowid) {
        if (djid == null || djid == "undefined")
            djid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getFwZlByQlid?djid=" + djid,
            success: function (result) {
                var fwzl = result.fwzl;
                if (fwzl == null || fwzl == "undefined")
                    fwzl = "";
                var cellVal = "";
                cellVal += '<span>' + fwzl + '</span>';
                table.setCell(rowid, "FWZL", cellVal);
            }
        });
    }

    //获取地籍号
    function getDjhByYdjh(ydjh, table, rowid) {
        if (ydjh == null || ydjh == "undefined")
            ydjh = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/getDjhByYdjh?ydjh=" + ydjh,
            dataType: "json",
            success: function (result) {
                var djh = result.djh;
                if (djh == null || djh == "undefined")
                    djh = "";
                var cellVal = "";
                cellVal += '<span>' + djh + '</span>';
                table.setCell(rowid, "XDJH", cellVal);
            }
        });
    }

    //获取 抵押 查封 预告 状态
    function getDyYgCfStatus(table, tdid) {
        if (tdid == null || tdid == 'undefined') {
            tdid = "";
        }
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdQlztByBdcid?tdid=" + tdid + "&bdclx=" + $bdclx,
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
//                if (result.zls != null && result.zls != "" && result.qlrs != "null")
//                    table.setCell(tdid, "ZL", result.zls);
//                if (result.xdjhs != null && result.xdjhs != "" && result.xdjhs != "null")
//                    table.setCell(tdid, "DJH", result.xdjhs);
                table.setCell(tdid, "STATUS", cellVal);
            }
        });
    }

    //zx 获取房产证抵押 查封 预告 异议 状态
    function asyncGetGdFwxx(bdcid, table, rowid, qlzt, qlid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdFwxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                //正常
                var zls = result.zls;
                if (result.qlzts == null || result.qlzts == "") {
                    cellVal += '<span class="label label-success">正常</span>';
                    $.ajax({
                        type: "GET",
                        url: "${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson?bdclx=" + $bdclx + "&zl=" + zls,
                        dataType: "json",
                        async: false,
                        success: function (result) {
                            if (result.rows != '') {
                                $.ajax({
                                    type: "GET",
                                    url: "${bdcdjUrl}/queryBdcdy/getBdcdyhQlxx?bdcdyh=" + result.rows[0].BDCDYH,
                                    dataType: "json",
                                    async: false,
                                    success: function (result) {
                                        if (!result.dy) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                                        }
                                        if (!result.cf) {
                                            cellVal = '';
                                            cellVal += '<span class="label label-danger">查封</span><span> </span>';
                                        }


                                    }
                                });
                            }
                        }
                    });
                } else {
                    var qlzts = result.qlzts.split(",");
                    for (var i = 0; i < qlzts.length; i++) {
                        //zhouwanqing 防止其他权利与注销同在
                        if (qlzts[i] == "ZX") {
                            cellVal = '<span class="label label-danger">注销</span><span> </span>';
                            break;
                        } else if (qlzts[i] == "DY") {
                            cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                        } else if (qlzts[i] == "CF") {
                            cellVal += '<span class="label label-warning">查封</span><span> </span>';
                        } else if (qlzts[i] == "YG") {
                            cellVal += '<span class="label label-info">预告</span><span> </span>';
                        } else if (qlzts[i] == "YY") {
                            cellVal += '<span class="label label-info">异议</span><span> </span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }
                table.setCell(qlid, "STATUS", cellVal);
//                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
//                    table.setCell(qlid, "RF1DWMC", result.qlrs);
//                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
//                    table.setCell(qlid, "FCZH", result.cqzhs);
//                if (result.zls != null && result.zls != "" && result.zls != "null")
//                    table.setCell(qlid, "FWZL", result.zls);

                getQtPpzt(result.ppzt, table, qlid);
            }
        });
    }

    //zx 获取土地证抵押 查封 预告 异议 状态
    function asyncGetGdTdxx(table, qlid, proid) {
        $.ajax({
            type: "POST",
            url: "${bdcdjUrl}/bdcJgSjgl/asyncGetGdTdxxByQlid?bdclx=" + $bdclx + "&qlid=" + qlid+"&proid="+proid,
            dataType: "json",
            success: function (result) {
                var cellVal = "";
                //正常
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
                            cellVal += '<span class="label label-danger">地役</span>';
                        } else if (qlzts[i] == "DGQLZT") {
                            cellVal += '<span class="label label-info">多个权利状态</span>';
                        }
                    }
                }

                table.setCell(qlid, "STATUS", cellVal);
//                if (result.qlrs != null && result.qlrs != "" && result.qlrs != "null")
//                    table.setCell(qlid, "RF1DWMC", result.qlrs);
//                if (result.cqzhs != null && result.cqzhs != "" && result.cqzhs != "null")
//                    table.setCell(qlid, "TDZH", result.cqzhs);
//                if (result.zls != null && result.zls != "" && result.zls != "null")
//                    table.setCell(qlid, "ZL", result.zls);
//                if (result.djhs != null && result.djhs != "" && result.djhs != "null")
//                    table.setCell(qlid, "LDJH", result.djhs);
//                if (result.xdjhs != null && result.xdjhs != "" && result.xdjhs != "null")
//                    table.setCell(qlid, "DJH", result.xdjhs);

                getQtPpzt(result.ppzt, table, qlid);
            }
        });
    }


    //获取匹配状态
    function getPpzt(ppzt, table, rowid) {
        if (ppzt == "1")
            ppzt = '<span class="label label-warning" value=' + ppzt + '>部分匹配</span>';
        else if (ppzt == "4" || ppzt == "3" || ppzt == "2")
            ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
        else
            ppzt = '<span class="label label-danger" value=' + ppzt + '>待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }

    //获取匹配状态
    function getQtPpzt(ppzt, table, rowid) {
        if (ppzt == "2")
            ppzt = '<span class="label label-success" value="2">已匹配</span>';
        else if (ppzt == "1")
            ppzt = '<span class="label label-warning" value="1">部分匹配</span>';
        else if(ppzt == "6")
            ppzt = '<span class="label label-success" value="6">已受理</span>';
        else
            ppzt = '<span class="label label-danger" value="0">待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }

    //获取匹配状态实际值
    function getPpztVal(ppzt) {
        if (ppzt != null && ppzt.indexOf("已匹配") > -1)
            ppzt = '2';
        else if (ppzt != null && ppzt.indexOf("部分匹配") > -1)
            ppzt = '1';
        else if(ppzt !=null && ppzt.indexOf("已受理")>-1)
            ppzt = "6";
        else
            ppzt = '0';
        return ppzt;
    }

    //土地证初始化
    function tdTableInit() {
        var grid_selector = "#td-grid-table";
        var pager_selector = "#td-grid-pager";

        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        var gridRowNum = $rownum;
        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/bdcJgSjgl/getGdTdJson?filterFwPpzt=${filterFwPpzt!}",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'QLID'},
            colNames: ['序列', '地籍号', '坐落', "土地证号", '匹配状态', '权利状态', 'QLID', '权利人'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="tdXl"  onclick="tdSel(\'' + rowObject.PROID + '\',\'' + rowObject.QLID + '\',\'' + rowObject.TDZH + '\',\'' + rowObject.QLZT + '\')"/>'
                    }
                },
                {name: 'DJH', index: 'DJH', width: '15%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '25%', sortable: false},
                {name: 'TDZH', index: 'TDZH', width: '25%', sortable: false},
                {name: 'PPZT', index: '', width: '13%', sortable: false},
                {name: 'STATUS', index: '', width: '13%', sortable: false},
                {name: 'QLID', index: 'QLID', width: '0%', sortable: false, hidden: true},
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: gridRowNum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                //zwq 清空全局数组
                $selectArray.length = 0;
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);

//                addDjhForTable(grid_selector);

                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == gridRowNum) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    asyncGetGdTdxx($(grid_selector), data.QLID, data.PROID);
//                    getQtPpzt(data.PPZT, $(grid_selector), data.QLID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //草原表初始化
    function cqTableInit() {
        var grid_selector = "#cq-grid-table";
        var pager_selector = "#cq-grid-pager";

        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'CQID'},
            colNames: ['序列', '权利人', "草原证号", '坐落', '匹配状态', 'ID'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="cqXl" onclick="cqSel(\'' + rowObject.CQZH + '\',\'' + rowObject.CQID + '\',\'' + rowObject.ZL + '\',\'' + rowObject.PPZT + '\')"/>'
                    }
                },
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '30%', sortable: false},
                {name: 'CQZH', index: 'CQZH', width: '25%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '30%', sortable: false},
                {name: 'PPZT', index: '', width: '15%', sortable: false},
                {name: 'CQID', index: 'CQID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //清空
                $("#cqid").val("");
                $("#xmmc").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getDyYgCfStatus($(grid_selector), data.CQID);
                    getQtPpzt(data.PPZT, $(grid_selector), data.CQID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //林权表初始化
    function lqTableInit() {
        var grid_selector = "#lq-grid-table";
        var pager_selector = "#lq-grid-pager";

        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'LQID'},
            colNames: ['序列', '权利人', "林权证号", '坐落', '匹配状态', 'ID'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="radio" name="lqXl" onclick="lqSel(\'' + rowObject.LQZH + '\',\'' + rowObject.LQID + '\',\'' + rowObject.LQZL + '\',\'' + rowObject.PPZT + '\')"/>'
                    }
                },
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '30%', sortable: false},
                {name: 'LQZH', index: 'LQZH', width: '25%', sortable: false},
                {name: 'LQZL', index: 'LQZL', width: '30%', sortable: false},
                {name: 'PPZT', index: '', width: '15%', sortable: false},
                {name: 'LQID', index: 'LQID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: $rownum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
                //清空
                $("#lqid").val("");
                $("#xmmc").val("");
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                    $(grid_selector).jqGrid("setGridHeight", "100%");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", $pageHight);
                }
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getDyYgCfStatus($(grid_selector), data.LQID);
                    getQtPpzt(data.PPZT, $(grid_selector), data.LQID);
                })
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    function dyhTableInit() {
        var grid_selector = "#dyh-grid-table";
        var pager_selector = "#dyh-grid-pager";

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
            colNames: ['序列', '原地籍号', '坐落', '权利人', '地籍号', "不动产单元号", '不动产类型', 'ID'],
            colModel: dyhColModel,
            viewrecords: true,
            rowNum: $rownum,
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

    //根据key获取是否加载数据

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
        jQuery(grid_selector).jqGrid({
            url: "",
            datatype: "json",
            height: $pageHight,
            jsonReader: {id: 'TDID'},
            colNames: ['序列', '土地证号','地籍号', "坐落", '权利人','状态', 'ID'],
            colModel: [
                {
                    name: 'XL',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"/>';
                    }
                },
                {name: 'TDZH', index: 'TDZH', width: '30%', sortable: false},
                {name: 'DJH', index: 'DJH', width: '22%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '38%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '20%', sortable: false},
                {name: 'STATUS', index: '', width: '15%', sortable: false},
                {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: gridRowNum,
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
            loadComplete: function () {
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
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getDyYgCfStatus($(grid_selector), data.TDID)
                })

                $(table).jqGrid("setGridWidth", parent_column.width());
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //通过房产证号级联不动产单元
    function selFwTdByFw(tdid, tdids,qlid) {
        var index = 0;
        var Url = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
        if (tdids != null && tdids != "")
            tdids = tdids.join(",");
        var data = {hhSearch: '', tdid: tdid, tdids: tdids,qlid:qlid,fwtdz: 'true'};
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
                        if (tdids != null && tdids != "" && tdids.indexOf(rowObject.TDID) > -1) {
                            $("#tdid").val("");
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                        } else if (index == 1) {
                            $("#tdid").val(rowObject.TDID);
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" checked="true" value="' + rowObject.TDID + '"/>';
                        } else {
                            return '<span style="font-family: cursive;"> ' + rowObject.ROWNUM_ + '. </span><input type="checkbox" name="fwtdXl" value="' + rowObject.TDID + '"/>';
                        }
                    }
                },
                {name: 'TDZH', index: 'TDZH', width: '30%', sortable: false},
                {name: 'DJH', index: 'DJH', width: '22%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '38%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '20%', sortable: false},
                {name: 'STATUS', index: '', width: '15%', sortable: false},
                {name: 'TDID', index: 'TDID', width: '0%', sortable: false, hidden: true}
            ],
            loadComplete: function () {
                var table = this;
                var ybdcqzh = $("#ybdcqzh").val();
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
                    getDyYgCfStatus($(jqgrid), data.TDID);
                    ybdcqzh = ybdcqzh + "," + data.TDZH + ";";
                });
                $("#ybdcqzh").val(ybdcqzh);
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }

    function fwTdSel(tdzh, tdid) {
        $("#tdzh").val(tdzh);
        $("#tdid").val(tdid);
    }

    //open新窗口
    function addOrUpdate(url) {
        openWin(url);
    }

    function refreshStore() {
        if ($bdclx == "TDFW") {
            $("#fw_search").click();
            $("#fwTd_search").click();
        } else if ($bdclx == "TD")
            $("#td_search").click();
        else if ($bdclx == "TDQT")
            $("#cq_search").click();
        else if ($bdclx == "TDSL")
            $("#lq_search").click();

    }

    //改变匹配状态
    function changePpzt(ppzt, table, rowid) {
        $("#ppzt").val(ppzt);
        if (ppzt == "1")
            ppzt = '<span class="label label-warning">已部分匹配未发证</span>';
        else if (ppzt == "4")
            ppzt = '<span class="label label-info">已匹配已发证</span>';
        else if (ppzt == "3")
            ppzt = '<span class="label label-warning">已匹配正在发证</span>';
        else if (ppzt == "2")
            ppzt = '<span class="label label-success">已匹配未发证</span>';
        else if (ppzt == "6")
            ppzt = '<span class="label label-success">已受理</span>';
        else
            ppzt = '<span class="label label-danger">待匹配未发证</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }

    //改变匹配状态
    function changeQtPpzt(ppzt, table, rowid) {
        $("#ppzt").val(ppzt);
        if (ppzt == "1" || ppzt == "2" || ppzt == "3" || ppzt == "4")
            ppzt = '<span class="label label-success" value=' + ppzt + '>已匹配</span>';
        else if (ppzt == "6")
            ppzt = '<span class="label label-success" value=' + ppzt + '>已受理</span>';
        else
            ppzt = '<span class="label label-danger" value=' + ppzt + '>待匹配</span>';
        table.setCell(rowid, "PPZT", ppzt);
    }

    function hideFwtdGrid() {
        $("#fwTdTab").hide();
        dyhTableInit();
        $("#dyhTab").click();
    }

    function showFwtdGrid() {
        $("#fwTdTab").show();
        dyhTableInit();
        fwTdTableInit();
        $("#dyhTab").click();
    }

    function changeSqlx() {
        var sqlx = $("#sqlxSelect").val();
        var wfids = "${wfids!}";
        if (wfids != null && wfids != "" && wfids.indexOf(sqlx) > -1) {
            hideFwtdGrid();
            $("#tdid").val();
        } else if ($("#fw").hasClass("active")) {
            showFwtdGrid();
        }

    }

    function clearTabDiv() {
        $("#dyh_search_qlr").val('');
        $("#fwTd_search_qlr").val('');
        var dyhgrid_selector = "#dyh-grid-table";
        var dyhparent_column = $(dyhgrid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(dyhgrid_selector).jqGrid('setGridWidth', dyhparent_column.width());
            }
        });
        var fwTdgrid_selector = "#fwTd-grid-table";
        var fwTdparent_column = $(fwTdgrid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(fwTdgrid_selector).jqGrid('setGridWidth', fwTdparent_column.width());
            }
        });
//    $("#dyh-grid-table").jqGrid("setGridWidth",800);
//    $("#fwTd-grid-table").jqGrid("setGridWidth",800);
        jQuery("#dyh-grid-table").jqGrid("clearGridData");
        jQuery("#fwTd-grid-table").jqGrid("clearGridData");

    }

    function disableDyhTabDiv(id) {
        $("#dyh_search").unbind('click');
        $("#dyhTab").unbind('click');
        $("#dyh_search_qlr").attr("disabled", true);
        $("#fwTd_search").unbind('click');
        $("#fwTdTab").unbind('click');
        $("#fwTd_search_qlr").attr("disabled", true);
    }

    function visableDyhTabDiv(id) {


        $("#dyh_search").click(function () {
            resetBdcdyhs();
            var hhSearch = $("#dyh_search_qlr").val();
            var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
            tableReload("dyh-grid-table", dyhUrl, {
                hhSearch: hhSearch,
                bdcdyh: '',
                bdcdyhs: '',
                bdclx: $bdclx
            }, dyhColModel, '');
        });
        $("#fwTd_search").click(function () {
            resetFwtdids();
            var hhSearch = $("#fwTd_search_qlr").val();
            var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson?fwtdz=true";
            $bdclx = "TDFW";
            tableReload("fwTd-grid-table", tdUrl, {hhSearch: hhSearch, tdzh: '', tdid: '', tdids: ''}, '', '');
        });

        $("#dyh_search_qlr").attr("disabled", false);
        $("#fwTd_search_qlr").attr("disabled", false);
    }

    function getBdcdyhs() {
        var chk_value = [];
        $('input[name="dyhXl"]:checked').each(function () {
            chk_value.push($(this).val());
        });
        return chk_value;
    }

    function resetBdcdyhs() {
        $("input[name='dyhXl']").attr("checked", false);
        $("#bdcdyhs").val('');
    }

    function getFwtdids() {
        var chk_value = [];
        $('input[name="fwtdXl"]:checked').each(function () {
            chk_value.push($(this).val());
        });
        return chk_value;
    }

    function resetFwtdids() {
        $("input[name='fwtdXl']").attr("checked", false);
        $("#tdids").val('');
    }

    function getBdcdyDjIds() {
        var chk_value = [];
        $('input[name="dyhXl"]:checked').each(function () {
            chk_value.push($(this)[0].attributes.djid.value);
        });
        return chk_value;
    }

    //选择多个房屋后关闭窗口更新房屋和单元号选择
    var updateGdFwAndDyhSel = function (djlx) {
        var gdproid = $("#gdproid").val();
        var qlid = $("#qlid").val();
        var qlrmc = $("#xmmc").val();
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcSjgl/getGdfwPpzt?gdproid=" + gdproid,
            dataType: "json",
            success: function (result) {
                getPpzt(result, $("#fw-grid-table"), qlid);
            },
            error: function (data) {
            }
        });
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByQlid?qlid=" + qlid,
            dataType: "json",
            success: function (result) {
                if (result == '' || result == null) {
                    $("#dyh_search_qlr").next().hide();
                    $("#dyh_search_qlr").val(qlrmc);

                    var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                    tableReload("dyh-grid-table", dyhUrl, {
                        hhSearch: qlrmc,
                        bdcdyh: '',
                        bdclx: $bdclx
                    }, dyhColModel, dyhLoadComplete);
                } else {
                    //清空查询内容
                    $("#dyh_search_qlr").val("");
                    $("#dyh_search_qlr").next().show();
                    var bdcdyhs = [];
                    $.each(result, function (index, data) {
                        if (data.bdcdyh != null && data.bdcdyh != "" && data.bdcdyh != "undefined")
                            bdcdyhs.push(data.bdcdyh);
                    });
                    if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length > 1) {
                        selDyhByFw("", bdcdyhs);
                        $("#bdcdyhs").val(bdcdyhs);
                        $("#bdcdyh").val("");
                        $("#djId").val("");
                    } else if (bdcdyhs != null && bdcdyhs != "" && bdcdyhs.length == 1) {
                        selDyhByFw(bdcdyhs.join(""), "");
                        $("#bdcdyhs").val("");
                        $("#bdcdyh").val(bdcdyhs.join(""));
                        $("#djIds").val("");
                    } else {
                        $("#dyh_search_qlr").next().hide();
                        $("#dyh_search_qlr").val(qlrmc);
                        //无匹配数据 不刷新
                        $("#file").addClass("active");
                        $("#bdcdyh").val("");
                        $("#djId").val("");
                        var dyhUrl = "${bdcdjUrl}/bdcJgSjgl/getBdcDyhPagesJson";
                        tableReload("dyh-grid-table", dyhUrl, {
                            hhSearch: qlrmc,
                            bdcdyh: '',
                            bdclx: $bdclx
                        }, dyhColModel, dyhLoadComplete);
                    }

                }
            },
            error: function (data) {
            }
        });
        var sqlx = $("#sqlxSelect").val();
        var wfids = "${wfids!}";
        if ("${matchTdzh!}" == "true" && (sqlx == null || sqlx == "" || wfids != null && wfids != "" && wfids.indexOf(sqlx) < 0)) {
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcJgSjgl/queryBdcdyhByQlid?qlid=" + qlid,
                dataType: "json",
                success: function (result) {
                    if (result == '' || result == null) {
                        $("#fwTd_search_qlr").next().hide();
                        $("#fwTd_search_qlr").val(qlrmc);
                        $("#tdzh").val('');
                        $("#tdid").val('');
                        var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson";
                        tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                    } else {
                        //清空查询内容
                        $("#fwTd_search_qlr").val("");
                        $("#fwTd_search_qlr").next().show();
                        var fwtdis = [];
                        $.each(result, function (index, data) {
                            if (data.tdid != null && data.tdid != "" && data.tdid != "undefined")
                                fwtdis.push(data.tdid);
                        });
                        if (fwtdis != null && fwtdis != "" && fwtdis.length > 1) {
                            selFwTdByFw("", fwtdis,"");
                            $("#fwtdis").val(fwtdis);
                            $("#tdzh").val('');
                            $("#tdid").val('');
                        } else if (fwtdis != null && fwtdis != "" && fwtdis.length == 1) {
                            selFwTdByFw(fwtdis.join(""), "","");
                            $("#fwtdis").val("");
                            $("#tdid").val(fwtdis.join(""));
                        } else {
                            $("#fwTd_search_qlr").next().hide();
                            $("#fwTd_search_qlr").val(qlrmc);
                            $("#tdid").val("");
                            var tdUrl = "${bdcdjUrl}/bdcJgSjgl/getGdFwTdJson";
                            tableReload("fwTd-grid-table", tdUrl, {hhSearch: qlrmc, tdzh: ''}, '', '');
                        }

                    }
                },
                error: function (data) {
                }
            });
            $("#fwTdTab").show();
        }


    }

    function uniqueArray(data) {
        data = data || [];
        var a = {};
        for (var i = 0; i < data.length; i++) {
            var v = data[i];
            if (typeof(a[v]) == 'undefined') {
                a[v] = 1;
            }
        }
        ;
        data.length = 0;
        for (var i in a) {
            data[data.length] = i;
        }
        return data;
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
        <div class="row">
            <div class="col-xs-2" style="min-width: 230px">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 登记类型</div>

                    <div class="profile-info-value">
                        <select id="djlxSelect" class="chosen-select" style="width:100px">
                            <#list djlxList as djlx>
                                <option value="${djlx.DM!}">${djlx.MC!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-xs-3" style="min-width:370px">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 申请类型</div>

                    <div class="profile-info-value">
                        <select id="sqlxSelect" class="chosen-select" style="width:250px" onchange="changeSqlx()">
                            <#list sqlxList as sqlx>
                                <option value="${sqlx.DM!}">${sqlx.MC!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-xs-3">
                <button type="button" class="btn btn-sm btn-primary" id="save">创建</button>
                <button type="button" class="btn btn-sm btn-primary" id="ylzs" style="display: none;">预览证书</button>
                <#if "${showZjfzBtn!}"=='true'>
                    <button type="button" class="btn btn-sm btn-primary" id="fz">缮证</button>
                </#if>
                <button type="button" class="btn btn-sm btn-primary" id="match">匹配</button>
                <button type="button" class="btn btn-sm btn-primary" id="dismatch">取消匹配</button>
                <div></div>
                <button type="button" class="btn btn-sm btn-primary" id="ppxm" style="margin-top: 5px">关联项目</button>
                <button type="button" class="btn btn-sm btn-primary" id="deletexmgl" style="margin-top: 5px;">删除项目关联
                </button>
            </div>
        </div>
        <div class="space-4"></div>
        <div class="row">
            <div class="col-xs-6">
                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <#list gdTabOrderList as gdTabOrder>
                            <#if gdTabOrder=='td'>
                                <li>
                                    <a data-toggle="tab" id="tdTab" href="#td">
                                        纯土地证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='lq'>
                                <li>
                                    <a data-toggle="tab" id="lqTab" href="#lq">
                                        林权证
                                    </a>
                                </li>
                            <#elseif   gdTabOrder=='cq'>
                                <li>
                                    <a data-toggle="tab" id="cqTab" href="#cq">
                                        草原证
                                    </a>
                                </li>
                            <#else>
                                <li>
                                    <a data-toggle="tab" id="fwTab" href="#fw">
                                        房产证
                                    </a>
                                </li>

                            </#if>

                        </#list>

                    </ul>
                    <div class="tab-content">
                        <div id="fw" class="tab-pane ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                                   data-watermark="请输入权利人/坐落/房产证号">
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
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdFwAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdFwUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="addSel">
                                                <span>添加列表</span>
                                            </button>
                                        </li>
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
                                        <li>
                                            <button type="button" id="pptzs">
                                                <i class="ace-icon fa fa-search"></i>
                                                <span>匹配审核通知书</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="fw-grid-table"></table>
                            <div id="fw-grid-pager"></div>
                        </div>
                    <#--林权-->
                        <div id="lq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="lq_search_qlr"
                                                   data-watermark="请输入权利人/坐落/林权证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="lq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdLqAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdLqUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="lq-grid-table"></table>
                            <div id="lq-grid-pager"></div>
                        </div>
                    <#--草原-->
                        <div id="cq" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="cq_search_qlr"
                                                   data-watermark="请输入权利人/坐落/草原证号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="cq_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdCqAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdCqUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <table id="cq-grid-table"></table>
                            <div id="cq-grid-pager"></div>
                        </div>
                    <#--土地-->
                        <div id="td" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="td_search_qlr"
                                                   data-watermark="请输入坐落/土地证号/地籍号">
                                        </td>
                                        <td class="Search">
                                            <a href="#" id="td_search">
                                                搜索
                                                <i class="ace-icon fa fa-search bigger-130"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>
                                        <li>
                                            <button type="button" id="gdTdAdd">
                                                <i class="ace-icon fa fa-file-o"></i>
                                                <span>新增</span>
                                            </button>
                                        </li>
                                        <li>
                                            <button type="button" id="gdTdUpdate">
                                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                                <span>修改</span>
                                            </button>
                                        </li>
                                    <#--<li>
                                        <button type="button" id="gdTdZx">
                                            <i class="ace-icon fa fa-pencil-square-o"></i>
                                            <span>注销</span>
                                        </button>
                                    </li>
                                    <li>
                                        <button type="button" id="gdTdJcZx">
                                            <i class="ace-icon fa fa-pencil-square-o"></i>
                                            <span>解除注销</span>
                                        </button>
                                    </li>-->
                                    </ul>
                                </div>
                            </#if>
                            <table id="td-grid-table"></table>
                            <div id="td-grid-pager"></div>
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
                        <li>
                            <a data-toggle="tab" id="fwTdTab" href="#fwTd" style="display:none;">
                                房屋土地证
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="file" class="tab-pane in active ">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="dyh_search_qlr"
                                                   data-watermark="坐落/地籍号">
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
                            <#if "${editFlag!}"=="true">
                                <div class="tableHeader">
                                    <ul>

                                    </ul>
                                </div>
                            </#if>
                            <table id="dyh-grid-table"></table>
                            <div id="dyh-grid-pager"></div>
                        </div>

                        <div id="fwTd" class="tab-pane">
                            <div class="simpleSearch">
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>
                                            <input type="text" class="SSinput watermarkText" id="fwTd_search_qlr"
                                                   data-watermark="坐落/土地证号/地籍号/权利人">
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
                        <#--  <#if "${editFlag!}"=="true">
                              <div class="tableHeader">
                                  <ul>fwTdAdd
                                      <li>
                                          <button type="button" id="fwTdAdd">
                                              <i class="ace-icon fa fa-file-o"></i>
                                              <span>新增</span>
                                          </button>
                                      </li>
                                      <li>
                                          <button type="button" id="fwTdUpdate">
                                              <i class="ace-icon fa fa-pencil-square-o"></i>
                                              <span>修改</span>
                                          </button>
                                      </li>
                                  </ul>
                              </div>
                          </#if>-->
                            <table id="fwTd-grid-table" style="width: 800px"></table>
                            <div id="fwTd-grid-pager" style="width: 800px"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--文件选择框-->
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle">选择文件（请确认文件为.xls格式）</h4>
                <button type="button" id="fileHide" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <td><input type="file" id="fileWindow" name="file"
                                                   style="width: 280px;height:34px"/></td>
                                        <input type="hidden" name="sjlx" id="sjlx">
                                        <td><input type="submit" id="fileSub" class="filesub" value="导入"/></td>
                                        <td><a href="" class="filesub" id="fileDownLoad">模板下载</a></td>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <table id="bdcxm-grid-table"></table>
                    <div id="bdcxm-grid-pager"></div>
                </div>
            </div>

        <#--</div>-->
        </div>
    </div>
</div>
<div class="Pop-upBox moveModel" style="display: none;" id="fileInput1">
    <div class="modal-dialog gjSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="fileTitle1">关联项目</h4>
                <button type="button" id="fileHide1" class="fileHide"><i
                        class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="page-content" id="glfw1">
                <div class="space-4"></div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="tabbable">
                            <div class="simpleSearch">
                                <form id="fileForm1" action="upload.do" method="post" enctype="multipart/form-data">
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td><input type="text" style="width: 265px" class="SSinput watermarkText"
                                                       id="bdcxm_search_qlr" data-watermark="受理编号"></td>
                                            <td class="Search"><a href="#" id="bdcxm_search"> 搜索<i
                                                    class="ace-icon fa fa-search bigger-130"></i></a></td>
                                        </tr>
                                    </table>
                                </form>
                            </div>
                        </div>
                        <table id="bdcxm-grid-table1"></table>
                        <div id="bdcxm-grid-pager1"></div>
                    </div>
                </div>
            </div>

        <#--</div>-->
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
    <input type="hidden" id="gdproid" name="gdproid"/>
    <input type="hidden" id="mulGdfw" name="mulGdfw"/>
    <input type="hidden" id="djIds" name="djIds"/>
    <input type="hidden" id="bdcdyhs" name="bdcdyhs"/>
    <input type="hidden" id="tdids" name="tdids"/>
    <input type="hidden" id="qlid" name="qlid"/>
    <input type="hidden" id="qlzt" name="qlzt"/>
    <input type="hidden" id="gdproids" name="gdproids"/>
    <input type="hidden" id="qlids" name="qlids"/>
    <input type="hidden" id="qlr"/>
    <input type="hidden" id="ybdcqzh" name="ybdcqzh"/>
</form>
<input type="hidden" id="iframeSrcUrl">
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
