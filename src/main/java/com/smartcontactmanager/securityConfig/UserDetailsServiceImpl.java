package com.smartcontactmanager.securityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;

public class UserDetailsServiceImpl implements  UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching  user  from database 
		
		 User userByUserName = userRepository.getUserByUserName(username);
		 if(userByUserName==null)
		 {
			 throw new UsernameNotFoundException("could not found user !!");
		 }
		 CustomUserDetail customUserDetail=new CustomUserDetail(userByUserName);
		return customUserDetail;
	}

}

