<@com.html title="匹配结果查看" import="frsingle,public,init,combo">
<script type="text/javascript">
    var proid = "${proid!}";
</script>
<div class="main-container" style="margin:auto; margin-top:50px; width: 1000px">
    <@f.form id="bdcPpgxLogForm" name="bdcPpgxLogForm">
        <@f.hidden id="proid" name="proid"/>
        <@f.table>
            <#if (bdcPpgxLogList?size >0)>
                <#list bdcPpgxLogList as bdcPpgxLog>
                    <@f.tr style="border:none;height:0px;">
                        <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                        <@f.td  style="border:none;height:0px;width:150px;"></@f.td>
                        <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                        <@f.td  style="border:none;height:0px;width:150px;"></@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="不动产单元号:"></@f.label>
                        </@f.td>
                        <@f.td colspan='3'>
                            <@f.text id="yyh" name="yyh" value="${bdcPpgxLog.bdcdyh!}" ></@f.text>
                        </@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="房屋虚拟不动产单元号:"></@f.label>
                        </@f.td>
                        <@f.td colspan='1'>
                            <@f.text  id="fwbdcdyh" name="fwbdcdyh" value="${bdcPpgxLog.fwbdcdyh!}"></@f.text>
                        </@f.td>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="土地虚拟不动产单元号:"></@f.label>
                        </@f.td>
                        <@f.td colspan='1'>
                            <@f.text  id="tdbdcdyh" name="tdbdcdyh" value="${bdcPpgxLog.tdbdcdyh!}"></@f.text>
                        </@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="房屋不动产权证号:"></@f.label>
                        </@f.td>
                        <@f.td colspan='3'>
                            <@f.text  id="fwBdcqzh" name="fwBdcqzh" value="${bdcPpgxLog.fwbdcqzh!}"></@f.text>
                        </@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="土地不动产权证号:"></@f.label>
                        </@f.td>
                        <@f.td colspan='3'>
                            <@f.text  id="tdBdcqzh" name="tdBdcqzh" value="${bdcPpgxLog.tdbdcqzh!}"></@f.text>
                        </@f.td>
                    </@f.tr>
                    <@f.tr>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="匹配时间:"></@f.label>
                        </@f.td>
                        <@f.td colspan='1'>
                            <@f.text  id="ppsj" name="ppsj" value = "${bdcPpgxLog.ppsj!}"></@f.text>
                        </@f.td>
                        <@f.td colspan='1' style="text-align:right">
                            <@f.label class="leftLabel" name="操作人:"></@f.label>
                        </@f.td>
                        <@f.td colspan='1'>
                            <@f.text  id="tdbdcdyh" name="tdbdcdyh" value="${bdcPpgxLog.czr!}"></@f.text>
                        </@f.td>
                    </@f.tr>
                </#list>
            <#else>
                <@f.tr>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="不动产单元号:"></@f.label>
                    </@f.td>
                    <@f.td colspan='3'>
                        <@f.text id="yyh" name="yyh" value="" ></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="房屋虚拟不动产单元号:"></@f.label>
                    </@f.td>
                    <@f.td colspan='1'>
                        <@f.text  id="fwbdcdyh" name="fwbdcdyh" value=""></@f.text>
                    </@f.td>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="土地虚拟不动产单元号:"></@f.label>
                    </@f.td>
                    <@f.td colspan='1'>
                        <@f.text  id="tdbdcdyh" name="tdbdcdyh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="房屋不动产权证号:"></@f.label>
                    </@f.td>
                    <@f.td colspan='3'>
                        <@f.text  id="fwBdcqzh" name="fwBdcqzh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="土地不动产权证号:"></@f.label>
                    </@f.td>
                    <@f.td colspan='3'>
                        <@f.text  id="tdBdcqzh" name="tdBdcqzh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="匹配时间:"></@f.label>
                    </@f.td>
                    <@f.td colspan='1'>
                        <@f.text  id="ppsj" name="ppsj" value = ""></@f.text>
                    </@f.td>
                    <@f.td colspan='1' style="text-align:right">
                        <@f.label class="leftLabel" name="操作人:"></@f.label>
                    </@f.td>
                    <@f.td colspan='1'>
                        <@f.text  id="tdbdcdyh" name="tdbdcdyh" value=""></@f.text>
                    </@f.td>
                </@f.tr>
            </#if>
        </@f.table>
    </@f.form>
</div>
</@com.html>