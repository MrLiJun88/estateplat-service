<#--图形-->
<div class="ace-settings-container" id="ace-settings-container" style="display: none;">
    <div class="btn btn-app btn-xs btn-warning ace-settings-btn open" id="ace-settings-btn">
        <i class="ace-icon fa fa-globe blue bigger-200"></i>
    </div>

    <div class="ace-settings-box clearfix " id="ace-settings-box">
        <iframe src="" style="width: 100%;height: 100%" id="iframe"></iframe>
    </div>
    <!-- /.ace-settings-box -->
</div>
<div class="row">
    <div class="col-xs-2" style="min-width: 230px">
    <#if "${onlyShowDatePic!}"!='true'>
        <div class="profile-user-info profile-user-info-striped">
            <div class="profile-info-name"> 登记类型</div>
            <div class="profile-info-value">
                <select id="djlxSelect" class="chosen-select" style="width:100px">
                    <#list djlxList as djlx>
                        <option value="${djlx.DM!}">${djlx.MC!}</option>
                    </#list>
                </select>
            </div>
        </div>
    </#if>
    </div>
    <div class="col-xs-3" style="min-width:370px">
    <#if "${onlyShowDatePic!}"!='true'>
        <div class="profile-user-info profile-user-info-striped">
            <div class="profile-info-name"> 申请类型</div>
            <div class="profile-info-value">
                <select id="sqlxSelect" class="chosen-select" style="width:250px" onchange="changeSqlx()">
                    <#list sqlxList as sqlx>
                        <option value="${sqlx.DM!}">${sqlx.MC!}</option>
                    </#list>
                </select>
            </div>
        </div>
    </#if>
    </div>

    <div class="col-xs-3">
    <#if "${onlyShowDatePic!}"!='true'>
        <button type="button" class="btn btn-sm btn-primary" id="save">创建</button>
    </#if>
        <button type="button" class="btn btn-sm btn-primary" id="ylzs" style="display: none;">预览证书</button>
    <#if "${showZjfzBtn!}"=='true'>
        <button type="button" class="btn btn-sm btn-primary" id="fz">缮证</button>
    </#if>
    <#if "${onlyShowDatePic!}"!='true'>
        <button type="button" class="btn btn-sm btn-primary" id="ppxm" style="margin-top: 5px">关联项目</button>
        <button type="button" class="btn btn-sm btn-primary" id="deletexmgl" style="margin-top: 5px;">删除项目关联
        </button>
    </#if>
    </div>
</div>