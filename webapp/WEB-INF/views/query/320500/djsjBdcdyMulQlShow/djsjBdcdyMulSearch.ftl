<#if showOptimize == "true">
    <#include "djsjBdcdyMulDjsjOptimizeSearch.ftl">
    <#include "djsjBdcdyMulYwsjOptimizeSearch.ftl">
    <#include  "djsjBdcdyMulQlxxOptimizeSearch.ftl">
    <#include "djsjBdcdyMulGdfcOptimizeSearch.ftl">
    <#include "djsjBdcdyMulGdtdOptimizeSearch.ftl">
    <#include  "djsjBdcdyMulGdcfOptimizeSearch.ftl">
<#else>
    <#include  "djsjBdcdyMulDjsjSearch.ftl">
    <#include  "djsjBdcdyMulYwsjSearch.ftl">
    <#include  "djsjBdcdyMulQlxxSearch.ftl">
    <#include  "djsjBdcdyMulGdfcSearch.ftl">
    <#include  "djsjBdcdyMulGdtdSearch.ftl">
    <#include  "djsjBdcdyMulGdcfSearch.ftl">
</#if>
