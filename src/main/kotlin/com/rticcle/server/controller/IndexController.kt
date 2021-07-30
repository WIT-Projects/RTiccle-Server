package com.rticcle.server.controller

import com.rticcle.server.config.auth.LoginUser
import com.rticcle.server.config.auth.dto.SessionUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpSession

@Controller
class IndexController {

    @GetMapping("/")
    fun index(model: Model, @LoginUser sessionUser: SessionUser?): String {
        if(sessionUser != null){
            model.addAttribute("userName", sessionUser.name)
        }
        return "index"
    }

}