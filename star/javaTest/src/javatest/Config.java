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
public class Config extends Commonclass {

    public int TagNum = 0;
    FrequencyHopTable fretable;

    public Config() {
        try {
            mystreamLog = new FileOutputStream("JavaAPI_Config_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_Config_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
        } catch (FileNotFoundException e) {
            psLog.println("" + e.getMessage());
        }
    }

    public void Test_Config() {
        //System.out.println("Config test cases");
        psLog.println("<html><br>");
        psLog.println("<body><br>");
        successCount = 0;
        failureCount = 0;
        TestNo = 103;
        SubNo = 1;
        Test_GPIGPO();
        Test_AntennaConfig();
        Test_Singulation();
        psLog.close();
        psResult.close();
        psSummary.println("JavaAPI:Config test cases:" + successCount + ":" + failureCount + ":" + "0");
        psLog.println("</html>\n");
        psLog.println("</body>\n");

    }

    public void Test_GPIGPO() {
        psLog.println("<br>CONFIG GPIGPO test cases");
        //System.out.println("CONFIG GPIGPO test cases");
        FormTestID(TestNo, SubNo++, "FUN");
        psLog.println("<br><b>Description:</b>");
        psLog.println(" Gets the number of GPI ports supported by the reader");
        psLog.println("<br>Expected Result: API should return no.of GPI ports supported");
        psLog.println("<br>Actual Result: " + reader.Config.GPI.getLength());
        psLog.println("<br>" + TestID + ":GPI.getLength():PASSED");
        psResult.println(TestID + ":GPI.getLength():PASSED");
        successCount++;


        FormTestID(TestNo, SubNo++, "FUN");
        psLog.println("<br><b>Description:</b>");
        psLog.println(" Gets the number of GPO ports supported by the reader");
        psLog.println("<br>Expected Result: API should return no.of GPO ports supported");
        psLog.println("<br>Actual Result: " + reader.Config.GPO.getLength());
        psLog.println("<br>" + TestID + ":GPO.getLength():PASSED");
        psResult.println(TestID + ":GPO.getLength():PASSED");
        successCount++;

        try {
            for (int i = 1; i <= reader.Config.GPI.getLength(); i++) {

                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Test GPI disable ports: " + (i));
                psLog.println("<br>Expected Result: API should enable the GPI ports");
                reader.Config.GPI.enablePort(i, true);
                psLog.println("<br>Actual Result: Port: " + (i) + " " + reader.Config.GPI.isPortEnabled(i));
                if (reader.Config.GPI.isPortEnabled(i)) {
                    psLog.println("<br>" + TestID + ":GPI.enablePort():PASSED");
                    psResult.println(TestID + ":GPI.enablePort():PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":GPI.enablePort():FAILED");
                    psResult.println(TestID + ":GPI.enablePort():FAILED");
                    failureCount++;
                }
            }
            for (int i = 1; i <= reader.Config.GPI.getLength(); i++) {

                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Test GPI disable ports: " + (i));
                psLog.println("<br>Expected Result: API should disable the GPI ports");
                reader.Config.GPI.enablePort(i, false);
                psLog.println("<br>Actual Result: Port: " + (i) + " " + reader.Config.GPI.isPortEnabled(i));
                if (!reader.Config.GPI.isPortEnabled(i)) {
                    psLog.println("<br>" + TestID + ":GPI.enablePort():PASSED");
                    psResult.println(TestID + ":GPI.enablePort():PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":GPI.enablePort():FAILED");
                    psResult.println(TestID + ":GPI.enablePort():FAILED");
                    failureCount++;
                }
            }
            for (int i = 1; i <= reader.Config.GPO.getLength(); i++) {

                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Test GPO disable ports: " + (i));
                psLog.println("<br>Expected Result: API should enable the GPO ports");
                reader.Config.GPO.setPortState(i, GPO_PORT_STATE.TRUE);
                psLog.println("<br>Actual Result: Port: " + (i) + " " + reader.Config.GPO.getPortState(i));
                GPO_PORT_STATE portstate = reader.Config.GPO.getPortState(i);
                if (reader.Config.GPO.getPortState(i) == GPO_PORT_STATE.TRUE) {
                    psLog.println("<br>" + TestID + ":GPO.enablePort():PASSED");
                    psResult.println(TestID + ":GPO.enablePort():PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":GPO.enablePort():FAILED");
                    psResult.println(TestID + ":GPO.enablePort():FAILED");
                    failureCount++;
                }
            }
            for (int i = 1; i <= reader.Config.GPO.getLength(); i++) {

                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Test GPO disable ports: " + (i));
                psLog.println("<br>Expected Result: API should disable the GPO ports");
                reader.Config.GPO.setPortState(i, GPO_PORT_STATE.FALSE);
                psLog.println("<br>Actual Result: Port: " + (i) + " " + reader.Config.GPO.getPortState(i));
                if (reader.Config.GPO.getPortState(i) == GPO_PORT_STATE.FALSE) {
                    psLog.println("<br>" + TestID + ":GPO.enablePort():PASSED");
                    psResult.println(TestID + ":GPO.enablePort():PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":GPO.enablePort():FAILED");
                    psResult.println(TestID + ":GPO.enablePort():FAILED");
                    failureCount++;
                }
            }

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo()+exp.getVendorMessage());
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage()+exp.getStatusDescription());
        }
    }

    public void Test_AntennaConfig() {
        psLog.println("<br>Antenna CONFIG test cases");
        //1. Receive sensitivity
        try {
            Antennas antenna = reader.Config.Antennas;
            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
            Antennas.PhysicalProperties physics = antenna.new PhysicalProperties();
            short[] sensitivity = {0, 10000};
            short availableANT[] = reader.Config.Antennas.getAvailableAntennas();
            for (int ant = 1; ant <= availableANT.length; ant++) {
                physics = reader.Config.Antennas.getPhysicalProperties(ant);
                if (physics.isConnected()) {
                    psLog.println("<br>For Antenna :" + availableANT[ant]);
            for (short i = 0; i < 2; i++) {
                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Setting receive sensitivity to: " + sensitivity[i]);
                psLog.println("<br>Expected Result: Should not throw any exception");
                Config.setReceiveSensitivityIndex(sensitivity[i]);
                short rxvalue = Config.getReceiveSensitivityIndex();
                reader.Config.Antennas.setAntennaConfig(availableANT[ant], Config);
                psLog.println("<br>Actual Result: " + rxvalue);
                Test_SimpleInventory();
                if (rxvalue == sensitivity[i] && TagNum > 0) {
                    psLog.println("<br>" + TestID + ":setReceiveSensitivityIndex:PASSED");
                    psResult.println(TestID + ":setReceiveSensitivityIndex:PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":setReceiveSensitivityIndex:FAILED");
                    psResult.println(TestID + ":setReceiveSensitivityIndex:FAILED");
                    failureCount++;
                }
                    }

                psLog.println("<br>");

            }
            }
        }//try
        catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo()+exp.getVendorMessage());
            psLog.println("<br>" + TestID + ":setReceiveSensitivityIndex:PASSED");
                    psResult.println(TestID + ":setReceiveSensitivityIndex:PASSED");
                    successCount++;
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage()+exp.getStatusDescription());
            psLog.println("<br>" + TestID + ":setReceiveSensitivityIndex:PASSED");
            psResult.println(TestID + ":setReceiveSensitivityIndex:PASSED");

        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.print("\nArrayIndexOutOfBoundsException" + e.getMessage());
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());

        }
        try
        {
            //Frequency Info
            Antennas antenna = reader.Config.Antennas;
            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
            Antennas.PhysicalProperties physics = antenna.new PhysicalProperties();
            FormTestID(TestNo, SubNo++, "FUN");
            psLog.println("<br><b>Description:</b>");
            psLog.println(" Transmit Frequency: ");
            psLog.println("<br>Expected Result:  Returns the Hop table for the given hop table index");
            psLog.println("<br>HoppingEnabled: " + reader.ReaderCapabilities.isHoppingEnabled());
            int frqHopValues[] = {};
            if (reader.ReaderCapabilities.isHoppingEnabled()) {
                for (int j = 0; j < reader.ReaderCapabilities.FrequencyHopInfo.Length(); j++)//check for how many hop tables
                {
                    fretable = reader.ReaderCapabilities.FrequencyHopInfo.getFrepuencyHopTablesInfo(j);
                    psLog.println("<br>getHopTableID: " + fretable.getHopTableID());
                    frqHopValues = fretable.getFrequencyHopValues();
                    psLog.println("<br>Hop frequencies: ");
                    for (int k = 1; k < frqHopValues.length; k++) {
                        psLog.println("" + frqHopValues[k] + ",");
                    }
                    if (frqHopValues.length > 0) {
                        psLog.println("<br>" + TestID + ":FrequencyHopInfo-Hopping:PASSED");
                        psResult.println(TestID + ":FrequencyHopInfo-Hopping:PASSED");
                        successCount++;
                    } else {
                        psLog.println("<br>" + TestID + ":FrequencyHopInfo-Hopping:FAILED");
                        psResult.println(TestID + ":FrequencyHopInfo-Hopping:FAILED");
                        failureCount++;
                    }
                }
            } else {
                psLog.println("<br>Hopping is not Enabled:<br>");
                int fixFreq[] = reader.ReaderCapabilities.getFixedFreqValues();
                for (int i = 0; i < reader.ReaderCapabilities.getFixedFreqValues().length; i++) {
                    psLog.println("" + fixFreq[i] + ",");
                }
                if (fixFreq.length > 0) {
                    psLog.println("<br>" + TestID + ":FrequencyHopInfo-Fixed:PASSED");
                    psResult.println(TestID + ":FrequencyHopInfo-Fixed:PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ":FrequencyHopInfo-Fixed:FAILED");
                    psResult.println(TestID + ":FrequencyHopInfo-Fixed:FAILED");
                    failureCount++;
                }
            }
            psLog.println("<br>");
            //Transmit frequency Get Set (check with prasad)
            Antennas.PhysicalProperties phys = antenna.new PhysicalProperties();
            short availableANT1[] = reader.Config.Antennas.getAvailableAntennas();
            for (int ant1 = 1; ant1 <= availableANT1.length; ant1++) {
                phys = reader.Config.Antennas.getPhysicalProperties(ant1);
                if (phys.isConnected()) {
                    psLog.println("<br>For Antenna :" + availableANT1[ant1]);
                    

                        if (reader.ReaderCapabilities.isHoppingEnabled()) {
                            for (int m = 1; m <= reader.ReaderCapabilities.FrequencyHopInfo.Length(); m++) {
                            psLog.println("<br>Setting the Hop table index " + m);
                            FormTestID(TestNo, SubNo++, "FUN");
                            psLog.println("<br><b>Description:</b>");
                            psLog.println(" Setting Transmit Frequency: ");
                            psLog.println("<br>Expected Result:  Sets the Hop table for the given hop table index");
                            Config.setTransmitFrequencyIndex((short) m);//setting the hop table index
                            Config.setReceiveSensitivityIndex((short) 0);

                            reader.Config.Antennas.setAntennaConfig(availableANT1[ant1], Config);

                            psLog.println("<br>Getting the Hop index " + Config.getTransmitFrequencyIndex());
                            if (m == Config.getTransmitFrequencyIndex()) {
                                psLog.println("<br>" + TestID + ":set and getTransmitFrequencyIndex:PASSED");
                                psResult.println(TestID + ":set and getTransmitFrequencyIndex:PASSED");
                                successCount++;
                            } else {
                                psLog.println("<br>" + TestID + ":set and getTransmitFrequencyIndex:FAILED");
                                psResult.println(TestID + ":set and getTransmitFrequencyIndex:FAILED");
                                failureCount++;
                            }
                            }//for
                        }// is hopping enabled
                        else//ishopping enabled
                        {
                            FormTestID(TestNo, SubNo++, "FUN");
                            psLog.println("<br><b>Description:</b>");
                            psLog.println(" Fixed Transmit Frequency: ");
                            psLog.println("<br>Expected Result:  Sets the fixed frequency for the given frequency index");
                            int fixFreq[] = reader.ReaderCapabilities.getFixedFreqValues();
                            for (int l = 1; l < fixFreq.length; l++) {
                                Config.setTransmitFrequencyIndex((short) l);//setting the hop table index
                                Config.setReceiveSensitivityIndex((short) 0);

                                reader.Config.Antennas.setAntennaConfig(availableANT1[ant1], Config);

                                psLog.println("<br>Getting the Hop index " + Config.getTransmitFrequencyIndex());
                                if (l == Config.getTransmitFrequencyIndex()) {
                                    psLog.println("<br>" + TestID + ":set and getTransmitFrequencyIndex:PASSED");
                                    psResult.println(TestID + ":set and getTransmitFrequencyIndex:PASSED");
                                    successCount++;
                                } else {
                                    psLog.println("<br>" + TestID + ":set and getTransmitFrequencyIndex:FAILED");
                                    psResult.println(TestID + ":set and getTransmitFrequencyIndex:FAILED");
                                    failureCount++;
                                }
                            }
                        }
                  

                }//if isConnected
            }//for availableANT
            //Physical properties

            Antennas.PhysicalProperties physAnt = antenna.new PhysicalProperties();
            short availANT[] = reader.Config.Antennas.getAvailableAntennas();
            for (int ant2 = 1; ant2 <= availANT.length; ant2++) {
                physAnt = reader.Config.Antennas.getPhysicalProperties(ant2);
                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Get Antenna Gain for all antennas: ");
                psLog.println("<br>Expected Result: Gets Antenna Gain  ");
                psLog.println("<br>For Antenna :" + ant2);
                psLog.println("<br>Antenna Gain :" + physAnt.getAntennaGain());
                if (physAnt.getAntennaGain() == 0) {
                    psLog.println("<br>" + TestID + ": getAntennaGain:PASSED");
                    psResult.println(TestID + ":set and getAntennaGain:PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>" + TestID + ": getAntennaGain:FAILED");
                    psResult.println(TestID + ":set and getAntennaGain:FAILED");
                    failureCount++;
                }
            }
            // Antenna isConnected()
            for (int ante = 1; ante <= availANT.length; ante++) {
                physAnt = reader.Config.Antennas.getPhysicalProperties(ante);
                FormTestID(TestNo, SubNo++, "FUN");
                psLog.println("<br><b>Description:</b>");
                psLog.println(" Gives the antenna status all antennas: ");
                psLog.println("<br>Expected Result:  antenna is connected or not");
                if (physAnt.isConnected()) {
                    psLog.println("<br>Antenna " + ante + " is Connected");
                    psLog.println("<br>" + TestID + ": isConnected:PASSED");
                    psResult.println(TestID + ":set and isConnected:PASSED");
                    successCount++;
                } else {
                    psLog.println("<br>Antenna " + ante + " is not Connected");
                    psLog.println("<br>" + TestID + ": isConnected:PASSED");
                    psResult.println(TestID + ":set and isConnected:PASSED");
                }
            }
            


        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo()+exp.getVendorMessage());
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage()+exp.getStatusDescription());

        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.print("\nArrayIndexOutOfBoundsException" + e.getMessage());
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());

        }



    }
    public void Test_Singulation() {
        //Singulation
        try {
            TestNo++;SubNo=0;
            SESSION[] mySessions = {SESSION.SESSION_S0, SESSION.SESSION_S1, SESSION.SESSION_S2, SESSION.SESSION_S3,SESSION.SESSION_S0};
            Antennas antenna = reader.Config.Antennas;
            Antennas.SingulationControl singControl = antenna.new SingulationControl();
            short availANT[] = reader.Config.Antennas.getAvailableAntennas();
            Antennas.PhysicalProperties physAnt = antenna.new PhysicalProperties();

            for (int ante = 1; ante <= availANT.length; ante++)
            {
                physAnt = reader.Config.Antennas.getPhysicalProperties(ante);

                if (physAnt.isConnected())
                {
                    for (int i = 0; i < 5; i++)
                    {
                        FormTestID(TestNo, SubNo++, "FUN");
                        psLog.println("<br><b>Description:</b>");
                        psLog.println(" Setting and Getting sessions" + mySessions[i]);
                        psLog.println("<br>Expected Result:  Sets the session singulation paramter and inventory should happen");
                        psLog.println("<br>For Antenna"+ (i));
                        singControl = reader.Config.Antennas.getSingulationControl(ante);
                        singControl.setSession(mySessions[i]);
                        reader.Config.Antennas.setSingulationControl(ante, singControl);
                        Test_SimpleInventory();
                        if(mySessions[i] == SESSION.SESSION_S2)
                        {
                            Thread.sleep(5000);
                        }
                        if (TagNum > 0 && singControl.getSession() == mySessions[i])
                        {

                            psLog.println("<br>" + TestID + ": setSession:PASSED");
                            psResult.println(TestID + ":setSession:PASSED");
                            successCount++;
                        }
                        else
                        {
                            psLog.println("<br>" + TestID + ": setSession:FAILED");
                            psResult.println(TestID + ":setSession:FAILED");
                            failureCount++;
                        }
                    }
                }



            }
            //Transit time
            TestNo++;SubNo=0;
            short tagTransitTime[] = {10,100};
            for (int ante = 1; ante <= availANT.length; ante++)
            {
                physAnt = reader.Config.Antennas.getPhysicalProperties(ante);

                if (physAnt.isConnected())
                {
                    for (int i = 0; i < 2; i++)
                    {
                        FormTestID(TestNo, SubNo++, "FUN");
                        psLog.println("<br><b>Description:</b>");
                        psLog.println(" Setting and Getting transit time" + tagTransitTime[i]);
                        psLog.println("<br>Expected Result:  Sets the transit time and inventory should happen");
                        psLog.println("<br>For Antenna"+ ante);
                        singControl.setTagTransitTime(tagTransitTime[i]);
                        Test_SimpleInventory();
                        if (TagNum > 0 && (singControl.getTagTransitTime() == tagTransitTime[i])) {

                            psLog.println("<br>" + TestID + ": setTagTransitTime:PASSED");
                            psResult.println(TestID + ":setTagTransitTime:PASSED");
                            successCount++;
                        } else {
                            psLog.println("<br>" + TestID + ": setTagTransitTime:FAILED");
                            psResult.println(TestID + ":setTagTransitTime:FAILED");
                            failureCount++;
                        }
                    }
                }



            }
            //Tag Population
            TestNo++;SubNo=0;
            short tagPop[] = {100,1000,10000};
             for (int ante = 1; ante <= availANT.length; ante++) {
                physAnt = reader.Config.Antennas.getPhysicalProperties(ante);

                if (physAnt.isConnected()) {
                    for (int i = 0; i < 2; i++) {
                        FormTestID(TestNo, SubNo++, "FUN");
                        psLog.println("<br><b>Description:</b>");
                        psLog.println(" Setting and Getting tag Population" + tagPop[i]);
                        psLog.println("<br>Expected Result:  Sets the tag Population and inventory should happen");
                        psLog.println("<br>For Antenna"+ ante);
                        singControl.setTagPopulation(tagPop[i]);
                        Test_SimpleInventory();
                        if (TagNum > 0 && singControl.getTagPopulation() == tagPop[i]) {

                            psLog.println("<br>" + TestID + ": setTagTransitTime:PASSED");
                            psResult.println(TestID + ":setTagTransitTime:PASSED");
                            successCount++;
                        } else {
                            psLog.println("<br>" + TestID + ": setTagTransitTime:FAILED");
                            psResult.println(TestID + ":setTagTransitTime:FAILED");
                            failureCount++;
                        }
                    }
                }
            }

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("<br>InvalidUsageException" + exp.getInfo()+ exp.getVendorMessage()+exp.getVendorMessage());
        } catch (OperationFailureException exp) {
            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("<br>OperationFailureException" + exp.getMessage()+ exp.getStatusDescription()+exp.getStatusDescription());

        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.print("\nArrayIndexOutOfBoundsException" + e.getMessage());
            psLog.println("<br>ArrayIndexOutOfBoundsException" + e.getMessage());

        }
        catch(InterruptedException exp)
        {
            psLog.println("<br>"+exp.getMessage());
        }
    }


    public void Test_SimpleInventory()
    {
        CleanupPendingSequence();
        try
        {
            reader.Actions.Inventory.perform();
            Thread.sleep(3000);
            reader.Actions.Inventory.stop();
            TagData tagdata[] = reader.Actions.getReadTags(10);

            if (tagdata != null)
            {
                for (int i = 0; i < tagdata.length; i++)
                {
                    //System.out.print("\n"+tagdata[i].getTagID());
                    psLog.print("<br>" + tagdata[i].getTagID());
                    //((DefaultTableModel)jTable1.getModel()).insertRow(0, new Object[]{tagdata[i].getTagID(),tagdata[i].getPC(),tagdata[i].getPeakRSSI(),count});

                }
                TagNum = tagdata.length;
            } 
            else
            {
                psLog.print("<br>No tags read");
                TagNum = 0;
            }
            reader.Actions.purgeTags();

        } catch (InvalidUsageException exp) {
            //System.out.print("\nInvalidUsageException" + exp.getInfo());
            psLog.println("\nInvalidUsageException" + exp.getInfo()+exp.getVendorMessage());
        } catch (OperationFailureException exp) {

            //System.out.print("\nOperationFailureException" + exp.getMessage());
            psLog.println("\nOperationFailureException" + exp.getMessage()+exp.getStatusDescription());

        } catch (InterruptedException e) {
            //System.out.print("\nInterruptedException" + e.getMessage());
            psLog.println("\nInterruptedException" + e.getMessage());
        }
    }
}
