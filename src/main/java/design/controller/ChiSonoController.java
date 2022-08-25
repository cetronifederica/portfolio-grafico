package design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chiSono")
public class ChiSonoController {

	@GetMapping
	public String chiSono() {
		return "/chiSono/chiSono";
	}
}
