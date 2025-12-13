package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'un code d'article existe deja
 */
public class DuplicateCodeArticleException extends RuntimeException {

    public DuplicateCodeArticleException(String code) {
        super("Un article avec le code '" + code + "' existe deja");
    }

    public DuplicateCodeArticleException(String message, String code) {
        super(message + " : " + code);
    }
}