var nDeviceIndex = 0;//装置的编号；   0：文档摄像头;1：人像摄像头
var szPostfix = ".jpg";
var nBar = 0;
var Capture;//必须得获取object对象
var Content;
var szFilePath = "D:\\";
var imgeId=0;
var content;




//打开设备
function openDevices_onclick(value){
	var iType = parseInt(value);
	nDeviceIndex = iType;
    var strResult = Capture.OpenDevice(nDeviceIndex);
	Capture.SetCameraExposure(nDeviceIndex,-4);//曝光度设置为-3，-4，亮度比较正常；-5聚焦更快些，但亮度暗些；
	DpiTextChange(200,200);//设置水平方向和竖直方向的DPI值为200；
	//两秒后执行
	setTimeout(Catch_onClick,5000);
}
//关闭设备
function closeDevices_onclick() {
    Capture.CloseDevice(nDeviceIndex);
}

//获取设备状态
function GetDeviceState(){
	var iResult = Capture.GetDeviceState(nDeviceIndex);
	if(iResult==0){
		//设备存在，未被使用
		$.Prompt("设备状态:设备存在，未被使用",1500);
	}else if(iResult==1){
		//设备被打开或被占用
		$.Prompt("设备状态:设备被打开或被占用",1500);
	}else if(iResult<0){
		//设备不存在
		$.Prompt("设备状态:设备不存在",1500);
	}
	return iResult;
}



//设置拍照存档的xDPI,yDPI
function DpiTextChange(XdpiValue,YdpiValue){
	if(Capture.GetDeviceState(nDeviceIndex)==1){
			xDpi = parseInt( XdpiValue );
			yDpi = parseInt( YdpiValue );
		    Capture.SetGrabbedDPI( xDpi, yDpi );
		    Capture.SetDeviceBarcode(nDeviceIndex,1);
	}else{
		$.Prompt("找不到设备或打开失败！",1500);
	}
}
//拍照并且存档,若勾选条码则一起获取条码信息
function Catch_onClick() {
	var szFileName = szFilePath + imgeId.toString() + szPostfix;
	Capture.GrabToFile(szFileName);
	
	 //删除文件
    //Capture.DeleteFolder(szFilePath);
    var szBarcode = "";
	var nBarcodeCount = Capture.GetBarcodeCount()
	for(var k = 0; k < nBarcodeCount; k++)
	{
		szBarcode += Capture.GetBarcodeContent(k);
	}
	content.next().hide();
	if(szBarcode!=''){
		szBarcode=decodeURI(szBarcode);
	}
	content.val(szBarcode);
	
	  
}

