package com.vuviet.ThuongMai.controller;

import com.vuviet.ThuongMai.dto.responsedto.ResultPageDTO;
import com.vuviet.ThuongMai.entity.Permission;
import com.vuviet.ThuongMai.service.PermissionService;
import com.vuviet.ThuongMai.util.annotation.ApiMessage;
import com.vuviet.ThuongMai.util.error.IdInValidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@RequestBody @Valid Permission permission) throws IdInValidException {
        if(this.permissionService.isPermissionExist(permission)){
            throw new IdInValidException("Permission này đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permissionDTO) throws IdInValidException{
        Permission permissionRq=this.permissionService.getById(permissionDTO.getId());
        if(permissionRq==null){
            throw new IdInValidException("Id "+permissionDTO.getId()+" không tồn tại");
        }

        if(this.permissionService.isPermissionExist(permissionDTO)){
            throw new IdInValidException("Permission này đã tồn tại");
        }

        return ResponseEntity.ok(this.permissionService.update(permissionDTO));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInValidException{
        Permission permissionRq=this.permissionService.getById(id);
        if(permissionRq==null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Get a permission")
    public ResponseEntity<Permission> getById(@PathVariable("id") long id) throws IdInValidException{
        Permission permissionRq=this.permissionService.getById(id);
        if(permissionRq==null){
            throw new IdInValidException("Id "+id+" không tồn tại");
        }
        return ResponseEntity.ok(permissionRq);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all permissions")
    public ResponseEntity<ResultPageDTO> getAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable
            ){
        return ResponseEntity.ok(this.permissionService.getAll(spec,pageable));
    }
}
