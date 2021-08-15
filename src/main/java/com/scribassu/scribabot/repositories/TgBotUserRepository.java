package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.TgBotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TgBotUserRepository extends JpaRepository<TgBotUser, String> {

    @Query("select u from TgBotUser u where u.userId = :userId")
    TgBotUser findOneById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update TgBotUser set department = :dep where userId = :userId")
    void updateDepartment(@Param("dep") String department, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update TgBotUser set groupNumber = :groupNum where userId = :userId")
    void updateGroupNumber(@Param("groupNum") String groupNumber, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update TgBotUser set educationForm = :educationForm where userId = :userId")
    void updateEducationForm(@Param("educationForm") String message, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update TgBotUser set previousUserMessage = :previousUserMessage where userId = :userId")
    void updatePreviousUserMessage(@Param("previousUserMessage") String previousUserMessage,
                                   @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("delete from TgBotUser u where u.userId = :userId")
    void deleteOneById(@Param("userId") String userId);
}
