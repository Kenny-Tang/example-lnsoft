package com.redjujubetree.users.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 每日股票交易数据
 * </p>
 *
 * @author tanjianwei
 * @since 2025-06-28
 */
@TableName("stock_daily_data")
public class StockDailyData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日期
     */
    private Date tradeDate;

    /**
     * 股票代码
     */
    private String tsCode;

    /**
     * 开盘
     */
    private BigDecimal open;

    /**
     * 收盘
     */
    private BigDecimal close;

    /**
     * 最高
     */
    private BigDecimal high;

    /**
     * 最低
     */
    private BigDecimal low;

    /**
     * 成交量（单位：股）
     */
    private Long vol;

    /**
     * 成交额（单位：元）
     */
    private BigDecimal amount;

    /**
     * 振幅（%）
     */
    private BigDecimal pctChg;

    /**
     * 涨跌幅（%）
     */
    private BigDecimal pctChange;

    /**
     * 涨跌额
     */
    private BigDecimal dailyChange;

    /**
     * 换手率（%）
     */
    private BigDecimal turnoverRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTsCode() {
        return tsCode;
    }

    public void setTsCode(String tsCode) {
        this.tsCode = tsCode;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public Long getVol() {
        return vol;
    }

    public void setVol(Long vol) {
        this.vol = vol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPctChg() {
        return pctChg;
    }

    public void setPctChg(BigDecimal pctChg) {
        this.pctChg = pctChg;
    }

    public BigDecimal getPctChange() {
        return pctChange;
    }

    public void setPctChange(BigDecimal pctChange) {
        this.pctChange = pctChange;
    }

    public BigDecimal getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(BigDecimal dailyChange) {
        this.dailyChange = dailyChange;
    }

    public BigDecimal getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(BigDecimal turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    @Override
    public String toString() {
        return "StockDailyData{" +
            "id = " + id +
            ", tradeDate = " + tradeDate +
            ", tsCode = " + tsCode +
            ", open = " + open +
            ", close = " + close +
            ", high = " + high +
            ", low = " + low +
            ", vol = " + vol +
            ", amount = " + amount +
            ", pctChg = " + pctChg +
            ", pctChange = " + pctChange +
            ", dailyChange = " + dailyChange +
            ", turnoverRate = " + turnoverRate +
        "}";
    }

}
