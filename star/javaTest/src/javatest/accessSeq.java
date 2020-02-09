/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javatest;
import com.mot.rfid.api3.*;
import java.io.*;
import java.util.Date;
import static javatest.Commonclass.antInfo;
/**
 *
 * @author QMTN48
 */
class accessListener implements RfidEventsListener
{
    private PrintStream psLog;
    ManualResetEvent accessStop;
    public accessListener( PrintStream fileStream,ManualResetEvent event)
    {
         psLog = fileStream;
         accessStop = event;
    }
    
    public void eventReadNotify(RfidReadEvents rfidReadEvents)
    {
//        psLog.println("<br>\n READ EVENT Happened");
//        if(rfidReadEvents != null)
//        {
//            TagData tagData  = rfidReadEvents.getReadEventData().tagData;
//            psLog.println("<br>\n tags ID :  "+tagData.getTagID()+" membank  :"+tagData.getMemoryBank()+" memBank Data:  "+tagData.getMemoryBankData()+" opCode  :"+tagData.getOpCode()
//                    +" OpCode Status  :"+tagData.getOpStatus());
//        }
    }

    public void eventStatusNotify(RfidStatusEvents rfidStatusEvents)
    {
        psLog.println("\n Event  :"+rfidStatusEvents.StatusEventData.getStatusEventType()+"<br> \r\n");
        if( STATUS_EVENT_TYPE.ACCESS_STOP_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            accessStop.set();
        }
        else if( STATUS_EVENT_TYPE.NXP_EAS_ALARM_EVENT == rfidStatusEvents.StatusEventData.getStatusEventType())
        {
            System.out.println( "alarmCode "+rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode());
            psLog.println( "alarmCode "+rfidStatusEvents.StatusEventData.NXPEASAlarmEventData.getEASAlarmCode());
            accessStop.set();
        }
    }
}

public class accessSeq extends Commonclass
{
    private RFIDReader reader;
    private TagData[] tagData;
    private PreFilters preFilters;
    private byte[] tagMask = { (byte)0xFF, (byte)0xFF };
    private byte[] tagPattern = { 0x30, 0x00 };
    private short[] antennaList  = { 1,2 };
    private OPERATION_QUALIFIER antOPQ[] = { OPERATION_QUALIFIER.C1G2_OPERATION, OPERATION_QUALIFIER.C1G2_OPERATION};
    private Antennas antenna;
    private Antennas.SingulationControl singControl;
    private AccessFilter accessfilter;
    private accessListener myaccesslistener;
    private ManualResetEvent accessComplete;
    private TagAccess.WriteAccessParams wParams;
    private TagAccess.ReadAccessParams rParams;
    private TagAccess tagAccess;
    TagAccess.Sequence sequance;
    private TagAccess.Sequence.Operation op1;
    private TagAccess.Sequence.Operation op2;
    private TagAccess.Sequence.Operation op3;
    private TagAccess.Sequence.Operation op4;
    private TagAccess.Sequence.Operation op5;
    private TriggerInfo tInfo;
    private NXP myNXP;
    private NXP.ResetReadProtectParams resetReadProtect;
    
