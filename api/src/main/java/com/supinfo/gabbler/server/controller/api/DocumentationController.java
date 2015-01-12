package com.supinfo.gabbler.server.controller.api;

import com.mangofactory.swagger.annotations.ApiIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ApiIgnore
@RequestMapping("/api/docs")
public class DocumentationController {

    @RequestMapping("/rest")
    public ModelAndView apiDocumentation(){
        return new ModelAndView("redirect:/sdoc.jsp");
    }

}
