/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.psSummary;

/**
 *
 * @author NVJ438
 */
public class Capabilities extends Commonclass {

     public Capabilities() {
         try {
            mystreamLog = new FileOutputStream("JavaAPI_Capabilities_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_Capabilities_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            AppendText();
        } catch (Exception e) {
            AnalyseException(psLog, e);
        }
    }

    
    // static GetEvents getEvents;
    public void capabilitiesMethod() {
        successCount = 0;
        failureCount = 0;
        
        try {
            TestNo = 1;
            SubNo = 0;
            FormTestID(TestNo, SubNo++, "CAP");
            psLog.println("<br>Reader Identification");
            System.out.println(" Reader Identification");
            
            psLog.println("<br>Reader ID: " + reader.ReaderCapabilities.ReaderID.getID());
            psLog.println("<br>Reader ID Type:" + reader.ReaderCapabilities.ReaderID.getReaderIDType());
            
            System.out.println(" Reader ID: " + reader.ReaderCapabilities.ReaderID.getID());
            System.out.println(" Reader ID Type:" + reader.ReaderCapabilities.ReaderID.getReaderIDType());
            
            psLog.println("<br>General Capabilities");
            psLog.println("<br>Firmware Version: " + reader.ReaderCapabilities.getFirwareVersion());
            psLog.println("<br>ModelName: " + reader.ReaderCapabilities.getModelName());
            psLog.println("<br>NumAntennaSupported: " + reader.ReaderCapabilities.getNumAntennaSupported());
            psLog.println("<br>NumGPIPorts: " + reader.ReaderCapabilities.getNumGPIPorts());
            psLog.println("<br>NumGPOPorts: " + reader.ReaderCapabilities.getNumGPOPorts());
            psLog.println("<br>UTCClockCapable: " + reader.ReaderCapabilities.isUTCClockSupported());
            
            System.out.println(" General Capabilities");
            System.out.println(" Firmware Version: " + reader.ReaderCapabilities.getFirwareVersion());
            System.out.println(" ModelName: " + reader.ReaderCapabilities.getModelName());
            System.out.println(" NumAntennaSupported: " + reader.ReaderCapabilities.getNumAntennaSupported());
            System.out.println(" NumGPIPorts: " + reader.ReaderCapabilities.getNumGPIPorts());
            System.out.println(" NumGPOPorts: " + reader.ReaderCapabilities.getNumGPOPorts());
            System.out.println(" UTCClockCapable: " + reader.ReaderCapabilities.isUTCClockSupported());
            
            int a[] = reader.ReaderCapabilities.getReceiveSensitivityValues();
            psLog.println("<br>ReceiveSensitivityTable: ");
            System.out.println(" ReceiveSensitivityTable: ");
            
            for (int i = 0; i < a.length; i++) {
                psLog.println(a[i]);
            }
            psLog.println("<br>Tag Event Reporting: " + reader.ReaderCapabilities.isTagEventReportingSupported());
            psLog.println("<br>RSSI Filter: " + reader.ReaderCapabilities.isRSSIFilterSupported());
            psLog.println("<br>NXP Command Support: " + reader.ReaderCapabilities.isNXPCommandSupported());
            psLog.println("<br>Tag Locating Reporting: " + reader.ReaderCapabilities.isTagLocationingSupported());
            psLog.println("<br>Duty Cycle Values: ");
            
            System.out.println(" Tag Event Reporting: " + reader.ReaderCapabilities.isTagEventReportingSupported());
            System.out.println(" RSSI Filter: " + reader.ReaderCapabilities.isRSSIFilterSupported());
            System.out.println(" NXP Command Support: " + reader.ReaderCapabilities.isNXPCommandSupported());
            System.out.println(" Tag Locating Reporting: " + reader.ReaderCapabilities.isTagLocationingSupported());
            System.out.println(" Duty Cycle Values: ");
            
            short b[] = reader.ReaderCapabilities.getDutyCycleValues();
            for (int i = 0; i < b.length; i++) {
                psLog.print(b[i] + ",");
            }
            psLog.println("<br>Gen2 LLRP Capabilities");
            psLog.println("<br>BlockEraseSupport: " + reader.ReaderCapabilities.isBlockEraseSupported());
            psLog.println("<br>BlockWriteSupport: " + reader.ReaderCapabilities.isBlockWriteSupported());
            psLog.println("<br>BlockPermalockSupport: " + reader.ReaderCapabilities.isBlockPermalockSupported());
            psLog.println("<br>StateAwareSingulationCapable: " + reader.ReaderCapabilities.isTagInventoryStateAwareSingulationSupported());
            psLog.println("<br>NumOperationsInAccessSequence: " + reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence());
            psLog.println("<br>NumPreFilters: " + reader.ReaderCapabilities.getMaxNumPreFilters());
            psLog.println("<br>RecommisionSupport: " + reader.ReaderCapabilities.isRecommisionSupported());
            psLog.println("<br>WriteWMISupport: " + reader.ReaderCapabilities.isWriteUMISupported());
            psLog.println("<br>RadioPowerControlSupport: " + reader.ReaderCapabilities.isRadioPowerControlSupported());
            psLog.println("<br>RadioPowerControlSupport: " + reader.ReaderCapabilities.isTagInventoryStateAwareSingulationSupported());
            psLog.println("<br>IsNXPCommandSupport: " + reader.ReaderCapabilities.isNXPCommandSupported());
            
            System.out.println(" Gen2 LLRP Capabilities");
            System.out.println(" BlockEraseSupport: " + reader.ReaderCapabilities.isBlockEraseSupported());
            System.out.println(" BlockWriteSupport: " + reader.ReaderCapabilities.isBlockWriteSupported());
            System.out.println(" BlockPermalockSupport: " + reader.ReaderCapabilities.isBlockPermalockSupported());
            System.out.println(" StateAwareSingulationCapable: " + reader.ReaderCapabilities.isTagInventoryStateAwareSingulationSupported());
            System.out.println(" NumOperationsInAccessSequence: " + reader.ReaderCapabilities.getMaxNumOperationsInAccessSequence());
            System.out.println(" NumPreFilters: " + reader.ReaderCapabilities.getMaxNumPreFilters());
            System.out.println(" RecommisionSupport: " + reader.ReaderCapabilities.isRecommisionSupported());
            System.out.println(" WriteWMISupport: " + reader.ReaderCapabilities.isWriteUMISupported());
            System.out.println(" RadioPowerControlSupport: " + reader.ReaderCapabilities.isRadioPowerControlSupported());
            System.out.println(" RadioPowerControlSupport: " + reader.ReaderCapabilities.isTagInventoryStateAwareSingulationSupported());
            System.out.println(" IsNXPCommandSupport: " + reader.ReaderCapabilities.isNXPCommandSupported());
            
            // psLog.printlnln("IsNXPCommandSupport: "+reader.ReaderCapabilities.

            int c = reader.ReaderCapabilities.RFModes.Length();
            for (int index = 0; index < c; index++) {
                psLog.println("<br>RF Modes: " + reader.ReaderCapabilities.RFModes.getRFModeTableInfo(index));
                RFModeTable obRF = reader.ReaderCapabilities.RFModes.getRFModeTableInfo(index);
                for (int j = 0; j < obRF.length(); j++) {
                    psLog.println("<br>RFModeTable:" + j);

                    psLog.println("<br>ProtocolID" + obRF.getProtocolID());
                    psLog.println("<br>BdrValue:" + obRF.getRFModeTableEntryInfo(j).getBdrValue());
                    psLog.println("<br>DivideRatio:" + obRF.getRFModeTableEntryInfo(j).getDivideRatio());
                    psLog.println("<br>ForwardLinkModulationType:" + obRF.getRFModeTableEntryInfo(j).getForwardLinkModulationType());
                    psLog.println("<br>MaxTariValue:" + obRF.getRFModeTableEntryInfo(j).getMaxTariValue());
                    psLog.println("<br>MinTariValue:" + obRF.getRFModeTableEntryInfo(j).getMinTariValue());
                    psLog.println("<br>ModeIdentifer:" + obRF.getRFModeTableEntryInfo(j).getModeIdentifer());
                    psLog.println("<br>Modulation:" + obRF.getRFModeTableEntryInfo(j).getModulation());
                    psLog.println("<br>PieValue:" + obRF.getRFModeTableEntryInfo(j).getPieValue());
                    psLog.println("<br>SpectralMaskIndicator:" + obRF.getRFModeTableEntryInfo(j).getSpectralMaskIndicator());
                    psLog.println("<br>StepTariValue:" + obRF.getRFModeTableEntryInfo(j).getStepTariValue());
                    psLog.println("<br>EpcHAGTCConformance:" + obRF.getRFModeTableEntryInfo(j).isEpcHAGTCConformance());


                }
            }

            psLog.println("<br>Regulatory Capabilities: <br>");
            psLog.println("<br>Communication Standard: " + reader.ReaderCapabilities.getCommunicationStandard().toString());
            psLog.println("<br>Country Code: " + reader.ReaderCapabilities.getCountryCode());

            psLog.println("<br>UHF Band capabilities: <br>");
            psLog.println("<br>Transmit Power table : <br>");
            
            System.out.println(" Regulatory Capabilities: ");
            System.out.println(" Communication Standard: " + reader.ReaderCapabilities.getCommunicationStandard().toString());
            System.out.println(" Country Code: " + reader.ReaderCapabilities.getCountryCode());

            System.out.println(" UHF Band capabilities: ");
            System.out.println(" Transmit Power table : ");
            
            int arr[] = reader.ReaderCapabilities.getTransmitPowerLevelValues();

            int d = arr.length;
            for (int powindex = 0; powindex < d; powindex++) {
                psLog.println("<br>powerindex: " + powindex + " " + arr[powindex] + ",");
            }
            
            psLog.println("<br>HoppingEnabled: " + reader.ReaderCapabilities.isHoppingEnabled());
            if (reader.ReaderCapabilities.isHoppingEnabled()) {
                psLog.println("<br>HoppingEnabled Length: " + reader.ReaderCapabilities.FrequencyHopInfo.Length());
                for (int i = 0; i < reader.ReaderCapabilities.FrequencyHopInfo.Length(); i++) {
                    FrequencyHopTable HopTablesInfo = (FrequencyHopTable) reader.ReaderCapabilities.FrequencyHopInfo.getFrepuencyHopTablesInfo(i);
                    int hoptablelist[] = HopTablesInfo.getFrequencyHopValues();
                    for (int j = 0; j < hoptablelist.length; j++) {
                        psLog.print(hoptablelist[j] + ",");
                    }
                }
            } else {
                psLog.println("<br>Hopping is not Enabled:<br>");
                int fixFreq[] = reader.ReaderCapabilities.getFixedFreqValues();
                for (int i = 0; i < reader.ReaderCapabilities.getFixedFreqValues().length; i++) {
                    psLog.println("" + fixFreq[i] + ",");
                }
            }

            LogStatus(TestID, "ReaderCapabilities", true);
        } catch (Exception e) {
            AnalyseException(psLog, e);
            LogStatus(TestID, "ReaderCapabilities", false);
        }
        psSummary.println("");
        psSummary.println("\n\nJavaAPI:Reader Capabilities" + ":" + successCount + ":" + failureCount + ":0");
    }
}
