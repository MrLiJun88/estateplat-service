<@com.html title="申请人信息" import="ace,public,init">
    <@script name="static/js/addSqr.js"></@script>
<script type="text/javascript">
    var bdcdjUrl="${serverUrl!}";
    var sqrid= "${bdcSqr.sqrid!}";
</script>
    <@f.contentDiv  title="申请人信息">
        <@f.form id="sqrForm" name="sqrForm">
            <@f.buttons>
                <@f.button id="save" name="save"   handler="saveQlrxx" text="保存" ></@f.button>
                <@f.button id="add" name="add"  handler="addQlrxx" text="增加" ></@f.button>
            </@f.buttons>
            <@f.hidden id="proid" name="proid"  value="${bdcSqr.proid!}"/>
            <@f.hidden id="sqrid"  name="sqrid" value="${bdcSqr.sqrid!}"/>
            <@f.hidden id="wiid"  name="wiid" value="${bdcSqr.wiid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="sqrmc" name="sqrmc" value="${bdcSqr.sqrmc!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="是否持证人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="sfczr" valueFieldName="1"  saveValue="${bdcSqr.sfczr!}" defaultValue="1">
                        是</@f.radio>
                        <@f.radio name="sfczr" valueFieldName="0" saveValue="${bdcSqr.sfczr!}"  defaultValue="1">
                        否</@f.radio>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="zjzl"  name="zjzl"  showFieldName="mc" valueFieldName="dm" source="zjlxList" defaultValue="${bdcSqr.zjzl!}" noEmptyValue="true"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  id="zjh" name="zjh" value="${bdcSqr.zjh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="共有方式"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select name="gyfs"   showFieldName="mc" valueFieldName="dm" source="[{'mc':'单独所有','dm':'0'},{'mc':'共同共有','dm':'1'},{'mc':'按份共有','dm':'2'},{'mc':'其他共有','dm':'3'}]"defaultValue="${bdcSqr.qlrsfzjzl!}" noEmptyValue="true"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有比例"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text  id="qlbl" name="qlbl"   value="${bdcSqr.qlbl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="性别"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.radio name="xb" defaultValue="男"  saveValue="${bdcSqr.xb!}"  valueFieldName="男">
                        男</@f.radio>
                        <@f.radio  name="xb" defaultValue="男" saveValue="${bdcSqr.xb!}" valueFieldName="女">
                        女</@f.radio>
                    </@f.td>
                    <@f.th>
                        <@f.label name="权利面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlmj" name="qlmj" value="${bdcSqr.qlmj!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="申请人联系电话"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="lxdh"  name="lxdh" value="${bdcSqr.lxdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="所属行业"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="sshy" name="sshy"value="${bdcSqr.sshy!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="通讯地址"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.text id="txdz" name="txdz"   value="${bdcSqr.txdz!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="法定代表人或负责人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="frmc"   name="frmc"   value="${bdcSqr.frmc!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代表人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="frdh" name="frdh"   value="${bdcSqr.frdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理人姓名"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dlrmc"  name="dlrmc"   value="${bdcSqr.dlrmc!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="代理人联系电话"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dlrdh" name="dlrdh"   value="${bdcSqr.dlrdh!}"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="代理机构名称"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dljg" name="dljg"   value="${bdcSqr.dljg!}"/>
                    </@f.td>
                    <@f.th>
                        <@f.label name="其余共有人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qygyr" name="qygyr" value="${bdcSqr.qygyr!}"/>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@p.listDiv>
    <section id="qlxx">
        <div class="row">
            <div class=" col-xs-2 demonstrative">权利信息</div>
            <div class="col-xs-8 ">
            </div>
            <div class="rowLabel col-xs-2">
            <#--<button type="button" class="btn btn-primary" id="addQlxx" onclick="addQlxx()">新增</button>-->
            </div>
        </div>
    <#--列表按钮-->
        <@p.toolBars>
            <@p.toolBar text="关联义务人" handler="glQlr('ywr')" iClass="ace-icon fa fa-file-o"/>
            <@p.toolBar text="关联权利人" handler="glQlr('qlr')" iClass="ace-icon fa fa-pencil-square-o"/>
        </@p.toolBars>
        <@p.list tableId="qlxx-grid-table" pageId="qlxx-grid-pager" keyField="QLID" dataUrl="${serverUrl}/bdcdjSlxx/getQlxxPagesJson?wiid=${bdcSqr.wiid!}" multiboxonly="true"multiselect="true">
            <@p.field fieldName="XH" header="序号" width="1%"/>
            <@p.field fieldName="BDCDYH" header="不动产单元号" width="5%"/>
            <@p.field fieldName="QLLX" header="权利类型" width="5%"/>
            <@p.field fieldName="ZL" header="坐落" width="5%"/>
            <@p.field fieldName="PROID" header="操作" width="5%"   renderer="qllxCz"/>
            <@p.field fieldName="QLLXDM" header="权利类型代码" hidden="true"/>
            <@p.field fieldName="PROID" header="项目ID" hidden="true"/>
            <@p.field fieldName="QLID" header="QLID" hidden="true"/>
            <@p.field fieldName="QLLXDM" header="QLLXDM"  hidden="true"/>
        </@p.list>
        <table id="qlxx-grid-table"></table>
        <div id="qlxx-grid-pager"></div>
    </section>
    </@p.listDiv>
    <#include "../../common/rightsManagement.ftl">
    <#include "../../common/fieldColorManagement.ftl">
</@com.html>