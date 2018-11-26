package org.jumpmind.pos.devices.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.jumpmind.pos.server.service.IMessageService;
import org.jumpmind.pos.service.ServiceResult;
import org.jumpmind.pos.service.ServiceResult.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import jpos.JposException;

abstract public class AbstractDeviceWrapper<T, R extends ServiceResult> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IMessageService messageService;

    @Value("${device.aquire.timeout.ms:10000}")
    long acquireTimeoutInMs;

    protected Map<String, T> devicesByLogicalName = new HashMap<>();

    protected Map<String, Semaphore> semaphoresByLogicalName = new HashMap<>();

    protected R doSynchronized(IDoSynchronized<R> function, DeviceRequest req, Class<R> resultClazz) {
        try {
            R result = (R)resultClazz.newInstance();
            result.setResultStatus(Result.FAILURE);
            Semaphore semaphore = getSemaphore(req);
            try {
                if (semaphore.tryAcquire(acquireTimeoutInMs, TimeUnit.MILLISECONDS)) {
                    function.doSynchronized(result);
                } else {
                    result.setResultMessage(String.format("Timed out after %dms", acquireTimeoutInMs));
                }
            } catch (Exception e) {
                logger.error("", e);
                result.setResultMessage(e.getMessage());
            }

            return result;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized Semaphore getSemaphore(DeviceRequest req) {
        String logicalName = req.getDeviceName();
        Semaphore semaphore = semaphoresByLogicalName.get(logicalName);
        if (semaphore == null) {
            semaphore = new Semaphore(1);
            semaphoresByLogicalName.put(logicalName, semaphore);
        }
        return semaphore;
    }

    protected T getDevice(DeviceRequest req) throws JposException {
        String logicalName = req.getDeviceName();
        T device = devicesByLogicalName.get(logicalName);
        if (device == null) {
            device = create(req);
            devicesByLogicalName.put(logicalName, device);
        }
        return device;
    }

    abstract protected T create(DeviceRequest req) throws JposException;

    public interface IDoSynchronized<R> {
        void doSynchronized(R results) throws JposException;
    }
}
