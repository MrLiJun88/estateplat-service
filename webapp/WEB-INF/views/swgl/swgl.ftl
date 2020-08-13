<@com.html title="税务管理" import="ace">
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
    #lqSearchPop .modal-dialog,#cqSearchPop .modal-dialog,#fcSearchPop .modal-dialog,#tdSearchPop  .modal-dialog {
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
        width;100% !important;
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
    $pageHight = '600px';
    $(function () {
        //默认初始化表格
        fwTableInit();

        /*判断浏览器是否是ie8  解决ie8弹出框居中问题*/
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject){
            if(ua.match(/msie ([\d.]+)/)[1]=='8.0'){
                $(window).resize(function(){
                    $.each($(".moveModel > .modal-dialog"),function(){
                        $(this).css("left",($(window).width()-$(this).width())/2);
                        $(this).css("top","40px");
                    })
                })
            }
        }

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


        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth=$(".tab-content").width();
            $("#fw-grid-table,#lq-grid-table,#cq-grid-table,#td-grid-table").jqGrid('setGridWidth', contentWidth);
        });

    })




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
            datatype: "local",
            height: $pageHight,
            jsonReader: {id: 'FWID'},
            colNames: ['受理日期', '业务类型', '业务细项','买房人','卖房人','房屋坐落','上传状态','税收状态','流程状态'],
            colModel: [
                {name: 'RF1DWMC', index: 'RF1DWMC', width: '10%', sortable: false},
                {name: 'FCZH', index: 'FCZH', width: '10%', sortable: false},
                {name: 'FWZL', index: 'FWZL', width: '10%', sortable: false},
                {name: 'RF1ZJH', index: 'RF1ZJH', width: '10%', sortable: false},
                {name: 'GHYT', index: 'GHYT', width: '10%', sortable: false},
                {name: 'FWJG', index: 'FWJG', width: '20%', sortable: false},
                {name: 'JZMJ', index: 'JZMJ', width: '10%', sortable: false} ,
                {name: 'JZMJ', index: 'JZMJ', width: '10%', sortable: false}  ,
                {name: 'JZMJ', index: 'JZMJ', width: '10%', sortable: false}

            ],
            viewrecords: true,
            rowNum: $rownum, /*
            rowList:[10, 20, 30],*/
            pager: pager_selector,
            pagerpos: "left",
            altRows: false,
            multiboxonly: true,
            multiselect: true,
            loadComplete: function () {
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
            ondblClickRow:function(){

            },
            editurl: "", //nothing is saved
            caption: "",
            autowidth: true
        });
    }


</script>
<div class="main-container">
    <div class="space-8"></div>
    <div class="page-content" id="mainContent">
        <div class="row">
            <div class="tabbable">

                    <div id="fw" class="tab-pane in active">
                        <div class="simpleSearch">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" class="SSinput watermarkText" id="fw_search_qlr"
                                               data-watermark="请输入业务类型/买房人/卖房人">
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
                                    <button type="button" id="gdFwAdd" >
                                        <i class="ace-icon fa fa-file-o"></i>
                                        <span>重新上传</span>
                                    </button>
                                </li>
                                <li>
                                    <button type="button" id="gdFwUpdate">
                                        <i class="ace-icon fa fa-pencil-square-o"></i>
                                        <span>办理</span>
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
                            <label>房产证号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="fczh" class="form-control">
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
                    <div class="row">
                        <div class="col-xs-2">
                            <label>证件号：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="rf1zjh" class="form-control">
                        </div>
                        <div class="col-xs-2">
                            <label>建筑面积：</label>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" name="jzmj" class="form-control">
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
</@com.html>
