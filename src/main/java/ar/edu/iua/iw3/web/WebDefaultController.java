package ar.edu.iua.iw3.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebDefaultController {

	@GetMapping(value = "/")
	public String defaultPage() {
		return "redirect:/index.html";
	}

}
