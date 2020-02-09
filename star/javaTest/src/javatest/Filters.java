/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import static javatest.Commonclass.logText;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;

class mytag
{
    public int CRC;
    public int PC;
    public byte[] epc;
    public byte[] tid;
    public byte[] res;
    public byte[] user;
    public int epclen;
    public int tidlen;
    public int reslen;
    public int userlen;

    public mytag(int CRCLen,int PClen,int epclen, int tidlen, int reslen,int userlen)
    {
        CRC = 0x00;
        PC = 0x00;
        epc = new byte[epclen];
        tid = new byte[tidlen];
        res = new byte[reslen];
        user = new byte[userlen];
    }
}
/**
 *
 * @author QMTN48
 */
public class Filters extends Commonclass
{
    static private mytag tag1,tag2;
    private boolean btag1,btag2;
    private TagData[] tagData;
    private RFIDReader myReader;
    private TagAccess tagAccess;
    private byte[] tagMask = { (byte)0xFF, (byte)0xFF };
    private byte[] tagPattern1 = { (byte)0xA2, (byte)0x2F };
    private byte[] tagPattern2 = { (byte)0xB2, (byte)0x2F };
    private short[] antennaList  = { 1,2 };
    private OPERATION_QUALIFIER antOPQ[] = { OPERATION_QUALIFIER.C1G2_OPERATION, OPERATION_QUALIFIER.C1G2_OPERATION};
    private TagPatternBase tpA;
    private TagPatternBase tpB;
    private PostFilter postfilter;
    private AccessFilter afilter;
    private TagAccess.ReadAccessParams rParams;
    // flags for the getting memory banks
    private boolean btid,buser;
    private Antennas antenna;
    private Antennas.SingulationControl singControl;
    private TriggerInfo tInfo;
    // Filters Constructor.
    public Filters( RFIDReader reader)
    {
        myReader = reader;
        tagAccess = new TagAccess( );
        rParams = tagAccess.new ReadAccessParams( );
        tagData = new TagData[1];
        antenna = myReader.Config.Antennas;
        tInfo = new TriggerInfo();
        
        //two tags
        tag1 = new mytag(2,2,12,24,8,64);
        tag2 = new mytag(2,2,12,24,8,64);

        btag1 = btag2 =false;
        
        tpA = new TagPatternBase( );
        tpB = new TagPatternBase( );
        postfilter = new PostFilter( );

        //tag Pattern A
        tpA.setBitOffset(32);
        tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpA.setTagMask(tagMask);
        tpA.setTagMaskBitCount(16);
        tpA.setTagPattern(tagPattern1);
        tpA.setTagPatternBitCount(16);

        //tag pattern B
        tpB.setBitOffset(32);
        tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
        tpB.setTagMask(tagMask);
        tpB.setTagMaskBitCount(16);
        tpB.setTagPattern(tagPattern2);
        tpB.setTagPatternBitCount(16);

         //post filter
        postfilter.TagPatternA = tpA;
        postfilter.TagPatternB = tpB;
        postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A_AND_NOTB);
        postfilter.setRSSIRangeFilter(false);
        btid = buser = false;
        singControl = antenna.new SingulationControl();
    }

    private void addSelectRecord(byte[] pattern,MEMORY_BANK mBank,int bitOffset,int patternbitCount,STATE_AWARE_ACTION action,TARGET target )
    {
        try
        {
            for(short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
            {
                PreFilters pfs = new PreFilters();
                PreFilters.PreFilter pf1  = pfs.new PreFilter();
                pf1.setAntennaID((short)antenna);
                pf1.setBitOffset(bitOffset);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_AWARE);
                pf1.StateAwareAction.setStateAwareAction(action);
                pf1.StateAwareAction.setTarget(target);
                pf1.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
                pf1.setTagPattern(pattern);
                pf1.setTagPatternBitCount(patternbitCount);
                myReader.Actions.PreFilters.add(pf1);
            }
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

    private void addUnawareSeletRecord(byte[] pattern,MEMORY_BANK mBank,int bitOffset,int patternbitCount,STATE_UNAWARE_ACTION action)
    {
        try
        {
            for(short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported();antenna++ )
            {
                PreFilters pfs = new PreFilters();
                PreFilters.PreFilter pf1  = pfs.new PreFilter();
                pf1.setAntennaID((short)antenna);
                pf1.setBitOffset(bitOffset);
                pf1.setFilterAction(FILTER_ACTION.FILTER_ACTION_STATE_UNAWARE);
                pf1.StateUnawareAction.setStateUnawareAction(action);
                pf1.setMemoryBank(mBank);
                pf1.setTagPattern(pattern);
                pf1.setTagPatternBitCount(patternbitCount);
                myReader.Actions.PreFilters.add(pf1);
            }
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
            for (short antenna = 1; antenna <= myReader.ReaderCapabilities.getNumAntennaSupported(); antenna++)
            {
                /* set singulation control paraeters and verify truncate for various options*/
                singControl.setSession(session);
                singControl.setTagPopulation((short) 100);
                singControl.setTagTransitTime((short) 0);
                singControl.Action.setInventoryState(inv);
                singControl.Action.setPerformStateAwareSingulationAction(true);
                singControl.Action.setSLFlag(SL);
                myReader.Config.Antennas.setSingulationControl(antenna, singControl);
            }
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

    private int ReadNDisplayTags(TriggerInfo tInfo)
    {
        int totaltagCount = 0;
        try
        {
            myReader.Actions.Inventory.perform(null,tInfo,null);
            Thread.sleep(4000);
            myReader.Actions.Inventory.stop();
            TagData[] tags = myReader.Actions.getReadTags(1000);
            if( tags != null )
            {
                for(int tagCount = 0; tagCount < tags.length;tagCount++)
                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+"<br>");
                }
                totaltagCount = tags.length;
            }

            myReader.Actions.PreFilters.deleteAll();
            myReader.Actions.purgeTags();
            myReader.Config.resetFactoryDefaults();
            return totaltagCount;
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
        return 0;
    }
    
    private void ReadNDisplayTags( TagAccess.ReadAccessParams rParams,PostFilter pfilter,TriggerInfo tInfo)
    {
        FILTER_MATCH_PATTERN[] array = { FILTER_MATCH_PATTERN.A_AND_B,FILTER_MATCH_PATTERN.NOTA_AND_B,FILTER_MATCH_PATTERN.NOTA_AND_NOTB,FILTER_MATCH_PATTERN.A_AND_NOTB,FILTER_MATCH_PATTERN.A};

        for( int pattern = 0; pattern <5;pattern++)
        {
            FILTER_MATCH_PATTERN match = array[pattern];
            FormTestID(TestNo++,SubNo,"PostFilter");
            pfilter.setPostFilterMatchPattern(match);
            if( match == FILTER_MATCH_PATTERN.A)
            {
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText);
                psLog.println("<br>Testing PostFilters with memBank "+pfilter.TagPatternA.getMemoryBank()+"Pattern-A :"+byte2Hex(pfilter.TagPatternA.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternA.getBitOffset() +" and Datalength :"+pfilter.TagPatternA.getTagPatternBitCount()+"\n");
                psLog.println("<br>Testing PostFilters with memBank "+pfilter.TagPatternB.getMemoryBank()+"Pattern-B :"+byte2Hex(pfilter.TagPatternB.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternB.getBitOffset() +"and Datalength :"+pfilter.TagPatternB.getTagPatternBitCount()+"\n");
                psLog.println("\n<br> Expected: match Pattern: Tags matching pattern A only be reported </b>");
                psLog.println("\n<br> Actual Result is :</b><br>");

            }

            if( match == FILTER_MATCH_PATTERN.A_AND_B)
            {
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText);
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternA.getMemoryBank()+"Pattern-A :"+byte2Hex(pfilter.TagPatternA.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternA.getBitOffset() +"and Datalength :"+pfilter.TagPatternA.getTagPatternBitCount()+"\n");
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternB.getMemoryBank()+"Pattern-B :"+byte2Hex(pfilter.TagPatternB.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternB.getBitOffset() +"and Datalength :"+pfilter.TagPatternB.getTagPatternBitCount()+"\n");
                psLog.println("\n<br> Expected: match Pattern: Tags matching pattern A and pattern B only be reported(No tags) </b>");
                psLog.println("\n<br> Actual Result is :</b><br>");
            }
            
            if( match == FILTER_MATCH_PATTERN.A_AND_NOTB)
            {
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText);
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternA.getMemoryBank()+"Pattern-A :"+byte2Hex(pfilter.TagPatternA.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternA.getBitOffset() +"and Datalength :"+pfilter.TagPatternA.getTagPatternBitCount()+"\n");
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternB.getMemoryBank()+"Pattern-B :"+byte2Hex(pfilter.TagPatternB.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternB.getBitOffset() +"and Datalength :"+pfilter.TagPatternB.getTagPatternBitCount()+"\n");
                psLog.println("\n<br> Expected: match Pattern: Tags matching pattern A and Not B only be reported </b>");
                psLog.println("\n<br> Actual Result is :</b><br>");
            }
            if( match == FILTER_MATCH_PATTERN.NOTA_AND_B)
            {
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText);
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternA.getMemoryBank()+"Pattern-A :"+byte2Hex(pfilter.TagPatternA.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternA.getBitOffset() +"and Datalength :"+pfilter.TagPatternA.getTagPatternBitCount()+"\n");
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternB.getMemoryBank()+"Pattern-B :"+byte2Hex(pfilter.TagPatternB.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternB.getBitOffset() +"and Datalength :"+pfilter.TagPatternB.getTagPatternBitCount()+"\n");
                psLog.println("\n<br> Expected: match Pattern: Tags matching pattern not A and only B be reported </b>");
                psLog.println("\n<br> Actual Result is :</b><br>");
            }
            if( match == FILTER_MATCH_PATTERN.NOTA_AND_NOTB)
            {
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText);
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternA.getMemoryBank()+"Pattern-A :"+byte2Hex(pfilter.TagPatternA.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternA.getBitOffset() +"and Datalength :"+pfilter.TagPatternA.getTagPatternBitCount()+"\n");
                psLog.println("<br>Testing PostFilters with memBank"+pfilter.TagPatternB.getMemoryBank()+"Pattern-B :"+byte2Hex(pfilter.TagPatternB.getTagPattern())+" with OFFSET(bits)"+ pfilter.TagPatternB.getBitOffset() +"and Datalength :"+pfilter.TagPatternB.getTagPatternBitCount()+"\n");
                psLog.println("\n<br> Expected: match Pattern: Tags matching pattern not A and not B be reported </b>");
                psLog.println("\n<br> Actual Result is :</b><br>");
            }


//            int totaltagCount = 0;
            try
            {
//                myReader.Config.resetFactoryDefaults();
                myReader.Actions.Inventory.perform(pfilter,tInfo,null);
                Thread.sleep(4000);
                myReader.Actions.Inventory.stop();
                TagData tags[] = myReader.Actions.getReadTags(1000);
                ArrayList<String> t = new ArrayList<String>();
                if( tags != null )
                {
                    String[] data = new String[tags.length];
                    for(int i=0;i<tags.length;i++)
                    {
                        data[i] = tags[i].getTagID().toString();
                    }
                    
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

                        myReader.Actions.purgeTags();
                        String s1 = byte2Hex(tag1.epc);
                        String s2 = byte2Hex(tag2.epc);
                        if(pfilter.TagPatternA.getMemoryBank() == MEMORY_BANK.MEMORY_BANK_TID){
//                            s1 = byte2Hex(tag1.tid);
//                            s2 = byte2Hex(tag2.tid);
                            if( match == FILTER_MATCH_PATTERN.A && (t.contains(s1) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.A_AND_NOTB && !(t.contains(s1)) && !(t.contains(s2) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.NOTA_AND_B && !(t.contains(s1)) && !(t.contains(s2) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.NOTA_AND_NOTB && !(t.contains(s1)) && !(t.contains(s2) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.A_AND_B && (t.contains(s1)) && (t.contains(s2) ))
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
                            
                        }else{
                        
                            if( match == FILTER_MATCH_PATTERN.A && (t.contains(s1) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.A_AND_NOTB && (t.contains(s1)) && !(t.contains(s2) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.NOTA_AND_B && !(t.contains(s1)) && (t.contains(s2) )) 
                            {
                                psLog.println("\n Test Result    :PASS");
                                psResult.println(TestID+"   "+logText+"    :PASS");
                                successCount++;

                            }
                            else if( match == FILTER_MATCH_PATTERN.NOTA_AND_NOTB && !(t.contains(s1)) && !(t.contains(s2) )) 
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
                }else{
                    // if no tags reported
                    if(pfilter.TagPatternA.getMemoryBank() == MEMORY_BANK.MEMORY_BANK_TID){
                        if( match == FILTER_MATCH_PATTERN.A_AND_NOTB  || match == FILTER_MATCH_PATTERN.NOTA_AND_B || match == FILTER_MATCH_PATTERN.NOTA_AND_NOTB)
                        {
                            psLog.println("\n Test Result    :PASS");
                            psResult.println(TestID+"   "+logText+"    :PASS");
                            successCount++;

                        }else
                        {
                            psLog.println("\n Test Result    :FAIL");
                            psResult.println(TestID+"   "+logText+"    :FAIL");
                            failureCount++;
                        }
                    
                    }else{
                    if( match == FILTER_MATCH_PATTERN.A_AND_B  || match == FILTER_MATCH_PATTERN.NOTA_AND_NOTB)
                        {
                            psLog.println("\n Test Result    :PASS");
                            psResult.println(TestID+"   "+logText+"    :PASS");
                            successCount++;

                        }else
                        {
                            psLog.println("\n Test Result    :FAIL");
                            psResult.println(TestID+"   "+logText+"    :FAIL");
                            failureCount++;
                        }
                    }
                    
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
        }
    }

    private void deleteAllFilters()
    {
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
    }
    
    public void testSngulationsStateAware( )
    {
        successCount = failureCount =0;TestNo = 41;    
        // Logger initialization for logging the results
        try
        {
            mystreamLog=new FileOutputStream("Java API_Filters_StateAware_Log.html");
            mystreamResult=new FileOutputStream("Java API_Filters_StateAware_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing StateAware Singulation------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "StateAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }

        // State unaware singulation with TID Emmory Bank
        byte[] pattern1 = new byte[4];
        System.arraycopy(tag1.epc, 0, pattern1, 0, 4);
        // State unaware singulation with TID Emmory Bank
        byte[] pattern2 = new byte[4];
        System.arraycopy(tag1.epc, 0, pattern2, 0, 4);
        byte[] pattern3 = {(byte)0xbe,(byte)0xdd};
        testStateAwareSingulation(pattern1,pattern2,pattern3, MEMORY_BANK.MEMORY_BANK_EPC, 32,null);
        testStateAwareSingulation(pattern1,pattern2,pattern3, MEMORY_BANK.MEMORY_BANK_EPC, 32,tInfo);
        psSummary.println("JavaAPI:State Aware: 2 filters: " + successCount + ":" + failureCount + ":" + "0");
    }

    private void testStateAwareSingulation(byte[] pattern1,byte[] pattern2,byte[] pattern3,MEMORY_BANK mBank,int bitOffSet,TriggerInfo triggerInfo)
    {
        SubNo = 1;
        FormTestID(TestNo,SubNo++,"STATE_AWARE");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S0);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL,TARGET.TARGET_SL);

        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S0 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S0, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S0, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);

        if( ReadNDisplayTags(triggerInfo)>0 )
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
        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S0 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S0, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags other than"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S0, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();
        SubNo = 1;
        FormTestID(TestNo,SubNo++,"STATE_AWARE");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S1);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL,TARGET.TARGET_SL);

        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S1 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);

        if( ReadNDisplayTags(triggerInfo)>0 )
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
        FormTestID(TestNo++,SubNo++,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S1 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags other than"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();
        SubNo = 1;
        FormTestID(TestNo,SubNo++,"STATE_AWARE");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S2);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL,TARGET.TARGET_SL);

        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S2 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S2, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S2, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);

        if( ReadNDisplayTags(triggerInfo)>0 )
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
        FormTestID(TestNo++,SubNo++,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S2 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S2, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags other than"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S2, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();
        SubNo = 1;
        FormTestID(TestNo,SubNo++,"STATE_AWARE");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S3);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL,TARGET.TARGET_SL);

        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S3 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S3, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S3, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);

        if( ReadNDisplayTags(triggerInfo)>0 )
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
        FormTestID(TestNo++,SubNo++,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A "+"Target TARGET_INVENTORIED_STATE_S3 <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1) +" Action:"+"STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL "+"Target: TARGET_SL <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S3, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags other than"+mBank+"pattern:"+ byte2Hex(pattern1)+" reported</b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S3, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();

        SubNo = 0;
        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S0+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S0, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags other than"+mBank+" pattern "+byte2Hex(pattern1)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        // test multiple select records.
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S0);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL,TARGET.TARGET_SL);
        addSelectRecord(pattern2, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S1);
        
        setSingulation(SESSION.SESSION_S0, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S0+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern1)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();
        
        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S1+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern2)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S1);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL,TARGET.TARGET_SL);
        addSelectRecord(pattern2, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S1);

        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);
        if( ReadNDisplayTags(triggerInfo)>0 )
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
        
        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S1+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern1)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_A);
        if( ReadNDisplayTags(triggerInfo)>0 )
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
        
        deleteAllFilters();

        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S1+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern1)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S1);
        addSelectRecord(pattern1, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL,TARGET.TARGET_SL);
        addSelectRecord(pattern2, mBank, bitOffSet,32, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S2);
        addSelectRecord(pattern3, mBank, bitOffSet,16, STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A,TARGET.TARGET_INVENTORIED_STATE_S3);
        setSingulation(SESSION.SESSION_S1, SL_FLAG.SL_FLAG_ASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S1+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S2, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern2)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S2, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_AWARE");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText);
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S1+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern1)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL+" Target "+TARGET.TARGET_SL+" <br>");
        psLog.println("\npattern :"+byte2Hex(pattern2)+"+mBank: "+mBank+" Action: "+STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A+" Target "+TARGET.TARGET_INVENTORIED_STATE_S2+" <br>");
        psLog.println("\n Singulation Params :SESSION.SESSION_S3, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B<br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern3)+" reported </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        setSingulation(SESSION.SESSION_S3, SL_FLAG.SL_FLAG_DEASSERTED, INVENTORY_STATE.INVENTORY_STATE_B);
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        deleteAllFilters();
    }

    private void testStateUnAwareSingulation(byte[] pattern,MEMORY_BANK mBank,int bitOffSet,TriggerInfo triggerInfo)
    {
        
        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT);
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank:"+ mBank+" bitOffSet: "+bitOffSet+" bitCount "+32+ STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT+" <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+" pattern "+byte2Hex(pattern)+" be read Other Tags Should not Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        
        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT);
        psLog.println("\n<a><br>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank: "+mBank+" bitOffSet:"+ bitOffSet +"bitCount "+32+ STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT+" <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern"+byte2Hex(pattern)+" be read Other may or may not Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_UNSELECT);
        psLog.println("\n<a><br>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_UNSELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank: "+mBank +"bitOffSet:"+ bitOffSet +"bitCount"+ 32 +"STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_UNSELECT <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern "+byte2Hex(pattern)+" may be Read other tags SHould not be Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT);
        psLog.println("\n<a><br>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank: "+mBank +"bitOffSet:"+ bitOffSet +"bitCount"+ 32 +"STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern "+byte2Hex(pattern)+" Should not be Read other tags may be Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");

        if( ReadNDisplayTags(triggerInfo)>0 )
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

        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT);
        psLog.println("\n<a><br>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank: "+mBank +"bitOffSet:"+ bitOffSet +"bitCount"+ 32 +"STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern "+byte2Hex(pattern)+" Should not be Read other tags must be Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        if( ReadNDisplayTags(triggerInfo)>0 )
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
        
        FormTestID(TestNo++,SubNo,"STATE_UNAWARE");
        addUnawareSeletRecord(pattern, mBank, bitOffSet, 32, STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT);
        psLog.println("\n<a><br>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:"+logText+"      "+STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT);
        psLog.println("\npattern :"+byte2Hex(pattern)+" mBank: "+mBank +"bitOffSet:"+ bitOffSet +"bitCount"+ 32 +"STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT <br>");
        psLog.println("\n<b> Expected: Tags matching"+mBank+"pattern "+byte2Hex(pattern)+" may be Read other tags must be Read </b><br>");
        psLog.println("\n<b> Actual Result is :</b><br>");
        
        if(mBank == MEMORY_BANK.MEMORY_BANK_TID)
        {
            if( ReadNDisplayTags(triggerInfo) == 0 )
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
            
        }else{

            if( ReadNDisplayTags(triggerInfo)>0 )
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
        
        psLog.println("</html>");
        
    }
    
    
    public boolean Read2Tags( )
    {
        if( get2Tags() != true )
        {
            return false;
        }
        return true;
    }
    private boolean get2Tags()
    {
        try
        {
            TagData tidMemData = new TagData();
            TagData userMemData = new TagData();
            myReader.Actions.PreFilters.deleteAll();
            myReader.Config.resetFactoryDefaults();
            myReader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(15000);
            myReader.Actions.Inventory.stop();
READAGAIN1:
            while( (tagData = myReader.Actions.getReadTags(1)) != null)
            {
                tag1.CRC = tagData[0].getCRC();
                tag1.PC = tagData[0].getPC();

                tag1.epc = hex2Byte(tagData[0].getTagID());
                tag1.epclen = tagData[0].getTagIDAllocatedSize();
                
                System.out.println("Tag 1:");
                psSummary.println("\nTag 1:");
                psSummary.println("");
                System.out.println("tag ID is\t"+tagData[0].getTagID()+"\t length :"+tag1.epclen);
                psSummary.println("\ntag ID is\t"+tagData[0].getTagID()+"\t length :"+tag1.epclen+"<br>");
                a22fTag = tagData[0].getTagID();
                // get TID Data
                rParams.setAccessPassword(0);
                rParams.setByteCount(0);
                rParams.setByteOffset(0);
                rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
                for(int iterCnt=0; iterCnt<5;iterCnt++)
                {
                    try
                    {
                        tidMemData = myReader.Actions.TagAccess.readWait(tagData[0].getTagID(), rParams, null);
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.println(""+opexp.getVendorMessage());
                    }
                    if(tidMemData != null && (tidMemData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS))
                    {
                        System.out.println("TID ID is\t"+tidMemData.getMemoryBankData()+"\t length :"+tidMemData.getMemoryBankDataAllocatedSize());
                        psSummary.println("\nTID ID is\t"+tidMemData.getMemoryBankData()+"\t length :"+tidMemData.getMemoryBankDataAllocatedSize()+"<br>");
                        btid = true;
                        break;
                    }
                }
                
                if(btid == false)
                {
                    continue READAGAIN1;
                }

                if(tidMemData.getMemoryBank() != MEMORY_BANK.MEMORY_BANK_TID)
                {
                    System.out.print("\n This is not the memory Bank we are intented to look for..exiting");
                    return false;
                }
                tag1.tid = hex2Byte(tidMemData.getMemoryBankData());
                tag1.tidlen = 12;//tidMemData.getMemoryBankDataAllocated();//hard code for time being and change latertidMemData.getMemoryBankDataAllocated();
                
                // get user Data
                rParams.setByteCount(0);
                rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                for(int iterCnt=0; iterCnt<5;iterCnt++)
                {
                    try
                    {
                        userMemData = myReader.Actions.TagAccess.readWait(tagData[0].getTagID(), rParams, null);
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.println(""+opexp.getVendorMessage());
                    }
                    if((userMemData != null) && (userMemData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS))
                    {
                        System.out.println("USER ID is\t"+userMemData.getMemoryBankData()+"\tlen :"+userMemData.getMemoryBankDataAllocatedSize());
                        psSummary.println("\nUSER ID is\t"+userMemData.getMemoryBankData()+"\tlen :"+userMemData.getMemoryBankDataAllocatedSize()+"<br>");
                        buser = true;
                        break;
                    }
                }

                if(buser == false)
                {
                    btid = false;
                    continue READAGAIN1;
                }

                if(userMemData.getMemoryBank() != MEMORY_BANK.MEMORY_BANK_USER)
                {
                    System.out.print("\n This is not the memory Bank we are intented to look for..exiting");
                    return false;
                }

                tag1.user = hex2Byte(userMemData.getMemoryBankData());
                tag1.userlen = userMemData.getMemoryBankDataAllocatedSize();
                if( btid && buser)
                {
                    btag1 = true;
                }
                myReader.Actions.purgeTags();
            }
            btid = buser = false;
            myReader.Actions.purgeTags();
            // get the other tag2.
            postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.NOTA_AND_B);
            myReader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(15000);
            myReader.Actions.Inventory.stop();
READAGAIN2:
            while( (tagData = myReader.Actions.getReadTags(1)) != null)
            {
                tag2.CRC = tagData[0].getCRC();
                tag2.PC = tagData[0].getPC();
                tag2.epc = hex2Byte(tagData[0].getTagID());
                tag2.epclen = tagData[0].getTagIDAllocatedSize();
                System.out.println("Tag 2:");                
                psSummary.println("\nTag 2:");
                psSummary.println("");
                System.out.println("tag ID is\t"+tagData[0].getTagID()+"\t len is :"+tag2.epclen);
                psSummary.println("\ntag ID is\t"+tagData[0].getTagID()+"\t len is :"+tag2.epclen+"<br>");
                b22fTag = tagData[0].getTagID();
                
                // get TID Data
                rParams.setAccessPassword(0);
                rParams.setByteCount(0);
                rParams.setByteOffset(0);
                rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
                for(int iterCnt=0; iterCnt<5;iterCnt++)
                {
                    try
                    {
                        tidMemData = myReader.Actions.TagAccess.readWait(tagData[0].getTagID(), rParams, null);
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.println(""+opexp.getVendorMessage());
                    }
                    if(tidMemData != null&& (tidMemData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS))
                    {
                        System.out.println("TID ID is\t"+tidMemData.getMemoryBankData()+"\tlen is :"+tidMemData.getMemoryBankDataAllocatedSize());
                        psSummary.println("\nTID ID is\t"+tidMemData.getMemoryBankData()+"\tlen is :"+tidMemData.getMemoryBankDataAllocatedSize()+"<br>");
                        btid = true;
                        break;
                    }
                }

                if(btid == false)
                {
                    continue READAGAIN2;
                }

                if(tidMemData.getMemoryBank() != MEMORY_BANK.MEMORY_BANK_TID)
                {
                    System.out.print("\n This is not the memory Bank we are intented to look for..exiting");
                    return false;
                }
                tag2.tid = hex2Byte(tidMemData.getMemoryBankData());
                tag2.tidlen = 12;//tidMemData.getMemoryBankDataAllocated();
                // get user Data
                rParams.setByteCount(0);
                rParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                for(int iterCnt=0; iterCnt<5;iterCnt++)
                {
                    try
                    {
                        userMemData = myReader.Actions.TagAccess.readWait(tagData[0].getTagID(), rParams, null);
                    }
                    catch(OperationFailureException opexp)
                    {
                        System.out.println(""+opexp.getVendorMessage());
                    }
                    
                    if(userMemData != null && (userMemData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS))
                    {
                        System.out.println("USER ID is\t"+userMemData.getMemoryBankData()+"len is :"+userMemData.getMemoryBankDataAllocatedSize());                        
                        psSummary.println("\n\nUSER ID is\t"+userMemData.getMemoryBankData()+"len is :"+userMemData.getMemoryBankDataAllocatedSize()+"<br>\n\n");
                       
                        buser = true;
                        break;
                    }
                }

                if(buser == false)
                {
                    btid = false;
                    continue READAGAIN2;
                }

                if(userMemData.getMemoryBank() != MEMORY_BANK.MEMORY_BANK_USER)
                {
                    System.out.print("\n This is not the memory Bank we are intented to look for..exiting");
                    return false;
                }
                tag2.user = hex2Byte(userMemData.getMemoryBankData());
                tag2.userlen = userMemData.getMemoryBankDataAllocatedSize();
                if( btid && buser)
                {
                    btag2 = true;
                }
                myReader.Actions.purgeTags();
                if( btag1 && btag2) return true;
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
            System.out.println(""+opexp.getVendorMessage());
        }
        return false;
    }
  
    public void testSngulationsStateUnAware( )
    {
         // Logger initialization for logging the results
        try
        {
            successCount = 0;
            failureCount = 0;
            mystreamLog=new FileOutputStream("Java API_FiltersStateUnAware_Log.html");
            mystreamResult=new FileOutputStream("Java API_FiltersStateUnAware_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing StateUnAware Singulation------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "StateUnAwareSingulation";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }

        successCount = failureCount =0;
        
        TestNo = 1;SubNo = 0;
        // State unaware singulation with TID Emmory Bank
        byte[] pattern = new byte[4];
        System.arraycopy(tag1.tid, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 0,null);

        if(tag1.tid.length >= 8)
        {
            System.arraycopy(tag1.tid, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 32,null);
        }

        if(tag1.tid.length >= 12)
        {
            System.arraycopy(tag1.tid, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 64,null);
        }

        if(tag1.tid.length >= 16)
        {
            System.arraycopy(tag1.tid, 12, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 96,null);
        }

        if(tag1.tid.length >= 20)
        {
            System.arraycopy(tag1.tid, 16, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 128,null);
        }
        
        // State unaware singulation with EPC Emmory Bank
        System.arraycopy(tag1.epc, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 32,null);

        if(tag1.epc.length >= 8)
        {
            System.arraycopy(tag1.epc, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 64,null);
        }
        if(tag1.epc.length >= 12)
        {
            System.arraycopy(tag1.epc, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 96,null);
        }

         // State unaware singulation with USER Emmory Bank
        System.arraycopy(tag1.user, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 0,null);

        if(tag1.user.length >= 8)
        {
            System.arraycopy(tag1.user, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 32,null);
        }

        if(tag1.user.length >= 12)
        {
            System.arraycopy(tag1.user, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 64,null);
        }

        if(tag1.user.length >= 16)
        {
            System.arraycopy(tag1.user, 12, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 96,null);
        }

        if(tag1.user.length >= 20)
        {
            System.arraycopy(tag1.user, 16, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 128,null);
        }

        if(tag1.user.length >= 24)
        {
            System.arraycopy(tag1.user, 20, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 160,null);
        }

        if(tag1.user.length >= 28)
        {
            System.arraycopy(tag1.user, 24, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 192,null);
        }
        psSummary.println("\nJavaAPI:Test Singulation State Un-Aware:" + successCount + ":" + failureCount + ":" + "0");
    }

    public void testSngulationsStateUnAwareAuto( )
    {
         // Logger initialization for logging the results
        try
        {
            successCount = 0;
            failureCount = 0;
            mystreamLog=new FileOutputStream("Java API_FiltersStateUnAwareAuto_Log.html");
            mystreamResult=new FileOutputStream("Java API_FiltersStateUnAwareAuto_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Testing StateUnAware Autonomous------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "StateUnAwareSingulation-Auto";
        }
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }

        successCount = failureCount =0;


        // State unaware singulation with TID Emmory Bank
        byte[] pattern = new byte[4];
        System.arraycopy(tag1.tid, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 0,tInfo);

        if(tag1.tid.length >= 8)
        {
            System.arraycopy(tag1.tid, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 32,tInfo);
        }

        if(tag1.tid.length >= 12)
        {
            System.arraycopy(tag1.tid, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 64,tInfo);
        }

        if(tag1.tid.length >= 16)
        {
            System.arraycopy(tag1.tid, 12, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 96,tInfo);
        }

        if(tag1.tid.length >= 20)
        {
            System.arraycopy(tag1.tid, 16, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_TID, 128,tInfo);
        }

        // State unaware singulation with EPC Emmory Bank
        System.arraycopy(tag1.epc, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 32,tInfo);

        if(tag1.epc.length >= 8)
        {
            System.arraycopy(tag1.epc, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 64,tInfo);
        }
        if(tag1.epc.length >= 12)
        {
            System.arraycopy(tag1.epc, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_EPC, 96,tInfo);
        }

         // State unaware singulation with USER Emmory Bank
        System.arraycopy(tag1.user, 0, pattern, 0, 4);
        testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 0,tInfo);

        if(tag1.user.length >= 8)
        {
            System.arraycopy(tag1.user, 4, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 32,tInfo);
        }

        if(tag1.user.length >= 12)
        {
            System.arraycopy(tag1.user, 8, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 64,tInfo);
        }

        if(tag1.user.length >= 16)
        {
            System.arraycopy(tag1.user, 12, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 96,tInfo);
        }

        if(tag1.user.length >= 20)
        {
            System.arraycopy(tag1.user, 16, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 128,tInfo);
        }

        if(tag1.user.length >= 24)
        {
            System.arraycopy(tag1.user, 20, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 160,tInfo);
        }

        if(tag1.user.length >= 28)
        {
            System.arraycopy(tag1.user, 24, pattern, 0, 4);
            testStateUnAwareSingulation(pattern, MEMORY_BANK.MEMORY_BANK_USER, 192,tInfo);
        }
        psSummary.println("JavaAPI:Test Singulation State Un-Aware-Auto:" + successCount + ":" + failureCount + ":" + "0");
    }

    public void testPostFiltersRead( )
    {
        successCount=failureCount=0;TestNo = 1;
        myReader.Actions.purgeTags();
        
        // Logger initialization for logging the results
        try
        {
            mystreamLog=new FileOutputStream("Java API_PostFilter_Log.html");
            mystreamResult=new FileOutputStream("Java API_PostFilter_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Post Filters------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "PostFilter with varying length and MB";
            
        }
        
        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }

        byte[] tagMask = new byte[512];
        bytefill( tagMask,(byte)(0xFF));
        int dataLen;
        for( dataLen = 16;dataLen<=96;dataLen=dataLen+16)
        {
            postfilter = new PostFilter();
            //tag Pattern A
            tpA.setBitOffset(32);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.epc,dataLen/8));
//            tpA.setTagPattern(tagPattern1);
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(32);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.epc,dataLen/8));
//            tpB.setTagPattern(tagPattern2);
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            postfilter.setRSSIRangeFilter(false);
            ReadNDisplayTags(rParams, postfilter,null);
        }

        for( dataLen = 16;dataLen<=64;dataLen=dataLen+16)
        {
            //tag Pattern A
            tpA.setBitOffset(0);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.tid,dataLen/8));
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(0);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.tid,dataLen/8));
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            ReadNDisplayTags(rParams, postfilter,null);
        }

        for( dataLen = 32;dataLen<=512;dataLen=dataLen+32)
        {
            //tag Pattern A
            tpA.setBitOffset(0);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.user,dataLen/8));
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(0);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.user,dataLen/8));
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            ReadNDisplayTags(rParams, postfilter,null);
        }
        
        psSummary.println("JavaAPI:Test Post Filters :" + successCount + ":" + failureCount + ":" + "0");
    }

     public void PostFilterAutoMode( )
    {
        successCount=failureCount=0;
        myReader.Actions.purgeTags();
        // Logger initialization for logging the results
        try
        {
            mystreamLog=new FileOutputStream("Java API_PostFilter_AutoLog.html");
            mystreamResult=new FileOutputStream("Java API_PostFilter_AutoResult.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b> "+ (new Date()) +" -------------Post Filters-AutoMode------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "PostFilterAuto";
        }

        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
        tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION);
        tInfo.StopTrigger.setDurationMilliSeconds(3000);
        tInfo.TagEventReportInfo.setReportNewTagEvent(TAG_EVENT_REPORT_TRIGGER.IMMEDIATE);
        tInfo.TagEventReportInfo.setReportTagInvisibleEvent(TAG_EVENT_REPORT_TRIGGER.IMMEDIATE);
        tInfo.TagEventReportInfo.setReportTagBackToVisibilityEvent(TAG_EVENT_REPORT_TRIGGER.IMMEDIATE);
        tInfo.setEnableTagEventReport(true);
        byte[] tagMask = new byte[512];
        bytefill( tagMask,(byte)(0xFF));
        int dataLen;
        for( dataLen = 16;dataLen<=96;dataLen=dataLen+16)
        {
            postfilter = new PostFilter();
            //tag Pattern A
            tpA.setBitOffset(32);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.epc,dataLen/8));
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(32);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.epc,dataLen/8));
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            postfilter.setRSSIRangeFilter(false);
            ReadNDisplayTags(rParams, postfilter,tInfo);
        }

        for( dataLen = 16;dataLen<=96;dataLen=dataLen+16)
        {
            //tag Pattern A
            tpA.setBitOffset(0);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.tid,dataLen/8));
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(0);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_TID);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.tid,dataLen/8));
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            ReadNDisplayTags(rParams, postfilter,tInfo);
        }

        for( dataLen = 32;dataLen<=512;dataLen=dataLen+32)
        {
            //tag Pattern A
            tpA.setBitOffset(0);
            tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
            tpA.setTagMask(readBytes(tagMask,dataLen/8));
            tpA.setTagMaskBitCount(dataLen);
            tpA.setTagPattern(readBytes(tag1.user,dataLen/8));
            tpA.setTagPatternBitCount(dataLen);

            //tag pattern B
            tpB.setBitOffset(0);
            tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
            tpB.setTagMask(readBytes(tagMask,dataLen/8));
            tpB.setTagMaskBitCount(dataLen);
            tpB.setTagPattern(readBytes(tag2.user,dataLen/8));
            tpB.setTagPatternBitCount(dataLen);

             //post filter
            postfilter.TagPatternA = tpA;
            postfilter.TagPatternB = tpB;
            ReadNDisplayTags(rParams, postfilter,tInfo);
        }

        psSummary.println("JavaAPI:Test Post Filters AutoMode:" + successCount + ":" + failureCount + ":" + "0");
    }
}
