package com.redjujubetree.qmt.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * A股股票日线行情数据（前复权）
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-05
 */
@TableName("stock_daily_data")
public class StockDailyData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 交易日期
     */
    private Date tradeDate;

    private String tsCode;

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 收盘价
     */
    private Double close;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 最低价
     */
    private Double low;

    /**
     * 成交量（手）
     */
    private Double vol;

    /**
     * 成交额（元）
     */
    private Double amount;

    /**
     * 振幅（%）
     */
    private Double amplitude;

    /**
     * 涨跌额
     */
    private Double dailyChange;

    /**
     * 涨跌幅（%）
     */
    private Double pctChange;

    /**
     * 换手率（%）
     */
    private Double turnoverRate;

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

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getVol() {
        return vol;
    }

    public void setVol(Double vol) {
        this.vol = vol;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(Double amplitude) {
        this.amplitude = amplitude;
    }

    public Double getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(Double dailyChange) {
        this.dailyChange = dailyChange;
    }

    public Double getPctChange() {
        return pctChange;
    }

    public void setPctChange(Double pctChange) {
        this.pctChange = pctChange;
    }

    public Double getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(Double turnoverRate) {
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
            ", amplitude = " + amplitude +
            ", dailyChange = " + dailyChange +
            ", pctChange = " + pctChange +
            ", turnoverRate = " + turnoverRate +
        "}";
    }
}
