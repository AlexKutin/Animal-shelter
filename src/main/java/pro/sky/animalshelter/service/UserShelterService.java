package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.UserCatShelterRepository;
import pro.sky.animalshelter.repository.UserDogShelterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserShelterService {
    private final Logger logger = LoggerFactory.getLogger(UserShelterService.class);

    private final UserDogShelterRepository userDogShelterRepository;
    private final UserCatShelterRepository userCatShelterRepository;
    private final ShelterService shelterService;

    @Autowired
    public UserShelterService(UserDogShelterRepository userDogShelterRepository, UserCatShelterRepository userCatShelterRepository, ShelterService shelterService) {
        this.userDogShelterRepository = userDogShelterRepository;
        this.userCatShelterRepository = userCatShelterRepository;
        this.shelterService = shelterService;
    }

    public void saveUserContacts(ShelterType shelterType, Long telegramId, String userName, String firstName, String lastName, String userContacts) {
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        if (shelterType == ShelterType.DOG_SHELTER) {
            UserDogShelter userDogShelter = userDogShelterRepository.findUserDogShelterByTelegramId(telegramId);
            if (userDogShelter == null) {
                userDogShelter = new UserDogShelter(telegramId, userName, firstName, lastName, userContacts, shelter);
            } else {
                userDogShelter.fillUserInfo(userName, firstName, lastName, userContacts, shelter);
            }
            userDogShelterRepository.save(userDogShelter);
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            UserCatShelter userCatShelter = userCatShelterRepository.findUserCatShelterByTelegramId(telegramId);
            if (userCatShelter == null) {
                userCatShelter = new UserCatShelter(telegramId, userName, firstName, lastName, userContacts, shelter);
            } else {
                userCatShelter.fillUserInfo(userName, firstName, lastName, userContacts, shelter);
            }
            userCatShelterRepository.save(userCatShelter);
        }
    }

    public List<UserShelterDTO> getAllUsersByShelterType(ShelterType shelterType) {
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        List<UserShelterDTO> userShelterDTOList;
        if (shelterType == ShelterType.DOG_SHELTER) {
            userShelterDTOList = userDogShelterRepository.findAllByShelter(shelter)
                    .stream()
                    .map(UserShelterDTO::fromUserShelter)
                    .collect(Collectors.toList());
            logger.info("The list users of Dog Shelter ({}) has been successfully created", shelter.getShelterName());
            return userShelterDTOList;
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            userShelterDTOList = userCatShelterRepository.findAllByShelter(shelter)
                    .stream()
                    .map(UserShelterDTO::fromUserShelter)
                    .collect(Collectors.toList());
            logger.info("The list users of Cat Shelter ({}) has been successfully created", shelter.getShelterName());
            return userShelterDTOList;
        }
        logger.error("Shelter type {} not supported. The list users can not be created", shelterType);
        throw new ShelterNotFoundException(String.format("Shelter type: %s not supported yet", shelterType));
    }
}
