package org.jumpmind.pos.management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "localtest")
public class OpenposManagementServerTest {

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
    
    /*
    @Test
    public void testRequest() {
        String url = String.format("http://localhost:%d/discover/url?deviceId=%s", port, "00000-001");
        assertThat(restTemplate.getForObject(url, String.class)).isEqualTo("wss://server/00000-001:6140");
    }
    */
    
}