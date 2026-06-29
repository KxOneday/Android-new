# 纪念日 Android

这是根据 `KxOneday/jinianri-ios` 复刻的 Android 版本，使用 Kotlin + Jetpack Compose 实现。

已包含：
- 主页卡片列表、搜索、分类筛选、置顶、批量选择删除、即将到期、浮动新建按钮
- 模板快速创建
- 新建/编辑纪念日，包含标题、备注、日期、类型、分类、标签、颜色、渐变、图标、圆角、阴影、提醒字段
- 详情页、精确倒计时、备注、标签、提醒、删除、分享文本
- 时间计算器：日期间隔、日期推算、时间换算、工作日计算
- 设置页：应用锁、通知开关、测试通知入口、关于信息
- SharedPreferences + Gson 本地持久化

打开方式：
1. 用 Android Studio 打开本目录 `jinianri-android`
2. 等 Gradle 同步完成
3. 运行 `app`

备注：当前环境没有 Android SDK/Gradle 命令，未在本机编译 APK；项目结构和依赖已按 Android Studio 可同步项目生成。
