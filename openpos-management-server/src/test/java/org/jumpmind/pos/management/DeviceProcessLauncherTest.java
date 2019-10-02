package org.jumpmind.pos.management;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "localtest")
@EnableScheduling
public class DeviceProcessLauncherTest {
    @Autowired
    DeviceProcessLauncher launcher;
    
    static String[] classpathEntriesFromConfig;
    
    @Before
    public void setUp() throws Exception {
        if (classpathEntriesFromConfig == null) {
            classpathEntriesFromConfig = launcher.config.getDeviceProcess().getClasspathEntries();
        }
        launcher.config.getDeviceProcess().setProcessPort(OpenposManagementServerConfig.DeviceProcess.AUTO_PORT_ALLOCATION);
        launcher.config.getDeviceProcess().setJavaRemoteDebugPort(null);
        launcher.config.getDeviceProcess().setJavaRemoteDebugArgTemplate(OpenposManagementServerConfig.DeviceProcess.DEFAULT_JAVA_REMOTE_DEBUG_ARG_TEMPLATE);
        launcher.config.getDeviceProcess().setProcessPortArgTemplate(OpenposManagementServerConfig.DeviceProcess.DEFAULT_PROCESS_PORT_ARG_TEMPLATE);
    }

    @Test
    public void testResolveJavaExecutablePath() {
        assertThat(launcher.resolveJavaExecutablePath()).isNotNull().contains(File.separator + "java");
    }

    @Test
    public void testClasspathEntriesFromConfig() {
        launcher.config.getDeviceProcess().setClasspathEntries(classpathEntriesFromConfig);
        assertThat(launcher.config.getDeviceProcess().getClasspathEntries()).hasSize(1);
        String cp = launcher.constructClasspath();
        assertThat(cp).isEqualTo(".");
    }
    @Test
    public void testEmptyClasspath() {
        launcher.config.getDeviceProcess().setClasspathEntries(new String[0]);
        assertThat(launcher.constructClasspath()).isEmpty();
    }
    
    @Test
    public void testMultipleClasspathEntries() {
        launcher.config.getDeviceProcess().setClasspathEntries(new String[]{"c:\\Program Files\\app\\jars", "entry 2", "/users/joe/abc def/g.jar"});
        String cp = launcher.constructClasspath();
        assertThat(cp).isEqualTo("c:\\Program Files\\app\\jars" + File.pathSeparator + "entry 2" + File.pathSeparator + "/users/joe/abc def/g.jar");
    }

    @Test
    public void testAllocatePortUsingAUTO() {
        launcher.config.getDeviceProcess().setProcessPort("AUTO");
        
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isNotNull();
    }
    
    @Test
    public void testAllocatePortUsingInteger() {
        int availablePort = SocketUtils.findAvailableTcpPort();
        launcher.config.getDeviceProcess().setProcessPort(availablePort + "");
        
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isEqualTo(availablePort);
    }
    
    @Test
    public void testAllocatePortUsingRange() {
        launcher.config.getDeviceProcess().setProcessPort("5000-5500");
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isNotNull().isGreaterThanOrEqualTo(5000).isLessThanOrEqualTo(5500);
    }

    @Test
    public void testAllocatePortUsingCommaSepIntegerList() {
        String[] portArray = {" 5000","5500","6000","6500 ","7000", " 7500"};
        List<String> portList = Arrays.asList(portArray);
        List<Integer> portIntList = portList.stream().map(p -> new Integer(p.trim())).collect(Collectors.toList());
        
        launcher.config.getDeviceProcess().setProcessPort(String.join(",", portList));
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isIn(portIntList);
    }

    @Test
    public void testAllocatePortUsingMultipleRanges() {
        String[] portRangeArray = {" 5000 - 5001 ","6000-6002 ","7000 - 7003"};
        List<String> portList = Arrays.asList(portRangeArray);
        
        launcher.config.getDeviceProcess().setProcessPort(String.join(",", portList));
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isIn(5000,5001,6000,6001,6002,7000,7001,7002,7003);
    }

    @Test
    public void testAllocatePortUsingMixOfRangesAndInts() {
        String[] portRangeArray = {" 5000-5001","6000", "6001-6002 ","7000 - 7002", "7003"};
        List<String> portList = Arrays.asList(portRangeArray);
        
        launcher.config.getDeviceProcess().setProcessPort(String.join(",", portList));
        Integer allocatedPort = launcher.allocateProcessPort();
        assertThat(allocatedPort).isIn(5000,5001,6000,6001,6002,7000,7001,7002,7003);
    }

    @Test(expected = DeviceProcessLaunchException.class)
    public void testAllocatePortUsingMalformedInteger() {
        launcher.config.getDeviceProcess().setProcessPort("a5000");
        Integer allocatedPort = launcher.allocateProcessPort();
        fail("Port should not be allocated and exception should have been raised");
    }
    
    @Test
    public void testProcessRemoteDebugPort_CommandLine() {
        launcher.config.getDeviceProcess().setJavaRemoteDebugPort("6000-7000");
        
        final String remoteDebugArgPatternStr = launcher.config.getDeviceProcess().getJavaRemoteDebugArgTemplate().replace("%d", "([6,7]\\d{3})");
        List<String> commandParts = launcher.constructProcessCommandParts(new DeviceProcessInfo("00000-000", DeviceProcessStatus.NotRunning));
        
        Pattern pattern = Pattern.compile(remoteDebugArgPatternStr);
        Integer debugPort = null;
        for (String part: commandParts) {
            Matcher matcher = pattern.matcher(part);
            if (matcher.find()) {
                debugPort = new Integer(matcher.group(1));
            }
        }
        assertThat(debugPort).isBetween(6000, 7000);
    }

    @Test
    public void testProcessProcessPort_CommandLine() {
        launcher.config.getDeviceProcess().setProcessPort("6000-7000");
        
        final String processPortPatternStr = launcher.config.getDeviceProcess().getProcessPortArgTemplate().replace("%d", "([6,7]\\d{3})");
        List<String> commandParts = launcher.constructProcessCommandParts(new DeviceProcessInfo("00000-000", DeviceProcessStatus.NotRunning));
        
        Pattern pattern = Pattern.compile(processPortPatternStr);
        Integer debugPort = null;
        for (String part: commandParts) {
            Matcher matcher = pattern.matcher(part);
            if (matcher.find()) {
                debugPort = new Integer(matcher.group(1));
            }
        }
        assertThat(debugPort).isBetween(6000, 7000);
    }

    @Test
    public void testProcessExecutableJar_CommandLine() {
        launcher.config.getDeviceProcess().setExecutableJarPath("some.jar");
        List<String> commandParts = launcher.constructProcessCommandParts(new DeviceProcessInfo("00000-000", DeviceProcessStatus.NotRunning));
        assertThat(commandParts).endsWith("-jar", "some.jar");
    }

    @Test
    public void testProcessMainClass_CommandLine() {
        launcher.config.getDeviceProcess().setMainClass("org.company.App");
        List<String> commandParts = launcher.constructProcessCommandParts(new DeviceProcessInfo("00000-000", DeviceProcessStatus.NotRunning));
        assertThat(commandParts).endsWith("org.company.App");
    }
    
}
