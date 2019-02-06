package org.jumpmind.pos.util.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TypeCodeTest {

    public static final class MyTypeCode extends AbstractTypeCode {
        private static final long serialVersionUID = 1L;
        
        public static final MyTypeCode CODE1 = new MyTypeCode("CODE1");
        public static final MyTypeCode CODE2 = new MyTypeCode("CODE2");
        public static final MyTypeCode CODE3 = new MyTypeCode("CODE3");
        
        public static MyTypeCode of(String value) {
            return ITypeCode.make(MyTypeCode.class, value);
        }

        private MyTypeCode(String value) {
            super(value);
        }
        
    }
    
    public static final class MyAddedTypeCodes {
        public static final MyTypeCode EXTRA_TYPE_CODE_1 = MyTypeCode.of("EXTRA_TYPE_CODE_1");
        public static final MyTypeCode EXTRA_TYPE_CODE_2 = MyTypeCode.of("EXTRA_TYPE_CODE_2");
        
    }

    public static final class MyTypeCodeContainer {
        MyTypeCode myTypeCodeValue;

        public MyTypeCode getMyTypeCodeValue() {
            return myTypeCodeValue;
        }

        public void setMyTypeCodeValue(MyTypeCode myTypeCodeValue) {
            this.myTypeCodeValue = myTypeCodeValue;
        }
        
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    
    @Test
    public void testConstantCodes() {
        MyTypeCode staticCode = MyTypeCode.CODE1;
        MyTypeCode runtimeCode = MyTypeCode.of("CODE1");
        
        // Objects are equal
        assertEquals(staticCode, runtimeCode);
        
        // Objects are the same since, .of method first looks for matching static
        // member
        assertSame(staticCode, runtimeCode);
        
        assertEquals(staticCode.hashCode(), runtimeCode.hashCode());
    }

    @Test
    public void testRuntimeCodes() {
        MyTypeCode runtimeTypeCode = MyTypeCode.of("NEW_VALUE");
        assertEquals("NEW_VALUE", runtimeTypeCode.value());
        assertEquals("NEW_VALUE", runtimeTypeCode.toString());
        
        assertNotEquals(MyTypeCode.CODE1, runtimeTypeCode);
        

    }
    
    @Test
    public void testStaticAddedCodes() {
        MyTypeCode runtimeTypeCode = MyTypeCode.of("EXTRA_TYPE_CODE_1");
        
        // Objects are equal
        assertEquals(MyAddedTypeCodes.EXTRA_TYPE_CODE_1, runtimeTypeCode);
        
        // String values are equal
        assertEquals(MyAddedTypeCodes.EXTRA_TYPE_CODE_1.toString(), runtimeTypeCode.toString());
        assertEquals(MyAddedTypeCodes.EXTRA_TYPE_CODE_1.value(), runtimeTypeCode.value());
        assertEquals(MyAddedTypeCodes.EXTRA_TYPE_CODE_1.toString(), runtimeTypeCode.value());
        
        // Not the same object, however, since one is created static and the other is
        // created at runtime.
        assertNotSame(MyAddedTypeCodes.EXTRA_TYPE_CODE_1, runtimeTypeCode);
        
        // Hashcodes will be the same
        assertEquals(MyAddedTypeCodes.EXTRA_TYPE_CODE_1.hashCode(), runtimeTypeCode.hashCode());

    }


    static final class ExTypeCode extends AbstractTypeCode {
        private static final long serialVersionUID = 1L;
        
        public static final ExTypeCode CODE1 = new ExTypeCode("CODE1");
        public static final ExTypeCode CODE2 = new ExTypeCode("CODE2");
        public static final ExTypeCode CODE3 = new ExTypeCode("CODE3");
        
        public static ExTypeCode of(String value) {
            return ITypeCode.make(ExTypeCode.class, value);
        }
        
        private ExTypeCode(String value) {
            super(value);
        }

    }

    static final ExTypeCode EXTRA_TYPE_CODE = ExTypeCode.of("EXTRA_TYPE_CODE_1");

    @Test
    public void testRegistry() {

        Set<ITypeCode> values = ITypeCodeRegistry.values(ExTypeCode.class);
        assertEquals(4, values.size());
        assertTrue(values.contains(ExTypeCode.CODE1));
        assertTrue(values.contains(ExTypeCode.CODE2));
        assertTrue(values.contains(ExTypeCode.CODE3));
        assertTrue(values.contains(EXTRA_TYPE_CODE));
        
    }

    @Test
    public void testDuplicateTypeCode() {
        final ExTypeCode DUPLICATE = ExTypeCode.of("EXTRA_TYPE_CODE_1");

        Set<ITypeCode> values = ITypeCodeRegistry.values(ExTypeCode.class);
        assertEquals(4, values.size());
        assertEquals(EXTRA_TYPE_CODE, DUPLICATE);
        
    }

    @Test
    public void testIsAssignable() {
        assertTrue(ITypeCode.class.isAssignableFrom(MyTypeCode.class));
    }
    
    
    @Test
    public void testMakeTypeCodeWithNullValue() {
        assertNull(ITypeCode.make(ExTypeCode.class, null));
    }
    
    @Test(expected = NullPointerException.class)
    public void testMakeTypeCodeWithNullClass() {
        ITypeCode.make(null, "VALUE1");
    }
    
    @Test
    public void testCompare() {
        assertTrue(0 == ExTypeCode.CODE1.compareTo(ExTypeCode.CODE1));
        assertTrue(0 > ExTypeCode.CODE1.compareTo(ExTypeCode.CODE2));
        assertTrue(0 > ExTypeCode.CODE1.compareTo(ExTypeCode.CODE3));
        
        assertTrue(0 < ExTypeCode.CODE2.compareTo(ExTypeCode.CODE1));
        assertTrue(0 == ExTypeCode.CODE2.compareTo(ExTypeCode.CODE2));
        assertTrue(0 > ExTypeCode.CODE2.compareTo(ExTypeCode.CODE3));

        assertTrue(0 < ExTypeCode.CODE3.compareTo(ExTypeCode.CODE1));
        assertTrue(0 < ExTypeCode.CODE3.compareTo(ExTypeCode.CODE2));
        assertTrue(0 == ExTypeCode.CODE3.compareTo(ExTypeCode.CODE3));
        
        assertTrue(0 < EXTRA_TYPE_CODE.compareTo(ExTypeCode.CODE1));
        assertTrue(0 < EXTRA_TYPE_CODE.compareTo(ExTypeCode.CODE2));
        assertTrue(0 < EXTRA_TYPE_CODE.compareTo(ExTypeCode.CODE3));
        
    }
    
    static final class TypeCodeWithDups extends AbstractTypeCode {
        private static final long serialVersionUID = 1L;
        
        public static final TypeCodeWithDups CODE1 = new TypeCodeWithDups("CODE1");
        public static final TypeCodeWithDups CODE1_DUP = new TypeCodeWithDups("CODE1");
        public static final TypeCodeWithDups CODE1_DUP_2 = TypeCodeWithDups.of("CODE1");
        
        
        public static TypeCodeWithDups of(String value) {
            return ITypeCode.make(TypeCodeWithDups.class, value);
        }

        private TypeCodeWithDups(String value) {
            super(value);
        }

    }
    
    @Test
    public void testSameValueDifferentTypes() {
        assertNotEquals(TypeCodeWithDups.CODE1, ExTypeCode.CODE1);
    }

    @Test
    public void testSameValueDups() {
        // All should be equal since they have the same string value
        assertEquals(TypeCodeWithDups.CODE1, TypeCodeWithDups.CODE1_DUP);
        assertEquals(TypeCodeWithDups.CODE1, TypeCodeWithDups.CODE1_DUP_2);
        assertEquals(TypeCodeWithDups.CODE1_DUP, TypeCodeWithDups.CODE1_DUP_2);
        
        // None of them should be the same object because the first two were
        // created with new and when the third is created using .of, there will be two other
        // static fields with duplicate values and since it's indeterminate which
        // one should be picked, a new instance will be returned.
        assertNotSame(TypeCodeWithDups.CODE1, TypeCodeWithDups.CODE1_DUP);
        assertNotSame(TypeCodeWithDups.CODE1, TypeCodeWithDups.CODE1_DUP_2);
        assertNotSame(TypeCodeWithDups.CODE1_DUP, TypeCodeWithDups.CODE1_DUP_2);
        
        Set<ITypeCode> values = ITypeCodeRegistry.values(TypeCodeWithDups.class);
        assertEquals(1, values.size());
    }
    
    public static final String EXPECTED_MyTypeCode_CODE1_JSON = 
        "{\"value\":\"CODE1\",\"class\":\"org.jumpmind.pos.util.model.TypeCodeTest$MyTypeCode\"}";
        
    @Test 
    public void testJsonSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyTypeCodeContainer container = new MyTypeCodeContainer();
        container.setMyTypeCodeValue(MyTypeCode.CODE1);
        
        String json = mapper.writeValueAsString(container);
        assertEquals("{\"myTypeCodeValue\":" + EXPECTED_MyTypeCode_CODE1_JSON + "}" , json);
    }

    @Test 
    public void testJsonDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyTypeCode myTypeCodeReadFromJson = mapper.readValue(EXPECTED_MyTypeCode_CODE1_JSON, MyTypeCode.class);
        assertEquals(MyTypeCode.CODE1, myTypeCodeReadFromJson);
    }
    
}
