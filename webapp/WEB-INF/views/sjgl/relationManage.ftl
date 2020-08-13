<@com.html title="登记资源配置" import="ace">
<style>
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
    .btn-sm {
        padding: 2px 9px;
    }
</style>
<script>
    $(function(){
        //下拉框  含搜索的
        $('.chosen-select').chosen({allow_single_deselect:true,no_results_text: "无匹配数据",width:"100%"});
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
            $(".Pop").hide();
        });
        //生成表格
        relationGrid();
        //relation删除
        $("#delRelation").click(function () {
            var ids = $('#relation-grid-table').jqGrid('getGridParam', 'selarrrow');
            delRule(ids,"${bdcdjUrl}/bdcConfig/delRelation","relation-grid-table");
        })

        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            var  contentWidth;
            if($("#relationContent").width()>0){
                contentWidth=$("#relationContent").width();
            }
            $("#relation-grid-table").jqGrid('setGridWidth',contentWidth);
        });

        //dj下拉框
        $("#djSelect").change(function(){
            $("#relation-grid-table").jqGrid('setGridParam',{
                datatype:'json',
                page:1,
                postData:{djlxId:$("#djSelect option:selected").val()},
                loadComplete: function() {
                    var rowNum = $("#relation-grid-table").jqGrid('getGridParam','records');
                    if(rowNum>0){
                        $("#addResourceInfo").attr("disabled",true);
                    }else if($("#resourceSelect option:selected").val()==""){
                        $("#addResourceInfo").attr("disabled",true);
                    }else{
                        $("#addResourceInfo").attr("disabled",false);
                    }
                }
            }).trigger("reloadGrid"); //重新载入
        })
        //resource下拉框
        $("#resourceSelect").change(function(){
            reloadGrid({djlxId:$("#djSelect").val(),resourceId:$("#resourceSelect").val()});
            $("#relation-grid-table").jqGrid('setGridParam',{
                datatype:'json',
                page:1,
                postData:{djlxId:$("#djSelect option:selected").val(),resourceId:$("#resourceSelect option:selected").val()},
                loadComplete: function() {//如果数据不存在，可以新增
                    var rowNum = $("#relation-grid-table").jqGrid('getGridParam','records');
                    if(rowNum>0){
                        $("#addResourceInfo").attr("disabled",true);
                    }else if($("#resourceSelect option:selected").val()==""){
                        $("#addResourceInfo").attr("disabled",true);
                    }else{
                        $("#addResourceInfo").attr("disabled",false);
                    }
                }
            }).trigger("reloadGrid"); //重新载入
        })
        $("#addResourceInfo").click(function(){
            var data={djlxId:  $("#djSelect option:selected").val(),djlxName:$("#djSelect option:selected").text(),
                resourceId: $("#resourceSelect option:selected").val()};
            $.ajax({
                type: "GET",
                url: "${bdcdjUrl}/bdcConfig/addRelation",
                data:data ,
                dataType: "json",
                success: function (data) {
                    bootbox.dialog({
                        message: data.result,
                        buttons: {
                            "success" : {
                                "label" : "关闭",
                                "className" : "btn-sm btn-primary"
                            }
                        },
                        css: {
                            width:		'15%'
                        }
                    });
                    reloadGrid({});
                },
                error: function (data) {
                    reloadGrid({});
                }

            });
        })
        $('#fuelux-wizard')
                .ace_wizard({
                    //step: 2 //optional argument. wizard will jump to step "2" at first
                })
                .on('change' , function(e, info){
                    //第一步点下一步
                    if(info.step==1 && info.direction=='next'){
                        if($("#djSelect  option:selected").val()==""){
                            tip("登记类型");
                            return false;
                        }
                        var rowNum = $("#relation-grid-table").jqGrid('getGridParam','records');
                        if($("#resourceSelect option:selected").val()==""){
                            $("#addResourceInfo").attr("disabled",true);
                        }else if(rowNum>0){
                            $("#addResourceInfo").attr("disabled",true);
                        }else{
                            $("#addResourceInfo").attr("disabled",false);
                        }
                        //获取第一步的值
                        $("#djStep2").text($("#djSelect  option:selected").text());
                        reloadGrid({djlxId:$("#djSelect  option:selected").val(),resourceId:$("#resourceSelect option:selected").val()});
                    }else{
                        $(".btn-next").attr("disabled",false);
                    }
                    //2to1
                    if(info.step==2 && info.direction=='previous'){
                        //获取第一步的值
                        reloadGrid({djlxId:$("#djSelect  option:selected").val(),resourceId:""})
                    }
                })
                .on('finished', function(e) {

                }).on('stepclick', function(e,info){
                    $(".btn-next").attr("disabled",false);
                    if(info.step==1){
                        reloadGrid({djlxId:$("#djSelect  option:selected").val(),resourceId:""});
                    }
                });

        $('#modal-wizard .modal-header').ace_wizard();
        $('#modal-wizard .wizard-actions .btn[data-dismiss=modal]').removeAttr('disabled');
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
                        className: "btn-primary",
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
    //relation表格初始化
    function relationGrid(){
        var grid_selector = "#relation-grid-table";
        var pager_selector = "#relation-grid-pager";
        //resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }
        });
        jQuery(grid_selector).jqGrid({
            url:"${bdcdjUrl}/bdcConfig/getBdcXtRelationPagesJson",
            datatype:"json",
            height:'auto',
            jsonReader:{id:'RELATION_ID'},
            colNames:[ '登记类型ID', '登记类型','资源序号','资源名','资源图标','资源URL','资源ID'],
            colModel:[
                {name:'DJLX_ID', index:'DJLX_ID', width:'25%',sortable:false, hidden:true},
                {name:'DJLX_NAME', index:'DJLX_NAME', width:'20%', sortable:false,editable:false},
                {name:'XH', index:'XH', width:'10%',sortable:false,editable:true,edittype:'text',editrules: {required:true,number:true,minValue:0}},
                {name:'RESOURCE_NAME', index:'RESOURCE_NAME', width:'20%',sortable:false, editable:false},
                {name:'RESOURCE_IMG', index:'RESOURCE_IMG', width:'20%', sortable:false,editable:false},
                {name:'RESOURCE_URL', index:'RESOURCE_URL', width:'30%',sortable:false, editable:false},
                {name:'RESOURCE_ID', index:'RESOURCE_ID', width:'25%',sortable:false,hidden:true}
            ],
            viewrecords:true,
            cellEdit:true,
            rowNum:5,
            rowList:[5,10, 15],
            pager:pager_selector,
            pagerpos:"left",
            altRows:false,
            multiboxonly:true,
            multiselect:true,
            cellurl:"${bdcdjUrl}/bdcConfig/updateRelation",
            loadComplete:function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                    enableTooltips(table);
                    //resize
                    $(grid_selector).jqGrid('setGridWidth',$("#relationContent").width());
                }, 0);
            },
            ondblClickRow:function (rowid) {
            },
            editurl:"", //nothing is saved
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

    //刷新表格
    function reloadGrid(data){
        $("#relation-grid-table").jqGrid('setGridParam',{
            datatype:'json',
            page:1,
            postData:data
        }).trigger("reloadGrid"); //重新载入
    }

    //弹出提示框
    function tip(name){
        bootbox.dialog({
            message: "请选择"+name+","+name+"不能为空！",
            buttons: {
                "success" : {
                    "label" : "关闭",
                    "className" : "btn-sm btn-primary"
                }
            }
        });
    }
