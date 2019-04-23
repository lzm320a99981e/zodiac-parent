package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：统一下单(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/unifiedorder">统一下单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1">统一下单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class UnifiedorderResponse {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断"
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
     * "描述":"调用接口提交的公众账号ID"
     */
    private String appid;
    /**
     * "字段名":"商户号",
     * "变量名":"mch_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1900000109",
     * "描述":"调用接口提交的商户号"
     */
    private String mch_id;
    /**
     * "字段名":"设备号",
     * "变量名":"device_info",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"013467007045764",
     * "描述":"自定义参数，可以为请求支付的终端设备号等"
     */
    private String device_info;
    /**
     * "字段名":"随机字符串",
     * "变量名":"nonce_str",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
     * "描述":"微信返回的随机字符串"
     */
    private String nonce_str;
    /**
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"微信返回的签名值，详见签名算法"
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
     * "字段名":"交易类型",
     * "变量名":"trade_type",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"JSAPI",
     * "描述":"JSAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付 说明详见参数规定"
     */
    private String trade_type;
    /**
     * "字段名":"预支付交易会话标识",
     * "变量名":"prepay_id",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"wx201410272009395522657a690389285100",
     * "描述":"微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时"
     */
    private String prepay_id;
    /**
     * "字段名":"二维码链接",
     * "变量名":"code_url",
     * "必填":"否",
     * "类型":"String(64)",
     * "示例值":"weixin://wxpay/bizpayurl/up?pr=NwY5Mz9&groupid=00",
     * "描述":"trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。 注意：code_url的值并非固定，使用时按照URL格式转成二维码即可"
     */
    private String code_url;
}