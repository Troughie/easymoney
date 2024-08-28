package com.example.MoneyLover.infra.Friend;

import com.example.MoneyLover.infra.Friend.Service.FriendService;
import com.example.MoneyLover.infra.User.Entity.User;
import com.example.MoneyLover.shares.HandleException.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FriendController {
    private final ResponseException _res;
    private final FriendService _fService;

    @PostMapping("/friend/add/{id}")
    public ResponseEntity<?> index(@AuthenticationPrincipal User user, @PathVariable String id) {
        var result = _fService.addFriend(user, id);
        return _res.responseEntity(result, 200);
    }

    @GetMapping("/friends-request")
    public ResponseEntity<?> allFriendRequest(@AuthenticationPrincipal User user, @RequestParam(required = false) String type) {
        var result = _fService.getAllFriendOrPending(user,type);
        return _res.responseEntity(result, 200);
    }

    @PostMapping("/friends/accept/{id}")
    public ResponseEntity<?> acceptFriend(@AuthenticationPrincipal User user,@PathVariable String id) {
        var result = _fService.acceptFriend(user,id);
        return _res.responseEntity(result, 200);
    }

    @DeleteMapping("/friends/delete/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable String id) {
        var result = _fService.removeFriend(id);
        return _res.responseEntity(result, 200);
    }
}
