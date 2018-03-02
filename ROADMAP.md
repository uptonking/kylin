# Roadmap for kylin

## Target
1. reliable olap query system 
2. support common data sources
3. support more query methods
4. new storage

## todo 
- support more local file formats, like .xls, db.dump
- integrate with datax
- integrate with playboard
- ui操作界面替换
- 加快计算列基数的速度

## faq
- MeasureIngester怎么理解
- java的基本数据类型变量可变吗
- core-metadata模块的realization怎么理解
是结果存储的实现，还是对结果查询的实现
- core-cube模块
    - FuzzyMaskEncoder
    - LazyRowKeyEncoder懒编码
    - HierarchyMask
    - GridTable
- core-storage
    - TsConditionExtractor
    - hybrid
- engine-mr
    - MapContextGTRecordWriter
    - MergeStatisticsStep
    - RowKeyDistributionCheckerJob
- storage-hbase
    - Results.getValueAsByteBuffer()
    - ZookeeperJobLock
    - RangeKeyDistributionJob
    - ExpectedSizeIterator
    - AggrKey 
- query
    - MassInUDF
    - OLAPEnumerator
    - AggregateProjectReduceRule
    - OLAPProjectRule
    - OLAPRel
    - ColumnRowType
    - RemoveBlackoutRealizationsRule
