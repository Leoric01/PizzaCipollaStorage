package leoric.pizzacipollastorage.handler;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.handler.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static leoric.pizzacipollastorage.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        log.warn("Email already in use: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.EMAIL_ALREADY_IN_USE.getCode())
                                .businessErrorDescription(BusinessErrorCodes.EMAIL_ALREADY_IN_USE.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(DuplicateIngredientNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateIngredientNameException(DuplicateIngredientNameException ex) {
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.DUPLICATE_INGREDIENT_NAME.getCode())
                                .businessErrorDescription(BusinessErrorCodes.DUPLICATE_INGREDIENT_NAME.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(SnapshotTooRecentException.class)
    public ResponseEntity<ExceptionResponse> handleSnapshotTooRecentException(SnapshotTooRecentException ex) {
        return ResponseEntity
                .status(BusinessErrorCodes.SNAPSHOT_ALREADY_EXISTS.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.SNAPSHOT_ALREADY_EXISTS.getCode())
                                .businessErrorDescription(BusinessErrorCodes.SNAPSHOT_ALREADY_EXISTS.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ENTITY_NOT_FOUND.getCode())
                                .businessErrorDescription(ENTITY_NOT_FOUND.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DuplicateVatRateNameException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateVatRateNameException(DuplicateVatRateNameException ex) {
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.DUPLICATE_VAT_NAME.getCode())
                                .businessErrorDescription(BusinessErrorCodes.DUPLICATE_VAT_NAME.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

//    @ExceptionHandler(LockedException.class)
//    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
//        return ResponseEntity
//                .status(UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode(ACCOUNT_LOCKED.getCode())
//                                .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
//                                .error(exp.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(DisabledException.class)
//    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
//        return ResponseEntity
//                .status(UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode(ACCOUNT_DISABLED.getCode())
//                                .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
//                                .error(exp.getMessage())
//                                .build()
//                );
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ExceptionResponse> handleException() {
//        return ResponseEntity
//                .status(UNAUTHORIZED)
//                .body(
//                        ExceptionResponse.builder()
//                                .businessErrorCode(BAD_CREDENTIALS.getCode())
//                                .businessErrorDescription(BAD_CREDENTIALS.getDescription())
//                                .error(BAD_CREDENTIALS.getDescription())
//                                .build()
//                );
//    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        log.warn(exp.getMessage());
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Internal error, please contact the admin")
                                .error(exp.getMessage())
                                .build()
                );
    }
}