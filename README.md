# 文物借展管理系统

## 原始需求

> 搭建一个给博物馆、承运公司、保险经纪、借展场馆共同使用的文物借展管理系统，藏品档案和运输点交页面可以用 Vue3 组织，后台用 Spring Boot 保存借展合同、保单版本、运输证据和点交结论。博物馆登记藏品档案、等级、估值、尺寸、恒湿要求和借展期限；保险经纪确认保单条款、免赔条件和估值调整记录；承运公司负责恒湿箱、押运人员、震动记录、运输路线和点交照片；借展场馆提交展柜条件、安保方案、到馆验收和撤展归还信息。系统要呈现从借展申请、保单确认、装箱押运、到馆点交、展期监测到撤展归还的链路。箱体震动、展柜条件不符、估值调整、延期展出要分别影响保险、运输和借展状态。

## 项目简介

多角色协作的文物借展管理系统，覆盖从借展申请到撤展归还的完整链路。支持博物馆、保险经纪、承运公司、借展场馆四方协同，实现藏品档案管理、保单确认、装箱押运、到馆点交、展期监测、撤展归还的全流程数字化管控。

## 技术栈

- 前端：Vue 3 + Vite + Element Plus + Vue Router + Pinia + Axios
- 后端：Spring Boot 3.2 + Spring Data JPA + H2 + Lombok
- 部署：Docker + Docker Compose + Nginx

## 四方角色功能

| 角色 | 功能模块 |
|------|----------|
| 博物馆 | 藏品档案登记（名称、等级、估值、尺寸、恒湿要求、借展期限） |
| 保险经纪 | 保单确认（条款、免赔条件、估值调整记录） |
| 承运公司 | 运输管理（恒湿箱、押运人员、震动记录、运输路线、点交照片） |
| 借展场馆 | 展期管理（展柜条件、安保方案、到馆验收、撤展归还） |

## 借展链路

借展申请 → 保单确认 → 装箱押运 → 到馆点交 → 展期监测 → 撤展归还

## 事件影响机制

| 事件 | 影响范围 |
|------|----------|
| 箱体震动超阈值 | 运输状态→异常，借展状态→暂停，保单状态→调整中 |
| 展柜条件不符 | 借展状态→暂停 |
| 估值调整 | 保单状态→调整中 |
| 延期展出 | 借展状态→逾期，保单自动续期 |

## 目录结构

```
├── backend/                    # Spring Boot 后端
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── pom.xml
│   └── src/main/java/com/relicloan/
│       ├── config/             # CORS 配置
│       ├── entity/             # JPA 实体（7个业务实体 + 4个枚举）
│       ├── repository/         # JPA Repository
│       ├── service/            # 业务逻辑（含状态机和事件联动）
│       ├── controller/         # REST API
│       └── dto/                # 请求/响应 DTO
├── frontend/                   # Vue3 前端
│   ├── Dockerfile
│   ├── .dockerignore
│   ├── nginx.conf
│   ├── package.json
│   └── src/
│       ├── api/                # Axios API 封装
│       ├── views/              # 9个页面视图
│       ├── components/         # 公共组件
│       ├── router/             # 路由配置
│       └── stores/             # Pinia 状态管理
├── docker-compose.yml          # Docker 编排
├── Dockerfile                  # 根目录默认构建入口（前端）
├── .dockerignore
└── README.md
```

## 启动方式

### 前置要求

- Docker 和 Docker Compose（推荐一键启动方式）
- 或本地开发需要：Node.js >= 18、JDK 17、Maven 3.9+

### Docker 一键启动（优先）

```bash
docker compose up --build
```

访问地址：http://localhost

后台运行：

```bash
docker compose up --build -d
```

停止服务：

```bash
docker compose down
```

### 本地开发启动

#### 1. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务地址：http://localhost:8080

H2 控制台：http://localhost:8080/h2-console（JDBC URL: jdbc:h2:mem:relicloan）

#### 2. 安装前端依赖

```bash
cd frontend
npm install
```

#### 3. 启动前端开发服务

```bash
cd frontend
npm run dev
```

前端访问地址：http://localhost:5173

> 前端开发模式已配置 Vite 代理，/api 请求自动转发至后端 localhost:8080。

## API 端点

| 模块 | 端点 | 说明 |
|------|------|------|
| 藏品档案 | GET/POST /api/artifacts | 列表/创建 |
| | GET/PUT /api/artifacts/{id} | 详情/更新 |
| 借展申请 | GET/POST /api/loans | 列表/创建 |
| | GET/PUT /api/loans/{id} | 详情/更新 |
| | PUT /api/loans/{id}/status | 状态流转 |
| 保单管理 | GET/POST /api/insurances | 列表/创建 |
| | GET/PUT /api/insurances/{id} | 详情/更新 |
| 运输记录 | GET/POST /api/transports | 列表/创建 |
| | GET/PUT /api/transports/{id} | 详情/更新 |
| | POST /api/transports/{id}/vibration | 震动上报 |
| 点交记录 | GET/POST /api/handovers | 列表/创建 |
| 展期监测 | GET/POST /api/monitorings | 列表/创建 |
| 撤展归还 | GET/POST /api/returns | 列表/创建 |
| 链路查询 | GET /api/chain/{loanId} | 完整链路信息 |
| 估值调整 | GET/POST /api/valuation-adjustments | 列表/创建估值调整 |
| | GET /api/valuation-adjustments/artifact/{artifactId} | 按藏品查询历史 |
| | GET /api/valuation-adjustments/loan/{loanId} | 按借展查询历史 |
| | GET /api/valuation-adjustments/policy/{policyId} | 按保单查询历史 |
| | GET /api/valuation-adjustments/check-sufficient/{loanId} | 检查保额是否充足 |
| 借展延期 | POST /api/loans/{id}/extend | 申请展出延期（自动校验保额） |
| | GET /api/loans/{id}/can-extend | 查询是否可延期 |

## 借展状态流转

```
APPLIED → INSURANCE_CONFIRMED → PACKED → HANDED_OVER → EXHIBITING → RETURNED
                ↓                   ↓          ↓            ↓
            SUSPENDED          SUSPENDED  SUSPENDED    SUSPENDED / OVERDUE
                ↓                                        ↓
        可恢复到之前状态                              RETURNED
```
