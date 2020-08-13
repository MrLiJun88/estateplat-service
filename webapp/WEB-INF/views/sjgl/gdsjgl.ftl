<@com.html title="" import="ace,public">
<style>
    span.label {
        border-radius: 3px !important;
    }

    .tab-content {
        overflow: hidden;
        height: auto;
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

        /*移动modal样式*/
    #lqSearchPop .modal-dialog, #cqSearchPop .modal-dialog, #fcSearchPop .modal-dialog, #tdSearchPop  .modal-dialog {
        width: 600px;
        position: fixed;
        top: 20px;
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

    .form .row .col-xs-4 {
        padding-left: 0px;
        padding-right: 0px;
    }

    label {
        font-weight: bold;
    }

    .ace-settings-btn {
        top: 38px;
    }

    .SSinput {
        min-width: 330px !important;
    }
</style>
<script type="text/javascript">
//table每页行数
$rownum = 10;
//table 每页高度
$pageHight = '370px';
//全局的不动产类型
$bdclx = 'TDFW';

$(function () {
    //默认初始化表格
//        fwTableInit();
    $("#fwTab,#lqTab,#cqTab,#tdTab").click(function () {
        //关掉高级查询
        $("#lqHide,#cqHide,#fcHide,#tdHide").click();
        if (this.id == "fwTab") {
            $bdclx = 'TDFW';
            $("#fw").addClass("active");
            var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdXmFwJsonByPage?" + $("#fcSearchForm").serialize();
            fwTableInit();
            if (isLoadGrid("fw"))
                tableReload("fw-grid-table", fwUrl, {iszx:1, hhSearch:'', rf1Dwmc:''}, '', '');
        } else if (this.id == "lqTab") {
            $bdclx = 'TDSL';
            $("#lq").addClass("active");
            var lqUrl = "${bdcdjUrl}/bdcSjgl/getGdLqJson?" + $("#lqSearchForm").serialize();
            lqTableInit();
            if (isLoadGrid("lq"))
                tableReload("lq-grid-table", lqUrl, {hhSearch:'', rf1Dwmc:''}, '', '');
        } else if (this.id == "cqTab") {
            $bdclx = 'TDQT';
            $("#cq").addClass("active");
            var cqUrl = "${bdcdjUrl}/bdcSjgl/getGdCqJson?" + $("#cqSearchForm").serialize();
            cqTableInit();
            if (isLoadGrid("cq"))
                tableReload("cq-grid-table", cqUrl, {hhSearch:'', rf1Dwmc:''}, '', '');
        } else if (this.id == "tdTab") {
            $bdclx = 'TD';
            $("#td").addClass("active");
            var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson?" + $("#tdSearchForm").serialize();
            tdTableInit();
            if (isLoadGrid("td"))
                tableReload("td-grid-table", tdUrl, {iszx:1, hhSearch:'', rf1Dwmc:''}, '', '');
        }
    })

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var contentWidth = $(".tab-content").width();
        $("#fw-grid-table,#lq-grid-table,#cq-grid-table,#td-grid-table").jqGrid('setGridWidth', contentWidth);
    });

    /*初始化列表*/
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

        $(window).trigger("resize.jqGrid");
    }

    /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
