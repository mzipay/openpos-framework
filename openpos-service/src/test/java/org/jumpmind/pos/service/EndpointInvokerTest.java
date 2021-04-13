package org.jumpmind.pos.service;

import org.jumpmind.pos.persist.DBSession;
import org.jumpmind.pos.service.instrumentation.Sample;
import org.jumpmind.pos.service.instrumentation.ServiceSampleModel;
import org.jumpmind.pos.service.strategy.IInvocationStrategy;
import org.jumpmind.pos.service.strategy.LocalOnlyStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EndpointInvokerTest {

    public void TestMethodNotAnnotated() {}

    @Sample
    public void TestMethodAnnotated() {}

    String installationId = "TestInstallationId";

    private ServiceSampleModel getServiceSampleModel() {
        ServiceSampleModel sampleModel = mock(ServiceSampleModel.class, RETURNS_DEEP_STUBS);
        sampleModel.setStartTime(new Date());
        sampleModel.setEndTime(new Date());
        return sampleModel;
    }

    @Test
    public void startSampleReturnsNullWhenNotConfiguredToSampleMethodTest() throws NoSuchMethodException {
        IInvocationStrategy invocationStrategy = new LocalOnlyStrategy();

        Method method = EndpointInvokerTest.class.getMethod("TestMethodNotAnnotated");

        EndpointInvoker endpointInvoker = new EndpointInvoker();
        ServiceSampleModel result = endpointInvoker.startSample(invocationStrategy, null, null, method, null);
        assertNull(result, "EndpointInvoker.startSample should return null when the method passed is not configured to be sampled");
    }

    @Test
    public void startSampleReturnsNotNullWhenIsConfiguredToSampleMethodTest() throws NoSuchMethodException {
        IInvocationStrategy invocationStrategy = new LocalOnlyStrategy();

        Method method = EndpointInvokerTest.class.getMethod("TestMethodAnnotated");

        EndpointInvoker endpointInvoker = new EndpointInvoker();
        endpointInvoker.installationId = installationId;
        ServiceSampleModel result = endpointInvoker.startSample(invocationStrategy, null, null, method, null);

        assertNotNull(result, "EndpointInvoker.startSample should return not null when the method passed is configured to be sampled.");
    }

    @Test
    public void startSampleReturnsAcceptableFieldsTest() throws NoSuchMethodException {
        IInvocationStrategy invocationStrategy = new LocalOnlyStrategy();

        Method method = EndpointInvokerTest.class.getMethod("TestMethodAnnotated");
        String simpleName = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();

        EndpointInvoker endpointInvoker = new EndpointInvoker();
        endpointInvoker.installationId = installationId;

        ServiceSampleModel result = endpointInvoker.startSample(invocationStrategy, null, null, method, null);

        assertTrue(Pattern.matches(installationId + "\\d*", result.getSampleId()), "sampleId should have installationId followed by the system time in milliseconds.");
        assertEquals(installationId, result.getInstallationId(), "installationId should be populated with the installationId.");
        assertNotNull(result.getHostname(), "hostname should populate results with the systems hostname.");
        assertTrue(Pattern.matches(simpleName + "." + methodName, result.getServiceName()), "serviceName should be populated with the methodDeclaringClassSimpleName.methodName.");
        assertEquals(invocationStrategy.getStrategyName(), result.getServiceType(), "serviceType should be populated with the strategy name of the strategy passed.");
        assertNotNull(result.getStartTime(), "startTime should be populated with the data the approximate date the method was called.");
        assertNull(result.getServicePath(), "servicePath should not be set by startSample.");
        assertNull(result.getServiceResult(), "serviceResult should not be set by startSample.");
        assertNull(result.getEndTime(), "serviceResult should not be set by startSample.");
        assertEquals( 0, result.getDurationMs(), "durationMS should not be set by startSample.");
        assertFalse(result.isErrorFlag(), "errorFlag should not be set by startSample.");
        assertNull(result.getErrorSummary(), "errorSummary should not be set by startSample.");
    }

    @Test
    public void endSampleSuccessEndsSampleTest() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        ServiceSampleModel sampleModel = getServiceSampleModel();

        Object testResult = new Object();
        DBSession session = mock(DBSession.class);

        EndpointInvoker endpointInvoker = spy(new EndpointInvoker());
        endpointInvoker.dbSession = session;

        endpointInvoker.endSampleSuccess(sampleModel, null, null, null, null, testResult);

        verify(sampleModel, atLeastOnce()).setServiceResult(anyString());
        verify(endpointInvoker, atLeastOnce()).endSample(sampleModel, null, null, null, null);
    }

    @Test
    public void endSampleErrorProperlySetsFieldsTest() throws NoSuchMethodException {
        ServiceSampleModel sampleModel = getServiceSampleModel();

        Object testResult = new Object();
        DBSession session = mock(DBSession.class);

        EndpointInvoker endpointInvoker = spy(new EndpointInvoker());
        endpointInvoker.dbSession = session;

        endpointInvoker.endSampleError(sampleModel, null, null, null, null, testResult, new Exception("Test"));

        verify(sampleModel, atLeastOnce()).setServiceResult(null);
        verify(sampleModel, atLeastOnce()).setErrorFlag(true);
        verify(sampleModel, atLeastOnce()).setErrorSummary(anyString());
        verify(endpointInvoker, atLeastOnce()).endSample(sampleModel, null, null, null, null);
    }

    @Test
    public void endSampleSetsDataAndSavesToDBSession() throws NoSuchMethodException {
        ServiceSampleModel sampleModel = getServiceSampleModel();

        DBSession session = mock(DBSession.class);

        EndpointInvoker endpointInvoker = new EndpointInvoker();
        endpointInvoker.dbSession = session;

        endpointInvoker.endSample(sampleModel, null, null, null, null);

        verify(sampleModel, atLeastOnce()).setEndTime(anyObject());
        verify(sampleModel, atLeastOnce()).setDurationMs(anyLong());
        verify(session, atLeastOnce()).save(sampleModel);
    }
}