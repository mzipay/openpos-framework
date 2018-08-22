package org.jumpmind.pos.core.screen;

import java.io.Serializable;

public class ConfirmationDialog implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String message;
	private String confirmButtonName = "Yes";
	private String cancelButtonName = "No";
	
	public ConfirmationDialog() {
	    
	}
	
	public ConfirmationDialog(String message) {
	    this.message = message;
	}
	
	public ConfirmationDialog(String message, String title) {
	    this.message = message;
	    this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getConfirmButtonName() {
		return confirmButtonName;
	}

	public void setConfirmButtonName(String confirmButtonName) {
		this.confirmButtonName = confirmButtonName;
	}

	public String getCancelButtonName() {
		return cancelButtonName;
	}

	public void setCancelButtonName(String cancelButtonName) {
		this.cancelButtonName = cancelButtonName;
	}
	
}
