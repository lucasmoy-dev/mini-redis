package com.miniredis.application.command.impl;

import org.springframework.stereotype.Component;
import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;

@Component
public class SetCommandHandler implements CommandHandler {
    @Override
    public String getCommandName() {
        return "SET";
    }

    @Override
    public String handle(RedisPort service, List<String> parts) {
        if (parts.size() == 3) {
            return service.set(parts.get(1), parts.get(2));
        }
        if (parts.size() == 5 && "EX".equalsIgnoreCase(parts.get(3))) {
            return service.setWithExpiry(parts.get(1), parts.get(2), Long.parseLong(parts.get(4)));
        }
        return String.format(ERR_WRONG_ARGS, getCommandName());
    }
}
