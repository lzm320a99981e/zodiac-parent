package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：下载对账单(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/downloadbill">下载对账单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_6">下载对账单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class DownloadbillResponse {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"FAIL",
     * "描述":"FAIL"
     */
    private String return_code;
    /**
     * "字段名":"错误码描述",
     * "变量名":"return_msg",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"签名失败",
     * "描述":"返回信息，如非空，为错误原因 如：签名失败 等。"
     */
    private String return_msg;
    /**
     * "字段名":"错误码",
     * "变量名":"error_code",
     * "必填":"否",
     * "类型":"String(16)",
     * "示例值":"20002",
     * "描述":"失败错误码，详见错误码列表"
     */
    private String error_code;
}