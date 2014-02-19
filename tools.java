import javax.swing.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: syanlik
 * Date: 12.02.2014
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class tools {

    private  FileInputStream fis;
    private  DataInputStream dis;
    private String kayitlar[][] = new String [500][6];
    private int rows = 0;
    private int total=0;
    private boolean durum=true;
    String[][] arrayDoldur(){
        try{
            fis=new FileInputStream("Banka.dat");
            dis=new DataInputStream(fis);
            while(true){
                for(int i=0;i<6;i++){
                    kayitlar[rows][i]=dis.readUTF();
                }
                rows++;
            }
        }catch (Exception ex){
            total=rows;
            durum=false;
            if(total == 0){
                JOptionPane.showMessageDialog(null,"Kayıt dosyası boş","Bank Sistem",JOptionPane.PLAIN_MESSAGE);
            }else{
                try{
                    dis.close();
                    fis.close();
                }catch (Exception exp){}
            }
        }

        return kayitlar;

    }
}
