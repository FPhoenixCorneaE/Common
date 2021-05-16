plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("version") {
            // 自定义 plugin 的 id，其他 module 引用要用到
            id = "com.fphoenixcorneae.plugin"
            // 实现这个插件的类的路径
            implementationClass = "com.fphoenixcorneae.plugin.VersionPlugin"
        }
    }
}
