<@com.html title="必填字段表格" import="ace">
<style>
        /*新建弹出框的样式修改及ie8居中问题*/
    #myModal .modal-dialog {
        width: 1000px;
        margin: 30px auto;
    }

    #myModal .bootbox-body {
        height: 580px;
        padding: 0px;
    }

    .new-modal {
        width: 600px;
    }

    .ui-jqgrid-bdiv {
        overflow-x: hidden !important;
    }

    .modal-dialog {
        width: 650px;
        margin: 30px auto;
    }

    .col-xs-4, .col-xs-3 {
        padding-left: 6px;
        padding-bottom: 10px;
    }

    .ui-jqgrid-bdiv {
        overflow-x: hidden !important;
    }

    .widget-title {
        color: #fff;
        font-size: 24px;
    }

    .widget-main {
        padding: 10px;
    }

    .step-content .step-pane {
        min-height: 50px;
        padding: 0px 10px 0px 5px;
    }

    .profile-user-info-striped .profile-info-name {
        color: #fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 130px;
    }

    .wizard-actions {
        padding: 0px 20px 0px 0px;
    }
</style>
<script>
$(function () {
    //下拉框  含搜索的
    $('.chosen-select').chosen({allow_single_deselect:true, no_results_text:"无匹配数据", width:"100%"});
    //resize the chosen on window resize
    $(window).on('resize.chosen',function () {
        $.each($('.chosen-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var w = $(obj).parent().width();
            $(obj).next().css("width", w);
        })
        $.each($('.chosen-single-select'), function (index, obj) {
            $(obj).next().css("width", 0);
            var e = $(obj).parent().width();
            $(obj).next().css("width", e);
        })
    }).trigger('resize.chosen');


    //拖拽功能
    $(".modal-header").mouseover(function () {
        $(this).css("cursor", "move");//改变鼠标指针的形状
    })
    $(".modal-header").mouseout(function () {
        $(".show").css("cursor", "default");
    })
    $(".Pop-upBox").draggable({opacity:0.7, handle:'div.modal-header'});


    $("#limitTableSaveBtn").click(function () {
        var logValidate = $("#limitTableForm").validate();
        if (logValidate.form()) {
            $("#formWorkflowId").attr("disabled", false);
            $("#formWorkflowNodeid").attr("disabled", false);
            $("#formWorkflowNodeid,#formWorkflowId").trigger('chosen:updated');
            $.ajax({
                url:'${bdcdjUrl}/bdcConfig/saveLimitField?' + $("#limitTableForm").serialize(),
                type:'post',
                dataType:'json',
                data:{workflowName:$("#formWorkflowId  option:selected").text(), workflowNodename:$("#formWorkflowNodeid  option:selected").text()},
                success:function (data) {
                    alert(data.result);
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    //此处可以添加对查询数据的合法验证
                    $("#limit-table-grid-table").jqGrid('setGridParam', {
                        datatype:'json',
                        page:1
                    }).trigger("reloadGrid"); //重新载入
                    $('#limitTableForm')[0].reset();
                },
                error:function (data) {
                    alert("保存失败");
                    $("#limitTablePop,#modal-backdrop-pop").hide();
                    $('#limitTableForm')[0].reset();
                }
            });
        }
    })
    /*表单校验 begin*/
    $.validator.setDefaults({
        showErrors:function (map, list) {
            // there's probably a way to simplify this
            this.currentElements.removeAttr("title").removeAttr("style");
            $(this.currentElements).tooltip("destroy");
            $.each(list, function (index, error) {
                $(error.element).attr("title", error.message).attr("style", "border:1px dotted red;");
                ;
                $(error.element).tooltip({placement:"bottom"});
            });
        }
    });
    jQuery.extend(jQuery.validator.messages, {
        required:"必填字段"
    });
    /*表单校验 end*/
    $(".hideButton").click(function () {
        $("#limitTableForm").find(".form-control").tooltip("destroy").removeAttr("aria-invalid").removeAttr("title").removeAttr("style");
        $('#limitTableForm')[0].reset();
        $(".Pop").hide();
    });
    //生成表格
    limitTableConfigGrid();


    //limitField新增
    $("#addLimitFieldConfig").click(function () {
        $("#lcpd").val("1");
        $("#formWorkflowId").attr("disabled", false);
        $("#formWorkflowNodeid").attr("disabled", false);
        $("#formWorkflowId").val($("#workflowId  option:selected").val());
        $("#formWorkflowId").trigger('chosen:updated');
        //让权利类型开始选择通用
//        $("#qllx").val('');
//        $("#qllx").trigger('chosen:updated');
        $("#formWorkflowId").change();
        $("#limitTablePop,#modal-backdrop-pop").show();
        $(window).trigger('resize.chosen');
        $("#title").html("新增配置");
        $("#id").val("");
    });
    //limitField删除
    $("#delLimitFieldConfig").click(function () {
        var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
        delRule(ids, "${bdcdjUrl}/bdcConfig/delLimitFieldConfig", "limit-table-grid-table");
    })
    //limitField修改
    $("#updateLimitFieldConfig").click(function () {
        var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
        if (ids.length == 0) {
            tipInfo("请选择一条数据!");
            return;
        }
        if (ids.length > 1) {
            tipInfo("只能同时修改一条数据!");
            return;
        }
        $("lcpd").val("2");
        var data = $('#limit-table-grid-table').getRowData(ids);
        $("#title").html("修改配置");
        //字段赋值
        $("#id").val(ids);
        $("#formWorkflowId").attr("disabled", true);
        $("#formWorkflowNodeid").attr("disabled", true);
        $("#formWorkflowId").val(data.WORKFLOW_ID);
        $("#formWorkflowId").trigger("chosen:updated");
        $.ajax({
            type:"GET",
            url:"${bdcdjUrl}/bdcConfig/getWorkFlowNodes",
            data:{workflowId:data.WORKFLOW_ID},
            dataType:"json",
            success:function (result) {
                //清空
                $("#formWorkflowNodeid").html("");
                if (result != null && result != '') {
                    $.each(result, function (index, data) {
                        $("#formWorkflowNodeid").append('<option value="' + data.dm + '" >' + data.mc + '</option>');
                    })
                }
                $("#formWorkflowNodeid").val(data.WORKFLOW_NODEID);
                $("#formWorkflowNodeid").trigger("chosen:updated");

                //zwq 权利类型回选
                $("#qllx").val(data.QLLX);
                $("#qllx").trigger("chosen:updated");

                $("#cptName").val(data.CPT_NAME);
                $("#cptFieldName").val(data.CPT_FIELD_NAME);
                $("#cptDesc").val(data.CPT_DESC);
                $("#tableId").val(data.TABLE_ID);
                $("#tableId").trigger("chosen:updated");
                $("#iscz").val(data.TABLE_ID);
                $.ajax({
                    type:"GET",
                    url:"${bdcdjUrl}/bdcConfig/getFields",
                    data:{id:data.TABLE_ID, cpt:$("#cptName").val(), workFlowId:data.WORKFLOW_ID, workFlowNodeid:data.WORKFLOW_NODEID},
                    dataType:"json",
                    success:function (result) {
                        //清空
                        $("#tableFieldId").html("");
                        if (result != null && result != '') {
                            $.each(result, function (index, data) {
                                $("#tableFieldId").append('<option value="' + data.dm + '" >' + data.mc + '</option>');
                            })
                        }
                        $("#tableFieldId").append('<option value="' + data.TABLE_FIELD_ID + '" >' + data.TABLE_FIELD_ID + '</option>');
                        $("#tableFieldId").val(data.TABLE_FIELD_ID);
                        $("#tableFieldId").trigger("chosen:updated");
                        $("#tableFieldName").val(data.TABLE_FIELD_NAME);
                        /*$(".chosen-select").trigger("chosen:updated");*/
                        $("#limitTablePop,#modal-backdrop-pop").show();
                        $(window).trigger('resize.chosen');
                    },
                    error:function (data) {
                    }
                });
            },
            error:function (data) {
            }
        });
    })
    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
        var contentWidth;
        if ($("#limitTableContent").width() > 0) {
            contentWidth = $("#limitTableContent").width();
        }
        $("#limit-table-grid-table").jqGrid('setGridWidth', contentWidth);
    });

    //下拉列表事件
    $("#workflowId,#formWorkflowId").change(function () {
        if (this.id == "workflowId") {
            getWorkflowNodes("workflowNodeid", "workflowId");
        } else {
            getWorkflowNodes("formWorkflowNodeid", "formWorkflowId");
        }
    })
    $("#workflowNodeid").change(function () {
        $("#limit-table-grid-table").jqGrid('setGridParam', {
            datatype:'json',
            page:1,
            url:"${bdcdjUrl}/bdcConfig/getLimitFieldPagesJson",
            postData:{workflowId:$("#workflowId  option:selected").val(), workflowNodeid:$("#workflowNodeid  option:selected").val()}
        }).trigger("reloadGrid"); //重新载入
    })

    //table下拉列表事件
    $("#tableId").change(function () {
        getFields($(this).val());
    })
    //cpt修改事件
    $("#cptName").change(function () {
        if ($("#lcpd").val() == "1")
            $("#tableId").change();
    })
    //默认
    $("#workflowId").change();
    $("#formWorkflowId").change();
})


