package com.example.MoneyLover.infra.Friend.Repository;

import com.example.MoneyLover.infra.Friend.Entity.Friend;
import com.example.MoneyLover.infra.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepo extends JpaRepository<Friend,String> {
    @Query("select n from Friend n where n.user = ?1 and n.status=?2")
    List<Friend> findAllUser(User user,String status);

    @Query("select n from Friend n where n.user = ?1 and n.status=?2")
    List<Friend> findAllUserSend(User user,String status);


    @Query("select n from Friend n where n.user = ?1 and n.friend=?2 and n.status=?3")
    Friend findFriendExist(User user,User friend,String status);
    @Query("select n from Friend n where n.friend = ?1 and n.status=?2")
    List<Friend> findAllUserReceive(User user,String type);

    Friend findTopById(String id);

    Friend findTopByUserAndFriend(User user,User friend);
}
