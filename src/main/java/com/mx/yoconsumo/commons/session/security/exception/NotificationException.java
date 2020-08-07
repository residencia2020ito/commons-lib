package com.mx.yoconsumo.commons.session.security.exception;

import lombok.Data;

@Data
public class NotificationException extends RuntimeException {

	private static final long serialVersionUID = 8952408314203805402L;
	private String code;
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
