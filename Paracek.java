import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 18.02.2014
 * Time: 09:11
 * To change this template use File | Settings | File Templates.
 */
public class Paracek extends JInternalFrame implements ActionListener{
    private JPanel jpCekilecek=new JPanel();
    private JLabel lbNo,lbAdi,lbTarih,lbCekilecek;
    private JTextField txtNo,txtAdi,txtSoyadi,txtCekilecek;
    private JComboBox cbAy,cbGun,cbYil;
    private JButton btnKaydet,btnIptal;

    private int kaySay=0;
    private int satir=0;
    private int toplam=0;
    private int curr;
    private int cekilecek;

    // Verileri almak için
    private String kayitlar[][]=new String[500][6];

    private FileInputStream fis;
    private DataInputStream dis;

    Paracek(){
        super("Para Çek",false,true,false,true);
        setSize(335,235);
        jpCekilecek.setLayout(null);
        lbNo=new JLabel("Hesap no:");
        lbNo.setForeground(Color.black);
        lbNo.setBounds(15,20,80,25);

        lbAdi=new JLabel("Adı");
        lbAdi.setForeground(Color.black);
        lbAdi.setBounds(15,55,80,25);

        lbTarih=new JLabel("Tarih:");
        lbTarih.setForeground(Color.black);
        lbTarih.setBounds(15,90,80,25);

        lbCekilecek=new JLabel("Miktar");
        lbCekilecek.setForeground(Color.black);
        lbCekilecek.setBounds(15,125,80,25);

        txtNo=new JTextField();
        txtNo.setHorizontalAlignment(JTextField.RIGHT);
        // Hesap no'yu kontol et.
        txtNo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtNo.getText().equals("")){}
                else{
                    satir=0;
                    arrayDoldur();
                    kayitBul();
                }
            }
        });
        txtNo.setBounds(105,20,205,25);

        txtAdi=new JTextField();
        txtAdi.setEnabled(false);
        txtAdi.setBounds(105,55,205,25);
        txtCekilecek=new JTextField();
        txtCekilecek.setHorizontalAlignment(JTextField.RIGHT);
        txtCekilecek.setBounds(105,125,205,25);

        txtCekilecek.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c=e.getKeyChar();
                if(!((Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE)))){
                    getToolkit().beep();
                    e.consume();
                }
            }
        });


        //Tarih olayları
        String aylar[]={"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};
        cbAy=new JComboBox(aylar);
        cbGun=new JComboBox();
        cbYil=new JComboBox();

        // Günleri ekle;
        for(int i=1;i<31;i++)
        {
            String gunler=""+i;
            cbGun.addItem(gunler);
        }

        for(int i=2000;i<=2015;i++){
            String yillar=""+i;
            cbYil.addItem(yillar);
        }
        //Controlleri yerlerini yaz
        cbAy.setBounds(105,90,92,25);
        cbGun.setBounds(202,90,43,25);
        cbYil.setBounds(250,90,60,25);

        // Butonları düzenle
        btnKaydet=new JButton("Kaydet");
        btnKaydet.setBounds(20,165,120,25);
        btnKaydet.addActionListener(this);

        btnIptal=new JButton("İptal");
        btnIptal.setBounds(185,165,120,25);
        btnIptal.addActionListener(this);

        // Kontrolleri panele ekle
        jpCekilecek.add(lbNo);
        jpCekilecek.add(txtNo);
        jpCekilecek.add(lbAdi);
        jpCekilecek.add(txtAdi);
        jpCekilecek.add(lbTarih);
        jpCekilecek.add(cbGun);
        jpCekilecek.add(cbAy);
        jpCekilecek.add(cbYil);
        jpCekilecek.add(lbCekilecek);
        jpCekilecek.add(txtCekilecek);
        jpCekilecek.add(btnKaydet);
        jpCekilecek.add(btnIptal);

        getContentPane().add(jpCekilecek);
        //populateArray();
        setVisible(true);

    }


    void arrayDoldur(){
        try{
            fis=new FileInputStream("Banka.dat");
            dis=new DataInputStream(fis);
            while(true){
                for(int i=0;i<6;i++){
                    kayitlar[satir][i]=dis.readUTF();
                }
                satir++;
            }
        }catch (Exception ex){
            toplam=satir;
            if(toplam == 0){
                JOptionPane.showMessageDialog(null,"Kayıt dosyası boş","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                btnEnable();
            }else{
                try{
                    dis.close();
                    fis.close();
                }catch (Exception exp){}
            }
        }
    }

    void kayitBul(){
        boolean bulundu=false;
        for(int x=0;x<toplam;x++){
            if(kayitlar[x][0].equals(txtNo.getText())){
                bulundu=true;
                kaydiGoster(x);
                break;
            }
        }
        if(bulundu == false){
            String str=txtNo.getText();
            txtTemizle();
            JOptionPane.showMessageDialog(this,"Hesap No:"+str+" hesabı bulunmamaktadır","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
        }

    }
    void kaydiGoster(int kayit){
        txtNo.setText(kayitlar[kayit][0]);
        txtAdi.setText(kayitlar[kayit][1]);
        curr=Integer.parseInt(kayitlar[kayit][5]);
        kaySay=kayit;
    }

    void txtTemizle(){
        txtNo.setText("");
        txtAdi.setText("");
        txtCekilecek.setText("");
        txtNo.requestFocus();
    }
    void btnEnable () {
        txtNo.setEnabled (false);
        cbAy.setEnabled (false);
        cbGun.setEnabled (false);
        cbYil.setEnabled (false);
        txtCekilecek.setEnabled (false);
        btnKaydet.setEnabled (false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Çekilecek para olayları
        Object obj=e.getSource();

        if(obj == btnKaydet){
            if(txtNo.getText().equals("")){
                JOptionPane.showMessageDialog(this,"Lütfen Müşteri ID'yi giriniz","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                txtNo.requestFocus();
            }else if(txtCekilecek.getText().equals("")){
                JOptionPane.showMessageDialog(this,"Lütfen Çekilecek Miktarı Giriniz","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                txtCekilecek.requestFocus();
            }
            else{
                cekilecek=Integer.parseInt(txtCekilecek.getText()) ;
                if(curr==0){
                    JOptionPane.showMessageDialog(this,txtAdi.getText() + " hesabında para bulunmamaktadır","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                    txtTemizle();

                }else{
                   kayitUpdate();
                }
            }
        }

    }

    public void kayitUpdate(){
        kayitlar[kaySay][0]=txtNo.getText();
        kayitlar[kaySay][1]=txtAdi.getText();
        kayitlar[kaySay][2]="" + cbGun.getSelectedItem();
        kayitlar[kaySay][3]="" + cbAy.getSelectedItem();
        kayitlar[kaySay][4]="" + cbYil.getSelectedItem();
        kayitlar[kaySay][5]="" + (curr-cekilecek);
        dosyaGuncelle();
    }


    //Function use to Save Records to File After editing the Record of User Choice.
    public void dosyaGuncelle () {

        try {
            FileOutputStream fos = new FileOutputStream ("Banka.dat");
            DataOutputStream dos = new DataOutputStream (fos);
            if (kayitlar != null) {
                for (int i = 0; i < toplam; i++) {
                    for (int c = 0; c < 6; c++) {
                        dos.writeUTF (kayitlar[i][c]);
                        if (kayitlar[i][c] == null) break;
                    }
                }
                JOptionPane.showMessageDialog (this, "Hesap Başarıyla Güncellenmiştir",
                        "Bank Sistem", JOptionPane.PLAIN_MESSAGE);
                txtTemizle ();
                dos.close();
                fos.close();
            }
        }
        catch (IOException ioe) {
            JOptionPane.showMessageDialog (this, "Dosya ile ilgili bir program bulunmaktadır.",
                    "Bank Sistem", JOptionPane.PLAIN_MESSAGE);
        }

    }
}
