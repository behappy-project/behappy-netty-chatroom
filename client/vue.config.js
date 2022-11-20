const { defineConfig } = require('@vue/cli-service')
module.exports=defineConfig({
  transpileDependencies: true,
  // 设置打包输出路径, 以下配置的相对路径都是基于此路径
  outputDir: '../server/src/main/resources/static/',
  // 设置访问的根路径
  publicPath:process.env.BASE_URL,
  // 设置js/css/fonts等资源路径
  assetsDir: '.',
  // index.html的路径
  indexPath: 'index.html',
  productionSourceMap:false,
  css:{
    sourceMap:true
  },
  devServer:{
    proxy: {
      '': {
        target: process.env.PROXY_SERVER,
        ws:true
      },
      '/assets': {
        target: process.env.PROXY_SERVER,
        ws:true
      },
      '/socket.io': {
        target: process.env.PROXY_SERVER,
        ws:true
      }
    }
  }
});
