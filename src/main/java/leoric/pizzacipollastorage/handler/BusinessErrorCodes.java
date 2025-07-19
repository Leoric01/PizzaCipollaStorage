package leoric.pizzacipollastorage.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or Password is incorrect"),
    ENTITY_NOT_FOUND(404, NOT_FOUND, "Requested entity was not found"),
    DUPLICATE_VAT_NAME(306, CONFLICT, "VAT rate with this name already exists"),
    DUPLICATE_INGREDIENT_NAME(307, CONFLICT, "Ingredient with this name already exists"),
    MISSING_INGREDIENT_QUANTITY(309, BAD_REQUEST, "Missing quantity for ingredient in base dish size"),
    SNAPSHOT_ALREADY_EXISTS(308, CONFLICT, "Snapshot for this ingredient already exists in the last 6 hours"),
    INGREDIENT_IN_USE_IN_MENUITEM(310, CONFLICT, "Ingredient is used in one or more menu items"),
    BRANCH_CREATION_FORBIDDEN(311, FORBIDDEN, "Only users with MANAGER role can create a branch"),
    BRANCH_ALREADY_ACCESSIBLE(312, CONFLICT, "User already has access to this branch"),
    BRANCH_ACCESS_REQUEST_ALREADY_PENDING(313, CONFLICT, "There is already a pending access request for this user and branch"),
    NOT_AUTHORIZED_FOR_BRANCH(314, FORBIDDEN, "You are not authorized to manage this branch"),
    REQUEST_ALREADY_RESOLVED(315, CONFLICT, "This access request has already been resolved"),

    EMAIL_ALREADY_IN_USE(305, CONFLICT, "Email is already in use");

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}