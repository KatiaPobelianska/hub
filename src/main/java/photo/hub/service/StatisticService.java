package photo.hub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import photo.hub.model.Statistic;
import photo.hub.repository.StatisticRepository;

import java.util.List;

@Service
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final PhotoLikeService photoLikeService;

    @Autowired
    public StatisticService(StatisticRepository statisticRepository, PostService postService, CommentService commentService, PhotoLikeService photoLikeService) {
        this.statisticRepository = statisticRepository;
        this.postService = postService;
        this.commentService = commentService;
        this.photoLikeService = photoLikeService;
    }
    @Scheduled(fixedRate = 3_600_000)
    public void updateStatistic() {
        long views = postService.totalCountOfViews();
        long likes = photoLikeService.getCount();
        long comments = commentService.getCount();
        Statistic statistic = new Statistic(views, likes, comments);
        statisticRepository.save(statistic);
    }
    public Statistic getLastStatistic() {
        return statisticRepository.findFirstByOrderByCreatedAtDesc().orElseThrow();
    }
    public List<Statistic> getAll(){
        return statisticRepository.findAllByOrderByCreatedAtDesc();
    }
}
