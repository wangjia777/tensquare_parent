package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userid, String friendid) {
        //判断是否是重复添加好友
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if(friend!=null){
            return 0;
        }
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);

        if(friendDao.findByUseridAndFriendid(friendid, userid)!=null){
            friendDao.updateIslike("1",userid,friendid);
            friendDao.updateIslike("1",friendid,userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        NoFriend nofriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if(nofriend!=null){
            return 0;
        }
        nofriend = new NoFriend();
        nofriend.setUserid(userid);
        nofriend.setFriendid(friendid);
        noFriendDao.save(nofriend);
        return  1;
    }

    public void deleFriend(String userid, String friendid) {
        //删除还有表中userid到friendid这条数据
        friendDao.deletefriend(userid,friendid);
        //更新friendid到userid的islike为0
        friendDao.updateIslike("0",friendid,userid);
        //非好友中添加好友
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
