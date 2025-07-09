package com.redjujubetree.qmt.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author tanjianwei
 * @since 2025-07-09
 */
@TableName("kenny_stock_info")
public class KennyStockInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("code")
    private String code;

    /**
     * 股票简称
     */
    @TableField("name")
    private String name;

    /**
     * 2024 分红
     */
    @TableField("dividend_24")
    private Double dividend24;

    /**
     * 2023 分红
     */
    @TableField("dividend_23")
    private Double dividend23;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Double getDividend24() {
        return dividend24;
    }

    public void setDividend24(Double dividend24) {
        this.dividend24 = dividend24;
    }

    public Double getDividend23() {
        return dividend23;
    }

    public void setDividend23(Double dividend23) {
        this.dividend23 = dividend23;
    }

    @Override
    public String toString() {
        return "KennyStockInfo{" +
            "id = " + id +
            ", code = " + code +
            ", name = " + name +
            ", dividend24 = " + dividend24 +
            ", dividend23 = " + dividend23 +
        "}";
    }
}
