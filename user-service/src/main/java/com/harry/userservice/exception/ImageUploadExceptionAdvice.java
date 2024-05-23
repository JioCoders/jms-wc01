package com.harry.userservice.exception;

// import com.jiocoders.rate.base.ResponseBase;
// import com.jiocoders.rate.utils.ApiConstant;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.multipart.MaxUploadSizeExceededException;
// import
// org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// @ControllerAdvice
// public class ImageUploadExceptionAdvice extends
// ResponseEntityExceptionHandler {

// @ExceptionHandler(MaxUploadSizeExceededException.class)
// public ResponseEntity<ResponseBase>
// handleMaxSizeException(MaxUploadSizeExceededException exc) {
// ResponseBase response = new ResponseBase();
// response.setStatus(ApiConstant.BAD_REQUEST_CODE);
// response.setMessage("File too large, please try again!");
// return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
// }
// }