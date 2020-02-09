/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.reader;

/**
 *
 * @author NVJ438
 */
public class EAS extends Commonclass {

    public int TagNum = 0;
    

    public void Test_EAS() {


        GetEvents getEvents = new GetEvents(reader, psLog);
        //reader.Events.setAccessStartEvent(true);
            //reader.Events.setAccessStopEvent(true);
        reader.Events.addEventsListener(getEvents);
        reader.Events.setTagReadEvent(true);
        reader.Events.setEASAlarmEvent(true);
        String tagId =  "F00D0001F00D0002F00D0003";//"CAB1CAB2CAB3CAB4CAB5CAB6";//"EBC1EBC2EBC3EBC4EBC5EBC6";//
//FACEFACEFACEFACEFACEFACE 000000000000000000000000
        TagAccess tagaccess = new TagAccess();
        NXP nxp = new NXP(tagaccess);
        NXP.SetEASParams setEASParams = nxp.new SetEASParams();
        NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
        NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();
        long acc = setEASParams.getAccessPassword();
        setEASParams.setAccessPassword(00000001);
        System.out.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        System.out.println("isEASSet: " + setEASParams.isEASSet());
        Inventory in = new Inventory();
        try {

            
//            reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams, null);
//            System.out.println("Set EAS done");
//            reader.Actions.TagAccess.NXP.performEASScan();
//            Thread.sleep(10000);
//            reader.Actions.TagAccess.NXP.stopEASScan();
            reader.Actions.purgeTags();
            Test_SimpleInventory();
            readProtectParams.setAccessPassword(00000001);
            reader.Actions.TagAccess.NXP.readProtectWait(tagId, readProtectParams, null);
            System.out.println("Set quiet done");
            int[] pass = new int[1]; int[] fail = new int[1];      
           reader.Actions.TagAccess.getLastAccessResult(pass, fail);
           
           System.out.println("\n PassCount = "+pass[0]+"     FailCount = "+fail[0]);
            reader.Actions.purgeTags();
            Test_SimpleInventory();
            resetReadProtectParams.setAccessPassword(00000001);
            reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtectParams, null);
            System.out.println("Reset quiet done");
            //int[] pass = new int[1]; int[] fail = new int[1];      
           reader.Actions.TagAccess.getLastAccessResult(pass, fail);
           
           System.out.println("\n PassCount = "+pass[0]+"     FailCount = "+fail[0]);
      
            Thread.sleep(5000);
            //reader.Config.resetFactoryDefaults();
            reader.Actions.purgeTags();
            Test_SimpleInventory();
            
        } catch (InvalidUsageException exp) {
            System.out.print("\nInvalidUsageException" + exp.getInfo());
            System.out.println("VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            System.out.print("\nOperationFailureException" + exp.getMessage());
            System.out.println("VendorMessage" + exp.getVendorMessage());
        }
        catch (InterruptedException e) {
        }

    }

    public void Test_SimpleInventory() {

        try {
            reader.Actions.Inventory.perform();
            System.out.print("<br>Here----------------1");
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();
             System.out.print("<br>Here----------------2");
            TagData tagdata[] = reader.Actions.getReadTags(50);
             System.out.print("<br>Here----------------3");
            TagNum = tagdata.length;
            if (tagdata != null) {
                for (int i = 0; i < tagdata.length; i++) {
                    //System.out.print("\n"+tagdata[i].getTagID());
                    System.out.print("\n" + tagdata[i].getTagID());
                    //((DefaultTableModel)jTable1.getModel()).insertRow(0, new Object[]{tagdata[i].getTagID(),tagdata[i].getPC(),tagdata[i].getPeakRSSI(),count});

                }
            } else {
                System.out.print("<br>No tags read");
            }

        } catch (InvalidUsageException exp) {
            System.out.print("\nInvalidUsageException--" + exp.getInfo());
            //psLog.println("\nInvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {

            System.out.print("\nOperationFailureException--" + exp.getMessage());
            System.out.print("\nOperationFailureException--" + exp.getStatusDescription());
            System.out.print("\nOperationFailureException--" + exp.getVendorMessage());
            //psLog.println("\nOperationFailureException" + exp.getMessage());

        } catch (InterruptedException e) {
            System.out.print("\nInterruptedException--" + e.getMessage());
            //psLog.println("\nInterruptedException" + e.getMessage());
        }
    }
}
