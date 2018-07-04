package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

@Controller
public class HomeController {
    @Autowired
    MessageRepository messages;

    @Autowired
    RecipientRepository recipientList;

    @RequestMapping("/")
    public String index(Model model)
    {
        model.addAttribute("recipientlist", recipientList.findAll());
        model.addAttribute("messages", messages.findAllByOrderBySentDateDesc());
        return "messagelist";
    }

    @GetMapping("/addmessage")
    public String addPersonColorInfo(Model model)
    {
        model.addAttribute("message", new Message());
        model.addAttribute("recipientlist", recipientList.findAll());
        return "messageform";
    }

    @PostMapping("/savemessage")
    public String savePersonColorInfo(@ModelAttribute("message") Message message)
    {
        message.setSentDate(LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss")));
        messages.save(message);
        return "redirect:/";
    }

    @GetMapping("/updaterecipientlist")
    public String updateColorList(Model model)
    {
        model.addAttribute("recipient", new Recipient());
        return "recipientslist";
    }

    @PostMapping("/saverecipientlist")
    public String updateColorList(@ModelAttribute("recipient") Recipient recipient)
    {
        recipientList.save(recipient);
        return "redirect:/addmessage";
    }

    @RequestMapping("/like/{id}")
    public String likeCounter(@PathVariable("id") long id){
        Message message =  messages.findById(id).get();
        message.setLikeCounter(message.getLikeCounter() + 1);
        messages.save(message);
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage( @PathVariable("id") long id, Model model){
        model.addAttribute("message", messages.findById(id).get());
        model.addAttribute("recipientlist", recipientList.findAll());
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public  String deleteMessage(@PathVariable("id") long id){
        messages.deleteById(id);
        return "redirect:/";
    }

    @PostConstruct
    public void fillRecipientList()
    {
        Recipient c;
        ArrayList<String> initialRecipients = new ArrayList<>(Arrays
                .asList("April Wilson", "Cornelius Kersey", "Andrina Augustine",
                        "Joan Hicks", "Isabelle Masters", "Ken Hart"));

        for (String recipient : initialRecipients) {
            c = new Recipient();
            c.setRecipientName(recipient);
            recipientList.save(c);
        }
    }

    @RequestMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        //Get the search string from the result form
        String searchString = request.getParameter("search");
        model.addAttribute("search", searchString);
        model.addAttribute("messages", 
                messages.findAllByContentContainingIgnoreCase(searchString));
        return "messagelist";
    }
}
