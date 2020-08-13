<@com.html title="不动产登记业务管理系统" import="ace,public,init">
    <@f.contentDiv  title="不动产权证书信息">
        <@f.form  id="bdcqzxx">
            <@f.buttons>
                <@f.button    text="保存" ></@f.button>
            </@f.buttons>
            <@f.table >
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产权证号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcqzh" name="bdcqzh" value="${bdcqzh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="权利人"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlr" name="qlr" value="${qlr!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="共有情况"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="gyqk" name="gyqk" value="${gyqk!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="zl" name="zl" value="${zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="不动产单元号"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="bdcdyh" name="bdcdyh" value="${bdcdyh!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="权利类型"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qllx" name="qllx" value="${qllx!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利性质"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="qlxz" name="qlxz" value="${qlxz!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="用途"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="yt" name="yt" value="${yt!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="面积"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.text id="mj" name="mj" value="${mj!}"></@f.text>
                    </@f.td>
                    <@f.th>
                        <@f.label name="使用期限"></@f.label>
                    </@f.th>
                    <@f.td>
                        <@f.date id="syqx" name="syqx" value=""></@f.date>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="权利其他状况"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">
                        <@f.textarea  name="qlqtzk" >${qlqtzk!}</@f.textarea>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th>
                        <@f.label name="附记"></@f.label>
                    </@f.th>
                    <@f.td colspan="3" >
                        <@f.textarea  name="fj" value="${fj!}"></@f.textarea>
                    </@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
        <@f.hidden id="wiid" name="wiid" value="${wiid!}"/>
        <@f.hidden id="proid" name="proid" value="${proid!}"/>
    </@f.contentDiv>
</@com.html>