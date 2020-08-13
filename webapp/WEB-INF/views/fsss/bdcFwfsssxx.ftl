<@com.html title="不动产登记业务管理系统" import="ace,public,init">
<script type="text/javascript">
    var bdcdjUrl="${bdcdjUrl!}"
    var wiid="${wiid!}"
    function saveBdcdyxx() {
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjBdcdyxx/saveBdcdyxx",
            type: 'POST',
            dataType: 'json',
            data: $("#bdcdyForm").serialize(),
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
                                    contentPane.refreshGrid('bdcdy');
                            }
                        }
                        //去掉遮罩
                        setTimeout($.unblockUI, 10);
                    }
                }
            },
            error: function (data) {
                tipInfo("保存失败");
            }
        });
    }
    function fsssCz(cellvalue, options, rowObject) {
        return '<div style="margin-left:8px;">' +
                '<div><a class="detail" href="javascript:detailFsssxx(\'' + cellvalue + '\')">详细</a><button type="button"  class="btn btn-primary" onclick="delFsss(\'' + cellvalue + '\')">删除</button></div>' +
                '</div>'
    }

    function delFsss(fwfsssid) {
        $.ajax({
            url: '${bdcdjUrl!}/bdcFwfsss/delFsss',
            type: 'POST',
            dataType: 'json',
            data: {fwfsssids: fwfsssid,wiid:${"#wiid"},bdcdyid:${"#bdcdyid"}},
            success: function (data) {
                tipInfo(data.result);
                var jqgrid = $("#fsssxx-grid-table");
                jqgrid.trigger("reloadGrid");//重新加载JqGrid
            },
            error: function (data) {
            }
        });
    }

    function detailFsssxx(fwfsssid) {
        var url = '${bdcdjUrl}/bdcFwfsss/bdcFwfsssxx?fwfsssid=' + fwfsssid+'&taskid=${taskid!}&from=${from!}&rid=${rid!}';
        var k=window.showModalDialog(url,window,"dialogWidth=900px;dialogHeight=600px");
        if(k==1){
            window.location.reload();
        }
    }
</script>
    <@f.contentDiv  title="不动产单元信息">
        <@f.form  id="bdcdyForm">
            <@f.buttons>
                <@f.button id="saveBdcdyxx" name="saveBdcdyxx"   handler="saveBdcdyxx" text="保存" ></@f.button>
            </@f.buttons>
            <@f.hidden id="bdcdyid" name="bdcdyid" value="${bdcBdcdy.bdcdyid!}"/>
            <@f.hidden id="djbid" name="djbid" value="${bdcBdcdy.djbid!}"/>
            <@f.hidden id="spxxid" name="spxxid" value="${bdcSpxx.spxxid!}"/>
            <@f.hidden id="proid" name="proid" value="${bdcXm.proid!}"/>
            <@f.hidden id="wiid" name="wiid" value="${bdcXm.wiid!}"/>
            <@f.table>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.textarea id="bdcdyh" name="bdcdyh" value="${bdcBdcdy.bdcdyh!}"style="overflow:hidden" readonly="true"></@f.textarea>
                    </@f.td>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.textarea id="zl" name="zl" value="${bdcSpxx.zl!}" style="overflow:hidden"></@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="bdclx" name="bdclx"  showFieldName="MC" valueFieldName="BM" source="dybdclxList"defaultValue="${bdcXm.bdclx!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="宗地/宗海面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zdzhmj" name="zdzhmj" value="${bdcSpxx.zdzhmj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="宗地/宗海用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="zdzhyt" name="zdzhyt"  showFieldName="MC" valueFieldName="DM" source="zdList"defaultValue="${bdcSpxx.zdzhyt!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="定着物面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="mj" name="mj" value="${bdcSpxx.mj!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="定着物用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="yt" name="yt"  showFieldName="mc" valueFieldName="dm" source="fwytList"defaultValue="${bdcSpxx.yt!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="宗地/宗海权利性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="zdzhqlxz" name="zdzhqlxz"  showFieldName="MC" valueFieldName="DM" source="qlxzList"defaultValue="${bdcSpxx.zdzhqlxz!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="用海类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="yhlx" name="yhlx"  showFieldName="MC" valueFieldName="DM" source="yhlxList"defaultValue="${bdcSpxx.yhlx!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="构筑物类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="gzwlx" name="gzwlx"  showFieldName="MC" valueFieldName="DM" source="gzwlxList"defaultValue="${bdcSpxx.gzwlx!}"></@f.select>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="林种"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.select id="lz" name="lz"  showFieldName="MC" valueFieldName="DM" source="lzList"defaultValue="${bdcSpxx.lz!}"></@f.select>
                    </@f.td>
                    <@f.th>
                        <@f.label name="原不动产权证书号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="ybdcqzh" name="ybdcqzh" value="${bdcXm.ybdcqzh!}"></@f.text>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <#include "../../views/wf/common/rightsManagement.ftl">
    <#include "../../views/wf/common/fieldColorManagement.ftl">
</@com.html>