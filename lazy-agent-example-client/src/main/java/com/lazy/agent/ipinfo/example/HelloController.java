package com.lazy.agent.ipinfo.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author laizhiyuan
 * @since 2020/4/15.
 */
@RestController
public class HelloController {

    @GetMapping("/test1")
    public String test1() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }


    @GetMapping("/test2")
    public String test2() {
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    @GetMapping("/test3")
    public String test3() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "ok";
    }
}
