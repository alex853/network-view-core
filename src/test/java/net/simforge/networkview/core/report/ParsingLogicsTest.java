package net.simforge.networkview.core.report;

import junit.framework.TestCase;
import net.simforge.networkview.core.report.ParsingLogics;
import net.simforge.networkview.core.report.persistence.ReportPilotPosition;

public class ParsingLogicsTest extends TestCase {

    //== parseRegNo tests ==============================================================================================

    public void testParseRegNo_REGinRemarks() {
        assertEquals("GDBAA", ParsingLogics.parseRegNo(mockPosition("BAW2771"), "+VFPS+/V/PBN/A1B1C1D1L1O1S1 NAV/RNVD1E2A1 DOF/151206 REG/GDBAA EET/GMMM0021 LPPC0111 LECM0148 LFFF0225 EGTT0314 RVR/75 PER/C OPR/BAVIRTUAL"));
    }

    public void testParseRegNo_inCallsign() {
        assertEquals("PTISG", ParsingLogics.parseRegNo(mockPosition("PT-ISG"), ""));
    }

    public void testParseRegNo_nowhere() {
        assertNull(ParsingLogics.parseRegNo(mockPosition("VIR33C"), "+VFPS+/T/ OPR/VIRGINVIRTUALGROUP.CO.UK"));
    }

    private ReportPilotPosition mockPosition(String callsign) {
        ReportPilotPosition position = new ReportPilotPosition();
        position.setCallsign(callsign);
        return position;
    }

    //== parseAircraftType tests =======================================================================================

    public void testParseAircraftType_null() {
        assertNull(ParsingLogics.parseAircraftType(null));
    }

    public void testParseAircraftType_empty() {
        assertNull(ParsingLogics.parseAircraftType(""));
    }

    public void testParseAircraftType_startsWithSlash() {
        assertEquals("A321", ParsingLogics.parseAircraftType("/A321"));
    }

    public void testParseAircraftType_slashMinus() {
        assertNull(ParsingLogics.parseAircraftType("/-"));
    }

    public void testParseAircraftType_includingQuestionMark() {
        assertEquals("B407", ParsingLogics.parseAircraftType("B407/?-VGDW/C"));
    }

    public void testParseAircraftType_B_T154_G() {
        assertEquals("T154", ParsingLogics.parseAircraftType("B/T154/G"));
    }
    public void testParseAircraftType_A320_G() {
        assertEquals("A320", ParsingLogics.parseAircraftType("A320/G"));
    }

    public void testParseAircraftType_1_B772_HSDRWY_S() {
        assertEquals("B772", ParsingLogics.parseAircraftType("1/B772/H-SDRWY/S"));
    }

    public void testParseAircraftType_A321_M_SDE2E3FGJ4HIRWYZ_LB1() {
        assertEquals("A321", ParsingLogics.parseAircraftType("A321/M-SDE2E3FGJ4HIRWYZ/LB1"));
    }

    public void testParseAircraftType_P8_X() {
        assertEquals("P8", ParsingLogics.parseAircraftType("P8/X"));
    }

    public void testParseAircraftType_C17_L() {
        assertEquals("C17", ParsingLogics.parseAircraftType("C17/L"));
    }

    public void testParseAircraftType_AN2_X() {
        assertEquals("AN2", ParsingLogics.parseAircraftType("AN2/X"));
    }
}
