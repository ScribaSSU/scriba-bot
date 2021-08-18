package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.TgUnrecognizedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgUnrecognizedMessageRepository extends JpaRepository<TgUnrecognizedMessage, Long> {
}
