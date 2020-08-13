<#macro contentDiv title="" width="">
<div id="formBody" class="formBody" style="<#if width?? && width!="">width:${width!}px</#if> ">
    <div class="firstTitle">${title!}</div>
    <div class="page-content">
        <#nested/>
    </div>
</div>
</#macro>
<#--form表单-->
<#macro form id="" name="" action="" method="">
<script type="text/javascript">
    function formClickEvent(fn, args) {
        fn.apply(this, args);
    }
</script>
<form class="UItable" id="${id!}" name="${name!}" <#if action!="">action="${action!}"</#if>>
    <#nested/>
</form>
</#macro>
<#--form表单中secondTitle-->
<#macro secondDiv id="" name="" action="" method="">

    <#nested/>
</#macro>
<#--form表格-->
<#macro table id="" name="" style="">
<table cellpadding="0" cellspacing="0" border="0" class="tableA" id="${id!}" name="${name!}" style="${style!}">
    <tbody>
        <#nested/>
    </tbody>
</table>
</#macro>
<#macro tr  style="" id="" name="">
<tr style="${style!}" id="${id!}" name="${name!}">
    <#nested/>
</tr>
</#macro>
<#macro th  id=""   name="" style="" width="" height="" colspan=""  rowspan="" class="" >
<th id="${id!}" name="${name!}" style="${style!}" width="<#if width?? && width!="">${width!}</#if>"
    height="${height!}" colspan="${colspan!}" rowspan="${rowspan!}" class="<#if class?? &&class!=""> ${class}</#if>">
    <#nested/>
</th>
</#macro>
<#macro td  id=""   name="" style="" width="" height="" colspan="" rowspan="" class="">
<td id="${id!}" name="${name!}" style="${style!}" width="<#if width?? && width!="">${width!}</#if>"
    height="${height!}" colspan="${colspan!}" rowspan="${rowspan!}" class="<#if class?? &&class!=""> ${class}</#if>" >
    <#nested/>
</td>
</#macro>
<#--form表单label-->
<#macro label name="" style="" class=""><label style="${style!}" class="${class!}">${name!}</label></#macro>
<#--form表单输入框-->
<#macro text id=""   name=""   value="" style="" readonly="">
<input style="${style!}" type="text" id="${id!}" name="${name!}" value="${value!}" width="100%" <#if readonly?? && readonly!=""> readonly="true"</#if>>
</#macro>
<#--form表单radio-->
<#macro radio id=""   name="" style="" valueFieldName="" defaultValue="" saveValue="">
<input style="${style!}" type="radio" id="${id!}" name="${name!}" value="${valueFieldName!}"
       <#if valueFieldName==saveValue>checked="checked"<#elseif valueFieldName==defaultValue>checked="checked"</#if>><label><#nested/></label>
</#macro>
<#--form表单下拉框-->
<#macro select id=""  name="" style="" showFieldName="" valueFieldName="" source="" defaultValue="" handler="" noEmptyValue="">
    <#assign txt="{'source':${source}}" />
    <#assign json=txt?eval />
<select id="${id!}" name="${name!}" style="${style!}" class="select" <#if handler??&&handler!="" >onchange="formClickEvent(${handler!},[])"</#if>>
    <#if noEmptyValue!="true">
        <option value=""></option>
    </#if>
    <#list json.source  as item>
        <option value="${item["${valueFieldName?if_exists}"]}" <#if item["${valueFieldName?if_exists}"]==defaultValue>
                selected="selected"</#if>>${item["${showFieldName?if_exists}"]}</option>
    </#list>
</select>
</#macro>
<#--form表单input隐藏域-->
<#macro hidden id=""   name=""   value="">
<input type="hidden" id="${id!}" name="${name!}" value="${value!}">
</#macro>

<#--form表单按钮-->
<#macro buttons >
<div class="simpleSearch" style="text-align: left;">
    <#nested/>
</div>
</#macro>

<#macro button id="" name="" handler="" type="" text="" iClass="" class="">
<button name="${name!}" id="${id!}" type="<#if type?? && type!="">${type!}<#else>button</#if>"onclick="formClickEvent(eval('${handler!}'),[])"
        class="<#if class?? && class!="">${class!}<#else>btn btn-primary save</#if>">
    <#if iClass?? && iClass!=""><i class="${iClass!}"></i></#if>
    <span>${text!}</span>
</button>
</#macro>

<#macro textarea id="" name="" style="" class="" cols="" rows="3" value="" readonly="">
    <textarea name="${name!}" id="${id!}" <#if style!=""> style="${style}"</#if> class="textarea<#if class!=""> ${class}</#if>"<#if cols!=""> cols="${cols}"</#if><#if rows!=""> rows="${rows}"</#if><#if readonly?? && readonly!=""> readonly="true"</#if> value="${value!}">${value!} </textarea>
</#macro>

<#macro date id="" name="" style="" value="">
    <span class="input-icon date">
         <input type="text" class="date-picker form-control" id="${id!}" name="${name!}"
                data-date-format="yyyy-mm-dd" <#if style!=""> style="${style}"</#if> value="${value!}">
        <i class="ace-icon fa fa-calendar"></i>
    </span>
    <script type="text/javascript">
        //时间控件
        $('.date-picker').datepicker({
        autoclose:true,
        todayHighlight:true,
        language:'zh-CN'
        }).next().on(ace.click_event, function () {
        $(this).prev().focus();
        });
    </script>
</#macro>

<#--签名图片-->
<#macro img   src="" width="" height=""signId="" style="">
    <#if signId=="">
    <img  src="" width="${width!}" height="${height!}" style="display: none"/>
    <#else >
    <img src="${src!}" width="${width!}" height="${height!}" />
    </#if>
</#macro>
