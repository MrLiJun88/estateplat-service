var checkboxSelect;
$(function () {
    //时间控件
    $('.date-picker').datepicker({
        autoclose: true,
        todayHighlight: true,
        language: 'zh-CN'
    }).next().on(ace.click_event, function () {
        $(this).prev().focus();
    });
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

    //可移动窗口
    //$("#draggable").draggable();
    //数据未匹配,跳转至匹配界面时默认直接搜索
    $(function () {
        var fw_search = $("#fw_search_qlr").val();
        if (fw_search !== null && fw_search !== undefined && fw_search !== "") {
            $("#fw_search").click();
        }
    });

    $(document).keydown(function (event) {
        if (event.keyCode == 13) { //绑定回车
        }
    });

    $("#djsjTab,#ywsjTab,#qlxxTab,#xzxxTab,#gdfcsjTab,#gdtdsjTab,#gdcfsjTab,#jcptsjTab").click(function () {
        if (this.id == "djsjTab") {
            $("#djsj").addClass("active");
            $("#ywsjHide").click();
            djsjInitTable();
        } else if (this.id == "ywsjTab") {
            $("#ywsj").addClass("active");
            $("#djsjHide").click();
            $("#fcsjHide").click();
            $("#tdsjHide").click();
            $("#jcptsjHide").click();
            ywsjInitTable();
        } else if (this.id == "qlxxTab") {
            $("#qlxx").addClass("active");
            $("#qlxxHide").click();
            qlxxInitTable();
        } else if (this.id == "xzxxTab") {
            $("#xzxx").addClass("active");
            $("#xzxxHide").click();
            xzxxInitTable();
        } else if (this.id == "gdfcsjTab") {
            $("#gdfcsj").addClass("active");
            $("#ywsjHide").click();
            gdfcInitTable();
        } else if (this.id == "gdtdsjTab") {
            $("#gdtdsj").addClass("active");
            $("#ywsjHide").click();
            gdtdInitTable();
        } else if (this.id == "gdcfsjTab") {
            $("#gdcfsj").addClass("active");
            $("#qlxxHide").click();
            gdcfInitTable();
        } else if (this.id == "jcptsjTab") {
            $("#jcptsj").addClass("active");
            $("#ywsjHide").click();
            jcptsjInitTable();
        }
    })


    //搜索事件
    $("#djsj_search_btn").click(function () {
        var bdclxZx = $("#bdclxZxSelect").val();
        if (showOptimize == "true") {
            var qlr = $("#qlrdjsj").val();
            var tdzl = $("#tdzldjsj").val();
            var bdcdyh = $("#bdcdyhdjsj").val();
            var fwbm = $("#fwbhdjsj").val();
            var djh = $("#djhdjsj").val();
            var bdclx = $("#bdclxSelect").val();
            var htbh = $("#htbhdjsj").val();
            var exactQuery = "false";
            if ((qlr == "" || qlr == null || qlr == undefined)
                && (tdzl == "" || tdzl == null || tdzl == undefined)
                && (fwbm == "" || fwbm == null || fwbm == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (djh == "" || djh == null || djh == undefined)
                && (bdclx == "" || bdclx == null || bdclx == undefined)
                && (htbh == "" || htbh == null || htbh == undefined)) {
                tipInfo("请输入权利人/坐落/不动产单元号/房屋代码");
            } else {
                var djsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&bdclxZx=" + bdclxZx;
                tableReload("djsj-grid-table", djsjUrl, {
                    qlr: qlr,
                    tdzl: tdzl,
                    fwbm: fwbm,
                    bdcdyh: bdcdyh,
                    djh: djh,
                    bdclx: bdclx,
                    htbh: htbh,
                    exactQuery: exactQuery
                });
            }
        } else {
            var dcxc = $("#djsj_search").val();
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入权利人/坐落/不动产单元号/房屋编号");
            } else {
                var djsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getDjsjBdcdyPagesJson?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&bdclxZx=" + bdclxZx;
                tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
            }
        }
    })
    $("#ywsj_search_btn").click(function () {
        var zslx = $("#zslxSelect").val();
        var fzqssj = $("#fzqssj").val();
        var fzjssj = $("#fzjssj").val();
        var dcxc = $("#ywsj_search").val();
        if (showOptimize == "true") {
            var bdcqzh = $("#bdcqzh").val();
            var qlr = $("#qlrywsj").val();
            var zl = $("#zlywsj").val();
            var bdcdyh = $("#bdcdyhywsj").val();
            var cqzhjc = $("#cqzhjcywsj").val();
            var fwbm = $("#fwbmywsj").val();
            var dyr = $("#dyrywsj").val();
            var exactQuery = "false";
            if ((bdcqzh == "" || bdcqzh == null || bdcqzh == undefined)
                && (qlr == "" || qlr == null || qlr == undefined)
                && (zl == "" || zl == null || zl == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)
                && (fwbm == "" || fwbm == null || fwbm == undefined)
                && (dyr == "" || dyr == null || dyr == undefined)) {
                $.Prompt("请输入不动产权证号/产权证号简称/坐落/权利人/不动产单元号/房屋代码", 1500);
            } else {
                var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm + "&zstype=" + zslx + "&fzqssj=" + fzqssj + "&fzjssj=" + fzjssj;
                tableReload("ywsj-grid-table", ywsjUrl, {
                    bdcqzh: bdcqzh,
                    fwbm: fwbm,
                    qlr: qlr,
                    zl: zl,
                    bdcdyh: bdcdyh,
                    cqzhjc: cqzhjc,
                    dyr: dyr,
                    exactQuery: exactQuery
                });
            }
        } else {
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入不动产权证号/权利人/坐落/不动产单元号");
            } else {
                var ywsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getBdczsListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm + "&zstype=" + zslx + "&fzqssj=" + fzqssj + "&fzjssj=" + fzjssj;
                tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
            }
        }
    })
    $("#jcptsj_search_btn").click(function () {
        var businessNo = $("#businessNo").val();
        if (businessNo == '') {
            tipInfo("请输入业务号进行查询！");
            return false;
        }
        var Url = bdcdjUrl + "/selectBdcdyQlShow/getBusinessData?businessNo=" + businessNo + "&proid=" + proid;
        tableReload("jcptsj-grid-table", Url, {});
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
                tipInfo("请输入权利人/坐落/房产证号/不动产单元号/产权证号简称");
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
            var cqzhjc = $("#cqzhjctd").val();
            var exactQuery = "false";
            if ((qlr == "" || qlr == null || qlr == undefined)
                && (tdzh == "" || tdzh == null || tdzh == undefined)
                && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
                && (tdzl == "" || tdzl == null || tdzl == undefined)
                && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)) {
                tipInfo("请输入不动产单元号/产权证号简称/权利人/坐落/土地证号");
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
    $("#qlxx_search_btn").click(function () {
        if (showOptimize == "true") {
            var bdcdyh = $("#bdcdyhqlxx").val();
            var zl = $("#zlqlxx").val();
            var bzxr = $("#bzxrqlxx").val();
            var bdcqzh = $("#bdcqzhqlxx").val();
            var fwbm = $("#fwbmqlxx").val();
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
                tipInfo("请输入被查封权利人/坐落/不动产单元号/不动产权证/查封文号/产权证号简称/房屋代码");
            } else {
                var qlxxUrl = bdcdjUrl + "/selectBdcdyQlShow/getQlxxListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("qlxx-grid-table", qlxxUrl, {
                    zl: zl,
                    bzxr: bzxr,
                    fwbm: fwbm,
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

    $("#xzxx_search_btn").click(function () {
        var bdcdyh = $("#bdcdyhxzxx").val();
        var zl = $("#zlxzxx").val();
        var bzxr = $("#bzxrxzxx").val();
        var bdcqzh = $("#bdcqzhxzxx").val();
        var fwbm = $("#fwbmxzxx").val();
        var qlr = $("#cqqlr").val();
        var cqzhjc = $("#cqzhjcxzxx").val();
        var exactQuery = "false";
        if ((zl == "" || zl == null || zl == undefined)
            && (bzxr == "" || bzxr == null || bzxr == undefined)
            && (bdcdyh == "" || bdcdyh == null || bdcdyh == undefined)
            && (bdcqzh == "" || bdcqzh == null || bdcqzh == undefined)
            && (cqzhjc == "" || cqzhjc == null || cqzhjc == undefined)
            && (fwbm == "" || fwbm == null || fwbm == undefined)
            && (qlr == "" || qlr == null || qlr == undefined)) {
            tipInfo("请输入坐落/不动产单元号/不动产权证/产权证号简称/房屋代码");
        } else {
            var xzxxUrl = bdcdjUrl + "/selectBdcdyQlShow/getXzqlxxListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
            tableReload("xzxx-grid-table", xzxxUrl, {
                zl: zl,
                bzxr: bzxr,
                fwbm: fwbm,
                bdcdyh: bdcdyh,
                bdcqzh: bdcqzh,
                cqzhjc: cqzhjc,
                qlr: qlr,
                exactQuery: exactQuery
            });
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
                var gdcfsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdcfListByPage?proid=" + proid + "&searchBdcdyh=" + searchBdcdyh;
                tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: $("#gdcfsj_search").val()});
            }
        }
    })

    /*高级按钮点击事件 begin*/
    $("#djsjShow,#ywsjShow,#gdfcsjShow,#gdtdsjShow").click(function () {
        if (this.id == "ywsjShow") {
            $("#ywsjSearchPop").show();

        } else if (this.id == "djsjShow") {
            $("#djsjSearchPop").show();

        } else if (this.id == "gdfcsjShow") {
            $("#fcsjSearchPop").show();

        } else if (this.id == "gdtdsjShow") {
            $("#tdsjSearchPop").show();
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
    //土地高级查询的搜索按钮事件
    $("#tdsjGjSearchBtn").click(function () {
        var gdtdsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdtdzListByPage?proid=" + $("#proid").val() + "&" + $("#tdsjSearchForm").serialize();
        tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: ""});
        $("#tdsjSearchPop").hide();
        $("#tdsjSearchForm")[0].reset();
    })

    //房产高级查询的搜索按钮事件
    $("#fcsjGjSearchBtn").click(function () {
        var gdfcsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdfczListByPage?proid=" + $("#proid").val() + "&" + $("#fcsjSearchForm").serialize();
        tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: ""});
        $("#fcsjSearchPop").hide();
        $("#fcsjSearchForm")[0].reset();
    })

    $("#cfsjGjSearchBtn").click(function () {
        var gdcfsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdcfListByPage?proid=" + proid + "&" + $("#cfsjSearchForm").serialize();
        tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: ""});
    })

    $("#djsjHide,#ywsjHide,#fcsjHide,#tdsjHide,#tipHide,#tipCloseBtn,#jcptsjHide").click(function () {
        if (this.id == "djsjHide") {
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        } else if (this.id == "ywsjHide") {
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        } else if (this.id == "tipHide" || this.id == "tipCloseBtn") {
            $("#modal-backdrop").hide();
            $("#tipPop").hide();
        } else if (this.id == "fcsjHide") {
            $("#fcsjSearchPop").hide();
            $("#fcsjSearchForm")[0].reset();
        } else if (this.id == "tdsjHide") {
            $("#tdsjSearchPop").hide();
            $("#tdsjSearchForm")[0].reset();
        } else if (this.id == "jcptsjHide") {
            $("#jcptsjSearchPop").hide();
            $("#jcptsjSearchForm")[0].reset();
        }
    });
    $(".djsjSearchPop-modal, .ywsjSearchPop-modal, .fcsjSearchPop-modal, .tdsjSearchPop-modal").draggable({
        opacity: 0.7,
        handle: 'div.modal-header'
    });
    //默认初始化表格
    if (bdcdyly == '0'  || bdcdyly == '5') {
         $("#djsjTab").click();
    } else if (bdcdyly == '1' ||bdcdyly == '2' ||  bdcdyly == '4' || bdcdyly == '6' || bdcdyly == '7' || bdcdyly == '8' || bdcdyly == '10') {
        $("#ywsjTab").click();
    } else if (bdcdyly == '3') {
        $("#qlxxTab").click();
    } else if (bdcdyly == '11') {
        $("#xzxxTab").click();
    }

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

