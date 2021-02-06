# sems

智能快递取件系统安卓端

本程序特点：自己实现的RPC，条形码生成与识别、多模块开发。

# EAN-13识别

EAN-13条形码识别用到的是我自己写的一个基于OpenCV的库，参考：[ean-13-recognition](https://github.com/zzyandzzy/ean-13-recognition)

# EAN-13条形码生成

EAN-13条形码是基于Android写的一个工具类，参考：[EAN13Utils](https://github.com/GuaSeed/sems/raw/master/app/src/main/java/cool/zzy/sems/application/util/EAN13Utils.java)

# RPC

- [intent-rpc-common](https://github.com/GuaSeed/sems/tree/master/intent-rpc-common)是一些RPC工具类，如序列化对象等

- [intent-rpc-client](https://github.com/GuaSeed/sems/tree/master/intent-rpc-client)是RPC的客户端。

- [intent-rpc-model](https://github.com/GuaSeed/sems/tree/master/intent-rpc-model)是RPC服务需要的POJO，如用户等。

- [intent-rpc-service](https://github.com/GuaSeed/sems/tree/master/intent-rpc-service)是RPC的Service层，方便客户端使用。
