package sk.oblecma;

import java.util.List;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public interface UzivatelDao {
    public void vytvorPouzivatela(String meno,String heslo,boolean muz) throws Exception;
    public void prihlasPouzivatela(String meno,String heslo) throws Exception;
    public void odhlasPouzivatela() throws Exception;
    public boolean jePrihlasny(String meno);
    public Long vratIdPrihlasenehoPouzivatela() throws Exception;
    public boolean jeMuz(Long id);
    public boolean jeZena(Long id);
    public List<Long> vratIdVsetkychUzivatelov();
}