//地籍数据
function djsjInitTable() {
    var grid_selector = "#djsj-grid-table";
    var pager_selector = "#djsj-grid-pager";

    //绑定回车键
    $('#djsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#qlrdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#tdzldjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#bdcdyhdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#fwbhdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#djhdjsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#djsj_search_btn").click();
        }
    });
    $('#htbhdjsj').keydown(function (event) {
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
        datatype: 'local',
        height: 'auto',
        jsonReader: {id: 'ID'},
        colNames: ['地籍号', '不动产单元号', '房屋代码', '坐落', '权利人', '不动产类型', '不动产单元状态', '交易状态', 'bdcdylx', 'ID', "地籍号"],
        colModel: [
            {
                name: 'DJH',
                index: 'DJH',
                width: '13%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else {
                        cell = '';
                    }
                    return cell;
                }
            },
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '15%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }
            },
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'TDZL', index: 'TDZL', width: '15%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
            {
                name: 'BDCLX',
                index: 'BDCLX',
                width: '6%',
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
                    return value;
                }
            },
            /*{name:'PPTZS', index:'PPTZS', width:'10%', sortable:false , formatter:function (cellvalue, options, rowObject){
                cell='<div title="匹配通知书" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="getPptzs(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' ;
                return cell;
            }},*/
            {name: 'QLZT', index: 'QLZT', width: '15%', sortable: false},
            {name: 'JYZT', index: 'JYZT', width: '10%', sortable: false},
            {name: 'BDCDYLX', index: 'BDCDYLX', width: '0%', hidden: true},
            {name: 'ID', index: 'ID', width: '10%', sortable: false, hidden: true},
            {name: 'DJH', index: 'DJH', width: '5%', sortable: false, hidden: true}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        rownumbers: true,
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
        }/*,
            ondblClickRow:function (rowid) {
                var rowData = $(grid_selector).getRowData(rowid);
                if(rowData!=null)
                    djsjEditXm(rowid,rowData.BDCLX,rowData.BDCDYH);
            }*/,
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//权利信息
function qlxxInitTable() {
    var grid_selector = "#qlxx-grid-table";
    var pager_selector = "#qlxx-grid-pager";
    $('#qlxx_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#bdcqzhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#cfwhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#zlqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#bzxrqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#cqzhjcqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#bdcdyhqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
        }
    });
    $('#fwbmqlxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#qlxx_search_btn").click();
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
        //colNames:["不动产单元号", '坐落', '类型', '查封机关','查封申请人','被查封申请人'],
        colNames: ["产权证号", "PROID", "BDCDYID", "不动产单元号", '房屋代码', '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间', '操作'],
        colModel: [
            {name: 'CQZH', index: 'CQZH', width: '10%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '10%', hidden: true, sortable: false},
            {name: 'BDCDYID', index: 'BDCDYID', width: '10%', hidden: true, sortable: false},
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '15%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'\',\'' + rowObject.BDCLX + '\',\'CF\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }
            },
            {name: 'FWBM', index: 'FWBM', width: '5%', sortable: false},
            {
                name: 'ZL',
                index: 'ZL',
                width: '11%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'\',\'' + rowObject.BDCLX + '\',\'CF\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }
            },
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
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
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
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getGdCfCqzh(data.PROID, data.BDCDYID, "", $(grid_selector), data.BDCDYH, data.PROID);
                getSdStatus(data.PROID, data.CQZH);
            });
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//限制信息
function xzxxInitTable() {
    var grid_selector = "#xzxx-grid-table";
    var pager_selector = "#xzxx-grid-pager";
    $('#xzxx_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
        }
    });
    $('#bdcqzhxzxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
        }
    });
    $('#zlxzxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
        }
    });
    $('#bzxrxzxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
        }
    });
    $('#cqzhjcxzxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
        }
    });
    $('#bdcdyhxzxx').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#xzxx_search_btn").click();
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
        //colNames:["不动产单元号", '坐落', '类型', '查封机关','查封申请人','被查封申请人'],
        colNames: ["PROID", "BDCDYID", "不动产单元号", '坐落', '限制类型', '来文单位', '限制原因', '限制开始时间', '限制结束时间'],
        colModel: [
            {name: 'PROID', index: 'PROID', width: '10%', hidden: true, sortable: false},
            {name: 'BDCDYID', index: 'BDCDYID', width: '10%', hidden: true, sortable: false},
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '15%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'\',\'' + rowObject.BDCLX + '\',\'CF\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }
            },
            {
                name: 'ZL',
                index: 'ZL',
                width: '11%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:ywsjEditXm(\'' + rowObject.PROID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCDYID + '\',\'\',\'' + rowObject.BDCLX + '\',\'CF\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'XZTYPE', index: 'XZTYPE', width: '6%', sortable: false},
            {name: 'SDJG', index: 'SDJG', width: '6%', sortable: false},
            {name: 'XZYY', index: 'XZYY', width: '6%', sortable: false},
            {name: 'XZKSSJ', index: 'XZKSSJ', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'XZJSSJ', index: 'XZJSSJ', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
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
            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

//过渡查封信息
function gdcfInitTable() {
    var grid_selector = "#gdcfsj-grid-table";
    var pager_selector = "#gdcfsj-grid-pager";
    $('#gdcfsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
        }
    });
    $('#cfwhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
        }
    });
    $('#fwzlgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
        }
    });
    $('#tdzlgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
        }
    });
    $('#bdcdyhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
        }
    });
    $('#yqzhgdcf').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdcfsj_search_btn").click();
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
        //colNames:["不动产单元号", '坐落', '类型', '查封机关','查封申请人','被查封申请人'],
        colNames: ['产权证号', "BDCDYID", "不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间', '不动产类型', 'GDPROID', 'QLID'],
        colModel: [
            {name: 'CQZH', index: 'CQZH', width: '6%', sortable: false},
            {name: 'BDCDYID', index: 'BDCDYID', width: '6%', hidden: true, sortable: false},
            {
                name: 'BDCDYH',
                index: 'BDCDYH',
                width: '15%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '') {
                        cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                        cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    } else
                        cell = '';
                    return cell;
                }
            },
            {
                name: 'ZL',
                index: 'ZL',
                width: '11%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'CFLX', index: 'CFLX', width: '6%', sortable: false},
            {name: 'CFJG', index: 'CFJG', width: '6%', sortable: false},
            {
                name: 'CFWH',
                index: 'CFWH',
                width: '11%',
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    if (cellvalue != null && cellvalue != '')
                        cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                    else
                        cell = '';
                    return cell;
                }
            },
            {name: 'CFSQR', index: 'CFSQR', width: '6%', sortable: false},
            {name: 'BCFQLR', index: 'BCFQLR', width: '6%', sortable: false},
            {name: 'CFKSQX', index: 'CFKSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'CFJSQX', index: 'CFJSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
            {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
            {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
            /*{name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},*/
            {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true}
            /*{name: 'BDCID', index: 'BDCID', width: '6%', sortable: false, hidden: true}*/
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
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
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                // getGdCfQlrmc(data.QLID,data.GDPROID,data.QLID, $(grid_selector));
                getGdCfCqzh(data.QLID, "", data.GDPROID, $(grid_selector), null, null);
                getSdStatus(data.QLID, data.CQZH);
            });
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}


