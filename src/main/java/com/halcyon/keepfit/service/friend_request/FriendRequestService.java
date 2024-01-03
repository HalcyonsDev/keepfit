package com.halcyon.keepfit.service.friend_request;

import com.halcyon.keepfit.exception.BadRequestException;
import com.halcyon.keepfit.model.FriendRequest;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.repository.IFriendRequestRepository;
import com.halcyon.keepfit.service.auth.AuthService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final IFriendRequestRepository friendRequestRepository;
    private final UserService userService;
    private final AuthService authService;

    public FriendRequest create(Long recipientId) {
        User sender = userService.findCurrentUser();
        User recipient = userService.findById(recipientId);

        if (sender.getFriends().contains(recipient)) {
            throw new BadRequestException("This user is already a friend.");
        }

        if (sender.getId().equals(recipientId)) {
            throw new BadRequestException("Attempt to send a friend request to yourself.");
        }

        String requestId = String.format("%s %s", sender.getId(), recipientId);
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByRequestId(requestId);

        if (friendRequestOptional.isPresent()) {
            throw new BadRequestException("The friend request has already been sent.");
        }

        FriendRequest friendRequest = new FriendRequest(requestId, sender, recipient);

        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest findByRequestId(String requestId) {
        return friendRequestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request with this request not found."));
    }

    public User accept(String requestId) {
        FriendRequest friendRequest = findByRequestId(requestId);
        User currentUser = userService.findCurrentUser();
        User sender = friendRequest.getSender();

        if (!currentUser.equals(friendRequest.getRecipient()) || currentUser.equals(sender)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this friend request.");
        }

        currentUser.getFriends().add(sender);
        sender.getFriends().add(currentUser);

        friendRequestRepository.delete(friendRequest);

        return currentUser;
    }
}
