<div class="col-xs-6">
    <div class="tabbable">
        <ul class="nav nav-tabs" id="myTab">
            <li class="active">
                <a data-toggle="tab" id="dyhTab" href="#file">
                    不动产单元
                </a>
            </li>
            <li>
                <a data-toggle="tab" id="fwTdTab" href="#fwTd" style="display:none;">
                    房屋土地证
                </a>
            </li>
            <div style="float:right">
                <button type="button" class="btn btn-sm btn-primary" id="match">匹配</button>
                <button type="button" class="btn btn-sm btn-primary" id="dismatch">取消匹配</button>
            </div>
        </ul>
        <div class="tab-content">
        <#include "dyh.ftl">
        <#include "fwtd.ftl">
        </div>
    </div>
</div>