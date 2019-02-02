package org.jumpmind.pos.util.model;


/**
 * Convenience abstract implementation for the ITypeCode interface.  Makes it
 * possible to implement a new type code with just a couple of methods and 
 * the static type code values.  E.g., 
 * 
 * <pre>
 *  public static final class MyTypeCode extends AbstractTypeCode {
 *      private static final long serialVersionUID = 1L;
 *         
 *      public static final MyTypeCode CODE1 = new MyTypeCode("CODE1");
 *      public static final MyTypeCode CODE2 = new MyTypeCode("CODE2");
 *      public static final MyTypeCode CODE3 = new MyTypeCode("CODE3");
 *         
 *      public static MyTypeCode of(String value) {
 *          return ITypeCode.make(MyTypeCode.class, value);
 *      }
 *  
 *      private MyTypeCode(String value) {
 *          super(value);
 *      }
 *          
 *  }
 * </pre>  
 *
 */
public abstract class AbstractTypeCode implements ITypeCode {

    private static final long serialVersionUID = 1L;
    
    private String value;

    protected AbstractTypeCode(String value) {
        if (value == null) {
            throw new NullPointerException(String.format("Cannot have null value for instance of %s", this.getClass().getName()));
        }
        
        this.value = value;
        ITypeCodeRegistry.register(this);
    }

    @Override
    public String value() {
        return this.toString();
    }

    @Override
    public String toString() {
        return this.value;
    }
    
    @Override
    public boolean equals(Object other) {
        return this.isEqual(other);
    }

    @Override
    public int hashCode() {
        return this.hash();
    }
    
}
