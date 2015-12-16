/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

// hotove testy

/**
 *
 * @author Slavomír
 */
class OblecMa {
    private PocasieDao pocasie;
    private OblecenieDao oblecenieDao;
    private UzivatelDao uzivatel;
    private KategoriaDao kategoriaDao;
    private Map<Long, Integer> hodnotenieOblecenia = new HashMap<Long, Integer>();
    private Pocasie mojePocasie;
    private boolean formalnaPrilezitost;
    private HistoriaDao odmietnuteKombinacie;
    private final OdporucenaZakazanaKombinaciaDao odporucenaZakazanaKombinaciaDao;
    private  Map<Oblecenie, Integer> kombinacia = new HashMap<>();
    
    private List<Long> boliVkombinacii = new ArrayList<>();
    private boolean preskocOdporucane = false;
    
    private boolean vratiloOdporucanu =false;
    private int poslednaOdporucana  = 0;
    private String oznaceneMesto;
    
    private boolean boloOhodnotene = false;
    
    private boolean[][] maAsponJedno = new boolean[7][5]; // [lokacia][vrstva]
    private boolean[][] maAsponJednoPamet = new boolean[maAsponJedno.length][maAsponJedno[0].length]; // [lokacia][vtstva]
    private Map<Integer,Integer> trebaPridelitLokacia = new HashMap<>();
    private Map<Integer,Integer> trebaPridelitVrstva = new HashMap<>();
    
    private boolean prehladavajuSaInyUzivatelia=false;
    private boolean prehladavaSaPranie=false;
    
    private void nulujMaAsponJedno()
    {
        for(int a = 0; a< maAsponJedno.length;a++)
        {
            for(int b=0;b<maAsponJedno[a].length;b++)
            {
                maAsponJedno[a][b]=maAsponJednoPamet[a][b];
            }
        }
        trebaPridelitLokacia.clear();
        trebaPridelitVrstva.clear();;
    }
    
    private void nulujMaAsponJednoPamet()
    {
        for(int a = 0; a< maAsponJedno.length;a++)
        {
            for(int b=0;b<maAsponJedno[a].length;b++)
            {
                maAsponJedno[a][b]=false;
                maAsponJednoPamet[a][b]=false;
            }
        }
        trebaPridelitLokacia.clear();
        trebaPridelitVrstva.clear();;
    }
    
    private void nastavMaAsponJedno(Oblecenie oblecenie,Long uzivatel)
    {
        Long kategoriaID = oblecenie.getKategoria();
        Kategoria kategoria = kategoriaDao.vratKategoriu(kategoriaID, uzivatel);
        int vrstva = kategoria.getVrstva();
        int lokacia = kategoria.getLokacia();
        
        maAsponJedno[lokacia][vrstva]=true;
        maAsponJednoPamet[lokacia][vrstva]=true;
        
    }
    
    private void pridelene(Oblecenie oblecenie,Long uzivatel)
    {
        Long kategoriaID = oblecenie.getKategoria();
        Kategoria kategoria = kategoriaDao.vratKategoriu(kategoriaID, uzivatel);
        int vrstva = kategoria.getVrstva();
        int lokacia = kategoria.getLokacia();
        
        maAsponJedno[lokacia][vrstva]=false;
    }
    
    private void nastavCoTrebaPridelit()
    {
        trebaPridelitLokacia.clear();
        trebaPridelitVrstva.clear();
        for(int a = 0; a< maAsponJedno.length;a++)
        {
            for(int b=0;b<maAsponJedno[a].length;b++)
            {
                if(maAsponJedno[a][b]==true)
                {
                    int nextId = trebaPridelitLokacia.size()+1;
                    trebaPridelitLokacia.put(nextId, a);
                    trebaPridelitVrstva.put(nextId, b);
                }
            }
        }
    }
    
    private boolean trebaTotoOblecenie(Oblecenie oblecenie,Long uzivatel)
    {
        Long kategoriaID = oblecenie.getKategoria();
        Kategoria kategoria = kategoriaDao.vratKategoriu(kategoriaID, uzivatel);
        int vrstva = kategoria.getVrstva();
        int lokacia = kategoria.getLokacia();
        
        return maAsponJedno[lokacia][vrstva];
    }
    
