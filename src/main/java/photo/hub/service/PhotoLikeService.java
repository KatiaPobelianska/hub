package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.exception.LikeAlreadyPresentException;
import photo.hub.exception.LikeMissingException;
import photo.hub.model.Person;
import photo.hub.model.PhotoLike;
import photo.hub.model.Post;
import photo.hub.repository.PhotoLikeRepository;

import java.util.Optional;

@Service
public class PhotoLikeService {
    private final PhotoLikeRepository photoLikeRepository;
    private final PostService postService;

    @Autowired
    public PhotoLikeService(PhotoLikeRepository photoLikeRepository, PostService postService) {
        this.photoLikeRepository = photoLikeRepository;
        this.postService = postService;
    }

    public int countOfLikes(long postId) {
        return photoLikeRepository.countPhotoLikeByPostId(postId);
    }
    public boolean isPresentLike(long postId, String username){
        Optional<PhotoLike> photoLike = photoLikeRepository.findByPostIdAndPersonUsername(postId,username);
        if (photoLike.isPresent()){ // TODO даже IDE подсказывает, что можно упростить код
            return true;
        }
        return false;
    }
    public void addLike(long postId, Person person){
        if (isPresentLike(postId, person.getUsername())){
            throw new LikeAlreadyPresentException("like already present of such user");
        }
        Post post = postService.getPostById(postId);
        photoLikeRepository.save(new PhotoLike(post, person));
    }
    public void delete(long postId, String username){
        if (isPresentLike(postId,username)){
            PhotoLike photoLike = photoLikeRepository.findByPostIdAndPersonUsername(postId, username).stream().findAny().orElseThrow();
            photoLikeRepository.deleteById(photoLike.getId());
        }else {
            throw new LikeMissingException("like is missing for this post");
        }
    }
    public long getCount(){
        return photoLikeRepository.count();
    }
}
