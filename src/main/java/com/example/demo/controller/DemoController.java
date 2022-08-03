package com.example.monitor.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class DemoController {
    LoadingCache cache = CacheBuilder.newBuilder()
            .softValues()
            .refreshAfterWrite(1, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 1000; i++) {
                        sb.append(key);
                    }
                    return sb.toString();
                }
            });

    @GetMapping("/demo")
    @ResponseBody
    public String demo() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    cache.get(UUID.randomUUID().toString());
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return "ok";
    }
}
