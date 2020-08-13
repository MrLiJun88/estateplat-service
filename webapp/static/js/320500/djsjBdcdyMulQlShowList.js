$(function () {
    //时间控件
    $('.date-picker').datepicker({
        autoclose: true,
        todayHighlight: true,
        language: 'zh-CN'
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
    Array.prototype.remove = function (index) {
        if (index > -1) {
            this.splice(index, 1);
        }
    };
    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
    var ua = navigator.userAgent.toLowerCase();
    if (window.ActiveXObject) {
        if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
            $(window).resize(function () {
                $.each($(".moveModel > .modal-dialog"), function () {
                    $(this).css("left", ($(window).width() - $(this).width()) / 2);
                    $(this).css("top", "40px");
                })
            })
        }
    }
    /*   文字水印  */
    $(".watermarkText").watermark();


    shoppingCartNameSetUp();

    $(document).keydown(function (event) {
        if (event.keyCode == 13) { //绑定回车
        }
    });

    $("#djsjTab,#ywsjTab,#qlxxTab,#gdfcsjTab,#gdtdsjTab,#hhcfsjTab").click(function () {
        //shoppingCartNameSetUp();
        if (this.id == "djsjTab") {
            $("#djsj").addClass("active");
            $("#ywsjHide").click();
            djsjInitTable("djsj");
            //tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
        } else if (this.id == "ywsjTab") {
            $("#ywsj").addClass("active");
            $("#djsjHide").click();
            ywsjInitTable("ywsj");
            //tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
        } else if (this.id == "qlxxTab") {
            $("#qlxx").addClass("active");
            $("#qlxxHide").click();
            qlxxInitTable("qlxx");
            //tableReload("qlxx-grid-table", qlxxUrl, {dcxc: $("#qlxx_search").val()});
        } else if (this.id == "gdfcsjTab") {
            $("#gdfcsj").addClass("active");
            $("#fcsjHide").click();
            gdfcInitTable("gdfcsj");
        } else if (this.id == "gdtdsjTab") {
            $("#gdtdsj").addClass("active");
            $("#tdsjHide").click();
            gdtdInitTable("gdtdsj");
        } else if (this.id == "hhcfsjTab") {
            $("#hhcfsj").addClass("active");
            var param = "zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
            $.cookie("param_" + proid, param);
            var url = bdcdjUrl + "/selectBdcdyQlShow/showHhcf?proid=" + proid;
            $("#hhcf").attr("src", url);
        }
        $mulData = new Array();
        $mulRowid = new Array();
    })
    $("#cfTab").click(function () {
        if (this.id == "cfTab") {
            $("#gdcfsj").addClass("active");
            gdcfInitTable("gdcfsj");
        }
        $mulData = new Array();
        $mulRowid = new Array();
    });
    //搜索事件
    $("#djsj_search_btn").click(function () {
        if (showOptimize == "true") {
            var qlr = $("#qlrdjsj").val();
            var tdzl = $("#tdzldjsj").val();
            var bdcdyh = $("#bdcdyhdjsj").val();
            var fwbm = $("#fwbmdjsj").val();
            var djh = $("#djhdjsj").val();
            var bdclx = $("#bdclxSelect").val();
            var exactQuery = "false";
            if ((qlr == "" || qlr == null || qlr == undefined)
                && (tdzl == "" || tdzl == null || tdzl == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (djh == "" || djh == null || djh == undefined)
                && (bdclx == "" || bdclx == null || bdclx == undefined)
                && (fwbm == "" || fwbm == null || fwbm == undefined)) {
                tipInfo("请输入权利人/坐落/不动产单元号/房屋代码");
            } else {
                var djsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm;
                tableReload("djsj-grid-table", djsjUrl, {
                    qlr: qlr,
                    fwbm: fwbm,
                    tdzl: tdzl,
                    bdcdyh: bdcdyh,
                    djh: djh,
                    bdclx: bdclx,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#djsj_search").val();
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入权利人/坐落/不动产单元号");
            } else {
                var djsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm;
                tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
            }
        }
    })
    $("#ywsj_search_btn").click(function () {
        var dcxc = $("#ywsj_search").val();
        if (showOptimize == "true") {
            var bdcqzh = $("#bdcqzh").val();
            var qlr = $("#qlrywsj").val();
            var zl = $("#zlywsj").val();
            var fwbm = $("#fwbmywsj").val();
            var bdcdyh = $("#bdcdyhywsj").val();
            var cqzhjc = $("#cqzhjcywsj").val();
            var bdclx = $("#bdclx").val();
            var exactQuery = "false";
            if ((bdcqzh == "" || bdcqzh == null || bdcqzh == undefined)
                && (qlr == "" || qlr == null || qlr == undefined)
                && (zl == "" || zl == null || zl == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)
                && (fwbm == "" || fwbm == null || fwbm == undefined)) {
                tipInfo("请输入不动产权证号/权利人/坐落/不动产单元号/产权证号简称/房屋代码");
            } else {
                var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("ywsj-grid-table", ywsjUrl, {
                    bdcqzh: bdcqzh,
                    fwbm: fwbm,
                    qlr: qlr,
                    zl: zl,
                    bdclx: bdclx,
                    bdcdyh: bdcdyh,
                    cqzhjc: cqzhjc,
                    exactQuery: exactQuery
                });
            }
        } else {
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入不动产权证号/权利人/坐落/不动产单元号");
            } else {
                var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
            }
        }
    })
    $("#qlxx_search_btn").click(function () {
        if (showOptimize == "true") {
            var bdcdyh = $("#bdcdyhqlxx").val();
            var zl = $("#zlqlxx").val();
            var bzxr = $("#bzxrqlxx").val();
            var fwbm = $("#fwbmqlxx").val();
            var bdcqzh = $("#bdcqzhqlxx").val();
            var cfwh = $("#cfwhqlxx").val();
            var cqzhjc = $("#cqzhjcqlxx").val();
            var qlr = $("#qlr").val();
            var exactQuery = "false";
            if ((zl == "" || zl == null || zl == undefined)
                && (bzxr == "" || bzxr == null || bzxr == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (bdcqzh == "" || bdcqzh == null || bdcqzh == undefined)
                && (cfwh == "" || cfwh == null || cfwh == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)
                && (fwbm == "" || fwbm == null || fwbm == undefined) && (qlr == "" || qlr == null || qlr == undefined)) {
                tipInfo("请输入不动产权证号/被查封权利人/坐落/不动产单元号/查封文号/产权证号简称/房屋代码");
            } else {
                var qlxxUrl = bdcdjUrl + "/selectBdcdyQlShow/getQlxxListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("qlxx-grid-table", qlxxUrl, {
                    zl: zl,
                    fwbm: fwbm,
                    bzxr: bzxr,
                    bdcdyh: bdcdyh,
                    bdcqzh: bdcqzh,
                    cfwh: cfwh,
                    cqzhjc: cqzhjc,
                    qlr: qlr,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#qlxx_search").val();
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入被查封权利人/坐落/不动产单元号/查封文号/产权证号");
            } else {
                var qlxxUrl = bdcdjUrl + "/selectBdcdyQlShow/getQlxxListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("qlxx-grid-table", qlxxUrl, {dcxc: $("#qlxx_search").val()});
            }
        }
    })

    /*高级按钮点击事件 begin*/
    $("#djsjShow,#ywsjShow,#gdtdsjShow,#gdfcsjShow").click(function () {
        if (this.id == "ywsjShow") {
            $("#ywsjSearchPop").show();
        } else if (this.id == "djsjShow") {
            $("#djsjSearchPop").show();
        } else if (this.id == "gdtdsjShow") {
            $("#tdsjSearchPop").show();
        } else if (this.id == "gdfcsjShow") {
            $("#fcsjSearchPop").show();
        }
    });

    //单元号高级查询的搜索按钮事件
    $("#djsjGjSearchBtn").click(function () {
        var djsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&" + $("#djsjSearchForm").serialize();
        tableReload("djsj-grid-table", djsjUrl, {dcxc: ""});
        $("#djsjSearchPop").hide();
        $("#djsjSearchForm")[0].reset();
    })

    //土地高级查询的搜索按钮事件
    $("#ywsjGjSearchBtn").click(function () {
        var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&qlxzdm=" + qlxzdm + "&bdclxdm=" + bdclxdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm + "&" + $("#ywsjSearchForm").serialize();
        tableReload("ywsj-grid-table", ywsjUrl, {dcxc: ""});
        $("#ywsjSearchPop").hide();
        $("#ywsjSearchForm")[0].reset();
    })

    $("#djsjHide,#ywsjHide,#tdsjHide,#fcsjHide,#cfxxCloseBtn,#tipHide,#tipCloseBtn,#djsjMulCloseBtn,#ywsjMulCloseBtn,#qlxxMulCloseBtn,#bdcdyTipHide,#bdcdyTipCloseBtn,#gdtdsjMulCloseBtn,#gdfcsjMulCloseBtn").click(function () {
        if (this.id == "djsjHide") {
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        } else if (this.id == "ywsjHide") {
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        } else if (this.id == "tdsjHide") {
            $("#tdsjSearchPop").hide();
            $("#tdsjSearchForm")[0].reset();
        } else if (this.id == "fcsjHide") {
            $("#fcsjSearchPop").hide();
            $("#fcsjSearchForm")[0].reset();
        } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
            setTimeout($.unblockUI, 10);
        } else if (this.id == "djsjMulCloseBtn") {
            $("#djsj-grid-table").trigger("reloadGrid");
            $("#modal-backdrop-mul").hide();
            $("#djsjMulTable").hide();
        } else if (this.id == "ywsjMulCloseBtn") {
            $("#ywsj-grid-table").trigger("reloadGrid");
            $("#modal-backdrop-mul").hide();
            $("#ywsjMulTable").hide();
        } else if (this.id == "qlxxMulCloseBtn") {
            $("#qlxx-grid-table").trigger("reloadGrid");
            $("#modal-backdrop-mul").hide();
            $("#qlxxMulTable").hide();
        } else if (this.id == "cfxxCloseBtn") {
            $("#modal-backdrop-mul").hide();
            $("#gdCfXxMulTable").hide();
        } else if (this.id == "gdtdsjMulCloseBtn") {
            $("#modal-backdrop-mul").hide();
            $("#gdtdsjMulTable").hide();
        } else if (this.id == "gdfcsjMulCloseBtn") {
            $("#modal-backdrop-mul").hide();
            $("#gdfcsjMulTable").hide();
        } else if (this.id == "bdcdyTipHide" || this.id == "bdcdyTipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#bdcdyTipPop").hide();
            valite.length = 0;
            setTimeout($.unblockUI, 10);
        }
    });

    $("#bdcdyTipBackBtn").click(function () {
        $(this).hide();
        $("#bdcdyTipHide,#bdcdyTipCloseBtn").show();
        mulDisplayTip();
    });

    $(".djsjSearchPop-modal,.ywsjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});
    //默认初始化表格
    if (bdcdyly == '2' || bdcdyly == '0' || bdcdyly == '4') {
        if (sqlxdm == '8009901') {
            $('#gdfcsj').addClass('active');
            gdfcInitTable("gdfcsj");
        } else if (sqlxdm == '8009903') {
            $('#gdtdsj').addClass('active');
            gdtdInitTable("gdtdsj");
        } else if (sqlxdm == '806') {
            $('#ywsj').addClass('active');
            ywsjInitTable("ywsj");
        } else {
            $('#djsj').addClass('active');
            djsjInitTable("djsj");
        }
    } else if (bdcdyly == '1' || bdcdyly == '7') {
        if (sqlxdm == '4009904') {
            $('#gdfcsj').addClass('active');
            gdfcInitTable("gdfcsj");
        } else {
            $('#ywsj').addClass('active');
            ywsjInitTable("ywsj");
        }
    } else if (bdcdyly == '3') {
        if (sqlxdm == '8009902' || sqlxdm == '8009904') {
            $("#gdcfsj").addClass("active");
            gdcfInitTable("gdcfsj");
        } else {
            $('#qlxx').addClass('active');
            qlxxInitTable("qlxx");
        }
    } else if (bdcdyly == '5') {
        $('#gdfcsj').addClass('active');
        gdfcInitTable("gdfcsj");
    } else if (bdcdyly == '6') {
        $('#gdtdsj').addClass('active');
        gdtdInitTable("gdtdsj");
    } else if (bdcdyly == '8' && (sqlxdm == '9920001' || sqlxdm == '9920005')) {
        $('#ywsj').addClass('active');
        ywsjInitTable("ywsj");
    }

    //三种类型选择的确定按钮
    $("#djsjSure,#ywsjSure,#gdCfSure,#gdCfSureBtn,#qlxxSure,#djsjMulSureBtn,#ywsjMulSureBtn,#qlxxMulSureBtn,#gdtdSure,#gdfcSure,#gdtdsjMulSureBtn,#gdfcsjMulSureBtn").click(function () {
        if ($mulData.length == 0) {
            tipInfo("请至少选择一条数据！");
            return;
        }
        var proid = "";
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var yzwProids = [];
        var bdcXmRelList = {};
        var bdcdyhs = {};
        var yxmids = {};
        var djIds = {};
        for (var i = 0; i < $mulData.length; i++) {
            djIds["djIds[" + i + "]"] = $mulData[i].ID;
            bdcXmRelList["bdcXmRelList[" + i + "].proid"] = proid;
            if (this.id == "djsjSure" || this.id == "djsjMulSureBtn") {
                bdcXmRelList["bdcXmRelList[" + i + "].qjid"] = $mulData[i].ID;
            } else {
                yzwProids.push($mulData[i].PROID);
                yxmids["yxmids[" + i + "]"] = $mulData[i].PROID;
                bdcXmRelList["bdcXmRelList[" + i + "].yproid"] = $mulData[i].PROID;
            }
            bdcXmRelList["bdcXmRelList[" + i + "].ydjxmly"] = "1";
            bdcdyhs["bdcdyhs[" + i + "]"] = $mulData[i].BDCDYH;
        }
        if (yzdfyz(yzwProids)) {
            return;
        }
        if (this.id == "djsjSure" || this.id == "djsjMulSureBtn") {
            djsjEditXm(djIds, bdcXmRelList, bdcdyhs);
        } else if (this.id == "ywsjSure" || this.id == "ywsjMulSureBtn" || this.id == "qlxxSure" || this.id == "qlxxMulSureBtn") {
            ywsjEditXm(yxmids, bdcdyhs, bdcXmRelList)
        } else if (this.id == 'gdfcSure' || this.id == 'gdtdSure' || this.id == 'gdCfSure' || this.id == 'gdCfSureBtn' || this.id == "gdtdsjMulSureBtn" || this.id == "gdfcsjMulSureBtn") {
            var gdproids = "";
            var qlids = "";
            var gdproidArr = new Array();
            var qlidArr = new Array();
            for (var i = 0; i < $mulData.length; i++) {
                gdproidArr.push($mulData[i].GDPROID)
                qlidArr.push($mulData[i].QLID);
            }
            gdproids = gdproidArr.join(",");
            qlids = qlidArr.join(",");
            $('#gdproids').val(gdproids);
            $('#qlids').val(qlids);
            if (this.id == 'gdfcSure' || this.id == "gdfcsjMulSureBtn") {
                checkMulXm(gdproids, qlids);
            } else if (this.id == 'gdCfSure' || this.id == "gdCfSureBtn") {
                checkMulXm(gdproids, qlids);
            } else if (this.id == "gdtdSure" || this.id == "gdtdsjMulSureBtn") {
                $bdclx = "TD";
                checkMulXm(gdproids, qlids);
            }
        }
    });

    $("#ywsjSureMul").click(function () {
        if ($mulData.length == 0) {
            tipInfo("请至少选择一条数据！");
            return;
        }
        var proid = "";
        if ($("#proid").val() != '') {
            proid = $("#proid").val();
        }
        var yzwProids = [];
        var bdcXmRelList = {};
        var bdcdyhs = {};
        var yxmids = {};
        var yproids = "";
        for (var i = 0; i < $mulData.length; i++) {
            yproids += "," + $mulData[i].PROID;
        }
        $.ajax({
            type: 'POST',
            url: bdcdjUrl + '/selectBdcdyQlShow/getMulBdcqzxx',
            data: {yxmids: yproids},
            async: false,
            success: function (result) {
                if (result != null && result != '') {
                    var i = 0;
                    $.each(result, function (index, data) {
                        bdcXmRelList["bdcXmRelList[" + i + "].proid"] = proid;
                        yzwProids.push(data.proid);
                        yxmids["yxmids[" + i + "]"] = data.proid;
                        bdcXmRelList["bdcXmRelList[" + i + "].yproid"] = data.proid;
                        bdcXmRelList["bdcXmRelList[" + i + "].ydjxmly"] = "1";
                        bdcdyhs["bdcdyhs[" + i + "]"] = data.bdcdyh;
                        i++;
                    })
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("#modal-backdrop-mul").hide();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            }
        });
        if (yzdfyz(yzwProids)) {
            return;
        }
        ywsjEditXm(yxmids, bdcdyhs, bdcXmRelList);
    });

    //三种类型选择的已选按钮
    $("#djsjMulXx,#ywsjMulXx,#qlxxMulXx,#gdCfMulXx,#gdtdsjMulXx,#gdfcsjMulXx").click(function () {
        $("#modal-backdrop-mul").show();
        var table = "";
        if (this.id == "djsjMulXx") {
            $("#djsjMulTable").show();
            djsjInitTable("djsj-mul");
            table = "djsj-mul-grid-table";
        } else if (this.id == "ywsjMulXx") {
            $("#ywsjMulTable").show();
            ywsjInitTable("ywsj-mul");
            table = "ywsj-mul-grid-table";
        } else if (this.id == "qlxxMulXx") {
            $("#qlxxMulTable").show();
            qlxxInitTable("qlxx-mul");
            table = "qlxx-mul-grid-table";
        } else if (this.id == "gdCfMulXx") {
            $("#gdCfXxMulTable").show();
            gdcfInitTable("gdcfsj-mul");
            table = "gdcfsj-mul-grid-table";
        } else if (this.id == "gdtdsjMulXx") {
            $("#gdtdsjMulTable").show();
            gdtdInitTable("gdtdsj-mul");
            table = "gdtdsj-mul-grid-table";
        } else if (this.id == "gdfcsjMulXx") {
            $("#gdfcsjMulTable").show();
            gdfcInitTable("gdfcsj-mul");
            table = "gdfcsj-mul-grid-table";
        }
        $("#" + table).jqGrid("clearGridData");
        for (var i = 0; i <= $mulData.length; i++) {
            $("#" + table).jqGrid('addRowData', i + 1, $mulData[i]);
        }
    });
    //三种类型选择的删除按钮
    $("#djsjDel,#ywsjDel,#qlxxDel,#cfxxDel,#gdtdsjDel,#gdfcsjDel").click(function () {
        var ids;
        var table;
        if (this.id == "djsjDel") {
            table = "#djsj-mul-grid-table";
        } else if (this.id == "ywsjDel") {
            table = "#ywsj-mul-grid-table";
        } else if (this.id == "qlxxDel") {
            table = "#qlxx-mul-grid-table";
        } else if (this.id == "cfxxDel") {
            table = "#gdcfsj-mul-grid-table";
        } else if (this.id == "gdtdsjDel") {
            table = "#gdtdsj-mul-grid-table";
        } else if (this.id == "gdfcsjDel") {
            table = "#gdfcsj-mul-grid-table";
        }
        ids = $(table).jqGrid('getGridParam', 'selarrrow');
        for (var i = ids.length - 1; i > -1; i--) {
            var cm = $(table).jqGrid('getRowData', ids[i]);
            var index;
            if (this.id == "djsjDel") {
                index = $.inArray(cm.ID, $mulRowid);
            } else if (this.id == "gdfcsjDel" || this.id == "gdtdsjDel" || this.id == "cfxxDel") {
                index = $.inArray(cm.QLID, $mulRowid);
            } else {
                index = $.inArray(cm.PROID, $mulRowid);
            }
            $mulData.remove(index);
            $mulRowid.remove(index);
            $(table).jqGrid("delRowData", ids[i]);
        }
    });
    //土地高级查询的搜索按钮事件
    $("#tdsjGjSearchBtn").click(function () {
        var gdtdsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdtdzListByPage?&" + $("#tdsjSearchForm").serialize();
        tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: ""});
        $("#tdsjSearchPop").hide();
        $("#tdsjSearchForm")[0].reset();
    })

    //土地高级查询的搜索按钮事件
    $("#fcsjGjSearchBtn").click(function () {
        var gdfcsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdfczListByPage?&" + $("#fcsjSearchForm").serialize();
        tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: ""});
        $("#fcsjSearchPop").hide();
        $("#fcsjSearchForm")[0].reset();
    })
    $("#gdfcsj_search_btn").click(function () {
        if (showOptimize == "true") {
            var qlr = $("#qlrgdfc").val();
            var fczh = $("#fczh").val();
            var bdcdyh = $("#bdcdyhgdfc").val();
            var fwzl = $("#fwzlgdfc").val();
            var cqzhjc = $("#cqzhjcgdfc").val();
            var exactQuery = "false";
            if ((qlr == "" || qlr == null || qlr == undefined)
                && (fczh == "" || fczh == null || fczh == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (fwzl == "" || fwzl == null || fwzl == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)) {
                tipInfo("请输入权利人/坐落/房产证号");
            } else {
                var gdfcsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdfczListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("gdfcsj-grid-table", gdfcsjUrl, {
                    qlr: qlr,
                    fczh: fczh,
                    fwzl: fwzl,
                    bdcdyh: bdcdyh,
                    cqzhjc: cqzhjc,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#gdfcsj_search").val();
            var searchBdcdyh = $("#gdfcBdcdyh").val();
            if ((dcxc == "" || dcxc == null || dcxc == undefined) && (searchBdcdyh == "" || searchBdcdyh == null || searchBdcdyh == undefined)) {
                tipInfo("请输入权利人/坐落/房产证号");
            } else {
                var gdfcsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdfczListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm + "&searchBdcdyh=" + searchBdcdyh;
                tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: $("#gdfcsj_search").val()});
            }
        }
    })
    $("#gdtdsj_search_btn").click(function () {
        if (showOptimize == "true") {
            var qlr = $("#qlrgdtd").val();
            var tdzh = $("#tdzh").val();
            var bdcdyh = $("#bdcdyhgdtd").val();
            var tdzl = $("#tdzlgdtd").val();
            var cqzhjc = $("#cqzhjcgdtd").val();
            var exactQuery = "false";
            if ((qlr == "" || qlr == null || qlr == undefined)
                && (tdzh == "" || tdzh == null || tdzh == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (tdzl == "" || tdzl == null || tdzl == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)) {
                tipInfo("请输入权利人/坐落/土地证号");
            } else {
                var gdtdsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdtdzListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("gdtdsj-grid-table", gdtdsjUrl, {
                    qlr: qlr,
                    tdzh: tdzh,
                    bdcdyh: bdcdyh,
                    tdzl: tdzl,
                    cqzhjc: cqzhjc,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#gdtdsj_search").val();
            var searchBdcdyh = $("#gdtdBdcdyh").val();
            if ((dcxc == "" || dcxc == null || dcxc == undefined) && (searchBdcdyh == "" || searchBdcdyh == null || searchBdcdyh == undefined)) {
                tipInfo("请输入权利人/坐落/土地证号");
            } else {
                var gdtdsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdtdzListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm + "&searchBdcdyh=" + searchBdcdyh;
                tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: $("#gdtdsj_search").val()});
            }
        }
    })
    $("#gdcfsj_search_btn").click(function () {
        if (showOptimize == "true") {
            var bdcdyh = $("#bdcdyhgdcf").val();
            var cfwh = $("#cfwhgdcf").val();
            var yqzh = $("#yqzhgdcf").val();
            var fwzl = $("#fwzlgdcf").val();
            var tdzl = $("#tdzlgdcf").val();
            var exactQuery = "false";
            if ((yqzh == "" || yqzh == null || yqzh == undefined)
                && (cfwh == "" || cfwh == null || cfwh == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (fwzl == "" || fwzl == null || fwzl == undefined)
                && (tdzl == "" || tdzl == null || tdzl == undefined)) {
                tipInfo("请输入查封文号/坐落/不动产单元号");
            } else {
                var gdcfsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdcfListByPage?proid=" + proid;
                tableReload("gdcfsj-grid-table", gdcfsjUrl, {
                    yqzh: yqzh,
                    cfwh: cfwh,
                    bdcdyh: bdcdyh,
                    fwzl: fwzl,
                    tdzl: tdzl,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#gdcfsj_search").val();
            var searchBdcdyh = $("#gdcfBdcdyh").val();
            if ((dcxc == "" || dcxc == null || dcxc == undefined) && (searchBdcdyh == "" || searchBdcdyh == null || searchBdcdyh == undefined)) {
                tipInfo("请输入查封文号/坐落/产权证号");
            } else {
                var gdcfsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdcfListByPage?searchBdcdyh=" + searchBdcdyh + "&proid=" + proid;
                tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: $("#gdcfsj_search").val()});
            }
        }
    })

    $("button[name='addHhcf']").click(function () {
        //获取localstorage
        var json = [];
        if ($mulData && $mulData.length > 0) {
            var type = $mulData[0].TYPE;
            //处理$mulData
            var mulData = changeJson($mulData);
            var sessionData = window.localStorage.getItem("hhcfData_" + proid);
            if (sessionData && sessionData != 'undefined') {
                json = JSON.parse(sessionData);
                var find = false;
                $.each(json, function (index, jsonData) {
                    if (jsonData.type && jsonData.type == type) {
                        var rowid = jsonData.id;
                        var arrayData = jsonData.data;
                        $.each($mulRowid, function (index, data) {
                            var i = $.inArray(data, rowid);
                            if (i < 0) {
                                arrayData.push(mulData[index]);
                                rowid.push(data);
                            }
                        });
                        find = true;
                        jsonData.id = rowid;
                        jsonData.data = arrayData;
                        return false;
                    }
                });
                //不同类型数据保存
                if (!find) {
                    json.push({type: type, data: $mulData, id: $mulRowid});
                }
            } else {
                json.push({type: type, data: $mulData, id: $mulRowid});
            }
            sessionData = JSON.stringify(json);
            window.localStorage.setItem("hhcfData_" + proid, sessionData);
            tipInfo("添加数据成功");
        } else {
            tipInfo("请选择数据");
        }
    });

    $('#gdcfBdcdyh').focus(function () {
        $('#gdcfsj_search').val("");
    });
    $('#gdcfsj_search').focus(function () {
        $('#gdcfBdcdyh').val("");
    });
    $('#gdfcBdcdyh').focus(function () {
        $('#gdfcsj_search').val("");
    });
    $('#gdfcsj_search').focus(function () {
        $('#gdfcBdcdyh').val("");
    });
    $('#gdtdBdcdyh').focus(function () {
        $('#gdtdsj_search').val("");
    });
    $('#gdtdsj_search').focus(function () {
        $('#gdtdBdcdyh').val("");
    });
});


//购物车名称设定
function shoppingCartNameSetUp() {
    if (sqlxdm == "9979902" || sqlxdm == "9979902") {
        //$("#shoppingCartButton").html("添加到抵押清单");
        $("span[name='shoppingCartButton']").html("添加到抵押清单");
    } else if (sqlxdm == "806") {
        //$("#shoppingCartButton").html("添加到查封清单");
        $("span[name='shoppingCartButton']").html("添加到查封清单");
    } else if (sqlxdm == "8009901" || sqlxdm == "8009903") {
        $("button[name='addHhcf']").attr("style", "display:none;");
    }
}


//地籍数据
function djsjInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";

    //绑定回车键
    $('#djsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#qlrdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#tdzldjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#djhdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fwbmdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
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
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'ID'},
        colNames: ["地籍号", '不动产单元号', '房屋代码', '坐落', '权利人', '不动产类型', '不动产单元状态', '交易状态', 'bdcdylx', "ID"],
        colModel: [
            {name: 'DJH', index: 'DJH', width: '18%', sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '25%', sortable: false},
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'TDZL', index: 'TDZL', width: '20%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
            {
                name: 'BDCLX',
                index: 'BDCLX',
                width: '15%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
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
                    if (value == "") {
                        value = cellvalue;
                    }
                    return value;
                }
            },
            {name: 'QLZT', index: 'QLZT', width: '10%', sortable: false},
            {name: 'JYZT', index: 'JYZT', width: '10%', sortable: false},
            {name: 'BDCDYLX', index: 'BDCDYLX', width: '0%', hidden: true},
            {name: 'ID', index: 'ID', width: '10%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
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
                //resize
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "auto");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
            }

            //赋值数量
            $("#djsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            qlrForTable(grid_selector, bdclxdm, zdtzm);

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getbdcdyZt(null, data.ID, data.BDCDYLX, $(grid_selector), data.ID);
                getbdcdyJyZt(data.FWBM, $(grid_selector), data.ID);
                if (data.QLR == "") {
                    getZdQlrByDjh(data.DJH, $(grid_selector), data.ID)
                }
            });
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                //判断是已选择界面还是原界面
                if (cm.ID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        //混合查封中数据来源的标志
                        cm["TYPE"] = "bdcdy";
                        $mulData.push(cm);
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            })
            //赋值数量
            $("#djsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            //判断是已选择界面还是原界面
            if (cm.ID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    //混合查封中数据来源的标志
                    cm["TYPE"] = "bdcdy";
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#djsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

//权利信息
function qlxxInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
    $('#qlxx_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcqzhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cqzhjcqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#zlqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bzxrqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cfwhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fwbmqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });

    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ["产权证号", "proid", "BDCDYID", "不动产单元号", "房屋代码", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间', '操作'],
        colModel: [
            {name: 'CQZH', index: 'CQZH', width: '10%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '0%', hidden: true},
            {name: 'BDCDYID', index: 'BDCDYID', width: '10%', hidden: true, sortable: false},
            {name: 'BDCDYH', index: 'BDCDYH', width: '15%', sortable: false},
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: 'ZL', width: '11%', sortable: false},
            {name: 'CFLX', index: 'CFLX', width: '6%', sortable: false},
            {name: 'CFJG', index: 'CFJG', width: '6%', sortable: false},
            {name: 'CFWH', index: 'CFWG', width: '10%', sortable: false},
            {name: 'CFSQR', index: 'CFSQR', width: '6%', sortable: false, hidden: true},
            {name: 'BCFQLR', index: 'BCFQLR', width: '6%', sortable: false},
            {name: 'CFKSQX', index: 'CFKSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CFJSQX', index: 'CFJSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CZ', index: 'CZ', width: '6%', sortable: false}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
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
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
            }
            //赋值数量
            $("#qlxxMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getGdCfCqzh(data.PROID, data.BDCDYID, "", $(grid_selector), data.BDCDYH, data.PROID);
            });
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }

        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.PROID == e) {
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
            $("#qlxxMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.PROID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#qlxxMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//业务数据
function ywsjInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
    $('#ywsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcqzh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cqzhjcywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#zlywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#qlrywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fwbmywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
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
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ["proid", "不动产单元号", '不动产权证号', '房屋代码', '坐落', '权利人', '不动产单元状态', '权利状态', '交易状态', '不动产类型', '操作', "匹配状态"],
        colModel: [
            {name: 'PROID', index: 'PROID', width: '0%', hidden: true},
            {name: 'BDCDYH', index: '', width: '12%', sortable: false},
            {name: 'BDCQZH', index: 'BDCQZH', width: '11%', sortable: false},
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: '', width: '10%', sortable: false},
            {name: 'QLR', index: '', width: '6%', sortable: false},
            {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false, hidden: true},
            {name: 'QLZT', index: 'QLZT', width: '10%', sortable: false},
            {name: 'JYZT', index: 'JYZT', width: '10%', sortable: false},
            {name: 'BDCLX', index: '', width: '6%', sortable: false, hidden: true},
            {name: 'CZ', index: 'CZ', width: '6%', sortable: false},
            {name: 'PPZT', index: 'PPZT', width: '6%', sortable: false}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
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
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID, $(grid_selector), data.PROID);
                getbdcdyZt(data.PROID, null, data.BDCLX, $(grid_selector), data.PROID);
                getbdcdyJyZt(data.FWBM, $(grid_selector), data.PROID)
                getbdcdyPpZt(data.PROID, $(grid_selector), data.PROID);
            })
            //赋值数量
            $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");

            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.PROID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        //混合查封中数据来源的标志
                        cm["TYPE"] = "bdcqz";
                        $mulData.push(cm);
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            })
            //赋值数量
            $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.PROID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    //混合查封中数据来源的标志
                    cm["TYPE"] = "bdcqz";
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#ywsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//jyl 获取权利人名称和座落
//获取数据
function getQlrmcAndZl(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getdateByProid?proid=" + proid,
        success: function (result) {
            getCzxx(rowid, table, result.bdcdyh, proid);
            table.setCell(rowid, "FWBM", result.fwbm);
            table.setCell(rowid, "ZL", result.zl);
            table.setCell(rowid, "QLR", result.qlr);
            table.setCell(rowid, "BDCDYH", result.bdcdyh);
            table.setCell(rowid, "BDCLX", result.bdclx);
            table.setCell(rowid, "BDCQZH", result.bdcqzh);
        }
    });
}

