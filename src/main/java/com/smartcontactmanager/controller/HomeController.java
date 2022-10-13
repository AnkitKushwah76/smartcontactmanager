package com.smartcontactmanager.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","Home - smart Contact  Manager");
		return "home";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title","Signup - smart Contact  Manager");
		return "signup";
	}
	// handler for  custom login 
	
		@GetMapping("/signin")
		public String customLogin(Model model)
		{
			model.addAttribute("title","Login-Page");
			return "login";
		}
}
