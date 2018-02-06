package org.jumpmind.pos.core.model;

import java.io.Serializable;

public class Total implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String amount;
	
	public Total( String name, String amount ) {
		this.name = name;
		this.amount = amount;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