    public void LogSuccessFailureCount()
    {
        psSummary.println("JavaAPI:Test Access Sequences Read:Write:BlockWrite:Erase:" + successCount + ":" + failureCount + ":" + "0");
    }
    public accessSeq( RFIDReader myreader)
    {
        reader = myreader;
        tagAccess = new TagAccess( );
        preFilters = new PreFilters( );
        accessfilter = new AccessFilter();
        tInfo = new TriggerInfo();
        antenna = reader.Config.Antennas;
        wParams = tagAccess.new WriteAccessParams( );
        rParams = tagAccess.new ReadAccessParams( );
        sequance = tagAccess.new Sequence(tagAccess);
        accessComplete = new ManualResetEvent(false);
        myNXP = new NXP(tagAccess);
        resetReadProtect = myNXP.new ResetReadProtectParams();
        successCount = 0;
        failureCount = 0;
        

        tInfo.StartTrigger.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
        tInfo.StopTrigger.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_N_ATTEMPTS_WITH_TIMEOUT);
        tInfo.StopTrigger.NumAttempts.setN((short)5);
        tInfo.StopTrigger.NumAttempts.setTimeout(15000);
    }
   
    private void setWParams2MemBank(long ap,int byteOffset,MEMORY_BANK mBank,byte[] writeData,int writeDataLength)
    {
        wParams.setAccessPassword(ap);
        wParams.setByteOffset(byteOffset);
        wParams.setMemoryBank(mBank);
        wParams.setWriteData(writeData);
        wParams.setWriteDataLength(writeDataLength);
    }
    
    private void setRParams2MemBank(long ap,int byteOffset,MEMORY_BANK mBank,int byteCount)
    {
        rParams.setAccessPassword(ap);
        rParams.setByteOffset(byteOffset);
        rParams.setMemoryBank(mBank);
        rParams.setByteCount(byteCount);
    }

    private void addOpSequenceRead(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,MEMORY_BANK mBank,int m_byteCount,int m_byteOffset,long ap )
    {
        //operation sequence 1
        
        op.setAccessOperationCode (opCode);
        op.ReadAccessParams.setMemoryBank (mBank);
        op.ReadAccessParams.setByteCount (m_byteCount);
        op.ReadAccessParams.setByteOffset(m_byteOffset);
        op.ReadAccessParams.setAccessPassword(ap);// perform access sequence
        // do a get of the parameters.
        //
        op.ReadAccessParams.getAccessPassword();
        op.ReadAccessParams.getByteCount();
        op.ReadAccessParams.getByteOffset();;
        op.ReadAccessParams.getMemoryBank();
        
        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }

    private void addOpSequenceWrite(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,MEMORY_BANK mBank,int writeLen,byte[] writeData,int m_byteOffset,long ap )
    {
        //operation sequence 
        
        op.setAccessOperationCode (opCode);
        op.WriteAccessParams.setMemoryBank (mBank);
        op.WriteAccessParams.setByteOffset(m_byteOffset);
        op.WriteAccessParams.setWriteDataLength(writeLen);
        op.WriteAccessParams.setWriteData(writeData);
        op.WriteAccessParams.setAccessPassword(ap);// perform access sequence
        
        // do a get of the params
        op.WriteAccessParams.getAccessPassword();
        op.WriteAccessParams.getByteOffset();
        op.WriteAccessParams.getMemoryBank();
        op.WriteAccessParams.getWriteData();
        op.WriteAccessParams.getWriteDataLength();
        op.getAccessOperationCode();
        op.getOperationIndex();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceBlockWrite(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,MEMORY_BANK mBank,int writeLen,byte[] writeData,int m_byteOffset,long ap )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.BlockWriteAccessParams.setMemoryBank (mBank);
        op.BlockWriteAccessParams.setByteOffset(m_byteOffset);
        op.BlockWriteAccessParams.setWriteDataLength(writeLen);
        op.BlockWriteAccessParams.setWriteData(writeData);
        op.BlockWriteAccessParams.setAccessPassword(ap);// perform access sequence

        // do a get of the params
        op.BlockWriteAccessParams.getAccessPassword();
        op.BlockWriteAccessParams.getByteOffset();
        op.BlockWriteAccessParams.getMemoryBank();
        op.BlockWriteAccessParams.getWriteData();
        op.BlockWriteAccessParams.getWriteDataLength();
        
        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
            
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    
    private void addOpSequenceBlockErase(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,MEMORY_BANK mBank,int m_byteCount,int m_byteOffset,long ap )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.BlockEraseAccessParams.setMemoryBank (mBank);
        op.BlockEraseAccessParams.setByteOffset(m_byteOffset);
        op.BlockEraseAccessParams.setByteCount(m_byteCount);
        op.BlockEraseAccessParams.setAccessPassword(ap);// perform access sequence


        op.BlockEraseAccessParams.getAccessPassword();
        op.BlockEraseAccessParams.getByteCount();
        op.BlockEraseAccessParams.getByteOffset();
        op.BlockEraseAccessParams.getMemoryBank();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceLock(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.LockAccessParams.setAccessPassword(0x12345678);
     
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_ACCESS_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_TID_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_EPC_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_READ_WRITE);

        op.LockAccessParams.getAccessPasswordMemoryIndex();
        op.LockAccessParams.getAccessPassword();
        op.LockAccessParams.getEPCMemoryIndex();
        op.LockAccessParams.getKillPasswordMemoryIndex();
        op.LockAccessParams.getLockPrivilege();
        op.LockAccessParams.getTIDMemoryIndex();
        op.LockAccessParams.getUserMemoryIndex();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceLock2(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        //operation sequence

        op.setAccessOperationCode (opCode);
        op.LockAccessParams.setAccessPassword(0x12345678);

//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_ACCESS_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_TID_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_EPC_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_USER_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);

        op.LockAccessParams.getAccessPasswordMemoryIndex();
        op.LockAccessParams.getAccessPassword();
        op.LockAccessParams.getEPCMemoryIndex();
        op.LockAccessParams.getKillPasswordMemoryIndex();
        op.LockAccessParams.getLockPrivilege();
        op.LockAccessParams.getTIDMemoryIndex();
        op.LockAccessParams.getUserMemoryIndex();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceLock3(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        //operation sequence

        op.setAccessOperationCode (opCode);
        op.LockAccessParams.setAccessPassword(0x12345678);

//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_ACCESS_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_TID_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_EPC_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_READ_WRITE);

        op.LockAccessParams.getAccessPasswordMemoryIndex();
        op.LockAccessParams.getAccessPassword();
        op.LockAccessParams.getEPCMemoryIndex();
        op.LockAccessParams.getKillPasswordMemoryIndex();
        op.LockAccessParams.getLockPrivilege();
        op.LockAccessParams.getTIDMemoryIndex();
        op.LockAccessParams.getUserMemoryIndex();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceLock4(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        //operation sequence

        op.setAccessOperationCode (opCode);
        op.LockAccessParams.setAccessPassword(0x12345678);

//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_ACCESS_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_TID_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE);
//        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_EPC_MEMORY,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);
        op.LockAccessParams.setLockPrivilege(LOCK_DATA_FIELD.LOCK_KILL_PASSWORD,LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK);

        op.LockAccessParams.getAccessPasswordMemoryIndex();
        op.LockAccessParams.getAccessPassword();
        op.LockAccessParams.getEPCMemoryIndex();
        op.LockAccessParams.getKillPasswordMemoryIndex();
        op.LockAccessParams.getLockPrivilege();
        op.LockAccessParams.getTIDMemoryIndex();
        op.LockAccessParams.getUserMemoryIndex();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    
    private boolean addOpSequenceKill(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.getAccessOperationCode();
        op.getOperationIndex();
        op.KillAccessParams.setKillPassword(0x33333333);
        op.KillAccessParams.getKillPassword();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
            return true;
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
           return false;
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
            return false;
        }
    }
    
    private void addOpSequenceBP(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode )
    {
        byte mask[] = {(byte)0x40,0x00};
        //operation sequence
        op.setAccessOperationCode (opCode);
        op.BlockPermaLockAccessParams.setAccessPassword(0x12345678);
        psLog.println("<br>\n getAccessPassword"+op.BlockPermaLockAccessParams.getAccessPassword());
        op.BlockPermaLockAccessParams.setByteCount(2);
        psLog.println("<br>\n getByteCount "+op.BlockPermaLockAccessParams.getByteCount());
        op.BlockPermaLockAccessParams.setByteOffset(0);
        psLog.println("<br>\n getByteOffset "+op.BlockPermaLockAccessParams.getByteOffset());
        op.BlockPermaLockAccessParams.setMask(mask);
        psLog.println("<br>\n getMask "+op.BlockPermaLockAccessParams.getMask());
        op.BlockPermaLockAccessParams.setMaskLength(2);
        psLog.println("<br>\n getMaskLength "+op.BlockPermaLockAccessParams.getMaskLength());
        op.BlockPermaLockAccessParams.setMemoryBank(MEMORY_BANK.MEMORY_BANK_USER);
        psLog.println("<br>\n getMemoryBank "+op.BlockPermaLockAccessParams.getMemoryBank());
        op.BlockPermaLockAccessParams.setReadLock(false);
        psLog.println("<br>\n getReadLock "+op.BlockPermaLockAccessParams.getReadLock());
        op.BlockPermaLockAccessParams.getReadLock();
        

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }

    private void addOpSequenceReadProtect(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,long ap )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.ResetReadProtectParams.setAccessPassword(ap);
        op.ResetReadProtectParams.getAccessPassword();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceResetReadProtect(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,long ap )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.ResetReadProtectParams.setAccessPassword(ap);
        op.ResetReadProtectParams.getAccessPassword();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    private void addOpSequenceSetEAS(TagAccess.Sequence.Operation op,ACCESS_OPERATION_CODE opCode,long ap,boolean setEAS )
    {
        //operation sequence
        
        op.setAccessOperationCode (opCode);
        op.SetEASParams.setAccessPassword(ap);
        op.SetEASParams.setEAS(setEAS);
        op.SetEASParams.getAccessPassword();
        op.SetEASParams.isEASSet();

        try
        {
            reader.Actions.TagAccess.OperationSequence.add(op);
        }
        catch(InvalidUsageException exp)
        {
           psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
    }
    
    private boolean ReadMemory( )
    {
        boolean isAccessSuccess = false;
        TagData[] tags = reader.Actions.getReadTags(1000);
        if( tags != null )
        {
            for(int tagCount = 0; tagCount < tags.length;tagCount++)
            {
                //System.out.println(tags[tagCount].getOpStatus());
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

    void testOpSeqBlockErase()
    {
        init();
        try
        {
            byte[] writeData = new byte[64];
            bytefill(writeData,(byte)0xBB);

            // Reader Reserved memory bank with accessSequences
            //
            int offset, length;
            offset = 2;
            reader.Actions.purgeTags();
            for(length =4;length <=8; length+=2 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceBlockErase(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                addOpSequenceRead(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE);
                psLog.println("\n<b> Expected: Erase memory Bank"+MEMORY_BANK.MEMORY_BANK_RESERVED+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, null);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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

            // write to EPC
            offset =6;
            for(length =6;length <=6; length+=2 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceBlockErase(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_EPC,length ,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_EPC,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_EPC+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, null);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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

            //write to user
            offset =0;
            for(length = 2;length <=2; length+=8 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                //addOpSequenceBlockErase(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_USER+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, null);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                //reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }

    }

    void testOpSeqWrite()
    {
        init();
        try
        {
            byte[] writeData = new byte[64];
            bytefill(writeData,(byte)0xBB);

            // Reader Reserved memory bank with accessSequences
            //
            int offset, length;
            offset =0;
            for(length = 4;length <= 4; length+=2 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,writeData,0,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_RESERVED.toString()+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                    psLog.println(" op seg operation"+ reader.Actions.TagAccess.OperationSequence.getOperation(0));
                    psLog.println(" op seg operation"+ reader.Actions.TagAccess.OperationSequence.getOperation(1));
                    reader.Actions.TagAccess.OperationSequence.delete(op1);
                    reader.Actions.TagAccess.OperationSequence.delete(op2);
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                psLog.println(" op seg operation========================================="+ reader.Actions.TagAccess.OperationSequence.getLength());

                //Object k = reader.Actions.TagAccess.OperationSequence.getOperation(0);

//
                if( ReadMemory() )
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

            // write to EPC
            offset =6;
            for(length =4;length <=4; length+=2 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_EPC,length ,writeData,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_EPC,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_EPC+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                    reader.Actions.TagAccess.OperationSequence.delete(op1);
                    reader.Actions.TagAccess.OperationSequence.delete(op2);
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                
                if( ReadMemory() )
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

            //write to user
            offset =0;
            for(length =4;length <=4; length+=8 )
            {
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_USER+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                //reader.Actions.TagAccess.OperationSequence.delete(op1);
                //reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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
        }
//        catch(InvalidUsageException exp)
//        {
//            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
//        }
//        catch(OperationFailureException exp)
//        {
//            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
//        }
        catch(InterruptedException e)
        {

        }
        
    }

     void testOpSeqBlockWrite()
    {
         init();
        try
        {
            byte[] writeData = new byte[64];
            //byte[] writeData = new byte[8];
            bytefill(writeData,(byte)0xDD);

            // Reader Reserved memory bank with accessSequences
            reader.Actions.purgeTags();
            int offset, length;
            offset =0;
            for(length = 2;length <=4; length+=4 )
            {
                accessComplete.reset();
                FormTestID(TestNo,SubNo++,"FUN");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                op3 = sequance.new Operation();
                op4 = sequance.new Operation();
                 op5 = sequance.new Operation();
                
//                addOpSequenceWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,0,0);
//                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
//                addOpSequenceBlockWrite(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,writeData,offset,0);
//                addOpSequenceRead(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,0,0);
//                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_EPC,length ,0,0);
//                addOpSequenceRead(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_TID,length ,100,0);
//                addOpSequenceRead(op4,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,0,0);

//                addOpSequenceBlockWrite(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,writeData,offset,0);
//                addOpSequenceBlockWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,writeData,offset,0);
//                addOpSequenceBlockWrite(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_EPC,2 ,writeData,200,0);
//                addOpSequenceBlockWrite(op4,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,offset,0);
                //addOpSequenceBlockWrite(op5,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,offset,0);
//
//                CleanupPendingSequence();
//                addOpSequenceWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,0,0);
//                addOpSequenceWrite(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,writeData,0,0);
//                addOpSequenceWrite(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE,MEMORY_BANK.MEMORY_BANK_EPC,2 ,writeData,6,0);
                addOpSequenceBlockErase(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                addOpSequenceBlockErase(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
//                 addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,offset,0);
//                addOpSequenceBlockErase(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_ERASE,MEMORY_BANK.MEMORY_BANK_RESERVED,length ,offset,0);


                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_RESERVED+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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

            // write to EPC
            offset =6;
            for(length =4;length <=4; length+=4 )
            {
                accessComplete.reset();
                FormTestID(TestNo,SubNo++,"FUN");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceBlockWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_EPC,length ,writeData,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_EPC,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_EPC+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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

            //write to user
            offset =0;
            for(length =4;length <=4; length+=8 )
            {
                accessComplete.reset();
                FormTestID(TestNo,SubNo++,"FUN");
                op1 = sequance.new Operation();
                op2 = sequance.new Operation();
                addOpSequenceBlockWrite(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE,MEMORY_BANK.MEMORY_BANK_USER,length ,writeData,offset,0);
                addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_WRITE);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_USER+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                reader.Actions.TagAccess.OperationSequence.delete(op1);
                reader.Actions.TagAccess.OperationSequence.delete(op2);
                if( ReadMemory() )
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
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }

    }

    void testOpSeqRead()
    {
        try
        {
            mystreamLog=new FileOutputStream("Java API_accSeq_Log.html");
            mystreamResult=new FileOutputStream("Java API_accSeq_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b>"+" ------------- Access Sequence Start Time :"+ (new Date()) +"------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "Access Sequence";
        }

        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }
        
        try
        {
            TestNo = 1;
//            reader.Config.resetFactoryDefaults();
            accessfilter.setRSSIRangeFilter(false);
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = reader.Config.getTagStorageSettings();
            tgSettings.enableAccessReports(true);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tgSettings);


            //Register for access start and stop events
            reader.Events.setAccessStartEvent(true);
            reader.Events.setAccessStopEvent(true);
            reader.Events.setAttachTagDataWithReadEvent(false);

            myaccesslistener = new accessListener(psLog,accessComplete);
            reader.Events.addEventsListener(myaccesslistener);

            // initialize the filter for which to do access on
            // short[] tagPattern1 = { 0xAB, 0xCD, 0xEF,0xAB, 0xCD, 0xEF };
            byte[] tagPattern1 = { (byte)0xBE,(byte)0xDD};
            byte[] tagMask = {(byte)0xFF,(byte)0xFF};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tagPattern1);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(tagMask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            accessfilter.setRSSIRangeFilter(false);
            
            // Reader Reserved memory bank with accessSequences
            //
            int offset, length;
            offset =0;
            // Reader TID memory bank with accessSequences
            //
            for(length =2;length <=12; length+=2 )
            {
                
                accessComplete.reset();
                FormTestID(TestNo++,SubNo,"OpSeq");
                op1 = sequance.new Operation();
                addOpSequenceRead(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_TID,length ,offset,0);
                psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
                psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ);
                psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_TID+" Offset" +offset +" Length "+length+"</b><br>");
                psLog.println("\n<b> Actual Result is :</b><br>");
                
                try
                {
                    //access sequence perform.
                    reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, null);//antInfo
                    accessComplete.waitOne();
                }
                catch(InvalidUsageException exp)
                {
                   psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                }
                catch(OperationFailureException exp)
                {
                    CleanupPendingSequence();
                    psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                }
                //reader.Actions.TagAccess.OperationSequence.delete(op1);
                if( ReadMemory() )
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

            // to test op seq without any parameters
            //
            FormTestID(TestNo++,SubNo,"OpSeq");
            op1 = sequance.new Operation();
            addOpSequenceRead(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_TID,length ,offset,0);
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ);
            psLog.println("\n<b> Expected: Read memory Bank"+MEMORY_BANK.MEMORY_BANK_TID+" Offset" +offset +" Length "+length+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");
            try
            {
                TagAccess.Sequence.Operation k = (TagAccess.Sequence.Operation)reader.Actions.TagAccess.OperationSequence.getOperation(0);
                psLog.println("getMemoryBank  "+k.ReadAccessParams.getMemoryBank());
                psLog.println("getMemoryBank  "+k.ReadAccessParams.getByteCount());
                psLog.println(" op seg operation========================================="+ k.toString());
                psLog.println(" op seg operation"+ reader.Actions.TagAccess.OperationSequence.getOperation(1));
                //access sequence perform.
                reader.Actions.TagAccess.OperationSequence.performSequence(null, null, null);
                Thread.sleep(5000);
                reader.Actions.TagAccess.OperationSequence.stopSequence();
                accessComplete.waitOne();
            }
            catch(InvalidUsageException exp)
            {
               psLog.println("<br>\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
            }
            catch(OperationFailureException exp)
            {
                CleanupPendingSequence();
                psLog.println("<br>\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
            }
           
            if( ReadMemory() )
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

            FormTestID(TestNo++,SubNo,"OpSeq");
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+" use all the methods in the class");
            psLog.println("\n<b> Expected: methods should return pass"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");
            //access sequence perform.
            try
            {
                

                psLog.println(" op seg getlength"+ reader.Actions.TagAccess.OperationSequence.getLength());
                psLog.println(" op seg operation"+ reader.Actions.TagAccess.OperationSequence.getOperation(reader.Actions.TagAccess.OperationSequence.getLength()));
                int[] success = new int[1];
                int [] failure = new int[1];
                reader.Actions.TagAccess.getLastAccessResult(success, failure);
                psLog.println(" Success failure count "+success + failure );
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");
                successCount++;
            }
            catch(InvalidUsageException exp)
            {
                System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
            catch(OperationFailureException exp)
            {
                System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
                failureCount++;
            }
            reader.Actions.TagAccess.OperationSequence.delete(op1);
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }


    void testOpSeqLock()
    {
         init();
        try
        {
            mystreamLog=new FileOutputStream("Java API_accSeqUNLOCK_Log.html");
            mystreamResult=new FileOutputStream("Java API_accSeqUNLOCK_Result.txt");
            psLog = new PrintStream(mystreamLog);
            psLog.println("<HTML>\n<BODY>\n");
            psLog.println("<br><b>"+" ------------- Access Sequence Start Time :"+ (new Date()) +"------------ </b><br>");
            psResult = new PrintStream(mystreamResult);
            logText = "Access Sequence";
        }

        catch(FileNotFoundException e)
        {
            psLog.println("\n "+e.getMessage());
        }

        try
        {
            accessComplete.reset();
//            FormTestID(TestNo,SubNo++,"FUN");
            op1 = sequance.new Operation();
            op2 = sequance.new Operation();
            op3 = sequance.new Operation();
            op4 = sequance.new Operation();
            addOpSequenceLock(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK );
            addOpSequenceLock2(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK );
            addOpSequenceLock3(op3,ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK );
            addOpSequenceLock4(op4,ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK );
//            addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,2 , 0,0x12345678);
//            addOpSequenceRead(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ,MEMORY_BANK.MEMORY_BANK_USER,2 , 0,33334444);
//            addOpSequenceLock(op2,ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK );
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_LOCK);
            psLog.println("\n<b> Expected: Lock memory Bank"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");

            
//            //access sequence perform.
//            byte[] tp = { (byte)0xA2,(byte)0x2F};
//            accessfilter.TagPatternA.setTagPattern(tp);
            
            reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
            accessComplete.waitOne();
            int successCount[] = new int[1];int failureCount[] = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
            reader.Actions.TagAccess.OperationSequence.delete(op1);
            if( successCount[0] == 0 )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
            if( ReadMemory() )
                {
                    psLog.println("\n Test Result    :PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
//                    successCount++;
                }
                else
                {
                    psLog.println("\n Test Result    :FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
//                    failureCount++;
                }
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            CleanupPendingSequence();
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }

    void testOpSeqKill()
    {
        init();
        boolean bSuccess = false;
        try
        {
            accessComplete.reset();
            FormTestID(TestNo,SubNo++,"FUN");
            op1 = sequance.new Operation();
            bSuccess=addOpSequenceKill(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_KILL );
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_KILL);
            psLog.println("\n<b> Expected: Kill memory Bank"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");

//            //access sequence perform.
//            byte[] tp = { (byte)0xAA,(byte)0xAA};
//
//            accessfilter.TagPatternA.setTagPattern(tp);
//            if(bSuccess)
//            {
                reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
                accessComplete.waitOne();
//            }
            int successCount[] = new int[1];int failureCount[] = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
            reader.Actions.TagAccess.OperationSequence.delete(op1);
            if( successCount[0] == 0 && bSuccess)
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
            if( ReadMemory() )
                {
                    psLog.println("\n Test Result    :PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
//                    successCount++;
                }
                else
                {
                    psLog.println("\n Test Result    :FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
//                    failureCount++;
                }
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            CleanupPendingSequence();
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }

    void testOpSeqSetEAS( )
    {
        init();
        try
        {
            // clean up any set quiet flags on NXP tags if any
            //
            resetReadProtect.setAccessPassword(0x00000001);

            reader.Actions.TagAccess.NXP.resetReadProtectEvent(resetReadProtect, null);
            Thread.sleep(5000);
            accessComplete.reset();
            FormTestID(TestNo,SubNo++,"FUN");
            //TagAccess.WriteSpecificFieldAccessParams wSpecific = tagAccess.new WriteSpecificFieldAccessParams();
           // byte[] writeData = {0x12,0x34,0x56,0x78};
            //wSpecific.setAccessPassword(1);
            //wSpecific.setWriteData(writeData);

            //wSpecific.setWriteDataLength(4);
            //reader.Actions.TagAccess.writeAccessPasswordWait("FACEFACEFACEFACEFACEFACE", wSpecific, null);
            op1 = sequance.new Operation();
            addOpSequenceSetEAS(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS,0x00000001,true );
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS);
            psLog.println("\n<b> Expected: Test EAS set on nxt tags"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");

//            //access sequence perform.
//            byte[] tp = { (byte)0xFA,(byte)0xCE};
//            accessfilter.TagPatternA.setTagPattern(tp);

            reader.Events.setAccessStartEvent(false);
            reader.Events.setAccessStopEvent(false);
            reader.Events.setEASAlarmEvent(true);
            reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, null);
            Thread.sleep(10000);
            //reader.Actions.TagAccess.OperationSequence.delete(op1);
            reader.Actions.TagAccess.NXP.performEASScan();
            Thread.sleep(10000);
            reader.Actions.TagAccess.NXP.stopEASScan();
            psLog.println("\n Test Result    :PASS");
            psResult.println(TestID+"   "+logText+"    :PASS");

            if( ReadMemoryBankdData() )
                {
                    psLog.println("\n Test Result    :PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
//                    successCount++;
                }
                else
                {
                    psLog.println("\n Test Result    :FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
//                    failureCount++;
                }

//            op1 = sequance.new Operation();
//            addOpSequenceSetEAS(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS,0x12345678,false );
//            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
//            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_SET_EAS);
//            psLog.println("\n<b> Expected: Test EAS set on nxt tags"+"</b><br>");
//            psLog.println("\n<b> Actual Result is :</b><br>");
//
//            accessComplete.reset();
//            reader.Actions.TagAccess.OperationSequence.performSequence(null, tInfo, null);
//            Thread.sleep(20000);
//            reader.Actions.TagAccess.OperationSequence.delete(op1);
//            psLog.println("\n Test Result    :PASS");
//            psResult.println(TestID+"   "+logText+"    :PASS");
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            CleanupPendingSequence();
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }
    
    void testOpSeqSetReadProtectEAS()
    {
        init();
        try
        {
//            accessComplete.reset();
//            FormTestID(TestNo,SubNo++,"FUN");
//            op1 = sequance.new Operation();
//            addOpSequenceReadProtect(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_READ_PROTECT,0x00000001 );
//            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
//            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_READ_PROTECT);
//            psLog.println("\n<b> Expected: Set Read Protect"+"</b><br>");
//            psLog.println("\n<b> Actual Result is :</b><br>");

            reader.Events.setAccessStartEvent(true);
            reader.Events.setAccessStopEvent(true);
            reader.Events.setEASAlarmEvent(false);

//            reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
//            Thread.sleep(10000);
            int successCount[] = new int[1];int failureCount[] = new int[1];
//            reader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
//            reader.Actions.TagAccess.OperationSequence.delete(op1);
//            if( ReadMemoryBankdData() )
//                {
//                    psLog.println("\n Test Result    :PASS");
//                    psResult.println(TestID+"   "+logText+"    :PASS");
////                    successCount++;
//                }
//                else
//                {
//                    psLog.println("\n Test Result    :FAIL");
//                    psResult.println(TestID+"   "+logText+"    :FAIL");
////                    failureCount++;
//                }
            
            // reset read protect
            accessComplete.reset();
            FormTestID(TestNo,SubNo++,"FUN");
            op1 = sequance.new Operation();
            addOpSequenceReadProtect(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_RESET_READ_PROTECT,0x00000001 );
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_NXP_RESET_READ_PROTECT);
            psLog.println("\n<b> Expected: Set Read Protect"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");

            reader.Actions.TagAccess.OperationSequence.performSequence(null, tInfo, antInfo);
            Thread.sleep(10000);

            reader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
            reader.Actions.TagAccess.OperationSequence.delete(op1);
            if( successCount[0] >= 1 )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
            if( ReadMemoryBankdData() )
                {
                    psLog.println("\n Test Result    :PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
//                    successCount++;
                }
                else
                {
                    psLog.println("\n Test Result    :FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
//                    failureCount++;
                }
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            CleanupPendingSequence();
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }

    }
    
    // to test this after it is being exposed through the op sequence list.
    //
    void testBP()
    {
        try
        {
            init();
            accessComplete.reset();
            FormTestID(TestNo,SubNo++,"FUN");

            op1 = sequance.new Operation();
            addOpSequenceBP(op1,ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_PERMALOCK );
            psLog.println("\n<br><a>TestCase No:"+TestID+"</a> <br>");
            psLog.println("\n<b>Description</b>:"+logText+"      "+ACCESS_OPERATION_CODE.ACCESS_OPERATION_BLOCK_PERMALOCK);
            psLog.println("\n<b> Expected: Block permalock memory Bank"+"</b><br>");
            psLog.println("\n<b> Actual Result is :</b><br>");

//            //access sequence perform.
//            byte[] tp = { (byte)0xBE,(byte)0xDD};
//
//            accessfilter.TagPatternA.setTagPattern(tp);

            reader.Actions.TagAccess.OperationSequence.performSequence(accessfilter, tInfo, antInfo);
            accessComplete.waitOne();
            int successCount[] = new int[1];int failureCount[] = new int[1];
            reader.Actions.TagAccess.getLastAccessResult(successCount, failureCount);
            reader.Actions.TagAccess.OperationSequence.delete(op1);
            if( successCount[0] == 0 )
            {
                psLog.println("\n Test Result    :PASS");
                psResult.println(TestID+"   "+logText+"    :PASS");

            }
            else
            {
                psLog.println("\n Test Result    :FAIL");
                psResult.println(TestID+"   "+logText+"    :FAIL");
            }
            if( ReadMemory() )
                {
                    psLog.println("\n Test Result    :PASS");
                    psResult.println(TestID+"   "+logText+"    :PASS");
//                    successCount++;
                }
                else
                {
                    psLog.println("\n Test Result    :FAIL");
                    psResult.println(TestID+"   "+logText+"    :FAIL");
//                    failureCount++;
                }
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            CleanupPendingSequence();
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        catch(InterruptedException e)
        {

        }
    }
    public void init()
    {
//        try
//        {
//            mystreamLog=new FileOutputStream("Java API_accSeq_Log.html");
//            mystreamResult=new FileOutputStream("Java API_accSeq_Result.txt");
//            psLog = new PrintStream(mystreamLog);
//            psLog.println("<HTML>\n<BODY>\n");
//            psLog.println("<br><b>"+" ------------- Access Sequence Start Time :"+ (new Date()) +"------------ </b><br>");
//            psResult = new PrintStream(mystreamResult);
//            logText = "Access Sequence";
//        }
//
//        catch(FileNotFoundException e)
//        {
//            psLog.println("\n "+e.getMessage());
//        }

        try
        {
            accessfilter.setRSSIRangeFilter(false);
            TagStorageSettings tgSettings = new TagStorageSettings();
            tgSettings = reader.Config.getTagStorageSettings();
            tgSettings.enableAccessReports(true);
            tgSettings.setTagFields(TAG_FIELD.ALL_TAG_FIELDS);
            reader.Config.setTagStorageSettings(tgSettings);


            //Register for access start and stop events
            reader.Events.setAccessStartEvent(true);
            reader.Events.setAccessStopEvent(true);
            reader.Events.setAttachTagDataWithReadEvent(false);

            myaccesslistener = new accessListener(psLog,accessComplete);
            reader.Events.addEventsListener(myaccesslistener);

            // initialize the filter for which to do access on
            // short[] tagPattern1 = { 0xAB, 0xCD, 0xEF,0xAB, 0xCD, 0xEF };
//            byte[] tagPattern1 = { (byte)0xF0,(byte)0x0D, (byte)0xFA,(byte)0xCE };
            byte[] tagPattern1 = { (byte)0xBE,(byte)0xDD };
            byte[] tagMask = {(byte)0xFF,(byte)0xFF};
            accessfilter.TagPatternA.setBitOffset(32);
            accessfilter.TagPatternA.setTagPattern(tagPattern1);
            accessfilter.TagPatternA.setMemoryBank(MEMORY_BANK.MEMORY_BANK_EPC);
            accessfilter.TagPatternA.setTagMaskBitCount(16);
            accessfilter.TagPatternA.setTagPatternBitCount(16);
            accessfilter.TagPatternA.setTagMask(tagMask);
            accessfilter.setAccessFilterMatchPattern(FILTER_MATCH_PATTERN.A);
            accessfilter.setRSSIRangeFilter(false);
            
        }
        catch(InvalidUsageException exp)
        {
            System.out.print("\nInvalidUsageException"+exp.getInfo()+exp.getVendorMessage());
        }
        catch(OperationFailureException exp)
        {
            System.out.print("\nOperationFailureException"+exp.getMessage()+exp.getStatusDescription());
        }
        

    }
}
