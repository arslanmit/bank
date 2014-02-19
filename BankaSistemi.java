import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Objects;
import java.text.*;
import java.io.*;
import java.awt.PrintJob.*;
import javax.swing.plaf.metal.*;

public class BankaSistemi extends JFrame implements ActionListener,ItemListener{


    private JDesktopPane desktop=new JDesktopPane();

    private JMenuBar bar;

    private JMenu mnuFile,mnuEdit,mnuView,mnuOpt,mnuWin,mnuHelp;

    //Dosya Menusu
    private JMenuItem yeniEkle,kayitYazdir,cikis;
    // Edit
    private JMenuItem yatirma,cekme,kayit_sil,ara,isim_ara;
    // View
    private JMenuItem birMusteri,tumMusteriler;
    // Options
    private JMenuItem degistir,tema;
    // Pencereler
    private JMenuBar kapat,tumunu_kapat;
    //Help Menu
    private JMenuItem icerik,yardimAra,hakkimda;


    //Popuplar halinde olacak sayfa
    private JPopupMenu popMenu=new JPopupMenu();
    // Popup menu
    private JMenuItem ac,raporla,yatir,cek,sil,bul,tumu;

    // Programın toolbarı
    private JToolBar toolBar;

    // Toolbar butonları

    private JButton btnYeni,btnYatir,btnCek,btnKayit,btnSil,btnAra,btnYardim,btnAnahtar;

    // Programın adının ve msajın yazılacağı ekran
    private JPanel statusBar=new JPanel();
    // Labellar
    private JLabel giris;
    private JLabel yazar;

    //Temalar
    private String strings[]={"Metal","OsX","Windows"};
    private UIManager.LookAndFeelInfo look[]=UIManager.getInstalledLookAndFeels();
    private ButtonGroup group=new ButtonGroup();
    private JRadioButtonMenuItem radio[]=new JRadioButtonMenuItem[strings.length];

    // Sistem Tarihini al
    private Date currDate=new Date();
    private SimpleDateFormat sdf=new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    private String d=sdf.format(currDate);

    private int rows=0;
    private int total=0;

    //Dosyadaki verileri tutmak için bir array
    private String kayitlar[][]=new String[500][6];

    // BankaSisteminin dosya okuma ve yazması için
    private FileInputStream fis;
    private DataInputStream dis;

