package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin") // 공통적인 URL
public class AdminUserController {

    private UserDaoService service;

    public AdminUserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveFindAll(){
        List<User> findAll = service.findAll();

        //전체 사용자 조회 제어하는 방법
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name","joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(findAll);

        mapping.setFilters(filters);
        return mapping;

    }

    @GetMapping("/v1/users/{id}")
    public MappingJacksonValue retrieveFindOneV1(@PathVariable int id){
        User user = service.findOne(id);
        if (user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // 개별 사용자 조회 제어하는 방법
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name","password", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);

        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue retrieveFindOneV2(@PathVariable int id) {
        User user = service.findOne(id);
        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        //User -> UserV2 copy
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); //id, name, joinDate, password, ssn
        userV2.setGrade("VIP");

        // 개별 사용자 조회 제어하는 방법
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);

        mapping.setFilters(filters);

        return mapping;
    }

//        @GetMapping(value = "/users/{id}/", params = "version=1") 파라미터를 이용한 버전관리
//        @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1") 헤더를 이용한 버전관리
        @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
        public MappingJacksonValue retrieveUserV1 (@PathVariable int id){
            User user = service.findOne(id);
            if (user == null) {
                throw new UserNotFoundException(String.format("ID[%s] not found", id));
            }

            //User -> UserV2 copy
            UserV2 userV2 = new UserV2();
            BeanUtils.copyProperties(user, userV2); //id, name, joinDate, password, ssn
            userV2.setGrade("VIP");

            // 개별 사용자 조회 제어하는 방법
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

            FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

            MappingJacksonValue mapping = new MappingJacksonValue(userV2);

            mapping.setFilters(filters);

            return mapping;
        }
//        @GetMapping(value = "/users/{id}/", params = "version=2") 파라미터를 이용한 버전관리
//        @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2") 헤더를 이용한 버전관리
        @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")
        public MappingJacksonValue retrieveUserV2 (@PathVariable int id){
            User user = service.findOne(id);
            if (user == null) {
                throw new UserNotFoundException(String.format("ID[%s] not found", id));
            }

            //User -> UserV2 copy
            UserV2 userV2 = new UserV2();
            BeanUtils.copyProperties(user, userV2); //id, name, joinDate, password, ssn
            userV2.setGrade("VIP");

            // 개별 사용자 조회 제어하는 방법
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");

            FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

            MappingJacksonValue mapping = new MappingJacksonValue(userV2);

            mapping.setFilters(filters);

            return mapping;

        }
    }

