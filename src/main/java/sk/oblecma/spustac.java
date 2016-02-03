package sk.oblecma;

import com.google.common.collect.HashBiMap;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.PopupWindow;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import sk.oblecma.pokus.pokusForm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slavomír
 */
public class spustac extends javax.swing.JFrame {

    /**
     * Creates new form spustac
     */
    public static boolean NapojeneNaDatabazu = true;

    private final DatabazaSpojenie databaza = new DatabazaSpojenie(false);

    private final PocasieDao pocasie = new DefaultPocasieDao();
    private final ObrazokDao zoznamobrazkov = new DefaultObrazokDao();
    private final List<Icon> icons = new ArrayList<>();

    private final OblecenieDao oblecenie = new DefaultOblecenieDao();
    private final UzivatelDao uzivatel = new DefaultUzivatelDao();
    private ObuvDao obuvDao = ObuvDaoFactory.INSTANCE.dajObuvDao();
   
    private List<Obuv> obuvy = new ArrayList<>();
    
    private final KategoriaDao kategoria = new DefaultKategoriaDao();

    private List<JRadioButton> spravcaKategorieAvailableButtons = new ArrayList();

    private final OdporucenaZakazanaKombinaciaDao odporucaneZakazaneKombinacie = new DefaultOdporucaneZakazaneKombinacieDao();

    private final OblecMa oblecMa = new OblecMa(pocasie, oblecenie, uzivatel, kategoria, odporucaneZakazaneKombinacie);

    private HistoriaDao historia = new DefaultHistoriaDao();

    public static Long aktualneID = Uzivatel.globalUser;
    private boolean pridavanieKategorie;
    private Kategoria upravovanaKategoria;
    private Pranie pranie;

    int obrazokVyska = 250;
    int obrazokSirka = 250;

    private boolean prvePrihlasenie = true;
    private boolean ukoncenePrihlasovanie = false;
    private boolean odhlasovanie = false;

    private final GridBagConstraints gbc;

    private boolean graphic = true;
    private Map<JButton, ViewOblecenie> oblecenieOdstranButttons = new HashMap();

    public void vykresli(Map<Oblecenie, ImageIcon> zoznamOblecenia) {
        //int pocetStlpcov = 3;
        //int pocetRiadkov = zoznamOblecenia.keySet().size() / 3 + 1;

        int stlpec = 0;
        int riadok = 0;

        for (Component aktualny : OblecenieGraphicViewPanel.getComponents()) {
            aktualny.setVisible(false);
        }
        oblecenieOdstranButttons.clear();
        for (Oblecenie aktualne : zoznamOblecenia.keySet()) {

            gbc.gridx = stlpec;
            gbc.gridy = riadok;
            //  gbc.anchor=NORTH;
            ViewOblecenie viewOblecenie = new ViewOblecenie(aktualne, zoznamOblecenia.get(aktualne));

            OblecenieGraphicViewPanel.add(viewOblecenie.getViewPanel(), gbc);

            JButton odstranovacie = viewOblecenie.odstranovacie();
            OblecenieOdstranButtonListener listener = new OblecenieOdstranButtonListener();
            listener.set(this, aktualne, oblecenie);
            odstranovacie.addActionListener(listener);

            JComboBox statusComboBox = viewOblecenie.returnStatusComboBox();
            OblecenieStatusComboListener listenerStatus = new OblecenieStatusComboListener();
            listenerStatus.set(statusComboBox, this, aktualne, oblecenie);
            statusComboBox.addActionListener(listenerStatus);

            JCheckBox poziciavanieCheckBox = viewOblecenie.returnPoziciavanieCheckBox();
            ObleceniePoziciavanieCheckListener listenerPozicaj = new ObleceniePoziciavanieCheckListener();
            listenerPozicaj.set(poziciavanieCheckBox, this, aktualne, oblecenie);
            poziciavanieCheckBox.addActionListener(listenerPozicaj);

            stlpec++;

            if (stlpec > 3) {
                stlpec = 0;
                riadok++;
            }
        }
    }

