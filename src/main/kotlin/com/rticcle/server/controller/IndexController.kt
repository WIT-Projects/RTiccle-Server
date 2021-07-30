package com.rticcle.server.controller

import com.rticcle.server.config.auth.dto.SessionUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpSession

@Controller
class IndexController {

    @Autowired
    private lateinit var httpSession: HttpSession

    @GetMapping("/")
    fun index(model: Model): String {
        val sessionUser: SessionUser? = httpSession.getAttribute("user") as SessionUser?
        if(sessionUser != null){
            model.addAttribute("userName", sessionUser.name)
        }
        return "index"
    }

}