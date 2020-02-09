/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import com.mot.rfid.api3.UserAppInfo;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.reader;
import static javatest.Commonclass.readerMgt;

/**
 *
 * @author knf483
 */
public class UserApp extends Commonclass {

    long TestNo = 1;
    long SubNo = 0;
    boolean bsuccess = false;
    static long THREAD_WAIT = 3000; //ms
    int status;
    String apps[] = {"package-1_3.6_all.deb"};
    /*
    {"javatestpackage1_1.2.0_all.deb",  "package-1_3.6_all.deb", "package-2_1.0_all.deb",
        "package-3_1.0_all.deb", "package-4_1.0_all.deb", "package-5_1.0_all.deb",
        "package-6_1.0_all.deb", "package-7_1.0_all.deb", "package-8_1.0_all.deb",
        "package-9_1.0_all.deb", "package-10_1.0_all.deb", "samplepackage_1.2_all.deb", "testpackage_1.2.7_all.deb" };
    */
    
    public UserApp() {
        
        try {
            mystreamLog = new FileOutputStream("JavaAPI_UserApp_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_UserApp_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            //AppendText();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }

    private void clearAppsData() {
        try {
            UserAppInfo[] myuserappinfo = readerMgt.UserApp.list();
            if (myuserappinfo != null) {
                for (short i = 0; i < myuserappinfo.length; i++) {
                    readerMgt.UserApp.autoStart(myuserappinfo[i].getAppName(), false);
                    readerMgt.UserApp.stop(myuserappinfo[i].getAppName());
                    Thread.sleep(THREAD_WAIT);
                    if (!myuserappinfo[i].getRunStatus()) {
                        readerMgt.UserApp.uninstall(myuserappinfo[i].getAppName());
                    }
                }
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }
            
    private void testUserAppApi(String application) {

        String installApp = new StringBuilder().append(System.getProperty("user.dir")).append("\\"+ application).toString();
        SubNo = 0;
        clearAppsData();
//1 Auto start true
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");            
            readerMgt.UserApp.install(installApp);
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), true);
            psLog.println("<br><b>Description:</b> Set autostart true and get the same for the app "+ userappinfo[0].getAppName());
            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            if (userappinfo[0].getAutoStart()) {
                psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
                psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
                psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
                bsuccess = true;
            }
            LogStatus( TestID, "", bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            bsuccess = false;
            LogStatus( TestID, "", bsuccess);
        }
//        
////2 Auto start false
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
            psLog.println("<br><b>Description:</b> Set autostart false and get the same for the app "+ userappinfo[0].getAppName());
            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            if (!userappinfo[0].getAutoStart()) {
                psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
                psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
                psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
                bsuccess = true;
            }
            LogStatus( TestID, "", bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            bsuccess = false;
            LogStatus( TestID, "", bsuccess);
        }

////3 No.of user apps installed.
        try {
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> To check the number of apps installed ");
            psLog.println("<br> Number of Appliactions Installed: " + userappinfo.length);
            for (short i = 0; i < userappinfo.length; i++) {
                psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[i].getAutoStart());
                psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[i].getMetaData());
                psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[i].getAppName()));
            }
            LogStatus( TestID,"" , true);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"" , false);
        }
//4. Install the app which is already installed.
         try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");            
            readerMgt.UserApp.install(installApp);
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Install the app which is already installed. "+ userappinfo[0].getAppName());
            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
            bsuccess = true;
            
            LogStatus( TestID, "", bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            bsuccess = false;
            LogStatus( TestID, "", bsuccess);
        }
//        
//5 Starting the installed user app
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> To start the installed user app");
            readerMgt.UserApp.start(userappinfo[0].getAppName());
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
            bsuccess = true;
           
            LogStatus( TestID,"" , bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"" , bsuccess);
        }

