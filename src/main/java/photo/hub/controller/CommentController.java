package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import photo.hub.dto.CommentInputDto;
import photo.hub.model.Comment;
import photo.hub.security.PersonDetails;
import photo.hub.service.CommentService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentInputDto commentInputDto,
                                        @AuthenticationPrincipal PersonDetails personDetails){
        try {
            commentService.addComment(commentInputDto, personDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllByPost(@RequestParam("postId") long postId){
        try {
            return new ResponseEntity<>(commentService.getAllByPost(postId), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   @GetMapping("/all")
    public ResponseEntity<?> getAllByUsername(@RequestParam("username") String username){
           return new ResponseEntity<>(commentService.getAllByUsername(username), HttpStatus.OK);
   }
}
