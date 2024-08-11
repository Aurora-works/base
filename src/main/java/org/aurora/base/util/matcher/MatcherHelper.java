package org.aurora.base.util.matcher;

import java.util.regex.Pattern;

public class MatcherHelper {

    /**
     * 手机号校验
     */
    public static void checkMobilePhoneNumber(String mobilePhoneNumber) {
        Pattern pattern = Pattern.compile("^1[0-9]{10}$");
        if (!pattern.matcher(mobilePhoneNumber).matches()) {
            throw new IllegalArgumentException();
        }
    }
}
