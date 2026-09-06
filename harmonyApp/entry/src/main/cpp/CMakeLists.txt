cmake_minimum_required(VERSION 3.5.0)
project(wanandroid)

set(MODULE_ROOT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/../../..)
if(DEFINED PACKAGE_FIND_FILE)
    include(${PACKAGE_FIND_FILE})
endif()
include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include)
list(APPEND CMAKE_PREFIX_PATH ${MODULE_ROOT_PATH}/oh_modules/@cpf-kmp-cmp/compose)
find_package(compose REQUIRED CONFIG)

add_library(entry SHARED napi_init.cpp)
target_link_libraries(entry PUBLIC
    libace_napi.z.so
    libhilog_ndk.z.so
    libdeviceinfo_ndk.z.so
    librawfile.z.so
    ${MODULE_ROOT_PATH}/libs/arm64-v8a/libwanandroid.so
    compose::skikobridge
    libc++_shared.so
)
