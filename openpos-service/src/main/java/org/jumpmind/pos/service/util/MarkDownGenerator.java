package org.jumpmind.pos.service.util;

public class MarkDownGenerator {

    boolean autoGenerateExamples;
    String outputDir;

    StringBuilder markdown = new StringBuilder();

    public MarkDownGenerator() {
    }

    public void setAutoGenerateExamples(boolean autoGenerateExamples) {
        this.autoGenerateExamples = autoGenerateExamples;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void document(Class<?> serviceInterface) {
        this.document(serviceInterface, null, null);
    }

    public void document(Class<?> serviceInterface, Object requestBody, Object responseBody) {
        // writes markdown to the markdown buffer
    }

    public void finish(String filename) {
        // TODO write the markdown buffer to output file
        // clear the markdown buffer
    }
}
