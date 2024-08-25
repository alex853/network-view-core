package net.simforge.networkview.core.report.file;

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

    //== recognizeRegNo tests ==========================================================================================

    public void testRecognizeRegNo_emptyInput() {
        assertNull(ParsingLogics.recognizeRegNo(""));
        assertNull(ParsingLogics.recognizeRegNo(null));
    }

    public void testRecognizeRegNo_PT_ISC() {
        assertEquals("PT-ISC", ParsingLogics.recognizeRegNo("PT-ISC"));
    }

    public void testRecognizeRegNo_callsign_TOM3AY() {
        assertNull(ParsingLogics.recognizeRegNo("TOM3AY"));
    }

    public void testRecognizeRegNo_GDBAA() {
        assertEquals("GDBAA", ParsingLogics.recognizeRegNo("GDBAA"));
    }

    public void testRecognizeRegNo_G_DBAA() {
        assertEquals("G-DBAA", ParsingLogics.recognizeRegNo("G-DBAA"));
    }

    // postponed - see TRACKER-41
/*    public void testRecognizeRegNo_GDBAAX() {
        assertNull(ParsingLogics.recognizeRegNo("GDBAAX"));
    }*/

    public void testRecognizeRegNo_G_DBAAX() {
        assertNull(ParsingLogics.recognizeRegNo("G_DBAAX"));
    }

    public void testRecognizeRegNo_B3077() {
        assertEquals("B3077", ParsingLogics.recognizeRegNo("B3077"));
    }

    public void testRecognizeRegNo_N63AC() {
        assertEquals("N63AC", ParsingLogics.recognizeRegNo("N63AC"));
    }

    public void testRecognizeRegNo_N2630S() {
        assertEquals("N2630S", ParsingLogics.recognizeRegNo("N2630S"));
    }

    public void testRecognizeRegNo_N567X() {
        assertEquals("N567X", ParsingLogics.recognizeRegNo("N567X"));
    }

    public void testRecognizeRegNo_N1R() {
        assertEquals("N1R", ParsingLogics.recognizeRegNo("N1R"));
    }

    public void testRecognizeRegNo_N234() {
        assertEquals("N234", ParsingLogics.recognizeRegNo("N234"));
    }

    public void testRecognizeRegNo_RA2439G() {
        assertEquals("RA2439G", ParsingLogics.recognizeRegNo("RA2439G"));
    }

    public void testRecognizeRegNo_HK5078() {
        assertEquals("HK5078", ParsingLogics.recognizeRegNo("HK5078"));
    }

    public void testRecognizeRegNo_HK4058G() {
        assertEquals("HK4058G", ParsingLogics.recognizeRegNo("HK4058G"));
    }

    public void testRecognizeRegNo_4XCXX() {
        assertEquals("4XCXX", ParsingLogics.recognizeRegNo("4XCXX"));
    }

    public void testRecognizeRegNo_4X_CXX() {
        assertEquals("4X-CXX", ParsingLogics.recognizeRegNo("4X-CXX"));
    }

    public void testRecognizeRegNo_JA3515() {
        assertEquals("JA3515", ParsingLogics.recognizeRegNo("JA3515"));
    }

    public void testRecognizeRegNo_JA007Z() {
        assertEquals("JA007Z", ParsingLogics.recognizeRegNo("JA007Z"));
    }

    public void testRecognizeRegNo_JA55TC() {
        assertEquals("JA55TC", ParsingLogics.recognizeRegNo("JA55TC"));
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
