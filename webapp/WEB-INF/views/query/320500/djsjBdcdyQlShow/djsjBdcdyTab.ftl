<ul class="nav nav-tabs">
    <#--标签页-->
    <#if bdcdyly==0>
        <#include "djsjBdcdyDjTab.ftl">
    <#elseif bdcdyly==1|| bdcdyly==6 || bdcdyly==7 || bdcdyly==8 >
        <#include "djsjBdcdyYwsjTab.ftl">
    <#elseif bdcdyly==2 ||bdcdyly==5>
        <#include "djsjBdcdyYwsjTab.ftl">
        <#include "djsjBdcdyDjTab.ftl">
    <#elseif bdcdyly==3>
        <#include "djsjBdcdyQlxxTab.ftl">
    <#elseif bdcdyly==4>
        <#include "djsjBdcdyYwsjTab.ftl">
        <#include "djsjBdcdyDjTab.ftl">
        <#include "djsjBdcdyQlxxTab.ftl">
    <#elseif bdcdyly==10>
        <#include "djsjBdcdyYwsjTab.ftl">
        <#include "showIntegartionTab.ftl">
    <#elseif bdcdyly==11>
        <#include "djsjBdcdyXzxxTab.ftl">
    </#if>
</ul>