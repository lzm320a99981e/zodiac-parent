package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：查询退款(请求参数)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/refundquery">查询退款-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_5">查询退款-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class RefundqueryRequest {
    /**
     * "字段名":"公众账号ID",
     * "变量名":"appid",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"wx8888888888888888",
     * "描述":"微信支付分配的公众账号ID（企业号corpid即为此appId）"
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
     * "必填":"四选一",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信订单号查询的优先级是： refund_id > out_refund_no > transaction_id > out_trade_no"
     */
    private String transaction_id;
    /**
     * "字段名":"偏移量",
     * "变量名":"offset",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"15",
     * "描述":"偏移量，当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录"
     */
    private Integer offset;
}