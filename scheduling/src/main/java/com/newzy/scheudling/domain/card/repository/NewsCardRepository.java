package com.newzy.scheudling.domain.card.repository;

import com.newzy.scheudling.domain.card.entity.NewsCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCardRepository extends JpaRepository<NewsCard, Long> {

}