//    var ua = navigator.userAgent.toLowerCase();
//    if (window.ActiveXObject) {
//        if (ua.match(/msie ([\d.]+)/)[1] == '8.0') {
//            $(window).resize(function () {
//                $.each($(".moveModel > .modal-dialog"), function () {
//                    $(this).css("left", ($(window).width() - $(this).width()) / 2);
//                    $(this).css("top", "40px");
//                })
//            })
//        }
//    }

    /*   文字水印  */
    $(".watermarkText").watermark();

    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".tdSearchPop-modal,.fcSearchPop-modal,.lqSearchPop-modal,.cqSearchPop-modal").draggable({opacity:0.7, handle:'div.modal-header'});


    //林权高级查询的搜索按钮事件
    $("#lqGjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcSjgl/getGdLqJson?" + $("#lqSearchForm").serialize();
        tableReload("lq-grid-table", Url, {hhSearch:"", rf1Dwmc:''});
    })

    //草权高级查询的搜索按钮事件
    $("#cqGjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcSjgl/getGdCqJson?" + $("#cqSearchForm").serialize();
        tableReload("cq-grid-table", Url, {hhSearch:"", rf1Dwmc:''});
    })

    //土地高级查询的搜索按钮事件
    $("#tdGjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcSjgl/getGdTdJson?" + $("#tdSearchForm").serialize();
        tableReload("td-grid-table", Url, {hhSearch:"", rf1Dwmc:''});
    })

    //房产高级查询的搜索按钮事件
    $("#fcGjSearchBtn").click(function () {
        var Url = "${bdcdjUrl}/bdcSjgl/getGdXmFwJsonGaoByPage?" + $("#fcSearchForm").serialize();
        tableReload("fw-grid-table", Url, {hhSearch:"", rf1Dwmc:''});
    })
    /*高级按钮点击事件 begin*/
    $("#lqShow,#cqShow,#tdShow,#fcShow").click(function () {
        if (this.id == "lqShow") {
            $("#lqSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        } else if (this.id == "tdShow") {
            $("#tdSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        } else if (this.id == "fcShow") {
            $("#fcSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        } else if (this.id == "cqShow") {
            $("#cqSearchPop").show();
            $(".modal-dialog").css({"_margin-left":"25%"});
        }
    });
    //高级查询弹出框隐藏
    $("#lqHide,#cqHide,#fcHide,#tdHide").click(function () {
        if (this.id == "cqHide") {
            $("#cqSearchPop").hide();
            $("#cqSearchForm")[0].reset();
        } else if (this.id == "fcHide") {
            $("#fcSearchPop").hide();
            $("#fcSearchForm")[0].reset();
        } else if (this.id == "tdHide") {
            $("#tdSearchPop").hide();
            $("#tdSearchForm")[0].reset();
        } else if (this.id == "lqHide") {
            $("#lqSearchPop").hide();
            $("#lqSearchForm")[0].reset();
        }
    });


    $('input').focus(function () {
        var id = $(this).attr('id');
        if (id == 'fw_search_qlr') {
            $('#fw_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#fw_search').click();
                }
            });
        }
        else if (id == 'td_search_qlr') {
            $('#td_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#td_search').click();
                }
            });

        } else if (id == 'lq_search_qlr') {
            $('#lq_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#lq_search').click();
                }
            });
        } else if (id == 'cq_search_qlr') {
            $('#cq_search_qlr').keydown(function (event) {
                if (event.keyCode == 13) {
                    $('#cq_search').click();
                }
            });
        }


    });


//查询按钮点击事件
    $("#fw_search").click(function () {
        var hhSearch = $("#fw_search_qlr").val();
        var fwUrl = "${bdcdjUrl}/bdcSjgl/getGdXmFwJsonByPage?" + $("#fwSearchForm").serialize();
        tableReload("fw-grid-table", fwUrl, {iszx:1, hhSearch:hhSearch, rf1Dwmc:''}, '', '');
    })
    $("#lq_search").click(function () {
        var hhSearch = $("#lq_search_qlr").val();
        var lqUrl = "${bdcdjUrl}/bdcSjgl/getGdLqJson?" + $("#lqSearchForm").serialize();
        tableReload("lq-grid-table", lqUrl, {hhSearch:'', rf1Dwmc:hhSearch}, '', '');
    })
    $("#cq_search").click(function () {
        var hhSearch = $("#cq_search_qlr").val();
        var cqUrl = "${bdcdjUrl}/bdcSjgl/getGdCqJson?" + $("#cqSearchForm").serialize();
        tableReload("cq-grid-table", cqUrl, {hhSearch:'', rf1Dwmc:hhSearch}, '', '');
    })
    $("#td_search").click(function () {
        var hhSearch = $("#td_search_qlr").val();
        var tdUrl = "${bdcdjUrl}/bdcSjgl/getGdTdJson?" + $("#tdSearchForm").serialize();
        tableReload("td-grid-table", tdUrl, {iszx:1, hhSearch:hhSearch, rf1Dwmc:''}, '', '');
    })
