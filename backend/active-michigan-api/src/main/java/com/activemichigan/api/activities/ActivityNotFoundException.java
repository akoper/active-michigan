package com.activemichigan.api.activities;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ActivityNotFoundException extends RuntimeException {
	public ActivityNotFoundException(long id) {
		super("Activity not found: " + id);
	}
}

