package com.example.ThuongMai.controller;

import com.example.ThuongMai.dto.ResultPageDTO;
import com.example.ThuongMai.entity.Role;
import com.example.ThuongMai.service.RoleService;
import com.example.ThuongMai.util.annotation.ApiMessage;
import com.example.ThuongMai.util.error.IdInValidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@RequestBody @Valid Role role) throws IdInValidException {
        if(this.roleService.isExistRole(role)){
            throw new IdInValidException("Role với name "+role.getName()+" đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable long id) throws IdInValidException {
        Role role=this.roleService.getRole(id);

        if(role==null){
            throw new IdInValidException("Id "+ id+" không tồn tại");
        }
        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPageDTO> getAllRoles(
            @Filter Specification<Role> spec,
            Pageable pageable
    ){
        return ResponseEntity.ok(this.roleService.getAll(spec, pageable));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role roleDTO) throws IdInValidException {

        if(this.roleService.getRole(roleDTO.getId())==null){
            throw new IdInValidException("Role "+roleDTO.getId()+" không tồn tại");
        }
        return ResponseEntity.ok(this.roleService.updateRole(roleDTO));
    }
}
