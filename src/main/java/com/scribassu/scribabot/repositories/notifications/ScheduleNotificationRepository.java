package com.scribassu.scribabot.repositories.notifications;

import com.scribassu.scribabot.entities.notifications.NotificationType;
import com.scribassu.scribabot.entities.notifications.ScheduleNotification;
import com.scribassu.scribabot.model.BotUserSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleNotificationRepository extends JpaRepository<ScheduleNotification, Long> {

    @Query("select s from ScheduleNotification s where s.hourForSend = :hour and s.isEnabled = true and s.notificationType = :notificationType")
    List<ScheduleNotification> findByHourForSendAndEnabled(@Param("hour") int hour, @Param("notificationType") NotificationType notificationType);

    @Query("select s from ScheduleNotification s where s.userId = :userId and s.userSource = :userSource and s.notificationType = :notificationType")
    Optional<ScheduleNotification> findByUserIdAndUserSource(@Param("userId") String userId,
                                                             @Param("userSource") BotUserSource userSource, @Param("notificationType") NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("update ScheduleNotification set isEnabled = true where userId = :userId and userSource = :userSource and notificationType = :notificationType")
    void enableByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource, @Param("notificationType") NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("update ScheduleNotification set isEnabled = false where userId = :userId and userSource = :userSource and notificationType = :notificationType")
    void disableByUserIdAndUserSource(@Param("userId") String userId,
                                      @Param("userSource") BotUserSource userSource, @Param("notificationType") NotificationType notificationType);

    @Modifying
    @Transactional
    @Query("delete from ScheduleNotification where userId = :userId and userSource = :userSource and notificationType = :notificationType")
    void deleteByUserIdAndUserSource(@Param("userId") String userId,
                                     @Param("userSource") BotUserSource userSource, @Param("notificationType") NotificationType notificationType);
}
