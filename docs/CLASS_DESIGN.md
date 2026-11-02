# 库存和会员系统的类与流程

## 类的职责

| 类或接口 | 当前用途 |
| --- | --- |
| ChocolateShop | 程序入口、演示数据、UC1 与 UC2 菜单和输入输出 |
| MenuUI、Menu | 保留原有接口和菜单类，主菜单选择 1–3 |
| In | 课堂原版输入工具，只负责读取，不额外校验或自动去空格 |
| Database | 上架、增减库存、下架、查询、会员注册、折扣及积分接口 |
| DataTable | 库存和价格表，使用 ArrayList 与 HashMap |
| Chocolate | 抽象父类，保存名称、克重、可可比例 |
| DarkChocolate、MilkChocolate | 商品子类，实现类型和配方说明 |
| Member | 会员编号、姓名、电话、积分 |

只有 products、stock、prices、members 四项业务数据；没有订单、现金余额、供应商或商品折扣管理表。

## UC1

```text
主菜单 1 → handleUpdateStock()
    ↓
上架 → 读取商品信息 → 创建子类对象 → addProduct() 校验 → 保存商品、数量、价格
加库存 → increaseStock() → 检查数量上限 → 修改数量
减库存 → decreaseStock() → 检查库存是否足够 → 修改数量
下架 → 显示商品 → Y 确认 → removeProduct() → 移除商品、库存、价格
查询 → printProduct() 或 printStock() → 显示，不修改数据
    ↓
回到库存子菜单，选 0 返回主菜单
```

校验失败返回 false，原数据不变。减到零仍保留上架记录，下架才删除当前记录。库存管理不调用积分方法，不创建销售或采购记录。

## UC2

注册：addMember() 检查空字段和重复编号，再创建 Member 放入 members。
查询：getMember() 返回已有会员对象，不存在返回 null。

组员接入时使用：

```text
商品名、会员编号、商品折扣比例
    ↓
getCheckoutPrice() → 商品折扣 → 会员九折 → 进价下限 → 返回单价
    ↓
组员的购买功能负责完成支付、记录等操作
    ↓
成功后调用一次 addMemberPoints() → 实付金额整数部分 → 累加积分
```

当前不实现中间的购买功能。价格查询不改变库存或积分；积分接口不检查支付状态与重复调用，由调用方保证。

## 写法约束

保留类、构造方法、继承、抽象方法、接口、重载、if/else、循环、基本类型转换及课堂集合写法。不使用 try/catch、主动抛异常、Stream、Lambda、Optional、record、var、线程、反射或第三方库。

In.java 与 Week 1 提供的原版一致，没有增加方法或校验逻辑。Menu 将选项当作文字比较；ChocolateShop 使用文字比较处理库存选项、D/M、A/V 和 Y/N，避免空输入调用 charAt(0)。

库存表单的 readWholeNumber()、readPrice() 用长度、字符循环和范围判断检查数量与金额，确认格式后才 valueOf()。无效字段会提示并返回库存菜单，不保存半成品。trim() 也在菜单中调用，而不是放进 In。

原版 In 不处理输入流突然结束；请通过主菜单 3 正常退出。业务层继续检查库存是否足够、商品是否重复等规则。数值范围使用普通比较，不使用 MAX_VALUE、isNaN()、isInfinite()。金额使用乘 100、加 0.5、转 int、除以 100.0 的基础写法，仅用于已经限制范围的非负金额。

以上是实现采用的写法，不代表对未收录课堂附件作出完整的课程合规保证。
