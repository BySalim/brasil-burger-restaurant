package com.brasilburger.domain.exceptions;

/**
 * Exception levee lorsqu'un article est introuvable
 */
public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(Long id) {
        super("Article introuvable avec l'ID : " + id);
    }

    public ArticleNotFoundException(String champ, String valeur) {
        super("Article introuvable avec " + champ + " : " + valeur);
    }
}