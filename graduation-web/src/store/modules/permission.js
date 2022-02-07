import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
// 匹配权限
function hasPermission(roles, route) {
  // console.log(route.meta.roles)
  if (route.meta && route.meta.roles) {
    for (var i = 0; i < route.meta.roles.length; i++) {
      if (roles === route.meta.roles[i]) {
        return true
      }
    }
    return false
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    // console.log(tmp);
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes) // 将过滤后的路由和constantRoutes存起来
  }
}

// 筛选
const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      // 路由是否有admin,有直接全部显示
      // 过滤路由
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      // accessedRoutes这个就是当前角色可见的动态路由
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
