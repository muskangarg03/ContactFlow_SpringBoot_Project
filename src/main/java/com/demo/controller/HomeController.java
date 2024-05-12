package com.demo.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.dao.UserRepository;
import com.demo.entities.User;
import com.demo.helper.Message;
import com.demo.service.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    private Random random = new Random();

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // home controller
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    // about controller
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    // signup controller
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // handle the registering user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
            HttpSession session) {

        if (!agreement) {
            System.out.println("You have not agreed to the terms and conditions");
            session.setAttribute("message",
                    new Message("Please agree to the terms and conditions", "danger"));
            return "signup"; // Return to the registration form with an error message
        }

        if (result1.hasErrors()) {
            System.out.println("ERROR: " + result1.toString());
            model.addAttribute("user", user);
            return "signup";
        }

        try {
            // Generate OTP
            int otp = random.nextInt(900000) + 100000; // Generates a 6-digit OTP
            System.out.println("OTP: " + otp);

            // Send OTP to user's email
            emailService.sendEmail("OTP from ContactFlow", "Your OTP for registration is: " + otp, user.getEmail());

            // Store OTP in session for validation
            session.setAttribute("otp", otp);
            session.setAttribute("user", user);

            return "verify_regotp"; // Redirect to OTP verification page
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "danger"));
            return "signup";
        }
    }

    @PostMapping("/verify_regotp")
    public String verifyOTP(@RequestParam("otp") int otp, HttpSession session, Model model) {
        int storedOTP = (int) session.getAttribute("otp");
        User user = (User) session.getAttribute("user");

        if (storedOTP == otp) {
            // OTP verified, save user data to the database
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("user.png");
            userRepository.save(user);

            session.removeAttribute("otp");
            session.removeAttribute("user");
            
            session.setAttribute("message", new Message("Registration successful!", "success"));
            return "redirect:/signup";
        } else {
        	session.setAttribute("message", "You have entered wrong otp!! ");
            return "verify_regotp";
        }
    }

    // Login Handler
    @GetMapping("/signin")
    public String cutomLogin(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }

}
