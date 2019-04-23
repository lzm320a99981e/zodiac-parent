package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：拉取订单评价数据(返回结果)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/billcommentsp/batchquerycomment">拉取订单评价数据-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_17&index=12">拉取订单评价数据-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class BatchquerycommentResponse {
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
     * "类型":"string(16)",
     * "示例值":"FAIL",
     * "描述":"FAIL 此字段是业务标识，表示业务是否成功。目前只在失败时返回这个字段，所以只会出现FAIL值"
     */
    private String result_code;
    /**
     * "字段名":"错误代码",
     * "变量名":"err_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SYSTEMERROR",
     * "描述":"见错误码列表"
     */
    private String err_code;
    /**
     * "字段名":"错误代码描述",
     * "变量名":"err_code_des",
     * "必填":"否",
     * "类型":"String(128)",
     * "示例值":"签名失败",
     * "描述":"错误信息描述"
     */
    private String err_code_des;
}