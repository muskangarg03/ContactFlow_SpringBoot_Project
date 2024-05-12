package com.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.dao.UserRepository;
import com.demo.entities.User;
import com.demo.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	Random random = new Random(100000);
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	//email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}
	
	
	//Sending the otp
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		System.out.println("EMAILL "+email);
		
		//generating otp of 4 digits
		
		int otp = random.nextInt(999999);
		System.out.println("OTP: "+otp);
		
		//write code for sending the otp to the email id
		
		String subject="OTP from ContactFlow";
		String message="<h1> Your OTP for Forgot Password: </h2> "+"</n>"+" <h1> OTP = "+otp+"</h1>";
		String to=email;	
		
		boolean flag = this.emailService.sendEmail(subject, message, to);
		if(flag) {
			session.setAttribute("oldOtp",otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			session.setAttribute("message", "Check your email id !!");
			return "forgot_email_form";
		}
	}
	
	//Verify otp
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp, HttpSession session) 
	{
		Integer oldOtp =  (int)session.getAttribute("oldOtp");
		String email = (String)session.getAttribute("email");
		if(oldOtp == otp) 
		{
			User user = this.userRepository.getUserByUserName(email);
			
			if(user == null) {
				//send error message
				session.setAttribute("message","User does not exists with this email !!");
				return "forgot_email_form";
			}
			else {
				//send change password form
			}
			
			
			return "password_change_form";
		}
		else {
			session.setAttribute("message", "You have entered wrong otp!! ");
			return "verify_otp";
		}
	}
	
	
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session)
	{
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bcrypt.encode(newpassword));
		this.userRepository.save(user);
		System.out.println("Password Changed");
		return "redirect:/signin?change=Password changed successfully!!";
	}
	
	
}
