<template>
    <div
        class="z-10 fixed left-0 top-0 right-0 p-6 box-border flex items-center justify-between transition-all duration-200">
        <RouterLink :to="currentPath.patch"
            class="text-[var(--button-color,#91949b)] no-underline relative text-sm font-[inherit] tracking-[-0.125px] py-[10px] pr-4 pl-3 font-semibold transition duration-200 opacity-100 hover:scale-105 active:scale-50">
            <div class="relative z-10 flex items-center gap-2 min-h-[20px] transition duration-200">
                <i class="icon-[material-symbols--swipe-left-alt-outline-rounded] size-5"></i>
                {{ currentPath.text }}
            </div>
        </RouterLink>
    </div>
</template>

<script setup lang="ts">
import { reactive, watch, onMounted } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

const currentPath = reactive({ patch: "/account/register", text: "注册" });

const setCurrentPath = () => {
    if (route.path.includes("register")) {
        currentPath.patch = "/account/login";
        currentPath.text = "登录";
    } else {
        currentPath.patch = "/account/register";
        currentPath.text = "注册";
    }
};

watch(
    () => route.path,
    () => setCurrentPath()
);
onMounted(() => setCurrentPath());
</script>

<style scoped></style>
