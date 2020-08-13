<@com.html title="登记资源配置" import="ace">
<style>
    .col-xs-4{
        padding-left: 6px;
        padding-bottom: 10px;
    }
    .ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }
    .widget-title{
        color: #fff;
        font-size: 24px;
    }
    .widget-main {
        padding: 0px;
    }
    .widget-box{
        border: 0px;
    }
    .step-content .step-pane{
        min-height: 50px;
        padding: 0px 10px 0px 5px;
    }
    .profile-user-info-striped .profile-info-name{
        color:#fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 100px;
    }
    .wizard-actions {
        padding: 0px 20px 0px 0px;
    }
</style>
<script>
$(function(){
    //下拉框  含搜索的
    $('.chosen-select').chosen({allow_single_deselect:true,no_results_text: "无匹配数据",width:"100%"});
    //resize the chosen on window resize
    $(window).on('resize.chosen', function() {
        $.each($('.chosen-select'),function(index,obj){
            $(obj).next().css("width",0);
            var w = $(obj).parent().width();
            $(obj).next().css("width",w);
        })
        $.each($('.chosen-single-select'),function(index,obj){
            $(obj).next().css("width",0);
            var e = $(obj).parent().width();
            $(obj).next().css("width",e);
        })
    }).trigger('resize.chosen');
    //无搜索的
    $('.chosen-single-select').chosen({disable_search_threshold: 10,allow_single_deselect:true,no_results_text: "无匹配数据",width:"100%"});
    //表单添加校验
    //var yzValidate= $("#yzForm").validate();
    $("#addCheckInfo").click(function(){
        //if(yzValidate.form()){
        $.ajax({
            type: "GET",
            url: "${bdcdjUrl}/bdcConfig/addCheckinfo",
            data: $("#yzForm").serialize(),
            dataType: "json",
            success: function (data) {
                bootbox.dialog({
                    message: data.result,
                    buttons: {
                        "success" : {
                            "label" : "关闭",
                            "className" : "btn-sm btn-primary"
                        }
                    }
                });
                $("#yz-grid-table").trigger("reloadGrid"); //重新载入
            },
            error: function (data) {
                $("#yz-grid-table").trigger("reloadGrid"); //重新载入
            }
        });
        //}
    })
    $("#sqlxdm,#qllxdm,#checkCode,#checkModel,#checkMsg,#ysqlxdm,#createSqlxdm").change(function(){
        $("#yz-grid-table").jqGrid('setGridParam',{
            datatype:'json',
            page:1,
            url:"${bdcdjUrl}/bdcConfig/getBdcXtCheckinfoPagesJson?"+$("#yzForm").serialize(),
            loadComplete: function() {
                var rowNum = $("#yz-grid-table").jqGrid('getGridParam','records');
                if(rowNum>0){
                    $("#addCheckInfo").attr("disabled",true);
                }else{
                    $("#addCheckInfo").attr("disabled",false);
                }
            }
        }).trigger("reloadGrid"); //重新载入
        if($("#checkModel").val()=='confirmAndCreate'){
            $("#newSqlx").show();
        }else {
            $("#newSqlx").hide();
        }
    })
    /*表单校验 begin*/
    /*$.validator.setDefaults({
        showErrors: function(map, list) {
            // there's probably a way to simplify this
            this.currentElements.removeAttr("title").removeAttr("style");
            $(this.currentElements).tooltip("destroy");
            $.each(list, function(index, error) {
                $(error.element).attr("title", error.message).attr("style", "border:1px dotted red;");;
                $(error.element).tooltip({placement:"bottom"});
            });
        }
    });
    jQuery.extend(jQuery.validator.messages, {
        required: "必填字段"
    });*/
    /*表单校验 end*/
    $(".hideButton").click(function () {
        $(".Pop").hide();
    });
    //生成表格
    yzGrid();

    //yz删除
    $("#delYz").click(function () {
        var ids = $('#yz-grid-table').jqGrid('getGridParam', 'selarrrow');
        delRule(ids,"${bdcdjUrl}/bdcConfig/delYz","yz-grid-table");
    })

    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var  contentWidth;
        if($("#yzContent").width()>0){
            contentWidth=$("#yzContent").width();
        }
        $("#yz-grid-table").jqGrid('setGridWidth',contentWidth);
    });
})
//删除判断是否没有选择数据
function delRule(ids,url,gridId){
    if(ids.length==0){
        bootbox.dialog({
            message: "<h3><b>请至少选择一条数据!</b></h3>",
            title: "",
            buttons: {
                main: {
                    label: "关闭",
                    className: "btn-primary"
                }
            }
        });
        return;
    }
    $.blockUI({ message:"请稍等……" });
    bootbox.dialog({
        message: "是否删除？",
        title: "",
        closeButton:false,
        buttons: {
            success: {
                label: "确定",
                className: "btn-success",
                callback: function () {
                    $.getJSON(url+"?ids=" + ids, {}, function (jsonData) {
                        setTimeout($.unblockUI, 10);
                        alert(jsonData.result);
                        $('#'+gridId).trigger("reloadGrid");
                    })
                }
            },
            main: {
                label: "取消",
                className: "btn-primary",
                callback: function () {
                    setTimeout($.unblockUI, 10);
                }
            }
        }
    });
}
//auth表格初始化
function yzGrid(){
    var grid_selector = "#yz-grid-table";
    var pager_selector = "#yz-grid-pager";
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        url:"${bdcdjUrl}/bdcConfig/getBdcXtCheckinfoPagesJson",
        datatype:"json",
        height:'auto',
        jsonReader:{id:'ID'},
        colNames:[ '申请类型', '权利类型','原申请类型','验证类型','验证方式','验证信息','需要创建的申请类型','验证项目'],
        colModel:[
            {name:'SQLXMC', index:'SQLXMC', width:'10%',sortable:false},
            {name:'QLLXMC', index:'QLLXMC', width:'10%', sortable:false/*,editoptions:{required:true,url:true} */},
            {name:'YSQLXMC', index:'YSQLXMC', width:'15%',sortable:false},
            {name:'CHECK_INFO', index:'CHECK_INFO', width:'10%', sortable:false},
            {name:'CHECK_MODEL', index:'CHECK_MODEL', width:'5%',sortable:false,formatter:function(cellvalue, options, rowObject){
                if(!cellvalue){
                    return"";
                }
                var value;
                if(cellvalue=="alert"){
                    value="警告";
                }else{
                    value="提示";
                }
                return value;
            }},
            {name:'CHECK_MSG', index:'CHECK_MSG', width:'15%',sortable:false},
            {name:'CREATESQLXDM', index:'CREATESQLXDM', width:'10%',sortable:false},
            {name:'CHECK_TYPE', index:'CHECK_TYPE', width:'10%',sortable:false,formatter:function(cellvalue, options, rowObject){
                if(!cellvalue){
                    return"";
                }
                var value;
                if(cellvalue=="1"){
                    value="只验证选择不动产单元";
                }else if(cellvalue=="2"){
                    value="只验证转发";
                } else{
                    value="验证全部";
                }
                return value;
            }}
        ],
        cellEdit:false,
        viewrecords:true,
        pagerpos:"left",
        pager:pager_selector,
        altRows:false,
        rowNum:5,
        rowList:[5,10, 15],
        multiboxonly:true,
        multiselect:true,
        cellurl:"",
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth',$("#yzContent").width());
            }, 0);
        },
        ondblClickRow:function (rowid) {
        },
        caption:"",
        autowidth:true
    });
}
//replace icons with FontAwesome icons like above
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

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container: 'body'});
    $(table).find('.ui-pg-div').tooltip({container: 'body'});
}
//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="yzContent">
        <form id="yzForm">
            <div class="row paddingRow">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 申请类型 </div>
                        <div class="profile-info-value">
                            <select  name="sqlxdm" id="sqlxdm" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <#list sqList as sqlx>
                                    <option value="${sqlx.DM!}" >${sqlx.MC!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 权利类型 </div>
                        <div class="profile-info-value">
                            <select name="qllxdm" id="qllxdm" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <#list qlList as qllx>
                                    <option value="${qllx.dm!}" >${qllx.mc!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 原申请类型 </div>
                        <div class="profile-info-value">
                            <select name="ysqlxdm" id="ysqlxdm" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <#list sqList as sqlx>
                                    <option value="${sqlx.DM!}" >${sqlx.MC!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row paddingRow">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 验证类型 </div>
                        <div class="profile-info-value">
                            <select name="checkCode" id="checkCode" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <#list checkList as check>
                                    <option value="${check.checkCode!}" >${check.checkInfo!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 验证方式 </div>
                        <div class="profile-info-value">
                            <select name="checkModel" id="checkModel" required="true" class="chosen-single-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <option value="alert">警告</option>
                                <option value="confirm">提示</option>
                                <option value="confirmAndCreate">提示并创建</option>
                                <option value="continue">办结前提醒</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 验证信息 </div>
                        <div class="profile-info-value" required="true">
                            <input type="text" name="checkMsg" id="checkMsg"  style="margin: 0px;height: 25px;width: 100%">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row paddingRow" id="newSqlx" style="display: none;">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 创建申请类型 </div>
                        <div class="profile-info-value">
                            <select name="createSqlxdm" id="createSqlxdm" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <#list sqList as sqlx>
                                    <option value="${sqlx.DM!}" >${sqlx.MC!}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row paddingRow">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 验证项目 </div>
                        <div class="profile-info-value">
                            <select name="checkType" id="checkType" required="true" class="chosen-select " data-placeholder=" ">
                                <option value="" selected></option>
                                <option value="1" selected>只验证选择不动产单元</option>
                                <option value="2" selected>只验证转发</option>
                                <option value="0" selected>验证全部</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-4" style="padding-left: 13px;">
                    <button class="btn btn-sm btn-success" type="button" id="addCheckInfo" disabled="disabled">
                        <i class="ace-icon fa fa-download"></i>
                        新增
                    </button>
                </div>
            </div>
        </form>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="delYz">
                        <i class="ace-icon glyphicon glyphicon-remove"></i>
                        <span>删除</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="yz-grid-table"></table>
        <div id="yz-grid-pager"></div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>