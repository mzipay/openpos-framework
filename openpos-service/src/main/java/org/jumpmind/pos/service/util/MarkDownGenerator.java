package org.jumpmind.pos.service.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.jumpmind.pos.persist.ColumnDef;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

@SuppressWarnings("deprecation")
public class MarkDownGenerator {

    private final static String LINE_SKIP = "\n\n";
    private final static String CODE_BLOCK = "```";
    private final static String TABLE_DIVISION = " | ";
    private final static String HORIZONTAL_SEPARATOR = "---";

    boolean autoGenerateExamples = true;
    String outputDir = "";

    StringBuilder markdown = new StringBuilder();

    Set<Class<?>> models = new HashSet<>();

    public MarkDownGenerator() {
        markdown.append(MarkDownGeneratorConstants.SERVICES_HEADING + "\n");
    }

    public void setAutoGenerateExamples(boolean autoGenerateExamples) {
        this.autoGenerateExamples = autoGenerateExamples;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void document(Class<?> serviceInterface) {

        boolean requestFound;
        Parameter requestParameter = null;
        Api api = serviceInterface.getAnnotation(io.swagger.annotations.Api.class);
        String tags[] = api != null ? api.tags() : new String[] { serviceInterface.getSimpleName() };
        markdown.append(MarkDownGeneratorConstants.SERVICE_NAME_HEADING_START + tags[0] + "\n");
        if (api != null && !api.description().equals("")) {
            markdown.append("*" + api.description() + "*");
        }
        markdown.append(LINE_SKIP);

        Method methods[] = serviceInterface.getMethods();

        for (Method method : methods) {
            requestFound = false;
            if (method.isAnnotationPresent(RequestMapping.class)) {

                // print new op table
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                markdown.append(MarkDownGeneratorConstants.OPERATION_HEADING + getOperationName(method));
                markdown.append(LINE_SKIP);
                
                ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                markdown.append(apiOperation != null ? apiOperation.value() : "");
                markdown.append(LINE_SKIP);
                
                markdown.append(MarkDownGeneratorConstants.OPERATION_TABLE_HEADING + "\n");
                markdown.append(MarkDownGeneratorConstants.OPERATION_TABLE_DIVIDER + "\n");
                markdown.append(TABLE_DIVISION);
                if (mapping.path().length > 0) {
                    markdown.append("`" + mapping.path()[0] + "`");
                } else if (mapping.value().length > 0) {
                    markdown.append("`" + mapping.value()[0] + "`");
                }
                markdown.append(TABLE_DIVISION);
                markdown.append(Arrays.toString(mapping.method()));
                markdown.append(TABLE_DIVISION);

                // find request
                requestFound = false;
                Parameter parameters[] = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(RequestBody.class) && !requestFound) {
                        requestFound = true;
                        requestParameter = parameter;
                        if (!models.contains(parameter.getType())) {
                            models.add(parameter.getType());
                        }
                    }
                }

                // print request
                if (requestFound) {
                    markdown.append(createModelLink(requestParameter.getType().getSimpleName()));
                    markdown.append(TABLE_DIVISION);
                } else {
                    markdown.append(MarkDownGeneratorConstants.NO_VALUE);
                    markdown.append(TABLE_DIVISION);
                }

                // print response
                if (method.isAnnotationPresent(ResponseBody.class)) {
                    Class<?> returnType = method.getReturnType();
                    if (canComplexExampleBeCreated(returnType)) {
                        models.add(returnType);
                        markdown.append(createModelLink(returnType.getSimpleName()));
                    } else {
                        markdown.append(returnType.getSimpleName());
                    }
                    markdown.append(TABLE_DIVISION);
                } else {
                    markdown.append(MarkDownGeneratorConstants.NO_VALUE);
                    markdown.append(TABLE_DIVISION);
                }

                markdown.append(LINE_SKIP);

                // print examples
                if (requestFound) {
                    markdown.append(MarkDownGeneratorConstants.REQUEST_EXAMPLE_HEADING + "\n");
                    markdown.append(CODE_BLOCK + "\n");
                    markdown.append(scanAndGenerateExample(requestParameter.getType()));
                    markdown.append("\n").append(CODE_BLOCK).append("\n");
                }

                if (method.isAnnotationPresent(ResponseBody.class)) {
                    markdown.append(MarkDownGeneratorConstants.RESPONSE_EXAMPLE_HEADING + "\n");
                    markdown.append(CODE_BLOCK + "\n");
                    markdown.append(scanAndGenerateExample(method.getReturnType()));
                    markdown.append("\n").append(CODE_BLOCK).append("\n");
                }
            }
        }
        markdown.append(HORIZONTAL_SEPARATOR);
        markdown.append(LINE_SKIP);
    }

    private String getOperationName(Method method) {
        StringBuilder builder = new StringBuilder();
        String[] words = method.getName().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        for (String word : words) {
            builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }

    private String createModelLink(String modelName) {
        return createModelLink(modelName, modelName, "");
    }

    private String createModelLink(String modelName, String format) {
        return createModelLink(modelName, modelName, format);
    }

    private String createModelLink(String modelName, String header, String format) {
        return "[" + format + modelName + format + "](#" + header.toLowerCase() + ")";
    }

    private String scanAndGenerateExample(Class<?> clazz) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(dateFormat);
        mapper.setSerializationInclusion(Include.ALWAYS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            if (canComplexExampleBeCreated(clazz)) {
                Object value = clazz.newInstance();
                setInstanceFields(value);
                return mapper.writeValueAsString(value);
            } else {
                Object value = generateExampleField(clazz);
                if (value != null) {
                    return mapper.writeValueAsString(value);
                } else {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Object generateExampleField(Class<?> clazz) {
        if (clazz.getName().equals("int") || clazz.getName().equals("long") || clazz.getName().equals("double")
                || clazz.getName().equals("float")) {
            return 1;
        } else if (clazz.getName().equals("boolean")) {
            return true;
        } else if (clazz.equals(Date.class)) {
            return new Date();
        } else if (clazz.equals(BigDecimal.class)) {
            return new BigDecimal("1.00");
        } else if (clazz.isEnum()) {
            try {
                Method values = clazz.getMethod("values");
                return Array.get(values.invoke(clazz), 0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean canComplexExampleBeCreated(Class<?> fieldType) {
        return !fieldType.isEnum() && !fieldType.isPrimitive() && !fieldType.isArray()
                && fieldType.getPackage().getName().startsWith("org.jumpmind");
    }

    private void setInstanceFields(Object instance) {
        Class<?> type = instance.getClass();
        while (type != null && !type.equals(Object.class)) {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (!Modifier.isFinal(field.getModifiers())) {
                        field.setAccessible(true);
                        Class<?> fieldType = field.getType();
                        if (field.isAnnotationPresent(ApiModelProperty.class)
                                && (fieldType.isPrimitive() || fieldType.equals(String.class))) {
                            ApiModelProperty apiProperty = field.getAnnotation(ApiModelProperty.class);
                            String example = apiProperty.example();
                            field.set(instance, example);
                        } else if (canComplexExampleBeCreated(fieldType) && !field.isAnnotationPresent(JsonIgnore.class)) {
                            try {
                                Object fieldValue = fieldType.newInstance();
                                setInstanceFields(fieldValue);
                                field.setAccessible(true);
                                field.set(instance, fieldValue);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            }

                        } else if (!Modifier.isFinal(field.getModifiers())) {
                            Object value = generateExampleField(fieldType);
                            if (value != null) {
                                field.set(instance, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            type = type.getSuperclass();
        }
    }

    private String buildModelMarkdown(Class<?> model) {
        StringBuilder string = new StringBuilder();
        Field fields[] = model.getDeclaredFields();
        string.append(MarkDownGeneratorConstants.MODEL_HEADING_START + model.getSimpleName() + "\n");
        ApiModel apiModel = model.getAnnotation(ApiModel.class);
        if (apiModel != null) {
            string.append("*" + apiModel.description() + "*");
        }
        string.append(LINE_SKIP);
        string.append(MarkDownGeneratorConstants.MODEL_TABLE_HEADING + "\n");
        string.append(MarkDownGeneratorConstants.MODEL_TABLE_DIVIDER + "\n");
        for (Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers()) && !field.isAnnotationPresent(JsonIgnore.class)) {
                string.append(TABLE_DIVISION);
                string.append(field.getName());
                string.append(TABLE_DIVISION);
                ApiModelProperty modelProperty = field.getAnnotation(ApiModelProperty.class);
                ColumnDef columnDef = field.getAnnotation(ColumnDef.class);
                if (canComplexExampleBeCreated(field.getType())) {
                    string.append(createModelLink(formatTypeName(field.getGenericType().getTypeName()), "`"));
                    string.append(TABLE_DIVISION);
                } else if (getModelNameFromParameterizedType(field) != null) {
                    string.append(createModelLink(formatTypeName(field.getGenericType().getTypeName()),
                            formatTypeName(getModelNameFromParameterizedType(field)), "`"));
                    string.append(TABLE_DIVISION);
                } else {
                    string.append("`" + formatTypeName(field.getGenericType().getTypeName()) + "`");
                    string.append(TABLE_DIVISION);
                }
                if (modelProperty != null) {
                    string.append(modelProperty.value());
                }
                if (columnDef != null) {
                    string.append(columnDef.description());
                }
                string.append(TABLE_DIVISION + "\n");
            }
        }
        string.append(LINE_SKIP);
        return string.toString();
    }

    private boolean fieldHasModelAsParameter(Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            Type types[] = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            for (Type type : types) {
                try {
                    Class<?> clazz = Class.forName(type.getTypeName());
                    if (canComplexExampleBeCreated(clazz)) {
                        return true;
                    }
                } catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            }
        }
        return false;
    }

    private String getModelNameFromParameterizedType(Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            Type types[] = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            for (Type type : types) {
                try {
                    Class<?> clazz = Class.forName(type.getTypeName());
                    if (canComplexExampleBeCreated(clazz)) {
                        return clazz.getName();
                    }
                } catch (ClassNotFoundException e) {
                    // e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String formatTypeName(String string) {
        StringBuilder formattedString = new StringBuilder();
        StringBuilder token = new StringBuilder();
        char str[] = string.toCharArray();
        for (int i = 0; i < str.length; i++) {
            if (str[i] == '.') {
                token.setLength(0);
            } else if (i == str.length - 1 || str[i] == '<' || str[i] == ',' || str[i] == '>') {
                token.append(str[i]);
                formattedString.append(token.toString());
                token.setLength(0);
            } else {
                token.append(str[i]);
            }
        }
        return formattedString.toString();
    }

    public void finish(String filename) throws IOException {
        BufferedWriter writer = null;

        // print models
        if (!models.isEmpty()) {
            markdown.append(MarkDownGeneratorConstants.MODELS_HEADING + "\n");

            List<Class<?>> sorted = new ArrayList<>(models);
            for (Class<?> model : models) {
                for (Field field : model.getDeclaredFields()) {
                    if (!Modifier.isFinal(field.getModifiers()) && !field.isAnnotationPresent(JsonIgnore.class)) {
                        Class<?> toAdd = null;
                        if (canComplexExampleBeCreated(field.getType())) {
                            toAdd = field.getType();
                        } else if (fieldHasModelAsParameter(field)) {
                            try {
                                toAdd = Class.forName(getModelNameFromParameterizedType(field));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        
                        if (toAdd != null && !sorted.contains(toAdd)) {
                            sorted.add(toAdd);
                        }
                    }
                }
            }

            Collections.sort(sorted, new Comparator<Class<?>>() {
                public int compare(Class<?> o1, Class<?> o2) {
                    return o1.getSimpleName().compareTo(o2.getSimpleName());
                };
            });

            for (Class<?> model : sorted) {
                markdown.append(buildModelMarkdown(model));
            }

        }

        if (outputDir != null)
            writer = new BufferedWriter(new FileWriter(outputDir + filename));
        else
            writer = new BufferedWriter(new FileWriter(filename));
        writer.write(markdown.toString());
        writer.close();
    }
}
