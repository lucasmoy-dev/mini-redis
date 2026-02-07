package com.miniredis.application.command.impl;

import org.springframework.stereotype.Component;
import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;

@Component
public class ZAddCommandHandler implements CommandHandler {
    @Override
    public String getCommandName() {
        return "ZADD";
    }

    @Override
    public String handle(RedisPort service, List<String> parts) {
        if (parts.size() != 4) {
            return String.format(ERR_WRONG_ARGS, getCommandName());
        }
        return service.zAdd(parts.get(1), Double.parseDouble(parts.get(2)), parts.get(3)).toString();
    }
}
