/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Slavomír
 */
public class DefaultHistoriaDao implements HistoriaDao{
    
    private HistoriaDao historia = HistoriaDaoFactory.instance.dajHistoriaDao();
    
    @Override
    public List<Kombinacia> vratCeluHistoriu() {
        return historia.vratCeluHistoriu();
    }

    public List<Kombinacia> vratHistoriuOdoDna(LocalDate datum) {
      return historia.vratHistoriuOdoDna(datum);
    }

    @Override
    public List<Kombinacia> vratKombinacieZoDna(LocalDate datum) {
        return historia.vratKombinacieZoDna(datum);
    }

    @Override
    public void vymazHistoriu() {
       historia.vymazHistoriu();
    }

    @Override
    public void nastavDlzkuHistorie(int pocetDni) {
       historia.nastavDlzkuHistorie(pocetDni);
    }

    @Override
    public void pridaj(Kombinacia kombinacia) {
        historia.pridaj(kombinacia);
    }

    @Override
    public void pridaj(List<Kombinacia> zoznamKombinacii) {
        historia.pridaj(zoznamKombinacii);
    }

    @Override
    public void odober(Kombinacia kombinacia) {
       historia.odober(kombinacia);
    }

    @Override
    public void odober(List<Kombinacia> zoznamKombinacii) {
        historia.odober(zoznamKombinacii);
    }

    @Override
    public boolean maRovnaku(Kombinacia kombinacia) {
       return historia.maRovnaku(kombinacia);
    }

    @Override
    public void nastavUzivatela(Long id) {
        historia.nastavUzivatela(id);
    }

    @Override
    public Map<Long, List<Kombinacia>> vratCeluHistoriuMapu() {
       return historia.vratCeluHistoriuMapu();
    }
    
}
