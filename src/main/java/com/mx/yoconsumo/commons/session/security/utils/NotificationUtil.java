package com.mx.yoconsumo.commons.session.security.utils;

import com.mx.yoconsumo.commons.session.security.exception.NotificationException;

public final class NotificationUtil {
	public static void send(String code, String message) {
		NotificationException n = new NotificationException();
		n.setCode(code);
		n.setMessage(message);
		throw n;
	}
}
