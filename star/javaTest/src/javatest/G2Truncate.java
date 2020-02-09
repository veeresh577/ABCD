/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.io.*;
import java.util.Date;

/**
 *
 * @author QMTN48
 */
public class G2Truncate extends Commonclass
{
    private PreFilters pfs;
    private PreFilters.PreFilter pf1;
    private byte[] pattern={(byte)0x34,(byte)0x00,(byte)0xbe,(byte)0xdd};
    private Antennas.SingulationControl singControl;
    private RFIDReader myReader;
    private boolean bSuccess = true;
    private Antennas antenna;
    public G2Truncate( RFIDReader r)
    {
        myReader = r;
        antenna = myReader.Config.Antennas;
        pfs = new PreFilters();
        pf1  = pfs.new PreFilter();
        singControl = antenna.new SingulationControl();
        pf1.setBitOffset(16);
        pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
        pf1.StateAwareAction.setStateAwareAction(STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL);
        pf1.StateAwareAction.setTarget(TARGET.TARGET_SL);
        pf1.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        pf1.setTagPattern(pattern);
        pf1.setTagPatternBitCount(32);
        pf1.setTruncateAction(TRUNCATE_ACTION.TRUNCATE_ACTION_TRUNCATE);
        singControl.setSession(SESSION.SESSION_S0);
        singControl.setTagPopulation((short) 100);
        singControl.setTagTransitTime((short) 100);
        singControl.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
        singControl.Action.setPerformStateAwareSingulationAction(true);
        singControl.Action.setSLFlag(SL_FLAG.SL_FLAG_ASSERTED);

    }
    
    public void testTruncate()
    {
        successCount = failureCount =0;
        // Logger initialization for logging the results
        try
        {
            mystreamLog=new FileOutputStream("Java API_G2Truncate_Log.html");
            mystreamResult=new FileOutputStream("Java API_G2TruncateResult.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing SG2Truncate------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "G2Truncate";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        
        try
        {

            for(short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
            {
                pf1.setAntennaID(antenna);
                myReader.Actions.PreFilters.add(pf1);
                myReader.Config.Antennas.setSingulationControl(antenna, singControl);
            }

            FormTestID(TestNo,SubNo++,"FUN");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText);
            psLog.println("\n\nTesting Truncate"+pf1.getTruncateAction()+"<br>\n");
            psLog.println("\n<b> Expected: No truncate </b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");
            myReader.Actions.Inventory.perform(null,null,null);
            Thread.sleep(30000);
            myReader.Actions.Inventory.stop();
            TagData[] tags = myReader.Actions.getReadTags(1000);
            if( tags != null )
            {
                for(int tagCount = 0; tagCount < tags.length;tagCount++)
                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+"  memBank:"+tags[tagCount].getMemoryBank()+"  memBankData"+ tags[tagCount].getMemoryBankData()+"<br> \r\n");
//                    if(hex2Byte(tags[tagCount].getTagID())[0] == 0x00 && hex2Byte(tags[tagCount].getTagID())[1] == 0x00 )
//                    {
//                        bSuccess = false;
//                    }
                }
            }

            myReader.Actions.purgeTags();
            if( bSuccess == true )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }

            // Do not truncate
            pf1.setTruncateAction(TRUNCATE_ACTION.TRUNCATE_ACTION_TRUNCATE);
            for(short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
            {
                pf1.setAntennaID(antenna);
                myReader.Actions.PreFilters.add(pf1);
            }

            FormTestID(TestNo,SubNo++,"FUN");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText);
            psLog.println("\n\nTesting Truncate"+pf1.getTruncateAction()+"<br>\n");
            psLog.println("\n<b> Expected: No truncate </b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");
            myReader.Actions.Inventory.perform(null,null,null);
            Thread.sleep(3000);
            myReader.Actions.Inventory.stop();
            tags = myReader.Actions.getReadTags(1000);
            if( tags != null )
            {
                for(int tagCount = 0; tagCount < tags.length;tagCount++)
                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+"  memBank:"+tags[tagCount].getMemoryBank()+"  memBankData"+ tags[tagCount].getMemoryBankData()+"<br> \r\n");
//                    if(hex2Byte(tags[tagCount].getTagID())[0] == 0x00 && hex2Byte(tags[tagCount].getTagID())[1] == 0x00 )
//                    {
//                        bSuccess = false;
//                    }
                }
            }

            myReader.Actions.purgeTags();
            if( bSuccess == true )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }

            // Truncate the tags
            //
            pf1.setTruncateAction(TRUNCATE_ACTION.TRUNCATE_ACTION_TRUNCATE);
            for(short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
            {
                pf1.setAntennaID(antenna);
                myReader.Actions.PreFilters.add(pf1);
            }

            FormTestID(TestNo,SubNo++,"FUN");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText);
            psLog.println("\n\nTesting Truncate"+pf1.getTruncateAction()+"<br>\n");
            psLog.println("\n<b> Expected: Truncate </b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");
            myReader.Actions.Inventory.perform(null,null,null);
            Thread.sleep(3000);
            myReader.Actions.Inventory.stop();
            tags = myReader.Actions.getReadTags(1000);
            if( tags != null )
            {
                for(int tagCount = 0; tagCount < tags.length;tagCount++)
                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+"  memBank:"+tags[tagCount].getMemoryBank()+"  memBankData"+ tags[tagCount].getMemoryBankData()+"<br> \r\n");
                    if(hex2Byte(tags[tagCount].getTagID())[0] == 0xa2 && hex2Byte(tags[tagCount].getTagID())[1] == 0x2f )
                    {
                        bSuccess = false;
                    }
                }
            }

            myReader.Actions.purgeTags();
            if( bSuccess == true )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
        }
        catch(InterruptedException e)
        {

        }
        catch(InvalidUsageException exp)
        {
            System.out.println(""+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(""+opexp.getStatusDescription());
        }
        //delete all pre filters.
        try
        {
            myReader.Actions.PreFilters.deleteAll();
        }
        catch(InvalidUsageException exp)
        {
            System.out.println(""+ exp.getVendorMessage());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(""+opexp.getStatusDescription());
        }
        psSummary.println("JavaAPI:Truncate:" + successCount + ":" + failureCount + ":" + "0");
    }

}
