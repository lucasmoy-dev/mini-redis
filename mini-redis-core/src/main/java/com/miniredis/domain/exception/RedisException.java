package com.miniredis.domain.exception;

import static com.miniredis.domain.constant.RedisConstants.*;

public class RedisException extends RuntimeException {

    public RedisException(String message) {
        super(message);
    }

    public static RedisException wrongType() {
        return new RedisException(ERR_WRONG_TYPE);
    }

    public static RedisException notInteger() {
        return new RedisException(ERR_NOT_INTEGER);
    }

    public static RedisException wrongArguments(String command) {
        return new RedisException(String.format(ERR_WRONG_ARGS, command));
    }

    public static RedisException unknownCommand(String command) {
        return new RedisException(String.format(ERR_UNKNOWN_COMMAND, command));
    }

    public static RedisException invalidCharacters() {
        return new RedisException(ERR_INVALID_CHARS);
    }

    public static RedisException generic(String message) {
        return new RedisException("ERR " + message);
    }
}
