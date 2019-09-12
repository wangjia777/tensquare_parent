package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FriendService friendService;
    @Autowired
    private UserClient userClient;
    /**
     * 添加好友
     * @param friendid 对方用户ID
     * @param type 1：喜欢 0：不喜欢
     * @return
     */
    @RequestMapping(value="/like/{friendid}/{type}",method= RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid , @PathVariable String type){
        //判断登录
        Claims claims=(Claims)request.getAttribute("claims_user");
        //没登录
        if(claims==null){
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //登录成功
        String userid = claims.getId();
        if(type!=null){
            if(type.equals("1")){
                //添加好友
                int flag = friendService.addFriend(userid,friendid);
                if(flag==0){
                    return new Result(false, StatusCode.ERROR,"不能重复添加");
                }
                if(flag==1){
                    userClient.updatefanscountfollowcount(userid,friendid,1);
                    return new Result(true, StatusCode.OK,"添加成功");
                }
            }else if(type.equals("2")){
                //添加非好友
                int flag = friendService.addNoFriend(userid,friendid);
                if(flag==0){
                    return new Result(false, StatusCode.ERROR,"不能重复添加非好友");
                }
                if(flag==1){
                    return new Result(true, StatusCode.OK,"添加成功");
                }
            }
            return new Result(false, StatusCode.ERROR,"参数异常");
        }else {
       return new Result(false, StatusCode.ERROR,"参数异常");
        }

    }
    /**
     * 删除好友
     * @param friendid 对方用户ID
     * @return
     */
    @RequestMapping(value="/{friendid}",method= RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        //判断登录
        Claims claims=(Claims)request.getAttribute("claims_user");
        //没登录的话
        if(claims==null){
            return new Result(false, StatusCode.LOGINERROR,"权限不足");
        }
        //登录上
        String userid = claims.getId();
        friendService.deleFriend(userid,friendid);
        userClient.updatefanscountfollowcount(userid,friendid,-1);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}


