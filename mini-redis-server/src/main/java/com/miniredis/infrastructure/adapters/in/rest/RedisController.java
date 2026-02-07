package com.miniredis.infrastructure.adapters.in.rest;

import com.miniredis.domain.ports.in.RedisPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.miniredis.domain.constant.RedisConstants.OK;
import static com.miniredis.domain.constant.RedisConstants.NIL;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mini Redis", description = "Redis-like service operations")
@RequestMapping("/api")
public class RedisController {

    private final RedisPort redis;

    @Operation(summary = "Execute a Redis command via a query parameter")
    @GetMapping(params = "cmd")
    public String executeCommand(
            @Parameter(description = "Space-delimited Redis command string") @RequestParam("cmd") String cmd) {
        return redis.executeCommand(cmd);
    }

    @Operation(summary = "Get the value of a key (REST)")
    @GetMapping("/{key}")
    public String get(@PathVariable("key") String key) {
        String result = redis.get(key);
        return result != null ? result : NIL;
    }

    @Operation(summary = "Set the value of a key (REST)")
    @PutMapping("/{key}")
    public String set(@PathVariable("key") String key, @RequestBody String value) {
        return redis.set(key, value);
    }

    @Operation(summary = "Delete a key (REST)")
    @DeleteMapping("/{key}")
    public String delete(@PathVariable("key") String key) {
        redis.delete(key);
        return OK;
    }
}
