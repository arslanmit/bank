import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 12.02.2014
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
public class Yatirilacak extends JInternalFrame implements ActionListener {
    private JPanel jpYat=new JPanel();
    private JLabel lbNo,lbAdi,lbTarih,lbYatirilacak;
    private JTextField txtNo,txtAdi,txtYatirilacak;
    private JComboBox cbGun,cbAy,cbYil;
    private JButton btnKaydet,btnIptal;

    private int kaySayisi=0;
    private int satir=0;
    private int toplam=0;
    private int curr;
    private int yatirilacak;

    // Dosyadan alınacak ve arraya çıkarılacak
    private String kayitlar[][]=new String [500][6];
    private FileInputStream fis;
    private DataInputStream dis;

    Yatirilacak(){

        super("Para Yatırma",false,true,false,true);

        setSize(335,235);

        jpYat.setLayout(null);

        lbNo=new JLabel("Hesap No:");
        lbNo.setForeground(Color.black);
        lbNo.setBounds(15,20,80,25);

        lbAdi=new JLabel("Müşteri Adı:");
        lbAdi.setForeground(Color.black);
        lbAdi.setBounds(15,55,80,25);

        lbYatirilacak=new JLabel("Yatırılacak Miktar");
        lbYatirilacak.setForeground(Color.black);
        lbYatirilacak.setBounds(15,125,80,25);

        lbTarih=new JLabel("Yatırılma Tarihi");
        lbTarih.setForeground(Color.black);
        lbTarih.setBounds(15,90,80,25);

        txtNo=new JTextField();
        txtNo.setHorizontalAlignment(JTextField.RIGHT);
        txtNo.setBounds(105,20,205,25);

        txtAdi=new JTextField();
        txtAdi.setEnabled(false);
        txtAdi.setBounds(105,55,205,25);

        txtYatirilacak=new JTextField();
        txtYatirilacak.setHorizontalAlignment(JTextField.RIGHT);
        txtYatirilacak.setBounds(105,125,205,25);

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

        // Butonları ayarlar
        btnKaydet=new JButton("Kaydet");
        btnKaydet.setBounds(20,165,120,25);
        btnKaydet.addActionListener(this);

        btnIptal=new JButton("İptal");
        btnIptal.setBounds(185,165,120,25);
        btnIptal.addActionListener(this);

        txtNo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if(txtNo.getText().equals("")){}
                else{
                    satir=0;
                    tools tool=new tools();
                    arrayDoldur();
                    kayitBul();
                }
            }
        });


        jpYat.add(lbNo);
        jpYat.add(txtNo);
        jpYat.add(lbAdi);
        jpYat.add(txtAdi);
        jpYat.add(lbTarih);
        jpYat.add(cbGun);
        jpYat.add(cbAy);
        jpYat.add(cbYil);
        jpYat.add(lbYatirilacak);
        jpYat.add(txtYatirilacak);
        jpYat.add(btnKaydet);
        jpYat.add(btnIptal);

        getContentPane().add(jpYat);
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
       kaySayisi=kayit;
   }

    void txtTemizle(){
        txtNo.setText("");
        txtAdi.setText("");
        txtYatirilacak.setText("");
        txtNo.requestFocus();
    }


    // Butonlar
    @Override
    public void actionPerformed(ActionEvent e) {
       Object obj =e.getSource();
       if(obj == btnKaydet){
           if(txtNo.getText().equals("")){
               JOptionPane.showMessageDialog(this,"Lütfen Müşterinin No'sunu giriniz","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
               txtNo.requestFocus();
           }
           else if(txtYatirilacak.getText().equals(""))
           {
               JOptionPane.showMessageDialog(this,"Lütfen yatırılan parayı giriniz","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
               txtYatirilacak.requestFocus();
           }else{

               kayitUpdate(); // Gelen değerlere göre kaydı update et.
           }
       }

    if(obj == btnIptal){
        txtTemizle();
        setVisible(false);
        dispose();
    }
    }

    public void kayitUpdate(){
        yatirilacak = Integer.parseInt(txtYatirilacak.getText());
        kayitlar[kaySayisi][0]=txtNo.getText();
        kayitlar[kaySayisi][1]=txtAdi.getText();
        kayitlar[kaySayisi][2]="" + cbGun.getSelectedItem();
        kayitlar[kaySayisi][3]="" + cbAy.getSelectedItem();
        kayitlar[kaySayisi][4]="" + cbYil.getSelectedItem();
        kayitlar[kaySayisi][5]="" + (curr+yatirilacak);
        dosyaGuncelle();
    }

    void dosyaGuncelle(){
        try{
            FileOutputStream fos=new FileOutputStream("Banka.dat");
            DataOutputStream dos=new DataOutputStream(fos);
            if(kayitlar != null )
            {
                for(int i=0;i<toplam;i++){
                    for(int c=0;c<6;c++){
                        dos.writeUTF(kayitlar[i][c]);
                        if(kayitlar[i][c]==null) break;
                    }
                }

                JOptionPane.showMessageDialog(this,"Güncelleme Başarılı","Bank Sistem",JOptionPane.PLAIN_MESSAGE);

                txtTemizle();
                dos.close();
                fos.close();
            }
        }catch (IOException ioe){
            JOptionPane.showMessageDialog(this,"Dosya ile ilgili bir sorun oluştu","Bank Sistem",JOptionPane.PLAIN_MESSAGE);

        }
    }

    void btnEnable(){
        txtNo.setEnabled(false);
        cbAy.setEnabled(false);
        cbGun.setEnabled(false);
        cbYil.setEnabled(false);
        btnKaydet.setEnabled(false);

    }

}
