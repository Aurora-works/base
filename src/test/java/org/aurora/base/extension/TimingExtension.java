package org.aurora.base.extension;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Log4j2
public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put("startTime", Instant.now());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        Instant startTime = getStore(context).remove("startTime", Instant.class);
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        log.info("[{}] Time taken: {} ms", testMethod.getName(), duration.toMillis());
    }

    private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }
}
