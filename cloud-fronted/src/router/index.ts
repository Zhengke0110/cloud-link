import { createRouter, createWebHistory } from "vue-router";
import type { RouteRecordRaw } from "vue-router";
import { LayoutMenu } from '@/config'
const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "home",
    component: () => import("@/views/home"),
    meta: { layout: LayoutMenu.BasicLayout }
  },
  {
    path: "/center",
    name: "center",
    component: () => import("@/views/center"),
    meta: { layout: LayoutMenu.BasicLayout }
  }, {
    path: "/scheme",
    name: "scheme",
    component: () => import("@/views/scheme"),
    meta: { layout: LayoutMenu.BasicLayout }
  }, {
    path: "/create",
    name: "create",
    component: () => import("@/views/create"),
    meta: { layout: LayoutMenu.BasicLayout }
  }, {
    path: "/links",
    name: "links",
    component: () => import("@/views/links/links"),
    meta: { layout: LayoutMenu.BasicLayout }
  }, {
    path: "/grouping",
    name: "grouping",
    component: () => import("@/views/links/grouping"),
    meta: { layout: LayoutMenu.BasicLayout }
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
