package com.rangel.courtflow.infrastructure.web.mapper;

import com.rangel.courtflow.domain.model.User;
import com.rangel.courtflow.infrastructure.persistence.UserJpaEntity;
import com.rangel.courtflow.infrastructure.web.dto.UserResponseDTO;

public class UserMapper {

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    public static UserJpaEntity toJpaEntity(User user) {
        if (user == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());

        return entity;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}