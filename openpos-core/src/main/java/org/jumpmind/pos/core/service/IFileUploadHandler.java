package org.jumpmind.pos.core.service;

import java.io.File;
import java.io.InputStream;
import java.util.function.BiConsumer;

public interface IFileUploadHandler {
    
    BiConsumer<String, InputStream> getUploadHandler();
    String getUploadContext();
    String getNodeId();
    void setUploadDirectory(File file);
    File getUploadDirectory();
}
