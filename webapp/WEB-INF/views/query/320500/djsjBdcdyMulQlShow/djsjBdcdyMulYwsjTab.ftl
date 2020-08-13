<#if (bdcdyly==7 && sqlxdm != "4009904") || bdcdyly==1 ||(bdcdyly==4 && sqlxdm == "806")>
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