package leoric.pizzacipollastorage.handler.exceptions;

import leoric.pizzacipollastorage.handler.BusinessErrorCodes;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public BusinessException(BusinessErrorCodes errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

}