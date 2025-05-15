import { createRouter, createWebHistory } from "vue-router";
import type { RouteRecordRaw } from "vue-router";
const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "home",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/login",
    redirect: "/account/login",
  },
  {
    path: "/register",
    redirect: "/account/register",
  },
  {
    path: "/account",
    name: "Account",
    component: () => import("@/layouts/AccountLayout.vue"),
    children: [{
      path: "login",
      name: "Login",
      component: () => import("@/views/account/Login.vue"),
    }, {
      path: "register",
      name: "Register",
      component: () => import("@/views/account/Register.vue"),
    },]
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    component: () => import("@/views/not-found"),
  },
];

const router = createRouter({
  routes,
  history: createWebHistory(),
});

export default router;
