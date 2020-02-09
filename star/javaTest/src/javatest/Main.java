/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import java.lang.Object.*;
import com.mot.rfid.api3.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.Scanner;
import static javatest.Commonclass.ip;
import static javatest.Commonclass.mystreamSummary;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psSummary;
import static javatest.Commonclass.reader;

/**
 *
 * @author NVJ438
 */
public class Main extends Commonclass  {
    //static RFIDReader reader;

    static GetEvents getEvents;
    static String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    
    //  static PrintStream psLog;
    static String PrintTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException 
    {
        // TODO code application logic here
        String OS = System.getProperty("os.name");
        Scanner in = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);

        mystreamSummary = new FileOutputStream("JavaAPI_Test_Summary.txt");
        psSummary = new PrintStream(mystreamSummary);

        Commonclass cc = new Commonclass();
        ConnectReader connect = new ConnectReader(); 
        Multihost mHost = new Multihost();
        
        getReaderConfig();

        System.out.print("\n Select from the Regression list to run");
        System.out.print("\n **************************************");
        System.out.print("\n 1. UnSecure Connection");
        System.out.print("\n 2. Secure Connection");
        System.out.print("\n 3. Multiple Hosts Connection");
        System.out.print("\n");

        int valueToRun = in.nextInt();
        switch (valueToRun) {
            case 1:
                connect.connectReaderUnSecureMode();
                break;
            case 2:
                connect.connectReaderInSecureMode();
                break;
            case 3:
                mHost.Multihost();
                break;
            case 0:
                
                break;
            default:
                System.out.print("\n Invalid parameter entered.....");
        }

