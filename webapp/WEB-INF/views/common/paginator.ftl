<#macro page url="" currentPage="" totalPage="" pageSize="" hideGoPageBtn="">
<div class="pagination PagingCombination tablePagination">
    <ul>
        <li ><a href="#" class="button" onclick="gotoPage(1)"><<</a></li>
        <li ><a href="#" class="button" onclick="gotoPage(${currentPage?number-1})"> < </a></li>
        <li>当前第 <input class="ui-pg-input" type="text" size="2" maxlength="7" value="${currentPage!}" role="textbox"> 页， 共${totalPage!}页</li>
        <li><a href="#" class="button" onclick="gotoPage(${currentPage?number+1})"> > </a></li>
        <li><a href="#" class="button" onclick="gotoPage(${totalPage?number})">>></a></li>

        <#if hideGoPageBtn!="true">
        <li>跳转到第
            <select class="ui-pg-selbox" onchange="changePage()" id="currentPage" name="currentPage" >
                <#list 1..totalPage?number as t>
                    <option role="option" value="${t!}" <#if currentPage?number==t>selected="selected" </#if>>${t!}</option>
                </#list>
            </select>
            页
        </li>
        </#if>
    </ul>
</div>
<script type="text/javascript">
    function getUrl(url){
        if(url==null || url=="")
            url = location.href;
        var pageTag='currentPage';
        var prefix;
        if (url.indexOf('?') > -1 && url.indexOf(pageTag) == -1) {
            prefix = url + '&';
        } else if (url.indexOf(pageTag) > -1) {
            prefix = url.substring(0, url.indexOf(pageTag));
        } else {
            prefix = '?';
        }
       return  prefix;
    }
    <#--document.onkeydown = function(e){-->
        <#--if(!e) e = window.event;//火狐中是 window.event-->
        <#--if((e.keyCode || e.which) == 13){-->
            <#--var currentPage=$('#currentPage').val();-->
            <#--if(currentPage>${totalPage!})-->
                <#--currentPage=1;-->
            <#--var url=getUrl("${url!}");-->
            <#--url=url+"currentPage="+currentPage;-->
            <#--window.location.href=url;-->
        <#--}-->
    <#--}-->
    function changePage(){
        var currentPage=$('#currentPage').val();
        if(currentPage>${totalPage!})
            currentPage=1;
        var url=getUrl("${url!}");
        url=url+"currentPage="+currentPage;
        window.location.href=url;
    }
    function gotoPage(currentPage){
        var url="";
        if(currentPage>${totalPage!})
            currentPage=1;
        if(currentPage<1)
            currentPage=1;
        url=getUrl("${url!}");
        url=url+"currentPage="+currentPage;
        window.location.href=url;
    }
</script>
</#macro>