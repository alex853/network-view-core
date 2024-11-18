package net.simforge.networkview.core.report;

import junit.framework.TestCase;

public class RegNoPatternsTest extends TestCase {

    public void test_isRegNo_emptyInput() {
        assertFalse(RegNoPatterns.isRegNo(""));
        assertFalse(RegNoPatterns.isRegNo(null));
    }

    public void test_isRegNo_PT_ISC() {
        assertTrue(RegNoPatterns.isRegNo("PT-ISC"));
    }

    public void test_isRegNo_callsign_TOM3AY() {
        assertFalse(RegNoPatterns.isRegNo("TOM3AY"));
    }

    public void test_isRegNo_incorrectRegNo() {
        assertFalse(RegNoPatterns.isRegNo("12324556"));
    }

    public void test_isRegNo_GDBAA() {
        assertTrue(RegNoPatterns.isRegNo("GDBAA"));
    }

    public void test_isRegNo_G_DBAA() {
        assertTrue(RegNoPatterns.isRegNo("G-DBAA"));
    }

    public void test_isRegNo_GDBAAX() {
        assertFalse(RegNoPatterns.isRegNo("GDBAAX"));
    }

    public void test_isRegNo_G_DBAAX() {
        assertFalse(RegNoPatterns.isRegNo("G_DBAAX"));
    }

    public void test_isRegNo_B3077() {
        assertTrue(RegNoPatterns.isRegNo("B3077"));
    }

    public void test_isRegNo_N63AC() {
        assertTrue(RegNoPatterns.isRegNo("N63AC"));
    }

    public void test_isRegNo_N2630S() {
        assertTrue(RegNoPatterns.isRegNo("N2630S"));
    }

    public void test_isRegNo_N567X() {
        assertTrue(RegNoPatterns.isRegNo("N567X"));
    }

    public void test_isRegNo_N1R() {
        assertTrue(RegNoPatterns.isRegNo("N1R"));
    }

    public void test_isRegNo_N234() {
        assertTrue(RegNoPatterns.isRegNo("N234"));
    }

    public void test_isRegNo_RA24390() {
        assertTrue(RegNoPatterns.isRegNo("RA24390"));
    }

    public void test_isRegNo_RA_24390() {
        assertTrue(RegNoPatterns.isRegNo("RA-24390"));
    }

    public void test_isRegNo_RA2439G() {
        assertTrue(RegNoPatterns.isRegNo("RA2439G"));
    }

    public void test_isRegNo_HK5078A() {
        assertTrue(RegNoPatterns.isRegNo("HK5078A"));
    }

    public void test_isRegNo_HK4058G() {
        assertTrue(RegNoPatterns.isRegNo("HK4058G"));
    }

    public void test_isRegNo_4XCXX() {
        assertTrue(RegNoPatterns.isRegNo("4XCXX"));
    }

    public void test_isRegNo_4X_CXX() {
        assertTrue(RegNoPatterns.isRegNo("4X-CXX"));
    }

    public void test_isRegNo_JA3515() {
        assertTrue(RegNoPatterns.isRegNo("JA3515"));
    }

    public void test_isRegNo_JA007Z() {
        assertTrue(RegNoPatterns.isRegNo("JA007Z"));
    }

    public void test_isRegNo_JA55TC() {
        assertTrue(RegNoPatterns.isRegNo("JA55TC"));
    }

    public void test_isRegNo_T7ARZ() {
        assertTrue(RegNoPatterns.isRegNo("T7ARZ"));
    }

    public void test_isRegNo_T7STAR() {
        assertTrue(RegNoPatterns.isRegNo("T7STAR"));
    }

    public void test_isRegNo_YV2392() {
        assertTrue(RegNoPatterns.isRegNo("YV2392"));
    }

    public void test_isRegNo_YV537T() {
        assertTrue(RegNoPatterns.isRegNo("YV537T"));
    }

    public void test_isRegNo_VHOQH() {
        assertTrue(RegNoPatterns.isRegNo("VHOQH"));
    }

    public void test_isRegNo_CSTNK() {
        assertTrue(RegNoPatterns.isRegNo("CSTNK"));
    }

    public void test_isRegNo_DQFAD() {
        assertTrue(RegNoPatterns.isRegNo("DQFAD"));
    }
}
