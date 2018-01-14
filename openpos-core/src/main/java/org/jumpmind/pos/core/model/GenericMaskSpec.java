package org.jumpmind.pos.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Provides a means for creation of a field mask on the server-side which conforms to the masking functionality provided by the
 * text-mask project.  
 * 
 * @see <a href="https://github.com/text-mask/text-mask">Text Mask main docs</a>
 * @see <a href="https://github.com/text-mask/text-mask/blob/master/componentDocumentation.md#mask">Text Mask <code>mask</code> 
 * array</a> for element values that can be used in the <code>runtimeMask</code> list.</a>
 * 
 */

public class GenericMaskSpec implements IMaskSpec {
    private static final long serialVersionUID = 1L;

    private MaskSpecType type = MaskSpecType.GenericMask;
    
    private boolean guide = false;
    private List<MaskElement> mask;

    public static GenericMaskSpec get() {
        return new GenericMaskSpec();
    }
    
    /**
     * Returns a mask that can be used to enforce a maximum length on a field with the stipulation that all of the
     * characters in the mask conform to the type of the given MaskElement.
     */
    public static GenericMaskSpec maxLengthMask(MaskElement elementToRepeat, int maxLen) {
        GenericMaskSpec returnSpec = new GenericMaskSpec();
        for (int i = 0; i < maxLen; i++) {
            returnSpec.addMaskElement(elementToRepeat);
        }
        return returnSpec;
    }
    
    public GenericMaskSpec() {
    }
    
    public GenericMaskSpec(MaskElement... maskElems) {
        this(false, maskElems);
    }
    
    public GenericMaskSpec(boolean guide, MaskElement... maskElems) {
        this.guide = guide;
        mask = Arrays.asList(maskElems);
    }
    
    public GenericMaskSpec guide(boolean guide) {
        this.setGuide(guide);
        return this;
    }

    @Override
    public MaskSpecType getType() {
        return this.type;
    }

    public boolean isGuide() {
        return guide;
    }

    public void setGuide(boolean guide) {
        this.guide = guide;
    }
    
    
    public GenericMaskSpec addMaskElement(MaskElement maskElem) {
        if (this.mask == null) {
            this.mask = new ArrayList<>();
        }
        
        this.mask.add(maskElem);
        return this;
    }
    
    public List<MaskElement> getMask() {
        return mask;
    }

    public void setMask(List<MaskElement> mask) {
        this.mask = mask;
    }
    
    public static class ImmutableGenericMaskSpec extends GenericMaskSpec {
        private static final long serialVersionUID = 1L;

        public ImmutableGenericMaskSpec(MaskElement... maskElems) {
            super(false, maskElems);
        }
        
        public ImmutableGenericMaskSpec(boolean guide, MaskElement... maskElems) {
            super(guide, maskElems);
        }
        
        @Override
        public ImmutableGenericMaskSpec guide(boolean guide) {
            return this;
        }
        
        @Override
        public void setMask(List<MaskElement> mask) {
            // don't allow changing the mask elements
        }
        
        @Override
        public List<MaskElement> getMask() {
            return Collections.unmodifiableList(super.getMask());
        }
        
        @Override
        public ImmutableGenericMaskSpec addMaskElement(MaskElement maskElem) {
            // add not supported
            return this;
        }

        @Override
        public void setGuide(boolean guide) {
            // can't change the guide setting
        }
        
    }

}
