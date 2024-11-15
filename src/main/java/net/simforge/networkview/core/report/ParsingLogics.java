package net.simforge.networkview.core.report;

import net.simforge.networkview.core.report.persistence.ReportPilotPosition;

public class ParsingLogics {

    private static final String REG_PREFIX = "REG/";

    public static String parseRegNo(ReportPilotPosition position, String fpRemarks) {
        String reg = null;
        if (fpRemarks != null) {
            fpRemarks = fpRemarks.toUpperCase();
            int index = fpRemarks.indexOf(REG_PREFIX);
            if (index != -1) {
                final String str1 = fpRemarks.substring(index + REG_PREFIX.length());
                final int spaceIndex = str1.indexOf(' ');
                final String str2 = spaceIndex >= 0 ? str1.substring(0, spaceIndex) : str1;
                if (RegNoPatterns.isRegNo(str2)) {
                    reg = str2;
                }
            }
        }

        if (reg == null) {
            if (RegNoPatterns.isRegNo(position.getCallsign())) {
                reg = position.getCallsign();
            }
        }

        if (reg != null) {
            int i = reg.indexOf('-');
            if (i != -1) {
                reg = reg.substring(0, i) + reg.substring(i + 1);
            }
        }

        return reg;
    }

    public static String parseAircraftType(String fpAircraft) {
        if (fpAircraft == null || fpAircraft.length() == 0) {
            return null;
        }

        int i = fpAircraft.indexOf("-");
        if (i != -1) {
            fpAircraft = fpAircraft.substring(0, i);
        }

        // We expect:
        //   [Z/]DDDD[/X] or [Z/]DDD[/X] or [Z/]DD[/X]
        // We split it by '/' symbol and the longest section will be aircraft type

        String[] strs = fpAircraft.split("/");
        if (strs.length == 0) {
            return null;
        }
        String result = strs[0];
        for (i = 1; i < strs.length; i++) {
            if (result.length() < strs[i].length()) {
                result = strs[i];
            }
        }

        return result;
    }
}
