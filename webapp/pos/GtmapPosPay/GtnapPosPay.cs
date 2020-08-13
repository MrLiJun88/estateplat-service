using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace GtmapPosPay
{
    [StructLayout(LayoutKind.Sequential)]
    struct MisinputStruct
    {
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string transtype; //交易类型
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string amout; //交易金额
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 2)]
        public string transflag; //交易标志,1:零售;2:批发
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string commport; //串口号-保留
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string paycnt; //分期付款期数，如果不是分期付款，则传0
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string oldpostrace; //原交易凭证号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string oldbatchcode; //原交易批次号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 9)]
        public string terminal; //终端号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string transamount; //退货、撤销时收银机通过amout传金额
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 9)]
        public string oldtransdate; //原交易日期(退货、撤销时用到)
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string oldauthornum; //原交易授权号(退货、撤销时用到)
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 20)]
        public string oldtranscardnum; //原交易卡号(退货、撤销时用到)
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string clientMAC; //瘦终端MAC地址
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string branchno; //柜台编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 21)]
        public string orderno; //业务受理编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 2)]
        public string signature; // 电子签名标记 '0'=不需要 '1'=需要
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string fqpayno; //分期项目编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string oldHostSer; //原交易参考号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 38)]
        public string qrCode; //二维码
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 41)]
        public string MerchantName; //商户名称
    }
    [StructLayout(LayoutKind.Sequential)]
    struct ReturnMsg
    {
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string RspCode; //交易结果（00表示交易成功）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 41)]
        public string RspMsg; //交易结果描述（字符串）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string TransType; //交易类型
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 41)]
        public string MerchantName; //商户名称
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 16)]
        public string MerchantNum; //商户编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 9)]
        public string TerminalNum; //终端编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 20)]
        public string TransCardNum; //交易卡号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string ExpDate; //卡片有效期
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string BatchNum; //交易批次号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string OldbatchNum; //原交易批次号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string PosTraceNum; //交易凭证号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string OldposTraceNum; //原交易凭证号（撤销、退货时存在）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 9)]
        public string SettleDate; //银行记帐日期
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 9)]
        public string TransDate; //交易日期
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string TransTime; //交易时间
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 7)]
        public string AuthorNum; //授权号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string TransAmount; //交易金额（撤销、退货时为原交易金额）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 2)]
        public string CardType; //卡类型0－银联1－外卡2－IC卡
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 41)]
        public string CardName; //发卡组织名称
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 51)]
        public string ToAddMsg1; //发卡行信息（如长度为0 则不打印）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 41)]
        public string ToAddMsg2; //中国银联信息（如长度为0 则不打印）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 21)]
        public string ToAddMsg3; //保留
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string branchno; //门店号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 21)]
        public string orderno; //订单号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 3)]
        public string Rspsignature; //电子签名结果（00 表示交易成功，其他表示失败）
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string fqpayno; //分期项目编号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 51)]
        public string szPSW; //会员卡密码，如果有持卡人姓名，上送
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 31)]
        public string sfqsxamt;//分期手续费
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 2)]
        public string szRMB; //内卡结帐平标志 0=平 1不平
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szRMBSale; //内卡消费笔数
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string szRMBSaleAmount;//内卡消费金额
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szRMBVoid; //内卡撤销笔数
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 14)]
        public string szRMBVoidAmount;//内卡撤销金额
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szRMBRefund; //内卡退货笔数
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string szRMBRefundAmount;//内卡退货金额
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szRMBSiginSucc; // 内卡电子签名成功笔数
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szRMBSiginErr; // 内卡电子签名失败笔数
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 5)]
        public string szPosSeq;//输出增加POS机序列号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 13)]
        public string szHostSer; //交易参考号
        [MarshalAs(UnmanagedType.ByValTStr, SizeConst = 21)]
        public string TransActualType; //实际交易类型
    }

    [Guid("53AD4056-8CAD-4785-954B-B16706DA312E")]
    public interface IGtmapPos
    {
        int Sign();
        string Charge(string amout, string orderno);
        string Refund(string amout, string orderno, string oldtransdate, string oldHostSer);
    }
    [Guid("01026045-80D5-4952-94B2-255714644FCF")]
    [ProgId("Gtmap.Pos.Pay")]
    public class GtmapPos : IGtmapPos
    {

        public int Sign()
        {
            MisinputStruct misinputStruct = new MisinputStruct();
            ReturnMsg returnMsg = new ReturnMsg();
            misinputStruct.transtype = "00";
            int res = PaxMisPosInterface(ref misinputStruct, ref returnMsg);
            return res;
        }

        public string Charge(string amout, string orderno)
        {
            if (string.IsNullOrEmpty(amout) || string.IsNullOrEmpty(orderno))
            {
                MessageBox.Show("参数有误！");
                return null;
            }
            MisinputStruct misinputStruct = new MisinputStruct();
            misinputStruct.amout = amout;
            misinputStruct.orderno = orderno;
            misinputStruct.MerchantName = "昆山市不动产登记中心";
            misinputStruct.transtype = "03";
            ReturnMsg returnMsg = new ReturnMsg();
            int res = PaxMisPosInterface(ref misinputStruct, ref returnMsg);
            return ToJson(returnMsg);
        }

        public string Refund(string amout, string orderno, string oldtransdate, string oldHostSer)
        {
            if (string.IsNullOrEmpty(amout) || string.IsNullOrEmpty(orderno)
                || string.IsNullOrEmpty(oldtransdate) || string.IsNullOrEmpty(oldHostSer))
            {
                MessageBox.Show("参数有误！");
                return null;
            }
            MisinputStruct misinputStruct = new MisinputStruct();
            misinputStruct.amout = amout;
            misinputStruct.orderno = orderno;
            misinputStruct.oldtransdate = oldtransdate;
            misinputStruct.oldHostSer = oldHostSer;
            misinputStruct.MerchantName = "昆山市不动产登记中心";
            misinputStruct.transtype = "05";
            ReturnMsg returnMsg = new ReturnMsg();
            int res = PaxMisPosInterface(ref misinputStruct, ref returnMsg);
            return ToJson(returnMsg);
        }


        private string ToJson(object o)
        {
            return o == null ? null : JsonConvert.SerializeObject(o);
        }

        [DllImport(@"PaxMis.dll", EntryPoint = "PaxMisPosInterface")]
        extern static int PaxMisPosInterface(ref MisinputStruct misInputStruct, ref ReturnMsg returnMsg);

    }
}
