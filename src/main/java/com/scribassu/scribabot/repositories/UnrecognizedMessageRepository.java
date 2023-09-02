package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.unrecognized_messages.UnrecognizedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnrecognizedMessageRepository extends JpaRepository<UnrecognizedMessage, String> {
}
