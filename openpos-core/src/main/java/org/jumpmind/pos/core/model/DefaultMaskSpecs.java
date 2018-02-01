package org.jumpmind.pos.core.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jumpmind.pos.core.model.GenericMaskSpec.ImmutableGenericMaskSpec;
import org.jumpmind.pos.core.model.IMaskSpec.PipeName;
import org.jumpmind.pos.core.model.MaskElement.MaskElementType;

public class DefaultMaskSpecs {
    public static final ImmutableGenericMaskSpec DEFAULT_PHONE_SPEC = new ImmutableGenericMaskSpec(true, 
            new MaskElement(MaskElementType.String, "("),
            new MaskElement(MaskElementType.RegExp, "[2-9]"),
            new MaskElement(MaskElementType.RegExp, "[0-8]"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.String, ")"),
            new MaskElement(MaskElementType.String, " "),
            new MaskElement(MaskElementType.RegExp, "[2-9]"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.String, "-"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d")
        );
    private static final MaskElement PRICECODE_CHAR_MASK = new MaskElement(MaskElementType.RegExp, "[b-df-hj-mB-DF-HJ-M]");
    public static final ImmutableGenericMaskSpec DEFAULT_PRICECODE_SPEC = new ImmutableGenericMaskSpec(false, PipeName.ToUpper,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK,
            PRICECODE_CHAR_MASK
        );
    
    // b-m,e,i
    protected final static Map<String,ImmutableGenericMaskSpec> countryCodeToPhoneSpec = new HashMap<>();
    static {
        countryCodeToPhoneSpec.put(Locale.US.getCountry(), DEFAULT_PHONE_SPEC);
        countryCodeToPhoneSpec.put(Locale.CANADA.getCountry(), new ImmutableGenericMaskSpec(true, 
            new MaskElement(MaskElementType.String, "("),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.String, ")"),
            new MaskElement(MaskElementType.String, " "),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.String, "-"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d"),
            new MaskElement(MaskElementType.RegExp, "\\d")
        ));
    }
    
    public static ImmutableGenericMaskSpec defaultPhoneSpecForCurrentLocale() {
        return defaultPhoneSpecForCountry(Locale.getDefault().getCountry());
    }
    
    public static ImmutableGenericMaskSpec defaultPhoneSpecForCountry(String countryCode) {
        ImmutableGenericMaskSpec returnSpec = null;
        if (null != countryCode) {
            returnSpec = countryCodeToPhoneSpec.get(countryCode);
            if (returnSpec == null) {
                returnSpec = DEFAULT_PHONE_SPEC;
            }
        }
        
        return returnSpec;
    }
}
