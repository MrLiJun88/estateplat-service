<@com.html title="在线用户" import="ace">
<style>
    .table-bordered {
        border: 0px solid #ddd;
    }
    .header{
        margin-bottom: 8px;
    }
    .table-header{
        font-size: initial;
    }
</style>
<script type="text/javascript">
$(function () {

})
</script>
<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">
            <h3 class="header smaller lighter blue">在线用户:&nbsp;${onlineUserList?size!}</h3>
            <div class="table-header">
                <Strong>
                    <i class="fa fa-user"></i>&nbsp;在线用户信息
                </Strong>
            </div>
            <table id="sample-table-1" class="table table-striped table-bordered table-hover">
                <thead>
                <tr>
                    <th>用户名</th>
                    <th>IP</th>
                    <th>登陆时间</th>
                </tr>
                </thead>
                <tbody>
                    <#list onlineUserList as onlineUser>
                    <tr>
                        <td class=" ">
                        ${onlineUser.userName!}
                        </td>
                        <td class=" ">
                        ${onlineUser.ip!}
                        </td>
                        <td class=" ">
                        ${onlineUser.onlineTime?date!} ${onlineUser.onlineTime?time!}
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<#--无用div 防止ace报错-->
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>
