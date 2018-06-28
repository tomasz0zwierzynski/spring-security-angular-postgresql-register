package org.complex.service;

import org.complex.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
