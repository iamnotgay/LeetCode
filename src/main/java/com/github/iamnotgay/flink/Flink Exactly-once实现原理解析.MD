## Flink Exactly-once实现原理解析

### 分布式快照机制

    flink采用基于 checkpoint 的分布式快照机制，能够保证作业出现 fail-over 后可以从最新的快照进行恢复，
    即分布式快照机制可以保证 Flink 系统内部的“精确一次”处理。
    
    Flink 分布式快照的核心元素
     Barrier（数据栅栏）：可以把 Barrier 简单地理解成一个标记，该标记是严格有序的，并且随着数据流往下流动。每个 Barrier 都带有自己的
     ID，Barrier 极其轻量，并不会干扰正常的数据处理。
     
     异步：每次在把快照存储到我们的状态后端时，如果是同步进行就会阻塞正常任务，从而引入延迟。因此 Flink 在做快照存储时，采用异步方式
     
     增量：由于 checkpoint 是一个全局状态，用户保存的状态可能非常大，多数达 G 或者 T 级别，checkpoint 的创建会非常慢，而且执行时占用
     的资源也比较多，因此 Flink 提出了增量快照的概念。也就是说，每次进行的全量 checkpoint，是基于上次进行更新的。
     
 ## 端到端Exactly-once
    内部保证 —— checkpoint
    source 端 —— 支持数据重放
    sink 端 —— 从故障恢复时，数据不会重复写入外部系统（幂等写入、事务写入）
    
 ## 事务写入
    实现思想：构建的事务对应着 checkpoint，等到 checkpoint 真正完成的时候，才把所有对应的结果写入 sink 系统中
    实现方式：
        (1)预写日志（WAL）: 把结果数据先当成状态保存，然后在收到 checkpoint 完成的通知时，一次性写入 sink 系统。
        缺点：做不到真正意义上的Exactly-once，写到一半时挂掉可能重复写入
        (2)两阶段提交（2PC) :
            - 对于每个 checkpoint，sink 任务会启动一个事务，并将接下来所有接收的数据添加到事务
            - 然后将这些数据写入外部 sink 系统，但不提交它们，这时只是“预提交”
            - 当它收到 checkpoint 完成的通知时，它才正式提交事务，实现结果的真正写入
            - 这种方式真正实现了 exactly-once，它需要一个提供事务支持的外部 sink 系统
            
            Flink 中两阶段提交的实现方法被封装到了 TwoPhaseCommitSinkFunction 这个抽象类中，我们只需要实现其中的
            beginTransaction、preCommit、commit、abort 四个方法就可以实现“精确一次”的处理语义。
            
            beginTransaction，在开启事务之前，创建一个临时文件夹，然后把数据写入这个文件夹里面
            preCommit，在预提交阶段，将内存中缓存的数据写入文件并关闭；
            commit，在提交阶段，将预提交写入的临时文件放入目标目录下，这代表着最终的数据会有一些延迟
            abort，在中止阶段，我们删除临时文件
            
### Flink-Kafka Exactly-once
    Flink 通过强大的异步快照机制和两阶段提交，实现了“端到端的精确一次语义”。
    “端到端（End to End）的精确一次”，指的是 Flink 应用从 Source 端开始到 Sink 端结束，数据必须经过的起始点和结束点。Flink 自身
    是无法保证外部系统“精确一次”语义的，所以 Flink 若要实现所谓“端到端（End to End）的精确一次”的要求，那么外部系统必须支持
    “精确一次”语义；然后借助 Flink 提供的分布式快照和两阶段提交才能实现。  
    
    整个过程可以总结为下面四个阶段：
    - 一旦 Flink 开始做 checkpoint 操作，那么就会进入 pre-commit 阶段，同时 Flink JobManager 会将检查点 Barrier 注入数据流中            
    - 当所有的 barrier 在算子中成功进行一遍传递，并完成快照后，则 pre-commit 阶段完成；
    - 等所有的算子完成“预提交”，就会发起一个“提交”动作，但是任何一个“预提交”失败都会导致 Flink 回滚到最近的 checkpoint；
    - pre-commit 完成，必须要确保 commit 也要成功，上图中的 Sink Operators 和 Kafka Sink 会共同来保证
    