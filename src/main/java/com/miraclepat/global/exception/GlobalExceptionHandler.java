package com.miraclepat.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[MethodArgumentNotValidException] " + ex.getMessage());
        List<ValidErrorResponse.ValidationError> errors = ValidErrorResponse.ValidationError.of(ex.getBindingResult());
        return ResponseEntity.status(status).body(ValidErrorResponse.of(ErrorCode.INVALID_REQUEST, null, errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage;
        logger.error("[HttpMessageNotReadableException] " + ex.getMessage());
        //데이터의 형식이 서버에서 예상한 형식과 다를 때
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            errorMessage = cause.getPath().stream().map(it -> it.getDescription()).collect(Collectors.joining(", ")) + ": " + ex.getMessage();

            //데이터의 형식이 서버에서 처리할 수 없는 형식일 때
        } else if (ex.getCause() instanceof MismatchedInputException) {
            MismatchedInputException cause = (MismatchedInputException) ex.getCause();
            errorMessage = cause.getPath().stream().map(it -> it.getFieldName()).collect(Collectors.joining(", ")) + ": " + ex.getMessage();
            //JSON 데이터의 형식이 잘못되었을 때
        } else {
            errorMessage = "유효하지 않은 요청입니다";
        }
        return getInvalidRequestResponse(errorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[HttpRequestMethodNotSupportedException] " + ex.getMessage());
        return getInvalidRequestResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[MissingPathVariableException] " + ex.getMessage());
        return getInvalidRequestResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[MissingServletRequestParameterException] " + ex.getMessage());
        return getInvalidRequestResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[MissingServletRequestPartException] " + ex.getMessage());
        return getInvalidRequestResponse(ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("[HttpMediaTypeNotSupportedException] " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> invalidRequestException(RuntimeException ex) {
        logger.error("[InvalidRequestException]", ex);
        ErrorCode invalidErrorCode = ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(invalidErrorCode.getHttpStatus())
                .body(ErrorResponse.of(invalidErrorCode, ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> noSuchElementException(NoSuchElementException ex) {
        logger.error("[NoSuchElementException]", ex);
        ErrorCode noSuchElementErrorCode = ErrorCode.NO_SUCH_ELEMENT;
        return ResponseEntity.status(noSuchElementErrorCode.getHttpStatus())
                .body(ErrorResponse.of(noSuchElementErrorCode, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception ex) {
        logger.error("[Exception]", ex);
        ErrorCode internalServerErrorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(internalServerErrorCode.getHttpStatus())
                .body(ErrorResponse.of(internalServerErrorCode, ex.getMessage()));
    }

    @ExceptionHandler({SizeLimitExceededException.class, FileSizeLimitExceededException.class})
    public ResponseEntity<ErrorResponse> sizeLimitExceededException(SizeException ex) {
        logger.error("[SizeException]", ex);
        ErrorCode payloadTooLargeErrorCode = ErrorCode.PAYLOAD_TOO_LARGE;
        return ResponseEntity.status(payloadTooLargeErrorCode.getHttpStatus())
                .body(ErrorResponse.of(payloadTooLargeErrorCode, ex.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(CustomException ex) {
        logger.error("[CustomException]", ex);
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(ex.getErrorCode(), ex.getMessage()));
    }

    private ResponseEntity getInvalidRequestResponse(String errorMessage) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_REQUEST, errorMessage));
    }
}
