package org.jumpmind.pos.translate;

import java.text.SimpleDateFormat;

public interface ILegacyDateTimeUtilities {
    /** Returns a 'normalized' version of the current locale date format where month is a 2 digit month, day of month is 2 digits,
     * and year is 4 digits.*/
    SimpleDateFormat getNormalizedLocaleDateFormat();
}
