package spittr.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import spittr.data.Spitter;
import spittr.data.SpitterRepository;

@Controller
@RequestMapping("/spitter")
public class SpitterController {
    
    private SpitterRepository repository;
    
    @Autowired
    public SpitterController(SpitterRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationForm() {
        return "registerForm";
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistration(@Valid Spitter spitter, Errors errors) {
        
        if(errors.hasErrors()) {
            return "registerForm";
        }
        repository.save(spitter);
        return "redirect:/spitter/" + spitter.getUserName();
    }
    
    @RequestMapping(value="/{username}", method = RequestMethod.GET)
    public String showSpitterProfile(@PathVariable String username, Model model) {
        Spitter spitter = repository.findByUsername(username);
        model.addAttribute(spitter);
        return "profile";
    }
}