        System.out.print("\n Select from the Regression list to run");
        System.out.print("\n ***************************************");
        System.out.print("\n 1. Regression Run");
        System.out.print("\n 2. Manual Run");
        System.out.print("\n 0. Exit");
        System.out.print("\n");
        valueToRun = in.nextInt();
       switch (valueToRun)
        {
            case 1:

                psSummary.println("Java Regression Start Time " + PrintTime());
                psSummary.println();
                psSummary.println();
                int iChoice = 0;
                do
                {
                    System.out.print("\n Select from the Regression list to run");
                    System.out.print("\n **************************************");
                    System.out.print("\n 0. All");
                    System.out.print("\n 1. Capabilities");
                    System.out.print("\n 2. Test StateUnAware filters");
                    System.out.print("\n 3. Test StateAware Filters");
                    System.out.print("\n 4. Test PostFilters-Read");
                    System.out.print("\n 5. Test RSSIFilters");
                    System.out.print("\n 6. Test RFID3Triggers");
                    System.out.print("\n 7. Test Events");
                    System.out.print("\n 8. Testing ReadWait and Read Events");
                    System.out.print("\n 9. Testing WriteWait and Write Events");
                    System.out.print("\n 10. Testing BlockEraseWait and BlockEraseEvents");
                    System.out.print("\n 11. Testing BlockWriteWait and BlockWriteEvents");
                    System.out.print("\n 12. Access Sequence");
                    System.out.print("\n 13. Op Sequence");
                    System.out.print("\n 14. Truncate");
                    System.out.print("\n 15. testWriteSpecificParameters");
                   System.out.print("\n 16. Test_TagStorageSettings");
                   System.out.print("\n 17. SetGet");
                    System.out.print("\n 18. Config ");
                    System.out.print("\n 19. RFMode ");
                    //System.out.print("\n 20. Recommission(Not Supported in FX7500) ");
                   System.out.print("\n 21. NegativeTest");
                   // System.out.print("\n 22. ResetFactory(DutyCycle Not supported in FX7500 & RadioPower will not reset when you do Factory Reset unlike Handheld devices in FX7500)  ");
                    //System.out.print("\n 23. Locationing(Not Supported in Fixed Readers only supported in Handheld Readers)");
                    System.out.print("\n 24. RdrManagement");
                   // System.out.print("\n 25. Short Run Test Cases");
                    System.out.print("\n 26. Long Run  Test Cases");
                    System.out.print("\n 27. User App Test");
                    //System.out.print("\n 28. Insatall User Apps");
                    System.out.print("\n 100. Exit Regression");
                    System.out.print("\n");
                    valueToRun = in.nextInt();
                    iChoice = valueToRun;
                   switch (valueToRun) {
                        case 1:
                            Capabilities cap = new Capabilities();
                            System.out.print("\n Capabilities Started");
                            cap.capabilitiesMethod();
                            System.out.print("\n Capabilities complete");
                            break;
                        case 2:
                            Inventory inv = new Inventory();
                            Filters ff = new Filters(reader);
                            //ff.Java_getBEDDTag();
                            System.out.print(" testSngulationsStateUnAware Started");
//                            if (ff.Read2Tags() != true) {
//                                System.out.println(" Could not read required tags for filtering..exiting.");
//                                break;
//                            }
                            ff.testSngulationsStateUnAware();
                            ff.testSngulationsStateUnAwareAuto();
                            inv.TestInventory_Unaware(reader);
                            System.out.print(" testSngulationsStateUnAware..Complete");
                            break;
                        case 3:
                            Inventory inv1 = new Inventory();
                            //inv1 = new Inventory();
                            Filters ff1 = new Filters(reader);
                            System.out.println(" testSngulationsStateAware..Started");
//                            if (ff1.Read2Tags() != true) {
//                                System.out.println(" Could not read required tags for filtering..exiting.");
//                                break;
//                            }
                            //inv1.Test_SimpleInventory();
                            inv1.TestInventory_aware(reader);
                            ff1.testSngulationsStateAware();
                            System.out.println(" testSngulationsStateAware..Complete");
                            break;
                        case 4:

                            ff = new Filters(reader);
//                            if (ff.Read2Tags() != true) {
//                                System.out.println(" Could not read required tags for filtering..exiting.");
//                                break;
//                            }
                            System.out.println(" testPostFiltersRead..Started");
                            ff.testPostFiltersRead();
                            System.out.println(" testPostFiltersRead..Complete");
                            break;
                        case 5:
                            RSSIFilter rssifilter = new RSSIFilter(reader);
                            System.out.print(" TestRSSIFilter..Started");
                            rssifilter.TestRSSIFilter();
                            rssifilter.LogSuccessFailureCount();
                            System.out.print(" TestRSSIFilter..Complete");
                            break;
                        case 6:
                            testTRIGGERS tt = new testTRIGGERS(reader);
                            System.out.print(" testRFID3Triggers..Started");
                            tt.testRFID3Triggers();
                            System.out.print(" testRFID3Triggers..Complete");
                            break;
                        case 7:
                            
                            tt = new testTRIGGERS(reader);
                            TagEvent TE = new TagEvent();
                            System.out.print(" testRFIDEvents..Started");
                            tt.testRFIDEvents();
                            System.out.print(" testRFIDEvents..Complete");
                            tt.LogSuccessFailureCount();
                            System.out.print(" testTagEvents..Started");
                            TE.TestRFIDTagEvents();
                            System.out.print(" testTagEvents..Complete");
                            break;
                        case 8:
                            Readwait rd = new Readwait();
                            ReadEvent RE = new ReadEvent();
                            rd.setantInfo();
                            rd.test_Readwait();
//                            if (cc.Check1111Tag("111122223333444455556666") == 1) {
//                                rd.setantInfo();
//                                System.out.print(" test_Readwait..Started");
//                                rd.test_Readwait();
//                                System.out.print(" test_Readwait..Complete");
//
//                                System.out.print(" Test_ReadEvent..Started");
//                                RE.Test_ReadEvent();
//                                System.out.print(" Test_ReadEvent..Complete");
//
//                            } else {
//                                System.out.print(" Tag not available in the field for Readwait and ReadEvent test cases");
//                            }
                            break;
                        case 9:
                            Filters Writewait = new Filters(reader);
                            Writewait.Java_getBEDDTag();
                            if (cc.ReadTagAvailability("BEDD11112222333344445555") == 1) {
                                WriteWait ww = new WriteWait();
                                System.out.println(" test_Writewait..Started");
//                            ww.test_Writewait();
                                System.out.println(" test_Writewait..Complete");

                                WriteEvent writeevent = new WriteEvent();
                                System.out.println(" Test_WriteEvent..Started");
                                writeevent.Test_WriteEvent();
                                System.out.println(" Test_WriteEvent..Complete");
                            } else {
                                System.out.println(" Tag not available in the field for Write test cases");
                            }
                            break;
                        case 10:
                            if (cc.ReadTagAvailability("BEDD11112222333344445555") == 1) {
                                BlockEraseWait bw = new BlockEraseWait();
                                System.out.print(" Test_BlockEraseWait..Started");
                                bw.Test_BlockEraseWait();
                                System.out.print(" Test_BlockEraseWait..Complete");

                                BlockEraseEvent be = new BlockEraseEvent();
                                System.out.print(" Test_BlockEraseEvent..Started");
                                be.Test_BlockEraseEvent();
                                System.out.print(" Test_BlockEraseEvent..Complete");
                            } else {
                                System.out.print(" Tag not available in the field for BlockErase test cases");
                            }
                            break;
                        case 11:
                            if (cc.ReadTagAvailability("BEDD11112222333344445555") == 1) {
                                BlockWriteWait bww = new BlockWriteWait();
                                System.out.print(" Test_BlockWriteWait..Started");
                                bww.Test_BlockWriteWait();
                                System.out.print(" Test_BlockWriteWait..Complete");

                                BlockWriteEvent bwe = new BlockWriteEvent();
                                System.out.print(" Test_BlockWriteEvent..Started");
                                bwe.Test_BlockWriteEvent();
                                System.out.print(" Test_BlockWriteEvent..Complete");
                            } else {
                                System.out.print(" Tag not available in the field for BlockWrite test cases");
                            }
                            break;
                        case 12:
                            accessSeq accseq = new accessSeq(reader);
                            //Access Sequence
                            System.out.print(" testOpSeqRead..Started\n");
                            accseq.testOpSeqRead();
                            System.out.print(" testOpSeqRead..Complete\n");
                            System.out.print(" testOpSeqBlockErase..Started\n");
                            accseq.testOpSeqBlockErase();
                            System.out.print(" testOpSeqBlockErase..Complete\n");
                            System.out.print(" testOpSeqWrite..Started\n");
                            accseq.testOpSeqWrite();
                            System.out.print(" testOpSeqWrite..Complete\n");
                            System.out.print(" testOpSeqBlockWrite..Started\n");
                            accseq.testOpSeqBlockWrite();
                            System.out.print(" testOpSeqBlockWrite..Complete\n");
                            System.out.print(" testOpSeqLock, Kill, EAS..Started\n");
//                        accseq.testOpSeqLock();
//                        accseq.testOpSeqKill();
//                        accseq.testBP();
//                        accseq.LogSuccessFailureCount();
//                        accseq.testOpSeqSetEAS();
//                        accseq.testOpSeqSetReadProtectEAS();
                            System.out.print("testOpSeqLock, Kill, EAS..Complete\n");
                            break;
                        case 13:
                            OpSequence ops = new OpSequence();
                            //Op Sequence
                            ops.Test_MultipleOpSequence();
                            break;
                        case 14:
                            G2Truncate g2Truncate = new G2Truncate(reader);
                            //Truncate
                            g2Truncate.testTruncate();
                            break;
                        case 15:
                            Filters Wrisp = new Filters(reader);
                            Wrisp.Java_getBEDDTag();
                            WriteSpecificParams wSpecific = new WriteSpecificParams(reader);
                            //testWriteSpecificParameters               
                            System.out.println(" \ntestWriteSpecificParameters..Started");
                            wSpecific.testWriteSpecificParameters();
                            System.out.println(" testWriteSpecificParameters..Complete");
                            System.out.println(" testWriteSpecificNegValues..Started");
                            wSpecific.testWriteSpecificNegValues();
                            wSpecific.LogSuccessFailureCount();
                            System.out.println(" testWriteSpecificNegValues..Complete");
                            break;
                        case 16:
                            TagStorage ts = new TagStorage();
                            //Test_TagStorageSettings
                            System.out.print("Test_TagStorageSettings ..Started");
                            ts.Test_TagStorageSettings();
                            System.out.print(" Test_TagStorageSettings..Complete");
                            break;
                        case 17:
                            SetGet setget = new SetGet();
                            //  SetGet          
                            System.out.print(" setgetMethod..Started");
                            setget.setgetMethod();
                            System.out.print(" setgetMethod..Complete");
                            break;
                        case 18:
                            Config cfg = new Config();
                            //   Config         
                            System.out.print(" Test_Config..Started");
                            cfg.Test_Config();
                            System.out.print(" Test_Config..Complete");
                            break;
                        case 19:
                            RFMode rfmodes = new RFMode();
                            //  RFMode      
                            System.out.print(" Test_RFmodePowerIndex..Started");
                            rfmodes.Test_RFmodePowerIndex();
                            System.out.print(" Test_RFmodePowerIndex..Complete");
                            break;
                        case 20:
                            Recommission rcom = new Recommission(reader);
                             // Recommission          
                            System.out.print(" testRecommission..Started");
                            rcom.testRecommission();
                            System.out.print(" testRecommission..Complete");
                            break;
                        case 21:
                            NegativeTest neg = new NegativeTest();
                            //  NegativeTest
                            System.out.print(" Test_Nagative..Started");
                            neg.Test_Nagative();
                            System.out.print(" Test_Nagative..Complete");
                            break;
//                        //case 22:
//                            //ResetFactory resetfact = new ResetFactory();
//                            //  ResetFactory       
//                            //System.out.print(" Test_ResetFactory - executing\n");
//                            //resetfact.Test_ResetFactory();
//                            //break;
//                        //case 23:
//                            //Locationing locate = new Locationing(reader);
//                            //if (OS.compareTo("Windows CE") == 0) {
//                            //   Locationing
//                            //locate.testLocationing();
//                            //locate.testDutyCycle();
//                            //break;
                        case 24:
                            RdrManagement rm = new RdrManagement();
                            //   RdrManagement
                            System.out.print(" Test_RdrManagement..Started\n");
                            rm.Test_RdrManagement();
                            System.out.print("\nTest_RdrManagement..Complete");
                            break;

                        case 26:
                            //Long Run Test cases
                             //RFMode 
                             rfmodes = new RFMode();
                            System.out.print(" Test_RFmodePowerIndex..Started");
                            rfmodes.Test_RFmodePowerIndex();
                            System.out.print(" Test_RFmodePowerIndex..Complete");
                            break;
                        case 27:
                            UserApp usrApp = new UserApp();
                            System.out.print(" testUserApp..Started\n");
                           usrApp.testUserApp();
                            System.out.print("\n testUserApp..Complete");

                             System.out.print(" testUsrAppsInstall..Started\n");
                            usrApp.testUsrAppsInstall();
                             System.out.print("\n testUsrAppsInstall..Complete");

                            System.out.print(" testUsrAppsUnInstall..Started\n");
                            usrApp.testUsrAppsUnInstall();
                            System.out.print("\n testUsrAppsUnInstall..Complete");
                            break;
                      //case 28:
                           // break;
                        case 0:
                            //All Test cases
                            System.out.println("Java Regression Started\n");
                            Filters ffobj = new Filters(reader);
                    if (ffobj.Read2Tags() != true)
                    {
                        System.out.println(" Could not read required tags for Regression / filtering..some test cases may fail ");
                        return;
                    }
                    ffobj.Java_getBEDDTag();

                            cap = new Capabilities();
                            System.out.println(" Capabilities Started");
                            cap.capabilitiesMethod();
                            System.out.println("Capabilities complete");

                            //state Unaware operation
                            Inventory invUnaware = new Inventory();
                            System.out.println("testSngulationsStateUnAware Started \n approx 15 mins");
                            ffobj.testSngulationsStateUnAware();
                            ffobj.testSngulationsStateUnAwareAuto();
                            invUnaware.TestInventory_Unaware(reader);
                            System.out.println("        testSngulationsStateUnAware..Complete");

                            //state aware operation
                            Inventory inven = new Inventory();
//
//                            //state aware
                            System.out.println("testSngulationsStateAware..Started \n approx 10 mins");
                            inven.TestInventory_aware(reader);
                            ffobj.testSngulationsStateAware();
                            System.out.println("        testSngulationsStateAware..Complete");

                            //post filter                       
                            System.out.println("testPostFiltersRead..Started\n approx 10 mins");
                            ffobj.testPostFiltersRead();
                            System.out.println("             testPostFiltersRead..Complete");

                            // RSSI filters
                            RSSIFilter rssifilter1 = new RSSIFilter(reader);
                            System.out.println("TestRSSIFilter..Started");
                            rssifilter1.TestRSSIFilter();
                            rssifilter1.LogSuccessFailureCount();
                            System.out.println("           TestRSSIFilter..Complete");

                            TagEvent TE1 = new TagEvent();
                            System.out.println("TestTagEvents..Started");
                            TE1.TestRFIDTagEvents();
                            System.out.println("                 testTagEvents..Complete");

                            //triggers and tag events
                            tt = new testTRIGGERS(reader);
                            System.out.println("testRFID3Triggers..Started");
                            tt.testRFID3Triggers();
                            System.out.println("                 testRFID3Triggers..Complete");

                            System.out.println("testRFIDEvents..Started");
                            testTRIGGERS tt1 = new testTRIGGERS(reader);
                            tt.testRFIDEvents();
                            System.out.println("                testRFIDEvents..Complete");
                            tt.LogSuccessFailureCount();

//                        //read wait and read event
                                                    if (cc.Check1111Tag("111122223333444455556666") == 1) {
                            Readwait rd1 = new Readwait();
                            ReadEvent RE1 = new ReadEvent();
//                            rd.setantInfo();
                            System.out.println("test_Readwait..Started");
                            rd1.test_Readwait();
                            System.out.println("        test_Readwait..Complete");

                            System.out.println("Test_ReadEvent..Started");
                            RE1.Test_ReadEvent();
                            System.out.println("        Test_ReadEvent..Complete");

                        } else {
                            System.out.println(" Tag not available in the field for Readwait and ReadEvent test cases");
                        }
                        ffobj.Java_getBEDDTag();
                        if (cc.ReadTagAvailability("BEDD11112222333344445555") == 1) {
                        try {

                            WriteWait ww = new WriteWait();
                            System.out.println("\ntest_Writewait..Started");
                            ww.test_Writewait();
                            System.out.println("        test_Writewait..Complete");
//                            WriteEvent writeeventfull = new WriteEvent();
//                            System.out.println("\nTest_WriteEvent..Started");
//                            writeeventfull.Test_WriteEvent();
//                            System.out.println("        Test_WriteEvent..Complete");

                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }
                        

                        ffobj.Java_getBEDDTag();
                        
                        try {

                            WriteEvent writeevent = new WriteEvent();
                            System.out.println("\nTest_WriteEvent..Started");
                            writeevent.Test_WriteEvent();
                            System.out.println("        Test_WriteEvent..Complete");
                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }
                        try {

                            BlockEraseWait bw = new BlockEraseWait();
                            System.out.println("\nTest_BlockEraseWait..Started");
                            bw.Test_BlockEraseWait();
                            System.out.println("        Test_BlockEraseWait..Complete");

                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }
                        try {

                            BlockEraseEvent be = new BlockEraseEvent();
                            System.out.println("\nTest_BlockEraseEvent..Started");
                            be.Test_BlockEraseEvent();
                            System.out.println("        Test_BlockEraseEvent..Complete");

                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }
                        try {

                            BlockWriteWait bww = new BlockWriteWait();
                            System.out.println("\nTest_BlockWriteWait..Started");
                            bww.Test_BlockWriteWait();
                            System.out.println("        Test_BlockWriteWait..Complete");

                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }
                        try {

                            BlockWriteEvent bwe = new BlockWriteEvent();
                            System.out.println("\nTest_BlockWriteEvent..Started");
                            bwe.Test_BlockWriteEvent();
                            System.out.println("        Test_BlockWriteEvent..Complete");
                        } catch (Exception e) {
                            System.out.println("      Exception: " + e.getMessage());
                        }

                        }
                        try {
                            reader.disconnect();   //to avoid RM failing
                            Thread.sleep(5000);
                            reader.connect();
                            Thread.sleep(2000);
                        } catch (InvalidUsageException e) {
                        } catch (OperationFailureException e) {
                        } catch (InterruptedException e) {
                        }

////                           
//                            //Test_TagStorageSettings
                            TagStorage tsfull = new TagStorage();
                            System.out.println("Test_TagStorageSettings ..Started");
                            tsfull.Test_TagStorageSettings();
                            System.out.println("      Test_TagStorageSettings..Complete");

                            ffobj.Java_getBEDDTag();
                            accessSeq accseqfull = new accessSeq(reader);
//                            //Access Sequence
                            System.out.print("testOpSeqRead..Started\n");
                            accseqfull.testOpSeqRead();
                            System.out.print("      testOpSeqRead..Complete\n");
                            System.out.print("testOpSeqBlockErase..Started\n");
                            accseqfull.testOpSeqBlockErase();
                            System.out.print("      testOpSeqBlockErase..Complete\n");
                            System.out.print("testOpSeqWrite..Started\n");
                            accseqfull.testOpSeqWrite();
                            System.out.print("      testOpSeqWrite..Complete\n");
                            System.out.print("testOpSeqBlockWrite..Started\n");
                            accseqfull.testOpSeqBlockWrite();
                            System.out.print("      testOpSeqBlockWrite..Complete\n");
//                        System.out.print(" testOpSeqLock, Kill, EAS..Started\n");
//                        accseq = new accessSeq(reader);
//                        accseq.testOpSeqLock();
//                        accseq.testOpSeqKill();
//                        accseq.testBP();
//                        accseq.LogSuccessFailureCount();
//                        accseq.testOpSeqSetEAS();
//                        accseq.testOpSeqSetReadProtectEAS();
//                        System.out.print(" testOpSeqLock, Kill, EAS..Complete\n");

                            //multiple Op sequence
                            OpSequence opsfull = new OpSequence();
                            opsfull.Test_MultipleOpSequence();
//
//                            //Truncate
                            G2Truncate g2Truncatefull = new G2Truncate(reader);
                            g2Truncatefull.testTruncate();

                            Filters Wrisp1 = new Filters(reader);
                            Wrisp1.Java_getBEDDTag();
                            Wrisp1.Java_getBEDDTag();
//                            //write specific test cases
//
                            WriteSpecificParams wSpecificfull = new WriteSpecificParams(reader);
//                            //testWriteSpecificParameters               
                            System.out.println(" testWriteSpecificParameters..Started");
                            wSpecificfull.testWriteSpecificParameters();
                            System.out.println("testWriteSpecificParameters..Complete");
                            System.out.println(" testWriteSpecificNegValues..Started");
                            wSpecificfull.testWriteSpecificNegValues();
                            wSpecificfull.LogSuccessFailureCount();
                            System.out.println("        testWriteSpecificNegValues..Complete");

                            //  SetGet         
                            SetGet setgetfull = new SetGet();
                            System.out.println(" setgetMethod..Started");
                            setgetfull.setgetMethod();
                            System.out.println("        setgetMethod..Complete");

                            //   Config    
                            Config cfgfull = new Config();
                            System.out.println(" Test_Config..Started");
                            cfgfull.Test_Config();
                            System.out.println("        Test_Config..Complete");

                              //Recommission    
                            Recommission rcomfull = new Recommission(reader);
                            System.out.println(" testRecommission..Started");
                            rcomfull.testRecommission();
                            System.out.println("      testRecommission..Complete");

                            //  NegativeTest  
                            NegativeTest negfull = new NegativeTest();
                            System.out.println(" Test_Nagative..Started");
                            negfull.Test_Nagative();
                            System.out.println("         Test_Nagative..Complete");

                            //   RdrManagement
                            RdrManagement rmfull = new RdrManagement();
                            System.out.println(" Test_RdrManagement..Started\n");
                            rmfull.Test_RdrManagement();
                            System.out.println("        Test_RdrManagement..Complete");

                            //  ResetFactory  
                            //ResetFactory resetfactfull = new ResetFactory();
                            //resetfactfull.Test_ResetFactory();
                            //System.out.println("    Test_ResetFactory - Complete\n");

                           // Locationing locatefull = new Locationing(reader);
                           // locatefull.testLocationing();
                           // locatefull.testDutyCycle();

                            try {
                                reader.disconnect();   //to avoid RM failing
                                Thread.sleep(5000);
                                reader.connect();
                                Thread.sleep(2000);
                            } catch (InvalidUsageException e) {
                            } catch (OperationFailureException e) {
                            } catch (InterruptedException e) {
                            }

//                       User application 
//                        UserApp usrAppfull = new UserApp();
 //                       System.out.print(" testUserApp..Started\n");
  //                      usrAppfull.testUserApp();
  //                      System.out.print("\n testUserApp..Complete");
  //                      System.out.print(" testUsrAppsInstall..Started\n");
  //                     usrAppfull.testUsrAppsInstall();
  //                     System.out.print("\n testUsrAppsInstall..Complete");
  //                     System.out.print(" testUsrAppsUnInstall..Started\n");
  //                     usrAppfull.testUsrAppsUnInstall();
  //                     System.out.print("\n testUsrAppsUnInstall..Complete");
                           break;
                        //case 29:
                            //break;
                        case 100:
                            System.out.print("\n Exit entered .....");
                            
                            break;
                        default:
                            System.out.print("\n Invalid parameter entered.....");
                    }
                }while(iChoice != 100);
                connect.testDisConnect();
                System.out.println(" Java Regression completed");
                psSummary.println();
                psSummary.println();
                String strTime = PrintTime();
                psSummary.println("Java Regression End Time " + strTime);
                System.out.println("Java Regression completed: " + strTime);
                psSummary.close();
                break;
            case 2:
                int iRegChoice = 0;
                do
                {
                    System.out.print("\n Select from the list to run");
                    System.out.print("\n *****************************");
                    System.out.print("\n 1. Blockpermlock");
                    System.out.print("\n 2. Events test");
                    System.out.print("\n 3. Test EAS Wait");
                    System.out.print("\n 4. Test EAS Events");
                    System.out.print("\n 5. Test Lock Events");
                    System.out.print("\n 6. Test Kill events");
                    System.out.print("\n 7. Test Simple inventory");
                    System.out.print("\n 8. Test software update");
                    System.out.print("\n 9. Test restarting reader");
                    System.out.print("\n 10. Test cable loss compensation cases");
                    System.out.print("\n 11. Test radioidle timeout");
                    System.out.print("\n 12. User App");
                    System.out.print("\n 13. Radio Firmware update");
                    System.out.print("\n 14. OEMconfiguration update");
                    System.out.print("\n 15. Secure Mode Connection");
                    System.out.print("\n 16. Test SaveLlrp Config");
                    System.out.print("\n 100. Exit Regression");
                    System.out.print("\n");
                    valueToRun = in.nextInt();
                    iRegChoice = valueToRun;
                    ManualTest manual = new ManualTest();
                    switch (valueToRun) 
                    {
                        case 1:
                            BlockPermaLock bp = new BlockPermaLock(reader);
                            bp.testBlockPermaLock();
                            break;
                        case 2:
                            EventsTest et = new EventsTest(reader);
                            System.out.print("\n genereate GPI event followed by ant conn/discon ev.....");
                            et.testHandHeldEvent();
                            System.out.print("\n genereate reader discon event.....");
                            break;
                        case 3:
                            manual.Test_EASwait();
                            break;
                        case 4:
                            manual.Test_EASevent();
                            break;
                        case 5:
                            manual.Test_LockEvent();
                            manual.Test_LockWait();
                            break;
                        case 6:
                            manual.Test_killWait();
                            manual.Test_killEvent();
                            break;
                        case 7:
                            manual.Test_SimpleInventory();
                            break;
                        case 8:
                            manual.Test_SoftwareUpdate();
                            break;
                        case 9:
                            manual.Test_restartingReader();
                            break;
                        case 10:
                            manual.Test_GetCableLossCompensation();
                            System.out.print(" Test_SetCableLossCompensation - executing\n");
                            manual.Test_SetCableLossCompensation();
                            System.out.print(" Test_CableLossCompensation for all antennas one by one - executing\n");
                            manual.Test_CableLossCompensation();
                            manual.Test_CableLossCompensationNegative();
                            break;
                        case 11:
                            manual.Test_RadioIdleTimeOut();
                            break;
                        case 12:
                            manual.Test_UserApp();
                            break;
                        case 13:
                            manual.Test_RadioFirmwareUpdate();
                            break;
                        case 14:
                            manual.Test_OEMconfigUpdate();
                            break;
                        case 15:
                            // Only "Enable Secure mode" check box is enabled ..
                            connect.testSecureConnection();

                            // make sure that reader is having Self signed certificate .
                            connect.testSelfSecureConnection();

                            //  "Enable Secure mode" and "Validate peer"  check box should be enabled before running this test.
                            connect.testSecureConnectionWithPeercheck();
                            break;
                        case 16:
                            manual.Test_SaveLlrpConfig();
                            break;
                        default:
                            System.out.print("\n Invalid parameter entered.....");
                        //               case : 3
                        ////EAS neweas = new EAS();
                        ////neweas.Test_EAS();
                        //case:4
                        ////Test_WriteWaitEPC(); commented in write wait.
                        //case : 5
                        //        //      et.testReaderDisconnectEvent();
                        //            case : 6
                        //                    EAS neweas = new EAS();
                        ////neweas.SetGetConfigWord(4);
                        //                case
                        //                        GetAntennaRfConfig();
                        //                        case
                        //        SetAntennaRfConfig();
                        //        case
                        //        Test_GPIGPO()
                        //            case:
                        //                testRFID3Triggers //GPIEvents are there
                        //            case:
                        //Event Notification - HandheldEvent
                        //            case:
                        ////                Event Notification - GPIEvent
                        //                break;
                        //            case:
                        ////                Event Notification - Events.setReaderDisconnectEvent
                        //                break;
                        //            case:
                        ////                Event Notification - Events.setAntennaEvent
                        //                break;
                        //            case:
                        ////                Event Notification - Events.TEMPERATURE_ALARM_EVENT
                        //                break;
                        //                 case:
                        ////                Impinj testcases
                        //                break; 
                        //                case:
                        ////               NXP testcases 
                        //                break;
                        //                        case:
                        ////                Monza 5
                        //                break;
                        //                                        case:
                        ////                Periodic Report Trigger
                        //                break;
                        //                                                        case:
                        ////                Initiate LLRP connection from reader(RM)
                        //                break;
                        //                case:
                        ////               Disconnect LLRP connection from reader(RM)                                            
                        //                break;
                        //                case:
                        ////                 Secure mode                                          
                        //                break;
                        //                case:
                        ////                Phase Info test                                            
                        //                break;
                        //                case:
                        ////                 Triggers                                           
                        //                break;
                        //                case:
                        ////                  START_TRIGGER_TYPE.START_TRIGGER_TYPE_HANDHELD                                          
                        //                break;
                        //                case:
                        ////                  STOP_TRIGGER_TYPE_GPI_WITH_TIMEOUT                                          
                        //                break;
                        //                case:
                        ////                 STOP_TRIGGER_TYPE_HANDHELD_WITH_TIMEOUT                                           
                        //                break;
                        //                case:
                        ////                  Truncate                                          
                        //                break;

                        //        EventsTest et = new EventsTest(reader);
                        //        System.out.print(" genereate GPI event followed by ant conn/discon ev.....");
                        //        et.testHandHeldEvent();
                        //        System.out.print(" genereate reader discon event.....");
                    }
                    break;
                    }while(iRegChoice != 100);
                  default:
                    System.out.print("\n Entered zero exiting Testing .....");
                
        }
        
        //System.exit(0);
    }//public static void main(String[] args)
}