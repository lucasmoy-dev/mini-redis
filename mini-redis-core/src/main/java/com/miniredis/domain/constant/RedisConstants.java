package com.miniredis.domain.constant;

public class RedisConstants {
    public static final String OK = "OK";
    public static final String NIL = "(nil)";
    public static final String ERR_WRONG_ARGS = "ERR wrong number of arguments for '%s' command";
    public static final String ERR_UNKNOWN_COMMAND = "ERR unknown command '%s'";
    public static final String ERR_WRONG_TYPE = "ERR WRONGTYPE Operation against a key holding the wrong kind of value";
    public static final String ERR_NOT_INTEGER = "ERR value is not an integer or out of range";
    public static final String EMPTY_LIST_OR_SET = "(empty list or set)";

    private RedisConstants() {
    }
}
