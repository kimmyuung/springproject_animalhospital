package animalhospital.conrtroller;

import animalhospital.domain.youtube.YoutubeProvider;
import animalhospital.dto.YouTubeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YouTubeController {
    private YoutubeProvider youTubeProvider;

    @Autowired
    public YouTubeController(
            final YoutubeProvider youTubeProvider
    ) {
        this.youTubeProvider = youTubeProvider;
    }

    @GetMapping("/videosearch")
    public YouTubeDto Index(@RequestParam("search") String text) {

        return youTubeProvider.get();
    }
}
