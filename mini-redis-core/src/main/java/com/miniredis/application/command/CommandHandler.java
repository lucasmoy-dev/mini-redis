package com.miniredis.application.command;

import com.miniredis.domain.ports.in.RedisUseCase;

public interface CommandHandler {
    String handle(RedisUseCase service, String[] parts);
}
