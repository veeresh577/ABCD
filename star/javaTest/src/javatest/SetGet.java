/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;

import com.mot.rfid.api3.*;
import java.io.*;
import static javatest.Commonclass.psLog;
import static javatest.Commonclass.psResult;
import static javatest.Commonclass.reader;

/**
 *
 * @author wjhc78
 */
public class SetGet extends Commonclass {

    ReaderManagement readerMgt;
    private PostFilter postfilter;
    public RFIDReader myReader = null;
    private TagPatternBase tpA;
    private TagPatternBase tpB;
    private byte[] tagMask = {(byte) 0xFF, (byte) 0xFF};
    private byte[] tagPattern1 = {0x11, 0x11};
    private byte[] tagPattern2 = {(byte) 0xFF, (byte) 0xFF};

    public SetGet() {
        TestNo = 1;
        SubNo = 0;
        successCount = 0;
        readerMgt = new ReaderManagement();
        myReader = reader;
    }

    public void setgetMethod() {
        int antennas = 1;
        short tagTransitTime = 2;

        try {
            mystreamLog = new FileOutputStream("JavaAPI_setget_Log.html");
            mystreamResult = new FileOutputStream("JavaAPI_setget_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psResult = new PrintStream(mystreamResult);
            AppendText();

        } catch (Exception e) {
            AnalyseException(psLog, e);
        }

        try {
            String tagId = "4321";
            byte writeData[] = {0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44};

            SYSTEMTIME ac = new SYSTEMTIME();
            psLog.println("<b>CurrentTime:</b>" + ac.GetCurrentTime());
            Antennas.Config Config = reader.Config.Antennas.getAntennaConfig(antennas);
            //RFIDReader
            psLog.println("<br><b>RFIDReader</b>");

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for Timeout ");
                psLog.println("<br><b>Expected:</b>getTimeout should be same as setTimeout");
                reader.setTimeout(1);
                psLog.println("<br><b>ActualResult</b>");
                psLog.println("<br>setTimeout:1");
                psLog.println("<br>getTimeout" + reader.getTimeout());
                LogStatus(TestID, "set get time out", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get time out", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for port ");
                psLog.println("<br><b>Expected:</b>getPort should be same as setPort");
                reader.setPort(5082);
                psLog.println("<br><b>ActualResult</b>");
                psLog.println("<br>setPort:5082");
                psLog.println("<br>getportNo:" + reader.getPort());
                LogStatus(TestID, "set get Port", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get Port", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for HostName");
                psLog.println("<br><b>Expected:</b>getHostName should be same as setHostName");
                reader.setHostName("FX9600EF941");
                psLog.println("<br><b>ActualResult</b>");
                psLog.println("<br>setHostName:257.235.206.142");
                psLog.println("<br>getHostName:" + reader.getHostName());
                LogStatus(TestID, "set get HostName", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get HostName", false);
            }

            //Reader.config.................


            psLog.println("<br><b>reader.config</b>");

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ReceiveSensitivityIndex ");
                psLog.println("<br><b>Expected:</b>getReceiveSensitivityIndex should be same as setReceiveSensitivityIndex");
                Config.setReceiveSensitivityIndex((short) 2);
                psLog.println("<br><b>Actual Result</b>");
                psLog.println("<br>SetReceiveSensitivityIndex: 2");
                psLog.println("<br>GetReceiveSensitivityIndex: " + Config.getReceiveSensitivityIndex());
                LogStatus(TestID, "set get ReceiveSensitivityIndex", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get ReceiveSensitivityIndex", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for TransmitFrequencyIndex ");
                psLog.println("<br><b>Expected:</b>getTransmitFrequencyIndex should be same as setTransmitFrequencyIndex");
                psLog.println("<br><b>Actual Result:</b>");
                Config.setTransmitFrequencyIndex((short) 2);
                psLog.println("<br>SetTransmitFrequencyIndex: 2");
                psLog.println("<br>GetTransmitFrequencyIndex: " + Config.getTransmitFrequencyIndex());
                LogStatus(TestID, "set get TransmitFrequencyIndex", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get TransmitFrequencyIndex", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for TransmitPowerIndex ");
                psLog.println("<br><b>Expected:</b>getTransmitPowerIndex should be same as setTransmitPowerIndex");
                psLog.println("<br><b>Actual Result:</b>");
                Config.setTransmitPowerIndex((short) 510);
                psLog.println("<br>SetTransmitPowerIndex: 510");
                psLog.println("<br>GetTransmitPowerIndex: " + Config.getTransmitPowerIndex());
                LogStatus(TestID, "set get TransmitPowerIndex", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get TransmitPowerIndex", false);
            }

            try {
                if (reader.ReaderCapabilities.getModelName().equals("3190")) {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for DutyCycleIndex ");
                    psLog.println("<br><b>Expected:</b>getDutyCycleIndex should be same as setDutyCycleIndex");
                    psLog.println("<br><b>ActualResult</b>");
                    reader.Config.setDutyCycleIndex(tagTransitTime);
                    psLog.println("<br>setDutyCycleIndex:2");
                    psLog.println("<br>getDutyCycleIndex:" + reader.Config.getDutyCycleIndex());
                    LogStatus(TestID, "set get DutyCycleIndex", true);
                }
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get DutyCycleIndex", false);
            }

        } catch (Exception e) {
            AnalyseException(psLog, e);
        }


        try {
            String tagId = "4321";
            byte writeData[] = {0x11, 0x11, 0x22, 0x22, 0x33, 0x33, 0x44, 0x44};

            //reader.Events.....

//          FormTestID(TestNo++, SubNo, "SetGet");
            psLog.println("<br><b>reader.Events</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AccessStartEvent ");
                psLog.println("<br><b>Expected:</b>getAccessStartEvent should be same as setAccessStartEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setAccessStartEvent(true);
                psLog.println("<br>setAccessStartEvent:true");
                psLog.println("<br>getAccessStartEvent:" + reader.Events.isAccessStartEventSet());
                LogStatus(TestID, "set get AccessStartEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get AccessStartEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AccessStopEvent ");
                psLog.println("<br><b>Expected:</b>getAccessStopEvent should be same as setAccessStopEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setAccessStopEvent(true);
                psLog.println("<br>setAccessStopEvent:true");
                psLog.println("<br>getAccessStopEvent:" + reader.Events.isAccessStopEventSet());
                LogStatus(TestID, "set get AccessStopEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get AccessStopEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AntennaEvent ");
                psLog.println("<br><b>Expected:</b>getAntennaEvent should be same as setAntennaEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setAntennaEvent(true);
                psLog.println("<br>setAntennaEvent:ture");
                psLog.println("<br>getAntennaEvent:" + reader.Events.isAntennaEventSet());
                LogStatus(TestID, "set get AntennaEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get AntennaEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AttachTagDataWithReadEvent ");
                psLog.println("<br><b>Expected:</b>getAttachTagDataWithReadEvent should be same as setAttachTagDataWithReadEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setAttachTagDataWithReadEvent(true);
                psLog.println("<br>setAttachTagDataWithReadEvent:ture");
                psLog.println("<br>getAttachTagDataWithReadEvent:" + reader.Events.isAttachTagDataWithReadEventSet());
                LogStatus(TestID, "set get AttachTagDataWithReadEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get AttachTagDataWithReadEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for BufferFullEvent ");
                psLog.println("<br><b>Expected:</b>getBufferFullEvent should be same as setBufferFullEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setBufferFullEvent(true);
                psLog.println("<br>setBufferFullEvent:ture");
                psLog.println("<br>getBufferFullEvent:" + reader.Events.isBufferFullEventSet());
                LogStatus(TestID, "set get BufferFullEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get BufferFullEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for BufferFullWarningEvent ");
                psLog.println("<br><b>Expected:</b>getBufferFullWarningEvent should be same as setBufferFullWarningEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setBufferFullWarningEvent(true);
                psLog.println("<br>setBufferFullWarningEvent:ture");
                psLog.println("<br>getBufferFullWarningEvent:" + reader.Events.isBufferFullWarningEventSet());
                LogStatus(TestID, "set get BufferFullWarningEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get BufferFullWarningEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for EASAlarmEvent ");
                psLog.println("<br><b>Expected:</b>getEASAlarmEvent should be same as setEASAlarmEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setEASAlarmEvent(true);
                psLog.println("<br>setEASAlarmEvent:ture");
                psLog.println("<br>getEASAlarmEvent:" + reader.Events.isEASAlarmEventSet());
                LogStatus(TestID, "set get  EASAlarmEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  EASAlarmEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for GPIEvent ");
                psLog.println("<br><b>Expected:</b>getGPIEvent should be same as setGPIEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setGPIEvent(true);
                psLog.println("<br>setGPIEvent:ture");
                psLog.println("<br>getGPIEvent:" + reader.Events.isGPIEventSet());
                LogStatus(TestID, "set get  GPIEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  GPIEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for HandheldEvent ");
                psLog.println("<br><b>Expected:</b>getHandheldEvent should be same as setHandheldEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setHandheldEvent(true);
                psLog.println("<br>setHandheldEvent:ture");
                psLog.println("<br>getHandheldEvent:" + reader.Events.isHandheldEventSet());
                LogStatus(TestID, "set get  HandheldEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  HandheldEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for InventoryStartEvent ");
                psLog.println("<br><b>Expected:</b>getInventoryStartEvent should be same as setInventoryStartEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setInventoryStartEvent(true);
                psLog.println("<br>setInventoryStartEvent:ture");
                psLog.println("<br>getInventoryStartEvent:" + reader.Events.isInventoryStartEventSet());
                LogStatus(TestID, "set get  InventoryStartEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  InventoryStartEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for InventoryStopEvent ");
                psLog.println("<br><b>Expected:</b>getInventoryStopEvent should be same as setInventoryStopEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setInventoryStopEvent(true);
                psLog.println("<br>setInventoryStopEvent:ture");
                psLog.println("<br>getInventoryStopEvent:" + reader.Events.isInventoryStopEventSet());
                LogStatus(TestID, "set get  InventoryStopEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  InventoryStopEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ReaderDisconnectEvent ");
                psLog.println("<br><b>Expected:</b>getReaderDisconnectEvent should be same as setReaderDisconnectEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setReaderDisconnectEvent(true);
                psLog.println("<br>setReaderDisconnectEvent:ture");
                psLog.println("<br>getReaderDisconnectEvent:" + reader.Events.isReaderDisconnectEventSet());
                LogStatus(TestID, "set get  ReaderDisconnectEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ReaderDisconnectEvent", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ReaderExceptionEvent ");
                psLog.println("<br><b>Expected:</b>getReaderExceptionEvent should be same as setReaderExceptionEvent");
                psLog.println("<br><b>ActualResult</b>");
                reader.Events.setReaderExceptionEvent(true);
                psLog.println("<br>setReaderExceptionEvent:ture");
                psLog.println("<br>getReaderExceptionEvent:" + reader.Events.isReaderExceptionEventSet());
                LogStatus(TestID, "set get  ReaderExceptionEvent", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ReaderExceptionEvent", false);
            }

            //try {
                //FormTestID(TestNo++, SubNo, "SetGet");
                //psLog.println("<br><b>Description:</b>set and get for TagReadEvent ");
                //psLog.println("<br><b>Expected:</b>getTagReadEvent should be same as setTagReadEvent");
                //psLog.println("<br><b>ActualResult</b>");
                //reader.Events.setTagReadEvent(true);
                //psLog.println("<br>setTagReadEvent:ture");
                //psLog.println("<br>getTagReadEvent:" + reader.Events.isTagReadEventSet());
                //LogStatus(TestID, "set get  TagReadEvent", true);
            //} catch (Exception e) {
                //AnalyseException(psLog, e);
                //LogStatus(TestID, "set get  TagReadEvent", false);
            //}

            //try {
                //FormTestID(TestNo++, SubNo, "SetGet");
                //psLog.println("<br><b>Description:</b>set and get for ReaderHandle ");
                //psLog.println("<br><b>Expected:</b>getReaderHandle should be same as setReaderHandle");
                //psLog.println("<br><b>ActualResult</b>");
                //reader.Events.setReaderHandle(antennas);
               // psLog.println("<br>setReaderHandle:1");
                //psLog.println("<br>getReaderHandle:" + reader.Events.getReaderHandle());
                //LogStatus(TestID, "set get  ReaderHandle", true);
            //} catch (Exception e) {
                ///AnalyseException(psLog, e);
                //LogStatus(TestID, "set get  ReaderHandle", false);
            //}


            //Antennas.RFMode
            try {
                psLog.println("<br><b>Antennas RFMode</b>");
                Antennas.RFMode rfmode = reader.Config.Antennas.getRFMode(antennas);
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for Tari value ");
                    psLog.println("<br><b>Expected:</b>getTari value should be same as setTari value");
                    psLog.println("<br><b>ActualResult</b>");
                    psLog.println("<br>setTari value: 1");
                    rfmode.setTari(1);
                    psLog.println("<br>get Tari value is:" + rfmode.getTari());
                    LogStatus(TestID, "set get  Tari value", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  Tari value", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for TableIndex: ");
                    psLog.println("<br><b>Expected:</b>getTableIndex should be same as setTableIndex:");
                    psLog.println("<br><b>ActualResult</b>");
                    psLog.println("<br>setTableIndex:15");
                    rfmode.setTableIndex(15);
                    psLog.println("<br>getTableIndex is:" + rfmode.getTableIndex());
                    LogStatus(TestID, "set get  TableIndex", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TableIndex", false);
                }
            } catch (Exception e) {
                psLog.println("<br>Invalid RF Mode:");
                AnalyseException(psLog, e);
            }


            // Antennas.SingulationControl
            try {
                psLog.println("<br><b>Antenns SingulationControl</b>");
                Antennas.SingulationControl sinulationcontrol = reader.Config.Antennas.getSingulationControl(antennas);

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for Session: ");
                    psLog.println("<br><b>Expected:</b>getSession should be same as setSession:");
                    psLog.println("<br><b>ActualResult</b>");
                    psLog.println("<br>setSession:S3");
                    sinulationcontrol.setSession(SESSION.SESSION_S3);
                    psLog.println("<br>getSession: " + sinulationcontrol.getSession());
                    LogStatus(TestID, "set get  Session", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  Session", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for TagPopulation: ");
                    psLog.println("<br><b>Expected:</b>getTagPopulation should be same as setTagPopulation:");
                    psLog.println("<br><b>ActualResult</b>");
                    sinulationcontrol.setTagPopulation((short) 50);
                    psLog.println("<br>setTagPopulation:50");
                    psLog.println("<br>getTagPopulation: " + sinulationcontrol.getTagPopulation());
                    LogStatus(TestID, "set get  TagPopulation", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPopulation", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for TagTransitTime: ");
                    psLog.println("<br><b>Expected:</b>getTagTransitTime should be same as setTagTransitTime:");
                    psLog.println("<br><b>ActualResult</b>");
                    sinulationcontrol.setTagTransitTime(tagTransitTime);
                    psLog.println("<br>setTagTransitTime:2");
                    psLog.println("<br>getTagTransitTime: " + sinulationcontrol.getTagTransitTime());
                    LogStatus(TestID, "set get  TagTransitTime", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagTransitTime", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for InventoryState: ");
                    psLog.println("<br><b>Expected:</b>getInventoryState should be same as setInventoryState:");
                    psLog.println("<br><b>ActualResult</b>");
                    sinulationcontrol.Action.setInventoryState(INVENTORY_STATE.INVENTORY_STATE_A);
                    psLog.println("<br>setInventoryState:INVENTORY_STATE_A");
                    psLog.println("<br>getInventoryState: " + sinulationcontrol.Action.getInventoryState());
                    LogStatus(TestID, "set get  InventoryState", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  InventoryState", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for PerformStateAwareSingulationAction: ");
                    psLog.println("<br><b>Expected:</b>getPerformStateAwareSingulationAction should be same as setPerformStateAwareSingulationAction:");
                    psLog.println("<br><b>ActualResult</b>");
                    sinulationcontrol.Action.setPerformStateAwareSingulationAction(true);
                    psLog.println("<br>setPerformStateAwareSingulationAction:true");
                    psLog.println("<br>getStateAwareSingulationAction: " + sinulationcontrol.Action.isPerformStateAwareSingulationActionSet());
                    LogStatus(TestID, "set get  PerformStateAwareSingulationAction", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  PerformStateAwareSingulationAction", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for SLFlag: ");
                    psLog.println("<br><b>Expected:</b>getSLFlag should be same as setSLFlag:");
                    psLog.println("<br><b>ActualResult</b>");
                    sinulationcontrol.Action.setSLFlag(SL_FLAG.SL_FLAG_DEASSERTED);
                    psLog.println("<br>setSLFlag:SL_FLAG_DEASSERTED");
                    psLog.println("<br>getSLFlag: " + sinulationcontrol.Action.getSLFlag());
                    LogStatus(TestID, "set get  SLFlag", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  SLFlag", false);
                }
            } catch (Exception e) {
                psLog.println("<br>Invalid Sinulationcontrol:");
                AnalyseException(psLog, e);
            }


            TagAccess tagaccess = new TagAccess();
            TagAccess.BlockEraseAccessParams eraseaccessparams = tagaccess.new BlockEraseAccessParams();
            TagAccess.WriteAccessParams writeParams = tagaccess.new WriteAccessParams();
            TagAccess.BlockPermalockAccessParams blockperma = tagaccess.new BlockPermalockAccessParams();
            TagAccess.KillAccessParams killparam = tagaccess.new KillAccessParams();
            TagAccess.LockAccessParams lockaccessparam = tagaccess.new LockAccessParams();
            TagAccess.ReadAccessParams readaccess = tagaccess.new ReadAccessParams();

            //TagAccess Block erase
            try {
                psLog.println("<br><b>TagAccess Block erase</b>");
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ByteCount: ");
                psLog.println("<br><b>Expected:</b>getByteCount should be same as setByteCount:");
                psLog.println("<br><b>ActualResult</b>");
                eraseaccessparams.setByteCount(2);
                psLog.println("<br>setByteCount:2");
                psLog.println("<br>getbyteCount :" + eraseaccessparams.getByteCount());
                LogStatus(TestID, "set get  ByteCount", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteCount", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ByteOffset: ");
                psLog.println("<br><b>Expected:</b>getByteOffset should be same as setByteOffset:");
                psLog.println("<br><b>ActualResult</b>");
                eraseaccessparams.setByteOffset(4);
                psLog.println("<br>setByteOffset:4");
                psLog.println("<br>getByteOffset :" + eraseaccessparams.getByteOffset());
                LogStatus(TestID, "set get  ByteOffset", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteOffset", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for MemoryBank: ");
                psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                psLog.println("<br><b>ActualResult</b>");
                eraseaccessparams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                psLog.println("<br>getMemoryBank :" + eraseaccessparams.getMemoryBank());
                LogStatus(TestID, "set get  MemoryBank", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  MemoryBank", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AccessPassword: ");
                psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                psLog.println("<br><b>ActualResult</b>");
                eraseaccessparams.setAccessPassword(1111);
                psLog.println("<br>setAccessPassward:1111");
                psLog.println("<br>getAccessPassword :" + eraseaccessparams.getAccessPassword());
                LogStatus(TestID, "set get  AccessPassword", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  AccessPassword", false);
            }

            //TagAccess Block write..
            psLog.println("<br><b>TagAccess Block write..</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ByteOffset: ");
                psLog.println("<br><b>Expected:</b>getByteOffset should be same as setByteOffset:");
                psLog.println("<br><b>ActualResult</b>");
                writeParams.setByteOffset(4);
                psLog.println("<br>setByteOffset:4");
                psLog.println("<br>getByteOffset :" + writeParams.getByteOffset());
                LogStatus(TestID, "set get  ByteOffset", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteOffset", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for WriteDataLength: ");
                psLog.println("<br><b>Expected:</b>getWriteDataLength should be same as setWriteDataLength:");
                psLog.println("<br><b>ActualResult</b>");
                writeParams.setWriteDataLength(2);
                psLog.println("<br>setWriteDataLength:2");
                psLog.println("<br>getWriteDataLength :" + writeParams.getWriteDataLength());
                LogStatus(TestID, "set get  WriteDataLength", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  WriteDataLength", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for MemoryBank: ");
                psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                psLog.println("<br><b>ActualResult</b>");
                writeParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                psLog.println("<br>getMemoryBank :" + writeParams.getMemoryBank());
                LogStatus(TestID, "set get  MemoryBank", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  MemoryBank", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for WriteData: ");
                psLog.println("<br><b>Expected:</b>getWriteData should be same as setWriteData:");
                psLog.println("<br><b>ActualResult</b>");
                writeParams.setWriteData(writeData);
                psLog.println("<br>setWriteData:1111222233334444");
                psLog.println("<br>getWriteData :" + byte2Hex(writeParams.getWriteData()));
                LogStatus(TestID, "set get  WriteData", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  WriteData", false);
            }

            //WriteSpecificFieldAccessParams
            psLog.println("<br><b>WtiteSpecificFieldAccessParams</b>");
            TagAccess.WriteSpecificFieldAccessParams wr = tagaccess.new WriteSpecificFieldAccessParams();
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AccessPassword: ");
                psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                psLog.println("<br><b>ActualResult</b>");
                wr.setAccessPassword(2222);
                psLog.println("<br>setAccessPassward:2222");
                psLog.println("<br>getAccessPassward" + wr.getAccessPassword());
                LogStatus(TestID, "set get  AccessPassward", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  AccessPassward", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for WriteData: ");
                psLog.println("<br><b>Expected:</b>getWriteData should be same as setWriteData:");
                psLog.println("<br><b>ActualResult</b>");
                wr.setWriteData(writeData);
                psLog.println("<br>setWrietData:1111222233334444");
                psLog.println("<br>getWriteData:" + byte2Hex(wr.getWriteData()));
                LogStatus(TestID, "set get  WriteData", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  WriteData", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for WriteDataLength: ");
                psLog.println("<br><b>Expected:</b>getWriteDataLength should be same as setWriteDataLength:");
                psLog.println("<br><b>ActualResult</b>");
                wr.setWriteDataLength(4);
                psLog.println("<br>setWriteDataLength:4");
                psLog.println("<br>getWriteDataLength:" + wr.getWriteDataLength());
                LogStatus(TestID, "set get  WriteDataLength", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  WriteDataLength", false);
            }


            //BlockPermalockAccessParams
            psLog.println("<br><b>BlockPermalockAccessParams</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for AccessPassword: ");
                psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setAccessPassword(22223333);
                psLog.println("<br>setAccessPassward:22223333");
                psLog.println("<br>getAccessPassward :" + blockperma.getAccessPassword());
                LogStatus(TestID, "set get  AccessPassward", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  AccessPassward", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ByteCount: ");
                psLog.println("<br><b>Expected:</b>getByteCount should be same as setByteCount:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setByteCount(2);
                psLog.println("<br>setByteCount:2");
                psLog.println("<br>getByteCount:" + blockperma.getByteCount());
                LogStatus(TestID, "set get  ByteCount", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteCount", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for ByteOffset: ");
                psLog.println("<br><b>Expected:</b>getByteOffset should be same as setByteOffset:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setByteOffset(4);
                psLog.println("<br>setByteOffset:4");
                psLog.println("<br>getByteOffset:" + blockperma.getByteOffset());
                LogStatus(TestID, "set get  ByteOffset", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteOffset", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:tagMask ");
                psLog.println("<br><b>Expected:</b>getMask should be same as setMask:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setMask(tagMask);
                psLog.println("<br>setMask:FFFF");
                psLog.println("<br>getMask :" + byte2Hex(blockperma.getMask()));
                LogStatus(TestID, "set get  Mask", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  Mask", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:MaskLength ");
                psLog.println("<br><b>Expected:</b>getMaskLength should be same as setMaskLength:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setMaskLength(2);
                psLog.println("<br>setMaskLength:2");
                psLog.println("<br>getMaskLength :" + blockperma.getMaskLength());
                LogStatus(TestID, "set get  MaskLength", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  MaskLength", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:MemoryBank ");
                psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                psLog.println("<br>getMemoryBank :" + blockperma.getMemoryBank());
                LogStatus(TestID, "set get  MemoryBank", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  MemoryBank", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:ReadLock ");
                psLog.println("<br><b>Expected:</b>getReadLock should be same as setReadLock:");
                psLog.println("<br><b>ActualResult</b>");
                blockperma.setReadLock(true);
                psLog.println("<br>setReadLock:true");
                psLog.println("<br>getReadLock :" + blockperma.getReadLock());
                LogStatus(TestID, "set get  ReadLock", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ReadLock", false);
            }

            //Kill AccessParams
            psLog.println("<br><b>Kill AccessParams</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:KillPassword");
                psLog.println("<br><b>Expected:</b>getKillPassword should be same as setKillPassword:");
                psLog.println("<br><b>ActualResult</b>");
                killparam.setKillPassword(1111);
                psLog.println("<br>setKillpassward:1111");
                psLog.println("<br>getKillPassward:" + killparam.getKillPassword());
                LogStatus(TestID, "set get  Killpassward", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  Killpassward", false);
            }

            //LockAccessParams
            psLog.println("<br><b>LockAccessParams</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:AccessPassword");
                psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                psLog.println("<br><b>ActualResult</b>");
                lockaccessparam.setAccessPassword(1111);
                psLog.println("<br>setAccesspassward:1111");
                psLog.println("<br>getAccessPassward :" + lockaccessparam.getAccessPassword());
                LogStatus(TestID, "set get  Accesspassward", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  Accesspassward", false);
            }


            lockaccessparam.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY, LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
            lockaccessparam.getLockPrivilege();
//          psLog.println(" "+lockaccessparam.getLockPrivilege());
//          LOCK_PRIVILEGE ap;
//          ap=LOCK_PRIVILEGE.LOCK_PRIVILEGE_PERMA_UNLOCK;
//          ap.LOCK_PRIVILEGE_PERMA_LOCK.getValue();
//          psLog.println("<br>setLockPrivilege:LOCK_PRIVILEGE_PERMA_UNLOCK");
//          psLog.println("<br>getLockPrivilege:"+ap);



            //ReadAccess Params
            psLog.println("<br><b>ReadAccess Params</b>");
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:AccessPassword");
                psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                psLog.println("<br><b>ActualResult</b>");
                readaccess.setAccessPassword(1111);
                psLog.println("<br>setAccesspassward:1111");
                psLog.println("<br>getAccessPassward:" + readaccess.getAccessPassword());
                LogStatus(TestID, "set get  Accesspassward", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  Accesspassward", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:ByteCount");
                psLog.println("<br><b>Expected:</b>getByteCount should be same as setByteCount:");
                psLog.println("<br><b>ActualResult</b>");
                readaccess.setByteCount(2);
                psLog.println("<br>setByteCount:2");
                psLog.println("<br>getByteCount:" + readaccess.getByteCount());
                LogStatus(TestID, "set get  ByteCount", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteCount", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:ByteOffset");
                psLog.println("<br><b>Expected:</b>getByteOffset should be same as setByteOffset:");
                psLog.println("<br><b>ActualResult</b>");
                readaccess.setByteOffset(2);
                psLog.println("<br>setByteOffset:2");
                psLog.println("<br>getByteOffset:" + readaccess.getByteOffset());
                LogStatus(TestID, "set get  ByteOffset", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ByteOffset", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:MemoryBank");
                psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                psLog.println("<br><b>ActualResult</b>");
                readaccess.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                psLog.println("<br>getMemoryBank:" + readaccess.getMemoryBank());
                LogStatus(TestID, "set get  MemoryBank", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  MemoryBank", false);
            }

            //NXP..................................................................
            psLog.println("<br><b>NXP</b>");
            try {
                NXP nxp = new NXP(tagaccess);
                NXP.SetEASParams setEASParams = nxp.new SetEASParams();
                NXP.ReadProtectParams readProtectParams = nxp.new ReadProtectParams();
                NXP.ResetReadProtectParams resetReadProtectParams = nxp.new ResetReadProtectParams();

                //EASParams...................
                psLog.println("<br><b>EASParams</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:AccessPassword");
                    psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                    psLog.println("<br><b>ActualResult</b>");
                    setEASParams.setAccessPassword(1111);
                    psLog.println("<br>setAccessPassward:1111");
                    psLog.println("<br>getAccessPassward:" + setEASParams.getAccessPassword());
                    LogStatus(TestID, "set get  AccessPassward", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  AccessPassward", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:setEAS");
                    psLog.println("<br><b>Expected:</b>getEAS should be same as setEAS:");
                    psLog.println("<br><b>ActualResult</b>");
                    setEASParams.setEAS(true);
                    psLog.println("<br>setEAS:ture");
                    psLog.println("<br>getEAS:" + setEASParams.isEASSet());
                    LogStatus(TestID, "set get  EAS", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  EAS", false);
                }

                //readProtectParams..............
                psLog.println("<br><br><b>readProtectParams</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:AccessPassword");
                    psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                    psLog.println("<br><b>ActualResult</b>");
                    readProtectParams.setAccessPassword(2222);
                    psLog.println("<br>setAccessPassward:2222");
                    psLog.println("<br>getAccessPassward:" + readProtectParams.getAccessPassword());
                    LogStatus(TestID, "set get  AccessPassward", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  AccessPassward", false);
                }


                //resetReadProtectParams........................
                psLog.println("<br><br><b>resetReadProtectParams</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:AccessPassword");
                    psLog.println("<br><b>Expected:</b>getAccessPassword should be same as setAccessPassword:");
                    psLog.println("<br><b>ActualResult</b>");
                    resetReadProtectParams.setAccessPassword(2222);
                    psLog.println("<br>setAccessPassward:2222");
                    psLog.println("<br>getAccessPassward:" + resetReadProtectParams.getAccessPassword());
                    LogStatus(TestID, "set get  AccessPassward", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  AccessPassward", false);
                }

//        psLog.println("<br>EASWait");
//        reader.Actions.TagAccess.NXP.SetEASWait(tagId, setEASParams, null);
//        psLog.println("<br>Set EAS done");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:EASEvent");
                    psLog.println("<br><b>Expected:</b>getEASEvent should be same as setEASEvent:");
                    psLog.println("<br><b>ActualResult</b>");
                    reader.Actions.TagAccess.NXP.setEASEvent(setEASParams, null, null);
                    psLog.println("<setEASEvent>");
                    psLog.println("<br>SetEASEvent done");
                    LogStatus(TestID, "set get  EASEvent", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  EASEvent", false);
                }
            } catch (Exception e) {
                AnalyseException(psLog, e);
            } // end of NXP


            //Prefilter...........................................................
            psLog.println("<br><b>Prefilter</b>");
            try {
                PreFilters prefilter = new PreFilters();
                PreFilters.PreFilter filter = prefilter.new PreFilter();

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:StateAwareAction");
                    psLog.println("<br><b>Expected:</b>getStateAwareAction should be same as setStateAwareAction:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.StateAwareAction.setStateAwareAction(STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A);
                    psLog.println("<br>setStateAwareAction:STATE_AWARE_ACTION_INV_A");
                    psLog.println("<br>getStateAwareAction" + filter.StateAwareAction.getStateAwareAction());
                    LogStatus(TestID, "set get  StateAwareAction", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  StateAwareAction", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:Target");
                    psLog.println("<br><b>Expected:</b>getTarget should be same as setTarget:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.StateAwareAction.setTarget(TARGET.TARGET_SL);
                    psLog.println("<br>setTarget:TARGET_SL");
                    psLog.println("<br>getTarget" + filter.StateAwareAction.getTarget());
                    LogStatus(TestID, "set get  Target", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  Target", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:StateUnawareAction");
                    psLog.println("<br><b>Expected:</b>getStateUnawareAction should be same as setStateUnawareAction:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.StateUnawareAction.setStateUnawareAction(STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT);
                    psLog.println("<br>setStateUnawareAction:STATE_UNAWARE_ACTION_SELECT");
                    psLog.println("<br>getStateUnawareAction" + filter.StateUnawareAction.getStateUnawareAction());
                    LogStatus(TestID, "set get  StateUnawareAction", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  StateUnawareAction", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:AntennaID");
                    psLog.println("<br><b>Expected:</b>getAntennaID should be same as setAntennaID:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setAntennaID(tagTransitTime);
                    psLog.println("<br>setAntennaID:2");
                    psLog.println("<br>getAntennaID:" + filter.getAntennaID());
                    LogStatus(TestID, "set get  AntennaID", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  AntennaID", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:BitOffset");
                    psLog.println("<br><b>Expected:</b>getBitOffset should be same as setBitOffset:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setBitOffset(2);
                    psLog.println("<br>setBitOffset:2");
                    psLog.println("<br>getBitOffset:" + filter.getBitOffset());
                    LogStatus(TestID, "set get  BitOffset", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  BitOffset", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:FilterAction");
                    psLog.println("<br><b>Expected:</b>getFilterAction should be same as setFilterAction:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setFilterAction(FILTER_ACTION.FILTER_ACTION_DEFAULT);
                    psLog.println("<br>setFilterAction:FILTER_ACTION_DEFAULT");
                    psLog.println("<br>getFilterAction" + filter.getFilterAction());
                    LogStatus(TestID, "set get  FilterAction", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  FilterAction", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MemoryBank");
                    psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                    psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                    psLog.println("<br>getMemoryBank" + filter.getMemoryBank());
                    LogStatus(TestID, "set get  MemoryBank", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MemoryBank", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPattern");
                    psLog.println("<br><b>Expected:</b>getTagPattern should be same as setTagPattern:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setTagPattern(writeData);
                    psLog.println("<br>setTagPattern:1111222233334444");
                    psLog.println("<br>getTagPattern:" + byte2Hex(filter.getTagPattern()));
                    LogStatus(TestID, "set get  TagPattern", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPattern", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPatternBitCount");
                    psLog.println("<br><b>Expected:</b>getTagPatternBitCount should be same as setTagPatternBitCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setTagPatternBitCount(4);
                    psLog.println("<br>setTagPatternBitCount:4");
                    psLog.println("<br>setTagPatternBitCount:" + filter.getTagPatternBitCount());
                    LogStatus(TestID, "set get  PatternBitCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  PatternBitCount", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TruncateAction");
                    psLog.println("<br><b>Expected:</b>getTruncateAction should be same as setTruncateAction:");
                    psLog.println("<br><b>ActualResult</b>");
                    filter.setTruncateAction(TRUNCATE_ACTION.TRUNCATE_ACTION_TRUNCATE);
                    psLog.println("<br>setTruncateAction:TRUNCATE_ACTION_TRUNCATE");
                    psLog.println("<br>getTruncateAction:" + filter.getTruncateAction());
                    LogStatus(TestID, "set get  TruncateAction", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TruncateAction", false);
                }
            } catch (Exception e) {
                AnalyseException(psLog, e);
            } // end of pre filter
//          reader.Actions.PreFilters.add(filter);
//          psLog.println("<br>setPreFilters");
//          psLog.println("<br>getPreFilters"+reader.Actions.PreFilters.getPreFilter(antennas));


            //PostFilter.....................................................
            psLog.println("<br><b>PostFilter</b>");
            try {
                postfilter = new PostFilter();
                byte[] tag1 = {2, 2, 12, 24, 8, 64};
                byte[] tag2 = {2, 2, 12, 24, 8, 64};
                tpA = new TagPatternBase();
                tpB = new TagPatternBase();
                postfilter = new PostFilter();

                //tag Pattern A
                psLog.println("<br><b>tag Pattern A:</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:BitOffset");
                    psLog.println("<br><b>Expected:</b>getBitOffset should be same as setBitOffset:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setBitOffset(32);
                    psLog.println("<br>setBitOffset:32");
                    psLog.println("<br>getBitOffset:" + tpA.getBitOffset());
                    LogStatus(TestID, "set get  BitOffset", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  BitOffset", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MemoryBank");
                    psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                    psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                    psLog.println("<br>getMemoryBank" + tpA.getMemoryBank());
                    LogStatus(TestID, "set get  MemoryBank", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MemoryBank", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagMask");
                    psLog.println("<br><b>Expected:</b>getTagMask should be same as setTagMask:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setTagMask(tagMask);
                    psLog.println("<br>setTagMask:FFFF");
                    psLog.println("<br>getTagMask" + byte2Hex(tpA.getTagMask()));
                    LogStatus(TestID, "set get  TagMask", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagMask", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagMaskBitCount");
                    psLog.println("<br><b>Expected:</b>getTagMaskBitCount should be same as setTagMaskBitCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setTagMaskBitCount(16);
                    psLog.println("<br>setTagMaskBitCount:16");
                    psLog.println("<br>getTagMaskBitCount:" + tpA.getTagMaskBitCount());
                    LogStatus(TestID, "set get  TagMaskBitCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagMaskBitCount", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPattern");
                    psLog.println("<br><b>Expected:</b>getTagPattern should be same as setTagPattern:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setTagPattern(tagPattern1);
                    psLog.println("<br>setTagPattern:1111");
                    psLog.println("<br>getTagPattern:" + byte2Hex(tpA.getTagPattern()));
                    LogStatus(TestID, "set get  TagPattern", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPattern", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPatternBitCount");
                    psLog.println("<br><b>Expected:</b>getTagPatternBitCount should be same as setTagPatternBitCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpA.setTagPatternBitCount(16);
                    psLog.println("<br>setTagPatternBitCount:16");
                    psLog.println("<br>getTagPatternBitCount:" + tpA.getTagPatternBitCount());
                    LogStatus(TestID, "set get  TagPatternBitCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPatternBitCount", false);
                }

                //tag pattern B
                psLog.println("<br><br><b>tag pattern B</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:BitOffset");
                    psLog.println("<br><b>Expected:</b>getBitOffset should be same as setBitOffset:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setBitOffset(32);
                    psLog.println("<br>setBitOffset:32");
                    psLog.println("<br>getBitOffset:" + tpB.getBitOffset());
                    LogStatus(TestID, "set get  BitOffset", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  BitOffset", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MemoryBank");
                    psLog.println("<br><b>Expected:</b>getMemoryBank should be same as setMemoryBank:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
                    psLog.println("<br>setMemoryBank:MEMORY_BANK_USER");
                    psLog.println("<br>getMemoryBank" + tpB.getMemoryBank());
                    LogStatus(TestID, "set get  MemoryBank", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MemoryBank", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagMask");
                    psLog.println("<br><b>Expected:</b>getTagMask should be same as setTagMask:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setTagMask(tagMask);
                    psLog.println("<br>setTagMask:FFFF");
                    psLog.println("<br>getTagMask" + byte2Hex(tpB.getTagMask()));
                    LogStatus(TestID, "set get  TagMask", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagMask", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagMaskBitCount");
                    psLog.println("<br><b>Expected:</b>getTagMaskBitCount should be same as setTagMaskBitCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setTagMaskBitCount(16);
                    psLog.println("<br>setTagMaskBitCount:16");
                    psLog.println("<br>getTagMaskBitCount:" + tpB.getTagMaskBitCount());
                    LogStatus(TestID, "set get  TagMaskBitCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagMaskBitCount", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPattern");
                    psLog.println("<br><b>Expected:</b>getTagPattern should be same as setTagPattern:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setTagPattern(tagPattern2);
                    psLog.println("<br>setTagPattern:FFFF");
                    psLog.println("<br>getTagPattern:" + byte2Hex(tpB.getTagPattern()));
                    LogStatus(TestID, "set get  TagPattern", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPattern", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagPatternBitCount");
                    psLog.println("<br><b>Expected:</b>getTagPatternBitCount should be same as setTagPatternBitCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    tpB.setTagPatternBitCount(16);
                    psLog.println("<br>setTagPatternBitCount:16");
                    psLog.println("<br>getTagPatternBitCount:" + tpB.getTagPatternBitCount());
                    LogStatus(TestID, "set get  TagPatternBitCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagPatternBitCount", false);
                }

                //Post Filter_RssiRangeFilter
                psLog.println("<br><br><b>Post Filter_RSSIRangeFilter</b>");
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MatchRange");
                    psLog.println("<br><b>Expected:</b>getMatchRange should be same as setMatchRange:");
                    psLog.println("<br><b>ActualResult</b>");
                    postfilter.RssiRangeFilter.setMatchRange(MATCH_RANGE.WITHIN_RANGE);
                    psLog.println("<br>setMatchRange:WITHIN_RANGE");
                    psLog.println("<br>getMatchRange:" + postfilter.RssiRangeFilter.getMatchRange());
                    LogStatus(TestID, "set get  MatchRange", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MatchRange", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:PeakRSSILowerLimit");
                    psLog.println("<br><b>Expected:</b>getPeakRSSILowerLimit should be same as setPeakRSSILowerLimit:");
                    psLog.println("<br><b>ActualResult</b>");
                    postfilter.RssiRangeFilter.setPeakRSSILowerLimit(tagTransitTime);
                    psLog.println("<br>setPeakRSSILowerLimit:2");
                    psLog.println("<br>getPeakRssiLowerLimit:" + postfilter.RssiRangeFilter.getPeakRSSILowerLimit());
                    LogStatus(TestID, "set get  PeakRssiLowerLimit", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  PeakRssiLowerLimit", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:PeakRSSIUpperLimit");
                    psLog.println("<br><b>Expected:</b>getPeakRSSIUpperLimit should be same as setPeakRSSIUpperLimit:");
                    psLog.println("<br><b>ActualResult</b>");
                    postfilter.RssiRangeFilter.setPeakRSSIUpperLimit(tagTransitTime);
                    psLog.println("<br>setPeakRSSIUpperLimit:2");
                    psLog.println("<br>getPeakRssiUpperLimit:" + postfilter.RssiRangeFilter.getPeakRSSIUpperLimit());
                    LogStatus(TestID, "set get  PeakRssiUpperLimit", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  PeakRssiUpperLimit", false);
                }

                //setAccessFilterMatchPattern for PostFilter......
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:PostFilterMatchPattern");
                    psLog.println("<br><b>Expected:</b>getPostFilterMatchPattern should be same as setPostFilterMatchPattern:");
                    psLog.println("<br><b>ActualResult</b>");
                    postfilter.setPostFilterMatchPattern(FILTER_MATCH_PATTERN.A_AND_B);
                    psLog.println("<br>setPostFilterMatchPattern:FILTER_MATCH_PATTERN.A_AND_B");
                    psLog.println("<br>getPostFilterMatchPattern:" + postfilter.getPostFilterMatchPattern());
                    LogStatus(TestID, "set get  PostFilterMatchPattern", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  PostFilterMatchPattern", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:RSSIRangeFilter");
                    psLog.println("<br><b>Expected:</b>getRSSIRangeFilter should be same as setRSSIRangeFilter:");
                    psLog.println("<br><b>ActualResult</b>");
                    postfilter.setRSSIRangeFilter(true);
                    psLog.println("<br>setRSSIRangeFilter:true");
                    psLog.println("<br>getRSSIRangeFilter:" + postfilter.isRSSIRangeFilterUsed());
                    LogStatus(TestID, "set get  RSSIRangeFilter", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  RSSIRangeFilter", false);
                }
            } catch (Exception e) {
                AnalyseException(psLog, e);
            } // end of post filter


            //TagStorage settings....
            psLog.println("<br><br><b>TagStorage settings</b>");
            try {
                TagStorageSettings aa = new TagStorageSettings();
                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MaxMemoryBankByteCount");
                    psLog.println("<br><b>Expected:</b>getMaxMemoryBankByteCount should be same as setMaxMemoryBankByteCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    aa.setMaxMemoryBankByteCount(2);
                    psLog.println("<br>setMaxMemoryBankByteCount:2");
                    psLog.println("<br>getMaxMemoryBankByteCount:" + aa.getMaxMemoryBankByteCount());
                    LogStatus(TestID, "set get  MaxMemoryBankByteCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MaxMemoryBankByteCount", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MaxTagCount");
                    psLog.println("<br><b>Expected:</b>getMaxTagCount should be same as setMaxTagCount:");
                    psLog.println("<br><b>ActualResult</b>");
                    aa.setMaxTagCount(100);
                    psLog.println("<br>setMaxTagCount:100");
                    psLog.println("<br>getMaxTagCount:" + aa.getMaxTagCount());
                    LogStatus(TestID, "set get  MaxTagCount", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MaxTagCount", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:MaxTagIDLength");
                    psLog.println("<br><b>Expected:</b>getMaxTagIDLength should be same as setMaxTagIDLength:");
                    psLog.println("<br><b>ActualResult</b>");
                    aa.setMaxTagIDLength(4);
                    psLog.println("<br>setMaxTagIDLength:4");
                    psLog.println("<br>getMaxTagIDLength:" + aa.getMaxTagIDLength());
                    LogStatus(TestID, "set get  MaxTagIDLength", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  MaxTagIDLength", false);
                }

                try {
                    FormTestID(TestNo++, SubNo, "SetGet");
                    psLog.println("<br><b>Description:</b>set and get for:TagFields");
                    psLog.println("<br><b>Expected:</b>getTagFields should be same as setTagFields:");
                    psLog.println("<br><b>ActualResult</b>");
                    aa.setTagFields(TAG_FIELD.PC);
                    psLog.println("<br>setTagFields:PC");
                    TAG_FIELD tf[] = aa.getTagFields();
                    LogStatus(TestID, "set get  TagFields", true);
                } catch (Exception e) {
                    AnalyseException(psLog, e);
                    LogStatus(TestID, "set get  TagFields", false);
                }
            } catch (Exception e) {
                AnalyseException(psLog, e);
            } // End TagStorageSettings


            //ReaderManagement.................................................
            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>ReaderMangagement</b>");
                psLog.println("<br><b>Description:</b>set and get for:AntennaMode");
                psLog.println("<br><b>Expected:</b>getAntennaMode should be same as setAntennaMode:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setAntennaMode(ANTENNA_MODE.ANTENNA_MODE_BISTATIC);
                psLog.println("<br>setAntennaMode:ANTENNA_MODE_BISTATIC");
                psLog.println("<br>getAntennaMode:" + readerMgt.getAntennaMode());
                LogStatus(TestID, "set get  AntennaMode", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  AntennaMode", true);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:GPIDebounceTimeMilliseconds");
                psLog.println("<br><b>Expected:</b>getGPIDebounceTimeMilliseconds should be same as setGPIDebounceTimeMilliseconds:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setGPIDebounceTimeMilliseconds(successCount);
                psLog.println("<br>setGPIDebounceTimeMilliseconds:");
                psLog.println("<br>getGPIDebounceTimeMilliseconds:" + readerMgt.getGPIDebounceTimeMilliseconds());
                LogStatus(TestID, "set get  GPIDebounceTimeMilliseconds", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  GPIDebounceTimeMilliseconds", true);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:ReaderInfo");
                psLog.println("<br><b>Expected:</b>getReaderInfo should be same as setReaderInfo:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setReaderInfo(null);
                psLog.println("<br>setReaderInfo:");
                psLog.println("<br>getReaderInfo:" + readerMgt.getReaderInfo());
                LogStatus(TestID, "set get  ReaderInfo", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  ReaderInfo", true);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:TraceLevel");
                psLog.println("<br><b>Expected:</b>getTraceLevel should be same as setTraceLevel:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setTraceLevel(TRACE_LEVEL.TRACE_LEVEL_OFF);
                psLog.println("<br>setTraceLevel:TRACE_LEVEL_OFF");
                psLog.println("<br>getTraceLevel:" + readerMgt.getTraceLevel());
                LogStatus(TestID, "set get  TraceLevel", true);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  TraceLevel", false);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:USBOperationMode");
                psLog.println("<br><b>Expected:</b>getUSBOperationMode should be same as setUSBOperationMode:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setUSBOperationMode(null);
                psLog.println("<br>setUSBOperationMode:");
                psLog.println("<br>getUSBOperationMode" + readerMgt.getUSBOperationMode());
                LogStatus(TestID, "set get  USBOperationMode", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  USBOperationMode", true);

            }


            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:UserLED");
                psLog.println("<br><b>Expected:</b>getUserLED should be same as setUserLED:");
                psLog.println("<br><b>ActualResult</b>");
                readerMgt.setUserLED(null);
                psLog.println("<br>setUserLED:");
                LogStatus(TestID, "set get  UserLED", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  UserLED", true);
            }

            try {
                FormTestID(TestNo++, SubNo, "SetGet");
                psLog.println("<br><b>Description:</b>set and get for:LocalTime");
                psLog.println("<br><b>Expected:</b>getLocalTime should be same as setLocalTime:");
                psLog.println("<br><b>ActualResult</b>");
                psLog.println("<br>setLocalTime:");
                readerMgt.setLocalTime(null);
                psLog.println("<br>getLocalTime" + readerMgt.getLocalTime());
                LogStatus(TestID, "set get  LocalTime", false);
            } catch (Exception e) {
                AnalyseException(psLog, e);
                LogStatus(TestID, "set get  LocalTime", true);
            }

        } catch (Exception e) {
            AnalyseException(psLog, e);
        } // end
        psSummary.println("JavaAPI:SetGet_Testcases" + ":" + successCount + ":" + failureCount + ":0");
        close();
//        psLog.close();
//        psResult.close();


    }
}
