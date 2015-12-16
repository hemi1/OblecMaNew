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
public class DefaultUzivatelDao implements UzivatelDao{
    
    private UzivatelDao uzivatelDao = UzivatelDaoFactory.Instance.dajUzivatelDao();

    @Override
    public void prihlasPouzivatela(String meno, String heslo) throws Exception {
       uzivatelDao.prihlasPouzivatela(meno, heslo);
    }

    @Override
    public void odhlasPouzivatela() throws Exception {
        uzivatelDao.odhlasPouzivatela();
    }

    @Override
    public boolean jePrihlasny(String meno) {
       return uzivatelDao.jePrihlasny(meno);
    }

    @Override
    public Long vratIdPrihlasenehoPouzivatela() throws Exception {
        return uzivatelDao.vratIdPrihlasenehoPouzivatela();
    }

    @Override
    public void vytvorPouzivatela(String meno, String heslo, boolean muz) throws Exception {
        uzivatelDao.vytvorPouzivatela(meno, heslo, muz);
    }

    @Override
    public List<Long> vratIdVsetkychUzivatelov() {
        return uzivatelDao.vratIdVsetkychUzivatelov();
    }

    @Override
    public boolean jeMuz(Long id) {
        return uzivatelDao.jeMuz(id);
    }

    @Override
    public boolean jeZena(Long id) {
       return uzivatelDao.jeZena(id);
    }
    
}
