<@com.html title="不动产登记业务管理系统" import="ace,public,init,authorize">
<script type="text/javascript" >
    function sjclCz(cellvalue, options, rowObject) {
        return '<div style="margin-left:0px;">' +
                '<div><a class="detail" href="javascript:editSjcl(\'' + rowObject.SJCLID+ '\')">详细</a><button type="button"  class="btn btn-success" onclick="fileUp(\'' + rowObject.CLMC + '\')">上传</button><button type="button"  class="btn btn-danger" onclick="delSjcl(\'' + rowObject.CLMC + '\',\'' + rowObject.SJCLID+ '\')">删除</button></div>' +
                '</div>'
    }
    function fileUp(clmc) {
        debugger;
        var proid = $("#proid").val();
        var wiid = $("#wiid").val();
        setTimeout(function(){
            $.ajax({
                type:'post',
                url:'${bdcdjUrl}/bdcdjSlxx/creatSjcl',
                data:{proid: proid,wiid:wiid},
                success:function (data) {
                    var w_width=screen.availWidth-21;
                    var w_height= screen.availHeight-47;
                    var url=  "${bdcdjUrl}/createSjdcl/createAllFileFolder?proid="+proid+"&clmc="+encodeURI(clmc);
                    var title="收件单材料";
                    var windowDia=window.showModalDialog(url, title, "dialogHeight="+w_height+"px;dialogWidth= "+w_width+"px");
                    if(windowDia=='ok'){
                        if (isNotBlank(data)) {
                            if (data.msg == "success") {
                                $.ajax({
                                    type: 'post',
                                    url: '/estateplat-server/createSjdcl/saveSjclFs',
                                    data: {proid: proid},
                                    success: function (data) {
                                        var djzx =  $("#djzx  option:selected").val();
                                        var sjclxxUrl = "${bdcdjUrl}/bdcdjSlxx/getSjclPagesJson?wiid=${wiid!}&proid=${proid!}"+"&djzx="+djzx;
                                        tableReload("sjclxx-grid-table", sjclxxUrl, '', '', '');
                                        location.reload();
                                    },
                                    error: function (_ex) {
                                    }
                                });
                            }
                        }
                    }
                },
                error:function (_ex) {
                }
            });
        },2000);
    }
    function addSjcl() {
        debugger;
        var proid = $("#proid").val();
        var wiid = $("#wiid").val();
        var taskid = $("#taskid").val();
        var from = $("#from").val();
        var rid = $("#rid").val();
        var sjxxid = $("#sjxxid").val();
        $.ajax({
            type:'post',
            url:'/estateplat-server/bdcdjSlxx/creatSjcl',
            data:{proid: proid,wiid:wiid},
            success: function (data) {
                if (data.msg == "success") {
                    var url ="${bdcdjUrl}/bdcdjSlxx/bdcSjclxx?proid=" + proid + "&sjxxid="+ sjxxid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
                    var k=window.showModalDialog(url,window,"dialogWidth=800px;dialogHeight=300px");
                    if(k==1){
                        window.location.reload();
                    }
                }
            },
            error: function (data) {
                alert("添加失败,请重新尝试！");
            }
        });
    }
    function delSjcl(clmc,sjclid) {
        debugger;
        var proid = $("#proid").val();
        var sjxxid = $("#sjxxid").val();
        var wiid = $("#wiid").val();
        $.blockUI({message: "正在删除，请稍等……"});
        $.ajax({
            type:'post',
            url:'${bdcdjUrl}//bdcdjSlxx/creatSjcl',
            data:{proid: proid,wiid:wiid},
            success: function (data) {
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        $.ajax({
                            url: "${bdcdjUrl}/createSjdcl/delete?proid=" + proid+"&sjclid="+sjclid+"&sjxxid="+sjxxid+"&clmc="+encodeURI(clmc),
                            type: 'POST',
                            dataType: 'json',
                            success: function (data) {
                                setTimeout($.unblockUI, 10);
                                if (isNotBlank(data)) {
                                    if (data.msg == "success") {
                                        alert("删除成功");
                                        var contentFrame=window.parent.document.getElementById("contentFrame");
                                        if(contentFrame!=null){
                                            var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                                            if(frameMain!=null){
                                                var contentPane=frameMain.contentWindow;
                                                if(contentPane!=null)
                                                    contentPane.refreshGrid('sjclxx');
                                            }
                                        }
                                        //加入一个调用收件单父页面的保存功能
                                        var  sjdxxSave=document.getElementById("sjdxxSaveButton");
                                        if(sjdxxSave!=null){
                                            sjdxxSave.onclick();
                                        }
                                        location.reload();//刷新父页面，并关闭子页面。
                                        //去掉遮罩
                                        setTimeout($.unblockUI, 10);
                                    }
                                }
                            },
                            error: function (data) {
                                alert("删除失败,请重新选择数据！");
                                setTimeout($.unblockUI, 10);
                            }
                        });
                    }
                }
            },
            error: function (data) {
                alert("删除失败,请重新尝试！");
            }
        });

    }
    function editSjcl(sjclid) {
        var proid = $("#proid").val();
        var wiid = $("#wiid").val();
        var taskid = $("#taskid").val();
        var from = $("#from").val();
        var rid = $("#rid").val();
        $.ajax({
            type:'post',
            url:'${bdcdjUrl}/bdcdjSlxx/creatSjcl',
            data:{proid: proid,wiid:wiid},
            success: function (data) {
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        var url ="${bdcdjUrl}/bdcdjSlxx/editSjcl?sjclid=" + sjclid +"&proid=" + proid + "&wiid="+ wiid+'&taskid='+taskid+'&from='+from+'&rid='+rid;
                        var k=window.showModalDialog(url,window,"dialogWidth=800px;dialogHeight=300px");
                        if(k==1){
                            window.location.reload();
                        }
                    }
                }
            },
            error: function (data) {
                alert("删除失败,请重新尝试！");
            }
        });
    }
    function idChange(){
        var djzx =  $("#djzx  option:selected").val();
        var dataUrl = "${bdcdjUrl}/bdcdjSlxx/getSjclPagesJson?wiid=${wiid!}&proid=${proid!}"+"&djzx="+djzx;
        tableReload("sjclxx-grid-table", dataUrl, '', '', '');
    }
    function saveSjd() {
        var proid = $("#proid").val();
        $.blockUI({message: "请稍等……"});
        $.ajax({
            url: "${bdcdjUrl}/bdcdjSlxx/saveSjd",
            type: 'POST',
            dataType: 'json',
            data: $("#sjdForm").serialize(),
            success: function (data) {
                setTimeout($.unblockUI, 10);
                if (isNotBlank(data)) {
                    if (data.msg == "success") {
                        alert("保存成功");
                    }
                }
            },
            error: function (data) {
                alert("保存失败");
                var contentFrame=window.parent.document.getElementById("contentFrame");
                if(contentFrame!=null){
                    var frameMain=contentFrame.contentWindow.document.getElementById("frameMain");
                    if(frameMain!=null){
                        var contentPane=frameMain.contentWindow;
                        if(contentPane!=null)
                            contentPane.refreshGrid('sjxx');
                    }
                }
            }
        });
    }
