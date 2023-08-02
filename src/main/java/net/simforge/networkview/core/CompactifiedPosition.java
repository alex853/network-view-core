package net.simforge.networkview.core;

import net.simforge.commons.misc.Geo;
import net.simforge.networkview.core.report.ReportInfo;
import net.simforge.networkview.core.report.ReportUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CompactifiedPosition implements Position {

    private static final int DOUBLE_LENGTH = 8;
    private static final int STRING_5_LENGTH = 5;
    private static final int STRING_7_LENGTH = 7;
    private static final int ACTUAL_ALTITUDE_LENGTH = 3;
    private static final int GROUNDSPEED_LENGTH = 2;

    private static final byte IS_ON_GROUND_MASK = 0x01;
    private static final byte IS_IN_AIRPORT_MASK = 0x02;

    private static final int FLAGS_INDEX = 0;
    private static final int COORDS_LAT_INDEX = FLAGS_INDEX + 1;
    private static final int COORDS_LON_INDEX = COORDS_LAT_INDEX + DOUBLE_LENGTH;
    private static final int ACTUAL_ALTITUDE_INDEX = COORDS_LON_INDEX + DOUBLE_LENGTH;
    private static final int ACTUAL_FL_INDEX = ACTUAL_ALTITUDE_INDEX + ACTUAL_ALTITUDE_LENGTH;
    private static final int AIRPORT_ICAO_INDEX = ACTUAL_FL_INDEX + STRING_5_LENGTH;
    private static final int GROUNDSPEED_INDEX = AIRPORT_ICAO_INDEX + STRING_5_LENGTH;
    private static final int CALLSIGN_INDEX = GROUNDSPEED_INDEX + GROUNDSPEED_LENGTH;
    private static final int REG_NO_INDEX = CALLSIGN_INDEX + STRING_7_LENGTH;
    private static final int FP_AIRCRAFT_TYPE_INDEX = REG_NO_INDEX + STRING_7_LENGTH;
    private static final int FP_DEPARTURE_INDEX = FP_AIRCRAFT_TYPE_INDEX + STRING_5_LENGTH;
    private static final int FP_DESTINATION_INDEX = FP_DEPARTURE_INDEX + STRING_5_LENGTH;
    private static final int TOTAL_LENGTH = FP_DESTINATION_INDEX + STRING_5_LENGTH;

    private int reportId;
    private int reportSeconds;

    private byte[] data;

    static Position from(Position source) {
        if (source.isPositionKnown()) {
            return buildKnownPosition(source);
        } else {
            return buildUnknownPosition(source);
        }
    }

    private static Position buildKnownPosition(Position source) {
        CompactifiedPosition compact = new CompactifiedPosition();

        copyReportInfo(compact, source);

        compact.data = new byte[TOTAL_LENGTH];

        Geo.Coords coords = source.getCoords();
        ByteBuffer.wrap(compact.data, COORDS_LAT_INDEX, DOUBLE_LENGTH).putDouble(coords.getLat());
        ByteBuffer.wrap(compact.data, COORDS_LON_INDEX, DOUBLE_LENGTH).putDouble(coords.getLon());
        putInteger(compact.data, source.getActualAltitude(), ACTUAL_ALTITUDE_INDEX, ACTUAL_ALTITUDE_LENGTH);
        putString(compact.data, source.getActualFL(), ACTUAL_FL_INDEX, STRING_5_LENGTH);
        putBoolean(compact.data, source.isOnGround(), IS_ON_GROUND_MASK);
        putBoolean(compact.data, source.isInAirport(), IS_IN_AIRPORT_MASK);
        putString(compact.data, source.getAirportIcao(), AIRPORT_ICAO_INDEX, STRING_5_LENGTH);
        putInteger(compact.data, source.getGroundspeed(), GROUNDSPEED_INDEX, GROUNDSPEED_LENGTH);
        putString(compact.data, source.getCallsign(), CALLSIGN_INDEX, STRING_7_LENGTH);
        putString(compact.data, source.getRegNo(), REG_NO_INDEX, STRING_7_LENGTH);
        putString(compact.data, source.getFpAircraftType(), FP_AIRCRAFT_TYPE_INDEX, STRING_5_LENGTH);
        putString(compact.data, source.getFpDeparture(), FP_DEPARTURE_INDEX, STRING_5_LENGTH);
        putString(compact.data, source.getFpDestination(), FP_DESTINATION_INDEX, STRING_5_LENGTH);

        return compact;
    }

    private static Position buildUnknownPosition(Position source) {
        CompactifiedPosition compact = new CompactifiedPosition();

        copyReportInfo(compact, source);

        return compact;
    }

    private static void copyReportInfo(CompactifiedPosition compact, Position source) {
        ReportInfo reportInfo = source.getReportInfo();

        Long reportId = reportInfo.getId();
        if (reportId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        compact.reportId = reportId.intValue();

        long epochSeconds = reportInfo.getDt().toEpochSecond(ZoneOffset.UTC);
        if (epochSeconds > Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        compact.reportSeconds = (int) epochSeconds;
    }

    private CompactifiedPosition() {
    }

    @Override
    public ReportInfo getReportInfo() {
        return new ReportInfo() {
            @Override
            public Long getId() {
                return (long) reportId;
            }

            @Override
            public String getReport() {
                LocalDateTime reportDt = LocalDateTime.ofEpochSecond(reportSeconds, 0, ZoneOffset.UTC);
                return ReportUtils.toTimestamp(reportDt);
            }
        };
    }

    @Override
    public Geo.Coords getCoords() {
        checkPositionKnown();
        return new Geo.Coords(
                ByteBuffer.wrap(data, COORDS_LAT_INDEX, DOUBLE_LENGTH).getDouble(),
                ByteBuffer.wrap(data, COORDS_LON_INDEX, DOUBLE_LENGTH).getDouble());
    }

    @Override
    public int getActualAltitude() {
        checkPositionKnown();
        return getInteger(data, ACTUAL_ALTITUDE_INDEX, ACTUAL_ALTITUDE_LENGTH);
    }

    @Override
    public String getActualFL() {
        checkPositionKnown();
        return getString(data, ACTUAL_FL_INDEX, STRING_5_LENGTH);
    }

    @Override
    public boolean isOnGround() {
        checkPositionKnown();
        return getBoolean(data, IS_ON_GROUND_MASK);
    }

    @Override
    public boolean isInAirport() {
        checkPositionKnown();
        return getBoolean(data, IS_IN_AIRPORT_MASK);
    }

    @Override
    public String getAirportIcao() {
        checkPositionKnown();
        return getString(data, AIRPORT_ICAO_INDEX, STRING_5_LENGTH);
    }

    @Override
    public short getGroundspeed() {
        checkPositionKnown();
        return (short) getInteger(data, GROUNDSPEED_INDEX, GROUNDSPEED_LENGTH);
    }

    private void checkPositionKnown() {
        if (!isPositionKnown()) {
            throw new IllegalStateException("Position is unknown");
        }
    }

    @Override
    public boolean isPositionKnown() {
        return data != null;
    }

    @Override
    public String getCallsign() {
        checkPositionKnown();
        return getString(data, CALLSIGN_INDEX, STRING_7_LENGTH);
    }

    @Override
    public String getRegNo() {
        checkPositionKnown();
        return getString(data, REG_NO_INDEX, STRING_7_LENGTH);
    }

    @Override
    public String getFpAircraftType() {
        checkPositionKnown();
        return getString(data, FP_AIRCRAFT_TYPE_INDEX, STRING_5_LENGTH);
    }

    @Override
    public String getFpDeparture() {
        checkPositionKnown();
        return getString(data, FP_DEPARTURE_INDEX, STRING_5_LENGTH);
    }

    @Override
    public String getFpDestination() {
        checkPositionKnown();
        return getString(data, FP_DESTINATION_INDEX, STRING_5_LENGTH);
    }

    @Override
    public String toString() {
        return "{" + getStatus() + ", rId=" + reportId + "}";
    }

    private static void putInteger(byte[] data, int value, int offset, int length) {
        boolean negative = value < 0;
        value = negative ? -value : value;
        while (length > 0) {
            data[offset] = (byte) (value % 256);
            value /= 256;
            length--;
            offset++;
        }
        if (negative) {
            data[offset-1] = (byte) (data[offset-1] | 128);
        }
    }

    private static int getInteger(byte[] data, int offset, int length) {
        int value = 0;
        offset += length - 1;

        boolean checkNegative = true;
        boolean negative = false;

        while (length > 0) {
            value *= 256;
            byte each = data[offset];
            if (checkNegative) {
                checkNegative = false;
                negative = (data[offset] & 128) > 0;
                each = (byte) (each & ~128);
            }
            value += each >= 0 ? each : 256 + each;
            length--;
            offset--;
        }

        return negative ? -value : value;
    }

    private static void putString(byte[] data, String value, int offset, int length) {
        if (value == null) {
            return;
        }

        if (value.length() > length) {
            throw new IllegalArgumentException();
        }

        ByteBuffer.wrap(data, offset, value.length()).put(value.getBytes(StandardCharsets.US_ASCII));
    }

    private static String getString(byte[] data, int offset, int length) {
        int actualLength = length;
        while (actualLength > 0) {
            if (data[offset + actualLength - 1] != 0) {
                break;
            }

            actualLength--;
            if (actualLength == 0) {
                return null;
            }
        }

        byte[] buf = new byte[actualLength];
        System.arraycopy(data, offset, buf, 0, actualLength);
        return new String(buf, StandardCharsets.US_ASCII);
    }

    private static void putBoolean(byte[] data, boolean value, byte mask) {
        byte flags = data[FLAGS_INDEX];
        if (value) {
            flags = (byte) (flags | mask);
        } else {
            flags = (byte) (flags & ~mask);
        }
        data[FLAGS_INDEX] = flags;
    }

    private boolean getBoolean(byte[] data, byte mask) {
        byte flags = data[FLAGS_INDEX];
        return (flags & mask) != 0;
    }
}
