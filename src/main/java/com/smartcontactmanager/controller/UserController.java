package com.smartcontactmanager.controller;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.dao.ContactRepository;
import com.smartcontactmanager.dao.UserRepository;
import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.User;
import com.smartcontactmanager.helper.Message;
import com.sun.xml.bind.api.impl.NameConverter.Standard;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired private UserRepository userRepository;
	@Autowired  private ContactRepository contactRepository;
	
	// method  for  adding  common  data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal)
	{
		//get the user  using  username(email)
		String username = principal.getName();
		System.out.println("USERNAME--"+username);
		
		   User user = userRepository.getUserByUserName(username);
		   System.out.println("UserDetails"+user);
		   model.addAttribute("user",user);
	}

	
	
	//dashboard  home
	@RequestMapping("/userDashboard")
	
	public String userDashboard(Model model,Principal principal)
	{
		model.addAttribute("title","UserDashboard");
		return  "norml/userDashboard" ;
	}
	
	
	
	//open  add form  handler
	
	@GetMapping("/addContact")
	public String  openAddContactForm(Model model )
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "norml/addContactForm";
	}
	
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage")MultipartFile file,  
			Principal principal ,HttpSession session) {
		
		try {
		String name = principal.getName();
		User userByUserName = this.userRepository.getUserByUserName(name);
		
		/*if(3>2)
		{
			throw new Exception();
			UI par alert  print karwane kai liye server side validation .....something  
		}*/
		
		//processing  and uploading  file...
		if(file.isEmpty())
		{
			//if the  file  is empty  then try our message..
		}
		else {
			//file  the  file  to  folder  and  update  the name  to contact..
			
			contact.setImage(file.getOriginalFilename());
			File saveFile=new ClassPathResource("/static/img").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		  Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		  
		  System.out.println("Image is uploaded");
		}
		
		//by  directional mapping  contact ko user and user ko contact...
		  contact.setUser(userByUserName);
		
		userByUserName.getContacts().add(contact);
		 this.userRepository.save(userByUserName);
		
		System.out.println("data add sucessfully !!!");
		
		
		System.out.println("Name--->"+name);
		System.out.println("contact--->"+contact);
		
		//message  success...
		session.setAttribute("message", new Message("Your Contact is  added !! And  More..","success") );
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//message error...
			
		session.setAttribute("message", new Message("Some went  wrong  !! Try again ","danger") );
			
					
		}
		return "norml/addContactForm";
	}
	
	//show  contacts handler...
	/*@GetMapping("/showContacts")
	 * aise bhi kar skte hai....and repository sai bhi kar skte hai
	public String showContacts(Model model,Principal principal)
	{
		String name = principal.getName();
		User userByUserName = this.userRepository.getUserByUserName(name);
		List<Contact> contacts = userByUserName .getContacts();
		
		System.out.println("contacts---"+contacts);
		model.addAttribute("title","Show User Contacts");
		return "norml/showContacts";
	}*/
	
	
	//show  contacts handler...
	//per page=5[n]
	//current page=0[page]
		@GetMapping("/showContacts/{page}")
		 
		public String showContacts(@PathVariable("page")Integer page,  Model model,Principal principal)
		{
			model.addAttribute("title","Show User Contacts");
			//send contact list UI
			String userName = principal.getName();
			User userByUserName = this.userRepository.getUserByUserName(userName);
			int id = userByUserName.getId();
			
			//currentPage-page
			//current Per page-5
			
			Pageable pageable = PageRequest.of(page, 5);
			
			Page<Contact> contacts = this.contactRepository.findContactByUser(id,pageable);
			
			model.addAttribute("contacts",contacts);
			model.addAttribute("currentPage",page);
			model.addAttribute("totalPages",contacts.getTotalPages());
			
			//List<Contact> contacts = this.contactRepository.findContactByUser(id);
			
			//model.addAttribute("contacts",contacts);
			
			return "norml/showContacts";
		}
		
		//showing  particuler  contact  details
		
		@RequestMapping("/{cId}/contact")
		public String showContactDetail(@PathVariable("cId") Integer cId ,Model model)
		{
			Optional<Contact> findById = this.contactRepository.findById(cId);
			
			  Contact contact = findById.get();
			  model.addAttribute("contact",contact);
			model.addAttribute("title","Contact-Details");
			System.out.println("CID---"+cId);
			return"norml/contactDetail";
		}
		
}
