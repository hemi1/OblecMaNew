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
public class DefaultPocasieDao implements PocasieDao{
    private PocasieDao pocasie = PocasieDaoFactory.Instance.dajPocasieDao();   

    @Override
    public Pocasie vratPocasie() {
       return pocasie.vratPocasie();
    }

    @Override
    public void vyhodnotPocasie() {
        pocasie.vyhodnotPocasie();
    }

    @Override
    public void zistiPocasieData(String oznaceneMesto) throws NeuspesneZiskanieDatException {
        pocasie.zistiPocasieData(oznaceneMesto);
    }

    @Override
    public List<String> vratMozneLokacie() {
        return pocasie.vratMozneLokacie();
    }
    
}
