package chapter07_sparksql优化实战

/**
 * @Description: spark sql 基本优化: 1、编码优化 2、参数优化(也就是提交时的参数)
 * @Author: tiancy
 * @Create: 2022/7/11
 */
object SparkSql02_参数的优化 {

/*
TODO spark sql 基础优化2: 参数优化
  一、spark 提交任务时,提交参数,企业推荐的参数如下
    bin/spark-submit \
    --class com.xx.xx.xx\
    --master yarn
    --deploy-mode cluster
    --num-executors 100\
    --driver-memory 5g \
    --executor-memory 5g \
    --executor-cores 4 \
    xxxxx.jar \
    名称	| 说明
    --num-executors	配置 Executor 的数量 : 100 - 200
    --driver-memory	配置 Driver 内存（ 影响不大） 1 - 5G
    --executor-memory	配置每个 Executor 的内存大小 5 - 20G
    --executor-cores	配置每个 Executor 的 CPU core 数量 4 - 8
    调节原则：尽量将任务分配的资源调节到可以使用的资源的最大限度.默认情况下，Spark采用资源`预分配`的方式。即为每个Spark应用设定一个最大可用资源总量，该应用在整个生命周期内都会持有这些资源
  二、spark动态资源分配
    spark默认情况下,采用资源预分配的方式,即为每一个spark应用设定一个最大可用资源总量.
    但是spark提供了一种动态分配资源的机制,不使用的资源,集群会回收,如果资源不够,会重新申请.
    动态申请: 如果有新任务在等待,并且等待时间超过`Spark.dynamicAllocation.schedulerBacklogTimeout(默认1秒)`  -- 动态分配.资源调度超时 则会依次启动 executor,每次启动 2的n次幂个.
    动态移除: 如果executor空闲时间超过`spark.dynamicAllocation.executorIdleTimeout(默认1分钟)`这个参数,则executor会被移除,除非有缓存的数据.
    相关参数:
      - spark.dynamicAllocation.enabled = true
      - spark.dynamicAllocation.executorIdleTimeout（默认60s）。Executor闲置了超过此持续时间，将被删除
      - spark.dynamicAllocation.cachedExecutorIdleTimeout（默认infifinity）。已缓存数据块的 Executor 闲置了超过此持续时间，则该执行器将被删除
      - spark.dynamicAllocation.initialExecutors（默认spark.dynamicAllocation.minExecutors）。初始分配Executor 的个数。如果设置了--num-executors（spark.executor.instances）并且大于此值，该参数将作为Executor 初始的个数
      - spark.dynamicAllocation.maxExecutors（默认infifinity）。Executor 数量的上限
      - spark.dynamicAllocation.minExecutors（默认0）。Executor 数量的下限
      - spark.dynamicAllocation.schedulerBacklogTimeout（默认1s）。任务等待时间超过了此期限，则将请求新的 Executor
    三、合理设置shuffle并行度.
      shuffle partitions 的数量默认为200,spark不会动态进行调整,而是需要我们基于数据规模进行调整.  ==> 合理的数值: 是分区数量的 1.5 到 2 倍.
      更改方式 : spark.sql(“set spark.sql.shuffle.partitions=100”)
*/
}
