/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import java.util.Date;

/**
 *
 * @author nvj438
 */
public class FastID extends Commonclass{
    static byte[] tagMask = { (byte)0x11, 0x11 };
    PreFilters prefilter = new PreFilters();
    PreFilters.PreFilter filter = prefilter.new PreFilter();
    Antennas antenna = reader.Config.Antennas;
    Antennas.SingulationControl antennaSingulation = antenna.new SingulationControl();
    
    public FastID()
    {
         try
        {
            mystreamLog = new FileOutputStream("Java_API_FastID_Log.html");
            mystreamResult=new FileOutputStream("Java_API_FastID_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing FastID------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "StateAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
    }
     private void addSR(byte[] pattern,MEMORY_BANK mBank,int bitOffset,int patternbitCount,STATE_AWARE_ACTION action,TARGET target )
    {
        try
        {
//            for(short antenna = 1; antenna <= reader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
//            {
                PreFilters pfs = new PreFilters();
                PreFilters.PreFilter pf1  = pfs.new PreFilter();
                pf1.setAntennaID((short)0);
                pf1.setBitOffset(bitOffset);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
                pf1.StateAwareAction.setStateAwareAction(action);
                pf1.StateAwareAction.setTarget(target);
                pf1.setMemoryBank(mBank);
                pf1.setTagPattern(pattern);
                pf1.setTagPatternBitCount(patternbitCount);
                reader.Actions.PreFilters.add(pf1);
                psLog.println("<br>State Aware Filter Action with :"+"<br>BitOffset: "+pf1.getBitOffset()+"<br>TagPatternBitCount: "+pf1.getTagPatternBitCount()+"<br>TagPattern: "+pf1.getTagPattern().toString()+"<br>MemoryBank: "+pf1.getMemoryBank());
                psLog.println("<br>FilterAction: "+pf1.getFilterAction()+"<br>StateAwareAction: "+pf1.StateAwareAction.getStateAwareAction()+"<br>Target: "+pf1.StateAwareAction.getTarget());
            //}
            psLog.println("\nPrefilter added successfully");
        }
        catch(InvalidUsageException exp)
        {
            System.out.println(""+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(""+opexp.getStatusDescription());
        }

    }
     private void setSingulation(SESSION session,SL_FLAG SL,INVENTORY_STATE inv)
    {
        try
        {
            for (short antenna = 1; antenna <= reader.ReaderCapabilities.getNumAntennaSupported(); antenna++)
            {
                /* set singulation control paraeters and verify truncate for various options*/
                antennaSingulation.setSession(session);
                antennaSingulation.setTagPopulation((short) 100);
                antennaSingulation.setTagTransitTime((short) 0);
                antennaSingulation.Action.setInventoryState(inv);
                antennaSingulation.Action.setPerformStateAwareSingulationAction(true);
                antennaSingulation.Action.setSLFlag(SL);
                reader.Config.Antennas.setSingulationControl(antenna, antennaSingulation);
                
            }
            psLog.println("<br>Singulation:  <br>"+"Session: "+antennaSingulation.getSession()+"<br>InventoryState: "+antennaSingulation.Action.getInventoryState()+"<br>SLFlag: "+antennaSingulation.Action.getSLFlag());
        }
        catch (InvalidUsageException exp)
        {
            System.out.println("" + exp.getVendorMessage());
        }
        catch (OperationFailureException opexp)
        {
            System.out.println("" + opexp.getStatusDescription());
        }
    }
     public void Test_SimpleInventory()
    {
//          try
//        {
//        mystreamLog=new FileOutputStream("JavaAPI_Inventory_Log.html");
//        mystreamResult=new FileOutputStream("JavaAPI_Inventory_Result.txt");
//        psLog = new PrintStream(mystreamLog);
//        psResult = new PrintStream(mystreamResult);
//        }
//        catch(FileNotFoundException e)
//        {psLog.println(""+e.getMessage());}
          
     try
            {

            TagStorageSettings tagStorageSet=new TagStorageSettings();
            //tagStorageSet.setTagFields(TAG_FIELD.)
            reader.Actions.Inventory.perform();
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();
            //readParams.
            TagData tagdata[]=reader.Actions.getReadTags(1000);
            //

            System.out.print("\nNo.0f tags reported in this inventory "+tagdata.length);
            //reader.Actions.TagAccess.readEvent(readParams, null, null);
            Thread.sleep(500);
            String[] data = {};String[] dataunique = {};
            for(int i=0;i<tagdata.length;i++)
            {
                
                //System.out.print("\n"+tagdata[i].getTagID());
                psLog.print("\n"+tagdata[i].getTagID()+"<br>");
                System.out.print("\n"+tagdata[i].getMemoryBank());
                data[i+1] = tagdata[i].getMemoryBankData().toString();

            }
            if(tagdata == null)
            {
                psLog.print("\nThe tags are not reported");
            }
            for (int i = 0; i < data.length; i++) {
             if (i == data.length-1) {
                dataunique[i] = data[i];
             }
             else if (data[i] != data[i+1]) {
               dataunique[i] = data[i];
             }
            }
            System.out.println(dataunique);


                
            }
        catch(InvalidUsageException exp) {
            System.out.print("\nInvalidUsageException"+exp.getInfo());
           psLog.println("\nInvalidUsageException"+exp.getInfo());
        }
        catch(OperationFailureException exp) {

            System.out.print("\nOperationFailureException"+exp.getMessage());
            psLog.println("\nOperationFailureException"+exp.getMessage());

                }
            catch(InterruptedException e)
            {
                System.out.print("\nInterruptedException"+e.getMessage());
                psLog.println("\nInterruptedException"+e.getMessage());
            }
        }
    
    
        public void Test_FastID()
        {
            psLog.println("<html><br>");
            psLog.println("<body><br>");

            AntennaPowersettings();
            successCount = 0;
            failureCount = 0;
            TestNo = 101;
            SubNo = 0;
            
            try
            {
                reader.Config.resetFactoryDefaults();
                reader.Actions.PreFilters.deleteAll();
                addSR(tagMask, MEMORY_BANK.MEMORY_BANK_EPC, 32,16,STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B,TARGET.TARGET_INVENTORIED_STATE_S1);
                setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);
                reader.Actions.purgeTags();
                Test_SimpleInventory();
            }
            catch(NullPointerException exp)
        {
            System.out.print("\nNullPointerException"+exp.getMessage());
           psLog.println("\nNullPointerException"+exp.getMessage());
        }
        catch(InvalidUsageException exp) {
            System.out.print("\nInvalidUsageException"+exp.getInfo());
           psLog.println("\nInvalidUsageException"+exp.getInfo());
        }
        catch(OperationFailureException exp) {

            System.out.print("\nOperationFailureException"+exp.getMessage());
            psLog.println("\nOperationFailureException"+exp.getMessage());

        }

            psLog.close();
            psResult.close();
            psSummary.println("JavaAPI:FastID_Testcases" + ":" + successCount + ":" + failureCount + ":0");
            psLog.println("</html>\n");
            psLog.println("</body>\n");

        }
    
    
    
    
}

