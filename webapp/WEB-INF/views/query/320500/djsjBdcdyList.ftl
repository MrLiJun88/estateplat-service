<@com.html title="不动产登记业务管理系统" import="ace,public,multiselect">
<style>
    a {
        color: #428bca;
    }

    .tab-content {
        overflow-y: auto;
        height: auto;
    }

    /*移动modal样式*/
    #djsjSearchPop .modal-dialog {
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

    /*移动modal样式*/
    #tipPop .modal-dialog {
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

    /*移动modal样式*/
    #ywsjSearchPop .modal-dialog {
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

    /*移动modal样式*/
    #fcsjSearchPop .modal-dialog {
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

    /*移动modal样式*/
    #tdsjSearchPop .modal-dialog {
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

    /*去掉表格横向滚动条*/
    /*.ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }*/

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

    .tooltips {
        border-width: 1px;
        border-style: solid;
        position: absolute;
        display: none;
        border-radius: 3px;
        opacity: 0;
        filter: alpha(opacity=0);
        z-index: 999;
    }

    .tooltips p.content {
        padding: 5px;
    }

    .tooltips .triangle-front, .tooltips .triangle-back {
        width: 0;
        height: 0;
        overflow: hidden;
        border-width: 8px;
        border-style: solid;
        position: absolute;
        border-color: transparent;
        top: 100%;
        left: 50%;
        margin-left: -8px;
    }

    .tooltips .triangle-back {
        margin-top: -1px;
    }

    .yellow {
        border-color: #c7c08d;
        background-color: #fffac3;
    }

    .yellow .triangle-front {
        border-top-color: #c7c08d;
    }

    .yellow .triangle-back {
        border-top-color: #fffac3;
    }
