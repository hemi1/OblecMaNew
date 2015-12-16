/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotove testy
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alfred
 */
public class PamatovaHistoriaDao implements HistoriaDao {

    private int dlzkaHistorie;
    private Long idUzivatela;
    private Map<Long,List<Kombinacia>> kombinacie;
    //private List<Long[]> kombinacie;

    public PamatovaHistoriaDao() {
        this.kombinacie = new HashMap<>();
    }
    

    public PamatovaHistoriaDao(HistoriaDao predoslaHistoria) {
        this.kombinacie = predoslaHistoria.vratCeluHistoriuMapu();

    }

    @Override
    public List<Kombinacia> vratCeluHistoriu() {
        List<Kombinacia> historia = new ArrayList<>();
        
        if(!kombinacie.containsKey(idUzivatela))
        {
            return historia;
        }
        for (Kombinacia komb : kombinacie.get(idUzivatela)) {
            historia.add(komb);
        }
        return historia;
    }

    @Override
    public List<Kombinacia> vratHistoriuOdoDna(LocalDate datum) {
        if (datum == null) {
            return null;
        }
        List<Kombinacia> vysledok = new ArrayList<>();
        for (Kombinacia komb : kombinacie.get(idUzivatela)) {
            if (datum.isBefore(komb.getDatum())) {
                vysledok.add(komb);
            }
        }

        vysledok.addAll(vratKombinacieZoDna(datum));
        return vysledok;
    }

    @Override
    public List<Kombinacia> vratKombinacieZoDna(LocalDate datum) {
        List<Kombinacia> vysledok = new ArrayList<>();
        if(kombinacie.get(idUzivatela)==null)
        {
            return vysledok;
        }
        for (Kombinacia komb : kombinacie.get(idUzivatela)) {
            if (komb.getDatum().getYear() == datum.getYear() 
                    && komb.getDatum().getMonth() == datum.getMonth()
                    && komb.getDatum().getDayOfMonth()== datum.getDayOfMonth()) {
                vysledok.add(komb);
            }
        }
        return vysledok;
    }

    @Override
    public void vymazHistoriu() {
        this.kombinacie.put(idUzivatela, new ArrayList<>());
    }

    @Override
    public void nastavDlzkuHistorie(int pocetDni) {
        this.dlzkaHistorie = pocetDni;
    }

    @Override
    public void pridaj(Kombinacia kombinacia) {
        if (kombinacia == null) {
            return;
        }
        if(kombinacie.containsKey(idUzivatela))
        {
        this.kombinacie.get(idUzivatela).add(kombinacia);
        }
        else
        {
            List<Kombinacia> novaKombinacia = new ArrayList<>();
            novaKombinacia.add(kombinacia);
            
            kombinacie.put(idUzivatela, novaKombinacia);
        }
    }

    @Override
    public void pridaj(List<Kombinacia> zoznamKombinacii) {
        for (Kombinacia komb : zoznamKombinacii) {
            pridaj(komb);
        }
    }

    @Override
    public void odober(Kombinacia kombinacia) {
        if (kombinacia == null) {
            return;
        }
        this.kombinacie.get(idUzivatela).remove(kombinacia);
    }

    @Override
    public void odober(List<Kombinacia> zoznamKombinacii) {
        for (Kombinacia komb : zoznamKombinacii) {
            odober(komb);
        }
    }

    @Override
    public boolean maRovnaku(Kombinacia kombinacia) {
        Long[] idKombinacie = kombinacia.vratZoznamId();

        for (Kombinacia vZoznameKomb : kombinacie.get(idUzivatela)) {
            Long[] idAktualnej = vZoznameKomb.vratZoznamId();
            if (suRovnake(idKombinacie, idAktualnej)) {
                return true;
            }
        }
        return false;
    }

    private boolean suRovnake(Long[] idPrva, Long[] idDruha) {
        if (idPrva.length != idDruha.length) {
            return false;
        }

        long[] prva = new long[idPrva.length];
        long[] druha = new long[idDruha.length];

        for (int i = 0; i < idPrva.length; i++) {
            if (idPrva[i] == null) {
                prva[i] = 0;
            } else {
                prva[i] = idPrva[i];
            }

            if (idDruha[i] == null) {
                druha[i] = 0;
            } else {
                druha[i] = idDruha[i];
            }
        }

        Arrays.sort(prva);
        Arrays.sort(druha);

        for (int i = 0; i < prva.length; i++) {
            if (prva[i] != druha[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void nastavUzivatela(Long id) {
        idUzivatela = id;
    }

    @Override
    public Map<Long, List<Kombinacia>> vratCeluHistoriuMapu() {
        Map<Long, List<Kombinacia>> historia = new HashMap<>();
        
        for(Long idUzivatela : kombinacie.keySet())
        {
            List<Kombinacia> nova = new ArrayList();
            for(Kombinacia komb : kombinacie.get(idUzivatela))
            {
                nova.add(komb);
            }
            historia.put(idUzivatela,nova);
            
        }
        
        return historia;
    }

}
