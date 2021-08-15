package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.TgUnrecognizedMessage;
import com.scribassu.scribabot.entities.VkUnrecognizedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgUnrecognizedMessageRepository extends JpaRepository<TgUnrecognizedMessage, Long> {
}
