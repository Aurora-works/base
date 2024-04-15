package org.aurora.base.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiresAuthentication
public class IndexController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }
}
