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
            List<Comment> comments = commentService.getAllByPost(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id){
        return new ResponseEntity<>(commentService.getById(id), HttpStatus.OK);
    }
}