    private boolean trebaNiecoPridelit()
    {
       return !trebaPridelitLokacia.isEmpty();
    }
    public OblecMa(PocasieDao pocasie, OblecenieDao oblecenie, UzivatelDao uzivatel, KategoriaDao kategoria, OdporucenaZakazanaKombinaciaDao odporucenaKombinaciaDao) {
        this.pocasie = pocasie;
        this.oblecenieDao = oblecenie;
        this.uzivatel = uzivatel;
        this.kategoriaDao = kategoria;
        this.odmietnuteKombinacie = new PamatovaHistoriaDao();
        this.formalnaPrilezitost = false;
        this.odporucenaZakazanaKombinaciaDao = odporucenaKombinaciaDao;
    }
    private void ohodnotOblecenie(Long uzivatel) {
        
        if(boloOhodnotene)
        {
            return;
        }
        if(!prehladavajuSaInyUzivatelia)
        {
            nulujMaAsponJednoPamet();
        }
        /* aby nedal zateplene veci do tepleho pocasia */
        int hodnotenieMinimum = -100;
        for (Oblecenie aktualne : oblecenieDao.dajVsetkyOblecenia(uzivatel)) {
            /*ak je v prani */
            if(!prehladavajuSaInyUzivatelia)
            {
              nastavMaAsponJedno(aktualne, uzivatel);
            }
        /*    if (aktualne.isvPrani()) {
                
                continue;
            }
         */   Long idOblecenie = aktualne.getIdOblecenia();
            int hodnotenie = 0;
            /* zakladne hodnotenie, ktore budu mat na zaciatku vsetky */
            hodnotenieOblecenia.put(idOblecenie, hodnotenie);
            /*ak je oblecenie spinave, da mu minimum (?) */
            Kategoria kategoria = kategoriaDao.vratKategoriu(aktualne.getKategoria(),uzivatel);
            
            if (aktualne.getPocetObleceniBezPrania() >= kategoria.getMaximalnyPocetNoseniBezPrania()) {
                hodnotenieOblecenia.put(idOblecenie, hodnotenieMinimum);
            }
            System.out.println("\n hodnotenie pre " + aktualne.toString() + "\n");
            /**
             * ****************** PODLA PRILEZITOSTI
             * ***********************************
             */
            if (formalnaPrilezitost == true) {
                if (aktualne.isFormalne() == false) {
                    hodnotenie = hodnotenie - 1;
                    System.out.println("nie je formlane");
                }
            } else // nie je formalna prilezitost
            {
                if (aktualne.isFormalne() == true) {
                    hodnotenie = hodnotenie - 1;
                    System.out.println("formalne netreba");
                }
            }
            /**
             * ****************** PODLA TEPLOTY
             * ***********************************
             */
            if (mojePocasie.isZima() || mojePocasie.isVelkaZima()) // menej ako 0
            {
                if (aktualne.isZateplene() == false) {
                    hodnotenie = hodnotenie - 1;
                    System.out.println("nie je zateplene");
                }
            } else if (mojePocasie.isChladno()) // od 0 do 10
            {
                if (aktualne.isZateplene() == true) {
                    System.out.println("zateplene velmi netreba");
                    hodnotenie = hodnotenie - 1;
                }
            } else // je viac ako 10 stupnov
            {
                if (aktualne.isZateplene() == true) {
                    System.out.println("velmi teplo pre zateplene");
                    hodnotenie = hodnotenieMinimum;
                }
            }
            /**
             * ****************** PODLA ZRAZOK
             * ***********************************
             */
            if (mojePocasie.isDazd()) {
                /* odporucanie zobrat dazdnik ??? */
                if (aktualne.isNepremokave() == false) {
                    System.out.println("nie je nepremokave");
                    hodnotenie = hodnotenie - 1;
                }
            }
            /**
             * ****************** PODLA VETRA
             * ***********************************
             */
            if (mojePocasie.fuka()) // aktualne fuka od rychlosti 40  (44 - obtiazne pouzivat dazdnik)
            {
                if (aktualne.isNeprefuka() == false) {
                    System.out.println("nie je neprefukatelne");
                    hodnotenie = hodnotenie - 1;
                }
            }
            hodnotenieOblecenia.put(idOblecenie, hodnotenie);
            System.out.println(aktualne.toString() + " = " + hodnotenie);
        }
        for (Long key : hodnotenieOblecenia.keySet()) {
            System.out.println(key + " ... " + hodnotenieOblecenia.get(key));
        }
        
        boloOhodnotene=true;
    }
    public void formalnaPrilezitost() {
        formalnaPrilezitost = true;
    }
    public void neformalnaPrilezitost() {
        formalnaPrilezitost = false;
    }
    public void oznaceneMesto(String nastav)
    {
        oznaceneMesto= nastav;
    }
    