function getGdCfCqzh(rowid, bdcdyid, gdproid, table, bdcdyh, proid) {
    if (proid != null) {
        $.ajax({
            type: "GET",
            url: bdcdjUrl + "/selectBdcdyQlShow/getdateByProid?proid=" + proid,
            success: function (result) {
                getCzxx(rowid, table, result.bdcdyh, proid);
            }
        });
    }
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getGdCfCqzh?bdcdyid=" + bdcdyid + "&gdproid=" + rowid + "&qlid=" + rowid,
        success: function (result) {
            debugger;
            table.setCell(rowid, "CQZH", result.cqzh);
        }
    });
}

//业务数据
function ywsjInitTable() {
    var grid_selector = "#ywsj-grid-table";
    var pager_selector = "#ywsj-grid-pager";
    $('#ywsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#bdcqzh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#cqzhjcywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#zlywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#qlrywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#bdcdyhywsj').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#ywsj_search_btn").click();
        }
    });
    $('#fwbmywsj').keydown(function (event) {
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

    // if ((qllx == "18" && sqlxdm != "1001" && sqlxdm != "1019" && sqlxdm != "9999905" && sqlxdm != "9979902" && sqlxdm != "9999910") || sqlxdm == "9940002")
    //
    // if () {}
    jQuery(grid_selector).jqGrid({
        datatype: "local",
        height: 'auto',
        jsonReader: {id: 'PROID'},
        colNames: ['不动产单元号', '不动产权证号', '房屋代码', '坐落', '权利人', '不动产类型', '不动产单元状态', '操作', 'PROID', '权利状态', '交易状态', "匹配状态"],
        colModel: [
            {
                name: 'BDCDYH',
                index: '',
                width: '12%',
                sortable: false
            },
            {
                name: 'BDCQZH',
                index: '',
                width: '11%',
                sortable: false
            },
            {name: 'FWBM', index: 'FWBM', width: '10%', sortable: false},
            {name: 'ZL', index: '', width: '10%', sortable: false},
            {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
            {name: 'BDCLX', index: '', width: '6%', sortable: false, hidden: true},
            {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false, hidden: true},
            {name: 'CZ', index: 'CZ', width: '5%', sortable: false},
            {name: 'PROID', index: 'PROID', width: '6%', sortable: false, hidden: true},
            {name: 'QLZT', index: 'QLZT', width: '5%', sortable: false},
            {name: 'JYZT', index: 'JYZT', width: '10%', sortable: false},
            {name: 'PPZT', index: 'PPZT', width: '5%', sortable: false}
        ],
        viewrecords: true,
        rowNum: 7,
        rowList: [7, 15, 20],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        rownumbers: true,
        loadComplete: function () {
            if ((qllx == "18" && sqlxdm != "1001" && sqlxdm != "1019" && sqlxdm != "9999905" && sqlxdm != "9979902" && sqlxdm != "9999910") || sqlxdm == "9940002") {
                $("#ywsj-grid-table").jqGrid('setLabel', 'BDCQZH', '不动产登记证明号');
                $("#ywsj-grid-table").jqGrid('setLabel', 'QLR', '抵押权人');
            }
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
            var url = $(grid_selector).getGridParam("url");
            $.each(jqData, function (index, data) {
                getQlrmcAndZl(data.PROID, $(grid_selector), data.PROID);
                getSdStatus(data.PROID, data.BDCQZH);
                getbdcdyZt(data.PROID, null, data.BDCLX, $(grid_selector), data.PROID);
                getbdcdyJyZt(data.FWBM, $(grid_selector), data.PROID);
                getbdcdyPpZt(data.PROID, $(grid_selector), data.PROID);
            });

            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }

        },
        editurl: "", //nothing is saved
        caption: "",
        autowidth: true
    });
}

function jcptsjInitTable() {
    var grid_selector = "#jcptsj-grid-table";
    var pager_selector = "#jcptsj-grid-pager";
    $('#jcptsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#jcptsj_search_btn").click();
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
        jsonReader: {id: 'contractid'},
        colNames: ["业务id", "房屋地址", "卖方人员姓名", "买方人员姓名", '产证号', '创建', 'yproid', 'bdcdyh', 'bdcdyid', 'bdclx'],
        colModel: [
            {name: 'contractid', index: 'contractid', width: '10%', sortable: false, hidden: true},
            {name: 'location', index: 'location', width: '15%', sortable: false},
            {name: 'sellername', index: 'sellername', width: '6%', sortable: false},
            {name: 'buyername', index: 'buyername', width: '6%', sortable: false},
            {name: 'propertyid', index: 'propertyid', width: '20%', sortable: false},
            {
                name: 'view',
                index: '',
                width: '10%',
                align: "center",
                sortable: false,
                formatter: function (cellvalue, options, rowObject) {
                    return '<div style="margin-left:20px;"><div title="创建"  style="float:left;cursor:pointer; margin-left: 10px;" class="ui-pg-div ui-inline-edit" id="" onclick="jcptcj(\'' + rowObject.yproid + '\',\'' + rowObject.bdcdyh + '\',\'' + rowObject.bdcdyid + '\',\'' + rowObject.propertyid + '\',\'' + rowObject.bdclx + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-cog fa-lg black"></span>创建</div></div>'
                }
            },
            {name: 'yproid', index: 'yproid', width: '10%', sortable: false, hidden: true},
            {name: 'bdcdyh', index: 'bdcdyh', width: '10%', sortable: false, hidden: true},
            {name: 'bdcdyid', index: 'bdcdyid', width: '10%', sortable: false, hidden: true},
            {name: 'bdclx', index: 'bdclx', width: '10%', sortable: false, hidden: true}

        ],
        viewrecords: true,
        rowNum: 10,
        rowList: [10, 20, 30],
        pager: pager_selector,
        pagerpos: "left",
        altRows: false,
        multiboxonly: false,
        multiselect: false,
        rownumbers: true,
        loadComplete: function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == 7) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", "275px");
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            var url = $(grid_selector).getGridParam("url");
            if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                tipInfo("未搜索到该数据，请核实！");
            }
        },
        editurl: "",
        caption: "",
        autowidth: true
    });
}

