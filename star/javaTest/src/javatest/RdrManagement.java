package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;
import static javatest.Commonclass.reader;
import static javatest.Commonclass.readerMgt;
//import static javatest.Main.testConnect;
//import static javatest.Main.testDisConnect;
//import com.mot.rfid.api3.SecureConnectionInfo;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//import static javatest.Commonclass.ip;

public class RdrManagement extends Commonclass {

    LoginInfo login;
    //READER_TYPE type = null;
    boolean bSuccess = false;
    short i = 0;
    String currentDir = System.getProperty("user.dir");

    public void Test_RdrManagement() {
        login = new LoginInfo();
        successCount = 0;
        failureCount = 0;
        TestNo = 1;
        SubNo = 0;
        try {
            mystreamLog = new FileOutputStream("JavaAPI_ReaderManagement_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_ReaderManagemant_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            AppendText();
            Thread.sleep(5000);
            System.out.print(" Test_RM_login - executing\n");
            Test_RM_login();
            System.out.print(" Test_RM_Logout - executing\n");
            Thread.sleep(5000);
            Test_RM_Logout();
            System.out.print(" Test_GetReadPointStatus - executing\n");
            Thread.sleep(5000);
            Test_GetReadPointStatus();
            System.out.print(" Test_SetReadPointStatus - executing\n");
            Thread.sleep(5000);
            Test_SetReadPointStatus();
            System.out.print(" Test_GetAntennaMode - executing\n");
            Thread.sleep(5000);
            Test_GetAntennaMode();
            System.out.print(" Test_SetAntennaMode - executing\n");
            Thread.sleep(5000);
            Test_SetAntennaMode();
            System.out.print(" Test_getProfileList - executing\n");
            Thread.sleep(5000);
            Test_getProfileList();
            System.out.print(" Test_ExportProfile - executing\n");
            Thread.sleep(5000);
            Test_ExportProfile();
            System.out.print(" Test_ImportProfile - executing\n");
            Thread.sleep(5000);
            Test_ImportProfile();
            System.out.print(" Test_DeletProfile - executing\n");
            Thread.sleep(5000);
            Test_DeletProfile();
            System.out.print(" Test_DeleteActiveProfile - executing\n");
            Thread.sleep(5000);
            Test_DeleteActiveProfile();
            System.out.print(" Test_getHealthStatus - executing\n");
            Thread.sleep(5000);
            Test_getHealthStatus();
            System.out.print("Test_USB - executing\n");
            Thread.sleep(5000);
            Test_USB();
            System.out.print(" Test_LEDinfo - executing\n");
            Test_LEDinfo();
            System.out.print(" Test_setReaderLocalTime - executing\n");
            Thread.sleep(5000);
            Test_setReaderLocalTime();
            System.out.print(" Test_getTimeZone - executing\n");
            Thread.sleep(5000);
            Test_getTimeZone();
            System.out.print(" Test_GetSysInfo - executing\n");
            Thread.sleep(5000);
            Test_GetSysInfo();
            System.out.print(" Test_getReaderStatistics - executing\n");
            Thread.sleep(5000);
            Test_getReaderStatistics();
            System.out.print(" Test_llrpConnectionOverride - executing\n");
            Thread.sleep(5000);
            Test_llrpConnectionOverride();
            System.out.print(" Test_ReaderInfo - executing\n");
            Thread.sleep(5000);
            Test_ReaderInfo();
            System.out.print(" Test_tracelevel - executing\n");
            Thread.sleep(5000);
            Test_tracelevel();

            psSummary.println("JavaAPI:Reader Managemant Test Cases:" + successCount + ":" + failureCount + ":" + "0");
            close();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }//Constructor....

    public void Test_RM_login() {


        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description: </b>Reader management Login");
        psLog.println("<br><b>Expected Result:</b> Reader management should be logged in");
        bSuccess = false;

        //Calling RM_Login()
        int status = RM_Login();
        if (status == success) {
            boolean loggedin = readerMgt.isLoggedIn();
            if (loggedin) {
                psLog.println("<br><b>Actual Result:</b> Reader management is logged IN");
                bSuccess = true;
            } else {
                bSuccess = false;
            }

            LogStatus(bSuccess, "ReaderManagement_LOGIN", i);
        }
    }//Test_RM_login() Method

    public void Test_RM_Logout() {
        SubNo = 0;
        bSuccess = false;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description: </b>Reader management Logout");
        psLog.println("<br><b>Expected Result:</b>Reader management should be logged out");
        psLog.println("<br>Inside the Reader Management Logout Method:");
        //Calling RM_Login();
        int status = RM_Login();

        if (status == success) {
            boolean loggedin = readerMgt.isLoggedIn();
            if (loggedin) {
                if (RM_Logout() == success) {
                    psLog.println("<br><b>Actual Result:</b>Reader management is logged out");
                    bSuccess = true;
                } else {
                    bSuccess = false;
                }
            }
            LogStatus(bSuccess, "ReaderManagement_LOGOUT", i);
        }
    }//Test_RM_Logout()

    public void Test_GetReadPointStatus() {
        int status = RM_Login();
        SubNo = 0;
        bSuccess = false;
        if (status == success) {
            for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++) {
                FormTestID(TestNo++, SubNo, "RM");
                psLog.println("<br><b>Description: </b>Test_GetReadPointStatus ");
                psLog.println("<br><b>Expected Result:</b>ReadPointStatus should be ENABLE or DISABLE");
                psLog.println("<br>Inside the Reader Management Test_GetReadPointStatus Method:");
                try {
//                    psLog.println("<br><b>Actual Result:</b> GetReadPointStatus is: " + readerMgt.ReadPoint.getReadPointStatus(i));
                    psLog.println("<br><b>Actual Result:</b> GetReadPointStatus for Antenna " + i + " is: " + readerMgt.ReadPoint.getReadPointStatus(i));
                    bSuccess = true;
                }//try block...
                catch (Exception e) {
                    bSuccess = false;
                    AnalyseException(psLog, e);
                }
                LogStatus(bSuccess, "GetReadPointStatus", i);
            } //for loop
            status = RM_Logout();
        }//if(status == success)
    }//Test_GetReadPointStatus()Method

    public void Test_SetReadPointStatus() {
        bSuccess = false;
        int status = RM_Login();
        if (status == success) {
            for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++) {
                FormTestID(TestNo++, SubNo, "RM");
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus");
                psLog.println("<br><b>Expected Result:</b>ReadPoingStatus should be DISABLE");
                psLog.println("<br>Inside the Reader Management Test_SetReadPointStatus Method:");
                try {
                    readerMgt.ReadPoint.setReadPointStatus(i, READPOINT_STATUS.DISABLE);
                    if (READPOINT_STATUS.DISABLE == readerMgt.ReadPoint.getReadPointStatus(i)) {
                        psLog.println("<br><b>Actual Result:</b> SetReadPointStatus :DISABLE");
                    }
                    bSuccess = true;
                } catch (Exception e) {
                    bSuccess = false;
                    AnalyseException(psLog, e);
                }
                LogStatus(bSuccess, "SetReadPointStatus_DISABLE", i);
            }


//Test_SetReadPointStatus() for  READPOINT_STATUS.ENABLE ......
            bSuccess = false;
            for (short i = 1; i <= reader.ReaderCapabilities.getNumAntennaSupported(); i++) {
                FormTestID(TestNo++, SubNo, "RM");
                psLog.println("<br><b>Description: </b>Test_SetReadPoingStatus");
                psLog.println("<br><b>Expected Result:</b>ReadPointStatus for Antenna " + i + " should be ENABLE");

                psLog.println("<br>Inside the Reader Management Test_SetReadPointStatus Method:");
                try {
                    readerMgt.ReadPoint.setReadPointStatus(i, READPOINT_STATUS.ENABLE);
                    if (READPOINT_STATUS.ENABLE == readerMgt.ReadPoint.getReadPointStatus(i)) {
                        psLog.println("<br><b>Actual Result:</b> SetReadPointStatus for Antenna " + i + " :ENABLE");
                        bSuccess = true;
                    }
                } catch (Exception e) {
                    bSuccess = false;
                    AnalyseException(psLog, e);
                }
                LogStatus(bSuccess, "SetReadPointStatus_ENABLE", i);
            }//for loop...
            status = RM_Logout();
        }// if(status == success)
    } // Test_SetReadPointStatus()Methode..

