<@com.html title="上传材料" import="jquery">
<style type="text/css">
    body{
        font-family: SimSun,Arial;
        color: #333333;
    }
</style>
<body onunload ="DisposeControl()">
<object id="cardReader1" classid="clsid:ACA1E246-6AC1-4442-B2CB-E7D5E116ED81"
        width="900"
        height="500"
        >

</object>
</body>
<script type="text/javascript">
    var w_width=screen.availWidth-21;
    var w_height= screen.availHeight-52;
    cardReader1.width=w_width+"px";
    cardReader1.height=w_height+"px";
    cardReader1.SetURL("${fileCenterUrl!}","${nodeId!}");
    window.returnValue = "ok";
    function DisposeControl()
    {
        cardReader1.CtrDispose();

    }
</script>
</@com.html>