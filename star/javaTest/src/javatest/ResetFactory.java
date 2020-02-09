/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.reader;

/**
 *
 * @author NVJ438
 */
public class ResetFactory extends Commonclass {

    public ResetFactory()
    {
        try
        {
            mystreamLog = new FileOutputStream("JavaAPI_ResetFactory_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ResetFactory_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        }
        catch (FileNotFoundException e)
        {
            psLog.println("" + e.getMessage());
        }
    }

    public void Test_ResetFactory()
    {
        //System.out.println("Test_ResetFactory Started");
        psLog.println("<html><br>");
        psLog.println("<body><br>");
        successCount = 0;
        failureCount = 0;
        TestNo = 103;
        SubNo = 1;
        Test_antennaConfig();

        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:ResetFactory test cases:" + successCount + ":" + failureCount + ":" + "0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");
    }

    private void Test_antennaConfig()
    {
        //1. Power
        try
        {
            Antennas antenna = reader.Config.Antennas;
            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
            FormTestID(TestNo, SubNo++, "FUN");
            Config.setTransmitPowerIndex((short) 100);
            reader.Config.Antennas.setAntennaConfig(1, Config);
            Config = reader.Config.Antennas.getAntennaConfig(1);
            psLog.println("<br><b>Description:</b>");
            psLog.println(" Antenna Power changed to 100 and reset to factory default: ");
            psLog.println("<br>Get antenna power Index = " + Config.getTransmitPowerIndex());
            psLog.println("<br>Expected Result:  Power should set to default(220)");
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            Config = reader.Config.Antennas.getAntennaConfig(1);
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>Antenna power Index = " + Config.getTransmitPowerIndex());
            //The default value may be different for each reader based
            if (Config.getTransmitPowerIndex() != (short) 100)
            {
                psLog.println("<br>" + TestID + ":ResetFactory for Transmit Power:PASSED");
                psResult.println(TestID + ":ResetFactory for Transmit Power:PASSED");
                successCount++;
            } else {
                psLog.println("<br>" + TestID + ":ResetFactory for Transmit Power:FAILED");
                psResult.println(TestID + ":ResetFactory for Transmit Power:FAILED");
                failureCount++;
            }
        } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        //2.Duty Cycle
        
        FormTestID(TestNo, SubNo++, "FUN");
        psLog.println("<br><b>Description:</b>set duty cycle index to 5 and doing a facotry reset ");
        psLog.println("<br><b>Expected:</b>getDutyCycleIndex should be default duty cycle index(16)");
        boolean bSuccess = false;
        try
        {
            psLog.println("<br>Setting the duty cycle index to 5");
            reader.Config.setDutyCycleIndex((short) 5);
            psLog.println("<br>getDutyCycleIndex" + reader.Config.getDutyCycleIndex());
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>getDutyCycleIndex : " + reader.Config.getDutyCycleIndex());
            if (reader.Config.getDutyCycleIndex() == 16)
            {
                bSuccess = true;
            }
            else
            {
                bSuccess = false;
            }
        }
        catch (InvalidUsageException exp)
        {
            bSuccess = false;
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } 
        catch (OperationFailureException exp)
        {
            bSuccess = false;
            psLog.println("<br>OperationFailureException" + exp.getStatusDescription());
            
        } 
        catch (ArrayIndexOutOfBoundsException e)
        {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
         
        if(bSuccess || ( !bSuccess && reader.ReaderCapabilities.getModelName().equals("74004")) || ( !bSuccess && reader.ReaderCapabilities.getModelName().equals("75004")))
        {
            psLog.println("<br>" + TestID + ":ResetFactory for duty cycle:PASSED");
            psResult.println(TestID + ":ResetFactory for duty cycle:PASSED");
            successCount++;
        }
        
        else
        {
            psLog.println("<br>" + TestID + ":ResetFactory for duty cycle:FAILED");
            psResult.println(TestID + ":ResetFactory for duty cycle:FAILED");
            failureCount++;

        }

        
        //3.session

        try {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b>set session to s3 and doing a facotry reset ");
            psLog.println("<br><b>Expected:</b>getsession should be defaulted to S0");
            Antennas.SingulationControl singControl = reader.Config.Antennas.getSingulationControl(1);
            psLog.println("<br>Setting the session to S3");
            singControl.setSession(SESSION.SESSION_S3);
            reader.Config.Antennas.setSingulationControl(1, singControl);
            psLog.println("<br>getSession: " + reader.Config.Antennas.getSingulationControl(1).getSession());
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>getSession: " + reader.Config.Antennas.getSingulationControl(1).getSession());
            if (reader.Config.Antennas.getSingulationControl(1).getSession() == SESSION.SESSION_S0) {
                psLog.println("<br>" + TestID + ":ResetFactory for sessions:PASSED");
                psResult.println(TestID + ":ResetFactory for sessions:PASSED");
                successCount++;
            } else {
                psLog.println("<br>" + TestID + ":ResetFactory for sessions:FAILED");
                psResult.println(TestID + ":ResetFactory for sessions:FAILED");
                failureCount++;
            }

        } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        //4.TagStorage
        TagStorageSettings tagStorageSettings = new TagStorageSettings();
        tagStorageSettings.setMaxMemoryBankByteCount(2);
        try {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b>set MaxMemoryBankByteCount to 2 and doing a facotry reset ");
            psLog.println("<br><b>Expected:</b>get MaxMemoryBankByteCount should be defaulted to 64");
            psLog.println("<br>Setting the tag storage to 2");
            reader.Config.setTagStorageSettings(tagStorageSettings);
            psLog.println("<br>getMaxMemoryBankByteCount: " + reader.Config.getTagStorageSettings().getMaxMemoryBankByteCount());
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>getMaxMemoryBankByteCount: " + reader.Config.getTagStorageSettings().getMaxMemoryBankByteCount());
            if (reader.Config.getTagStorageSettings().getMaxMemoryBankByteCount() == 64) {
                psLog.println("<br>" + TestID + ":ResetFactory for tagstorage:PASSED");
                psResult.println(TestID + ":ResetFactory for tagstorage:PASSED");
                successCount++;
            } else {
                psLog.println("<br>" + TestID + ":ResetFactory for tagstorage:FAILED");
                psResult.println(TestID + ":ResetFactory for tagstorage:FAILED");
                failureCount++;
            }

        } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        //5. RF mode
        try {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b>set RFmode to 6 and do a facotry reset ");
            psLog.println("<br><b>Expected:</b>get RFmode should be defaulted to 0");
            psLog.println("<br>Setting the RFmode to 6");
            Antennas.RFMode antennaRFMode = reader.Config.Antennas.getRFMode(1);
            psLog.println("<br>Getting the RF mode before factory reset and setting the RF: " + reader.Config.Antennas.getRFMode(1).getTableIndex());
            antennaRFMode.setTableIndex(6);
            reader.Config.Antennas.setRFMode(1, antennaRFMode);
            antennaRFMode = reader.Config.Antennas.getRFMode(1);
            psLog.println("<br>Getting the RF mode before factory reset: " + reader.Config.Antennas.getRFMode(1).getTableIndex());
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>Getting the RF mode after factory reset: " + reader.Config.Antennas.getRFMode(1).getTableIndex());
            if(reader.Config.Antennas.getRFMode(1).getTableIndex() == 0)
            {
                psLog.println("<br>" + TestID + ":ResetFactory for RF mode:PASSED");
                psResult.println(TestID + ":ResetFactory for RF mode:PASSED");
                successCount++;
            }
            else
            {
                psLog.println("<br>" + TestID + ":ResetFactory for RF mode:FAILED");
                psResult.println(TestID + ":ResetFactory for RF mode:FAILED");
                failureCount++;
            }

        } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        //6. Disable read point

        //7. Diable radio
        try
        {
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b>set radio power to OFF and do a facotry reset ");
            psLog.println("<br><b>Expected:</b>Radio should be n OFF statet");
            psLog.println("<br>getRadioPowerState before reset factory default: "+reader.Config.getRadioPowerState());
            psLog.println("<br>Setting the radio to OFF state");
            reader.Config.setRadioPowerState(RADIO_POWER_STATE.OFF);
            Thread.sleep(3000);
            psLog.println("<br>getRadioPowerState : "+reader.Config.getRadioPowerState());
            psLog.println("<br>Calling Reset factory default");
            reader.Config.resetFactoryDefaults();
            Thread.sleep(3000);
            psLog.println("<br><b>ActualResult</b>");
            psLog.println("<br>getRadioPowerState after reset factory default: "+reader.Config.getRadioPowerState());
            if(reader.ReaderCapabilities.getModelName().equals("75002") || reader.ReaderCapabilities.getModelName().equals("75004") || reader.ReaderCapabilities.getModelName().equals("75008"))
            {
                if(reader.Config.getRadioPowerState() == RADIO_POWER_STATE.OFF)
                {
                    psLog.println("<br>" + TestID + ":ResetFactory Radio power:PASSED");
                    psResult.println(TestID + ":ResetFactory Radio power:PASSED");
                    successCount++;
                }
                else
                {
                    psLog.println("<br>" + TestID + ":ResetFactory Radio power:FAILED");
                    psResult.println(TestID + ":ResetFactory Radio power:FAILED");
                    failureCount++;
                }
            }else{
                if(reader.Config.getRadioPowerState() == RADIO_POWER_STATE.ON)
                {
                    psLog.println("<br>" + TestID + ":ResetFactory Radio power:PASSED");
                    psResult.println(TestID + ":ResetFactory Radio power:PASSED");
                    successCount++;
                }
                else
                {
                    psLog.println("<br>" + TestID + ":ResetFactory Radio power:FAILED");
                    psResult.println(TestID + ":ResetFactory Radio power:FAILED");
                    failureCount++;
                }
            }
        } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException " + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException " + exp.getStatusDescription());
            if(exp.getStatusDescription().equals("Functionality Not Supported by Reader"))
            {
                psLog.println("<br>" + TestID + ":ResetFactory Radio power:PASSED");
                psResult.println(TestID + ":ResetFactory Radio power:PASSED");
                successCount++;
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException " + e.getMessage());
        } catch(InterruptedException e){
            psLog.println("<br>InterruptedException " + e.getMessage());
        }
        
        //7.filters
        FormTestID(TestNo, SubNo++, "FUN");
        try{
        PreFilters pfs = new PreFilters();
        PreFilters.PreFilter pf1  = pfs.new PreFilter();
        Antennas antenna = reader.Config.Antennas;
        Antennas.SingulationControl antennaSingulation = antenna.new SingulationControl();
         pf1.setAntennaID((short)0);
                pf1.setBitOffset(32);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
                pf1.StateAwareAction.setStateAwareAction(STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B);
                pf1.StateAwareAction.setTarget(TARGET.TARGET_INVENTORIED_STATE_S3);
                pf1.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
                byte[] tagMask = { 0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44, 0x55, 0x55, 0x66, 0x66 };
                pf1.setTagPattern(tagMask);
                pf1.setTagPatternBitCount(12);
                reader.Actions.PreFilters.add(pf1);
                psLog.println("<br>State Aware Filter Action with :"+"<br>BitOffset: "+pf1.getBitOffset()+"<br>TagPatternBitCount: "+pf1.getTagPatternBitCount()+"<br>TagPattern: "+byte2Hex(pf1.getTagPattern())+"<br>MemoryBank: "+pf1.getMemoryBank());
                psLog.println("<br>FilterAction: "+pf1.getFilterAction()+"<br>StateAwareAction: "+pf1.StateAwareAction.getStateAwareAction()+"<br>Target: "+pf1.StateAwareAction.getTarget());
            //}
            psLog.println("<br>Prefilter added successfully");
            
            for (short j = 1; j <= reader.ReaderCapabilities.getNumAntennaSupported(); j++)
            {
                antennaSingulation.setSession(SESSION.SESSION_S3);
                antennaSingulation.setTagPopulation((short) 100);
                antennaSingulation.setTagTransitTime((short) 0);
                antennaSingulation.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                antennaSingulation.Action.setPerformStateAwareSingulationAction(true);
                antennaSingulation.Action.setSLFlag(SL_FLAG.SL_FLAG_DEASSERTED);
                reader.Config.Antennas.setSingulationControl(j, antennaSingulation);
            }
                
            
            psLog.println("<br>Singulation:  <br>"+"Session: "+antennaSingulation.getSession()+"<br>InventoryState: "+antennaSingulation.Action.getInventoryState()+"<br>SLFlag: "+antennaSingulation.Action.getSLFlag());
            Simple_Inventroy();
            reader.Config.resetFactoryDefaults();
            psLog.println("<br>Singulation:  <br>"+"Session: "+antennaSingulation.getSession()+"<br>InventoryState: "+antennaSingulation.Action.getInventoryState()+"<br>SLFlag: "+antennaSingulation.Action.getSLFlag());
            psLog.println("<br><br><br><br>");
             Simple_Inventroy();
            } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getStatusDescription());
            if(exp.getStatusDescription().equals("Functionality Not Supported by Reader"))
            {
                psLog.println("<br>" + TestID + ":ResetFactory Radio power:PASSED");
                psResult.println(TestID + ":ResetFactory Radio power:PASSED");
                successCount++;
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        
    }
    public void Simple_Inventroy()
    {
            try{
                
            
            reader.Actions.Inventory.perform();
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();
            //readParams.
            TagData tagdata[]=reader.Actions.getReadTags(50);
            if(tagdata != null)
            {
                for(int i = 0; i<tagdata.length; i++)
                {
                    //System.out.println(tagdata[i].getTagID()+" AntennaID: "+tagdata[i].getAntennaID());
                    psLog.println("<br>TagID: "+tagdata[i].getTagID()+" AntennaID: "+tagdata[i].getAntennaID());
                }
                
            }
             } catch (InvalidUsageException exp) {
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
        } catch (OperationFailureException exp) {
            psLog.println("<br>OperationFailureException" + exp.getStatusDescription());
            if(exp.getStatusDescription().equals("Functionality Not Supported by Reader"))
            {
                psLog.println("<br>" + TestID + ":ResetFactory Radio power:PASSED");
                psResult.println(TestID + ":ResetFactory Radio power:PASSED");
                successCount++;
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());
        }
        catch(InterruptedException e)
            {
//                System.out.print("\nInterruptedException"+e.getMessage());
                psLog.println("\nInterruptedException"+e.getMessage());
            }
    }
}
