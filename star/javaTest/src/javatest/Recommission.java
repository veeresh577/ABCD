/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javatest;
import com.mot.rfid.api3.*;
import java.io.*;

/**
 *
 * @author QMTN48
 */
public class Recommission extends Commonclass
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
    private TagAccess.RecommisionAccessParams recomParams;
    private TagAccess tagAccess;
	private AntennaInfo antInfo;

    public Recommission( RFIDReader myreader)
    {
        reader = myreader;
        tagAccess = new TagAccess( );
        accessfilter = new AccessFilter();
        antenna = reader.Config.Antennas;
        recomParams = tagAccess.new RecommisionAccessParams();
		antInfo = new AntennaInfo();
        recomParams.setKillPassword(0x12345678);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_PERMALOCK);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_PERMALOCK_PASSWORD);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_USER_MEMORY);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_USER_MEMORY_2ND_OPTION);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_USER_MEMORY_PASSWORD);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_USER_MEMORY_PASSWORD_2ND_OPTION);
        recomParams.setOpCode(RECOMMISSION_OPERATION_CODE.RECOMMISSION_DISABLE_PASSWORD);
    }

    public void testRecommission()
    {
        try
        {
			antInfo.setAntennaID(antennaList);
            antInfo.setAntennaOperationQualifier(antOPQ);
            recomParams.getKillPassword();
            recomParams.getOpCode();
            reader.Actions.TagAccess.recommisionWait("AAAAAAAA", recomParams, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }

        try
        {
            reader.Actions.TagAccess.recommisionWait(null, recomParams, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }
        try
        {
            reader.Actions.TagAccess.recommisionWait("AAAAAAAA", null, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }

        try
        {
            reader.Actions.TagAccess.recommisionEvent(recomParams, accessfilter, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }

        try
        {
            reader.Actions.TagAccess.recommisionEvent(null, accessfilter, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }

        try
        {
            reader.Actions.TagAccess.recommisionEvent(recomParams, null, antInfo);
        }
        catch(InvalidUsageException exp)
        {
            System.out.println( exp.getVendorMessage() + exp.getInfo());
        }
        catch(OperationFailureException opexp)
        {
            System.out.println(opexp.getVendorMessage()+ opexp.getStatusDescription());
        }

    }

}