</style>
<script type="text/javascript">
    var checkboxSelect;
    $(function () {
        //时间控件
        $('.date-picker').datepicker({
            autoclose:true,
            todayHighlight:true,
            language:'zh-CN'
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
        $(function(){
            var fw_search = $("#fw_search_qlr").val();
            if(fw_search!==null && fw_search!==undefined && fw_search!==""){
                $("#fw_search").click();
            }
        });

        $(document).keydown(function (event) {
            if (event.keyCode == 13) { //绑定回车
            <#--if ($("#djsjTab").html()!=null&&!($("#djsjTab").html().indexOf("undefined") > -1)) {-->
            <#--var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";-->
            <#--tableReload("djsj-grid-table", djsjUrl, {dcxc:$("#djsj_search").val()});-->
            <#--}-->
            <#--if ($("#ywsjTab").html()!=null&&!($("#ywsjTab").html().indexOf("undefined") > -1)) {-->
            <#--var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}";-->
            <#--tableReload("ywsj-grid-table", ywsjUrl, {dcxc:$("#ywsj_search").val()});-->
            <#--}-->
            }
        });

        $("#djsjTab,#ywsjTab,#qlxxTab,#gdfcsjTab,#gdtdsjTab,#gdcfsjTab").click(function () {
            if (this.id == "djsjTab") {
                $("#djsj").addClass("active");
                $("#ywsjHide").click();
                //var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}";
                djsjInitTable();
                //tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
            } else if (this.id == "ywsjTab") {
                $("#ywsj").addClass("active");
                $("#djsjHide").click();
                $("#fcsjHide").click();
                $("#tdsjHide").click();
                //var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
                ywsjInitTable();
                // tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
            } else if (this.id == "qlxxTab") {
                $("#qlxx").addClass("active");
                $("#qlxxHide").click();
                //var qlxxUrl = "${bdcdjUrl}/selectBdcdy/getQlxxListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}${proid}&proid=${proid}&ysqlxdm=${ysqlxdm!}";
                qlxxInitTable();
                //tableReload("qlxx-grid-table", ywsjUrl, {dcxc: $("#qlxx_search").val()});
            } else if (this.id == "gdfcsjTab") {
                $("#gdfcsj").addClass("active");
                $("#ywsjHide").click();
                //var gdfcsjUrl = "${bdcdjUrl}/selectBdcdy/getGdfczListByPage?qllx=${qllx!}";
                gdfcInitTable();
                //tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: $("#gdfcsj_search").val()});
            } else if (this.id == "gdtdsjTab") {
                $("#gdtdsj").addClass("active");
                $("#ywsjHide").click();
                //var gdtdsjUrl = "${bdcdjUrl}/selectBdcdy/getGdtdzListByPage?qllx=${qllx!}";
                gdtdInitTable();
                //tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: $("#gdtdsj_search").val()});
            }else if (this.id == "gdcfsjTab") {
                $("#gdcfsj").addClass("active");
                $("#qlxxHide").click();
                //var gdcfsjUrl = "${bdcdjUrl}/selectBdcdy/getGdcfListByPage";
                gdcfInitTable();
                //tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: $("#gdcfsj_search").val()});
            }
        })


        //搜索事件
        $("#djsj_search_btn").click(function () {
            var bdclxZx = $("#bdclxZxSelect").val();
            var dcxc = $("#djsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入权利人/坐落/不动产单元号/房屋编号",1500);
            }else{
                var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&bdclxZx="+bdclxZx;
                tableReload("djsj-grid-table", djsjUrl, {dcxc: $("#djsj_search").val()});
            }
        })
        $("#ywsj_search_btn").click(function () {
            var zslx = $("#zslxSelect").val();
            var fzqssj = $("#fzqssj").val();
            var fzjssj = $("#fzjssj").val();
            var dcxc = $("#ywsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入不动产权证号/权利人/坐落/不动产单元号",1500);
            }else{
                var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}&zstype="+zslx+"&fzqssj="+fzqssj+"&fzjssj="+fzjssj;
                tableReload("ywsj-grid-table", ywsjUrl, {dcxc: $("#ywsj_search").val()});
            }
        })
        $("#gdfcsj_search_btn").click(function () {
            var dcxc = $("#gdfcsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入权利人/坐落/房产证号",1500);
            }else{
                var gdfcsjUrl = "${bdcdjUrl}/selectBdcdy/getGdfczListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}";
                tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: $("#gdfcsj_search").val()});
            }

        })
        $("#gdtdsj_search_btn").click(function () {
            var dcxc = $("#gdtdsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入权利人/坐落/土地证号",1500);
            }else{
                var gdtdsjUrl = "${bdcdjUrl}/selectBdcdy/getGdtdzListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}";
                tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: $("#gdtdsj_search").val()});
            }

        })
        $("#qlxx_search_btn").click(function () {
            var dcxc = $("#qlxx_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入被查封权利人/坐落/不动产单元号/查封文号",1500);
            }else{
                var qlxxUrl = "${bdcdjUrl}/selectBdcdy/getQlxxListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}";
                tableReload("qlxx-grid-table", qlxxUrl, {dcxc: $("#qlxx_search").val()});
            }

        })

        $("#gdcfsj_search_btn").click(function () {
            var dcxc = $("#gdcfsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入查封文号/坐落",1500);
            }else{
                var gdcfsjUrl = "${bdcdjUrl}/selectBdcdy/getGdcfListByPage?proid=${proid!}";
                tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: $("#gdcfsj_search").val()});
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
            var djsjUrl = "${bdcdjUrl}/selectBdcdy/getDjsjBdcdyPagesJson?zdtzm=${zdtzm!}&dyfs=${dyfs!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&" + $("#djsjSearchForm").serialize();
            tableReload("djsj-grid-table", djsjUrl, {dcxc: ""});
            $("#djsjSearchPop").hide();
            $("#djsjSearchForm")[0].reset();
        })

        //土地高级查询的搜索按钮事件
        $("#ywsjGjSearchBtn").click(function () {
            var ywsjUrl = "${bdcdjUrl}/selectBdcdy/getBdczsListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&qlxzdm=${qlxzdm!}&bdclxdm=${bdclxdm!}&proid=${proid}&ysqlxdm=${ysqlxdm!}&" + $("#ywsjSearchForm").serialize();
            tableReload("ywsj-grid-table", ywsjUrl, {dcxc: ""});
            $("#ywsjSearchPop").hide();
            $("#ywsjSearchForm")[0].reset();
        })
        //土地高级查询的搜索按钮事件
        $("#tdsjGjSearchBtn").click(function () {
            var gdtdsjUrl = "${bdcdjUrl}/selectBdcdy/getGdtdzListByPage?proid=" + $("#proid").val() + "&" + $("#tdsjSearchForm").serialize();
            tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: ""});
            $("#tdsjSearchPop").hide();
            $("#tdsjSearchForm")[0].reset();
        })

        //房产高级查询的搜索按钮事件
        $("#fcsjGjSearchBtn").click(function () {
            var gdfcsjUrl = "${bdcdjUrl}/selectBdcdy/getGdfczListByPage?proid=" + $("#proid").val() + "&" + $("#fcsjSearchForm").serialize();
            tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: ""});
            $("#fcsjSearchPop").hide();
            $("#fcsjSearchForm")[0].reset();
        })

        $("#cfsjGjSearchBtn").click(function () {
            var gdcfsjUrl = "${bdcdjUrl}/selectBdcdy/getGdcfListByPage?proid=${proid!}&" + $("#cfsjSearchForm").serialize();
            tableReload("gdcfsj-grid-table", gdcfsjUrl, {dcxc: ""});
        })

        $("#djsjHide,#ywsjHide,#fcsjHide,#tdsjHide,#tipHide,#tipCloseBtn").click(function () {
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
            }
        });
        $(".djsjSearchPop-modal, .ywsjSearchPop-modal, .fcsjSearchPop-modal, .tdsjSearchPop-modal").draggable({
            opacity: 0.7,
            handle: 'div.modal-header'
        });
        //默认初始化表格
        if ("${bdcdyly!}" == '2' || "${bdcdyly!}" == '0' || "${bdcdyly!}" == '4') {
            djsjInitTable();
        } else if ("${bdcdyly!}" == '1' || "${bdcdyly!}" == '7') {
            if("${sqlxdm!}" == "4009903"){
                gdfcInitTable();
            }else{
                ywsjInitTable();
            }
        } else if ("${bdcdyly!}" == '3' || "${bdcdyly!}" == '9') {
            qlxxInitTable();
        } else if ("${bdcdyly!}" == '5') {
            gdfcInitTable();
        } else if ("${bdcdyly!}" == '6') {
            gdtdInitTable();
        } else if ("${bdcdyly!}" == '8') {
            if("${sqlxdm!}" != "4009901" && "${sqlxdm!}" != "4009902"){
                ywsjInitTable();
            }else{
                gdfcInitTable();
            }
        }

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
            colNames: ['地籍号', '不动产单元号', '坐落', '权利人', '不动产类型', 'ID'],
            colModel: [
                {
                    name: 'DJH',
                    index: 'DJH',
                    width: '18%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue != null && cellvalue != '')
                            cell = '<a href="javascript:djsjEditXm(\'' + rowObject.ID + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.BDCDYH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                        else
                            cell = '';
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
                        }
                        else
                            cell = '';
                        return cell;
                    }
                },
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
                        return value;
                    }
                },
                /*{name:'PPTZS', index:'PPTZS', width:'10%', sortable:false , formatter:function (cellvalue, options, rowObject){
                    cell='<div title="匹配通知书" style="float:left;cursor:pointer;margin-left: 8px" class="ui-pg-div ui-inline-edit" id="" onclick="getPptzs(\'' + rowObject.BDCDYH + '\')" onmouseover="jQuery(this).addClass(\'ui-state-hover\');" onmouseout="jQuery(this).removeClass(\'ui-state-hover\');"><span class="fa fa-search fa-lg blue"></span></div>' ;
                    return cell;
                }},*/
                {name: 'ID', index: 'ID', width: '10%', sortable: false, hidden: true}
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
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                }, 0);
                //如果7条设置宽度为auto,如果少于7条就设置固定高度
                if ($(grid_selector).jqGrid("getRowData").length == 7) {
                    $(grid_selector).jqGrid("setGridHeight", "auto");
                } else {
                    $(grid_selector).jqGrid("setGridHeight", "275px");
                }
                qlrForTable(grid_selector, "${bdclxdm!}", "${zdtzm!}");

                var jqData = $(grid_selector).jqGrid("getRowData");
                var url = $(grid_selector).getGridParam("url");
                if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                    $.Prompt("未搜索到该数据，请核实！",1500);
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
            colNames: ["产权证号","PROID","BDCDYID","不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间'],
            colModel: [
                {name: 'CQZH', index: 'CQZH', width: '10%', sortable: false},
                {name: 'PROID', index: 'PROID', width: '10%',hidden:true,sortable: false},
                {name: 'BDCDYID', index: 'BDCDYID', width: '10%', hidden:true,sortable: false},
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
                {name: 'CFLX', index: 'CFLX', width: '6%', sortable: false},
                {name: 'CFJG', index: 'CFJG', width: '6%', sortable: false},
                {name: 'CFWH', index: 'CFWG', width: '6%', sortable: false},
                {name: 'CFSQR', index: 'CFSQR', width: '6%', sortable: false},
                {name: 'BCFQLR', index: 'BCFQLR', width: '6%', sortable: false},
                {name: 'CFKSQX', index: 'CFKSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}},
                {name: 'CFJSQX', index: 'CFJSQX', width: '6%', sortable: false, formatoptions: {newformat: 'Y-m-d'}}
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
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
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
                    getGdCfCqzh(data.PROID,data.BDCDYID,"",$(grid_selector));
                    getSdStatus(data.PROID,data.CQZH);
                });
                if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                    $.Prompt("未搜索到该数据，请核实！",1500);
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
            colNames: ['产权证号',"BDCDYID","不动产单元号", '坐落', '类型', '查封机关', '查封文号', '查封申请人', '被查封权利人', '查封开始时间', '查封结束时间','不动产类型', 'GDPROID',  'QLID'],
            colModel: [
                {name: 'CQZH', index: 'CQZH', width: '6%', sortable: false},
                {name: 'BDCDYID', index: 'BDCDYID', width: '6%',hidden:true, sortable: false},
                {
                    name: 'BDCDYH',
                    index: 'BDCDYH',
                    width: '15%',
                    sortable: false,
                    formatter: function (cellvalue, options, rowObject) {
                        if (cellvalue != null && cellvalue != '') {
                            cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                            cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\''  + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID +  '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
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
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
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
                    getGdCfQlrmc(data.QLID,data.GDPROID,data.QLID, $(grid_selector));
                    getGdCfCqzh(data.QLID,data.BDCDYID,data.GDPROID,$(grid_selector));
                    getSdStatus(data.QLID,data.CQZH);
                });
                if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                    $.Prompt("未搜索到该数据，请核实！",1500);
                }
            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


    function getGdCfQlrmcAndZl(rowid,gdproid,qlid,table){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getGdCfQlrmcAndZl?gdproid=" + gdproid + "&qlid=" + qlid,
            success: function (result) {
                table.setCell(rowid, "ZL", result.zl);
                table.setCell(rowid, "BCFQLR", result.qlr);
            }
        });
    }

    function getGdCfQlrmc(rowid,gdproid,qlid,table){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getGdCfQlrmcAndZl?gdproid=" + gdproid + "&qlid=" + qlid,
            success: function (result) {
                table.setCell(rowid, "BCFQLR", result.qlr);
            }
        });
    }

    function getGdCfCqzh(rowid,bdcdyid,gdproid,table){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getGdCfCqzh?bdcdyid=" + bdcdyid + "&gdproid=" + rowid,
            success: function (result) {
                debugger;
                table.setCell(rowid,"CQZH", result.cqzh);
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
            colNames: ['不动产单元号', '不动产权证号', '坐落', '权利人', '不动产类型','PROID'],
            colModel: [
                {
                    name: 'BDCDYH',
                    index: '',
                    width: '12%',
                    sortable: false
                },
                {
                    name: 'BDCQZH',
                    index: 'BDCQZH',
                    width: '11%',
                    sortable: false
                },
                {name: 'ZL', index: '', width: '10%', sortable: false},
                {name: 'QLR', index: '', width: '6%', sortable: false},
                {name: 'BDCLX', index: '', width: '6%', sortable: false, hidden: true},
                {name: 'PROID', index: 'PROID', width: '6%', sortable: false, hidden: true}
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
                    $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
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
                    getQlrmcAndZl(data.PROID,data.BDCQZH, $(grid_selector),data.PROID);
                    getSdStatus(data.PROID,data.BDCQZH);
                });

                if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                    $.Prompt("未搜索到该数据，请核实！",1500);
                }

            }/*,
            ondblClickRow:function (rowid) {
                var rowData = $(grid_selector).getRowData(rowid);
                if(rowData!=null)
                    ywsjEditXm(rowid,rowData.BDCDYH);
            }*/,
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }

    //jyl 获取权利人名称和座落
    //获取数据
    function getQlrmcAndZl(proid,bdcqzh, table, rowid) {
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getdateByProid?proid=" + proid,
            success: function (result) {
                table.setCell(rowid, "ZL", result.zl);
                table.setCell(rowid, "QLR", result.qlr);
                table.setCell(rowid, "BDCDYH", result.bdcdyh);
                table.setCell(rowid, "BDCLX", result.bdclx);
                var bdcdyCellVal = "";
                bdcdyCellVal = '<a href="javascript:ywsjEditXm(\'' + proid + '\',\'' + result.bdcdyh + '\',\'' + result.bdcdyid + '\',\'' + bdcqzh + '\',\'' + result.bdclx+ '\')" title="' + result.bdcdyh + '" >' + result.bdcdyh+ "</a>";
                table.setCell(rowid, "BDCDYH", bdcdyCellVal);
                var bdcqzhCellVal = "";
                bdcqzhCellVal = '<a href="javascript:ywsjEditXm(\'' + proid + '\',\'' + result.bdcdyh + '\',\'' + result.bdcdyid + '\',\'' + bdcqzh + '\',\'' + result.bdclx+ '\')" title="' + bdcqzh + '" >' + bdcqzh+ "</a>";
                table.setCell(rowid, "BDCQZH", bdcqzhCellVal);
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
        var sqlxdm = ${sqlxdm!};
        if(sqlxdm == "8009901" || sqlxdm == "8009902" || sqlxdm == "4009903" || sqlxdm == "4009901" || sqlxdm == "4009902" || sqlxdm == "8009903" || sqlxdm == "8009904"){
            jQuery(grid_selector).jqGrid({
                datatype: "local",
                height: 'auto',
                jsonReader: {id: 'BDCID'},
                colNames: ["不动产单元号", '房产证号', 'TFCZH', '坐落', '权利人','不动产类型', 'GDPROID', 'DJID', 'QLID', 'BDCID','ID'],
                colModel: [
                    {
                        name: 'BDCDYH',
                        index: 'BDCDYH',
                        width: '12%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '') {
                                cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID+ '\',\'' + rowObject.TFCZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            } else
                                cell = '';
                            return cell;
                        }
                    },
                    {
                        name: 'FCZH',
                        index: 'FCZH',
                        width: '8%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '')
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID+ '\', \'' + rowObject.TFCZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            else
                                cell = '';
                            return cell;
                        }
                    },
                    {name: 'TFCZH', index: 'TFCZH', width: '5%', sortable: false,hidden:true},
                    {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                    {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                    {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                    {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                    {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                    {name: 'BDCID', index: 'BDCID', width: '6%', sortable: false, hidden: true},
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
                        $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                    }, 0);
                    //如果7条设置宽度为auto,如果少于7条就设置固定高度
                    if ($(grid_selector).jqGrid("getRowData").length == 7) {
                        $(grid_selector).jqGrid("setGridHeight", "100%");
                    } else {
                        $(grid_selector).jqGrid("setGridHeight", "275px");
                    }
                    dateForTable(grid_selector,"fc");
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    var url = $(grid_selector).getGridParam("url");
                    if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                        $.Prompt("未搜索到该数据，请核实！",1500);
                    }
                    $.each(jqData, function (index, data) {
                        getSdStatus(data.BDCID,data.FCZH);
                    });
                },
                editurl: "", //nothing is saved
                caption: "",
                autowidth: true
            });
        }else{
            jQuery(grid_selector).jqGrid({
                datatype: "local",
                height: 'auto',
                jsonReader: {id: 'BDCID'},
                colNames: ["不动产单元号", '房产证号','TFCZH','坐落', '权利人', '匹配状态','不动产类型', 'GDPROID', 'DJID', 'QLID', 'BDCID','ID'],
                colModel: [
                    {
                        name: 'BDCDYH',
                        index: 'BDCDYH',
                        width: '12%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '') {
                                cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID+ '\' + \'' + rowObject.TFCZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            } else
                                cell = '';
                            return cell;
                        }
                    },
//                    {name: 'TBDCDYH', index: 'TBDCDYH', width: '5%', sortable: false,hidden:true},
                    {
                        name: 'FCZH',
                        index: 'FCZH',
                        width: '8%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != ''){
                                if(rowObject.BDCDYH == "" || rowObject.BDCDYH == null || rowObject.BDCDYH == "undefined"){
                                    cell = cellvalue ;
                                }else{
                                    cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\' + \''+ rowObject.BDCID + '\',\'' + rowObject.TFCZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                                }
                            } else{
                                cell = '';
                            }
                            return cell;
                        }
                    },
                    {name: 'TFCZH', index: 'TFCZH', width: '5%', sortable: false,hidden:true},
                    {name: 'ZL', index: 'ZL', width: '15%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '10%', sortable: false},
                    {name: 'PPZT', index: '', width: '4%', sortable: false},
                    {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                    {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                    {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                    {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                    {name: 'BDCID', index: 'BDCID', width: '6%', sortable: false, hidden: true},
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
                        $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                    }, 0);
                    //如果7条设置宽度为auto,如果少于7条就设置固定高度
                    if ($(grid_selector).jqGrid("getRowData").length == 7) {
                        $(grid_selector).jqGrid("setGridHeight", "100%");
                    } else {
                        $(grid_selector).jqGrid("setGridHeight", "275px");
                    }
                    dateForTable(grid_selector,"fc");
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    var url = $(grid_selector).getGridParam("url");
                    if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                        $.Prompt("未搜索到该数据，请核实！",1500);
                    }
                    $.each(jqData, function (index, data) {
                        getSdStatus(data.BDCID,data.FCZH);
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
        var sqlxdm = ${sqlxdm!};
        if(sqlxdm == "8009901" || sqlxdm == "8009902" || sqlxdm == "4009903" || sqlxdm == "4009901" || sqlxdm == "4009902" || sqlxdm == "8009903" || sqlxdm == "8009904") {
            jQuery(grid_selector).jqGrid({
                datatype: "local",
                height: 'auto',
                jsonReader: {id: 'QLID'},
                colNames: ["不动产单元号", '土地证号','TTDZH', '坐落', '权利人','不动产类型', 'GDPROID', 'DJID', 'QLID', 'bdcid'],
                colModel: [
                    {
                        name: 'BDCDYH',
                        index: 'BDCDYH',
                        width: '12%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '') {
                                cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\' + \''+ rowObject.BDCID + '\',\'' + rowObject.TTDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            } else
                                cell = '';
                            return cell;
                        }
                    },
                    {
                        name: 'TDZH',
                        index: 'TDZH',
                        width: '11%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '')
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\'' + rowObject.BDCID + '\',\''+ rowObject.TTDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            else
                                cell = '';
                            return cell;
                        }
                    },
                    {name: 'TTDZH', index: 'TTDZH', width: '5%', sortable: false,hidden:true},
                    {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
                    {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                    {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                    {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                    {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                    {name: 'BDCID', index: 'BDCID', width: '6%', sortable: false, hidden: true}
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
                        $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                    }, 0);
                    //如果7条设置宽度为auto,如果少于7条就设置固定高度
                    if ($(grid_selector).jqGrid("getRowData").length == 7) {
                        $(grid_selector).jqGrid("setGridHeight", "100%");
                    } else {
                        $(grid_selector).jqGrid("setGridHeight", "275px");
                    }
                    dateForTable(grid_selector,"unPptd");
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    var url = $(grid_selector).getGridParam("url");
                    if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                        $.Prompt("未搜索到该数据，请核实！",1500);
                    }
                    $.each(jqData, function (index, data) {
                        getSdStatus(data.QLID,data.TDZH);
                    });
                },
                editurl: "", //nothing is saved
                caption: "",
                autowidth: true
            });
        }else{
            jQuery(grid_selector).jqGrid({
                datatype: "local",
                height: 'auto',
                jsonReader: {id: 'QLID'},
                colNames: ["不动产单元号",'土地证号','TTDZH', '坐落', '权利人','匹配状态','不动产类型', 'GDPROID', 'DJID', 'QLID', 'bdcid'],
                colModel: [
                    {
                        name: 'BDCDYH',
                        index: 'BDCDYH',
                        width: '12%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != '') {
                                cellvalue = cellvalue.substr(0, 6) + " " + cellvalue.substr(6, 6) + " " + cellvalue.substr(12, 7) + " " + cellvalue.substr(19);
                                cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\''+ rowObject.BDCID + '\' + \'' + rowObject.TTDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                            } else
                                cell = '';
                            return cell;
                        }
                    },
//                    {name: 'TBDCDYH', index: 'TBDCDYH', width: '5%', sortable: false,hidden:true},
                    {
                        name: 'TDZH',
                        index: 'TDZH',
                        width: '11%',
                        sortable: false,
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue != null && cellvalue != ''){
                                if((rowObject.BDCDYH == "" || rowObject.BDCDYH == null || rowObject.BDCDYH == "undefined")&&sqlxdm!="9920001"&&sqlxdm!="9920005"){
                                    cell = cellvalue ;
                                }else if(sqlxdm=="9920001"||sqlxdm=="9920005"){
                                    cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\''+ rowObject.BDCID +  '\',\'' + rowObject.TTDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                                }else{
                                    cell = '<a href="javascript:gdsjEditXm(\'' + rowObject.QLID + '\',\'' + rowObject.BDCDYH + '\',\'' + rowObject.BDCLX + '\',\'' + rowObject.ZL + '\',\'' + rowObject.GDPROID + '\',\'' + rowObject.DJID + '\',\''+ rowObject.BDCID +  '\',\'' + rowObject.TTDZH + '\')" title="' + cellvalue + '" >' + cellvalue + "</a>";
                                }
                            } else{
                                cell = '';
                            }
                            return cell;
                        }
                    },
                    {name: 'TTDZH', index: 'TTDZH', width: '5%', sortable: false,hidden:true},
                    {name: 'ZL', index: 'ZL', width: '10%', sortable: false},
                    {name: 'QLR', index: 'QLR', width: '6%', sortable: false},
                    {name: 'PPZT', index: '', width: '4%', sortable: false},
                    {name: 'BDCLX', index: 'BDCLX', width: '6%', sortable: false, hidden: true},
                    {name: 'GDPROID', index: 'GDPROID', width: '6%', sortable: false, hidden: true},
                    {name: 'DJID', index: 'DJID', width: '6%', sortable: false, hidden: true},
                    {name: 'QLID', index: 'QLID', width: '6%', sortable: false, hidden: true},
                    {name: 'BDCID', index: 'BDCID', width: '6%', sortable: false, hidden: true}
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
                        $(grid_selector).jqGrid('setGridWidth', $("#mainContent").width());
                    }, 0);
                    //如果7条设置宽度为auto,如果少于7条就设置固定高度
                    if ($(grid_selector).jqGrid("getRowData").length == 7) {
                        $(grid_selector).jqGrid("setGridHeight", "100%");
                    } else {
                        $(grid_selector).jqGrid("setGridHeight", "275px");
                    }
                    dateForTable(grid_selector,"td");
                    var jqData = $(grid_selector).jqGrid("getRowData");
                    var url = $(grid_selector).getGridParam("url");
                    if(url != "" && url != null && url != "undefined"&&(jqData == null||jqData.length == 0)){
                        $.Prompt("未搜索到该数据，请核实！",1500);
                    }
                    $.each(jqData, function (index, data) {
                        getSdStatus(data.QLID,data.TDZH);
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
            url: '${bdcdjUrl}/wfProject/checkBdcXm',
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
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><!--<span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span>--><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if (isNotBlank(item.wiid)){
                                islw = true;
                                confirmCreateLw(item, "${bdcdjUrl}", "${sflw}","djsjBdcdyList");
                            }else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
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
                    if($("button[name='hlBtn']").length == 0) {
                        $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                    }
                    $("button[name='hlBtn']").click(function () {
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
        if ("${glbdcdy!}" == "true") {
            initurl = '${bdcdjUrl}/wfProject/glBdcdy?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh;
        } else {
            initurl = '${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + '&djId=' + id + "&bdclx=" + bdclx + "&bdcdyh=" + bdcdyh;
        }
        $.ajax({
            type: 'post',
            url: initurl,
            success: function (data) {

                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        async: true,
                        url: '${bdcdjUrl}/wfProject/updateWorkFlow?proid=' + proid,
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
                    $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                /*if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }*/
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
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
        var plchoseone = "${plChoseOne!}";
        var url = "${bdcdjUrl}/wfProject/checkBdcXm";
        if (plchoseone == "1") {
            url = "${bdcdjUrl}/wfProject/checkBdcXmForDyChoseOne";
        }
        var options = {
            url: url,
            type: 'post',
            dataType: 'json',
            data: {proid: proid, yxmid: id, bdcdyh: bdcdyh},
            success: function (data) {
                var alertSize = 0;
                var confirmSize = 0;
                var ysqlx = '${sqlx}';
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
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><!--<span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span>--><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "confirmAndCreate") {
                            confirmSize++;
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in"  name="createBtn"  onclick="createProjectInfo(\'' + id + '\',\'' + item.createSqlxdm + '\',\'' + bdcdyh + '\',\'' + bdcdyid + '\',\'' + alertCount + '\')">创建</span><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + id + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if (isNotBlank(item.wiid)) {
                                islw = true;
                                confirmCreateLw(item, "${bdcdjUrl}", "${sflw}","djsjBdcdyList");
                            }else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids + '\')" >查看</span>' + item.checkMsg + '</div>');
                            }
                        }
                    })
                    //去掉遮罩
                    setTimeout($.unblockUI, 10);
                    if (!islw) {
                        $("#tipPop").show();
                        $("#modal-backdrop").show();
                    }
                }
                if (alertSize == 0 && confirmSize == 0) {
                    ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx);
                } else if (alertSize == 0 && confirmSize > 0) {
                    if($("button[name='hlBtn']").length == 0) {
                        $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                    }
                    $("button[name='hlBtn']").click(function () {
                        ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx);
                    })
                }
            },
            error: function (data) {
                //去掉遮罩
                setTimeout($.unblockUI, 10);
                $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
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
            url: "${bdcdjUrl}/bdcFwfsss/getFsssBdcXm?proid=" + yproid,
            async: false,
            dataType: "json",
            success: function (result) {
                if (result != null && result != "" && result != undefined) {
                    var yxmids = result.yxmids;
                    var  bdcdyhs = result.bdcdyhs;
                    var yxmidArr = new Array();
                    var bdcdyhArr = new Array();
                    if(yxmids!=""&&yxmids!=null){
                        yxmidArr=yxmids.split(",");
                    }
                    if(bdcdyhs!=""&&bdcdyhs!=null){
                        bdcdyhArr=bdcdyhs.split(",");
                    }
                    if(yxmidArr.length!=0&&bdcdyhArr.length!=0){
                        for(var i=0;i<yxmidArr.length;i++){
                            var id=yxmidArr[i];
                            var bdcdyh=bdcdyhArr[i];
                            var plchoseone = "${plChoseOne!}";
                            var url = "${bdcdjUrl}/wfProject/checkBdcXm";
                            if (plchoseone == "1") {
                                url = "${bdcdjUrl}/wfProject/checkBdcXmForDyChoseOne";
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
                                    var ysqlx = '${sqlx}';
                                    if (ysqlx == 'JF') {
                                        sqlx = 'JF';
                                    }else{
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
                                                $("#csdjConfirmInfo").append('<div class="alert alert-warning"><!--<span style="cursor:pointer" class="pull-right label label-sm label-primary arrowed-in" data-dismiss="alert" name="hlBtn">忽略</span>--><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids + '\')">查看</span>' + item.checkMsg + '</div>');
                                            } else if (item.checkModel == "alert"||item.checkModel == "confirmAndCreate") {
                                                alertSize++;
                                                if (isNotBlank(item.wiid)) {
                                                    islw = true;
                                                    confirmCreateLw(item, "${bdcdjUrl}", "${sflw}","djsjBdcdyList");
                                                }else {
                                                    $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.checkPorids + '\')" >查看</span>' + item.checkMsg + '</div>');
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
                                    }else if(alertSize == 0 && confirmSize > 0){
                                        if($("button[name='hlBtn']").length == 0) {
                                            $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                                        }
                                        $("button[name='hlBtn']").click(function () {
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
            ,error: function (result) {
            }
        });
        $.ajax({
            type: "post",
            async: false,
            url: "${bdcdjUrl}/bdcFwfsss/initBdcFwfsssFromFsss?proid=" + proid+"&yproid="+yproid,
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
            url: "${bdcdjUrl}/bdcFwfsss/initBdcFwfsssNotInherit?proid=" +proid+"&yproid="+yproid,
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
    function fsssInheritForGd(proid){
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
    function fsssNotInheritForGd(proid){
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
            url: "${bdcdjUrl}/bdcFwfsss/initBdcFwfsssFromFsss?proid=" + proid+"&yproid="+yproid,
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
    function gdsjEditXm(qlid, bdcdyh, bdclx, zl, gdproid, djid,bdcid,bdcqzh) {
        //遮罩
        $.blockUI({message: "请稍等……"});
        var bdcdyh=(bdcdyh == null || bdcdyh == "undefined")?"":bdcdyh;
        $("#qlid").val(qlid);
        $("#bdcdyh").val(bdcdyh);
        $("#bdclx").val(bdclx);
        $("#gdproid").val(gdproid);
        $("#xmmc").val(zl);
        $("#djlx").val('${djlx!}');
        $("#workFlowDefId").val('${workFlowDefId!}');
        $("#djId").val(djid);
        $("#sqlx").val('${sqlxmc!}');
        $("#proids").val('${proid}');
        $("#ybdcqzh").val(bdcqzh);
        $.ajax({
            url: '${bdcdjUrl}/bdcJgSjgl/isCancelAll?bdclx=' + bdclx,
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
                            $("#csdjConfirmInfo").append('<div class="alert alert-warning"><span style="cursor:pointer" class="label label-sm label-primary arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')">查看</span>' + item.checkMsg + '</div>');
                        } else if (item.checkModel == "alert") {
                            alertSize++;
                            if (isNotBlank(item.wiid)){
                                islw = true;
                                confirmCreateLw(item, "${bdcdjUrl}", "${sflw}","djsjBdcdyList");
                            }else {
                                $("#csdjAlertInfo").append('<div class="alert alert-danger"><span style="cursor:pointer" class="label label-sm label-primary arrowed arrowed-right pull-right" onclick="openProjectInfo(\'' + item.info + '\')" >查看</span>' + item.checkMsg + '</div>');
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
                    if($("button[name='hlBtn']").length == 0) {
                        $("#footer").append('<button name="hlBtn" type="button" class="btn btn-sm btn-primary">忽略</button>');
                    }
                    $("button[name='hlBtn']").click(function () {
                        createXm(gdproid, bdcdyh, qlid, bdclx, djid);
                    })
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
            }
        });
    }
    var createXm = function (gdproid, bdcdyh, qlid, bdclx, djid) {
        $.ajax({
            url: '${bdcdjUrl}/bdcJgSjgl/createCsdj?bdclx=' + bdclx,
            type: 'post',
            dataType: 'json',
            data: $("#form").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                var id = "${proid!}";
                if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined") && data.taskid != null && data.taskid != "") {
                    var check = checkExistFsssForGd(id);
                    if(check == "true"){
                        //抵押，查封，合并流程弹出提示
                        var djlx = "${djlx!}";
                        var qllx = "${qllx!}";
                        var sqlxdm = "${sqlxdm!}";
                        if(sqlxdm=="801"||sqlxdm=="1019"||sqlxdm=="8009901"||djlx=="999"){
                            var msg = "主房存在附属设施，是否继承到抵押或查封项目之中";
                            showConfirmDialogWithClose("提示信息", msg, "fsssInheritForGd", "'" + id + "'", "fsssNotInheritForGd", "'" + id + "'");
                        }else{
                            getBdcXtConfigFj();
                            hideModelAndRefreshParant();
                        }
                    }else{
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();
                    }
                } else if (data != null && (data.msg == null || data.msg == "" || data.msg == "undefined")) {
                    var check = checkExistFsssForGd(id);
                    if(check == "true"){
                        //抵押，查封，合并流程弹出提示
                        var djlx = "${djlx!}";
                        var qllx = "${qllx!}";
                        var sqlxdm = "${sqlxdm!}";
                        if(sqlxdm=="801"||sqlxdm=="1019"||sqlxdm=="8009901"||djlx=="999"){
                            var msg = "主房存在附属设施，是否继承到抵押或查封项目之中";
                            showConfirmDialogWithClose("提示信息", msg, "fsssInheritForGd", "'" + id + "'", "fsssNotInheritForGd", "'" + id + "'");
                        }else{
                            getBdcXtConfigFj();
                            hideModelAndRefreshParant();
                        }
                    }else{
                        getBdcXtConfigFj();
                        hideModelAndRefreshParant();
                    }
                } else {
                    $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
            }
        });
    }


    function  checkZybdcZsbh(proid,id){
        $.ajax({
            type:'post',
            url:'${bdcdjUrl}/selectBdcdy/checkZybdcZsbh',
            datatype: 'json',
            data:{proid:proid,yxmid:id},
            success:function (data) {
                if(data != null && data != ""){
                    $.Prompt(data,1500);
                }
            }
        });

    }
    function ywsjInitVoFromOldData(proid, id, bdcdyh, bdcdyid, bdcqzh, bdclx) {
        checkZybdcZsbh(proid,id);
        var initurl = "";
        if ("${glzs!}" == "true") {
            initurl = '${bdcdjUrl}/wfProject/glZs?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx;
        } else {
            initurl = '${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid + "&ybdcqzh=" + encodeURI(bdcqzh) + "&bdclx=" + bdclx;
        }
        $.ajax({
            type: 'post',
            //url:'${bdcdjUrl}/wfProject/initVoFromOldData?proid=' + proid + "&bdcdyh=" + bdcdyh + "&yxmid=" + id + "&ybdcdyid=" + bdcdyid+"&ybdcqzh="+encodeURI(bdcqzh)+"&bdclx="+bdclx,
            url: initurl,
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (data == '成功') {
                    $.ajax({
                        type: 'get',
                        async: true,
                        url: '${bdcdjUrl!}/wfProject/updateWorkFlow?proid=' + proid,
                        success: function (data) {

                        }
                    });
                    //jyl 检验主房是否存在附属设施，并判断是否继承
                    if(bdclx=="TDFW") {
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
                            var djlx = ${djlx!};
                            var sqlxdm = ${sqlxdm!};
                            if(sqlxdm=="801"||sqlxdm=="1019"||djlx=="999"){
                                var msg = "主房存在附属设施，是否继承到抵押或查封项目之中"
                                showConfirmDialogWithClose("提示信息", msg, "fsssInherit", "'" + id + "'", "fsssNotInherit", "'" + id + "'");
                            }else{
                                fsssInherit(id);
                                getBdcXtConfigFj();
                                hideModelAndRefreshParant();
                            }
                        }else{
                            hideModelAndRefreshParant();
                        }
                    }else{
                        hideModelAndRefreshParant();
                    }

                } else {
                    //alert(data);
                    $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
                }
            },
            error: function (data) {
                setTimeout($.unblockUI, 10);
                $.Prompt("该选择数据创建项目出现问题，请及时与系统管理员联系！",1500);
                /*if (XMLHttpRequest.readyState == 4) {
                    alert("保存失败!");
                }*/
            }
        });
    }

    function getBdcXtConfigFj(){
        $.ajax({
            type:'get',
            url: '${bdcdjUrl!}/wfProject/getBdcXtConfigFj?wiid=' + '${wiid!}',
            datatype: 'json',
            success: function () {
            },
            error: function () {
            }
        });
    }

    function hideModelAndRefreshParant(){
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
            url:'${bdcdjUrl}/bdcFwfsss/initVoFromFsss?proid=' + proid + "&ybdcdyh=" + ybdcdyh + "&yproid=" + yproid,
            success: function (data) {

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (XMLHttpRequest.readyState == 4) {
                    $.Prompt("保存失败!",1500);
                }
            }
        });
    }

    //检查一证多房是否存在附属设施
    function checkExistFsssForGd(proid){
        var check='';
        $.ajax({
            url: "${bdcdjUrl}/bdcFwfsss/checkExistFsssForGd?proid="+proid,
            type: 'GET',
            async:false,
            success: function (result) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(result)) {
                    if(result.msg=='exist'){
                        check='true';
                    }
                }
            },
            error: function (data) {
                $.Prompt("检查失败",1500);
            }
        });
        return check;
    }

    //检查主房不动产状态
    function checkExistFsss(proid){
        var check='';
        $.ajax({
            url: "${bdcdjUrl}/bdcFwfsss/checkExistFsss?proid="+proid,
            type: 'GET',
            async:false,
            success: function (result) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(result)) {
                    if(result.msg=='exist'){
                        check='true';
                    }
                }
            },
            error: function (data) {
                $.Prompt("检查失败",1500);
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
                    $.Prompt("查看失败！",1500);
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
            $.Prompt("验证不通过，不能创建项目！",1500);
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
                    url: "${bdcdjUrl!}/wfProject/createLhcfByProid?proid=" + proid + "&yproid=" + yproid + "&createSqlxdm=" + sqlxdm + "&bdcdyh=" + bdcdyh + "&bdcdyid=" + bdcdyid,
                    success: function (result) {
                        if (result != null && result != "" && taskid != null && taskid != "") {
                            $.ajax({
                                type: "post",
                                url: "${platformUrl!}/task!del.action?taskid=" + taskid,
                                success: function (data) {
                                    if (data.indexOf("true") > -1 || data.indexOf("1") > -1) {
                                        window.parent.parent.window.location.href = "${portalUrl!}/taskHandle?taskid=" + result;
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
            url: "${bdcdjUrl}/bdcSjgl/getQlrByDjid?djid=" + djid + "&bdclxdm=" + bdclxdm + "&zdtzm=" + zdtzm,
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

    function dateForTable(grid_selector,gdly) {
        var jqData = $(grid_selector).jqGrid("getRowData");
        var rowIds = $(grid_selector).jqGrid('getDataIDs');
        $.each(jqData, function (index, data) {
            if(gdly == "fc"){
                getdateByBdcid(data.BDCID, $(grid_selector), rowIds[index], data.BDCLX, data.QLID,data.DJID,data.GDPROID,data.ZL,data.ID,gdly);
            }else if(gdly == "td"){
                getTdDateByBdcid(data.BDCID, $(grid_selector), rowIds[index], "TD", data.QLID,data.DJID,data.GDPROID,data.ZL,data.ID,gdly);
            }else if(gdly == "unPptd"){
                getTdDateByBdcid(data.BDCID, $(grid_selector), rowIds[index], "TD", data.QLID,data.DJID,data.GDPROID,data.ZL,data.ID,gdly);
            }
        })
    }
    //获取数据
    function getTdDateByBdcid(bdcid, table, rowid, bdclx, qlid, djid, gdproid, zl, id, gdly) {
        if (bdcid == null || bdcid == "undefined")
            bdcid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getdateByBdcid?bdcid=" + bdcid + "&bdclx=" + bdclx + "&qlid=" + qlid,
            success: function (result) {
                table.setCell(qlid, "ZL", result.zl);
                table.setCell(qlid, "QLR", result.qlr);
                var ppztCellVal = "";
                //var fczhCellVal = "";
                //var tdzhCellVal = "";
                var ppzt = result.ppzt;
                var ppztMc = "";
                var gdzh = result.gdzh;
                if(ppzt == '0'){
                    ppztMc = "已匹配";
//                    ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly+ '\')"  title="' + ppztMc + '" >' + ppztMc+ "</a>";
//                    table.setCell(id,"PPZT",ppztCellVal);
                    //fczhCellVal  = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + result.zl + '\',\'' + gdproid + '\',\'' + djid + '\')" title="' + cqzh + '" >' + cqzh + "</a>";
                    //tdzhCellVal  = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + result.zl + '\',\'' + gdproid + '\',\'' + djid + '\')" title="' + cqzh + '" >' + cqzh + "</a>";
                    if(bdclx == "TD"){
                        table.setCell(qlid,"PPZT",ppztMc);
                        //table.setCell(qlid,"TDZH",tdzhCellVal);
                    }else{
                        //table.setCell(bdcid,"FCZH",fczhCellVal);
                        table.setCell(bdcid,"PPZT",ppztMc);
                    }
                }else{
                    ppztMc = "未匹配";
                    ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly+ '\')"  title="' + ppztMc + '" >' + ppztMc+ "</a>";
                    //fczhCellVal = cqzh;
                    //tdzhCellVal = cqzh;
                    if(bdclx == "TD"){
                        //table.setCell(qlid,"TDZH", tdzhCellVal);
                        table.setCell(qlid,"PPZT", ppztCellVal);
                    }else{
                        //table.setCell(bdcid,"FCZH", fczhCellVal);
                        table.setCell(bdcid,"PPZT", ppztCellVal);
                    }
                }
            }
        });
    }
    //获取数据
    function getdateByBdcid(bdcid, table, rowid, bdclx, qlid, djid, gdproid, zl, id, gdly) {
        if (bdcid == null || bdcid == "undefined")
            bdcid = "";
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/selectBdcdy/getdateByBdcid?bdcid=" + bdcid + "&bdclx=" + bdclx + "&qlid=" + qlid,
            async:false,
            success: function (result) {
                table.setCell(bdcid, "ZL", result.zl);
                table.setCell(bdcid, "QLR", result.qlr);
                var ppztCellVal = "";
                //var fczhCellVal = "";
                //var tdzhCellVal = "";
                var ppzt = result.ppzt;
                var ppztMc = "";
                var gdzh = result.gdzh;
                if(ppzt == '0'){
                    ppztMc = "已匹配";
//                    ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly+ '\')"  title="' + ppztMc + '" >' + ppztMc+ "</a>";
//                    table.setCell(id,"PPZT",ppztCellVal);
                    //fczhCellVal  = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + result.zl + '\',\'' + gdproid + '\',\'' + djid + '\')" title="' + cqzh + '" >' + cqzh + "</a>";
                    //tdzhCellVal  = '<a href="javascript:gdsjEditXm(\'' + qlid + '\',\'' + bdcdyh + '\',\'' + bdclx + '\',\'' + result.zl + '\',\'' + gdproid + '\',\'' + djid + '\')" title="' + cqzh + '" >' + cqzh + "</a>";
                    if(bdclx == "TD"){
                        table.setCell(qlid,"PPZT",ppztMc);
                        //table.setCell(qlid,"TDZH",tdzhCellVal);
                    }else{
                        //table.setCell(bdcid,"FCZH",fczhCellVal);
                        table.setCell(bdcid,"PPZT",ppztMc);
                    }
                }else{
                    ppztMc = "未匹配";
                    ppztCellVal = '<a href="javascript:ppBdcdy(\'' + qlid + '\',\'' + gdzh + '\',\'' + gdly+ '\')"  title="' + ppztMc + '" >' + ppztMc+ "</a>";
                    //fczhCellVal = cqzh;
                    //tdzhCellVal = cqzh;
                    if(bdclx == "TD"){
                        //table.setCell(qlid,"TDZH", tdzhCellVal);
                        table.setCell(qlid,"PPZT", ppztCellVal);
                    }else{
                        //table.setCell(bdcid,"FCZH", fczhCellVal);
                        table.setCell(bdcid,"PPZT", ppztCellVal);
                    }
                }
            }
        });
    }
    function ppBdcdy(qlid,gdzh,gdly){
        var url =  "${bdcdjUrl}/bdcJgSjgl/toDataPicForSelect?matchTdzh=true&checkTdzh=false&editFlag=true&filterFwPpzt=3,2,1,0&gdzh="+gdzh +"&gdly="+gdly;
        var w_width=screen.availWidth-10;
        var w_height= screen.availHeight-32;
        window.open(url, "匹配数据界面", "left=1,top=0,height="+w_height+",width="+w_width+",resizable=yes,scrollbars=yes");
    }
    function afterClosePpBdcdy(gdly){
        if(gdly == "fc"){
            var dcxc = $("#gdfcsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入权利人/坐落/房产证号",1500);
            }else{
                var gdfcsjUrl = "${bdcdjUrl}/selectBdcdy/getGdfczListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}";
                tableReload("gdfcsj-grid-table", gdfcsjUrl, {dcxc: $("#gdfcsj_search").val()});
            }
        }else if (gdly = "td"){
            var dcxc = $("#gdtdsj_search").val();
            if(dcxc == "" || dcxc == null ||dcxc == undefined) {
                $.Prompt("请输入权利人/坐落/土地证号",1500);
            }else{
                var gdtdsjUrl = "${bdcdjUrl}/selectBdcdy/getGdtdzListByPage?zdtzm=${zdtzm!}&dyfs=${dyfs!}&qllx=${yqllxdm!}&bdclxdm=${bdclxdm!}&qlxzdm=${qlxzdm!}&proid=${proid!}&ysqlxdm=${ysqlxdm!}";
                tableReload("gdtdsj-grid-table", gdtdsjUrl, {dcxc: $("#gdtdsj_search").val()});
            }
        }
    }
    function getPptzs(bdcdyh) {
        openWin("${reportUrl}/ReportServer?reportlet=print%2Fbdc_pptzs.cpt&op=write&bdcdyh=" + bdcdyh);
    }

    $(function () {
        var bdclx="TD";
        if(bdclx!=""&&bdclx!=null){
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
        if(bdclx!=""&&bdclx!=null){
            initBdclxZxByBdclx(bdclx)
        }
    }

    function initBdclxZxByBdclx(bdclx) {
        $("#bdclxZxSelect").html('<option value="">请选择...</option>');
        if(bdclx!=""&&bdclx!=null){
            if(bdclx=='TD'){
                var bdclxZx_html= '<option value="">请选择...</option>'
                bdclxZx_html += '<option value="ZD">宗地 </option>';
                bdclxZx_html += '<option value="QSZD">权属宗地</option>';
                bdclxZx_html += '<option value="CBZD">承包宗地</option>';
                $("#bdclxZxSelect").html(bdclxZx_html);
            }else if(bdclx=='TDFW'){
                var bdclxZx_html= '<option value="">请选择...</option>'
                bdclxZx_html += '<option value="FWXMXX">房屋项目信息 </option>';
                bdclxZx_html += '<option value="LJZ">逻辑幢</option>';
                bdclxZx_html += '<option value="HS">户室</option>';
                $("#bdclxZxSelect").html(bdclxZx_html);
            }else if(bdclx=='TDQT'){
                var bdclxZx_html= '<option value="">请选择...</option>'
                bdclxZx_html += '<option value="CBZD">承包宗地</option>';
                $("#bdclxZxSelect").html(bdclxZx_html);
            }else if(bdclx=='TDSL'){
                var bdclxZx_html= '<option value="">请选择...</option>'
                bdclxZx_html += '<option value="LQ">林权 </option>';
                $("#bdclxZxSelect").html(bdclxZx_html);
            }else if(bdclx=='HY'){
                var bdclxZx_html= '<option value="">请选择...</option>'
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

    function getSdStatus(rowid,cqzh){
        cqzh = encodeURI(cqzh);
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl!}/selectBdcdy/getXzyy?cqzh=" + cqzh,
            dataType: "json",
            async: false,
            success: function (jsonData) {
                if (jsonData.msg == "false") {
                    var xzyy = jsonData.xzyy;
                    $("#" + rowid).css("background-color", "#F9F900");
                    $("#" + rowid).attr("tooltips", "限制原因：" + jsonData.xzyy);
                    $("#" + rowid).hoverTips();
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
    });
</script>
<div class="main-container" id="draggable">
    <input type="hidden" id="proid" value="${proid!}">
    <div class="page-content">
        <div class="row">
            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <#if bdcdyly==2>
                        <li class="active">
                            <a data-toggle="tab" id="djsjTab" href="#djsj">
                                不动产单元
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                产权证
                            </a>
                        </li>
                    <#elseif bdcdyly==4 && sqlxdm == "8009901">
                        <li>
                            <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                                续封
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdcfsjTab" href="#gdcfsj">
                                续封过渡
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                查封（房地）
                            </a>
                        </li>
                    <#elseif bdcdyly==4 && sqlxdm == "8009903">
                        <li>
                            <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                                续封
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdcfsjTab" href="#gdcfsj">
                                续封过渡
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                查封（纯土地）
                            </a>
                        </li>
                    <#elseif bdcdyly==4 >
                        <li class="active">
                            <a data-toggle="tab" id="djsjTab" href="#djsj">
                                预查封
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                                续封
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdcfsjTab" href="#gdcfsj">
                                续封过渡
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                查封（不动产）
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                查封（房地）
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                查封（纯土地）
                            </a>
                        </li>
                    <#elseif bdcdyly==5>
                        <li>
                            <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                产权证
                            </a>
                        </li>
                        <li class="active">
                            <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                房产证
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" id="djsjTab" href="#djsj">
                                不动产单元
                            </a>
                        </li>
                    <#elseif bdcdyly==6>
                        <#if (sqlxdm != "4009901" && sqlxdm != "4009902")>
                            <li>
                                <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                    产权证
                                </a>
                            </li>
                            <li class="active">
                                <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                    纯土地证
                                </a>
                            </li>
                        <#else>
                            <li class="active">
                                <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                    纯土地证
                                </a>
                            </li>
                        </#if>
                    <#elseif bdcdyly==7>
                        <#if sqlxdm != "4009903">
                            <li class="active">
                                <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                    产权证
                                </a>
                            </li>
                            <li>
                                <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                    房产证
                                </a>
                            </li>
                        <#else>
                            <li class="active">
                                <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                    房产证
                                </a>
                            </li>
                        </#if>
                        <li>
                            <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                纯土地证
                            </a>
                        </li>
                    <#elseif bdcdyly==8>
                        <#if (sqlxdm != "4009901" && sqlxdm != "4009902")>
                                 <li class="active">
                                         <a data-toggle="tab" id="ywsjTab" href="#ywsj">
                                                        产权证
                                         </a>
                                 </li>
                                 <li>
                                         <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                                        房产证
                                         </a>
                                 </li>
                                <#if (sqlxdm=="9920001")||(sqlxdm=="9920005")>
                                    <li>
                                        <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
                                            纯土地证
                                        </a>
                                    </li>
                                </#if>
                        <#else>
                                 <li  class="active">
                                         <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
                                                         房产证
                                         </a>
                                 </li>
                        </#if>
                    <#--  <#elseif bdcdyly==9>
                          <li class="active">
                              <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                                  查封信息
                              </a>
                          </li>
                          <li>
                              <a data-toggle="tab" id="gdcfsjTab" href="#gdcfsj">
                                  过渡查封信息
                              </a>
                          </li>-->
                    <#else>
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
                        <#if bdcdyly==3>
                            <li class="active">
                                <a data-toggle="tab" id="qlxxTab" href="#qlxx">
                                    解封
                                </a>
                            </li>
                            <li>
                                <a data-toggle="tab" id="gdcfsjTab" href="#gdcfsj">
                                    解封过渡
                                </a>
                            </li>
                        </#if>
                    </#if>
                </ul>
                <div class="tab-content">
                    <#if bdcdyly==2 || bdcdyly==4>
                    <div id="djsj" class="tab-pane in active">
                    <#else>
                        <#if bdcdyly==0>
                        <div id="djsj" class="tab-pane in active">
                        <#else>
                        <div id="djsj" class="tab-pane">
                        </#if>
                    </#if>
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="djsj_search"
                                           data-watermark="请输入权利人/坐落/不动产单元号/房屋编号">
                                </td>
                                <td class="Search">
                                    <a href="#" id="djsj_search_btn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="djsjShow">高级搜索</button>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton"onclick=show("bdclxZxShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="simpleSearch" id="bdclxZxShow" style="display:none">
                    <#--<input type="text" id="checkboxSelect" style="width: 200px;height: 34px;"/>-->
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td style="border: 0px">
                                    <select name="bdclx" id="bdclxSelect" class="form-control" style="width: 150px;" onchange="changeBdclx()">
                                        <#list bdclxList! as bdclx>
                                            <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td style="border: 0px">
                                    <select name="bdclxZx" id="bdclxZxSelect" class="form-control" style="width: 150px;">
                                        <option value="">请选择...</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <table id="djsj-grid-table"></table>
                    <div id="djsj-grid-pager"></div>
                </div>
                    <#if (bdcdyly==5 || (bdcdyly==8 && (sqlxdm == "4009901" || sqlxdm == "4009902"))|| (bdcdyly=7 && sqlxdm=="4009903"))>
                    <div id="gdfcsj" class="tab-pane in active">
                    <#else>
                    <div id="gdfcsj" class="tab-pane">
                    </#if>
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="gdfcsj_search"
                                           data-watermark="请输入权利人/坐落/房产证号">
                                </td>
                                <td class="Search">
                                    <a href="#" id="gdfcsj_search_btn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="gdfcsjShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <table id="gdfcsj-grid-table"></table>
                    <div id="gdfcsj-grid-pager"></div>
                </div>
                    <#if bdcdyly==6 >
                    <div id="gdtdsj" class="tab-pane in active">
                    <#else>
                    <div id="gdtdsj" class="tab-pane">
                    </#if>
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="gdtdsj_search"
                                           data-watermark="请输入权利人/坐落/土地证号">
                                </td>
                                <td class="Search">
                                    <a href="#" id="gdtdsj_search_btn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="gdtdsjShow">高级搜索</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <table id="gdtdsj-grid-table"></table>
                    <div id="gdtdsj-grid-pager"></div>
                </div>
                    <#if (bdcdyly==7 && sqlxdm!="4009903" )|| (bdcdyly==8 && sqlxdm != "4009901" && sqlxdm != "4009902")>
                        <div id="ywsj" class="tab-pane in active">
                    <#else>
                        <#if bdcdyly==1>
                            <div id="ywsj" class="tab-pane in active">
                        <#else>
                            <div id="ywsj" class="tab-pane">
                        </#if>
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
                                <td>
                                    <button type="button" class="btn01 AdvancedButton" id="ywsjShow">高级搜索</button>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td>
                                    <button type="button" class="btn01 AdvancedButton"onclick=show("zslxShow")><i class="ace-icon fa fa-plus bigger-130"></i></button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="simpleSearch" id="zslxShow" style="display:none">
                    <#--<input type="text" id="checkboxSelect" style="width: 200px;height: 34px;"/>-->
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td style="border: 0px">
                                    <select name="zslx" id="zslxSelect" class="form-control" style="width: 150px;">
                                        <option value="">请选择...</option>
                                        <option value="zs">不动产权证书</option>
                                        <option value="zms">不动产权证明书</option>
                                        <option value="zmd">不动产权证明单</option>
                                    </select>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                                <td style="border: 0px">发证日期(起)</td>
                                <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzqssj" id="fzqssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
                                <td style="border: 0px">发证日期(至)</td>
                                <td style="border: 0px"><input type="text" class="date-picker form-control" name="fzjssj" id="fzjssj" data-date-format="yyyy-mm-dd" style="width: 150px;"></td>
                            </tr>
                        </table>
                    </div>
                    <table id="ywsj-grid-table"></table>
                    <div id="ywsj-grid-pager"></div>
                </div>
                    <#if bdcdyly==2  || bdcdyly==4 >
                    <div id="qlxx" class="tab-pane">
                    <#else>

                        <#if bdcdyly==3>
                        <div id="qlxx" class="tab-pane in active">
                        <#else>
                        <div id="qlxx" class="tab-pane">
                        </#if>

                    </#if>
                    <div id="qlxx" class="tab-pane">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                    <#--<input type="text" class="SSinput watermarkText" id="qlxx_search" data-watermark="权利人/坐落/不动产单元号">-->
                                        <input type="text" class="SSinput watermarkText" id="qlxx_search"
                                               data-watermark="请输入被查封权利人/坐落/不动产单元号/查封文号">
                                    </td>
                                    <td class="Search">
                                        <a href="#" id="qlxx_search_btn">
                                            搜索
                                            <i class="ace-icon fa fa-search bigger-130"></i>
                                        </a>
                                    </td>
                                    <td style="border: 0px">&nbsp;</td>
                                </tr>
                            </table>
                        </div>
                        <table id="qlxx-grid-table"></table>
                        <div id="qlxx-grid-pager"></div>
                    </div>
                </div>

                    <#if bdcdyly==3 || bdcdyly==4 >
                    <div id="gdcfsj" class="tab-pane">
                    <#else>
                    <div id="qlxx" class="tab-pane">
                    </#if>
                    <div class="simpleSearch">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" class="SSinput watermarkText" id="gdcfsj_search"
                                           data-watermark="请输入查封文号/坐落">
                                </td>
                                <td class="Search">
                                    <a href="#" id="gdcfsj_search_btn">
                                        搜索
                                        <i class="ace-icon fa fa-search bigger-130"></i>
                                    </a>
                                </td>
                                <td style="border: 0px">&nbsp;</td>
                            </tr>
                        </table>
                    </div>
                    <table id="gdcfsj-grid-table"></table>
                    <div id="gdcfsj-grid-pager"></div>
                </div>

                </div>
                </div>
                </div>
                </div>
                    <!--地籍高级搜索-->
                    <div class="Pop-upBox moveModel" style="display: none;" id="djsjSearchPop">
                        <div class="modal-dialog djsjSearchPop-modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                                    <button type="button" id="djsjHide" class="proHide"><i
                                            class="ace-icon glyphicon glyphicon-remove"></i>
                                    </button>
                                </div>
                                <div class="bootbox-body" style="background: #fafafa;">
                                    <form class="form advancedSearchTable" id="djsjSearchForm">
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产单元号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcdyh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>地籍号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="djh" class="form-control">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产类型：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <select name="bdclx" class="form-control">
                                                    <#list bdclxList! as bdclx>
                                                        <option value="${bdclx.DM!}">${bdclx.MC}</option>
                                                    </#list>
                                                </select>
                                            </div>
                                            <div class="col-xs-2">
                                                <label>权利人：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="qlr" class="form-control">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>土地坐落：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="tdzl" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>合同编号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="htbh" class="form-control">
                                            </div>
                                        </div>

                                    </form>
                                </div>
                                <div class="modelFooter">
                                    <button type="button" class="btn btn-sm btn-primary" id="djsjGjSearchBtn">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--业务高级搜索-->
                    <div class="Pop-upBox moveModel" style="display: none;" id="ywsjSearchPop">
                        <div class="modal-dialog ywsjSearchPop-modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>业务高级查询</h4>
                                    <button type="button" id="ywsjHide" class="proHide"><i
                                            class="ace-icon glyphicon glyphicon-remove"></i>
                                    </button>
                                </div>
                                <div class="bootbox-body" style="background: #fafafa;">
                                    <form class="form advancedSearchTable" id="ywsjSearchForm">
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产单元号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcdyh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label> 土地坐落：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="zl" class="form-control">
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产权证号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcqzh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>权利人：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="qlr" class="form-control">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="modelFooter">
                                    <button type="button" class="btn btn-sm btn-primary" id="ywsjGjSearchBtn">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--房产高级搜索-->
                    <div class="Pop-upBox moveModel" style="display: none;" id="fcsjSearchPop">
                        <div class="modal-dialog fcsjSearchPop-modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                                    <button type="button" id="fcsjHide" class="proHide"><i
                                            class="ace-icon glyphicon glyphicon-remove"></i>
                                    </button>
                                </div>
                                <div class="bootbox-body" style="background: #fafafa;">
                                    <form class="form advancedSearchTable" id="fcsjSearchForm">
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产单元号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcdyh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>房产号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="fczh" class="form-control">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="modelFooter">
                                    <button type="button" class="btn btn-sm btn-primary" id="fcsjGjSearchBtn">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="Pop-upBox moveModel" style="display: none;" id="fcsjSearchPop">
                        <div class="modal-dialog fcsjSearchPop-modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                                    <button type="button" id="fcsjHide" class="proHide"><i
                                            class="ace-icon glyphicon glyphicon-remove"></i>
                                    </button>
                                </div>
                                <div class="bootbox-body" style="background: #fafafa;">
                                    <form class="form advancedSearchTable" id="fcsjSearchForm">
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产单元号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcdyh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>房产号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="fczh" class="form-control">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="modelFooter">
                                    <button type="button" class="btn btn-sm btn-primary" id="fcsjGjSearchBtn">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--土地高级搜索-->
                    <div class="Pop-upBox moveModel" style="display: none;" id="tdsjSearchPop">
                        <div class="modal-dialog tdsjSearchPop-modal">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h4 class="modal-title"><i class="ace-icon fa fa-search bigger-110"></i>高级查询</h4>
                                    <button type="button" id="tdsjHide" class="proHide"><i
                                            class="ace-icon glyphicon glyphicon-remove"></i>
                                    </button>
                                </div>
                                <div class="bootbox-body" style="background: #fafafa;">
                                    <form class="form advancedSearchTable" id="tdsjSearchForm">
                                        <div class="row">
                                            <div class="col-xs-2">
                                                <label>不动产单元号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="bdcdyh" class="form-control">
                                            </div>
                                            <div class="col-xs-2">
                                                <label>土地号：</label>
                                            </div>
                                            <div class="col-xs-4">
                                                <input type="text" name="tdzh" class="form-control">
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="modelFooter">
                                    <button type="button" class="btn btn-sm btn-primary" id="tdsjGjSearchBtn">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!--错误提示-->
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
                                <div id="footer" class="modelFooter">
                                <#--<button type="button" class="btn btn-sm btn-primary" id="tipIgnoreBtn">忽略</button>-->
                                    <button type="button" class="btn btn-sm btn-primary" id="tipCloseBtn">关闭</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <form id="form" hidden="hidden">
                        <input type="hidden" id="djlx" name="djlx">
                        <input type="hidden" id="bdclxdm" name="bdclxdm">
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
                        <input type="hidden" id="proids" name="proid"/>
                        <input type="hidden" id="bdcid" name="bdcid"/>
                    </form>
                    <div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop"></div>
                <#--无用div 防止ace报错-->
                    <div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>