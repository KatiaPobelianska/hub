package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import photo.hub.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Autowired
    public PostService(PostRepository postRepository, ImageService imageService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }
}