//////6 Starting the user app which is already running.
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Starting the user app which is already running.");
            if (userappinfo[0].getRunStatus()) {
                readerMgt.UserApp.start(userappinfo[0].getAppName());
                psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
                psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
                psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
                bsuccess = true;
            }
           
            LogStatus( TestID,"" , bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"" , bsuccess);
        }

//
//7 stop the user app 0 and start again 
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Stop the running user app and start it again");
            if (userappinfo != null && userappinfo.length > 0) {
                readerMgt.UserApp.stop(userappinfo[0].getAppName());
            }

            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
            readerMgt.UserApp.start(userappinfo[0].getAppName());

            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));

            if (userappinfo[0].getRunStatus()) {
                readerMgt.UserApp.stop(userappinfo[0].getAppName());
                bsuccess = true;
            }
            LogStatus( TestID,"" , bsuccess);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"" , bsuccess);
        }
//
//8. stop the user app 0 and stop it again 
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Stop the running user app and stop it again");
            
            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
            readerMgt.UserApp.stop(userappinfo[0].getAppName());

            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));

            LogStatus( TestID,"" , true);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"" , bsuccess);
        }

//9. Uninstall the app while it is running
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Uninstall the app while it is running");
            readerMgt.UserApp.start(userappinfo[0].getAppName());
            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));


            if (userappinfo[0].getRunStatus()) {
                readerMgt.UserApp.uninstall(userappinfo[0].getAppName());
                bsuccess = false;
            }
            LogStatus( TestID,application , false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,application , true);
        }

//10 Stop the app and Uninstall the same
        try {
            bsuccess = false;
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            psLog.println("<br><b>Description:</b> Stop the app and uninstall the same");
           
            // Get latest user app info/settings.
            userappinfo = readerMgt.UserApp.list();
            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));


            if (userappinfo[0].getRunStatus()) {
                readerMgt.UserApp.stop(userappinfo[0].getAppName());
                Thread.sleep(THREAD_WAIT);
                readerMgt.UserApp.uninstall(userappinfo[0].getAppName());
                bsuccess = true;
            }
            LogStatus( TestID,application , true);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,application , bsuccess);
        }


