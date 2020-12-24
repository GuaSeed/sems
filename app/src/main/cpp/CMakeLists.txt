cmake_minimum_required(VERSION 3.4.1)

add_library(
        native-lib
        SHARED
        native-lib.cpp
        ean_13.h
        ean_13.cpp
        ean_13_constant.h
        cpp_log.h
        barcode.h
        barcode.cpp)

#导入头文件
include_directories(include)

#导入库文件
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")

find_library(
        log-lib
        log)

target_link_libraries(
        native-lib
        opencv_java4
        android
        jnigraphics
        ${log-lib})