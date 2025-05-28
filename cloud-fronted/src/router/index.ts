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
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
  }, {
    path: "/scheme",
    name: "scheme",
    component: () => import("@/views/scheme"),
    meta: { layout: LayoutMenu.BasicLayout }
  }, {
    path: "/create",
    name: "create",
    component: () => import("@/views/create"),
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
  }, {
    path: "/links",
    name: "links",
    component: () => import("@/views/links/links"),
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
  }, {
    path: "/grouping",
    name: "grouping",
    component: () => import("@/views/links/grouping"),
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
  }, {
    path: "/domain",
    name: "domain",
    component: () => import("@/views/links/domain"),
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
  }, {
    path: "/analysis",
    name: "analysis",
    component: () => import("@/views/analysis"),
    meta: { layout: LayoutMenu.BasicLayout, auth: true }
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

// 路由拦截器
router.beforeEach((to, _, next) => {
  // 检查路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.auth);

  // 检查用户是否已登录（通过localStorage中的token判断）
  const token = localStorage.getItem('userToken');
  const isAuthenticated = !!token;

  if (requiresAuth && !isAuthenticated) {
    // 需要认证但未登录，重定向到登录页
    next({
      path: '/account/login',
      query: { redirect: to.fullPath } // 保存原始路径，登录后可以重定向回来
    });
  } else if ((to.path === '/account/login' || to.path === '/login') && isAuthenticated) {
    // 已登录用户访问登录页，重定向到首页
    next('/home');
  } else {
    // 正常访问
    next();
  }
});

export default router;
