package kr.hhplus.be.server.support.lock;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LockStrategyRegistry {

    private final Map<LockStrategy, LockTemplate> strategies;

    public LockStrategyRegistry(List<LockTemplate> templates) {
        this.strategies = templates.stream()
            .collect(Collectors.toMap(LockTemplate::getLockStrategy, template -> template));
    }

    public LockTemplate getLockTemplate(LockStrategy lockStrategy) {
        return strategies.get(lockStrategy);
    }
}