    public ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {

        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);

    }
    private Map<Oblecenie, ImageIcon> skusobnyZoznam = new HashMap<>();

    public spustac() {

        initComponents();

        OblecenieGraphicViewPanel.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.insets = new Insets(5, 5, 5, 5);

        gbc.ipadx = 1;
        gbc.ipady = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        OblecenieListViewPanel.setVisible(!graphic);
        OblecenieGraphicScrollPane.setVisible(graphic);
        OblecenieGraphicViewPanel.setVisible(graphic);
        //    OblecenieGraphicScrollPane.setSize(0,0);
        OblecenieGraphicScrollPane.setSize(OblecenieFrame.getMaximumSize());

        if (graphic == false) {
            /*      OblecenieGraphicScrollPane.setEnabled(false);
            OblecenieListViewPanel.setLocation(0,0);
             */        }

        List<Obrazok> ob = zoznamobrazkov.zistiData();

        Calendar kalendar = new GregorianCalendar();

        List<LocalDate> datum = new ArrayList();

        int dlzkaHistorie = 7;

        historia.nastavDlzkuHistorie(dlzkaHistorie);
        for (int ax = 0; ax < dlzkaHistorie; ax++) {
            LocalDate pracujem = LocalDate.now();
            //  Date pracujem = new Date();
            //  System.out.println(pracujem.getYear()+1900);
            int den = pracujem.getDayOfMonth();
            int a = ax;
            System.out.print(pracujem.toString() + ">>");
            LocalDate pomoc = pracujem.minusDays(a);
            pracujem = pomoc;
            System.out.println(pracujem.toString());
            datum.add(pracujem);
            historiaDatumComboBox.addItem(pracujem);
        }

        ObrazokVybranehoObleceniaLabel.setSize(obrazokSirka, obrazokVyska);
        ObrazokVybranehoObleceniaLabel.setVisible(true);
        for (Obrazok aktualny : ob) {
            System.out.println("path is >>" + aktualny.getSubor());
            String nazov = "";
            if (aktualny.getSubor().startsWith("file://")) {
                nazov = aktualny.getSubor().split("file:/")[1];
            } else {
                nazov = aktualny.getSubor().split("file:")[1];
            }
            pridanieIcon(nazov, aktualny.getId().intValue(), obrazokSirka, obrazokVyska);
            //     jLabel1.setIcon(icon);
        }
        List<String> vratMozneLokacie = pocasie.vratMozneLokacie();
        for (String lokacia : vratMozneLokacie) {
            lokaciaPreZistovaniePocasia.addItem(lokacia);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OblecenieFrame = new javax.swing.JFrame();
        OblecenieViewPanel = new javax.swing.JPanel();
        zobrazOblecenieKategoriaCombo = new javax.swing.JComboBox();
        oblecenieFormPridajNoveOblecenieButton = new javax.swing.JButton();
        spravcaKategoriiButton = new javax.swing.JButton();
        spravcaPraniaButton = new javax.swing.JButton();
        OblecenieListViewPanel = new javax.swing.JPanel();
        OblecenieList = new javax.swing.JScrollPane();
        oblecenieList = new javax.swing.JList();
        odoberOblecenieButton = new javax.swing.JButton();
        ObrazokVybranehoObleceniaLabel = new javax.swing.JLabel();
        nazovObleceniaLabel = new javax.swing.JLabel();
        kategoriaLabel = new javax.swing.JLabel();
        novostLabel = new javax.swing.JLabel();
        nepremokneLabel = new javax.swing.JLabel();
        neprefukaLabel = new javax.swing.JLabel();
        zatepleneLabel = new javax.swing.JLabel();
        formalnostLabel = new javax.swing.JLabel();
        vPraniLabel = new javax.swing.JLabel();
        MozeSaPoziciavatButton = new javax.swing.JToggleButton();
        obleceniePresunNoseneStareButton = new javax.swing.JButton();
        OblecenieGraphicScrollPane = new javax.swing.JScrollPane();
        OblecenieGraphicViewPanel = new javax.swing.JPanel();
        HlavneMenuFrame = new javax.swing.JFrame();
        odhlasButton = new javax.swing.JButton();
        hlavneMenuOblecenieButton = new javax.swing.JButton();
        oblecMaButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        oblecMaVysledokList = new javax.swing.JList();
        obleciemSiToButton = new javax.swing.JButton();
        zmenOznaceneButton = new javax.swing.JButton();
        OdporucKombinaciuButton = new javax.swing.JButton();
        lokaciaPreZistovaniePocasia = new javax.swing.JComboBox();
        DalsiaKombinaciaButton = new javax.swing.JButton();
        ZakazKombinaciuButton = new javax.swing.JButton();
        stupneLabel = new javax.swing.JLabel();
        stavPocasiaLabel = new javax.swing.JLabel();
        vietorLable = new javax.swing.JLabel();
        kombinacieButton = new javax.swing.JButton();
        historiaButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        obujMaButton2 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        obujMaList = new javax.swing.JList<>();
        VyberObrazkaChooserFrame = new javax.swing.JFrame();
        VyberObrazkaPreOblecenieChooser = new javax.swing.JFileChooser();
        registraciaFrame = new javax.swing.JFrame();
        registraciaMenoTextField = new javax.swing.JTextField();
        registraciaHesloTextField = new javax.swing.JTextField();
        reistraciaButton = new javax.swing.JButton();
        registraciaReporterLabel = new javax.swing.JLabel();
        registraciaMuzRadioButton = new javax.swing.JRadioButton();
        registraciaZenaRadioButton = new javax.swing.JRadioButton();
        SpravcaKategoriiForm = new javax.swing.JFrame();
        vymazKategoriuButton = new javax.swing.JButton();
        upravitKategoriuButton = new javax.swing.JButton();
        pridatKategoriuButton = new javax.swing.JButton();
        VyberKategoriuDialogForm = new javax.swing.JDialog();
        kategorieButtonGroup = new javax.swing.ButtonGroup();
        PridatKategoriuDialog = new javax.swing.JDialog();
        okPridatKategoriuButton = new javax.swing.JButton();
        nazovPridatKategoriuLabel = new javax.swing.JLabel();
        lokaciaPridatKategoriuLabel = new javax.swing.JLabel();
        vrstvaPridatKategoriuLabel = new javax.swing.JLabel();
        nazovPridatKategoriuTextField = new javax.swing.JTextField();
        lokalizaciaPridatKategoriuComboBox = new javax.swing.JComboBox();
        zrusitPridatKategoriuButton = new javax.swing.JButton();
        vrstvaPridatKategoriuComboBox = new javax.swing.JComboBox();
        pouzitiaKategoriaLabel = new javax.swing.JLabel();
        pouzitiaKategoriaTextField = new javax.swing.JTextField();
        pouzitiaKategoriaLabel2 = new javax.swing.JLabel();
        registraciaPohlavieButtonGroup = new javax.swing.ButtonGroup();
        oblecenieNemaKategoriuForm = new javax.swing.JDialog();
        jScrollPane2 = new javax.swing.JScrollPane();
        oblecenieBezKategorieList = new javax.swing.JList();
        zmazanaKategoriaLabel = new javax.swing.JLabel();
        zmenKategoriuButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        novaKategoriaComboBox = new javax.swing.JComboBox();
        upravKategoriuDialog = new javax.swing.JDialog();
        nazovKategorieLabel = new javax.swing.JLabel();
        lokalizaciaKategorieLabel = new javax.swing.JLabel();
        vrstvaKategorieLabel = new javax.swing.JLabel();
        nazovKategorieTextField = new javax.swing.JTextField();
        lokalizaciaKategorieCombo = new javax.swing.JComboBox();
        vrstvaKategorieCombo = new javax.swing.JComboBox();
        okUpravitButtno = new javax.swing.JButton();
        spravcaPraniaForm = new javax.swing.JFrame();
        pranieLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obleceniePranieList = new javax.swing.JList();
        pranieComboBox = new javax.swing.JComboBox();
        odobratZPraniaButton = new javax.swing.JButton();
        pridatDoPraniaButton = new javax.swing.JButton();
        NoveOblecenieFrame = new javax.swing.JFrame();
        noveOblecenieNepremokaveCheckBox = new javax.swing.JCheckBox();
        noveOblecenieKategoriaCombo = new javax.swing.JComboBox();
        noveOblecenieMenoTextField = new javax.swing.JTextField();
        noveOblecenieNeprefukaCheckBox = new javax.swing.JCheckBox();
        noveOblecenieZatepleneCheckBox = new javax.swing.JCheckBox();
        noveOblecenieFormalneCheckBox = new javax.swing.JCheckBox();
        vyberObrazkaComboBox = new javax.swing.JComboBox();
        pridajNoveOblecenieButton = new javax.swing.JButton();
        noveOblecenieObrazokLabel = new javax.swing.JLabel();
        nacitajObrazok = new javax.swing.JButton();
        noveOblecenieBezObrazkaRadioButton = new javax.swing.JRadioButton();
        noveObleceniePoziciavanieCheckBox = new javax.swing.JCheckBox();
        kombinacieFrame = new javax.swing.JFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        odporuceneKombinacieList = new javax.swing.JList();
        odporuceneKombinacieLable = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        zakazaneKombinacieList = new javax.swing.JList();
        ZakazaneKombinacieLabel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        InformacieOznacenejKombinacieList = new javax.swing.JList();
        odporucaneNaZakazaneButton = new javax.swing.JButton();
        ZakazaneNaOdporucaneButton = new javax.swing.JButton();
        odoberZoZoznamuOdporucaneZakazaneButton = new javax.swing.JButton();
        stupnePreKombinaciuLabel = new javax.swing.JLabel();
        stavPocasiaPreKombinaciuLabel = new javax.swing.JLabel();
        vietorPreKombinaciuLabel = new javax.swing.JLabel();
        HistoriaFrame = new javax.swing.JFrame();
        historiaDatumComboBox = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        historiaObleceneKombinacieList = new javax.swing.JList();
        jScrollPane8 = new javax.swing.JScrollPane();
        historiaOblecenieKombinacieList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        ObuvFrame = new javax.swing.JFrame();
        pridajObuvButton = new javax.swing.JButton();
        odstranObuvButton = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        obuvList = new javax.swing.JList<>();
        obuvLableZateplene = new javax.swing.JLabel();
        obuvLableNepremokave = new javax.swing.JLabel();
        vetraneLable = new javax.swing.JLabel();
        pridajObuvFrame = new javax.swing.JFrame();
        nazovObuvyText = new javax.swing.JTextField();
        obuvNepremokaveCheckBox1 = new javax.swing.JCheckBox();
        obuvZatepleneCheckBox2 = new javax.swing.JCheckBox();
        vetraneCheckBox3 = new javax.swing.JCheckBox();
        pridajObuvButtonObuvFrame = new javax.swing.JButton();
        zrusitObuvFrameButton = new javax.swing.JButton();
        prihlasmenofield = new javax.swing.JTextField();
        prihlasheslofield = new javax.swing.JTextField();
        prihlasovanieReportLabel = new javax.swing.JLabel();
        prihlasButton = new javax.swing.JButton();
        registrujOdkazButton = new javax.swing.JButton();

        OblecenieFrame.setTitle("Oblecenie");

        zobrazOblecenieKategoriaCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                zobrazOblecenieKategoriaComboItemStateChanged(evt);
            }
        });
        zobrazOblecenieKategoriaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zobrazOblecenieKategoriaComboActionPerformed(evt);
            }
        });

        oblecenieFormPridajNoveOblecenieButton.setText("Nove Oblecenie");
        oblecenieFormPridajNoveOblecenieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oblecenieFormPridajNoveOblecenieButtonActionPerformed(evt);
            }
        });

        spravcaKategoriiButton.setText("Správca kategórií");
        spravcaKategoriiButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spravcaKategoriiButtonActionPerformed(evt);
            }
        });

        spravcaPraniaButton.setText("SprávcaPrania");
        spravcaPraniaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spravcaPraniaButtonActionPerformed(evt);
            }
        });

        OblecenieList.setAutoscrolls(true);
        OblecenieList.setMinimumSize(new java.awt.Dimension(250, 100));
        OblecenieList.setPreferredSize(new java.awt.Dimension(250, 100));

        oblecenieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        oblecenieList.setToolTipText("");
        oblecenieList.setMaximumSize(new java.awt.Dimension(250, 100));
        oblecenieList.setMinimumSize(new java.awt.Dimension(250, 100));
        oblecenieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                oblecenieListValueChanged(evt);
            }
        });
        OblecenieList.setViewportView(oblecenieList);

        odoberOblecenieButton.setText("Odober oblečenie");
        odoberOblecenieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odoberOblecenieButtonActionPerformed(evt);
            }
        });

        ObrazokVybranehoObleceniaLabel.setText(" ");

        nazovObleceniaLabel.setText("Nazov oblecenia");

        kategoriaLabel.setText("(Kategoria)");

        novostLabel.setText("nove/nosene/stare");

        nepremokneLabel.setText("nepremokne");

        neprefukaLabel.setText("neprefuka");

        zatepleneLabel.setText("zateplene");

        formalnostLabel.setText("formalne/neformalne");

        vPraniLabel.setText("perie sa  / neperie sa");

        MozeSaPoziciavatButton.setText("Moze sa poziciavat");
        MozeSaPoziciavatButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MozeSaPoziciavatButtonMouseClicked(evt);
            }
        });
        MozeSaPoziciavatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MozeSaPoziciavatButtonActionPerformed(evt);
            }
        });

        obleceniePresunNoseneStareButton.setText("nosene");
        obleceniePresunNoseneStareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obleceniePresunNoseneStareButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OblecenieListViewPanelLayout = new javax.swing.GroupLayout(OblecenieListViewPanel);
        OblecenieListViewPanel.setLayout(OblecenieListViewPanelLayout);
        OblecenieListViewPanelLayout.setHorizontalGroup(
            OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(ObrazokVybranehoObleceniaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                        .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nazovObleceniaLabel)
                                    .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(kategoriaLabel))))
                            .addComponent(formalnostLabel))
                        .addGap(101, 101, 101))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, OblecenieListViewPanelLayout.createSequentialGroup()
                        .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(vPraniLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nepremokneLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(neprefukaLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, OblecenieListViewPanelLayout.createSequentialGroup()
                                .addComponent(novostLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(obleceniePresunNoseneStareButton))
                            .addComponent(zatepleneLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MozeSaPoziciavatButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OblecenieList, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
                    .addComponent(odoberOblecenieButton, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE))
                .addContainerGap())
        );
        OblecenieListViewPanelLayout.setVerticalGroup(
            OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OblecenieList, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(odoberOblecenieButton)
                .addGap(18, 18, 18)
                .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(OblecenieListViewPanelLayout.createSequentialGroup()
                        .addComponent(nazovObleceniaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kategoriaLabel)
                        .addGap(27, 27, 27)
                        .addComponent(formalnostLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(OblecenieListViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(novostLabel)
                            .addComponent(obleceniePresunNoseneStareButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vPraniLabel)
                        .addGap(24, 24, 24)
                        .addComponent(MozeSaPoziciavatButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(neprefukaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nepremokneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zatepleneLabel)
                        .addGap(38, 38, 38))
                    .addComponent(ObrazokVybranehoObleceniaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(100, 100, 100))
        );

        javax.swing.GroupLayout OblecenieGraphicViewPanelLayout = new javax.swing.GroupLayout(OblecenieGraphicViewPanel);
        OblecenieGraphicViewPanel.setLayout(OblecenieGraphicViewPanelLayout);
        OblecenieGraphicViewPanelLayout.setHorizontalGroup(
            OblecenieGraphicViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1198, Short.MAX_VALUE)
        );
        OblecenieGraphicViewPanelLayout.setVerticalGroup(
            OblecenieGraphicViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 798, Short.MAX_VALUE)
        );

        OblecenieGraphicScrollPane.setViewportView(OblecenieGraphicViewPanel);

        javax.swing.GroupLayout OblecenieViewPanelLayout = new javax.swing.GroupLayout(OblecenieViewPanel);
        OblecenieViewPanel.setLayout(OblecenieViewPanelLayout);
        OblecenieViewPanelLayout.setHorizontalGroup(
            OblecenieViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OblecenieViewPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(OblecenieViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OblecenieViewPanelLayout.createSequentialGroup()
                        .addComponent(zobrazOblecenieKategoriaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(oblecenieFormPridajNoveOblecenieButton)
                        .addGap(18, 18, 18)
                        .addComponent(spravcaKategoriiButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spravcaPraniaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(OblecenieGraphicScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(OblecenieListViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        OblecenieViewPanelLayout.setVerticalGroup(
            OblecenieViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OblecenieViewPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(OblecenieViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zobrazOblecenieKategoriaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oblecenieFormPridajNoveOblecenieButton)
                    .addComponent(spravcaKategoriiButton)
                    .addComponent(spravcaPraniaButton))
                .addGap(109, 109, 109)
                .addGroup(OblecenieViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(OblecenieListViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(OblecenieGraphicScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(251, 251, 251))
        );

        javax.swing.GroupLayout OblecenieFrameLayout = new javax.swing.GroupLayout(OblecenieFrame.getContentPane());
        OblecenieFrame.getContentPane().setLayout(OblecenieFrameLayout);
        OblecenieFrameLayout.setHorizontalGroup(
            OblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(OblecenieViewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        OblecenieFrameLayout.setVerticalGroup(
            OblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OblecenieFrameLayout.createSequentialGroup()
                .addComponent(OblecenieViewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 285, Short.MAX_VALUE))
        );

        OblecenieFrame.getAccessibleContext().setAccessibleParent(HlavneMenuFrame);

        HlavneMenuFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        HlavneMenuFrame.setTitle("Oblec Ma ! ");

        odhlasButton.setText("Odhlas");
        odhlasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odhlasButtonActionPerformed(evt);
            }
        });

        hlavneMenuOblecenieButton.setText("Oblecenie");
        hlavneMenuOblecenieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hlavneMenuOblecenieButtonActionPerformed(evt);
            }
        });

        oblecMaButton.setText("Oblec Ma");
        oblecMaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oblecMaButtonActionPerformed(evt);
            }
        });

        oblecMaVysledokList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        oblecMaVysledokList.setCellRenderer(new OblecMaListCellRenderer());
        jScrollPane1.setViewportView(oblecMaVysledokList);

        obleciemSiToButton.setText("Oblečiem si to");
        obleciemSiToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obleciemSiToButtonActionPerformed(evt);
            }
        });

        zmenOznaceneButton.setText("Zmeň označené");
        zmenOznaceneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zmenOznaceneButtonActionPerformed(evt);
            }
        });

        OdporucKombinaciuButton.setText("Odporuc");
        OdporucKombinaciuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OdporucKombinaciuButtonActionPerformed(evt);
            }
        });

        DalsiaKombinaciaButton.setText("Dalsia Kombinacia");
        DalsiaKombinaciaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DalsiaKombinaciaButtonActionPerformed(evt);
            }
        });

        ZakazKombinaciuButton.setText("Zakaz");
        ZakazKombinaciuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZakazKombinaciuButtonActionPerformed(evt);
            }
        });

        stupneLabel.setText("teplota");

        stavPocasiaLabel.setText("dazd/sneh/slnecno");

        vietorLable.setText("vietor");

        kombinacieButton.setText("Kombinacie");
        kombinacieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kombinacieButtonActionPerformed(evt);
            }
        });

        historiaButton.setText("Historia");
        historiaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historiaButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Obuv");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        obujMaButton2.setText("Obuj Ma!");

        obujMaList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane10.setViewportView(obujMaList);

        javax.swing.GroupLayout HlavneMenuFrameLayout = new javax.swing.GroupLayout(HlavneMenuFrame.getContentPane());
        HlavneMenuFrame.getContentPane().setLayout(HlavneMenuFrameLayout);
        HlavneMenuFrameLayout.setHorizontalGroup(
            HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HlavneMenuFrameLayout.createSequentialGroup()
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HlavneMenuFrameLayout.createSequentialGroup()
                        .addContainerGap(19, Short.MAX_VALUE)
                        .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(odhlasButton)
                            .addComponent(stavPocasiaLabel)
                            .addComponent(stupneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vietorLable))
                        .addGap(36, 36, 36))
                    .addGroup(HlavneMenuFrameLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ZakazKombinaciuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(OdporucKombinaciuButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(obleciemSiToButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lokaciaPreZistovaniePocasia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(oblecMaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HlavneMenuFrameLayout.createSequentialGroup()
                        .addComponent(DalsiaKombinaciaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(zmenOznaceneButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HlavneMenuFrameLayout.createSequentialGroup()
                        .addComponent(historiaButton)
                        .addGap(119, 119, 119)
                        .addComponent(kombinacieButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(hlavneMenuOblecenieButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(obujMaButton2)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        HlavneMenuFrameLayout.setVerticalGroup(
            HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HlavneMenuFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HlavneMenuFrameLayout.createSequentialGroup()
                        .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hlavneMenuOblecenieButton)
                            .addComponent(odhlasButton)
                            .addComponent(kombinacieButton)
                            .addComponent(historiaButton))
                        .addGap(23, 23, 23)
                        .addComponent(lokaciaPreZistovaniePocasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oblecMaButton)
                    .addComponent(obujMaButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane10)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addGroup(HlavneMenuFrameLayout.createSequentialGroup()
                        .addComponent(stupneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stavPocasiaLabel)
                        .addGap(10, 10, 10)
                        .addComponent(vietorLable)
                        .addGap(22, 22, 22)
                        .addComponent(OdporucKombinaciuButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ZakazKombinaciuButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(obleciemSiToButton)
                .addGap(12, 12, 12)
                .addGroup(HlavneMenuFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(zmenOznaceneButton)
                    .addComponent(DalsiaKombinaciaButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HlavneMenuFrame.getAccessibleContext().setAccessibleDescription("");

        VyberObrazkaPreOblecenieChooser.setAcceptAllFileFilterUsed(false);
        VyberObrazkaPreOblecenieChooser.setFileFilter(new PicturesFileFilter());
        VyberObrazkaPreOblecenieChooser.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        VyberObrazkaPreOblecenieChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VyberObrazkaPreOblecenieChooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout VyberObrazkaChooserFrameLayout = new javax.swing.GroupLayout(VyberObrazkaChooserFrame.getContentPane());
        VyberObrazkaChooserFrame.getContentPane().setLayout(VyberObrazkaChooserFrameLayout);
        VyberObrazkaChooserFrameLayout.setHorizontalGroup(
            VyberObrazkaChooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
            .addGroup(VyberObrazkaChooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VyberObrazkaChooserFrameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(VyberObrazkaPreOblecenieChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        VyberObrazkaChooserFrameLayout.setVerticalGroup(
            VyberObrazkaChooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
            .addGroup(VyberObrazkaChooserFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(VyberObrazkaChooserFrameLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(VyberObrazkaPreOblecenieChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        registraciaFrame.setTitle("Registracia");

        registraciaMenoTextField.setText("meno");

        registraciaHesloTextField.setText("heslo");

        reistraciaButton.setText("Registruj sa");
        reistraciaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reistraciaButtonActionPerformed(evt);
            }
        });

        registraciaReporterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registraciaReporterLabel.setText("Dobry den");

        registraciaMuzRadioButton.setText("muz");
        registraciaMuzRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registraciaMuzRadioButtonActionPerformed(evt);
            }
        });

        registraciaZenaRadioButton.setText("zena");

        javax.swing.GroupLayout registraciaFrameLayout = new javax.swing.GroupLayout(registraciaFrame.getContentPane());
        registraciaFrame.getContentPane().setLayout(registraciaFrameLayout);
        registraciaFrameLayout.setHorizontalGroup(
            registraciaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registraciaFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(registraciaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(registraciaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(reistraciaButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(registraciaMuzRadioButton)
                        .addComponent(registraciaZenaRadioButton)
                        .addComponent(registraciaHesloTextField)
                        .addComponent(registraciaMenoTextField))
                    .addComponent(registraciaReporterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        registraciaFrameLayout.setVerticalGroup(
            registraciaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registraciaFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registraciaMenoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(registraciaHesloTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(registraciaMuzRadioButton)
                .addGap(9, 9, 9)
                .addComponent(registraciaZenaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reistraciaButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(registraciaReporterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        registraciaPohlavieButtonGroup.add(registraciaMuzRadioButton);  
        registraciaMuzRadioButton.setSelected(true);
        registraciaPohlavieButtonGroup.add(registraciaZenaRadioButton);

        SpravcaKategoriiForm.setTitle("Spravca kategorii");

        vymazKategoriuButton.setText("Vymazať kategóriu");
        vymazKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vymazKategoriuButtonActionPerformed(evt);
            }
        });

        upravitKategoriuButton.setText("Upraviť kategóriu");
        upravitKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upravitKategoriuButtonActionPerformed(evt);
            }
        });

        pridatKategoriuButton.setText("Pridať kategóriu");
        pridatKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridatKategoriuButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SpravcaKategoriiFormLayout = new javax.swing.GroupLayout(SpravcaKategoriiForm.getContentPane());
        SpravcaKategoriiForm.getContentPane().setLayout(SpravcaKategoriiFormLayout);
        SpravcaKategoriiFormLayout.setHorizontalGroup(
            SpravcaKategoriiFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SpravcaKategoriiFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SpravcaKategoriiFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vymazKategoriuButton)
                    .addComponent(upravitKategoriuButton)
                    .addComponent(pridatKategoriuButton))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        SpravcaKategoriiFormLayout.setVerticalGroup(
            SpravcaKategoriiFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SpravcaKategoriiFormLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(pridatKategoriuButton)
                .addGap(18, 18, 18)
                .addComponent(vymazKategoriuButton)
                .addGap(18, 18, 18)
                .addComponent(upravitKategoriuButton)
                .addGap(72, 72, 72))
        );

        javax.swing.GroupLayout VyberKategoriuDialogFormLayout = new javax.swing.GroupLayout(VyberKategoriuDialogForm.getContentPane());
        VyberKategoriuDialogForm.getContentPane().setLayout(VyberKategoriuDialogFormLayout);
        VyberKategoriuDialogFormLayout.setHorizontalGroup(
            VyberKategoriuDialogFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        VyberKategoriuDialogFormLayout.setVerticalGroup(
            VyberKategoriuDialogFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        PridatKategoriuDialog.setTitle("Nova Kategoria");
        PridatKategoriuDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                PridatKategoriuDialogWindowClosing(evt);
            }
        });

        okPridatKategoriuButton.setText("OK");
        okPridatKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okPridatKategoriuButtonActionPerformed(evt);
            }
        });

        nazovPridatKategoriuLabel.setText("Názov:");

        lokaciaPridatKategoriuLabel.setText("Lokalizácia:");

        vrstvaPridatKategoriuLabel.setText("Vrstva:");

        lokalizaciaPridatKategoriuComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HLAVA", "KRK", "HRUĎ", "NOHY", "CHODIDLÁ","RUKY" }));
        lokalizaciaPridatKategoriuComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lokalizaciaPridatKategoriuComboBoxActionPerformed(evt);
            }
        });

        zrusitPridatKategoriuButton.setText("Cancel");
        zrusitPridatKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zrusitPridatKategoriuButtonActionPerformed(evt);
            }
        });

        vrstvaPridatKategoriuComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Prvá", "Druhá", "Tretia", "Štvrtá" }));

        pouzitiaKategoriaLabel.setText("Navrhnúť do prania po:");

        pouzitiaKategoriaTextField.setText("10");

        pouzitiaKategoriaLabel2.setText("použitiach.");

        javax.swing.GroupLayout PridatKategoriuDialogLayout = new javax.swing.GroupLayout(PridatKategoriuDialog.getContentPane());
        PridatKategoriuDialog.getContentPane().setLayout(PridatKategoriuDialogLayout);
        PridatKategoriuDialogLayout.setHorizontalGroup(
            PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                                    .addComponent(lokaciaPridatKategoriuLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(lokalizaciaPridatKategoriuComboBox, 0, 125, Short.MAX_VALUE))
                                .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                                    .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nazovPridatKategoriuLabel)
                                        .addComponent(vrstvaPridatKategoriuLabel))
                                    .addGap(52, 52, 52)
                                    .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nazovPridatKategoriuTextField)
                                        .addComponent(vrstvaPridatKategoriuComboBox, 0, 125, Short.MAX_VALUE))))
                            .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                                .addComponent(pouzitiaKategoriaLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pouzitiaKategoriaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pouzitiaKategoriaLabel2))))
                    .addGroup(PridatKategoriuDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(okPridatKategoriuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(zrusitPridatKategoriuButton)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        PridatKategoriuDialogLayout.setVerticalGroup(
            PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PridatKategoriuDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nazovPridatKategoriuLabel)
                    .addComponent(nazovPridatKategoriuTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lokaciaPridatKategoriuLabel)
                    .addComponent(lokalizaciaPridatKategoriuComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vrstvaPridatKategoriuLabel)
                    .addComponent(vrstvaPridatKategoriuComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pouzitiaKategoriaLabel)
                    .addComponent(pouzitiaKategoriaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pouzitiaKategoriaLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, Short.MAX_VALUE)
                .addGroup(PridatKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okPridatKategoriuButton)
                    .addComponent(zrusitPridatKategoriuButton))
                .addContainerGap())
        );

        oblecenieNemaKategoriuForm.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        oblecenieBezKategorieList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(oblecenieBezKategorieList);

        zmazanaKategoriaLabel.setText("Zmazal si kategóriu. Niektoré oblečenia teraz nemajú žiadnu kategóriu.");

        zmenKategoriuButton.setText("Zmeň kategóriu");
        zmenKategoriuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zmenKategoriuButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        novaKategoriaComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        novaKategoriaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novaKategoriaComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout oblecenieNemaKategoriuFormLayout = new javax.swing.GroupLayout(oblecenieNemaKategoriuForm.getContentPane());
        oblecenieNemaKategoriuForm.getContentPane().setLayout(oblecenieNemaKategoriuFormLayout);
        oblecenieNemaKategoriuFormLayout.setHorizontalGroup(
            oblecenieNemaKategoriuFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oblecenieNemaKategoriuFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(oblecenieNemaKategoriuFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(oblecenieNemaKategoriuFormLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addGroup(oblecenieNemaKategoriuFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(zmenKategoriuButton)
                            .addComponent(novaKategoriaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(zmazanaKategoriaLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(oblecenieNemaKategoriuFormLayout.createSequentialGroup()
                .addGap(204, 204, 204)
                .addComponent(okButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        oblecenieNemaKategoriuFormLayout.setVerticalGroup(
            oblecenieNemaKategoriuFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, oblecenieNemaKategoriuFormLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(zmazanaKategoriaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(oblecenieNemaKategoriuFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(oblecenieNemaKategoriuFormLayout.createSequentialGroup()
                        .addComponent(novaKategoriaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(zmenKategoriuButton)))
                .addGap(33, 33, 33)
                .addComponent(okButton)
                .addContainerGap())
        );

        upravKategoriuDialog.setTitle("Upravovanie Kategorie");

        nazovKategorieLabel.setText("Názov:");

        lokalizaciaKategorieLabel.setText("Lokalizácia:");

        vrstvaKategorieLabel.setText("Vrstva:");

        lokalizaciaKategorieCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "HLAVA", "KRK", "HRUĎ", "RUKY", "NOHY", "CHODIDLÁ" }));

        vrstvaKategorieCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));

        okUpravitButtno.setText("OK");
        okUpravitButtno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okUpravitButtnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout upravKategoriuDialogLayout = new javax.swing.GroupLayout(upravKategoriuDialog.getContentPane());
        upravKategoriuDialog.getContentPane().setLayout(upravKategoriuDialogLayout);
        upravKategoriuDialogLayout.setHorizontalGroup(
            upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upravKategoriuDialogLayout.createSequentialGroup()
                .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(upravKategoriuDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nazovKategorieLabel)
                            .addComponent(lokalizaciaKategorieLabel)
                            .addComponent(vrstvaKategorieLabel))
                        .addGap(57, 57, 57)
                        .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nazovKategorieTextField)
                            .addComponent(lokalizaciaKategorieCombo, 0, 121, Short.MAX_VALUE)
                            .addComponent(vrstvaKategorieCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(upravKategoriuDialogLayout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(okUpravitButtno)))
                .addContainerGap(126, Short.MAX_VALUE))
        );
        upravKategoriuDialogLayout.setVerticalGroup(
            upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(upravKategoriuDialogLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nazovKategorieLabel)
                    .addComponent(nazovKategorieTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lokalizaciaKategorieLabel)
                    .addComponent(lokalizaciaKategorieCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(upravKategoriuDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vrstvaKategorieLabel)
                    .addComponent(vrstvaKategorieCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(okUpravitButtno)
                .addGap(58, 58, 58))
        );

        pranieLabel.setText("Zobraziť:");

        jScrollPane3.setViewportView(obleceniePranieList);

        pranieComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "oblečenie v praní", "oblečenie mimo prania", "čo treba dať vyprať" }));
        pranieComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pranieComboBoxItemStateChanged(evt);
            }
        });

        odobratZPraniaButton.setText("Odobrať z prania");
        odobratZPraniaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odobratZPraniaButtonActionPerformed(evt);
            }
        });

        pridatDoPraniaButton.setText("Pridať do prania");
        pridatDoPraniaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridatDoPraniaButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout spravcaPraniaFormLayout = new javax.swing.GroupLayout(spravcaPraniaForm.getContentPane());
        spravcaPraniaForm.getContentPane().setLayout(spravcaPraniaFormLayout);
        spravcaPraniaFormLayout.setHorizontalGroup(
            spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(odobratZPraniaButton)
                            .addComponent(pridatDoPraniaButton)))
                    .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                        .addComponent(pranieLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pranieComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        spravcaPraniaFormLayout.setVerticalGroup(
            spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pranieLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pranieComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(spravcaPraniaFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(spravcaPraniaFormLayout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(odobratZPraniaButton)
                        .addGap(34, 34, 34)
                        .addComponent(pridatDoPraniaButton)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        NoveOblecenieFrame.setTitle("Nove Oblecenie");

        noveOblecenieNepremokaveCheckBox.setText("nepremokave");
        noveOblecenieNepremokaveCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noveOblecenieNepremokaveCheckBoxItemStateChanged(evt);
            }
        });

        noveOblecenieKategoriaCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noveOblecenieKategoriaComboItemStateChanged(evt);
            }
        });
        noveOblecenieKategoriaCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noveOblecenieKategoriaComboActionPerformed(evt);
            }
        });

        noveOblecenieMenoTextField.setText("oblecenie meno");

        noveOblecenieNeprefukaCheckBox.setText("neprefuka");
        noveOblecenieNeprefukaCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noveOblecenieNeprefukaCheckBoxItemStateChanged(evt);
            }
        });
        noveOblecenieNeprefukaCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noveOblecenieNeprefukaCheckBoxActionPerformed(evt);
            }
        });

        noveOblecenieZatepleneCheckBox.setText("zateplene");
        noveOblecenieZatepleneCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noveOblecenieZatepleneCheckBoxItemStateChanged(evt);
            }
        });

        noveOblecenieFormalneCheckBox.setText("formalne");
        noveOblecenieFormalneCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                noveOblecenieFormalneCheckBoxItemStateChanged(evt);
            }
        });
        noveOblecenieFormalneCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noveOblecenieFormalneCheckBoxActionPerformed(evt);
            }
        });

        vyberObrazkaComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                vyberObrazkaComboBoxItemStateChanged(evt);
            }
        });

        pridajNoveOblecenieButton.setText("Pridaj Oblecenie");
        pridajNoveOblecenieButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridajNoveOblecenieButtonActionPerformed(evt);
            }
        });

        noveOblecenieObrazokLabel.setText("obrazok");
        noveOblecenieObrazokLabel.setMaximumSize(new java.awt.Dimension(250, 250));
        noveOblecenieObrazokLabel.setMinimumSize(new java.awt.Dimension(250, 250));
        noveOblecenieObrazokLabel.setName(""); // NOI18N

        nacitajObrazok.setText("novyObrazok");
        nacitajObrazok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nacitajObrazokActionPerformed(evt);
            }
        });

        noveOblecenieBezObrazkaRadioButton.setText("bez obrazka");
        noveOblecenieBezObrazkaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noveOblecenieBezObrazkaRadioButtonActionPerformed(evt);
            }
        });

        noveObleceniePoziciavanieCheckBox.setText("poziciavanie");

        javax.swing.GroupLayout NoveOblecenieFrameLayout = new javax.swing.GroupLayout(NoveOblecenieFrame.getContentPane());
        NoveOblecenieFrame.getContentPane().setLayout(NoveOblecenieFrameLayout);
        NoveOblecenieFrameLayout.setHorizontalGroup(
            NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NoveOblecenieFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(noveObleceniePoziciavanieCheckBox)
                    .addComponent(noveOblecenieMenoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(NoveOblecenieFrameLayout.createSequentialGroup()
                        .addGroup(NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(noveOblecenieZatepleneCheckBox)
                            .addComponent(noveOblecenieFormalneCheckBox)
                            .addComponent(noveOblecenieNepremokaveCheckBox)
                            .addComponent(noveOblecenieNeprefukaCheckBox)
                            .addComponent(noveOblecenieKategoriaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vyberObrazkaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(noveOblecenieBezObrazkaRadioButton))
                        .addGap(18, 18, 18)
                        .addComponent(noveOblecenieObrazokLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(NoveOblecenieFrameLayout.createSequentialGroup()
                        .addComponent(pridajNoveOblecenieButton)
                        .addGap(109, 109, 109)
                        .addComponent(nacitajObrazok)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        NoveOblecenieFrameLayout.setVerticalGroup(
            NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NoveOblecenieFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(noveOblecenieMenoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NoveOblecenieFrameLayout.createSequentialGroup()
                        .addComponent(noveOblecenieKategoriaCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(noveOblecenieBezObrazkaRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(vyberObrazkaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(noveOblecenieFormalneCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(noveOblecenieZatepleneCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noveOblecenieNeprefukaCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(noveOblecenieNepremokaveCheckBox))
                    .addComponent(noveOblecenieObrazokLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(noveObleceniePoziciavanieCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(NoveOblecenieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pridajNoveOblecenieButton)
                    .addComponent(nacitajObrazok))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kombinacieFrame.setTitle("Kombinacie");

        odporuceneKombinacieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        odporuceneKombinacieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                odporuceneKombinacieListValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(odporuceneKombinacieList);

        odporuceneKombinacieLable.setText("Odporucene Kombinacie");

        zakazaneKombinacieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        zakazaneKombinacieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                zakazaneKombinacieListValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(zakazaneKombinacieList);

        ZakazaneKombinacieLabel.setText("Zakazane Kombinacie");

        InformacieOznacenejKombinacieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        InformacieOznacenejKombinacieList.setCellRenderer(new BezOznaceniaJList());
        InformacieOznacenejKombinacieList.setSelectionBackground(new java.awt.Color(255, 255, 255));
        InformacieOznacenejKombinacieList.setValueIsAdjusting(true);
        jScrollPane6.setViewportView(InformacieOznacenejKombinacieList);

        odporucaneNaZakazaneButton.setText(">>");
        odporucaneNaZakazaneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odporucaneNaZakazaneButtonActionPerformed(evt);
            }
        });

        ZakazaneNaOdporucaneButton.setText("<<");
        ZakazaneNaOdporucaneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZakazaneNaOdporucaneButtonActionPerformed(evt);
            }
        });

        odoberZoZoznamuOdporucaneZakazaneButton.setText("Odober zo Zoznamu");
        odoberZoZoznamuOdporucaneZakazaneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odoberZoZoznamuOdporucaneZakazaneButtonActionPerformed(evt);
            }
        });

        stupnePreKombinaciuLabel.setText("stupne");

        stavPocasiaPreKombinaciuLabel.setText("stav");

        vietorPreKombinaciuLabel.setText("vietor");

        javax.swing.GroupLayout kombinacieFrameLayout = new javax.swing.GroupLayout(kombinacieFrame.getContentPane());
        kombinacieFrame.getContentPane().setLayout(kombinacieFrameLayout);
        kombinacieFrameLayout.setHorizontalGroup(
            kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kombinacieFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kombinacieFrameLayout.createSequentialGroup()
                        .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(odporuceneKombinacieLable)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(odporucaneNaZakazaneButton)
                            .addGroup(kombinacieFrameLayout.createSequentialGroup()
                                .addComponent(ZakazaneNaOdporucaneButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ZakazaneKombinacieLabel)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(kombinacieFrameLayout.createSequentialGroup()
                        .addComponent(stupnePreKombinaciuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(stavPocasiaPreKombinaciuLabel)
                        .addGap(80, 80, 80)
                        .addComponent(vietorPreKombinaciuLabel))
                    .addGroup(kombinacieFrameLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(odoberZoZoznamuOdporucaneZakazaneButton, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        kombinacieFrameLayout.setVerticalGroup(
            kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kombinacieFrameLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ZakazaneKombinacieLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(odporuceneKombinacieLable))
                .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kombinacieFrameLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)))
                    .addGroup(kombinacieFrameLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(odporucaneNaZakazaneButton)
                        .addGap(18, 18, 18)
                        .addComponent(ZakazaneNaOdporucaneButton)))
                .addGap(18, 18, 18)
                .addComponent(odoberZoZoznamuOdporucaneZakazaneButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(kombinacieFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stupnePreKombinaciuLabel)
                    .addComponent(stavPocasiaPreKombinaciuLabel)
                    .addComponent(vietorPreKombinaciuLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        HistoriaFrame.setTitle("Historia");

        historiaDatumComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                historiaDatumComboBoxItemStateChanged(evt);
            }
        });

        historiaObleceneKombinacieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        historiaObleceneKombinacieList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                historiaObleceneKombinacieListValueChanged(evt);
            }
        });
        jScrollPane7.setViewportView(historiaObleceneKombinacieList);

        historiaOblecenieKombinacieList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane8.setViewportView(historiaOblecenieKombinacieList);

        javax.swing.GroupLayout HistoriaFrameLayout = new javax.swing.GroupLayout(HistoriaFrame.getContentPane());
        HistoriaFrame.getContentPane().setLayout(HistoriaFrameLayout);
        HistoriaFrameLayout.setHorizontalGroup(
            HistoriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoriaFrameLayout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addGroup(HistoriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(historiaDatumComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        HistoriaFrameLayout.setVerticalGroup(
            HistoriaFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HistoriaFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historiaDatumComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        pridajObuvButton.setText("Pridaj");
        pridajObuvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridajObuvButtonActionPerformed(evt);
            }
        });

        odstranObuvButton.setText("Odstran");
        odstranObuvButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odstranObuvButtonActionPerformed(evt);
            }
        });

        obuvList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(obuvList);

        obuvLableZateplene.setText("zateplene");

        obuvLableNepremokave.setText("nepremokave");

        vetraneLable.setText("vetrane");

        javax.swing.GroupLayout ObuvFrameLayout = new javax.swing.GroupLayout(ObuvFrame.getContentPane());
        ObuvFrame.getContentPane().setLayout(ObuvFrameLayout);
        ObuvFrameLayout.setHorizontalGroup(
            ObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObuvFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(odstranObuvButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(obuvLableZateplene)
                    .addComponent(obuvLableNepremokave)
                    .addComponent(vetraneLable)
                    .addComponent(pridajObuvButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ObuvFrameLayout.setVerticalGroup(
            ObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ObuvFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ObuvFrameLayout.createSequentialGroup()
                        .addComponent(pridajObuvButton)
                        .addGap(18, 18, 18)
                        .addComponent(odstranObuvButton)
                        .addGap(18, 18, 18)
                        .addComponent(obuvLableZateplene))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(obuvLableNepremokave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(vetraneLable)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        nazovObuvyText.setText("nazov obuvy");
        nazovObuvyText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nazovObuvyTextActionPerformed(evt);
            }
        });

        obuvNepremokaveCheckBox1.setText("nepremokave");
        obuvNepremokaveCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                obuvNepremokaveCheckBox1ActionPerformed(evt);
            }
        });

        obuvZatepleneCheckBox2.setText("zateplene");

        vetraneCheckBox3.setText("vetrane");

        pridajObuvButtonObuvFrame.setText("Pridaj");
        pridajObuvButtonObuvFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridajObuvButtonObuvFrameActionPerformed(evt);
            }
        });

        zrusitObuvFrameButton.setText("Zrusit");
        zrusitObuvFrameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zrusitObuvFrameButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pridajObuvFrameLayout = new javax.swing.GroupLayout(pridajObuvFrame.getContentPane());
        pridajObuvFrame.getContentPane().setLayout(pridajObuvFrameLayout);
        pridajObuvFrameLayout.setHorizontalGroup(
            pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pridajObuvFrameLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nazovObuvyText, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pridajObuvFrameLayout.createSequentialGroup()
                        .addComponent(pridajObuvButtonObuvFrame)
                        .addGap(26, 26, 26)
                        .addComponent(zrusitObuvFrameButton)))
                .addGap(18, 18, 18)
                .addGroup(pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vetraneCheckBox3)
                    .addComponent(obuvZatepleneCheckBox2)
                    .addComponent(obuvNepremokaveCheckBox1))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pridajObuvFrameLayout.setVerticalGroup(
            pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pridajObuvFrameLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nazovObuvyText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(obuvNepremokaveCheckBox1))
                .addGap(18, 18, 18)
                .addGroup(pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pridajObuvFrameLayout.createSequentialGroup()
                        .addComponent(obuvZatepleneCheckBox2)
                        .addGap(18, 18, 18)
                        .addComponent(vetraneCheckBox3))
                    .addGroup(pridajObuvFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pridajObuvButtonObuvFrame)
                        .addComponent(zrusitObuvFrameButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prihlasenie");

        prihlasmenofield.setText("meno");
        prihlasmenofield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prihlasmenofieldActionPerformed(evt);
            }
        });

        prihlasheslofield.setText("heslo");
        prihlasheslofield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prihlasheslofieldActionPerformed(evt);
            }
        });

        prihlasovanieReportLabel.setText("Odhlaseny");

        prihlasButton.setText("Prihlas");
        prihlasButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prihlasButtonActionPerformed(evt);
            }
        });

        registrujOdkazButton.setText("Novy Uzivatel");
        registrujOdkazButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registrujOdkazButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(prihlasovanieReportLabel)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(prihlasmenofield)
                    .addComponent(prihlasheslofield)
                    .addComponent(registrujOdkazButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prihlasButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(registrujOdkazButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prihlasmenofield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(prihlasheslofield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prihlasovanieReportLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prihlasButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public ImageIcon ImageIconFromFile(File file, int width, int height) {
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

    public ImageIcon ImageIconFromFile(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);

    }

    public ImageIcon ImageIconFromFile(File file) {
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }
    private void VyberObrazkaPreOblecenieChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VyberObrazkaPreOblecenieChooserActionPerformed

        File file = VyberObrazkaPreOblecenieChooser.getSelectedFile();
        int index = vyberObrazka(file);
        vyberObrazkaComboBox.setSelectedIndex(index - 1);
        VyberObrazkaChooserFrame.setVisible(false);

    }//GEN-LAST:event_VyberObrazkaPreOblecenieChooserActionPerformed

    private void prihlasmenofieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prihlasmenofieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prihlasmenofieldActionPerformed

    private void prihlasheslofieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prihlasheslofieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_prihlasheslofieldActionPerformed

    private void prihlasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prihlasButtonActionPerformed
        prihlasPouzivatela(prihlasmenofield.getText(), prihlasheslofield.getText());
        //zobrazObleceniePodlaKategorie("Vsetko");
        try {
            //uzivatel.vratIdPrihlasenehoPouzivatela();
            this.pranie = new Pranie(oblecenie, uzivatel.vratIdPrihlasenehoPouzivatela(), kategoria);
        } catch (NieJePrihlasenyZiadenPouzivatelException e) {
            //   Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Nie je prihlaseny pouzivatel");
        } catch (Exception ex) {
            //  Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ina chyba");
        }
    }//GEN-LAST:event_prihlasButtonActionPerformed

    private void registrujOdkazButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registrujOdkazButtonActionPerformed
        registraciaFrame.setVisible(true);
        registraciaFrame.pack();
    }//GEN-LAST:event_registrujOdkazButtonActionPerformed

    private void reistraciaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reistraciaButtonActionPerformed

        vytvorPouzivatela(registraciaMenoTextField.getText(), registraciaHesloTextField.getText(), registraciaMuzRadioButton.isSelected());

    }//GEN-LAST:event_reistraciaButtonActionPerformed

    private void upravitKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upravitKategoriuButtonActionPerformed
        JRadioButton uprav = null;
        Long cisloKategorie = Kategoria.ineFinal;
        for (JRadioButton button : spravcaKategorieAvailableButtons) {
            if (button.isSelected()) {
                cisloKategorie = kategoria.vratCislo(button.getText(), aktualneID);
                uprav = button;
                break;
            }
        }
        if (Kategoria.ineFinal.equals(cisloKategorie)) {
            return;
        }
        this.upravovanaKategoria = kategoria.vratKategoriu(cisloKategorie, aktualneID);

        upravKategoriuDialog.setVisible(true);
        upravKategoriuDialog.setAlwaysOnTop(true);
        upravKategoriuDialog.pack();


    }//GEN-LAST:event_upravitKategoriuButtonActionPerformed

    private void vymazKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vymazKategoriuButtonActionPerformed
        JRadioButton odstran = null;
        Long cisloKategorie = 0L;
        for (JRadioButton button : spravcaKategorieAvailableButtons) {
            if (button.isSelected()) {
                cisloKategorie = kategoria.vratCislo(button.getText(), aktualneID);
                kategoria.odstranKategoriu(button.getText(), aktualneID);
                aktualizujKategorie(false, true, false, button.getText());
                odstran = button;
            }
        }

        if (odstran != null) {
            try {
                oblecenie.odstranKategoriuOblecenia(cisloKategorie, uzivatel.vratIdPrihlasenehoPouzivatela());

                Long IdUzivatela = this.uzivatel.vratIdPrihlasenehoPouzivatela();

                Object[] bezKategorie = oblecenie.dajObleceniePodlaKategorie(Kategoria.ineFinal, IdUzivatela).toArray();
                if (bezKategorie.length > 0) {
                    this.oblecenieBezKategorieList.setListData(bezKategorie);

                    oblecenieNemaKategoriuForm.pack();

                    oblecenieNemaKategoriuForm.setAlwaysOnTop(true);
                    oblecenieNemaKategoriuForm.setModal(true);
                    oblecenieNemaKategoriuForm.setVisible(true);

                }
                spravcaKategorieAvailableButtons.remove(odstran);
                SpravcaKategoriiForm.remove(odstran);
                kategorieButtonGroup.remove(odstran);
                aktualizujSpravcaKategoriiPolohaRadioButtons();
                SpravcaKategoriiForm.repaint();

            } catch (Exception ex) {
                Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_vymazKategoriuButtonActionPerformed

    private void pridatKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridatKategoriuButtonActionPerformed

        PridatKategoriuDialog.setVisible(true);
        PridatKategoriuDialog.setAlwaysOnTop(true);
        PridatKategoriuDialog.pack();
    }//GEN-LAST:event_pridatKategoriuButtonActionPerformed

    private void lokalizaciaPridatKategoriuComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lokalizaciaPridatKategoriuComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lokalizaciaPridatKategoriuComboBoxActionPerformed

    private void zrusitPridatKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zrusitPridatKategoriuButtonActionPerformed

        if (pridavanieKategorie) {
            nebolaPridanaKategoriaNastavenieCombo();
            OblecenieFrame.setEnabled(true);
            pridavanieKategorie = false;
        }
        PridatKategoriuDialog.setVisible(false);


    }//GEN-LAST:event_zrusitPridatKategoriuButtonActionPerformed

    private void okPridatKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okPridatKategoriuButtonActionPerformed
        String nazov = nazovPridatKategoriuTextField.getText();
        if (nazov == null) {
            return;
        }
        if ("".equals(nazov)) {
            JOptionPane.showMessageDialog(this, "Kategória nemá názov!");
            return;
        }
        if (platnyString(nazov) == false) {
            return;
        }
        String lokalizacia = (String) lokalizaciaPridatKategoriuComboBox.getSelectedItem();
        String vrstva = (String) vrstvaPridatKategoriuComboBox.getSelectedItem();

        if (kategoria.vratCislo(nazov, aktualneID) != Kategoria.ineFinal) {
            JOptionPane.showInternalMessageDialog(this, "Kategŕia s rovnakým názvom už existuje!");
            System.out.println("Taka uz je");
            return;
        }

        int cisloLokalizacie = 0;
        switch (lokalizacia) {
            case "HLAVA":
                cisloLokalizacie = Kategoria.HLAVA;
                break;
            case "KRK":
                cisloLokalizacie = Kategoria.KRK;
                break;
            case "HRUĎ":
                cisloLokalizacie = Kategoria.TELO;
                break;
            case "NOHY":
                cisloLokalizacie = Kategoria.NOHY;
                break;
            case "CHODIDLÁ":
                cisloLokalizacie = Kategoria.CHODIDLA;
                break;
            case "RUKY":
                cisloLokalizacie = Kategoria.RUKY;
            default:
                break;
        }
        int cisloVrstvy = 0;

        switch (vrstva) {
            case "Prvá":
                cisloVrstvy = 1;
                break;
            case "Druhá":
                cisloVrstvy = 2;
                break;
            case "Tretia":
                cisloVrstvy = 3;
                break;
            case "Štvrtá":
                cisloVrstvy = 4;
                break;
            default:
                break;
        }

        int maxPocetPouziti = 10;

        try {
            maxPocetPouziti = Integer.parseInt(pouzitiaKategoriaTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nepsrávne zadaný počet pre použitia, po ktorých má byť oblečenie z danej kategórie navrhnuté do prania. Očakávaná  hodnota: celé číslo.");
            return;
        }

        Kategoria novaKategoria = new Kategoria(0L, nazov, cisloLokalizacie, cisloVrstvy, aktualneID, maxPocetPouziti);
        kategoria.pridajKategoriu(novaKategoria, aktualneID);

        /* START : pridanie radio do kreslenia */
        JRadioButton radioButton = new JRadioButton(nazov);
        SpravcaKategoriiForm.add(radioButton);

        radioButton.setBounds(250, 30 * (kategoria.vratPocet(aktualneID) - 1), 100, 30);
        spravcaKategorieAvailableButtons.add(radioButton);
        kategorieButtonGroup.add(radioButton);

        aktualizujKategorie(true, false, false, nazov);

        /*END : pridanie radio do kreslenia*/
        PridatKategoriuDialog.setVisible(false);
        /* aktivacia okna */
        if (pridavanieKategorie) {
            OblecenieFrame.setEnabled(true);
            bolaPridanaKategoriaNastavenieCombo();
            pridavanieKategorie = false;
        }

    }//GEN-LAST:event_okPridatKategoriuButtonActionPerformed

    private void registraciaMuzRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registraciaMuzRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registraciaMuzRadioButtonActionPerformed

    private void PridatKategoriuDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_PridatKategoriuDialogWindowClosing

        if (pridavanieKategorie) {
            pridavanieKategorie = false;
            OblecenieFrame.setEnabled(true);
            nebolaPridanaKategoriaNastavenieCombo();
        }

    }//GEN-LAST:event_PridatKategoriuDialogWindowClosing

    private void zmenKategoriuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zmenKategoriuButtonActionPerformed

        Oblecenie vybraneOblecenie = (Oblecenie) oblecenieBezKategorieList.getSelectedValue();

        if (vybraneOblecenie == null) {
            return;
        }

        int indexVybraneho = oblecenieBezKategorieList.getSelectedIndex();
        Long idUzivatela;
        try {
            idUzivatela = uzivatel.vratIdPrihlasenehoPouzivatela();

            String nazovNovejKategorie = novaKategoriaComboBox.getSelectedItem().toString();

            System.out.println(nazovNovejKategorie);

            Kategoria novaKategoria = kategoria.vratKategoriu(nazovNovejKategorie, aktualneID);
            Long idNovejKategorie = novaKategoria.getCislo();

            oblecenie.nastavKategoriuOblecenie(vybraneOblecenie.getIdOblecenia(), idNovejKategorie, idUzivatela);

            oblecenieBezKategorieList.setListData(oblecenie.dajObleceniePodlaKategorie(Kategoria.ineFinal, idUzivatela).toArray());
            System.out.println(oblecenie.dajObleceniePodlaKategorie(Kategoria.ineFinal, uzivatel.vratIdPrihlasenehoPouzivatela()));
        } catch (Exception ex) {
            Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);

        }

    }//GEN-LAST:event_zmenKategoriuButtonActionPerformed

    private void novaKategoriaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novaKategoriaComboBoxActionPerformed
    }//GEN-LAST:event_novaKategoriaComboBoxActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        ListModel model = oblecenieBezKategorieList.getModel();
        if (model.getSize() != 0) {
            return;
        }

        oblecenieNemaKategoriuForm.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void okUpravitButtnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okUpravitButtnoActionPerformed

        if (upravovanaKategoria == null) {
            return;

        }
        String nazov = nazovKategorieTextField.getText();

        if (platnyString(nazov) == false) {
            return;
        }

        if (!Kategoria.ineFinal.equals(kategoria.vratCislo(nazov, aktualneID)) && !upravovanaKategoria.getNazov().equals(nazov)) {
            System.out.println("Taka uz je");
            JOptionPane.showMessageDialog(this, "Taky nazov uz je");
            return;
        }

        String staryNazov = this.upravovanaKategoria.getNazov();
        this.aktualizujKategorie(false, true, false, staryNazov);

        this.upravovanaKategoria.setNazov(nazov);

        int cisloLokalizacie = 0;
        String lokalizacia = lokalizaciaKategorieCombo.getSelectedItem().toString();
        String vrstva = vrstvaKategorieCombo.getSelectedItem().toString();
        int cisloVrstvy = 0;

        switch (lokalizacia) {
            case "HLAVA":
                cisloLokalizacie = Kategoria.HLAVA;
                break;
            case "KRK":
                cisloLokalizacie = Kategoria.KRK;
                break;
            case "HRUĎ":
                cisloLokalizacie = Kategoria.TELO;
                break;
            case "NOHY":
                cisloLokalizacie = Kategoria.NOHY;
                break;
            case "CHODIDLÁ":
                cisloLokalizacie = Kategoria.CHODIDLA;
                break;
            case "RUKY":
                cisloLokalizacie = Kategoria.RUKY;
            default:
                break;
        }

        switch (vrstva) {
            case "Prvá":
                cisloVrstvy = 1;
                break;
            case "Druhá":
                cisloVrstvy = 2;
                break;
            case "Tretia":
                cisloVrstvy = 3;
                break;
            case "Štvrtá":
                cisloVrstvy = 4;
                break;
            default:
                break;
        }

        this.upravovanaKategoria.setLokacia(cisloLokalizacie);
        this.upravovanaKategoria.setVrstva(cisloVrstvy);

        kategoria.upravKategoriu(upravovanaKategoria, aktualneID);

        this.aktualizujKategorie(true, false, false, this.upravovanaKategoria.getNazov());
        this.zmenNazovRadioButton(staryNazov, this.upravovanaKategoria.getNazov());
