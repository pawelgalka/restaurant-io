package com.agh.restaurant.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandler {
	private class ErrorResponse {
		private String message;
		private String key;

		public ErrorResponse(String message, String key) {
			super();
			this.message = message;
			this.key = key;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}

	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@org.springframework.web.bind.annotation.ExceptionHandler(value = AccessDeniedException.class)
	public @ResponseBody ErrorResponse handleBaseException(AccessDeniedException e) {
		return new ErrorResponse(e.getMessage(), "");
	}
}
