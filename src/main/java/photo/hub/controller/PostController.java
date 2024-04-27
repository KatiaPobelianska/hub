package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import photo.hub.dto.PostDtoOutput;
import photo.hub.exception.InvalidUserException;
import photo.hub.model.Category;
import photo.hub.security.PersonDetails;
import photo.hub.service.PostService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(postService.getById(id), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getAllPostsByUser(@AuthenticationPrincipal PersonDetails personDetails) {
        return new ResponseEntity<>(postService.getAllPostsByUser(personDetails.getPerson()), HttpStatus.OK);
    }

    @GetMapping("/find/{key}")
    public ResponseEntity<?> getAllByKey(@PathVariable("key") String key) {
        return new ResponseEntity<>(postService.getAllByKey(key), HttpStatus.OK);
    }

    //Photo will get without RequestBody
    @PostMapping()
    public ResponseEntity<?> save(@RequestPart("file") MultipartFile file,
                                  @RequestParam("description") String description,
                                  @RequestParam("title") String title,
                                  @RequestParam("category") String category,
                                  @AuthenticationPrincipal PersonDetails personDetails) {

        return new ResponseEntity<>(postService.save(file, description, title, Category.valueOf(category), personDetails.getPerson()), HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody PostDtoOutput postDtoOutput,
                                    @AuthenticationPrincipal PersonDetails personDetails) {
        try {
            return new ResponseEntity<>(postService.updatePost(postDtoOutput, personDetails.getPerson()), HttpStatus.OK);
        } catch (InvalidUserException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> addView(@PathVariable long id) {
        try {
            postService.addView(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") long id,
                                     @AuthenticationPrincipal PersonDetails personDetails) {
        try {
            postService.delete(id, personDetails.getPerson());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidUserException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/best")
    public ResponseEntity<?> getFivePostsOfMostViews() {
        return new ResponseEntity<>(postService.getFiveOfMostViews(), HttpStatus.OK);
    }
}
