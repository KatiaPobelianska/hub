package photo.hub.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.dto.PostDtoInput;
import photo.hub.dto.PostDtoOutput;
import photo.hub.model.Person;
import photo.hub.model.Post;
import photo.hub.repository.PostRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Autowired
    public PostService(PostRepository postRepository, ImageService imageService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }
    public PostDtoOutput save(PostDtoInput postDtoInput, Person person){
        Post post = convertPostDtoInputToPost(postDtoInput, person);
        post = postRepository.save(post);
       return convertPostToPostDtoOutput(post);
    }
    public PostDtoOutput getById(long id){
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()){
            throw new NoSuchElementException("no post with id " + id);
        }
        PostDtoOutput postDtoOutput = convertPostToPostDtoOutput(postOptional.get());
        return postDtoOutput;
    }
    public List<PostDtoOutput> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        return convertPostsToPostDtoOutput(posts);
    }
    private List<PostDtoOutput> convertPostsToPostDtoOutput(List<Post> posts){
        return posts.stream()
                .map(this::convertPostToPostDtoOutput)
                .toList();
    }
    @SneakyThrows
    private Post convertPostDtoInputToPost(PostDtoInput postDtoInput, Person person){
        Post post = new Post();
        String title = postDtoInput.getTitle();
        String description = postDtoInput.getDescription();
        post.setTitle(title);
        post.setDescription(description);
        post.setPhotoUrl(imageService.uploadToImgur(postDtoInput.getPhoto().getBytes(), title, description));

        post.setOwner(person);
        return post;
    }
    private PostDtoOutput convertPostToPostDtoOutput(Post post){
        PostDtoOutput postDtoOutput = new PostDtoOutput();
        postDtoOutput.setId(post.getId());
        postDtoOutput.setUsername(post.getOwner().getUsername());
        postDtoOutput.setTitle(post.getTitle());
        postDtoOutput.setDescription(post.getDescription());
        postDtoOutput.setViews(post.getViews());
        postDtoOutput.setPhotoUrl(post.getPhotoUrl());
        postDtoOutput.setCreatedAt(post.getCreatedAt());
        return postDtoOutput;
    }

}
