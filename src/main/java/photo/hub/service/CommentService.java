package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.dto.CommentInputDto;
import photo.hub.model.Comment;
import photo.hub.model.Person;
import photo.hub.model.Post;
import photo.hub.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public void addComment(CommentInputDto commentInputDto, String username) {
        Post post = postService.getPostById(commentInputDto.getPostId());
        commentRepository.save(new Comment(commentInputDto.getPostId(), username, commentInputDto.getDescription()));
    }
    public Comment getById(long postId){
        return commentRepository.findById(postId).stream().findAny().orElseThrow();
    }
    public List<Comment> getAllByPost(long postId){
        Post post = postService.getPostById(postId);
        return commentRepository.findAllByPostId(postId);
    }
}
