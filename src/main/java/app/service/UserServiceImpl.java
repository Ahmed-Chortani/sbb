package app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.model.User;
import app.util.PasswordUtil;
import dao.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired UserRepository userRepository;

	@Override
	public User save(User user) {
		String password = PasswordUtil.getPasswordHash(user.getPassword());
		user.setPassword(password);
		user.setCreatedAt(new Date());
		return userRepository.save(user);
	}
	
	@Override 
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override 
	public User getUserByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email);
	}
}
