package com.miniredis.application.command;

import com.miniredis.domain.exception.RedisException;
import com.miniredis.domain.ports.in.RedisPort;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandDispatcher {

    private final Map<String, CommandHandler> handlers;

    public CommandDispatcher(List<CommandHandler> commandHandlers) {
        this.handlers = commandHandlers.stream()
                .collect(Collectors.toConcurrentMap(
                        handler -> handler.getCommandName().toUpperCase(),
                        handler -> handler));
    }

    public String execute(RedisPort service, String commandLine) {
        List<String> parts = CommandParser.parse(commandLine);

        try {
            if (isEmpty(parts)) {
                throw RedisException.generic("empty command");
            }

            String cmd = parts.get(0).toUpperCase();
            CommandHandler handler = handlers.get(cmd);

            if (isNull(handler)) {
                throw RedisException.unknownCommand(cmd);
            }

            String result = handler.handle(service, parts);
            log.trace("Command execution result: {}", result);
            return result;
        } catch (RedisException e) {
            return e.getMessage();
        } catch (Exception e) {
            log.error("Command execution failed: {}", commandLine, e);
            return "ERR " + e.getMessage();
        }
    }
}