    public void aktualizujPocasie() throws NeuspesneZiskanieDatException
    {
        pocasie.zistiPocasieData(oznaceneMesto);
        pocasie.vyhodnotPocasie();
        boloOhodnotene=false;
        hodnotenieOblecenia.clear();
        poslednaOdporucana=0;
    }
    
    public List<Oblecenie> vratKombinaciu() {
        aplikujZakazaneKombinacie();
        kombinacia.clear();
        Long IDu = Uzivatel.globalUser;
        nulujMaAsponJedno();
        try {
            
            mojePocasie = pocasie.vratPocasie();
            Long ID = uzivatel.vratIdPrihlasenehoPouzivatela();
            
            if(preskocOdporucane==false)
            {
                List<Oblecenie> dajNasledujuceOdporucane = dajNasledujuceOdporucane(ID);
            
                if(dajNasledujuceOdporucane!=null)
                {
                    vratiloOdporucanu=true;
                    return dajNasledujuceOdporucane;
                }
                else
                {
                    vratiloOdporucanu=false;
                }
            }
            else
            {
                preskocOdporucane=false;
            }
            ohodnotOblecenie(ID);
            System.out.println("Ohodnotene");
            
            /* zoberie to nsjlepsie z kategorie*/
            IDu = ID;
            
            kombinacia=vyberNajlepsie(ID);
            
        } catch (NullPointerException e) {
            System.out.println("null pointer");
     //       e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(OblecMa.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        // PREDTYM AKO RETURN, OVERI CI NAHODOU NIE SU DVA KUSY NA ROVNAKU CAST, NAPR KOSELA A TRICKO
        List<Oblecenie> porovnane = OblecMa.porovnajPodobnostKategorii(kombinacia, kategoriaDao, IDu);
        
        for(Oblecenie aktualne : porovnane)
        {   
            pridelene(aktualne,IDu);
        }   
        
        nastavCoTrebaPridelit();
        System.out.println("lokacie ktore treba zaplnit "+trebaPridelitLokacia.toString());
        if(trebaNiecoPridelit())
        {
           List<Oblecenie> najdene = PrehladajPranie(IDu);
           
           for(Oblecenie najdeneOblecenie : najdene)
           {
               pridelene(najdeneOblecenie, IDu);
               porovnane.add(najdeneOblecenie);
           }
           nastavCoTrebaPridelit();
        }
        if(trebaNiecoPridelit())
        {
           List<Oblecenie> najdene = PrehladajInychUzivatelov(IDu);
           
           for(Oblecenie najdeneOblecenie : najdene)
           {
               pridelene(najdeneOblecenie, IDu);
               porovnane.add(najdeneOblecenie);
           }
           
        }
        if(trebaNiecoPridelit())
        {
           // JOptionPane.showMessageDialog(null, "Nebolo mozne pridelit vsetko oblecenie");
            System.out.println("Nenasiel som vhodne Oblecenie");
        }
        
        
        return porovnane;
    }
    
    private List<Oblecenie> PrehladajPranie(Long idU)
    {
        List<Oblecenie> vrat = new ArrayList<>();
        prehladavaSaPranie = true;        
        
        Map<Oblecenie, Integer> najlepsie = vyberNajlepsie(idU);
        
        List<Oblecenie> zoznam = porovnajPodobnostKategorii(najlepsie, kategoriaDao, idU);
        
        for(Oblecenie oblecenie : zoznam)
        {
            if(trebaTotoOblecenie(oblecenie, idU))
            {
                vrat.add(oblecenie);
            }
        }
        
        prehladavaSaPranie = false;
        
        return vrat;
    }
    
    private List<Oblecenie> PrehladajInychUzivatelov(Long aktualnyUzivatel)
    {
        prehladavajuSaInyUzivatelia=true;
        List<Oblecenie> najdene = new ArrayList<>();
        
        List<Long> uzivatelia = uzivatel.vratIdVsetkychUzivatelov();
        for(Long aktualny : uzivatelia)
        {
            if(aktualny.equals(aktualnyUzivatel))
            {
                continue;
            }
            
            // ak nesedia pohlavia
            if(uzivatel.jeMuz(aktualny)!=uzivatel.jeMuz(aktualnyUzivatel))
            {
                continue;
            }
            boloOhodnotene=false;
            ohodnotOblecenie(aktualny);
            Map<Oblecenie, Integer> najlepsie = vyberNajlepsie(aktualny);
            List<Oblecenie> porovnane = porovnajPodobnostKategorii(najlepsie, kategoriaDao, aktualny);
            List<Oblecenie> pridaj = new ArrayList<>();
            for(Oblecenie oblecenie : porovnane)
            {
                if(trebaTotoOblecenie(oblecenie,aktualny))
                {
                    pridaj.add(oblecenie);
                }
                
            }
            najdene.addAll(pridaj);
        }
        
        prehladavajuSaInyUzivatelia=false;        
        return najdene;         
    }
    
    private Map<Oblecenie,Integer> vyberNajlepsie(Long IDu)
    {
        Map<Oblecenie,Integer> kombinacia = new HashMap<>();
        for (Kategoria aktualna : kategoriaDao.vratZoznamKategorii(IDu)) {
                List<Oblecenie> podlaKategorie = oblecenieDao.dajObleceniePodlaKategorie(aktualna.getCislo(), IDu);
                if (podlaKategorie == null) {
                    continue;
                }
                Oblecenie najlepsieHodnoteneOblecenie = null;
                for (Oblecenie oznaceneOblecenie : podlaKategorie) {
                    if(oznaceneOblecenie.isvPrani())
                    {
                        if(prehladavaSaPranie==false)
                        {
                          // ak je v prani a  neprehladava sa pranie
                          continue;
                        }
                    }
                    else
                    {
                        if(prehladavaSaPranie)
                        { 
                            // ak nie je v prani a prehladava sa pranie
                            continue;
                        }
                    }
                    if(boliVkombinacii.contains(oznaceneOblecenie.getIdOblecenia()))
                    {
                        continue;
                    }
                    if(prehladavajuSaInyUzivatelia)
                    {
                        if(oznaceneOblecenie.isMozeSaPoziciavat()==false)
                        {
                            continue;
                        }
                    }
                    
        //          System.out.print(oznaceneOblecenie.getNazov() + " id = " + oznaceneOblecenie.getIdOblecenia() + " katrgoria " + oznaceneOblecenie.getKategoria());
                    if (najlepsieHodnoteneOblecenie == null) {
                        najlepsieHodnoteneOblecenie = oznaceneOblecenie;
                        continue;
                    }
         /*          System.out.println( hodnotenieOblecenia.get(oznaceneOblecenie.getIdOblecenia()));
                  System.out.println(  hodnotenieOblecenia.get(najlepsieHodnoteneOblecenie.getIdOblecenia()));
           */         
                    if (hodnotenieOblecenia.get(oznaceneOblecenie.getIdOblecenia()) > hodnotenieOblecenia.get(najlepsieHodnoteneOblecenie.getIdOblecenia())) {
                        najlepsieHodnoteneOblecenie = oznaceneOblecenie;
                    }
                    
                    if (hodnotenieOblecenia.get(oznaceneOblecenie.getIdOblecenia()) == hodnotenieOblecenia.get(najlepsieHodnoteneOblecenie.getIdOblecenia())) {
                        boolean najlepsieNove = najlepsieHodnoteneOblecenie.isNove();
                        boolean najlepsieNosene = najlepsieHodnoteneOblecenie.isNosene();
                        boolean najlepsieStare = najlepsieHodnoteneOblecenie.isStare();
                        
                        boolean oznaceneNove = oznaceneOblecenie.isNove();
                        boolean oznaceneNosene = oznaceneOblecenie.isNosene();
                        boolean oznaceneStare = oznaceneOblecenie.isStare();
                        
                        
                        if(oznaceneNove && !najlepsieNove)
                        {
                            najlepsieHodnoteneOblecenie=oznaceneOblecenie;
                        }
                        else
                        if(oznaceneNosene && najlepsieStare)
                        {
                            najlepsieHodnoteneOblecenie=oznaceneOblecenie;
                        }
                        
                        
                    }
                    
                }
                if (najlepsieHodnoteneOblecenie != null) {
                    kombinacia.put(najlepsieHodnoteneOblecenie, hodnotenieOblecenia.get(najlepsieHodnoteneOblecenie.getIdOblecenia()));
            //        System.out.println("\nnajlepsie ohodnotene v kategorii " + aktualna.getNazov() + " je  " + najlepsieHodnoteneOblecenie.getNazov());
                }
            }
        
        return kombinacia;
    }
    
    public List<Oblecenie> dajNasledujuceOdporucane(Long idU)
    {
        int pocidatdlo=0;
        mojePocasie=pocasie.vratPocasie();
         List<KombinaciaANG> vratOdporuceneKombinacie = odporucenaZakazanaKombinaciaDao.vratOdporuceneKombinacie(mojePocasie, formalnaPrilezitost,idU);
            if(vratOdporuceneKombinacie!=null && vratOdporuceneKombinacie.isEmpty()==false)
            {
                List odporucenaKombinacia = new ArrayList();
                
                for(KombinaciaANG aktualna : vratOdporuceneKombinacie)
                {
                    if(pocidatdlo<poslednaOdporucana)
                    {
                        pocidatdlo++;
                        continue;
                    }
                    for(Long idOblecenia : aktualna.getKombinacia())
                    {
                        if(idOblecenia==-1L)
                        {
                            continue;
                        }
                        Oblecenie odporuceneOblecenie = oblecenieDao.dajOblecenie(idOblecenia);
                        if(odporuceneOblecenie==null)
                        {
                            continue;
                        }
                        odporucenaKombinacia.add(odporuceneOblecenie);
                    }
                    System.out.println("Vraciam odporucanu kombinaciu");
                    poslednaOdporucana++;
                    return odporucenaKombinacia;
                }
                
            }
        //    JOptionPane.showMessageDialog(null, "nemam dalsie odporucane kombinacie");
            return null; // tu by malo ist na inu funkciu
    }
    void odporucZakazKombinaciu(ListModel model,boolean odporuc,Long idU) {
        KombinaciaANG kombinacia = new KombinaciaANG();
        int dlzka = kombinacia.getKombinacia().length;
        int a=0;
       for(a=0;a<model.getSize();a++)
       {
           if(a>=dlzka)
           {
               System.out.println("Kombinacia je dlhsia ako je pripustne");
               return;
           }
           Oblecenie aktualne = (Oblecenie) model.getElementAt(a);
           Long id = aktualne.getIdOblecenia();
           kombinacia.setKombinacia(a, id);
       }
       for(int b=a;b<dlzka;b++)
       {
           kombinacia.setKombinacia(b,-1L);
       }
        kombinacia.sort();
        
            if(odporuc)
            {
                odporucenaZakazanaKombinaciaDao.OdporucKombinaciu(kombinacia, mojePocasie, formalnaPrilezitost,idU);
            }
            else
            {
                odporucenaZakazanaKombinaciaDao.ZakazKombinaciu(kombinacia, mojePocasie, formalnaPrilezitost,idU);
 
            }
       }
    
    public static List<Oblecenie> porovnajPodobnostKategorii(Map<Oblecenie, Integer> kombinacia, KategoriaDao kategoria, Long idU) {
        System.out.println("IDEM TRIEDIT !!!!");
        System.out.println();
        List<Oblecenie> vyslednaKombinacia = new ArrayList<>();
        for (Oblecenie oznaceneOblecenie : kombinacia.keySet()) {
            vyslednaKombinacia.add(oznaceneOblecenie);
        }
        for (Oblecenie oznaceneOblecenie : kombinacia.keySet()) {
            A:
            for (Oblecenie ineOblecenie : kombinacia.keySet()) {
                if (oznaceneOblecenie.equals(ineOblecenie)) {
                    continue;
                }
                int lokaciaJeden = kategoria.vratKategoriu(oznaceneOblecenie.getKategoria(), idU).getLokacia();
                int lokaciaDva = kategoria.vratKategoriu(ineOblecenie.getKategoria(), idU).getLokacia();
                int vrstvaJeden = kategoria.vratKategoriu(oznaceneOblecenie.getKategoria(), idU).getVrstva();
                int vrstvaDva = kategoria.vratKategoriu(ineOblecenie.getKategoria(), idU).getVrstva();
              //  System.out.println("loakcie " + lokaciaJeden + " =?= " + lokaciaDva);
                if (lokaciaJeden == lokaciaDva) {
                 //   System.out.println("vrstva " + vrstvaJeden + " =?= " + vrstvaDva);
                    if (vrstvaJeden == vrstvaDva) {
                        if (kombinacia.get(oznaceneOblecenie) <= kombinacia.get(ineOblecenie)) {
                  //          System.out.println("nechavam " + ineOblecenie.getNazov() + " s kategoriou " + ineOblecenie.getKategoria() + " a ma hodnotenie " + kombinacia.get(ineOblecenie));
                //            System.out.println("vyhadzujem " + oznaceneOblecenie.getNazov() + " s kategoriou " + oznaceneOblecenie.getKategoria() + " a ma hodnotenie " + kombinacia.get(oznaceneOblecenie));
                            kombinacia.put(oznaceneOblecenie, Integer.MIN_VALUE);
                            vyslednaKombinacia.remove(oznaceneOblecenie);
              //              System.out.println("vyhadzujem " + oznaceneOblecenie.getNazov() + " s kategoriou " + oznaceneOblecenie.getKategoria() + " a ma hodnotenie " + kombinacia.get(oznaceneOblecenie));
                            break A;
                        }
                    }
                }
            }
        }
        return vyslednaKombinacia;
    }
    /*
    public List<Oblecenie> zmenOblecenie(List<Oblecenie> staraKombinacia, Oblecenie oblecenie) {
        
        /* maly vnutorny if *
        
         ohodnotOblecenie(oblecenie.getVlastnikID());
         System.out.println("Ohodnotene");
        
        
         
        this.odmietnuteKombinacie.pridaj(new Kombinacia(staraKombinacia));
        Long idUzivatela = null;
        try {
            idUzivatela = uzivatel.vratIdPrihlasenehoPouzivatela();
        } catch (Exception e) {
            System.out.println("Nepodarilo sa ziskat ID prihlaseneho uzivatela");
        }
        if (idUzivatela == null) {
            return staraKombinacia;
        }
        /*uz raz bolo vygenerovane  - nebude sa generovat pri vrateni dalsej kategorie*
        boliVkombinacii.add(oblecenie.getIdOblecenia());
        
        Map<Oblecenie, Integer> hodnoteniaBezOdmietnuteho = new HashMap<>();
        List<Oblecenie> komb = new ArrayList<>();
        komb.addAll(staraKombinacia);
        komb.remove(oblecenie);
        for (Oblecenie obl : oblecenieDao.dajVsetkyOblecenia()) {
            long id = obl.getIdOblecenia();
            if (!hodnotenieOblecenia.keySet().contains(id)) {
                continue;
            }
            komb.add(obl);
            if (this.odmietnuteKombinacie.maRovnaku(new Kombinacia(komb))) {
                komb.remove(obl);
                continue;
            }
            komb.remove(obl);
            hodnoteniaBezOdmietnuteho.put(obl, hodnotenieOblecenia.get(id));
        }
        //mame mapu Oblecenie : hodnotenie, v ktorej nie je/su tie vyhodene
        // teraz spustime znova vyberovy algoritmus ale s nasou novoou mapou
        // !!! prerobit vyber na 2 metody, samotny vyber bude jedna
        //List<Oblecenie> novaKombinacia = vyberNajlepsiuKombinaciu(hodnoteniaBezOdmietnuteho, idUzivatela);
        List<Oblecenie> novaKombinacia = porovnajPodobnostKategorii(hodnoteniaBezOdmietnuteho, kategoriaDao, idUzivatela);
        Kategoria katMeneneho = kategoriaDao.vratKategoriu(oblecenie.getKategoria(), idUzivatela);
        int lokaciaMeneneho = katMeneneho.getLokacia();
        int vrstvaMeneneho = katMeneneho.getVrstva();
        for (Oblecenie obl : novaKombinacia) {
            Kategoria kat = kategoriaDao.vratKategoriu(obl.getKategoria(), idUzivatela);
            if (kat.getLokacia() == lokaciaMeneneho && kat.getVrstva() == vrstvaMeneneho) {
                komb.add(obl);
            }
        }
        return komb;
    }  
    

    
    private List<Oblecenie> vyberNajlepsiuKombinaciu(Map<Oblecenie, Integer> hodnotenia, Long idUzivatela) {
        List<Oblecenie> najlepsiVyber = new ArrayList<>();
        for (Kategoria aktualna : kategoriaDao.vratZoznamKategorii(idUzivatela)) {
            if (aktualna == null) {
                return null;
            }
            long idKategorie = aktualna.getCislo();
            Oblecenie najlepsie = null;
            for (Oblecenie obl : oblecenieDao.dajObleceniePodlaKategorie(idKategorie, idUzivatela)) {
                if (!hodnotenia.containsKey(obl)) {
                    continue;
                }
                if (najlepsie == null) {
                    najlepsie = obl;
                    continue;
                }
                if (hodnotenia.get(najlepsie) < hodnotenia.get(obl)) {
                    najlepsie = obl;
                }
            }
            if (najlepsie != null) {
                najlepsiVyber.add(najlepsie);
            }
        }
        return najlepsiVyber;
    }
*/
    public void bolaKombinacia(ListModel model) {
      
       for(int a=0;a<model.getSize();a++)
       {

           Oblecenie aktualne = (Oblecenie) model.getElementAt(a);
           Long id = aktualne.getIdOblecenia();
           boliVkombinacii.add(id);
       }

    }
    public void vycistPouziteKombinacie()
    {
        boliVkombinacii.clear();
    }


    public List<Oblecenie> zmenJednoOblecenie(List<Oblecenie> stare,Oblecenie oznacene) {
       
        for(Oblecenie aktualne: stare )
        {
            boliVkombinacii.remove(aktualne.getIdOblecenia());
        }
       boliVkombinacii.add(oznacene.getIdOblecenia());
       System.out.print(Arrays.toString(boliVkombinacii.toArray()));
       
       preskocOdporucane=true;
        List<Oblecenie> nova = vratKombinaciu();
       
        // ak je stara kombinacia vysledok odporucanej kombinacie
        if(vratiloOdporucanu)
        {
           int miesto = stare.indexOf(oznacene);
            stare.remove(oznacene);
            
            Long kategoriaOznaceneID = oznacene.getKategoria();
            Kategoria kategoriaOznacene = kategoriaDao.vratKategoriu(kategoriaOznaceneID, oznacene.getVlastnikID());
            
            int lokaciaOznacene = kategoriaOznacene.getLokacia();
            int vrstvaOznacene = kategoriaOznacene.getVrstva();
            
           for(Oblecenie aktualne : nova)
           {
               Long kategoriaAktualneID = aktualne.getKategoria();
               if(kategoriaOznaceneID.equals(kategoriaAktualneID))
               {
                   stare.add(miesto, aktualne);
                   System.out.println("nasiel podla kategoirie "+aktualne.getNazov());
                   break;
               }
                Kategoria kategoriaAktualne = kategoriaDao.vratKategoriu(kategoriaAktualneID, aktualne.getVlastnikID());
                int lokaciaAktualne = kategoriaAktualne.getLokacia();
                int vrstvaAktualne = kategoriaAktualne.getVrstva();
                if(lokaciaAktualne==lokaciaOznacene)
                {
                    if(vrstvaAktualne==vrstvaOznacene)
                    {
                        stare.add(miesto, aktualne);
                        break;
                    }
                }
           }
           nova  = stare;
        }

        return nova;
    }

    void aplikujZakazaneKombinacie() {
        mojePocasie=pocasie.vratPocasie();
        try {
            List<KombinaciaANG> zakazaneKombinacie = odporucenaZakazanaKombinaciaDao.vratZakazaneKombinacie(mojePocasie, formalnaPrilezitost,uzivatel.vratIdPrihlasenehoPouzivatela() );
            for(KombinaciaANG kombinacia : zakazaneKombinacie)
            {
                for(Long id : kombinacia.getKombinacia())
                {
                    if(id.equals(-1L))
                    {
                        continue;
                    }
                    boliVkombinacii.add(id);
                }
            }
        } catch (Exception ex) {
           
        }
    }
    
    
}