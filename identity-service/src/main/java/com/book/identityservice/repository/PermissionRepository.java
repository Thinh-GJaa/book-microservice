package com.book.identityservice.repository;

import com.book.identityservice.entity.Permission;
import com.book.identityservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    

}