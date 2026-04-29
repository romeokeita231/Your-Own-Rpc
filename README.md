#  own-rpc 使用指南

## 环境要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| **JDK** | ✅ **21 LTS** | 强烈推荐，Lombok 完美兼容 |
| Maven | 3.8.x+ | 依赖管理 |
| IDEA | 2023.x+ | 需启用注解处理器 |

---

## 🚀 快速开始

### 1. 环境配置

#### 1.1 设置 JDK 21

在 IDEA 中配置：
```
文件 → 项目结构 → 项目
  ├─ SDK: 选择已安装的 JDK 21
  └─ 语言级别: 21 - Switch 的模式匹配
```
> 英文界面：File → Project Structure → Project

#### 1.2 启用 Lombok 注解处理器
```
设置 → 构建、执行、部署 → 编译器 → 注解处理器
  ✅ 启用注解处理
```
> 英文界面：Settings → Build, Execution, Deployment → Compiler → Annotation Processors

#### 1.3 Maven 配置
所有模块已预配置 JDK 21 编译：
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

---

### 2. 运行示例

#### 2.1 启动服务提供者
运行 `EasyProviderExample.java`：
```java
public static void main(String[] args) {
    // 注册服务
    LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
    // 启动 Web 服务
    HttpServer httpServer = new VertxHttpServer();
    httpServer.doStart(8080);
}
```

控制台输出：
```
✅ 服务注册成功: com.rom.example.common.service.UserService -> com.rom.example.provider.UserServiceImpl
✅ 服务器启动成功，监听端口：8080
```

#### 2.2 启动服务消费者
运行 `EasyConsumerExample.java`：
```java
public static void main(String[] args) {
    UserService userService = ServiceProxyFactory.getProxy(UserService.class);
    User user = new User();
    user.setName("rom");
    User newUser = userService.getUser(user);
    System.out.println("用户名: " + newUser.getName());
}
```

控制台输出：
```
✅ 用户名: rom
```

---

## ❌ 常见错误与解决方案

---

### 🔴 错误 1: TypeTag :: UNKNOWN

**错误信息：**
```
java: java.lang.ExceptionInInitializerError
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

| 原因 | 解决方案 |
|------|----------|
| ❌ Lombok 版本与 JDK 25+ 不兼容 | ✅ 使用 JDK 21 LTS |
| ❌ IDEA 缓存旧的注解处理器 | ✅ File → Invalidate Caches → 重启 |

> 💡 **最佳实践**：JDK 21 是 LTS 长期支持版，生态最稳定。

---

### 🔴 错误 2: 服务不存在 java.lang.Object

**错误信息：**
```
RuntimeException: 服务不存在: java.lang.Object
```

| 原因 | 解决方案 |
|------|----------|
| ❌ JDK 动态代理拦截 Object 方法 | ✅ ServiceProxy 已内置过滤逻辑 |

**修复原理：**
```java
// 过滤 Object 类的方法（toString、hashCode、equals 等）
if (method.getDeclaringClass() == Object.class) {
    return method.invoke(this, args);
}
```

---

### 🔴 错误 3: java.io.EOFException

**错误信息：**
```
java.io.EOFException at ObjectInputStream.<init>
```

| 原因 | 解决方案 |
|------|----------|
| ❌ 在浏览器直接访问 http://localhost:8080 | ✅ 这是正常现象！浏览器发送空请求 |
| ❌ 空字节数组反序列化 | ✅ 序列化器已增加空值防护 |

> 💡 **说明**：RPC 框架只接收 Consumer 发送的序列化 POST 请求。

---

### 🔴 错误 4: implClass 为 null

**错误信息：**
```
NullPointerException: Cannot invoke "Class.getMethod(...)" because "implClass" is null
```

| 排查步骤 |
|----------|
| 1️⃣ **检查启动顺序**：必须先启动 Provider，再启动 Consumer |
| 2️⃣ 对比控制台输出的「服务注册名」和「请求服务名」是否完全一致 |
| 3️⃣ 修改代码后，是否重新启动了 Provider |

---

### 🔴 错误 5: Class.newInstance() 过时

**错误信息：**
```
警告: Class.newInstance() 已过时
```

已修复：
```java
// 旧代码（废弃）
implClass.newInstance()

// 新代码（标准）
implClass.getDeclaredConstructor().newInstance()
```

---

### 🔴 错误 6: Unsafe 已终结弃用

**警告信息：**
```
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
```

| 说明 |
|------|
| ✅ 这只是警告，**完全不影响运行** |
| Vert.x/Netty 使用 Unsafe 实现极致性能优化 |
| JDK 25 新增终结弃用标记，属于生态适配问题 |

---

## 📦 项目模块说明

| 模块 | 作用 |
|------|------|
| **own-rpc-easy** | RPC 核心框架 |
| example-common | 公共接口与模型 |
| example-provider | 服务提供者示例 |
| example-consumer | 服务消费者示例 |

---

## 🎯 核心概念

| 组件 | 功能 |
|------|------|
| VertxHttpServer | 基于 Vert.x 的 HTTP 服务器 |
| JdkSerializer | JDK 原生序列化实现 |
| LocalRegistry | 本地服务注册表 |
| ServiceProxy | JDK 动态代理 |
| @Data @Builder | Lombok 注解，用于模型类 |

---




