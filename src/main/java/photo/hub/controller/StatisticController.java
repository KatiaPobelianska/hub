package photo.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import photo.hub.service.StatisticService;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }
    @GetMapping("/last")
    public ResponseEntity<?> getLastStatistic(){
        try {
            return new ResponseEntity<>(statisticService.getLastStatistic(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<?> saveNewStatistic(){
        statisticService.updateStatistic();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
