package lk.ijse.gdse.HealthTheraphyCenter.bo.custom.impl;

import lk.ijse.gdse.HealthTheraphyCenter.Entity.User;
import lk.ijse.gdse.HealthTheraphyCenter.bo.custom.UserBO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoFactory;
import lk.ijse.gdse.HealthTheraphyCenter.dao.DaoTypes;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.TherapistDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dao.custom.UserDAO;
import lk.ijse.gdse.HealthTheraphyCenter.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBOImpl implements UserBO {
    UserDAO userDAO = DaoFactory.getInstance().getDAO(DaoTypes.USER);
    @Override
    public boolean saveUser(UserDto userDto) throws Exception {
        String newUserId = generateNewUserId();
        userDto.setId(newUserId);

        User user = new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getUsername(),
                userDto.getPassword()
        );

        return userDAO.save(user);
    }

    @Override
    public boolean updateUser(UserDto userDto) {

        User user = new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getUsername(),
                userDto.getPassword()
        );

        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(String id) throws Exception {
        boolean isDeleted = userDAO.deleteByPK(id);
        if (!isDeleted) {
            throw new Exception("User with ID " + id + " not found or could not be deleted.");
        }
        return isDeleted;
    }

    @Override
    public List<UserDto> getAllUsers() {

        List<User> users = userDAO.getAll();
        List<UserDto> userDtos = new ArrayList<>();


        for (User user : users) {
            userDtos.add(new UserDto(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword()
            ));
        }
        return userDtos;
    }

    @Override
    public UserDto getUserById(String id) {

        Optional<User> optionalUser = userDAO.findByPK(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new UserDto(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword()
            );
        }
        return null; // Return null if user is not found
    }

    @Override
    public int getUserCount() throws Exception {

        return userDAO.getUserCount();
    }

    @Override
    public UserDto getUserByName(String username) throws Exception {

        Optional<User> optionalUser = userDAO.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new UserDto(
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword()
            );
        }
        return null;
    }
    private String generateNewUserId() throws Exception {
        String lastId = userDAO.getLastUserId();
        if (lastId == null) {
            return "U001";
        } else {
            int lastNum = Integer.parseInt(lastId.substring(1));
            return String.format("U%03d", lastNum + 1);
        }
    }


}
