package com.miniredis.application.command.impl;

import org.springframework.stereotype.Component;
import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;

@Component
public class ZRangeCommandHandler implements CommandHandler {
    @Override
    public String getCommandName() {
        return "ZRANGE";
    }

    @Override
    public String handle(RedisPort service, List<String> parts) {
        if (parts.size() != 4) {
            return String.format(ERR_WRONG_ARGS, getCommandName());
        }
        List<String> range = service.zRange(parts.get(1), Integer.parseInt(parts.get(2)),
                Integer.parseInt(parts.get(3)));
        return range.isEmpty() ? EMPTY_LIST_OR_SET : String.join(" ", range);
    }
}
