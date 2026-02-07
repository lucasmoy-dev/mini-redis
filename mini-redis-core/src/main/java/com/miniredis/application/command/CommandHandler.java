package com.miniredis.application.command;

import com.miniredis.domain.ports.in.RedisPort;
import java.util.List;

public interface CommandHandler {
    String handle(RedisPort service, List<String> parts);

    String getCommandName();
}
