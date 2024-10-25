package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.responsedto.ResCreateUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResUpdateUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResUserDTO;
import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Role;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.repository.RoleRepository;
import com.vuviet.ThuongMai.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;

    }

    public User createUser(User user) {
        if(user.getRole()!=null) {
            Role r=this.roleService.getRole(user.getRole().getId());
            user.setRole(r!=null?r:null);
        }
        return this.userRepository.save(user);
    }

    public User getUserById(long id) {
        Optional<User> user=this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User updateUser(User userDTO) {
        User user=this.getUserById(userDTO.getId());
        if(user!=null){
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setAddress(userDTO.getAddress());
            user.setUrlAvatar(userDTO.getUrlAvatar());
            user.setPhoneNumber(userDTO.getPhoneNumber());

            if(userDTO.getRole()!=null) {
                Role r=this.roleService.getRole(userDTO.getRole().getId());
                user.setRole(r!=null?r:null);
            }
            user = this.userRepository.save(user);
        }
        return user;
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User getByUsername(String email) {
        return this.userRepository.getByEmail(email);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPageDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser=this.userRepository.findAll(spec, pageable);
        ResultPageDTO rs=new ResultPageDTO();
        ResultPageDTO.Meta mt=new ResultPageDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPage(pageUser.getTotalPages());
        mt.setTotalElement(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<ResUserDTO> users=pageUser.getContent()
                .stream().map(x->this.convertToResUserDTO(x)).toList();
        rs.setResult(users);
        return rs;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO rs=new ResUserDTO();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setPassword(user.getPassword());
        rs.setAddress(user.getAddress());
        rs.setPhone(user.getPhoneNumber());
        rs.setCreatedAt(user.getCreatedAt());
        rs.setUpdatedAt(user.getUpdatedAt());
        ResUserDTO.RoleUser role=new ResUserDTO.RoleUser();
        if(user.getRole()!=null) {
            role.setId(user.getRole().getId());
            role.setName(user.getRole().getName());
            rs.setRole(role);
        }
        return rs;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO rs=new ResCreateUserDTO();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setPassword(user.getPassword());
        rs.setAddress(user.getAddress());
        rs.setPhone(user.getPhoneNumber());
        rs.setCreatedAt(user.getCreatedAt());
        return rs;
    }

    public ResUpdateUserDTO convertToResUpdateToUserDTO(User user){
        ResUpdateUserDTO rs=new ResUpdateUserDTO();
        rs.setId(user.getId());
        rs.setName(user.getName());
        rs.setEmail(user.getEmail());
        rs.setPassword(user.getPassword());
        rs.setAddress(user.getAddress());
        rs.setPhone(user.getPhoneNumber());
        rs.setUpdatedAt(user.getUpdatedAt());
        return rs;
    }

    public User getUserByRefreshTokenAndEmail(String token,String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public void updatePassword(String newPassword,User user) {
        user.setPassword(newPassword);
        this.userRepository.save(user);
    }

    public void updateUserToken(String token, String email){
        User user=this.userRepository.getByEmail(email);
        if(user!=null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }


}
