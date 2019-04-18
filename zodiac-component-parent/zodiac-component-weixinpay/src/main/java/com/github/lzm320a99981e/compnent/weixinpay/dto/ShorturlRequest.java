package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：转换短链接(请求参数)
 * 接口地址：@see <a href="转换短链接-接口地址">https://api.mch.weixin.qq.com/tools/shorturl</a>
 * 接口文档：@see <a href="转换短链接-接口文档">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_9&index=10</a>
 */
@XStreamAlias("xml")
@Data
public class ShorturlRequest {
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
     * "字段名":"URL链接",
     * "变量名":"long_url",
     * "必填":"是",
     * "类型":"String(512、",
     * "示例值":"weixin：//wxpay/bizpayurl?sign=XXXXX&appid=XXXXX&mch_id=XXXXX&product_id=XXXXXX&time_stamp=XXXXXX&nonce_str=XXXXX",
     * "描述":"需要转换的URL，签名用原串，传输需URLencode"
     */
    private String long_url;
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
}