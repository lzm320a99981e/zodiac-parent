package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：下载对账单(请求参数)
 * 接口地址：@see <a href="下载对账单-接口地址">https://api.mch.weixin.qq.com/pay/downloadbill</a>
 * 接口文档：@see <a href="下载对账单-接口文档">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_6</a>
 */
@XStreamAlias("xml")
@Data
public class DownloadbillRequest {
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
     * "字段名":"对账单日期",
     * "变量名":"bill_date",
     * "必填":"是",
     * "类型":"String(8)",
     * "示例值":"20140603",
     * "描述":"下载对账单的日期，格式：20140603"
     */
    private String bill_date;
    /**
     * "字段名":"账单类型",
     * "变量名":"bill_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"ALL",
     * "描述":"ALL（默认值），返回当日所有订单信息（不含充值退款订单） SUCCESS，返回当日成功支付的订单（不含充值退款订单） REFUND，返回当日退款订单（不含充值退款订单） RECHARGE_REFUND，返回当日充值退款订单"
     */
    private String bill_type;
    /**
     * "字段名":"压缩账单",
     * "变量名":"tar_type",
     * "必填":"否",
     * "类型":"String",
     * "示例值":"GZIP",
     * "描述":"非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。"
     */
    private String tar_type;
}