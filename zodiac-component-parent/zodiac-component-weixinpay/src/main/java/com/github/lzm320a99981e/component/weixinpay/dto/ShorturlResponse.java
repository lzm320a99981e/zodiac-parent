package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：转换短链接(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/tools/shorturl">转换短链接-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_9&index=10">转换短链接-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class ShorturlResponse {
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
     * "描述":"微信分配的公众账号ID"
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
     * "描述":"SYSTEMERROR—系统错误 URLFORMATERROR—URL格式错误"
     */
    private String err_code;
    /**
     * "字段名":"URL链接",
     * "变量名":"short_url",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"weixin：//wxpay/s/XXXXXX",
     * "描述":"转换后的URL"
     */
    private String short_url;
}