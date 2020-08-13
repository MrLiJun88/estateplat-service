<@com.html title="银行配置" import="ace">
<style>
    /*新建弹出框的样式修改及ie8居中问题*/
    #myModal .modal-dialog {
        width: 1000px;
        margin:30px auto;
    }
    #myModal .bootbox-body{
        height: 580px;
        padding: 0px;
    }

    .new-modal{
        width: 600px;
    }
    .ui-jqgrid-bdiv{
        overflow-x: hidden!important;
    }
    .modal-dialog{
        width:650px;
        margin:30px auto;
    }
    .col-xs-4,.col-xs-3{
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
        padding: 10px;
    }
    .step-content .step-pane{
        min-height: 50px;
        padding: 0px 10px 0px 5px;
    }
    .profile-user-info-striped .profile-info-name{
        color:#fff;
        background-color: #408fc6;
        border-top: 1px solid #408fc6;
        width: 130px;
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

        $("#companyTableSaveBtn").click(function(){
            var logValidate= $("#companyTableForm").validate();
            if(logValidate.form()){
                $.ajax({
                    url:'${bdcdjUrl}/bdcConfig/saveCompany?'+$("#companyTableForm").serialize(),
                    type:'post',
                    dataType:'json',
                    data:{},
                    success:function (data) {
                        alert(data.result);
                        $("#limitTablePop,#modal-backdrop-pop").hide();
                        //此处可以添加对查询数据的合法验证
                        $("#limit-table-grid-table").jqGrid('setGridParam',{
                            datatype:'json',
                            page:1
                        }).trigger("reloadGrid"); //重新载入
                        $('#companyTableForm')[0].reset();
                    },
                    error:function (data) {
                        alert("保存失败");
                        $("#limitTablePop,#modal-backdrop-pop").hide();
                        $('#companyTableForm')[0].reset();
                    }
                });
            }
        })
        /*表单校验 begin*/
        $.validator.setDefaults({
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
        });
        /*表单校验 end*/
        $(".hideButton").click(function () {
            $("#companyTableForm").find(".form-control").tooltip("destroy").removeAttr("aria-invalid").removeAttr("title").removeAttr("style");
            $('#companyTableForm')[0].reset();
            $(".Pop").hide();
        });
        //生成表格
        companyTableConfigGrid();
        fillCompanyTable();



        //company新增
        $("#addCompanyConfig").click(function () {
            $("#limitTablePop,#modal-backdrop-pop").show();
            $(window).trigger('resize.chosen');
            $("#title").html("新增配置");
            $("#qyid").val("");
        });
        //company删除
        $("#delCompanyConfig").click(function () {
            var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
            delRule(ids,"${bdcdjUrl}/bdcConfig/delCompany","limit-table-grid-table");
        })
        //company修改
        $("#updateCompanyConfig").click(function(){
            var ids = $('#limit-table-grid-table').jqGrid('getGridParam', 'selarrrow');
            if(ids.length==0){
                tipInfo("请选择一条数据!");
                return;
            }
            if(ids.length>1){
                tipInfo("只能同时修改一条数据!");
                return;
            }
            var data= $('#limit-table-grid-table').getRowData(ids);
            $("#title").html("修改配置");
            //字段赋值
            $("#qyid").val(data.QYID);
            $("#qymc").val(data.QYMC);
            $("#zjbh").val(data.ZJBH);
            $("#bz").val(data.BZ);
            $("#zjlx").find("option[value='"+data.ZJLX+"']").attr("selected", true);
            $("#zjlx").trigger("chosen:updated");
            $("#limitTablePop,#modal-backdrop-pop").show();
            $(window).trigger('resize.chosen');
        })
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#limitTableContent").width()>0){
                contentWidth=$("#limitTableContent").width();
            }
            $("#limit-table-grid-table").jqGrid('setGridWidth',contentWidth);
        });

        //下拉列表事件
        $("#workflowId,#formWorkflowId").change(function(){
            if(this.id=="workflowId"){
                getWorkflowNodes("workflowNodeid","workflowId");
            }else{
                getWorkflowNodes("formWorkflowNodeid","formWorkflowId");
            }
        })


       function fillCompanyTable() {
           $("#limit-table-grid-table").jqGrid('setGridParam',{
               datatype:'json',
               page:1,
               url:"${bdcdjUrl}/bdcConfig/getCompanyPagesJson",
               postData:{}
           }).trigger("reloadGrid"); //重新载入
       }
    })


    //删除判断是否没有选择数据
    function delRule(ids,url,gridId){
        if(ids.length==0){
            tipInfo("请选择一条数据!");
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

    //打开table页面
    function toLimitTable(){
        $("#limitTablePop").hide();
        $("#myModal").show();
    }
    //提示
    function tipInfo(msg){
        // bootbox.dialog({
        //     message: "<h3><b>"+msg+"</b></h3>",
        //     title: "",
        //     buttons: {
        //         main: {
        //             label: "关闭",
        //             className: "btn-primary",
        //         }
        //     }
        // });
        $.Prompt(msg,1500);
    }
    //auth表格初始化
    function companyTableConfigGrid(){
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
            jsonReader:{id:'QYID'},
            colNames:[ '企业ID','企业名称','证件类型','证件编号','备注','证件编号值'],
            colModel:[
                {name:'QYID', index:'QYID', width:'0%',sortable:false,hidden:true},
                {name:'QYMC', index:'QYMC', width:'15%',sortable:false},
                {name:'ZJLX', index:'ZJLX', width:'10%', sortable:false ,formatter:function (cellvalue, options, rowObject) {
                    if(!cellvalue){
                        return"";
                    }
                    var value=cellvalue;
                    $.each(${bdcZdZjlxListJosn!},function(i,item){
                        if(item.dm==cellvalue){
                            value= item.mc;
                        }
                    })
                    return value;
                }},
                {name:'ZJBH', index:'ZJBH', width:'15%', sortable:false},
                {name:'BZ', index:'BZ', width:'20%', sortable:false},
                {name:'ZJLX', index:'ZJLXTEMP', width:'0%',sortable:false,hidden:true}
            ],
//            subGrid:true,
//            subGridOptions:{
//                plusicon:"ace-icon fa fa-plus  bigger-110 blue",
//                minusicon:"ace-icon fa fa-minus  bigger-110 blue",
//                openicon:"ace-icon fa fa-chevron-right  orange"
//            },
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
                    $(grid_selector).jqGrid('setGridWidth',$("#limitTableContent").width());
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

</script>
<div class="main-container">
    <div class="space-10"></div>
<div class="page-content" id="limitTableContent">

    <div class="tableHeader">
        <ul>
            <li>
                <button type="button" id="addCompanyConfig">
                    <i class="ace-icon fa fa-download"></i>
                    <span>新建</span>
                </button>
            </li>
            <li>
                <button type="button" id="updateCompanyConfig">
                    <i class="ace-icon fa fa-pencil"></i>
                    <span>修改</span>
                </button>
            </li>
            <li>
                <button type="button" id="delCompanyConfig">
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
                <h4 class="modal-title" ><i class="ace-icon fa fa-pencil-square-o icon-only bigger-110" id="title"></i></h4>
                <button type="button"  class="hideButton"><i class="ace-icon glyphicon glyphicon-remove"></i></button>
            </div>
            <div class="PBTools">
                <ul>
                    <li>
                        <a href="#" id="companyTableSaveBtn">
                            <i class="ace-icon fa fa-download"></i>
                            <span>保存</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="bootbox-body">
                <form  id="companyTableForm"  novalidate="novalidate">
                    <div class="UItable">
                        <input type="hidden" id="qyid" name="qyid">
                        <table cellpadding="0" cellspacing="0" border="0" class="tableA">
                            <tbody>
                            <tr>
                                <td>
                                    <label>企业名称:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="qymc" name="qymc" required="false">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>证件类型:</label>
                                </td>
                                <td>
                                     <select  name="zjlx" id="zjlx"  class="chosen-select" data-placeholder=" ">
                                         <#list bdcZdZjlxList as zllx>
                                             <option value="${zllx.dm!}" >${zllx.mc!}</option>
                                         </#list>
                                     </select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>证件编号:</label>
                                </td>
                                <td>
                                    <input type="text" class="form-control" id="zjbh" name="zjbh" required="false">
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label>备注:</label>
                                </td>
                                <td>
                                     <input type="text" class="form-control" id="bz" name="bz" required="false">
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
                <iframe src="${bdcdjUrl!}/bdcConfig/toZdLimitTableConfig?selTable=true" style="width: 100%;height: 100%;border: 0px;" id="iframe"></iframe>
            </div>
        </div>
    </div>
</div>

<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
<div class="modal-backdrop fade in Pop" style="display:none" id="modal-backdrop-pop"></div>

</@com.html>