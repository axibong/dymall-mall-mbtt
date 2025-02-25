//package cn.mbtt.service.component;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 商品同步任务定时器。
// * <p>
// * 该类用于定时将商品数据从数据库同步到 Elasticsearch 中。
// * 通过使用 Spring 的定时任务功能，可以按指定的时间间隔（如每天午夜12点）定期执行同步任务。
// * </p>
// */
//@Component
//public class ProductSyncScheduler {
//
//    /**
//     * 注入商品数据同步服务，负责与 Elasticsearch 进行交互。
//     */
//    @Autowired
//    private EsProductService esProductService;
//
//    /**
//     * 每天午夜12点执行商品数据同步任务。
//     * <p>
//     * 该方法会调用 {@link EsProductService} 的 {@code importAll} 方法，将数据库中的所有商品数据导入到 Elasticsearch。
//     * 定时任务可以根据实际需求调整执行的频率，例如可以改为每小时执行一次。
//     * </p>
//     *
//     * @Scheduled(cron = "0 0 0 * * ?") 配置任务每天午夜12点执行。
//     * <ul>
//     *   <li>秒：0</li>
//     *   <li>分钟：0</li>
//     *   <li>小时：0</li>
//     *   <li>日：每一天</li>
//     *   <li>月：每月</li>
//     *   <li>星期：每周</li>
//     * </ul>
//     *
//     * @throws Exception 如果同步任务执行失败，则抛出异常
//     */
//    @Scheduled(cron = "0 0 0 * * ?")  // 每天午夜12点执行
//    public void syncProductsToES() {
//        try {
//            // 调用同步方法，将商品数据同步到 Elasticsearch
//            int importedCount = esProductService.importAll();
//            System.out.println("成功导入 " + importedCount + " 个商品到 Elasticsearch");
//        } catch (Exception e) {
//            // 输出错误日志
//            System.err.println("商品同步到 Elasticsearch 失败：" + e.getMessage());
//        }
//    }
//
//}
