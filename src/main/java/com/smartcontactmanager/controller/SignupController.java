package com.smartcontactmanager.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;

@Controller
public class SignupController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/register")
	//handler  for registerUser
	public String registerUser(@ModelAttribute("userdata") User user, Model model, HttpSession session) {
		try {
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("defult.png");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			
			User result = this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Sucessfully Registered !!" , "alert-success"));
			
			System.out.println("user" + user);
			System.out.println("userresult-->" + result);
			return "signup";
		} catch (Exception e) {
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrongn !!" + e.getMessage(), "alert-danger"));
			e.printStackTrace();
			return "signup";

		}
		
		
	}
	
}