//this.spravZoznamKategoriiDoRadioButtons();
        this.SpravcaKategoriiForm.repaint();
        this.upravovanaKategoria = null;
        upravKategoriuDialog.setVisible(false);
    }//GEN-LAST:event_okUpravitButtnoActionPerformed

    private void upravKategoriuDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_upravKategoriuDialogWindowClosing

    }//GEN-LAST:event_upravKategoriuDialogWindowClosing

    private void upravKategoriuDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_upravKategoriuDialogWindowClosed

    }//GEN-LAST:event_upravKategoriuDialogWindowClosed

    private void odobratZPraniaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odobratZPraniaButtonActionPerformed
        Oblecenie oznaceneOblecenie = (Oblecenie) obleceniePranieList.getSelectedValue();

        if (oznaceneOblecenie == null) {
            return;
        }
        pranie.odoberZPrania(oznaceneOblecenie);
        oznaceneOblecenie.setvPrani(false);
        oblecenie.upravOblecenie(oznaceneOblecenie);
        nacitajPranieList(pranieComboBox.getSelectedItem().toString());    }//GEN-LAST:event_odobratZPraniaButtonActionPerformed

    private void pridatDoPraniaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridatDoPraniaButtonActionPerformed
        Oblecenie oznaceneOblecenie = (Oblecenie) obleceniePranieList.getSelectedValue();
        if (oznaceneOblecenie == null) {
            return;
        }
        pranie.pridajDoPrania(oznaceneOblecenie);
        oznaceneOblecenie.setvPrani(true);
        oblecenie.upravOblecenie(oznaceneOblecenie);
        nacitajPranieList(pranieComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_pridatDoPraniaButtonActionPerformed

    private void pranieComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pranieComboBoxItemStateChanged
        String oznaceneText = pranieComboBox.getSelectedItem().toString();
        nacitajPranieList(oznaceneText);
    }//GEN-LAST:event_pranieComboBoxItemStateChanged

    private void nacitajObrazokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nacitajObrazokActionPerformed
        VyberObrazkaChooserFrame.setVisible(true);
        VyberObrazkaChooserFrame.setAlwaysOnTop(true);
        VyberObrazkaChooserFrame.pack();
    }//GEN-LAST:event_nacitajObrazokActionPerformed

    private void spravcaPraniaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spravcaPraniaButtonActionPerformed
        this.spravcaPraniaForm.setVisible(true);
        this.spravcaPraniaForm.pack();
        this.spravcaPraniaForm.setAlwaysOnTop(true);
        nacitajPranieList("oblečenie v praní");
    }//GEN-LAST:event_spravcaPraniaButtonActionPerformed

    private void odoberOblecenieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odoberOblecenieButtonActionPerformed
        Oblecenie oznaceneOblecenie = (Oblecenie) this.oblecenieList.getSelectedValue();
        if (oznaceneOblecenie != null) {
            oblecenie.vyhodOblecenie(oznaceneOblecenie.getIdOblecenia(), oznaceneOblecenie.getVlastnikID());
            //this.oblecenieList.remove(oblecenieList.getSelectedIndex());
            //  this.oblecenieList.setSelectedIndex(-1);
            refreshOblecenieList();
        }


    }//GEN-LAST:event_odoberOblecenieButtonActionPerformed

    private void noveOblecenieFormalneCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noveOblecenieFormalneCheckBoxItemStateChanged
        String text = noveOblecenieKategoriaCombo.getSelectedItem().toString();
        if (noveOblecenieFormalneCheckBox.isSelected()) {
            noveOblecenieMenoTextField.setText(text + " (formalne)");
        } else {
            noveOblecenieMenoTextField.setText(text);
        }
    }//GEN-LAST:event_noveOblecenieFormalneCheckBoxItemStateChanged

    private void noveOblecenieZatepleneCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noveOblecenieZatepleneCheckBoxItemStateChanged
        String text = noveOblecenieKategoriaCombo.getSelectedItem().toString();
        if (noveOblecenieZatepleneCheckBox.isSelected()) {
            noveOblecenieMenoTextField.setText(text + " (zateplene)");
        } else {
            noveOblecenieMenoTextField.setText(text);
        }
    }//GEN-LAST:event_noveOblecenieZatepleneCheckBoxItemStateChanged

    private void noveOblecenieNeprefukaCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noveOblecenieNeprefukaCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noveOblecenieNeprefukaCheckBoxActionPerformed

    private void noveOblecenieNeprefukaCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noveOblecenieNeprefukaCheckBoxItemStateChanged
        String text = noveOblecenieKategoriaCombo.getSelectedItem().toString();
        if (noveOblecenieNeprefukaCheckBox.isSelected()) {
            noveOblecenieMenoTextField.setText(text + " (neprefuka)");
        } else {
            noveOblecenieMenoTextField.setText(text);
        }
    }//GEN-LAST:event_noveOblecenieNeprefukaCheckBoxItemStateChanged

    private void noveOblecenieNepremokaveCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noveOblecenieNepremokaveCheckBoxItemStateChanged

        String text = noveOblecenieKategoriaCombo.getSelectedItem().toString();
        if (noveOblecenieNepremokaveCheckBox.isSelected()) {
            noveOblecenieMenoTextField.setText(text + " (nepremokave)");
        } else {
            noveOblecenieMenoTextField.setText(text);
        }
    }//GEN-LAST:event_noveOblecenieNepremokaveCheckBoxItemStateChanged

    private void zobrazOblecenieKategoriaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zobrazOblecenieKategoriaComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zobrazOblecenieKategoriaComboActionPerformed

    private void zobrazOblecenieKategoriaComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_zobrazOblecenieKategoriaComboItemStateChanged

        String oznaceneText = zobrazOblecenieKategoriaCombo.getSelectedItem().toString();
        zobrazObleceniePodlaKategorie(oznaceneText);
    }//GEN-LAST:event_zobrazOblecenieKategoriaComboItemStateChanged

    private void spravcaKategoriiButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spravcaKategoriiButtonActionPerformed
        //DAČO

        nacitajZoznamKategoriiDoRadioButtons();

        SpravcaKategoriiForm.repaint();
        SpravcaKategoriiForm.setVisible(true);
        SpravcaKategoriiForm.setAlwaysOnTop(true);
        SpravcaKategoriiForm.pack();
    }//GEN-LAST:event_spravcaKategoriiButtonActionPerformed

    private void pridajNoveOblecenieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridajNoveOblecenieButtonActionPerformed
        String kategoriaNazov = (String) noveOblecenieKategoriaCombo.getSelectedItem();
        if ("ine".equals(kategoriaNazov)) {

            //   PridatKategoriuDialog.setAlwaysOnTop(true);
            PridatKategoriuDialog.setVisible(true);
            PridatKategoriuDialog.setAlwaysOnTop(true);
            PridatKategoriuDialog.pack();

            // break;
            return;
        }
        String nazov = noveOblecenieMenoTextField.getText();

        if (platnyString(nazov) == false) {
            return;
        }

        pridajOblecenie(nazov, kategoriaNazov);

        /*zresetovanie checkBoxov po pridani*/
        noveOblecenieNeprefukaCheckBox.setSelected(false);
        noveOblecenieNepremokaveCheckBox.setSelected(false);
        noveOblecenieZatepleneCheckBox.setSelected(false);
        noveOblecenieFormalneCheckBox.setSelected(false);
        noveObleceniePoziciavanieCheckBox.setSelected(false);
        /* aktualizacia zobrazeni */
        zobrazObleceniePodlaKategorie(zobrazOblecenieKategoriaCombo.getSelectedItem().toString());
    }//GEN-LAST:event_pridajNoveOblecenieButtonActionPerformed

    private void noveOblecenieKategoriaComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noveOblecenieKategoriaComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noveOblecenieKategoriaComboActionPerformed

    private void noveOblecenieKategoriaComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_noveOblecenieKategoriaComboItemStateChanged

        if (!ukoncenePrihlasovanie) {
            return;
        }
        try {
            uzivatel.vratIdPrihlasenehoPouzivatela();
        } catch (Exception ex) {
            return;
        }
        Object selectedItem = noveOblecenieKategoriaCombo.getSelectedItem();

        if (selectedItem == null) {
            return;
        }
        String selected = selectedItem.toString();

        if (kategoria.vratNazov(Kategoria.ineFinal, aktualneID).equals(selected)) {
            /* zadefinovanie novej kategorie */
            int pocet = kategoria.vratPocet(aktualneID);
            OblecenieFrame.setEnabled(false);

            pridavanieKategorie = true;

            PridatKategoriuDialog.setVisible(true);
            PridatKategoriuDialog.setAlwaysOnTop(true);
            PridatKategoriuDialog.pack();

        } else {
            noveOblecenieMenoTextField.setText(selected);
        }
    }//GEN-LAST:event_noveOblecenieKategoriaComboItemStateChanged

    private void oblecenieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_oblecenieListValueChanged
        nacitanieParametrovObleceinaDoLabelov();
        nastavTextNosenieButton(obleceniePresunNoseneStareButton, (Oblecenie) oblecenieList.getSelectedValue());
    }//GEN-LAST:event_oblecenieListValueChanged


    private void oblecenieFormPridajNoveOblecenieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oblecenieFormPridajNoveOblecenieButtonActionPerformed
        NoveOblecenieFrame.setVisible(true);
        NoveOblecenieFrame.setAlwaysOnTop(true);
        Icon selectedIcon = (Icon) vyberObrazkaComboBox.getSelectedItem();
        if (selectedIcon != null) {
            noveOblecenieObrazokLabel.setIcon(selectedIcon.getIcon());
        }
        NoveOblecenieFrame.pack();
    }//GEN-LAST:event_oblecenieFormPridajNoveOblecenieButtonActionPerformed

    private void noveOblecenieFormalneCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noveOblecenieFormalneCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noveOblecenieFormalneCheckBoxActionPerformed

    private void vyberObrazkaComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_vyberObrazkaComboBoxItemStateChanged
        if (aktualneID == null || ukoncenePrihlasovanie == false) {
            return;
        }
        Icon selectedIcon = (Icon) vyberObrazkaComboBox.getSelectedItem();
        noveOblecenieObrazokLabel.setIcon(selectedIcon.getIcon());
    }//GEN-LAST:event_vyberObrazkaComboBoxItemStateChanged

    private void noveOblecenieBezObrazkaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noveOblecenieBezObrazkaRadioButtonActionPerformed
        if (noveOblecenieBezObrazkaRadioButton.isSelected()) {
            vyberObrazkaComboBox.setEnabled(false);
            nacitajObrazok.setEnabled(false);
            noveOblecenieObrazokLabel.setVisible(false);
        } else {
            vyberObrazkaComboBox.setEnabled(true);
            nacitajObrazok.setEnabled(true);
            noveOblecenieObrazokLabel.setVisible(true);
        }
    }//GEN-LAST:event_noveOblecenieBezObrazkaRadioButtonActionPerformed

    private void MozeSaPoziciavatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MozeSaPoziciavatButtonActionPerformed
        zmenPoziciavanie(MozeSaPoziciavatButton);
    }//GEN-LAST:event_MozeSaPoziciavatButtonActionPerformed

    private void odporuceneKombinacieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_odporuceneKombinacieListValueChanged
        if (!odporuceneKombinacieList.isSelectionEmpty()) {
            zakazaneKombinacieList.clearSelection();
            refreshKombinacieInfo();
            odoberZoZoznamuOdporucaneZakazaneButton.setEnabled(true);
            odoberZoZoznamuOdporucaneZakazaneButton.setText("odober odporucenu kombinaciu");
        }

    }//GEN-LAST:event_odporuceneKombinacieListValueChanged

    private void zakazaneKombinacieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_zakazaneKombinacieListValueChanged
        if (!zakazaneKombinacieList.isSelectionEmpty()) {
            odporuceneKombinacieList.clearSelection();
            refreshKombinacieInfo();
            odoberZoZoznamuOdporucaneZakazaneButton.setEnabled(true);
            odoberZoZoznamuOdporucaneZakazaneButton.setText("odober zakazanu kombinaciu");
        }


    }//GEN-LAST:event_zakazaneKombinacieListValueChanged

    private void odoberZoZoznamuOdporucaneZakazaneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odoberZoZoznamuOdporucaneZakazaneButtonActionPerformed
        if (odporuceneKombinacieList.isSelectionEmpty() == false) {
            KombinaciaANG oznacenaKombinacia = (KombinaciaANG) odporuceneKombinacieList.getSelectedValue();
            odporucaneZakazaneKombinacie.odstranOdporucanu(oznacenaKombinacia.getId());
            odporuceneKombinacieList.clearSelection();
            refreshKombinacieList();
            refreshKombinacieInfo();
        } else if (zakazaneKombinacieList.isSelectionEmpty() == false) {
            KombinaciaANG oznacenaKombinacia = (KombinaciaANG) zakazaneKombinacieList.getSelectedValue();
            odporucaneZakazaneKombinacie.odstranZakazanu(oznacenaKombinacia.getId());
            zakazaneKombinacieList.clearSelection();
            refreshKombinacieList();
            refreshKombinacieInfo();
        }
    }//GEN-LAST:event_odoberZoZoznamuOdporucaneZakazaneButtonActionPerformed

    private void ZakazaneNaOdporucaneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZakazaneNaOdporucaneButtonActionPerformed
        KombinaciaANG oznacenaKombinacia = (KombinaciaANG) zakazaneKombinacieList.getSelectedValue();

        if (oznacenaKombinacia == null) {
            return;
        }

        odporucaneZakazaneKombinacie.presunDoOdporucanej(oznacenaKombinacia.getId());

        zakazaneKombinacieList.clearSelection();

        refreshKombinacieList();
    }//GEN-LAST:event_ZakazaneNaOdporucaneButtonActionPerformed

    private void odporucaneNaZakazaneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odporucaneNaZakazaneButtonActionPerformed
        KombinaciaANG oznacenaKombinacia = (KombinaciaANG) odporuceneKombinacieList.getSelectedValue();

        if (oznacenaKombinacia == null) {
            return;
        }

        odporucaneZakazaneKombinacie.presunDoZakazanej(oznacenaKombinacia.getId());

        odporuceneKombinacieList.clearSelection();

        refreshKombinacieList();

    }//GEN-LAST:event_odporucaneNaZakazaneButtonActionPerformed

    private void kombinacieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kombinacieButtonActionPerformed
        kombinacieFrame.setVisible(true);
        kombinacieFrame.setAlwaysOnTop(true);
        kombinacieFrame.pack();
        refreshKombinacieList();
    }//GEN-LAST:event_kombinacieButtonActionPerformed

    private void ZakazKombinaciuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZakazKombinaciuButtonActionPerformed
        ListModel model = oblecMaVysledokList.getModel();
        if (model == null || model.getSize() == 0) {
            return;
        }
        oblecMa.odporucZakazKombinaciu(model, false, aktualneID);
        refreshKombinacieList();
    }//GEN-LAST:event_ZakazKombinaciuButtonActionPerformed

    private void DalsiaKombinaciaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DalsiaKombinaciaButtonActionPerformed
        ListModel model = oblecMaVysledokList.getModel();
        if (model == null || model.getSize() == 0) {
            return;
        }
        oblecMa.bolaKombinacia(model);
        oblecMaVysledokList.setListData(oblecMa.vratKombinaciu().toArray());
    }//GEN-LAST:event_DalsiaKombinaciaButtonActionPerformed

    private void OdporucKombinaciuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OdporucKombinaciuButtonActionPerformed
        ListModel model = oblecMaVysledokList.getModel();
        if (model == null || model.getSize() == 0) {
            return;
        }
        oblecMa.odporucZakazKombinaciu(model, true, aktualneID);
        refreshKombinacieList();
    }//GEN-LAST:event_OdporucKombinaciuButtonActionPerformed

    private void zmenOznaceneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zmenOznaceneButtonActionPerformed
        Oblecenie oznacene = (Oblecenie) oblecMaVysledokList.getSelectedValue();
        if (oznacene == null) {
            return;
        }

        ListModel<Oblecenie> model = oblecMaVysledokList.getModel();
        List<Oblecenie> staraKombinacia = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            Oblecenie aktualne = model.getElementAt(i);
            staraKombinacia.add(aktualne);
        }
        /*
        List<Oblecenie> novaKomb = oblecMa.zmenOblecenie(staraKombinacia, oznacene);
        oblecMaVysledokList.setListData(novaKomb.toArray());
         */

        oblecMaVysledokList.setListData(oblecMa.zmenJednoOblecenie(staraKombinacia, oznacene).toArray());
        oblecMaVysledokList.repaint();

    }//GEN-LAST:event_zmenOznaceneButtonActionPerformed

    private void obleciemSiToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obleciemSiToButtonActionPerformed
        ListModel<Oblecenie> model = oblecMaVysledokList.getModel();
        if (model.getSize() == 0) {
            return;
        }

        //PRIDAT DO HISTORIE
        List<Oblecenie> zoznam = new ArrayList<>();

        for (int i = 0; i < model.getSize(); i++) {
            Oblecenie aktualne = model.getElementAt(i);

            aktualne.use();
            oblecenie.upravOblecenie(aktualne);
            zoznam.add(aktualne);
        }
        Kombinacia nova = new Kombinacia(zoznam);
        historia.pridaj(nova);
        refreshOblecenieList();

    }//GEN-LAST:event_obleciemSiToButtonActionPerformed

    private void oblecMaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oblecMaButtonActionPerformed
        try {
            oblecMa.neformalnaPrilezitost(); // nastavi ze ide o neformalnu prilezitost
            String selectedItem = (String) lokaciaPreZistovaniePocasia.getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            oblecMa.oznaceneMesto(selectedItem);
            oblecMa.aktualizujPocasie();
            oblecMa.vycistPouziteKombinacie();
            oblecMaVysledokList.setListData(oblecMa.vratKombinaciu().toArray());

            nastavLabelyPocasia();
        } catch (NeuspesneZiskanieDatException ex) {
            JOptionPane.showMessageDialog(this, "Nepodarilo sa ziskat informacie o pocasi. \n Skontrolujte internetove pripojenie alebo skuste neskor prosim.");
        }

    }//GEN-LAST:event_oblecMaButtonActionPerformed

    private void hlavneMenuOblecenieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hlavneMenuOblecenieButtonActionPerformed

        if (graphic) {
            zobrazObleceniePodlaKategorie(zobrazOblecenieKategoriaCombo.getSelectedItem().toString());

        }

        /*  
        int x=5000;
        int y=5000;
         */

 /*    OblecenieGraphicScrollPane.setSize(x,y);
        OblecenieGraphicScrollPane.setSize(x, y);
        OblecenieGraphicScrollPane.getLayout().minimumLayoutSize(OblecenieGraphicScrollPane);
        OblecenieGraphicViewPanel.setSize(x,y);
         */
        OblecenieFrame.setVisible(true);
        //   OblecenieFrame.setAlwaysOnTop(true);
        OblecenieFrame.setSize(OblecenieFrame.getMaximumSize());
        //      OblecenieFrame.validate();
        //  OblecenieFrame.pack();

    }//GEN-LAST:event_hlavneMenuOblecenieButtonActionPerformed

    private void odhlasButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odhlasButtonActionPerformed
        odhlasPouzivatela();
    }//GEN-LAST:event_odhlasButtonActionPerformed

    private void historiaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historiaButtonActionPerformed

        List<Kombinacia> celaHistoria = historia.vratCeluHistoriu();
        List<Kombinacia> dnesHistoria = historia.vratKombinacieZoDna(LocalDate.now());
        historiaObleceneKombinacieList.setListData(dnesHistoria.toArray());

        HistoriaFrame.setVisible(true);      // otvori sa okno historie
        HistoriaFrame.pack();
    }//GEN-LAST:event_historiaButtonActionPerformed

    private void historiaObleceneKombinacieListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_historiaObleceneKombinacieListValueChanged
        Kombinacia oznacenaKombinacia = (Kombinacia) historiaObleceneKombinacieList.getSelectedValue();

        if (oznacenaKombinacia == null) {
            historiaOblecenieKombinacieList.setListData(new ArrayList().toArray());
            return;
        }

        Oblecenie[] kombinaciaOblecenia = oznacenaKombinacia.vratKombinaciu();

        if (kombinaciaOblecenia == null) {
            historiaOblecenieKombinacieList.setListData(new ArrayList().toArray());
            return;
        }

        historiaOblecenieKombinacieList.setListData(kombinaciaOblecenia);

    }//GEN-LAST:event_historiaObleceneKombinacieListValueChanged

    private void historiaDatumComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_historiaDatumComboBoxItemStateChanged
        LocalDate oznacenyDen = (LocalDate) historiaDatumComboBox.getSelectedItem();
        if (oznacenyDen == null) {
            return;
        }
        List<Kombinacia> kombinacieZoDna = historia.vratKombinacieZoDna(oznacenyDen);
        if (kombinacieZoDna == null) {
            return;
        }
        historiaObleceneKombinacieList.setListData(kombinacieZoDna.toArray());

    }//GEN-LAST:event_historiaDatumComboBoxItemStateChanged

    private void MozeSaPoziciavatButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MozeSaPoziciavatButtonMouseClicked

    }//GEN-LAST:event_MozeSaPoziciavatButtonMouseClicked

    private void obleceniePresunNoseneStareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obleceniePresunNoseneStareButtonActionPerformed
        zmenNosenie(obleceniePresunNoseneStareButton);
    }//GEN-LAST:event_obleceniePresunNoseneStareButtonActionPerformed
    private void refreshObuv() {
        obuvList.setListData((String[]) obuvy.toArray());
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ObuvFrame.setVisible(true);
        ObuvFrame.setAlwaysOnTop(true);
        ObuvFrame.pack();
        nepremokneLabel.setVisible(false);
        zatepleneLabel.setVisible(false);
        vetraneLable.setVisible(false);
        obuvy.addAll(obuvDao.dajVsetkyObuvy());
        refreshObuv();
        if (obuvList.isCursorSet()) {
            for (Obuv obuv : obuvy) {
                if (obuv.getNazov().equalsIgnoreCase(obuvList.getName())) {
                    nepremokneLabel.setVisible(obuv.isNepremokave());
                }
                zatepleneLabel.setVisible(obuv.isZateplene());
                vetraneLable.setVisible(obuv.isVetrane());
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void pridajObuvButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridajObuvButtonActionPerformed
        pridajObuvFrame.setVisible(true);
        pridajObuvFrame.setAlwaysOnTop(true);
        pridajObuvFrame.pack();
        refreshObuv();
        obuvNepremokaveCheckBox1.setSelected(false);
        obuvZatepleneCheckBox2.setSelected(false);
        vetraneCheckBox3.setSelected(false);
    }//GEN-LAST:event_pridajObuvButtonActionPerformed

    private void odstranObuvButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odstranObuvButtonActionPerformed
        obuvy.remove(obuvList.getSelectedIndex());
        refreshObuv();
    }//GEN-LAST:event_odstranObuvButtonActionPerformed

    private void nazovObuvyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nazovObuvyTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nazovObuvyTextActionPerformed

    private void zrusitObuvFrameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zrusitObuvFrameButtonActionPerformed
        pridajObuvFrame.setVisible(false);
        pridajObuvFrame.setAlwaysOnTop(false);
    }//GEN-LAST:event_zrusitObuvFrameButtonActionPerformed

    private void pridajObuvButtonObuvFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridajObuvButtonObuvFrameActionPerformed
        Obuv novaObuv = new Obuv();
        novaObuv.setNazov(nazovObuvyText.getText());
        novaObuv.setNepremokave(obuvNepremokaveCheckBox1.isSelected());
        novaObuv.setZateplene(obuvZatepleneCheckBox2.isSelected());
        novaObuv.setVetrane(vetraneCheckBox3.isSelected());
        obuvDao.pridajObuv(novaObuv, aktualneID);
        pridajObuvFrame.setVisible(false);
        pridajObuvFrame.setAlwaysOnTop(false);
        refreshObuv();
    }//GEN-LAST:event_pridajObuvButtonObuvFrameActionPerformed

    private void obuvNepremokaveCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_obuvNepremokaveCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_obuvNepremokaveCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(spustac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(spustac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(spustac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(spustac.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new spustac().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DalsiaKombinaciaButton;
    private javax.swing.JFrame HistoriaFrame;
    private javax.swing.JFrame HlavneMenuFrame;
    private javax.swing.JList InformacieOznacenejKombinacieList;
    private javax.swing.JToggleButton MozeSaPoziciavatButton;
    private javax.swing.JFrame NoveOblecenieFrame;
    private javax.swing.JFrame OblecenieFrame;
    private javax.swing.JScrollPane OblecenieGraphicScrollPane;
    private javax.swing.JPanel OblecenieGraphicViewPanel;
    private javax.swing.JScrollPane OblecenieList;
    private javax.swing.JPanel OblecenieListViewPanel;
    private javax.swing.JPanel OblecenieViewPanel;
    private javax.swing.JLabel ObrazokVybranehoObleceniaLabel;
    private javax.swing.JFrame ObuvFrame;
    private javax.swing.JButton OdporucKombinaciuButton;
    private javax.swing.JDialog PridatKategoriuDialog;
    private javax.swing.JFrame SpravcaKategoriiForm;
    private javax.swing.JDialog VyberKategoriuDialogForm;
    private javax.swing.JFrame VyberObrazkaChooserFrame;
    private javax.swing.JFileChooser VyberObrazkaPreOblecenieChooser;
    private javax.swing.JButton ZakazKombinaciuButton;
    private javax.swing.JLabel ZakazaneKombinacieLabel;
    private javax.swing.JButton ZakazaneNaOdporucaneButton;
    private javax.swing.JLabel formalnostLabel;
    private javax.swing.JButton historiaButton;
    private javax.swing.JComboBox historiaDatumComboBox;
    private javax.swing.JList historiaObleceneKombinacieList;
    private javax.swing.JList historiaOblecenieKombinacieList;
    private javax.swing.JButton hlavneMenuOblecenieButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel kategoriaLabel;
    private javax.swing.ButtonGroup kategorieButtonGroup;
    private javax.swing.JButton kombinacieButton;
    private javax.swing.JFrame kombinacieFrame;
    private javax.swing.JComboBox lokaciaPreZistovaniePocasia;
    private javax.swing.JLabel lokaciaPridatKategoriuLabel;
    private javax.swing.JComboBox lokalizaciaKategorieCombo;
    private javax.swing.JLabel lokalizaciaKategorieLabel;
    private javax.swing.JComboBox lokalizaciaPridatKategoriuComboBox;
    private javax.swing.JButton nacitajObrazok;
    private javax.swing.JLabel nazovKategorieLabel;
    private javax.swing.JTextField nazovKategorieTextField;
    private javax.swing.JLabel nazovObleceniaLabel;
    private javax.swing.JTextField nazovObuvyText;
    private javax.swing.JLabel nazovPridatKategoriuLabel;
    private javax.swing.JTextField nazovPridatKategoriuTextField;
    private javax.swing.JLabel neprefukaLabel;
    private javax.swing.JLabel nepremokneLabel;
    private javax.swing.JComboBox novaKategoriaComboBox;
    private javax.swing.JRadioButton noveOblecenieBezObrazkaRadioButton;
    private javax.swing.JCheckBox noveOblecenieFormalneCheckBox;
    private javax.swing.JComboBox noveOblecenieKategoriaCombo;
    private javax.swing.JTextField noveOblecenieMenoTextField;
    private javax.swing.JCheckBox noveOblecenieNeprefukaCheckBox;
    private javax.swing.JCheckBox noveOblecenieNepremokaveCheckBox;
    private javax.swing.JLabel noveOblecenieObrazokLabel;
    private javax.swing.JCheckBox noveObleceniePoziciavanieCheckBox;
    private javax.swing.JCheckBox noveOblecenieZatepleneCheckBox;
    private javax.swing.JLabel novostLabel;
    private javax.swing.JButton oblecMaButton;
    private javax.swing.JList oblecMaVysledokList;
    private javax.swing.JList oblecenieBezKategorieList;
    private javax.swing.JButton oblecenieFormPridajNoveOblecenieButton;
    private javax.swing.JList oblecenieList;
    private javax.swing.JDialog oblecenieNemaKategoriuForm;
    private javax.swing.JList obleceniePranieList;
    private javax.swing.JButton obleceniePresunNoseneStareButton;
    private javax.swing.JButton obleciemSiToButton;
    private javax.swing.JButton obujMaButton2;
    private javax.swing.JList<String> obujMaList;
    private javax.swing.JLabel obuvLableNepremokave;
    private javax.swing.JLabel obuvLableZateplene;
    private javax.swing.JList<String> obuvList;
    private javax.swing.JCheckBox obuvNepremokaveCheckBox1;
    private javax.swing.JCheckBox obuvZatepleneCheckBox2;
    private javax.swing.JButton odhlasButton;
    private javax.swing.JButton odoberOblecenieButton;
    private javax.swing.JButton odoberZoZoznamuOdporucaneZakazaneButton;
    private javax.swing.JButton odobratZPraniaButton;
    private javax.swing.JButton odporucaneNaZakazaneButton;
    private javax.swing.JLabel odporuceneKombinacieLable;
    private javax.swing.JList odporuceneKombinacieList;
    private javax.swing.JButton odstranObuvButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton okPridatKategoriuButton;
    private javax.swing.JButton okUpravitButtno;
    private javax.swing.JLabel pouzitiaKategoriaLabel;
    private javax.swing.JLabel pouzitiaKategoriaLabel2;
    private javax.swing.JTextField pouzitiaKategoriaTextField;
    private javax.swing.JComboBox pranieComboBox;
    private javax.swing.JLabel pranieLabel;
    private javax.swing.JButton pridajNoveOblecenieButton;
    private javax.swing.JButton pridajObuvButton;
    private javax.swing.JButton pridajObuvButtonObuvFrame;
    private javax.swing.JFrame pridajObuvFrame;
    private javax.swing.JButton pridatDoPraniaButton;
    private javax.swing.JButton pridatKategoriuButton;
    private javax.swing.JButton prihlasButton;
    private javax.swing.JTextField prihlasheslofield;
    private javax.swing.JTextField prihlasmenofield;
    private javax.swing.JLabel prihlasovanieReportLabel;
    private javax.swing.JFrame registraciaFrame;
    private javax.swing.JTextField registraciaHesloTextField;
    private javax.swing.JTextField registraciaMenoTextField;
    private javax.swing.JRadioButton registraciaMuzRadioButton;
    private javax.swing.ButtonGroup registraciaPohlavieButtonGroup;
    private javax.swing.JLabel registraciaReporterLabel;
    private javax.swing.JRadioButton registraciaZenaRadioButton;
    private javax.swing.JButton registrujOdkazButton;
    private javax.swing.JButton reistraciaButton;
    private javax.swing.JButton spravcaKategoriiButton;
    private javax.swing.JButton spravcaPraniaButton;
    private javax.swing.JFrame spravcaPraniaForm;
    private javax.swing.JLabel stavPocasiaLabel;
    private javax.swing.JLabel stavPocasiaPreKombinaciuLabel;
    private javax.swing.JLabel stupneLabel;
    private javax.swing.JLabel stupnePreKombinaciuLabel;
    private javax.swing.JDialog upravKategoriuDialog;
    private javax.swing.JButton upravitKategoriuButton;
    private javax.swing.JLabel vPraniLabel;
    private javax.swing.JCheckBox vetraneCheckBox3;
    private javax.swing.JLabel vetraneLable;
    private javax.swing.JLabel vietorLable;
    private javax.swing.JLabel vietorPreKombinaciuLabel;
    private javax.swing.JComboBox vrstvaKategorieCombo;
    private javax.swing.JLabel vrstvaKategorieLabel;
    private javax.swing.JComboBox vrstvaPridatKategoriuComboBox;
    private javax.swing.JLabel vrstvaPridatKategoriuLabel;
    private javax.swing.JComboBox vyberObrazkaComboBox;
    private javax.swing.JButton vymazKategoriuButton;
    private javax.swing.JList zakazaneKombinacieList;
    private javax.swing.JLabel zatepleneLabel;
    private javax.swing.JLabel zmazanaKategoriaLabel;
    private javax.swing.JButton zmenKategoriuButton;
    private javax.swing.JButton zmenOznaceneButton;
    private javax.swing.JComboBox zobrazOblecenieKategoriaCombo;
    private javax.swing.JButton zrusitObuvFrameButton;
    private javax.swing.JButton zrusitPridatKategoriuButton;
    // End of variables declaration//GEN-END:variables

    private void vytvorPouzivatela(String meno, String heslo, boolean muz) {
        try {

            if (platnyString(meno) == false) {
                return;
            }

            if (platnyString(heslo) == false) {
                return;
            }

            uzivatel.vytvorPouzivatela(meno, heslo, muz);
            registraciaFrame.setVisible(false);
        } catch (UzivatelExistujeException ex) {
            registraciaReporterLabel.setText("TAKE MENO UZ JE");
        } catch (Exception ex) {
            //   Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void prihlasPouzivatela(String meno, String heslo) {

        try {
            uzivatel.vratIdPrihlasenehoPouzivatela();
        } catch (NieJePrihlasenyZiadenPouzivatelException e) {

            try {
                ukoncenePrihlasovanie = false;

                if (platnyString(meno) == false) {
                    return;
                }
                if (platnyString(heslo) == false) {
                    return;
                }
                uzivatel.prihlasPouzivatela(meno, heslo);
                aktualneID = uzivatel.vratIdPrihlasenehoPouzivatela();
                System.out.println("uzivatel s id = " + aktualneID);

                spravZoznamKategoriiDoRadioButtons();
                /* ( pridaj, odober , nacitaj vsetko , nazov pre pridanie / odobranie ) */
                aktualizujKategorie(false, false, true, null);

                noveOblecenieMenoTextField.setText(noveOblecenieKategoriaCombo.getSelectedItem().toString());
                zobrazObleceniePodlaKategorie("Vsetko");

                this.setVisible(false);
                HlavneMenuFrame.setVisible(true);
                HlavneMenuFrame.pack();
                nastavTextNosenieButton(obleceniePresunNoseneStareButton, null);
                System.out.println("podarilo sa prihlasit");
                registraciaFrame.setVisible(false);
                historia.nastavUzivatela(aktualneID);

            } catch (NespravneMenoException ex) {
                prihlasovanieReportLabel.setText("TAKE MENO NIE JE");
            } catch (NespravneHesloException ex) {
                prihlasovanieReportLabel.setText("ZLE HESLO");
            } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
                prihlasovanieReportLabel.setText("NESPRAVNE MENO ALEBO HESLO");
            } catch (Exception ex) {
                Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (uzivatel.jePrihlasny(meno)) {
            prihlasovanieReportLabel.setText("Prihlaseny ako " + prihlasmenofield.getText());
            ukoncenePrihlasovanie = true;
        } else {
            System.out.println("nepodarilo sa prihlasit");
        }

    }

    private void odhlasPouzivatela() {
        try {
            odhlasovanie = true;
            uzivatel.odhlasPouzivatela();
            prihlasovanieReportLabel.setText("Odhlaseny");

            historia.nastavUzivatela(aktualneID);
            HlavneMenuFrame.setVisible(false);
            this.setVisible(true);
            odstranKategoriePoOdhlaseni();
            kombinacieFrame.setVisible(false);
            HistoriaFrame.setVisible(false);
            SpravcaKategoriiForm.setVisible(false);
            PridatKategoriuDialog.setVisible(false);
            upravKategoriuDialog.setVisible(false);
            NoveOblecenieFrame.setVisible(false);
            OblecenieFrame.setVisible(false);
            SpravcaKategoriiForm.setVisible(false);
            VyberObrazkaChooserFrame.setVisible(false);
            spravcaPraniaForm.setVisible(false);
            odhlasovanie = false;
            aktualneID = null;
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            prihlasovanieReportLabel.setText("NEMAM KOHO ODHLASIT");
        } catch (Exception ex) {
            System.out.println("nejaka chyba odhlasovania");
        }

    }

    private void pridajOblecenie(String nazov, Object oznacenaKategoria) {
        try {

            Long kategoriaCislo = kategoria.vratCislo(oznacenaKategoria.toString(), aktualneID);

            Oblecenie noveOblecenie = new Oblecenie(nazov, kategoriaCislo);

            if (noveOblecenieNepremokaveCheckBox.isSelected()) {
                noveOblecenie.setNepremokave();
            }
            if (noveOblecenieNeprefukaCheckBox.isSelected()) {
                noveOblecenie.setNeprefuka();
            }
            if (noveOblecenieFormalneCheckBox.isSelected()) {
                noveOblecenie.setFormalne();
            }
            if (noveOblecenieZatepleneCheckBox.isSelected()) {
                noveOblecenie.setZateplene();
            }
            boolean moze = noveObleceniePoziciavanieCheckBox.isSelected();
            noveOblecenie.setMozeSaPoziciavat(moze);

            Icon selectedItem = (Icon) vyberObrazkaComboBox.getSelectedItem();

            Long idObrazka = null;
            if (selectedItem != null && noveOblecenieBezObrazkaRadioButton.isSelected() == false) {
                idObrazka = (long) selectedItem.getId();
            }

            noveOblecenie.setIdObrazka(idObrazka);

            oblecenie.pridajOblecenie(noveOblecenie, uzivatel.vratIdPrihlasenehoPouzivatela());

        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            prihlasovanieReportLabel.setText("NEMOZES PRIDAVAT OBLECENIE KED NIE SI PRIHLASENY");
        } catch (Exception ex) {
            Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void dajVsetkyOblecenia(JList oblecenieList) {
        if (aktualneID == Uzivatel.globalUser) {
            return;
        }
        if (odhlasovanie) {
            return;
        }
        try {
            List<Oblecenie> dajVsetkyOblecenia = oblecenie.dajVsetkyOblecenia(uzivatel.vratIdPrihlasenehoPouzivatela());
            oblecenieList.setListData(dajVsetkyOblecenia.toArray());
            if (graphic) {
                Map<Oblecenie, ImageIcon> mapa = vytvorMapuOblecenieObrazok(dajVsetkyOblecenia);
                vykresli(mapa);
            }
        } catch (Exception ex) {
            System.err.println("nepodarilo sa dat vsetky oblecenia");

        }
    }

    private void dajKategoriu(JList oblecenieList, Long kategoria) {
        try {
            List<Oblecenie> dajObleceniePodlaKategorie = oblecenie.dajObleceniePodlaKategorie(kategoria, uzivatel.vratIdPrihlasenehoPouzivatela());
            oblecenieList.setListData(dajObleceniePodlaKategorie.toArray());
            if (graphic) {
                Map<Oblecenie, ImageIcon> mapa = vytvorMapuOblecenieObrazok(dajObleceniePodlaKategorie);
                vykresli(mapa);
            }
        } catch (Exception ex) {
            Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int vyberObrazka(File selectedFile) {
        try {
            zoznamobrazkov.pridajData(selectedFile);
        } catch (NepodariloSaPridatObrazokException ex) {

        }
        System.out.println("NAcial sa nnuvy");
        return pridanieIcon(selectedFile, -1, obrazokSirka, obrazokVyska);
    }

    private int pridanieIcon(File aktualny, int id, int i, int i0) {
        if (id == -1) {
            id = icons.size() + 1;
        }
        ImageIcon icon = ImageIconFromFile(aktualny, i, i0);
        Icon newIcon = new Icon(id);
        newIcon.setImage(icon);
        System.out.println("used " + aktualny.getPath());

        icons.add(newIcon);
        vyberObrazkaComboBox.addItem(newIcon);

        return id;

    }

    private void pridanieIcon(String aktualny, int id, int i, int i0) {
        if (id == -1) {
            id = icons.size() + 1;
        }
        ImageIcon icon = ImageIconFromFile(aktualny, i, i0);
        Icon newIcon = new Icon(id);
        newIcon.setImage(icon);
        System.out.println("used >>" + aktualny);

        icons.add(newIcon);
        vyberObrazkaComboBox.addItem(newIcon);
    }

    private void spravZoznamKategoriiDoRadioButtons() {
        int i = 0;

        for (JRadioButton button : spravcaKategorieAvailableButtons) {
            SpravcaKategoriiForm.remove(button);
            kategorieButtonGroup.remove(button);
        }
        spravcaKategorieAvailableButtons.clear();

        for (Kategoria category : kategoria.vratZoznamKategorii(aktualneID)) {

            if (category.getCislo() == Kategoria.ineFinal) {
                // tuto tam nebude zobrazovat
                continue;
            }

            JRadioButton radioButton = new JRadioButton(category.getNazov());
            SpravcaKategoriiForm.add(radioButton);
            spravcaKategorieAvailableButtons.add(radioButton);
            radioButton.setBounds(250, 30 * i, 100, 30);
            kategorieButtonGroup.add(radioButton);

            i++;

            //kategorieButtonGroup.add(radioButton);
            // kategorieScrollPane.add(radioButton);
        }
    }

    private void zmenNazovRadioButton(String staryNazov, String novyNazov) {
        for (JRadioButton radioButton : spravcaKategorieAvailableButtons) {
            if (staryNazov.equals(radioButton.getText())) {
                radioButton.setText(novyNazov);
            }
        }
    }

    private void aktualizujSpravcaKategoriiPolohaRadioButtons() {
        int i = 0;
        for (JRadioButton button : spravcaKategorieAvailableButtons) {
            button.setBounds(250, 30 * i, 100, 30);
            i++;
        }
    }

    private void nacitajZoznamKategoriiDoRadioButtons() {

    }

    private void odstranKategoriePoOdhlaseni() {
        int pocet = kategoria.vratPocet(aktualneID);
        System.out.println("pocet kategorii " + pocet);
        for (Kategoria aktualna : kategoria.vratZoznamKategorii(aktualneID)) {
            try {

                zobrazOblecenieKategoriaCombo.removeItem(aktualna.getNazov());

                if (aktualna.getCislo() == Kategoria.ineFinal) {
                    continue;
                }
                noveOblecenieKategoriaCombo.removeItem(aktualna.getNazov());
                novaKategoriaComboBox.removeItem(aktualna.getNazov());
            } catch (Exception e) {
                System.out.println("chyba pri vymazavani kategorii \n" + " pocet " + kategoria.vratPocet(aktualneID));
            }
        }

        if (prvePrihlasenie) {
            prvePrihlasenie = false;
            zobrazOblecenieKategoriaCombo.addItem("Vsetko");
        } else {
            noveOblecenieKategoriaCombo.removeItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
        }
    }

    private void aktualizujKategorie(boolean pridaj, boolean odober, boolean nacitajCele, String nazov) {

        if (nacitajCele) {
            int pocet = kategoria.vratPocet(aktualneID);
            System.out.println("pocet kategorii " + pocet);
            for (Kategoria aktualna : kategoria.vratZoznamKategorii(aktualneID)) {
                try {

                    zobrazOblecenieKategoriaCombo.removeItem(aktualna.getNazov());

                    if (aktualna.getCislo().equals(Kategoria.ineFinal)) {
                        continue;
                    }
                    noveOblecenieKategoriaCombo.removeItem(aktualna.getNazov());
                    novaKategoriaComboBox.removeItem(aktualna.getNazov());
                } catch (Exception e) {
                    System.out.println("chyba pri vymazavani kategorii \n" + " pocet " + kategoria.vratPocet(aktualneID));
                }
            }

            if (prvePrihlasenie) {
                prvePrihlasenie = false;
                zobrazOblecenieKategoriaCombo.addItem("Vsetko");
            } else {
                noveOblecenieKategoriaCombo.removeItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
            }
            List<Kategoria> vratZoznamKategorii = kategoria.vratZoznamKategorii(aktualneID);
            System.out.println("pocet kategorii " + kategoria.vratPocet(aktualneID));
            for (Kategoria aktualna : vratZoznamKategorii) {
                if (Kategoria.ineFinal.equals(aktualna.getCislo())) {
                    continue;
                }
                String nazovKategorie = aktualna.getNazov();
                System.out.println(nazovKategorie);
                noveOblecenieKategoriaCombo.addItem(nazovKategorie);
                zobrazOblecenieKategoriaCombo.addItem(nazovKategorie);
                novaKategoriaComboBox.addItem(nazovKategorie);
            }

            noveOblecenieKategoriaCombo.addItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
            // zobrazOblecenieKategoriaCombo.addItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));

        } else if (pridaj) {
            noveOblecenieKategoriaCombo.removeItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
            noveOblecenieKategoriaCombo.addItem(nazov);
            noveOblecenieKategoriaCombo.addItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));

            novaKategoriaComboBox.addItem(nazov);

            //  zobrazOblecenieKategoriaCombo.removeItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
            zobrazOblecenieKategoriaCombo.addItem(nazov);
            //  zobrazOblecenieKategoriaCombo.addItem(kategoria.vratNazov(Kategoria.ineFinal, aktualneID));
        } else if (odober) {
            noveOblecenieKategoriaCombo.removeItem(nazov);
            zobrazOblecenieKategoriaCombo.removeItem(nazov);
            novaKategoriaComboBox.removeItem(nazov);
        }

    }

    private void zobrazObleceniePodlaKategorie(String oznaceneText) {
        if ("Vsetko".equals(oznaceneText)) {
            dajVsetkyOblecenia(oblecenieList);
        } else {
            Long cisloKategorie = kategoria.vratCislo(oznaceneText, aktualneID);
            dajKategoriu(oblecenieList, cisloKategorie);
        }
    }

    private void nebolaPridanaKategoriaNastavenieCombo() {
        /*Nebola pridana kategoria*/
        noveOblecenieKategoriaCombo.setSelectedIndex(0);
        String selected = noveOblecenieKategoriaCombo.getSelectedItem().toString();
        noveOblecenieMenoTextField.setText(selected);
    }

    private void bolaPridanaKategoriaNastavenieCombo() {
        /* nastavi tu novo pridanu */
        noveOblecenieKategoriaCombo.setSelectedIndex(kategoria.vratPocet(aktualneID) - 2);
        String selected = noveOblecenieKategoriaCombo.getSelectedItem().toString();

        noveOblecenieMenoTextField.setText(selected);
    }

    private void nacitajPranieList(String oznaceneText) {
        switch (oznaceneText) {
            case "oblečenie v praní": {
                obleceniePranieList.setListData(pranie.getZoznamVeciVPrani().toArray());
                pridatDoPraniaButton.setVisible(false);
                odobratZPraniaButton.setVisible(true);
            }
            break;
            case "oblečenie mimo prania": {
                try {
                    obleceniePranieList.setListData(Pranie.dajVeciMimoPrania(oblecenie, this.uzivatel.vratIdPrihlasenehoPouzivatela(), pranie).toArray());
                    pridatDoPraniaButton.setVisible(true);
                    odobratZPraniaButton.setVisible(false);
                } catch (Exception ex) {
                    Logger.getLogger(spustac.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case "čo treba dať vyprať": {
                obleceniePranieList.setListData(pranie.getCoTrebaDatVyprat().toArray());
                pridatDoPraniaButton.setVisible(true);
                odobratZPraniaButton.setVisible(false);
                // System.out.println("vratil sa co treba prat");
            }
            break;
            default: {
                obleceniePranieList.setListData(pranie.getZoznamVeciVPrani().toArray());
                odobratZPraniaButton.setVisible(true);
                pridatDoPraniaButton.setVisible(false);
            }
            break;
        }
    }

    void resetLabelov() {
        nazovKategorieLabel.setText("");
        nazovObleceniaLabel.setText("");
        formalnostLabel.setText("");
        neprefukaLabel.setText("");
        nepremokneLabel.setText("");
        novostLabel.setText("");
        zatepleneLabel.setText("");
        vPraniLabel.setText("");
//        poziciavanieLabel.setText("");
        kategoriaLabel.setText("");
    }

    private void zmenPoziciavanie(JToggleButton mozeSaPozicat) {

        Oblecenie selectedValue = (Oblecenie) oblecenieList.getSelectedValue();
        boolean pozicaj = false;
        if (selectedValue == null) {
            mozeSaPozicat.setVisible(false);
            return;

        } else {
            mozeSaPozicat.setVisible(true);
            pozicaj = selectedValue.isMozeSaPoziciavat();

        }
        if (mozeSaPozicat.isSelected() && !pozicaj) {
            selectedValue.setMozeSaPoziciavat(true);
            oblecenie.upravOblecenie(selectedValue);
            nacitanieParametrovObleceinaDoLabelov();
        } else if (!mozeSaPozicat.isSelected() && pozicaj) {
            selectedValue.setMozeSaPoziciavat(false);
            oblecenie.upravOblecenie(selectedValue);
            nacitanieParametrovObleceinaDoLabelov();
        }
    }

    private void nacitanieParametrovObleceinaDoLabelov() {
        Oblecenie selectedValue = (Oblecenie) oblecenieList.getSelectedValue();
        System.out.println("icons");
        int i = 0;
        if (selectedValue == null) {
            MozeSaPoziciavatButton.setVisible(false);
            resetLabelov();
            return;
        }
        if (MozeSaPoziciavatButton.isVisible() == false) {
            MozeSaPoziciavatButton.setVisible(true);
        }
        Long idObrazka = selectedValue.getIdObrazka();
        ObrazokVybranehoObleceniaLabel.setIcon(null);
        if (idObrazka != null) {
            for (Icon aktualna : icons) {
                if (aktualna.getId() == idObrazka) {
                    System.out.println(aktualna.getId() + " == " + idObrazka);
                    ObrazokVybranehoObleceniaLabel.setIcon(aktualna.getIcon());
                    break;
                }
            }
        }
        // nastavovanie labelov
        nazovObleceniaLabel.setText(selectedValue.getNazov());
        kategoriaLabel.setText("(" + kategoria.vratNazov(selectedValue.getKategoria(), aktualneID) + ")");
        if (selectedValue.isFormalne()) {
            formalnostLabel.setText("formalne");
        } else {
            formalnostLabel.setText("neformalne");
        }
        String text = "";
        if (selectedValue.isNeprefuka()) {
            text = "neprefuka";
        }
        neprefukaLabel.setText(text);
        text = "";
        if (selectedValue.isNepremokave()) {
            text = "nepremokave";
        }
        nepremokneLabel.setText(text);
        text = "";
        if (selectedValue.isZateplene()) {
            text = "zateplene";
        }
        zatepleneLabel.setText(text);
        text = "";
        if (selectedValue.isNove()) {
            text = "nove";
        }
        if (selectedValue.isNosene()) {
            text = "nosene";
        }
        if (selectedValue.isStare()) {
            text = "stare";
        }
        novostLabel.setText(text);
        text = "";
        if (selectedValue.isvPrani()) {
            text = "perie sa";
        } else {
            text = "neperie sa";
        }
        vPraniLabel.setText(text);
        text = "";
        if (selectedValue.isMozeSaPoziciavat()) {
            text = "moze sa poziciavat";
            MozeSaPoziciavatButton.setSelected(true);
            MozeSaPoziciavatButton.setText(text);
        } else {
            MozeSaPoziciavatButton.setSelected(false);
            text = "nemoze sa poziciavat";
            MozeSaPoziciavatButton.setText(text);
        }
//        poziciavanieLabel.setText(text);
    }

    private void nastavLabelyPocasia() {
        Pocasie mojePocasie = pocasie.vratPocasie();

        int teplota = mojePocasie.getTeplotaCezDen();
        String text = teplota + " C";
        stupneLabel.setText(text);

        if (mojePocasie.fuka()) {
            vietorLable.setText("fuka");
        } else {
            vietorLable.setText("nefuka");
        }

        if (mojePocasie.isDazd()) {
            stavPocasiaLabel.setText("prsi");
        } else if (mojePocasie.isSneh()) {
            stavPocasiaLabel.setText("snezi");
        } else {
            stavPocasiaLabel.setText("bez zrazok");
        }
    }

    private void refreshKombinacieList() {

        List<KombinaciaANG> vsetkyOdporuceneKombinacie = odporucaneZakazaneKombinacie.vratVsetkyOdporuceneKombinacie(aktualneID);
        List<KombinaciaANG> vsetkyZakazaneKombinacie = odporucaneZakazaneKombinacie.vratVsetkyZakazaneKombinacie(aktualneID);

        odporuceneKombinacieList.setListData(vsetkyOdporuceneKombinacie.toArray());
        zakazaneKombinacieList.setListData(vsetkyZakazaneKombinacie.toArray());

        odporuceneKombinacieList.clearSelection();
        zakazaneKombinacieList.clearSelection();

        odoberZoZoznamuOdporucaneZakazaneButton.setEnabled(false);
        refreshKombinacieInfo();

    }

    private void refreshKombinacieInfo() {
        List<Oblecenie> oblecenieKombinacie = new ArrayList();
        KombinaciaANG oznacenaKombinacia = null;
        if (!odporuceneKombinacieList.isSelectionEmpty()) {
            oznacenaKombinacia = (KombinaciaANG) odporuceneKombinacieList.getSelectedValue();

        }
        if (!zakazaneKombinacieList.isSelectionEmpty()) {
            oznacenaKombinacia = (KombinaciaANG) zakazaneKombinacieList.getSelectedValue();
        }
        if (oznacenaKombinacia == null) {
            InformacieOznacenejKombinacieList.setListData(oblecenieKombinacie.toArray());
            return;
        }
        oblecenieKombinacie = ziskajOblecenieKombinacie(oznacenaKombinacia);
        refreshInformaciePocasiaPreKombinaciu(oznacenaKombinacia.getId());
        InformacieOznacenejKombinacieList.setListData(oblecenieKombinacie.toArray());
    }

    private List<Oblecenie> ziskajOblecenieKombinacie(KombinaciaANG oznacenaKombinacia) {
        List<Oblecenie> vrat = new ArrayList<>();

        Long[] kombinacia = oznacenaKombinacia.getKombinacia();

        for (Long aktualna : kombinacia) {
            if (aktualna == -1L) {
                continue;
            }

            Oblecenie oznaceneOblecenie = oblecenie.dajOblecenie(aktualna);
            vrat.add(oznaceneOblecenie);

        }

        return vrat;

    }

    private void refreshInformaciePocasiaPreKombinaciu(Long id) {
        Pocasie pocasiePreKombinaciu = odporucaneZakazaneKombinacie.vratPocasiePreKombinaciu(id);
        if (pocasiePreKombinaciu == null) {
            stavPocasiaPreKombinaciuLabel.setText("");
            vietorPreKombinaciuLabel.setText("");
            stupnePreKombinaciuLabel.setText("");
            return;
        }

        String text = pocasiePreKombinaciu.getTeplotaCezDen() + " C";
        stupnePreKombinaciuLabel.setText(text);

        if (pocasiePreKombinaciu.fuka()) {
            vietorPreKombinaciuLabel.setText("fuka");
        } else {
            vietorPreKombinaciuLabel.setText("nefuka");
        }
        if (pocasiePreKombinaciu.isDazd()) {
            stavPocasiaPreKombinaciuLabel.setText("prsi");
        } else if (pocasiePreKombinaciu.isSneh()) {
            stavPocasiaPreKombinaciuLabel.setText("snezi");
        } else {
            stavPocasiaPreKombinaciuLabel.setText("bez zrazok");
        }
    }

    private int denPodlaMesiaca(int mesiac, int rok) {
        switch (mesiac) {
            case 1:
                return 31;
            case 2:
                if (rok % 4 == 0) {
                    return 29;
                }

                return 28;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 30;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;

        }
        return 0;
    }

    private void zmenNosenie(JButton obleceniePresunNoseneStareButton) {
        Oblecenie oznacene = (Oblecenie) oblecenieList.getSelectedValue();

        if (oznacene == null) {
            return;
        }

        if (oznacene.isNove()) {
            oznacene.setNove(false);
            oznacene.setNosene(true);
        } else if (oznacene.isNosene()) {
            oznacene.setNosene(false);
            oznacene.setStare(true);
        } else if (oznacene.isStare()) {
            oznacene.setNosene(true);
            oznacene.setStare(false);

        }

        oblecenie.upravOblecenie(oznacene);
        nacitanieParametrovObleceinaDoLabelov();
        nastavTextNosenieButton(obleceniePresunNoseneStareButton, oznacene);

    }

    private void nastavTextNosenieButton(JButton obleceniePresunNoseneStareButton, Oblecenie oznOblecenie) {
        if (oznOblecenie == null) {
            obleceniePresunNoseneStareButton.setEnabled(false);
            obleceniePresunNoseneStareButton.setText("---");
            return;
        }

        if (obleceniePresunNoseneStareButton.isEnabled() == false) {
            obleceniePresunNoseneStareButton.setEnabled(true);
        }

        if (oznOblecenie.isNove()) {
            obleceniePresunNoseneStareButton.setText(">> nosene");
        }

        if (oznOblecenie.isNosene()) {
            obleceniePresunNoseneStareButton.setText(">> stare");
        }

        if (oznOblecenie.isStare()) {
            obleceniePresunNoseneStareButton.setText(">> nosene");
        }
    }

    private void refreshOblecenieList() {
        zobrazObleceniePodlaKategorie(zobrazOblecenieKategoriaCombo.getSelectedItem().toString());
        nacitanieParametrovObleceinaDoLabelov();
        nastavTextNosenieButton(obleceniePresunNoseneStareButton, (Oblecenie) oblecenieList.getSelectedValue());
    }

    private boolean platnyString(String nazov) {

        if (nazov.contains(">") || nazov.contains("<") || nazov.contains("=") || nazov.contains("?") || nazov.contains("!") || nazov.contains("$")) {
            JOptionPane.showMessageDialog(this, "Zadany text nesmie obsahovat : \n >, <, =, ?, !,$");
            return false;
        }

        if (nazov.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Zadany text nesmie obsahovať iba medzery alebo byť prázdny");
            return false;
        }

        return true;
    }

    public Map<Oblecenie, ImageIcon> vytvorMapuOblecenieObrazok(List<Oblecenie> dajVsetkyOblecenia) {
        Map<Oblecenie, ImageIcon> mapa = new HashMap<>();

        for (Oblecenie aktualne : dajVsetkyOblecenia) {
            if (aktualne == null) {
                continue;
            }
            mapa.put(aktualne, null);
            Long idObrazka = aktualne.getIdObrazka();
            if (idObrazka == null) {
                continue;
            }
            for (Icon aktualna : icons) {
                if (aktualna.getId() == idObrazka.intValue()) {
                    //      System.out.println(aktualna.getId() + " == " + idObrazka);
                    mapa.put(aktualne, aktualna.getIcon());
                    break;
                }
            }
        }

        return mapa;
    }
}
