<@com.html title="不动产登记业务管理系统" import="bootstrap,public">
    <script type="text/javascript">
        var bdcdjUrl = "${bdcdjUrl!}";
        var bdcQrdJson = '${bdcQrdJson!}';
    </script>
    <link rel="stylesheet" type="text/css" href="${bdcdjUrl!}/static/css/bdcQrd.css"/>
    <div class="container-fluid">
        <div class="center1">
            <div class="title_qrd"> <#if bdcQrd ??>${bdcQrd.qrms!}</#if></div>
            <div>
                <table class="qrdTable">
                    <#if xgsjMap??>
                        <#list xgsjMap?keys as strKey>
                            <#assign xgsjList=xgsjMap[strKey] >
                            <tr>
                                <td class="sjms xbk" rowspan="2">${strKey!}</td>
                                <#if (xgsjList?size > 0)>
                                    <#list xgsjList as xgsj>
                                        <td class="xgzd" colspan="3">${xgsj.xgzd!}</td>
                                    </#list>
                                </#if>
                            </tr>
                            <tr>
                                <#if (xgsjList?size > 0)>
                                    <#list xgsjList as xgsj>
                                        <td class="yz xbk">${xgsj.yz!}</td>
                                        <td class="xbk">→→→</td>
                                        <td class="xz xbk">${xgsj.xz!}</td>
                                    </#list>
                                </#if>
                            </tr>
                        </#list>
                    </#if>
                </table>
            </div>
            <#--            <div class="markSure">-->
            <#--                <#if bdcQrd ?? && bdcQrd.qrr??>-->
            <#--                    <button class="btn btn-success" type="button" id="markSure">已确认</button>-->
            <#--                <#else>-->
            <#--                    <button class="btn btn-primary" type="button" onclick="markSure()" id="markSure">确认</button>-->
            <#--                </#if>-->
            <#--            </div>-->
        </div>
    </div>

    <script type="text/javascript">
        function markSure() {
            $.ajax({
                url: bdcdjUrl + "/bdcQrd/qr",
                type: 'POST',
                data: {bdcQrdJson: bdcQrdJson},
                success: function (data) {
                    if (data.msg == "success") {
                        infoMsg("确认成功");
                        $("#markSure").text("已确认");
                        $("#markSure").removeClass("btn-primary");
                        $("#markSure").addClass("btn-success");
                    }
                },
                error: function (data) {
                    alert("保存失败!");
                }
            });
        }
    </script>
</@com.html>
