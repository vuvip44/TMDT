package com.vuviet.ThuongMai.service;

import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Permission;
import com.vuviet.ThuongMai.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission){
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod());
    }

    public Permission getById(long id){
        Optional<Permission> permission=this.permissionRepository.findById(id);
        if(permission.isPresent()){
            return permission.get();
        }
        return null;
    }

    public Permission create(Permission permission){
        return this.permissionRepository.save(permission);
    }

   public Permission update(Permission permissionDTO){
        Permission permission=this.getById(permissionDTO.getId());
        permission.setName(permissionDTO.getName());
        permission.setMethod(permissionDTO.getMethod());
        permission.setModule(permissionDTO.getModule());
        permission.setApiPath(permissionDTO.getApiPath());
        this.permissionRepository.save(permission);
        return permission;
   }

   public void delete(long id){
        this.permissionRepository.deleteById(id);
   }

   public ResultPageDTO getAll(Specification<Permission> spec, Pageable pageable){
       Page<Permission> pagePer=this.permissionRepository.findAll(spec,pageable);

       ResultPageDTO rs=new ResultPageDTO();
       ResultPageDTO.Meta mt=new ResultPageDTO.Meta();

       mt.setPage(pageable.getPageNumber()+1);
       mt.setPageSize(pageable.getPageSize());
       mt.setTotalPage(pagePer.getTotalPages());
       mt.setTotalElement(pagePer.getTotalElements());

       rs.setMeta(mt);
       rs.setResult(pagePer.getContent());

       return rs;
   }
}
