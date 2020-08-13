<@com.html title="不动产登记业务管理系统" import="ace,public">
<script type="text/javascript">
    function rendererField(cellvalue, options, rowObject){
       return 1;
    }
    function search(){
       alert(1);
    }
    function rowdbclick1(parm){
       alert(parm);
    }
</script>

<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content">
        <#--查询表单-->
        <@p.queryForm id="form1" method="post">
            <#--查询条件每一行，目前只支持两个条件一行-->
            <@p.queryRow>
                <@p.queryItem label="权利人" name="qlr" itemType="text" labelClass="col-xs-1" fieldClass="col-xs-3"/>
                <@p.queryItem label="权利人1" name="qlr1" itemType="text" labelClass="col-xs-1" fieldClass="col-xs-3"/>
                <@p.queryItem label="坐落" name="zl" itemType="combo" showFieldName="name" valueFieldName="value" source="[{'name':'所有','value':''},{'name':'超期','value':'1'}]" defaultValue="1" labelClass="col-xs-1" fieldClass="col-xs-3"/>
            </@p.queryRow>
            <@p.queryRow>
                <@p.queryItem label="开始时间" name="kssj" itemType="datefield" labelClass="col-xs-1" fieldClass="col-xs-3"/>
                <@p.queryBars class="col-xs-8 right">
                    <@p.queryBar text="搜索" handler="search"/>
                </@p.queryBars>
            </@p.queryRow>
            <#--表单查询-->
            <@p.queryBar text="搜索" handler="search"/>
        </@p.queryForm>
        <#--列表按钮-->
        <@p.toolBars>
            <@p.toolBar text="归档" handler="search" iClass="ace-icon fa fa-file-o"/>
            <@p.toolBar text="归档2" handler="search2" iClass="ace-icon fa fa-pencil-square-o"/>
        </@p.toolBars>
        <#--列表-->
        <@p.list tableId="grid-table" pageId="grid-pager" keyField="BDCDYH" dataUrl="${bdcdjUrl}/queryBdcdy/getBdcdyPagesJson" rowNum="10"multiboxonly="true"multiselect="true">
            <#--列表字段-->
            <@p.field fieldName="BDCLX" header="不动产类型" width="10%" renderer="rendererField"/>
            <@p.field fieldName="BDCDYH" header="不动产单元号" width="20%"/>
        </@p.list>
        <table id="grid-table"></table>
        <div id="grid-pager"></div>
    </div>
</div>
<div class="main-container">
    <div class="space-10"></div>
    <div class="page-content">
        <#--查询表单-->
        <@p.queryForm id="form2" method="post">
            <#--查询条件每一行，目前只支持两个条件一行-->
            <@p.queryRow>
                <@p.queryItem label="权利人" name="qlr" itemType="text"/>
                <@p.queryItem label="坐落" name="zl" itemType="combo" showFieldName="name" valueFieldName="value" source="[{'name':'所有','value':''},{'name':'超期','value':'1'}]" defaultValue="1"/>
            </@p.queryRow>
            <@p.queryRow>
                <@p.queryItem label="开始时间" name="kssj" itemType="datefield"/>
                <@p.queryItem label="结束时间" name="jssj" itemType="datefield"/>
            </@p.queryRow>
            <#--表单查询-->
            <@p.queryBar text="搜索" handler="search"/>
        </@p.queryForm>
        <#--列表按钮-->
        <@p.toolBars>
            <@p.toolBar text="归档" handler="search"/>
        </@p.toolBars>
        <#--列表-->
        <@p.list tableId="zs_grid-table" pageId="zs_grid-pager" keyField="BDCDYH" dataUrl="${bdcdjUrl}/bdcqz/getBdcqzPagesJson?zstype=zs" rowNum="5"multiboxonly="false"multiselect="false">
            <#--列表字段-->
            <@p.field fieldName="QLR" header="不动产权利人" width="10%"/>
            <@p.field fieldName="BDCQZH" header="不动产权证号" width="20%"/>
        </@p.list>
        <table id="zs_grid-table"></table>
        <div id="zs_grid-pager"></div>
    </div>
</div>
</@com.html>