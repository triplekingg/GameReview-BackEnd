package io.muzoo.ssc.project.backend.gamerepositories;

import io.muzoo.ssc.project.backend.User;
import io.muzoo.ssc.project.backend.games.Fifa;
import io.muzoo.ssc.project.backend.games.Fortnite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FortniteRepository extends JpaRepository<Fortnite, Long> {
//    Fifa findAllByReviews(String username);
}
