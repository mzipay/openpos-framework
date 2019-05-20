package org.jumpmind.pos.util;

import java.util.*;

public class DriversLicenseUtils {

    private static List<String> supportedStates;

    public final static String HASH_MASK = "*********";

    private final static String HASH_MASK_PATTERN = "^" + HASH_MASK.replaceAll("\\*", "\\\\*") + "$";

    private static Map<String, String> rules;
    static {
        rules = new HashMap<>();
        rules.put("AL", "^\\d{1,7}$");
        rules.put("AK", "^\\d{1,7}$");
        rules.put("AZ", "^[a-zA-Z]\\d{8,9}$");
        rules.put("AR", "^\\d{4,9}$");
        rules.put("CA", "^[a-zA-Z]\\d{7}$");
        rules.put("CO", "^\\d{9}$|^[a-zA-Z]\\d{3,6}$|^[a-zA-Z]{2}\\d{2,5}$");
        rules.put("CT", "^\\d{9}$");
        rules.put("DE", "^\\d{1,7}$");
        rules.put("DC", "^\\d{7}$|^\\d{9}$");
        rules.put("FL", "^[a-zA-Z]\\d{12}$");
        rules.put("GA", "^\\d{7,9}$");
        rules.put("HI", "^[a-zA-Z]\\d{8,9}$");
        rules.put("ID", "^[a-zA-Z]{2}\\d{6}[a-zA-Z]$|^\\d{9}$");
        rules.put("IL", "^[a-zA-Z]\\d{11,12}$");
        rules.put("IN", "^[a-zA-Z]\\d{9}$|^\\d{9,10}$");
        rules.put("IA", "^\\d{9}$|^\\d{3}[a-zA-Z]{2}\\d{4}$");
        rules.put("KS", "^[a-zA-Z]\\d[a-zA-Z]\\d[a-zA-Z]$|^[a-zA-Z]\\d{8}$|^\\d{9}$");
        rules.put("KY", "^[a-zA-Z]\\d{8,9}$|^\\d{9}$");
        rules.put("LA", "^\\d{1,9}$");
        rules.put("ME", "^\\d{7,8}$|^\\d{7}[a-zA-Z]$");
        rules.put("MD", "^[a-zA-Z]\\d{12}$");
        rules.put("MA", "^[a-zA-Z]\\d{8}$|^\\d{9}$");
        rules.put("MI", "^[a-zA-Z]\\d{10}$|^[a-zA-Z]\\d{12}$");
        rules.put("MN", "^[a-zA-Z]\\d{12}$");
        rules.put("MS", "^\\d{9}$");
        rules.put("MO", "^[a-zA-Z]\\d{5,9}$|^[a-zA-Z]\\d{6}R$|^\\d{8}[a-zA-Z]{2}$|^\\d{9}[a-zA-Z]$|^\\d{9}$");
        rules.put("MT", "^[a-zA-Z]\\d{8}$|^\\d{9}$|^\\d{13,14}$");
        rules.put("NE", "^[a-zA-Z]\\d{6,8}$");
        rules.put("NV", "^\\d{9,10}$|^\\d{12}$|^X\\d{8}$");
        rules.put("NH", "^\\d{2}[a-zA-Z]{3}\\d{5}$");
        rules.put("NJ", "^[a-zA-Z]\\d{14}$");
        rules.put("NM", "^\\d{8,9}$");
        rules.put("NY", "^[a-zA-Z]\\d{7}$|^[a-zA-Z]\\d{18}$|^\\d{8,9}$|^\\d{16}$|^[a-zA-Z]{8}$");
        rules.put("NC", "^\\d{1,12}$");
        rules.put("ND", "^[a-zA-Z]{3}\\d{6}$|^\\d{9}$");
        rules.put("OH", "^[a-zA-Z]\\d{4,8}$|^[a-zA-Z]{2}\\d{3,7}$|^\\d{8}$");
        rules.put("OK", "^[a-zA-Z]\\d{9}$|^\\d{9}$");
        rules.put("OR", "^\\d{1,9}$");
        rules.put("PA", "^\\d{8}$");
        rules.put("RI", "^\\d{7}$|^[a-zA-Z]\\d{6}$");
        rules.put("SC", "^\\d{5,11}$");
        rules.put("SD", "^\\d{6,10}$|^\\d{12}$");
        rules.put("TN", "^\\d{7,9}$");
        rules.put("TX", "^\\d{7,8}$");
        rules.put("UT", "^\\d{4,10}$");
        rules.put("VT", "^\\d{8}$|^\\d{7}A$");
        rules.put("VA", "^[a-zA-Z]\\d{8,11}$|^\\d{9}$");
        rules.put("WA", "^[a-zA-Z]{1}\\*{4}[a-zA-Z]{2}\\d{3}[a-zA-Z0-9]{2}$|^" +
                "[a-zA-Z]{2}\\*{3}[a-zA-Z]{2}\\d{3}[a-zA-Z0-9]{2}$|^" +
                "[a-zA-Z]{3}\\*{2}[a-zA-Z]{2}\\d{3}[a-zA-Z0-9]{2}$|^" +
                "[a-zA-Z]{4}\\*{1}[a-zA-Z]{2}\\d{3}[a-zA-Z0-9]{2}$|^" +
                "[a-zA-Z]{5}[a-zA-Z]{2}\\d{3}[a-zA-Z0-9]{2}$");
        rules.put("WV", "^\\d{7}$|^[a-zA-Z]{1,2}\\d{5,6}$");
        rules.put("WI", "^[a-zA-Z]\\d{13}$");
        rules.put("WY", "^\\d{9,10}$");
        rules.put("HASHED", HASH_MASK_PATTERN);
        supportedStates = new ArrayList<>();
        supportedStates = new ArrayList<>(rules.keySet());
        Collections.sort(supportedStates);
    }

    private DriversLicenseUtils() {

    }

    public static boolean validate(String number, String stateCode) {
        return (number.matches(rules.get(stateCode)));
    }

    public static List<String> getSupportedStates() {return supportedStates;}

    public static String getRule(String stateCode) {
        return rules.get(stateCode);
    }
}
