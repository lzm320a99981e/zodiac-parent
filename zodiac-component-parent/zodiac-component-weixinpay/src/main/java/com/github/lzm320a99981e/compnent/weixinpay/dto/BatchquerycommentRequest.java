package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：拉取订单评价数据(请求参数)
 * 接口地址：@see <a href="https://api.mch.weixin.qq.com/billcommentsp/batchquerycomment">拉取订单评价数据-接口地址</a>
 * 接口文档：@see <a href="https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_17&index=12">拉取订单评价数据-接口文档</a>
 */
@XStreamAlias("xml")
@Data
public class BatchquerycommentRequest {
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
     * "类型":"String(64)",
     * "示例值":"3AE1368BD96B4644FA5823E024CFE938F1B852EFA87919EDDEE324AE24C8C04F",
     * "描述":"签名，详见签名生成算法"
     */
    private String sign;
    /**
     * "字段名":"签名类型",
     * "变量名":"sign_type",
     * "必填":"否",
     * "类型":"String(32)",
     * "示例值":"HMAC-SHA256",
     * "描述":"签名类型，目前仅支持HMAC-SHA256，默认就是HMAC-SHA256"
     */
    private String sign_type;
    /**
     * "字段名":"开始时间",
     * "变量名":"begin_time",
     * "必填":"是",
     * "类型":"String(19)",
     * "示例值":"20170724000000",
     * "描述":"按用户评论时间批量拉取的起始时间，格式为yyyyMMddHHmmss"
     */
    private String begin_time;
    /**
     * "字段名":"结束时间",
     * "变量名":"end_time",
     * "必填":"是",
     * "类型":"String(19)",
     * "示例值":"20170725000000",
     * "描述":"按用户评论时间批量拉取的结束时间，格式为yyyyMMddHHmmss"
     */
    private String end_time;
    /**
     * "字段名":"位移",
     * "变量名":"offset",
     * "必填":"是",
     * "类型":"uint(64)",
     * "示例值":"0",
     * "描述":"指定从某条记录的下一条开始返回记录。接口调用成功时，会返回本次查询最后一条数据的offset。商户需要翻页时，应该把本次调用返回的offset 作为下次调用的入参。注意offset是评论数据在微信支付后台保存的索引，未必是连续的"
     */
    private String offset;
    /**
     * "字段名":"条数",
     * "变量名":"limit",
     * "必填":"否",
     * "类型":"uint(32)",
     * "示例值":"100",
     * "描述":"一次拉取的条数, 最大值是200，默认是200"
     */
    private String limit;
}