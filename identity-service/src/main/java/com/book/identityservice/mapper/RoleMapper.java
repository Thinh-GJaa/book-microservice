package com.book.identityservice.mapper;

import com.book.identityservice.dto.request.RoleRequest;
import com.book.identityservice.dto.response.RoleResponse;
import com.book.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
