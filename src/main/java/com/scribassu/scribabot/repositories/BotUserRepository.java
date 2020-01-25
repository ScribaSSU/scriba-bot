package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BotUserRepository extends JpaRepository<BotUser, String> {

    @Query("select u from BotUser u where u.userId = :userId")
    BotUser findOneById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update BotUser set department = :dep where userId = :userId")
    void updateDepartment(@Param("dep") String department, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update BotUser set groupNumber = :groupNum where userId = :userId")
    void updateGroupNumber(@Param("groupNum") String groupNumber, @Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update BotUser set educationForm = :educationForm where userId = :userId")
    void updateEducationForm(@Param("educationForm") String message, @Param("userId") String userId);
}
