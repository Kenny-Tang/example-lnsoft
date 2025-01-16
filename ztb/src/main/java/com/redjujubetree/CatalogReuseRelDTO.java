package com.redjujubetree;

import lombok.Data;

@Data
public class CatalogReuseRelDTO {
	private Long virBidId;
	private String projectName;
	private String itemName;
	private String virBidName;
	private String relVirBidName;
	private String vendorName;
	private String bidType;
	private String relProjectName;
	private String relItemName;
	private Long relVirBidId;
}
