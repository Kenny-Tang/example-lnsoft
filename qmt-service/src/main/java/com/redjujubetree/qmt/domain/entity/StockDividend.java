package com.redjujubetree.qmt.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-07
 */
@TableName("stock_dividend")
public class StockDividend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票代码
     */
    @TableField("stock_code")
    private String stockCode;

    /**
     * 股票名称
     */
    @TableField("stock_name")
    private String stockName;

    /**
     * 公告日期
     */
    @TableField("Announcement_Date")
    private Date announcementDate;

    /**
     * 报告期
     */
    @TableField("real_year")
    private Integer realYear;

    /**
     * 派息
     */
    @TableField("Dividend_Distribution")
    private Double dividendDistribution;

    /**
     * 送股
     */
    @TableField("bonus_share")
    private Long bonusShare;

    /**
     * 转增
     */
    @TableField("converted_share")
    private Long convertedShare;

    /**
     * 进度
     */
    @TableField("progress")
    private String progress;

    /**
     * 除权除息日
     */
    @TableField("ex_dividend_date")
    private Date exDividendDate;

    /**
     * 股权登记日
     */
    @TableField("record_date")
    private Date recordDate;

    /**
     * 红股上市日
     */
    @TableField("bonus_listing_date")
    private Date bonusListingDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Date getAnnouncementDate() {
        return announcementDate;
    }

    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }

    public Integer getRealYear() {
        return realYear;
    }

    public void setRealYear(Integer realYear) {
        this.realYear = realYear;
    }

    public Double getDividendDistribution() {
        return dividendDistribution;
    }

    public void setDividendDistribution(Double dividendDistribution) {
        this.dividendDistribution = dividendDistribution;
    }

    public Long getBonusShare() {
        return bonusShare;
    }

    public void setBonusShare(Long bonusShare) {
        this.bonusShare = bonusShare;
    }

    public Long getConvertedShare() {
        return convertedShare;
    }

    public void setConvertedShare(Long convertedShare) {
        this.convertedShare = convertedShare;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Date getExDividendDate() {
        return exDividendDate;
    }

    public void setExDividendDate(Date exDividendDate) {
        this.exDividendDate = exDividendDate;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Date getBonusListingDate() {
        return bonusListingDate;
    }

    public void setBonusListingDate(Date bonusListingDate) {
        this.bonusListingDate = bonusListingDate;
    }

    @Override
    public String toString() {
        return "StockDividend{" +
            "id = " + id +
            ", stockCode = " + stockCode +
            ", stockName = " + stockName +
            ", announcementDate = " + announcementDate +
            ", realYear = " + realYear +
            ", dividendDistribution = " + dividendDistribution +
            ", bonusShare = " + bonusShare +
            ", convertedShare = " + convertedShare +
            ", progress = " + progress +
            ", exDividendDate = " + exDividendDate +
            ", recordDate = " + recordDate +
            ", bonusListingDate = " + bonusListingDate +
        "}";
    }
}
