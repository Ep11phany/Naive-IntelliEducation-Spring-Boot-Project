package com.javaproj.backend.api;

import com.alibaba.fastjson.*;
import com.javaproj.backend.config.JsonResult;
import com.javaproj.backend.domain.UserRepository;
import com.javaproj.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.net.http.HttpHeaders;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController // This means that this class is a Controller
@RequestMapping(path="/api/user")
public class UserController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
//    private String id;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(path="/register") // Map ONLY POST Requests
    public JsonResult<User> addNewUser (@RequestParam String name
            , @RequestParam String email, @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
//        User userList = userRepository.findByName(name);
        List<User> userList = userRepository.findAllByName(name);
        if(!userList.isEmpty()) {
            JsonResult<User> res = new JsonResult<>("404", "Name Duplicated!");
            return res;
        }
        User n = new User();
        n.setName(name);
        n.setEmail(email);
        n.setPassword(password);
        userRepository.save(n);
        JsonResult<User> res = new JsonResult<>(n);
        return res;
    }

    @GetMapping(path="/login")
    public @ResponseBody JsonResult<User> userLogin(@RequestParam String name, @RequestParam String password) {
        String psw = DigestUtils.md5DigestAsHex(password.getBytes());
        User n1 = userRepository.findByName(name);
        if(n1 == null) return new JsonResult<>("404", "User not found!");
        if(n1.getPassword().equals(psw)) return new JsonResult<>(n1);
        else return new JsonResult<>("404", "Wrong Password!");
    }

    @PostMapping(path = "/modifyPassword")
    public @ResponseBody
    JsonResult<User> modifyPassword (@RequestParam String name
    , @RequestParam String newPassword, @RequestParam String oldPassword) {
        User n = userRepository.findByName(name);
        if (n == null) return new JsonResult<>("404", "User not found!"); //I guess it never happens.
        String oldPsw = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (n.getPassword() != oldPsw) return new JsonResult<>("404", "Password wrong!");
        n.setPassword(newPassword);
        userRepository.save(n);
        return new JsonResult<>(n);
    }

    @PostMapping(path = "/modifyInfo")
    public @ResponseBody JsonResult<User> modifyInfo (@RequestParam String name, @RequestParam String newName, @RequestParam String email) {
        User n = userRepository.findByName(name);
        if (n == null) return new JsonResult<>("404", "User not found!"); //I guess it never happens.
        n.setEmail(email);
        n.setName(newName);
        userRepository.save(n);
        return new JsonResult<>(n);
    }

    @GetMapping(path="/showAllUser")
    public @ResponseBody JsonResult<Iterable<User>> getAllUsers() {
        // This returns a JSON or XML with the users
        return new JsonResult<>(userRepository.findAll());
    }

    /**
     * 任何时候不允许使用！
     * @return
     */
    @GetMapping(path="/deleteAllUser")
    public @ResponseBody JsonResult<User> clearUsers() {
        userRepository.deleteAll();
        return new JsonResult<>();
    }

}