package com.miniredis;

import com.miniredis.domain.model.SortedSetMember;
import com.miniredis.application.services.RedisService;
import com.miniredis.infrastructure.adapters.out.memory.InMemoryRedisRepository;
import java.util.List;

/**
 * Standalone verification class to demonstrate logic functionality
 * without requiring the full Spring Boot environment to run.
 */
public class LogicVerification {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Mini Redis Logic Verification ===");

        InMemoryRedisRepository repo = new InMemoryRedisRepository();
        RedisService service = new RedisService(repo);

        // 1. Basic SET/GET
        System.out.print("Testing SET/GET: ");
        service.set("name", "antigravity");
        String val = service.get("name");
        if ("antigravity".equals(val))
            System.out.println("PASSED");
        else
            System.out.println("FAILED (got " + val + ")");

        // 2. INCR
        System.out.print("Testing INCR: ");
        service.incr("count");
        long count = Long.parseLong(service.get("count"));
        if (count == 1)
            System.out.println("PASSED");
        else
            System.out.println("FAILED (got " + count + ")");

        // 3. TTL (Simulated)
        System.out.print("Testing TTL (SETEX): ");
        service.setEx("temp", "msg", 1); // 1 second
        System.out.print("Immediate: " + (service.get("temp").equals("msg") ? "OK" : "FAIL") + ", ");
        Thread.sleep(1100);
        System.out.println("After 1.1s: " + (service.get("temp").equals("(nil)") ? "PASSED" : "FAILED"));

        // 4. Sorted Sets
        System.out.print("Testing Sorted Sets (ZADD/ZRANGE): ");
        service.zAdd("fruits", 10.0, "apple");
        service.zAdd("fruits", 5.0, "banana");
        service.zAdd("fruits", 15.0, "cherry");
        List<String> range = service.zRange("fruits", 0, -1);
        if (range.size() == 3 && range.get(0).equals("banana") && range.get(2).equals("cherry")) {
            System.out.println("PASSED");
        } else {
            System.out.println("FAILED (got " + range + ")");
        }

        // 5. Command Execution (String based)
        System.out.print("Testing Command Execution: ");
        String result = service.executeCommand("SET mykey 123");
        String getResult = service.executeCommand("GET mykey");
        if ("OK".equals(result) && "123".equals(getResult))
            System.out.println("PASSED");
        else
            System.out.println("FAILED");

        System.out.println("=== Verification Complete ===");
    }
}
