package org.jumpmind.pos.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientConnectInfo {

    private String host;
    private Integer port;
    private String webServiceBaseUrl;
    private String secureWebServiceBaseUrl;
    private String webSocketBaseUrl;
    private String secureWebSocketBaseUrl;
}
