package org.jumpmind.pos.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class EndpointSpecificConfigTest {

    String serviceTestId;
    int endpointIndex;
    String path;
    SamplingConfig samplingConfig;

    EndpointSpecificConfig endpointSpecificConfig = new EndpointSpecificConfig();

    @Mock
    IConfigApplicator iConfigApplicator;

    @BeforeEach
    public void before(){
        MockitoAnnotations.initMocks(this);
        serviceTestId = "TestID";
        endpointIndex = 0;
        path = String.format("openpos.services.specificConfig.%s.endpoints[%d].samplingConfig", serviceTestId, endpointIndex);
        endpointSpecificConfig.additionalConfigSource = iConfigApplicator;
        samplingConfig = new SamplingConfig();
        endpointSpecificConfig.samplingConfig = samplingConfig;

    }

    @Test
    public void findAdditionalConfigsWithNullSampleConfig() {
        endpointSpecificConfig.samplingConfig = null;
        endpointSpecificConfig.findAdditionalConfigs(serviceTestId, endpointIndex);
        verify(iConfigApplicator, atLeastOnce()).applyAdditionalConfiguration(eq(path), any(SamplingConfig.class));
    }

    @Test
    public void findAdditionalConfigsWithNullAdditionalConfigSource() {
        endpointSpecificConfig.additionalConfigSource = null;
        endpointSpecificConfig.findAdditionalConfigs(serviceTestId, endpointIndex);
        verify(iConfigApplicator, never()).applyAdditionalConfiguration(eq(path), eq(samplingConfig));
    }
}