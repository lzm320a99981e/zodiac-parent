package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：退款结果通知(通知参数)
 * 接口地址：@see <a href="https://pay.weixin.qq.com/wxpay/pay.action">退款结果通知-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_16&index=11">退款结果通知-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class RefundNotifyRequest {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断"
     */
    private String return_code;
    /**
     * "字段名":"返回信息",
     * "变量名":"return_msg",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"OK",
     * "描述":"当return_code为FAIL时返回信息为错误原因 ，例如 签名失败 参数格式校验错误"
     */
    private String return_msg;
    /**
     * "字段名":"公众账号ID",
     * "变量名":"appid",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"wx8888888888888888",
     * "描述":"微信分配的公众账号ID（企业号corpid即为此appId）"
     */
    private String appid;
    /**
     * "字段名":"退款的商户号",
     * "变量名":"mch_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1900000109",
     * "描述":"微信支付分配的商户号"
     */
    private String mch_id;
    /**
     * "字段名":"随机字符串",
     * "变量名":"nonce_str",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
     * "描述":"随机字符串，不长于32位。推荐随机数生成算法"
     */
    private String nonce_str;
    /**
     * "字段名":"加密信息",
     * "变量名":"req_info",
     * "必填":"是",
     * "类型":"String(1024)",
     * "示例值":"",
     * "描述":"加密信息请用商户秘钥进行解密，详见解密方式"
     */
    private String req_info;
    /**
     * "字段名":"微信订单号",
     * "变量名":"transaction_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信订单号"
     */
    private String transaction_id;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户系统内部的订单号"
     */
    private String out_trade_no;
    /**
     * "字段名":"微信退款单号",
     * "变量名":"refund_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信退款单号"
     */
    private String refund_id;
    /**
     * "字段名":"商户退款单号",
     * "变量名":"out_refund_no",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户退款单号"
     */
    private String out_refund_no;
    /**
     * "字段名":"订单金额",
     * "变量名":"total_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"订单总金额，单位为分，只能为整数，详见支付金额"
     */
    private Integer total_fee;
    /**
     * "字段名":"应结订单金额",
     * "变量名":"settlement_total_fee",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"当该订单有使用非充值券时，返回此字段。应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。"
     */
    private Integer settlement_total_fee;
    /**
     * "字段名":"申请退款金额",
     * "变量名":"refund_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"退款总金额,单位为分"
     */
    private Integer refund_fee;
    /**
     * "字段名":"退款金额",
     * "变量名":"settlement_refund_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额"
     */
    private Integer settlement_refund_fee;
    /**
     * "字段名":"退款状态",
     * "变量名":"refund_status",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS-退款成功 CHANGE-退款异常 REFUNDCLOSE—退款关闭"
     */
    private String refund_status;
    /**
     * "字段名":"退款成功时间",
     * "变量名":"success_time",
     * "必填":"否",
     * "类型":"String(20)",
     * "示例值":"2017-12-15 09:46:01",
     * "描述":"资金退款至用户帐号的时间，格式2017-12-15 09:46:01"
     */
    private String success_time;
    /**
     * "字段名":"退款入账账户",
     * "变量名":"refund_recv_accout",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"招商银行信用卡0403",
     * "描述":"取当前退款单的退款入账方"
     */
    private String refund_recv_accout;
    /**
     * "字段名":"退款资金来源",
     * "变量名":"refund_account",
     * "必填":"是",
     * "类型":"String(30)",
     * "示例值":"REFUND_SOURCE_RECHARGE_FUNDS",
     * "描述":"REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户 REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款"
     */
    private String refund_account;
    /**
     * "字段名":"退款发起来源",
     * "变量名":"refund_request_source",
     * "必填":"是",
     * "类型":"String(30)",
     * "示例值":"API",
     * "描述":"API接口 VENDOR_PLATFORM商户平台"
     */
    private String refund_request_source;
}