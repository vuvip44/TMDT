package com.vuviet.ThuongMai.config;

import com.vuviet.ThuongMai.entity.Category;
import com.vuviet.ThuongMai.entity.Permission;
import com.vuviet.ThuongMai.entity.Role;
import com.vuviet.ThuongMai.entity.User;
import com.vuviet.ThuongMai.repository.PermissionRepository;
import com.vuviet.ThuongMai.repository.RoleRepository;
import com.vuviet.ThuongMai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Database initialization started");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if(countPermissions==0){
            ArrayList<Permission> arr=new ArrayList<>();
            arr.add(new Permission("Create a brand","/api/v1/brands","POST","BRANDS"));
            arr.add(new Permission("Get brand by id","/api/v1/brands/{id}","GET","BRANDS"));
            arr.add(new Permission("Update a brand","/api/v1/brands","PUT","BRANDS"));
            arr.add(new Permission("Get all brands","/api/v1/brands","GET","BRANDS"));
            arr.add(new Permission("Delete a brand","/api/v1/brands/{id}","DELETE","BRANDS"));

            arr.add(new Permission("Create a category","/api/v1/categories","POST","CATEGORIES"));
            arr.add(new Permission("Get category by id","/api/v1/categories/{id}","GET","CATEGORIES"));
            arr.add(new Permission("Get all categories","/api/v1/categories","GET","CATEGORIES"));
            arr.add(new Permission("Update a category","/api/v1/categories","PUT","CATEGORIES"));
            arr.add(new Permission("Delete a category","/api/v1/categories/{id}","DELETE","CATEGORIES"));

            arr.add(new Permission("Create a role","/api/v1/roles","POST","ROLES"));
            arr.add(new Permission("Get role by id","/api/v1/roles/{id}","GET","ROLES"));
            arr.add(new Permission("Get all roles","/api/v1/roles","GET","ROLES"));
            arr.add(new Permission("Update a role","/api/v1/roles","PUT","ROLES"));
            arr.add(new Permission("Delete role","/api/v1/roles/{id}","DELETE","ROLES"));

            arr.add(new Permission("Create a order","/api/v1/orders","POST","ORDERS"));
            arr.add(new Permission("Get order by id","/api/v1/orders/{id}","GET","ORDERS"));
            arr.add(new Permission("Get all orders by user","/api/v1/orders","GET","ORDERS"));
            arr.add(new Permission("Update status order","/api/v1/admin/orders","PUT","ORDERS"));
            arr.add(new Permission("Get all orders by admin","/api/v1/admin/orders","GET","ORDERS"));
            arr.add(new Permission("Cancel a order","/api/v1/admin/orders","PUT","ORDERS"));

            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Fetch all permissions", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permission("Create a product", "/api/v1/products", "POST", "PRODUCTS"));
            arr.add(new Permission("Update a product", "/api/v1/products", "PUT", "PRODUCTS"));
            arr.add(new Permission("Get product detail by id", "/api/v1/products/{id}", "GET", "PRODUCTS"));
            arr.add(new Permission("Get all product by admin", "/api/v1/admin/products", "GET", "PRODUCTS"));
            arr.add(new Permission("Delete a product", "/api/v1/products/{id}", "DELETE", "PRODUCTS"));
            arr.add(new Permission("Get all product by user", "/api/v1/products", "GET", "PRODUCTS"));

            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get all users", "/api/v1/users", "GET", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("Get user by id", "/api/v1/users/{id}", "GET", "USERS"));

            this.permissionRepository.saveAll(arr);
        }

        if(countRoles==0){
            List<Permission> per=this.permissionRepository.findAll();
            Role adminRole=new Role();
            adminRole.setName("ADMIN");
            adminRole.setActive(true);
            adminRole.setPermissions(per);
            this.roleRepository.save(adminRole);
        }

        if(countUsers==0){
            User admin=new User();
            admin.setName("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setAddress("Hanoi");
            admin.setPhoneNumber("0886146550");
            admin.setUrlAvatar("avatar.jpg");

            Role adminRole=this.roleRepository.findByName("ADMIN");
            if(adminRole!=null){
                admin.setRole(adminRole);
            }
            this.userRepository.save(admin);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }
}