function getbdcdyZt(proid, djid, bdclx, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyZt?proid=" + proid + "&djid=" + djid + "&bdclx=" + bdclx,
        success: function (result) {
            var cellVal = ""
            if (result.bdcCxBdcdyZt != null) {
                if (result.bdcCxBdcdyZt.xszjgcdycs == 1) {
                    cellVal += '<span class="label label-default">在建工程抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsygcs == 1) {
                    cellVal += '<span class="label label-pink">预告</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsydyacs == 1) {
                    cellVal += '<span class="label label-success">预抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyacs == 1) {
                    cellVal += '<span class="label label-success">抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsycfcs == 1) {
                    cellVal += '<span class="label label-danger">预查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xscfcs == 1) {
                    cellVal += '<span class="label label-danger">查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsyycs == 1) {
                    cellVal += '<span class="label label-warning">异议</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyics == 1) {
                    cellVal += '<span class="label label-purple">地役</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xssdcs == 1) {
                    cellVal += '<span class="label label-yellow">锁定</span><span> </span>';
                }
            }
            if (cellVal == "" && djid == null && proid == null) {
                cellVal += '<span class="label label-success">未匹配不动产单元</span><span> </span>';
            } else if (cellVal == "") {
                cellVal += '<span class="label label-success">正常</span><span> </span>';
            }
            table.setCell(rowid, "BDCDYZT", cellVal);
        }
    });
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyQlZt?proid=" + proid + "&djid=" + djid + "&bdclx=" + bdclx,
        success: function (result) {
            var cellVal = ""
            if (result.sfyg == true) {
                cellVal += '<span class="label label-pink">预告</span><span> </span>';
            }
            if (result.sfdy == true) {
                cellVal += '<span class="label label-success">抵押</span><span> </span>';
            }
            if (result.sfcf == true || result.sfyy == true || result.sfsd == true) {
                cellVal += '<span class="label label-danger">限制</span><span> </span>';
            }
            if (result.sfcd == true) {
                cellVal += '<span class="label label-danger">裁定</span><span> </span>';
            }
            if (cellVal == "") {
                cellVal += '<span class="label label-success">确权</span><span> </span>';
            }
            table.setCell(rowid, "QLZT", cellVal);
        }
    });
}

