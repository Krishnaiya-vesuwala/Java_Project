/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EJB;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.File;

/**
 *
 * @author krishnaiya
 */
public class QRCodeManager {
     public void qrcode(){
    
        try{
            String wardID="1";
            String data = "https://untearable-yasmin-intermetameric.ngrok-free.dev/Government_Grievance_Redressal_System/ComplaintRegister.jsf?wardID="+wardID;
            
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 300, 300);
            
            File file = new File("C:/qr_codes/" + wardID + ".png");
            
            MatrixToImageWriter.writeToFile(matrix, "PNG", file);
            System.out.println("QR Generated for "+ wardID);
            
        }catch(Exception e){
           e.printStackTrace();
        }
    }
}