//根据table获取字段
function getFields(tableId) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcConfig/getFields",
        data:{id:tableId, cpt:$("#cptName").val(), workFlowId:$("#formWorkflowId  option:selected").val(), workFlowNodeid:$("#formWorkflowNodeid  option:selected").val()},
        dataType:"json",
        success:function (result) {
            //清空
            $("#tableFieldId").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    $("#tableFieldId").append('<option value="' + data.dm + '" >' + data.mc + '</option>');
                })
            }
            $("#tableFieldId").trigger("chosen:updated");
        },
        error:function (data) {
        }
    });
}

function getWorkflowNodes(id, selId) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcConfig/getWorkFlowNodes",
        data:{workflowId:$("#" + selId + "  option:selected").val()},
        dataType:"json",
        success:function (result) {
            //清空
            $("#" + id).html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    $("#" + id).append('<option value="' + data.dm + '" >' + data.mc + '</option>');
                })
            }
            $("#" + id).trigger("chosen:updated");
            if (id == "workflowNodeid") {
                $("#" + id).change();
            } else {
                $("#tableId").change();
            }
        },
        error:function (data) {
        }
    });
}
//删除判断是否没有选择数据
function delRule(ids, url, gridId) {
    if (ids.length == 0) {
        tipInfo("请选择一条数据!");
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
                    $.getJSON(url + "?ids=" + ids, {}, function (jsonData) {
                        setTimeout($.unblockUI, 10);
                        alert(jsonData.result);
                        $('#' + gridId).trigger("reloadGrid");
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
//选择table
function selTable(id) {
    $.ajax({
        type:"GET",
        url:"${bdcdjUrl}/bdcConfig/getZdTable",
        dataType:"json",
        success:function (result) {
            //清空
            $("#tableId").html("");
            if (result != null && result != '') {
                $.each(result, function (index, data) {
                    $("#tableId").append('<option value="' + data.id + '" >' + data.tableName + '</option>');
                })
            }
            if (id != "") {
                $("#tableId").val(id);
            }
            $("#tableId").trigger("chosen:updated");
            $("#tableId").change();
        },
        error:function (data) {
        }
    });
    $("#limitTablePop").show();
    $("#myModal").hide();
}
//打开table页面
function toLimitTable() {
    $("#limitTablePop").hide();
    $("#myModal").show();
}
//提示
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
    $.Prompt(msg,1500);
}
//auth表格初始化
function limitTableConfigGrid() {
    var grid_selector = "#limit-table-grid-table";
    var pager_selector = "#limit-table-grid-pager";
    //resize on sidebar collapse/expand
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }
    });
    jQuery(grid_selector).jqGrid({
        datatype:"local",
        height:'auto',
        jsonReader:{id:'ID'},
        colNames:[ '工作流ID', '节点ID', '验证ID', '工作流名称', '节点名称', 'CPT报表名称', 'CPT字段名称', 'CPT描述', '字段名注释', '必填字段', 'SQL','QLLX'],
        colModel:[
            {name:'WORKFLOW_ID', index:'WORKFLOW_ID', width:'0%', sortable:false, hidden:true},
            {name:'WORKFLOW_NODEID', index:'WORKFLOW_NODEID', width:'0%', sortable:false, hidden:true},
            {name:'TABLE_ID', index:'TABLE_ID', width:'0%', sortable:false, hidden:true},
            {name:'WORKFLOW_NAME', index:'WORKFLOW_NAME', width:'20%', sortable:false},
            {name:'WORKFLOW_NODENAME', index:'WORKFLOW_NODENAME', width:'10%', sortable:false},
//            {name:"QLLXMC", index:'QLLXMC', width:'10%', sortable:false},
            {name:'CPT_NAME', index:'CPT_NAME', width:'10%', sortable:false},
            {name:'CPT_FIELD_NAME', index:'CPT_FIELD_NAME', width:'10%', sortable:false},
            {name:'CPT_DESC', index:'CPT_DESC', width:'10%', sortable:false},
            {name:'TABLE_FIELD_NAME', index:'TABLE_FIELD_NAME', width:'10%', sortable:false},
            {name:'TABLE_FIELD_ID', index:'TABLE_FIELD_ID', width:'10%', sortable:false},
            {name:'TABLE_XMREL_SQL', index:'TABLE_XMREL_SQL', width:'0%', sortable:false, hidden:true},
            {name:'QLLX', index:'QLLX', width:'0%', sortable:false, hidden:true}
        ],
        subGrid:true,
        subGridOptions:{
            plusicon:"ace-icon fa fa-plus  bigger-110 blue",
            minusicon:"ace-icon fa fa-minus  bigger-110 blue",
            openicon:"ace-icon fa fa-chevron-right  orange"
        },
        subGridRowExpanded:function (subgridDivId, rowId) {
            var data = $(grid_selector).getRowData(rowId);
            var subgridTableId = subgridDivId + "_t";
            $("#" + subgridDivId).html("<b>查询SQL:&nbsp;</b> " + data.TABLE_XMREL_SQL);
            $("#" + subgridDivId).css("min-height", "50px").css("padding-top", "10px");
        },
        viewrecords:true,
        rowNum:5,
        rowList:[5, 10, 15],
        pagerpos:"left",
        pager:pager_selector,
        altRows:false,
        multiboxonly:true,
        multiselect:true,
        loadComplete:function () {
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
                enableTooltips(table);
                //resize
                $(grid_selector).jqGrid('setGridWidth', $("#limitTableContent").width());
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

function enableTooltips(table) {
    $('.navtable .ui-pg-button').tooltip({container:'body'});
    $(table).find('.ui-pg-div').tooltip({container:'body'});
}
//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');

function resetAll() {
    //新增时重置所有,更新时手动填写的才清空,其他的不要清空
    if ($("#lcpd").val() == "1") {
        $('#limitTableForm')[0].reset();
        $("#formWorkflowId").val($("#workflowId  option:selected").val());
        $("#formWorkflowId").trigger('chosen:updated');
        $("#formWorkflowId").change();
//        $("#qllx").val('');
//        $("#qllx").trigger('chosen:updated');
    } else {
        $('#cptName').val('');
        $('#cptFieldName').val('');
        $('#cptDesc').val('');
        $('#tableFieldName').val('');
    }
}

</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content" id="limitTableContent">
        <div class="row paddingRow">
            <div class="col-xs-4">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 工作流名称</div>
                    <div class="profile-info-value">
                        <select name="workflowId" id="workflowId" class="chosen-select" data-placeholder=" ">
                            <#list workFlowList as workFlow>
                                <option value="${workFlow.workflowDefinitionId!}">${workFlow.workflowName!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <div class="profile-user-info profile-user-info-striped">
                    <div class="profile-info-name"> 工作流节点名称</div>
                    <div class="profile-info-value">
                        <select name="workflowNodeid" id="workflowNodeid" class="chosen-select " data-placeholder=" ">
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="tableHeader">
            <ul>
                <li>
                    <button type="button" id="addLimitFieldConfig">
                        <i class="ace-icon fa fa-download"></i>
                        <span>新建</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="updateLimitFieldConfig">
                        <i class="ace-icon fa fa-pencil"></i>
                        <span>修改</span>
                    </button>
                </li>
                <li>
                    <button type="button" id="delLimitFieldConfig">
                        <i class="ace-icon glyphicon glyphicon-remove"></i>
                        <span>删除</span>
                    </button>
                </li>
            </ul>
        </div>
        <table id="limit-table-grid-table"></table>
        <div id="limit-table-grid-pager"></div>
    </div>
    <div class="Pop-upBox bootbox modal fade bootbox-prompt in Pop" style="display:none" id="limitTablePop">
        <div class="modal-dialog new-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"><i class="ace-icon fa fa-pencil-square-o icon-only bigger-110"
                                               id="title"></i></h4>
                    <button type="button" class="hideButton"><i class="ace-icon glyphicon glyphicon-remove"></i>
                    </button>
                </div>
                <div class="PBTools">
                    <ul>
                        <li>
                            <a href="#" id="limitTableSaveBtn">
                                <i class="ace-icon fa fa-download"></i>
                                <span>保存</span>
                            </a>
                        </li>
                        <li>
                            <a href="#" onclick="resetAll()">
                                <i class="ace-icon fa fa-pencil-square-o"></i>
                                <span>重置</span>
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="bootbox-body">
                    <form id="limitTableForm" novalidate="novalidate">
                        <div class="UItable">
                            <input type="hidden" id="id" name="id">
                            <table cellpadding="0" cellspacing="0" border="0" class="tableA">
                                <tbody>
                                <tr>
                                    <td>
                                        <label>工作流名称:</label>
                                    </td>
                                    <td>
                                        <select name="workflowId" id="formWorkflowId" class="chosen-select "
                                                data-placeholder=" ">
                                            <#list workFlowList as workFlow>
                                                <option value="${workFlow.workflowDefinitionId!}">${workFlow.workflowName!}</option>
                                            </#list>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>工作流节点名称:</label>
                                    </td>
                                    <td>
                                        <select name="workflowNodeid" id="formWorkflowNodeid" class="chosen-select "
                                                data-placeholder=" ">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>权利类型:</label>
                                    </td>
                                    <td>
                                        <select name="qllx" id="qllx" class="chosen-select " data-placeholder=" ">
                                            <option value='0'>通用</option>
                                            <#list qllxList as qllx>
                                                <option value="${qllx.dm!}">${qllx.mc!}</option>
                                            </#list>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>CPT报表名称:</label>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control" id="cptName"
                                               name="cptName" required="true">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>CPT字段名称:</label>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control" id="cptFieldName"
                                               name="cptFieldName" required="true">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>CPT描述:</label>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control" id="cptDesc"
                                               name="cptDesc" required="true">
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="#" onclick="toLimitTable()">查询SQL(编辑):</a>
                                    </td>
                                    <td>
                                        <select name="tableId" id="tableId" class="chosen-select " data-placeholder=" ">
                                            <#list zdTableList as zdTable>
                                                <option value="${zdTable.id!}">${zdTable.tableName!}</option>
                                            </#list>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>必填字段:</label>
                                    </td>
                                    <td>
                                        <select name="tableFieldId" id="tableFieldId" class="chosen-select "
                                                data-placeholder=" ">
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <label>字段名注释:</label>
                                    </td>
                                    <td>
                                        <input type="text" class="form-control" id="tableFieldName"
                                               name="tableFieldName" required="true">
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<#--新建验证-->
    <div class="Pop-upBox bootbox modal fade bootbox-prompt in Pop" style="display: none;" id="myModal">
        <div class="modal-dialog newPro-modal">
            <div class="modal-content">
                <div class="bootbox-body">
                    <iframe src="${bdcdjUrl!}/bdcConfig/toZdLimitTableConfig?selTable=true"
                            style="width: 100%;height: 100%;border: 0px;" id="iframe"></iframe>
                </div>
            </div>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-pop"></div>
<#--lcpd点击新增按钮为1,更新为2-->
<div id="lcpd" hidden="hidden"></div>
</@com.html>