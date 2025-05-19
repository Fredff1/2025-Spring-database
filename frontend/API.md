# 汽车维修管理系统 API 文档

## 目录
1. [通用数据结构](#通用数据结构)
2. [认证相关 API](#认证相关-api)
3. [订单管理 API](#订单管理-api)
4. [车辆管理 API](#车辆管理-api)
5. [管理员 API](#管理员-api)
6. [维修人员 API](#维修人员-api)
7. [客户 API](#客户-api)

## 通用数据结构

### 分页请求
```java
public class PageRequest {
    private Integer page = 1;
    private Integer limit = 10;
    
    // getters and setters
}
```

### 分页响应
```java
public class PageResponse<T> {
    private List<T> list;
    private Long total;
    
    // getters and setters
}
```

### 通用响应
```java
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;
    
    // getters and setters
}
```

### 用户角色枚举
```java
public enum UserRole {
    ADMIN,
    REPAIRMAN,
    CUSTOMER
}
```

### 订单状态枚举
```java
public enum OrderStatus {
    PENDING,    // 待处理
    PROCESSING, // 处理中
    COMPLETED,  // 已完成
    CANCELLED   // 已取消
}
```

### 维修类型枚举
```java
public enum RepairType {
    MAINTENANCE, // 常规保养
    REPAIR,      // 故障维修
    ACCIDENT     // 事故维修
}
```

## 认证相关 API

### DTO 类
```java
public class LoginRequest {
    private String username;
    private String password;
    private UserRole role;
    
    // getters and setters
}

public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private UserRole role;
    
    // getters and setters
}

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    
    // getters and setters
}

public class UserInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private UserRole role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/login")
    public ApiResponse<UserInfoDTO> login(@RequestBody LoginRequest request);
    
    @PostMapping("/register")
    public ApiResponse<UserInfoDTO> register(@RequestBody RegisterRequest request);
    
    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request);
}
```

## 订单管理 API

### DTO 类
```java
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long vehicleId;
    private String vehiclePlate;
    private RepairType repairType;
    private String problem;
    private OrderStatus status;
    private BigDecimal amount;
    private Boolean isPaid;
    private Boolean isReviewed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}

public class CreateOrderRequest {
    private Long vehicleId;
    private RepairType repairType;
    private String problem;
    
    // getters and setters
}

public class ReviewRequest {
    private Integer rating;
    private String comment;
    
    // getters and setters
}

public class OrderStatsDTO {
    private Integer vehicleCount;
    private Integer orderCount;
    private Integer historyCount;
    
    // getters and setters
}

public class CompleteRepairRequest {
    private String repairDetails;
    private List<String> partsUsed;
    private Integer laborHours;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @GetMapping
    public ApiResponse<PageResponse<OrderDTO>> getList(PageRequest request);
    
    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getDetail(@PathVariable Long id);
    
    @PostMapping
    public ApiResponse<OrderDTO> create(@RequestBody CreateOrderRequest request);
    
    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderDTO> cancel(@PathVariable Long id);
    
    @PostMapping("/{id}/pay")
    public ApiResponse<OrderDTO> pay(@PathVariable Long id);
    
    @PostMapping("/{id}/review")
    public ApiResponse<OrderDTO> review(@PathVariable Long id, @RequestBody ReviewRequest request);
    
    @GetMapping("/stats")
    public ApiResponse<OrderStatsDTO> getStats();
    
    @PostMapping("/{id}/start-repair")
    public ApiResponse<OrderDTO> startRepair(@PathVariable Long id);
    
    @PostMapping("/{id}/complete-repair")
    public ApiResponse<OrderDTO> completeRepair(@PathVariable Long id, @RequestBody CompleteRepairRequest request);
    
    @PostMapping("/{id}/assign")
    public ApiResponse<OrderDTO> assignRepairman(@PathVariable Long id, @RequestParam Long repairmanId);
    
    @PostMapping("/{id}/complete")
    public ApiResponse<OrderDTO> complete(@PathVariable Long id);
}
```

## 车辆管理 API

### DTO 类
```java
public class VehicleDTO {
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private String vin;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}

public class CreateVehicleRequest {
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private String vin;
    
    // getters and setters
}

public class UpdateVehicleRequest {
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private String vin;
    
    // getters and setters
}

public class RepairHistoryDTO {
    private Long id;
    private Long orderId;
    private RepairType repairType;
    private String problem;
    private String solution;
    private List<String> partsUsed;
    private Integer laborHours;
    private BigDecimal amount;
    private LocalDateTime createTime;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @GetMapping
    public ApiResponse<PageResponse<VehicleDTO>> getList(PageRequest request);
    
    @GetMapping("/{id}")
    public ApiResponse<VehicleDTO> getDetail(@PathVariable Long id);
    
    @PostMapping
    public ApiResponse<VehicleDTO> create(@RequestBody CreateVehicleRequest request);
    
    @PutMapping("/{id}")
    public ApiResponse<VehicleDTO> update(@PathVariable Long id, @RequestBody UpdateVehicleRequest request);
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id);
    
    @GetMapping("/{id}/repair-history")
    public ApiResponse<List<RepairHistoryDTO>> getRepairHistory(@PathVariable Long id);
}
```

## 管理员 API

### DTO 类
```java
public class AdminInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}

public class UpdateAdminInfoRequest {
    private String name;
    private String phone;
    private String email;
    
    // getters and setters
}

public class OverviewDTO {
    private Integer todayOrders;
    private Integer orderTrend;
    private Integer pendingOrders;
    private BigDecimal monthlyIncome;
    private Integer incomeTrend;
    private BigDecimal rating;
    
    // getters and setters
}

public class StatisticsDTO {
    private Integer completedOrders;
    private Integer repairHours;
    private BigDecimal income;
    private BigDecimal rating;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/info")
    public ApiResponse<AdminInfoDTO> getInfo();
    
    @PutMapping("/info")
    public ApiResponse<AdminInfoDTO> updateInfo(@RequestBody UpdateAdminInfoRequest request);
    
    @GetMapping("/overview")
    public ApiResponse<OverviewDTO> getOverview();
    
    @GetMapping("/pending-orders")
    public ApiResponse<List<OrderDTO>> getPendingOrders();
    
    @GetMapping("/statistics")
    public ApiResponse<StatisticsDTO> getStatistics(@RequestParam String type);
}
```

## 维修人员 API

### DTO 类
```java
public class RepairmanInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}

public class UpdateRepairmanInfoRequest {
    private String name;
    private String phone;
    private String email;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/repairman")
public class RepairmanController {
    @GetMapping("/info")
    public ApiResponse<RepairmanInfoDTO> getInfo();
    
    @PutMapping("/info")
    public ApiResponse<RepairmanInfoDTO> updateInfo(@RequestBody UpdateRepairmanInfoRequest request);
    
    @GetMapping("/statistics")
    public ApiResponse<StatisticsDTO> getStatistics(@RequestParam String type);
}
```

## 客户 API

### DTO 类
```java
public class CustomerInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // getters and setters
}

public class UpdateCustomerInfoRequest {
    private String name;
    private String phone;
    private String email;
    
    // getters and setters
}
```

### API 端点
```java
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @GetMapping("/info")
    public ApiResponse<CustomerInfoDTO> getInfo();
    
    @PutMapping("/info")
    public ApiResponse<CustomerInfoDTO> updateInfo(@RequestBody UpdateCustomerInfoRequest request);
}
```

## 错误处理

### 错误码枚举
```java
public enum ErrorCode {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误");
    
    private final int code;
    private final String message;
    
    // constructor and getters
}
```

### 全局异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e);
    
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException e);
    
    @ExceptionHandler(ValidationException.class)
    public ApiResponse<Void> handleValidationException(ValidationException e);
}
```

## 安全配置

### JWT 配置
```java
@Configuration
public class JwtConfig {
    private String secret;
    private long expiration;
    
    // getters and setters
}
```

### 安全过滤器
```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain);
}
```

## 数据库实体


## 注意事项

1. 所有时间字段使用 `LocalDateTime` 类型
2. 金额字段使用 `BigDecimal` 类型
3. 所有实体类都需要实现 `Serializable` 接口
4. 所有 DTO 类都需要实现 `Serializable` 接口
5. 所有枚举类都需要实现 `Serializable` 接口
