# Chocolate Shop 库存和会员系统

当前本地程序只包含 Shiyou 的 UC1 商品与库存管理、UC2 会员系统。使用终端文字菜单，不使用 JavaFX 或 MVC。没有销售结算、购买记录、报告、商品折扣管理或供应商管理功能。

## 运行

在 VS Code 中打开本目录，打开 `src/ChocolateShop.java`，点击 `main()` 上方的 Run。或在本目录终端执行：

```sh
mkdir -p bin
javac --release 17 -encoding UTF-8 -d bin src/*.java
java -cp bin ChocolateShop
```

主菜单：

```text
1. Stock management
2. Membership
3. Exit
```

UC1 子菜单：

```text
1. Add product
2. Increase stock
3. Decrease stock
4. Remove product
5. View stock
0. Back
```

- Add product：输入名称、类型 D/M、克重、可可比例、初始数量、到期信息、进价和售价。
- Increase stock：增加已上架商品的数量，不扣款，也不生成采购记录。
- Decrease stock：减少数量，例如损坏或过期；不能减成负数。减到零不会自动下架。
- Remove product：显示商品和剩余数量，只有输入 Y 才删除当前商品、库存和价格；N 或其他输入取消。此版本没有历史表，不会操作组员的历史数据。
- View stock：输入完整名称查询单个商品；直接回车查询全部商品。
- 商品名和会员编号区分大小写，忽略首尾空格。到期信息目前只是可选文字，不检查日期格式。

UC2 菜单：A 注册会员，V 查询会员资料和积分。

## 会员优惠与积分

保留会员九折、商品折扣叠加、最低不低于进价和积分接口，不提供销售结算页面。

- `getCheckoutPrice(productName, memberId, discountRate)` 计算单价；商品折扣由组员传入，例如 0.20 表示八折。
- 两参数 `getCheckoutPrice(productName, memberId)` 表示没有商品折扣。
- `memberId` 为空字符串表示非会员；不存在的非空会员编号返回失败。
- `addMemberPoints(memberId, paidAmount)` 按每笔实付每满 $1 积 1 分；不足 $1 的部分不跨笔累计。不做积分兑换。
- 购买功能接入后，由它在支付成功后调用积分接口一次。接口本身不验证支付、不判断重复订单，不要从库存调整或价格查询中调用它。

## 示例

1. 选 2 → A，注册 M1 / Alex / 0400000000。
2. 选 1 → 1，上架 Hazelnut Chocolate，类型 M、100 g、35% 可可、初始库存 8，进价 2.50、售价 6.00。
3. 选 2，给该商品增加 4 件，库存变为 12。
4. 选 3，减少 2 件，库存变为 10。
5. 选 5，输入商品名称查看；选 4，再输入 Y 下架。
6. 选 0 返回主菜单，2 → V → M1 查询。库存操作后会员积分仍为 0。

## 数值范围和保存方式

为了用基础判断处理数值边界，本地示例暂设：单品库存最多 1000000 件、克重最多 1000000 g、单价和单次积分入账金额最多 $1000000、会员总积分最多 1000000。超过范围返回失败，不自动截断。它们是可调整的示例限制，不是已确认的课程业务要求。

输入金额最多两位小数；折扣后的单价四舍五入到分。所有数据仅在内存中保存，退出后不保存，下次运行重新加载两种演示商品。

In.java 已恢复为老师在 Week 1 提供的原版。菜单通过 In.nextLine() 读取文字，在 Menu / ChocolateShop 中检查选项和表单字段；空输入、非数字、负数量等会被拒绝。无效表单字段返回库存菜单，重新选上架或增减库存即可再次填写。金额请填写 0.50、2 或 2.50，不使用 .50、2. 或科学计数法。

原版 In 不负责处理输入流结束，请用主菜单 3 退出，不使用 Ctrl+D 代替退出选项。

## 验证与对接

运行方式和测试范围见 [测试说明](docs/TESTING.md)，组员需要统一的方法及参数见 [命名与接口约定](docs/NAMING_GUIDE.md)。

原有团队协作流程见 [协作说明](docs/COLLABORATION.md)。此版本在个人分支维护，合并到 main 前应由组员检查。
