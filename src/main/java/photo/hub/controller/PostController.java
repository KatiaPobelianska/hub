package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import photo.hub.dto.PostDtoInput;
import photo.hub.security.PersonDetails;
import photo.hub.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody PostDtoInput postDtoInput, @AuthenticationPrincipal PersonDetails personDetails){
        return new ResponseEntity<>(postService.save(postDtoInput, personDetails.getPerson()), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id){
        return new ResponseEntity<>(postService.getById(id), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }
//Photo will get without RequestBody
    @GetMapping("/test")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @RequestPart("description") String description) {
        System.out.println(file.getOriginalFilename());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
