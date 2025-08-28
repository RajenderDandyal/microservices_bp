package com.yourcompany.user.service.repos;

import com.yourcompany.user.service.entities.UserE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserE, String> {
}
