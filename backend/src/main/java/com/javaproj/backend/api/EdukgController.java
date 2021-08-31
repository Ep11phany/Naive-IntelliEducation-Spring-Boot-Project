package com.javaproj.backend.api;

import com.alibaba.fastjson.JSON;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(path="/api/edukg")
public class EdukgController {
    private static String id;

    @GetMapping(path = "/updateID")
    public static String idForEdukg() {
        String url = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("password", "ZMLgood1");
        params.add("phone", "16643235607");
        String resJson = restTemplate.postForObject(url, params, String.class);
        id = JSON.parseObject(resJson).getString("id");
        return JSON.parseObject(resJson).getString("id");
    } // return my id for edukg

    @GetMapping(path = "/searchInstance")
    public @ResponseBody
    String searchInstance(@RequestParam String course, @RequestParam String searchKey) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course={course}&searchKey={searchKey}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        String resJson = restTemplate.getForObject(url, String.class, course, searchKey, this.id);
        return resJson;
    }

    @GetMapping(path = "/infoInstance")
    public @ResponseBody String infoInstance(@RequestParam String course, @RequestParam String name) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course={course}&name={name}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        String resJson = restTemplate.getForObject(url, String.class, course, name, this.id);
        return resJson;
    }

    @PostMapping(path = "/qa")
    public @ResponseBody String qAndA(@RequestParam String course, @RequestParam String inputQuestion) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/inputQuestion";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("inputQuestion", inputQuestion);
        params.add("id", this.id);
        String resJson = restTemplate.postForObject(url, params, String.class);
        return resJson;
    }

    @PostMapping(path = "/linkInstance")
    public @ResponseBody String linkInstance(@RequestParam String context, @RequestParam String course) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/linkInstance";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("context", context);
        params.add("course", course);
        params.add("id", this.id);
        String resJson = restTemplate.postForObject(url, params, String.class);
        return resJson;
    }

    @GetMapping(path = "/questionList")
    public @ResponseBody String questionListByInstance(@RequestParam String uriName) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/questionListByUriName?uriName={uriName}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        String resJson = restTemplate.getForObject(url, String.class, uriName, this.id);
        return resJson;
    }

    @PostMapping(path = "/relatedSubject")
    public @ResponseBody String relatedSubject(@RequestParam String course, @RequestParam String subjectName) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/relatedsubject";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("subjectName", subjectName);
        params.add("id", this.id);
        String resJson = restTemplate.postForObject(url, params, String.class);
        return resJson;
    }

    @PostMapping(path = "knowledgeCard")
    public @ResponseBody String knowledgeCard(@RequestParam String course, @RequestParam String uri) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/getKnowledgeCard";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("course", course);
        params.add("uri", uri);
        params.add("id", this.id);
        String resJson = restTemplate.postForObject(url, params, String.class);
        return resJson;
    }
}
