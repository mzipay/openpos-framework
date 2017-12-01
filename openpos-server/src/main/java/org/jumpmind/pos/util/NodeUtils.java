package org.jumpmind.pos.util;

final public class NodeUtils {

    private NodeUtils() {
    }

    public static String parseStoreId(String nodeId) {
        return nodeId.substring(0, 5);
    }

    public static String parseWorkstationId(String nodeId) {
        return nodeId.substring(6);
    }

}
