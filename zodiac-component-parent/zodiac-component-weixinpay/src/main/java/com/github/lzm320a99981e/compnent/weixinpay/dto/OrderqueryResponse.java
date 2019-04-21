package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：查询订单(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/orderquery">查询订单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2">查询订单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class OrderqueryResponse {
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
     * "示例值":"wxd678efh567hg6787",
     * "描述":"微信分配的公众账号ID"
     */
    private String appid;
    /**
     * "字段名":"商户号",
     * "变量名":"mch_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1230000109",
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
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"签名，详见签名生成算法"
     */
    private String sign;
    /**
     * "字段名":"业务结果",
     * "变量名":"result_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS/FAIL"
     */
    private String result_code;
    /**
     * "字段名":"错误代码",
     * "变量名":"err_code",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"",
     * "描述":"当result_code为FAIL时返回错误代码，详细参见下文错误列表"
     */
    private String err_code;
    /**
     * "字段名":"错误代码描述",
     * "变量名":"err_code_des",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"",
     * "描述":"当result_code为FAIL时返回错误描述，详细参见下文错误列表"
     */
    private String err_code_des;
    /**
     * "字段名":"设备号",
     * "变量名":"device_info",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"013467007045764",
     * "描述":"微信支付分配的终端设备号"
     */
    private String device_info;
    /**
     * "字段名":"用户标识",
     * "变量名":"openid",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"oUpF8uMuAJO_M2pxb1Q9zNjWeS6o",
     * "描述":"用户在商户appid下的唯一标识"
     */
    private String openid;
    /**
     * "字段名":"是否关注公众账号",
     * "变量名":"is_subscribe",
     * "必填":"是",
     * "类型":"String(1)",
     * "示例值":"Y",
     * "描述":"用户是否关注公众账号，Y-关注，N-未关注"
     */
    private String is_subscribe;
    /**
     * "字段名":"交易类型",
     * "变量名":"trade_type",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"JSAPI",
     * "描述":"调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY，详细说明见参数规定"
     */
    private String trade_type;
    /**
     * "字段名":"交易状态",
     * "变量名":"trade_state",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVOKED—已撤销（付款码支付） USERPAYING--用户支付中（付款码支付） PAYERROR--支付失败(其他原因，如银行返回失败) 支付状态机请见下单API页面"
     */
    private String trade_state;
    /**
     * "字段名":"付款银行",
     * "变量名":"bank_type",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"CMC",
     * "描述":"银行类型，采用字符串类型的银行标识"
     */
    private String bank_type;
    /**
     * "字段名":"标价金额",
     * "变量名":"total_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"订单总金额，单位为分"
     */
    private Integer total_fee;
    /**
     * "字段名":"应结订单金额",
     * "变量名":"settlement_total_fee",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。"
     */
    private Integer settlement_total_fee;
    /**
     * "字段名":"标价币种",
     * "变量名":"fee_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"CNY",
     * "描述":"货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
     */
    private String fee_type;
    /**
     * "字段名":"现金支付金额",
     * "变量名":"cash_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"现金支付金额订单现金支付金额，详见支付金额"
     */
    private Integer cash_fee;
    /**
     * "字段名":"现金支付币种",
     * "变量名":"cash_fee_type",
     * "必填":"否",
     * "类型":"String(16)",
     * "示例值":"CNY",
     * "描述":"货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
     */
    private String cash_fee_type;
    /**
     * "字段名":"代金券金额",
     * "变量名":"coupon_fee",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"“代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，详见支付金额"
     */
    private Integer coupon_fee;
    /**
     * "字段名":"代金券使用数量",
     * "变量名":"coupon_count",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"1",
     * "描述":"代金券使用数量"
     */
    private Integer coupon_count;
    /**
     * "字段名":"代金券类型",
     * "变量名":"coupon_type_$n",
     * "必填":"否",
     * "类型":"String",
     * "示例值":"CASH",
     * "描述":"CASH--充值代金券 NO_CASH---非充值优惠券 开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_$0"
     */
    private String coupon_type_$n;
    /**
     * "字段名":"代金券ID",
     * "变量名":"coupon_id_$n",
     * "必填":"否",
     * "类型":"String(20)",
     * "示例值":"10000",
     * "描述":"代金券ID, $n为下标，从0开始编号"
     */
    private String coupon_id_$n;
    /**
     * "字段名":"单个代金券支付金额",
     * "变量名":"coupon_fee_$n",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"单个代金券支付金额, $n为下标，从0开始编号"
     */
    private Integer coupon_fee_$n;
    /**
     * "字段名":"微信支付订单号",
     * "变量名":"transaction_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1009660380201506130728806387",
     * "描述":"微信支付订单号"
     */
    private String transaction_id;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"20150806125346",
     * "描述":"商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。"
     */
    private String out_trade_no;
    /**
     * "字段名":"附加数据",
     * "变量名":"attach",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"深圳分店",
     * "描述":"附加数据，原样返回"
     */
    private String attach;
    /**
     * "字段名":"支付完成时间",
     * "变量名":"time_end",
     * "必填":"是",
     * "类型":"String(14)",
     * "示例值":"20141030133525",
     * "描述":"订单支付时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则"
     */
    private String time_end;
    /**
     * "字段名":"交易状态描述",
     * "变量名":"trade_state_desc",
     * "必填":"是",
     * "类型":"String(256)",
     * "示例值":"支付失败，请重新下单支付",
     * "描述":"对当前查询订单状态的描述和下一步操作的指引"
     */
    private String trade_state_desc;
}