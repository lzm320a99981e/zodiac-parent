package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：统一下单(请求参数)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/unifiedorder">统一下单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1">统一下单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class UnifiedorderRequest {
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
     * "字段名":"设备号",
     * "变量名":"device_info",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"013467007045764",
     * "描述":"自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB""
     */
    private String device_info;
    /**
     * "字段名":"随机字符串",
     * "变量名":"nonce_str",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
     * "描述":"随机字符串，长度要求在32位以内。推荐随机数生成算法"
     */
    private String nonce_str;
    /**
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"通过签名算法计算得出的签名值，详见签名生成算法"
     */
    private String sign;
    /**
     * "字段名":"签名类型",
     * "变量名":"sign_type",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"MD5",
     * "描述":"签名类型，默认为MD5，支持HMAC-SHA256和MD5。"
     */
    private String sign_type;
    /**
     * "字段名":"商品描述",
     * "变量名":"body",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"腾讯充值中心-QQ会员充值",
     * "描述":"商品简单描述，该字段请按照规范传递，具体请见参数规定"
     */
    private String body;
    /**
     * "字段名":"商品详情",
     * "变量名":"detail",
     * "必填":"否",
     * "类型":"String(6000)",
     * "示例值":"",
     * "描述":"商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明"
     */
    private String detail;
    /**
     * "字段名":"附加数据",
     * "变量名":"attach",
     * "必填":"否",
     * "类型":"String(127)",
     * "示例值":"深圳分店",
     * "描述":"附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。"
     */
    private String attach;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"20150806125346",
     * "描述":"商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一。详见商户订单号"
     */
    private String out_trade_no;
    /**
     * "字段名":"标价币种",
     * "变量名":"fee_type",
     * "必填":"否",
     * "类型":"String(16)",
     * "示例值":"CNY",
     * "描述":"符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型"
     */
    private String fee_type;
    /**
     * "字段名":"标价金额",
     * "变量名":"total_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"88",
     * "描述":"订单总金额，单位为分，详见支付金额"
     */
    private Integer total_fee;
    /**
     * "字段名":"终端IP",
     * "变量名":"spbill_create_ip",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"123.12.12.123",
     * "描述":"支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP"
     */
    private String spbill_create_ip;
    /**
     * "字段名":"交易起始时间",
     * "变量名":"time_start",
     * "必填":"否",
     * "类型":"String(14)",
     * "示例值":"20091225091010",
     * "描述":"订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则"
     */
    private String time_start;
    /**
     * "字段名":"交易结束时间",
     * "变量名":"time_expire",
     * "必填":"否",
     * "类型":"String(14)",
     * "示例值":"20091227091010",
     * "描述":"订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则 建议：最短失效时间间隔大于1分钟"
     */
    private String time_expire;
    /**
     * "字段名":"订单优惠标记",
     * "变量名":"goods_tag",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"WXG",
     * "描述":"订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠"
     */
    private String goods_tag;
    /**
     * "字段名":"通知地址",
     * "变量名":"notify_url",
     * "必填":"是",
     * "类型":"String(256)",
     * "示例值":"http://www.weixin.qq.com/wxpay/pay.php",
     * "描述":"异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。"
     */
    private String notify_url;
    /**
     * "字段名":"交易类型",
     * "变量名":"trade_type",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"JSAPI",
     * "描述":"JSAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付 说明详见参数规定"
     */
    private String trade_type;
    /**
     * "字段名":"商品ID",
     * "变量名":"product_id",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"12235413214070356458058",
     * "描述":"trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。"
     */
    private String product_id;
    /**
     * "字段名":"指定支付方式",
     * "变量名":"limit_pay",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"no_credit",
     * "描述":"上传此参数no_credit--可限制用户不能使用信用卡支付"
     */
    private String limit_pay;
    /**
     * "字段名":"用户标识",
     * "变量名":"openid",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"oUpF8uMuAJO_M2pxb1Q9zNjWeS6o",
     * "描述":"trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换"
     */
    private String openid;
    /**
     * "字段名":"电子发票入口开放标识",
     * "变量名":"receipt",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"Y",
     * "描述":"Y，传入Y时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效"
     */
    private String receipt;
    /**
     * "字段名":"+场景信息",
     * "变量名":"scene_info",
     * "必填":"否",
     * "类型":"String(256)",
     * "示例值":"{"store_info" : { "id": "SZTX001", "name": "腾大餐厅", "area_code": "440305", "address": "科技园中一路腾讯大厦" }}",
     * "描述":"该字段常用于线下活动时的场景信息上报，支持上报实际门店信息，商户也可以按需求自己上报相关信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }} ，字段详细说明请点击行前的+展开"
     */
    private String scene_info;
    /**
     * "字段名":"-门店id",
     * "变量名":"id",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"SZTX001",
     * "描述":"门店编号，由商户自定义"
     */
    private String id;
    /**
     * "字段名":"-门店名称",
     * "变量名":"name",
     * "必填":"否",
     * "类型":"String(64)",
     * "示例值":"腾讯大厦腾大餐厅",
     * "描述":"门店名称 ，由商户自定义"
     */
    private String name;
    /**
     * "字段名":"-门店行政区划码",
     * "变量名":"area_code",
     * "必填":"否",
     * "类型":"String(6)",
     * "示例值":"440305",
     * "描述":"门店所在地行政区划码，详细见《最新县及县以上行政区划代码》"
     */
    private String area_code;
    /**
     * "字段名":"-门店详细地址",
     * "变量名":"address",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"科技园中一路腾讯大厦",
     * "描述":"门店详细地址 ，由商户自定义"
     */
    private String address;
}