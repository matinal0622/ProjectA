# UC1 和 UC2 的个人范围

本目录只完成 Shiyou 的两个用例，未修改或推送其他组员的仓库。

| 范围 | 当前方法 |
| --- | --- |
| UC1 商品与库存管理 | addProduct、increaseStock、decreaseStock、removeProduct、getStockQuantity、printProduct、printStock |
| UC2 会员系统 | addMember、getMember、getCheckoutPrice、addMemberPoints |
| 共用部分 | Chocolate 及子类、Member、DataTable、MenuUI、Menu、In |

## 不在本次范围

- 购买记录、报告、供应商管理。
- 商品折扣的添加、删除与保存；这里只接受组员提供的 discountRate。
- 付款、进货扣款、店铺现金余额、销售订单、历史记录保存。
- JavaFX、MVC、文件保存、积分兑换。

旧 buyOrder()、sellOrder() 和订单/余额代码已经从当前版本移除，原版保留在完整 ZIP 备份中。不能再把减库存当作一次销售。

## 以后合并时

复用同一个 Database，不要另外创建一份库存或会员集合。接口与变更见 NAMING_GUIDE.md。

- 购买功能可以读取库存、计算结账单价、调用 decreaseStock()，并在支付成功后调用一次 addMemberPoints()。
- 购买功能自行负责检查所有步骤能否成功以及失败回退，避免已经扣库存后又因积分上限失败；当前接口不是完整结算事务。
- 历史记录由组员独立保存当时的商品名称和实付金额；商品下架不得删除或重新计算历史记录。
- 下架会删除当前商品和价格，调用方不能再通过当前商品表查询已下架商品的历史售价。
- 商品折扣比例由 UC5 提供，不能在传入前已经计算会员优惠，否则会重复打折。
