package com.javaproj.backend.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.javaproj.backend.config.JsonResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.hankcs.hanlp.HanLP;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@RestController
@Component
@RequestMapping(path="/api/edukg")
public class EdukgController {
    private static String id;

    @GetMapping(path = "/updateID")
    @Scheduled(fixedRate = 300000)
    public static String idForEdukg() {
        String url = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("password", "ZMLgood1");
        params.add("phone", "16643235607");
        String resJson = restTemplate.postForObject(url, params, String.class);
        id = JSON.parseObject(resJson).getString("id");
        return id;
    } // return my id for edukg

    @GetMapping(path = "/searchInstance")
    public @ResponseBody
    String searchInstance(@RequestParam String course, @RequestParam String searchKey) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course={course}&searchKey={searchKey}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class, course, searchKey, id);
    }

    @GetMapping(path = "/infoInstance")
    public @ResponseBody String infoInstance(@RequestParam String course, @RequestParam String name) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course={course}&name={name}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class, course, name, id);
    }

    @PostMapping(path = "/qa")
    public @ResponseBody String qAndA(@RequestParam String course, @RequestParam String inputQuestion) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("inputQuestion", inputQuestion);
        params.add("id", id);
        return restTemplate.postForObject(url, params, String.class);
    }

    @PostMapping(path = "/linkInstance")
    public @ResponseBody String linkInstance(@RequestParam String context, @RequestParam String course) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("context", context);
        params.add("course", course);
        params.add("id", id);
        return restTemplate.postForObject(url, params, String.class);
    }

    @GetMapping(path = "/questionList")
    public @ResponseBody String questionListByInstance(@RequestParam String uriName) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName={uriName}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class, uriName, id);
    }

    @PostMapping(path = "/relatedSubject")
    public @ResponseBody String relatedSubject(@RequestParam String course, @RequestParam String subjectName) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("subjectName", subjectName);
        params.add("id", id);
        return restTemplate.postForObject(url, params, String.class);
    }

    @PostMapping(path = "/knowledgeCard")
    public @ResponseBody String knowledgeCard(@RequestParam String course, @RequestParam String uri) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/getKnowledgeCard";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("uri", uri);
        params.add("id", id);
        return restTemplate.postForObject(url, params, String.class);
    }

    @GetMapping(path = "/instanceRecommend")
    public @ResponseBody
    JsonResult<Object> instanceRecommendation(@RequestParam String course) throws IOException, InterruptedException {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course={course}&name={name}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        List<String> res = new LinkedList<>();
        InputStream inputStream = this.getClass().getResourceAsStream("/static/" + course + "_instanceName.json");
        Reader reader = new InputStreamReader(inputStream, "utf-8");
        int ch = 0;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        JSONArray jsonArray= JSONObject.parseArray(sb.toString());
        List<String> nameList = new LinkedList<>();
        for(int i = 0; i < jsonArray.size(); i++) {
            nameList.add(jsonArray.getObject(i, String.class));
        }
        List<Integer> utils = new LinkedList<>();
        for(int index = 0; index < 20; index++) {
            Random random = new Random();
            while(true) {
                int i = random.nextInt(nameList.size());
                if(!utils.contains(i)) {
                    utils.add(i);
                    res.add(restTemplate.getForObject(url, String.class, course, nameList.get(i), id));
                    break;
                }
            }
        }
        return new JsonResult<>(res);
    }
}
