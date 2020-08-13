<div class="col-xs-6">
    <div class="tabbable">
        <ul class="nav nav-tabs">
        <#list gdTabOrderList as gdTabOrder>
            <#if gdTabOrder=='td'>
                <li>
                    <a data-toggle="tab" id="tdTab" href="#td">
                        纯土地证
                    </a>
                </li>
            <#elseif   gdTabOrder=='lq'>
                <li>
                    <a data-toggle="tab" id="lqTab" href="#lq">
                        林权证
                    </a>
                </li>
            <#elseif   gdTabOrder=='cq'>
                <li>
                    <a data-toggle="tab" id="cqTab" href="#cq">
                        草原证
                    </a>
                </li>
            <#else>
                <li>
                    <a data-toggle="tab" id="fwTab" href="#fw">
                        房产证
                    </a>
                </li>
            </#if>
        </#list>
        </ul>
        <div class="tab-content">
        <#--房产-->
        <#include "fcz.ftl">
        <#--林权-->
        <#include "lqz.ftl">
        <#--草原-->
        <#include "cyz.ftl">
        <#--土地-->
        <#include "tdz.ftl">
        </div>
    </div>
</div>