function getbdcdyPpZt(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getPpzt?proid=" + proid,
        success: function (result) {
            var ppzt = '';
            ppzt = '<a href="javascript:showPpjg(\'' + proid + '\')"><span class="label label-success" value=' + ppzt + '>' + result + '</span></a>';
            table.setCell(rowid, "PPZT", ppzt);
        },
        error: function () {
            tipInfo("error");
        }
    });
}

function showPpjg(proid) {
    showWindow(bdcdjUrl + "/bdcpic/showPpjg?proid=" + proid + "&order=true", "匹配结果查看", 1100, 400);
}

function getZdQlrByDjh(djh, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getZdQlrByDjh?djh=" + djh,
        success: function (result) {
            if (result != "" && result.qlr != "")
                table.setCell(rowid, "QLR", result.qlr);
        }
    });
}

function getbdcdyJyZt(fwbm, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcdyJyZt?fwbm=" + fwbm + "&proid=" + rowid,
        success: function (result) {
            var cellVal = "";
            if (result.jyzt == "0") {
                cellVal += '<span class="label label-danger">交易数据缺失</span>';
            }
            if (result.jyzt == "1") {
                cellVal += '<span class="label label-gray">待售</span>';
            }
            if (result.jyzt == "2") {
                cellVal += '<span class="label label-info">已售</span>';
            }
            if (result.jyzt == "3") {
                cellVal += '<span class="label label-success">备案</span>';
            }
            if (result.jyzt == "9") {
                cellVal += '<span class="label label-success">确权</span>';
            }
            table.setCell(rowid, "JYZT", cellVal);
        }
    });

}

