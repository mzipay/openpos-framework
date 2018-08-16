package org.jumpmind.pos.core.screen;

public class Toast extends Screen {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private ToastType toastType;
	private int duration = 2500;
	
	public static Toast CreateSuccessToast( String message ) {
		return new Toast(message) {{ setToastType(ToastType.Success);}};
	}
	
	public static Toast CreateWarningToast( String message ) {
		return new Toast(message) {{ setToastType(ToastType.Warn); setDuration(0);}};
	}
	
	public Toast() {
		setScreenType("Toast");
		setName("Toast");
	}
	
	public Toast(String message) {
		this();
		setMessage(message);
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public ToastType getToastType() {
		return toastType;
	}

	public void setToastType(ToastType toastType) {
		this.toastType = toastType;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}

