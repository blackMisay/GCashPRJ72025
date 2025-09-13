package services;

import models.User;
import utils.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private Map<String, User> users = new HashMap<>();

    public boolean register(String username, String password) {
        if (users.containsKey(username)) return false;
        String hashedPassword = SecurityUtil.hashPassword(password);
        users.put(username, new User(username, hashedPassword));
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPasswordHash().equals(SecurityUtil.hashPassword(password))) {
            return user;
        }
        return null;
    }

    public User findUser(String username) {
        return users.get(username);
    }
}