//抵押录入
    $("#gdDySave").click(function () {
        var table = $("#fw-grid-table");
        var ids = table.jqGrid('getGridParam', 'selarrrow');
        if (ids.length != 1) {
            bootbox.dialog({
                message:"<h3><b>请选择一条数据!</b></h3>",
                title:"",
                buttons:{
                    main:{
                        label:"关闭",
                        className:"btn-primary"
                    }
                }
            });
            return;
        }
        var table = $("#fw-grid-table");
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/bdcSjgl/getDyYgCfStatus?bdcid=" + ids[0],
            dataType:"json",
            success:function (result) {
                var cellVal = "";
                //正常
                if (result.dy) {
                    addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_dy.cpt&op=write&bdcid=" + ids[0]);
                } else {
                    bootbox.dialog({
                        message:"<h3><b>已存在抵押信息!</b></h3>",
                        title:"",
                        buttons:{
                            main:{
                                label:"关闭",
                                className:"btn-primary"
                            }
                        }
                    });
                    return;
                }
            }
        });
    })
//过度查封信息录入
    $("#gdCfSave").click(function () {
        var table = $("#fw-grid-table");
        var ids = table.jqGrid('getGridParam', 'selarrrow');
        if (ids.length != 1) {
            bootbox.dialog({
                message:"<h3><b>请选择一条数据!</b></h3>",
                title:"",
                buttons:{
                    main:{
                        label:"关闭",
                        className:"btn-primary"
                    }
                }
            });
            return;
        }

        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/bdcSjgl/getDyYgCfStatus?bdcid=" + ids[0],
            dataType:"json",
            success:function (result) {
                var cellVal = "";
                //正常
                if (result.cf) {
                    addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cf.cpt&op=write&bdcid=" + ids[0]);
                } else {
                    bootbox.dialog({
                        message:"<h3><b>已存在查封信息!</b></h3>",
                        title:"",
                        buttons:{
                            main:{
                                label:"关闭",
                                className:"btn-primary"
                            }
                        }
                    });
                    return;
                }
            }
        });
    })
