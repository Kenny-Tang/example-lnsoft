package com.redjujubetree.qmt.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 沪深A股实时行情（含top500）
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-10
 */
@TableName("top500_stock")
public class Top500Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 股票代码
     */
    @TableId("code")
    private String code;

    /**
     * 股票名称
     */
    @TableField("name")
    private String name;

    /**
     * 最新成交价
     */
    @TableField("last_price")
    private Double lastPrice;

    /**
     * 成交量（股）
     */
    @TableField("volume")
    private Long volume;

    /**
     * 成交额（元）
     */
    @TableField("amount")
    private Long amount;

    /**
     * 今日开盘价
     */
    @TableField("open")
    private Double open;

    /**
     * 昨日收盘价
     */
    @TableField("prev_close")
    private Double prevClose;

    /**
     * 动态市盈率（PE）
     */
    @TableField("pe_dynamic")
    private Double peDynamic;

    /**
     * 市净率（PB）
     */
    @TableField("pb")
    private Double pb;

    /**
     * 总市值（元）
     */
    @TableField("total_mv")
    private Long totalMv;

    /**
     * 流通市值（元）
     */
    @TableField("circ_mv")
    private Long circMv;

    /**
     * 60日涨跌幅（%）
     */
    @TableField("pct_change_60d")
    private Double pctChange60d;

    /**
     * 年初至今涨跌幅（%）
     */
    @TableField("pct_change_ytd")
    private Double pctChangeYtd;

    /**
     * 近几年的最小股息
     */
    @TableField("dividend_yield_min")
    private Double dividendYieldMin;

    /**
     * 分红率
     */
    @TableField("dividend_yield_pct")
    private Double dividendYieldPct;

    /**
     * 均值回归线
     */
    @TableField("k_value")
    private Double kValue;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(Double prevClose) {
        this.prevClose = prevClose;
    }

    public Double getPeDynamic() {
        return peDynamic;
    }

    public void setPeDynamic(Double peDynamic) {
        this.peDynamic = peDynamic;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public Long getTotalMv() {
        return totalMv;
    }

    public void setTotalMv(Long totalMv) {
        this.totalMv = totalMv;
    }

    public Long getCircMv() {
        return circMv;
    }

    public void setCircMv(Long circMv) {
        this.circMv = circMv;
    }

    public Double getPctChange60d() {
        return pctChange60d;
    }

    public void setPctChange60d(Double pctChange60d) {
        this.pctChange60d = pctChange60d;
    }

    public Double getPctChangeYtd() {
        return pctChangeYtd;
    }

    public void setPctChangeYtd(Double pctChangeYtd) {
        this.pctChangeYtd = pctChangeYtd;
    }

    public Double getDividendYieldMin() {
        return dividendYieldMin;
    }

    public void setDividendYieldMin(Double dividendYieldMin) {
        this.dividendYieldMin = dividendYieldMin;
    }

    public Double getDividendYieldPct() {
        return dividendYieldPct;
    }

    public void setDividendYieldPct(Double dividendYieldPct) {
        this.dividendYieldPct = dividendYieldPct;
    }

    public Double getkValue() {
        return kValue;
    }

    public void setkValue(Double kValue) {
        this.kValue = kValue;
    }

    @Override
    public String toString() {
        return "Top500Stock{" +
            "code = " + code +
            ", name = " + name +
            ", lastPrice = " + lastPrice +
            ", volume = " + volume +
            ", amount = " + amount +
            ", open = " + open +
            ", prevClose = " + prevClose +
            ", peDynamic = " + peDynamic +
            ", pb = " + pb +
            ", totalMv = " + totalMv +
            ", circMv = " + circMv +
            ", pctChange60d = " + pctChange60d +
            ", pctChangeYtd = " + pctChangeYtd +
            ", dividendYieldMin = " + dividendYieldMin +
            ", dividendYieldPct = " + dividendYieldPct +
            ", kValue = " + kValue +
        "}";
    }
}
