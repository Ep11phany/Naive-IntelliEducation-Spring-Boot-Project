package com.javaproj.backend.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.javaproj.backend.config.JsonResult;
import com.javaproj.backend.domain.QuestionCollectRepository;
import com.javaproj.backend.model.QuestionCollect;
import com.javaproj.backend.model.SimilarityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@RestController
@Component
@RequestMapping(path="/api/edukg")
public class EdukgController {
    private static String id;
    @Autowired
    private QuestionCollectRepository questionCollectRepository;

    @GetMapping(path = "/updateID")
    @Scheduled(fixedRate = 300000)
    public static void idForEdukg() {
        String url = "http://open.edukg.cn/opedukg/api/typeAuth/user/login";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("password", "Marksu010204");
        params.add("phone", "18618452874");
        String resJson = restTemplate.postForObject(url, params, String.class);
        id = JSON.parseObject(resJson).getString("id");
    } // return my id for edukg

    @GetMapping(path = "/searchInstance")
    public @ResponseBody
    String searchInstance(@RequestParam String course, @RequestParam String searchKey) {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course={course}&searchKey={searchKey}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        questionListByInstance(searchKey);
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
        String resStr = restTemplate.getForObject(url, String.class, uriName, id);
        JSONObject jsonObject = JSONObject.parseObject(resStr);
        JSONArray questionList = jsonObject.getJSONArray("data");
        for(int i = 0; i < questionList.size(); i++) {
            Long questionID = questionList.getJSONObject(i).getLong("id");
            String qBody = questionList.getJSONObject(i).getString("qBody");
            String qAnswer = questionList.getJSONObject(i).getString("qAnswer").replace("答案", "");
            if(questionCollectRepository.findByQuestionID(questionID) == null) {
                QuestionCollect questionCollect = new QuestionCollect();
                questionCollect.setQuestionID(questionID);
                questionCollect.setqAnswer(qAnswer);
                questionCollect.setqBody(qBody);
                questionCollectRepository.save(questionCollect);
            }
        }
        return resStr;
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
    JsonResult<Object> instanceRecommendation(@RequestParam String course) throws IOException {
        String url = "http://open.edukg.cn/opedukg/api/typeOpen/open/infoByInstanceName?course={course}&name={name}&id={id}";
        RestTemplate restTemplate = new RestTemplate();
        String anotherUrl = "http://open.edukg.cn/opedukg/api/typeOpen/open/instanceList?course={course}&searchKey={searchKey}&id={id}";
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
                if(!utils.contains(i) && !nameList.get(i).contains(" ")) {
                    JSONObject instanceReturn = restTemplate.getForObject(url, JSONObject.class, course, nameList.get(i), id);
                    instanceReturn.getJSONObject("data").getString("label").replace(" ", "");
                    if(instanceReturn.getString("code") != "0") {
                        JSONObject searchInstanceReturn = restTemplate.getForObject(anotherUrl, JSONObject.class, course, instanceReturn.getJSONObject("data").getString("label"), id);
                        if(searchInstanceReturn.getJSONArray("data").size() == 0) {
                            instanceReturn.getJSONObject("data").put("category", "");
                            res.add(instanceReturn.toJSONString());
                            utils.add(i);
                            break;
                        } else {
                            JSONArray jsonArray1 = searchInstanceReturn.getJSONArray("data");
                            for (int j = 0; j < jsonArray1.size(); j++) {
                                if (((String) jsonArray1.getJSONObject(j).get("label")).equals(nameList.get(i))) {
                                    instanceReturn.getJSONObject("data").put("category", jsonArray1.getJSONObject(j).getString("category"));
                                    res.add(instanceReturn.toJSONString());
                                    utils.add(i);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return new JsonResult<>(res);
    }

    @GetMapping(path = "/questionRecommend")
    public @ResponseBody JsonResult<Object> questionRecommend(@RequestParam String label) {
        Iterable<QuestionCollect> questionCollectIterable = questionCollectRepository.findAll();
        List<QuestionCollect> questionCollectList = new LinkedList<>();
        questionCollectIterable.forEach(questionCollectList::add);
        Comparator<QuestionCollect> comparator = new Comparator<>() {
            @Override
            public int compare(QuestionCollect o1, QuestionCollect o2) {
                if(SimilarityUtil.getSimilarity(o1.getqBody(), label) > SimilarityUtil.getSimilarity(o2.getqBody(), label)) {
                    return -1;
                } else if(SimilarityUtil.getSimilarity(o1.getqBody(), label) < SimilarityUtil.getSimilarity(o2.getqBody(), label)) {
                    return 1;
                } else { return 0; }
            }
        };
        questionCollectList.sort(comparator);
        questionCollectList = questionCollectList.subList(0, 19);
        return new JsonResult<>(questionCollectList);
    }
}