function jcptcj(id, bdcdyh, bdcdyid, bdcqzh, bdclx, sqlx) {
    if (id != null) {
        ywsjEditXm(id, bdcdyh, bdcdyid, bdcqzh, bdclx, sqlx)
    }
}

function importYwbh() {
    $.blockUI({message: "正在导入数据,请稍等..."});
    var businessNo = $("#businessNo").val();
    $.ajax({
        type: "GET",
        async: false,
        url: bdcdjUrl + "/selectBdcdyQlShow/importYwbh?businessNo=" + businessNo + "&proid=" + proid,
        success: function (data) {
            if (data && data != 'undefined') {
                setTimeout($.unblockUI, 10);
                if (data.msg == 'success') {
                    tipInfo("导入成功");
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                } else {
                    tipInfo("导入失败！");
                }
            }
        }
    })
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
            var bdcdyCellVal = '<a href="javascript:ywsjEditXm(\'' + proid + '\',\'' + result.bdcdyh + '\',\'' + result.bdcdyid + '\',\'' + result.bdcqzh + '\',\'' + result.bdclx + '\')" title="' + result.bdcdyh + '" >' + result.bdcdyh + "</a>";
            table.setCell(rowid, "BDCDYH", bdcdyCellVal);
            var bdcqzhCellVal = '<a href="javascript:ywsjEditXm(\'' + proid + '\',\'' + result.bdcdyh + '\',\'' + result.bdcdyid + '\',\'' + result.bdcqzh + '\',\'' + result.bdclx + '\')" title="' + result.bdcqzh + '" >' + result.bdcqzh + "</a>";
            table.setCell(rowid, "BDCQZH", bdcqzhCellVal);
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
                if (result.bdcCxBdcdyZt.xszjgcdycs >= 1) {
                    cellVal += '<span class="label label-default">在建工程抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsygcs >= 1) {
                    cellVal += '<span class="label label-pink">预告</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsydyacs >= 1) {
                    cellVal += '<span class="label label-success">预抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyacs >= 1) {
                    cellVal += '<span class="label label-success">抵押</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsycfcs >= 1) {
                    cellVal += '<span class="label label-danger">预查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xscfcs >= 1) {
                    cellVal += '<span class="label label-danger">查封</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsyycs >= 1) {
                    cellVal += '<span class="label label-warning">异议</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xsdyics >= 1) {
                    cellVal += '<span class="label label-purple">地役</span><span> </span>';
                }
                if (result.bdcCxBdcdyZt.xssdcs >= 1) {
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
            if (result.existXscq == true) {
                cellVal += '<span class="label label-success">确权</span><span> </span>';
            }
            if (cellVal == "") {
                cellVal += '<span class="label label-success">未登记</span><span> </span>';
            }
            table.setCell(rowid, "QLZT", cellVal);
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

function getbdcdyPpZt(proid, table, rowid) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcpic/getPpzt?proid=" + proid,
        success: function (result) {
            var ppzt = '';
            ppzt = '<a href="javascript:showPpjg(\'' + proid + '\')"><span class="label label-success" value=' + ppzt + '>'+result+'</span></a>';
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

//过渡房产证数据
function gdfcInitTable() {
    var grid_selector = "#gdfcsj-grid-table";
    var pager_selector = "#gdfcsj-grid-pager";
    $('#gdfcsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
        }
    });
    $('#fczh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
        }
    });
    $('#cqzhjcgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
        }
    });
    $('#fwzlgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
        }
    });
    $('#qlrgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
        }
    });
    $('#bdcdyhgdfc').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdfcsj_search_btn").click();
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
    if (sqlxdm == "8009901" || sqlxdm == "8009902" || sqlxdm == "4009903" || sqlxdm == "4009901" || sqlxdm == "4009902" || sqlxdm == "8009903" || sqlxdm == "8009904") {
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'QLID'},
            colNames: ["不动产单元号", '房产证号', 'TFCZH', '坐落', '权利人', '不动产类型', 'GDPROID', 'DJID', 'QLID', 'ID'],
            colModel: [
                {
                    name: 'BDCDYH',
                    index: '',
                    width: '12%',
                    sortable: false,
                },
                {
                    name: 'FCZH',
                    index: 'FCZH',
                    width: '8%',
                    sortable: false,
                },
                {name: 'TFCZH', index: 'TFCZH', width: '5%', sortable: false, hidden: true},
                {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                {name: 'ID', index: 'ID', width: '6%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 7,
            rowList: [7, 15, 20],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
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
                dateForTable(grid_selector, "unPpfw");
                var jqData = $(grid_selector).jqGrid("getRowData");
                var url = $(grid_selector).getGridParam("url");
                if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                    tipInfo("未搜索到该数据，请核实！");
                }
                $.each(jqData, function (index, data) {
                    getSdStatus(data.QLID, data.FCZH);
                });
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    } else {
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'QLID'},
            colNames: ["不动产单元号", '房产证号', 'TFCZH', '坐落', '权利人', '匹配状态', '不动产单元状态', '不动产类型', 'GDPROID', 'DJID', 'QLID', 'ID'],
            colModel: [
                {
                    name: 'BDCDYH',
                    index: '',
                    width: '12%',
                    sortable: false,
                },
                {
                    name: 'FCZH',
                    index: 'FCZH',
                    width: '8%',
                    sortable: false,
                },
                {name: 'TFCZH', index: 'TFCZH', width: '5%', sortable: false, hidden: true},
                {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '7%', sortable: false},
                {name: 'PPZT', index: '', width: '4%', sortable: false},
                {name: 'BDCDYZT', index: 'BDCDYZT', width: '13%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                {name: 'ID', index: 'ID', width: '6%', sortable: false, hidden: true}
            ],
            viewrecords: true,
            rowNum: 7,
            rowList: [7, 15, 20],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
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
                dateForTable(grid_selector, "fc");
                var jqData = $(grid_selector).jqGrid("getRowData");
                var url = $(grid_selector).getGridParam("url");
                if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                    tipInfo("未搜索到该数据，请核实！");
                }
                $.each(jqData, function (index, data) {
                    getSdStatus(data.QLID, data.FCZH);
                    getbdcdyZt(null, data.DJID, data.BDCLX, $(grid_selector), data.ID);
                });
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
}

