# CPF Curl 传输与资源清理修复

来源：[CPF Ktor](https://gitcode.com/CPF-KMP-CMP/ktor)，固定版本 `3.3.3-1.0.0`，保留原版权声明和 [Apache-2.0 许可证](LICENSE)。

保留发布源码，只替换两个内部文件的取消调度、资源清理和协议版本读取逻辑：

- 取消请求交回已有的完成队列，统一更新 `activeHandles`、关闭响应并释放资源。发布版直接释放句柄却保留登记，超时或取消后的 `HttpClient.close()` 会抛出 `CURLM_BAD_EASY_HANDLE` 并终止进程。
- `CurlProcessor` 在事件循环每轮结束时 `yield()`，让同线程的取消任务能够执行；`CurlMultiApiHandler` 每轮先处理取消队列，避免等待远端响应才释放连接。只恢复完成队列仍会导致单个挂起连接不能及时取消，已通过服务端观察 TCP 断开验证。
- 忽略已完成句柄的延迟背压恢复，避免对已释放句柄调用 `curl_easy_pause`。
- `RequestHolder` 统一持有并释放请求头及全部 native 回调引用，覆盖正常完成、取消和关闭路径，避免取消分支漏清理或完成分支重复释放。
- 取消监听保留到完整传输结束，由 `RequestHolder` 注销，避免收到响应头后取消下载仍保持连接。取消回调携带原请求身份，防止延迟回调误操作复用的 native 地址。
- 通过 `CURLINFO_HTTP_VERSION` 读取实际协商的协议版本，避免读取未赋值变量后将 HTTP/1.0 错误报告为 HTTP/1.1。

其余 Kotlin 源码由 `:shared:prepareOhosCurlSources` 从同版本的 CPF `sources.jar` 提取；C interop 和 libcurl / OpenSSL 仍使用 CPF 发布的 KLIB。Android、iOS 引擎不经过此源码编译。

升级 Ktor 时需要先确认发布版已覆盖上述修复，并通过响应头前后取消、客户端关闭及协议版本回归，再删除本目录和源码提取配置，恢复 OHOS 对 `ktor-client-curl` 的直接依赖。不能仅修改版本号后继续套用旧文件。

回归与实际运行截图见[迁移验证记录](../../../docs/multiplatform-migration.md#鸿蒙切换-curl2026-09-08)。
