package design.repository;

import org.springframework.data.repository.CrudRepository;

import design.model.Servizi;

public interface ServiziRepo extends CrudRepository<Servizi, Integer> {

	public Integer countByName(String name);
}