function dateForTable(grid_selector) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getdate($(grid_selector), rowIds[index], data.BDCLX, data.QLID);
    })
}

//获取数据
function getdate(table, rowid, bdclx, qlid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcDateByQlid?qlid=" + qlid,
        success: function (result) {
            table.setCell(qlid, "BDCDYH", result.bdcdyh);
            table.setCell(qlid, "BDCLX", result.bdclx);
            table.setCell(qlid, "DJID", result.djid);
            getbdcdyZt(null, result.djid, result.bdclx, table, qlid);
        }
    });
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

//修改项目信息的函数
function djsjEditXm(djIds, bdcXmRelList, bdcdyhs) {
    djidsAll = djIds;
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl + '/wfProject/checkMulBdcXm?proid=' + proid,
        type: 'post',
        data: $.param(bdcdyhs),
        dataType: 'json',
        success: function (data) {
            if (data && data != 'undefined')
                hadleTipData(data);
            if (data.length > 0)
                djsjDisplayTip(data, djIds, proid, bdcXmRelList, bdcdyhs);
            else {
                $("#bdcXmRelList").val(bdcXmRelList);
                $("#bdcdyTipPop").show();
                $("#modal-backdrop").show();
                mulDisplayTip("djsj");
            }

        },
        error: function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            $("#modal-backdrop-mul").hide();
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    };
    $.ajax(options);
}

