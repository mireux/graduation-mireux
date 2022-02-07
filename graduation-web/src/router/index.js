import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import icon from 'element-ui/packages/icon'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'table' }
    }]
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
// 异步挂载的路由
export const asyncRoutes = [
  // 预定页面 权限:student
  {
    path: '/student/1',
    component: Layout,
    redirect: '/student/booking',
    alwaysShow: true,
    meta: {
      title: '预定界面',
      icon: 'form',
      roles: ['Student']
      // roles: ['Student','Admin']
    },
    children: [
      {
        path: '/student/booking',
        component: () => import('@/views/student/booking'),
        meta: {
          title: '预定座位',
          icon: 'form'
        }
      },
      {
        path: '/student/check',
        component: () => import('@/views/student/check'),
        meta: {
          title: '我的预定',
          icon: 'form',
          roles: ['Student']
        }
      }
    ]
  },
  // 学习报告 权限：student
  {
    path: '/student/2',
    component: Layout,
    redirect: '/student/studyReport',
    meta: {
      roles: ['Student']
    },
    children: [
      {
        path: '/student/studyReport',
        component: () => import('@/views/student/studyReport'),
        name: 'studyReport',
        meta: {
          title: '学习报告',
          icon: 'dashboard'
        }
      }
    ]
  },
  // 注意事项 路由：/student/warning 权限：student
  {
    path: '/student/3',
    component: Layout,
    redirect: '/student/warning',
    meta: {
      roles: ['Student']
    },
    children: [
      {
        path: '/student/warning',
        component: () => import('@/views/student/warning'),
        name: 'Warning',
        meta: {
          title: '注意事项',
          icon: 'nested'
        }
      }
    ]
  },
  {
    path: '/admin/1',
    component: Layout,
    redirect: '/admin/manageRoom',
    meta: {
      roles: ['Admin']
    },
    children: [
      {
        path: '/admin/manageRoom',
        component: () => import('@/views/admin/manageRoom'),
        meta: {
          title: '自习室管理',
          icon: 'user'
        }
      }
    ]
  },
  {
    path: '/admin/2',
    component: Layout,
    redirect: '/admin/manageSeat',
    meta: {
      roles: ['Admin']
    },
    children: [
      {
        path: '/admin/manageSeat',
        component: () => import('@/views/admin/manageSeat'),
        meta: {
          title: '座位管理',
          icon: 'user'
        }
      }
    ]
  },
  // 个人中心 权限：student Admin
  {
    path: '/student/4',
    component: Layout,
    redirect: '/student/personal',
    meta: {
      roles: ['Student', 'Admin']
    },
    children: [
      {
        path: '/student/personal',
        component: () => import('@/views/student/personal'),
        name: 'personal',
        meta: {
          title: '个人中心',
          icon: 'user'
        }
      }
    ]
  },

  // // role:admin path:/account 账号管理
  // {
  //   path: '/account',
  //   component: Layout,
  //   redirect: '/account/userinfo',
  //   alwaysShow: true,
  //   name: 'Account',
  //   meta: {
  //     title: '账号管理',
  //     icon: 'dashboard',
  //     roles: ['Admin']
  //   },
  //   children: [{
  //     path: '/account/userinfo',
  //     component: () => import('@/views/admin/account/userinfo'),
  //     name: 'Userinfo',
  //     meta: {
  //       title: '用户查询',
  //       icon: 'dashboard',
  //       roles: ['Admin']
  //     }
  //   }]
  // },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]
const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
