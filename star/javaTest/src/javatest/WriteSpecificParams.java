/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
import java.util.Date;
import static javatest.Commonclass.psLog;
/**
 *
 * @author QMTN48
 */
public class WriteSpecificParams extends Commonclass
{
    private TagAccess.WriteSpecificFieldAccessParams wSpecific;
    private TagAccess tagAccess;
    private RFIDReader myReader;
    private byte[] accessPwd = { 0x12,0x34,0x56,0x78 };
    private short[] antennaList  = { 1,2 };
    private OPERATION_QUALIFIER antOPQ[] = { OPERATION_QUALIFIER.C1G2_OPERATION, OPERATION_QUALIFIER.C1G2_OPERATION};
    private boolean apisuccess;
    public void LogSuccessFailureCount()
    {
        psSummary.println("JavaAPI:Test WriteSpecific Parameters:" + successCount + ":" + failureCount + ":" + "0");
    }
    public WriteSpecificParams( RFIDReader r)
    {
        successCount = 0;
        failureCount = 0;
        apisuccess = false;
        myReader = r;
        tagAccess = new TagAccess( );
        wSpecific = tagAccess.new WriteSpecificFieldAccessParams();
    }

    public void testWriteSpecificNegValues()
    {
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
        psLog.println("\n<b>Description</b>:" + logText + "      ");
        psLog.println("\n<b> Expected: negative values test");
        psLog.println("\n<b> Actual Result is :</b><br>");

        // write with null tag is
        try
        {
            wSpecific.setWriteDataLength(4);
            myReader.Actions.TagAccess.writeAccessPasswordWait(null, wSpecific, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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
        //write with null writespecific params
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        try
        {
            myReader.Actions.TagAccess.writeAccessPasswordWait(beddTag, null, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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

        // write with null tag is
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        try
        {
            myReader.Actions.TagAccess.writeKillPasswordWait(null, wSpecific, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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
        //write with null writespecific params
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        try
        {
            myReader.Actions.TagAccess.writeKillPasswordWait(beddTag, null, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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

         // write with null tag is
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        try
        {
            wSpecific.setWriteDataLength(12);
            myReader.Actions.TagAccess.writeTagIDWait(null, wSpecific, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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

        //write with null writespecific params
        FormTestID(TestNo++, SubNo, "WriteSpeci");
        try
        {
            myReader.Actions.TagAccess.writeTagIDWait(beddTag, null, null);
            apisuccess = false;
        }
        catch(InvalidUsageException exp)
        {
            apisuccess =true;
            psLog.println("\n vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            apisuccess =true;
            CleanupPendingSequence();
            psLog.println("\n"+opexp.getStatusDescription());
        }

        if(apisuccess)
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

    public void testWriteSpecificParameters()
    {
        try
        {
            // Logger initialization for logging the results
            try
            {
                mystreamLog=new FileOutputStream("Java API_WriteSpecific_Log.html");
                mystreamResult=new FileOutputStream("Java API_WriteSpecific_Result.txt");
                psLog = new PrintStream(mystreamLog);
                psLog.println("<HTML>\n<BODY>\n");
                psLog.println("<br><b> "+ (new Date()) +" -------------Testing Write Specific Parameters------------ </b><br>");
                psResult = new PrintStream(mystreamResult);
                logText = "WriteSpecificParams";
            }
            catch(FileNotFoundException e)
            {
                psLog.println("\n "+e.getMessage());
            }
            TestNo = 1;
            String tagId = "BEDD11112222333344445555";
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = myReader.Config.getTagStorageSettings();
            tgSettings.enableAccessReports(true);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            tgSettings.setMaxMemoryBankByteCount(64);
            tgSettings.setMaxTagIDLength(24);
            myReader.Config.setTagStorageSettings(tgSettings);
            byte[] writeData = {(byte)0xBE,(byte)0xDD,0x11,0x11,(byte)0x22,0x22,0x33,0x33,(byte)0x44,0x44,0x55,0x55};

            wSpecific.setAccessPassword(0);
            wSpecific.setWriteData(writeData);
            wSpecific.setWriteDataLength(4);

            FormTestID(TestNo++,SubNo,"WriteSpeci");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      ");
            psLog.println("\n<b> Expected: WriteAccessPassword to the tag ID"+beddTag);
            psLog.println("\n<b> Actual Result is :</b><br>");
            myReader.Actions.purgeTags();
            try
            {
                /*write access and kill passwords using specifc write API*/
                myReader.Actions.TagAccess.writeAccessPasswordWait(tagId, wSpecific, null);
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
                if( ReadMemoryBankdData() )
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
            catch(InvalidUsageException exp)
            {
                System.out.println(" vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
                failureCount++;
            }
            catch(OperationFailureException opexp)
            {
                CleanupPendingSequence();
                System.out.println(""+opexp.getStatusDescription());
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }

            FormTestID(TestNo,SubNo++,"WriteSpeci");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      ");
            psLog.println("\n<b> Expected: WriteKillPassword to the tag ID"+beddTag);
            psLog.println("\n<b> Actual Result is :</b><br>");
            try
            {
                /*write access and kill passwords using specifc write API*/
                myReader.Actions.TagAccess.writeKillPasswordWait(tagId, wSpecific, null);
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
                if( ReadMemoryBankdData() )
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
            catch(InvalidUsageException exp)
            {
                System.out.println(" vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
                failureCount++;
            }
            catch(OperationFailureException opexp)
            {
                failureCount++;
                CleanupPendingSequence();
                System.out.println(""+opexp.getStatusDescription());
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
            
            FormTestID(TestNo,SubNo++,"WriteSpeci");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      ");
            psLog.println("\n<b> Expected: writeTagIDWait to the tag ID"+beddTag);
            psLog.println("\n<b> Actual Result is :</b><br>");
            try
            {
                wSpecific.setWriteDataLength(12);
                /*write EPC*/
                myReader.Actions.TagAccess.writeTagIDWait(tagId, wSpecific, null);
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
                if( ReadMemoryBankdData() )
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
            catch(InvalidUsageException exp)
            {
                failureCount++;
                System.out.println(" vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
            }
            catch(OperationFailureException opexp)
            {
                failureCount++;
                CleanupPendingSequence();
                System.out.println(""+opexp.getStatusDescription());
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
        }

        catch(InvalidUsageException exp)
        {
            System.out.println(" vend Msg "+ exp.getVendorMessage()+" errorDes"+exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            CleanupPendingSequence();
            System.out.println(""+opexp.getStatusDescription());
        }
    }

}