//新增按钮点击事件
    $("#gdFwAdd,#gdLqAdd,#gdTdAdd,#gdCqAdd").click(function () {
        var bdclxId = this.id;
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/gdXxLr/getUUid",
            dataType:"json",
            success:function (result) {
                if (result != null && result != "") {
                    if (bdclxId == "gdFwAdd") {
                        addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=fw&proid=" + result);
                    } else if (bdclxId == "gdLqAdd") {
                        addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write");
                    } else if (bdclxId == "gdTdAdd") {
                        addOrUpdate("${bdcdjUrl!}/gdXxLr?editFlag=true&bdclx=td&proid=" + result);
                    } else if (bdclxId == "gdCqAdd") {
                        addOrUpdate("${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write");
                    }
                }
            }
        });
    })
    /**解除抵押**/
    $("#gdFwJcdy").click(function () {
        var table = $("#fw-grid-table");
        var ids = table.jqGrid('getGridParam', 'selarrrow');
        var url = "${bdcdjUrl!}/bdcSjgl/jcdyGdsj";

        for (var i = 0; i < ids.length; i++) {
            var rowData = table.jqGrid("getRowData", ids[i]);
            var val = rowData.STATUS;
            if (val.indexOf("抵押") <= 0) {
                bootbox.dialog({
                    message:"<h3><b>存在非抵押状态数据!</b></h3>",
                    title:"",
                    buttons:{
                        main:{
                            label:"关闭",
                            className:"btn-primary"
                        }
                    }
                });
                return;
            }
        }

        if (ids.length == 0) {
            bootbox.dialog({
                message:"<h3><b>请至少选择一条数据!</b></h3>",
                title:"",
                buttons:{
                    main:{
                        label:"关闭",
                        className:"btn-primary"
                    }
                }
            });
            return;
        }
        $.blockUI({ message:"请稍等……" });
        bootbox.dialog({
            message:"是否解除抵押？",
            title:"",
            closeButton:false,
            buttons:{
                success:{
                    label:"确定",
                    className:"btn-success",
                    callback:function () {
                        $.getJSON(url + "?ids=" + ids, {}, function (jsonData) {
                            setTimeout($.unblockUI, 10);
                            tipInfo(jsonData.result);
                            table.trigger("reloadGrid");
                        })
                    }
                },
                main:{
                    label:"取消",
                    className:"btn-primary",
                    callback:function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    })
    /**解除抵押**/
    $("#gdFwJccf").click(function () {
        var table = $("#fw-grid-table");
        var ids = table.jqGrid('getGridParam', 'selarrrow');
        var url = "${bdcdjUrl!}/bdcSjgl/jccfGdsj";

        for (var i = 0; i < ids.length; i++) {
            var rowData = table.jqGrid("getRowData", ids[i]);
            var val = rowData.STATUS;
            if (val.indexOf("查封") <= 0) {
                bootbox.dialog({
                    message:"<h3><b>存在非查封状态数据!</b></h3>",
                    title:"",
                    buttons:{
                        main:{
                            label:"关闭",
                            className:"btn-primary"
                        }
                    }
                });
                return;
            }
        }

        if (ids.length == 0) {
            bootbox.dialog({
                message:"<h3><b>请至少选择一条数据!</b></h3>",
                title:"",
                buttons:{
                    main:{
                        label:"关闭",
                        className:"btn-primary"
                    }
                }
            });
            return;
        }
        $.blockUI({ message:"请稍等……" });
        bootbox.dialog({
            message:"是否解除查封？",
            title:"",
            closeButton:false,
            buttons:{
                success:{
                    label:"确定",
                    className:"btn-success",
                    callback:function () {
                        $.getJSON(url + "?ids=" + ids, {}, function (jsonData) {
                            setTimeout($.unblockUI, 10);
                            tipInfo(jsonData.result);
                            table.trigger("reloadGrid");
                        })
                    }
                },
                main:{
                    label:"取消",
                    className:"btn-primary",
                    callback:function () {
                        setTimeout($.unblockUI, 10);
                    }
                }
            }
        });
    })
//修改按钮点击事件
    $("#gdFwUpdate,#gdLqUpdate,#gdTdUpdate,#gdCqUpdate").click(function () {
        if (this.id == "gdFwUpdate") {
            updateGdsj($("#fw-grid-table"), "${bdcdjUrl!}/gdXxLr?bdclx=fw&editFlag=true&proid=");
        } else if (this.id == "gdLqUpdate") {
            updateGdsj($("#lq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=");
        } else if (this.id == "gdTdUpdate") {
            updateGdsj($("#td-grid-table"), "${bdcdjUrl!}/gdXxLr?bdclx=td&editFlag=true&proid=");
        } else if (this.id == "gdCqUpdate") {
            updateGdsj($("#cq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=");
        }
    })

//删除按钮点击事件
    $("#gdFwDel,#gdLqDel,#gdTdDel,#gdCqDel").click(function () {
        if (this.id == "gdFwDel") {
            delRule($("#fw-grid-table"), "${bdcdjUrl!}/bdcSjgl/delGdsj");
        } else if (this.id == "gdLqDel") {
            delRule($("#lq-grid-table"), "${bdcdjUrl!}/bdcSjgl/delGdsj");
        } else if (this.id == "gdTdDel") {
            delRule($("#td-grid-table"), "${bdcdjUrl!}/bdcSjgl/delGdsj");
        } else if (this.id == "gdCqDel") {
            delRule($("#cq-grid-table"), "${bdcdjUrl!}/bdcSjgl/delGdsj");
        }
    })

//权限控制
//    authorizeElements();
})
//删除判断是否没有选择数据
function delRule(table, url) {
    var ids = table.jqGrid('getGridParam', 'selarrrow');
    if (ids.length == 0) {
        bootbox.dialog({
            message:"<h3><b>请至少选择一条数据!</b></h3>",
            title:"",
            buttons:{
                main:{
                    label:"关闭",
                    className:"btn-primary"
                }
            }
        });
        return;
    }
    $.blockUI({ message:"请稍等……" });
    bootbox.dialog({
        message:"是否删除？",
        title:"",
        closeButton:false,
        buttons:{
            success:{
                label:"确定",
                className:"btn-success",
                callback:function () {
                    $.getJSON(url + "?ids=" + ids + "&bdclx=" + $bdclx, {}, function (jsonData) {
                        setTimeout($.unblockUI, 10);
                        tipInfo(jsonData.result);
                        table.trigger("reloadGrid");
                    })
                }
            },
            main:{
                label:"取消",
                className:"btn-primary",
                callback:function () {
                    setTimeout($.unblockUI, 10);
                }
            }
        }
    });
}
//修改过度数据
function updateGdsj(table, url) {
    var ids = table.jqGrid('getGridParam', 'selarrrow');
    if (ids.length == 0) {
        tipInfo("请选择一条要修改的数据!");
    } else if (ids.length > 1) {
        tipInfo("注意:只能选择一条数据进行修改!");
    } else {
        addOrUpdate(url + ids[0]);
    }
}

//权限控制
function authorizeElements() {
    var platformUrl = '${bdcdjUrl!}/bdcSjgl/getGdsjAuthor';
    var urlVars = getUrlVars();
    var param = {from:urlVars.from, taskid:urlVars.taskid, proid:urlVars.proid, wiid:urlVars.wiid, rid:urlVars.rid};
    $.post(platformUrl, param, function (data) {
        if (data != null && data != '') {
            for (var i = 0; i < data.length; i++) {
                var elementName = data[i].elementName;
                $("button[name='" + elementName + "']").parent().removeClass("hidden");
            }
        }
    });
}

//获取参数
function getUrlVars() {

    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

//提示信息
function tipInfo(msg) {
    // bootbox.dialog({
    //     message:"<h3><b>" + msg + "</b></h3>",
    //     title:"",
    //     buttons:{
    //         main:{
    //             label:"关闭",
    //             className:"btn-primary"
    //         }
    //     }
    // });
    // return;
    $.Prompt(msg,1500);
}

function tableReload(table, Url, data, colModel, loadComplete) {
    var index = 0;
    var jqgrid = $("#" + table);
    if (colModel == '' && loadComplete == '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data});
    } else if (loadComplete == '' && colModel != '') {
        jqgrid.setGridParam({url:Url, datatype:'json', page:1, postData:data, colModel:colModel});
    } else if (loadComplete != '' && colModel != '') {
        jqgrid.setGridParam({
            url:Url,
            datatype:'json',
            page:1,
            postData:data,
            colModel:colModel,
            loadComplete:loadComplete
        });
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
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'PROID'},
        colNames:['受理编号', '业务类型', '坐落', '权利人', '状态', 'ID'],
        colModel:[
            {name:'SLBH', index:'SLBH', width:'10%', sortable:false},
            {name:'YWLX', index:'YWLX', width:'15%', sortable:false},
            {name:'FWZL', index:'FWZL', width:'25%', sortable:false},
            {name:'RF1DWMC', index:'RF1DWMC', width:'20%', sortable:false},
            {name:'PPZT', index:'PPZT', width:'10%', sortable:false},
            {name:'PROID', index:'PROID', width:'0%', sortable:false, hidden:true}
        ],
        viewrecords:true,
        rowNum:$rownum,
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
            var jqData = $(grid_selector).jqGrid("getRowData");
            $.each(jqData, function (index, data) {
                getPpzt(data.PPZT, $(grid_selector), data.PROID);
            })
        },
        editurl:"",
        caption:"",
        autowidth:true,
        subGrid:true,
        subGridOptions:{
            plusicon:"ace-icon fa fa-plus center bigger-110 blue",
            minusicon:"ace-icon fa fa-minus center bigger-110 blue",
            openicon:"ace-icon fa fa-chevron-right center orange"
        },
        subGridRowExpanded:function (subgridDivId, rowId) {
            var proid = rowId;
            var subgridTableId = subgridDivId + "_t";
            $("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table>");
            $("#" + subgridTableId).jqGrid({
                url:"${bdcdjUrl}/gdXxLr/getGdFwJson?proid=" + proid,
                datatype:"json",
                jsonReader:{id:'FWID'},
                height:250,
                colNames:['档案号', '房屋坐落', '规划用途', '建筑面积', '所在层', '总层数'],
                colModel:[
                    {name:'DAH', index:'DAH', width:'8%', sortable:false},
                    {name:'FWZL', index:'FWZL', width:'30%', sortable:false},
                    {name:'GHYT', index:'GHYT', width:'16%', sortable:false},
                    {name:'JZMJ', index:'JZMJ', width:'12%', sortable:false},
                    {name:'SZC', index:'SZC', width:'12%', sortable:false},
                    {name:'ZCS', index:'ZCS', width:'12%', sortable:false}
                ],
                autowidth:true
            });
        }
    });
}

//获取 抵押 查封 预告 状态
function getDyYgCfStatus(bdcid, table, rowid) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcSjgl/getDyYgCfStatus?bdcid=" + bdcid,
        dataType:"json",
        success:function (result) {
            var cellVal = "";
            //正常
            if (result.cf && result.dy && result.yg) {
                cellVal = '<span class="label label-success">正常</span>';
            } else {//有查封 预告 或 抵押
                if (!result.cf) {
                    cellVal += '<span class="label label-warning">查封</span><span> </span>';
                }
                if (!result.dy) {
                    cellVal += '<span class="label label-danger">抵押</span><span> </span>';
                }
                if (!result.yg) {
                    cellVal += '<span class="label label-info">预告</span>';
                }
            }
            table.setCell(rowid, "STATUS", cellVal);
        }
    });
}
//土地证初始化
function tdTableInit() {
    var grid_selector = "#td-grid-table";
    var pager_selector = "#td-grid-pager";
    //resize to fit page size
    /*  $(window).on('resize.jqGrid', function () {
          $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
      });*/
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    var gridRowNum = $rownum;
    jQuery(grid_selector).jqGrid({
        url:"${bdcdjUrl}/bdcSjgl/getGdTdJson",
        datatype:"json",
        height:$pageHight,
        jsonReader:{id:'TDID'},
        colNames:['权利人', "坐落", '土地证号', '地籍号', '权利人证件号', '宗地面积', '用途'/*,'状态','ID'*/],
        colModel:[
            {name:'RF1DWMC', index:'RF1DWMC', width:'15%', sortable:false},
            {name:'ZL', index:'ZL', width:'25%', sortable:false},
            {name:'TDZH', index:'TDZH', width:'15%', sortable:false},
            {name:'DJH', index:'DJH', width:'15%', sortable:false},
            {name:'RF1ZJH', index:'RF1ZJH', width:'14%', sortable:false},
            {name:'ZDMJ', index:'ZDMJ', width:'6%', sortable:false},
            {name:'YT', index:'YT', width:'10%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                if (!cellvalue) {
                    return"";
                }
                var value = cellvalue;
                $.each(${fwytList!}, function (i, item) {
                    if (item.DM == cellvalue) {
                        value = item.MC;
                    }
                })
                return value;
            }}/*,
            {name:'STATUS', index:'', width:'20%', sortable:false},
            {name:'TD_ID', index:'TD_ID', width:'0%', sortable:false,hidden:true}*/
        ],
        viewrecords:true,
        rowNum:gridRowNum, /*
            rowList:[10, 20, 30],*/
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
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
        },
        ondblClickRow:function () {
            updateGdsj($("#td-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_td.cpt&op=write&tdid=");
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}
//草权表初始化
function cqTableInit() {
    var grid_selector = "#cq-grid-table";
    var pager_selector = "#cq-grid-pager";
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
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'CQID'},
        colNames:['权利人', "草权证号", '坐落', '权利人证件号', '承包面积'/*,'状态','ID'*/],
        colModel:[
            {name:'RF1DWMC', index:'RF1DWMC', width:'15%', sortable:false},
            {name:'CQZH', index:'CQZH', width:'20%', sortable:false},
            {name:'ZL', index:'ZL', width:'30%', sortable:false},
            {name:'RF1ZJH', index:'RF1ZJH', width:'25%', sortable:false},
            {name:'CBMJ', index:'CBMJ', width:'10%', sortable:false}/*,
            {name:'STATUS', index:'', width:'20%', sortable:false},
            {name:'CQID', index:'CQID', width:'0%', sortable:false,hidden:true}*/
        ],
        viewrecords:true,
        rowNum:$rownum, /*
            rowList:[10, 20, 30],*/
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
        },
        ondblClickRow:function () {
            updateGdsj($("#cq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_cq.cpt&op=write&cqid=");
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}
//获取匹配状态
function getPpzt(ppzt, table, rowid) {
    if (ppzt == "1")
        ppzt = '<span class="label label-warning">已部分匹配未发证</span>';
    else if (ppzt == "4")
        ppzt = '<span class="label label-info">已完全匹配已发证</span>';
    else if (ppzt == "3")
        ppzt = '<span class="label label-warning">已完全匹配正在发证</span>';
    else if (ppzt == "2")
        ppzt = '<span class="label label-success">已完全匹配未发证</span>';
    else
        ppzt = '<span class="label label-danger">待匹配未发证</span>';
    table.setCell(rowid, "PPZT", ppzt);
}
//林权表初始化
function lqTableInit() {
    var grid_selector = "#lq-grid-table";
    var pager_selector = "#lq-grid-pager";
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
        datatype:"local",
        height:$pageHight,
        jsonReader:{id:'LQID'},
        colNames:['权利人', "林权证号", '坐落', '权利人证件号', '承包面积', '林种'/*,'状态','ID'*/],
        colModel:[
            {name:'RF1DWMC', index:'RF1DWMC', width:'15%', sortable:false},
            {name:'LQZH', index:'LQZH', width:'15%', sortable:false},
            {name:'LQZL', index:'LQZL', width:'30%', sortable:false},
            {name:'RF1ZJH', index:'RF1ZJH', width:'20%', sortable:false},
            {name:'SYQMJ', index:'SYQMJ', width:'10%', sortable:false},
            {name:'LZ', index:'LZ', width:'10%', sortable:false}/*,
            {name:'STATUS', index:'', width:'20%', sortable:false},
            {name:'LQID', index:'LQID', width:'0%', sortable:false,hidden:true}*/
        ],
        viewrecords:true,
        rowNum:$rownum, /*
            rowList:[10, 20, 30],*/
        pager:pager_selector,
        pagerpos:"left",
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
            }, 0);
            //如果7条设置宽度为auto,如果少于7条就设置固定高度
            if ($(grid_selector).jqGrid("getRowData").length == $rownum) {
                $(grid_selector).jqGrid("setGridHeight", "100%");
            } else {
                $(grid_selector).jqGrid("setGridHeight", $pageHight);
            }
        },
        ondblClickRow:function () {
            updateGdsj($("#lq-grid-table"), "${reportUrl!}/ReportServer?reportlet=edit%2Fgd_lq.cpt&op=write&lqid=");
        },
        editurl:"", //nothing is saved
        caption:"",
        autowidth:true
    });
}
//open新窗口
function addOrUpdate(url) {
    openWin(url);
}

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
}
function updatePagerIcons(table) {
    var replacement =
    {
        'ui-icon-seek-first':'ace-icon fa fa-angle-double-left bigger-140',
        'ui-icon-seek-prev':'ace-icon fa fa-angle-left bigger-140',
        'ui-icon-seek-next':'ace-icon fa fa-angle-right bigger-140',
        'ui-icon-seek-end':'ace-icon fa fa-angle-double-right bigger-140'
    };
    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
        var icon = $(this);
        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
    })
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
</script>
<div class="main-container">
<div class="space-8"></div>
<div class="page-content" id="mainContent">
<div class="row">
<div class="tabbable">
<ul class="nav nav-tabs">
    <#list gdTabOrderList as gdTabOrder>
        <#if gdTabOrder=='td'>
            <li>
                <a data-toggle="tab" id="tdTab" href="#td">
                    土地证
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
                    草权证
                </a>
            </li>
        <#else>
            <li>
                <a data-toggle="tab" id="fwTab" href="#fw">
                    房产项目
                </a>
            </li>

        </#if>

    </#list>
