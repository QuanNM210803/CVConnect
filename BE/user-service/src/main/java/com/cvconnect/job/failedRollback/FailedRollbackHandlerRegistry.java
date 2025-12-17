package com.cvconnect.job.failedRollback;

import com.cvconnect.enums.FailedRollbackType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FailedRollbackHandlerRegistry {

    private final Map<FailedRollbackType, FailedRollbackHandler> handlerMap;

    public FailedRollbackHandlerRegistry(List<FailedRollbackHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(FailedRollbackHandler::getType, Function.identity()));
    }

    public FailedRollbackHandler getHandler(FailedRollbackType type) {
        return handlerMap.get(type);
    }
}