     // Const. ile gerekli değişkenler atanır
    public BankaSistemi(){
        System.out.println("Debug1");
        UIManager.addPropertyChangeListener(new UISwitchListener((JComponent)getRootPane()));

        //Menubarı olustur
        bar=new JMenuBar();
        // İkonu ayarlayalım
        setIconImage(getToolkit().getImage("Src/Images/Bank.gif"));
        setSize(700,550);
        setJMenuBar(bar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //quitApp();
            }
        });


        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

        // Menubarı olustur
        mnuFile=new JMenu("Dosya");
        mnuFile.setMnemonic((int)'D');

        mnuEdit=new JMenu("Düzenle");
        mnuEdit.setMnemonic((int)'E');

        mnuView=new JMenu("Gözden Geçir");
        mnuEdit.setMnemonic((int)'G');

        mnuOpt=new JMenu("Ayarlar");
        mnuOpt.setMnemonic((int)'A');

        mnuWin=new JMenu("Pencereler");
        mnuWin.setMnemonic((int)'P');

        mnuHelp=new JMenu("Yardım");
        mnuHelp.setMnemonic((int)'Y');

        // Dosya Menusunun itemları

        yeniEkle=new JMenuItem("Yeni Hesap Aç",new ImageIcon("Src/Images/Open.gif"));
        yeniEkle.setMnemonic((int)'Y');
        yeniEkle.addActionListener(this);


        kayitYazdir=new JMenuItem("Müşterilerin hesaplarını göster",new ImageIcon("Src/Images/New.gif"));
        kayitYazdir.setMnemonic((int)'G');
        kayitYazdir.addActionListener(this);

        cikis=new JMenuItem("Çıkış",new ImageIcon("Src/Images/export.gif"));
        cikis.setMnemonic((int)'Q');
        cikis.addActionListener(this);

         // Düzenle Menusu Itemları
        yatirma=new JMenuItem("Para Yatır");
        yatirma.setMnemonic((int)'Y');
        yatirma.addActionListener(this);

        cekme=new JMenuItem("Para Çekme");
        cekme.setMnemonic((int)'C');
        cekme.addActionListener(this);

        kayit_sil=new JMenuItem("Müşteri Sil",new ImageIcon("Src/Images/Delete.gif"));
        kayit_sil.setMnemonic((int)'S');
        kayit_sil.addActionListener(this);

        ara=new JMenuItem("Ara No:",new ImageIcon("Src/Images/find.gif"));
        ara.setMnemonic((int) 'N');
        ara.addActionListener(this);

        isim_ara=new JMenuItem("Ara İsim:");
        isim_ara.setMnemonic((int)'İ');
        isim_ara.addActionListener(this);






        // Gözden Geçirme Itemları
        birMusteri=new JMenuItem("Tek Hesap");
        birMusteri.setMnemonic((int)'O');
        birMusteri.addActionListener(this);

        tumMusteriler=new JMenuItem("Tüm Müşteriler");
        tumMusteriler.setMnemonic((int)'T');
        tumMusteriler.addActionListener(this);
         // Ayarlar Menusu

        tema=new JMenu("Tema Değiştir");
        for(int i=0;i<radio.length;i++){
            radio[i]=new JRadioButtonMenuItem(strings[i]);
            radio[i].addItemListener(this);
            group.add(radio[i]);
            tema.add(radio[i]);
        }

        // Status bar icin
        yazar=new JLabel("Şahin - Banka Uygulaması",Label.LEFT);
        yazar.setForeground(Color.black);
        yazar.setToolTipText("Şahin Yanlık");
        giris=new JLabel(d,JLabel.RIGHT);

        statusBar.setLayout(new BorderLayout());
        statusBar.add(yazar,BorderLayout.WEST);
        statusBar.add(giris,BorderLayout.EAST);

        mnuFile.add(yeniEkle);
        mnuFile.addSeparator();
        mnuFile.add(kayitYazdir);
        mnuFile.addSeparator();
        mnuFile.add(cikis);

        // Menuleri editler
        mnuEdit.add(yatirma);
        mnuEdit.addSeparator();
        mnuEdit.add(cekme);
        mnuEdit.addSeparator();
        mnuEdit.add(kayit_sil);
        mnuEdit.addSeparator();
        mnuEdit.add(ara);
        mnuEdit.addSeparator();
        mnuEdit.add(isim_ara);
        // Menuleri viewe ekle;
        mnuView.add(birMusteri);
        mnuView.addSeparator();
        mnuView.add(tumMusteriler);
        // Menu Edit
        mnuOpt.add(tema);
        // Menuleri bara ekle
        bar.add(mnuFile);
        bar.add(mnuEdit);
        bar.add(mnuView);
        bar.add(mnuOpt);


        getContentPane().add(statusBar,BorderLayout.SOUTH);
        getContentPane().add(desktop,BorderLayout.CENTER);
        setVisible(true);
    }

    public boolean altSayfaAc(String title)
    {
        JInternalFrame[] childs=desktop.getAllFrames();
        for(int i=0;i<childs.length;i++)
        {
            if(childs[i].getTitle().equalsIgnoreCase(title)){
                childs[i].show();
                return true;
            }
        }
        return false;
    }






    // Uygulama içerisindeki aksiyonlar
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        if(obj == yeniEkle || obj == ac)
        {
           boolean b=altSayfaAc("Yeni Hesap Ekle");
           if(b== false){
               YeniHesap yHesap=new YeniHesap();
               desktop.add(yHesap);
               yHesap.show();
           }
        }

        else if(obj == kayitYazdir){
             hesapnoAl();
        }

        else if(obj == cikis){
            uygulamadanCik();
        }
        // Edit bölümüne geçtik
        else if(obj == yatirma){
            boolean b=altSayfaAc("Para Yatır");
            if(b == false)
            {
                Yatirilacak yatPara=new Yatirilacak();
                desktop.add(yatPara);
                yatPara.show();
            }
        }

        else if(obj == cekme){
            boolean b=altSayfaAc("Para Çekme");
            if(b == false){
                Paracek pcek=new Paracek();
                desktop.add(pcek);
                pcek.show();
            }
        }
}



    void hesapnoAl(){
        String yazdiriliyor;
        rows=0;
        boolean b=arrayDoldur();
        if(b==false){}
        else{
            try{
                yazdiriliyor=JOptionPane.showInputDialog(this,"Hesap Numarasını giriniz","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                if(yazdiriliyor == null ){}
                if(yazdiriliyor.equals("")){
                    JOptionPane.showMessageDialog(this,"Lütfen Hesap No Yazınız","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                    hesapnoAl();
                }else{
                    kayitBul(yazdiriliyor);
                }
            }catch (Exception e){}
        }

    }

    boolean arrayDoldur(){
        boolean b=false;
        try{
            fis=new FileInputStream("Banka.dat");
            dis=new DataInputStream(fis);
            // 6 sutun...
            while(true){
                for(int i=0;i<6;i++){
                    kayitlar[rows][i]=dis.readUTF();
                }
                rows++;
            }

        }catch (Exception ex){
            total=rows;
            if(total == 0 ){
                JOptionPane.showMessageDialog(null,"Kayıt Dosyası boş","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                b=false;
            }
            else{
                b=true;
                try{
                    dis.close();
                    fis.close();
                }catch (Exception ext){}
            }
        }
        return b;
    }

    void kayitBul(String kay){
        boolean bulundu=false;
        for(int x=0;x<total;x++){
            if(kayitlar[x][0].equals(kay)){
                bulundu=true;
                kayitYazdir(kayitPrintHaleGetir(x));
                break;
            }
        }
    }


    String kayitPrintHaleGetir(int kay){
        String data;
        String data0="        Banka Sistem          \n";
        String data1="      Kullanıcı Raporu        \n\n";
        String data2=" Hesap No:          "+kayitlar[kay][0]+"\n";
        String data3=" Müşteri Adı:       "+kayitlar[kay][1]+"\n";
        String data4=" Son İşlem Tarihi   "+kayitlar[kay][2]+", "+kayitlar[kay][3]+", "+kayitlar[kay][4]+"\n";
        String data5=" Hesabındaki Para   "+kayitlar[kay][5]+"\n\n";
        String data6=" Tüm hakları size aittir. \n";

        data=data0+data1+data2+data3+data4+data5+data6;
        return  data;

    }

    void kayitYazdir(String kay){
        StringReader sr=new StringReader(kay);
        LineNumberReader lnr=new LineNumberReader(sr);
        Font ftipi=new Font("Times New Roman",Font.PLAIN,12);
        Properties p=new Properties();
        PrintJob pJob=getToolkit().getPrintJob(this,"Müşteri Bilgileri Yazdırılıyor ....",p);

        if(pJob != null){
            Graphics gr=pJob.getGraphics();
            if(gr != null){
                FontMetrics fm=gr.getFontMetrics(ftipi);
                int margin=20;
                int sayfaYuksekligi=pJob.getPageDimension().height-margin;
                int fontYuksekligi=fm.getHeight();
                int fontAraligi=fm.getDescent();
                int curHeight=margin;
                String sonrakiSatir;
                gr.setFont(ftipi);

                try{
                    do{
                        sonrakiSatir=lnr.readLine();
                        if(sonrakiSatir != null){
                            if((curHeight+fontYuksekligi)>sayfaYuksekligi){// Yeni Sayfa
                            gr.dispose();
                            gr=pJob.getGraphics();
                            curHeight=margin;
                        }
                        curHeight +=fontYuksekligi;
                        if(gr != null ){
                            gr.setFont(ftipi);
                            gr.drawString(sonrakiSatir,margin,curHeight-fontAraligi);
                        }
                    }
                    }while(sonrakiSatir != null);
                }catch (EOFException eof){}
                catch  (Throwable t){}
            }
            gr.dispose();
        }
        if(pJob != null);
            pJob.end();

    }

    private void uygulamadanCik(){
        try{
            // Çıkmak istiyormusun diye sordur.
            int cevap=JOptionPane.showConfirmDialog(this,
                    "Çıkmak istediğinize emin misiniz?",
                    "Bank Sistem",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);

            if(cevap == JOptionPane.YES_OPTION){
                setVisible(false);// Framei kapat
                dispose();
                System.exit(0);//Uygulamayı Kapat;

            }

        }catch (Exception e){}
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        for(int i=0;i<radio.length;i++)
        {
            //goruntuyuDegistir(i);
        }
    }
}
