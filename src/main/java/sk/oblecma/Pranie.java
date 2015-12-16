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
 * @author alfred
 */
public class Pranie {

    private Long IDuzivatela=Uzivatel.globalUser;
    private List<Oblecenie> zoznamVeciVPrani = new ArrayList<>();
    private List<Oblecenie> coTrebaDatVyprat = new ArrayList<>();
    
    
    public Pranie(OblecenieDao zoznamOblecenia, Long IDuzivatela,KategoriaDao kategoriaDao) {
        this.IDuzivatela = IDuzivatela;
        List<Oblecenie> vsetkyOblcenia = zoznamOblecenia.dajVsetkyOblecenia(IDuzivatela);
        for (Oblecenie oblecenie : vsetkyOblcenia) {
            if (oblecenie.isvPrani()) {
                this.zoznamVeciVPrani.add(oblecenie);
                continue;
            }
            Kategoria kategoria = kategoriaDao.vratKategoriu(oblecenie.getKategoria(), IDuzivatela);
            System.out.println(oblecenie.getIdOblecenia()+".... bez prania ... "+oblecenie.getPocetObleceniBezPrania());
            if (oblecenie.getPocetObleceniBezPrania() >= kategoria.getMaximalnyPocetNoseniBezPrania()) {
                this.coTrebaDatVyprat.add(oblecenie);
                continue;
            }
        }
    }

    public void pridajDoPrania(Oblecenie oblecenie) {
        if (oblecenie == null) {
            return;
        }
        if (!IDuzivatela.equals(oblecenie.getVlastnikID())) {
            return;
        }
        
        this.zoznamVeciVPrani.add(oblecenie);
    }

    public void odoberZPrania(Oblecenie oblecenie) {
        if (oblecenie == null) {
            return;
        }
        this.zoznamVeciVPrani.remove(oblecenie);
    }

    public List<Oblecenie> getZoznamVeciVPrani() {
        return zoznamVeciVPrani;
    }

    public List<Oblecenie> getCoTrebaDatVyprat() {
        return coTrebaDatVyprat;
    }

    public static List<Oblecenie> dajVeciMimoPrania(OblecenieDao zoznamOblecenia, Long IDuzivatela, Pranie pranie) {
        List<Oblecenie> veciVPrani = pranie.getZoznamVeciVPrani();
        List<Oblecenie> veciMimoPrania = new ArrayList<>();

        for (Oblecenie oblecenie : zoznamOblecenia.dajVsetkyOblecenia(IDuzivatela)) {
            if (pranie.jevPrani(oblecenie)) {
                continue;
            }
            // inak pridaj do veci mimo prania
            veciMimoPrania.add(oblecenie);
        }

        return veciMimoPrania;

    }

    private boolean jevPrani(Oblecenie oblecenie) {
        if (oblecenie == null) {
            return false;
        }
        for (Oblecenie aktualne : this.zoznamVeciVPrani) {
            if (aktualne.getIdOblecenia().equals(oblecenie.getIdOblecenia())) {
                return true;
            }
        }
        return false;
    }
}
