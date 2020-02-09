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
public class RFMode extends Commonclass {
    int count=0;
    boolean flag=true;

    public void Test_RFmodePowerIndex()
        {
            try
            {
                mystreamLog = new FileOutputStream("JavaAPI_RFmodePowerIndex_Log.html");
                mystreamResult = new FileOutputStream("JavaAPI_RFmodePowerIndex_Result.txt");
                psLog = new PrintStream(mystreamLog);
                psResult = new PrintStream(mystreamResult);
            } 
            catch (FileNotFoundException e)
            {
                psLog.println("" + e.getMessage());
            }

            try
            {
                Antennas antenna = reader.Config.Antennas;
                Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
                Config.setTransmitPowerIndex((short) (reader.ReaderCapabilities.getTransmitPowerLevelValues().length-1));
                reader.Config.Antennas.setAntennaConfig(1, Config);
            }
            catch (InvalidUsageException exp)
            {
                psResult.println("Antenna Configuration:FAILED");
                psLog.print("<br>Antenna Configuration:FAILED");
                //System.out.print("\nInvalidUsageException" + exp.getInfo());
                psLog.println("<br>InvalidUsageException" + exp.getInfo());
            }
            catch (OperationFailureException exp)
            {
                psResult.println("Antenna Configuration:FAILED");
                psLog.print("Antenna Configuration:FAILED");
                //System.out.print("\nOperationFailureException" + exp.getMessage());
                psLog.println("<br>OperationFailureException" + exp.getMessage());
            }
            successCount = 0;
            failureCount = 0;
            TestNo = 110;
            SubNo = 1;
            System.out.println("RF mode test cases");
            Antennas.RFMode antennaRFMode;
            int arr[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();
            int txPowerValues = arr.length;
            try {
                psLog.println("<br>Setting to factory default");
                reader.Config.resetFactoryDefaults();
                //Seting and getting RF mode
                RFModeTable obRF = reader.ReaderCapabilities.RFModes.getRFModeTableInfo(0);

                psLog.println("<br>Description:Set and Get RFMode");
                psLog.println("<br>Expected OutPut:Set mode and getmode should be equal");
                int rfTableEntries = obRF.length();
                int Powervalues[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();
                for (int k = 1; k <= reader.ReaderCapabilities.getNumAntennaSupported(); k++)//antenna
                {
                    psLog.println("<br>Antenna: " + k);
                    psLog.println("<br>==========");
                    Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(k);
                    psLog.println("ReceiveSensitivityIndex: " + Config.getReceiveSensitivityIndex());
                    psLog.println("TransmitFrequencyIndex: " + Config.getTransmitFrequencyIndex());
                    psLog.println("TransmitPowerIndex: " + Config.getTransmitPowerIndex());
                    
                    for (int rfmode = 0; rfmode < rfTableEntries; rfmode++)
                    {
                        FormTestID(TestNo, SubNo++, "FUN");
                        psLog.println("<br><br>" + TestID);
                        psLog.println("<br>Setting the mode Index to : " + rfmode);
                        antennaRFMode = reader.Config.Antennas.getRFMode(k);
                        antennaRFMode.setTableIndex(rfmode);
                        reader.Config.Antennas.setRFMode(k, antennaRFMode);
                        psLog.println(" setRFMode:Success");
                        antennaRFMode = reader.Config.Antennas.getRFMode(k);

                        psLog.println("<br>Set value:" + rfmode + " Get value:" + antennaRFMode.getTableIndex());
                        if (rfmode == antennaRFMode.getTableIndex())
                        {
                            psLog.println("<br>" + TestID + ": Setting mode index " + rfmode + " Antenna" + k + ":PASSED");
                            psResult.println("" + TestID + ": Setting mode index " + rfmode + " Antenna" + k + ":PASSED");
                            successCount++;
                        }
                        else
                        {
                            psLog.println("<br>" + TestID + ": Setting mode index " + rfmode + " Antenna" + k + ":FAILED");
                            psResult.println("" + TestID + ": Setting mode index " + rfmode + " Antenna" + k + ":FAILED");
                            failureCount++;
                        }
                    }
                }
                //RFModeTable obRF = reader.ReaderCapabilities.RFModes.getRFModeTableInfo(0);
                reader.Config.resetFactoryDefaults();
                Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(1);
                for (short j = 0; j < txPowerValues; j++)
                {
                    j++;
                    FormTestID(TestNo, SubNo++, "FUN");
                    psLog.println("<br><br>" + TestID);
                    psLog.println("<br>Description:Set and Get RFMode with Inventory");
                    psLog.println("<br>Expected OutPut:Should Read the Tags. ");
                    psLog.println("<br>Setting the power Index to : " + j);
                    psLog.println("<br>===========================");
                    psLog.println("<br>TransmitPowerIndex: " + j);
                    Config.setTransmitPowerIndex(j);
                    for (int ant = 1; ant <= reader.ReaderCapabilities.getNumAntennaSupported(); ant++)
                    {
                        reader.Config.Antennas.setAntennaConfig(ant, Config);
                        psLog.println(" setAntennaConfig on antenna :"+ant+" Success");
                    }

                    for (int i = 0; i < rfTableEntries; i++)
                    {
                        psLog.println("<br>Setting the mode Index to : " + i);
                        antennaRFMode = reader.Config.Antennas.getRFMode(1);
                        antennaRFMode.setTableIndex(i);
                        for (int ant = 1; ant <= reader.ReaderCapabilities.getNumAntennaSupported(); ant++)
                        {
                            reader.Config.Antennas.setRFMode(ant, antennaRFMode);
                            psLog.println(" setRFMode on antenna :"+ant+" Success");
                        }

                        antennaRFMode = reader.Config.Antennas.getRFMode(1);
                        psLog.println(" GetTableIndex: " + antennaRFMode.getTableIndex());
                        Test_SimpleInventory(reader, psLog);
                        count++;
                    }
                    
                    if (count == rfTableEntries && flag == true)
                    {
                        psLog.println("<br>" + TestID + ": RF Power " + j + " Value:" + Powervalues[j] + " and different mode index:PASSED");
                        psResult.println("" + TestID + ": RF Power " + j + " Value:" + Powervalues[j] + " and different mode index:PASSED");
                        successCount++;
                        count = 0;
                    }
                    else
                    {
                        flag = true;
                        psLog.println("<br>" + TestID + ": RF Power " + j + " Value:" + Powervalues[j] + " and different mode index:FAILED");
                        psResult.println("" + TestID + ": RF Power " + j + " Value:" + Powervalues[j] + " and different mode index:FAILED");
                        failureCount++;
                        count = 0;
                    }
                }
            }
            catch (InvalidUsageException exp)
            {
                //System.out.print("\nInvalidUsageException" + exp.getInfo());
                psLog.println("<br>InvalidUsageException: " + exp.getInfo());
                psLog.println("<br>getVendorMessage: " + exp.getVendorMessage());


            }
            catch (OperationFailureException exp)
            {
                //System.out.print("\nOperationFailureException" + exp.getMessage());
                psLog.println("<br>nOperationFailureException: " + exp.getStatusDescription());
                psLog.println("<br>getVendorMessage: " + exp.getVendorMessage());
            }
            psLog.close();
            psResult.close();
            psSummary.println("JavaAPI:RFModeandPower test cases:" + successCount + ":" + failureCount + ":" + "0");


    }
    
    public void Test_SimpleInventory(RFIDReader reader,PrintStream psLog)
    {
        try
        {
            reader.Actions.Inventory.perform();
            Thread.sleep(1000);
            reader.Actions.Inventory.stop();
            TagData tagdata[]=reader.Actions.getReadTags(1000);
            if( tagdata != null )
            {
                if(tagdata.length > 0)
                {
                   psLog.print("No.of Tags read:"+tagdata.length);
                   reader.Actions.purgeTags();
                   flag = true;
                }
            }
            else
            {
                psLog.print("No.of Tags read: 0");
                flag = false;
                //psLog.print("\nThe test case is FAIL");
            }
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
                psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {
            System.out.print("\nInterruptedException"+e.getMessage());
            psLog.println("\nInterruptedException"+e.getMessage());
        }

    }
}