//过度土地证数据
function gdtdInitTable() {
    var grid_selector = "#gdtdsj-grid-table";
    var pager_selector = "#gdtdsj-grid-pager";
    $('#gdtdsj_search').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
        }
    });
    $('#tdzh').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
        }
    });
    $('#cqzhjctd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
        }
    });
    $('#tdzlgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
        }
    });
    $('#qlrgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
        }
    });
    $('#bdcdyhgdtd').keydown(function (event) {
        if (event.keyCode == 13) {
            $("#gdtdsj_search_btn").click();
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
    if (sqlxdm == "8009901" || sqlxdm == "8009902" || sqlxdm == "4009903" || sqlxdm == "4009901" || sqlxdm == "4009902" || sqlxdm == "8009903" || sqlxdm == "8009904") {
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'QLID'},
            colNames: ["不动产单元号", '土地证号', 'TTDZH', '坐落', '权利人', '不动产类型', 'GDPROID', 'DJID', 'QLID'],
            colModel: [
                {
                    name: 'BDCDYH',
                    index: '',
                    width: '12%',
                    sortable: false
                },
                {
                    name: 'TDZH',
                    index: 'TDZH',
                    width: '11%',
                    sortable: false
                },
                {name: 'TTDZH', index: 'TTDZH', width: '5%', sortable: false, hidden: true},
                {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
            ],
            viewrecords: true,
            rowNum: 7,
            rowList: [7, 15, 20],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
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
                dateForTable(grid_selector, "unPptd");
                var jqData = $(grid_selector).jqGrid("getRowData");
                var url = $(grid_selector).getGridParam("url");
                if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                    tipInfo("未搜索到该数据，请核实！");
                }
                $.each(jqData, function (index, data) {
                    getSdStatus(data.QLID, data.TDZH);
                });
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    } else {
        jQuery(grid_selector).jqGrid({
            datatype: "local",
            height: 'auto',
            jsonReader: {id: 'QLID'},
            colNames: ["不动产单元号", '土地证号', 'TTDZH', '坐落', '权利人', '匹配状态', '不动产单元状态', '不动产类型', 'GDPROID', 'DJID', 'QLID'],
            colModel: [
                {
                    name: 'BDCDYH',
                    index: '',
                    width: '12%',
                    sortable: false
                },
//                    {name: 'TBDCDYH', index: 'TBDCDYH', width: '5%', sortable: false,hidden:true},
                {
                    name: 'TDZH',
                    index: 'TDZH',
                    width: '11%',
                    sortable: false,
                },
                {name: 'TTDZH', index: 'TTDZH', width: '5%', sortable: false, hidden: true},
                {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
                {name: 'PPZT', index: '', width: '4%', sortable: false},
                {name: 'BDCDYZT', index: 'BDCDYZT', width: '10%', sortable: false},
                {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
            ],
            viewrecords: true,
            rowNum: 7,
            rowList: [7, 15, 20],
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: false,
            multiselect: false,
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
                dateForTable(grid_selector, "td");
                var jqData = $(grid_selector).jqGrid("getRowData");
                var url = $(grid_selector).getGridParam("url");
                if (url != "" && url != null && url != "undefined" && (jqData == null || jqData.length == 0)) {
                    tipInfo("未搜索到该数据，请核实！");
                }
                $.each(jqData, function (index, data) {
                    getSdStatus(data.QLID, data.TDZH);
                });
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }
}

function tableReload(table, Url, data) {
    var jqgrid = $("#" + table);
    jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
    jqgrid.trigger("reloadGrid");//重新加载JqGrid
}

//修改项目信息的函数
function djsjEditXm(id, bdclx, bdcdyh) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    $.blockUI({message: "请稍等……"});
    var options = {
        url: bdcdjUrl + '/wfProject/checkBdcXm',
        type: 'post',
        dataType: 'json',
        data: {proid: proid, bdcdyh: bdcdyh, djId: id},
        success: function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                var islw = false;
                $.each(data, function (i, item) {
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        confirmInfoDom = '<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="';
                        if (item.function == null) {
                            confirmInfoDom += 'openProjectInfo(\'' + item.checkPorids + '\')">';
                        } else {
                            confirmInfoDom += item.function + '">';
                        }
                        if (item.title == null) {
                            confirmInfoDom += '查看';
                        } else {
                            confirmInfoDom += item.title;
                        }
                        confirmInfoDom += '</span>' + item.checkMsg + '</div>';
                        $("#csdjConfirmInfo").append(confirmInfoDom);
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, bdcdjUrl, sflw, "djsjBdcdyList");
                        } else {
                            if (item.checkCode == '199') {
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            } else {
                                if (notShowCk.indexOf(item.checkCode) < 0) {
                                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                                } else {
                                    $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                                }
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
                djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
            } else if (alertSize == 0 && confirmSize > 0) {
                if ($("button[name='hlBtn']").length == 0) {
                    $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                }
                $("button[name='hlBtn']").click(function () {
                    $('#tipPop').hide();
                    djsjInitVoFromOldData(proid, id, bdclx, bdcdyh);
                })
            }

        },
        error: function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
        }
    };
    $.ajax(options);
}

function djsjInitVoFromOldData(proid, id, bdclx, bdcdyh) {
    var initurl = "";
    if (glbdcdy == "true") {
        initurl = bdcdjUrl + '/wfProject/glBdcdy?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh;
    } else {
        initurl = bdcdjUrl + '/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh;
    }
    $.ajax({
        type: 'post',
        url: initurl,
        success: function (data) {

            if (data == '成功') {
                $.ajax({
                    type: 'get',
                    async: true,
                    url: bdcdjUrl + '/wfProject/updateWorkFlow?proid=' + proid,
                    success: function (data) {

                    }
                });
                getBdcXtConfigFj();
                hideModelAndRefreshParant();
                //去掉遮罩
                setTimeout($.unblockUI, 10);
            } else {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            /*if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }*/
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });
}

//修改项目信息的函数
function ywsjEditXm(id, bdcdyh, bdcdyid, bdcqzh, bdclx, sqlx) {
    //遮罩
    $.blockUI({message: "请稍等……"});
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    var url = bdcdjUrl + "/wfProject/checkBdcXm";
    if (plChoseOne == "1") {
        url = bdcdjUrl + "/wfProject/checkBdcXmForDyChoseOne";
    }
    var options = {
        url: url,
        type: 'post',
        dataType: 'json',
        data: {proid: proid, yxmid: id, bdcdyh: bdcdyh, wiid: wiid},
        success: function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            var ysqlx = cfsqlx;
            if (ysqlx == 'JF') {
                sqlx = 'JF';
            }
            if (data.length > 0 && sqlx != 'CF') {
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
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
                        confirmInfoDom = '<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="';
                        if (item.function == null) {
                            confirmInfoDom += 'openProjectInfo(\'' + item.checkPorids + '\')">';
                        } else {
                            confirmInfoDom += item.function + '">';
                        }
                        if (item.title == null) {
                            confirmInfoDom += '查看';
                        } else {
                            confirmInfoDom += item.title;
                        }
                        confirmInfoDom += '</span>' + item.checkMsg + '</div>';
                        $("#csdjConfirmInfo").append(confirmInfoDom);
                    } else if (item.checkModel == "confirmAndCreate") {
                        confirmSize++;
                        $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + id + '\')">查看</span>' + item.checkMsg + '</div>');
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, bdcdjUrl, sflw, "djsjBdcdyList");
                        } else {
                            if (item.checkCode == '199') {
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            } else {
                                if (notShowCk.indexOf(item.checkCode) < 0) {
                                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                                } else {
                                    $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                                }
                            }
                        }
                    }
                })
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (!islw) {
                    $("button[name='hlBtn']").remove();
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx);
            } else if (alertSize == 0 && confirmSize > 0) {
                if ($("button[name='hlBtn']").length == 0) {
                    $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                }
                $("button[name='hlBtn']").click(function () {
                    $('#tipPop').hide();
                    ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx);
                })
            }
        },
        error: function (data) {
            //去掉遮罩
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    };
    $.ajax(options);
}

