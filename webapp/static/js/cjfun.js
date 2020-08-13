function getCzxx(rowid, table, bdcdyh, proid) {
    var xnzdjh = "320281030005GB00187";//江阴虚拟宗地地籍号
    var sfxndyh = false;
    if (xnzdjh == bdcdyh.substr(0, 19)) {
        sfxndyh = true;
    }
    if (bdcdyh.substr(6, 6) == "000000" && bdcdyh.substr(14, 5) == "00000") {
        sfxndyh = true;
    }

    $.ajax({
        url: bdcdjUrl+"/selectBdcdyQlShow/getPzUrl",
        type: 'GET',
        async:false,
        success: function (data) {
            setTimeout($.unblockUI, 10);
            if (isNotBlank(data)) {
                var cellVal="";
                if (data.dayxUrl !="") {
                    var dayxCellVal = '<span style="cursor:pointer;margin-left: 5px" class="label label-sm label-primary arrowed"  onclick="shouBdcxx(\'' + proid + '\',\'' + bdcdyh + '\',\''+data.dayxUrl+'\')" >档案影像查看</span>'
                    cellVal=cellVal+dayxCellVal;
                }
                if (data.djbxxUrl !="") {
                    var djbxxCellVal = '<span style="cursor:pointer;margin-left: 5px" class="label label-sm label-primary arrowed"  onclick="shouBdcxx(\'' + proid + '\',\'' + bdcdyh + '\',\''+data.djbxxUrl+'\')" >登记簿信息查询</span>'
                    cellVal=cellVal+djbxxCellVal;
                }
                table.setCell(rowid, "CZ", cellVal);
            }
        },
        error: function (data) {

        }
    });
    if (sfxndyh) {
        // var bdcdyCellVal = '<span style="cursor:pointer;margin-left: 5px" class="label label-sm label-primary arrowed"  onclick="bdcPic(\'' + proid + '\')" >虚拟单元号匹配</span>'
        // table.setCell(rowid, "CZ", bdcdyCellVal);
        $("[id='rowid']").find("td").addClass("SelectXnDyh");
    }else{
        // table.setCell(rowid, "CZ", "无");
    }
}

function shouBdcxx(proid,bdcdyh,url) {
    window.open(url + "?proid=" + proid+"&bdcdyh="+bdcdyh);
}

function bdcPic(proids) {
    window.open(bdcdjUrl + "/bdcpic?proids=" + proids+"&wwslbh="+wwslbh)
}

function yzdfYqdr(proids) {
    if (proids != null) {
        var proidArray = proids.split(",");
        for (var i = 0; i < proidArray.length; i++) {
            var proidTemp = proidArray[i];
            var index = $.inArray(proidTemp, $mulRowid);
            if (index < 0) {
                var cm = [];
                cm.PROID = proidTemp;
                $mulData.push(cm);
                $mulRowid.push(proidTemp);
            }
        }
        var bdcXmRelList = {};
        for (var i = 0; i < $mulData.length; i++) {
            bdcXmRelList["bdcXmRelList[" + i + "].proid"] = proid;
            if (this.id == "djsjSure" || this.id == "djsjMulSureBtn") {
                bdcXmRelList["bdcXmRelList[" + i + "].qjid"] = $mulData[i].ID;
            } else {
                bdcXmRelList["bdcXmRelList[" + i + "].yproid"] = $mulData[i].PROID;
            }
            bdcXmRelList["bdcXmRelList[" + i + "].ydjxmly"] = "1";
        }
        bdcXmRelListAll = bdcXmRelList;
        tipInfo("加入成功点击忽略，批量创建");
    }
}