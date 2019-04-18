package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：退款结果通知(返回参数)
 * 接口地址：@see <a href="退款结果通知-接口地址">https://pay.weixin.qq.com/wxpay/pay.action”</a>
 * 接口文档：@see <a href="退款结果通知-接口文档">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_16&index=11</a>
 */
@XStreamAlias("xml")
@Data
public class RefundNotifyResponse {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS/FAIL"
     */
    private String return_code;
    /**
     * "字段名":"返回信息",
     * "变量名":"return_msg",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"OK",
     * "描述":"返回信息，如非空，为错误原因"
     */
    private String return_msg;
}