function djsjInitVoFromOldData(djIds, proid, bdcXmRelList, bdcdyhs) {
    //权籍创建不应该带入bdcxmrel
    bdcXmRelList = "";
    var bdclx = "";
    if (bdclxdm != null && bdclxdm != "") {
        if (bdclxdm == "W") {
            bdclx = "TD"
        } else if (bdclxdm == "F") {
            bdclx = "TDFW"
        }
    } else {
        bdclx = $("#bdclx").val();
    }
    $.ajax({
        type: 'POST',
        url: bdcdjUrl + '/wfProject/initVoFromOldData?proid=' + proid + "&bdclx=" + bdclx,
        data: $.param(bdcXmRelList) + "&" + $.param(djIds) + "&" + $.param(bdcdyhs),
        success: function (data) {
            if (data == '成功') {
                $.ajax({
                    type: 'get',
                    async: true,
                    url: bdcdjUrl + '/wfProject/updateWorkFlow?proid=' + proid,
                    success: function (data) {
                    }
                });
                $(".mulSelectPop").parent().hide();
                window.parent.hideModel();
                window.parent.resourceRefresh();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            } else {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
            $("#modal-backdrop-mul").hide();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            /*if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }*/
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            $("#modal-backdrop-mul").hide();
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });
}

//修改项目信息的函数
function ywsjEditXm(yxmids, bdcdyhs, bdcXmRelList) {
    bdcXmRelListAll = bdcXmRelList;
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl + '/wfProject/checkMulBdcXm?proid=' + proid,
        data: $.param(yxmids) + "&" + $.param(bdcdyhs) + "&" + $.param(bdcXmRelList),
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if (data && data != 'undefined')
                hadleTipData(data);
            if (valite.length == 0)
                ywsjDisplayTip(data, "", "", "", bdcXmRelList, proid);
            else {
                $("#bdcXmRelList").val(bdcXmRelList);
                $("#bdcdyTipPop").show();
                $("#modal-backdrop").show();
                mulDisplayTip("ywsj");
            }
        },
        error: function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            $("#modal-backdrop-mul").hide();
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    };
    $.ajax(options);
}

