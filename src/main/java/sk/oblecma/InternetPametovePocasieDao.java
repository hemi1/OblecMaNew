package sk.oblecma;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public class InternetPametovePocasieDao implements PocasieDao {

    private Pocasie mojePocasie;
    
    private List<String> mozneLokacie;
    private int neuspesneZiskanieDatPokus=0;
    public InternetPametovePocasieDao() {
        mojePocasie = new Pocasie();
        
        mozneLokacie = new ArrayList<>();
        
        mozneLokacie.add("kosice");
        mozneLokacie.add("bratislava");
        mozneLokacie.add("nitra");
        mozneLokacie.add("trencin");
        mozneLokacie.add("martin");
        mozneLokacie.add("zilina");
        mozneLokacie.add("trnava");
        mozneLokacie.add("poprad");
        mozneLokacie.add("zvolen");
        mozneLokacie.add("ruzomberok");
        mozneLokacie.add("presov");
        mozneLokacie.add("banska-bystrica");   
        
    }
    
    

    @Override
    public void zistiPocasieData(String oznaceneMesto) throws NeuspesneZiskanieDatException{
     URL url;
    InputStream is = null;
    BufferedReader br;
    String line;

        PrintWriter pw=null;
        try {
            pw = new PrintWriter(new File("pocasie.txt"));
        } catch (FileNotFoundException ex) {
           System.err.println("\n Zistovanie Pocasia >>> nemozem vytvarat zapis \n");
        }
    
        boolean pis=false;
        String zaciatokPredpovede = "<ul class='daily-forecast '>";
        String koniecPredpovede = "</ul>";
        
        String teplota = "class='temp'";
        String teplotaDen = "class='day'";
        String tepltoaNoc = "class='night'";
        
        String ZhrnuteInfo = " <li class='additional-info'>";
        
        boolean citajZhrnuteInfo=false;
        boolean dnesnyDen=false;
        
    try {
        url = new URL("http://www.pocasie.sk/slovensko/"+oznaceneMesto+"/");
        is = url.openStream();  // throws an IOException
        br = new BufferedReader(new InputStreamReader(is));

        while ((line = br.readLine()) != null) {
            
            if(!pis)
            {
                if(line.contains(zaciatokPredpovede))
                {
                    pis=true;
                   pw.write("ZACIATOK\n");
                }else
                {
                   pw.write("nie : "+line+"\n");
                }
            }
            else
            {
                if(line.contains(koniecPredpovede))
                {
                    pis=false;
                    dnesnyDen=false;
                    pw.write("KONIEC\n");
                }
            }
            if(pis)
            {
                
                if(line.contains("Dnes"))
                {
                    pw.write("TU\n");
                    dnesnyDen=true;
                }
                if(dnesnyDen)
                {
                    if(line.contains(teplotaDen))
                    {
                        int a=ziskajTeplotu(line,teplotaDen);
                        pw.write("\n CEZ DEN "+a+" stupnov");
                        mojePocasie.setTeplotaCezDen(a);
                    }
                    if(line.contains(ZhrnuteInfo))
                    {
                        citajZhrnuteInfo=true;
                    }
                    if(citajZhrnuteInfo)
                    {
                       if(line.contains("km/h"))
                       {
                           int a=zistiCislo(line,"nbsp; "," km/h");
                           pw.write("\n VIETOR "+a+"\n");
                           mojePocasie.setRychlostVetra(a);
                       }
                       if(line.contains("%"))
                       {
                           int a=zistiCislo(line,"nbsp;","%");
                           pw.write("\n ZRAZKY "+a+"\n");
                           mojePocasie.setPravdepodobnostZrazok(a);
                       }
                    }
                }
                pw.write(line+"\n");
            }
            else
            {
                System.out.println(line);
            }
        }
    } catch (MalformedURLException mue) {
          throw new NeuspesneZiskanieDatException();
    } catch (IOException ioe) {
          throw new NeuspesneZiskanieDatException();
    }   catch (NepodariloSaZiskatDataException ex) {
           neuspesneZiskanieDatPokus++;
           if(neuspesneZiskanieDatPokus>2)
           {
               throw new NeuspesneZiskanieDatException();
           }
           zistiPocasieData(oznaceneMesto);
           return;
        } finally {
        try {
            if (is != null) is.close();
            if(pw != null) pw.close();
        } catch (IOException ioe) {
            // nothing to see here
        }
    }    
        System.out.println("Pocasie");
        System.out.println("Teplota "+mojePocasie.getTeplotaCezDen()+" stupnov Celzia");
        System.out.println("Vietor "+mojePocasie.getRychlostVetra()+" km/h");
        System.out.println("Pravdepodobnost zrazok "+mojePocasie.getPravdepodobnostZrazok()+"%");
       neuspesneZiskanieDatPokus=0;
    }

    private int ziskajTeplotu(String line, String teplotaDen) throws NepodariloSaZiskatDataException {
        StringBuilder nove = new StringBuilder();
        int cislo = 0;
        boolean start=false;
        
        for(int a=0;a<line.length();a++)
        {
            if(!start)
            {
                if(line.charAt(a)=='>')
                {
                    start=true;
                    continue;
                }
            }
            
            if(start)
            {
                if(line.charAt(a+2)=='C')
                {
                    start=false;
                     System.out.println("\n*******************************\n"+nove.toString()+"\n*********************************\n");
                     try{
                     cislo=Integer.parseInt(nove.toString());
                     }catch(NumberFormatException e)
                     {
                         System.out.println("nepodarilo sa ziskat teplotu");
                         cislo=-300;
                         throw new NepodariloSaZiskatDataException();
                     }
                           
                    break;
                }
            }
            
            if(start)
            {
                nove.append(line.charAt(a));
            }
            
        }
        
            return cislo;
    }

    private int zistiCislo(String line, String prvy,String posledny) throws NepodariloSaZiskatDataException {
        String nove;
        int cislo = 0;
        boolean start=false;
        String[] st =line.split(prvy);
        int n = st.length-1;
   //     System.out.println("st[1]"+st[n]);
        String[] sb = st[n].split(posledny);
       
        try{
        cislo=Integer.parseInt(sb[0]);
        }
        catch(NumberFormatException e)
        {
            System.out.println("nepodarilo sa ziskat cislo");
             cislo=-300;
             throw new NepodariloSaZiskatDataException();
        }
                
        return cislo;
    }

    @Override
    public Pocasie vratPocasie() {
      return mojePocasie;
    }
    
    private boolean jeVrozmedzi(int cislo, int dolnaHranica, int hornaHranica)
    {
 
        if(cislo<dolnaHranica || cislo>hornaHranica || cislo==dolnaHranica)
        {
            return false;
        }
        
        return true;
    }
    
    private void resetVyhodnoteniePocasie()
    {
     mojePocasie.setChladno(false);
     mojePocasie.setHoruco(false);
     mojePocasie.setPriemerne(false);
     mojePocasie.setSneh(false);
     mojePocasie.setTeplejsie(false);
     mojePocasie.setTeplo(false);
     mojePocasie.setTropickaHorucava(false);
     mojePocasie.setVelkaZima(false);
     mojePocasie.setZima(false);
     mojePocasie.setDazd(false);
     
     mojePocasie.setSlabyVietor(false);
     mojePocasie.setSilnyVietor(false);
     mojePocasie.setVelmiSilnyVietor(false);
     mojePocasie.setBurlivyVietor(false);
     
    }
    
    @Override
    public void vyhodnotPocasie() {
       
        resetVyhodnoteniePocasie();
        boolean zima = false;
        int teplotaCezDen = mojePocasie.getTeplotaCezDen();
        int rychlostVetra = mojePocasie.getRychlostVetra();
        if(teplotaCezDen<0)
        {
            zima=true;
        }
        if(teplotaCezDen < -9)
        {
            mojePocasie.setVelkaZima(true);
        }
        if(jeVrozmedzi(teplotaCezDen, -10, 0))
        {
            mojePocasie.setZima(true);
        }
        if(jeVrozmedzi(teplotaCezDen, 0, 10))
        {
            mojePocasie.setChladno(true);
        }
        if(jeVrozmedzi(teplotaCezDen, 10, 15))
        {
            mojePocasie.setPriemerne(true);
        }
        if(jeVrozmedzi(teplotaCezDen, 15, 20))
        {
            mojePocasie.setTeplejsie(true);
        }
        if(jeVrozmedzi(teplotaCezDen, 20, 28))
        {
            mojePocasie.setTeplo(true);
        }
        if(jeVrozmedzi(teplotaCezDen, 28, 33))
        {
            mojePocasie.setHoruco(true);
        }
        if(teplotaCezDen>33)
        {
            mojePocasie.setTropickaHorucava(true);
        }
        if(mojePocasie.getPravdepodobnostZrazok()>80)
        {
            if(zima)
            {
                mojePocasie.setSneh(true);
            }
            else
            {
                mojePocasie.setDazd(true);
            }

        }
        if(jeVrozmedzi(rychlostVetra, 34, 40))
        {
            // hybu sa listnate kry
            mojePocasie.setSlabyVietor(true);
        }
        if(jeVrozmedzi(rychlostVetra, 40, 53))
        {
            // obtiazne pouzivanie dazdnika
            mojePocasie.setSilnyVietor(true);
        }
        if(jeVrozmedzi(rychlostVetra, 53, 60))
        {
            // pohybyje stromami
            mojePocasie.setVelmiSilnyVietor(true);
        }
        if(rychlostVetra>60)
        {
            // ?? radsej ostat doma ??
            mojePocasie.setBurlivyVietor(true);
        }
        

    }

    @Override
    public List<String> vratMozneLokacie() {
        List<String> vrat = new ArrayList<>();
      for(String text :  mozneLokacie)
      {
          String copy = text;
          vrat.add(copy);
      }
        
        return vrat;
    }
    
}
