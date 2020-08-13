<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    function saveBdcFdcqDzxx() {
        $.blockUI({message: "请稍等......"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjQlxx/saveBdcFdcqDzxx",
            type: 'POST',
            dataType: 'json',
            data: $("#fdcqDzForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        tipInfo("保存成功");
                        var contentFrame=window.parent.document.getElementById("contentFrame");
                        if(contentFrame!=null){
                            var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                            if(frameMain!=null){
                                var contentPane=frameMain.contentWindow;
                                if(contentPane!=null)
                                    contentPane.refreshGrid('qlxx');
                            }
                        }
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
            }
        });
    }


    function rendererFwyt(cellvalue, options, rowObject) {
        var value = cellvalue;
        $.each(${fwytListJosn!}, function (i, item) {
            if (item.dm == cellvalue) {
                value = item.mc;
            }
        });
        return value;
    }

    function rendererFwjg(cellvalue, options, rowObject) {
        var value = cellvalue;
        $.each(${fwjgListJosn!}, function (i, item) {
            if (item.DM == cellvalue) {
                value = item.MC;
            }
        });
        return value;
    }
    function fwFzxxCz(cellvalue, options, rowObject) {
        return '<div style="margin-left:8px;">' +
                '<div><button type="button"  class="btn btn-success">保存</button><button type="button"  class="btn btn-danger"onclick="delFwFzxx(\'' + cellvalue + '\')">删除</button></div>' +
                '</div>'
    }

    function detailFwFzxx(fzid) {
        var url = bdcdjUrl+'/bdcdjQlrxx?qlrid=' + qlrid;
        showIndexModel(url, "权利人信息", "1000", "800", false);
    }

    function delFwFzxx(fzid) {
        var wiid = $("#wiid").val();
        $.blockUI({message: "正在删除，请稍等……"});
        $.ajax({
            url: bdcdjUrl+"/bdcdjQlrxx/delQlrxx?qlrid=" + qlrid,
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        tipInfo("删除成功");
                        var dataUrl = bdcdjUrl+"/bdcdjQlrxx/getSqrxxPagesJson?wiid="+wiid;
                        tableReload("qlr-grid-table", dataUrl, '', '', '');
                    }
                }
            },
            error: function (data) {
                alert("删除失败");
            }
        });
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button id="saveBdcFdcqDzxx" type="button" class="btn btn-info save" onclick="saveBdcFdcqDzxx()">保存</button>
    </div>
</div>
    <@f.contentDiv  title="房地产权信息（项目内多幢房屋）">
        <@f.form  id="fdcqDzForm">
            <#--<@f.buttons>-->
            <#--<@f.button   handler="saveBdcFdcqDzxx" text="保存" ></@f.button>-->
            <#--</@f.buttons>-->
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="qlid" name="qlid" value="${bdcFdcqDz.qlid!}"/>
            <@f.table >
                <@f.tr >
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="房地坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="业务号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ywh" name="ywh" value="${bdcFdcqDz.ywh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="djlx" name="djlx"  showFieldName="mc" valueFieldName="dm" source="djlxList"defaultValue="${bdcXm.djlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr >
                    <@f.th class="center">
                        <@f.label name="房屋所有权人"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件种类"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="证件号"></@f.label>
                    </@f.th>
                    <@f.th class="center">
                        <@f.label name="权利性质"></@f.label>
                    </@f.th>
                </@f.tr>
                <@f.td style="background:#ffffff;" >
                    <@f.text id="qlr"name="qlr"value="" style="text-align:center" ></@f.text>
                </@f.td>
                <@f.td>
                    <@f.text id="qlrsfzjzl"name="qlrsfzjzl"value="" style="text-align:center" ></@f.text>
                </@f.td>
                <@f.td style="background:#ffffff;">
                    <@f.text id="qlrzjh"name="qlrzjh"value="" style="text-align:center" ></@f.text>
                </@f.td>
                <@f.td style="background:#ffffff;">
                    <@f.text id="qlrxz"name="qlrxz"value="" style="text-align:center" ></@f.text>
                </@f.td>
                <@f.tr>
                    <@f.th>
                        <@f.label name="产权来源"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="cqly" name="cqly" value="${cqly!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="房屋性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fwxz" name="fwxz" value="${fwxz!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋编号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fwdah" name="fwdah" value="${fwbh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="独用土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dytdmj" name="dytdmj" value="${dytdmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="房屋土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fwtdmj" name="fwtdmj" value="${fwtdmj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="分摊土地面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fttdmj" name="fttdmj" value="${fttdmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="另有临时有地"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="lsyd" name="lsyd" value="${lylsyd!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="房地产交易价格"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="fdcjyjg" name="fdcjyjg" value="${fdcjyjg!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地使用权人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="tdsyqr" name="tdsyqr" value="${tdsyqr!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh" name="bdcqzh" value="${bdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地使用开始期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="tdsyksqx" name="tdsyksqx" value=""></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登薄人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="dbr" name="dbr" value="${dbr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="土地使用结束期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="tdsyjsqx" name="tdsyjsqx" value=""></@f.date>
                    </@f.td>
                    <@f.th>
                        <@f.label name="登记时间"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djsj" name="djsj" value="${djsj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="登记原因"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="djyy" name="djyy" value="${djyy!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk" name="gyqk" value="${gyqk!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  name="fj" value="${fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@p.listDiv>
    <section id="fwfzxx">
        <div class="row">
            <div class="col-xs-2 demonstrative">
                房屋分幢信息：
            </div>
        </div>
        <div class="row">
            <@f.buttons>
                    <@f.button   handler="addFzxx" text="新增" ></@f.button>
                </@f.buttons>
        </div>
        <@p.list tableId="fwfzxx-grid-table" pageId="fwfzxx-grid-pager" keyField="FZID" dataUrl="${bdcdjUrl}/bdcFdcqDzxx/getBdcFwfzxxPagesJson?qlid=${qlid!}" multiboxonly="false"multiselect="false">
        <#--editurl="${bdcdjUrl}/bdcFdcqDzxx/getBdcFwfzxxPagesJson?qlid=${qlid!}">-->
            <@p.field fieldName="XMMC" header="项目名称" width="3%" editable="true" edittype="text"/>
            <@p.field fieldName="ZH" header="幢 号" width="3%" editable="true" edittype="text"/>
            <@p.field fieldName="GHYT" header="规划用途" width="3%" renderer="rendererFwyt" editable="true" edittype="select" selectData="${fwytData!}" />
            <@p.field fieldName="FWJG" header="房屋结构" width="3%"  renderer="rendererFwjg" editable="true" edittype="select" selectData="${fwjgData!}"/>
            <@p.field fieldName="JZMJ" header="建筑面积（㎡）" width="3%" editable="true" edittype="text"/>
            <@p.field fieldName="JGSJ" header="竣工时间" width="3%" editable="true" edittype="text" sorttype = "date"/>
            <@p.field fieldName="ZCS" header="总层数" width="3%" editable="true" edittype="text"/>
            <@p.field fieldName="ZTS" header="总套数" width="3%" editable="true" edittype="text"/>
            <@p.field fieldName="FZID" header="操作" width="5%"  renderer="fwFzxxCz"/>
            <@p.field fieldName="QLID" header="QLID" width="5%" hidden="true"/>
        </@p.list>
        <table id="fwfzxx-grid-table"></table>
        <div id="fwfzxx-grid-pager"></div>
    </section>
    </@p.listDiv>
    <#include "../../../common/rightsManagement.ftl">
    <#include "../../../common/fieldColorManagement.ftl">
</@com.html>