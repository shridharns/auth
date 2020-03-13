package com.shridhar.auth.exception;

import static com.shridhar.auth.constants.Constants.DEBUG;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.dozer.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

	private static Map<String, String> codeMap = new HashMap<>();

	/*
	 * Internal error to external facing error code to prevent internal details.
	 */
	static {
		codeMap.put("500", "code-100");
		codeMap.put("400", "code-101");
		codeMap.put("404", "code-102");
		codeMap.put("422", "code-103");
	}

	private ResponseEntity<Error> buildResponseEntity(Error apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(LoginFailedException.class)
	protected ResponseEntity<Error> handleLoginFailedException(LoginFailedException ex, WebRequest request) {
		return buildResponseEntity(setErrorDetails(ex, BAD_REQUEST));
	}

	@ExceptionHandler({ SQLIntegrityConstraintViolationException.class, ConstraintViolationException.class })
	protected ResponseEntity<Error> handleConstraintViolationException(ConstraintViolationException ex,
			WebRequest request) {
		return buildResponseEntity(setErrorDetails(ex, HttpStatus.UNPROCESSABLE_ENTITY));
	}

	@ExceptionHandler(InvalidFormatException.class)
	protected ResponseEntity<Error> handleInvalidFormatException(InvalidFormatException ex, WebRequest request) {
		return buildResponseEntity(setErrorDetails(ex, BAD_REQUEST));
	}

	@ExceptionHandler(MappingException.class)
	protected ResponseEntity<Error> handleMappingException(MappingException ex, WebRequest request) {
		return buildResponseEntity(setErrorDetails(ex, BAD_REQUEST));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Error> handleGenericException(Exception ex) {
		return buildResponseEntity(setErrorDetails(ex, INTERNAL_SERVER_ERROR));
	}

	private Error setErrorDetails(Exception ex, HttpStatus status) {
		Error apiError = new Error(status);
		apiError.setMessage(status.getReasonPhrase() + " - " + ex.getClass().getSimpleName() + " - "
				+ ex.getLocalizedMessage().split(":")[0]);
		apiError.setCode(codeMap.get(String.valueOf(status.value())));
		if (MDC.get(DEBUG) != null) {
			apiError.setDebugMessage(ex.getLocalizedMessage());
		}
		LOGGER.error("Error handler", ex);
		return apiError;
	}

}
