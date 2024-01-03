package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.model.FriendRequest;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.service.friend_request.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friends/request")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping
    public ResponseEntity<FriendRequest> create(@RequestParam Long recipient_id) {
        FriendRequest friendRequest = friendRequestService.create(recipient_id);
        return ResponseEntity.ok(friendRequest);
    }

    @GetMapping("/get")
    public ResponseEntity<FriendRequest> getByRequestId(@RequestParam String requestId) {
        FriendRequest friendRequest = friendRequestService.findByRequestId(requestId);
        return ResponseEntity.ok(friendRequest);
    }

    @GetMapping("/accept")
    public ResponseEntity<User> accept(@RequestParam String requestId) {
        User user = friendRequestService.accept(requestId);
        return ResponseEntity.ok(user);
    }
}
