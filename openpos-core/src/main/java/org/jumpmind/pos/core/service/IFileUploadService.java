package org.jumpmind.pos.core.service;

import java.io.InputStream;
import java.util.function.BiConsumer;

public interface IFileUploadService {
    public void registerNodeUploadHandler(String nodeId, String context, BiConsumer<String, InputStream> handler);
    public void registerNodeUploadHandler(IFileUploadHandler handler);
}
