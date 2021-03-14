package vloving.site.crawler.controller;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.util.Map;

/**
 * @author yzw
 * @date 2021/3/10 16:34
 */
@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    private final Logger logger = LoggerFactory.getLogger(CrawlerController.class);
    @Autowired
    private HttpClient httpClient;

    @GetMapping("/getData")
    public Map getData() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://www.runoob.com").openStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter("data.html"));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