//修改项目信息的函数
function fsssEditXm(yproid, sqlx) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/bdcFwfsss/getFsssBdcXm?proid=" + yproid,
        async: false,
        dataType: "json",
        success: function (result) {
            if (result != null && result != "" && result != undefined) {
                var yxmids = result.yxmids;
                var bdcdyhs = result.bdcdyhs;
                var yxmidArr = new Array();
                var bdcdyhArr = new Array();
                if (yxmids != "" && yxmids != null) {
                    yxmidArr = yxmids.split(",");
                }
                if (bdcdyhs != "" && bdcdyhs != null) {
                    bdcdyhArr = bdcdyhs.split(",");
                }
                if (yxmidArr.length != 0 && bdcdyhArr.length != 0) {
                    for (var i = 0; i < yxmidArr.length; i++) {
                        var id = yxmidArr[i];
                        var bdcdyh = bdcdyhArr[i];
                        var url = bdcdjUrl + "/wfProject/checkBdcXm";
                        if (plChoseOne == "1") {
                            url = bdcdjUrl + "/wfProject/checkBdcXmForDyChoseOne";
                        }
                        var options = {
                            url: url,
                            type: 'post',
                            async: false,
                            dataType: 'json',
                            data: {proid: proid, yxmid: id, bdcdyh: bdcdyh},
                            success: function (data) {
                                debugger;
                                var alertSize = 0;
                                var confirmSize = 0;
                                var ysqlx = cfsqlx;
                                if (ysqlx == 'JF') {
                                    sqlx = 'JF';
                                } else {
                                    sqlx = 'CF';
                                }
                                if (data.length > 0 && sqlx != 'CF') {
                                    $("#csdjAlertInfo,#csdjConfirmInfo").html("");
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
                                            confirmInfoDom = '<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="';
                                            if (item.function == null) {
                                                confirmInfoDom += 'openProjectInfo(\'' + item.checkPorids + '\')">';
                                            } else {
                                                confirmInfoDom += item.function + '">';
                                            }
                                            if (item.title == null) {
                                                confirmInfoDom += '查看';
                                            } else {
                                                confirmInfoDom += item.title;
                                            }
                                            confirmInfoDom += '</span>' + item.checkMsg + '</div>';
                                            $("#csdjConfirmInfo").append(confirmInfoDom);
                                        } else if (item.checkModel == "alert" || item.checkModel == "confirmAndCreate") {
                                            alertSize++;
                                            if (isNotBlank(item.wiid)) {
                                                islw = true;
                                                confirmCreateLw(item, bdcdjUrl, sflw, "djsjBdcdyList");
                                            } else {
                                                if (item.checkCode == '199') {
                                                    $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                                                } else {
                                                    if (notShowCk.indexOf(item.checkCode) < 0) {
                                                        $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                                                    } else {
                                                        $("#csdjAlertInfo").append('<div class="alert alert-danger">' + item.checkMsg + '</div>');
                                                    }
                                                }
                                            }
                                        }
                                    })
                                    if (!islw) {
                                        $("#tipPop").show();
                                        $("#modal-backdrop").show();
                                    }
                                }
                                if (alertSize == 0 && confirmSize == 0) {
                                    ywsjInitVoFromFsss(proid, id, bdcdyh, "TDFW");
                                } else if (alertSize == 0 && confirmSize > 0) {
                                    if ($("button[name='hlBtn']").length == 0) {
                                        $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                                    }
                                    $("button[name='hlBtn']").click(function () {
                                        $('#tipPop').hide();
                                        ywsjInitVoFromFsss(proid, id, bdcdyh, "TDFW");
                                    })
                                }
                            },
                            error: function (data) {

                            }
                        };
                        $.ajax(options);
                    }
                }
            }
        }
        , error: function (result) {
        }
    });
    $.ajax({
        type: "post",
        async: false,
        url: bdcdjUrl + "/bdcFwfsss/initBdcFwfsssFromFsss?proid=" + proid + "&yproid=" + yproid,
        dataType: "json",
        success: function () {
            window.parent.hideModel();
            window.parent.resourceRefresh();
        },
        error: function () {
            window.parent.hideModel();
            window.parent.resourceRefresh();
        }
    });
}

