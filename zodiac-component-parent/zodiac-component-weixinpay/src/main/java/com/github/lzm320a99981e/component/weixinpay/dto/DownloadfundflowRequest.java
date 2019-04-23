package com.github.lzm320a99981e.component.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：下载资金账单(请求参数)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/pay/downloadfundflow">下载资金账单-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_18&index=7">下载资金账单-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class DownloadfundflowRequest {
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
     * "描述":"签名类型，目前仅支持HMAC-SHA256"
     */
    private String sign_type;
    /**
     * "字段名":"资金账单日期",
     * "变量名":"bill_date",
     * "必填":"是",
     * "类型":"String(8)",
     * "示例值":"20140603",
     * "描述":"下载对账单的日期，格式：20140603"
     */
    private String bill_date;
    /**
     * "字段名":"资金账户类型",
     * "变量名":"account_type",
     * "必填":"是",
     * "类型":"String(8)",
     * "示例值":"Basic",
     * "描述":"账单的资金来源账户： Basic 基本账户 Operation 运营账户 Fees 手续费账户"
     */
    private String account_type;
    /**
     * "字段名":"压缩账单",
     * "变量名":"tar_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"GZIP",
     * "描述":"非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。"
     */
    private String tar_type;
}