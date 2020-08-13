<@com.html title="不动产登记业务管理系统" import="ace,public">
<style>
    /*.imgClass {*/
        /*max-width: 350px;*/
        /*max-height: 350px;*/
        /*myimg: expression(onload=function(){*/
            /*this.style.width=(this.offsetWidth > 500)?"500px":"auto"}*/
        /*);*/
    /*}*/
</style>


<div class="main-container">
    <div class="page-content" id="mainContent">
        <div class="row">

            <#if xsfs=="ZDXS">
            <div class="row-fluid" align="center">
                <img src="${bdcdjUrl!}/dcxx/selectZdt?proid=${proid}&bdcdyh=${bdcdyh!}"
                     class="imgClass" alt="无宗地图" width="550" height="800"/>
            </div>
            </#if>
            <#if xsfs=="FWXS">
                <div class="row-fluid" align="center">
                    <img src="${bdcdjUrl!}/dcxx/selectHst?proid=${proid}&bdcdyh=${bdcdyh!}"
                         class="imgClass" alt="无分层分户图"  width="600" height="800"/>
                </div>
            </#if>
        </div>
    </div>
</div>
<div id="navbar" class="navbar navbar-default" hidden="hidden"></div>
</@com.html>