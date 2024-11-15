package net.simforge.networkview.core.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RegNoPatterns {
    private static final Logger log = LoggerFactory.getLogger(RegNoPatterns.class.getName());

    public static boolean isRegNo(final String input) {
        if (input == null || input.length() == 0) {
            return false;
        }

        final List<String> foundPatterns = new ArrayList<>();
        for (final CountryRules rule : rules) {
            for (final String pattern : rule.patterns) {
                if (checkPattern(input, pattern)) {
                    foundPatterns.add(pattern);
                }
            }
        }

        if (foundPatterns.size() == 1) {
            return true;
        } else if (foundPatterns.isEmpty()) {
            return false;
        } else {
            log.warn("found more than 1 pattern for '{}' - found patterns {}", input, foundPatterns);
            return false;
        }
    }

    private static boolean checkPattern(final String input, final String pattern) {
        int inputIndex = 0;
        char inputChar = input.charAt(inputIndex);
        boolean goToNextInputChar = false;

        for (int patternIndex = 0; patternIndex < pattern.length(); patternIndex++) {
            if (goToNextInputChar) {
                inputIndex++;
                if (input.length() == inputIndex) {
                    return false; // no more chars in input while we have something in pattern
                }
                inputChar = input.charAt(inputIndex);
                goToNextInputChar = false;
            }

            final char patternChar = pattern.charAt(patternIndex);

            if (isLetter(patternChar) || isDigit(patternChar)) {
                if (inputChar != patternChar) {
                    return false;
                } else {
                    goToNextInputChar = true;
                }
            } else if (isHyphen(patternChar)) {
                if (isHyphen(inputChar)) {
                    goToNextInputChar = true;
                } // else - it means hyphen is omitted in input and we accept it
            } else if (isLetterPlaceholder(patternChar)) {
                if (isLetter(inputChar)) {
                    goToNextInputChar = true;
                } else {
                    return false;
                }
            } else if (isDigitPlaceholder(patternChar)) {
                if (isDigit(inputChar)) {
                    goToNextInputChar = true;
                } else {
                    return false;
                }
            } else if (isLetterOrDigitPlaceholder(patternChar)) {
                if (isLetter(inputChar) || isDigit(inputChar)) {
                    goToNextInputChar = true;
                } else {
                    return false;
                }
            } else {
                throw new IllegalArgumentException("unknown pattern char in pattern " + pattern);
            }
        }

        //noinspection RedundantIfStatement
        if (inputIndex + 1 == input.length()) {
            return true; // we checked whole input string
        } else {
            return false; // we did not reach end of input string, pattern is shorted obviously
        }
    }

    private static boolean isLetterOrDigitPlaceholder(char c) {
        return c == 'z';
    }

    private static boolean isDigitPlaceholder(char c) {
        return c == 'd';
    }

    private static boolean isLetterPlaceholder(char c) {
        return c == 'a';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private static boolean isHyphen(char c) {
        return c == '-';
    }

    // see https://en.wikipedia.org/wiki/List_of_aircraft_registration_prefixes
    private static final CountryRules[] rules = {
            rule("Afghanistan", "YA-aaa"),
            rule("Albania", "ZA-aaa"),
            rule("Algeria", "7T-Vaa", "7T-Waa"),
            rule("Andorra", "C3-aaa"),
            rule("Angola", "D2-aaa"),
            rule("Anguilla", "VP-Aaa"),
            rule("Antigua and Barbuda", "V2-aaa"),
            rule("Argentina", "LV-aaa", "LV-Xddd", "LV-Sddd", "LV-Uddd"),
            rule("Armenia", "EK-ddddd"),
            rule("Aruba", "P4-aaa"),
            rule("Australia", "VH-zzz"),
            rule("Austria", "OE-aaa", "OE-dddd"),
            rule("Azerbaijan", "4K-AZd", "4K-AZdd", "4K-AZddd", "4K-ddddd"),
            rule("Bahamas", "C6-aaa"),
            rule("Bahrain", "A9C-aa", "A9C-aaa", "A9C-aaaa"),
            rule("Bangladesh", "S2-aaa"),
            rule("Barbados", "8P-aaa"),
            rule("Belarus", "EW-dddzz"),
            rule("Belgium", "OO-aaa", "OO-dd", "OO-ddd", "OO-add"),
            rule("Belize", "V3-aaa"),
            rule("Benin", "TY-aaa"),
            rule("Bermuda", "VP-Baa", "VQ-Baa"),
            rule("Bhutan", "A5-aaa"),
            rule("Bolivia", "CP-dddd"),
            rule("Bosnia and Herzegovina", "E7-aaa"),
            rule("Botswana", "A2-aaa"),
            rule("Brazil", "PP-aaa", "PT-aaa", "PR-aaa", "PU-aaa", "PS-aaa"),
            rule("British Virgin Islands", "VP-Laa"),
            rule("Brunei", "V8-zzz"),
            rule("Bulgaria", "LZ-aaa"),
            rule("Burkina Faso", "XT-aaa"),
            rule("Burundi", "9U-aaa"),
            rule("Cambodia", "XU-aaa"),
            rule("Cameroon", "TJ-aaa"),
            rule("Canada", "C-Faaa", "C-Gaaa", "C-Iaaa"),
            rule("Cape Verde", "D4-aaa"),
            rule("Cayman Islands", "VP-Caa", "VQ-Caa"),
            rule("Central African Republic", "TL-aaa"),
            rule("Chad", "TT-aaa"),
            rule("Chile", "CC-aaa"),
            rule("China", "B-ddzz"),
            rule("Colombia", "HJ-dddda", "HK-dddda"),
            rule("Comoros", "D6-aaa"),
            rule("Congo, Republic of", "TN-aaa"),
            rule("Congo, Democratic Republic of", "9S-aaa", "9T-aaa"),
            rule("Cook Islands", "E5-aaa"),
            rule("Costa Rica", "TI-aaa", "TI-ddd"),
            rule("Croatia", "9A-aaa"),
            rule("Cuba", "CU-adddd"),
            rule("Cyprus", "5B-aaa"),
            rule("Czech Republic", "OK-aaa", "OK-aaa dd", "OK-dddd", "OK-addd", "OK-addda"),
            rule("Denmark", "OY-aaa"),
            rule("Djibouti", "J2-aaa"),
            rule("Dominica", "J7-aaa"),
            rule("Dominican Republic", "HIdddaa", "HIddd", "HIdddd"),
            rule("East Timor", "4W-aaa"),
            rule("Ecuador", "HC-aaa"),
            rule("Egypt", "SU-aaa", "SU-ddd"),
            rule("El Salvador", "YS-aaa"),
            rule("Equatorial Guinea", "3C-aaa"),
            rule("Eritrea", "E3-aaaa"),
            rule("Estonia", "ES-aaa", "ES-dddd"),
            rule("Eswatini", "3DC-aaa"),
            rule("Ethiopia", "ET-aaa"),
            rule("Falkland Islands", "VP-Faa"),
            rule("Fiji", "DQ-aaa"),
            rule("Finland", "OH-aaa", "OH-ddd", "OH-Gddd", "OH-Uddd"),
            rule("France", "F-aaaa"),
            rule("Gabon", "TR-aaa"),
            rule("Gambia", "C5-aaa"),
            rule("Georgia", "4L-aaa", "4L-ddddd"),
            rule("Germany", "D-aaaa", "D-dddd"),
            rule("Ghana", "9G-aaa"),
            rule("Gibraltar", "VP-Gaa"),
            rule("Greece", "SX-aaa", "SX-ddd"),
            rule("Grenada", "J3-aaa"),
            rule("Guatemala", "TG-aaa"),
            rule("Guernsey", "2-aaaa"),
            rule("Guinea", "3X-aaa"),
            rule("Guinea-Bissau", "J5-aaa"),
            rule("Guyana", "8R-aaa"),
            rule("Haiti", "HH-aaa"),
            rule("Honduras", "HR-aaa"),
            rule("Hong Kong", "B-Haa", "B-Kzz", "B-Laa"),
            rule("Hungary", "HA-aaa", "HA-dddd"),
            rule("Iceland", "TF-aaa", "TF-ddd"),
            rule("India", "VT-aaa"),
            rule("Indonesia", "PK-aaa", "PK-Sddd"),
            rule("Iran", "EP-aaa"),
            rule("Iraq", "YI-aaa"),
            rule("Ireland", "EI-aaa", "EJ-aaaa"),
            rule("Isle of Man", "M-aaaa"),
            rule("Israel", "4X-aaa"),
            rule("Italy", "I-aaaa", "I-zddd"),
            rule("Ivory Coast", "TU-aaa"),
            rule("Jamaica", "6Y-aaa"),
            rule("Japan", "JAdddd", "JAddda", "JAddaa", "JA-aaaa", "JAAddd", "JRdzzz"),
            rule("Jersey", "ZJ-aaa"),
            rule("Jordan", "JY-aaa"),
            rule("Kazakhstan", "UP-aaadd"),
            rule("Kenya", "5Y-aaa"),
            rule("Kiribati", "T3-aaa"),
            rule("Kosovo", "Z6-aaa"),
            rule("Kuwait", "9K-aaa"),
            rule("Kyrgyzstan", "EX-ddd", "EX-ddddd"),
            rule("Laos", "RDPL-ddddd"),
            rule("Latvia", "YL-aaa", "LV-zdd"),
            rule("Lebanon", "OD-aaa"),
            rule("Lesotho", "7P-aaa"),
            rule("Liberia", "A8-aaa"),
            rule("Libya", "5A-aaa"),
            rule("Lithuania", "LY-aaa"),
            rule("Luxembourg", "LX-aaa"),
            rule("Macau", "B-Maa"),
            rule("Madagascar", "5R-aaa"),
            rule("Malawi", "7Q-aaa"),
            rule("Malaysia", "9M-aaa"),
            rule("Maldives", "8Q-aaa"),
            rule("Mali", "TZ-aaa"),
            rule("Malta", "9H-aaa", "9H-aaaa", "9H-aaaaa", "9H-ddd", "9H-dddd", "9H-ddddd"),
            rule("Marshall Islands", "V7-dddd"),
            rule("Mauritius", "3B-aaa"),
            rule("Mexico", "XA-aaa", "XB-aaa", "XC-aaa"),
            rule("Micronesia", "V6-aaa"),
            rule("Moldova", "ER-aaa", "ER-ddddd"),
            rule("Monaco", "3A-Maa"),
            rule("Mongolia", "JU-dddd"),
            rule("Montenegro", "4O-aaa"),
            rule("Montserrat", "VP-Maa"),
            rule("Morocco", "CN-aaa", "CNA-aa"),
            rule("Mozambique", "C9-aaa"),
            rule("Myanmar", "XY-aaa"),
            rule("Namibia", "V5-aaa"),
            rule("Nauru", "C2-aaa"),
            rule("Nepal", "9N-aaa"),
            rule("Netherlands", "PH-aaa", "PH-1aa", "PH-dad", "PH-ddd", "PH-dddd"),
            rule("Netherlands Antilles", "PJ-aaa"),
            rule("New Zealand", "ZK-aaa", "ZL-aaa", "ZM-aaa"),
            rule("Nicaragua", "YN-aaa"),
            rule("Niger", "5U-aaa"),
            rule("Nigeria", "5N-aaa"),
            rule("North Korea", "P-ddd"),
            rule("North Macedonia", "Z3-aaa", "Z3-UA-ddd"),
            rule("Norway", "LN-aaa"),
            rule("Oman", "A4O-aa", "A4O-aaa"),
            rule("Pakistan", "AP-aaa"),
            rule("Panama", "HP-ddddaaa"),
            rule("Papua New Guinea", "P2-aaa"),
            rule("Paraguay", "ZP-aaa"),
            rule("Peru", "OB-dddd"),
            rule("Philippines", "RP-dddd", "RP-adddd"),
            rule("Poland", "SP-aaa", "SN-ddaa"),
            rule("Portugal", "CR-aaa", "CS-aaa"),
            rule("Qatar", "A7-aaa"),
            rule("Romania", "YR-aaa", "YR-dddd", "YR-Ddddd"),
            rule("Russia", "RA-ddddd", "RA-ddddG", "RA-ddddA"),
            rule("Rwanda", "9XR-aa"),
            rule("Saint Helena/Ascension", "VQ-Haa"),
            rule("Saint Kitts and Nevis", "V4-aaa"),
            rule("Saint Lucia", "J6-aaa"),
            rule("Saint Vincent and the Grenadines", "J8-aaa"),
            rule("Samoa", "5W-aaa"),
            rule("San Marino", "T7-aaa", "T7-aaaa", "T7-aaaaa", "T7-ddd", "T7-dddd", "T7-ddddd"),
            rule("Sao Tome and Principe", "S9-aaa"),
            rule("Saudi Arabia", "HZ-aaa", "HZ-aad", "HZ-aadd", "HZ-aaad", "HZ-aaadd", "HZ-aaaa"),
            rule("Senegal", "6V-aaa", "6W-aaa"),
            rule("Serbia", "YU-aaa", "YU-dddd", "YU-addd", "YU-Ddddd"),
            rule("Seychelles", "S7-aaa"),
            rule("Sierra Leone", "9L-aaa"),
            rule("Singapore", "9V-aaa"),
            rule("Slovakia", "OM-aaa", "OM-aaaa", "OM-Mddd", "OM-dddd"),
            rule("Slovenia", "S5-aaa", "S5-dddd"),
            rule("Solomon Islands", "H4-aaa"),
            rule("Somalia", "6O-aaa"),
            rule("South Africa", "ZS-aaa", "ZT-Raa", "ZT-Taa", "ZU-aaa"),
            rule("South Korea", "HLdddd", "HL-Cddd"),
            rule("South Sudan", "Z8-aaa"),
            rule("Spain", "EC-aaa", "EC-aad", "EC-ddd", "EM-aaa"),
            rule("Sri Lanka", "4R-aaa"),
            rule("Sudan", "ST-aaa"),
            rule("Suriname", "PZ-aaa"),
            rule("Sweden", "SE-aaa", "SE-add"),
            rule("Switzerland", "HB-aaa", "HB-d", "HB-dd", "HB-ddd", "HB-dddd"),
            rule("Syria", "YK-aaa"),
            rule("Taiwan", "B-ddddd"),
            rule("Tajikistan", "EY-ddddd"),
            rule("Tanzania", "5H-aaa"),
            rule("Thailand", "HS-aaa", "U-add"),
            rule("Togo", "5V-aaa"),
            rule("Tonga", "A3-aaa"),
            rule("Trinidad and Tobago", "9Y-aaa"),
            rule("Tunisia", "TS-aaa"),
            rule("Turkey", "TC-aaa"),
            rule("Turkmenistan", "EZ-addd"),
            rule("Turks and Caicos", "VQ-Taa"),
            rule("Tuvalu", "T2-aaa"),
            rule("Uganda", "5X-aaa"),
            rule("Ukraine", "UR-aaa", "URddddd", "UR-aaaa"),
            rule("United Arab Emirates", "A6-aaa", "A6-aad", "DU-ddd"),
            rule("United Kingdom", "G-aaaa"),
            rule("United Nations", "4U-aaa"),
            rule("United States", "Nd", "Ndd", "Nddd", "Ndddd", "Nddddd", "Nda", "Ndda", "Nddda", "Ndddda", "Ndaa", "Nddaa", "Ndddaa"),
            rule("Uruguay", "CX-aaa"),
            rule("Uzbekistan", "UKddddd"),
            rule("Vanuatu", "YJ-aad", "YJ-aadd"),
            rule("Venezuela", "YVdddd", "YVdddT", "YVdddE", "YVOddd"),
            rule("Vietnam", "VN-dddd", "VN-addd"),
            rule("Yemen", "7O-aaa"),
            rule("Zambia", "9J-aaa"),
            rule("Zimbabwe", "Z-aaa")
    };

    private static CountryRules rule(final String countryName, final String... prefixes) {
        return new CountryRules(countryName, prefixes);
    }

    private static class CountryRules {
        private final String countryName;
        private final String[] patterns;

        public CountryRules(final String countryName, final String[] patterns) {
            this.countryName = countryName;
            this.patterns = patterns;
        }
    }
}
