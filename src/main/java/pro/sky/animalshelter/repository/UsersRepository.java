package pro.sky.animalshelter.repository;

import pro.sky.animalshelter.model.UserShelter;

import java.util.Collection;

public interface UsersRepository {

    Collection<UserShelter> findUserById(Integer userId);

}
