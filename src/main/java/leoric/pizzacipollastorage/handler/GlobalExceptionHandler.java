package leoric.pizzacipollastorage.handler;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import leoric.pizzacipollastorage.handler.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static leoric.pizzacipollastorage.handler.BusinessErrorCodes.ENTITY_NOT_FOUND;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IngredientInUseException.class)
    public ResponseEntity<ExceptionResponse> handleIngredientInUseException(IngredientInUseException ex) {
        log.warn("Ingredient in use: {}", ex.getMessage());
        return ResponseEntity
                .status(CONFLICT)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.INGREDIENT_IN_USE_IN_MENUITEM.getCode())
                                .businessErrorDescription(BusinessErrorCodes.INGREDIENT_IN_USE_IN_MENUITEM.getDescription())
                                .error(ex.getMessage())
                                .build()
                );
    }

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

    @ExceptionHandler(InsufficientRoleException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(InsufficientRoleException ex) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BRANCH_CREATION_FORBIDDEN.getCode())
                        .businessErrorDescription(BusinessErrorCodes.BRANCH_CREATION_FORBIDDEN.getDescription())
                        .error(ex.getMessage())
                        .build());
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

    @ExceptionHandler(MissingQuantityException.class)
    public ResponseEntity<ExceptionResponse> handleMissingQuantityException(MissingQuantityException ex) {
        return ResponseEntity
                .status(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY.getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY.getCode())
                                .businessErrorDescription(BusinessErrorCodes.MISSING_INGREDIENT_QUANTITY.getDescription())
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
//                .branchAccessRequestStatus(UNAUTHORIZED)
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
//                .branchAccessRequestStatus(UNAUTHORIZED)
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
//                .branchAccessRequestStatus(UNAUTHORIZED)
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