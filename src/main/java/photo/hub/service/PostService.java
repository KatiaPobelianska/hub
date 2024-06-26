package photo.hub.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import photo.hub.dto.PostDtoOutput;
import photo.hub.exception.InvalidUserException;
import photo.hub.model.Category;
import photo.hub.model.Person;
import photo.hub.model.Post;
import photo.hub.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Autowired
    public PostService(PostRepository postRepository, ImageService imageService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }

    public PostDtoOutput save(MultipartFile file, String description, String title, Category category, Person person) {
        Post post = convertPostDtoInputToPost(file, description, title, category, person);
        post.setCreatedAt(LocalDateTime.now());
        post = postRepository.save(post);
        return convertPostToPostDtoOutput(post);
    }

    public PostDtoOutput getById(long id) {
        Post post = postRepository.findById(id).orElseThrow();
        PostDtoOutput postDtoOutput = convertPostToPostDtoOutput(post);
        return postDtoOutput;
    }

    public List<PostDtoOutput> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return convertPostsToPostDtoOutput(posts);
    }

    public List<PostDtoOutput> getAllPostsByUser(Person person) {
        List<Post> posts = postRepository.getPostsByOwnerUsername(person.getUsername());
        return convertPostsToPostDtoOutput(posts);
    }

    public PostDtoOutput updatePost(PostDtoOutput postDtoOutput, Person person) {
        if (!postDtoOutput.getUsername().equals(person.getUsername())) {
            throw new InvalidUserException("invalid user access denied");
        }
        Post post = postRepository.findById(postDtoOutput.getId()).stream().findAny().orElseThrow();
        post.setTitle(postDtoOutput.getTitle());
        post.setDescription(postDtoOutput.getDescription());
        postRepository.save(post);
        return convertPostToPostDtoOutput(post);
    }

    public void delete(long id, Person person) {
        Post post = postRepository.findById(id).stream().findAny().orElseThrow();
        if (!person.getUsername().equals(post.getOwner().getUsername())) {
            throw new InvalidUserException("invalid user access denied");
        }
        postRepository.deleteById(id);
    }

    public void addView(long id) {
        Post post = postRepository.findById(id).stream().findAny().orElseThrow();
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    public Post getPostById(long id) {
        return postRepository.findById(id).stream().findAny().orElseThrow();
    }
    public List<PostDtoOutput> getAllByKey(String key){
        List<Post> posts = postRepository.findByTitleContaining(key);
        return convertPostsToPostDtoOutput(posts);
    }
    public long totalCountOfViews(){
        return postRepository.getTotalViews();
    }
    public List<PostDtoOutput> getFiveOfMostViews(){
        return convertPostsToPostDtoOutput(postRepository.findTop5ByOrderByViewsDesc());
    }

    private List<PostDtoOutput> convertPostsToPostDtoOutput(List<Post> posts) {
        return posts.stream()
                .map(this::convertPostToPostDtoOutput)
                .toList();
    }

    @SneakyThrows
    private Post convertPostDtoInputToPost(MultipartFile file, String description, String title, Category category, Person person) {
        Post post = new Post();
        post.setTitle(title);
        post.setCategory(category);
        post.setDescription(description);
        post.setPhotoUrl(imageService.uploadToImgur(file.getBytes(), title, description));

        post.setOwner(person);
        return post;
    }

    private PostDtoOutput convertPostToPostDtoOutput(Post post) {
        PostDtoOutput postDtoOutput = new PostDtoOutput();
        postDtoOutput.setId(post.getId());
        postDtoOutput.setUsername(post.getOwner().getUsername());
        postDtoOutput.setTitle(post.getTitle());
        postDtoOutput.setCategory(post.getCategory());
        postDtoOutput.setDescription(post.getDescription());
        postDtoOutput.setViews(post.getViews());
        postDtoOutput.setPhotoUrl(post.getPhotoUrl());
        postDtoOutput.setCreatedAt(post.getCreatedAt());
        return postDtoOutput;
    }
}
