/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

/**
 *
 * @author alfred
 */
public class NeexistujeCisloKategorieException extends Exception {

    /**
     * Creates a new instance of <code>NeexistujeCisloKategorieException</code>
     * without detail message.
     */
    public NeexistujeCisloKategorieException() {
    }

    /**
     * Constructs an instance of <code>NeexistujeCisloKategorieException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NeexistujeCisloKategorieException(String msg) {
        super(msg);
    }
}
