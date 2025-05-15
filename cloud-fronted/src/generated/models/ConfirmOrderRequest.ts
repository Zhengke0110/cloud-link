/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ConfirmOrderRequest = {
    /**
     * 商品id
     */
    productId?: number;
    /**
     * 购买数量
     */
    buyNum?: number;
    /**
     * 终端类型
     */
    clientType?: string;
    /**
     * 支付类型，微信-银行-支付宝
     */
    payType?: string;
    /**
     * 订单总金额
     */
    totalAmount?: number;
    /**
     * 订单实际支付价格
     */
    payAmount?: number;
    /**
     * 防重令牌
     */
    token?: string;
    /**
     * 发票类型：0->不开发票；1->电子发票；2->纸质发票
     */
    billType?: string;
    /**
     * 发票抬头
     */
    billHeader?: string;
    /**
     * 发票内容
     */
    billContent?: string;
    /**
     * 发票收票人电话
     */
    billReceiverPhone?: string;
    /**
     * 发票收票人邮箱
     */
    billReceiverEmail?: string;
};

