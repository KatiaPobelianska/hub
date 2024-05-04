package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.dto.CommentInputDto;
import photo.hub.dto.CommentOutputDto;
import photo.hub.model.Comment;
import photo.hub.model.Post;
import photo.hub.repository.CommentRepository;

import java.util.ArrayList;
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
    public List<CommentOutputDto> getAllByPost(long postId){
        Post post = postService.getPostById(postId);
        return convertListCommentToListCommentOutputDto(commentRepository.findAllByPostId(postId));
    }
    public List<CommentOutputDto> getAllByUsername(String username){
       return convertListCommentToListCommentOutputDto(commentRepository.findAllByPersonUsername(username));
    }
    public long getCount(){
        return commentRepository.count();
    }

    private CommentOutputDto convertCommentToOutputDto(Comment comment){
        return new CommentOutputDto(comment.getId(), comment.getPostId(), comment.getPersonUsername(), comment.getDescription(), comment.getCreatedAt());
    }
    private List<CommentOutputDto> convertListCommentToListCommentOutputDto(List<Comment> comments){
        List<CommentOutputDto> commentOutputDtos = new ArrayList<>();
        for (Comment comment: comments){
          commentOutputDtos.add(convertCommentToOutputDto(comment));
        }
        return commentOutputDtos;
    }
}