    public void Test_GetAntennaMode() {
        bSuccess = false;
//        SubNo = 0;TestNo=15;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description: </b>Test_GetAntennaMode");
        psLog.println("<br><b>Expected Result:</b>GetAntennaMode should be BISTATIC or MONOSTATIC");
        psLog.println("<br>Inside the Reader Management Test_GetAntennaMode Method:");
        int status = RM_Login();
        if (status == success) {
            try {
                psLog.println("<br><b>Actual Result:</b> " + readerMgt.getAntennaMode());
                bSuccess = true;
            } catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            short i = 0;
            LogStatus(bSuccess, "GetAntennaMode", i);
            status = RM_Logout();

        } //if(status == success)

    } //Test_SetAntennaMode() Method

    public void Test_SetAntennaMode() {
        bSuccess = false;
        int status = RM_Login();
        if (status == success) {
            FormTestID(TestNo++, SubNo, "RM");
            try {
                psLog.println("<br><b>Description: </b>Set AntennaMode to Bistatic");
                psLog.println("<br><b>Expected Result:</b>Option is not Valid Exception should be thrown");
                if (readerMgt.getAntennaMode() != ANTENNA_MODE.ANTENNA_MODE_BISTATIC) {
                    readerMgt.setAntennaMode(ANTENNA_MODE.ANTENNA_MODE_BISTATIC);
                    psLog.println("<br>Antenna Mode set to: " + readerMgt.getAntennaMode());
                } else {
                    readerMgt.setAntennaMode(ANTENNA_MODE.ANTENNA_MODE_MONOSTATIC);
                    psLog.println("<br>Antenna Mode set to: " + readerMgt.getAntennaMode());
                }
                bSuccess = false;
            }//try block...
            catch (Exception e) {
                bSuccess = true;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "setAntennaMode", i);
            status = RM_Logout();
        }//if(status == success)
    }//Test_SetAntennaMode()Methode

