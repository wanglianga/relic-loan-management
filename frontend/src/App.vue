<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background-color: #304156">
      <div class="sidebar-title">文物借展管理系统</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item-group title="总览">
          <el-menu-item index="/dashboard">
            <span>链路总览</span>
          </el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="博物馆">
          <el-menu-item index="/artifacts">
            <span>藏品档案</span>
          </el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="借展管理">
          <el-menu-item index="/loans">
            <span>借展申请</span>
          </el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="保险">
          <el-menu-item index="/insurances">
            <span>保单管理</span>
          </el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="承运">
          <el-menu-item index="/transports">
            <span>运输记录</span>
          </el-menu-item>
          <el-menu-item index="/handovers">
            <span>点交记录</span>
          </el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="场馆">
          <el-menu-item index="/monitorings">
            <span>展期监测</span>
          </el-menu-item>
          <el-menu-item index="/returns">
            <span>撤展归还</span>
          </el-menu-item>
        </el-menu-item-group>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; font-size: 18px; font-weight: 600; color: #333">
        文物借展管理系统
      </el-header>
      <el-main style="background: #f5f7fa; padding: 20px">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
.sidebar-title {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  border-bottom: 1px solid #3a4a5e;
}
.el-menu-item-group__title {
  color: #8a9bb5 !important;
  font-size: 12px !important;
  padding-left: 20px !important;
}
</style>
