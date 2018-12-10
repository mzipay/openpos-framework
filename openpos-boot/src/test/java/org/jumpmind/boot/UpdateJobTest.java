package org.jumpmind.boot;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.jumpmind.boot.BootConstants.RESPONSE_CODE_UPDATE_AVAILABLE;
import static org.jumpmind.boot.BootConstants.RESPONSE_CODE_UP_TO_DATE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class UpdateJobTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void setup() throws Exception {
        Path path = Paths.get("work/app");
        path.toFile().mkdirs();
        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        Files.createDirectories(path);
    }
    
    protected UpdateJob doInitialInstall() throws Exception {
        stubFor(get(urlEqualTo("/check/deviceid/test001/version/")).willReturn(status(RESPONSE_CODE_UPDATE_AVAILABLE)));

        stubFor(get(urlEqualTo("/update/deviceid/test001/version/"))
                .willReturn(okJson(new String(Files.readAllBytes(Paths.get("src/test/resources/deploy1.json"))))));
        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/1.0.0/file1.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file1.txt")))));

        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/1.0.0/conf/file2.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file2.txt")))));
        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/1.0.0/lib/file3.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file2.txt")))));

        BootConfig bootConfig = new BootConfig(new File("src/test/resources/boot-test.properties"));
        AppConfig appConfig = new AppConfig(bootConfig);
        UpdateJob job = new UpdateJob(bootConfig, appConfig);
        assertTrue(job.update());
        return job;

    }

    @Test
    public void testInitialRun() throws Exception {
        UpdateJob job = doInitialInstall();
        
        stubFor(get(urlEqualTo("/check/deviceid/test001/version/1.0.0")).willReturn(status(RESPONSE_CODE_UP_TO_DATE)));
        
        assertFalse(job.update());
        
        // TODO additional assertations for the correct files
    }
    
    @Test
    public void testUpgrade() throws Exception {
        UpdateJob job = doInitialInstall();
        
        stubFor(get(urlEqualTo("/check/deviceid/test001/version/1.0.0")).willReturn(status(RESPONSE_CODE_UPDATE_AVAILABLE)));

        stubFor(get(urlEqualTo("/update/deviceid/test001/version/1.0.0"))
                .willReturn(okJson(new String(Files.readAllBytes(Paths.get("src/test/resources/deploy2.json"))))));
        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/2.0.0/file1.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file1.txt")))));

        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/2.0.0/conf/file2.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file2.txt")))));
        
        stubFor(get(urlEqualTo("/download/deviceid/test001/version/2.0.0/lib/file4.txt"))
                .willReturn(aResponse().withBody(Files.readAllBytes(Paths.get("src/test/resources/file2.txt")))));
        
        assertTrue(job.update());
    }
    
    @Test
    public void testRestartUpgrade() throws Exception {
        
    }

}
