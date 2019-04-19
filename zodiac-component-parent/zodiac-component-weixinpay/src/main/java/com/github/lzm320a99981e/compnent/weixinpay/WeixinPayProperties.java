package com.github.lzm320a99981e.compnent.weixinpay;

import lombok.Data;

/**
 * 微信支付配置信息
 */
@Data
public class WeixinPayProperties {
    /**
     * 接口秘钥，用于签名和签证签名
     */
    private String apikey;
    /**
     * 支付应用ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 请求方IP(可固定为服务器IP)
     */
    private String spbill_create_ip;
    /**
     * 支付回调通知路径
     */
    private String notify_url;
    /**
     * 接口请求路径
     */
    private ApiUrl apiUrl;


    @Data
    public static class ApiUrl {
        /**
         * 统一下单
         */
        private String unifiedorder;
        /**
         * 订单查询
         */
        private String orderquery;
        /**
         * 关闭订单
         */
        private String closeorder;
    }
}
