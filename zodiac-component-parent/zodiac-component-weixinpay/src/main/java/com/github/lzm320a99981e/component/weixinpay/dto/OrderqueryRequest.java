package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：查询订单(是否需要证书)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/orderquery">查询订单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2">查询订单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class OrderqueryRequest {
    /**
     * "字段名":"公众账号ID",
     * "变量名":"appid",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"wxd678efh567hg6787",
     * "描述":"微信支付分配的公众账号ID（企业号corpid即为此appId）"
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
     * "字段名":"微信订单号",
     * "变量名":"transaction_id",
     * "必填":"二选一",
     * "类型":"String(32)",
     * "示例值":"1009660380201506130728806387",
     * "描述":"微信的订单号，建议优先使用"
     */
    private String transaction_id;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"二选一",
     * "类型":"String(32)",
     * "示例值":"20150806125346",
     * "描述":"商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。 详见商户订单号"
     */
    private String out_trade_no;
    /**
     * "字段名":"随机字符串",
     * "变量名":"nonce_str",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"随机字符串，不长于32位。推荐随机数生成算法"
     */
    private String nonce_str;
    /**
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
     * "描述":"通过签名算法计算得出的签名值，详见签名生成算法"
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
}