function ywsjInitVoFromOldData(proid, bdcXmRelList) {
    $.ajax({
        type: 'POST',
        url: bdcdjUrl + '/wfProject/initVoFromOldData?proid=' + proid,
        data: $.param(bdcXmRelList),
        success: function (data) {

            if (data == '成功') {
                $.ajax({
                    type: 'get',
                    async: true,
                    url: bdcdjUrl + '/wfProject/updateWorkFlow?proid=' + proid,
                    success: function (data) {
                    }
                });
                for (var i = 0; i < $mulData.length; i++) {
                    if ($mulData[i].BDCLX == "TDFW") {
                        var check = checkExistFsss($mulData[i].PROID);
                        if (check == "true") {
                            //抵押，查封，合并流程弹出提示
                            if (sqlxdm == "801" || sqlxdm == "1019" || djlx == "999") {
                                var msg = "主房存在附属设施，是否继承到抵押或查封项目之中"
                                showConfirmDialogWithClose("提示信息", msg, "fsssInherit", "'" + $mulData[i].PROID + "'", "fsssNotInherit", "'" + $mulData[i].PROID + "'");
                            } else {
                                fsssInherit($mulData[i].PROID);
                            }
                        } else {
                            getBdcXtConfigFj();
                        }
                    } else {
                        getBdcXtConfigFj();
                    }
                }

                $(".mulSelectPop").parent().hide();
                window.parent.hideModel();
                window.parent.resourceRefresh();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            } else {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
            $("#modal-backdrop-mul").hide();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#modal-backdrop-mul").hide();
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            /*if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }*/
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });
}

//检查主房不动产状态
function checkExistFsss(proid) {
    var check = '';
    $.ajax({
        url: bdcdjUrl + "/bdcFwfsss/checkExistFsss?proid=" + proid,
        type: 'GET',
        async: false,
        success: function (result) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(result)) {
                if (result.msg == 'exist') {
                    check = 'true';
                }
            }
        },
        error: function (data) {
            $.Prompt("检查失败", 1500);
        }
    });
    return check;
}

function getBdcXtConfigFj() {
    $.ajax({
        type: 'get',
        url: bdcdjUrl + '/wfProject/getBdcXtConfigFj?wiid=' + $("#wiid").val(),
        datatype: 'json',
        success: function () {
        },
        error: function () {
        }
    });
}

//继承上一手的附属设施信息
function fsssInherit(yproid) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }

    $.ajax({
        type: "post",
        async: false,
        url: bdcdjUrl + "/bdcFwfsss/initBdcFwfsssFromFsss?proid=" + proid + "&yproid=" + yproid,
        dataType: "json",
        success: function () {
            getBdcXtConfigFj();
            hideModelAndRefreshParant();
        },
        error: function () {
            getBdcXtConfigFj();
            hideModelAndRefreshParant();
        }
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

function openProjectInfo(proid) {
    if (proid && proid != undefined) {
        $.ajax({
            url: bdcdjUrl + "/qllxResource/getViewUrl?proid=" + proid,
            type: 'post',
            success: function (data) {
                if (data && data != undefined) {
                    openWin(data);
                } else {
                    openWin(bdcdjUrl + '/bdcJsxx?bdcdyh=' + proid);
                }
            },
            error: function (data) {
                tipInfo("查看失败！");
            }
        });
    }
}

function openBdcYcfxx(proid) {
    if (proid && proid != undefined) {
        openWin(formUrl + "/bdcCfxx/ycf?proid=" + proid);
    }
}


function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function createProjectInfo(yproid, sqlxdm, bdcdyh, bdcdyid, alertCount) {
    if (alertCount > 0) {
        tipInfo("验证不通过，不能创建项目！");
        return false;
    }
    if (confirm("确定要创建项目吗？")) {
        $("#tipPop").hide();
        $("#modal-backdrop").hide();
        $.blockUI({message: "请稍等……"});
        if (yproid && yproid != undefined) {
            var proid = $("#proid").val();
            var taskid;
            var url = window.parent.parent.window.location.href;

            if (url != null && url.indexOf("?taskid=") > -1)
                taskid = url.substr(url.indexOf("?taskid=") + 8, url.length);
            $.ajax({
                type: "GET",
                url: bdcdjUrl + "/wfProject/createLhcfByProid?proid=" + proid + "&yproid=" + yproid + "&createSqlxdm=" + sqlxdm + "&bdcdyh=" + bdcdyh + "&bdcdyid=" + bdcdyid,
                success: function (result) {
                    if (result != null && result != "" && taskid != null && taskid != "") {
                        $.ajax({
                            type: "post",
                            url: platformUrl + "/task!del.action?taskid=" + taskid,
                            success: function (data) {
                                if (data.indexOf("true") > -1 || data.indexOf("1") > -1) {
                                    window.parent.parent.window.location.href = portalUrl + "/taskHandle?taskid=" + result;
                                }
                            }
                        });
                    }

                }
            });

        }
    }

}

//zdd 此处用意是在前台页面加载权利人   提高后台SQL相应相率   但是经过测试未必有慢的情况，并且导致权利人查询出现问题  所以暂时去掉
//为表格添加权利人列数据
function qlrForTable(grid_selector, bdclxdm, zdtzm) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        getQlrByDjid(data.ID, $(grid_selector), rowIds[index], bdclxdm, zdtzm);
    })
}

