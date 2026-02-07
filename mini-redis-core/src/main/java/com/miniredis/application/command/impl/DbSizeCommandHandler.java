package com.miniredis.application.command.impl;

import org.springframework.stereotype.Component;
import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;

@Component
public class DbSizeCommandHandler implements CommandHandler {
    @Override
    public String getCommandName() {
        return "DBSIZE";
    }

    @Override
    public String handle(RedisPort service, List<String> parts) {
        if (parts.size() != 1) {
            return String.format(ERR_WRONG_ARGS, getCommandName());
        }
        return service.databaseSize().toString();
    }
}
