package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：申请退款(请求参数)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/secapi/pay/refund">申请退款-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_4">申请退款-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class RefundRequest {
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
     * "字段名":"签名类型",
     * "变量名":"sign_type",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"HMAC-SHA256",
     * "描述":"签名类型，目前支持HMAC-SHA256和MD5，默认为MD5"
     */
    private String sign_type;
    /**
     * "字段名":"微信订单号",
     * "变量名":"transaction_id",
     * "必填":"二选一",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信生成的订单号，在支付通知中有返回"
     */
    private String transaction_id;
    /**
     * "字段名":"商户退款单号",
     * "变量名":"out_refund_no",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。"
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
     * "字段名":"退款金额",
     * "变量名":"refund_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"退款总金额，订单总金额，单位为分，只能为整数，详见支付金额"
     */
    private Integer refund_fee;
    /**
     * "字段名":"退款货币种类",
     * "变量名":"refund_fee_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"CNY",
     * "描述":"退款货币类型，需与支付一致，或者不填。符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
     */
    private String refund_fee_type;
    /**
     * "字段名":"退款原因",
     * "变量名":"refund_desc",
     * "必填":"否",
     * "类型":"String(80)",
     * "示例值":"商品已售完",
     * "描述":"若商户传入，会在下发给用户的退款消息中体现退款原因 注意：若订单退款金额≤1元，且属于部分退款，则不会在退款消息中体现退款原因"
     */
    private String refund_desc;
    /**
     * "字段名":"退款资金来源",
     * "变量名":"refund_account",
     * "必填":"否",
     * "类型":"String(30)",
     * "示例值":"REFUND_SOURCE_RECHARGE_FUNDS",
     * "描述":"仅针对老资金流商户使用 REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款） REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款"
     */
    private String refund_account;
    /**
     * "字段名":"退款结果通知url",
     * "变量名":"notify_url",
     * "必填":"否",
     * "类型":"String(256)",
     * "示例值":"https://weixin.qq.com/notify/",
     * "描述":"异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。"
     */
    private String notify_url;
}