package vloving.site.crawler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author yzw
 * @date 2021/3/10 16:34
 */
@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    @GetMapping("/getData")
    public Map getData() {
        return null;
    }
}
