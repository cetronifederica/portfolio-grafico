package design.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import design.model.Lavori;
import design.repository.LavoriRepo;

@Controller
@RequestMapping("/lavori")
public class LavoriController {

	@Autowired
	private LavoriRepo repo;

	@GetMapping
	public String lavori(Model model) {
		model.addAttribute("lavoriList", repo.findAll());
		return "/lavori/lavori";
	}

	@GetMapping("/add")
	public String serviziForm(Model model) {
		model.addAttribute("newLavoro", new Lavori());
		return "/lavori/add";
	}

	// Aggiunta del lavoro al database
	@PostMapping("/add")
	public String save(@Valid @ModelAttribute("lavori") Lavori lavoroForm, BindingResult br, Model model) {
		boolean hasErrors = br.hasErrors();
		boolean validateName = true;
		if (lavoroForm.getId() != null) { // edit
			Lavori lavoroBefUpdate = repo.findById(lavoroForm.getId()).get();
			if (lavoroBefUpdate.getName().equalsIgnoreCase(lavoroForm.getName())) {
				validateName = false;
			}
		}
		// se il nome è univoco
		if (validateName && repo.countByName(lavoroForm.getName()) > 0) {
			br.addError(new FieldError("lavoro", "name", "Name must be unique"));
			hasErrors = true;
		}

		if (hasErrors) {
			// se non ci sono errori non salvo il servizio nel database
			return "lavori/add";
		} else {
			try {
				repo.save(lavoroForm);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Unable to save the Work");
				return "lavori/add";
			}
			return "redirect:/";
		}

	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer lavoriId, Model model) {
		Optional<Lavori> result = repo.findById(lavoriId);
		if (result.isPresent()) {
			model.addAttribute("lavori", result.get());
			return "/lavori/add";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The work with id " + lavoriId + " is not present");
		}
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") RedirectAttributes ra, Integer lavoriId) {
		Optional<Lavori> result = repo.findById(lavoriId);
		if (result.isPresent()) {
			repo.delete(result.get());
			ra.addFlashAttribute("successMessage", "Work " + result.get().getName() + " deleted!");
			return "redirect:/";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The work with id " + lavoriId + " is not present");
		}
	}
}
