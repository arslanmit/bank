import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 12.02.2014
 * Time: 13:35
 * To change this template use File | Settings | File Templates.
 */
public class YeniHesap extends JInternalFrame implements ActionListener{

    private JPanel jpInfo=new JPanel();
    private JLabel lblNo,lblAdi,lblTarih,lblYatirilan;
    private JTextField txtNo,txtAdi,txtYatirilan;
    private JComboBox cbAy,cbGun,cbYil;
    private JButton btnKaydet,btnIptal;

    private int count=0;
    private int satir=0;
    private int total=0;

    // Dosyadan gelecek değerleri array\'a alacağız
    private String kayitlar[][]=new String[500][6];

    //Kaydedilecek değerler.
    private String kaydedilecek[][]=new String[500][6];

    private FileInputStream fis;
    private DataInputStream dis;

    YeniHesap(){
        // başlık,genişleyebilir,kapatılabilir,küçültülebilir,ikonlaştırılabilir
      super("Yeni Hesap Aç",false,true,false,true);
      setSize(335,235);

      jpInfo.setBounds(0,0,500,115);
      jpInfo.setLayout(null);

      lblNo=new JLabel("Hesap No");
      lblNo.setForeground(Color.black);
      lblNo.setBounds(15,20,80,25);

      lblAdi=new JLabel("Adı");
      lblAdi.setForeground(Color.black);
      lblAdi.setBounds(15,55,80,25);

      lblTarih=new JLabel("Yatırdığı Tarih");
      lblTarih.setForeground(Color.black);
      lblTarih.setBounds(15,90,80,25);

      lblYatirilan=new JLabel("Yatırılan Miktar");
      lblYatirilan.setForeground(Color.black);
      lblYatirilan.setBounds(15,125,80,25);

      txtNo=new JTextField();
      txtNo.setHorizontalAlignment(JTextField.RIGHT);
      txtNo.setBounds(105,20,205,25);

      txtAdi=new JTextField();
      txtAdi.setBounds(105,55,205,25);

      txtYatirilan=new JTextField();
      txtYatirilan.setHorizontalAlignment(JTextField.RIGHT);
      txtYatirilan.setBounds(105,125,205,25);

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

      jpInfo.add(lblNo);
      jpInfo.add(txtNo);
      jpInfo.add(lblAdi);
      jpInfo.add(txtAdi);
      jpInfo.add(lblTarih);
      jpInfo.add(cbGun);
      jpInfo.add(cbAy);
      jpInfo.add(cbYil);

      jpInfo.add(lblYatirilan);
      jpInfo.add(txtYatirilan);

      jpInfo.add(btnKaydet);
      jpInfo.add(btnIptal);

      getContentPane().add(jpInfo);
      setVisible(true);
    }


    // Butonların işleri
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();

        if(obj == btnKaydet){
             if(txtNo.getText().equals("")){
                 JOptionPane.showMessageDialog(this,"Lütfen Kullanıcının Id'sini yazınız","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                 txtNo.requestFocus();
             }else if(txtAdi.getText().equals("")){
                 JOptionPane.showMessageDialog(this,"Lütfen müşterinin adını yazınız","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                 txtAdi.requestFocus();
             }else if(txtYatirilan.getText().equals("")){
                 JOptionPane.showMessageDialog(this,"Lütfen yatırılan parayı yazınız","Banka Sistemi",JOptionPane.PLAIN_MESSAGE);
                 txtYatirilan.requestFocus();
             }else{
                 arrayDoldur();//Hafızadaki verileri doldur.
                 kayitBul();
             }
        }
        if(obj == btnIptal){
            txtTemizle();// İçeriği Silsin
            setVisible(false);
            dispose();
        }
    }

    void arrayDoldur(){
        try{
            fis=new FileInputStream("Banka.dat");
            dis=new DataInputStream(fis);
           // 6 sutun...
            while(true){
                for(int i=0;i<6;i++){
                    kayitlar[satir][i]=dis.readUTF();
                }
                satir++;
            }

        }catch (Exception ex){
           total=satir;
           if(total == 0 ){}
           else{
               try{
                   dis.close();
                   fis.close();
               }catch (Exception ext){}
           }
        }
    }

    void kayitBul(){
        boolean bulundu=false;
        for(int x=0;x<total;x++){
            if(kayitlar[x][0].equals(txtNo.getText())){
                bulundu=true;
                JOptionPane.showMessageDialog(this,"Hesap No:"+txtNo.getText()+" zaten var","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
                txtTemizle();
                break;
            }
        }

        if(bulundu == false){
            arrayKaydet();
        }
    }

    void arrayKaydet(){
        kaydedilecek[count][0]=txtNo.getText();
        kaydedilecek[count][1]=txtAdi.getText();
        kaydedilecek[count][2]=""+cbGun.getSelectedItem();
        kaydedilecek[count][3]=""+cbAy.getSelectedItem();
        kaydedilecek[count][4]=""+cbYil.getSelectedItem();
        kaydedilecek[count][5]=txtYatirilan.getText();
        dosyayaKaydet();
        count++;
    }

    void dosyayaKaydet(){
        try{
            FileOutputStream fos=new FileOutputStream("Banka.dat",true);
            DataOutputStream dos=new DataOutputStream(fos);

            dos.writeUTF(kaydedilecek[count][0]);
            dos.writeUTF(kaydedilecek[count][1]);
            dos.writeUTF(kaydedilecek[count][2]);
            dos.writeUTF(kaydedilecek[count][3]);
            dos.writeUTF(kaydedilecek[count][4]);
            dos.writeUTF(kaydedilecek[count][5]);

            JOptionPane.showMessageDialog(this,"Başarıyla Kaydedildi","Banka Sistem", JOptionPane.PLAIN_MESSAGE);
            txtTemizle();
            dos.close();
            fos.close();
        }
        catch (IOException ioe){
            JOptionPane.showMessageDialog(this,"Problem Oluştu","Banka Sistem",JOptionPane.PLAIN_MESSAGE);
        }
    }

    void txtTemizle(){
        txtNo.setText("");
        txtAdi.setText("");
        txtYatirilan.setText("");
        txtNo.requestFocus();
    }


}
