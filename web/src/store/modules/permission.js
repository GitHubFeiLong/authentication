import Layout from "@/layout";
import Iframe from "@/views/iframe";
import {goudongWebAdminComponent} from "@/router/modules/goudong-web-admin-router";
import LocalStorageUtil from "@/utils/LocalStorageUtil";
import {resetRouter} from "@/router";

function hasPermissionByMenus(menus, route) {
  if (route.type !== 0 && route.path) {
    return menus.some(menu => route.path === menu.path);
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param menus
 */
export function filterAsyncRoutes(routes, menus) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermissionByMenus(menus, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, menus)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  createdRoutes: false,
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  // 设置标识，在路由守卫判断该值进行重构路由
  SET_CREATED_ROUTES: (state, flag) => {
    state.createdRoutes = flag
  },
}

const actions = {
  /**
   * 动态添加路由
   * @param commit
   * @returns {Promise<unknown>}
   */
  generateRoutes({ commit }) {
    return new Promise(resolve => {
      let permission_routes = LocalStorageUtil.getPermissionRoutes();
      // 循环设置组件
      permissionRoutesComponent(permission_routes);
      // 404 路由必须放在最后
      permission_routes.push({ path: '*', redirect: '/404', hidden: true })
      commit('SET_ROUTES', permission_routes)
      // 设置标识，重新创建路由信息
      commit('SET_CREATED_ROUTES', true)
      console.log("permission_routes", permission_routes)
      resolve(permission_routes)
    })
  }
}

/**
 * 处理路由信息
 * @param permission_routes
 */
function permissionRoutesComponent(permission_routes) {
  // 循环处理路由
  permission_routes.forEach(item => {
    if (item.parentId === null || item.parentId === undefined) { // 顶级菜单
      item.component = Layout
    } else { // 次级菜单
      let com = goudongWebAdminComponent.find(c => {
        return c.permissionId === item.permissionId
      })
      // 有预定义的菜单，就使用对应的组件
      if (com) {
        item.component = com.component
      } else { // 其余使用 iframe 组件
        item.component = Iframe
      }
    }
    // 处理子菜单
    if (item.children && item.children.length > 0) {
      // 下级只有一个子菜单，也让本菜单显示出来
      if (item.children.length === 1) {
        item.alwaysShow = true;
      }
      permissionRoutesComponent(item.children)
    }
  })
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
