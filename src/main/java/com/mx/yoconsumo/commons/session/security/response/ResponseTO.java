package com.mx.yoconsumo.commons.session.security.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mx.yoconsumo.commons.session.security.model.Notification;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseTO {

	private Object data;
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	@JsonProperty("notifications")
	
	private List<Notification> notification = new ArrayList<Notification>();
	
	public void addNotification(Notification n) {
		notification.add(n);
	}
	
}
