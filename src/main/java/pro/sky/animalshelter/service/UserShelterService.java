package pro.sky.animalshelter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.animalshelter.dto.UserShelterDTO;
import pro.sky.animalshelter.exception.ShelterNotFoundException;
import pro.sky.animalshelter.exception.UserNotFoundException;
import pro.sky.animalshelter.model.*;
import pro.sky.animalshelter.repository.UserCatShelterRepository;
import pro.sky.animalshelter.repository.UserDogShelterRepository;

import java.util.List;
import java.util.stream.Collectors;

import static pro.sky.animalshelter.Constants.TextConstants.*;

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

    public void saveUserContacts(UserShelterDTO userShelterDTO) {
        ShelterType shelterType = userShelterDTO.getShelterType();
        Shelter shelter = shelterService.findShelterByShelterType(shelterType);
        if (shelterType == ShelterType.DOG_SHELTER) {
            UserDogShelter userDogShelter = userDogShelterRepository.findByTelegramId(userShelterDTO.getTelegramId());
            if (userDogShelter == null) {
                userDogShelter = UserDogShelter.fromDTO(userShelterDTO);
                userDogShelter.setShelter(shelter);
            } else {
                userDogShelter.fillUserInfo(userShelterDTO);
            }
            userDogShelterRepository.save(userDogShelter);
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            UserCatShelter userCatShelter = userCatShelterRepository.findByTelegramId(userShelterDTO.getTelegramId());
            if (userCatShelter == null) {
                userCatShelter = UserCatShelter.fromDTO(userShelterDTO);
                userCatShelter.setShelter(shelter);
            } else {
                userCatShelter.fillUserInfo(userShelterDTO);
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
                    .map(UserDogShelter::toDTO)
                    .collect(Collectors.toList());
            logger.info("The list users of Dog Shelter ({}) has been successfully created", shelter.getShelterName());
            return userShelterDTOList;
        } else if (shelterType == ShelterType.CAT_SHELTER) {
            userShelterDTOList = userCatShelterRepository.findAllByShelter(shelter)
                    .stream()
                    .map(UserCatShelter::toDTO)
                    .collect(Collectors.toList());
            logger.info("The list users of Cat Shelter ({}) has been successfully created", shelter.getShelterName());
            return userShelterDTOList;
        }
        logger.error("Shelter type {} not supported. The list users can not be created", shelterType);
        throw new ShelterNotFoundException(String.format(SHELTER_TYPE_NOT_SUPPORTED_MESSAGE, shelterType));
    }

    public UserCatShelter findUserCatShelterById(Integer userId) {
        return userCatShelterRepository.findById(userId)
                .orElseThrow (() -> new UserNotFoundException(
                        String.format(CAT_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, userId)));
    }

    public UserDogShelter findUserDogShelterById(Integer userId) {
        return userDogShelterRepository.findById(userId)
                .orElseThrow (() -> new UserNotFoundException(
                        String.format(DOG_SHELTER_USER_BY_ID_NOT_FOUND_MESSAGE, userId)));
    }

}
