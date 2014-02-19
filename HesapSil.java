import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 19.02.2014
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
public class HesapSil extends JInternalFrame implements ActionListener {
    private JPanel jpSil=new JPanel();
    private JLabel lbNo,lbAdi,lbTarih,lbKalan;
    private JTextField txtNo,txtAdi,txtTarih,txtKalan;
    private JButton btnKaydet,btnIptal;
    private int kaySay=0;
    private int satir=0;
    private int toplam=0;

    //Array
    private String kayitlar[][]=new String[500][6];

    private FileInputStream fis;
    private DataInputStream dis;

    HesapSil(){
       super("Hesap Sahibini Sil",false,true,false,true);
       setSize(350,235);

       jpSil.setLayout(null);

       lbNo=new JLabel("Account No:");
       lbNo.setForeground(Color.black);
       lbNo.setBounds(15,20,80,25);

       lbAdi=new JLabel("Adı");
       lbAdi.setForeground(Color.black);
       lbAdi.setBounds(15,55,90,25);

       lbTarih=new JLabel("Son İşlem tarihi:");
       lbTarih.setForeground(Color.black);
       lbTarih.setBounds(15,90,100,25);

       lbKalan=new JLabel("Kalan:");
       lbKalan.setForeground(Color.black);
       lbKalan.setBounds(15,125,80,25);

       txtNo=new JTextField();
       txtNo.setHorizontalAlignment(JTextField.RIGHT);
       txtNo.setBounds(125,20,200,25);

       txtAdi=new JTextField();
       txtAdi.setEnabled(false);
       txtAdi.setBounds(125,55,200,25);

        txtTarih=new JTextField();
        txtTarih.setEnabled(false);
        txtTarih.setBounds(125,90,200,25);

        txtKalan=new JTextField();
        txtKalan.setEnabled(false);
        txtKalan.setHorizontalAlignment(JTextField.RIGHT);
        txtKalan.setBounds(125,125,200,25);

        // Butonları ayarla
        btnKaydet=new JButton("Sil");
        btnKaydet.setBounds(20,165,120,25);
        btnKaydet.addActionListener(this);

        btnIptal=new JButton("İptal");
        btnIptal.setBounds(200,165,120,25);
        btnIptal.addActionListener(this);


        jpSil.add(lbNo);
        jpSil.add(txtNo);
        jpSil.add(lbAdi);
        jpSil.add(txtAdi);
        jpSil.add(lbTarih);
        jpSil.add(txtTarih);
        jpSil.add(lbKalan);
        jpSil.add(txtKalan);
        jpSil.add(btnKaydet);
        jpSil.add(btnIptal);

        txtNo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

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


        getContentPane().add(jpSil);
        arrayDoldur();
        setVisible(true);





    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj=e.getSource();
        if(obj == btnKaydet){
            if(txtNo.getText().equals("")){
                JOptionPane.showMessageDialog(this,"Lütfen Kullanıcı Id'yi giriniz","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                txtNo.requestFocus();
            }else{
                siliver();
            }
        }

        if(obj == btnIptal){
            txtTemizle();
            setVisible(false);
            dispose();
        }



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
        txtTarih.setText(kayitlar[kayit][2]+","+kayitlar[kayit][3]+","+kayitlar[kayit][4]);
        txtKalan.setText(kayitlar[kayit][5]);
        kaySay=kayit;
    }

    void siliver(){
        try{
            int q=JOptionPane.showConfirmDialog(this,txtAdi.getText()+" kullanıcısını silmek istediğinize emin misiniz?","Bank Sistem",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(q == JOptionPane.YES_OPTION){
          kayitSil();
        }else if(q == JOptionPane.NO_OPTION){}

        }catch (Exception e){}
    }
       // array içerisindekini sil
    void kayitSil(){
        try{
            if(kayitlar != null){
                for(int i=kaySay;i<toplam;i++){
                    for(int r=0;r<6;r++){
                        kayitlar[i][r]=kayitlar[i+1][r];
                        if(kayitlar[i][r]==null)break;
                    }
                }
                toplam=toplam-1;
                dosyaSil();
            }
        }catch (ArrayIndexOutOfBoundsException ex){}
    }

    // Dosyadakini sil
    void dosyaSil(){
        try{
            FileOutputStream fos=new FileOutputStream("Banka.dat");
            DataOutputStream dos=new DataOutputStream(fos);
            if(kayitlar != null){
                for(int i=0;i<toplam;i++){
                    for(int r=0;r<6;r++){
                        dos.writeUTF(kayitlar[i][r]);
                        if(kayitlar[i][r] == null )break;
                    }
                }
                JOptionPane.showMessageDialog(this,"Veri Silindi","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
                txtTemizle();
            }else{}
            dos.close();
            fos.close();
        }catch (IOException ioe){
            JOptionPane.showMessageDialog(this,"Dosya okumada problem var","Bank Sistem",JOptionPane.PLAIN_MESSAGE);

        }
    }

    void txtTemizle(){
        txtNo.setText("");
        txtAdi.setText("");
        txtTarih.setText("");
        txtKalan.setText("");
        txtNo.requestFocus();
    }

    void btnEnable () {
        txtNo.setEnabled (false);


    }
}
