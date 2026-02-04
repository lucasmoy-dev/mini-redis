package com.miniredis.application.services;

import com.miniredis.application.command.CommandHandler;
import com.miniredis.domain.constant.RedisConstants;
import com.miniredis.domain.ports.in.RedisUseCase;
import com.miniredis.domain.ports.out.RedisRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedisService implements RedisUseCase {

    private final RedisRepository repository;
    private final Map<String, CommandHandler> handlers = new HashMap<>();

    public RedisService(RedisRepository repository) {
        this.repository = repository;
        initializeHandlers();
    }

    private void initializeHandlers() {
        handlers.put("SET", (service, parts) -> {
            if (parts.length == 3)
                return service.set(parts[1], parts[2]);
            if (parts.length == 5 && parts[3].equalsIgnoreCase("EX")) {
                return service.setEx(parts[1], parts[2], Long.parseLong(parts[4]));
            }
            return String.format(RedisConstants.ERR_WRONG_ARGS, "SET");
        });
        handlers.put("GET", (service, parts) -> {
            if (parts.length != 2)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "GET");
            return service.get(parts[1]);
        });
        handlers.put("DEL", (service, parts) -> {
            if (parts.length != 2)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "DEL");
            return service.del(parts[1]).toString();
        });
        handlers.put("DBSIZE", (service, parts) -> service.dbSize().toString());
        handlers.put("INCR", (service, parts) -> {
            if (parts.length != 2)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "INCR");
            return service.incr(parts[1]).toString();
        });
        handlers.put("ZADD", (service, parts) -> {
            if (parts.length != 4)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "ZADD");
            return service.zAdd(parts[1], Double.parseDouble(parts[2]), parts[3]).toString();
        });
        handlers.put("ZCARD", (service, parts) -> {
            if (parts.length != 2)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "ZCARD");
            return service.zCard(parts[1]).toString();
        });
        handlers.put("ZRANK", (service, parts) -> {
            if (parts.length != 3)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "ZRANK");
            Long rank = service.zRank(parts[1], parts[2]);
            return rank != null ? rank.toString() : RedisConstants.NIL;
        });
        handlers.put("ZRANGE", (service, parts) -> {
            if (parts.length != 4)
                return String.format(RedisConstants.ERR_WRONG_ARGS, "ZRANGE");
            List<String> range = service.zRange(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
            return range.isEmpty() ? RedisConstants.EMPTY_LIST_OR_SET : String.join(" ", range);
        });
    }

    @Override
    public String set(String key, String value) {
        repository.set(key, value, null);
        return RedisConstants.OK;
    }

    @Override
    public String setEx(String key, String value, long seconds) {
        long expiryTime = System.currentTimeMillis() + (seconds * 1000);
        repository.set(key, value, expiryTime);
        return RedisConstants.OK;
    }

    @Override
    public String get(String key) {
        return repository.get(key).orElse(RedisConstants.NIL);
    }

    @Override
    public Long del(String key) {
        return repository.delete(key) ? 1L : 0L;
    }

    @Override
    public Long dbSize() {
        return repository.dbSize();
    }

    @Override
    public Long incr(String key) {
        return repository.increment(key);
    }

    @Override
    public Long zAdd(String key, double score, String member) {
        return repository.zAdd(key, score, member);
    }

    @Override
    public Long zCard(String key) {
        return repository.zCard(key);
    }

    @Override
    public Long zRank(String key, String member) {
        return repository.zRank(key, member).orElse(null);
    }

    @Override
    public List<String> zRange(String key, int start, int stop) {
        return repository.zRange(key, start, stop);
    }

    @Override
    public String executeCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return "ERR empty command";
        }

        List<String> partList = parseArguments(commandLine.trim());
        if (partList.isEmpty())
            return "ERR empty command";

        String[] parts = partList.toArray(new String[0]);
        String cmd = parts[0].toUpperCase();

        CommandHandler handler = handlers.get(cmd);
        if (handler == null) {
            return String.format(RedisConstants.ERR_UNKNOWN_COMMAND, cmd);
        }

        try {
            return handler.handle(this, parts);
        } catch (Exception e) {
            return "ERR " + e.getMessage();
        }
    }

    private List<String> parseArguments(String text) {
        List<String> list = new ArrayList<>();
        // Robust regex to handle unquoted strings, single quoted strings, and double
        // quoted strings
        Matcher m = Pattern.compile("([^'\"\\s]+|'[^']*'|\"[^\"]*\")").matcher(text);
        while (m.find()) {
            String arg = m.group();
            if ((arg.startsWith("'") && arg.endsWith("'")) || (arg.startsWith("\"") && arg.endsWith("\""))) {
                list.add(arg.substring(1, arg.length() - 1));
            } else {
                list.add(arg);
            }
        }
        return list;
    }

    public void cleanup() {
        repository.cleanupExpired();
    }
}
