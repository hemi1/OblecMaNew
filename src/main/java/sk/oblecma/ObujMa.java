/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rastislav
 */
public class ObujMa {

    private Pocasie predpoved;
    private ObuvDao obuvDao;

    private List<Obuv> najdiVhodnuObuv() {
        List<Obuv> vhodne = new ArrayList<>();
        List<Obuv> vsetky = new ArrayList<>();
        vsetky = obuvDao.dajVsetkyObuvy();
        for (Obuv obuv : vsetky) {
            if ((predpoved.isChladno() || predpoved.isVelkaZima()) && obuv.isZateplene() == true && obuv.isNepremokave() == false) {
                vhodne.add(obuv);
            }
            if ((predpoved.isChladno() || predpoved.isVelkaZima()) && obuv.isZateplene() == true && obuv.isNepremokave() == true) {
                vhodne.add(obuv);
            }
            if (predpoved.isChladno() && predpoved.isDazd() && obuv.isZateplene() == true && obuv.isNepremokave() == true) {
                vhodne.add(obuv);
            }
            if (predpoved.isChladno() == false && predpoved.isDazd() && obuv.isZateplene() == false && obuv.isNepremokave() == true
                    && obuv.isVetrane()) {
                vhodne.add(obuv);
            }
            if ((predpoved.isTeplo() || predpoved.isTeplejsie() || predpoved.isTropickaHorucava()) && obuv.isZateplene() == false && obuv.isNepremokave() == false && obuv.isVetrane() == true) {
                vhodne.add(obuv);
            }
            if((predpoved.isPriemerne())&& obuv.isZateplene()==false){
            vhodne.add(obuv);}
            
        }
        return vhodne;
    }
}
