package edu.iis.mto.blog.domain.errors;

public class DomainError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String USER_NOT_FOUND = "unknown user";
    public static final String POST_NOT_FOUND = "unknown post";
    public static final String SELF_LIKE = "cannot like own post";
    public static final String INVALID_ACCOUNT_STATE = "only a user with a CONFIRMED account status can add a like";
    public static final String UNCONFIRMED_CANNOT_POST = "unconfirmed users cannot post";

    public DomainError(String msg) {
        super(msg);
    }

}
