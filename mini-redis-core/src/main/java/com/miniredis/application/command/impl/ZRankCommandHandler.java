package com.miniredis.application.command.impl;

import org.springframework.stereotype.Component;
import static com.miniredis.domain.constant.RedisConstants.*;
import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;
import java.util.Optional;

@Component
public class ZRankCommandHandler implements CommandHandler {
    @Override
    public String getCommandName() {
        return "ZRANK";
    }

    @Override
    public String handle(RedisPort service, List<String> parts) {
        if (parts.size() != 3) {
            return String.format(ERR_WRONG_ARGS, getCommandName());
        }
        Long rank = service.zRank(parts.get(1), parts.get(2));
        return rank == null ? NIL : rank.toString();
    }
}