</script>
<div class="main-container">
    <div class="space-10"></div>
    <div class="widget-box">
        <div class="widget-body">
        <div class="widget-main">
        <!-- #section:plugins/fuelux.wizard -->
        <div id="fuelux-wizard" data-target="#step-container">
            <!-- #section:plugins/fuelux.wizard.steps -->
            <ul class="wizard-steps">
                <li data-target="#step1" class="active">
                    <span class="step">1</span>
                    <span class="title">登记类型</span>
                </li>

                <li data-target="#step2">
                    <span class="step">2</span>
                    <span class="title">资源</span>
                </li>

            </ul>
            <!-- /section:plugins/fuelux.wizard.steps -->
        </div>
        <hr>
        <!-- #section:plugins/fuelux.wizard.container -->
        <div class="step-content pos-rel" id="step-container">
            <div class="step-pane active" id="step1">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                            <div class="profile-info-name"> 登记类型 </div>

                            <div class="profile-info-value">
                                <select name="djSelect" id="djSelect" class="chosen-select" data-placeholder=" ">
                                    <option value="" selected></option>
                                    <#list djList as djlx>
                                        <option value="${djlx.dm!}" >${djlx.mc!}</option>
                                    </#list>
                                </select>
                            </div>
                    </div>
                </div>
            </div>
            <div class="step-pane" id="step2">
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 登记类型 </div>

                        <div class="profile-info-value" aria-readonly="true" id="djStep2">
                        </div>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="profile-user-info profile-user-info-striped" >
                        <div class="profile-info-name"> 资源 </div>

                        <div class="profile-info-value">

                            <div class="input-group">
                                <select name="resourceSelect" id="resourceSelect" class="chosen-select" data-placeholder=" ">
                                    <option value="" selected></option>
                                    <#list resourceList as resource>
                                        <option value="${resource.resourceId!}" >${resource.resourceName!}</option>
                                    </#list>
                                </select>
                                 <span class="input-group-btn">
                                    <button class="btn btn-sm btn-success" type="button" id="addResourceInfo" disabled="true">
                                        <i class="ace-icon fa fa-download"></i>
                                        新增
                                    </button>
                                 </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="page-content" id="relationContent">
                <div class="tableHeader">
                    <ul>
                        <li>
                            <button type="button" id="delRelation">
                                <i class="ace-icon glyphicon glyphicon-remove"></i>
                                <span>删除</span>
                            </button>
                        </li>
                    </ul>
                </div>
                <table id="relation-grid-table"></table>

                <div id="relation-grid-pager"></div>

            </div>
        </div>
        <div class="wizard-actions">
            <!-- #section:plugins/fuelux.wizard.buttons -->
            <button type="button" class="btn btn-prev" disabled="disabled">
                <i class="ace-icon fa fa-arrow-left"></i>
                上一步
            </button>

            <button type="button" class="btn btn-success btn-next" data-last="完成" >
                下一步
                <i class="ace-icon fa fa-arrow-right icon-on-right"></i>
            </button>

            <!-- /section:plugins/fuelux.wizard.buttons -->
        </div>

        <!-- /section:plugins/fuelux.wizard -->
        </div><!-- /.widget-main -->
        </div><!-- /.widget-body -->
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>