package com.Assessment_employability.projects.infrastructure.adapter.out.persistence;

import com.Assessment_employability.projects.domain.model.User;
import com.Assessment_employability.projects.domain.port.out.UserRepositoryPort;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.Assessment_employability.projects.infrastructure.adapter.out.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter: UserRepositoryAdapter
 * Implements UserRepositoryPort using Spring Data JPA
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity;
        // Check if it's a new entity or an update
        if (user.getId() != null && jpaUserRepository.existsById(user.getId())) {
            // Updating existing entity - fetch and update
            entity = jpaUserRepository.findById(user.getId()).orElseThrow();
            entity.setUsername(user.getUsername());
            entity.setEmail(user.getEmail());
            entity.setPassword(user.getPassword());
        } else {
            // New entity - let JPA generate the ID
            entity = userMapper.toEntity(user);
        }
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}

