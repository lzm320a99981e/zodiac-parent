package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：支付结果通知(通知参数)
 * 接口地址：@see <a href="https://pay.weixin.qq.com/wxpay/pay.action">支付结果通知-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7&index=8">支付结果通知-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class PayNotifyRequest {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS"
     */
    private String return_code;
    /**
     * "字段名":"返回信息",
     * "变量名":"return_msg",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"OK",
     * "描述":"OK"
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
     * "字段名":"商户号",
     * "变量名":"mch_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1900000109",
     * "描述":"微信支付分配的商户号"
     */
    private String mch_id;
    /**
     * "字段名":"设备号",
     * "变量名":"device_info",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"013467007045764",
     * "描述":"微信支付分配的终端设备号，"
     */
    private String device_info;
    /**
     * "字段名":"随机字符串",
     * "变量名":"nonce_str",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
     * "描述":"随机字符串，不长于32位"
     */
    private String nonce_str;
    /**
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"签名，详见签名算法"
     */
    private String sign;
    /**
     * "字段名":"签名类型",
     * "变量名":"sign_type",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"HMAC-SHA256",
     * "描述":"签名类型，目前支持HMAC-SHA256和MD5，默认为MD5"
     */
    private String sign_type;
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
     * "示例值":"SYSTEMERROR",
     * "描述":"错误返回的信息描述"
     */
    private String err_code;
    /**
     * "字段名":"错误代码描述",
     * "变量名":"err_code_des",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"系统错误",
     * "描述":"错误返回的信息描述"
     */
    private String err_code_des;
    /**
     * "字段名":"用户标识",
     * "变量名":"openid",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"wxd930ea5d5a258f4f",
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
     * "描述":"JSAPI、NATIVE、APP"
     */
    private String trade_type;
    /**
     * "字段名":"付款银行",
     * "变量名":"bank_type",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"CMC",
     * "描述":"银行类型，采用字符串类型的银行标识，银行类型见银行列表"
     */
    private String bank_type;
    /**
     * "字段名":"订单金额",
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
     * "描述":"应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。"
     */
    private Integer settlement_total_fee;
    /**
     * "字段名":"货币种类",
     * "变量名":"fee_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"CNY",
     * "描述":"货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
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
     * "字段名":"现金支付货币类型",
     * "变量名":"cash_fee_type",
     * "必填":"否",
     * "类型":"String(16)",
     * "示例值":"CNY",
     * "描述":"货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
     */
    private String cash_fee_type;
    /**
     * "字段名":"总代金券金额",
     * "变量名":"coupon_fee",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"10",
     * "描述":"代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额"
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
     * "描述":"CASH--充值代金券 NO_CASH---非充值代金券 并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0"
     */
    private String coupon_type_$n;
    /**
     * "字段名":"代金券ID",
     * "变量名":"coupon_id_$n",
     * "必填":"否",
     * "类型":"String(20)",
     * "示例值":"10000",
     * "描述":"代金券ID,$n为下标，从0开始编号"
     */
    private String coupon_id_$n;
    /**
     * "字段名":"单个代金券支付金额",
     * "变量名":"coupon_fee_$n",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"单个代金券支付金额,$n为下标，从0开始编号"
     */
    private Integer coupon_fee_$n;
    /**
     * "字段名":"微信支付订单号",
     * "变量名":"transaction_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信支付订单号"
     */
    private String transaction_id;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1212321211201407033568112322",
     * "描述":"商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。"
     */
    private String out_trade_no;
    /**
     * "字段名":"商家数据包",
     * "变量名":"attach",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"123456",
     * "描述":"商家数据包，原样返回"
     */
    private String attach;
    /**
     * "字段名":"支付完成时间",
     * "变量名":"time_end",
     * "必填":"是",
     * "类型":"String(14)",
     * "示例值":"20141030133525",
     * "描述":"支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则"
     */
    private String time_end;
}