</ul>
<div class="tab-content">
<div id="fw" class="tab-pane in active">
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                           data-watermark="请输入受理编号/权利人/坐落">
                </td>
                <td class="Search">
                    <a href="#" id="fw_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="fcShow">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" name="add" id="gdFwAdd">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>新增</span>
                </button>
            </li>
            <li>
                <button type="button" name="update" id="gdFwUpdate">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>修改</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="del" id="gdFwDel">
                    <i class="ace-icon fa fa-trash-o"></i>
                    <span>删除</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="jcdy" id="gdFwJcdy">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>解除抵押</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="jccf" id="gdFwJccf">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>解除查封</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="dylr" id="gdDySave">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>抵押录入</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="cflr" id="gdCfSave">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>查封录入</span>
                </button>
            </li>
        </ul>
    </div>
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
                           data-watermark="请输入权利人">
                </td>
                <td class="Search">
                    <a href="#" id="lq_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="lqShow">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" name="add" id="gdLqAdd">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>新增</span>
                </button>
            </li>
            <li>
                <button type="button" name="update" id="gdLqUpdate">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>修改</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="del" id="gdLqDel">
                    <i class="ace-icon fa fa-trash-o"></i>
                    <span>删除</span>
                </button>
            </li>
        </ul>
    </div>
    <table id="lq-grid-table"></table>
    <div id="lq-grid-pager"></div>
