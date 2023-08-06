package pro.sky.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.animalshelter.model.Rules;

public interface RulesRepository extends JpaRepository<Rules, Integer> {

}