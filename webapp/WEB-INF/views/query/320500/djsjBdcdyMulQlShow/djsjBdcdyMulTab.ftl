<ul class="nav nav-tabs">
    <#if bdcdyly==0>
        <#include "djsjBdcdyMulDjsjTab.ftl">
    <#elseif bdcdyly==1|| bdcdyly==6 >
        <#include "djsjBdcdyMulYwsjTab.ftl">
    <#elseif bdcdyly==2 ||bdcdyly==5>
        <#include "djsjBdcdyMulYwsjTab.ftl">
        <#include "djsjBdcdyMulDjsjTab.ftl">
    <#elseif bdcdyly==3>
        <#include "djsjBdcdyMulQlxxTab.ftl">
    <#elseif bdcdyly==4>
        <#include "djsjBdcdyMulYwsjTab.ftl">
        <#include "djsjBdcdyMulDjsjTab.ftl">
        <#include "djsjBdcdyMulQlxxTab.ftl">
        <#include "djsjBdcdyMulCfqdTab.ftl">
    <#elseif bdcdyly==7 || (bdcdyly==8 && sqlxdm=="9920001"||sqlxdm=="9920005")>
        <#if sqlxdm != "4009904">
            <#include "djsjBdcdyMulYwsjTab.ftl">
        </#if>
        <#if sqlxdm != "4009904" && sqlxdm!="9920001" && sqlxdm!="9920005">
            <#include "djsjBdcdyMulDyqdTab.ftl">
        </#if>
    </#if>
</ul>