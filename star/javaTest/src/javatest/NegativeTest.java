/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
/**
 *
 * @author NVJ438
 */
public class NegativeTest extends Commonclass{

    public RFIDReader myReader = null;
    public PrintStream myLogger = null;
    ReaderManagement readerMgt;
    LoginInfo login;
    TagAccess tagaccess = new TagAccess();
    TagAccess.ReadAccessParams readParams = tagaccess.new ReadAccessParams();
    TagAccess.WriteAccessParams writeParams = tagaccess.new WriteAccessParams();
    TagAccess.KillAccessParams killParams = tagaccess.new KillAccessParams();
    TagAccess.LockAccessParams lockParams = tagaccess.new LockAccessParams();
    LOCK_PRIVILEGE[] lockPrivilege = new LOCK_PRIVILEGE[5];
    NXP nxp = new NXP(tagaccess);
    NXP.SetEASParams setEASParams = nxp.new SetEASParams();
    NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
    NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();
    AntennaInfo antInfoNXP = new AntennaInfo();
    short[] antList = new short[]{1};
    OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.NXP_EAS_SCAN};

    TagAccess.BlockEraseAccessParams eraseParams = tagaccess.new BlockEraseAccessParams();
    String tagId = null;
    byte writeData[] = {(byte)0xAA,(byte)0xAA,(byte)0xBB,(byte)0xBB,(byte)0xCC,(byte)0xCC,(byte)0xDD,(byte)0xDD,(byte)0xEE,(byte)0xEE};

     private TagData[] tagData;
     public int len=0;

    byte[] tagMask = {(byte)0xFF, (byte)0xFF };
    byte[] tagPattern1 = { (byte)0xa2, (byte)0x2f };
    byte[] tagPattern2 = { (byte)0xFF, (byte)0xFF };
    PostFilter postfilter;

    public NegativeTest()
    {
        try {
            myReader = reader;
            myLogger = psLog;
            readerMgt = new ReaderManagement();
            login = new LoginInfo();
            successCount = 0;
            failureCount = 0;
            TestNo = 111;
            SubNo = 0;
            mystreamLog = new FileOutputStream("JavaAPI_NegativeTest_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_NegativeTest_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            //System_Time......
            SYSTEMTIME ac = new SYSTEMTIME();
            psLog.println("<b>CurrentTime:</b>" + ac.GetCurrentTime());

            postfilter = new PostFilter();

         //tag Pattern A

            postfilter.TagPatternA.setBitOffset(32);
            postfilter.TagPatternA.setTagPattern(tagPattern1);
            postfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            postfilter.TagPatternA.setTagMaskBitCount(tagPattern1.length * 8);
            postfilter.TagPatternA.setTagPatternBitCount((2 * 8));
            postfilter.TagPatternA.setTagMask(tagMask);
            postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);


        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
    }

    public void Test_Nagative()
    {
        psLog.println("<html><br>");
        psLog.println("<body><br>");

        AntennaPowersettings();
        successCount = 0;
        failureCount = 0;
        TestNo = 101;
        SubNo = 1;

        psLog.println("<br>Negative Test cases");
//        System.out.print("\nNegative Test cases");

        Test_ReadWaitNegative();
        Test_ReadEventNegative();
        Test_WriteWaitNegative();
        Test_WriteEventNegative();
        Test_KillWaiteNegative();
        Test_KillEventNegative();
        Test_LockWaitnegative();
        Test_LockEventNegative();
        Test_BlockWriteWaitNegative();
        Test_BlockWriteEventNegative();
        Test_BlockEraseWaitNegative();
        Test_BlockEraseEventNegative();
        Test_EASwaitNegative();
        Test_EASeventNegative();
        Test_readProtectWaitNegative();
        Test_readProtectEventNegative();
        Test_resetReadProtectEventNegative();
        Test_PerformEasScanNegative();

        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:Negative_Testcases" + ":" + successCount + ":" + failureCount + ":0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");
    }
    public void Test_ReadWaitNegative()
    {
        tagId = a22fTag;

        psLog.println("<br> Inside Test_ReadWaitNegative method");
        psLog.println("<br>=========================================");
       
        //1. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with invalid length ");
        psLog.println("<br>Expected Result: Invalid Read byte count ");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_EPC,0,99);
        psResult.println("<br>"+TestID+": ReadWait with invalid length:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with invalid length:PASSED");
        successCount++;

        //2. EPC Invalid offset

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with invalid offset ");
        psLog.println("<br>Expected Result: Invalid Read offset ");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_EPC,1,10);
        psResult.println("<br>"+TestID+": ReadWait with invalid offset:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with invalid offset:PASSED");
        successCount++;

        //3. Empty tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with Empty TagID");
        psLog.println("<br>Expected Result: Empty Parameter");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank("",MEMORY_BANK.MEMORY_BANK_EPC,4,12);
        psResult.println("<br>"+TestID+": ReadWait with Empty TagID:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with Empty TagID:PASSED");
        successCount++;

        //4. null tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with null TagID");
        psLog.println("<br>Expected Result: null TagID");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank(null,MEMORY_BANK.MEMORY_BANK_EPC,4,12);
        psResult.println("<br>"+TestID+": ReadWait with null TagID:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with null TagID:PASSED");
        successCount++;

        //5. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with Reservedmemory  Invalid length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,100);
        psResult.println("<br>"+TestID+": ReadWait with Reservedmemory  Invalid length:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with Reservedmemory  Invalid length:PASSED");
        successCount++;

         //6. Reserved Invalid length

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with Reservedmemory  Invalid offset");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,100,4);
        psResult.println("<br>"+TestID+": ReadWait with Reservedmemory  Invalid offset:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with Reservedmemory  Invalid offset:PASSED");
        successCount++;

        //6. Read Access Params null

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");

       
        try
        {
            TagData membankData = reader.Actions.TagAccess.readWait(tagId, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        
        psResult.println("<br>"+TestID+": ReadWait with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with Access Params null parameter:PASSED");
        successCount++;

         //7. null tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadWait with invalid TagID");
        psLog.println("<br>Expected Result: null TagID");
        psLog.print("<br>Calling ReadWait API");
        psLog.println("<br>Actual Result:");
        ReadMemoryBank("1",MEMORY_BANK.MEMORY_BANK_EPC,4,12);
        psResult.println("<br>"+TestID+": ReadWait with null TagID:PASSED");
        psLog.println("<br>"+TestID+": ReadWait with null TagID:PASSED");
        successCount++;


    }
    public void Test_ReadEventNegative()
    {
        psLog.println("<br><br> Inside Test_ReadEventNegative method");
        psLog.println("<br>=========================================");
         //6. Read Access Params null

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> ReadEvent with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling ReadEvent API");
        psLog.println("<br>Actual Result:");

        try
        {
          reader.Actions.TagAccess.readEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": ReadEvent with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": ReadEvent with Access Params null parameter:PASSED");
        successCount++;

    }
    public void Test_WriteWaitNegative()
    {
        tagId = beddTag;

        psLog.println("<br><br> Inside Test_WriteWaitNegative method");
        psLog.println("<br>=========================================");
        
        //1. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with invalid length ");
        psLog.println("<br>Expected Result: Invalid Write byte count ");
        writeParams.setWriteData(writeData);
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 4, 1);
        psResult.println("<br>"+TestID+": WriteWait with invalid length:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with invalid length:PASSED");
        successCount++;

        //2. EPC Invalid offset
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with invalid offset ");
        psLog.println("<br>Expected Result: Invalid Write byte offset ");
        writeParams.setWriteData(writeData);
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 99, 4);
        psResult.println("<br>"+TestID+": WriteWait with invalid offset:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with invalid offset:PASSED");
        successCount++;

        //3. Empty tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with Empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        writeParams.setWriteData(writeData);
         psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank("",MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": WriteWait with Empty tagID:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with Empty tagID:PASSED");
        successCount++;

        //4. Null tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with Null tagID ");
        psLog.println("<br>Expected Result: Null Parameter");
        writeParams.setWriteData(writeData);
         psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(null,MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": WriteWait with Null tagID:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with Null tagID:PASSED");
        successCount++;

         //5. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with Reservedmemory  Invalid length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,10);
        psResult.println("<br>"+TestID+": WriteWait with Reservedmemory  Invalid length:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with Reservedmemory  Invalid length:PASSED");
        successCount++;

         //6. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with Reservedmemory Invalid offset");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,100,4);
        psResult.println("<br>"+TestID+": WriteWait with Reservedmemory Invalid offset:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with Reservedmemory Invalid offset:PASSED");
        successCount++;

        //7. write Access Params null

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        
        try
        {
            reader.Actions.TagAccess.writeWait(tagId, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": WriteWait with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with Access Params null parameter:PASSED");
        successCount++;

         //8. Zero length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with  zero length");
        psLog.println("<br>Expected Result: writeAccessParams.Length null");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,0);
        psResult.println("<br>"+TestID+": WriteWait with zero length:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with zero length:PASSED");
        successCount++;

        //9. Write wait on TID memory bank
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait on TID memory bank");
        psLog.println("<br>Expected Result: Memory bank locked error");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        String result = WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_TID,0,2);
        if(!result.equals("Tag Memory Locked "))
        {
        psResult.println("<br>"+TestID+": WriteWait on TID memory:PASSED");
        psLog.println("<br>"+TestID+": WriteWait on TID memory:PASSED");
        successCount++;
        }
        else
        {
            psResult.println("<br>"+TestID+": WriteWait on TID memory:FAILED");
            psLog.println("<br>"+TestID+": WriteWait on TID memory:FAILED");
            failureCount++;
        }
        //10. EPC Invalid offset
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteWait with invalid offset ");
        psLog.println("<br>Expected Result: Invalid Write byte offset ");
        writeParams.setWriteData(writeData);
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        WriteMemoryBank(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 99, 4);
        psResult.println("<br>"+TestID+": WriteWait with invalid offset:PASSED");
        psLog.println("<br>"+TestID+": WriteWait with invalid offset:PASSED");
        successCount++;

    }
    public void Test_WriteEventNegative()
    {
        psLog.println("<br><br> Inside Test_WriteEventNegative method");
        psLog.println("<br>=========================================");
        //1. Access param null
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteEvent with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteEvent API");
        psLog.println("<br>Actual Result:");
          try
        {
            reader.Actions.TagAccess.writeEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": WriteEvent with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": WriteEvent with Access Params null parameter:PASSED");
        successCount++;

        //2. Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteEvent with Invalid length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteEvent API");
        psLog.println("<br>Actual Result:");
        WriteEventMemory(MEMORY_BANK.MEMORY_BANK_RESERVED,0,5);
        psResult.println("<br>"+TestID+": WriteEvent with Invalid length:PASSED");
        psLog.println("<br>"+TestID+": WriteEvent with Invalid length:PASSED");
        successCount++;

        //3. Zero length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteEvent with zero length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteEvent API");
        psLog.println("<br>Actual Result:");
        WriteEventMemory(MEMORY_BANK.MEMORY_BANK_RESERVED,0,0);
        psResult.println("<br>"+TestID+": WriteEvent with zero length:PASSED");
        psLog.println("<br>"+TestID+": WriteEvent with zero length:PASSED");
        successCount++;
       
        //4. Write wait on TID memory bank
        tagId="E200";
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> WriteEvent on TID memory bank");
        psLog.println("<br>Expected Result: Memory bank locked error");
        psLog.print("<br>Calling WriteEvent API");
        psLog.println("<br>Actual Result:");
        String resultEvent = WriteEventMemory(MEMORY_BANK.MEMORY_BANK_TID,0,2);
        if(!resultEvent.equals("Tag Memory Locked "))
        {
        psResult.println("<br>"+TestID+": WriteWait on TID memory:PASSED");
        psLog.println("<br>"+TestID+": WriteWait on TID memory:PASSED");
        successCount++;
        }
        else
        {
            psResult.println("<br>"+TestID+": WriteWait on TID memory:FAILED");
            psLog.println("<br>"+TestID+": WriteWait on TID memory:FAILED");
            failureCount++;
        }

    }
    public String WriteEventMemory(MEMORY_BANK mBank, int offset,int dataLen)
    {
        String exp1 = "";
        writeParams.setMemoryBank(mBank);
        writeParams.setWriteDataLength(dataLen);
        writeParams.setByteOffset(offset);
        try
        {
            reader.Actions.TagAccess.writeEvent(writeParams, null, null);
            return exp1;
        }
        catch(InvalidUsageException exp)
        {
            exp1 = exp.getVendorMessage()+" "+exp.getInfo();
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());
            return exp1;

        }
        catch(OperationFailureException opexp)
        {
            exp1 = opexp.getVendorMessage()+ " "+opexp.getStatusDescription();
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
            return exp1;
        }

    }
    public void Test_KillWaiteNegative()
    {
        psLog.println("<br><br> Inside Test_KillWaitNegative method");
        psLog.println("<br>=========================================");

        //1. Null tagID
        killParams.setKillPassword(0xAAAAAAAA);
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> KillWait with Null tagID ");
        psLog.println("<br>Expected Result: Null Parameter");
        psLog.print("<br>Calling KillWait API");
        psLog.println("<br>Actual Result:");
        Killtag(null);
        psResult.println("<br>"+TestID+": KillWait with Null tagID:PASSED");
        psLog.println("<br>"+TestID+": KillWait with Null tagID:PASSED");
        successCount++;

        //2. Empty TagID
        killParams.setKillPassword(0xAAAAAAAA);
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> KillWait with Empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        psLog.print("<br>Calling KillWait API");
        psLog.println("<br>Actual Result:");
        Killtag("");
        psResult.println("<br>"+TestID+": KillWait with Empty tagID :PASSED");
        psLog.println("<br>"+TestID+":KillWait with Empty tagID :PASSED");
        successCount++;

        //3. Invalid length TagID
        killParams.setKillPassword(0xAAAAAAAA);
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> KillWait with invalid length tagID ");
        psLog.println("<br>Expected Result: invalid length tagID");
        psLog.print("<br>Calling KillWait API");
        psLog.println("<br>Actual Result:");
        Killtag("E2000");
        psResult.println("<br>"+TestID+": KillWait with invalid length tagID:PASSED");
        psLog.println("<br>"+TestID+": KillWait with invalid length tagID:PASSED");
        successCount++;
    }
    public void Test_KillEventNegative()
    {
        psLog.println("<br><br> Inside Test_KillEventNegative method");
        psLog.println("<br>=========================================");
        killParams.setKillPassword(0xAAAAAAAA);
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> Kill event Access Param null ");
        psLog.println("<br>Expected Result: Null Parameter");
        psLog.print("<br>Calling KillEvent API");
        psLog.println("<br>Actual Result:");
        try
        {
            reader.Actions.TagAccess.killEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": Kill event Access Param null:PASSED");
        psLog.println("<br>"+TestID+": Kill event Access Param null:PASSED");
        successCount++;

    }
    public void Test_LockWaitnegative()
    {
        psLog.println("<br><br> Inside Test_LockWaitNegative method");
        psLog.println("<br>=========================================");

        //1. Null tag ID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b>Lock Wait with null tagID ");
        psLog.println("<br>Expected Result: Null Parameter");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        lockParams.setAccessPassword(0xAAAAAAAA);
        String s = LockTag(null);
        psResult.println("<br>"+TestID+": Lock Wait with null tagID:PASSED");
        psLog.println("<br>"+TestID+": Lock Wait with null tagID:PASSED");
        successCount++;

        //2. Empty TagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b>Lock Wait with empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        LockTag("");
        psResult.println("<br>"+TestID+": Lock Wait with empty tagID:PASSED");
        psLog.println("<br>"+TestID+": Lock Wait with empty tagID:PASSED");
        successCount++;

        //3. Lock params null
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b>Lock Wait with Access Param null ");
        psLog.println("<br>Expected Result: Access Param Null Parameter");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        try
        {
            reader.Actions.TagAccess.lockWait(tagId, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }

        psResult.println("<br>"+TestID+": Lock Wait with Access Param null:PASSED");
        psLog.println("<br>"+TestID+": Lock Wait with Access Param null:PASSED");
        successCount++;

        //4. Locking a memory bank which is permanently locked
        FormTestID(TestNo, SubNo++, "NEG");
        lockParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_TID_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
        psLog.println("<br><b>Description:</b>Locking a memory bank which is permanently locked ");
        psLog.println("<br>Expected Result: Memory bank locked error");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        s = LockTag(tagId);
        if(!s.equals("Tag lock failed"))
        {
            psResult.println("<br>"+TestID+": Locking memory bank which is permanently locked:PASSED");
            psLog.println("<br>"+TestID+": Locking memory bank which is permanently locked:PASSED");
            successCount++;
        }
        //2. Empty TagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b>Lock Wait with empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        LockTag("E");
        psResult.println("<br>"+TestID+": Lock Wait with empty tagID:PASSED");
        psLog.println("<br>"+TestID+": Lock Wait with empty tagID:PASSED");
        successCount++;

    }
    public void Test_LockEventNegative()
    {
        psLog.println("<br><br> Inside Test_LockEventNegative method");
        psLog.println("<br>=========================================");

        //3. Lock params null
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b>Lock Event with Access Param null ");
        psLog.println("<br>Expected Result: Access Param Null Parameter");
        psLog.print("<br>Calling LockWait API");
        psLog.println("<br>Actual Result:");
        try
        {
            reader.Actions.TagAccess.lockEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": Lock Event with Access Param null:PASSED");
        psLog.println("<br>"+TestID+": Lock Event with Access Param null:PASSED");
        successCount++;
    }
    
    public void Test_BlockWriteWaitNegative()
    {
        psLog.println("<br><br> Inside Test_BlockWriteWaittNegative method");
        psLog.println("<br>=========================================");
         //1. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with invalid length ");
        psLog.println("<br>Expected Result: Invalid Write byte count ");
        writeParams.setWriteData(writeData);
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 4, 1);
        psResult.println("<br>"+TestID+": BlockWriteWait with invalid length :PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with invalid length :PASSED");
        successCount++;

        //2. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with invalid offset ");
        psLog.println("<br>Expected Result: Invalid Write byte offset ");
        writeParams.setWriteData(writeData);
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 99, 4);
        psResult.println("<br>"+TestID+": BlockWriteWait with invalid offset:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with invalid offset:PASSED");
        successCount++;

        //3. Empty tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with Empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        writeParams.setWriteData(writeData);
         psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite("",MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": BlockWriteWait with Empty tagID :PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with Empty tagID :PASSED");
        successCount++;

        //4. Null tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with Null tagID ");
        psLog.println("<br>Expected Result: Null Parameter");
        writeParams.setWriteData(writeData);
         psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(null,MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": BlockWriteWait with Null tagID:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with Null tagID:PASSED");
        successCount++;

         //5. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait Invalid length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,10);
        psResult.println("<br>"+TestID+": BlockWriteWait Invalid length:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait Invalid length:PASSED");
        successCount++;

         //6. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait Invalid offset");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,100,4);
        psResult.println("<br>"+TestID+": BlockWriteWait Invalid offset:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait Invalid offset:PASSED");
        successCount++;

        //7. write Access Params null

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");

        try
        {
            reader.Actions.TagAccess.writeWait(tagId, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        
        psResult.println("<br>"+TestID+": BlockWriteWait with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with Access Params null parameter:PASSED");
        successCount++;

         //7. Zero length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteWait with Reservedmemory  zero length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        BlockWrite(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,0);
        psResult.println("<br>"+TestID+": BlockWriteWait with Reservedmemory  zero length:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteWait with Reservedmemory  zero length:PASSED");
        successCount++;


    }
    public void Test_BlockWriteEventNegative()
    {
        psLog.println("<br><br> Inside Test_BlockWriteEventNegative method");
        psLog.println("<br>=========================================");
        //1. write Access Params null

        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockWriteEvent with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling BlockWriteWait API");
        psLog.println("<br>Actual Result:");

        try
        {
            reader.Actions.TagAccess.blockWriteEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": BlockWriteEvent with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": BlockWriteEvent with Access Params null parameter:PASSED");
        successCount++;

    }
    public void Test_BlockEraseWaitNegative()
    {
        psLog.println("<br><br> Inside Test_BlockEraseWaitNegative method");
        psLog.println("<br>=========================================");
          //1. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with invalid length ");
        psLog.println("<br>Expected Result: Invalid Write byte count ");
        writeParams.setWriteData(writeData);
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 4, 1);
        psResult.println("<br>"+TestID+": BlockEraseWait with invalid length:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with invalid length:PASSED");
        successCount++;

        //2. EPC Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with invalid offset ");
        psLog.println("<br>Expected Result: Invalid Write byte offset ");
        writeParams.setWriteData(writeData);
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 99, 4);
        psResult.println("<br>"+TestID+": BlockEraseWait with invalid offset:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with invalid offset:PASSED");
        successCount++;

        //3. Empty tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Empty tagID ");
        psLog.println("<br>Expected Result: Empty Parameter");
        writeParams.setWriteData(writeData);
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": BlockEraseWait with Empty tagID:PASSED");
        psLog.println("<br>"+TestID+": RBlockEraseWait with Empty tagID:PASSED");
        successCount++;

        //4. Null tagID
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Null tagID ");
        psLog.println("<br>Expected Result: Null Parameter");
        writeParams.setWriteData(writeData);
        psLog.println("<br>Actual Result:");
        BlockErase(null,MEMORY_BANK.MEMORY_BANK_EPC, 4, 4);
        psResult.println("<br>"+TestID+": BlockEraseWait with Null tagID :PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with Null tagID :PASSED");
        successCount++;

         //5. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Reservedmemory  Invalid length");
        psLog.println("<br>Expected Result: ");
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,10);
        psResult.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  Invalid length:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  Invalid length:PASSED");
        successCount++;

         //6. Reserved Invalid length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Reservedmemory  Invalid offset");
        psLog.println("<br>Expected Result: ");
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,100,4);
        psResult.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  Invalid offset:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  Invalid offset:PASSED");
        successCount++;

        //7. write Access Params null
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.println("<br>Actual Result:");

        try
        {
            reader.Actions.TagAccess.blockEraseWait(tagId, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": BlockEraseWait with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with Access Params null parameter:PASSED");
        successCount++;

         //7. Zero length
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseWait with Reservedmemory  zero length");
        psLog.println("<br>Expected Result: ");
        psLog.print("<br>Calling WriteWait API");
        psLog.println("<br>Actual Result:");
        BlockErase(tagId,MEMORY_BANK.MEMORY_BANK_RESERVED,0,0);
        psResult.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  zero length:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseWait with Reservedmemory  zero length:PASSED");
        successCount++;

    }
    public void Test_BlockEraseEventNegative()
    {
        psLog.println("<br><br> Inside Test_BlockEraseEventNegative method");
        psLog.println("<br>=========================================");
        //1. Null erase params
        FormTestID(TestNo, SubNo++, "NEG");
        psLog.println("<br><b>Description:</b> BlockEraseEvent with Access Params null parameter");
        psLog.println("<br>Expected Result: ");
        psLog.println("<br>Actual Result:");

        try
        {
            reader.Actions.TagAccess.blockEraseEvent(null, null, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
        psResult.println("<br>"+TestID+": BlockEraseEvent with Access Params null parameter:PASSED");
        psLog.println("<br>"+TestID+": BlockEraseEvent with Access Params null parameter:PASSED");
        successCount++;
    }
    public void Test_EASwaitNegative()
    {
        psLog.println("<br><br> Inside Test_EASwaitNegative method");
        psLog.println("<br>=========================================");
        //1. Tag ID Null
       
        nxpEventsListener nxplistener = new nxpEventsListener(reader,psLog);
        reader.Events.addEventsListener(nxplistener);
        reader.Events.setTagReadEvent(true);
        reader.Events.setEASAlarmEvent(true);
         tagId = null;
         //String tagId = "111122223333444455556666";

             antList = new short[]{1};
            OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.NXP_EAS_SCAN};

            antInfoNXP.setAntennaID(antList);
            antInfoNXP.setAntennaOperationQualifier(opq);


        //1. correct access password
        setEASParams.setAccessPassword(00000001);
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());
        EAS(setEASParams,antInfoNXP);

        //2.
        tagId ="";
        EAS(setEASParams,antInfoNXP);
        //3.
        tagId ="1";
        EAS(setEASParams,antInfoNXP);
        //4.
        tagId ="1111";
         EAS(null,antInfoNXP);

    }
    public String EAS(NXP.SetEASParams setEASParams,AntennaInfo antInfoNXP)
    {
        NXP.SetEASParams setEASParams1 = setEASParams;
        AntennaInfo antInfoNXP1 = antInfoNXP;
        String ep1 = "";
         try {

            reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams1, antInfoNXP1);
            return ep1;

        } catch (InvalidUsageException exp) {
            ep1 = exp.getVendorMessage()+" "+exp.getInfo();
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
            return ep1;
        } catch (OperationFailureException exp) {
            ep1 = exp.getVendorMessage()+" "+exp.getMessage();
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
            return ep1;
        }
    }
    public void Test_EASeventNegative()
    {
        psLog.println("<br><br> Inside Test_EASEventNegative method");
        psLog.println("<br>=========================================");
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());

        try {

            reader.Actions.TagAccess.NXP.setEASEvent(null, null, antInfo);

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        }
         
    }
    public void Test_readProtectWaitNegative()
    {
        psLog.println("<br><br> Inside Test_readProtectWaitNegative method");
        psLog.println("<br>=========================================");
        readProtectParams.setAccessPassword(0xABCDABCD);
        setEASParams.setAccessPassword(0xABCDABCD);
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());

        setEASParams.setEAS(true);
        EAS(setEASParams,antInfoNXP);
        //1. tagID null
        tagId = null;
        readProtectWait(readProtectParams,antInfo);
        //2. Tag ID empty
        tagId = "";
        readProtectWait(readProtectParams,antInfo);
        //3. Tag ID invalid length
        tagId = "F";
        readProtectWait(readProtectParams,antInfo);
        //4.
        readProtectParams = null;
        readProtectWait(readProtectParams,antInfo);

    }
    public void readProtectWait(NXP.ReadProtectParams readProtectParams,AntennaInfo antInfo)
    {
        try {
            reader.Actions.TagAccess.NXP.readProtectWait(tagId, readProtectParams, antInfo);
        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        }

    }
    public void Test_readProtectEventNegative()
    {
        psLog.println("<br><br> Inside Test_readProtectEventNegative method");
        psLog.println("<br>=========================================");
         setEASParams.setAccessPassword(0xABCDABCD);
        //readProtectParams.setAccessPassword(0x00000001);
       
        psLog.println("getAccessPassword: " + setEASParams.getAccessPassword());
        setEASParams.setEAS(true);
        psLog.println("<br>isEASSet: " + setEASParams.isEASSet());

        setEASParams.setEAS(true);
        EAS(setEASParams,antInfoNXP);
        tagId = "FFFF";
        //1. tagID null
        
         try {

            reader.Actions.TagAccess.NXP.readProtectEvent(null, null, antInfo);

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        }

        
        

    }
    public void Test_resetReadProtectEventNegative()
    {
         psLog.println("<br><br> Inside Test_ResetreadProtectEventNegative method");
        psLog.println("<br>=========================================");
        try {

            reader.Actions.TagAccess.NXP.resetReadProtectEvent(null, antInfo);

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        }

    }
    public void Test_PerformEasScanNegative()
    {
        psLog.println("<br><br> Inside Test_PerformEasScanNegative method");
        psLog.println("<br>=========================================");
         try {

            reader.Actions.TagAccess.NXP.setEASEvent(setEASParams, null, antInfo);
            Thread.sleep(10000);
            psLog.println("\nSet EAS done");
            reader.Actions.TagAccess.NXP.performEASScan();
            Thread.sleep(10000);
            reader.Actions.TagAccess.NXP.stopEASScan();
             Thread.sleep(10000);
           
        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage());
            psLog.println("<br>VendorMessage" + exp.getVendorMessage());
        }
         catch (InterruptedException e) {
        }
    }
    public void BlockErase(String tagEPC,MEMORY_BANK mBank, int offset,int dataLen)
    {
        eraseParams.setMemoryBank(mBank);
        eraseParams.setByteOffset(offset);
        eraseParams.setByteCount(dataLen);
        psLog.print("<br>Calling BlockEraseWait API");
        try
        {
            reader.Actions.TagAccess.blockEraseWait(tagEPC, eraseParams, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }

    }
    public void BlockWrite(String tagEPC,MEMORY_BANK mBank, int offset,int dataLen)
    {
        writeParams.setMemoryBank(mBank);
        writeParams.setWriteDataLength(dataLen);
        writeParams.setByteOffset(offset);
        try
        {
            reader.Actions.TagAccess.blockWriteWait(tagEPC, writeParams, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
    }
    public String LockTag(String tagEPC)
    {
        String exp1 = "";
        try
        {
            reader.Actions.TagAccess.lockWait(tagEPC, lockParams, null);
            return exp1;
        }
        catch(InvalidUsageException exp)
        {
            exp1 = exp.getVendorMessage()+ " "+exp.getInfo();
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());
            return exp1;

        }
        catch(OperationFailureException opexp)
        {
            exp1 = opexp.getVendorMessage()+ " "+opexp.getStatusDescription();
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
            return exp1;

        }
    }
    public void Killtag(String tagEPC)
    {
        try
        {
            reader.Actions.TagAccess.killWait(tagEPC, killParams, null);
        }
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }
    }

  
   public void ReadMemoryBank(String tagEPC,MEMORY_BANK mBank, int offset,int dataLen)
    {
        readParams.setAccessPassword(0);
        readParams.setByteCount(dataLen);
        readParams.setByteOffset(offset);
        readParams.setMemoryBank(mBank);

        try
        {
            TagData membankData = new TagData();
            
            membankData = reader.Actions.TagAccess.readWait(tagEPC, readParams, null);
            if(membankData.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
            {
                psLog.println("<br> memBank data\t"+membankData.getMemoryBank()+"    "+membankData.getMemoryBankData()+"\tmemBank allocated  :"+
                        membankData.getMemoryBankDataAllocatedSize());
            }
        }
        
        catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }

    }
   public String WriteMemoryBank(String tagEPC,MEMORY_BANK mBank, int offset,int dataLen)
   {
        String exp1 = null;
        writeParams.setMemoryBank(mBank);
        writeParams.setWriteDataLength(dataLen);
        writeParams.setByteOffset(offset);
        try
        {
            reader.Actions.TagAccess.writeWait(tagEPC, writeParams, null);
            return exp1;
        }
        catch(InvalidUsageException exp)
        {
            exp1 = exp.getVendorMessage()+"  "+exp.getInfo();
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());
            return exp1;

        }
        catch(OperationFailureException opexp)
        {
            exp1 = opexp.getVendorMessage()+"  "+opexp.getStatusDescription();
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
            return exp1;
        }

   }
   public void getLastError()
   {
       int[] pass = new int[1]; int[] fail = new int[1];
       try{
           reader.Actions.TagAccess.getLastAccessResult(pass, fail);
       }
       catch(InvalidUsageException exp)
        {
            psLog.println("<br>"+ exp.getVendorMessage());
            psLog.println("<br>"+ exp.getInfo());

        }
        catch(OperationFailureException opexp)
        {
            psLog.println("<br>"+opexp.getStatusDescription());
            psLog.println("<br>"+ opexp.getVendorMessage());
        }

   }
}
