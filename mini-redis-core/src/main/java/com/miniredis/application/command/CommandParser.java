package com.miniredis.application.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Objects.isNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandParser {

    private final Pattern COMMAND_PATTERN = Pattern.compile("([^'\"\\s]+|'[^']*'|\"[^\"]*\")");

    public List<String> parse(String commandLine) {
        if (isNull(commandLine) || commandLine.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>();
        Matcher matcher = COMMAND_PATTERN.matcher(commandLine.trim());

        while (matcher.find()) {
            String argument = matcher.group();
            if ((argument.startsWith("'") && argument.endsWith("'"))
                    || (argument.startsWith("\"") && argument.endsWith("\""))) {
                list.add(argument.substring(1, argument.length() - 1));
            } else {
                list.add(argument);
            }
        }
        return list;
    }
}
