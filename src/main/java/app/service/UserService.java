package app.service;

import java.util.List;

import app.model.User;

public interface UserService {

	User save(User user);

	List<User> findAll();

	User getUserByEmail(String name);



}
