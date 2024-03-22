import {getUserDetailApi, getUserDetailApiByPost} from '@/api/user'
import router, {resetRouter} from '@/router'
import LocalStorageUtil from '@/utils/LocalStorageUtil'
import {arrayToTree} from "@/utils/tree";
import store from "@/store";

const state = {
  user: {}
}

const mutations = {
  SET_USER: (state, user) => {
    state.user = user
  },
}
const actions = {
  /**
   * <pre>
   *   获取用户信息（用户名，角色，菜单，路由等信息）
   * </pre>
   * @param commit
   * @returns {Promise<unknown>}
   */
  getUserDetailByToken({commit}) {
    return new Promise((resolve, reject) => {
      console.log("执行获取token信息");
      const token = LocalStorageUtil.getToken()
      getUserDetailApiByPost(token.accessToken).then(data => {
        // 设置用户
        const user = {
          id: data.id,
          appId: data.appId,
          realAppId: data.realAppId,
          realAppName: data.realAppName,
          realAppHome: data.realAppHome,
          username: data.username,
          roles: data.roles
        };
        LocalStorageUtil.setUser(user);
        commit('SET_USER', user)

        // 设置用户菜单
        const menus = data.menus

        let permission_routes = [] // 权限路由信息
        let permission_buttons = [] // 权限按钮
        if (menus) {
          // 循环所有
          menus.map((item, index, array) => {
            const metadata = item.metadata ? JSON.parse(item.metadata) : {};
            switch (item.type) {
              case 1:
                let menu = {
                  id: item.id,
                  parentId: item.parentId,
                  permissionId: item.permissionId,
                  path: item.path,
                  name: item.name,
                  alwaysShow: item.parentId == null ? !item.hide : undefined,
                  meta: item.meta ? JSON.parse(item.meta) : {},
                  sortNum: item.sortNum,
                  openModel: item.openModel
                }
                /*
                  给菜单赋值
                 */
                // 默认title
                if (menu.meta.title === null || menu.meta.title === undefined) {
                  menu.meta.title = menu.name
                }
                // 默认icon
                if (menu.meta.icon === null || menu.meta.icon === undefined) {
                  menu.meta.icon = "defaultRouteIcon"
                }
                // 默认icon
                if (menu.path !== null && menu.path !== undefined && menu.path.startsWith("http")) {
                  menu.meta.isIframeMenu = true
                  menu.meta.iframeLinnk = menu.path
                  // 避免菜单名相同路由重复
                  menu.path = '/iframe/' + menu.name + "_" +menu.id;
                }
                // 菜单
                permission_routes.push(menu)

                break;
              default: // 按钮
                permission_buttons.push(item.permissionId)
                break;
            }
          })
        }
        // 路由先排序
        permission_routes.sort(function(a, b) {
          return (a.sortNum - b.sortNum);
        });
        // 路由数组转tree
        permission_routes = arrayToTree(permission_routes, null);
        // 设置到本地缓存
        LocalStorageUtil.setPermissionRoutes(permission_routes)
        LocalStorageUtil.setPermissionButtons(permission_buttons)

        resolve(data);
      }).catch(reason => reject());
    })
  },

  /**
   * 退出登录，清除用户缓存信息
   * @param commit
   * @param state
   * @returns {Promise<unknown>}
   */
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      LocalStorageUtil.cleanByLogout();
      resetRouter()
      resolve()
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
