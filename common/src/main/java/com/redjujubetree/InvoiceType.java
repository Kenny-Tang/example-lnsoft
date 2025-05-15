package com.redjujubetree;

public enum InvoiceType {
	VATSpecialInvoice("01","增值税专用发票"),
	VATGeneralInvoice("04", "增值税普通发票"),
	GeneralInvoice("10","电子普通发票");

	private String code ;
	private String description ;
	InvoiceType(String code, String desc) {
		this.code = code;
		this.description = desc;
	}
}