</div>
<#--草权-->
<div id="cq" class="tab-pane">
    <div class="simpleSearch">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <input type="text" class="SSinput watermarkText" id="cq_search_qlr"
                           data-watermark="请输入权利人">
                </td>
                <td class="Search">
                    <a href="#" id="cq_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="cqShow">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" name="add" id="gdCqAdd">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>新增</span>
                </button>
            </li>
            <li>
                <button type="button" name="update" id="gdCqUpdate">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>修改</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="del" id="gdCqDel">
                    <i class="ace-icon fa fa-trash-o"></i>
                    <span>删除</span>
                </button>
            </li>
        </ul>
    </div>
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
                           data-watermark="请输入权利人">
                </td>
                <td class="Search">
                    <a href="#" id="td_search">
                        搜索
                        <i class="ace-icon fa fa-search bigger-130"></i>
                    </a>
                </td>
                <td style="border: 0px">&nbsp;</td>
                <td>
                    <button type="button" class="btn01 AdvancedButton" id="tdShow">高级搜索</button>
                </td>
            </tr>
        </table>
    </div>
    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" name="add" id="gdTdAdd">
                    <i class="ace-icon fa fa-file-o"></i>
                    <span>新增</span>
                </button>
            </li>
            <li>
                <button type="button" name="update" id="gdTdUpdate">
                    <i class="ace-icon fa fa-pencil-square-o"></i>
                    <span>修改</span>
                </button>
            </li>
            <li class="hidden">
                <button type="button" name="del" id="gdTdDel">
                    <i class="ace-icon fa fa-trash-o"></i>
                    <span>删除</span>
                </button>
            </li>
        </ul>
    </div>
    <table id="td-grid-table"></table>
    <div id="td-grid-pager"></div>