//
////12 
//        try {
//            bsuccess = false;
//            FormTestID(TestNo, ++SubNo, "UserApp");
//
//            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//            if (userappinfo != null && userappinfo.length > 0) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//            }
//
//            Thread.sleep(THREAD_WAIT);
//            readerMgt.UserApp.install(installApp);
//            userappinfo = readerMgt.UserApp.list();
//            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
//            readerMgt.UserApp.start(userappinfo[0].getAppName());
//
//            // Get latest user app info/settings.
//            userappinfo = readerMgt.UserApp.list();
//            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
//            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
//            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//
//            if (userappinfo[0].getRunStatus()) {
//                readerMgt.UserApp.start(userappinfo[0].getAppName());
//                bsuccess = true;
//            }
//            LogStatus( TestID,application , bsuccess);
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            LogStatus( TestID,application , bsuccess);
//        }
//            
//        
////13 
//        try {
//            bsuccess = false;
//            FormTestID(TestNo, ++SubNo, "UserApp");
//            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//            Thread.sleep(THREAD_WAIT);
//            if (userappinfo != null && userappinfo.length > 0) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//            }
//
//            readerMgt.UserApp.install(installApp);
//            userappinfo = readerMgt.UserApp.list();
//            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
//
//            readerMgt.UserApp.start(userappinfo[0].getAppName());
//            // Get latest user app info/settings.
//            userappinfo = readerMgt.UserApp.list();
//            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
//            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
//            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//
//            if (userappinfo[0].getRunStatus()) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//                Thread.sleep(THREAD_WAIT);
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//                bsuccess = true;
//            }
//            LogStatus( TestID,application , bsuccess);
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            LogStatus( TestID,application , bsuccess);
//        }
//
////14 
//        try {
//            bsuccess = false;
//            FormTestID(TestNo, ++SubNo, "UserApp");
//            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//            Thread.sleep(THREAD_WAIT);
//            if (userappinfo != null && userappinfo.length > 0) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//            }
//
//            readerMgt.UserApp.install(installApp);
//            userappinfo = readerMgt.UserApp.list();
//            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
//            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
//            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
//            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//            if (!userappinfo[0].getRunStatus()) {
//                readerMgt.UserApp.uninstall(userappinfo[0].getAppName());
//                bsuccess = true;
//            }
//            LogStatus( TestID,application , bsuccess);
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            LogStatus( TestID,application , bsuccess);
//        }
//
//
////15 
//        try {
//            bsuccess = false;
//            FormTestID(TestNo, ++SubNo, "UserApp");
//            Thread.sleep(THREAD_WAIT);
//            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//
//            if (userappinfo != null && userappinfo.length > 0) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//            }
//
//            readerMgt.UserApp.install(installApp);
//            userappinfo = readerMgt.UserApp.list();
//            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), false);
//            readerMgt.UserApp.start(userappinfo[0].getAppName());
//            
//            userappinfo = readerMgt.UserApp.list();
//            psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
//            psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
//            psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//            if (userappinfo[0].getRunStatus()) {
//                readerMgt.UserApp.uninstall(userappinfo[0].getAppName());
//                bsuccess = false;
//            }
//            LogStatus( TestID,application , bsuccess);
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            bsuccess = true;
//            LogStatus( TestID,application , bsuccess);
//        }
//
//
////16 
//        try {
//            bsuccess = false;
//            FormTestID(TestNo, ++SubNo, "UserApp");
//            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
//            if (userappinfo != null && userappinfo.length > 0) {
//                readerMgt.UserApp.stop(userappinfo[0].getAppName());
//            }
//            
//            Thread.sleep(THREAD_WAIT);
//            readerMgt.UserApp.install(installApp);
//            userappinfo = readerMgt.UserApp.list();
//            readerMgt.UserApp.autoStart(userappinfo[0].getAppName(), true);
//            userappinfo = readerMgt.UserApp.list();
//            if (userappinfo[0].getAutoStart()) {
//                psLog.println("<br> Appliaction Name: " + userappinfo[0].getAppName() + "    AutoStart: " + userappinfo[0].getAutoStart());
//                psLog.println("<br> RunStatus: " + userappinfo[0].getRunStatus() + "  Appliaction MetaData: " + userappinfo[0].getMetaData());
//                psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(userappinfo[0].getAppName()));
//                readerMgt.UserApp.install(installApp);
//                bsuccess = true;
//            }
//            LogStatus( TestID,application , bsuccess);
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            bsuccess = true;
//            LogStatus( TestID,application , bsuccess);
//        }
//        
    }

    public void testUserApp() {
        status = RM_Login();

        successCount = 0;
        failureCount = 0;
        TestNo = 1;
        psResult.print("testUserApp.. Started... \n");
        try{
        if (status != success) {
            System.out.print("\n RM Login Failed..  ");
        } else {
            for (int index = 0; index < apps.length; index++) {
                TestNo++;
                testUserAppApi(apps[index]);
            }
        }
        }
        catch(Exception e){
                AnalyseException(psLog, e);
                }
        
        psResult.print("testUserApp..Completed... \n");
        psSummary.println("JavaAPI:testUserApp" + ":" + successCount + ":" + failureCount + "  :0");
  
        RM_Logout();
    }
    public void NegUserApp(){
        String invalidAppName = "notepad.txt";
        status = RM_Login();
         if (status != success) {
            System.out.print("\n RM Login Failed..  ");
        } else {
//1.Invalid app installation
        
        String invalidApp = new StringBuilder().append(System.getProperty("user.dir")).append("\\"+invalidAppName).toString();
        try {
            FormTestID(TestNo, ++SubNo, "UserApp");
            psLog.println("<br>Invalid Appliaction Name: " + invalidApp);
            readerMgt.UserApp.install(invalidApp);
            LogStatus( TestID,"Invalid Appliaction Name : " + invalidAppName , false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"Invalid Appliaction Name : " + invalidAppName, true);
        }
//2. Invalid path 
        String invalidPath = "C:\\Program Files\\Motorola EMDK for Java\\RFID\\";
        String application = "package-1_3.6_all.deb";
        try {
            FormTestID(TestNo, ++SubNo, "UserApp");
            String appInInvalidPath = invalidPath + "\\" + application;
            psLog.println("<br> Invalid Appliaction Path: " + appInInvalidPath);
            readerMgt.UserApp.install(appInInvalidPath);
            LogStatus( TestID, "Invalid Appliaction Path", false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID, "Invalid Appliaction Path", true);
        }
            
         }
 //3.starting an invalid app
        try {
            FormTestID(TestNo, ++SubNo, "UserApp");
            UserAppInfo[] userappinfo = readerMgt.UserApp.list();
            
            psLog.println("<br>Invalid Appliaction Name: " + invalidAppName);
            readerMgt.UserApp.start(invalidAppName);
            LogStatus( TestID,"Starting Invalid Appliaction Name : " + invalidAppName, false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"Starting Invalid Appliaction Name : " + invalidAppName, true);
        }
//4. Stoping an invlid app
        try {
            FormTestID(TestNo, ++SubNo, "UserApp");
            psLog.println("<br>Invalid Appliaction Name: " + invalidAppName);
            readerMgt.UserApp.stop(invalidAppName);
            LogStatus( TestID,"Stopping Invalid Appliaction Name : " + invalidAppName, false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID,"Stopping Invalid Appliaction Name : " + invalidAppName, true);
        }
//5. Auto start=false for invalid app
        try {
            
            FormTestID(TestNo, ++SubNo, "UserApp");
            psLog.println("<br>Invalid Appliaction Name: " + invalidAppName);
            readerMgt.UserApp.autoStart(invalidAppName,true);
            LogStatus( TestID, invalidAppName, false);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID, invalidAppName, true);
        }
