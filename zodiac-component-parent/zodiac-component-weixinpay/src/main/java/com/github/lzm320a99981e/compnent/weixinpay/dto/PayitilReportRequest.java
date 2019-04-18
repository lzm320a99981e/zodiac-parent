package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：交易保障(输入参数)
 * 接口地址：@see <a href="交易保障-接口地址">https://api.mch.weixin.qq.com/payitil/report</a>
 * 接口文档：@see <a href="交易保障-接口文档">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_8&index=9</a>
 */
@XStreamAlias("xml")
@Data
public class PayitilReportRequest {
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
     * "描述":"微信支付分配的终端设备号，商户自定义"
     */
    private String device_info;
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
     * "字段名":"接口URL",
     * "变量名":"interface_url",
     * "必填":"是",
     * "类型":"String(127)",
     * "示例值":"https://api.mch.weixin.qq.com/pay/unifiedorder",
     * "描述":"报对应的接口的完整URL，类似： https://api.mch.weixin.qq.com/pay/unifiedorder 对于刷卡支付，为更好的和商户共同分析一次业务行为的整体耗时情况，对于两种接入模式，请都在门店侧对一次刷卡支付进行一次单独的整体上报，上报URL指定为： https://api.mch.weixin.qq.com/pay/micropay/total 关于两种接入模式具体可参考本文档章节：刷卡支付商户接入模式 其它接口调用仍然按照调用一次，上报一次来进行。"
     */
    private String interface_url;
    /**
     * "字段名":"接口耗时",
     * "变量名":"execute_time",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"1000",
     * "描述":"接口耗时情况，单位为毫秒"
     */
    private Integer execute_time;
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
     * "描述":"ORDERNOTEXIST—订单不存在 SYSTEMERROR—系统错误"
     */
    private String err_code;
    /**
     * "字段名":"错误代码描述",
     * "变量名":"err_code_des",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"系统错误",
     * "描述":"结果信息描述"
     */
    private String err_code_des;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户系统内部的订单号,商户可以在上报时提供相关商户订单号方便微信支付更好的提高服务质量。"
     */
    private String out_trade_no;
    /**
     * "字段名":"访问接口IP",
     * "变量名":"user_ip",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"8.8.8.8",
     * "描述":"发起接口调用时的机器IP"
     */
    private String user_ip;
    /**
     * "字段名":"商户上报时间",
     * "变量名":"time",
     * "必填":"否",
     * "类型":"String(14)",
     * "示例值":"20091227091010",
     * "描述":"系统时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。其他详见时间规则"
     */
    private String time;
}