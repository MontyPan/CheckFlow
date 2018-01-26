> # 關關難過關關過的輔助系統 #


目標
====

輔助使用者在面對這樣子的流程時，可以紀錄 / 檢視目前進度：

```
Start ---> A    ---> B  ---> ... ---> X    ---> End
           + A1                       + X1
           + A2                       + X2
                                      + X3
```

* A、B、...、X 稱之為「檢查點」。
* 檢查點之間是有前後關係的，亦即 A 必須確認完畢才能開始進入 B。
* 檢查點下可以有「檢查細項」（如 A 下有 A1、A2）。
	檢查細項都確認後，該檢查點才可以進行確認。

本系統另一個重點功能為：檢查點與檢查細項都可由使用者自行定義。


Build
=====

Build tool 是 Maven，`mvn install` 可得 war 檔。

由於 dependency 當中的 [GF] 尚未進 Maven Repository，
必須先自行作 [GF] 的 `mvn install`。

[GF]: https://github.com/DontCareAbout/GF


參數設定
========

此系統需要一個目錄存放資料，預設路徑為 `D:\test\CheckFlow`。
要修改這個參數，請以 UTF-8 建立一個 `CheckFlow.xml`，
放到 Web Server 的 classpath 下。
`CheckFlow.xml` 的內容參見 [dev-setting.xml]。

[dev-setting.xml]: https://github.com/MontyPan/CheckFlow/blob/master/src/main/resources/dev-setting.xml
