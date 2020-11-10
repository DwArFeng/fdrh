# ChangeLog

### Release_1.2.1_20201109_build_A

#### 功能构建

- 同步 fdr 1.9.0.a 版本中的 qos 指令 mlc 以及相关配置文件
- 消除预设配置文件中的真实的 ip 地址
- 升级 subgrade 依赖至 1.2.0.a，并更新不兼容的部分代码。
- 升级 fdr 依赖至 1.9.1.a。
- 优化 BehaviorAnalyse，取消有可能产生大量文本的返回结果以及入口参数的记录。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.2.0_20201107_build_A

#### 功能构建

- 更新 dubbo 的配置文件。
- 更新推送器配置文件 push.properties。
- 升级 fdr 依赖至 1.9.0.a，并更改数据访问层代码适配接口的差异。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.1.0_20201021_build_A

#### 功能构建

- 优化 pom.xml 升级依赖版本，解决部分冲突。
- 同步 fdr 版本至 1.8.5.a。
  - 同步新增的配置文件。
  - 同步 telqos 框架功能。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.3_20200828_build_A

#### 功能构建

- (无)

#### Bug修复

- 修复项目打包后目录结构不全的bug。

#### 功能移除

- (无)

---

### Release_1.0.2_20200827_build_A

#### 功能构建

- 更新fdr依赖为1.7.2。
- 优化fdrh-impl模块中pom.xml插件配置。

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.1_20200711_build_A

#### 功能构建

- 更改 Phoenix 表结构，去除数据表的 tableImmutableRows 属性。
  - fdrh.persistence_value
  - fdrh.filtered_value
  - fdrh.triggered_value

#### Bug修复

- (无)

#### 功能移除

- (无)

---

### Release_1.0.0_20200630_build_A

#### 功能构建

- 引用fdr项目版本1.7.0.b。
- 引用subgrade依赖版本1.1.1.b。
- 将持久数据实体的数据访问层转移至HBase。
  - com.dwarfeng.fdr.stack.bean.entity.PersistenceValue
  - com.dwarfeng.fdr.stack.bean.entity.FilteredValue
  - com.dwarfeng.fdr.stack.bean.entity.TriggeredValue

#### Bug修复

- (无)

#### 功能移除

- (无)
