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

import design.model.Servizi;
import design.repository.ServiziRepo;

@Controller
@RequestMapping("/servizi")
public class ServiziController {

	@Autowired
	private ServiziRepo repo;

	@GetMapping
	public String servizi(Model model) {
		model.addAttribute("serviziList", repo.findAll());
		return "/servizi/servizi";
	}

	@GetMapping("/add")
	public String serviziForm(Model model) {
		model.addAttribute("newServizio", new Servizi());
		return "/servizi/add";
	}

	// Aggiunta del servizio al database
	@PostMapping("/add")
	public String save(@Valid @ModelAttribute("servizi") Servizi servizioForm, BindingResult br, Model model) {
		boolean hasErrors = br.hasErrors();
		boolean validateName = true;
		if (servizioForm.getId() != null) { // edit
			Servizi servizioBefUpdate = repo.findById(servizioForm.getId()).get();
			if (servizioBefUpdate.getName().equalsIgnoreCase(servizioForm.getName())) {
				validateName = false;
			}
		}
		// se il nome è univoco
		if (validateName && repo.countByName(servizioForm.getName()) > 0) {
			br.addError(new FieldError("servizio", "name", "Name must be unique"));
			hasErrors = true;
		}

		if (hasErrors) {
			// se non ci sono errori non salvo il servizio nel database
			return "servizi/add";
		} else {
			try {
				repo.save(servizioForm);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Unable to save the Service");
				return "servizi/add";
			}
			return "redirect:/";
		}

	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer serviziId, Model model) {
		Optional<Servizi> result = repo.findById(serviziId);
		if (result.isPresent()) {
			model.addAttribute("servizi", result.get());
			return "/servizi/add";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Il servizio con id " + serviziId + " non è presente");
		}
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") RedirectAttributes ra, Integer serviziId) {
		Optional<Servizi> result = repo.findById(serviziId);
		if (result.isPresent()) {
			repo.delete(result.get());
			ra.addFlashAttribute("successMessage", "Service " + result.get().getName() + " deleted!");
			return "redirect:/";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service with id " + serviziId + " is not present");
		}
	}
}