//获取权利人
function getQlrByDjid(djid, table, rowid, bdclxdm, zdtzm) {
    if (djid == null || djid == "undefined")
        djid = "";
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + bdclxdm + "&zdtzm=" + zdtzm,
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

//过渡房产证数据
function gdfcInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
    $('#gdfcsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fczh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cqzhjcgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fwzlgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#qlrgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
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
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'QLID'},
        colNames: ["不动产单元号", '房产证号', '坐落', '权利人', '不动产单元状态', '不动产类型', 'GDPROID', 'DJID', 'QLID'],
        colModel: [
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '12%',
                sortable: false,
            },
            {
                name: 'FCZH',
                index: 'FCZH',
                width: '11%',
                sortable: false,
            },
            {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
            {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false},
            {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
            {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
            {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            dateForTable(grid_selector);
            $("#gdfcsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.QLID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        //混合查封中数据来源的标志
                        cm["TYPE"] = "fcz";
                        $mulData.push(cm);
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            })
            //赋值数量
            $("#gdfcsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.QLID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    //混合查封中数据来源的标志
                    cm["TYPE"] = "fcz";
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#gdfcsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//过度土地证数据
function gdtdInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
    $('#gdtdsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#tdzh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cqzhjcgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#tdzlgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#qlrgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
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
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'QLID'},
        colNames: ["不动产单元号", '土地证号', '坐落', '权利人', '不动产单元状态', '不动产类型', 'GDPROID', 'DJID', 'QLID'],
        colModel: [
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '12%',
                sortable: false,
            },
            {
                name: 'TDZH',
                index: 'TDZH',
                width: '11%',
                sortable: false,
            },
            {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
            {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false},
            {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
            {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
            {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            dateForTable(grid_selector);
            $("#gdtdsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.QLID == e) {
                    var index = $.inArray(e, $mulRowid);
                    if (status && index < 0) {
                        //混合查封中数据来源的标志
                        cm["TYPE"] = "tdz";
                        $mulData.push(cm);
                        $mulRowid.push(e);
                    } else if (!status && index >= 0) {
                        $mulData.remove(index);
                        $mulRowid.remove(index);
                    }
                }
            })
            //赋值数量
            $("#gdtdsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.QLID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    //混合查封中数据来源的标志
                    cm["TYPE"] = "tdz";
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#gdtdsjMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function gdcfInitTable(tableKey) {
    var grid_selector = "#" + tableKey + "-grid-table";
    var pager_selector = "#" + tableKey + "-grid-pager";
    $('#gdcfsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#cfwhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#fwzlgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#tdzlgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#bdcdyhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $('#yqzhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#" + tableKey + "_search_btn").click();
        }
    });
    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    });
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });

    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'QLID'},
        colNames: ['产权证号', "不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间', '不动产类型', 'GDPROID', 'QLID'],
        colModel: [
            {name: 'CQZH', index: 'CQZH', width: '6%', sortable: false},
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '15%',
                sortable: false

            },
            {
                name: 'ZL',
                index: 'ZL',
                width: '11%',
                sortable: false
            },
            {name: 'CFLX', index: 'CFLX', width: '6%', sortable: false},
            {name: 'CFJG', index: 'CFJG', width: '6%', sortable: false},
            {
                name: 'CFWH',
                index: 'CFWH',
                width: '11%',
                sortable: false

            },
            {name: 'CFSQR', index: 'CFSQR', width: '6%', sortable: false},
            {name: 'BCFQLR', index: 'BCFQLR', width: '6%', sortable: false},
            {name: 'CFKSQX', index: 'CFKSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CFJSQX', index: 'CFJSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
            {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
            {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20, 50, 100],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: true,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                // $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }

            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getGdCfCqzh(data.QLID, "", data.GDPROID, $(grid_selector), null, null);
            })
            for (var i = 0; i <= $mulRowid.length; i++) {
                $(grid_selector).jqGrid('setSelection', $mulRowid[i]);
            }
            //赋值数量
            $("#gdCfMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");

            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        onSelectAll: function (aRowids, status) {
            var $myGrid = $(this);
            //aRowids.forEach(function(e){
            $.each(aRowids, function (i, e) {
                var cm = $myGrid.jqGrid('getRowData', e);
                if (cm.QLID == e) {
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
            $("#gdCfMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
        },
        onSelectRow: function (rowid, status) {
            var $myGrid = $(this);
            var cm = $myGrid.jqGrid('getRowData', rowid);
            if (cm.QLID == rowid) {
                var index = $.inArray(rowid, $mulRowid);
                if (status && index < 0) {
                    $mulData.push(cm);
                    $mulRowid.push(rowid);
                } else if (!status && index >= 0) {
                    $mulData.remove(index);
                    $mulRowid.remove(index);
                }
                //赋值数量
                $("#gdCfMulXx").html("<span>已选择(" + $mulRowid.length + ")</span>");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

function getGdCfCqzh(rowid, bdcdyid, gdproid, table, bdcdyh, proid) {
    getCzxx(rowid, table, bdcdyh, proid);
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getGdCfCqzh?bdcdyid=" + bdcdyid + "&gdproid=" + gdproid + "&qlid=" + rowid,
        success: function (result) {
            table.setCell(rowid, "CQZH", result.cqzh);
        }
    });
}

function gdsjEditXm(qlid, bdcdyh, bdclx, zl, gdproid, djid) {
    $("#qlid").val(qlid);
    $("#bdcdyh").val(bdcdyh);
    $("#bdclx").val(bdclx);
    $("#gdproid").val(gdproid);
    $("#xmmc").val(zl);
    $("#djlx").val(djlx);
    $("#workFlowDefId").val(workFlowDefId);
    $("#djId").val(djid);
    $("#sqlx").val(sqlxmc);
    $("#proids").val(proid);

    $.ajax({
        url: bdcdjUrl + '/bdcJgSjgl/isCancelAll?bdclx=' + bdclx,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data.hasOwnProperty("result")) {
                if (data.result) {
                    createXm(gdproid, bdcdyh, qlid, bdclx, djid);
                } else if (!data.result && data.msg != null && data.msg != "") {
                    setTimeout($.unblockUI, 10);
                    if (data.checkModel == "ALERT")
                        tipInfo(data.msg);
                    else if (data.checkModel == "CONFIRM") {
                        showConfirmDialog("提示信息", data.msg, "confirmCreateXm", "'" + gdproid + "','" + bdcdyh + "','','" + fwid + "','" + tdid + "','" + qlid + "','" + ppzt + "','" + gdid + "','" + grid_selector + "','" + djId + "','" + ppDyh + "'", "", "");
                    }
                } else {
                    setTimeout($.unblockUI, 10);
                    tipInfo("创建项目失败！")
                }
            } else {
                setTimeout($.unblockUI, 10);
                var str = "";
                $.each(data.resultList, function (index, obj) {
                    str += obj + "\n";
                })
                window.open(reportUrl + "/ReportServer?reportlet=edit%2Fgd_fw.cpt&op=write&fwid=" + $("#fwid").val());
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("创建项目失败！")
        }
    });
}

var createXm = function (gdproid, bdcdyh, qlid, bdclx, djid) {
    $.ajax({
        url: bdcdjUrl + '/bdcJgSjgl/createCsdj?bdclx=' + bdclx,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                setTimeout($.unblockUI, 10);
                window.parent.hideModel();
                window.parent.resourceRefresh();
            } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                setTimeout($.unblockUI, 10);
                window.parent.hideModel();
                window.parent.resourceRefresh();
            } else {
                setTimeout($.unblockUI, 10);
                tipInfo(data.msg);
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
        }
    });

}
var checkMulXm = function (gdproids, qlids) {
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl + '/bdcJgSjgl/checkMulXm',
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            if (data && data != 'undefined')
                hadleTipData(data);
            if (valite.length == 0)
                gdDisplayTip(gdproids, qlids, data);
            else {
                $("#bdcdyTipPop").show();
                $("#modal-backdrop").show();
                mulGdDisplayTip();
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);
}
var createMulXm = function (gdproids, qlids, bdclx) {
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl + '/bdcSjgl/updateGdPpzt',
        type: 'post',
        dataType: 'json',
        data: {gdproids: gdproids, ppzt: 3, bdclx: bdclx},
        success: function (matchData) {
            $.ajax({
                url: bdcdjUrl + '/bdcJgSjgl/createCsdj?lx=' + bdclx + "&gdFwWay=cg",
                type: 'post',
                dataType: 'json',
                data: $("#form").serialize(),
                success: function (data) {
                    if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                        setTimeout($.unblockUI, 10);
                        openWin(portalUrl + '/taskHandle?taskid=' + data.taskid, '_task');
                    } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                        setTimeout($.unblockUI, 10);
                        //alert("创建项目成功!");
                    } else {
                        setTimeout($.unblockUI, 10);
                        //alert(data.msg);
                    }
                    //清空列表
                    $("#gdproids").val("");
                    $("#qlids").val("");
                    $mulData.splice(0, $mulData.length);
                    $mulRowid.splice(0, $mulRowid.length);
                    setTimeout($.unblockUI, 10);
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                },
                error: function (data) {
                }
            });
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);
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
                if (item.checkCode == "194") {
                    $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openBdcYcfxx(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                } else {
                    $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
                }
            } else if (item.checkModel == "alert") {
                alertSize++;
                if (notShowCk.indexOf(item.checkCode) < 0) {
                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                } else {
                    $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                }
            }
        })
        $("#tipPop").show();
        $("#modal-backdrop").show();
    }
    if (alertSize == 0 && confirmSize == 0) {
        djsjInitVoFromOldData(djIds, proid, bdcXmRelList, bdcdyhs);
    } else if (alertSize == 0 && confirmSize > 0) {
        if ($("button[name='hlBtn']").length == 0) {
            $("#tipFooter").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
        }
        $("button[name='hlBtn']").click(function () {
            $('#tipPop').hide();
            djsjInitVoFromOldData(djIds, proid, bdcXmRelList, bdcdyhs);
        })
    }
}

function ywsjDisplayTip(data, id, bdcdyid, bdcdyh, bdcXmRelList, proid) {
    var alertSize = 0;
    var confirmSize = 0;
    if (data.length > 0) {
        $("#csdjAlertInfo,#csdjConfirmInfo").html("");
        //去掉遮罩
        setTimeout($.unblockUI, 10);
        var alertCount = 0;
        $.each(data, function (i, item) {
            if (item.checkModel == "alert") {
                alertCount++;
            }
        })
        var islw = false;
        $.each(data, function (i, item) {
            if (item.checkModel == "confirm") {
                confirmSize++;
                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><!--<span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span>--><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
            } else if (item.checkModel == "confirmAndCreate") {
                confirmSize++;
                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
            } else if (item.checkModel == "alert") {
                alertSize++;
                if (isNotBlank(item.wiid)) {
                    islw = true;
                    confirmCreateLw(item, bdcdjUrl, sflw, "djsjMultiselectList");
                } else {
                    if (notShowCk.indexOf(item.checkCode) < 0) {
                        $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                    } else {
                        $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                    }
                }
            }
        });
        if (!islw) {
            $("#tipPop").show();
            $("#modal-backdrop").show();
        }
    }
    if (alertSize == 0 && confirmSize == 0) {
        ywsjInitVoFromOldData(proid, bdcXmRelList);
    } else if (alertSize == 0 && confirmSize > 0) {
        if ($("button[name='hlBtn']").length == 0) {
            $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
        }
        $("button[name='hlBtn']").click(function () {
            $('#tipPop').hide();
            ywsjInitVoFromOldData(proid, bdcXmRelList);
        })
    }
}