//6. Auto start=true for invalid app
        try {
            
            FormTestID(TestNo, ++SubNo, "UserApp");
            psLog.println("<br>Invalid Appliaction Name: " + invalidAppName);
            readerMgt.UserApp.autoStart(invalidAppName,true);
            LogStatus( TestID, invalidAppName, true);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID, invalidAppName, true);
        }
    }

    public void testUsrAppsInstall() {
        RM_Login();
        TestNo = 1;
        SubNo = 0;
        successCount = 0;
        failureCount = 0;
        bsuccess = false;
        psResult.print("testUsrAppsInstall.. Started... \n");
        for (int index = 0; index < apps.length; index++) {
            String installApps = new StringBuilder().append(System.getProperty("user.dir")).append("\\"+apps[index]).toString();
            FormTestID(TestNo++, SubNo, "UserApp");
            try {
                readerMgt.UserApp.install(installApps);
                bsuccess = true;
                LogStatus( TestID, apps[index]+"  Installed .. ", bsuccess);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus( TestID, apps[index] +"  Installed .. ", bsuccess);
            }
        }
        
        try {
            UserAppInfo[] usrAppInfo = readerMgt.UserApp.list();
            if (usrAppInfo != null) {
                for (int app = 0; app < usrAppInfo.length; app++) {
                    psLog.println("<br> Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
                    psLog.println("<br> RunStatus: " + usrAppInfo[app].getRunStatus() + "      Appliaction MetaData: " + usrAppInfo[app].getMetaData());
                    psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
                }
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        psResult.print("testUsrAppsInstall..Completed... \n");
        psSummary.println("JavaAPI:testUsrAppsInstall" + ":" + successCount + ":" + failureCount + "  :0");
        RM_Logout();
    }

    public void testUsrAppsUnInstall() {
        RM_Login();
        TestNo = 1;
        SubNo = 0;
        successCount = 0;
        failureCount = 0;
        bsuccess = false;
        psResult.print(" testUsrAppsUnInstall.. Started... \n");
        try {
            UserAppInfo[] usrAppInfo = readerMgt.UserApp.list();
            if (usrAppInfo != null) {
                
                for (int app = 0; app < usrAppInfo.length; app++) {
                    FormTestID(++TestNo, SubNo, "UserApp");
                    psLog.println("<br> Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
                    psLog.println("<br> RunStatus: " + usrAppInfo[app].getRunStatus() + "      Appliaction MetaData: " + usrAppInfo[app].getMetaData());
                    psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
                    if (!usrAppInfo[app].getRunStatus()) {
                        readerMgt.UserApp.uninstall(usrAppInfo[app].getAppName());
                    }
                    bsuccess = true;
                    LogStatus(TestID, usrAppInfo[app].getAppName() + "  UnInstalled .. ", bsuccess);
                }
            }
            usrAppInfo = readerMgt.UserApp.list();
            if (usrAppInfo == null) {
                psLog.println("<br> All Apps Successfully UnInstalled .. "); 
            }
            
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus( TestID, " Failed to uninstall user App .. ", bsuccess);
        }
        psResult.print(" testUsrAppsUnInstall.. Completed... \n");
        psSummary.println("JavaAPI:testUsrAppsUnInstall" + ":" + successCount + ":" + failureCount + "  :0");
        close();        
        RM_Logout();
    }

//    public void Test_UserApp_AutoStartTrue() {
//        RM_Login();
//        bsuccess = false;
//        try {
//            UserAppInfo[] usrAppInfo = readerMgt.UserApp.list();
//            if (usrAppInfo != null) {
//                for (int app = 0; app < usrAppInfo.length; app++) {
//                    psLog.println("<br> Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
//                    psLog.println("<br> RunStatus: " + usrAppInfo[app].getRunStatus() + "      Appliaction MetaData: " + usrAppInfo[app].getMetaData());
//                    psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
//                    System.out.print("\n Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
//                    System.out.print("\n RunStatus: " + usrAppInfo[app].getRunStatus() + "    RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
//                    if (!usrAppInfo[app].getRunStatus()) {
//                        readerMgt.UserApp.autoStart(usrAppInfo[app].getAppName(), true);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            LogStatus( TestID, " Failed to uninstall user App .. ", bsuccess);
//        }
//        try {
//            readerMgt.restart();
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//        }
//        RM_Logout();
//    }
//
//    public void Test_UserApp__AutoStartFalse() {
//        try {
//            UserAppInfo[] usrAppInfo = readerMgt.UserApp.list();
//            if (usrAppInfo != null) {
//                for (int app = 0; app < usrAppInfo.length; app++) {
//                    psLog.println("<br> Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
//                    psLog.println("<br> RunStatus: " + usrAppInfo[app].getRunStatus() + "      Appliaction MetaData: " + usrAppInfo[app].getMetaData());
//                    psLog.println("<br> User Appliaction RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
//                    System.out.print("\n Appliaction Name: " + usrAppInfo[app].getAppName() + "    AutoStart: " + usrAppInfo[app].getAutoStart());
//                    System.out.print("\n RunStatus: " + usrAppInfo[app].getRunStatus() + "    RunStatus from readerMgt: " + readerMgt.UserApp.getRunStatus(usrAppInfo[app].getAppName()));
//                    if (!usrAppInfo[app].getRunStatus()) {
//                        readerMgt.UserApp.autoStart(usrAppInfo[app].getAppName(), false);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//            LogStatus( TestID, " Failed to uninstall user App .. ", bsuccess);
//        }
//        try {
//            readerMgt.restart();
//        } catch (Exception e) {
//            AnalyseException(psLog, e);
//        }
//    }

    
    
}
