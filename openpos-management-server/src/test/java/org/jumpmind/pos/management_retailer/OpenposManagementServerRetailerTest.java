package org.jumpmind.pos.management_retailer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import org.jumpmind.pos.management.ClientConnectInfo;
import org.jumpmind.pos.management.DiscoveryServiceController;
import org.jumpmind.pos.management.OpenposManagementServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={OpenposManagementServer.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "bltest")
@EnableScheduling
public class OpenposManagementServerRetailerTest {

    @Autowired
    private DiscoveryServiceController discoveryController;

    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    /*
    @Autowired
    private MockMvc mvc;

    
    @Test
    public void testWebService() throws Exception {
        mvc.perform(get("/discover/url?deviceId=00000-001"))
            .andExpect(status().isOk())
            .andExpect(content().string("wss://server/00000-001:6140"));
    }
    */
    @Test
    public void contextLoads() throws Exception {
        assertThat(discoveryController).isNotNull();
    }
    
    @Test
    public void testRequest() {
        String url = String.format("http://localhost:%d/discover/url?deviceId=%s", port, "00000-001");
        ClientConnectInfo ci = restTemplate.getForObject(url, ClientConnectInfo.class);
        assertThat(ci.getHost()).isEqualTo("localhost");
        //).isEqualTo("wss://server/00000-001:6140");
    }
    
}