function gdDisplayTip(gdproids, qlids, data) {
    var alertSize = 0;
    var confirmSize = 0;
    if (data.length > 0) {
        $("#csdjAlertInfo,#csdjConfirmInfo").html("");
        //去掉遮罩
        setTimeout($.unblockUI, 10);
        var alertCount = 0;
        $.each(data, function (i, item) {
            if (item.checkModel == "alert") {
                alertCount++;
            }
        })
        var islw = false;
        $.each(data, function (i, item) {
            if (item.checkModel == "confirm") {
                confirmSize++;
                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><!--<span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span>--><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
            } else if (item.checkModel == "confirmAndCreate") {
                confirmSize++;
                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')">查看</span>' + item.checkMsg + '</div>');
            } else if (item.checkModel == "alert") {
                alertSize++;
                if (isNotBlank(item.wiid)) {
                    islw = true;
                    confirmCreateLw(item, bdcdjUrl, sflw, "djsjMultiselectList");
                } else {
                    if (notShowCk.indexOf(item.checkCode) < 0) {
                        $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info[0] + '\')" >查看</span>' + item.checkMsg + '</div>');
                    } else {
                        $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                    }
                }
            }
        });
        if (!islw) {
            $("#tipPop").show();
            $("#modal-backdrop").show();
        }
    }
    if (alertSize == 0 && confirmSize == 0) {
        createMulXm(gdproids, qlids, $bdclx);
    } else if (alertSize == 0 && confirmSize > 0) {
        if ($("button[name='hlBtn']").length == 0) {
            $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
        }
        $("button[name='hlBtn']").click(function () {
            $('#tipPop').hide();
            createMulXm(gdproids, qlids, $bdclx);
        })
    }
}

function mulDisplayTip(type) {
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
            }
            $("#bdcdyInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="viewBdcdyTip(\'' + i + '\',\'' + altercount + '\')">查看</span>' + content + '</div>');
        } else {
            //没有值的清空
            valite.splice(i, 1);
        }
    }
    if (altercount == 0 && confirmcount == 0) {
        $("#bdcdyTipPop").hide();
        var proid = $("#proid").val();
        var bdcXmRelList = $("#bdcXmRelList").val();
        var djIds = djidsAll;
        if (type == "ywsj")
            ywsjInitVoFromOldData(proid, bdcXmRelList);
        else
            djsjInitVoFromOldData(djIds, proid, bdcXmRelList, "");
    } else if (altercount == 0 && confirmcount > 0) {
        if ($("button[name='hlBtn']").length == 0) {
            $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
        }

        $("button[name='hlBtn']").click(function () {
            $('#tipPop').hide();
            $("#bdcdyTipPop").hide();
            var proid = $("#proid").val();
            //var bdcXmRelList = $("#bdcXmRelList").val();
            var bdcXmRelList = bdcXmRelListAll;
            var djIds = djidsAll;
            if (type == "ywsj")
                ywsjInitVoFromOldData(proid, bdcXmRelList);
            else
                djsjInitVoFromOldData(djIds, proid, bdcXmRelList, "");
        })
    }
}

function mulGdDisplayTip() {
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
            }
            $("#bdcdyInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="viewBdcdyTip(\'' + i + '\',\'' + altercount + '\')">查看</span>' + content + '</div>');
        } else {
            //没有值的清空
            valite.splice(i, 1);
        }
    }
    if (altercount == 0 && confirmcount == 0) {
        $("#bdcdyTipPop").hide();
        var gdproids = $("#gdproids").val();
        var qlids = $("#qlids").val();
        var bdclx = $bdclx;
        createMulXm(gdproids, qlids, bdclx);
    } else if (altercount == 0 && confirmcount > 0) {
        if ($("button[name='hlBtn']").length == 0) {
            $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
        }

        $("button[name='hlBtn']").click(function () {
            $('#tipPop').hide();
            $("#bdcdyTipPop").hide();
            var gdproids = $("#gdproids").val();
            var qlids = $("#qlids").val();
            var bdclx = $bdclx;
            createMulXm(gdproids, qlids, bdclx);
        })
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
        $.each(confirmInfo, function (i, item) {
            var confirmInfoDom = '<div class="alert alert-warning" value=' + i + '><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="';
            if (item.function == null) {
                confirmInfoDom += 'openProjectInfo(\'' + item.info + '\')">';
            } else {
                confirmInfoDom += item.function + '">';
            }
            if (item.title == null) {
                confirmInfoDom += '查看';
            } else {
                confirmInfoDom += item.title;
            }
            confirmInfoDom += '</span>' + item.msg + '</div>';
            $("#bdcdyConfirmInfo").append(confirmInfoDom);
            // $("#bdcdyConfirmInfo").append('<div class="alert alert-warning" value=' + i + '><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.msg + '</div>');
        });
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
            if (item.title != null) {
                json["title"] = item.title;
            }
            if (item.function != null) {
                json["function"] = item.function;
            }
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

$(function () {
    var bdclx = "TD";
    if (bdclx != "" && bdclx != null) {
        $("#bdclxSelect option").each(function () {
            if ($(this).text() == bdclx) {
                $(this).attr('selected', 'selected');
            }
            $("#bdclxSelect").trigger("chosen:updated");
        });
        initBdclxZxByBdclx(bdclx);
    }
})

function changeBdclx() {
    var bdclx = $("#bdclxSelect").val();
    $("#bdclxZxSelect").html('<option value="">请选择...</option>');
    if (bdclx != "" && bdclx != null) {
        initBdclxZxByBdclx(bdclx)
    }
}

function initBdclxZxByBdclx(bdclx) {
    $("#bdclxZxSelect").html('<option value="">请选择...</option>');
    if (bdclx != "" && bdclx != null) {
        if (bdclx == 'TD') {
            var bdclxZx_html = '<option value="">请选择...</option>'
            bdclxZx_html += '<option value="ZD">宗地 </option>';
            bdclxZx_html += '<option value="QSZD">权属宗地</option>';
            bdclxZx_html += '<option value="CBZD">承包宗地</option>';
            $("#bdclxZxSelect").html(bdclxZx_html);
        } else if (bdclx == 'TDFW') {
            var bdclxZx_html = '<option value="">请选择...</option>'
            bdclxZx_html += '<option value="FWXMXX">房屋项目信息 </option>';
            bdclxZx_html += '<option value="LJZ">逻辑幢</option>';
            bdclxZx_html += '<option value="HS">户室</option>';
            $("#bdclxZxSelect").html(bdclxZx_html);
        } else if (bdclx == 'TDQT') {
            var bdclxZx_html = '<option value="">请选择...</option>'
            bdclxZx_html += '<option value="CBZD">承包宗地</option>';
            $("#bdclxZxSelect").html(bdclxZx_html);
        } else if (bdclx == 'TDSL') {
            var bdclxZx_html = '<option value="">请选择...</option>'
            bdclxZx_html += '<option value="LQ">林权 </option>';
            $("#bdclxZxSelect").html(bdclxZx_html);
        } else if (bdclx == 'HY') {
            var bdclxZx_html = '<option value="">请选择...</option>'
            bdclxZx_html += '<option value="ZH">宗海 </option>';
            $("#bdclxZxSelect").html(bdclxZx_html);
        }
    }
}

function show(c_Str) {
    if (document.all(c_Str).style.display == 'none') {
        document.all(c_Str).style.display = 'block';
    } else {
        document.all(c_Str).style.display = 'none';
    }
}

function changeJson(arrayData) {
    var type = arrayData[0].TYPE;
    $.each(arrayData, function () {
        if (type && type == "bdcdy") {
            this["ZL"] = this["TDZL"];
            this["QT"] = this["DJH"];
        } else if (type && type == "bdcqz") {
            this["QT"] = this["BDCQZH"];
            this["ID"] = this["PROID"];
        } else if (type && type == "fcz") {
            this["QT"] = this["FCZH"];
            this["ID"] = this["DJID"];
        } else if (type && type == "tdz") {
            this["QT"] = this["TDZH"];
            this["ID"] = this["DJID"];
        }
    });
    return arrayData;
}
