package com.miniredis.domain.constant;

public class RedisConstants {
    // String Replies
    public static final String OK = "OK";
    public static final String NIL = "(nil)";
    public static final String EMPTY_LIST_OR_SET = "(empty list or set)";

    // Numerical Values (Avoid Magic Numbers)
    public static final long VALUE_EXISTS = 1L;
    public static final long VALUE_NOT_EXISTS = 0L;
    public static final long DEFAULT_INCREMENT_START = 1L;
    public static final int START_INDEX_ALL = 0;
    public static final int STOP_INDEX_ALL = -1;

    // Error Messages
    public static final String ERR_WRONG_ARGS = "ERR wrong number of arguments for '%s' command";
    public static final String ERR_UNKNOWN_COMMAND = "ERR unknown command '%s'";
    public static final String ERR_WRONG_TYPE = "ERR WRONGTYPE Operation against a key holding the wrong kind of value";
    public static final String ERR_NOT_INTEGER = "ERR value is not an integer or out of range";
    public static final String ERR_INVALID_CHARS = "ERR invalid characters in key or value. Allowed: [a-zA-Z0-9-_:]";

    private RedisConstants() {
    }
}
