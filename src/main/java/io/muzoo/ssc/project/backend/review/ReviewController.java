package io.muzoo.ssc.project.backend.review;


import io.muzoo.ssc.project.backend.User;
import io.muzoo.ssc.project.backend.UserRepository;
import io.muzoo.ssc.project.backend.gamerepositories.FifaRepository;
import io.muzoo.ssc.project.backend.gamerepositories.FortniteRepository;
import io.muzoo.ssc.project.backend.games.Fifa;
import io.muzoo.ssc.project.backend.games.Fortnite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    private FortniteRepository fortniteRepository;

    @Autowired
    private FifaRepository fifaRepository;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/api/review")
    public void review(HttpServletRequest request){
        String review = request.getParameter("review");
        String name = request.getParameter("gameName");

        // Put try aroudn the statement because we use nested dot notation which could raise a NullPointerException
        try {
            //Get current username so that we can keep track of the author of the review
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
            User u = userRepository.findFirstByUsername(user.getUsername());
            String username = u.getUsername();

            if(name.equals("Fifa")){
                Fifa fifa = new Fifa();
                fifa.setReviews(review);
                fifaRepository.save(fifa);
            }
            else if(name.equals("Fortnite")){
                Fortnite fortnite = new Fortnite();
                fortnite.setReviews(review);
                fortnite.setUsername(username);
                fortniteRepository.save(fortnite);
            }
        }
         catch(Exception e){
        }
    }
    @GetMapping("/api/review/fortnite")
    public FortniteDTO fetchFortniteReviews(){
        // Put try aroudn the statement because we use nested dot notation which could raise a NullPointerException
        try {
            List<Fortnite> all = fortniteRepository.findAll();
            return FortniteDTO.builder().Reviews(all).Test("Failed just kidding ").build();
        }
        catch(Exception e){
        }
        return null;
    }
}
