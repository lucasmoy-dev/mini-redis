package com.miniredis.infrastructure.adapters.in.rest;

import com.miniredis.domain.ports.in.RedisUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Mini Redis", description = "Redis-like service operations")
public class RedisController {

    private final RedisUseCase redisUseCase;

    @Operation(summary = "Execute a Redis command via a query parameter")
    @GetMapping(params = "cmd")
    public String executeCommand(
            @Parameter(description = "Space-delimited Redis command string") @RequestParam("cmd") String cmd) {
        return redisUseCase.executeCommand(cmd);
    }

    @Operation(summary = "Get the value of a key (REST)")
    @GetMapping("/{key}")
    public String get(@PathVariable("key") String key) {
        return redisUseCase.get(key);
    }

    @Operation(summary = "Set the value of a key (REST)")
    @PutMapping("/{key}")
    public String set(@PathVariable("key") String key, @RequestBody String value) {
        return redisUseCase.set(key, value);
    }

    @Operation(summary = "Delete a key (REST)")
    @DeleteMapping("/{key}")
    public String delete(@PathVariable("key") String key) {
        return redisUseCase.del(key) == 1 ? "OK" : "(nil)";
    }
}
