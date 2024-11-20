package com.example.wewear_backend.Repository;

import com.example.wewear_backend.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification,Integer> {
}