    public void Test_getProfileList() {
        bSuccess = false;
        SubNo = 0;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description: </b>Test_getProfileList");
        psLog.println("<br><b>Expected Result:</b>All the Profiles From the ReaderManagement should be listed");
        psLog.println("<br>Inside the Reader Management Test_getProfileList Method:");
        int status = RM_Login();
        if (status == success) {
            try {
                boolean flag;
                Profile profile = readerMgt.Profile;
                ProfileInfo profileList = profile.getList();
                String[] profileListk = profileList.getProfileNames();
                psLog.println("<br><b>Actual Result:</b>");
                getListProfile(profileListk);
                bSuccess = true;
            }//try block....
            catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "getProfileList", i);
            status = RM_Logout();
        }//if(status == success)
    } //Test_getProfileList() Method

    public void Test_ImportProfile() {
        bSuccess = false;
        SubNo = 0;
        FormTestID(TestNo++, SubNo, "RM");

        psLog.println("<br><b>Description: </b>Download all the profiles from the ReaderManagement");
        psLog.println("<br><b>Expected Result:</b>All the Profiles should be downloaded from the ReaderManagement");
        psLog.println("<br>Inside the Reader Management Test_getProfileList Method:");
        int status = RM_Login();
        if (status == success) {
            try {
                boolean flag;
                Profile profile = readerMgt.Profile;
                ProfileInfo profileList = profile.getList();
                String[] profileListk = profileList.getProfileNames();
                //dowloaded the profile...................................

                psLog.println("<br>download the profile");
                profile.importFromReader("Default.xml", currentDir);
                psLog.println("<br>Profile download success");
                psLog.println("<br><b>Actual Result:</b>");
                getListProfile(profileListk);
                bSuccess = true;
            }//Try Block............
            catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "ImportProfile", i);
            status = RM_Logout();
        } //if(status == success)
    } //Test_getProfileList() Method

    public void Test_DeleteActiveProfile() {
        bSuccess = false;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description: </b>Deleting Active profile");
        psLog.println("<br>Expected Result:Profile should not be deleted");
        psLog.println("<br>Actual Result: ");
        int status = RM_Login();
        try {
            Profile profile = readerMgt.Profile;
            ProfileInfo profileList = profile.getList();
            String[] profileListk = profileList.getProfileNames();
            int active = profileList.getActiveProfileIndex();
            profile.delete(profileListk[active]);
            bSuccess = false;
        } catch (Exception e) {
            bSuccess = true;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "DeleteActiveProfile", i);
        status = RM_Logout();
    }

    public void Test_ExportProfile() throws IOException {
        SubNo = 0;
        bSuccess = false;
        FormTestID(TestNo++, SubNo, "RM");


        psLog.println("<br><b>Description: </b>Upload  abc.xml and all the profiles into the ReaderMenagement");
        psLog.println("<br><b>Expected Result:</b> abc.xml and all the profiles should be Uploaded into the ReaderManagement");
        psLog.println("<br>Inside the Reader Management Test_getProfileList Method:");
        int status = RM_Login();
        if (status == success) {
            try {
                boolean flag;
                Profile profile = readerMgt.Profile;
                ProfileInfo profileList = profile.getList();
                String[] profileListk = profileList.getProfileNames();

                //     dowloaded the profile...................................
                psLog.println("<br>download the profile");
                profile.importFromReader("Default.xml", currentDir);
                psLog.println("<br>Profile download success");

                // Rename the profile.........................................

                psLog.println("<br>Rename the profile .....");
                String currentDir = System.getProperty("user.dir");
                //System.out.print("\n " + currentDir);
                File oldFile = new File(currentDir + "\\Default.xml");
                oldFile.renameTo(new File(currentDir + "\\abc.xml"));
                profile.exportToReader("abc.xml", currentDir, true);



                //Upload the Profile...........................................
                psLog.println("<br>Upload the profile");


                profileListk = profile.getList().getProfileNames();
                profile.getList().getActiveProfileIndex();
                for (int length = 0; length < profileListk.length; length++) {
                    if (profileListk[0].equals(profileListk[length])) {
                        bSuccess = true;
                    }
                    psLog.println(", " + profileListk[length]);
                }
                psLog.println("<br><b>Actual Result:</b>");


            }//try block....
            catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "ExportProfile", i);
            status = RM_Logout();
        }//if(status == succes
    }//Test_ExportProfile() methode............

    public void Test_DeletProfile() {
        SubNo = 0;
        bSuccess = false;
//        TestNo = 21;
        FormTestID(TestNo++, SubNo, "RM");

        psLog.println("<br><b>Description: </b>Delete Uploaded Profile from the ReaderManagement");
        psLog.println("<br><b>Expected Result:</b> Remaining profiles from the ReaderManagement");
        psLog.println("<br>Inside the Reader Management Test_DeletProfile() Method:");
        int status = RM_Login();
        if (status == success) {
            try {
                boolean flag;
                int setactive[] = new int[1];
                Profile profile = readerMgt.Profile;
                ProfileInfo profileList = profile.getList();
                String[] profileListk = profileList.getProfileNames();

                //Upload the Profile
                psLog.println("<br>Upload the profile");
                profile.exportToReader("abc.xml", currentDir, true);
                getListProfile(profileListk);

                //Delete the profile
                psLog.println("<br>Delete the profile");
                readerMgt.Profile.delete("abc.xml");
                for (int i = 0; i < profileListk.length; i++) {
                    if (profileListk[0] == profileListk[i]) {
                        flag = true;
                        break;
                    }
                }
                if (flag = true) {
                    psLog.println("<br>Delete the profile: success");
                    psLog.println("<br>Current Available Profiles");
                    psLog.println("<br>");
                    profileListk = profile.getList().getProfileNames();
                    psLog.println("<br><b>Actual Result:</b>");
                    getListProfile(profileListk);
                    bSuccess = true;
                } else {
                    psLog.println("<br>Profile is not deleted yet");

                }

            }//Try Block.......
            catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "DeletProfile", i);
            status = RM_Logout();
        }//if(status == success)
    }//Test_DeletProfile()

    public void Test_getHealthStatus() {
        SubNo = 0;
        bSuccess = false;

        FormTestID(TestNo++, SubNo, "RM");

        psLog.println("<br><b>Description: </b>Test_getHealthStatus()");
        psLog.println("<br><b>Expected Result:</b>HealthStatus_UP");
        psLog.println("<br>Inside the Reader Management Test_getHealthStatus Method:");
        int status = RM_Login();

        if (status == success) {
            try {

                if (readerMgt.getHealthStatus(SERVICE_ID.RM) == readerMgt.getHealthStatus(SERVICE_ID.RM).UP) {
                    psLog.println("<br><b>Actual Result:</b>");
                    psLog.println(" " + "HealthStatus RM  " + readerMgt.getHealthStatus(SERVICE_ID.RM));
                    psLog.println(" " + "HealthStatus LLRP  " + readerMgt.getHealthStatus(SERVICE_ID.LLRP_SERVER));
                    bSuccess = true;
                }

            }//Try Block...................
            catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "getHealthStatus", i);
            status = RM_Logout();
        } //if(status == success)

    }//Test_getHealthStatus() Method

    public void Test_USB() {
        psLog.println("<br>Inside the Reader Management testUSB() Method:");
        int status;
        bSuccess = false;
        SubNo = 0;
//        TestNo = 164;

        try {
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description:</b> get USBmode");
            psLog.println("<br><b>Expected Result:</b>The LEDs should be glowing one after the other.");
            status = RM_Login();
            USBOperationMode uMode = readerMgt.getUSBOperationMode();
            USBOperationMode setMode = readerMgt.getUSBOperationMode();
            psLog.println("<br>getUSBOperationMode: " + uMode.getMode());
            if (uMode.getMode() == USB_OPERATION_MODE.ACTIVE_SYNC || uMode.getMode() == USB_OPERATION_MODE.NETWORK) {
                bSuccess = true;
            }

        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "getUSBOperationMode", i);


        try {
            USBOperationMode uMode = readerMgt.getUSBOperationMode();
            USBOperationMode setMode = readerMgt.getUSBOperationMode();
            bSuccess = false;
            FormTestID(TestNo++, SubNo, "RM");
            uMode.setAllowLLRPConnectionOverride(true);
            psLog.println("<br><b>Description: </b> Set USB operation mode to NETWOEK");
            psLog.println("<br>Expected: Operation mode should be set to NETWORK");
            uMode.setMode(USB_OPERATION_MODE.NETWORK);
            readerMgt.setUSBOperationMode(uMode);

            psLog.println("<br>Actual: Operation mode is set to " + setMode.getMode());
            if ((setMode.getAllowLLRPConnectionOverride() == true) && (setMode.getMode() == USB_OPERATION_MODE.NETWORK)) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "setUSBOperationMode:NETWORK", i);

        try {
            USBOperationMode uMode = readerMgt.getUSBOperationMode();
            USBOperationMode setMode = readerMgt.getUSBOperationMode();
            bSuccess = false;
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> Set USB operation mode to ACTIVE_SYNC");
            psLog.println("<br>Expected: Operation mode should be set to ACTIVE_SYNC");
            uMode.setMode(USB_OPERATION_MODE.ACTIVE_SYNC);
            readerMgt.setUSBOperationMode(uMode);
            setMode = readerMgt.getUSBOperationMode();
            psLog.println("<br>Actual: Operation mode is set to " + setMode.getMode());
            if ((setMode.getAllowLLRPConnectionOverride() == true) && (setMode.getMode() == USB_OPERATION_MODE.ACTIVE_SYNC)) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
        }//Try Block....
        catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus( bSuccess, "setUSBOperationMode:ACTIVE_SYNC", i);
        status = RM_Logout();

    }//testUSB() method

    public void Test_LEDinfo() {
        psLog.println("Inside the Reader Management Test_LEDinfo Method:");
        int status;
        bSuccess = false;
//        SubNo = 0; TestNo = 30;
        status = RM_Login();
        LED_COLOR ledColor[] = {LED_COLOR.LED_GREEN, LED_COLOR.LED_RED, LED_COLOR.LED_YELLOW};
//        for(int color=0;color<3;color++)
        for (short color = 0; color < 3; color++) {
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description:</b> Test_LEDinfo");
            psLog.println("Inside the Reader Management Test_LEDinfo Method:");
            LedInfo ledinfo = new LedInfo();
            psLog.println("setBlink: true");
            ledinfo.setBlink(true);
            psLog.println("getBlink: " + ledinfo.getBlink());
            psLog.println("setDuration 10 secs");
            ledinfo.setDurationSeconds(10);
            psLog.println("getDurationSeconds: " + ledinfo.getDurationSeconds());


            try {
                ledinfo.setLEDColor(ledColor[color]);
                readerMgt.setUserLED(ledinfo);
                Thread.sleep(11000);
                bSuccess = true;
            } catch (Exception e) {
                bSuccess = false;
                AnalyseException(psLog, e);
            }
            LogStatus(bSuccess, "LEDinfo for" + ledColor[color], i);
        }

        status = RM_Logout();
    }

    public void Test_setReaderLocalTime() {

        int status;
        bSuccess = false;
//        SubNo = 0;TestNo = 167;

        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> SetReaderLocalTime");
        psLog.println("Inside the Reader Management Test_setReaderLocalTime Method:");
        status = RM_Login();
        SYSTEMTIME time = new SYSTEMTIME();

        psLog.println("System time:" + time.GetCurrentTime());
        try {
            psLog.println("getLocalTime: " + readerMgt.getLocalTime());
            time.Year = 2012;
            readerMgt.setLocalTime(time);
            psLog.println("Setting time : Success");
            psLog.println("getLocalTime: " + readerMgt.getLocalTime());
            //time.Year = 2012;
            // readerMgt.setLocalTime(time);
            bSuccess = true;
        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "setReaderLocalTime", i);
        status = RM_Logout();
    }

    public void Test_getTimeZone() {

        int status;
        bSuccess = false;
//        SubNo = 1;TestNo = 168;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> GetTimeZone");
        psLog.println("Inside the Reader Management Test_getTimeZone Method:");
        status = RM_Login();
        int active[] = new int[1];
        active[0] = 0;
        try {
            //System.out.println(" before gettimezonelist function");
            TimeZoneInfo timezoneInfo = readerMgt.TimeZone.getList();
            //System.out.println(" after gettimezonelist function");
            psLog.println("getTimeZone:Current " + timezoneInfo.getTimeZones()[timezoneInfo.getActiveTimeZoneIndex()]);
            psLog.println("TimeZoneList.length: " + timezoneInfo.getTimeZones().length);

            for (int i = 0; i < timezoneInfo.getTimeZones().length; i++) {
                psLog.println("<br>" + timezoneInfo.getTimeZones()[i]);
                psLog.println("Setting the Time Zone to the next index");
                readerMgt.TimeZone.setActive((short) i);
                timezoneInfo = readerMgt.TimeZone.getList();
                psLog.println("getTimeZone: New active" + timezoneInfo.getTimeZones()[timezoneInfo.getActiveTimeZoneIndex()]);
                if (i == timezoneInfo.getActiveTimeZoneIndex()) {
                    psLog.println("Setting the Time Zone: Success");
                    bSuccess = true;
                } else {
                    psLog.println("Setting the Time Zone: failure");
                }
            }
        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "getTimeZone", i);
        status = RM_Logout();
    }

    public void Test_GetSysInfo() {

        int status;
        bSuccess = false;
//        SubNo = 0;TestNo = 36;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> GetSysInfo");
        psLog.println("Inside the Reader Management Test_GetSysInfo Method:");
        status = RM_Login();
        try {
            SystemInfo sysinfo = readerMgt.getSystemInfo();
            bSuccess = true;
            psLog.println("<br>getFPGAVersion: " + sysinfo.getFPGAVersion());
            psLog.println("<br>getFlashAvailable: " + sysinfo.getFlashAvailable());
            psLog.println("<br>getRAMAvailable: " + sysinfo.getRAMAvailable());
            psLog.println("<br>getRadioFirmwareVersion: " + sysinfo.getRadioFirmwareVersion());
            psLog.println("<br>getReaderLocation: " + sysinfo.getReaderLocation());
            psLog.println("<br>getReaderName: " + sysinfo.getReaderName());
            psLog.println("<br>getUpTime: " + sysinfo.getUpTime());
        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "GetSysInfo", i);
        status = RM_Logout();

    }

    public void Test_getReaderStatistics() {
        short k = 1;
        int status;
//        SubNo = 0;TestNo = 171;
        bSuccess = false;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br><b>Description:</b> GetReaderStatistics");
        psLog.println("<br>Inside the Reader Management Test_getReaderStatistics Method:");
        status = RM_Login();
        try {
            ReaderStatistics rdrstatis = readerMgt.getReaderStatistics(k);
            bSuccess = true;
            psLog.println("<br>getBlockEraseFailureCount: " + rdrstatis.getBlockEraseFailureCount());
            psLog.println("<br>getBlockEraseSuccessCount: " + rdrstatis.getBlockEraseSuccessCount());
            psLog.println("<br>getBlockPermalockFailureCount: " + rdrstatis.getBlockPermalockFailureCount());
            psLog.println("<br>getBlockPermalockSuccessCount: " + rdrstatis.getBlockPermalockSuccessCount());
            psLog.println("<br>getBlockWriteFailureCount: " + rdrstatis.getBlockWriteFailureCount());
            psLog.println("<br>getBlockWriteSuccessCount: " + rdrstatis.getBlockWriteSuccessCount());
            psLog.println("<br>getIdentifiedFailureCount: " + rdrstatis.getIdentifiedFailureCount());
            psLog.println("<br>getIdentifiedSuccessCount: " + rdrstatis.getIdentifiedSuccessCount());
            psLog.println("<br>getKillFailureCount: " + rdrstatis.getKillFailureCount());
            psLog.println("<br>getKillSuccessCount: " + rdrstatis.getKillSuccessCount());
            psLog.println("<br>getLockFailureCount: " + rdrstatis.getLockFailureCount());
            psLog.println("<br>getLockSuccessCount: " + rdrstatis.getLockSuccessCount());
            // psLog.println("<br>getLockSuccessCount: " + rdrstatis.
            psLog.println("<br>NXP statistics");
            NXPStats nxpstate = rdrstatis.getM_NXPStats();
            psLog.println("<br>getCalibrateFailureCount: " + nxpstate.getCalibrateFailureCount());
            psLog.println("<br>getCalibrateSuccessCount: " + nxpstate.getCalibrateSuccessCount());
            psLog.println("<br>getChangeEASFailureCount: " + nxpstate.getChangeEASFailureCount());
            psLog.println("<br>getChangeEASSuccessCount: " + nxpstate.getChangeEASSuccessCount());
            psLog.println("<br>getEASAlarmFailureCount: " + nxpstate.getEASAlarmFailureCount());
            psLog.println("<br>getEASAlarmSuccessCount: " + nxpstate.getEASAlarmSuccessCount());
            psLog.println("getCalibrateFailureCount: " + nxpstate.getCalibrateFailureCount());
            psLog.println("<br>getReadProtectFailureCount: " + nxpstate.getReadProtectFailureCount());
            psLog.println("<br>getReadProtectSuccessCount: " + nxpstate.getReadProtectSuccessCount());
            psLog.println("<br>getResetReadProtectFailureCount: " + nxpstate.getResetReadProtectFailureCount());
            psLog.println("<br>getResetReadProtectSuccessCount: " + nxpstate.getResetReadProtectSuccessCount());

            psLog.println("<br>getReadFailureCount: " + rdrstatis.getReadFailureCount());
            psLog.println("<br>getReadSuccessCount: " + rdrstatis.getReadSuccessCount());
            psLog.println("<br>getWriteFailureCount: " + rdrstatis.getWriteFailureCount());
            psLog.println("<br>getWriteSuccessCount: " + rdrstatis.getWriteSuccessCount());
            psLog.println("<br>Clearing the reader statistics");
            LLRPConnectionConfig llrpconConfig = readerMgt.LLRPConnection.getLLRPConnectionConfig();
            psLog.println("<br>getHostServerIP: " + llrpconConfig.getHostServerIP());
            psLog.println("<br>getPort: " + llrpconConfig.getPort());
            psLog.println("<br>isClient: " + llrpconConfig.isClient());
            bSuccess = true;

        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "GetReaderStatistics", i);
        status = RM_Logout();
    }

    public void Test_llrpConnectionOverride() {
        int status;
        bSuccess = false;
        status = RM_Login();
//        TestNo = 39;
        FormTestID(TestNo++, SubNo, "RM");
        psLog.println("<br>Inside the Reader Management Test_llrpConnectionOverride() Method:");
        psLog.println("<br><b>Description: </b> setAllowLLRPConnectionOverride: TRUE");
        psLog.println("<br>Expected: AllowLLRPConnectionOverride should be set: TRUE");
        try {
            USBOperationMode uMode = readerMgt.getUSBOperationMode();
            uMode.setMode(USB_OPERATION_MODE.NETWORK);
            // uMode.setMode(USB_OPERATION_MODE.ACTIVE_SYNC);

            uMode.setAllowLLRPConnectionOverride(true);
            readerMgt.setUSBOperationMode(uMode);
            psLog.println("<br>Actual: AllowLLRPConnectionOverride is set to " + uMode.getAllowLLRPConnectionOverride());
            if (uMode.getAllowLLRPConnectionOverride()) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
            LogStatus(bSuccess, "AllowLLRPConnectionOverride:true", i);

            
            bSuccess = false;
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> setAllowLLRPConnectionOverride: FALSE");
            psLog.println("<br>Expected: AllowLLRPConnectionOverride should not be set: FALSE");
            uMode.setAllowLLRPConnectionOverride(false);
            readerMgt.setUSBOperationMode(uMode);
            psLog.println("<br>Actual: AllowLLRPConnectionOverride is set to " + uMode.getAllowLLRPConnectionOverride());
            if (uMode.getAllowLLRPConnectionOverride()) {
                bSuccess = false;

            } else {
                bSuccess = true;
            }

        } catch (Exception e) {
            bSuccess = false;
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "AllowLLRPConnectionOverride:false", i);
        status = RM_Logout();
    }//llrp over ride

    public void Test_ReaderInfo() {
        psLog.println("Inside the Reader Management Test_ReaderInfo Method:");
        int status = RM_Login();
        ReaderInfo readerinfo;
        bSuccess = false;
        try {
            readerinfo = readerMgt.getReaderInfo();
            //reader name
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> setName-My reade");
            psLog.println("<br>Expected: Reader name should be set");
            readerinfo.setName("My reader");
            psLog.println("<br>Actual: " + readerinfo.getName());
            if ("My reader".equals(readerinfo.getName())) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        
        LogStatus(bSuccess, "ReaderManagement_SetGetReaderName:", i);
        
        bSuccess = false;
        //Reader descrption
        try {
            readerinfo = readerMgt.getReaderInfo();
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> setDescrption-My description");
            psLog.println("<br>Expected: Reader Description should be set");
            readerinfo.setDescription("My description");
            psLog.println("<br>Actual: " + readerinfo.getDescription());
            if ("My description".equals(readerinfo.getDescription())) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "ReaderManagement_SetGetReaderDescription", i);
        
        bSuccess = false;
        // Reader Location
        try {
            readerinfo = readerMgt.getReaderInfo();
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> setDescrption-My Location");
            psLog.println("<br>Expected: Reader Description should be set");
            readerinfo.setLocation("My Location");
            psLog.println("<br>Actual: " + readerinfo.getLocation());
            if ("My Location".equals(readerinfo.getLocation())) {
                successCount++;
            } else {
                failureCount++;
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "ReaderManagement_SetGetReaderLocation", i);

        //Reader Contact
        bSuccess = false;
        try {
            readerinfo = readerMgt.getReaderInfo();
            FormTestID(TestNo++, SubNo, "RM");
            psLog.println("<br><b>Description: </b> setDescrption-My Contact");
            psLog.println("<br>Expected: Reader Description should be set");
            readerinfo.setContact("My Contact");
            psLog.println("<br>Actual: " + readerinfo.getContact());
            if ("My Contact".equals(readerinfo.getContact())) {
                bSuccess = true;
            } else {
                bSuccess = false;
            }
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "ReaderManagement_SetGetReaderContact", i);
        
        status = RM_Logout();


    }

    public void Test_tracelevel() {
        psLog.println("Inside the Reader Management Test_tracelevel Method:");
        bSuccess = false;
        try {
            FormTestID(TestNo++, SubNo, "RM");
            readerMgt.setTraceLevel(TRACE_LEVEL.TRACE_LEVEL_ALL);
            TRACE_LEVEL trace[] = readerMgt.getTraceLevel();
            bSuccess = true;
            //readerMgt.setTraceLevel(trace.TRACE_LEVEL_ALL);
            psLog.println("");
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
        LogStatus(bSuccess, "ReaderManagement_SetGetTraceLevel", i);
        int status = RM_Logout();
    }
}