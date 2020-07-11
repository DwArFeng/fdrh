# ChangeLog

### Release_1.0.1_20200708_build_A

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
