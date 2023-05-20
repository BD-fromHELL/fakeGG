package com.BDFH.fakeGG.repository;

import com.BDFH.fakeGG.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
