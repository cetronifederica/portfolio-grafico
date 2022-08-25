package design.repository;

import org.springframework.data.repository.CrudRepository;

import design.model.Lavori;

public interface LavoriRepo extends CrudRepository<Lavori, Integer> {

	public Integer countByName(String name);
}
