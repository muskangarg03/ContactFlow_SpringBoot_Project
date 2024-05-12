package com.demo.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.dao.UserRepository;
import com.demo.dao.ContactRepository;
import com.demo.entities.Contact;
import com.demo.entities.EmailForm;
import com.demo.entities.User;
import com.demo.helper.Message;
import com.demo.service.EmailService;
import com.demo.service.TwilioService;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
//	@Autowired
//	private EmailValidationService emailValidationService; 
	
	@Autowired
    private TwilioService twilioService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private EmailService emailService;

	
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal){
		//get the user by username(email)
		
			String userName = principal.getName();
			System.out.println("USERNAME: "+userName);
			
			User user = userRepository.getUserByUserName(userName);
			System.out.println("USER: "+user);
			
			model.addAttribute("user",user);        
	}
	
	
	//dashboard home
	@RequestMapping("/index")    //URL like: user/index
	public String dashboard(Model model, Principal principal)   //principal is used to get the unique entity
	{	
		model.addAttribute("title","User Dashboard");
		return "user/user_dashboard";
	}
	
	
	//open add from handler
	@GetMapping("/add-contact")
	public String openAllContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		return "user/add_contact_form";
	} 
	
	
	//processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile file, 
			BindingResult result, Principal principal, 
			HttpSession session, Model model,
			@RequestParam("email") String email) {
        
		if (result.hasErrors()) {
            // If there are validation errors, return to the add contact form
            model.addAttribute("title", "Add Contact");
            return "user/add_contact_form";
        }
        
        try {
        // If validation is successful, process the contact
        String name = principal.getName();
        
        
//        boolean isValidEmail = emailValidationService.validateEmail(email);
//        if (!isValidEmail) {
//            // If the email is not valid, return to the add contact form with an error message
//            session.setAttribute("message", new Message("Enter a valid email id!!","danger"));           
//            		return "user/add_contact_form";
//        }
        
        User user = userRepository.getUserByUserName(name);
        
        //processing and uploading file image
        if(file.isEmpty())
        {
        	System.out.println("File is Empty!!");
        	contact.setImage("default.png");
        }
        else {
        	//upload file to folder and update the name to contact in database
        	contact.setImage(file.getOriginalFilename());
        	File saveFile=new ClassPathResource("static/img").getFile();
        	
        	Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        	System.out.println("Imgae is Uploaded..");
        }
        
        
        user.getContacts().add(contact);
        contact.setUser(user);
        
        this.userRepository.save(user);

        System.out.println("Data: " + contact);
        System.out.println("Added to the database");
        
        //success message to user
        session.setAttribute("message", new Message("Your contact is added successfully!!","success"));
        }
        catch(Exception e){
        	System.out.println("ERROR :"+e.getMessage());
        	e.printStackTrace();
        	
        	//error message to user
        	session.setAttribute("message", new Message("Something went wrong, Try Again!!","danger"));
        }
		return "user/add_contact_form";
	}
	
	
	//show all contacts
	//contact per page = 5[n]
	//current page=0[page]
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		m.addAttribute("title", "Show User Contacts");
		
		//Show contact list of the user
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		PageRequest pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage",page);
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		return "user/show_contacts";
	}
	
	
	//Showing Specific User Details
	@RequestMapping("/{cid}/contact")
	public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal){
		System.out.println("CID: "+cid);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		
		String userName= principal.getName();
		User user= this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title",contact.getName());
		}
		return "user/contact_detail";
	}
	
	
	//delete contact handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session, Principal principal) {
		//Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = this.contactRepository.findById(cid).get();
		System.out.println("Contact: " +contact.getCid());		
		
		String userName= principal.getName();
		User user= this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{ 
			//contact.setUser(null);      //unlinking the user with contact
			//this.contactRepository.delete(contact);
			
			user.getContacts().remove(contact);	
			this.userRepository.save(user);
			System.out.println("DELETED");
			session.setAttribute("message", new Message("Contact Deleted Successfully...","success"));
		}
		// Redirect to the first page after deletion  
	    return "redirect:/user/show-contacts/0";
	}
	
	
	//Open Update Form controller
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model m)
	{	
		m.addAttribute("title", "Update Contacts");
		
		Contact contact = this.contactRepository.findById(cid).get();
		m.addAttribute("contact",contact);
		return "user/update_form";
	}
	
	
	//Update Contact Handler
	@RequestMapping(value="/process-update", method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file, Model m, 
			Principal principal, HttpSession session) 
	{
		try {
			Contact oldContactDetail = this.contactRepository.findById(contact.getCid()).get();
			if(!file.isEmpty()) {
				//rewrite the new file
				
				//delete old photo
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
				
				//update new photo
				File saveFile=new ClassPathResource("static/img").getFile();
	        	Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
	        	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        	contact.setImage(file.getOriginalFilename());
			}
			else {
				contact.setImage(oldContactDetail.getImage());
			}
			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your contact is Updated!!","success"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Contact Name: "+contact.getName());
		System.out.println("Contact Id: "+contact.getCid());
		m.addAttribute("title", "Update");
		return "redirect:/user/"+contact.getCid()+"/contact";
	}
	
	//View Profile Handler
	@GetMapping("/profile")
	public String yourProfile(Model model) 
	{
		model.addAttribute("title", "Profile Page");
		return "user/profile";
	}
	
	
	//open settings handler
	@GetMapping("/settings")
	public String openString() 
	{
		return "user/settings";
	}
	
	
	//change password handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword
			,Principal principal, HttpSession session)
	{
		System.out.println("Old Password: "+oldPassword);
		System.out.println("New Password: "+newPassword);
		
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			//change password
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your password is successfully changed!!","success"));
		}else
		{
			//error..
			session.setAttribute("message", new Message("Please Enter your correct old password !!","danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
	
	
	// Make Call
	@GetMapping("/make-call/{phoneNumber}")
	public String makeCall(@PathVariable String phoneNumber) {
	    String message = "ContactFlow"; // Define your default message here
	    twilioService.makeVoiceCall(phoneNumber, message);
	    System.out.println("Call initiated to " + phoneNumber + " with message: " + message);
	    
	    return "redirect:/user/show-contacts/0";
	}
	
	@GetMapping("/compose-email/{email}")
	public String showComposeEmailForm(@PathVariable String email, Model model) {
	    model.addAttribute("emailForm", new EmailForm());
	    model.addAttribute("recipient", email); 
	    return "user/compose_email_form"; 
	}

	
	@PostMapping("/send-email")
    public String sendEmail(@ModelAttribute("emailForm") EmailForm emailForm, Model model,
    		HttpSession session, Principal principal) {
		User userEmail = userRepository.getUserByUserName(principal.getName());
		
        String recipient = emailForm.getRecipient();
        String subject = emailForm.getSubject();
        String message = emailForm.getMessage();
        

        boolean isEmailSent = emailService.sendEmail(subject, message, recipient);
        
        if(isEmailSent)
        	session.setAttribute("message", new Message("Email Sent Successfully!!","success"));
        else
        	session.setAttribute("message", new Message("An error occurred, Try Again!!","danger"));

        return "redirect:/user/show-contacts/0";
    }
}
	
	

	

