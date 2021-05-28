package com.study.web;

import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class HealthController {

    @GetMapping(path = "_health/checking")
    public Map healthCheck() {
        Map<String, String> map = new HashMap<>();
        map.put("date", new Date().toString());
        map.put("time", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        map.put("service", "gateway");
        map.put("_health", "success");
        map.put("v", "1");

        return map;
    }
}
