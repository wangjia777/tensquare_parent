package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.servie.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findall(){
    return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    /**
     * ID查询数据
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(spitId));
    }
    /**
     * 添加数据
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }
    /**
     * 修改数据
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"修改成功");
    }
    /**
     * ID删除数据
     * @return
     */
    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    /**
     * ID查询数据
     * @return
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> page1 = spitService.findByParentid(parentid, page, size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<Spit>(page1.getTotalElements(),page1.getContent()));
    }
    /**
     * 吐糟点赞
     * @return
     */
    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        //判断用户是否已经点过赞了
        String userid = "111";
        if(redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            return new Result(false, StatusCode.REPERROR,"不能重复点赞");
        }
        spitService.thumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_"+userid,1);
        return new Result(true, StatusCode.OK,"点赞成功");
    }

}
