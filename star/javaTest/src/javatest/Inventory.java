/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
import java.util.Date;
import static javatest.Commonclass.logText;
import static javatest.Commonclass.mystreamLog;
import static javatest.Commonclass.mystreamResult;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psResult;
import java.util.ArrayList;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;
/**
 *
 * @author NVJ438
 */
public class Inventory extends Commonclass{
    static byte[] tagMask = { 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66 };
    String matchingTag = "111122223333444455556666";
            
    PreFilters prefilter = new PreFilters();
    PreFilters.PreFilter filter = prefilter.new PreFilter();
    Antennas antenna = reader.Config.Antennas;
    Antennas.SingulationControl antennaSingulation = antenna.new SingulationControl();
    MEMORY_BANK mb = MEMORY_BANK.MEMORY_BANK_EPC;
    PreFilters pfs = new PreFilters();
    PreFilters.PreFilter pf1  = pfs.new PreFilter();
    int pass = 0;int fail = 0;
    
    public void TestInventory_Default(RFIDReader reader)
    {
        try{
                mystreamLog = new FileOutputStream("Java_API_Filters_Default_Log.html");
                psLog = new PrintStream(mystreamLog);

                reader.Actions.PreFilters.deleteAll();
                filter.setAntennaID((short)0);
                filter.setBitOffset(32);
                filter.setFilterAction(FILTER_ACTION.FILTER_ACTION_DEFAULT);
                 //filter.StateAwareAction.setTarget(TARGET.TARGET_INVENTORIED_STATE_S0);
                filter.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
                filter.setTagPattern(tagMask);
                int tagPatternBitCount = tagMask.length*8;
                filter.setTagPatternBitCount(tagPatternBitCount);
//                filter.StateAwareAction.setStateAwareAction(STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B);
                Antennas.SingulationControl singControl = null;
                //singControl.Action.
//                singControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_AB_FLIP);
                reader.Actions.PreFilters.add(filter);
                psLog.println("Prefilter added successfully");
            reader.Events.setAttachTagDataWithReadEvent(true);
            reader.Actions.Inventory.perform();
            Thread.sleep(10000);
            reader.Actions.Inventory.stop();
            TagData tagdata[]=reader.Actions.getReadTags(100);
            for(int i=0;i<tagdata.length;i++)
            {
                //System.out.print("\n"+tagdata[i].getTagID());
                psLog.println("<br>"+tagdata[i].getTagID());
                        //((DefaultTableModel)jTable1.getModel()).insertRow(0, new Object[]{tagdata[i].getTagID(),tagdata[i].getPC(),tagdata[i].getPeakRSSI(),count});

            }
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

        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
    }
     public void TestInventory_aware(RFIDReader reader)
    {
        successCount = 0;
        failureCount = 0;
        logText = "StateAware-filter";
         try
        {
            mystreamLog = new FileOutputStream("JavaAPI_State_Aware_Filters_Log.html");
            mystreamResult=new FileOutputStream("JavaAPI_State_Aware_Filters_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing StateAware Filters------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            //logText = "StateAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        try{
                TestNo = 1;               
                
                TARGET[] target = {TARGET.TARGET_INVENTORIED_STATE_S0,TARGET.TARGET_INVENTORIED_STATE_S1,TARGET.TARGET_INVENTORIED_STATE_S2,TARGET.TARGET_INVENTORIED_STATE_S3,TARGET.TARGET_SL};
                SESSION[] sessions = {SESSION.SESSION_S0,SESSION.SESSION_S1,SESSION.SESSION_S2,SESSION.SESSION_S3};
                STATE_AWARE_ACTION[] actions  = {STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B,STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_B,STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A2BB2A,STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_A,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_A2BB2A};
                INVENTORY_STATE[] state = {INVENTORY_STATE.INVENTORY_STATE_A,INVENTORY_STATE.INVENTORY_STATE_B,INVENTORY_STATE.INVENTORY_STATE_AB_FLIP};
                STATE_AWARE_ACTION[] actionsSL  = {STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_DSRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NEG_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_ASRT_SL,STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_NEG_SL};
                SL_FLAG[] slflag = {SL_FLAG.SL_ALL,SL_FLAG.SL_FLAG_ASSERTED,SL_FLAG.SL_FLAG_DEASSERTED};
                
                for(int i=0; i<target.length; i++)
                {
                    if(target[i] == TARGET.TARGET_SL)
                    {
                        for(int j =0; j<actionsSL.length; j++)
                        {
                            try{
                                    SubNo = 1;
                                    
                                    reader.Config.resetFactoryDefaults();
                                    reader.Actions.PreFilters.deleteAll();
                                    addSR(tagMask, mb, 32,16,actionsSL[j],target[i]);
                                    for(int k = 0; k<slflag.length; k++)
                                    {
                                        FormTestID(TestNo,SubNo++,"STATE_AWARE");
                                        setSingulation(SESSION.SESSION_S0, slflag[k], INVENTORY_STATE.INVENTORY_STATE_AB_FLIP);
                                        reader.Actions.purgeTags();
                                        Test_SimpleInventory();
                                    }
                                    TestNo++;
                                }
                                catch(NullPointerException exp)
                                {
//                                    System.out.print("\nNullPointerException"+exp.getMessage());
                                   psLog.println("\nNullPointerException"+exp.getMessage());
                                }
                                catch(InvalidUsageException exp) {
//                                    System.out.print("\nInvalidUsageException"+exp.getInfo());
                                   psLog.println("\nInvalidUsageException"+exp.getInfo());
                                }
                                catch(OperationFailureException exp) {

//                                    System.out.print("\nOperationFailureException"+exp.getMessage());
                                    psLog.println("\nOperationFailureException"+exp.getMessage());

                                }    
                            
                        }

                    }else{
                        for(int j =0; j<actions.length; j++)
                        {
                            try{
                                    SubNo = 1;
                                    reader.Config.resetFactoryDefaults();
                                    reader.Actions.PreFilters.deleteAll();
                                    addSR(tagMask, MEMORY_BANK.MEMORY_BANK_EPC, 32,16,actions[j],target[i]);
                                    for(int k = 0; k<state.length; k++)
                                    {
                                        FormTestID(TestNo,SubNo++,"STATE_AWARE");
                                        setSingulation(sessions[i], SL_FLAG.SL_FLAG_DEASSERTED, state[k]);
                                        reader.Actions.purgeTags();
                                        Test_SimpleInventory();
                                    }
                                    TestNo++;
                                }
                                catch(NullPointerException exp)
                                {
//                                    System.out.print("\nNullPointerException"+exp.getMessage());
                                   psLog.println("\nNullPointerException"+exp.getMessage());
                                }
                                catch(InvalidUsageException exp) {
//                                    System.out.print("\nInvalidUsageException"+exp.getInfo());
                                   psLog.println("\nInvalidUsageException"+exp.getInfo());
                                }
                                catch(OperationFailureException exp) {

//                                    System.out.print("\nOperationFailureException"+exp.getMessage());
                                    psLog.println("\nOperationFailureException"+exp.getMessage());

                                }


                        }
                    }
               }
            psLog.close();
            psResult.close();
            psSummary.println("JavaAPI:State Aware test cases: Single filter: " + successCount + ":" + failureCount + ":" + "0");
            psLog.println("</html>\n");
            psLog.println("</body>\n");
        }
        catch(NullPointerException exp)
        {
            System.out.print("\nNullPointerException"+exp.getMessage());
           psLog.println("\nNullPointerException"+exp.getMessage());
        }
        
//        
    }
      public void TestInventory_Unaware(RFIDReader reader)
    {
        successCount = 0;
        failureCount = 0;
        logText = "StateUnAware-filter";
        //byte[] tagMask = { (byte)0xA2, (byte)0x2F, (byte)0xA2, (byte)0x2F, (byte)0xA2, (byte)0x2F };
         try
        {
            mystreamLog = new FileOutputStream("JavaAPI_State_UnAware_Filters_Log.html");
            mystreamResult=new FileOutputStream("JavaAPI_State_UnAware_Filters_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing StateUnAware Filters------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            //logText = "StateAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        try{
                TestNo = 182;                
                STATE_UNAWARE_ACTION[] UnAwareaction  = {STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT,STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT,STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_UNSELECT,STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT,STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT,STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT};                
          
                for(int i=0; i<UnAwareaction.length; i++)
                {
                      try{
                           
                            FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
                            reader.Config.resetFactoryDefaults();
                            reader.Actions.PreFilters.deleteAll();
                            addUnawareSeletRecord(tagMask, MEMORY_BANK.MEMORY_BANK_EPC, 32, 96, UnAwareaction[i]);
                            reader.Actions.purgeTags();
                            Test_SimpleInventory();
                            
                        }
                        catch(NullPointerException exp)
                        {
//                                    System.out.print("\nNullPointerException"+exp.getMessage());
                           psLog.println("\nNullPointerException"+exp.getMessage());
                        }
                        catch(InvalidUsageException exp) {
//                                    System.out.print("\nInvalidUsageException"+exp.getInfo());
                           psLog.println("\nInvalidUsageException"+exp.getInfo());
                        }
                        catch(OperationFailureException exp) {

//                                    System.out.print("\nOperationFailureException"+exp.getMessage());
                            psLog.println("\nOperationFailureException"+exp.getMessage());

                        }                       
               }
            psLog.close();
            psResult.close();
            psSummary.println("JavaAPI:State Un-Aware test cases:" + successCount + ":" + failureCount + ":" + "0");
            psLog.println("</html>\n");
            psLog.println("</body>\n");
        }
        catch(NullPointerException exp)
        {
            System.out.print("\nNullPointerException"+exp.getMessage());
           psLog.println("\nNullPointerException"+exp.getMessage());
        }
        
//        
    }

      public void Test_FastID(RFIDReader reader)
    {
         try
        {
            mystreamLog = new FileOutputStream("Java_FastID_Log.html");
            mystreamResult=new FileOutputStream("Java_FastID_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Tag Focus test case------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "StateAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        try{
                              
//                FormTestID(TestNo,SubNo++,"FUN");
            
                            byte[] Mask = { (byte)0xE2, (byte)0x80, (byte)0x1B, (byte)0x30 };//FastID
//                            byte[] Mask = { (byte)0xE2, (byte)0x80, (byte)0x1D, (byte)0x30 };//TagFocus
                            reader.Config.resetFactoryDefaults();
                            reader.Actions.PreFilters.deleteAll();
                            addSR(Mask, MEMORY_BANK.MEMORY_BANK_TID, 0, 32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B, TARGET.TARGET_INVENTORIED_STATE_S1);
                            
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
//        
    }
     private void addSR(byte[] pattern,MEMORY_BANK mBank,int bitOffset,int patternbitCount,STATE_AWARE_ACTION action,TARGET target )
    {
        try
        {
//            for(short antenna = 1; antenna <= reader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
//            {
//                PreFilters pfs = new PreFilters();
//                PreFilters.PreFilter pf1  = pfs.new PreFilter();
                pf1.setAntennaID((short)0);
                pf1.setBitOffset(bitOffset);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
                pf1.StateAwareAction.setStateAwareAction(action);
                pf1.StateAwareAction.setTarget(target);
                pf1.setMemoryBank(mBank);
                pf1.setTagPattern(pattern);
                pf1.setTagPatternBitCount(patternbitCount);
                reader.Actions.PreFilters.add(pf1);
                psLog.println("<br>State Aware Filter Action with :"+"<br>BitOffset: "+pf1.getBitOffset()+"<br>TagPatternBitCount: "+pf1.getTagPatternBitCount()+"<br>TagPattern: "+byte2Hex(pf1.getTagPattern())+"<br>MemoryBank: "+pf1.getMemoryBank());
                psLog.println("<br>FilterAction: "+pf1.getFilterAction()+"<br>StateAwareAction: "+pf1.StateAwareAction.getStateAwareAction()+"<br>Target: "+pf1.StateAwareAction.getTarget());
            //}
            psLog.println("<br>Prefilter added successfully");
        }
        catch(InvalidUsageException exp)
        {
//            System.out.println(""+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
        }

    }
     private void addUnawareSeletRecord(byte[] pattern,MEMORY_BANK mBank,int bitOffset,int patternbitCount,STATE_UNAWARE_ACTION action)
    {
        try
        {
//            for(short antenna = 1; antenna <= reader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
//            {
                pf1.setAntennaID((short)0);
                pf1.setBitOffset(bitOffset);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_UNAWARE);
                pf1.StateUnawareAction.setStateUnawareAction(action);
                pf1.setMemoryBank(mBank);
                pf1.setTagPattern(pattern);
                pf1.setTagPatternBitCount(patternbitCount);
                reader.Actions.PreFilters.add(pf1);
                psLog.println("<br>State Un-Aware Filter Action with :"+"<br>BitOffset: "+pf1.getBitOffset()+"<br>TagPatternBitCount: "+pf1.getTagPatternBitCount()+"<br>TagPattern: "+byte2Hex(pf1.getTagPattern())+"<br>MemoryBank: "+pf1.getMemoryBank());
                psLog.println("<br>FilterAction: "+pf1.getFilterAction()+"<br>StateUnAwareAction: "+pf1.StateUnawareAction.getStateUnawareAction());
//            }
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
            psLog.println("<br>" + exp.getVendorMessage());
        }
        catch (OperationFailureException opexp)
        {
            psLog.println("<br>" + opexp.getStatusDescription());
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
//          
     try
            {

            TagStorageSettings tagStorageSet=new TagStorageSettings();
            //tagStorageSet.setTagFields(TAG_FIELD.)
            reader.Actions.Inventory.perform();
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();
            //readParams.
            TagData tagdata[]=reader.Actions.getReadTags(50);
            //
            readParams.setMemoryBank(mb);
            readParams.setByteOffset(0);
            readParams.setByteCount(8);
            
//            reader.Actions.TagAccess.readEvent(readParams, null, null);
//            Thread.sleep(500);
           ArrayList<String> t = new ArrayList<String>();
            ArrayList<String> unique = new ArrayList<String>();
            if(tagdata != null)
            {
                String[] data = new String[tagdata.length];
                
//                System.out.print("\nNo.0f tags reported in this inventory "+tagdata.length);
                for(int i=0;i<tagdata.length;i++)
                {

                  data[i] = tagdata[i].getTagID().toString();

                }
                if(data != null)
                {
                    for(int i =0; i<data.length; i++)
                    {
                        if(!t.contains(data[i]))
                            t.add(data[i]);
                    }

                    for(int i =0; i< t.size(); i++)
                    {
//                        System.out.println(t.get(i).toString());
                         psLog.print("<br>"+t.get(i).toString());
                    }
                    psLog.print("<br>");
                    // Analysing the results 
                    AnalyzeResult(t);
                }

            }else
                {
                    if(antennaSingulation.Action.getSLFlag() == SL_FLAG.SL_FLAG_DEASSERTED )
                    {
                        psLog.print("<br>The tags are not reported<br>");
                        successCount++;
                        
                        psLog.println(TestID+" "+logText+": PASS"); 
                        psResult.println(TestID+" "+logText+": PASS"); 
                    }else{
                         psLog.print("<br>The tags are not reported<br>");
                         failureCount++;
                         psLog.println(TestID+" "+logText+": FAIL"); 
                        psResult.println(TestID+" "+logText+": FAIL"); 
                    }
                }
                
            }
        catch(InvalidUsageException exp) {
//            System.out.print("\nInvalidUsageException"+exp.getInfo());
           psLog.println("\nInvalidUsageException"+exp.getInfo());
        }
        catch(OperationFailureException exp) {

//            System.out.print("\nOperationFailureException"+exp.getMessage());
            psLog.println("\nOperationFailureException"+exp.getMessage());

                }
            catch(InterruptedException e)
            {
//                System.out.print("\nInterruptedException"+e.getMessage());
                psLog.println("\nInterruptedException"+e.getMessage());
            }
        }
     public boolean AnalyzeResult(ArrayList<String> t)
     {
         ArrayList<String> tags = t;
         if(pf1.getFilterAction() == FILTER_ACTION.FILTER_ACTION_STATE_UNAWARE)
         {
             STATE_UNAWARE_ACTION act = pf1.StateUnawareAction.getStateUnawareAction();
             switch(act.getValue())
             {
                 case 0:
                      {
                          //STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT
                          if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                 case 1:
                      {
                          //STATE_UNAWARE_ACTION_SELECT
                          if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                 case 2:
                      {
                          //STATE_UNAWARE_ACTION_NOT_UNSELECT
                          if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                 case 3:
                      {
                          //STATE_UNAWARE_ACTION_UNSELECT
                          if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                 case 4:
                      {
                          //STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT
                          if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                 case 5:
                      {
                          //STATE_UNAWARE_ACTION_NOT_SELECT
                          if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                          {
                            pass++;
                          }else{
                            fail++;
                          }
                          break;
                      }
                     
             }
             PrintResult();
         }else{
            STATE_AWARE_ACTION actions = pf1.StateAwareAction.getStateAwareAction();
            INVENTORY_STATE st = antennaSingulation.Action.getInventoryState();
            SL_FLAG slflag = antennaSingulation.Action.getSLFlag();
            if(pf1.StateAwareAction.getTarget() == TARGET.TARGET_SL)
            {
                switch(actions.getValue())
                {
                      case 0:
                      {
                           //STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL
                           if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                           {
                               if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                               {
                                   pass++;
                               }else{
                                   fail++;
                               }
                           }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                           {
                               if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                               {
                                   pass++;
                               }else{
                                   fail++;
                               }
                           }else
                           {
                               if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                               {
                                   pass++;
                               }else{
                                   fail++;
                               }
                           }
                           break;
                      }
                     case 1:
                           {
                               //STATE_AWARE_ACTION_ASRT_SL
                               if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                               {
                                   if(tags.size() == 0)
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                      case 2:
                           {
                               //STATE_AWARE_ACTION_NOT_DSRT_SL
                               if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                                }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                      case 3:
                           {
                               //STATE_AWARE_ACTION_NEG_SL_NOT_ASRT_SL

                                   if((tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }

                               break;
                           }
                      case 4:
                           {
                               //STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL
                               if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;                                    
                                   }else{
                                       fail++;
                                   }
                                }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                       case 5:
                           {
                               //STATE_AWARE_ACTION_DSRT_SL
                               if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                        case 6:
                           {
                               //STATE_AWARE_ACTION_NOT_ASRT_SL
                               if(slflag == SL_FLAG.SL_FLAG_ASSERTED)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                                }else if (slflag == SL_FLAG.SL_FLAG_DEASSERTED)
                               {
                                   if(tags.size() == 0)
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }  
                               break;
                           }
                         case 7:
                           {
                               //STATE_AWARE_ACTION_NOT_NEG_SL
                               if((tags.size() > 1))
                               {
                                   pass++;
                               }else{
                                   fail++;
                               }
                               break;
                           }

                }
                 PrintResult();
            }else{
                switch(actions.getValue())
                {
                    case 0:
                           {
                               //STATE_AWARE_ACTION_INV_A_NOT_INV_B

                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                     case 1:
                           {
                               //STATE_AWARE_ACTION_INV_A
                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if(tags.size() == 0)
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                      case 2:
                           {
                               //STATE_AWARE_ACTION_NOT_INV_B

                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                                }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if(pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S2 || pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S3)
                                   {
                                       if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                       {
                                           pass++; 
                                       }else{
                                           fail++;
                                       }
                                   }else{
                                       if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                       {
                                           pass++;
                                       }else{
                                           fail++;
                                       }
                                   }

                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                      case 3:
                           {
                               //STATE_AWARE_ACTION_INV_A2BB2A_NOT_INV_A

                                   if((tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }

                               break;
                           }
                      case 4:
                           {
                               //STATE_AWARE_ACTION_INV_B_NOT_INV_A
                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                                }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                       case 5:
                           {
                               //STATE_AWARE_ACTION_INV_B
                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if(!(tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if(pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S2 || pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S3)
                                   {
                                       if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                       {
                                           pass++;

                                       }else{
                                           fail++;
                                       }
                                   }else{
                                       if((tags.contains(matchingTag) == true) && (tags.size() == 1))
                                       {
                                           pass++; 
                                       }else{
                                           fail++;
                                       }

                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                               }
                               break;
                           }
                        case 6:
                           {
                               //STATE_AWARE_ACTION_NOT_INV_A
                               if(st == INVENTORY_STATE.INVENTORY_STATE_A)
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++;
                                   }else{
                                       fail++;
                                   }
                                }else if (st == INVENTORY_STATE.INVENTORY_STATE_B)
                               {
                                   if(pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S2 || pf1.StateAwareAction.getTarget() == TARGET.TARGET_INVENTORIED_STATE_S3)
                                   {
                                       if(tags.size() > 0)
                                       {
                                           pass++; 
                                       }else{
                                           fail++;
                                       }
                                   }else{
                                       if(tags.size() == 0)
                                       {
                                           pass++; 
                                       }else{
                                           fail++;
                                       }
                                   }
                               }else
                               {
                                   if((tags.contains(matchingTag) == true) && (tags.size() > 1))
                                   {
                                       pass++; 
                                   }else{
                                       fail++;
                                   }
                               }  
                               break;
                           }
                         case 7:
                           {
                               //STATE_AWARE_ACTION_NOT_INV_A2BB2A
                               if((tags.size() > 1))
                               {
                                   pass++; 
                               }else{
                                   fail++;
                               }
                               break;
                           }

                }
                PrintResult();
            }
            psLog.print("<br>");
         }
         return true;
     }
     public void PrintResult()
     {
         
         if(pass>0 && fail==0)
         {
             successCount++;
             psLog.println(TestID+" "+logText+": PASS"); 
             psResult.println(TestID+" "+logText+": PASS"); 
             
             
    
         }else{
             failureCount++;
             psLog.println(TestID+" "+logText+": FAIL"); 
             psResult.println(TestID+" "+logText+": FAIL"); 
             
         }
         pass = fail = 0;
     }
    }
