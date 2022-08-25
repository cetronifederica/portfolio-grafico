package design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contatti")
public class ContattiController {

	@GetMapping
	public String contatti() {
		return "/contatti/contatti";
	}
}