package com.book.identityservice.mapper;


import com.book.identityservice.dto.request.PermissionRequest;
import com.book.identityservice.dto.response.PermissionResponse;
import com.book.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
