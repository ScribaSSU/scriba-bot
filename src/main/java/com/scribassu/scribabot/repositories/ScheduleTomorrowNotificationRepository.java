package com.scribassu.scribabot.repositories;

import com.scribassu.scribabot.entities.ScheduleDailyNotification;
import com.scribassu.scribabot.entities.ScheduleTomorrowNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ScheduleTomorrowNotificationRepository extends JpaRepository<ScheduleTomorrowNotification, Long> {

    @Query("select s from ScheduleTomorrowNotification s where s.hourForSend = :hour and s.isEnabled = true")
    List<ScheduleTomorrowNotification> findByHourForSendAndEnabled(@Param("hour") int hour);

    @Query("select s from ScheduleTomorrowNotification s where s.userId = :userId")
    ScheduleTomorrowNotification findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ScheduleTomorrowNotification set isEnabled = true where userId = :userId")
    void enableScheduleTomorrowNotificationByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("update ScheduleTomorrowNotification set isEnabled = false where userId = :userId")
    void disableScheduleTomorrowNotificationByUserId(@Param("userId") String userId);
}
