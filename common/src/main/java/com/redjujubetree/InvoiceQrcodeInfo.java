package com.redjujubetree;

public class InvoiceQrcodeInfo implements InvoiceInfo {

	private String invoiceType;
	private String invoiceCode;
	private String invoiceNo;
	private String invoiceDate;
	private String invoiceAmount;
	private String checkCode;

	@Override
	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	@Override
	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	@Override
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Override
	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Override
	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	@Override
	public String getBuyerName() {
		throw new UnsupportedOperationException("二维码中不存在相关信息");
	}

	@Override
	public String getBuyerTaxId() {
		throw new UnsupportedOperationException("二维码中不存在相关信息");
	}

	@Override
	public String getSellerName() {
		throw new UnsupportedOperationException("二维码中不存在相关信息");
	}

	@Override
	public String getSellerTaxId() {
		throw new UnsupportedOperationException("二维码中不存在相关信息");
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	@Override
	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}


}
