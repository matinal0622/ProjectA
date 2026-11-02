# UC1、UC2 接口和变量约定

当前版本仅管理商品、库存和会员。接口不是完整购买流程，不自动扣款或保存订单。

## UC1 方法

| 方法签名 | 作用 |
| --- | --- |
| boolean addProduct(Chocolate product, int quantity, String expiry, double buyPrice, double sellPrice) | 上架新商品；名称不重复，初始数量可为零 |
| boolean increaseStock(String productName, int quantity) | 给已有商品增加数量，必须大于零 |
| boolean decreaseStock(String productName, int quantity) | 减少数量，不能超过现有库存 |
| boolean removeProduct(String productName) | 删除当前商品、库存、价格；菜单会先确认 Y，接口本身不弹出确认 |
| boolean hasProduct(String productName) | 判断是否已上架 |
| Chocolate getProduct(String productName) | 返回商品对象，不存在返回 null |
| int getStockQuantity(String productName) | 返回数量，不存在返回 -1 |
| void printProduct(String productName) | 在终端显示单个商品 |
| void printStock() | 按名称顺序显示全部商品；没有商品时提示空列表 |
| double getBuyPrice(String productName) | 读取进价，不存在返回 -1 |
| double getSellPrice(String productName) | 读取未打折售价，不存在返回 -1 |

## UC2 方法

| 方法签名 | 作用 |
| --- | --- |
| boolean addMember(String memberId, String name, String contactNumber) | 注册会员，空字段或重复编号返回 false |
| Member getMember(String memberId) | 查找会员，不存在返回 null |
| double getCheckoutPrice(String productName, String memberId) | 没有商品折扣时的顾客单价 |
| double getCheckoutPrice(String productName, String memberId, double discountRate) | 商品折扣后再应用会员九折，最终不低于进价；无效参数返回 -1 |
| boolean addMemberPoints(String memberId, double paidAmount) | 成功付款后调用一次，按本笔实付整数美元累计积分；无效参数或超过上限返回 false |

memberId 为 "" 表示非会员，仅用于价格接口；积分接口必须传入真实会员编号。discountRate 范围 0–1，0.20 表示减价 20%，不是支付 20%。

例：原价 $5、商品八折、会员九折、进价 $2，返回单价 $3.60。购买三件的实付总额 $10.80 由购买功能计算，调用 addMemberPoints("M1", 10.80) 后增加 10 分。查询价格本身不加分，也不扣库存。

addMemberPoints() 不检查支付凭证，也不去重。不要在预览价格、调整库存、支付失败或重复回调时调用。每笔不足 $1 的部分不结转，不做积分兑换。

## 统一变量

| 名称 | 类型 | 含义 |
| --- | --- | --- |
| database | Database | 整个程序共用的数据对象 |
| products | HashMap<String, Chocolate> | 用商品名称找到商品对象 |
| productName | String | 商品名称，也是 stock 和 prices 的行键 |
| quantity | int | 新商品初始数量，或本次增减的数量 |
| currentQuantity | int | 修改前的库存 |
| expiry | String | 可选到期信息，当前不校验日期格式 |
| buyPrice、sellPrice | double | 进价、原始售价 |
| members | HashMap<String, Member> | 用会员编号找到会员对象 |
| memberId | String | 会员编号，例如 M1 |
| name、contactNumber | String | 会员姓名、电话；电话不是数字类型 |
| discountRate | double | 组员传入的商品减价比例 |
| paidAmount | double | 一笔成功支付的实际总额，不是单价 |
| points、earnedPoints | int | 已有积分、本次增加的积分 |

商品名称与会员编号区分大小写，首尾空格会去掉。
会员减价比例在 Database.MEMBER_DISCOUNT_RATE。
示例上限：库存和克重 1000000，单价/单次入账金额 1000000，总积分 1000000。可在对应常量处调整，但金额扩大时要同时检查转 int 的范围。
