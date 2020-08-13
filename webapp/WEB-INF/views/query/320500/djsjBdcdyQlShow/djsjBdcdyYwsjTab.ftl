<#if (bdcdyly==7 && sqlxdm != "4009903") || (bdcdyly==8 && sqlxdm != "4009901" && sqlxdm != "4009902") || bdcdyly==1 || bdcdyly==10 || bdcdyly==2>
    <li class="active">
<#else>
    <li>
</#if>
    <a data-toggle="tab" id="ywsjTab" href="#ywsj">
        <#if bdcdyly==4>
            查封（不动产）
        <#else>
            产权证
        </#if>
    </a>
</li>