/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import java.io.*;
import com.mot.rfid.api3.*;
import java.util.Date;
import static javatest.Main.psLog;
import javatest.ConnectReader;
import java.util.Calendar;
import java.text.SimpleDateFormat;
/**
 *
 * @author NVJ438
 */
class ManualResetEvent {

    private final Object monitor = new Object();
    private volatile boolean open = false;

    public ManualResetEvent(boolean open) {
        this.open = open;
    }

    public void waitOne() throws InterruptedException {
        synchronized (monitor) {
            while (open == false) {
                monitor.wait();
            }
        }
    }

    public void startWait() {
        try {
            while (!open) {
                wait();
            }
        } catch (InterruptedException exc) {
            System.out.println("wait() interrupted");
        }
    }

    public void set() {
        //open start
        synchronized (monitor) {
            open = true;
            monitor.notifyAll();
        }
    }

    public void reset() {
        //close stop
        open = false;
    }
}

 public class Commonclass {

    public long TestNo;
    public long SubNo;
    public String TestType;
    public String TestID;
    int successCount;
    int failureCount;
    public static String logText;
    public static RFIDReader reader = null;
    public static PrintStream psLog = null;
    public static PrintStream psResult = null;
    public static PrintStream psSummary = null;
    public static ReaderManagement readerMgt = null;
    public static FileOutputStream mystreamLog;
    public static FileOutputStream mystreamResult;
    public static FileOutputStream mystreamSummary;
    public static AntennaInfo antInfo;
    public int success;
    public int failure;
    public static String a22fTag;
    public static String b22fTag;
    public static String beddTag;
    public static TagData td;
    public static READER_TYPE ReaderType;
    public static String ip = "";
    public static boolean RdrSecureMode = false;
    public static short[] antList;    
    
    TagAccess tagaccess;
    TagAccess.ReadAccessParams readParams;
    TagAccess.WriteAccessParams writeParams;
    TagAccess.BlockEraseAccessParams EraseAccessParams;
    TagData readAccessTag;
    AccessFilter accessfilter;
    
    Commonclass() {
        tagaccess = new TagAccess();
        readParams = tagaccess.new ReadAccessParams();
        writeParams = tagaccess.new WriteAccessParams();
        EraseAccessParams = tagaccess.new BlockEraseAccessParams();
        readAccessTag = new TagData();
        accessfilter = new AccessFilter();
         
        a22fTag = b22fTag = beddTag = ""; // Tag Id .. 
        TestNo = SubNo = successCount = failureCount = 0; // Test Case number tracking.
        success = 1;  // return status of function
        failure = 0;  // return status of function
    }

    // Common routine to ananlyze the exception values, if the file pointer is NULL only console message is displayed.
     public void AnalyseException(PrintStream Log, Exception e) {
        
        if (e.toString().contains("OperationFailureException")) {
            OperationFailureException opfe = (OperationFailureException) e;
            //System.out.print("\nOperationFailureException:" + "| getMessage: " + opfe.getMessage() + "| getLocalizedMessage: " + opfe.getLocalizedMessage() + "| VendorMsg: " + opfe.getVendorMessage() + "| getStatusDescription: " + opfe.getStatusDescription() + "| toString: " + opfe.toString() + "| getTimeStamp: " + opfe.getTimeStamp());
            if (psLog != null) {
                psLog.println("<br> OperationFailureException:" +  opfe.getStatusDescription() );
            }
        }
        if (e.toString().contains("InvalidUsageException")) {
            InvalidUsageException iue = (InvalidUsageException) e;
            //System.out.print("\nInvalidUsageException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| VendorMsg: " + iue.getVendorMessage() + "| toString: " + iue.toString() + "| getTimeStamp: " + iue.getTimeStamp());
            if (psLog != null) {
                psLog.println("<br> InvalidUsageException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| VendorMsg: " + iue.getVendorMessage() + "| toString: " + iue.toString() + "| getTimeStamp: " + iue.getTimeStamp());
            }
        }
        if (e.toString().contains("FileNotFoundException")) {
            FileNotFoundException iue = (FileNotFoundException) e;
            //System.out.print("\nFileNotFoundException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            if (psLog != null) {
                psLog.println("<br> FileNotFoundException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            }
        }
        if(e.toString().contains("java.lang.NullPointerException")){
            NullPointerException iue = (NullPointerException) e;
            //System.out.print("\nNullPointerException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            if (psLog != null) {
                psLog.println("<br> NullPointerException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            }
        }
        if (e.toString().contains("ArrayIndexOutOfBoundsException")){
            ArrayIndexOutOfBoundsException iue = (ArrayIndexOutOfBoundsException) e;
            //System.out.print("\nArrayIndexOutOfBoundsException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            if (psLog != null) {
                psLog.println("<br> ArrayIndexOutOfBoundsException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            }
        }

        if (e.toString().contains("InterruptedException")){
            InterruptedException iue = (InterruptedException) e;
            System.out.print("\nInterruptedException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            if (psLog != null) {
                psLog.println("InterruptedException:" + "| getMessage: " + iue.getMessage() + "| getLocalizedMessage: " + iue.getLocalizedMessage() + "| toString: " + iue.toString());
            }
        }
//        if (e.toString().("Exception") ) {
//            Exception te = (Exception) e;
//            System.out.print("Exception:" + "| getMessage: " + te.getMessage() + "| getLocalizedMessage: " + te.getLocalizedMessage() + "| toString: " + te.toString());
//            if (psLog != null) {
//                psLog.println("Exception:" + "| getMessage: " + te.getMessage() + "| getLocalizedMessage: " + te.getLocalizedMessage() + "| toString: " + te.toString());
//            }
//        }

//        if(e.getLocalizedMessage().equals("Functionality Not Supported by Reader"))
//            {
//                psLog.println("<br>" + TestID + ":Functionality Not Supported by Reader");
//                psResult.println(TestID + ":Functionality Not Supported by Reader");
//            }
//        CleanupPendingSequence();
        // In situations where reader gets disconnected and regression stops. But not suggested.
        if (!reader.isConnected()) {
            try {
                Thread.sleep(90000);// Waiting time for the Reader to come up after unwarranted Crash or Reboot during Regression
                ConnectReader rc = new ConnectReader();
                rc.connectReaderUnSecureMode();
            } catch (InterruptedException ie) {
                System.out.println("InterruptedException: " + "| getMessage: " + ie.getMessage() + "| getLocalizedMessage: " + ie.getLocalizedMessage() + "| toString: " + ie.toString());
            }
        }

    }

    public void close() {
        psLog.println("<br><b> " + (new Date()) + "</b><br>");
        psResult.println("\n" + new Date());
        psLog.println("</html>");
        psLog.println("</body>");
        psLog.close();
        psResult.close();
    }

    public void AppendText() {
        try {

            String OS = System.getProperty("os.name");
            psLog.println("<HTML><BODY>");
            psLog.println("<br><b> " + (new Date()) + "</b><br>");
            psLog.println("OS: " + OS.toString() + "\n");
            psLog.println("C dll version Info : " + reader.versionInfo().getVersion() + "    Reader IP: " + reader.getHostName());
            psLog.println("Reader Model : " + reader.ReaderCapabilities.getModelName());
            psLog.println("FW Version : " + reader.ReaderCapabilities.getFirwareVersion());
            psLog.println("isConnected : " + reader.isConnected());
            psLog.println("Secure Connection : " + reader.SecureConnectionInfo.isSecureModeEnabled());
            psLog.println();
            psLog.println();

            psResult.println(new Date());
            psResult.println("OS: " + OS.toString() + "\n");
            psResult.println("C dll version Info : " + reader.versionInfo().getVersion() + "    Reader IP: " + reader.getHostName());
            psResult.println("Reader Model : " + reader.ReaderCapabilities.getModelName());
            psResult.println("FW Version : " + reader.ReaderCapabilities.getFirwareVersion());
            psResult.println("isConnected : " + reader.isConnected());
            psResult.println("Secure Connection : " + reader.SecureConnectionInfo.isSecureModeEnabled());
            psResult.println();
            psResult.println();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }

    public void LogStatus(boolean bSuccess, String testcase, short i) {
        if (i != 0) {
            if (bSuccess) {
                psLog.println("<br>" + TestID + ":"+testcase+" for Antenna " + i +":PASSED");                        
                psResult.println(TestID + ":" + testcase + " for Antenna " + i + ":PASSED");
                successCount++;
            } else {
                psLog.println("<br>" + TestID + ":" + testcase + " for Antenna " + i + ":FAILED");
                psResult.println(TestID + ":" + testcase + " for Antenna " + i + ":FAILED");
                failureCount++;
            } 

        } else {
            if (bSuccess ) {
                psLog.println("<br>" + TestID + ":" + testcase + ":   PASSED");
                psResult.println(TestID + ":" + testcase + ":   PASSED");
                successCount++;
            } else {
                psLog.println("<br>" + TestID + ":" + testcase + ":   FAILED");
                psResult.println(TestID + ":" + testcase + ":   FAILED");
                failureCount++;
            } 
        }
    }
    
public void LogStatus(String TestID, String logText, boolean bSuccess) {
        if (bSuccess) {
            psLog.println("<br>" + TestID + ":" + logText + ":   PASSED");
            psResult.println(TestID + ":" + logText + ":   PASSED");
            //System.out.print("\n" + TestID + ":" + logText + ":   PASSED");
            successCount++;
        } else {
            psLog.println("<br>" + TestID + ":" + logText + ":   FAILED");
            psResult.println(TestID + ":" + logText + ":   FAILED");
            //System.out.print("\n" + TestID + ":" + logText + ":   FAILED");
            failureCount++;
        }
    }
        

    public void setantInfo() {
        antInfo = new AntennaInfo();
        antList = new short[]{1};
        OPERATION_QUALIFIER[] opq = {OPERATION_QUALIFIER.C1G2_OPERATION, OPERATION_QUALIFIER.C1G2_OPERATION};
        antInfo.setAntennaID(antList);
        //antInfo.setAntennaOperationQualifier(opq);
        antList = antInfo.getAntennaID();
        System.out.print("#######################################" + antList[0]);
        System.out.print("\n" + "#######################################" + antInfo.getAntennaID());
    }

    public short[] String2Short(String hex) {
        hex = hex.toLowerCase();
        short[] buf = new short[hex.length() / 2];
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (short) (((Character.digit(hex.charAt(j++), 16) << 4)
                    | Character.digit(hex.charAt(j++), 16)) & 0xff);
        }
        return buf;
    }

    public byte[] readBytes(byte[] buf, int number) {
        byte[] newbuf = new byte[number];
        for (int i = 0; i < number; i++) {
            newbuf[i] = buf[i];
        }

        return newbuf;
    }

    public byte[] read2OffSet(byte[] buf, int offset, int number) {
        byte[] newbuf = new byte[number];
        for (int i = 0; i < number; i++) {
            newbuf[i] = buf[offset + i];
        }

        return newbuf;
    }

    public byte[] hex2Byte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void bytefill(byte[] array, byte value) {
        int len = array.length;
        if (len > 0) {
            array[0] = value;
        }
        for (int i = 1; i < len; i += i) {
            System.arraycopy(array, 0, array, i, ((len - i) < i) ? (len - i) : i);
        }
    }

    public String byte2Hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    public int ReadTagAvailability(String TagAvail) {


        String tagAvail = TagAvail;
        byte value = (byte) 0xFF;
        byte tag[] = hex2Byte(tagAvail);
        int k = tag.length;
        byte[] mask = new byte[k];
        //byte mask[tag[].length;];
        bytefill(mask, value);  //byte[] array, byte value)
        System.out.println(" Checking for " + tagAvail + " in the field");

        try {
            byte tp[] = {(byte) 0xBE, (byte) 0xDD};
            byte mask1[] = {(byte) 0xFF, (byte) 0xFF};
            PostFilter postfilter = new PostFilter();
            //tag Pattern A
            postfilter.TagPatternA.setBitOffset(32);
            postfilter.TagPatternA.setTagPattern(tag);
            postfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            postfilter.TagPatternA.setTagMaskBitCount(96);
            postfilter.TagPatternA.setTagPatternBitCount(96);
            postfilter.TagPatternA.setTagMask(mask1);
            postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            postfilter.setRSSIRangeFilter(false);
            reader.Config.resetFactoryDefaults();
            reader.Events.setAttachTagDataWithReadEvent(false);
            reader.Actions.Inventory.perform(postfilter, null, null);
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();

            TagData mytags[] = new TagData[1];
            mytags = reader.Actions.getReadTags(1);
            if (mytags != null) {
                beddTag = mytags[0].getTagID();
                System.out.println(" Tag " + beddTag +" is present in the FOV");
                reader.Actions.purgeTags();
                return 1;
            } else {
                return 0;
            }
        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        return 0;
                    }
        }
//        byte tagUser[] = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF, (byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55, (byte) 0x66, (byte) 0x66, (byte) 0x77, (byte) 0x77, (byte) 0x88, (byte) 0x88, (byte) 0x99, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0xAA, (byte) 0xAA, (byte) 0xBB, (byte) 0xBB, (byte) 0xCC, (byte) 0xCC, (byte) 0xDD, (byte) 0xDD, (byte) 0xEE, (byte) 0xEE, (byte) 0xFF, (byte) 0xFF};
//        byte tagReserved[] = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44};
//        if(tagAvail.equals("111122223333444455556666"))
//        {
//            if((CheckTagPostfilter(tag,32,MEMORY_BANK.MEMORY_BANK_EPC,16,16,mask) == 1 ))
//                    //(CheckTagPostfilter(tagReserved,0,MEMORY_BANK.MEMORY_BANK_RESERVED,16,16,mask) == 1) &&
//                      //  (CheckTagPostfilter(tagUser,0,MEMORY_BANK.MEMORY_BANK_USER,16,64,mask) == 1))
//
//
//            {
//                ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED,0, 4, tagAvail );
//                TagData mytags[] = new TagData[1];
//                mytags = reader.Actions.getReadTags(1);
//                if(mytags != null)
//                {System.out.println(mytags[0].getMemoryBankData());}
//                return 1;
//            }
//            else{return 0;}
//        }else{CheckTagPostfilter(tag,32,MEMORY_BANK.MEMORY_BANK_EPC,16,16,mask);}
//        return 0;
//    }

    public int Check1111Tag(String id) {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_Log.html");
            psLog = new PrintStream(mystreamLog);

        } catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
        for (int i = 0; i < 3; i++) {
            try {
                boolean retry = false;
                String readdata1, readdata2, readdata3;
                String userid = "1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF1111222233334444555566667777888899990000AAAABBBBCCCCDDDDEEEEFFFF";


                if (retry == true) {
                    break;
                }
                int status = ReadWait(MEMORY_BANK.MEMORY_BANK_RESERVED, 0, 8, id);
                readdata1 = readAccessTag.getMemoryBankData();


                status = ReadWait(MEMORY_BANK.MEMORY_BANK_EPC, 4, 12, id);
                readdata2 = readAccessTag.getMemoryBankData();

                status = ReadWait(MEMORY_BANK.MEMORY_BANK_USER, 0, 64, id);
                readdata3 = readAccessTag.getMemoryBankData();

                if (!(readdata1.equals("1111222233334444") && readdata2.equals("111122223333444455556666") && readdata3.equals("userid"))) {
                    retry = true;
                    return 1;
                }

                return 0;




            } 
            catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        return 0;
                    }
        }
        return 0;
    }

    public void Java_getBEDDTag() {
        mytag tag1, tag2;
        //two tags
        tag1 = new mytag(2, 2, 12, 24, 8, 64);
        tag2 = new mytag(2, 2, 12, 24, 8, 64);
        boolean btid = false;
        try {
            mystreamLog = new FileOutputStream("JavaAPI_Log.html");
            psLog = new PrintStream(mystreamLog);

        } catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
        try {
            byte tp[] = {(byte) 0xBE, (byte) 0xDD};
            byte mask[] = {(byte) 0xFF, (byte) 0xFF};



            PostFilter pf = new PostFilter();
            //tag Pattern A
            pf.TagPatternA.setBitOffset(32);
            pf.TagPatternA.setTagPattern(tp);
            pf.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            pf.TagPatternA.setTagMaskBitCount(16);
            pf.TagPatternA.setTagPatternBitCount(16);
            pf.TagPatternA.setTagMask(mask);
            pf.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            pf.setRSSIRangeFilter(false);
            //reader.Config.resetFactoryDefaults();

            reader.Actions.Inventory.perform(pf, null, null);
            Thread.sleep(5000);
            reader.Actions.Inventory.stop();

            TagData mytags[] = new TagData[1];

            READAGAIN1:
            while ((mytags = reader.Actions.getReadTags(1)) != null) {
                tag1.CRC = mytags[0].getCRC();
                tag1.PC = mytags[0].getPC();

                tag1.epc = hex2Byte(mytags[0].getTagID());
                tag1.epclen = mytags[0].getTagIDAllocatedSize();
                System.out.print("tag ID is\t" + mytags[0].getTagID() + "\t length :" + tag1.epclen);
                psLog.println("tag ID is\t" + mytags[0].getTagID() + "\t length :" + tag1.epclen + "<br>");
                a22fTag = mytags[0].getTagID();
                // get TID Data
                //readParams.setAccessPassword(0);
                readParams.setByteCount(12);
                readParams.setByteOffset(4);
                readParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
                for (int iterCnt = 0; iterCnt < 5; iterCnt++) {
                    try {
                        td = reader.Actions.TagAccess.readWait(mytags[0].getTagID(), readParams, null);
                    } catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
                    if (td != null && (td.getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)) {
                        System.out.print("TagID ID is\t" + td.getMemoryBankData() + "\t length :" + td.getMemoryBankDataAllocatedSize());
                        psLog.println("TagID ID is\t" + td.getMemoryBankData() + "\t length :" + td.getMemoryBankDataAllocatedSize() + "<br>");
                        btid = true;
                        break;
                    }
                }

                if (btid == false) {
                    continue READAGAIN1;
                }

                if (td.getMemoryBank() != MEMORY_BANK.MEMORY_BANK_EPC) {
                    System.out.print(" This is not the memory Bank we are intented to look for..exiting");
                    //return false;
                }
                tag1.tid = hex2Byte(td.getMemoryBankData());
                tag1.tidlen = 12;//tidMemData.getMemoryBankDataAllocated();//hard code for time being and change latertidMemData.getMemoryBankDataAllocated();
                if (btid) {
                    break;
                }



            }

            reader.Actions.purgeTags();
            byte writeData[] = {(byte) 0x11, (byte) 0x11, (byte) 0x22, (byte) 0x22, (byte) 0x33, (byte) 0x33, (byte) 0x44, (byte) 0x44, (byte) 0x55, (byte) 0x55};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tp);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(mask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            WriteEvent(MEMORY_BANK.MEMORY_BANK_EPC, 6, 10, writeData, accessfilter);
        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
    }

    public void FormTestID(long TestNo, long SubNo, String TestType) {

        TestID = "";
        TestID = TestID.concat("Java-");
        TestID = TestID.concat(TestType);
        TestID = TestID.concat("-");
        if (TestNo <= 9) {
            TestID = TestID.concat("00" + TestNo);
        }
        if (TestNo > 9 && TestNo < 100) {
            TestID = TestID.concat("0" + TestNo);
        }
        if (TestNo > 99) {
            TestID = TestID.concat("" + TestNo);
        }
        if (SubNo != 0) {
            TestID = TestID.concat(".");
            TestID = TestID.concat("" + SubNo);
        }
        psLog.println("<br><a name=" + TestID + "</a><br><br><b>" + TestID + "</b>");
        System.out.print("\n name= " + TestID);

    }

    public void CleanupPendingSequence() {
        //
        try {
            reader.Actions.TagAccess.OperationSequence.stopSequence();
        } catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
    }

    public void AntennaPowersettings() {
        try {
            reader.Config.resetFactoryDefaults();
            psLog.println("<br>Inside Antenna Power setting Method");
            Antennas antenna = reader.Config.Antennas;

            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
            Config.setTransmitPowerIndex((short) ((reader.ReaderCapabilities.getTransmitPowerLevelValues().length) - 16));
            for (int i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++) {
                reader.Config.Antennas.setAntennaConfig(i, Config);
                Config = reader.Config.Antennas.getAntennaConfig(i);
                psLog.print("<br>getTransmitPowerIndex for antenna " + i + " is " + Config.getTransmitPowerIndex());
            }

        } catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                    }
    }

    public int tagCompare(String s, String r, int count, int offset) {

        int endIndex = (offset * 2) + (count * 2);
        if ((s.substring(offset * 2, endIndex).equals(r))) {
            return 0;
        } else {
            return 1;
        }
    }

    class ReadEventlistener implements RfidEventsListener {

        int test;
        int count;
        public RFIDReader myReader = null;
        public PrintStream myLogger = null;
        public TagData tagData = null;

//RFIDReader reader;
        public ReadEventlistener(RFIDReader reader, PrintStream psLog) {
            myReader = reader;
            myLogger = psLog;
        }

        public void eventReadNotify(RfidReadEvents rfidReadEvents) {
            myLogger.println("<br>READ EVENT Happened");

            if (rfidReadEvents != null) {
                tagData = rfidReadEvents.getReadEventData().tagData;
                myLogger.println("<br>+++++++++++++++++++++++++++"+tagData.getMemoryBankData());
            }

        }

        public void eventStatusNotify(RfidStatusEvents rfidStatusEvents) {
            myLogger.println("<br>" + rfidStatusEvents.StatusEventData.getStatusEventType());
        }
    }
    public void getLastError()
   {
       int[] pass = new int[1]; int[] fail = new int[1];
       try{
           reader.Actions.TagAccess.getLastAccessResult(pass, fail);
           psLog.println();
           psLog.println("\n\n PassCount = "+pass[0]+"     FailCount = "+fail[0]);
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
    public boolean ReadMemoryBankdData( )
    {
        boolean isAccessSuccess = false;
        TagData[] tags = reader.Actions.getReadTags(1000);
        if( tags != null )
        {
            for(int tagCount = 0; tagCount < tags.length;tagCount++)
            {
                psLog.println(tags[tagCount].getOpStatus());
//                if( tags[tagCount].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
//                {
                    psLog.println("\n EPC  :"+tags[tagCount].getTagID()+
                                  " memBank "+tags[tagCount].getMemoryBank()+
                                  "  memoryBank Data :"+ tags[tagCount].getMemoryBankData()+
                                  " getMemoryBankDataAllocatedSize  :"+ tags[tagCount].getMemoryBankDataAllocatedSize()+
                                  "Op code: "+tags[tagCount].getOpCode()+
                                  "Op Status: "+tags[tagCount].getOpStatus()+
                                  "Offset: "+tags[tagCount].getMemoryBankDataOffset()+
                                  "getAntennaID: "+tags[tagCount].getAntennaID()+
                                  "getPC: "+tags[tagCount].getPC());
                    psLog.println("<br>");
                    isAccessSuccess = true;
//                }
            }
            reader.Actions.purgeTags();
            return isAccessSuccess;
        }
        else
        {
            reader.Actions.purgeTags();
            return isAccessSuccess;
        }

    }

    //Calling ReadWait for a particular memory bank
    public int ReadWait(MEMORY_BANK memBank, int pointer, int cntbyte, String tid) {
        try {

            psLog.print("<br>Inside the ReadWait Function");
            psLog.println("<br>Read Params");
            psLog.print("<br>Memory bank: " + memBank);
            readParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            readParams.setByteOffset(pointer);
            psLog.print(" Count: " + cntbyte);
            readParams.setByteCount(cntbyte);

            psLog.print("<br>Calling ReadWait API");
            readAccessTag = reader.Actions.TagAccess.readWait(tid, readParams, antInfo);
            psLog.print("<br>ReadWait API:Success");

        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling ReadEvent for a particular memory bank

    public int ReadEvent(MEMORY_BANK memBank, int pointer, int cntbyte, AccessFilter accessfilter) {
        try {
            reader.Actions.purgeTags();

            psLog.print("<br>Inside the ReadEvent Function");
            psLog.println("<br>Read Params");
            psLog.print("<br>Memory bank: " + memBank);
            readParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            readParams.setByteOffset(pointer);
            psLog.print(" Count: " + cntbyte);
            readParams.setByteCount(cntbyte);

            psLog.print("<br>Calling ReadEvent API");
            //reader.Actions.TagAccess.readEvent(null, null, antInfo);
            reader.Actions.TagAccess.readEvent(readParams, null, antInfo);
//            reader.Actions.TagAccess.readEvent(readParams, accessfilter, antInfo);
//            reader.Actions.TagAccess.readEvent(readParams, accessfilter, antInfo);
            Thread.sleep(5000);
//            int[] success = new int[1];
//            int[] failure = new int[1];
//            reader.Actions.TagAccess.getLastAccessResult(success, failure);
//            psLog.println("\n Success count: " + success[0]+ "failure count: "  + failure[0]);

        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling WriteWait for a particular memory bank

    public int WriteWait(MEMORY_BANK memBank, int pointer, int wrcount, byte[] wrData, String tid) {
        try {

            psLog.print("<br>Inside the WriteWait Function");
            psLog.println("<br>Write Params");
            psLog.print("<br>Memory bank: " + memBank);
            writeParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            writeParams.setByteOffset(pointer);
            psLog.print(" Write Data length: " + wrcount);
            writeParams.setWriteDataLength(wrcount);
            psLog.print(" Write Data: " + wrData);
            writeParams.setWriteData(wrData);
            psLog.print("<br>Calling WriteWait API");
            reader.Actions.TagAccess.writeWait(tid, writeParams, antInfo);
            psLog.print("<br>WriteWait API:Success");

        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling WriteEvent for a particular memory bank

    public int WriteEvent(MEMORY_BANK memBank, int pointer, int wrcount, byte[] wrData, AccessFilter accessfilter) {

        try {
            reader.Actions.purgeTags();
            psLog.print("<br>Inside the WriteEvent Function");
            psLog.println("<br>Write Params");
            psLog.print("<br>Memory bank: " + memBank);
            writeParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            writeParams.setByteOffset(pointer);
            psLog.print(" Write Data length: " + wrcount);
            writeParams.setWriteDataLength(wrcount);
            psLog.print(" Write Data: " + wrData);
            writeParams.setWriteData(wrData);


            psLog.print("<br>Calling WriteEvent API");
            reader.Actions.TagAccess.writeEvent(writeParams, accessfilter, antInfo);
            Thread.sleep(6000);
            int[] success = new int[1];
            int[] failure = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(success, failure);
            psLog.println("\n Success count: " + success[0]+ "failure count: "  + failure[0]);
//                TagData[] tags = reader.Actions.getReadTags(1000);
//                if( tags != null )
//                {
//                    for(int tagCount = 0; tagCount < tags.length;tagCount++)
//                    {
//                        if( tags[tagCount].getOpStatus() == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS )
//                        {
//                            psLog.print("*************************<br>WriteEvent API:Success");
//                            break;
//                        }
//                    }
//                }



        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }

    public int BlockWriteWait(MEMORY_BANK memBank, int pointer, int wrcount, byte[] wrData, String tid) {
        try {

            psLog.print("<br>Inside the BlockWriteWait Function");
            psLog.println("<br>BlockWrite Params");
            psLog.print("<br>Memory bank: " + memBank);
            writeParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            writeParams.setByteOffset(pointer);
            psLog.print(" Write Data length: " + wrcount);
            writeParams.setWriteDataLength(wrcount);
            psLog.print(" Write Data: " + wrData);
            writeParams.setWriteData(wrData);


            psLog.print("<br>Calling BlockWriteWait API");
            reader.Actions.TagAccess.blockWriteWait(tid, writeParams, antInfo);
            psLog.print("<br>BlockWriteWait API:Success");

        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling BlockWriteEvent for a particular memory bank

    public int BlockWriteEvent(MEMORY_BANK memBank, int pointer, int wrcount, byte[] wrData, AccessFilter accessfilter) {
        try {
            reader.Actions.purgeTags();
            psLog.print("<br>Inside the BlockWriteWait Function");
            psLog.println("<br>BlockWrite Params");
            psLog.print("<br>Memory bank: " + memBank);
            writeParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            writeParams.setByteOffset(pointer);
            psLog.print(" Write Data length: " + wrcount);
            writeParams.setWriteDataLength(wrcount);
            psLog.print(" Write Data: " + wrData);
            writeParams.setWriteData(wrData);


            psLog.print("<br>Calling BlockWriteEvent API");
            reader.Actions.TagAccess.blockWriteEvent(writeParams, accessfilter, antInfo);
            Thread.sleep(3000);
            psLog.print("<br>BlockWriteEvent API:Success");
            int[] success = new int[1];
            int[] failure = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(success, failure);
            psLog.println("\n Success count: " + success[0]+ "failure count: "  + failure[0]);
        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling WriteWait for a particular memory bank

    public int BlockEraseWait(MEMORY_BANK memBank, int pointer, int wrcount, String tid) {
        try {

            psLog.print("<br>Inside the WriteWait Function");
            psLog.println("<br>Write Params");
            psLog.print("<br>Memory bank: " + memBank);
            EraseAccessParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            EraseAccessParams.setByteOffset(pointer);
            psLog.print(" Byte count: " + wrcount);
            EraseAccessParams.setByteCount(wrcount);



            psLog.print("<br>Calling BlockEraseWait API");
            reader.Actions.TagAccess.blockEraseWait(tid, EraseAccessParams, antInfo);
            psLog.print("<br>BlockEraseWait API:Success");

        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }
    //Calling BlockEraseEvent for a particular memory bank

    public int BlockEraseEvent(MEMORY_BANK memBank, int pointer, int wrcount, AccessFilter accessfilter) {
        try {

            psLog.print("<br>Inside the BlockWriteWait Function");
            psLog.println("<br>BlockWrite Params");
            psLog.print("<br>Memory bank: " + memBank);
            EraseAccessParams.setMemoryBank(memBank);
            psLog.print(" Offset: " + pointer);
            EraseAccessParams.setByteOffset(pointer);
            psLog.print(" Byte count: " + wrcount);
            EraseAccessParams.setByteCount(wrcount);
            psLog.print("<br>Calling BlockEraseEvent API");
            reader.Actions.TagAccess.blockEraseEvent(EraseAccessParams, accessfilter, antInfo);
            Thread.sleep(3000);
            psLog.println("<br>BlockEraseEvent API:Success");
            int[] success = new int[1];
            int[] failure = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(success, failure);
            psLog.println("\n Success count: " + success[0]+ "failure count: "  + failure[0]);
        } 
        catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        CleanupPendingSequence();
                        return 1;
                    }
        return 0;
    }

    static boolean getReaderConfig() {
        // intialize RFID3 with the data read from the Config file
        // Read input data from config file
        try {

            FileInputStream fstream = new FileInputStream("config.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String version = strLine;
                String[] word;
                if (strLine.contains("readerIP")) {
                    word = strLine.split(" ");
                    ip = word[1].trim();
                }
                if (strLine.contains("type")) {
                    word = strLine.split(" ");
                    String rtype = word[1];
                    if (rtype.contains("FX")) {
                        ReaderType = READER_TYPE.FX;
                    }
                    if (rtype.contains("MC")) {
                        ReaderType = READER_TYPE.MC;
                    }
                    if (rtype.contains("XR")) {
                        ReaderType = READER_TYPE.XR;
                    }
                }
                if (strLine.contains("antennaList")) {
                    word = strLine.split(" ");
                    if (word[1].contains("0")) {
                        antInfo = null;
                    } else {
//                        short noAnts = Short(word[1].trim());
//                        antInfo = new AntennaInfo();
//                        antList = new short[noAnts];
//                        for(short ant=0; ant<noAnts; ant++)
//                        {
//                            //Integer intObj = new Integer(++ant);
//                            //byte b = intObj.byteValue();
//                            antList[ant] = (short)(ant+1);
//
//                        }
                    }
//                    OPERATION_QUALIFIER[] opq = { OPERATION_QUALIFIER.C1G2_OPERATION,OPERATION_QUALIFIER.C1G2_OPERATION};
//                     antInfo.setAntennaOperationQualifier(opq);
//                     antInfo.setAntennaID(antList);
                }



            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Exception: " + e.getMessage());
        }
//        System.out.print("\nIP: address"+ip);
        return true;
    }

    public void getListProfile(String[] profileListk) {
        int k = profileListk.length;
        for (int i = 0; i < profileListk.length; i++) {
            psLog.println(", " + profileListk[i]);
        }
    }

    public int RM_Login() {
        LoginInfo login = null;
        READER_TYPE type = null;
        psLog.println("<br>Inside the Reader Management Login Method:");

        //READER_TYPE type = null;
        String model;
        String user = "admin";
        String password = "change";
        readerMgt = new ReaderManagement();
        if (reader.ReaderCapabilities.getModelName().equals("3190")) {
            //  model = "MC";
            login = new LoginInfo("127.0.0.1", user, password, null, true);
            //login = null;
            type = READER_TYPE.MC;
        }

        if (reader.ReaderCapabilities.getModelName().equals("74004") || reader.ReaderCapabilities.getModelName().equals("74002") || reader.ReaderCapabilities.getModelName().equals("75002") || reader.ReaderCapabilities.getModelName().equals("75004") || reader.ReaderCapabilities.getModelName().equals("75008") || reader.ReaderCapabilities.getModelName().equals("96008") || reader.ReaderCapabilities.getModelName().equals("96004") || reader.ReaderCapabilities.getModelName().equals("7400256"))  {
            login = new LoginInfo(reader.getHostName(), user, password, SECURE_MODE.HTTP, true);
            type = READER_TYPE.FX;
            model = "FX";
            psLog.println("<br>Attempting Login....");
        }
        if (reader.ReaderCapabilities.getModelName().equals("9190")) {
            //  model = "MC";
            login = new LoginInfo(reader.getHostName(), user, password, SECURE_MODE.HTTP, true);
            type = READER_TYPE.MC;
            model = "MC";
            psLog.println("<br>Attempting Login....");
        }

        try {
            readerMgt.login(login, type);
            psLog.println("<br>Login success");
            return success;
        } catch (Exception e) {
            AnalyseException(psLog, e);
            return failure;
        }
    }//RM_Login()

    public int RM_Logout() {
        try {
            readerMgt.logout();
            psLog.println("<br>Logout success");
            return success;
        } 
                catch(Exception e)
                    {
                        AnalyseException(psLog,e);
                        return failure;
        }
    }//RM_Logout()
    
    
}

