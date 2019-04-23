package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：交易保障(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/payitil/report">交易保障-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_8&index=9">交易保障-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class PayitilReportResponse {
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
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"签名失败",
     * "描述":"返回信息，如非空，为错误原因 签名失败 参数格式校验错误"
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
}