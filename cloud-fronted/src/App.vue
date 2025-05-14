<script setup lang="ts"></script>

<template>
  <transition name="page" mode="out-in">
    <router-view></router-view>
  </transition>
</template>

<style>
.page-enter-active,
.page-leave-active {
  transition:
    opacity 0.3s,
    transform 0.3s;
}

.page-enter-from,
.page-leave-to {
  opacity: 0;
  transform: translateY(10px);
}
</style>
