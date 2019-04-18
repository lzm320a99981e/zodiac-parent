package com.github.lzm320a99981e.compnent.weixinpay.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 接口名称：查询退款(返回数据)
 * 接口地址：@see <a href="查询退款-接口地址">https://api.mch.weixin.qq.com/pay/refundquery</a>
 * 接口文档：@see <a href="查询退款-接口文档">https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_5</a>
 */
@XStreamAlias("xml")
@Data
public class RefundqueryResponse {
    /**
     * "字段名":"返回状态码",
     * "变量名":"return_code",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"SUCCESS/FAIL 此字段是通信标识，表示接口层的请求结果，并非退款状态。"
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
     * "描述":"SUCCESS/FAIL SUCCESS退款申请接收成功，结果通过退款查询接口查询 FAIL"
     */
    private String result_code;
    /**
     * "字段名":"错误码",
     * "变量名":"err_code",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"SYSTEMERROR",
     * "描述":"错误码详见第6节"
     */
    private String err_code;
    /**
     * "字段名":"错误描述",
     * "变量名":"err_code_des",
     * "必填":"是",
     * "类型":"String(128)",
     * "示例值":"系统错误",
     * "描述":"结果信息描述"
     */
    private String err_code_des;
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
     * "描述":"随机字符串，不长于32位"
     */
    private String nonce_str;
    /**
     * "字段名":"签名",
     * "变量名":"sign",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"C380BEC2BFD727A4B6845133519F3AD6",
     * "描述":"签名，详见签名算法"
     */
    private String sign;
    /**
     * "字段名":"订单总退款次数",
     * "变量名":"total_refund_count",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"35",
     * "描述":"订单总共已发生的部分退款次数，当请求参数传入offset后有返回"
     */
    private Integer total_refund_count;
    /**
     * "字段名":"微信订单号",
     * "变量名":"transaction_id",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信订单号"
     */
    private String transaction_id;
    /**
     * "字段名":"商户订单号",
     * "变量名":"out_trade_no",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。"
     */
    private String out_trade_no;
    /**
     * "字段名":"订单金额",
     * "变量名":"total_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"订单总金额，单位为分，只能为整数，详见支付金额"
     */
    private Integer total_fee;
    /**
     * "字段名":"应结订单金额",
     * "变量名":"settlement_total_fee",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"当订单使用了免充值型优惠券后返回该参数，应结订单金额=订单金额-免充值优惠券金额。"
     */
    private Integer settlement_total_fee;
    /**
     * "字段名":"货币种类",
     * "变量名":"fee_type",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"CNY",
     * "描述":"订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型"
     */
    private String fee_type;
    /**
     * "字段名":"现金支付金额",
     * "变量名":"cash_fee",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"现金支付金额，单位为分，只能为整数，详见支付金额"
     */
    private Integer cash_fee;
    /**
     * "字段名":"退款笔数",
     * "变量名":"refund_count",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"1",
     * "描述":"当前返回退款笔数"
     */
    private Integer refund_count;
    /**
     * "字段名":"商户退款单号",
     * "变量名":"out_refund_no_$n",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。"
     */
    private String out_refund_no_$n;
    /**
     * "字段名":"微信退款单号",
     * "变量名":"refund_id_$n",
     * "必填":"是",
     * "类型":"String(32)",
     * "示例值":"1217752501201407033233368018",
     * "描述":"微信退款单号"
     */
    private String refund_id_$n;
    /**
     * "字段名":"退款渠道",
     * "变量名":"refund_channel_$n",
     * "必填":"否",
     * "类型":"String(16)",
     * "示例值":"ORIGINAL",
     * "描述":"ORIGINAL—原路退款 BALANCE—退回到余额 OTHER_BALANCE—原账户异常退到其他余额账户 OTHER_BANKCARD—原银行卡异常退到其他银行卡"
     */
    private String refund_channel_$n;
    /**
     * "字段名":"申请退款金额",
     * "变量名":"refund_fee_$n",
     * "必填":"是",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"退款总金额,单位为分,可以做部分退款"
     */
    private Integer refund_fee_$n;
    /**
     * "字段名":"退款金额",
     * "变量名":"settlement_refund_fee_$n",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额"
     */
    private Integer settlement_refund_fee_$n;
    /**
     * "字段名":"代金券类型",
     * "变量名":"coupon_type_$n_$m",
     * "必填":"否",
     * "类型":"String(8)",
     * "示例值":"CASH",
     * "描述":"CASH--充值代金券 NO_CASH---非充值优惠券 开通免充值券功能，并且订单使用了优惠券后有返回（取值：CASH、NO_CASH）。$n为下标,$m为下标,从0开始编号，举例：coupon_type_$0_$1"
     */
    private String coupon_type_$n_$m;
    /**
     * "字段名":"总代金券退款金额",
     * "变量名":"coupon_refund_fee_$n",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠"
     */
    private Integer coupon_refund_fee_$n;
    /**
     * "字段名":"退款代金券使用数量",
     * "变量名":"coupon_refund_count_$n",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"1",
     * "描述":"退款代金券使用数量 ,$n为下标,从0开始编号"
     */
    private Integer coupon_refund_count_$n;
    /**
     * "字段名":"退款代金券ID",
     * "变量名":"coupon_refund_id_$n_$m",
     * "必填":"否",
     * "类型":"String(20)",
     * "示例值":"10000",
     * "描述":"退款代金券ID, $n为下标，$m为下标，从0开始编号"
     */
    private String coupon_refund_id_$n_$m;
    /**
     * "字段名":"单个代金券退款金额",
     * "变量名":"coupon_refund_fee_$n_$m",
     * "必填":"否",
     * "类型":"Int",
     * "示例值":"100",
     * "描述":"单个退款代金券支付金额, $n为下标，$m为下标，从0开始编号"
     */
    private Integer coupon_refund_fee_$n_$m;
    /**
     * "字段名":"退款状态",
     * "变量名":"refund_status_$n",
     * "必填":"是",
     * "类型":"String(16)",
     * "示例值":"SUCCESS",
     * "描述":"退款状态： SUCCESS—退款成功 REFUNDCLOSE—退款关闭。 PROCESSING—退款处理中 CHANGE—退款异常，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，可前往商户平台（pay.weixin.qq.com）-交易中心，手动处理此笔退款。$n为下标，从0开始编号。"
     */
    private String refund_status_$n;
    /**
     * "字段名":"退款资金来源",
     * "变量名":"refund_account_$n",
     * "必填":"否",
     * "类型":"String(30)",
     * "示例值":"REFUND_SOURCE_RECHARGE_FUNDS",
     * "描述":"REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款/基本账户 REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款 $n为下标，从0开始编号。"
     */
    private String refund_account_$n;
    /**
     * "字段名":"退款入账账户",
     * "变量名":"refund_recv_accout_$n",
     * "必填":"是",
     * "类型":"String(64)",
     * "示例值":"招商银行信用卡0403",
     * "描述":"取当前退款单的退款入账方 1）退回银行卡： {银行名称}{卡类型}{卡尾号} 2）退回支付用户零钱: 支付用户零钱 3）退还商户: 商户基本账户 商户结算银行账户 4）退回支付用户零钱通: 支付用户零钱通"
     */
    private String refund_recv_accout_$n;
    /**
     * "字段名":"退款成功时间",
     * "变量名":"refund_success_time_$n",
     * "必填":"否",
     * "类型":"String(20)",
     * "示例值":"2016-07-25 15:26:26",
     * "描述":"退款成功时间，当退款状态为退款成功时有返回。$n为下标，从0开始编号。"
     */
    private String refund_success_time_$n;
}