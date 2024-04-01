package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import photo.hub.exception.LikeAlreadyPresentException;
import photo.hub.exception.LikeMissingException;
import photo.hub.security.PersonDetails;
import photo.hub.service.PhotoLikeService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/likes")
public class PhotoLikeController {
    private final PhotoLikeService photoLikeService;

    @Autowired
    public PhotoLikeController(PhotoLikeService photoLikeService) {
        this.photoLikeService = photoLikeService;
    }
    @GetMapping()
    public ResponseEntity<?> countOfLikeByPost(@RequestParam("postId") long postId){
        return new ResponseEntity<>(photoLikeService.countOfLikes(postId), HttpStatus.OK);
    }
    @GetMapping("/present")
    public ResponseEntity<?> isLikePresent(@RequestParam("postId") long postId, @AuthenticationPrincipal PersonDetails personDetails){
        return new ResponseEntity<>(photoLikeService.isPresentLike(postId, personDetails.getUsername()), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<?> addLike(@RequestParam("postId") long postId, @AuthenticationPrincipal PersonDetails personDetails){
        try {
            photoLikeService.addLike(postId, personDetails.getPerson());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (NoSuchElementException | LikeAlreadyPresentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("postId") long postId, @AuthenticationPrincipal PersonDetails personDetails){
        try {
            photoLikeService.delete(postId, personDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException | LikeMissingException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
