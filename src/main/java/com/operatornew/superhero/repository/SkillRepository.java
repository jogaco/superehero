package com.operatornew.superhero.repository;

import com.operatornew.superhero.model.Skill;
import org.springframework.data.repository.CrudRepository;


public interface SkillRepository extends CrudRepository<Skill, Long> {

    Skill findBySkill(String skill);

}