</div>
</div>
</div>
</div>
</div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<!--房产证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="fcSearchPop">
    <div class="modal-dialog fcSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="fcHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="fcSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>档案号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="dah" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwzl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>规划用途：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="ghyt" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>房屋结构：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fwjg" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="fcGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--土地证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="tdSearchPop">
    <div class="modal-dialog tdSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="tdHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="tdSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>土地证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="tdzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>地籍号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="djh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>宗地面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zdmj" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>用途：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="yt" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="tdGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--林权证高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="lqSearchPop">
    <div class="modal-dialog lqSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="lqHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="lqSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>林权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lqzl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>承包面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="syqmj" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>林种：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="lz" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="lqGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
<!--草权高级搜索-->
<div class="Pop-upBox moveModel" style="display: none;" id="cqSearchPop">
    <div class="modal-dialog cqSearchPop-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                <button type="button" id="cqHide" class="proHide"><i class="ace-icon glyphicon glyphicon-remove"></i>
                </button>
            </div>
            <div class="bootbox-body" style="background: #fafafa;">
                <form class="form advancedSearchTable" id="cqSearchForm">
                    <div class="row">
                        <div class="col-xs-2">
                            <label>草权证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cqzh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>坐落：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="zl" class="form-control">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>承包面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="cbmj" class="form-control">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modelFooter">
                <button type="button" class="btn btn-sm btn-primary" id="cqGjSearchBtn">搜索</button>
            </div>
        </div>
    </div>
</div>
</@com.html>