//选择“不继承”时，合并的产权默认仍继承上一手附属设施，抵押的不继承
function fsssNotInherit(yproid) {
    var proid = '';
    if ($("#proid").val() != '') {
        proid = $("#proid").val();
    }

    $.ajax({
        type: "post",
        async: false,
        url: bdcdjUrl + "/bdcFwfsss/initBdcFwfsssNotInherit?proid=" + proid + "&yproid=" + yproid,
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

//过渡继承附属设施信息
function fsssInheritForGd(proid) {
    $.ajax({
        type: "post",
        async: false,
        url: bdcdjUrl + "/bdcFwfsss/fsssInheritForGd?proid=" + proid,
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

//过渡不继承附属设施信息
function fsssNotInheritForGd(proid) {
    $.ajax({
        type: "post",
        async: false,
        url: bdcdjUrl + "/bdcFwfsss/fsssNotInheritForGd?proid=" + proid,
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

//修改项目信息的函数
function gdsjEditXm(qlid, bdcdyh, bdclx, zl, gdproid, djid, bdcqzh) {
    //遮罩
    $.blockUI({message: "请稍等……"});
    var bdcdyh = (bdcdyh == null || bdcdyh == "undefined") ? "" : bdcdyh;
    var bdclx = (bdclx == null || bdclx == "undefined") ? "" : bdclx;
    var djid = (djid == null || djid == "undefined") ? "" : djid;
    var bdcqzh = (bdcqzh == null || bdcqzh == "undefined") ? "" : bdcqzh;
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
    $("#ybdcqzh").val(bdcqzh);
    $.ajax({
        url: bdcdjUrl + '/bdcJgSjgl/isCancelAll?bdclx=' + bdclx,
        type: 'post',
        dataType: 'json',
        data: $("#form").serialize(),
        success: function (data) {
            var alertSize = 0;
            var confirmSize = 0;
            if (data.length > 0) {
                $("#csdjAlertInfo,#csdjConfirmInfo").html("");
                var islw = false;
                $.each(data, function (i, item) {
                    if (item.checkModel == "confirm") {
                        confirmSize++;
                        confirmInfoDom = '<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="';
                        if (item.function == null) {
                            confirmInfoDom += 'openProjectInfo(\'' + item.checkPorids + '\')">';
                        } else {
                            confirmInfoDom += item.function + '">';
                        }
                        if (item.title == null) {
                            confirmInfoDom += '查看';
                        } else {
                            confirmInfoDom += item.title;
                        }
                        confirmInfoDom += '</span>' + item.checkMsg + '</div>';
                        $("#csdjConfirmInfo").append(confirmInfoDom);
                    } else if (item.checkModel == "alert") {
                        alertSize++;
                        if (isNotBlank(item.wiid)) {
                            islw = true;
                            confirmCreateLw(item, bdcdjUrl, sflw, "djsjBdcdyList");
                        } else {
                            if (item.checkCode == '199') {
                                $("#csdjAlertInfo").append('<div class="alert alert-info"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="bdcPic(\'' + item.info + '\')" >匹配</span>' + item.checkMsg + '</div>');
                            } else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    }
                });
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                if (!islw) {
                    $("#tipPop").show();
                    $("#modal-backdrop").show();
                }
            }
            if (alertSize == 0 && confirmSize == 0) {
                createXm(gdproid, bdcdyh, qlid, bdclx, djid);
            } else if (alertSize == 0 && confirmSize > 0) {
                if ($("button[name='hlBtn']").length == 0) {
                    $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                }
                $("button[name='hlBtn']").click(function () {
                    $('#tipPop').hide();
                    createXm(gdproid, bdcdyh, qlid, bdclx, djid);
                })
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
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
            setTimeout($.unblockUI, 10);
            var id = proid;
            if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                var check = checkExistFsssForGd(id);
                if (check == "true") {
                    //抵押，查封，合并流程弹出提示
                    if (sqlxdm == "801" || sqlxdm == "1019" || sqlxdm == "8009901" || djlx == "999") {
                        var msg = "主房存在附属设施，是否继承到抵押或查封项目之中";
                        showConfirmDialogWithClose("提示信息", msg, "fsssInheritForGd", "'" + id + "'", "fsssNotInheritForGd", "'" + id + "'");
                    } else {
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();
                    }
                } else {
                    getBdcXtConfigFj();
                    hideModelAndRefreshParant();
                }
            } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                var check = checkExistFsssForGd(id);
                if (check == "true") {
                    //抵押，查封，合并流程弹出提示
                    if (sqlxdm == "801" || sqlxdm == "1019" || sqlxdm == "8009901" || djlx == "999") {
                        var msg = "主房存在附属设施，是否继承到抵押或查封项目之中";
                        showConfirmDialogWithClose("提示信息", msg, "fsssInheritForGd", "'" + id + "'", "fsssNotInheritForGd", "'" + id + "'");
                    } else {
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();
                    }
                } else {
                    getBdcXtConfigFj();
                    hideModelAndRefreshParant();
                }
            } else {
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
        }
    });

}

function ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx) {
    var initurl = "";
    var xtly = "";
    if ($("#businessNo").val() != null && $("#businessNo").val() != "") {
        xtly = "5";
    }
    if (glzs == "true") {
        initurl = bdcdjUrl + '/wfProject/glZs?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx;
    } else {
        initurl = bdcdjUrl + '/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx + "&xtly=" + xtly;
    }
    $.ajax({
        type: 'post',
        url: initurl,
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (data == '成功') {
                var businessNo = $("#businessNo").val();
                if (businessNo != null && businessNo != "") {
                    importYwbh();
                }
                $.ajax({
                    type: 'get',
                    async: true,
                    url: bdcdjUrl + '/wfProject/updateWorkFlow?proid=' + proid,
                    success: function (data) {

                    }
                });
                //jyl 检验主房是否存在附属设施，并判断是否继承
                if (bdclx == "TDFW") {
                    var check = checkExistFsss(id);
                    if (check == "true") {
                        //var msg = "主房存在附属设施是否继承!";
                        //showConfirmDialogWithClose("提示信息", msg, "fsssEditXm", "'" + id + "'", "", "");
                        //继承上一手的房屋附属设施信息
                        //showConfirmDialogWithClose("提示信息", msg, "fsssInherit", "'" + id + "'", "", "");
                        //liujie
                        //由于附属设施面积已经加到主房上，因此附属设施必须继承，如需删除或者取消，去关联附属设施功能中取消关联和删除
                        // fsssInherit(id);
                        //抵押，查封，合并流程弹出提示
                        if (sqlxdm == "801" || sqlxdm == "1019" || djlx == "999") {
                            var msg = "主房存在附属设施，是否继承到抵押或查封项目之中"
                            showConfirmDialogWithClose("提示信息", msg, "fsssInherit", "'" + id + "'", "fsssNotInherit", "'" + id + "'");
                        } else {
                            fsssInherit(id);
                            hideModelAndRefreshParant();
                        }
                    } else {
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();
                    }
                } else {
                    getBdcXtConfigFj();
                    hideModelAndRefreshParant();
                }
            } else {
                //alert(data);
                tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            }
        },
        error: function (data) {
            setTimeout($.unblockUI, 10);
            tipInfo("该选择数据创建项目出现问题，请及时与系统管理员联系！");
            /*if (XMLHttpRequest.readyState == 4) {
                alert("保存失败!");
            }*/
        }
    });
}

function hideModelAndRefreshParant() {
    window.parent.hideModel();
    window.parent.resourceRefresh();
}

//显示确认对话框
function showConfirmDialogWithClose(title, msg, okMethod, okParm, cancelMethod, cancelParm) {
    var comfirmDia = bootbox.dialog({
        message: "<h3><b>" + msg + "</b></h3>",
        title: title,
        buttons: {
            OK: {
                label: "是",
                className: "btn-primary",
                callback: function () {
                    if (okMethod != null && okMethod != "")
                        eval(okMethod + "(" + okParm + ")");
                }
            },
            Cancel: {
                label: "否",
                className: "btn-default",
                callback: function () {
                    comfirmDia.hide();
                    if (cancelMethod != null && cancelMethod != "")
                        eval(cancelMethod + "(" + cancelParm + ")");
                    window.parent.hideModel();
                    window.parent.resourceRefresh();
                }
            }
        }
    });
}

function ywsjInitVoFromFsss(proid, yproid, ybdcdyh) {
    $.ajax({
        type: 'post',
        async: false,
        url: bdcdjUrl + '/bdcFwfsss/initVoFromFsss?proid=' + proid + "&ybdcdyh=" + ybdcdyh + "&yproid=" + yproid,
        success: function (data) {

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.readyState == 4) {
                $.Prompt("保存失败!", 1500);
            }
        }
    });
}

//检查一证多房是否存在附属设施
function checkExistFsssForGd(proid) {
    var check = '';
    $.ajax({
        url: bdcdjUrl + "/bdcFwfsss/checkExistFsssForGd?proid=" + proid,
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

function openWin(url) {
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function createProjectInfo(yproid, sqlxdm, bdcdyh, bdcdyid, alertCount) {
    if (alertCount > 0) {
        $.Prompt("验证不通过，不能创建项目！", 1500);
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

function dateForTable(grid_selector, gdly) {
    var jqData = $(grid_selector).jqGrid("getRowData");
    var rowIds = $(grid_selector).jqGrid('getDataIDs');
    $.each(jqData, function (index, data) {
        if (gdly == "fc") {
            getDate($(grid_selector), rowIds[index], data.BDCLX, data.QLID, data.DJID, data.GDPROID, data.ZL, data.FCZH, gdly);
        } else if (gdly == "td") {
            getTdDate($(grid_selector), rowIds[index], "TD", data.QLID, data.DJID, data.GDPROID, data.ZL, data.TDZH, gdly);
        } else if (gdly == "unPptd") {
            getTdDate($(grid_selector), rowIds[index], "TD", data.QLID, data.DJID, data.GDPROID, data.ZL, data.TDZH, gdly);
        } else if (gdly == "unPpfw") {
            getDate($(grid_selector), rowIds[index], data.BDCLX, data.QLID, data.DJID, data.GDPROID, data.ZL, data.FCZH, gdly);
        }
    })
}

//获取数据
function getTdDate(table, rowid, bdclx, qlid, djid, gdproid, zl, gdzh, gdly) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcDateByQlid?qlid=" + qlid,
        success: function (result) {
            var bdcdyh = result.bdcdyh;
            var ppzt = result.ppzt;
            if (bdcdyh != "" && bdcdyh != null && bdcdyh != "undefined" && sqlxdm != "9920001" && sqlxdm != "9920005") {
                var bdcdyhCellvalue = bdcdyh.substr(0, 6) + " " + bdcdyh.substr(6, 6) + " " + bdcdyh.substr(12, 7) + " " + bdcdyh.substr(19);
                var bdcdyhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + bdcdyhCellvalue + '" >' + bdcdyhCellvalue + "</a>";
                table.setCell(qlid, "BDCDYH", bdcdyhCell);
                var gdzhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + gdzh + '" >' + gdzh + "</a>";
                table.setCell(qlid, "TDZH", gdzhCell);
            } else if (sqlxdm == "9920001" || sqlxdm == "9920005") {
                var gdzhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + gdzh + '" >' + gdzh + "</a>";
                table.setCell(qlid, "TDZH", gdzhCell);
            } else {
                bdcdyh = "";
                if (ppzt == "0" && gdly != "unPptd") {
                    table.setCell(qlid, "TDZH", gdzh);
                } else {
                    var gdzhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + gdzh + '" >' + gdzh + "</a>";
                    table.setCell(qlid, "TDZH", gdzhCell);
                }
            }
            table.setCell(qlid, "BDCLX", result.bdclx);
            table.setCell(qlid, "DJID", result.djid);
            var ppztCellVal = "";
            var ppztMc = "";
            if (ppzt == '2') {
                ppztMc = "已匹配";
                table.setCell(qlid, "PPZT", ppztMc);
            } else {
                if (ppzt == '0') {
                    ppztMc = "未匹配";
                } else if (ppzt == '1') {
                    ppztMc = "部分匹配";
                }
                ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly + '\')"  title="' + ppztMc + '" >' + ppztMc + "</a>";
                table.setCell(qlid, "PPZT", ppztCellVal);
            }
            getbdcdyZt(null, result.djid, result.bdclx, table, qlid);
        }
    });
}

//获取数据
function getDate(table, rowid, bdclx, qlid, djid, gdproid, zl, gdzh, gdly) {
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getBdcDateByQlid?qlid=" + qlid,
        async: false,
        success: function (result) {
            var bdcdyh = result.bdcdyh;
            var ppzt = result.ppzt;
            if (bdcdyh != "" && bdcdyh != null) {
                var bdcdyhCellvalue = bdcdyh.substr(0, 6) + " " + bdcdyh.substr(6, 6) + " " + bdcdyh.substr(12, 7) + " " + bdcdyh.substr(19);
                var bdcdyhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + bdcdyhCellvalue + '" >' + bdcdyhCellvalue + "</a>";
                table.setCell(qlid, "BDCDYH", bdcdyhCell);
                var gdzhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + gdzh + '" >' + gdzh + "</a>";
                table.setCell(qlid, "FCZH", gdzhCell);
            } else {
                bdcdyh = "";
                if (ppzt == "0" && gdly != "unPpfw") {
                    table.setCell(qlid, "FCZH", gdzh);
                } else {
                    var gdzhCell = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + zl + '\',\'' + gdproid + '\',\'' + djid + '\',\'' + gdzh + '\')" title="' + gdzh + '" >' + gdzh + "</a>";
                    table.setCell(qlid, "FCZH", gdzhCell);
                }
            }
            table.setCell(qlid, "BDCLX", result.bdclx);
            table.setCell(qlid, "DJID", result.djid);
            var ppztCellVal = "";

            var ppztMc = "";
            if (ppzt == '2') {
                ppztMc = "已匹配";
                table.setCell(qlid, "PPZT", ppztMc);
            } else {
                if (ppzt == '0') {
                    ppztMc = "未匹配";
                } else if (ppzt == '1') {
                    ppztMc = "部分匹配";
                }
                ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly + '\')"  title="' + ppztMc + '" >' + ppztMc + "</a>";
                table.setCell(qlid, "PPZT", ppztCellVal);
            }
        }
    });
}

function ppBdcdy(qlid, gdzh, gdly) {
    var url = bdcdjUrl + "/bdcJgSjgl/toDataPicForSelect?matchTdzh=true&checkTdzh=false&editFlag=true&filterFwPpzt=3,2,1,0&gdzh=" + gdzh + "&gdly=" + gdly;
    var w_width = screen.availWidth - 10;
    var w_height = screen.availHeight - 32;
    window.open(url, "匹配数据界面", "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
}

function afterClosePpBdcdy(gdly) {
    if (showOptimize == "true") {
        if (gdly == "fc") {
            $("#gdfcsj_search_btn").click();
        } else if (gdly = "td") {
            $("#gdtdsj_search_btn").click();
        }
    } else {
        if (gdly == "fc") {
            var dcxc = $("#gdfcsj_search").val();
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入权利人/坐落/房产证号");
            } else {
                var gdfcsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdfczListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: $("#gdfcsj_search").val()});
            }
        } else if (gdly = "td") {
            var dcxc = $("#gdtdsj_search").val();
            if (dcxc == "" || dcxc == null || dcxc == undefined) {
                tipInfo("请输入权利人/坐落/土地证号");
            } else {
                var gdtdsjUrl = bdcdjUrl + "/selectBdcdyQlShow/getGdtdzListByPage?zdtzm=" + zdtzm + "&dyfs=" + dyfs + "&qllx=" + yqllxdm + "&bdclxdm=" + bdclxdm + "&qlxzdm=" + qlxzdm + "&proid=" + proid + "&ysqlxdm=" + ysqlxdm;
                tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: $("#gdtdsj_search").val()});
            }
        }
    }
}

function getPptzs(bdcdyh) {
    openWin(reportUrl + "/ReportServer?reportlet=print%2Fbdc_pptzs.cpt&op=write&bdcdyh=" + bdcdyh);
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

function getBdcXtConfigFj() {
    $.ajax({
        type: 'get',
        url: bdcdjUrl + '/wfProject/getBdcXtConfigFj?wiid=' + wiid,
        datatype: 'json',
        success: function () {
        },
        error: function () {
        }
    });
}

function show(c_Str) {
    if (document.all(c_Str).style.display == 'none') {
        document.all(c_Str).style.display = 'block';
    } else {
        document.all(c_Str).style.display = 'none';
    }
}

function getSdStatus(qlid, cqzh) {
    cqzh = encodeURI(cqzh);
    $.ajax({
        type: "GET",
        url: bdcdjUrl + "/selectBdcdyQlShow/getXzyy?cqzh=" + cqzh + "&qlid=" + qlid,
        dataType: "json",
        async: false,
        success: function (jsonData) {
            if (jsonData.msg == "false") {
                var xzyy = jsonData.xzyy;
                $("#" + qlid).css("background-color", "#F9F900");
                $("#" + qlid).attr("tooltips", "限制原因：" + jsonData.xzyy);
                $("#" + qlid).hoverTips();
            }
        },
        error: function (data) {
        }
    });
}

$.fn.extend({
    hoverTips: function () {
        var self = $(this);
        var sw = "";
        if (!sw) {
            sw = true;
            var content = self.attr("tooltips");
            var htmlDom = $("<div class='tooltips'>")
                .addClass("yellow")
                .html("<p class='content'></p>"
                    + "<p class='triangle-front'></p>"
                    + "<p class='triangle-back'></p>");
            htmlDom.find("p.content").html(content);
        }
        self.on("mouseover", function () {
            $("body").append(htmlDom);
            var left = self.offset().left - htmlDom.outerWidth() / 2 + self.outerWidth() / 2;
            var top = self.offset().top - htmlDom.outerHeight() - parseInt(htmlDom.find(".triangle-front").css("border-width"));
            htmlDom.css({"left": left, "top": top - 10, "display": "block"});
            htmlDom.stop().animate({"top": top, "opacity": 1}, 300);
        });
        self.on("mouseout", function () {
            var top = parseInt(htmlDom.css("top"));
            htmlDom.stop().animate({"top": top - 10, "opacity": 0}, 300, function () {
                htmlDom.remove();
                sw = false;
            });
        });
    }
})