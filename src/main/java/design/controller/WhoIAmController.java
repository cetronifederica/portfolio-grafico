package design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/whoIAm")
public class WhoIAmController {

	@GetMapping
	public String chiSono() {
		return "/whoIAm/whoIAm";
	}
}
