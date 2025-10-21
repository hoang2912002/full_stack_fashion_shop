package vn.clothing.fashion_shop.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.RequiredArgsConstructor;
import vn.clothing.fashion_shop.constants.GenderEnum;
import vn.clothing.fashion_shop.constants.util.SlugUtil;
import vn.clothing.fashion_shop.constants.util.SplitCamelCase;
import vn.clothing.fashion_shop.domain.Permission;
import vn.clothing.fashion_shop.domain.Role;
import vn.clothing.fashion_shop.domain.User;
import vn.clothing.fashion_shop.repository.PermissionRepository;
import vn.clothing.fashion_shop.repository.RoleRepository;
import vn.clothing.fashion_shop.repository.UserRepository;
import vn.clothing.fashion_shop.web.rest.DTO.permission.GetPermissionDTO;

/*
 * GIẢI THÍCH CÁCH HOẠT ĐỘNG
 * 
 * I. implements ApplicationListener<ContextRefreshedEvent>
 * 
 * 🚀 1️⃣ ApplicationListener là gì?
 *      - ApplicationListener là interface gốc của cơ chế sự kiện (event system) trong Spring Framework.
 *      - Nếu bạn muốn "nghe" một loại sự kiện nào đó trong Spring (ví dụ khi context được tạo, 
 *      khi bean được load, khi refresh xong, khi app shutdown...),
 *      thì bạn chỉ cần implements ApplicationListener<Loại Sự Kiện>
 *      và override onApplicationEvent().
 * 
 * 🧠 2️⃣ ContextRefreshedEvent là gì?
 *      - ContextRefreshedEvent là một event đặc biệt trong Spring lifecycle — nó được publish khi 
 *      ApplicationContext (container) được khởi tạo xong hoặc refresh xong.
 *      - Cụ thể:
 *          + Khi Spring Boot start, nó sẽ tạo ApplicationContext (nơi chứa tất cả các bean).
 *          + Sau khi tất cả bean được khởi tạo, dependency được inject, và context đã sẵn sàng dùng,
 *            Spring sẽ bắn ra event ContextRefreshedEvent.  
 *          + Mọi bean nào lắng nghe (implements ApplicationListener<ContextRefreshedEvent>) đều sẽ được gọi.
 * 
 * ⚙️ 3️⃣ Vì sao phải implements ApplicationListener<ContextRefreshedEvent>
 *      - Bởi vì bạn muốn thực hiện một công việc sau khi toàn bộ context (các bean) đã sẵn sàng ví dụ như:
 *          + Tạo dữ liệu mặc định (roles, admin, permissions,...)
 *          + Gọi setup logic (load config file, sync data)
 *          + Seed database ban đầu.
 *      - Khi đó, nếu bạn chỉ dùng @PostConstruct (chạy khi bean vừa khởi tạo) thì lúc đó:
 *          + Một số bean khác có thể chưa sẵn sàng (VD repository, handlerMapping,...)
 *          + Bạn không thể quét toàn bộ endpoint được vì RequestMappingHandlerMapping chưa hoàn tất load.
 *      - Do đó, bạn phải chờ đến ContextRefreshedEvent
 *          + Vì tại thời điểm này, Spring đã hoàn tất load toàn bộ route, controller, repository, security.
 *          + Có thể quét toàn bộ RequestMappingHandlerMapping để lấy ra endpoints.
 * 
 * 🧩 4️⃣ Tóm tắt Roadmap ngắn gọn
    Spring Boot khởi động
      ↓
    Tạo ApplicationContext
        ↓
    Load tất cả bean, controller, mapping
        ↓
    Phát ContextRefreshedEvent
        ↓
    Gọi onApplicationEvent() trong SetupDataLoader
        ↓
    Quét tất cả endpoint, tạo permission, role, admin
        ↓
    App sẵn sàng nhận request

 * II. Dependency injection
 * 
 * ⚙️ 1️⃣ Tổng quan: Dependency Injection trong Spring
 *      - Khi Spring khởi động, nó tạo ra các bean (đối tượng singleton hoặc prototype) và lưu vào ApplicationContext.
 *      - Khi bạn khai báo trong constructor. Spring sẽ tự động tìm trong context một bean có kiểu (type) là 
 *      RequestMappingHandlerMapping, và inject (tiêm) nó vào tham số handlerMapping. 
 * 
 * ⚠️ 2️⃣ Vấn đề phát sinh: Có nhiều bean cùng loại
 *      - Spring Boot framework tự động đăng ký nhiều bean thuộc kiểu RequestMappingHandlerMapping.
 *      - Ví dụ trong ứng dụng Spring Web MVC / Spring WebFlux, có thể tồn tại:
        | Tên Bean                       | Kiểu                                           | Chức năng                             |
        | ------------------------------ | ---------------------------------------------- | ------------------------------------- |
        | `requestMappingHandlerMapping` | `RequestMappingHandlerMapping`                 | mapping chính của REST Controller     |
        | `viewControllerHandlerMapping` | `RequestMappingHandlerMapping` (hoặc subclass) | mapping cho view (nếu có)             |
        | `endpointHandlerMapping`       | `RequestMappingHandlerMapping`                 | mapping cho actuator endpoints        |
        | `webMvcHandlerMapping`         | `RequestMappingHandlerMapping`                 | (trong 1 số config) mapping chung MVC |

 *      => 💥 Kết quả: Khi Spring thấy bạn cần RequestMappingHandlerMapping, nó không biết phải chọn cái nào.
 *  
 * 💡 3️⃣ Giải pháp: @Qualifier
 *      - @Qualifier nói cho Spring biết chính xác bean nào bạn muốn inject
 *      → bằng tên bean trong context.
 * 
 * 🔍 4️⃣ Tên bean "requestMappingHandlerMapping" từ đâu ra?
 *      - Spring Boot tự đặt tên mặc định cho các bean MVC chính, theo tên class
 * 
 * 🧠 5️⃣ Vì sao SetupDataLoader cần đúng bean này?
 *      - Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
 *      - Do phương thức này trả về tất cả route mapping của REST Controller.
 *      - Nếu inject nhầm bean kiểu khác (ví dụ endpointHandlerMapping dành cho actuator) thì:
 *          + Sẽ không lấy được route của các controller trong app, không có endpoint.
 *  
 * 🗺️ 6️⃣ Tóm tắt roadmap injection tại runtime
 *      - Khi Spring khởi tạo bean SetupDataLoader
 *      - Nó đọc constructor:
 *          + Thấy 1 đang gọi đến 1 bean với type là RequestMappingHandlerMapping 
 *          sẽ vô context để tìm bean đó nhưng do có tên trùng nhau dẫn đến lỗi 
 *          + Cho nên cần có @Qualifier( tên của bean đó )
 * 
 * 🧩 4️⃣ Vai trò của RequestMappingHandlerMapping
 *      - Đây là class chịu trách nhiệm ánh xạ toàn bộ controller endpoint trong ứng dụng.    
 *      - Nó lưu map:
 *          Map<RequestMappingInfo, HandlerMethod>
 *          + RequestMappingInfo → chứa path, method (GET/POST/PUT/DELETE,…)
 *          + HandlerMethod → chính là method Java tương ứng (UserController.getUsers() chẳng hạn)
 */

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RequestMappingHandlerMapping handlerMapping;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(
        @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping, 
        PermissionRepository permissionRepository,
        RoleRepository roleRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.handlerMapping = handlerMapping;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // optional: chỉ quét package app của bạn

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();

        List<Permission> listPermission = new ArrayList<>();
        map.forEach((info, handlerMethod) -> {
            Set<String> patterns = info.getPathPatternsCondition().getPatternValues();
            String path = patterns.stream().findFirst().orElse("");

            List<String> range = List.of("USER", "ADMIN");
            //Module
            String[] arrPath = path.split("/"); 
            String module = "UNKNOWN";

            if (arrPath.length > 3) {
                String third = arrPath[3].toUpperCase();
                module = (arrPath.length > 4 && range.contains(third)) ? arrPath[4].toUpperCase() : third;

                Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
                String method = (methods == null || methods.isEmpty()) ? "GET" : methods.iterator().next().name();

                String endpointName = handlerMethod.getMethod().getName();
                String readableName = SplitCamelCase.convertCamelCase(endpointName);

                listPermission.add(
                    Permission.builder()
                    .apiPath(path)
                    .method(method)
                    .module(module)
                    .name(readableName)
                    .activated(true)
                    .build()
                );
            }
        });
        List<Permission> permissionsDB = this.permissionRepository.findAll();
        Set<String> existingEndpoints = permissionsDB.stream()
            .map(p -> p.getApiPath() + "::" + p.getMethod())
            .collect(Collectors.toSet());

        List<Permission> newPermissions = listPermission.stream()
            .filter(p -> !existingEndpoints.contains(p.getApiPath() + "::" + p.getMethod()))
            .toList();
        if(!newPermissions.isEmpty()){
            this.permissionRepository.saveAll(newPermissions);
        }
        
        Role roleByName = this.roleRepository.findByName("admin");
        if(roleByName == null){
            List<Permission> listPermissions = this.permissionRepository.findAll();
            Role role = new Role();
            role.setName("admin");
            role.setSlug(SlugUtil.toSlug(role.getName()));
            role.setActivated(true);
            role.setPermissions(listPermissions);
            this.roleRepository.save(role);
        }

        Optional<User> userOptional = this.userRepository.findByEmail("admin@gmail.com");
        if(!userOptional.isPresent() || userOptional == null){
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setFullName("Admin");
            user.setGender(GenderEnum.MALE);
            user.setAge(23);
            user.setActivated(true);
            user.setPassword(this.passwordEncoder.encode("123456"));
            Role adminRole = this.roleRepository.findByName("admin");
            if(adminRole != null){
                user.setRole(adminRole);
            }
            this.userRepository.save(user);
        }

        if(newPermissions.isEmpty() && roleByName != null && userOptional.isPresent()){
            System.out.println(">>> SKIP INIT DATABASE ALREADY HAVE DATA....");
        }
        else{
            System.out.println(">>> END INIT DATABASE");
        }

    }

}
