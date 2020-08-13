<#if bdcdyly==6>
    <li class="active">
<#elseif bdcdyly==4 && sqlxdm == "8009901">
    <li class="active">
<#else>
    <li>
</#if>
    <a data-toggle="tab" id="gdtdsjTab" href="#gdtdsj">
         <#if bdcdyly==4>
             查封（纯土地）
         <#else>
            纯土地证
         </#if>
    </a>
</li>