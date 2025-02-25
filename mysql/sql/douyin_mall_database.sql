-- 用户信息表
CREATE TABLE users (
    -- 主键及用户基本信息
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识用户的自增主键',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，唯一，最大长度50字符',
    password VARCHAR(255) NOT NULL COMMENT '用户密码，使用哈希存储',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '用户电子邮件地址，唯一，最大长度100字符',
    phone VARCHAR(20) COMMENT '用户手机号，可选',

    -- 头像及角色信息
    avatar_url VARCHAR(255) COMMENT '用户头像的URL，可选',
    role ENUM('user', 'admin') DEFAULT 'user' COMMENT '用户角色，默认为普通用户(user)',

    -- 用户状态信息
    status TINYINT DEFAULT 1 COMMENT '用户状态: 1为活跃, 0为停用, -1为已删除',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    deleted_at TIMESTAMP NULL COMMENT '记录删除时间, 若为NULL表示未删除',

    -- 索引设计
    INDEX idx_email (email), -- 为邮箱字段创建单列索引，加快查询速度
    INDEX idx_phone (phone), -- 为手机号字段创建单列索引，便于按手机号查询
    INDEX idx_status_deleted (status, deleted_at) -- 联合索引，提高按状态和删除时间的查询效率
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='用户信息表，存储系统用户的基础信息';

-- 用户地址表
CREATE TABLE user_addresses (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识地址的自增主键',
    
    -- 用户关联信息
    user_id BIGINT NOT NULL COMMENT '关联的用户ID，外键指向users表的id',
    
    -- 收件人信息
    recipient_name VARCHAR(50) NOT NULL COMMENT '收件人姓名，最大长度50字符',
    phone VARCHAR(20) NOT NULL COMMENT '收件人手机号',
    
    -- 地址详细信息
    province VARCHAR(50) NOT NULL COMMENT '省份，最大长度50字符',
    city VARCHAR(50) NOT NULL COMMENT '城市，最大长度50字符',
    district VARCHAR(50) NOT NULL COMMENT '区/县，最大长度50字符',
    detailed_address VARCHAR(200) NOT NULL COMMENT '详细地址，最大长度200字符',
    
    -- 默认地址标识
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否为默认地址，默认值为FALSE',
    
    -- 地址状态信息
    status TINYINT DEFAULT 1 COMMENT '地址状态：1为活跃，0为无效',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    deleted_at TIMESTAMP NULL COMMENT '记录删除时间，若为NULL表示未删除',
    
    -- 索引设计
    INDEX idx_user_id (user_id) COMMENT '加快按用户ID查询地址的速度',
    INDEX idx_status (status) COMMENT '加快按状态查询地址的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='用户地址表，存储用户的收货地址信息';

-- 商品种类表
CREATE TABLE categories (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识分类的自增主键',
    
    -- 分类名称
    name VARCHAR(50) NOT NULL COMMENT '分类名称，最大长度50字符',
    
    -- 上级分类
    parent_id BIGINT DEFAULT NULL COMMENT '上级分类ID，指向本表的id字段，NULL表示顶级分类',
    
    -- 分类层级
    level TINYINT NOT NULL COMMENT '分类层级，用于表示当前分类所在的深度，从1开始',
    
    -- 排序字段
    sort_order INT DEFAULT 0 COMMENT '分类排序字段，数值越小优先级越高',
    
    -- 分类状态
    status TINYINT DEFAULT 1 COMMENT '分类状态：1为活跃，0为无效',
    
    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    deleted_at TIMESTAMP NULL COMMENT '记录删除时间，NULL表示未删除（软删除字段）',
    
    -- 索引设计
    INDEX idx_parent_id (parent_id) COMMENT '加快按上级分类ID查询的速度',
    INDEX idx_status (status) COMMENT '加快按分类状态查询的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='商品分类表，存储商品的分类信息';

-- 产品信息表
CREATE TABLE products (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识商品的自增主键',
    
    -- 分类ID
    category_id BIGINT NOT NULL COMMENT '商品所属分类ID，指向分类表的id字段',
    
    -- 商品名称
    name VARCHAR(100) NOT NULL COMMENT '商品名称，最大长度100字符',
    
    -- 商品描述
    description TEXT COMMENT '商品的详细描述，可包含HTML等格式',
    
    -- 商品价格
    price DECIMAL(10,2) NOT NULL COMMENT '商品的销售价格，保留两位小数',
    
    -- 商品原价
    original_price DECIMAL(10,2) COMMENT '商品的原始价格，保留两位小数（可为空）',
    
    -- 库存数量
    stock INT NOT NULL DEFAULT 0 COMMENT '商品库存数量，默认为0',
    
    -- 商品图片
    images JSON COMMENT '商品图片信息，存储为JSON格式，可包含多个图片链接',
    
    -- 销售数量
    sales_count INT DEFAULT 0 COMMENT '商品的销售数量，默认为0',
    
    -- 商品状态
    status TINYINT DEFAULT 1 COMMENT '商品状态：1为在售，0为下架，-1为删除',
    
    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '商品记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '商品记录最近更新时间',
    deleted_at TIMESTAMP NULL COMMENT '商品删除时间，NULL表示未删除（软删除字段）',
    
    -- 索引设计
    INDEX idx_category_id (category_id) COMMENT '加快按分类ID查询商品的速度',
    INDEX idx_status (status) COMMENT '加快按商品状态查询的速度',
    INDEX idx_sales (sales_count) COMMENT '加快按销售数量排序的速度',
    INDEX idx_updated_at (updated_at) COMMENT '加快按更新时间查询的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='商品表，存储商品的基本信息，包括名称、价格、库存等';


-- 购物车表
CREATE TABLE shopping_cart_items (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识购物车商品项的自增主键',
    
    -- 用户ID
    user_id BIGINT NOT NULL COMMENT '购物车所属用户的ID，指向用户表的id字段',
    
    -- 商品ID
    product_id BIGINT NOT NULL COMMENT '购物车中商品的ID，指向商品表的id字段',
    
    -- 商品数量
    quantity INT NOT NULL DEFAULT 1 COMMENT '购物车中该商品的数量，默认为1',
    
    -- 是否选中
    selected BOOLEAN DEFAULT TRUE COMMENT '是否选中该商品，默认为选中',
    
    -- 商品状态
    status TINYINT DEFAULT 1 COMMENT '商品项状态：1为有效，0为无效',
    
    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    
    -- 唯一约束，确保每个用户对于同一商品的购物车项唯一
    UNIQUE KEY uk_user_product (user_id, product_id, status) COMMENT '保证每个用户对于同一商品只有一个购物车记录',
    
    -- 索引设计
    INDEX idx_user_id (user_id) COMMENT '加快按用户ID查询购物车项的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='购物车商品项表，存储用户购物车中的商品信息';

-- 订单表
CREATE TABLE orders (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识订单的自增主键',
    
    -- 订单编号
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单唯一编号',
    
    -- 用户ID
    user_id BIGINT NOT NULL COMMENT '下单用户的ID，指向用户表的id字段',
    
    -- 总金额
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    
    -- 实际支付金额
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '用户实际支付金额（可能包含优惠、折扣等）',
    
    -- 地址快照
    address_snapshot JSON NOT NULL COMMENT '下单时的地址快照，包含收货信息',
    
    -- 订单状态
    status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已送达，4-已完成，-1-已取消',
    
    -- 支付类型
    payment_type TINYINT COMMENT '支付方式：1-支付宝，2-微信支付，3-信用卡支付',
    
    -- 支付时间
    payment_time TIMESTAMP NULL COMMENT '支付时间（若已支付）',
    
    -- 发货时间
    shipping_time TIMESTAMP NULL COMMENT '发货时间（若已发货）',
    
    -- 送达时间
    delivery_time TIMESTAMP NULL COMMENT '送达时间（若已送达）',
    
    -- 完成时间
    completion_time TIMESTAMP NULL COMMENT '订单完成时间（若已完成）',
    
    -- 取消时间
    cancel_time TIMESTAMP NULL COMMENT '订单取消时间（若已取消）',
    
    -- 取消原因
    cancel_reason VARCHAR(255) COMMENT '订单取消原因（若已取消）',
    
    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单最近更新时间',
    
    -- 索引设计
    INDEX idx_user_id (user_id) COMMENT '加快按用户ID查询订单的速度',
    INDEX idx_order_no (order_no) COMMENT '加快按订单编号查询订单的速度',
    INDEX idx_status (status) COMMENT '加快按订单状态查询的速度',
    INDEX idx_created_at (created_at) COMMENT '加快按创建时间查询的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci
COMMENT='订单表，存储用户的订单信息';

-- 订单品项表
CREATE TABLE order_items (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识订单商品项的自增主键',
    
    -- 订单ID
    order_id BIGINT NOT NULL COMMENT '所属订单的ID，指向订单表的id字段',
    
    -- 商品ID
    product_id BIGINT NOT NULL COMMENT '商品ID，指向商品表的id字段',
    
    -- 商品快照
    product_snapshot JSON NOT NULL COMMENT '下单时商品的快照，包含商品的相关信息（如名称、价格、描述等）',
    
    -- 数量
    quantity INT NOT NULL COMMENT '商品购买数量',
    
    -- 价格
    price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    
    -- 创建时间
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '商品项创建时间',
    
    -- 索引设计
    INDEX idx_order_id (order_id) COMMENT '加快按订单ID查询商品项的速度',
    INDEX idx_product_id (product_id) COMMENT '加快按商品ID查询商品项的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci 
COMMENT='订单商品项表，存储每个订单包含的商品信息';

-- 产品评论表
CREATE TABLE product_reviews (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识产品评论的自增主键',
    
    -- 用户ID
    user_id BIGINT NOT NULL COMMENT '评论的用户ID，指向用户表的id字段',
    
    -- 商品ID
    product_id BIGINT NOT NULL COMMENT '评论所属商品的ID，指向商品表的id字段',
    
    -- 订单ID
    order_id BIGINT NOT NULL COMMENT '关联的订单ID，指向订单表的id字段',
    
    -- 评分
    rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5) COMMENT '评分，取值范围为1到5',
    
    -- 内容
    content TEXT COMMENT '评论的文字内容',
    
    -- 图片
    images JSON COMMENT '评论的附带图片，存储图片的URL或路径信息',
    
    -- 状态
    status TINYINT DEFAULT 1 COMMENT '评论的状态：1: 显示, 0: 隐藏',
    
    -- 创建时间
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论的创建时间',
    
    -- 更新时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评论的更新时间',
    
    -- 删除时间
    deleted_at TIMESTAMP NULL COMMENT '评论的删除时间，若被删除则记录此字段',
    
    -- 索引设计
    INDEX idx_product_id (product_id) COMMENT '加快按商品ID查询评论的速度',
    INDEX idx_user_id (user_id) COMMENT '加快按用户ID查询评论的速度',
    INDEX idx_order_id (order_id) COMMENT '加快按订单ID查询评论的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci 
COMMENT='产品评论表，存储每个用户对商品的评价信息';

-- 支付记录表
CREATE TABLE payment_records (
    -- 主键
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '唯一标识支付记录的自增主键',
    
    -- 订单ID
    order_id BIGINT NOT NULL COMMENT '关联的订单ID，指向订单表的id字段',
    
    -- 支付单号
    payment_no VARCHAR(100) NOT NULL UNIQUE COMMENT '支付的唯一单号',
    
    -- 交易ID
    transaction_id VARCHAR(100) COMMENT '支付平台的交易ID',
    
    -- 支付金额
    amount DECIMAL(10,2) NOT NULL COMMENT '支付的金额',
    
    -- 支付方式
    payment_type TINYINT NOT NULL COMMENT '支付方式：1: 支付宝, 2: 微信, 3: 信用卡',
    
    -- 支付状态
    status TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0: 待支付, 1: 支付成功, 2: 支付失败, 3: 已退款',
    
    -- 回调时间
    callback_time TIMESTAMP NULL COMMENT '支付回调时间，记录支付平台的回调时间',
    
    -- 回调数据
    callback_data JSON COMMENT '支付平台返回的完整回调数据',
    
    -- 创建时间
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录的创建时间',
    
    -- 更新时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录的更新时间',
    
    -- 索引设计
    INDEX idx_order_id (order_id) COMMENT '加快按订单ID查询支付记录的速度',
    INDEX idx_payment_no (payment_no) COMMENT '加快按支付单号查询支付记录的速度',
    INDEX idx_transaction_id (transaction_id) COMMENT '加快按交易ID查询支付记录的速度',
    INDEX idx_created_at (created_at) COMMENT '加快按创建时间查询支付记录的速度'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8mb4 
COLLATE=utf8mb4_unicode_ci 
COMMENT='支付记录表，记录用户支付的详细信息';


-- 1. 用户地址表与用户表的关联
ALTER TABLE user_addresses
ADD CONSTRAINT fk_user_addresses_user_id
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 2. 产品表与产品分类表的关联
ALTER TABLE products
ADD CONSTRAINT fk_products_category_id
FOREIGN KEY (category_id) REFERENCES categories(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 3. 购物车表与用户和产品表的关联
ALTER TABLE shopping_cart_items
ADD CONSTRAINT fk_shopping_cart_items_user_id
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE shopping_cart_items
ADD CONSTRAINT fk_shopping_cart_items_product_id
FOREIGN KEY (product_id) REFERENCES products(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 4. 订单表与用户表的关联
ALTER TABLE orders
ADD CONSTRAINT fk_orders_user_id
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 5. 订单品项表与订单表和产品表的关联
ALTER TABLE order_items
ADD CONSTRAINT fk_order_items_order_id
FOREIGN KEY (order_id) REFERENCES orders(id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE order_items
ADD CONSTRAINT fk_order_items_product_id
FOREIGN KEY (product_id) REFERENCES products(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 6. 产品评论表与用户表、产品表和订单表的关联
ALTER TABLE product_reviews
ADD CONSTRAINT fk_product_reviews_user_id
FOREIGN KEY (user_id) REFERENCES users(id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE product_reviews
ADD CONSTRAINT fk_product_reviews_product_id
FOREIGN KEY (product_id) REFERENCES products(id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE product_reviews
ADD CONSTRAINT fk_product_reviews_order_id
FOREIGN KEY (order_id) REFERENCES orders(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 7. 支付记录表与订单表的关联
ALTER TABLE payment_records
ADD CONSTRAINT fk_payment_records_order_id
FOREIGN KEY (order_id) REFERENCES orders(id)
ON DELETE CASCADE ON UPDATE CASCADE;
