### Kotlin扩展属性、函数

* SystemServiceKt.kt

  诸如AM、WM等**SystemServiceManager**实例扩展属性。

* NavigationKt.kt

  Jetpack组件**navigation**导航到某页面扩展方法。

  eg：**nav().navigateTo(...)**。

* FragmentKt.kt Fragment实例化扩展函数。

  eg：**newInstanceFragment<T>()、addFragment(...)、replaceFragment(...)** etc。

* LogKt.kt

  Log日志打印扩展函数。

  eg：**logv()、logd()、logi()、logw()、loge()**。

* View的一些扩展函数。

  可查看com.infore.jetpackmvvm.ext.view包下的各View扩展类。

* ServiceKt.kt

  启动、绑定、停止服务以及判断服务是否运行，eg：**startService(...)、bindService(...)、stopService(...)、isServiceRunning(...)**。

* DateFormatKt.kt

  时间戳、时间字符串、Date、Calendar类型相互转化。

  eg：**millis2String(...)、millis2Date(...)、millis2Calendar(...)、string2Millis(...)、string2Date(...)、**

  **string2Calendar(...)、transformDateFormat(...)、 date2String(...)、date2Millis(...)、date2Calendar(...)、**

  **calendar2String(...)、calendar2Millis(...)、calendar2Date(...)**。

* DateTimeKt.kt

  时间计算，时间获取，生肖、星座获取。

  eg：**getStartOfDay()、getEndOfDay()、daysBeforeOrAfter(...)、monthsBeforeOrAfter(...)** etc。

* ColorKt.kt

  获取颜色、矫正透明度、十六进制字符串与颜色值互转、加深颜色、变浅颜色、判断是否是深色的颜色、获取随机颜色值。

  eg：**getColor(...)、adjustColorAlpha(...)、colorInt2HexString()、hexString2ColorInt()、darker()、lighter()、isDarkColor()、getRandomColor()**。

* DisplayKt.kt

  Int/Float/Double 类型的 px 与 dp 互转、px 与 sp 互转。

  eg：**10.dp、10f.dp、10.0.dp、26.toDp()、26f.toDp()、26.0.toDp()、10.sp、10f.sp、10.0.sp、26.toSp()、26f.toSp()、26.0.toSp()**。

* ScreenKt.kt

  获取屏幕宽高、屏幕密度、屏幕密度dpi、屏幕旋转角度、屏幕休眠时长、状态栏高度、标题栏高度，判断横竖屏、锁屏，设置横竖屏等。

  eg：**screenWidth、screenHeight、screenDensity、screenDensityDpi、getScreenRotation()、sleepDuration、statusBarHeight、titleBarHeight、**

  **isLandscape、isPortrait、isScreenLock、setLandscape()、setPortrait()、isFullscreen、setFullscreen()、setNonFullscreen()、**

  **toggleFullscreen()、isTablet。**

* GsonKt.kt

  Gson实例扩展，将对象转为JSON字符串。

  eg：**defaultGsonBuilder、defaultGson、excludeFieldsWithoutExposeGson、toJson()。**

* JSONObjectKt.kt

  JSONObject解析，JSONObject与HashMap<String, String>互转, Json String转JSONObject。

  eg：**getIntValue(...)、getLongValue(...)、getDoubleValue(...)、getBooleanValue(...)、getStringValue(...)、getJSONObjectValue(...)、getJSONArrayValue(...)、toMap()、toJSONObject()。**

* Base64Kt.kt

  Base64编码转换。

  eg：**encodeBase64(...)、decodeBase64(...)。**

* MD5Kt.kt

  MD5加密。

  eg：**md5(...)**。

* PhoneKt.kt

  手机信息扩展属性与方法。

* CloseableKt.kt

  IO流关闭。

  eg：**closeSafely()、closeIO(...)、closeIOQuietly(...)。**

* ShellKt.kt

  cmd命令扩展。

  eg：**checkRootPermission()、executeCmd(...)** etc。

* ClipboardKt.kt

  剪贴板扩展方法：复制文本到剪贴板、获取剪贴板文本、复制Uri到剪贴板、获取剪贴板Uri、复制Intent到剪贴板、获取剪贴板Intent。

  eg：**copy2Clipboard()、getClipboardText()、getClipboardUri()、getClipboardIntent()。**

* CharsetKt.kt

  转换字符串的字符集编码、转换文件的字符集编码。

* FileKt.kt

  文件操作：判断文件是否存在、获取文件、重命名、复制、移动、删除、文件读写、获取文件MD5、通知系统扫描文件、获取文件系统的总大小、获取文件系统的可用大小等。

* TimeUnitKt.kt

  年月日时分秒的毫秒值属性扩展。

  eg：**Int.MILLISECONDS、Int.SECONDS、Int.MINUTES、Int.HOURS、Int.DAYS、Int.MONTHS、Int.YEARS**。

* KeyboardKt.kt

  软键盘是否可见, 显示、隐藏软键盘, 延迟隐藏虚拟键盘, 切换软键盘的状态, 注册软键盘监听, 解决AndroidBug5497, 解决软键盘泄露, 点击空白区域隐藏软键盘。

  eg：**isSoftInputVisible、showSoftInput()、hideSoftInput()、hideSoftInputDelayed(...)、toggleSoftInput()、registerSoftInputChangedListener(...)、unregisterSoftInputChangedListener()、fixAndroidBug5497()、fixSoftInputLeaks()** etc。

* ConvertKt.kt

  OutputStream与InputStream与ByteArray与String四者之间互转、内存单位大小之间互转、时间单位大小之间互转、ByteArray与JSONObject互转、ByteArray与JSONArray互转、ByteArray与Parcelable互转、ByteArray与Serializable互转。

* FileIOKt.kt

  IO流读写文件。