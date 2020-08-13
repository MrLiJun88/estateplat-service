<#if bdcdyly==5 || (bdcdyly==8 &&(sqlxdm == "4009901" || sqlxdm == "4009902"))>
    <li class="active">
<#elseif bdcdyly==4 && sqlxdm == "8009901">
    <li class="active">
<#else>
    <li>
</#if>
    <a data-toggle="tab" id="gdfcsjTab" href="#gdfcsj">
        <#if bdcdyly==4>
            查封（房地）
        <#else>
            房产证
        </#if>
    </a>
</li>