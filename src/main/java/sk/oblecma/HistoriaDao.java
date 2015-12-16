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
 * @author alfred
 */
public interface HistoriaDao {

    public List<Kombinacia> vratCeluHistoriu();

    public List<Kombinacia> vratHistoriuOdoDna(LocalDate datum);

    public List<Kombinacia> vratKombinacieZoDna(LocalDate datum);

    public void vymazHistoriu();

    public void nastavDlzkuHistorie(int pocetDni);

    public void pridaj(Kombinacia kombinacia);

    public void pridaj(List<Kombinacia> zoznamKombinacii);

    //public void pridajZId(List<Long[]> idZoznamKombinacii);

    public void odober(Kombinacia kombinacia);

    public void odober(List<Kombinacia> zoznamKombinacii);
    
    public boolean maRovnaku(Kombinacia kombinacia);
    
     public void nastavUzivatela(Long id);

    //public void odoberZId(List<Long[]> idZoznamKombinacii);

    public Map<Long, List<Kombinacia>> vratCeluHistoriuMapu();

}