</script>
<div class="bs-docs-example toolTop">
    <div class="leftToolTop">
        <button type="button" class="btn btn-primary save" id="sjdxxSaveButton" name="提交"onclick="saveSjd()">保存</button>
    </div>
</div>
<div class="main-container" >
    <@f.contentDiv  title="不动产登记收件单" >
        <@f.form id="sjdForm" name="sjdForm">
            <@f.hidden id="proid" name="proid"  value="${proid!}"/>
            <@f.hidden id="wiid" name="wiid"  value="${wiid!}"/>
            <@f.hidden id="from" name="from"  value="${from!}"/>
            <@f.hidden id="taskid" name="taskid"  value="${taskid!}"/>
            <@f.hidden id="rid" name="rid"  value="${rid!}"/>
            <@f.hidden id="sjxxid" name="sjxxid"  value="${bdcSjxx.sjxxid!}"/>
            <@f.hidden id="spxxid" name="spxxid"  value="${bdcSpxx.spxxid!}"/>
            <@f.table style="width:650px">
                <@f.tr style="border:none;height:0px;">
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:50px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:40px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:55px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:45px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:65px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:80px;"></@f.td>
                    <@f.td style="border:none;height:0px;width:25px;"></@f.td>
                    <@f.td  style="border:none;height:0px;width:40px;"></@f.td>
                </@f.tr>
                <@f.tr style="height:15px">
                    <@f.td colspan="6" style="border:none;height:0px;">
                    </@f.td>
                    <@f.td colspan="2" style="border:none;text-align:right">
                        <@f.label name="收件编号："></@f.label>
                    </@f.td>
                    <@f.td colspan="4" style="border:none;text-align:left">
                        <@f.text id="bh" name="bh" value="${bdcXm.bh!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="不动产登记类型"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text id="djlxMc" name="djlxMc" value="${bdcdjlx!}" readonly="true"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="登记子项"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.select id="djzx" name="djzx"   showFieldName="MC" valueFieldName="DM" source="djzxList" defaultValue="${bdcXm.djzx!}" handler="idChange"/>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="坐落"></@f.label>
                    </@f.th>
                    <@f.td colspan="9">
                        <@f.text  id="zl" name="zl" value="${bdcSpxx.zl!}"></@f.text>
                    </@f.td>
                </@f.tr>
                <@f.tr>
                    <@f.th colspan="3">
                        <@f.label name="收件人"></@f.label>
                    </@f.th>
                    <@f.td colspan="3">${bdcSjxx.sjr!}</@f.td>
                    <@f.th colspan="2">
                        <@f.label name="收件时间"></@f.label>
                    </@f.th>
                    <@f.td colspan="4">${sjrq!}</@f.td>
                </@f.tr>
            </@f.table>
        </@f.form>
    </@f.contentDiv>
    <@p.listDiv>
        <section id="sqcllb">
            <div class="row">
                <div class=" col-xs-2 demonstrative">申请材料列表</div>
                <div class="col-xs-8 ">
                </div>
            </div>
        <#--列表按钮-->
            <@p.toolBars>
                <@p.toolBar text="批量上传" handler="fileUp" iClass="ace-icon fa fa-file-o"/>
                <@p.toolBar text="新增" handler="addSjcl" iClass="ace-icon fa fa-file-o"/>
            </@p.toolBars>
            <@p.list tableId="sjclxx-grid-table" pageId="sjclxx-grid-pager" keyField="SJCLID" dataUrl="${bdcdjUrl}/bdcdjSlxx/getSjclPagesJson?wiid=${wiid!}&proid=${proid!}" multiboxonly="false"multiselect="false">
                <@p.field fieldName="XH" header="序号" width="2%"/>
                <@p.field fieldName="CLMC" header="材料名称" width="13%"/>
                <@p.field fieldName="FS" header="份数" width="3%"/>
                <@p.field fieldName="FS" header="页数" width="3%"/>
                <@p.field fieldName="LX" header="类型" width="3%"/>
                <@p.field fieldName="CZ" header="操作" width="6%"   renderer="sjclCz"/>
                <@p.field fieldName="SJCLID" header="收件材料ID"  width="0%" hidden="true"/>
            </@p.list>
            <table id="sjclxx-grid-table"></table>
            <div id="sjclxx-grid-pager"></div>
        </section>
    </@p.listDiv>
</div>
</@com.html>