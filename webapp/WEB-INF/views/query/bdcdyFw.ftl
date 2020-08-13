<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    .modal-dialog {
        width: 1000px;
    }

    /*高级搜索样式添加 begin*/
    /*移动modal样式*/
    #gjSearchPop .modal-dialog {
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

    .modelFooter {
        background: none;
        border-top: 1px solid #e5e5e5;
        padding: 10px;
        text-align: center;
    }

    form input[type='text'], select, textarea {
        border-radius: 4px !important;
        width;
        100% !important;
        margin: 0px 5px 0px 0px;
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

    /*重写下拉列表高度*/
    .chosen-container > .chosen-single, [class*="chosen-container"] > .chosen-single {
        height: 34px;
    }
</style>
<script src="${bdcdjUrl}/static/js/icapturevideo.js"></script>
<script type="text/javascript">
    $(function () {
        //下拉框
        $('.chosen-select').chosen({allow_single_deselect: true, no_results_text: "无匹配数据", width: "100%"});
        $(window).on('resize.chosen', function () {
            $.each($('.chosen-select'), function (index, obj) {
                $(obj).next().css("width", 0);
                var w = $(obj).parent().width();
                $(obj).next().css("width", w);
            })
        }).trigger('resize.chosen');

        try {
            Capture = document.getElementById("Capture");//根据js的脚本内容，必须先获取object对象
            content = $("#search_xmmc");
        } catch (err) {
            alert("请安装Active X控件CaptureVideo.cab");
        }
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

        //zwq 回车键搜索
        $('input').focus(function () {
            $('#search_xmmc').keydown(function (event) {
                if (event.keyCode == 13) {
                    $("#search").click();
                }
            });

        });


        //项目表搜索事件
        $("#search").click(function () {
            var xmmc = $("#search_xmmc").val();
            var Url = "${bdcdjUrl}/queryBdcdy/bdcdyFwPagesJson";
            tableReload("grid-table", Url, {dcxc: xmmc});
        });

        //项目表高级查询的搜索按钮事件
        $("#gjSearchBtn").click(function () {
            var Url = "${bdcdjUrl}/queryBdcdy/bdcdyFwPagesJson?" + $("#gjSearchForm").serialize();
            tableReload("grid-table", Url, {dcxc: ""});
        });
        //拖拽功能
        $(".modal-header").mouseover(function () {
            $(this).css("cursor", "move");//改变鼠标指针的形状
        })
        $(".modal-header").mouseout(function () {
            $(".show").css("cursor", "default");
        })
        $(".gjSearchPop-modal").draggable({opacity: 0.7, handle: 'div.modal-header'});

        //项目高级搜索关闭事件
        $("#proHide").click(function () {
            $("#gjSearchPop").hide();
            $("#gjSearchForm")[0].reset();
            $(".chosen-select").trigger('chosen:updated');
        });
        //项目高级查询按钮点击事件
        $("#show").click(function () {
            $("#gjSearchPop").show();
            $(window).trigger('resize.chosen');
            $(".modal-dialog").css({"_margin-left": "25%"});
        });
    });
    /* 调用子页面方法  */
    function showModal() {
        $('#myModal').show();
        $('#modal-backdrop').show();
    }
    function hideModal() {
        $('#myModal').hide();
        $('#modal-backdrop').hide();
        $("#myModalFrame").attr("src", "${bdcdjUrl!}/bdcSjgl/toAddBdcxm");
    }
    var onmessage = function (e) {
        showModal();
    };

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

    $(function () {

        /*   文字水印  */
        $(".watermarkText").watermark();
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

        jQuery(grid_selector).jqGrid({
            url: "${bdcdjUrl}/queryBdcdy/bdcdyFwPagesJson",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'ID'},
            colNames: ['不动产单元号', '不动产类型', '权利人', '坐落', '登记状态', '权利状态', '查看','ID'],
            colModel:[
                {name:'BDCDYH', index:'BDCDYH', width:'20%', sortable:false, formatter:function (cellvalue, options, rowObject) {
                    var value = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                    return value;
                }},
                {name: 'BDCLX', index: 'BDCLX', width: '10%', sortable: false},
                {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                {name: 'DJZT', index: 'DJZT', width: '5%', sortable: false},
                {name: 'QLZT', index: 'QLZT', width: '6%', sortable: false},
                {
                    name: 'mydy',
                    index: '',
                    width: '10%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (rowObject.BDCLX == '土地房屋') {
                            return '<div style="margin-left:8px;">' +
                                    '<div title="分割土地权利" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="fgtdql(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-file-text fa-lg red"></span></div>' +
                                    '<div title="证书关系" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="hisRel(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-sitemap"></span></div>' +
                                    '</div>'
                        } else {
                            return '<div style="margin-left:8px;">' +
                                    '<div title="证书关系" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="hisRel(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="ace-icon fa fa-sitemap"></span></div>' +
                                    '</div>'
                        }

                    }
                },
                {name: "ID", index: 'ID', width: '6%', sortable: false,hidden:true}
            ],
            viewrecords:true,
            rowNum:10,
            rowList:[10, 20, 30],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            rownumbers:true,
            rownumWidth:50,
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    var replacement =
                    {
                        'ui-icon ui-icon-plus':'ace-icon fa fa-plus bigger-140'
                    };
                    $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                        var icon = $(this);
                        var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                        if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
                    })
                    $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
                }, 0);
                var jqData = $(grid_selector).jqGrid("getRowData");
                $.each(jqData, function (index, data) {
                    getBdcdyxx(data.ID, $(grid_selector));
                })
            },
            editurl:"", //nothing is saved
            caption:"",
            autowidth:true
        });

    });

    function Trim(str, is_global) {
        var result;
        result = str.replace(/(^\s+)|(\s+$)/g, "");
        if (is_global.toLowerCase() == "g") {
            result = result.replace(/\s/g, "");
        }
        return result;
    }
    //lcl 二次查询权利人信息
    function getBdcdyxx(id, table) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/queryBdcdy/getBdcdyxx?id=" + id ,
            dataType: "json",
            success: function (result) {
                if(result.QLR!=null){
                table.setCell(id, "QLR", result.QLR);
                }
                if(result.ZL!=null){
                    table.setCell(id, "ZL", result.ZL);
                }
                if(result.DJZT!=null){
                    table.setCell(id, "DJZT", result.DJZT);
                }
                var cellVal = '';
                if (result.zx && result.zx != undefined) {
                    cellVal += '<span class="label label-danger">注销</span>　';
                }else{
                    if (!result.cf && result.cf != undefined) {
                        cellVal += '<span class="label label-danger">查封</span>　';
                    }
                    if (!result.dy && result.dy != undefined) {
                        cellVal += '<span class="label label-warning">抵押</span>';
                    }
                    if ((result.dy || result.dy == undefined ) && ( result.cf || result.cf == undefined)) {
                        cellVal += '<span class="label label-success">正常</span>';
                    }
                }
                table.setCell(id, "QLZT", cellVal);

            }
        });
    }

    function fgtdql( bdcdyh) {
        if (bdcdyh != "") {
            var url = "${reportUrl!}/ReportServer?reportlet=edit/gd_fgql.cpt&op=write&bdcdyh="+bdcdyh;
            openWin(url);
        }
    }
    function hisRel( bdcdyh) {
        if (bdcdyh != "") {
            var url = "/workflowPlugin/index.html?url=" + "${bdcdjUrl!}" + "/bdcZsRel/getDyAllXmRelXml?bdcdyh=" + bdcdyh;
            openWin(url);
        }
    }

    function openWin(url, name) {
        var w_width = screen.availWidth - 10;
        var w_height = screen.availHeight - 32;
        window.open(url, name, "left=1,top=0,height=" + w_height + ",width=" + w_width + ",resizable=yes,scrollbars=yes");
    }
    function tableReload(table, Url, data) {
        var jqgrid = $("#" + table);
        jqgrid.setGridParam({url: Url, datatype: 'json', page: 1, postData: data});
        jqgrid.trigger("reloadGrid");//重新加载JqGrid
    }
</script>
<div class="main-container">
    <input type="hidden" id="proid" value="${proid!}">

    <div class="space-10"></div>
    <div class="page-content">
        <div class="space-4"></div>
        <div class="simpleSearch">
            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <input type="text" class="SSinput watermarkText" id="search_xmmc"
                               data-watermark="请输入身份证号/权利人/不动产单元号/不动产权证号/用途/坐落">
                    </td>
                    <td class="Search">
                        <a href="#" class="search" id="search">
                            搜索
                            <i class="ace-icon fa fa-search bigger-130"></i>
                        </a>
                    </td>
                    <#--<td>-->
                        <#--<button type="button" class="btn01 AdvancedButton" onclick="openDevices_onclick(0)">扫描</button>-->
                    <#--</td>-->
                    <#--<td style="border: 0px">&nbsp;</td>-->
                    <#--<td>-->
                        <#--<button type="button" class="btn01 AdvancedButton" id="show">高级搜索</button>-->
                    <#--</td>-->
                </tr>
            </table>
        </div>

        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<!--高级搜索-->

<div class="Pop-upBox bootbox modal fade bootbox-prompt in " style="display: none;" id="myModal">
    <div class="modal-dialog newPro-modal">
    </div>
</div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
