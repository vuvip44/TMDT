package com.example.ThuongMai.service;

import com.example.ThuongMai.dto.ResultPageDTO;
import com.example.ThuongMai.entity.Permission;
import com.example.ThuongMai.entity.Role;
import com.example.ThuongMai.repository.PermissionRepository;
import com.example.ThuongMai.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role createRole(Role roleDTO) {
        if(roleDTO.getPermissions()!=null){
            List<Long> reqPermission=roleDTO.getPermissions()
                    .stream().map(x->x.getId()).toList();

            List<Permission> permissions=permissionRepository.findAllById(reqPermission);
            roleDTO.setPermissions(permissions);
        }
        return this.roleRepository.save(roleDTO);
    }

    public Role getRole(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(Role roleDTO) {
        Role role = this.getRole(roleDTO.getId());
        if(roleDTO.getPermissions()!=null){
            List<Long> reqPermission=roleDTO.getPermissions()
                    .stream().map(x->x.getId()).toList();

            List<Permission> permissions=permissionRepository.findAllById(reqPermission);
            roleDTO.setPermissions(permissions);
        }

        role.setName(roleDTO.getName());
        role.setActive(roleDTO.isActive());
        role.setPermissions(roleDTO.getPermissions());

        this.roleRepository.save(role);

        return role;
    }

    public void deleteRole(Long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPageDTO getAll(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRoles = roleRepository.findAll(spec, pageable);

        ResultPageDTO res=new ResultPageDTO();
        ResultPageDTO.Meta meta=new ResultPageDTO.Meta();

        meta.setPage(pageRoles.getNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPage(pageRoles.getTotalPages());
        meta.setTotalElement(pageRoles.getTotalElements());

        res.setMeta(meta);
        res.setResult(pageRoles.getContent());

        return res;
    }

    public boolean isExistRole(Role role) {
        return this.roleRepository.existsByName(role.getName());
    }
}
