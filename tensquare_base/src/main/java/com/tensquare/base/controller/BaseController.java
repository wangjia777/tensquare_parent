package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/label")
@RefreshScope
public class BaseController {
    @Autowired
    private LabelService labelService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
    return new Result(true,StatusCode.OK,"查询成功",labelService.findAll());
    }

    @RequestMapping(value ="/{labelId}" ,method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId")String labelId){
        //头部信息
        String header = request.getHeader("Authorization");
        System.out.println(header);
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(labelId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    @RequestMapping(value ="/{labelId}" ,method = RequestMethod.PUT)
    public Result update(@PathVariable String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"更新成功");
    }

    @RequestMapping(value ="/{labelId}" ,method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true,StatusCode.OK,"删除成功");
    }


    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findSearch(searchMap));
    }

    @RequestMapping(value="/search/{page}/{size}",method =RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap,@PathVariable int page,@PathVariable int size ){
        Page pageList= labelService.findSearch(searchMap,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageList.getTotalElements(),pageList.getContent() ));
    }


}
